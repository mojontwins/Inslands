package net.minecraft.src;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.misc.bo3import.WorldGenBo3Tree;
import com.mojang.minecraft.betabiomes.BiomeGenBeta;
import com.mojontwins.minecraft.poisonisland.BiomeGenThemePoisonIsland;

public class BiomeGenBase {
	public static final BiomeGenBase biomeDefault = new BiomeGenBase().setBiomeName("Default Alpha");
	
	// Level themes
	public static final BiomeGenBase themeHell = new BiomeGenThemeHell().setBiomeName("Hell");
	public static final BiomeGenBase themeForest = new BiomeGenThemeForest().setBiomeName("Forest");
	public static final BiomeGenBase themeParadise = new BiomeGenThemeParadise().setBiomeName("Paradise");
	public static final BiomeGenBase themeForestHell = new BiomeGenThemeForestHell().setBiomeName("Forest Hell").setDisableRain();
	public static final BiomeGenBase themePoison = new BiomeGenThemePoisonIsland().setBiomeName("Poison Island");
	
	// Leave these for compatibility
	public static final BiomeGenBase hell = (new BiomeGenHell()).setColor(16711680).setBiomeName("Hell").setDisableRain();
	public static final BiomeGenBase sky = (new BiomeGenSky()).setColor(8421631).setBiomeName("Sky").setDisableRain();

	public String biomeName = "Default Alpha";
	public int biomeCode = 0;
	public static int currentBiomeCode = 0;
	
	private static BiomeGenBase biomeLookupTable [] = new BiomeGenBase[64*64];
	
	public byte topBlock = (byte)Block.grass.blockID;
	public byte fillerBlock = (byte)Block.dirt.blockID;
	
	public int biomeColor = 5169201;
	public int color = 5169201;
	protected List<SpawnListEntry> spawnableMonsterList = new ArrayList<SpawnListEntry>();
	protected List<SpawnListEntry> spawnableCreatureList = new ArrayList<SpawnListEntry>();
	protected List<SpawnListEntry> spawnableWaterCreatureList = new ArrayList<SpawnListEntry>();
	protected List<SpawnListEntry> spawnableCaveCreatureList = new ArrayList<SpawnListEntry>();
	private boolean enableSnow;
	
	public WorldGenBo3Tree bo3Tree = new WorldGenBo3Tree();

	protected BiomeGenBase() {
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySpider.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityZombie.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySkeleton.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityCreeper.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 10));
		
		this.spawnableCreatureList.add(new SpawnListEntry(EntitySheep.class, 12));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityPig.class, 10));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 10));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityCow.class, 8));
		
		this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySquid.class, 5));
		
		this.biomeCode = BiomeGenBase.currentBiomeCode ++;
	}
	
	public BiomeGenBase setCode(int code) {
		this.biomeCode = code;
		return this;
	}
	
	public boolean isPermaFrost() {
		return false;
	}
	
	// Biome related values for ChunkProviderGenerate.populate
	public int dungeonAttempts = 8;
	public int clayAttempts = 10;
	public int dirtLumpAttempts = 20;
	public int gravelLumpAttempts = 10;
	public int coalLumpAttempts = 20;
	public int glowLumpAttempts = 10;
	public int ironLumpAttempts = 20;
	public int goldLumpAttempts = 2;
	public int goldLumpMaxHeight = 32;
	public int redstoneLumpAttempts = 8;
	public int redstoneLumpMaxHeight = 16;
	public int diamondLumpAttempts = 1;
	public int diamondLumpMaxHeight = 16;
	public int treeBaseAttemptsModifier = 0;
	public int bigTreesEach10Trees = 5;
	public int yellowFlowersAttempts = 4;
	public int redFlowersAttempts = 1;
	public int mushroomBrownChance = 4;
	public int mushroomRedChance = 8;
	public int reedAttempts = 10;
	public int cactusAttempts = 1;
	public int waterFallAttempts = 50;
	public int waterFallMaxHeight = 128;
	public int waterFallMinHeight = 8;
	public int lavaAttempts = 20;
	public int tallGrassAttempts = 0;
	public int pumpkinChance = 32;			// Chance is 1 in N
	public int deadBushAttempts = 0;
	public boolean genBeaches = true;
	public int mainLiquid = Block.waterStill.blockID;
	
	// Related to level theme (using biomes as level themes)
	public int indevHouseWalls = Block.planks.blockID;
	public int overrideSkyColor = -1; 		// Set to -1 to get general default color.
	public int overrideCloudColor = -1; 	// Set to -1 to get general clouds color.
	public int overrideFogColor = -1; 		// Set to -1 to get general fog color.
	public float lightMultiplier = 1.0F;	// Will only take effect when biome is a level theme.
	public boolean dayCycle = true; 		// Will only take effect when biome is a level theme.

	public Weather weather = Weather.normal; 
	
	// Set this to a different colour so leaves are tinted differently for this biome.
	// This is an index to an array in BlockLeaves.
	public int foliageColorizer = 0;
	public float maxHeight = 1.0F;
	public float minHeight = 0.0F;

	private boolean enableRain;

	public boolean forceBlockLightInitLikeNether = false;

	// Unused, as of yet.
	//private boolean canSpawnLightningBolt = true;;
	
	public BiomeGenBase setWeather(Weather weather) {
		this.weather = weather;
		return this;
	}

	public WorldGenerator genTreeTryFirst(Random rand) {
		return null;
	}
	
	public WorldGenerator getTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		return new WorldGenTrees();
	}
	
	public WorldGenerator getBigTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		return new WorldGenBigTree();
	}
	
	public byte getTopBlock(Random rand) {
		return this.topBlock;
	}
	
	// To select themed spawners in dungeons
	public String getPreferedSpawner() {
		return null;
	}
	
	public int getPreferedSpawnerChance() {
		return 1;
	}
	
	public int getPreferedSpawnerChanceOffset() {
		return 1;
	}

	private BiomeGenBase setDisableRain() {
		this.enableRain = false;
		return this;
	}

	public WorldGenerator getRandomWorldGenForTrees(Random random1) {
		return (WorldGenerator)(random1.nextInt(10) == 0 ? new WorldGenBigTree() : new WorldGenTrees());
	}

	public static void generateBiomeLookup() {
		for(int var0 = 0; var0 < 64; ++var0) {
			for(int var1 = 0; var1 < 64; ++var1) {
				biomeLookupTable[var0 + var1 * 64] = getBiome((float)var0 / 63.0F, (float)var1 / 63.0F);
			}
		}
	}

	public static BiomeGenBase getBiome(float temperature, float humidity) {
		if(LevelThemeGlobalSettings.themeID == LevelThemeSettings.biomes.id) return BiomeGenBeta.getBiome(temperature, humidity);
		
		humidity *= temperature;
		
		BiomeGenBase biome = biomeDefault;
				
		return biome;
	}

	protected BiomeGenBase setEnableSnow() {
		this.enableSnow = true;
		return this;
	}

	protected BiomeGenBase setBiomeName(String string1) {
		this.biomeName = string1;
		return this;
	}

	protected BiomeGenBase setBiomeColor(int i1) {
		this.biomeColor = i1;
		return this;
	}

	protected BiomeGenBase setColor(int i1) {
		this.color = i1;
		return this;
	}

	public static BiomeGenBase getBiomeFromLookup(double temperature, double humidity) {
		int var4 = (int)(temperature * 63.0D);
		int var5 = (int)(humidity * 63.0D);
		return biomeLookupTable[var4 + var5 * 64];
	}
	
	// Called during generation
	public void generate(Random rand, int y0, Chunk chunk) {
	}
	
	// Called before standard population (i.e. gen lakes etc).
	public void prePopulate(World world, Random rand, int x0, int z0) {
	}
	
	// Called after standard population
	public void populate(World world, Random rand, int x0, int z0) {
	}

	public int getSkyColorByTemp(float f1) {
		f1 /= 3.0F;
		if(f1 < -1.0F) {
			f1 = -1.0F;
		}

		if(f1 > 1.0F) {
			f1 = 1.0F;
		}

		return Color.getHSBColor(0.62222224F - f1 * 0.05F, 0.5F + f1 * 0.1F, 1.0F).getRGB();
	}

	public List<SpawnListEntry> getSpawnableList(EnumCreatureType enumCreatureType) {
		if(enumCreatureType == EnumCreatureType.monster) return this.spawnableMonsterList;
		if(enumCreatureType == EnumCreatureType.creature) return this.spawnableCreatureList;
		if(enumCreatureType == EnumCreatureType.waterCreature) return this.spawnableWaterCreatureList;
		if(enumCreatureType == EnumCreatureType.caveCreature) return this.spawnableCaveCreatureList;
		return null;
	}

	public boolean getEnableSnow() {
		return this.enableSnow;
	}

	public boolean canSpawnLightningBolt() {
		return this.enableSnow ? false : this.enableRain;
	}
	
		
	public void replaceBlocksForBiome(IChunkProvider generator, World world, Random rand, int chunkX, int chunkZ, int x, int z, byte[] blocks, byte[] metadata, int seaLevel, double sandNoise, double gravelNoise, double stoneNoise) {
		boolean generateSand = sandNoise + rand.nextDouble() * 0.2D > 0.0D;
		boolean generateGravel = gravelNoise + rand.nextDouble() * 0.2D > 3.0D;
		int height = (int)(stoneNoise / 3.0D + 3.0D + rand.nextDouble() * 0.25D);

		int stoneHeight = -1;
		byte topBlock = this.getTopBlock(rand);
		byte fillerBlock = this.fillerBlock;

		for(int y = 127; y >= 0; --y) {
			int index = x << 11 | z << 7 | y; // (x * 16 + z) * 128 + y
			
			if(y <= 0 + rand.nextInt(5) && !(generator instanceof ChunkProviderSky)) {
				blocks[index] = (byte)Block.bedrock.blockID;
			} else {
				byte blockID = blocks[index];
				byte meta = metadata[index];
				if(blockID == 0) {
					stoneHeight = -1;
				} else if(blockID == Block.stone.blockID && meta == 0) {
					if(stoneHeight == -1) {
						if(height <= 0) {
							topBlock = 0;
							fillerBlock = (byte)Block.stone.blockID;
						} else if(y >= seaLevel - 4 && y <= seaLevel + 1 && !(generator instanceof ChunkProviderSky)) {
							topBlock = this.topBlock;
							fillerBlock = this.fillerBlock;
							if(generateGravel) {
								topBlock = 0;
							}
							
							if(this.genBeaches) {
								if(generateGravel) {
									fillerBlock = (byte)Block.gravel.blockID;
								}

								if(generateSand) {
									topBlock = (byte)Block.sand.blockID;
									fillerBlock = (byte)Block.sand.blockID;
								}
							}
						}

						if(y < seaLevel && topBlock == 0 && !(generator instanceof ChunkProviderSky)) {
							if(this.weather == Weather.cold) {
								topBlock = (byte)Block.ice.blockID;
							} else {
								topBlock = (byte)this.mainLiquid;
							}
						}

						stoneHeight = height;
						if(y >= seaLevel - 1 || generator instanceof ChunkProviderSky) {
							blocks[index] = topBlock;
						} else {
							blocks[index] = fillerBlock;
						}
					} else if(stoneHeight > 0) {
						--stoneHeight;
						blocks[index] = fillerBlock;
						if(stoneHeight == 0 && fillerBlock == this.sandstoneGenTriggerer()) {
							stoneHeight = rand.nextInt(4);
							fillerBlock = this.sandstoneGenBlock();
						}
					}
				} else if(blockID == Block.waterStill.blockID && y == seaLevel - 1 && this.weather == Weather.cold) {
					blocks[index] = (byte)Block.ice.blockID;
				}
			}
		}
	}
	
	public byte sandstoneGenTriggerer() {
		return (byte)Block.sand.blockID;
	}
	
	public byte sandstoneGenBlock() {
		return (byte)Block.sandStone.blockID;
	}

	public boolean isHumid() {
		return false;
	}
}
