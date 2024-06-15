package net.minecraft.src;

public class WorldType {
	public static final WorldType[] worldTypes = new WorldType[16];
	public static final WorldType DEFAULT = (new WorldType(3, "default", 1)).func_48631_f();
	public static final WorldType FLAT = (new WorldType(1, "flat")).setCanBeCreated(false);
	public static final WorldType SKY = (new WorldType(2, "sky", 1)).func_48631_f();
	public static final WorldType INFDEV = (new WorldType(0, "infdev", 1)).func_48631_f();

	private final String worldType;
	private final int generatorVersion;
	private boolean canBeCreated;
	private boolean field_48638_h;
	private final int id;

	protected WorldType(int par1, String par2Str) {
		this(par1, par2Str, 0);
	}

	protected WorldType(int par1, String par2Str, int par3) {
		this.worldType = par2Str;
		this.generatorVersion = par3;
		this.canBeCreated = true;
		this.id = par1;
		worldTypes[par1] = this;
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

	private WorldType setCanBeCreated(boolean par1) {
		this.canBeCreated = par1;
		return this;
	}

	public boolean getCanBeCreated() {
		return this.canBeCreated;
	}

	private WorldType func_48631_f() {
		this.field_48638_h = true;
		return this;
	}

	public boolean func_48626_e() {
		return this.field_48638_h;
	}

	public static WorldType parseWorldType(String par0Str) {
		for(int var1 = 0; var1 < worldTypes.length; ++var1) {
			if(worldTypes[var1] != null && worldTypes[var1].worldType.equalsIgnoreCase(par0Str)) {
				return worldTypes[var1];
			}
		}

		return null;
	}

	public WorldChunkManager getChunkManager(World var1) {
		return (WorldChunkManager)(this == SKY ? new WorldChunkManager(var1) : new WorldChunkManager(var1));
	}

	public IChunkProvider getChunkGenerator(World var1) {
		return (IChunkProvider)(
				this == SKY ? 
						new ChunkProviderSky(var1, var1.getRandomSeed(), var1.getWorldInfo().isMapFeaturesEnabled()) 
					: 
						(this == INFDEV ?
								new ChunklProviderInfdev(var1, var1.getRandomSeed(), var1.getWorldInfo().isMapFeaturesEnabled())
							:
								new ChunkProviderGenerate(var1, var1.getRandomSeed(), var1.getWorldInfo().isMapFeaturesEnabled())
						)
		);
	}

	/** @deprecated */
	public int getSeaLevel(World var1) {
		return this.getMinimumSpawnHeight(var1);
	}

	public int getMinimumSpawnHeight(World world) {
		return this == FLAT ? 4 : 64;
	}

	public double getHorizon(World world) {
		return this == FLAT ? 0.0D : 63.0D;
	}

	public boolean hasVoidParticles(boolean var1) {
		return this != FLAT && !var1;
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
