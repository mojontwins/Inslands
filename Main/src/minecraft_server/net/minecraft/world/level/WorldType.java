package net.minecraft.world.level;

import net.minecraft.world.level.chunk.IChunkProvider;
import net.minecraft.world.level.levelgen.ChunkProviderCaves;
import net.minecraft.world.level.levelgen.ChunkProviderGenerate;
import net.minecraft.world.level.levelgen.ChunkProviderIndev;
import net.minecraft.world.level.levelgen.ChunkProviderInfdev;
import net.minecraft.world.level.levelgen.ChunkProviderSky;

public enum WorldType {
	INFDEV(0, "infdev", 1) {
		@Override
		public IChunkProvider getChunkGenerator(World world) {
			return new ChunkProviderInfdev(world, world.getRandomSeed(), world.getWorldInfo().isMapFeaturesEnabled(), world.getWorldInfo().isLayeredSand());
		}
	},
	FLAT(1, "flat", 0, false) {
		@Override
		public int getMinimumSpawnHeight(World world) {
			return 4;
		}

		@Override
		public double getHorizon(World world) {
			return 0.0D;
		}

		@Override
		public boolean hasVoidParticles(boolean hasVoidParticles) {
			return false;
		}

		@Override
		public double voidFadeMagnitude() {
			return 1.0D;
		}
	},
	SKY(2, "sky", 1) {
		@Override
		public IChunkProvider getChunkGenerator(World world) {
			return new ChunkProviderSky(world, world.getRandomSeed(), world.getWorldInfo().isMapFeaturesEnabled(), world.getWorldInfo().isLayeredSand());
		}

		@Override
		public boolean isIslandTerrain() {
			return true;
		}

		@Override
		public boolean hasSurfaceSea() {
			return false;
		}
	},
	DEFAULT(3, "default", 1),
	INDEV(4, "indev", 1) {
		@Override
		public IChunkProvider getChunkGenerator(World world) {
			return new ChunkProviderIndev(world, world.getRandomSeed(), world.getWorldInfo().isMapFeaturesEnabled(), world.getWorldInfo().isLayeredSand());
		}
	},
	CAVES(5, "caves", 1, false) {
		@Override
		public IChunkProvider getChunkGenerator(World world) {
			return new ChunkProviderCaves(world, world.getRandomSeed(), world.getWorldInfo().isMapFeaturesEnabled(), world.getWorldInfo().isLayeredSand());
		}

		@Override
		public int getSeaLevel(World world) {
			return 32;
		}
	};

	public static final WorldType[] worldTypes = new WorldType[16];

	static {
		for(WorldType worldType : values()) {
			worldTypes[worldType.id] = worldType;
		}
	}

	private final String worldType;
	private final int generatorVersion;
	private final boolean canBeCreated;
	public final int id;

	WorldType(int id, String name, int generatorVersion) {
		this(id, name, generatorVersion, true);
	}

	WorldType(int id, String name, int generatorVersion, boolean canBeCreated) {
		this.worldType = name;
		this.generatorVersion = generatorVersion;
		this.canBeCreated = canBeCreated;
		this.id = id;
	}

	public static int getIdByName(String worldType) {
		if(worldType != null && !"".equals(worldType)) {
			for(int i = 0; i < worldTypes.length; i ++) {
				if(worldTypes[i] != null && worldType.equals(worldTypes[i].worldType)) return i;
			}
		}

		return 0;
	}

	public String getWorldType() {
		return this.worldType;
	}

	public String getTranslateName() {
		return "generator." + this.worldType;
	}

	public int getGeneratorVersion() {
		return this.generatorVersion;
	}

	public boolean getCanBeCreated() {
		return this.canBeCreated;
	}

	public static WorldType parseWorldType(String string) {
		for(int i = 0; i < worldTypes.length; ++i) {
			if(worldTypes[i] != null && worldTypes[i].worldType.equalsIgnoreCase(string)) {
				return worldTypes[i];
			}
		}

		return null;
	}

	public static WorldType getById(int id) {
		return id >= 0 && id < worldTypes.length && worldTypes[id] != null ? worldTypes[id] : DEFAULT;
	}

	public WorldChunkManager getChunkManager(World world) {
		return new WorldChunkManager(world);
	}

	public IChunkProvider getChunkGenerator(World world) {
		return new ChunkProviderGenerate(world, world.getRandomSeed(), world.getWorldInfo().isMapFeaturesEnabled(), world.getWorldInfo().isLayeredSand());
	}

	public int getSeaLevel(World world) {
		return this.getMinimumSpawnHeight(world);
	}

	public int getMinimumSpawnHeight(World world) {
		return 64;
	}

	public double getHorizon(World world) {
		return 63.0D;
	}

	public boolean hasVoidParticles(boolean hasVoidParticles) {
		return !hasVoidParticles;
	}

	public double voidFadeMagnitude() {
		return 8.0D / 256D;
	}

	public boolean isIslandTerrain() {
		return false;
	}

	public boolean hasSurfaceSea() {
		return true;
	}

	public void onGUICreateWorldPress() {
	}

	public int getId() {
		return id;
	}
}
