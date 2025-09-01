package net.minecraft.network;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.network.packet.Packet;

public class NetworkManager {
	public static final Object threadCounterLock = new Object();
	public static int numReadThreads;
	public static int numWriteThreads;
	private Object writeLock = new Object();

	private Socket socket;
	private final SocketAddress address;
	private DataInputStream dis;
	private DataOutputStream dos;
	private boolean running = true;
	private List<Packet> incoming = Collections.synchronizedList(new ArrayList<Packet>());
	private List<Packet> outgoing = Collections.synchronizedList(new ArrayList<Packet>());
	private List<Packet> outgoing_slow = Collections.synchronizedList(new ArrayList<Packet>());
	private NetHandler packetListener;
	private boolean quitting = false;
	private Thread writeThread;
	private Thread readThread;
	private boolean disconnected = false;
	private String disconnectReason = "";
	private Object[] disconnectReasonObjects;
	private int noInputTicks = 0;
	private int estimatedRemaining = 0;

	private int slowWriteDelay = 50;

	public NetworkManager(Socket socket1, String s, NetHandler netHandler3) throws IOException {
		this.socket = socket1;
		this.address = socket1.getRemoteSocketAddress();
		this.packetListener = netHandler3;

		try {
			socket1.setSoTimeout(30000);
			socket1.setTrafficClass(24);
		} catch (SocketException e) {
			System.err.println(e.getMessage());
		}

		this.dis = new DataInputStream(socket1.getInputStream());
		this.dos = new DataOutputStream(new BufferedOutputStream(socket1.getOutputStream(), 5120));
		this.readThread = new NetworkReaderThread(this, s + " read thread");
		this.writeThread = new NetworkWriterThread(this, s + " write thread");
		this.readThread.start();
		this.writeThread.start();
	}

	public void setNetHandler(NetHandler netHandler1) {
		this.packetListener = netHandler1;
	}

	public void addToSendQueue(Packet packet) {
		if(!this.quitting) {
			synchronized(this.writeLock) {
				this.estimatedRemaining += packet.getPacketSize() + 1;

				if(packet.shouldDelay) {
					this.outgoing_slow.add(packet);
				} else {
					this.outgoing.add(packet);
				}

			}
		}
	}

	private boolean writeTick() {
		boolean sent = false;

		try {
			Packet packet;

			if(!this.outgoing.isEmpty()) {
				synchronized(this.writeLock) {
					packet = (Packet)this.outgoing.remove(0);
					this.estimatedRemaining -= packet.getPacketSize() + 1;
				}

				Packet.writePacket(packet, this.dos);
				sent = true;
			}

			if(this.slowWriteDelay-- <= 0 && !this.outgoing_slow.isEmpty()) {
				synchronized(this.writeLock) {
					packet = (Packet)this.outgoing_slow.remove(0);
					this.estimatedRemaining -= packet.getPacketSize() + 1;
				}

				Packet.writePacket(packet, this.dos);
				this.slowWriteDelay = 0;
				sent = true;
			}

			return sent;
		} catch (Exception e) {
			if(!this.disconnected) {
				this.handleException(e);
			}

			return false;
		}
	}

	private boolean readTick() {
		boolean read = false;

		try {
			Packet packet = Packet.readPacket(this.dis, this.packetListener.isServerHandler());
			if(packet != null) {
				this.incoming.add(packet);
				read = true;
			} else {
				this.close("disconnect.endOfStream", new Object[0]);
			}

			return read;
		} catch (Exception e) {
			if(!this.disconnected) {
				this.handleException(e);
			}

			return false;
		}
	}

	private void handleException(Exception exception1) {
		exception1.printStackTrace();
		this.close("disconnect.genericReason", new Object[]{"Internal exception: " + exception1.toString()});
	}

	public void close(String disconnectReason, Object... disconnectReasonObjects) {
		if(this.running) {
			this.disconnected = true;
			this.disconnectReason = disconnectReason;
			this.disconnectReasonObjects = disconnectReasonObjects;
			(new NetworkMasterThread(this)).start();
			this.running = false;

			try {
				this.dis.close();
				this.dis = null;
			} catch (Throwable t) {
			}

			try {
				this.dos.close();
				this.dos = null;
			} catch (Throwable t) {
			}

			try {
				this.socket.close();
				this.socket = null;
			} catch (Throwable t) {
			}

		}
	}

	public void processReadPackets() {
		if(this.estimatedRemaining > 1048576) {
			this.close("disconnect.overflow", new Object[0]);
		}

		if(this.incoming.isEmpty()) {
			if(this.noInputTicks++ == 1200) {
				this.close("disconnect.timeout", new Object[0]);
			}
		} else {
			this.noInputTicks = 0;
		}

		int n = 100;

		while(!this.incoming.isEmpty() && n-- >= 0) {
			Packet packet = (Packet)this.incoming.remove(0);
			packet.processPacket(this.packetListener);
		}

		this.wakeThreads();
		if(this.disconnected && this.incoming.isEmpty()) {
			this.packetListener.handleErrorMessage(this.disconnectReason, this.disconnectReasonObjects);
		}

	}

	public SocketAddress getRemoteAddress() {
		return this.address;
	}

	public void serverShutdown() {
		this.wakeThreads();
		this.quitting = true;
		this.readThread.interrupt();
		(new ThreadMonitorConnection(this)).start();
	}

	public int countDelayedPackets() {
		return this.outgoing_slow.size();
	}

	public void wakeThreads() {
		this.readThread.interrupt();
		this.writeThread.interrupt();
	}

	public static boolean isRunning(NetworkManager netManager) {
		return netManager.running;
	}

	public static boolean isServerTerminating(NetworkManager netManager) {
		return netManager.quitting;
	}

	public static boolean readTick(NetworkManager netManager) {
		return netManager.readTick();
	}

	public static boolean writeTick(NetworkManager netManager) {
		return netManager.writeTick();
	}

	public static DataOutputStream getSocketOutputStream(NetworkManager netManager) {
		return netManager.dos;
	}

	public static boolean isTerminating(NetworkManager netManager) {
		return netManager.disconnected;
	}

	public static void handleException(NetworkManager netManager, Exception e) {
		netManager.handleException(e);
	}

	public static Thread getReadThread(NetworkManager netManager) {
		return netManager.readThread;
	}

	public static Thread getWriteThread(NetworkManager netManager) {
		return netManager.writeThread;
	}

	public SocketAddress getaddress() {
		return address;
	}
}
