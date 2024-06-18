package net.minecraft.server;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.src.AnvilSaveConverter;
import net.minecraft.src.AnvilSaveHandler;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.ConsoleCommandHandler;
import net.minecraft.src.ConsoleLogManager;
import net.minecraft.src.ConvertProgressUpdater;
import net.minecraft.src.EntityTracker;
import net.minecraft.src.ICommandListener;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.IServer;
import net.minecraft.src.IUpdatePlayerListBox;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NetworkListenThread;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet4UpdateTime;
import net.minecraft.src.PropertyManager;
import net.minecraft.src.RConConsoleSource;
import net.minecraft.src.RConThreadMain;
import net.minecraft.src.RConThreadQuery;
import net.minecraft.src.ServerCommand;
import net.minecraft.src.ServerConfigurationManager;
import net.minecraft.src.ServerGUI;
import net.minecraft.src.StatList;
import net.minecraft.src.ThreadCommandReader;
import net.minecraft.src.ThreadServerApplication;
import net.minecraft.src.ThreadServerSleep;
import net.minecraft.src.Vec3D;
import net.minecraft.src.WorldManager;
import net.minecraft.src.WorldServer;
import net.minecraft.src.WorldServerMulti;
import net.minecraft.src.WorldSettings;
import net.minecraft.src.WorldType;

public class MinecraftServer implements Runnable, ICommandListener, IServer {
	public static Logger logger = Logger.getLogger("Minecraft");
	public static HashMap field_6037_b = new HashMap();
	private String hostname;
	private int serverPort;
	public NetworkListenThread networkServer;
	public PropertyManager propertyManagerObj;
	public WorldServer[] worldMngr;
	public long[] field_40027_f = new long[100];
	public long[][] field_40028_g;
	public ServerConfigurationManager configManager;
	private ConsoleCommandHandler commandHandler;
	private boolean serverRunning = true;
	public boolean serverStopped = false;
	int deathTime = 0;
	public String currentTask;
	public int percentDone;
	private List playersOnline = new ArrayList();
	private List commands = Collections.synchronizedList(new ArrayList());
	public EntityTracker[] entityTracker = new EntityTracker[3];
	public boolean onlineMode;
	public boolean spawnPeacefulMobs;
	public boolean field_44002_p;
	public boolean pvpOn;
	public boolean allowFlight;
	public String motd;
	public int buildLimit;
	private long field_48074_E;
	private long field_48075_F;
	private long field_48076_G;
	private long field_48077_H;
	public long[] field_48080_u = new long[100];
	public long[] field_48079_v = new long[100];
	public long[] field_48078_w = new long[100];
	public long[] field_48082_x = new long[100];
	private RConThreadQuery rconQueryThread;
	private RConThreadMain rconMainThread;

	public MinecraftServer() {
		new ThreadServerSleep(this);
	}

	private boolean startServer() throws UnknownHostException {
		this.commandHandler = new ConsoleCommandHandler(this);
		ThreadCommandReader threadCommandReader1 = new ThreadCommandReader(this);
		threadCommandReader1.setDaemon(true);
		threadCommandReader1.start();
		ConsoleLogManager.init();
		logger.info("Starting minecraft server version 1.2.5");
		if(Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
			logger.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
		}

		logger.info("Loading properties");
		this.propertyManagerObj = new PropertyManager(new File("server.properties"));
		this.hostname = this.propertyManagerObj.getStringProperty("server-ip", "");
		this.onlineMode = this.propertyManagerObj.getBooleanProperty("online-mode", true);
		this.spawnPeacefulMobs = this.propertyManagerObj.getBooleanProperty("spawn-animals", true);
		this.field_44002_p = this.propertyManagerObj.getBooleanProperty("spawn-npcs", true);
		this.pvpOn = this.propertyManagerObj.getBooleanProperty("pvp", true);
		this.allowFlight = this.propertyManagerObj.getBooleanProperty("allow-flight", false);
		this.motd = this.propertyManagerObj.getStringProperty("motd", "A Minecraft Server");
		this.motd.replace('\u00a7', '$');
		InetAddress inetAddress2 = null;
		if(this.hostname.length() > 0) {
			inetAddress2 = InetAddress.getByName(this.hostname);
		}

		this.serverPort = this.propertyManagerObj.getIntProperty("server-port", 25565);
		logger.info("Starting Minecraft server on " + (this.hostname.length() == 0 ? "*" : this.hostname) + ":" + this.serverPort);

		try {
			this.networkServer = new NetworkListenThread(this, inetAddress2, this.serverPort);
		} catch (IOException iOException15) {
			logger.warning("**** FAILED TO BIND TO PORT!");
			logger.log(Level.WARNING, "The exception was: " + iOException15.toString());
			logger.warning("Perhaps a server is already running on that port?");
			return false;
		}

		if(!this.onlineMode) {
			logger.warning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
			logger.warning("The server will make no attempt to authenticate usernames. Beware.");
			logger.warning("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
			logger.warning("To change this, set \"online-mode\" to \"true\" in the server.settings file.");
		}

		this.configManager = new ServerConfigurationManager(this);
		this.entityTracker[0] = new EntityTracker(this, 0);
		this.entityTracker[1] = new EntityTracker(this, -1);
		this.entityTracker[2] = new EntityTracker(this, 1);
		long j3 = System.nanoTime();
		String string5 = this.propertyManagerObj.getStringProperty("level-name", "world");
		String string6 = this.propertyManagerObj.getStringProperty("level-seed", "");
		String string7 = this.propertyManagerObj.getStringProperty("level-type", "DEFAULT");
		long j8 = (new Random()).nextLong();
		if(string6.length() > 0) {
			try {
				long j10 = Long.parseLong(string6);
				if(j10 != 0L) {
					j8 = j10;
				}
			} catch (NumberFormatException numberFormatException14) {
				j8 = (long)string6.hashCode();
			}
		}

		WorldType worldType16 = WorldType.parseWorldType(string7);
		if(worldType16 == null) {
			worldType16 = WorldType.DEFAULT;
		}

		this.buildLimit = this.propertyManagerObj.getIntProperty("max-build-height", 256);
		this.buildLimit = (this.buildLimit + 8) / 16 * 16;
		this.buildLimit = MathHelper.clamp_int(this.buildLimit, 64, 256);
		this.propertyManagerObj.setProperty("max-build-height", this.buildLimit);
		logger.info("Preparing level \"" + string5 + "\"");
		this.initWorld(new AnvilSaveConverter(new File(".")), string5, j8, worldType16);
		long j11 = System.nanoTime() - j3;
		String string13 = String.format("%.3fs", new Object[]{(double)j11 / 1.0E9D});
		logger.info("Done (" + string13 + ")! For help, type \"help\" or \"?\"");
		if(this.propertyManagerObj.getBooleanProperty("enable-query", false)) {
			logger.info("Starting GS4 status listener");
			this.rconQueryThread = new RConThreadQuery(this);
			this.rconQueryThread.startThread();
		}

		if(this.propertyManagerObj.getBooleanProperty("enable-rcon", false)) {
			logger.info("Starting remote control listener");
			this.rconMainThread = new RConThreadMain(this);
			this.rconMainThread.startThread();
		}

		return true;
	}

	private void initWorld(ISaveFormat iSaveFormat1, String string2, long j3, WorldType worldType5) {
		if(iSaveFormat1.isOldMapFormat(string2)) {
			logger.info("Converting map!");
			iSaveFormat1.convertMapFormat(string2, new ConvertProgressUpdater(this));
		}

		this.worldMngr = new WorldServer[3];
		this.field_40028_g = new long[this.worldMngr.length][100];
		int i6 = this.propertyManagerObj.getIntProperty("gamemode", 0);
		i6 = WorldSettings.validGameType(i6);
		logger.info("Default game type: " + i6);
		boolean z7 = this.propertyManagerObj.getBooleanProperty("generate-structures", true);
		WorldSettings worldSettings8 = new WorldSettings(j3, i6, z7, false, worldType5);
		AnvilSaveHandler anvilSaveHandler9 = new AnvilSaveHandler(new File("."), string2, true);

		for(int i10 = 0; i10 < this.worldMngr.length; ++i10) {
			byte b11 = 0;
			if(i10 == 1) {
				b11 = -1;
			}

			if(i10 == 2) {
				b11 = 1;
			}

			if(i10 == 0) {
				this.worldMngr[i10] = new WorldServer(this, anvilSaveHandler9, string2, b11, worldSettings8);
			} else {
				this.worldMngr[i10] = new WorldServerMulti(this, anvilSaveHandler9, string2, b11, worldSettings8, this.worldMngr[0]);
			}

			this.worldMngr[i10].addWorldAccess(new WorldManager(this, this.worldMngr[i10]));
			this.worldMngr[i10].difficultySetting = this.propertyManagerObj.getIntProperty("difficulty", 1);
			this.worldMngr[i10].setAllowedSpawnTypes(this.propertyManagerObj.getBooleanProperty("spawn-monsters", true), this.spawnPeacefulMobs);
			this.worldMngr[i10].getWorldInfo().setGameType(i6);
			this.configManager.setPlayerManager(this.worldMngr);
		}

		short s22 = 196;
		long j23 = System.currentTimeMillis();

		for(int i13 = 0; i13 < 1; ++i13) {
			logger.info("Preparing start region for level " + i13);
			WorldServer worldServer14 = this.worldMngr[i13];
			ChunkCoordinates chunkCoordinates15 = worldServer14.getSpawnPoint();

			for(int i16 = -s22; i16 <= s22 && this.serverRunning; i16 += 16) {
				for(int i17 = -s22; i17 <= s22 && this.serverRunning; i17 += 16) {
					long j18 = System.currentTimeMillis();
					if(j18 < j23) {
						j23 = j18;
					}

					if(j18 > j23 + 1000L) {
						int i20 = (s22 * 2 + 1) * (s22 * 2 + 1);
						int i21 = (i16 + s22) * (s22 * 2 + 1) + i17 + 1;
						this.outputPercentRemaining("Preparing spawn area", i21 * 100 / i20);
						j23 = j18;
					}

					worldServer14.chunkProviderServer.loadChunk(chunkCoordinates15.posX + i16 >> 4, chunkCoordinates15.posZ + i17 >> 4);

					while(worldServer14.updatingLighting() && this.serverRunning) {
					}
				}
			}
		}

		this.clearCurrentTask();
	}

	private void outputPercentRemaining(String string1, int i2) {
		this.currentTask = string1;
		this.percentDone = i2;
		logger.info(string1 + ": " + i2 + "%");
	}

	private void clearCurrentTask() {
		this.currentTask = null;
		this.percentDone = 0;
	}

	private void saveServerWorld() {
		logger.info("Saving chunks");

		for(int i1 = 0; i1 < this.worldMngr.length; ++i1) {
			WorldServer worldServer2 = this.worldMngr[i1];
			worldServer2.saveWorld(true, (IProgressUpdate)null);
			worldServer2.func_30006_w();
		}

	}

	private void stopServer() {
		logger.info("Stopping server");
		if(this.configManager != null) {
			this.configManager.savePlayerStates();
		}

		for(int i1 = 0; i1 < this.worldMngr.length; ++i1) {
			WorldServer worldServer2 = this.worldMngr[i1];
			if(worldServer2 != null) {
				this.saveServerWorld();
			}
		}

	}

	public void initiateShutdown() {
		this.serverRunning = false;
	}

	public void run() {
		try {
			if(this.startServer()) {
				long j1 = System.currentTimeMillis();

				for(long j3 = 0L; this.serverRunning; Thread.sleep(1L)) {
					long j5 = System.currentTimeMillis();
					long j7 = j5 - j1;
					if(j7 > 2000L) {
						logger.warning("Can\'t keep up! Did the system time change, or is the server overloaded?");
						j7 = 2000L;
					}

					if(j7 < 0L) {
						logger.warning("Time ran backwards! Did the system time change?");
						j7 = 0L;
					}

					j3 += j7;
					j1 = j5;
					if(this.worldMngr[0].isAllPlayersFullyAsleep()) {
						this.doTick();
						j3 = 0L;
					} else {
						while(j3 > 50L) {
							j3 -= 50L;
							this.doTick();
						}
					}
				}
			} else {
				while(this.serverRunning) {
					this.commandLineParser();

					try {
						Thread.sleep(10L);
					} catch (InterruptedException interruptedException57) {
						interruptedException57.printStackTrace();
					}
				}
			}
		} catch (Throwable throwable58) {
			throwable58.printStackTrace();
			logger.log(Level.SEVERE, "Unexpected exception", throwable58);

			while(this.serverRunning) {
				this.commandLineParser();

				try {
					Thread.sleep(10L);
				} catch (InterruptedException interruptedException56) {
					interruptedException56.printStackTrace();
				}
			}
		} finally {
			try {
				this.stopServer();
				this.serverStopped = true;
			} catch (Throwable throwable54) {
				throwable54.printStackTrace();
			} finally {
				System.exit(0);
			}

		}

	}

	private void doTick() {
		long j1 = System.nanoTime();
		ArrayList arrayList3 = new ArrayList();
		Iterator iterator4 = field_6037_b.keySet().iterator();

		while(iterator4.hasNext()) {
			String string5 = (String)iterator4.next();
			int i6 = ((Integer)field_6037_b.get(string5)).intValue();
			if(i6 > 0) {
				field_6037_b.put(string5, i6 - 1);
			} else {
				arrayList3.add(string5);
			}
		}

		int i9;
		for(i9 = 0; i9 < arrayList3.size(); ++i9) {
			field_6037_b.remove(arrayList3.get(i9));
		}

		AxisAlignedBB.clearBoundingBoxPool();
		Vec3D.initialize();
		++this.deathTime;

		for(i9 = 0; i9 < this.worldMngr.length; ++i9) {
			long j10 = System.nanoTime();
			if(i9 == 0 || this.propertyManagerObj.getBooleanProperty("allow-nether", true)) {
				WorldServer worldServer7 = this.worldMngr[i9];
				if(this.deathTime % 20 == 0) {
					this.configManager.sendPacketToAllPlayersInDimension(new Packet4UpdateTime(worldServer7.getWorldTime()), worldServer7.worldProvider.worldType);
				}

				worldServer7.tick();

				while(true) {
					if(!worldServer7.updatingLighting()) {
						worldServer7.updateEntities();
						break;
					}
				}
			}

			this.field_40028_g[i9][this.deathTime % 100] = System.nanoTime() - j10;
		}

		this.networkServer.handleNetworkListenThread();
		this.configManager.onTick();

		for(i9 = 0; i9 < this.entityTracker.length; ++i9) {
			this.entityTracker[i9].updateTrackedEntities();
		}

		for(i9 = 0; i9 < this.playersOnline.size(); ++i9) {
			((IUpdatePlayerListBox)this.playersOnline.get(i9)).update();
		}

		try {
			this.commandLineParser();
		} catch (Exception exception8) {
			logger.log(Level.WARNING, "Unexpected exception while parsing console command", exception8);
		}

		this.field_40027_f[this.deathTime % 100] = System.nanoTime() - j1;
		this.field_48080_u[this.deathTime % 100] = Packet.field_48099_n - this.field_48074_E;
		this.field_48074_E = Packet.field_48099_n;
		this.field_48079_v[this.deathTime % 100] = Packet.field_48100_o - this.field_48075_F;
		this.field_48075_F = Packet.field_48100_o;
		this.field_48078_w[this.deathTime % 100] = Packet.field_48101_l - this.field_48076_G;
		this.field_48076_G = Packet.field_48101_l;
		this.field_48082_x[this.deathTime % 100] = Packet.field_48102_m - this.field_48077_H;
		this.field_48077_H = Packet.field_48102_m;
	}

	public void addCommand(String string1, ICommandListener iCommandListener2) {
		this.commands.add(new ServerCommand(string1, iCommandListener2));
	}

	public void commandLineParser() {
		while(this.commands.size() > 0) {
			ServerCommand serverCommand1 = (ServerCommand)this.commands.remove(0);
			this.commandHandler.handleCommand(serverCommand1);
		}

	}

	public void addToOnlinePlayerList(IUpdatePlayerListBox iUpdatePlayerListBox1) {
		this.playersOnline.add(iUpdatePlayerListBox1);
	}

	public static void main(String[] string0) {
		StatList.func_27092_a();

		try {
			MinecraftServer minecraftServer1 = new MinecraftServer();
			if(!GraphicsEnvironment.isHeadless() && (string0.length <= 0 || !string0[0].equals("nogui"))) {
				ServerGUI.initGui(minecraftServer1);
			}

			(new ThreadServerApplication("Server thread", minecraftServer1)).start();
		} catch (Exception exception2) {
			logger.log(Level.SEVERE, "Failed to start the minecraft server", exception2);
		}

	}

	public File getFile(String string1) {
		return new File(string1);
	}

	public void log(String string1) {
		logger.info(string1);
	}

	public void logWarning(String string1) {
		logger.warning(string1);
	}

	public String getUsername() {
		return "CONSOLE";
	}

	public WorldServer getWorldManager(int i1) {
		return i1 == -1 ? this.worldMngr[1] : (i1 == 1 ? this.worldMngr[2] : this.worldMngr[0]);
	}

	public EntityTracker getEntityTracker(int i1) {
		return i1 == -1 ? this.entityTracker[1] : (i1 == 1 ? this.entityTracker[2] : this.entityTracker[0]);
	}

	public int getIntProperty(String string1, int i2) {
		return this.propertyManagerObj.getIntProperty(string1, i2);
	}

	public String getStringProperty(String string1, String string2) {
		return this.propertyManagerObj.getStringProperty(string1, string2);
	}

	public void setProperty(String string1, Object object2) {
		this.propertyManagerObj.setProperty(string1, object2);
	}

	public void saveProperties() {
		this.propertyManagerObj.saveProperties();
	}

	public String getSettingsFilename() {
		File file1 = this.propertyManagerObj.getPropertiesFile();
		return file1 != null ? file1.getAbsolutePath() : "No settings file";
	}

	public String getHostname() {
		return this.hostname;
	}

	public int getPort() {
		return this.serverPort;
	}

	public String getMotd() {
		return this.motd;
	}

	public String getVersionString() {
		return "1.2.5";
	}

	public int playersOnline() {
		return this.configManager.playersOnline();
	}

	public int getMaxPlayers() {
		return this.configManager.getMaxPlayers();
	}

	public String[] getPlayerNamesAsList() {
		return this.configManager.getPlayerNamesAsList();
	}

	public String getWorldName() {
		return this.propertyManagerObj.getStringProperty("level-name", "world");
	}

	public String getPlugin() {
		return "";
	}

	public void func_40010_o() {
	}

	public String handleRConCommand(String string1) {
		RConConsoleSource.instance.resetLog();
		this.commandHandler.handleCommand(new ServerCommand(string1, RConConsoleSource.instance));
		return RConConsoleSource.instance.getLogContents();
	}

	public boolean isDebuggingEnabled() {
		return false;
	}

	public void logSevere(String string1) {
		logger.log(Level.SEVERE, string1);
	}

	public void logIn(String string1) {
		if(this.isDebuggingEnabled()) {
			logger.log(Level.INFO, string1);
		}

	}

	public String[] getBannedIPsList() {
		return (String[])this.configManager.getBannedIPsList().toArray(new String[0]);
	}

	public String[] getBannedPlayersList() {
		return (String[])this.configManager.getBannedPlayersList().toArray(new String[0]);
	}

	public String func_52003_getServerModName() {
		return "vanilla";
	}

	public static boolean isServerRunning(MinecraftServer minecraftServer0) {
		return minecraftServer0.serverRunning;
	}
}
