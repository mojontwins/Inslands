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
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.network.packet.Packet4UpdateTime;
import net.minecraft.network.packet.Packet95UpdateDayOfTheYear;
import net.minecraft.world.GlobalVars;
import net.minecraft.world.Version;
import net.minecraft.world.level.Seasons;
import net.minecraft.world.level.WorldInfo;
import net.minecraft.world.level.WorldSettings;
import net.minecraft.world.level.WorldSize;
import net.minecraft.world.level.WorldType;
import net.minecraft.world.level.PortalRegistry;
import net.minecraft.world.level.WorldNameGen;
import net.minecraft.world.level.biome.BiomeGenBase;
import net.minecraft.world.level.chunk.storage.IProgressUpdate;
import net.minecraft.world.level.chunk.storage.ISaveFormat;
import net.minecraft.world.level.chunk.storage.SaveConverterMcRegion;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.chunk.storage.SaveOldDir;
import net.minecraft.world.level.theme.LevelThemeGlobalSettings;
import net.minecraft.world.level.theme.LevelThemeSettings;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.stats.StatList;

public class MinecraftServer implements Runnable, ICommandListener {
	public static Logger logger = Logger.getLogger("Minecraft");
	public static HashMap<String,Integer> s_field_6037_b = new HashMap<String,Integer>();
	public NetworkListenThread networkServer;
	public PropertyManager propertyManagerObj;
	public Map<Integer,WorldServer> worldMngr = new HashMap<Integer,WorldServer>();
	public ServerConfigurationManager configManager;
	private ConsoleCommandHandler commandHandler;
	private boolean serverRunning = true;
	public boolean serverStopped = false;
	int deathTime = 0;
	public String currentTask;
	public int percentDone;
	private List<IUpdatePlayerListBox> playersOnline = new ArrayList<IUpdatePlayerListBox>();
	private List<ServerCommand> commands = Collections.synchronizedList(new ArrayList<ServerCommand>());
	public Map<Integer,EntityTracker> entityTracker = new HashMap<Integer,EntityTracker>();
	public boolean onlineMode;
	public boolean spawnPeacefulMobs;
	public boolean pvpOn;
	public boolean allowFlight;

	public MinecraftServer() {
		new ThreadSleepForeverServer(this);
	}

	private boolean startServer() throws UnknownHostException {
		this.commandHandler = new ConsoleCommandHandler(this);
		ThreadCommandReader threadCommandReader1 = new ThreadCommandReader(this);
		threadCommandReader1.setDaemon(true);
		threadCommandReader1.start();
		ConsoleLogManager.init();
		logger.info("Starting minecraft server " + Version.getVersion());
		if(Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
			logger.warning("**** NOT ENOUGH RAM!");
			logger.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
		}

		logger.info("Loading properties");
		this.propertyManagerObj = new PropertyManager(new File("server.properties"));
		String string2 = this.propertyManagerObj.getStringProperty("server-ip", "");
		this.onlineMode = this.propertyManagerObj.getBooleanProperty("online-mode", true);
		this.spawnPeacefulMobs = this.propertyManagerObj.getBooleanProperty("spawn-animals", true);
		this.pvpOn = this.propertyManagerObj.getBooleanProperty("pvp", true);
		this.allowFlight = this.propertyManagerObj.getBooleanProperty("allow-flight", false);
		
		// Level size
		WorldSize.setSizeByName(this.propertyManagerObj.getStringProperty("world-size", "normal"));
		
		// Level theme
		LevelThemeGlobalSettings.loadThemeById(LevelThemeSettings.findThemeByName(this.propertyManagerObj.getStringProperty("world-theme", "normal")).id);
		
		InetAddress inetAddress3 = null;
		if(string2.length() > 0) {
			inetAddress3 = InetAddress.getByName(string2);
		}

		int i4 = this.propertyManagerObj.getIntProperty("server-port", 25565);
		logger.info("Starting Minecraft server on " + (string2.length() == 0 ? "*" : string2) + ":" + i4);

		try {
			this.networkServer = new NetworkListenThread(this, inetAddress3, i4);
		} catch (IOException iOException13) {
			logger.warning("**** FAILED TO BIND TO PORT!");
			logger.log(Level.WARNING, "The exception was: " + iOException13.toString());
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
		this.entityTracker.clear();
		this.entityTracker.put(0, new EntityTracker(this, 0));
		this.entityTracker.put(1, new EntityTracker(this, 1));
		long j5 = System.nanoTime();
		String string7 = this.propertyManagerObj.getStringProperty("level-name", "world");
		String string8 = this.propertyManagerObj.getStringProperty("level-seed", "");
		String string9 = this.propertyManagerObj.getStringProperty("level-type", "DEFAULT");
		
		long j9 = (new Random()).nextLong();
		if(string8.length() > 0) {
			try {
				j9 = Long.parseLong(string8);
			} catch (NumberFormatException numberFormatException12) {
				j9 = (long)string8.hashCode();
			}
		}

		WorldType worldType16 = WorldType.parseWorldType(string9);
		if(worldType16 == null) {
			worldType16 = WorldType.DEFAULT;
		}
		
		logger.info("Preparing level \"" + string7 + "\"");
		this.initWorld(new SaveConverterMcRegion(new File(".")), string7, j9, worldType16);
		logger.info("Done (" + (System.nanoTime() - j5) + "ns)! For help, type \"help\" or \"?\"");
		return true;
	}
	
	/**
	 * Forces the whole level into memory before the server starts ticking.
	 * <p>
	 * A brand-new overworld or nether is generated and lit in one bulk pass (see
	 * {@code ChunkProviderServer.generateWholeWorld}) and then saved once. Existing worlds
	 * keep the original chunk-by-chunk preload. The nether's post-generation (indev house,
	 * spawn point) is intentionally skipped — it has no overworld-style spawn.
	 */
	private void preloadWorld(WorldServer world, boolean isNew) {
		if(isNew && world.worldProvider.worldType == 0) {
			// Overworld: bulk generation + spawn placement FIRST (so the first
			// level.dat already carries a valid spawn, never 0,0,0) + single full save.
			this.outputPercentRemaining("Building terrain", 0);
			world.chunkProviderServer.generateWholeWorld(new ConvertProgressUpdater(this));
			// Theme-specific post generation runs at the end of generateWholeWorld.
			// The nether's setInitialSpawnLocation would build an indev house there,
			// which is never wanted.  canRespawnHere() == false already prevents regular respawns.
			world.worldProvider.setInitialSpawnLocation(world);
			this.outputPercentRemaining("Saving level", 0);
			world.saveWorld(true, new ConvertProgressUpdater(this));
		} else if(isNew && world.worldProvider.worldType == -1) {
			// Nether: bulk generation + single full save of DIM-1. No spawn, no post-gen.
			this.outputPercentRemaining("Building nether", 0);
			world.chunkProviderServer.generateWholeWorld(new ConvertProgressUpdater(this));
			this.outputPercentRemaining("Saving nether", 0);
			world.saveWorld(true, new ConvertProgressUpdater(this));
		} else {
			// Legacy chunk-by-chunk (existing worlds, sky if ever preloaded).
			int chunksLoaded = 0;
			long prevTime = System.currentTimeMillis();

			for(int chunkX = 0; chunkX < WorldSize.xChunks; chunkX ++) {
				for(int chunkZ = 0; chunkZ < WorldSize.zChunks; chunkZ ++) {
					long curTime = System.currentTimeMillis();
					if(curTime > prevTime + 1000L) {
						prevTime = curTime;
						this.outputPercentRemaining("Preparing spawn area", chunksLoaded * 100 / WorldSize.getTotalChunks());
					}
					chunksLoaded++;
					world.chunkProviderServer.prepareChunk(chunkX, chunkZ);
				}
			}
		}
	}

	private void initWorld(ISaveFormat saveHandler, String folderName, long seed, WorldType worldType) {
		BiomeGenBase.generateBiomeLookup();

		this.worldMngr.clear();
		this.entityTracker.clear();
		boolean generateStructures = this.propertyManagerObj.getBooleanProperty("generate-structures", true);
		boolean layeredSand = this.propertyManagerObj.getBooleanProperty("layered-sand", true);

		SaveOldDir saveOldDir = new SaveOldDir(new File("."), folderName, true);
		WorldSettings settings = new WorldSettings(seed, 0, generateStructures, false, false, layeredSand, worldType);

		int[] worldIds = new int[] { 0, 1 };
		for(int i = 0; i < worldIds.length; ++i) {
			int worldId = worldIds[i];
			logger.info("** DIM " + worldId);
			GlobalVars.initializeGameFlags();

			WorldServer world;
			if(worldId == 0) {
				world = new WorldServer(this, saveOldDir, folderName, 0, settings);
			} else {
				world = new WorldServerMulti(this, saveOldDir, folderName, 1, settings, this.worldMngr.get(0));
			}

			this.worldMngr.put(worldId, world);
			this.entityTracker.put(worldId, new EntityTracker(this, worldId));

			world.addWorldAccess(new WorldManager(this, world));
			world.difficultySetting = this.propertyManagerObj.getBooleanProperty("spawn-monsters", true) ? 1 : 0;
			world.setAllowedMobSpawns(this.propertyManagerObj.getBooleanProperty("spawn-monsters", true), this.spawnPeacefulMobs);

			// Check if valid
			boolean newWorld = world.isNewWorld;

			// The nether (DIM-1) shares the world folder: it is "new" exactly when the
			// overworld was just created, so propagate the flag.
			if(worldId == 1) newWorld = this.worldMngr.get(0).isNewWorld;
			if(newWorld) {
				logger.info("Generating new world");
			} else {
				logger.info("Loading existing world");
			}

			// Pregenerate/preload all level
			this.preloadWorld(world, newWorld);
		}

		this.configManager.setPlayerManager(getWorldManager(0));
		this.configManager.registerPlayerManager(getWorldManager(0));
		this.configManager.registerPlayerManager(getWorldManager(1));

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

		for(WorldServer worldServer2 : this.worldMngr.values()) {
			if(worldServer2 != null) {
				worldServer2.saveWorld(true, (IProgressUpdate)null);
				worldServer2.s_func_30006_w();
			}
		}

	}

	private void stopServer() {
		logger.info("Stopping server");
		if(this.configManager != null) {
			this.configManager.savePlayerStates();
		}

		this.saveServerWorld();
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
					if(this.getWorldManager(0).isAllPlayersFullyAsleep()) {
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
		ArrayList<String> arrayList1 = new ArrayList<String>();
		Iterator<String> iterator2 = s_field_6037_b.keySet().iterator();

		while(iterator2.hasNext()) {
			String string3 = (String)iterator2.next();
			int i4 = ((Integer)s_field_6037_b.get(string3)).intValue();
			if(i4 > 0) {
				s_field_6037_b.put(string3, i4 - 1);
			} else {
				arrayList1.add(string3);
			}
		}

		int i6;
		for(i6 = 0; i6 < arrayList1.size(); ++i6) {
			s_field_6037_b.remove(arrayList1.get(i6));
		}

		AxisAlignedBB.clearBoundingBoxPool();
		Vec3D.initialize();
		++this.deathTime;

		long tWorldTick = 0L;
		long tWorldEnt = 0L;
		long tNet = 0L;
		long tCfg = 0L;
		long tTrack = 0L;
		long tPlayers = 0L;
		long sectionStart = System.currentTimeMillis();
		long lastSlow = 0L;
		long now;
		boolean slow = false;
		long tWorldMark;
		int entCount = 0;
		int activeEntCount = 0;
		int tileCount = 0;
		int livingEntCount = 0;
		int itemEntCount = 0;
		int otherEntCount = 0;
		long slowEntNanos = 0L;
		String slowEntName = "";

		for(WorldServer worldServer7 : this.worldMngr.values()) {
			if(worldServer7 == null) continue;
			int worldId = worldServer7.worldProvider.dimensionId;
			if(worldId != 0 && !this.propertyManagerObj.getBooleanProperty("allow-nether", true)) continue;

			// Re-apply this world's theme and size so the per-world settings drive the globals.
			WorldInfo worldInfo = worldServer7.getWorldInfo();
			if(worldInfo != null) {
				LevelThemeGlobalSettings.loadThemeById(worldInfo.getThemeId());
				WorldSize.setSize(worldInfo.getWorldWidthChunks(), worldInfo.getWorldLengthChunks());
			}

			// Idle linked worlds (no players in them) are kept loaded but paused:
			// no tick, no entity updates, no time/day/weather broadcasts. They are
			// still periodically saved so a crash does not lose recent travel.
			boolean worldIsIdle = worldId != 0 && worldId != 1 && (worldServer7.playerEntities == null || worldServer7.playerEntities.size() == 0);
			if(worldIsIdle) {
				if(this.deathTime % 1200 == 0) {
					worldServer7.saveWorld(false, (IProgressUpdate)null);
					worldServer7.s_func_30006_w();
				}
				continue;
			}

			if(this.deathTime % 20 == 0) {
				this.configManager.sendPacketToAllPlayersInDimension(new Packet4UpdateTime(worldServer7.getWorldTime()), worldId);
			}

			tWorldMark = System.currentTimeMillis();
			int dayOfTheYear = worldInfo != null ? worldInfo.getDayOfTheYear() : Seasons.dayOfTheYear;
			worldServer7.tick();
			if(worldInfo != null && worldInfo.getDayOfTheYear() != dayOfTheYear) {
				this.configManager.sendPacketToAllPlayersInDimension(new Packet95UpdateDayOfTheYear(worldInfo.getDayOfTheYear()), worldId);
			}
			tWorldTick += System.currentTimeMillis() - tWorldMark;

			tWorldMark = System.currentTimeMillis();
			worldServer7.updateEntities();
			tWorldEnt += System.currentTimeMillis() - tWorldMark;
			entCount += worldServer7.loadedEntityList.size();
			activeEntCount += worldServer7.updatedEntities;
			livingEntCount += worldServer7.cachedLivingCount;
			itemEntCount += worldServer7.cachedItemCount;
			otherEntCount += worldServer7.cachedOtherCount;
			tileCount += worldServer7.loadedTileEntityList.size();
			if(worldServer7.slowestEntityNanos > slowEntNanos) {
				slowEntNanos = worldServer7.slowestEntityNanos;
				slowEntName = worldServer7.slowestEntityName;
			}
		}

		tWorldMark = System.currentTimeMillis();
		this.networkServer.handleNetworkListenThread();
		tNet += System.currentTimeMillis() - tWorldMark;

		tWorldMark = System.currentTimeMillis();
		this.configManager.onTick();
		tCfg += System.currentTimeMillis() - tWorldMark;

		tWorldMark = System.currentTimeMillis();
		for(EntityTracker entityTracker2 : this.entityTracker.values()) {
			if(entityTracker2 != null) {
				entityTracker2.updateTrackedEntities();
			}
		}
		tTrack += System.currentTimeMillis() - tWorldMark;

		tWorldMark = System.currentTimeMillis();
		for(i6 = 0; i6 < this.playersOnline.size(); ++i6) {
			((IUpdatePlayerListBox)this.playersOnline.get(i6)).update();
		}
		tPlayers += System.currentTimeMillis() - tWorldMark;

		now = System.currentTimeMillis();
		long totalTick = now - sectionStart;
		slow = totalTick > 49L || tWorldTick > 25L || tWorldEnt > 25L || tNet > 25L || tCfg > 25L || tTrack > 25L || tPlayers > 25L;

		try {
			this.commandLineParser();
		} catch (Exception exception5) {
			logger.log(Level.WARNING, "Unexpected exception while parsing console command", exception5);
		}

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
		StatList.preInit();

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
		if(i1 == -1) i1 = 1;
		WorldServer world = this.worldMngr.get(i1);
		if(world != null) return world;
		return this.loadWorld(i1);
	}

	/**
	 * Lazily loads a linked world (2..255) into memory and registers its entity
	 * tracker and player manager. The world is NOT generated or preloaded here —
	 * that only happens on a player's first visit (see prepareWorldForEntry).
	 */
	private WorldServer loadWorld(int worldId) {
		if(worldId != 0 && worldId != 1 && !PortalRegistry.isLinkedWorldId(worldId)) {
			logger.warning("Invalid world id " + worldId + ", redirecting to the main world");
			worldId = 0;
		}
		WorldServer existing = this.worldMngr.get(worldId);
		if(existing != null || worldId == 0 || worldId == 1) {
			return existing != null ? existing : this.worldMngr.get(0);
		}

		WorldServer mainWorld = this.worldMngr.get(0);
		if(mainWorld == null) return null;

		long baseSeed = PortalRegistry.getBaseSeed(mainWorld);
		String name = WorldNameGen.getName(baseSeed, worldId);
		WorldSettings settings = new WorldSettings(WorldNameGen.deriveSeed(baseSeed, worldId), 0,
				this.propertyManagerObj.getBooleanProperty("generate-structures", true), false,
				this.propertyManagerObj.getBooleanProperty("allow-nether", true),
				mainWorld.getWorldInfo().isLayeredSand(), WorldType.DEFAULT);

		// Linked worlds live in their own DIM-<id> save folder with their own level.dat.
		// The folder name already addresses the world, so the provider must not nest again
		// (SaveOldDir skips the provider folder when it matches its own directory name).
		logger.info("Loading DIM " + worldId + " (" + name + ")");
		GlobalVars.initializeGameFlags();
		SaveOldDir handler = new SaveOldDir(new File("."), "DIM-" + worldId, true);
		WorldServer world = new WorldServerMulti(this, handler, name, worldId, settings, mainWorld);
		WorldInfo worldInfo = world.getWorldInfo();
		if(worldInfo != null) {
			worldInfo.setDimension(worldId);
		}
		// Route packets, entity tracking and player-manager lookups by the world id.
		world.worldProvider.dimensionId = worldId;

		this.entityTracker.put(worldId, new EntityTracker(this, worldId));
		this.configManager.registerPlayerManager(world);
		this.worldMngr.put(worldId, world);

		world.addWorldAccess(new WorldManager(this, world));
		world.difficultySetting = this.propertyManagerObj.getBooleanProperty("spawn-monsters", true) ? 1 : 0;
		world.setAllowedMobSpawns(this.propertyManagerObj.getBooleanProperty("spawn-monsters", true), this.spawnPeacefulMobs);
		return world;
	}

	/**
	 * First-visit step for a linked world: apply the world's frozen theme/size,
	 * generate the whole level, place the overworld-style spawn, build the return
	 * portal to the source world on the spawn tile, mark the world generated and
	 * save it. Mirrors the single-player travel path (Minecraft.travelToDimension).
	 */
	public void prepareWorldForEntry(WorldServer world, int sourceId) {
		WorldInfo worldInfo = world.getWorldInfo();
		LevelThemeGlobalSettings.loadThemeById(worldInfo.getThemeId());
		WorldSize.setSize(worldInfo.getWorldWidthChunks(), worldInfo.getWorldLengthChunks());
		world.chunkProviderServer.generateWholeWorld(new ConvertProgressUpdater(this));

		logger.info("Finding spawn point for DIM " + world.worldProvider.dimensionId + " (" + worldInfo.getWorldName() + ")");
		world.worldProvider.setInitialSpawnLocation(world);

		// Build the return portal on the spawn tile; keep the landing tile clear.
		int sx = worldInfo.getSpawnX();
		int sy = worldInfo.getSpawnY();
		int sz = worldInfo.getSpawnZ();
		if(sy <= 1 || sy >= 127) sy = 64;
		world.setBlockAndMetadataWithNotify(sx, sy, sz, Block.worldPortal.blockID, sourceId);
		world.setBlockWithNotify(sx, sy + 1, sz, 0);

		worldInfo.setSpawn(sx, sy + 1, sz);
		worldInfo.setGenerated(true);
		world.saveWorld(true, (IProgressUpdate)null);
	}

	public EntityTracker getEntityTracker(int i1) {
		if(i1 == -1) i1 = 1;
		return this.entityTracker.get(i1);
	}

	public static boolean isServerRunning(MinecraftServer minecraftServer0) {
		return minecraftServer0.serverRunning;
	}
}
