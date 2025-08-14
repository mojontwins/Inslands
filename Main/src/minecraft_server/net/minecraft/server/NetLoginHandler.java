package net.minecraft.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Logger;

import net.minecraft.network.NetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet255KickDisconnect;
import net.minecraft.network.packet.Packet2Handshake;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.network.packet.Packet4UpdateTime;
import net.minecraft.network.packet.Packet6SpawnPosition;
import net.minecraft.network.packet.Packet93FiniteWorldSettings;
import net.minecraft.network.packet.Packet95UpdateDayOfTheYear;
import net.minecraft.network.packet.Packet99SetCreativeMode;
import net.minecraft.world.level.Seasons;
import net.minecraft.world.level.WorldSize;
import net.minecraft.world.level.chunk.ChunkCoordinates;
import net.minecraft.world.level.theme.LevelThemeGlobalSettings;

public class NetLoginHandler extends NetHandler {
	public static Logger logger = Logger.getLogger("Minecraft");
	private static Random rand = new Random();
	public NetworkManager netManager;
	public boolean finishedProcessing = false;
	private MinecraftServer mcServer;
	private int loginTimer = 0;
	private String username = null;
	private Packet1Login packet1login = null;
	private String serverId = "";

	public NetLoginHandler(MinecraftServer minecraftServer1, Socket socket2, String string3) throws IOException {
		this.mcServer = minecraftServer1;
		this.netManager = new NetworkManager(socket2, string3, this);
		this.netManager.chunkDataSendCounter = 0;
	}

	public void tryLogin() {
		if(this.packet1login != null) {
			this.doLogin(this.packet1login);
			this.packet1login = null;
		}

		if(this.loginTimer++ == 600) {
			this.kickUser("Took too long to log in");
		} else {
			this.netManager.processReadPackets();
		}

	}

	public void kickUser(String string1) {
		try {
			logger.info("Disconnecting " + this.getUserAndIPString() + ": " + string1);
			this.netManager.addToSendQueue(new Packet255KickDisconnect(string1));
			this.netManager.serverShutdown();
			this.finishedProcessing = true;
		} catch (Exception exception3) {
			exception3.printStackTrace();
		}

	}

	public void handleHandshake(Packet2Handshake packet2Handshake1) {
		if(this.mcServer.onlineMode) {
			this.serverId = Long.toHexString(rand.nextLong());
			this.netManager.addToSendQueue(new Packet2Handshake(this.serverId));
		} else {
			this.netManager.addToSendQueue(new Packet2Handshake("-"));
		}

	}

	public void handleLogin(Packet1Login packet1Login1) {
		this.username = packet1Login1.username;
		if(packet1Login1.protocolVersion != 14) {
			if(packet1Login1.protocolVersion > 14) {
				this.kickUser("Outdated server!");
			} else {
				this.kickUser("Outdated client!");
			}

		} else {
			if(!this.mcServer.onlineMode) {
				this.doLogin(packet1Login1);
			} else {
				(new ThreadLoginVerifier(this, packet1Login1)).start();
			}

		}
	}

	public void doLogin(Packet1Login packet1Login1) {
		EntityPlayerMP entityPlayerMP = this.mcServer.configManager.login(this, packet1Login1.username);
		if(entityPlayerMP != null) {
			this.mcServer.configManager.readPlayerDataFromFile(entityPlayerMP);
			entityPlayerMP.setWorldHandler(this.mcServer.getWorldManager(entityPlayerMP.dimension));
			
			logger.info(this.getUserAndIPString() + " logged in with entity id " + entityPlayerMP.entityId + " at (" + entityPlayerMP.posX + ", " + entityPlayerMP.posY + ", " + entityPlayerMP.posZ + ")");
			WorldServer worldServer = this.mcServer.getWorldManager(entityPlayerMP.dimension);
			ChunkCoordinates chunkCoordinates = worldServer.getSpawnPoint();
			NetServerHandler netServerHandler = new NetServerHandler(this.mcServer, this.netManager, entityPlayerMP);
			
			netServerHandler.sendPacket(new Packet1Login("", entityPlayerMP.entityId, worldServer.getRandomSeed(), (byte)worldServer.worldProvider.worldType));
			netServerHandler.sendPacket(new Packet6SpawnPosition(chunkCoordinates.posX, chunkCoordinates.posY, chunkCoordinates.posZ));
			netServerHandler.sendPacket(new Packet93FiniteWorldSettings(LevelThemeGlobalSettings.themeID, WorldSize.sizeID));
			
			this.mcServer.configManager.joinNewPlayerManager(entityPlayerMP, worldServer);
			this.mcServer.configManager.sendPacketToAllPlayers(new Packet3Chat("\u00a7e" + entityPlayerMP.username + " joined the game."));
			this.mcServer.configManager.playerLoggedIn(entityPlayerMP);
			
			netServerHandler.teleportTo(entityPlayerMP.posX, entityPlayerMP.posY, entityPlayerMP.posZ, entityPlayerMP.rotationYaw, entityPlayerMP.rotationPitch);
			netServerHandler.sendPacket(new Packet99SetCreativeMode(entityPlayerMP.isCreative));

			this.mcServer.networkServer.addPlayer(netServerHandler);
			netServerHandler.sendPacket(new Packet4UpdateTime(worldServer.getWorldTime()));
			netServerHandler.sendPacket(new Packet95UpdateDayOfTheYear(Seasons.dayOfTheYear));
			entityPlayerMP.sendUpdateTimeAndWeather();
		}

		this.finishedProcessing = true;
	}

	public void handleErrorMessage(String string1, Object[] object2) {
		logger.info(this.getUserAndIPString() + " lost connection");
		this.finishedProcessing = true;
	}

	public void registerPacket(Packet packet1) {
		this.kickUser("Protocol error");
	}

	public String getUserAndIPString() {
		return this.username != null ? this.username + " [" + this.netManager.getRemoteAddress().toString() + "]" : this.netManager.getRemoteAddress().toString();
	}

	public boolean isServerHandler() {
		return true;
	}

	static String getServerId(NetLoginHandler netLoginHandler0) {
		return netLoginHandler0.serverId;
	}

	static Packet1Login setLoginPacket(NetLoginHandler netLoginHandler0, Packet1Login packet1Login1) {
		return netLoginHandler0.packet1login = packet1Login1;
	}
}
