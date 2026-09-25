package net.minecraft.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.network.packet.Packet4UpdateTime;
import net.minecraft.network.packet.Packet70Bed;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.network.packet.Packet93FiniteWorldSettings;
import net.minecraft.network.packet.Packet95UpdateDayOfTheYear;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.WorldInfo;
import net.minecraft.world.level.WorldSize;
import net.minecraft.world.level.chunk.ChunkCoordinates;
import net.minecraft.world.level.chunk.storage.ISaveHandler;
import net.minecraft.world.level.dimension.Teleporter;
import net.minecraft.world.level.theme.LevelThemeGlobalSettings;
import net.minecraft.world.level.tile.entity.TileEntity;

public class ServerConfigurationManager {
	public static Logger logger = Logger.getLogger("Minecraft");
	public List<EntityPlayerMP> playerEntities = new ArrayList<EntityPlayerMP>();
	private MinecraftServer mcServer;
	private Map<Integer,PlayerManager> playerManagerObj = new HashMap<Integer,PlayerManager>();
	private final int playerManagerViewDistance;
	private int maxPlayers;
	private Set<String> bannedPlayers = new HashSet<String>();
	private Set<String> bannedIPs = new HashSet<String>();
	private Set<String> ops = new HashSet<String>();
	private Set<String> whiteListedIPs = new HashSet<String>();
	private File bannedPlayersFile;
	private File ipBanFile;
	private File opFile;
	private File whitelistPlayersFile;
	private ISaveHandler playerNBTManagerObj;
	private boolean whiteListEnforced;

	public ServerConfigurationManager(MinecraftServer minecraftServer1) {
		this.mcServer = minecraftServer1;
		this.bannedPlayersFile = minecraftServer1.getFile("banned-players.txt");
		this.ipBanFile = minecraftServer1.getFile("banned-ips.txt");
		this.opFile = minecraftServer1.getFile("ops.txt");
		this.whitelistPlayersFile = minecraftServer1.getFile("white-list.txt");
		int i2 = minecraftServer1.propertyManagerObj.getIntProperty("view-distance", 10);
		this.playerManagerViewDistance = i2;
		this.playerManagerObj.put(0, new PlayerManager(minecraftServer1, 0, i2));
		this.playerManagerObj.put(1, new PlayerManager(minecraftServer1, 1, i2));
		this.maxPlayers = minecraftServer1.propertyManagerObj.getIntProperty("max-players", 20);
		this.whiteListEnforced = minecraftServer1.propertyManagerObj.getBooleanProperty("white-list", false);
		this.readBannedPlayers();
		this.loadBannedList();
		this.loadOps();
		this.loadWhiteList();
		this.writeBannedPlayers();
		this.saveBannedList();
		this.saveOps();
		this.saveWhiteList();
	}

	public void setPlayerManager(WorldServer worldServer1) {
		this.playerNBTManagerObj = worldServer1.getWorldFile().getSaveHandler();
	}

	/**
	 * Makes sure a player manager exists for the given world. Called when a world
	 * is created (startup or lazily loaded); getPlayerManager() creates one on
	 * demand as a fallback so no lookup can miss.
	 */
	public void registerPlayerManager(WorldServer world) {
		int worldId = world.worldProvider.dimensionId;
		if(!this.playerManagerObj.containsKey(worldId)) {
			this.playerManagerObj.put(worldId, new PlayerManager(this.mcServer, worldId, this.playerManagerViewDistance));
		}
	}

	public void s_func_28172_a(EntityPlayerMP entityPlayerMP1) {
		this.getPlayerManager(entityPlayerMP1.dimension).removePlayer(entityPlayerMP1);
		this.getPlayerManager(entityPlayerMP1.dimension).addPlayer(entityPlayerMP1);
		WorldServer worldServer2 = this.mcServer.getWorldManager(entityPlayerMP1.dimension);
		worldServer2.chunkProviderServer.prepareChunk((int)entityPlayerMP1.posX >> 4, (int)entityPlayerMP1.posZ >> 4);
	}

	public int getMaxTrackingDistance() {
		return this.getPlayerManager(0).getMaxTrackingDistance();
	}

	private PlayerManager getPlayerManager(int i1) {
		if(i1 == -1) i1 = 1;
		PlayerManager playerManager2 = this.playerManagerObj.get(i1);
		if(playerManager2 == null) {
			playerManager2 = new PlayerManager(this.mcServer, i1, this.playerManagerViewDistance);
			this.playerManagerObj.put(i1, playerManager2);
		}
		return playerManager2;
	}

	public void readPlayerDataFromFile(EntityPlayerMP entityPlayerMP1) {
		this.playerNBTManagerObj.readPlayerData(entityPlayerMP1);
	}

	public void playerLoggedIn(EntityPlayerMP entityPlayerMP1) {
		this.playerEntities.add(entityPlayerMP1);
		WorldServer worldServer2 = this.mcServer.getWorldManager(entityPlayerMP1.dimension);
		worldServer2.chunkProviderServer.prepareChunk((int)entityPlayerMP1.posX >> 4, (int)entityPlayerMP1.posZ >> 4);

		while(worldServer2.getCollidingBoundingBoxes(entityPlayerMP1, entityPlayerMP1.boundingBox).size() != 0) {
			entityPlayerMP1.setPosition(entityPlayerMP1.posX, entityPlayerMP1.posY + 1.0D, entityPlayerMP1.posZ);
		}

		worldServer2.spawnEntityInWorld(entityPlayerMP1);
		this.getPlayerManager(entityPlayerMP1.dimension).addPlayer(entityPlayerMP1);
	}

	public void serverUpdateMountedMovingPlayer(EntityPlayerMP entityPlayerMP1) {
		this.getPlayerManager(entityPlayerMP1.dimension).updateMountedMovingPlayer(entityPlayerMP1);
	}

	public void playerLoggedOut(EntityPlayerMP entityPlayerMP1) {
		this.playerNBTManagerObj.writePlayerData(entityPlayerMP1);
		this.mcServer.getWorldManager(entityPlayerMP1.dimension).setEntityDead(entityPlayerMP1);
		this.playerEntities.remove(entityPlayerMP1);
		this.getPlayerManager(entityPlayerMP1.dimension).removePlayer(entityPlayerMP1);
	}

	public EntityPlayerMP login(NetLoginHandler netLoginHandler1, String string2) {
		if(this.bannedPlayers.contains(string2.trim().toLowerCase())) {
			netLoginHandler1.kickUser("You are banned from this server!");
			return null;
		} else if(!this.isAllowedToLogin(string2)) {
			netLoginHandler1.kickUser("You are not white-listed on this server!");
			return null;
		} else {
			String string3 = netLoginHandler1.netManager.getRemoteAddress().toString();
			string3 = string3.substring(string3.indexOf("/") + 1);
			string3 = string3.substring(0, string3.indexOf(":"));
			if(this.bannedIPs.contains(string3)) {
				netLoginHandler1.kickUser("Your IP address is banned from this server!");
				return null;
			} else if(this.playerEntities.size() >= this.maxPlayers) {
				netLoginHandler1.kickUser("The server is full!");
				return null;
			} else {
				for(int i4 = 0; i4 < this.playerEntities.size(); ++i4) {
					EntityPlayerMP entityPlayerMP5 = (EntityPlayerMP)this.playerEntities.get(i4);
					if(entityPlayerMP5.username.equalsIgnoreCase(string2)) {
						entityPlayerMP5.playerNetServerHandler.kickPlayer("You logged in from another location");
					}
				}

				return new EntityPlayerMP(this.mcServer, this.mcServer.getWorldManager(0), string2, new ItemInWorldManager(this.mcServer.getWorldManager(0)));
			}
		}
	}

	public EntityPlayerMP recreatePlayerEntity(EntityPlayerMP entityPlayerMP1, int i2) {
		this.mcServer.getEntityTracker(entityPlayerMP1.dimension).removeTrackedPlayerSymmetric(entityPlayerMP1);
		this.mcServer.getEntityTracker(entityPlayerMP1.dimension).untrackEntity(entityPlayerMP1);
		this.getPlayerManager(entityPlayerMP1.dimension).removePlayer(entityPlayerMP1);
		this.playerEntities.remove(entityPlayerMP1);
		this.mcServer.getWorldManager(entityPlayerMP1.dimension).removePlayer(entityPlayerMP1);
		ChunkCoordinates chunkCoordinates3 = entityPlayerMP1.getPlayerSpawnCoordinate();
		entityPlayerMP1.dimension = i2;
		EntityPlayerMP entityPlayerMP4 = new EntityPlayerMP(this.mcServer, this.mcServer.getWorldManager(entityPlayerMP1.dimension), entityPlayerMP1.username, new ItemInWorldManager(this.mcServer.getWorldManager(entityPlayerMP1.dimension)));
		entityPlayerMP4.entityId = entityPlayerMP1.entityId;
		entityPlayerMP4.playerNetServerHandler = entityPlayerMP1.playerNetServerHandler;
		WorldServer worldServer5 = this.mcServer.getWorldManager(entityPlayerMP1.dimension);
		if(chunkCoordinates3 != null) {
			ChunkCoordinates chunkCoordinates6 = null;
			if(entityPlayerMP1.isDontCheckSpawnCoordinates()) {
				chunkCoordinates6 = chunkCoordinates3;
			} else {
				chunkCoordinates6 = EntityPlayer.verifyRespawnCoordinates(this.mcServer.getWorldManager(entityPlayerMP1.dimension), chunkCoordinates3);
			}
			
			if(chunkCoordinates6 != null) {
				entityPlayerMP4.setLocationAndAngles((double)((float)chunkCoordinates6.posX + 0.5F), (double)((float)chunkCoordinates6.posY + 0.1F), (double)((float)chunkCoordinates6.posZ + 0.5F), 0.0F, 0.0F);
				entityPlayerMP4.setPlayerSpawnCoordinate(chunkCoordinates3);
			} else {
				entityPlayerMP4.playerNetServerHandler.sendPacket(new Packet70Bed(0));
			}
		}

		worldServer5.chunkProviderServer.prepareChunk((int)entityPlayerMP4.posX >> 4, (int)entityPlayerMP4.posZ >> 4);

		while(worldServer5.getCollidingBoundingBoxes(entityPlayerMP4, entityPlayerMP4.boundingBox).size() != 0) {
			entityPlayerMP4.setPosition(entityPlayerMP4.posX, entityPlayerMP4.posY + 1.0D, entityPlayerMP4.posZ);
		}

		entityPlayerMP4.playerNetServerHandler.sendPacket(new Packet9Respawn(entityPlayerMP4.dimension));
		entityPlayerMP4.playerNetServerHandler.teleportToNoEcho(entityPlayerMP4.posX, entityPlayerMP4.posY, entityPlayerMP4.posZ, entityPlayerMP4.rotationYaw, entityPlayerMP4.rotationPitch);
		this.joinNewPlayerManager(entityPlayerMP4, worldServer5);
		entityPlayerMP4.clearTerrainSyncQueue();
		this.getPlayerManager(entityPlayerMP4.dimension).addPlayer(entityPlayerMP4);
		worldServer5.spawnEntityInWorld(entityPlayerMP4);
		this.playerEntities.add(entityPlayerMP4);
		entityPlayerMP4.startTerrainSync(entityPlayerMP4.posX, entityPlayerMP4.posY, entityPlayerMP4.posZ, entityPlayerMP4.rotationYaw, entityPlayerMP4.rotationPitch);
		entityPlayerMP4.sendUpdateTimeAndWeather();
		entityPlayerMP4.s_func_22068_s();
		return entityPlayerMP4;
	}

	public void sendPlayerToOtherDimension(EntityPlayerMP entityPlayerMP1, int destId) {
		if(destId == -1) destId = 1;
		int sourceId = entityPlayerMP1.dimension;
		if(destId == sourceId) return;

		// Broken or stale linked-world destination: fall back to the main world.
		if(destId >= 2 && !new File(new File("."), "DIM-" + destId + File.separator + "level.dat").isFile()) {
			if(sourceId == 0) return;
			destId = 0;
		}

		boolean toNether = destId == 1;
		boolean fromNether = sourceId == 1;
		if(toNether) {
			// Remember where the player came from so leaving the nether returns there.
			entityPlayerMP1.netherReturnWorldId = sourceId;
		}

		WorldServer sourceWorld = this.mcServer.getWorldManager(sourceId);

		// Nether <-> world coordinate scaling (mirrors the single-player travel path).
		double d5 = entityPlayerMP1.posX;
		double d7 = entityPlayerMP1.posZ;
		if(fromNether) {
			if(WorldSize.xChunks >= 16 && WorldSize.zChunks >= 16) {
				d5 = (d5 - WorldSize.xChunks / 4) * 2;
				d7 = (d7 - WorldSize.zChunks / 4) * 2;
			}
		} else if(toNether) {
			if(WorldSize.xChunks >= 16 && WorldSize.zChunks >= 16) {
				d5 = WorldSize.xChunks / 4 + d5 / 2;
				d7 = WorldSize.zChunks / 4 + d7 / 2;
			} else {
				if(d5 == 0) d5 = 0;
				if(d5 >= WorldSize.xChunks - 1) d5 --;
				if(d7 == 0) d7 = 0;
				if(d7 >= WorldSize.zChunks - 1) d7 --;
			}
		}

		entityPlayerMP1.dimension = destId;
		WorldServer destWorld = this.mcServer.getWorldManager(destId);
		entityPlayerMP1.playerNetServerHandler.sendPacket(new Packet9Respawn(destId));
		sourceWorld.removePlayer(entityPlayerMP1);
		entityPlayerMP1.isDead = false;
		entityPlayerMP1.setLocationAndAngles(d5, entityPlayerMP1.posY, d7, entityPlayerMP1.rotationYaw, entityPlayerMP1.rotationPitch);

		if(entityPlayerMP1.isEntityAlive()) {
			// Apply the destination world's frozen theme/size so the globals agree
			// with it for the rest of this travel (teleporter bounds, spawning...).
			WorldInfo destInfo = destWorld.getWorldInfo();
			LevelThemeGlobalSettings.loadThemeById(destInfo.getThemeId());
			WorldSize.setSize(destInfo.getWorldWidthChunks(), destInfo.getWorldLengthChunks());

			boolean brandNew = !destInfo.isGenerated();
			if(brandNew) {
				// First visit: generate the whole level and build the return portal.
				this.mcServer.prepareWorldForEntry(destWorld, sourceId);
			}

			destWorld.spawnEntityInWorld(entityPlayerMP1);
			entityPlayerMP1.setLocationAndAngles(d5, entityPlayerMP1.posY, d7, entityPlayerMP1.rotationYaw, entityPlayerMP1.rotationPitch);
			destWorld.updateEntityWithOptionalForce(entityPlayerMP1, false);
			destWorld.chunkProviderServer.chunkLoadOverride = true;
			if(toNether || fromNether) {
				// Nether involved on either side: find/create the vanilla nether portal.
				(new Teleporter()).setExitLocation(destWorld, entityPlayerMP1);
			} else if(brandNew) {
				// First visit: stand the player on the return portal at the spawn point.
				destInfo = destWorld.getWorldInfo();
				entityPlayerMP1.setLocationAndAngles((double)destInfo.getSpawnX() + 0.5D, (double)(destInfo.getSpawnY() + 1), (double)destInfo.getSpawnZ() + 0.5D, entityPlayerMP1.rotationYaw, entityPlayerMP1.rotationPitch);
			} else {
				// Re-entry: land on the partner portal, or fall back to the natural spawn.
				int[] portal = (new Teleporter()).findWorldPortalTo(destWorld, sourceId);
				if(portal != null) {
					entityPlayerMP1.setLocationAndAngles((double)portal[0] + 0.5D, (double)(portal[1] + 1), (double)portal[2] + 0.5D, entityPlayerMP1.rotationYaw, entityPlayerMP1.rotationPitch);
				} else {
					destInfo = destWorld.getWorldInfo();
					entityPlayerMP1.setLocationAndAngles((double)destInfo.getSpawnX() + 0.5D, (double)(destInfo.getSpawnY() + 1), (double)destInfo.getSpawnZ() + 0.5D, entityPlayerMP1.rotationYaw, entityPlayerMP1.rotationPitch);
				}
			}
			destWorld.chunkProviderServer.chunkLoadOverride = false;
		}

		this.s_func_28172_a(entityPlayerMP1);
		entityPlayerMP1.playerNetServerHandler.teleportToNoEcho(entityPlayerMP1.posX, entityPlayerMP1.posY, entityPlayerMP1.posZ, entityPlayerMP1.rotationYaw, entityPlayerMP1.rotationPitch);
		entityPlayerMP1.setWorldHandler(destWorld);
		this.joinNewPlayerManager(entityPlayerMP1, destWorld);

		// Whole-island membership is per dimension: unregister from the source
		// island and register with the destination island, then dump it.
		this.getPlayerManager(sourceId).removePlayer(entityPlayerMP1);
		entityPlayerMP1.clearTerrainSyncQueue();
		this.getPlayerManager(destId).addPlayer(entityPlayerMP1);

		// Sync theme, size and day of year with the destination world.
		WorldInfo syncInfo = destWorld.getWorldInfo();
		entityPlayerMP1.playerNetServerHandler.sendPacket(new Packet93FiniteWorldSettings(syncInfo.getThemeId(), WorldSize.getSizeId(syncInfo.getWorldWidthChunks(), syncInfo.getWorldLengthChunks())));
		entityPlayerMP1.playerNetServerHandler.sendPacket(new Packet95UpdateDayOfTheYear(syncInfo.getDayOfTheYear()));

		entityPlayerMP1.startTerrainSync(entityPlayerMP1.posX, entityPlayerMP1.posY, entityPlayerMP1.posZ, entityPlayerMP1.rotationYaw, entityPlayerMP1.rotationPitch);
		this.s_func_30008_g(entityPlayerMP1);
	}

public void onTick() {
		for(PlayerManager playerManager1 : this.playerManagerObj.values()) {
			playerManager1.updatePlayerInstances();
		}

		// Fixed-size worlds: drive each player's island dump from the tick loop so
		// it always progresses, even while the client idles on "Downloading terrain".
		for(EntityPlayerMP player : this.playerEntities) {
			player.updateTerrainSync();
		}
	}

	public void markBlockNeedsUpdate(int i1, int i2, int i3, int i4) {
		this.getPlayerManager(i4).markBlockNeedsUpdate(i1, i2, i3);
	}

	public void sendPacketToAllPlayers(Packet packet1) {
		for(int i2 = 0; i2 < this.playerEntities.size(); ++i2) {
			EntityPlayerMP entityPlayerMP3 = (EntityPlayerMP)this.playerEntities.get(i2);
			entityPlayerMP3.playerNetServerHandler.sendPacket(packet1);
		}

	}

	public void sendPacketToAllPlayersInDimension(Packet packet1, int i2) {
		for(int i3 = 0; i3 < this.playerEntities.size(); ++i3) {
			EntityPlayerMP entityPlayerMP4 = (EntityPlayerMP)this.playerEntities.get(i3);
			if(entityPlayerMP4.dimension == i2) {
				entityPlayerMP4.playerNetServerHandler.sendPacket(packet1);
			}
		}

	}

	public String getPlayerList() {
		String string1 = "";

		for(int i2 = 0; i2 < this.playerEntities.size(); ++i2) {
			if(i2 > 0) {
				string1 = string1 + ", ";
			}

			string1 = string1 + ((EntityPlayerMP)this.playerEntities.get(i2)).username;
		}

		return string1;
	}

	public void banPlayer(String string1) {
		this.bannedPlayers.add(string1.toLowerCase());
		this.writeBannedPlayers();
	}

	public void pardonPlayer(String string1) {
		this.bannedPlayers.remove(string1.toLowerCase());
		this.writeBannedPlayers();
	}

	private void readBannedPlayers() {
		try {
			this.bannedPlayers.clear();
			BufferedReader bufferedReader1 = new BufferedReader(new FileReader(this.bannedPlayersFile));
			String string2 = "";

			while((string2 = bufferedReader1.readLine()) != null) {
				this.bannedPlayers.add(string2.trim().toLowerCase());
			}

			bufferedReader1.close();
		} catch (Exception exception3) {
			logger.warning("Failed to load ban list: " + exception3);
		}

	}

	private void writeBannedPlayers() {
		try {
			PrintWriter printWriter1 = new PrintWriter(new FileWriter(this.bannedPlayersFile, false));
			Iterator<String> iterator2 = this.bannedPlayers.iterator();

			while(iterator2.hasNext()) {
				String string3 = (String)iterator2.next();
				printWriter1.println(string3);
			}

			printWriter1.close();
		} catch (Exception exception4) {
			logger.warning("Failed to save ban list: " + exception4);
		}

	}

	public void banIP(String string1) {
		this.bannedIPs.add(string1.toLowerCase());
		this.saveBannedList();
	}

	public void pardonIP(String string1) {
		this.bannedIPs.remove(string1.toLowerCase());
		this.saveBannedList();
	}

	private void loadBannedList() {
		try {
			this.bannedIPs.clear();
			BufferedReader bufferedReader1 = new BufferedReader(new FileReader(this.ipBanFile));
			String string2 = "";

			while((string2 = bufferedReader1.readLine()) != null) {
				this.bannedIPs.add(string2.trim().toLowerCase());
			}

			bufferedReader1.close();
		} catch (Exception exception3) {
			logger.warning("Failed to load ip ban list: " + exception3);
		}

	}

	private void saveBannedList() {
		try {
			PrintWriter printWriter1 = new PrintWriter(new FileWriter(this.ipBanFile, false));
			Iterator<String> iterator2 = this.bannedIPs.iterator();

			while(iterator2.hasNext()) {
				String string3 = (String)iterator2.next();
				printWriter1.println(string3);
			}

			printWriter1.close();
		} catch (Exception exception4) {
			logger.warning("Failed to save ip ban list: " + exception4);
		}

	}

	public void opPlayer(String string1) {
		this.ops.add(string1.toLowerCase());
		this.saveOps();
	}

	public void deopPlayer(String string1) {
		this.ops.remove(string1.toLowerCase());
		this.saveOps();
	}

	private void loadOps() {
		try {
			this.ops.clear();
			BufferedReader bufferedReader1 = new BufferedReader(new FileReader(this.opFile));
			String string2 = "";

			while((string2 = bufferedReader1.readLine()) != null) {
				this.ops.add(string2.trim().toLowerCase());
			}

			bufferedReader1.close();
		} catch (Exception exception3) {
			logger.warning("Failed to load ip ban list: " + exception3);
		}

	}

	private void saveOps() {
		try {
			PrintWriter printWriter1 = new PrintWriter(new FileWriter(this.opFile, false));
			Iterator<String> iterator2 = this.ops.iterator();

			while(iterator2.hasNext()) {
				String string3 = (String)iterator2.next();
				printWriter1.println(string3);
			}

			printWriter1.close();
		} catch (Exception exception4) {
			logger.warning("Failed to save ip ban list: " + exception4);
		}

	}

	private void loadWhiteList() {
		try {
			this.whiteListedIPs.clear();
			BufferedReader bufferedReader1 = new BufferedReader(new FileReader(this.whitelistPlayersFile));
			String string2 = "";

			while((string2 = bufferedReader1.readLine()) != null) {
				this.whiteListedIPs.add(string2.trim().toLowerCase());
			}

			bufferedReader1.close();
		} catch (Exception exception3) {
			logger.warning("Failed to load white-list: " + exception3);
		}

	}

	private void saveWhiteList() {
		try {
			PrintWriter printWriter1 = new PrintWriter(new FileWriter(this.whitelistPlayersFile, false));
			Iterator<String> iterator2 = this.whiteListedIPs.iterator();

			while(iterator2.hasNext()) {
				String string3 = (String)iterator2.next();
				printWriter1.println(string3);
			}

			printWriter1.close();
		} catch (Exception exception4) {
			logger.warning("Failed to save white-list: " + exception4);
		}

	}

	public boolean isAllowedToLogin(String string1) {
		string1 = string1.trim().toLowerCase();
		return !this.whiteListEnforced || this.ops.contains(string1) || this.whiteListedIPs.contains(string1);
	}

	public boolean isOp(String string1) {
		return this.ops.contains(string1.trim().toLowerCase());
	}

	public EntityPlayerMP getPlayerEntity(String string1) {
		for(int i2 = 0; i2 < this.playerEntities.size(); ++i2) {
			EntityPlayerMP entityPlayerMP3 = (EntityPlayerMP)this.playerEntities.get(i2);
			if(entityPlayerMP3.username.equalsIgnoreCase(string1)) {
				return entityPlayerMP3;
			}
		}

		return null;
	}

	public void sendChatMessageToPlayer(String string1, String string2) {
		EntityPlayerMP entityPlayerMP3 = this.getPlayerEntity(string1);
		if(entityPlayerMP3 != null) {
			entityPlayerMP3.playerNetServerHandler.sendPacket(new Packet3Chat(string2));
		}

	}

	public void sendPacketToPlayersAroundPoint(double d1, double d3, double d5, double d7, int i9, Packet packet10) {
		this.s_func_28171_a((EntityPlayer)null, d1, d3, d5, d7, i9, packet10);
	}

	public void s_func_28171_a(EntityPlayer entityPlayer1, double d2, double d4, double d6, double d8, int i10, Packet packet11) {
		for(int i12 = 0; i12 < this.playerEntities.size(); ++i12) {
			EntityPlayerMP entityPlayerMP13 = (EntityPlayerMP)this.playerEntities.get(i12);
			if(entityPlayerMP13 != entityPlayer1 && entityPlayerMP13.dimension == i10) {
				double d14 = d2 - entityPlayerMP13.posX;
				double d16 = d4 - entityPlayerMP13.posY;
				double d18 = d6 - entityPlayerMP13.posZ;
				if(d14 * d14 + d16 * d16 + d18 * d18 < d8 * d8) {
					entityPlayerMP13.playerNetServerHandler.sendPacket(packet11);
				}
			}
		}

	}

	public void sendChatMessageToAllOps(String string1) {
		Packet3Chat packet3Chat2 = new Packet3Chat(string1);

		for(int i3 = 0; i3 < this.playerEntities.size(); ++i3) {
			EntityPlayerMP entityPlayerMP4 = (EntityPlayerMP)this.playerEntities.get(i3);
			if(this.isOp(entityPlayerMP4.username)) {
				entityPlayerMP4.playerNetServerHandler.sendPacket(packet3Chat2);
			}
		}

	}

	public boolean sendPacketToPlayer(String string1, Packet packet2) {
		EntityPlayerMP entityPlayerMP3 = this.getPlayerEntity(string1);
		if(entityPlayerMP3 != null) {
			entityPlayerMP3.playerNetServerHandler.sendPacket(packet2);
			return true;
		} else {
			return false;
		}
	}

	public void savePlayerStates() {
		for(int i1 = 0; i1 < this.playerEntities.size(); ++i1) {
			this.playerNBTManagerObj.writePlayerData((EntityPlayer)this.playerEntities.get(i1));
		}

	}

	public void sentTileEntityToPlayer(int i1, int i2, int i3, TileEntity tileEntity4) {
	}

	public void addToWhiteList(String string1) {
		this.whiteListedIPs.add(string1);
		this.saveWhiteList();
	}

	public void removeFromWhiteList(String string1) {
		this.whiteListedIPs.remove(string1);
		this.saveWhiteList();
	}

	public Set<String> getWhiteListedIPs() {
		return this.whiteListedIPs;
	}

	public void reloadWhiteList() {
		this.loadWhiteList();
	}

	public void joinNewPlayerManager(EntityPlayerMP entityPlayerMP1, WorldServer worldServer2) {
		entityPlayerMP1.playerNetServerHandler.sendPacket(new Packet4UpdateTime(worldServer2.getWorldTime()));
		entityPlayerMP1.playerNetServerHandler.sendPacket(new Packet70Bed(worldServer2.worldInfo.getRaining(), worldServer2.worldInfo.getSnowing(), worldServer2.worldInfo.getThundering()));
	}

	public void s_func_30008_g(EntityPlayerMP entityPlayerMP1) {
		entityPlayerMP1.s_func_28017_a(entityPlayerMP1.inventorySlots);
		entityPlayerMP1.s_func_30001_B();
	}
	
	public void sendChatMessageToAllPlayers(String string1) {
		Packet3Chat packet3Chat2 = new Packet3Chat(string1);

		for(int i3 = 0; i3 < this.playerEntities.size(); ++i3) {
			((EntityPlayerMP)this.playerEntities.get(i3)).playerNetServerHandler.sendPacket(packet3Chat2);
		}

	}	
	
	public int getMaxConnectedPlayers() {
		return this.playerEntities.size();
	}
}
