package net.minecraft.src;

import com.mojang.minecraft.indev.ChunkProviderIndev;
import com.mojang.minecraft.infdev.ChunkProviderInfdev;

public class WorldType {
	public static final WorldType[] worldTypes = new WorldType[16];
	public static final WorldType DEFAULT = (new WorldType(3, "default", 1));
	public static final WorldType FLAT = (new WorldType(1, "flat")).setCanBeCreated(false);
	public static final WorldType SKY = (new WorldType(2, "sky", 1));
	public static final WorldType INFDEV = (new WorldType(0, "infdev", 1));
	public static final WorldType INDEV = (new WorldType(4, "indev", 1));

	private final String worldType;
	private final int generatorVersion;
	private boolean canBeCreated;
	public final int id;

	protected WorldType(int id, String name) {
		this(id, name, 0);
	}

	protected WorldType(int id, String name, int generatorVersion) {
		this.worldType = name;
		this.generatorVersion = generatorVersion;
		this.canBeCreated = true;
		this.id = id;
		worldTypes[id] = this;
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

	private WorldType setCanBeCreated(boolean canBeCreated) {
		this.canBeCreated = canBeCreated;
		return this;
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

	public WorldChunkManager getChunkManager(World world) {
		return (WorldChunkManager)(this == SKY ? new WorldChunkManager(world) : new WorldChunkManager(world));
	}

	public IChunkProvider getChunkGenerator(World world) {
		if(this == SKY) return 	new ChunkProviderSky(world, world.getRandomSeed(), world.getWorldInfo().isMapFeaturesEnabled());
		if(this == INFDEV) return new ChunkProviderInfdev(world, world.getRandomSeed(), world.getWorldInfo().isMapFeaturesEnabled());
		if(this == INDEV) return new ChunkProviderIndev(world, world.getRandomSeed(), world.getWorldInfo().isMapFeaturesEnabled());
		return new ChunkProviderGenerate(world, world.getRandomSeed(), world.getWorldInfo().isMapFeaturesEnabled());
	}

	public int getSeaLevel(World world) {
		return this.getMinimumSpawnHeight(world);
	}

	public int getMinimumSpawnHeight(World world) {
		return this == FLAT ? 4 : 64;
	}

	public double getHorizon(World world) {
		return this == FLAT ? 0.0D : 63.0D;
	}

	public boolean hasVoidParticles(boolean hasVoidParticles) {
		return this != FLAT && !hasVoidParticles;
	}

	public double voidFadeMagnitude() {
		return this == FLAT ? 1.0D : 8.0D / 256D;
	}

	public void onGUICreateWorldPress() {
	}

	public int getId() {
		return id;
	}
}
