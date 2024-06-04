package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class World implements IBlockAccess {
	public boolean scheduledUpdatesAreImmediate;
	private List lightingToUpdate;
	public List loadedEntityList;
	private List unloadedEntityList;
	private TreeSet scheduledTickTreeSet;
	private Set scheduledTickSet;
	public List loadedTileEntityList;
	private List field_30900_E;
	public List playerEntities;
	public List weatherEffects;
	private long field_1019_F;
	public int skylightSubtracted;
	protected int field_9437_g;
	protected final int field_9436_h;
	protected float prevRainingStrength;
	protected float rainingStrength;
	protected float prevThunderingStrength;
	protected float thunderingStrength;
	protected int field_27168_F;
	public int field_27172_i;
	public boolean editingBlocks;
	private long lockTimestamp;
	protected int autosavePeriod;
	public int difficultySetting;
	public Random rand;
	public boolean isNewWorld;
	public final WorldProvider worldProvider;
	protected List worldAccesses;
	protected IChunkProvider chunkProvider;
	protected final ISaveHandler saveHandler;
	protected WorldInfo worldInfo;
	public boolean findingSpawnPoint;
	private boolean allPlayersSleeping;
	public MapStorage field_28108_z;
	private ArrayList collidingBoundingBoxes;
	private boolean field_31055_L;
	private int lightingUpdatesCounter;
	private boolean spawnHostileMobs;
	private boolean spawnPeacefulMobs;
	static int lightingUpdatesScheduled = 0;
	private Set positionsToUpdate;
	private int soundCounter;
	private List field_1012_M;
	public boolean multiplayerWorld;

	public WorldChunkManager getWorldChunkManager() {
		return this.worldProvider.worldChunkMgr;
	}

	public World(ISaveHandler wt1, String s1, WorldProvider xa1, long l1) {
		this.field_9436_h = 1013904223;
		this.scheduledUpdatesAreImmediate = false;
		this.lightingToUpdate = new ArrayList();
		this.loadedEntityList = new ArrayList();
		this.unloadedEntityList = new ArrayList();
		this.scheduledTickTreeSet = new TreeSet();
		this.scheduledTickSet = new HashSet();
		this.loadedTileEntityList = new ArrayList();
		this.field_30900_E = new ArrayList();
		this.playerEntities = new ArrayList();
		this.weatherEffects = new ArrayList();
		this.field_1019_F = 16777215L;
		this.skylightSubtracted = 0;
		this.field_9437_g = (new Random()).nextInt();
		this.field_27168_F = 0;
		this.field_27172_i = 0;
		this.editingBlocks = false;
		this.lockTimestamp = System.currentTimeMillis();
		this.autosavePeriod = 40;
		this.rand = new Random();
		this.isNewWorld = false;
		this.worldAccesses = new ArrayList();
		this.collidingBoundingBoxes = new ArrayList();
		this.lightingUpdatesCounter = 0;
		this.spawnHostileMobs = true;
		this.spawnPeacefulMobs = true;
		this.positionsToUpdate = new HashSet();
		this.soundCounter = this.rand.nextInt(12000);
		this.field_1012_M = new ArrayList();
		this.multiplayerWorld = false;
		this.saveHandler = wt1;
		this.worldInfo = new WorldInfo(l1, s1);
		this.worldProvider = xa1;
		this.field_28108_z = new MapStorage(wt1);
		xa1.registerWorld(this);
		this.chunkProvider = this.getChunkProvider();
		this.calculateInitialSkylight();
		this.func_27163_E();
	}

	public World(World fd1, WorldProvider xa1) {
		this.field_9436_h = 1013904223;
		this.scheduledUpdatesAreImmediate = false;
		this.lightingToUpdate = new ArrayList();
		this.loadedEntityList = new ArrayList();
		this.unloadedEntityList = new ArrayList();
		this.scheduledTickTreeSet = new TreeSet();
		this.scheduledTickSet = new HashSet();
		this.loadedTileEntityList = new ArrayList();
		this.field_30900_E = new ArrayList();
		this.playerEntities = new ArrayList();
		this.weatherEffects = new ArrayList();
		this.field_1019_F = 16777215L;
		this.skylightSubtracted = 0;
		this.field_9437_g = (new Random()).nextInt();
		this.field_27168_F = 0;
		this.field_27172_i = 0;
		this.editingBlocks = false;
		this.lockTimestamp = System.currentTimeMillis();
		this.autosavePeriod = 40;
		this.rand = new Random();
		this.isNewWorld = false;
		this.worldAccesses = new ArrayList();
		this.collidingBoundingBoxes = new ArrayList();
		this.lightingUpdatesCounter = 0;
		this.spawnHostileMobs = true;
		this.spawnPeacefulMobs = true;
		this.positionsToUpdate = new HashSet();
		this.soundCounter = this.rand.nextInt(12000);
		this.field_1012_M = new ArrayList();
		this.multiplayerWorld = false;
		this.lockTimestamp = fd1.lockTimestamp;
		this.saveHandler = fd1.saveHandler;
		this.worldInfo = new WorldInfo(fd1.worldInfo);
		this.field_28108_z = new MapStorage(this.saveHandler);
		this.worldProvider = xa1;
		xa1.registerWorld(this);
		this.chunkProvider = this.getChunkProvider();
		this.calculateInitialSkylight();
		this.func_27163_E();
	}

	public World(ISaveHandler wt1, String s1, long l1) {
		this(wt1, s1, l1, (WorldProvider)null);
	}

	public World(ISaveHandler wt1, String s1, long l1, WorldProvider xa1) {
		this.field_9436_h = 1013904223;
		this.scheduledUpdatesAreImmediate = false;
		this.lightingToUpdate = new ArrayList();
		this.loadedEntityList = new ArrayList();
		this.unloadedEntityList = new ArrayList();
		this.scheduledTickTreeSet = new TreeSet();
		this.scheduledTickSet = new HashSet();
		this.loadedTileEntityList = new ArrayList();
		this.field_30900_E = new ArrayList();
		this.playerEntities = new ArrayList();
		this.weatherEffects = new ArrayList();
		this.field_1019_F = 16777215L;
		this.skylightSubtracted = 0;
		this.field_9437_g = (new Random()).nextInt();
		this.field_27168_F = 0;
		this.field_27172_i = 0;
		this.editingBlocks = false;
		this.lockTimestamp = System.currentTimeMillis();
		this.autosavePeriod = 40;
		this.rand = new Random();
		this.isNewWorld = false;
		this.worldAccesses = new ArrayList();
		this.collidingBoundingBoxes = new ArrayList();
		this.lightingUpdatesCounter = 0;
		this.spawnHostileMobs = true;
		this.spawnPeacefulMobs = true;
		this.positionsToUpdate = new HashSet();
		this.soundCounter = this.rand.nextInt(12000);
		this.field_1012_M = new ArrayList();
		this.multiplayerWorld = false;
		this.saveHandler = wt1;
		this.field_28108_z = new MapStorage(wt1);
		this.worldInfo = wt1.loadWorldInfo();
		this.isNewWorld = this.worldInfo == null;
		if(xa1 != null) {
			this.worldProvider = xa1;
		} else {
			int flag = 0;
			if(this.worldInfo != null) {
				flag = this.worldInfo.getDimension();
			}

			DimensionBase localDimensionBase = DimensionBase.getDimByNumber(flag);
			this.worldProvider = localDimensionBase.getWorldProvider();
		}

		boolean flag1 = false;
		if(this.worldInfo == null) {
			this.worldInfo = new WorldInfo(l1, s1);
			flag1 = true;
		} else {
			this.worldInfo.setWorldName(s1);
		}

		this.worldProvider.registerWorld(this);
		this.chunkProvider = this.getChunkProvider();
		if(flag1) {
			this.getInitialSpawnLocation();
		}

		this.calculateInitialSkylight();
		this.func_27163_E();
	}

	protected IChunkProvider getChunkProvider() {
		IChunkLoader bf = this.saveHandler.getChunkLoader(this.worldProvider);
		return new ChunkProvider(this, bf, this.worldProvider.getChunkProvider());
	}

	protected void getInitialSpawnLocation() {
		this.findingSpawnPoint = true;
		int i1 = 0;
		byte byte0 = 64;

		int j1;
		for(j1 = 0; !this.worldProvider.canCoordinateBeSpawn(i1, j1); j1 += this.rand.nextInt(64) - this.rand.nextInt(64)) {
			i1 += this.rand.nextInt(64) - this.rand.nextInt(64);
		}

		this.worldInfo.setSpawn(i1, byte0, j1);
		this.findingSpawnPoint = false;
	}

	public void setSpawnLocation() {
		if(this.worldInfo.getSpawnY() <= 0) {
			this.worldInfo.setSpawnY(64);
		}

		int i1 = this.worldInfo.getSpawnX();

		int j1;
		for(j1 = this.worldInfo.getSpawnZ(); this.getFirstUncoveredBlock(i1, j1) == 0; j1 += this.rand.nextInt(8) - this.rand.nextInt(8)) {
			i1 += this.rand.nextInt(8) - this.rand.nextInt(8);
		}

		this.worldInfo.setSpawnX(i1);
		this.worldInfo.setSpawnZ(j1);
	}

	public int getFirstUncoveredBlock(int i1, int j1) {
		int k1;
		for(k1 = 63; !this.isAirBlock(i1, k1 + 1, j1); ++k1) {
		}

		return this.getBlockId(i1, k1, j1);
	}

	public void emptyMethod1() {
	}

	public void spawnPlayerWithLoadedChunks(EntityPlayer gs1) {
		try {
			NBTTagCompound exception = this.worldInfo.getPlayerNBTTagCompound();
			if(exception != null) {
				gs1.readFromNBT(exception);
				this.worldInfo.setPlayerNBTTagCompound((NBTTagCompound)null);
			}

			if(this.chunkProvider instanceof ChunkProviderLoadOrGenerate) {
				ChunkProviderLoadOrGenerate kx1 = (ChunkProviderLoadOrGenerate)this.chunkProvider;
				int i1 = MathHelper.floor_float((float)((int)gs1.posX)) >> 4;
				int j1 = MathHelper.floor_float((float)((int)gs1.posZ)) >> 4;
				kx1.setCurrentChunkOver(i1, j1);
			}

			this.entityJoinedWorld(gs1);
		} catch (Exception exception6) {
			exception6.printStackTrace();
		}

	}

	public void saveWorld(boolean flag, IProgressUpdate yb1) {
		if(this.chunkProvider.canSave()) {
			if(yb1 != null) {
				yb1.func_594_b("Saving level");
			}

			this.saveLevel();
			if(yb1 != null) {
				yb1.displayLoadingString("Saving chunks");
			}

			this.chunkProvider.saveChunks(flag, yb1);
		}
	}

	private void saveLevel() {
		this.checkSessionLock();
		this.saveHandler.saveWorldInfoAndPlayer(this.worldInfo, this.playerEntities);
		this.field_28108_z.saveAllData();
	}

	public boolean func_650_a(int i1) {
		if(!this.chunkProvider.canSave()) {
			return true;
		} else {
			if(i1 == 0) {
				this.saveLevel();
			}

			return this.chunkProvider.saveChunks(false, (IProgressUpdate)null);
		}
	}

	public int getBlockId(int i1, int j1, int k1) {
		return i1 >= -32000000 && k1 >= -32000000 && i1 < 32000000 && k1 <= 32000000 ? (j1 < 0 ? 0 : (j1 >= 128 ? 0 : this.getChunkFromChunkCoords(i1 >> 4, k1 >> 4).getBlockID(i1 & 15, j1, k1 & 15))) : 0;
	}

	public boolean isAirBlock(int i1, int j1, int k1) {
		return this.getBlockId(i1, j1, k1) == 0;
	}

	public boolean blockExists(int i1, int j1, int k1) {
		return j1 >= 0 && j1 < 128 ? this.chunkExists(i1 >> 4, k1 >> 4) : false;
	}

	public boolean doChunksNearChunkExist(int i1, int j1, int k1, int l1) {
		return this.checkChunksExist(i1 - l1, j1 - l1, k1 - l1, i1 + l1, j1 + l1, k1 + l1);
	}

	public boolean checkChunksExist(int i1, int j1, int k1, int l1, int i2, int j2) {
		if(i2 >= 0 && j1 < 128) {
			i1 >>= 4;
			j1 >>= 4;
			k1 >>= 4;
			l1 >>= 4;
			i2 >>= 4;
			j2 >>= 4;

			for(int k2 = i1; k2 <= l1; ++k2) {
				for(int l2 = k1; l2 <= j2; ++l2) {
					if(!this.chunkExists(k2, l2)) {
						return false;
					}
				}
			}

			return true;
		} else {
			return false;
		}
	}

	private boolean chunkExists(int i1, int j1) {
		return this.chunkProvider.chunkExists(i1, j1);
	}

	public Chunk getChunkFromBlockCoords(int i1, int j1) {
		return this.getChunkFromChunkCoords(i1 >> 4, j1 >> 4);
	}

	public Chunk getChunkFromChunkCoords(int i1, int j1) {
		return this.chunkProvider.provideChunk(i1, j1);
	}

	public boolean setBlockAndMetadata(int i1, int j1, int k1, int l1, int i2) {
		if(i1 >= -32000000 && k1 >= -32000000 && i1 < 32000000 && k1 <= 32000000) {
			if(j1 < 0) {
				return false;
			} else if(j1 >= 128) {
				return false;
			} else {
				Chunk lm1 = this.getChunkFromChunkCoords(i1 >> 4, k1 >> 4);
				l1 = SAPI.interceptBlockSet(this, new Loc(i1, j1, k1), l1);
				return lm1.setBlockIDWithMetadata(i1 & 15, j1, k1 & 15, l1, i2);
			}
		} else {
			return false;
		}
	}

	public boolean setBlock(int i1, int j1, int k1, int l1) {
		if(i1 >= -32000000 && k1 >= -32000000 && i1 < 32000000 && k1 <= 32000000) {
			if(j1 < 0) {
				return false;
			} else if(j1 >= 128) {
				return false;
			} else {
				Chunk lm1 = this.getChunkFromChunkCoords(i1 >> 4, k1 >> 4);
				l1 = SAPI.interceptBlockSet(this, new Loc(i1, j1, k1), l1);
				return lm1.setBlockID(i1 & 15, j1, k1 & 15, l1);
			}
		} else {
			return false;
		}
	}

	public Material getBlockMaterial(int i1, int j1, int k1) {
		int l1 = this.getBlockId(i1, j1, k1);
		return l1 == 0 ? Material.air : Block.blocksList[l1].blockMaterial;
	}

	public int getBlockMetadata(int i1, int j1, int k1) {
		if(i1 >= -32000000 && k1 >= -32000000 && i1 < 32000000 && k1 <= 32000000) {
			if(j1 < 0) {
				return 0;
			} else if(j1 >= 128) {
				return 0;
			} else {
				Chunk lm1 = this.getChunkFromChunkCoords(i1 >> 4, k1 >> 4);
				i1 &= 15;
				k1 &= 15;
				return lm1.getBlockMetadata(i1, j1, k1);
			}
		} else {
			return 0;
		}
	}

	public void setBlockMetadataWithNotify(int i1, int j1, int k1, int l1) {
		if(this.setBlockMetadata(i1, j1, k1, l1)) {
			int i2 = this.getBlockId(i1, j1, k1);
			if(Block.field_28032_t[i2 & 255]) {
				this.notifyBlockChange(i1, j1, k1, i2);
			} else {
				this.notifyBlocksOfNeighborChange(i1, j1, k1, i2);
			}
		}

	}

	public boolean setBlockMetadata(int i1, int j1, int k1, int l1) {
		if(i1 >= -32000000 && k1 >= -32000000 && i1 < 32000000 && k1 <= 32000000) {
			if(j1 < 0) {
				return false;
			} else if(j1 >= 128) {
				return false;
			} else {
				Chunk lm1 = this.getChunkFromChunkCoords(i1 >> 4, k1 >> 4);
				i1 &= 15;
				k1 &= 15;
				lm1.setBlockMetadata(i1, j1, k1, l1);
				return true;
			}
		} else {
			return false;
		}
	}

	public boolean setBlockWithNotify(int i1, int j1, int k1, int l1) {
		if(this.setBlock(i1, j1, k1, l1)) {
			this.notifyBlockChange(i1, j1, k1, l1);
			return true;
		} else {
			return false;
		}
	}

	public boolean setBlockAndMetadataWithNotify(int i1, int j1, int k1, int l1, int i2) {
		if(this.setBlockAndMetadata(i1, j1, k1, l1, i2)) {
			this.notifyBlockChange(i1, j1, k1, l1);
			return true;
		} else {
			return false;
		}
	}

	public void markBlockNeedsUpdate(int i1, int j1, int k1) {
		for(int l1 = 0; l1 < this.worldAccesses.size(); ++l1) {
			((IWorldAccess)this.worldAccesses.get(l1)).markBlockAndNeighborsNeedsUpdate(i1, j1, k1);
		}

	}

	protected void notifyBlockChange(int i1, int j1, int k1, int l1) {
		this.markBlockNeedsUpdate(i1, j1, k1);
		this.notifyBlocksOfNeighborChange(i1, j1, k1, l1);
	}

	public void markBlocksDirtyVertical(int i1, int j1, int k1, int l1) {
		if(k1 > l1) {
			int i2 = l1;
			l1 = k1;
			k1 = i2;
		}

		this.markBlocksDirty(i1, k1, j1, i1, l1, j1);
	}

	public void markBlockAsNeedsUpdate(int i1, int j1, int k1) {
		for(int l1 = 0; l1 < this.worldAccesses.size(); ++l1) {
			((IWorldAccess)this.worldAccesses.get(l1)).markBlockRangeNeedsUpdate(i1, j1, k1, i1, j1, k1);
		}

	}

	public void markBlocksDirty(int i1, int j1, int k1, int l1, int i2, int j2) {
		for(int k2 = 0; k2 < this.worldAccesses.size(); ++k2) {
			((IWorldAccess)this.worldAccesses.get(k2)).markBlockRangeNeedsUpdate(i1, j1, k1, l1, i2, j2);
		}

	}

	public void notifyBlocksOfNeighborChange(int i1, int j1, int k1, int l1) {
		this.notifyBlockOfNeighborChange(i1 - 1, j1, k1, l1);
		this.notifyBlockOfNeighborChange(i1 + 1, j1, k1, l1);
		this.notifyBlockOfNeighborChange(i1, j1 - 1, k1, l1);
		this.notifyBlockOfNeighborChange(i1, j1 + 1, k1, l1);
		this.notifyBlockOfNeighborChange(i1, j1, k1 - 1, l1);
		this.notifyBlockOfNeighborChange(i1, j1, k1 + 1, l1);
	}

	private void notifyBlockOfNeighborChange(int i1, int j1, int k1, int l1) {
		if(!this.editingBlocks && !this.multiplayerWorld) {
			Block uu1 = Block.blocksList[this.getBlockId(i1, j1, k1)];
			if(uu1 != null) {
				uu1.onNeighborBlockChange(this, i1, j1, k1, l1);
			}

		}
	}

	public boolean canBlockSeeTheSky(int i1, int j1, int k1) {
		return this.getChunkFromChunkCoords(i1 >> 4, k1 >> 4).canBlockSeeTheSky(i1 & 15, j1, k1 & 15);
	}

	public int getFullBlockLightValue(int i1, int j1, int k1) {
		if(j1 < 0) {
			return 0;
		} else {
			if(j1 >= 128) {
				j1 = 127;
			}

			return this.getChunkFromChunkCoords(i1 >> 4, k1 >> 4).getBlockLightValue(i1 & 15, j1, k1 & 15, 0);
		}
	}

	public int getBlockLightValue(int i1, int j1, int k1) {
		return this.getBlockLightValue_do(i1, j1, k1, true);
	}

	public int getBlockLightValue_do(int i1, int j1, int k1, boolean flag) {
		if(i1 >= -32000000 && k1 >= -32000000 && i1 < 32000000 && k1 <= 32000000) {
			if(flag) {
				int lm1 = this.getBlockId(i1, j1, k1);
				if(lm1 == Block.stairSingle.blockID || lm1 == Block.tilledField.blockID || lm1 == Block.stairCompactCobblestone.blockID || lm1 == Block.stairCompactPlanks.blockID) {
					int i2 = this.getBlockLightValue_do(i1, j1 + 1, k1, false);
					int j2 = this.getBlockLightValue_do(i1 + 1, j1, k1, false);
					int k2 = this.getBlockLightValue_do(i1 - 1, j1, k1, false);
					int l2 = this.getBlockLightValue_do(i1, j1, k1 + 1, false);
					int i3 = this.getBlockLightValue_do(i1, j1, k1 - 1, false);
					if(j2 > i2) {
						i2 = j2;
					}

					if(k2 > i2) {
						i2 = k2;
					}

					if(l2 > i2) {
						i2 = l2;
					}

					if(i3 > i2) {
						i2 = i3;
					}

					return i2;
				}
			}

			if(j1 < 0) {
				return 0;
			} else {
				if(j1 >= 128) {
					j1 = 127;
				}

				Chunk lm11 = this.getChunkFromChunkCoords(i1 >> 4, k1 >> 4);
				i1 &= 15;
				k1 &= 15;
				return lm11.getBlockLightValue(i1, j1, k1, this.skylightSubtracted);
			}
		} else {
			return 15;
		}
	}

	public boolean canExistingBlockSeeTheSky(int i1, int j1, int k1) {
		if(i1 >= -32000000 && k1 >= -32000000 && i1 < 32000000 && k1 <= 32000000) {
			if(j1 < 0) {
				return false;
			} else if(j1 >= 128) {
				return true;
			} else if(!this.chunkExists(i1 >> 4, k1 >> 4)) {
				return false;
			} else {
				Chunk lm1 = this.getChunkFromChunkCoords(i1 >> 4, k1 >> 4);
				i1 &= 15;
				k1 &= 15;
				return lm1.canBlockSeeTheSky(i1, j1, k1);
			}
		} else {
			return false;
		}
	}

	public int getHeightValue(int i1, int j1) {
		if(i1 >= -32000000 && j1 >= -32000000 && i1 < 32000000 && j1 <= 32000000) {
			if(!this.chunkExists(i1 >> 4, j1 >> 4)) {
				return 0;
			} else {
				Chunk lm1 = this.getChunkFromChunkCoords(i1 >> 4, j1 >> 4);
				return lm1.getHeightValue(i1 & 15, j1 & 15);
			}
		} else {
			return 0;
		}
	}

	public void neighborLightPropagationChanged(EnumSkyBlock eb1, int i1, int j1, int k1, int l1) {
		if(!this.worldProvider.hasNoSky || eb1 != EnumSkyBlock.Sky) {
			if(this.blockExists(i1, j1, k1)) {
				if(eb1 == EnumSkyBlock.Sky) {
					if(this.canExistingBlockSeeTheSky(i1, j1, k1)) {
						l1 = 15;
					}
				} else if(eb1 == EnumSkyBlock.Block) {
					int i2 = this.getBlockId(i1, j1, k1);
					if(Block.lightValue[i2] > l1) {
						l1 = Block.lightValue[i2];
					}
				}

				if(this.getSavedLightValue(eb1, i1, j1, k1) != l1) {
					this.scheduleLightingUpdate(eb1, i1, j1, k1, i1, j1, k1);
				}

			}
		}
	}

	public int getSavedLightValue(EnumSkyBlock eb1, int i1, int j1, int k1) {
		if(j1 < 0) {
			j1 = 0;
		}

		if(j1 >= 128) {
			j1 = 127;
		}

		if(j1 >= 0 && j1 < 128 && i1 >= -32000000 && k1 >= -32000000 && i1 < 32000000 && k1 <= 32000000) {
			int l1 = i1 >> 4;
			int i2 = k1 >> 4;
			if(!this.chunkExists(l1, i2)) {
				return 0;
			} else {
				Chunk lm1 = this.getChunkFromChunkCoords(l1, i2);
				return lm1.getSavedLightValue(eb1, i1 & 15, j1, k1 & 15);
			}
		} else {
			return eb1.field_1722_c;
		}
	}

	public void setLightValue(EnumSkyBlock eb1, int i1, int j1, int k1, int l1) {
		if(i1 >= -32000000 && k1 >= -32000000 && i1 < 32000000 && k1 <= 32000000) {
			if(j1 >= 0) {
				if(j1 < 128) {
					if(this.chunkExists(i1 >> 4, k1 >> 4)) {
						Chunk lm1 = this.getChunkFromChunkCoords(i1 >> 4, k1 >> 4);
						lm1.setLightValue(eb1, i1 & 15, j1, k1 & 15, l1);

						for(int i2 = 0; i2 < this.worldAccesses.size(); ++i2) {
							((IWorldAccess)this.worldAccesses.get(i2)).markBlockAndNeighborsNeedsUpdate(i1, j1, k1);
						}

					}
				}
			}
		}
	}

	public float getBrightness(int i1, int j1, int k1, int l1) {
		int i2 = this.getBlockLightValue(i1, j1, k1);
		if(i2 < l1) {
			i2 = l1;
		}

		return this.worldProvider.lightBrightnessTable[i2];
	}

	public float getLightBrightness(int i1, int j1, int k1) {
		return this.worldProvider.lightBrightnessTable[this.getBlockLightValue(i1, j1, k1)];
	}

	public boolean isDaytime() {
		return this.skylightSubtracted < 4;
	}

	public MovingObjectPosition rayTraceBlocks(Vec3D bt1, Vec3D bt2) {
		return this.func_28105_a(bt1, bt2, false, false);
	}

	public MovingObjectPosition rayTraceBlocks_do(Vec3D bt1, Vec3D bt2, boolean flag) {
		return this.func_28105_a(bt1, bt2, flag, false);
	}

	public MovingObjectPosition func_28105_a(Vec3D bt1, Vec3D bt2, boolean flag, boolean flag1) {
		if(!Double.isNaN(bt1.xCoord) && !Double.isNaN(bt1.yCoord) && !Double.isNaN(bt1.zCoord)) {
			if(!Double.isNaN(bt2.xCoord) && !Double.isNaN(bt2.yCoord) && !Double.isNaN(bt2.zCoord)) {
				int i1 = MathHelper.floor_double(bt2.xCoord);
				int j1 = MathHelper.floor_double(bt2.yCoord);
				int k1 = MathHelper.floor_double(bt2.zCoord);
				int l1 = MathHelper.floor_double(bt1.xCoord);
				int i2 = MathHelper.floor_double(bt1.yCoord);
				int j2 = MathHelper.floor_double(bt1.zCoord);
				int k2 = this.getBlockId(l1, i2, j2);
				int i3 = this.getBlockMetadata(l1, i2, j2);
				Block uu1 = Block.blocksList[k2];
				if((!flag1 || uu1 == null || uu1.getCollisionBoundingBoxFromPool(this, l1, i2, j2) != null) && k2 > 0 && uu1.canCollideCheck(i3, flag)) {
					MovingObjectPosition l2 = uu1.collisionRayTrace(this, l1, i2, j2, bt1, bt2);
					if(l2 != null) {
						return l2;
					}
				}

				int i42 = 200;

				while(i42-- >= 0) {
					if(Double.isNaN(bt1.xCoord) || Double.isNaN(bt1.yCoord) || Double.isNaN(bt1.zCoord)) {
						return null;
					}

					if(l1 == i1 && i2 == j1 && j2 == k1) {
						return null;
					}

					boolean flag2 = true;
					boolean flag3 = true;
					boolean flag4 = true;
					double d1 = 999.0D;
					double d2 = 999.0D;
					double d3 = 999.0D;
					if(i1 > l1) {
						d1 = (double)l1 + 1.0D;
					} else if(i1 < l1) {
						d1 = (double)l1 + 0.0D;
					} else {
						flag2 = false;
					}

					if(j1 > i2) {
						d2 = (double)i2 + 1.0D;
					} else if(j1 < i2) {
						d2 = (double)i2 + 0.0D;
					} else {
						flag3 = false;
					}

					if(k1 > j2) {
						d3 = (double)j2 + 1.0D;
					} else if(k1 < j2) {
						d3 = (double)j2 + 0.0D;
					} else {
						flag4 = false;
					}

					double d4 = 999.0D;
					double d5 = 999.0D;
					double d6 = 999.0D;
					double d7 = bt2.xCoord - bt1.xCoord;
					double d8 = bt2.yCoord - bt1.yCoord;
					double d9 = bt2.zCoord - bt1.zCoord;
					if(flag2) {
						d4 = (d1 - bt1.xCoord) / d7;
					}

					if(flag3) {
						d5 = (d2 - bt1.yCoord) / d8;
					}

					if(flag4) {
						d6 = (d3 - bt1.zCoord) / d9;
					}

					boolean byte0 = false;
					byte b43;
					if(d4 < d5 && d4 < d6) {
						if(i1 > l1) {
							b43 = 4;
						} else {
							b43 = 5;
						}

						bt1.xCoord = d1;
						bt1.yCoord += d8 * d4;
						bt1.zCoord += d9 * d4;
					} else if(d5 < d6) {
						if(j1 > i2) {
							b43 = 0;
						} else {
							b43 = 1;
						}

						bt1.xCoord += d7 * d5;
						bt1.yCoord = d2;
						bt1.zCoord += d9 * d5;
					} else {
						if(k1 > j2) {
							b43 = 2;
						} else {
							b43 = 3;
						}

						bt1.xCoord += d7 * d6;
						bt1.yCoord += d8 * d6;
						bt1.zCoord = d3;
					}

					Vec3D bt3 = Vec3D.createVector(bt1.xCoord, bt1.yCoord, bt1.zCoord);
					l1 = (int)(bt3.xCoord = (double)MathHelper.floor_double(bt1.xCoord));
					if(b43 == 5) {
						--l1;
						++bt3.xCoord;
					}

					i2 = (int)(bt3.yCoord = (double)MathHelper.floor_double(bt1.yCoord));
					if(b43 == 1) {
						--i2;
						++bt3.yCoord;
					}

					j2 = (int)(bt3.zCoord = (double)MathHelper.floor_double(bt1.zCoord));
					if(b43 == 3) {
						--j2;
						++bt3.zCoord;
					}

					int j3 = this.getBlockId(l1, i2, j2);
					int k3 = this.getBlockMetadata(l1, i2, j2);
					Block uu2 = Block.blocksList[j3];
					if((!flag1 || uu2 == null || uu2.getCollisionBoundingBoxFromPool(this, l1, i2, j2) != null) && j3 > 0 && uu2.canCollideCheck(k3, flag)) {
						MovingObjectPosition vf1 = uu2.collisionRayTrace(this, l1, i2, j2, bt1, bt2);
						if(vf1 != null) {
							return vf1;
						}
					}
				}

				return null;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public void playSoundAtEntity(Entity sn1, String s1, float f1, float f2) {
		for(int i1 = 0; i1 < this.worldAccesses.size(); ++i1) {
			((IWorldAccess)this.worldAccesses.get(i1)).playSound(s1, sn1.posX, sn1.posY - (double)sn1.yOffset, sn1.posZ, f1, f2);
		}

	}

	public void playSoundEffect(double d1, double d2, double d3, String s1, float f1, float f2) {
		for(int i1 = 0; i1 < this.worldAccesses.size(); ++i1) {
			((IWorldAccess)this.worldAccesses.get(i1)).playSound(s1, d1, d2, d3, f1, f2);
		}

	}

	public void playRecord(String s1, int i1, int j1, int k1) {
		for(int l1 = 0; l1 < this.worldAccesses.size(); ++l1) {
			((IWorldAccess)this.worldAccesses.get(l1)).playRecord(s1, i1, j1, k1);
		}

	}

	public void spawnParticle(String s1, double d1, double d2, double d3, double d4, double d5, double d6) {
		for(int i1 = 0; i1 < this.worldAccesses.size(); ++i1) {
			((IWorldAccess)this.worldAccesses.get(i1)).spawnParticle(s1, d1, d2, d3, d4, d5, d6);
		}

	}

	public boolean addWeatherEffect(Entity sn1) {
		this.weatherEffects.add(sn1);
		return true;
	}

	public boolean entityJoinedWorld(Entity sn1) {
		int i1 = MathHelper.floor_double(sn1.posX / 16.0D);
		int j1 = MathHelper.floor_double(sn1.posZ / 16.0D);
		boolean flag = false;
		if(sn1 instanceof EntityPlayer) {
			flag = true;
		}

		if(!flag && !this.chunkExists(i1, j1)) {
			return false;
		} else {
			if(sn1 instanceof EntityPlayer) {
				EntityPlayer gs1 = (EntityPlayer)sn1;
				this.playerEntities.add(gs1);
				this.updateAllPlayersSleepingFlag();
			}

			this.getChunkFromChunkCoords(i1, j1).addEntity(sn1);
			this.loadedEntityList.add(sn1);
			this.obtainEntitySkin(sn1);
			return true;
		}
	}

	protected void obtainEntitySkin(Entity sn1) {
		for(int i1 = 0; i1 < this.worldAccesses.size(); ++i1) {
			((IWorldAccess)this.worldAccesses.get(i1)).obtainEntitySkin(sn1);
		}

	}

	protected void releaseEntitySkin(Entity sn1) {
		for(int i1 = 0; i1 < this.worldAccesses.size(); ++i1) {
			((IWorldAccess)this.worldAccesses.get(i1)).releaseEntitySkin(sn1);
		}

	}

	public void setEntityDead(Entity sn1) {
		if(sn1.riddenByEntity != null) {
			sn1.riddenByEntity.mountEntity((Entity)null);
		}

		if(sn1.ridingEntity != null) {
			sn1.mountEntity((Entity)null);
		}

		sn1.setEntityDead();
		if(sn1 instanceof EntityPlayer) {
			this.playerEntities.remove((EntityPlayer)sn1);
			this.updateAllPlayersSleepingFlag();
		}

	}

	public void addWorldAccess(IWorldAccess pm1) {
		this.worldAccesses.add(pm1);
	}

	public void removeWorldAccess(IWorldAccess pm1) {
		this.worldAccesses.remove(pm1);
	}

	public List getCollidingBoundingBoxes(Entity sn1, AxisAlignedBB eq1) {
		this.collidingBoundingBoxes.clear();
		int i1 = MathHelper.floor_double(eq1.minX);
		int j1 = MathHelper.floor_double(eq1.maxX + 1.0D);
		int k1 = MathHelper.floor_double(eq1.minY);
		int l1 = MathHelper.floor_double(eq1.maxY + 1.0D);
		int i2 = MathHelper.floor_double(eq1.minZ);
		int j2 = MathHelper.floor_double(eq1.maxZ + 1.0D);

		for(int d1 = i1; d1 < j1; ++d1) {
			for(int l2 = i2; l2 < j2; ++l2) {
				if(this.blockExists(d1, 64, l2)) {
					for(int list = k1 - 1; list < l1; ++list) {
						Block j3 = Block.blocksList[this.getBlockId(d1, list, l2)];
						if(j3 != null) {
							j3.getCollidingBoundingBoxes(this, d1, list, l2, eq1, this.collidingBoundingBoxes);
						}
					}
				}
			}
		}

		double d14 = 0.25D;
		List list15 = this.getEntitiesWithinAABBExcludingEntity(sn1, eq1.expand(d14, d14, d14));

		for(int i16 = 0; i16 < list15.size(); ++i16) {
			AxisAlignedBB eq2 = ((Entity)list15.get(i16)).getBoundingBox();
			if(eq2 != null && eq2.intersectsWith(eq1)) {
				this.collidingBoundingBoxes.add(eq2);
			}

			eq2 = sn1.getCollisionBox((Entity)list15.get(i16));
			if(eq2 != null && eq2.intersectsWith(eq1)) {
				this.collidingBoundingBoxes.add(eq2);
			}
		}

		return this.collidingBoundingBoxes;
	}

	public int calculateSkylightSubtracted(float f1) {
		float f2 = this.getCelestialAngle(f1);
		float f3 = 1.0F - (MathHelper.cos(f2 * 3.141593F * 2.0F) * 2.0F + 0.5F);
		if(f3 < 0.0F) {
			f3 = 0.0F;
		}

		if(f3 > 1.0F) {
			f3 = 1.0F;
		}

		f3 = 1.0F - f3;
		f3 = (float)((double)f3 * (1.0D - (double)(this.func_27162_g(f1) * 5.0F) / 16.0D));
		f3 = (float)((double)f3 * (1.0D - (double)(this.func_27166_f(f1) * 5.0F) / 16.0D));
		f3 = 1.0F - f3;
		return (int)(f3 * 11.0F);
	}

	public Vec3D func_4079_a(Entity sn1, float f1) {
		float f2 = this.getCelestialAngle(f1);
		float f3 = MathHelper.cos(f2 * 3.141593F * 2.0F) * 2.0F + 0.5F;
		if(f3 < 0.0F) {
			f3 = 0.0F;
		}

		if(f3 > 1.0F) {
			f3 = 1.0F;
		}

		int i1 = MathHelper.floor_double(sn1.posX);
		int j1 = MathHelper.floor_double(sn1.posZ);
		float f4 = (float)this.getWorldChunkManager().getTemperature(i1, j1);
		int k1 = this.getWorldChunkManager().getBiomeGenAt(i1, j1).getSkyColorByTemp(f4);
		float f5 = (float)(k1 >> 16 & 255) / 255.0F;
		float f6 = (float)(k1 >> 8 & 255) / 255.0F;
		float f7 = (float)(k1 & 255) / 255.0F;
		f5 *= f3;
		f6 *= f3;
		f7 *= f3;
		float f8 = this.func_27162_g(f1);
		float f10;
		float f13;
		if(f8 > 0.0F) {
			f10 = (f5 * 0.3F + f6 * 0.59F + f7 * 0.11F) * 0.6F;
			f13 = 1.0F - f8 * 0.75F;
			f5 = f5 * f13 + f10 * (1.0F - f13);
			f6 = f6 * f13 + f10 * (1.0F - f13);
			f7 = f7 * f13 + f10 * (1.0F - f13);
		}

		f10 = this.func_27166_f(f1);
		if(f10 > 0.0F) {
			f13 = (f5 * 0.3F + f6 * 0.59F + f7 * 0.11F) * 0.2F;
			float f14 = 1.0F - f10 * 0.75F;
			f5 = f5 * f14 + f13 * (1.0F - f14);
			f6 = f6 * f14 + f13 * (1.0F - f14);
			f7 = f7 * f14 + f13 * (1.0F - f14);
		}

		if(this.field_27172_i > 0) {
			f13 = (float)this.field_27172_i - f1;
			if(f13 > 1.0F) {
				f13 = 1.0F;
			}

			f13 *= 0.45F;
			f5 = f5 * (1.0F - f13) + 0.8F * f13;
			f6 = f6 * (1.0F - f13) + 0.8F * f13;
			f7 = f7 * (1.0F - f13) + 1.0F * f13;
		}

		return Vec3D.createVector((double)f5, (double)f6, (double)f7);
	}

	public float getCelestialAngle(float f1) {
		return this.worldProvider.calculateCelestialAngle(this.worldInfo.getWorldTime(), f1);
	}

	public Vec3D func_628_d(float f1) {
		float f2 = this.getCelestialAngle(f1);
		float f3 = MathHelper.cos(f2 * 3.141593F * 2.0F) * 2.0F + 0.5F;
		if(f3 < 0.0F) {
			f3 = 0.0F;
		}

		if(f3 > 1.0F) {
			f3 = 1.0F;
		}

		float f4 = (float)(this.field_1019_F >> 16 & 255L) / 255.0F;
		float f5 = (float)(this.field_1019_F >> 8 & 255L) / 255.0F;
		float f6 = (float)(this.field_1019_F & 255L) / 255.0F;
		float f7 = this.func_27162_g(f1);
		float f9;
		float f11;
		if(f7 > 0.0F) {
			f9 = (f4 * 0.3F + f5 * 0.59F + f6 * 0.11F) * 0.6F;
			f11 = 1.0F - f7 * 0.95F;
			f4 = f4 * f11 + f9 * (1.0F - f11);
			f5 = f5 * f11 + f9 * (1.0F - f11);
			f6 = f6 * f11 + f9 * (1.0F - f11);
		}

		f4 *= f3 * 0.9F + 0.1F;
		f5 *= f3 * 0.9F + 0.1F;
		f6 *= f3 * 0.85F + 0.15F;
		f9 = this.func_27166_f(f1);
		if(f9 > 0.0F) {
			f11 = (f4 * 0.3F + f5 * 0.59F + f6 * 0.11F) * 0.2F;
			float f12 = 1.0F - f9 * 0.95F;
			f4 = f4 * f12 + f11 * (1.0F - f12);
			f5 = f5 * f12 + f11 * (1.0F - f12);
			f6 = f6 * f12 + f11 * (1.0F - f12);
		}

		return Vec3D.createVector((double)f4, (double)f5, (double)f6);
	}

	public Vec3D getFogColor(float f1) {
		float f2 = this.getCelestialAngle(f1);
		return this.worldProvider.func_4096_a(f2, f1);
	}

	public int findTopSolidBlock(int i1, int j1) {
		Chunk lm1 = this.getChunkFromBlockCoords(i1, j1);
		int k1 = 127;
		i1 &= 15;

		for(j1 &= 15; k1 > 0; --k1) {
			int l1 = lm1.getBlockID(i1, k1, j1);
			Material ln1 = l1 != 0 ? Block.blocksList[l1].blockMaterial : Material.air;
			if(ln1.getIsSolid() || ln1.getIsLiquid()) {
				return k1 + 1;
			}
		}

		return -1;
	}

	public float getStarBrightness(float f1) {
		float f2 = this.getCelestialAngle(f1);
		float f3 = 1.0F - (MathHelper.cos(f2 * 3.141593F * 2.0F) * 2.0F + 0.75F);
		if(f3 < 0.0F) {
			f3 = 0.0F;
		}

		if(f3 > 1.0F) {
			f3 = 1.0F;
		}

		return f3 * f3 * 0.5F;
	}

	public void scheduleBlockUpdate(int i1, int j1, int k1, int l1, int i2) {
		NextTickListEntry qy1 = new NextTickListEntry(i1, j1, k1, l1);
		byte byte0 = 8;
		if(this.scheduledUpdatesAreImmediate) {
			if(this.checkChunksExist(qy1.xCoord - byte0, qy1.yCoord - byte0, qy1.zCoord - byte0, qy1.xCoord + byte0, qy1.yCoord + byte0, qy1.zCoord + byte0)) {
				int j2 = this.getBlockId(qy1.xCoord, qy1.yCoord, qy1.zCoord);
				if(j2 == qy1.blockID && j2 > 0) {
					Block.blocksList[j2].updateTick(this, qy1.xCoord, qy1.yCoord, qy1.zCoord, this.rand);
				}
			}

		} else {
			if(this.checkChunksExist(i1 - byte0, j1 - byte0, k1 - byte0, i1 + byte0, j1 + byte0, k1 + byte0)) {
				if(l1 > 0) {
					qy1.setScheduledTime((long)i2 + this.worldInfo.getWorldTime());
				}

				if(!this.scheduledTickSet.contains(qy1)) {
					this.scheduledTickSet.add(qy1);
					this.scheduledTickTreeSet.add(qy1);
				}
			}

		}
	}

	public void updateEntities() {
		int iterator;
		Entity iterator1;
		for(iterator = 0; iterator < this.weatherEffects.size(); ++iterator) {
			iterator1 = (Entity)this.weatherEffects.get(iterator);
			iterator1.onUpdate();
			if(iterator1.isDead) {
				this.weatherEffects.remove(iterator--);
			}
		}

		this.loadedEntityList.removeAll(this.unloadedEntityList);

		int ow2;
		int lm2;
		for(iterator = 0; iterator < this.unloadedEntityList.size(); ++iterator) {
			iterator1 = (Entity)this.unloadedEntityList.get(iterator);
			ow2 = iterator1.chunkCoordX;
			lm2 = iterator1.chunkCoordZ;
			if(iterator1.addedToChunk && this.chunkExists(ow2, lm2)) {
				this.getChunkFromChunkCoords(ow2, lm2).removeEntity(iterator1);
			}
		}

		for(iterator = 0; iterator < this.unloadedEntityList.size(); ++iterator) {
			this.releaseEntitySkin((Entity)this.unloadedEntityList.get(iterator));
		}

		this.unloadedEntityList.clear();

		for(iterator = 0; iterator < this.loadedEntityList.size(); ++iterator) {
			iterator1 = (Entity)this.loadedEntityList.get(iterator);
			if(iterator1.ridingEntity != null) {
				if(!iterator1.ridingEntity.isDead && iterator1.ridingEntity.riddenByEntity == iterator1) {
					continue;
				}

				iterator1.ridingEntity.riddenByEntity = null;
				iterator1.ridingEntity = null;
			}

			if(!iterator1.isDead) {
				this.updateEntity(iterator1);
			}

			if(iterator1.isDead) {
				ow2 = iterator1.chunkCoordX;
				lm2 = iterator1.chunkCoordZ;
				if(iterator1.addedToChunk && this.chunkExists(ow2, lm2)) {
					this.getChunkFromChunkCoords(ow2, lm2).removeEntity(iterator1);
				}

				this.loadedEntityList.remove(iterator--);
				this.releaseEntitySkin(iterator1);
			}
		}

		this.field_31055_L = true;
		Iterator iterator10 = this.loadedTileEntityList.iterator();

		while(iterator10.hasNext()) {
			TileEntity tileEntity5 = (TileEntity)iterator10.next();
			if(!tileEntity5.func_31006_g()) {
				tileEntity5.updateEntity();
			}

			if(tileEntity5.func_31006_g()) {
				iterator10.remove();
				Chunk chunk7 = this.getChunkFromChunkCoords(tileEntity5.xCoord >> 4, tileEntity5.zCoord >> 4);
				if(chunk7 != null) {
					chunk7.removeChunkBlockTileEntity(tileEntity5.xCoord & 15, tileEntity5.yCoord, tileEntity5.zCoord & 15);
				}
			}
		}

		this.field_31055_L = false;
		if(!this.field_30900_E.isEmpty()) {
			Iterator iterator6 = this.field_30900_E.iterator();

			while(iterator6.hasNext()) {
				TileEntity tileEntity8 = (TileEntity)iterator6.next();
				if(!tileEntity8.func_31006_g()) {
					if(!this.loadedTileEntityList.contains(tileEntity8)) {
						this.loadedTileEntityList.add(tileEntity8);
					}

					Chunk chunk9 = this.getChunkFromChunkCoords(tileEntity8.xCoord >> 4, tileEntity8.zCoord >> 4);
					if(chunk9 != null) {
						chunk9.setChunkBlockTileEntity(tileEntity8.xCoord & 15, tileEntity8.yCoord, tileEntity8.zCoord & 15, tileEntity8);
					}

					this.markBlockNeedsUpdate(tileEntity8.xCoord, tileEntity8.yCoord, tileEntity8.zCoord);
				}
			}

			this.field_30900_E.clear();
		}

	}

	public void func_31054_a(Collection collection) {
		if(this.field_31055_L) {
			this.field_30900_E.addAll(collection);
		} else {
			this.loadedTileEntityList.addAll(collection);
		}

	}

	public void updateEntity(Entity sn1) {
		this.updateEntityWithOptionalForce(sn1, true);
	}

	public void updateEntityWithOptionalForce(Entity sn1, boolean flag) {
		int i1 = MathHelper.floor_double(sn1.posX);
		int j1 = MathHelper.floor_double(sn1.posZ);
		byte byte0 = 32;
		if(!flag || this.checkChunksExist(i1 - byte0, 0, j1 - byte0, i1 + byte0, 128, j1 + byte0)) {
			sn1.lastTickPosX = sn1.posX;
			sn1.lastTickPosY = sn1.posY;
			sn1.lastTickPosZ = sn1.posZ;
			sn1.prevRotationYaw = sn1.rotationYaw;
			sn1.prevRotationPitch = sn1.rotationPitch;
			if(flag && sn1.addedToChunk) {
				if(sn1.ridingEntity != null) {
					sn1.updateRidden();
				} else {
					sn1.onUpdate();
				}
			}

			if(Double.isNaN(sn1.posX) || Double.isInfinite(sn1.posX)) {
				sn1.posX = sn1.lastTickPosX;
			}

			if(Double.isNaN(sn1.posY) || Double.isInfinite(sn1.posY)) {
				sn1.posY = sn1.lastTickPosY;
			}

			if(Double.isNaN(sn1.posZ) || Double.isInfinite(sn1.posZ)) {
				sn1.posZ = sn1.lastTickPosZ;
			}

			if(Double.isNaN((double)sn1.rotationPitch) || Double.isInfinite((double)sn1.rotationPitch)) {
				sn1.rotationPitch = sn1.prevRotationPitch;
			}

			if(Double.isNaN((double)sn1.rotationYaw) || Double.isInfinite((double)sn1.rotationYaw)) {
				sn1.rotationYaw = sn1.prevRotationYaw;
			}

			int k1 = MathHelper.floor_double(sn1.posX / 16.0D);
			int l1 = MathHelper.floor_double(sn1.posY / 16.0D);
			int i2 = MathHelper.floor_double(sn1.posZ / 16.0D);
			if(!sn1.addedToChunk || sn1.chunkCoordX != k1 || sn1.chunkCoordY != l1 || sn1.chunkCoordZ != i2) {
				if(sn1.addedToChunk && this.chunkExists(sn1.chunkCoordX, sn1.chunkCoordZ)) {
					this.getChunkFromChunkCoords(sn1.chunkCoordX, sn1.chunkCoordZ).removeEntityAtIndex(sn1, sn1.chunkCoordY);
				}

				if(this.chunkExists(k1, i2)) {
					sn1.addedToChunk = true;
					this.getChunkFromChunkCoords(k1, i2).addEntity(sn1);
				} else {
					sn1.addedToChunk = false;
				}
			}

			if(flag && sn1.addedToChunk && sn1.riddenByEntity != null) {
				if(!sn1.riddenByEntity.isDead && sn1.riddenByEntity.ridingEntity == sn1) {
					this.updateEntity(sn1.riddenByEntity);
				} else {
					sn1.riddenByEntity.ridingEntity = null;
					sn1.riddenByEntity = null;
				}
			}

		}
	}

	public boolean checkIfAABBIsClear(AxisAlignedBB eq1) {
		List list = this.getEntitiesWithinAABBExcludingEntity((Entity)null, eq1);

		for(int i1 = 0; i1 < list.size(); ++i1) {
			Entity sn1 = (Entity)list.get(i1);
			if(!sn1.isDead && sn1.preventEntitySpawning) {
				return false;
			}
		}

		return true;
	}

	public boolean getIsAnyLiquid(AxisAlignedBB eq1) {
		int i1 = MathHelper.floor_double(eq1.minX);
		int j1 = MathHelper.floor_double(eq1.maxX + 1.0D);
		int k1 = MathHelper.floor_double(eq1.minY);
		int l1 = MathHelper.floor_double(eq1.maxY + 1.0D);
		int i2 = MathHelper.floor_double(eq1.minZ);
		int j2 = MathHelper.floor_double(eq1.maxZ + 1.0D);
		if(eq1.minX < 0.0D) {
			--i1;
		}

		if(eq1.minY < 0.0D) {
			--k1;
		}

		if(eq1.minZ < 0.0D) {
			--i2;
		}

		for(int k2 = i1; k2 < j1; ++k2) {
			for(int l2 = k1; l2 < l1; ++l2) {
				for(int i3 = i2; i3 < j2; ++i3) {
					Block uu1 = Block.blocksList[this.getBlockId(k2, l2, i3)];
					if(uu1 != null && uu1.blockMaterial.getIsLiquid()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean isBoundingBoxBurning(AxisAlignedBB eq1) {
		int i1 = MathHelper.floor_double(eq1.minX);
		int j1 = MathHelper.floor_double(eq1.maxX + 1.0D);
		int k1 = MathHelper.floor_double(eq1.minY);
		int l1 = MathHelper.floor_double(eq1.maxY + 1.0D);
		int i2 = MathHelper.floor_double(eq1.minZ);
		int j2 = MathHelper.floor_double(eq1.maxZ + 1.0D);
		if(this.checkChunksExist(i1, k1, i2, j1, l1, j2)) {
			for(int k2 = i1; k2 < j1; ++k2) {
				for(int l2 = k1; l2 < l1; ++l2) {
					for(int i3 = i2; i3 < j2; ++i3) {
						int j3 = this.getBlockId(k2, l2, i3);
						if(j3 == Block.fire.blockID || j3 == Block.lavaMoving.blockID || j3 == Block.lavaStill.blockID) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public boolean handleMaterialAcceleration(AxisAlignedBB eq1, Material ln1, Entity sn1) {
		int i1 = MathHelper.floor_double(eq1.minX);
		int j1 = MathHelper.floor_double(eq1.maxX + 1.0D);
		int k1 = MathHelper.floor_double(eq1.minY);
		int l1 = MathHelper.floor_double(eq1.maxY + 1.0D);
		int i2 = MathHelper.floor_double(eq1.minZ);
		int j2 = MathHelper.floor_double(eq1.maxZ + 1.0D);
		if(!this.checkChunksExist(i1, k1, i2, j1, l1, j2)) {
			return false;
		} else {
			boolean flag = false;
			Vec3D bt1 = Vec3D.createVector(0.0D, 0.0D, 0.0D);

			for(int d1 = i1; d1 < j1; ++d1) {
				for(int l2 = k1; l2 < l1; ++l2) {
					for(int i3 = i2; i3 < j2; ++i3) {
						Block uu1 = Block.blocksList[this.getBlockId(d1, l2, i3)];
						if(uu1 != null && uu1.blockMaterial == ln1) {
							double d2 = (double)((float)(l2 + 1) - BlockFluid.getPercentAir(this.getBlockMetadata(d1, l2, i3)));
							if((double)l1 >= d2) {
								flag = true;
								uu1.velocityToAddToEntity(this, d1, l2, i3, sn1, bt1);
							}
						}
					}
				}
			}

			if(bt1.lengthVector() > 0.0D) {
				bt1 = bt1.normalize();
				double d18 = 0.014D;
				sn1.motionX += bt1.xCoord * d18;
				sn1.motionY += bt1.yCoord * d18;
				sn1.motionZ += bt1.zCoord * d18;
			}

			return flag;
		}
	}

	public boolean isMaterialInBB(AxisAlignedBB eq1, Material ln1) {
		int i1 = MathHelper.floor_double(eq1.minX);
		int j1 = MathHelper.floor_double(eq1.maxX + 1.0D);
		int k1 = MathHelper.floor_double(eq1.minY);
		int l1 = MathHelper.floor_double(eq1.maxY + 1.0D);
		int i2 = MathHelper.floor_double(eq1.minZ);
		int j2 = MathHelper.floor_double(eq1.maxZ + 1.0D);

		for(int k2 = i1; k2 < j1; ++k2) {
			for(int l2 = k1; l2 < l1; ++l2) {
				for(int i3 = i2; i3 < j2; ++i3) {
					Block uu1 = Block.blocksList[this.getBlockId(k2, l2, i3)];
					if(uu1 != null && uu1.blockMaterial == ln1) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean isAABBInMaterial(AxisAlignedBB eq1, Material ln1) {
		int i1 = MathHelper.floor_double(eq1.minX);
		int j1 = MathHelper.floor_double(eq1.maxX + 1.0D);
		int k1 = MathHelper.floor_double(eq1.minY);
		int l1 = MathHelper.floor_double(eq1.maxY + 1.0D);
		int i2 = MathHelper.floor_double(eq1.minZ);
		int j2 = MathHelper.floor_double(eq1.maxZ + 1.0D);

		for(int k2 = i1; k2 < j1; ++k2) {
			for(int l2 = k1; l2 < l1; ++l2) {
				for(int i3 = i2; i3 < j2; ++i3) {
					Block uu1 = Block.blocksList[this.getBlockId(k2, l2, i3)];
					if(uu1 != null && uu1.blockMaterial == ln1) {
						int j3 = this.getBlockMetadata(k2, l2, i3);
						double d1 = (double)(l2 + 1);
						if(j3 < 8) {
							d1 = (double)(l2 + 1) - (double)j3 / 8.0D;
						}

						if(d1 >= eq1.minY) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public Explosion createExplosion(Entity sn1, double d1, double d2, double d3, float f1) {
		return this.newExplosion(sn1, d1, d2, d3, f1, false);
	}

	public Explosion newExplosion(Entity sn1, double d1, double d2, double d3, float f1, boolean flag) {
		Explosion qx1 = new Explosion(this, sn1, d1, d2, d3, f1);
		qx1.isFlaming = flag;
		qx1.doExplosionA();
		qx1.doExplosionB(true);
		return qx1;
	}

	public float func_675_a(Vec3D bt1, AxisAlignedBB eq1) {
		double d1 = 1.0D / ((eq1.maxX - eq1.minX) * 2.0D + 1.0D);
		double d2 = 1.0D / ((eq1.maxY - eq1.minY) * 2.0D + 1.0D);
		double d3 = 1.0D / ((eq1.maxZ - eq1.minZ) * 2.0D + 1.0D);
		int i1 = 0;
		int j1 = 0;

		for(float f1 = 0.0F; f1 <= 1.0F; f1 = (float)((double)f1 + d1)) {
			for(float f2 = 0.0F; f2 <= 1.0F; f2 = (float)((double)f2 + d2)) {
				for(float f3 = 0.0F; f3 <= 1.0F; f3 = (float)((double)f3 + d3)) {
					double d4 = eq1.minX + (eq1.maxX - eq1.minX) * (double)f1;
					double d5 = eq1.minY + (eq1.maxY - eq1.minY) * (double)f2;
					double d6 = eq1.minZ + (eq1.maxZ - eq1.minZ) * (double)f3;
					if(this.rayTraceBlocks(Vec3D.createVector(d4, d5, d6), bt1) == null) {
						++i1;
					}

					++j1;
				}
			}
		}

		return (float)i1 / (float)j1;
	}

	public void onBlockHit(EntityPlayer gs1, int i1, int j1, int k1, int l1) {
		if(l1 == 0) {
			--j1;
		}

		if(l1 == 1) {
			++j1;
		}

		if(l1 == 2) {
			--k1;
		}

		if(l1 == 3) {
			++k1;
		}

		if(l1 == 4) {
			--i1;
		}

		if(l1 == 5) {
			++i1;
		}

		if(this.getBlockId(i1, j1, k1) == Block.fire.blockID) {
			this.func_28107_a(gs1, 1004, i1, j1, k1, 0);
			this.setBlockWithNotify(i1, j1, k1, 0);
		}

	}

	public Entity func_4085_a(Class class1) {
		return null;
	}

	public String func_687_d() {
		return "All: " + this.loadedEntityList.size();
	}

	public String func_21119_g() {
		return this.chunkProvider.makeString();
	}

	public TileEntity getBlockTileEntity(int i1, int j1, int k1) {
		Chunk lm1 = this.getChunkFromChunkCoords(i1 >> 4, k1 >> 4);
		return lm1 != null ? lm1.getChunkBlockTileEntity(i1 & 15, j1, k1 & 15) : null;
	}

	public void setBlockTileEntity(int i1, int j1, int k1, TileEntity ow1) {
		if(!ow1.func_31006_g()) {
			if(this.field_31055_L) {
				ow1.xCoord = i1;
				ow1.yCoord = j1;
				ow1.zCoord = k1;
				this.field_30900_E.add(ow1);
			} else {
				this.loadedTileEntityList.add(ow1);
				Chunk lm1 = this.getChunkFromChunkCoords(i1 >> 4, k1 >> 4);
				if(lm1 != null) {
					lm1.setChunkBlockTileEntity(i1 & 15, j1, k1 & 15, ow1);
				}
			}
		}

	}

	public void removeBlockTileEntity(int i1, int j1, int k1) {
		TileEntity ow1 = this.getBlockTileEntity(i1, j1, k1);
		if(ow1 != null && this.field_31055_L) {
			ow1.func_31005_i();
		} else {
			if(ow1 != null) {
				this.loadedTileEntityList.remove(ow1);
			}

			Chunk lm1 = this.getChunkFromChunkCoords(i1 >> 4, k1 >> 4);
			if(lm1 != null) {
				lm1.removeChunkBlockTileEntity(i1 & 15, j1, k1 & 15);
			}
		}

	}

	public boolean isBlockOpaqueCube(int i1, int j1, int k1) {
		Block uu1 = Block.blocksList[this.getBlockId(i1, j1, k1)];
		return uu1 == null ? false : uu1.isOpaqueCube();
	}

	public boolean isBlockNormalCube(int i1, int j1, int k1) {
		Block uu1 = Block.blocksList[this.getBlockId(i1, j1, k1)];
		return uu1 == null ? false : uu1.blockMaterial.getIsTranslucent() && uu1.renderAsNormalBlock();
	}

	public void saveWorldIndirectly(IProgressUpdate yb1) {
		this.saveWorld(true, yb1);
	}

	public boolean updatingLighting() {
		if(this.lightingUpdatesCounter >= 50) {
			return false;
		} else {
			++this.lightingUpdatesCounter;

			try {
				int i1 = 500;

				boolean flag1;
				boolean z4;
				while(this.lightingToUpdate.size() > 0) {
					--i1;
					if(i1 <= 0) {
						flag1 = true;
						z4 = flag1;
						return z4;
					}

					((MetadataChunkBlock)this.lightingToUpdate.remove(this.lightingToUpdate.size() - 1)).func_4127_a(this);
				}

				flag1 = false;
				z4 = flag1;
				return z4;
			} finally {
				--this.lightingUpdatesCounter;
			}
		}
	}

	public void scheduleLightingUpdate(EnumSkyBlock eb1, int i1, int j1, int k1, int l1, int i2, int j2) {
		this.scheduleLightingUpdate_do(eb1, i1, j1, k1, l1, i2, j2, true);
	}

	public void scheduleLightingUpdate_do(EnumSkyBlock eb1, int i1, int j1, int k1, int l1, int i2, int j2, boolean flag) {
		if(!this.worldProvider.hasNoSky || eb1 != EnumSkyBlock.Sky) {
			++lightingUpdatesScheduled;

			try {
				if(lightingUpdatesScheduled == 50) {
					return;
				}

				int k2 = (l1 + i1) / 2;
				int l2 = (j2 + k1) / 2;
				if(!this.blockExists(k2, 64, l2)) {
					return;
				}

				if(this.getChunkFromBlockCoords(k2, l2).func_21167_h()) {
					return;
				}

				int i3 = this.lightingToUpdate.size();
				int k3;
				if(flag) {
					k3 = 5;
					if(k3 > i3) {
						k3 = i3;
					}

					for(int l3 = 0; l3 < k3; ++l3) {
						MetadataChunkBlock st1 = (MetadataChunkBlock)this.lightingToUpdate.get(this.lightingToUpdate.size() - l3 - 1);
						if(st1.field_1299_a == eb1 && st1.func_866_a(i1, j1, k1, l1, i2, j2)) {
							return;
						}
					}
				}

				this.lightingToUpdate.add(new MetadataChunkBlock(eb1, i1, j1, k1, l1, i2, j2));
				k3 = 1000000;
				if(this.lightingToUpdate.size() > 1000000) {
					System.out.println("More than " + k3 + " updates, aborting lighting updates");
					this.lightingToUpdate.clear();
				}
			} finally {
				--lightingUpdatesScheduled;
			}

		}
	}

	public void calculateInitialSkylight() {
		int i1 = this.calculateSkylightSubtracted(1.0F);
		if(i1 != this.skylightSubtracted) {
			this.skylightSubtracted = i1;
		}

	}

	public void setAllowedMobSpawns(boolean flag, boolean flag1) {
		this.spawnHostileMobs = flag;
		this.spawnPeacefulMobs = flag1;
	}

	public void tick() {
		this.updateWeather();
		long l2;
		if(this.isAllPlayersFullyAsleep()) {
			boolean i1 = false;
			if(this.spawnHostileMobs && this.difficultySetting >= 1) {
				i1 = SpawnerAnimals.performSleepSpawning(this, this.playerEntities);
			}

			if(!i1) {
				l2 = this.worldInfo.getWorldTime() + 24000L;
				this.worldInfo.setWorldTime(l2 - l2 % 24000L);
				this.wakeUpAllPlayers();
			}
		}

		SpawnerAnimals.performSpawning(this, this.spawnHostileMobs, this.spawnPeacefulMobs);
		this.chunkProvider.unload100OldestChunks();
		int i4 = this.calculateSkylightSubtracted(1.0F);
		if(i4 != this.skylightSubtracted) {
			this.skylightSubtracted = i4;

			for(int i5 = 0; i5 < this.worldAccesses.size(); ++i5) {
				((IWorldAccess)this.worldAccesses.get(i5)).updateAllRenderers();
			}
		}

		l2 = this.worldInfo.getWorldTime() + 1L;
		if(l2 % (long)this.autosavePeriod == 0L) {
			this.saveWorld(false, (IProgressUpdate)null);
		}

		this.worldInfo.setWorldTime(l2);
		this.TickUpdates(false);
		this.updateBlocksAndPlayCaveSounds();
	}

	private void func_27163_E() {
		if(this.worldInfo.getRaining()) {
			this.rainingStrength = 1.0F;
			if(this.worldInfo.getThundering()) {
				this.thunderingStrength = 1.0F;
			}
		}

	}

	protected void updateWeather() {
		if(!this.worldProvider.hasNoSky) {
			if(this.field_27168_F > 0) {
				--this.field_27168_F;
			}

			int i1 = this.worldInfo.getThunderTime();
			if(i1 <= 0) {
				if(this.worldInfo.getThundering()) {
					this.worldInfo.setThunderTime(this.rand.nextInt(12000) + 3600);
				} else {
					this.worldInfo.setThunderTime(this.rand.nextInt(168000) + 12000);
				}
			} else {
				--i1;
				this.worldInfo.setThunderTime(i1);
				if(i1 <= 0) {
					this.worldInfo.setThundering(!this.worldInfo.getThundering());
				}
			}

			int j1 = this.worldInfo.getRainTime();
			if(j1 <= 0) {
				if(this.worldInfo.getRaining()) {
					this.worldInfo.setRainTime(this.rand.nextInt(12000) + 12000);
				} else {
					this.worldInfo.setRainTime(this.rand.nextInt(168000) + 12000);
				}
			} else {
				--j1;
				this.worldInfo.setRainTime(j1);
				if(j1 <= 0) {
					this.worldInfo.setRaining(!this.worldInfo.getRaining());
				}
			}

			this.prevRainingStrength = this.rainingStrength;
			if(this.worldInfo.getRaining()) {
				this.rainingStrength = (float)((double)this.rainingStrength + 0.01D);
			} else {
				this.rainingStrength = (float)((double)this.rainingStrength - 0.01D);
			}

			if(this.rainingStrength < 0.0F) {
				this.rainingStrength = 0.0F;
			}

			if(this.rainingStrength > 1.0F) {
				this.rainingStrength = 1.0F;
			}

			this.prevThunderingStrength = this.thunderingStrength;
			if(this.worldInfo.getThundering()) {
				this.thunderingStrength = (float)((double)this.thunderingStrength + 0.01D);
			} else {
				this.thunderingStrength = (float)((double)this.thunderingStrength - 0.01D);
			}

			if(this.thunderingStrength < 0.0F) {
				this.thunderingStrength = 0.0F;
			}

			if(this.thunderingStrength > 1.0F) {
				this.thunderingStrength = 1.0F;
			}

		}
	}

	private void stopPrecipitation() {
		this.worldInfo.setRainTime(0);
		this.worldInfo.setRaining(false);
		this.worldInfo.setThunderTime(0);
		this.worldInfo.setThundering(false);
	}

	protected void updateBlocksAndPlayCaveSounds() {
		this.positionsToUpdate.clear();

		int k1;
		int i2;
		int j3;
		int k4;
		for(int iterator = 0; iterator < this.playerEntities.size(); ++iterator) {
			EntityPlayer yy1 = (EntityPlayer)this.playerEntities.get(iterator);
			k1 = MathHelper.floor_double(yy1.posX / 16.0D);
			i2 = MathHelper.floor_double(yy1.posZ / 16.0D);
			byte lm1 = 9;

			for(j3 = -lm1; j3 <= lm1; ++j3) {
				for(k4 = -lm1; k4 <= lm1; ++k4) {
					this.positionsToUpdate.add(new ChunkCoordIntPair(j3 + k1, k4 + i2));
				}
			}
		}

		if(this.soundCounter > 0) {
			--this.soundCounter;
		}

		Iterator iterator12 = this.positionsToUpdate.iterator();

		while(iterator12.hasNext()) {
			ChunkCoordIntPair chunkCoordIntPair13 = (ChunkCoordIntPair)iterator12.next();
			k1 = chunkCoordIntPair13.chunkXPos * 16;
			i2 = chunkCoordIntPair13.chunkZPos * 16;
			Chunk chunk14 = this.getChunkFromChunkCoords(chunkCoordIntPair13.chunkXPos, chunkCoordIntPair13.chunkZPos);
			int k5;
			int k6;
			int j7;
			if(this.soundCounter == 0) {
				this.field_9437_g = this.field_9437_g * 3 + 1013904223;
				j3 = this.field_9437_g >> 2;
				k4 = j3 & 15;
				k5 = j3 >> 8 & 15;
				k6 = j3 >> 16 & 127;
				j7 = chunk14.getBlockID(k4, k6, k5);
				k4 += k1;
				k5 += i2;
				if(j7 == 0 && this.getFullBlockLightValue(k4, k6, k5) <= this.rand.nextInt(8) && this.getSavedLightValue(EnumSkyBlock.Sky, k4, k6, k5) <= 0) {
					EntityPlayer l7 = this.getClosestPlayer((double)k4 + 0.5D, (double)k6 + 0.5D, (double)k5 + 0.5D, 8.0D);
					if(l7 != null && l7.getDistanceSq((double)k4 + 0.5D, (double)k6 + 0.5D, (double)k5 + 0.5D) > 4.0D) {
						this.playSoundEffect((double)k4 + 0.5D, (double)k6 + 0.5D, (double)k5 + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.rand.nextFloat() * 0.2F);
						this.soundCounter = this.rand.nextInt(12000) + 6000;
					}
				}
			}

			if(this.rand.nextInt(100000) == 0 && this.func_27161_C() && this.func_27160_B()) {
				this.field_9437_g = this.field_9437_g * 3 + 1013904223;
				j3 = this.field_9437_g >> 2;
				k4 = k1 + (j3 & 15);
				k5 = i2 + (j3 >> 8 & 15);
				k6 = this.findTopSolidBlock(k4, k5);
				if(this.canBlockBeRainedOn(k4, k6, k5)) {
					this.addWeatherEffect(new EntityLightningBolt(this, (double)k4, (double)k6, (double)k5));
					this.field_27168_F = 2;
				}
			}

			int i15;
			if(this.rand.nextInt(16) == 0) {
				this.field_9437_g = this.field_9437_g * 3 + 1013904223;
				j3 = this.field_9437_g >> 2;
				k4 = j3 & 15;
				k5 = j3 >> 8 & 15;
				k6 = this.findTopSolidBlock(k4 + k1, k5 + i2);
				if(this.getWorldChunkManager().getBiomeGenAt(k4 + k1, k5 + i2).getEnableSnow() && k6 >= 0 && k6 < 128 && chunk14.getSavedLightValue(EnumSkyBlock.Block, k4, k6, k5) < 10) {
					j7 = chunk14.getBlockID(k4, k6 - 1, k5);
					i15 = chunk14.getBlockID(k4, k6, k5);
					if(this.func_27161_C() && i15 == 0 && Block.snow.canPlaceBlockAt(this, k4 + k1, k6, k5 + i2) && j7 != 0 && j7 != Block.ice.blockID && Block.blocksList[j7].blockMaterial.getIsSolid()) {
						this.setBlockWithNotify(k4 + k1, k6, k5 + i2, Block.snow.blockID);
					}

					if(j7 == Block.waterStill.blockID && chunk14.getBlockMetadata(k4, k6 - 1, k5) == 0) {
						this.setBlockWithNotify(k4 + k1, k6 - 1, k5 + i2, Block.ice.blockID);
					}
				}
			}

			for(j3 = 0; j3 < 80; ++j3) {
				this.field_9437_g = this.field_9437_g * 3 + 1013904223;
				k4 = this.field_9437_g >> 2;
				k5 = k4 & 15;
				k6 = k4 >> 8 & 15;
				j7 = k4 >> 16 & 127;
				i15 = chunk14.blocks[k5 << 11 | k6 << 7 | j7] & 255;
				if(Block.tickOnLoad[i15]) {
					Block.blocksList[i15].updateTick(this, k5 + k1, j7, k6 + i2, this.rand);
				}
			}
		}

	}

	public boolean TickUpdates(boolean flag) {
		int i1 = this.scheduledTickTreeSet.size();
		if(i1 != this.scheduledTickSet.size()) {
			throw new IllegalStateException("TickNextTick list out of synch");
		} else {
			if(i1 > 1000) {
				i1 = 1000;
			}

			for(int j1 = 0; j1 < i1; ++j1) {
				NextTickListEntry qy1 = (NextTickListEntry)this.scheduledTickTreeSet.first();
				if(!flag && qy1.scheduledTime > this.worldInfo.getWorldTime()) {
					break;
				}

				this.scheduledTickTreeSet.remove(qy1);
				this.scheduledTickSet.remove(qy1);
				byte byte0 = 8;
				if(this.checkChunksExist(qy1.xCoord - byte0, qy1.yCoord - byte0, qy1.zCoord - byte0, qy1.xCoord + byte0, qy1.yCoord + byte0, qy1.zCoord + byte0)) {
					int k1 = this.getBlockId(qy1.xCoord, qy1.yCoord, qy1.zCoord);
					if(k1 == qy1.blockID && k1 > 0) {
						Block.blocksList[k1].updateTick(this, qy1.xCoord, qy1.yCoord, qy1.zCoord, this.rand);
					}
				}
			}

			return this.scheduledTickTreeSet.size() != 0;
		}
	}

	public void randomDisplayUpdates(int i1, int j1, int k1) {
		byte byte0 = 16;
		Random random = new Random();

		for(int l1 = 0; l1 < 1000; ++l1) {
			int i2 = i1 + this.rand.nextInt(byte0) - this.rand.nextInt(byte0);
			int j2 = j1 + this.rand.nextInt(byte0) - this.rand.nextInt(byte0);
			int k2 = k1 + this.rand.nextInt(byte0) - this.rand.nextInt(byte0);
			int l2 = this.getBlockId(i2, j2, k2);
			if(l2 > 0) {
				Block.blocksList[l2].randomDisplayTick(this, i2, j2, k2, random);
			}
		}

	}

	public List getEntitiesWithinAABBExcludingEntity(Entity sn1, AxisAlignedBB eq1) {
		this.field_1012_M.clear();
		int i1 = MathHelper.floor_double((eq1.minX - 2.0D) / 16.0D);
		int j1 = MathHelper.floor_double((eq1.maxX + 2.0D) / 16.0D);
		int k1 = MathHelper.floor_double((eq1.minZ - 2.0D) / 16.0D);
		int l1 = MathHelper.floor_double((eq1.maxZ + 2.0D) / 16.0D);

		for(int i2 = i1; i2 <= j1; ++i2) {
			for(int j2 = k1; j2 <= l1; ++j2) {
				if(this.chunkExists(i2, j2)) {
					this.getChunkFromChunkCoords(i2, j2).getEntitiesWithinAABBForEntity(sn1, eq1, this.field_1012_M);
				}
			}
		}

		return this.field_1012_M;
	}

	public List getEntitiesWithinAABB(Class class1, AxisAlignedBB eq1) {
		int i1 = MathHelper.floor_double((eq1.minX - 2.0D) / 16.0D);
		int j1 = MathHelper.floor_double((eq1.maxX + 2.0D) / 16.0D);
		int k1 = MathHelper.floor_double((eq1.minZ - 2.0D) / 16.0D);
		int l1 = MathHelper.floor_double((eq1.maxZ + 2.0D) / 16.0D);
		ArrayList arraylist = new ArrayList();

		for(int i2 = i1; i2 <= j1; ++i2) {
			for(int j2 = k1; j2 <= l1; ++j2) {
				if(this.chunkExists(i2, j2)) {
					this.getChunkFromChunkCoords(i2, j2).getEntitiesOfTypeWithinAAAB(class1, eq1, arraylist);
				}
			}
		}

		return arraylist;
	}

	public List getLoadedEntityList() {
		return this.loadedEntityList;
	}

	public void func_698_b(int i1, int j1, int k1, TileEntity ow1) {
		if(this.blockExists(i1, j1, k1)) {
			this.getChunkFromBlockCoords(i1, k1).setChunkModified();
		}

		for(int l1 = 0; l1 < this.worldAccesses.size(); ++l1) {
			((IWorldAccess)this.worldAccesses.get(l1)).doNothingWithTileEntity(i1, j1, k1, ow1);
		}

	}

	public int countEntities(Class class1) {
		int i1 = 0;

		for(int j1 = 0; j1 < this.loadedEntityList.size(); ++j1) {
			Entity sn1 = (Entity)this.loadedEntityList.get(j1);
			if(class1.isAssignableFrom(sn1.getClass())) {
				++i1;
			}
		}

		return i1;
	}

	public void func_636_a(List list) {
		this.loadedEntityList.addAll(list);

		for(int i1 = 0; i1 < list.size(); ++i1) {
			this.obtainEntitySkin((Entity)list.get(i1));
		}

	}

	public void func_632_b(List list) {
		this.unloadedEntityList.addAll(list);
	}

	public void func_656_j() {
		while(this.chunkProvider.unload100OldestChunks()) {
		}

	}

	public boolean canBlockBePlacedAt(int i1, int j1, int k1, int l1, boolean flag, int i2) {
		int j2 = this.getBlockId(j1, k1, l1);
		Block uu1 = Block.blocksList[j2];
		Block uu2 = Block.blocksList[i1];
		AxisAlignedBB eq1 = uu2.getCollisionBoundingBoxFromPool(this, j1, k1, l1);
		if(flag) {
			eq1 = null;
		}

		if(eq1 != null && !this.checkIfAABBIsClear(eq1)) {
			return false;
		} else {
			if(uu1 == Block.waterMoving || uu1 == Block.waterStill || uu1 == Block.lavaMoving || uu1 == Block.lavaStill || uu1 == Block.fire || uu1 == Block.snow) {
				uu1 = null;
			}

			return i1 > 0 && uu1 == null && uu2.canPlaceBlockOnSide(this, j1, k1, l1, i2);
		}
	}

	public PathEntity getPathToEntity(Entity sn1, Entity sn2, float f1) {
		int i1 = MathHelper.floor_double(sn1.posX);
		int j1 = MathHelper.floor_double(sn1.posY);
		int k1 = MathHelper.floor_double(sn1.posZ);
		int l1 = (int)(f1 + 16.0F);
		int i2 = i1 - l1;
		int j2 = j1 - l1;
		int k2 = k1 - l1;
		int l2 = i1 + l1;
		int i3 = j1 + l1;
		int j3 = k1 + l1;
		ChunkCache ew1 = new ChunkCache(this, i2, j2, k2, l2, i3, j3);
		return (new Pathfinder(ew1)).createEntityPathTo(sn1, sn2, f1);
	}

	public PathEntity getEntityPathToXYZ(Entity sn1, int i1, int j1, int k1, float f1) {
		int l1 = MathHelper.floor_double(sn1.posX);
		int i2 = MathHelper.floor_double(sn1.posY);
		int j2 = MathHelper.floor_double(sn1.posZ);
		int k2 = (int)(f1 + 8.0F);
		int l2 = l1 - k2;
		int i3 = i2 - k2;
		int j3 = j2 - k2;
		int k3 = l1 + k2;
		int l3 = i2 + k2;
		int i4 = j2 + k2;
		ChunkCache ew1 = new ChunkCache(this, l2, i3, j3, k3, l3, i4);
		return (new Pathfinder(ew1)).createEntityPathTo(sn1, i1, j1, k1, f1);
	}

	public boolean isBlockProvidingPowerTo(int i1, int j1, int k1, int l1) {
		int i2 = this.getBlockId(i1, j1, k1);
		return i2 == 0 ? false : Block.blocksList[i2].isIndirectlyPoweringTo(this, i1, j1, k1, l1);
	}

	public boolean isBlockGettingPowered(int i1, int j1, int k1) {
		return this.isBlockProvidingPowerTo(i1, j1 - 1, k1, 0) ? true : (this.isBlockProvidingPowerTo(i1, j1 + 1, k1, 1) ? true : (this.isBlockProvidingPowerTo(i1, j1, k1 - 1, 2) ? true : (this.isBlockProvidingPowerTo(i1, j1, k1 + 1, 3) ? true : (this.isBlockProvidingPowerTo(i1 - 1, j1, k1, 4) ? true : this.isBlockProvidingPowerTo(i1 + 1, j1, k1, 5)))));
	}

	public boolean isBlockIndirectlyProvidingPowerTo(int i1, int j1, int k1, int l1) {
		if(this.isBlockNormalCube(i1, j1, k1)) {
			return this.isBlockGettingPowered(i1, j1, k1);
		} else {
			int i2 = this.getBlockId(i1, j1, k1);
			return i2 == 0 ? false : Block.blocksList[i2].isPoweringTo(this, i1, j1, k1, l1);
		}
	}

	public boolean isBlockIndirectlyGettingPowered(int i1, int j1, int k1) {
		return this.isBlockIndirectlyProvidingPowerTo(i1, j1 - 1, k1, 0) ? true : (this.isBlockIndirectlyProvidingPowerTo(i1, j1 + 1, k1, 1) ? true : (this.isBlockIndirectlyProvidingPowerTo(i1, j1, k1 - 1, 2) ? true : (this.isBlockIndirectlyProvidingPowerTo(i1, j1, k1 + 1, 3) ? true : (this.isBlockIndirectlyProvidingPowerTo(i1 - 1, j1, k1, 4) ? true : this.isBlockIndirectlyProvidingPowerTo(i1 + 1, j1, k1, 5)))));
	}

	public EntityPlayer getClosestPlayerToEntity(Entity sn1, double d1) {
		return this.getClosestPlayer(sn1.posX, sn1.posY, sn1.posZ, d1);
	}

	public EntityPlayer getClosestPlayer(double d1, double d2, double d3, double d4) {
		double d5 = -1.0D;
		EntityPlayer gs1 = null;

		for(int i1 = 0; i1 < this.playerEntities.size(); ++i1) {
			EntityPlayer gs2 = (EntityPlayer)this.playerEntities.get(i1);
			double d6 = gs2.getDistanceSq(d1, d2, d3);
			if((d4 < 0.0D || d6 < d4 * d4) && (d5 == -1.0D || d6 < d5)) {
				d5 = d6;
				gs1 = gs2;
			}
		}

		return gs1;
	}

	public EntityPlayer getPlayerEntityByName(String s1) {
		for(int i1 = 0; i1 < this.playerEntities.size(); ++i1) {
			if(s1.equals(((EntityPlayer)this.playerEntities.get(i1)).username)) {
				return (EntityPlayer)this.playerEntities.get(i1);
			}
		}

		return null;
	}

	public void setChunkData(int i1, int j1, int k1, int l1, int i2, int j2, byte[] abyte0) {
		int k2 = i1 >> 4;
		int l2 = k1 >> 4;
		int i3 = i1 + l1 - 1 >> 4;
		int j3 = k1 + j2 - 1 >> 4;
		int k3 = 0;
		int l3 = j1;
		int i4 = j1 + i2;
		if(j1 < 0) {
			l3 = 0;
		}

		if(i4 > 128) {
			i4 = 128;
		}

		for(int j4 = k2; j4 <= i3; ++j4) {
			int k4 = i1 - j4 * 16;
			int l4 = i1 + l1 - j4 * 16;
			if(k4 < 0) {
				k4 = 0;
			}

			if(l4 > 16) {
				l4 = 16;
			}

			for(int i5 = l2; i5 <= j3; ++i5) {
				int j5 = k1 - i5 * 16;
				int k5 = k1 + j2 - i5 * 16;
				if(j5 < 0) {
					j5 = 0;
				}

				if(k5 > 16) {
					k5 = 16;
				}

				k3 = this.getChunkFromChunkCoords(j4, i5).setChunkData(abyte0, k4, l3, j5, l4, i4, k5, k3);
				this.markBlocksDirty(j4 * 16 + k4, l3, i5 * 16 + j5, j4 * 16 + l4, i4, i5 * 16 + k5);
			}
		}

	}

	public void sendQuittingDisconnectingPacket() {
	}

	public void checkSessionLock() {
		this.saveHandler.func_22150_b();
	}

	public void setWorldTime(long l1) {
		this.worldInfo.setWorldTime(l1);
	}

	public long getRandomSeed() {
		return this.worldInfo.getRandomSeed();
	}

	public long getWorldTime() {
		return this.worldInfo.getWorldTime();
	}

	public ChunkCoordinates getSpawnPoint() {
		return new ChunkCoordinates(this.worldInfo.getSpawnX(), this.worldInfo.getSpawnY(), this.worldInfo.getSpawnZ());
	}

	public void setSpawnPoint(ChunkCoordinates br1) {
		this.worldInfo.setSpawn(br1.x, br1.y, br1.z);
	}

	public void joinEntityInSurroundings(Entity sn1) {
		int i1 = MathHelper.floor_double(sn1.posX / 16.0D);
		int j1 = MathHelper.floor_double(sn1.posZ / 16.0D);
		byte byte0 = 2;

		for(int k1 = i1 - byte0; k1 <= i1 + byte0; ++k1) {
			for(int l1 = j1 - byte0; l1 <= j1 + byte0; ++l1) {
				this.getChunkFromChunkCoords(k1, l1);
			}
		}

		if(!this.loadedEntityList.contains(sn1)) {
			this.loadedEntityList.add(sn1);
		}

	}

	public boolean func_6466_a(EntityPlayer gs1, int i1, int j1, int k1) {
		return true;
	}

	public void func_9425_a(Entity sn1, byte byte0) {
	}

	public void updateEntityList() {
		this.loadedEntityList.removeAll(this.unloadedEntityList);

		int k1;
		Entity sn2;
		int i2;
		int k2;
		for(k1 = 0; k1 < this.unloadedEntityList.size(); ++k1) {
			sn2 = (Entity)this.unloadedEntityList.get(k1);
			i2 = sn2.chunkCoordX;
			k2 = sn2.chunkCoordZ;
			if(sn2.addedToChunk && this.chunkExists(i2, k2)) {
				this.getChunkFromChunkCoords(i2, k2).removeEntity(sn2);
			}
		}

		for(k1 = 0; k1 < this.unloadedEntityList.size(); ++k1) {
			this.releaseEntitySkin((Entity)this.unloadedEntityList.get(k1));
		}

		this.unloadedEntityList.clear();

		for(k1 = 0; k1 < this.loadedEntityList.size(); ++k1) {
			sn2 = (Entity)this.loadedEntityList.get(k1);
			if(sn2.ridingEntity != null) {
				if(!sn2.ridingEntity.isDead && sn2.ridingEntity.riddenByEntity == sn2) {
					continue;
				}

				sn2.ridingEntity.riddenByEntity = null;
				sn2.ridingEntity = null;
			}

			if(sn2.isDead) {
				i2 = sn2.chunkCoordX;
				k2 = sn2.chunkCoordZ;
				if(sn2.addedToChunk && this.chunkExists(i2, k2)) {
					this.getChunkFromChunkCoords(i2, k2).removeEntity(sn2);
				}

				this.loadedEntityList.remove(k1--);
				this.releaseEntitySkin(sn2);
			}
		}

	}

	public IChunkProvider getIChunkProvider() {
		return this.chunkProvider;
	}

	public void playNoteAt(int i1, int j1, int k1, int l1, int i2) {
		int j2 = this.getBlockId(i1, j1, k1);
		if(j2 > 0) {
			Block.blocksList[j2].playBlock(this, i1, j1, k1, l1, i2);
		}

	}

	public WorldInfo getWorldInfo() {
		return this.worldInfo;
	}

	public void updateAllPlayersSleepingFlag() {
		this.allPlayersSleeping = !this.playerEntities.isEmpty();
		Iterator iterator = this.playerEntities.iterator();

		while(iterator.hasNext()) {
			EntityPlayer gs1 = (EntityPlayer)iterator.next();
			if(!gs1.isPlayerSleeping()) {
				this.allPlayersSleeping = false;
				break;
			}
		}

	}

	protected void wakeUpAllPlayers() {
		this.allPlayersSleeping = false;
		Iterator iterator = this.playerEntities.iterator();

		while(iterator.hasNext()) {
			EntityPlayer gs1 = (EntityPlayer)iterator.next();
			if(gs1.isPlayerSleeping()) {
				gs1.wakeUpPlayer(false, false, true);
			}
		}

		this.stopPrecipitation();
	}

	public boolean isAllPlayersFullyAsleep() {
		if(this.allPlayersSleeping && !this.multiplayerWorld) {
			Iterator iterator = this.playerEntities.iterator();

			while(iterator.hasNext()) {
				EntityPlayer gs1 = (EntityPlayer)iterator.next();
				if(!gs1.isPlayerFullyAsleep()) {
					return false;
				}
			}

			return true;
		} else {
			return false;
		}
	}

	public float func_27166_f(float f1) {
		return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * f1) * this.func_27162_g(f1);
	}

	public float func_27162_g(float f1) {
		return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * f1;
	}

	public void func_27158_h(float f1) {
		this.prevRainingStrength = f1;
		this.rainingStrength = f1;
	}

	public boolean func_27160_B() {
		return (double)this.func_27166_f(1.0F) > 0.9D;
	}

	public boolean func_27161_C() {
		return (double)this.func_27162_g(1.0F) > 0.2D;
	}

	public boolean canBlockBeRainedOn(int i1, int j1, int k1) {
		if(!this.func_27161_C()) {
			return false;
		} else if(!this.canBlockSeeTheSky(i1, j1, k1)) {
			return false;
		} else if(this.findTopSolidBlock(i1, k1) > j1) {
			return false;
		} else {
			BiomeGenBase kd1 = this.getWorldChunkManager().getBiomeGenAt(i1, k1);
			return kd1.getEnableSnow() ? false : kd1.canSpawnLightningBolt();
		}
	}

	public void setItemData(String s1, MapDataBase hm) {
		this.field_28108_z.setData(s1, hm);
	}

	public MapDataBase loadItemData(Class class1, String s1) {
		return this.field_28108_z.loadData(class1, s1);
	}

	public int getUniqueDataId(String s1) {
		return this.field_28108_z.getUniqueDataId(s1);
	}

	public void func_28106_e(int i1, int j1, int k1, int l1, int i2) {
		this.func_28107_a((EntityPlayer)null, i1, j1, k1, l1, i2);
	}

	public void func_28107_a(EntityPlayer gs1, int i1, int j1, int k1, int l1, int i2) {
		for(int j2 = 0; j2 < this.worldAccesses.size(); ++j2) {
			((IWorldAccess)this.worldAccesses.get(j2)).func_28136_a(gs1, i1, j1, k1, l1, i2);
		}

	}
}
