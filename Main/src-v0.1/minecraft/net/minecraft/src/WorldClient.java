package net.minecraft.src;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class WorldClient extends World {
	private LinkedList<WorldBlockPositionType> blocksToReceive = new LinkedList<WorldBlockPositionType>();
	private NetClientHandler sendQueue;
	private ChunkProviderClient chunkProviderClient;
	private MCHash entityHashSet = new MCHash();
	private Set<Entity> entityList = new HashSet<Entity>();
	private Set<Entity> entitySpawnQueue = new HashSet<Entity>();

	public WorldClient(NetClientHandler netClientHandler1, WorldSettings worldSettings2, int i4) {
		super(new SaveHandlerMP(), "MpServer", WorldProvider.getProviderForDimension(i4), (WorldSettings)worldSettings2);
		this.sendQueue = netClientHandler1;
		this.setSpawnPoint(new ChunkCoordinates(8, 64, 8));
		this.mapStorage = netClientHandler1.field_28118_b;
	}

	public void tick() {
		this.setWorldTime(this.getWorldTime() + 1L);
		int i1 = this.calculateSkylightSubtracted(1.0F);
		int i2;
		if(i1 != this.skylightSubtracted) {
			this.skylightSubtracted = i1;

			for(i2 = 0; i2 < this.worldAccesses.size(); ++i2) {
				((IWorldAccess)this.worldAccesses.get(i2)).updateAllRenderers();
			}
		}

		for(i2 = 0; i2 < 10 && !this.entitySpawnQueue.isEmpty(); ++i2) {
			Entity entity3 = (Entity)this.entitySpawnQueue.iterator().next();
			if(!this.loadedEntityList.contains(entity3)) {
				this.entityJoinedWorld(entity3);
			}
		}

		this.sendQueue.processReadPackets();

		for(i2 = 0; i2 < this.blocksToReceive.size(); ++i2) {
			WorldBlockPositionType worldBlockPositionType4 = (WorldBlockPositionType)this.blocksToReceive.get(i2);
			if(--worldBlockPositionType4.field_1206_d == 0) {
				super.setBlockAndMetadata(worldBlockPositionType4.posX, worldBlockPositionType4.posY, worldBlockPositionType4.posZ, worldBlockPositionType4.blockID, worldBlockPositionType4.metadata);
				super.markBlockNeedsUpdate(worldBlockPositionType4.posX, worldBlockPositionType4.posY, worldBlockPositionType4.posZ);
				this.blocksToReceive.remove(i2--);
			}
		}

	}

	public void invalidateBlockReceiveRegion(int i1, int i2, int i3, int i4, int i5, int i6) {
		for(int i7 = 0; i7 < this.blocksToReceive.size(); ++i7) {
			WorldBlockPositionType worldBlockPositionType8 = (WorldBlockPositionType)this.blocksToReceive.get(i7);
			if(worldBlockPositionType8.posX >= i1 && worldBlockPositionType8.posY >= i2 && worldBlockPositionType8.posZ >= i3 && worldBlockPositionType8.posX <= i4 && worldBlockPositionType8.posY <= i5 && worldBlockPositionType8.posZ <= i6) {
				this.blocksToReceive.remove(i7--);
			}
		}

	}

	protected IChunkProvider getChunkProvider() {
		this.chunkProviderClient = new ChunkProviderClient(this);
		return this.chunkProviderClient;
	}

	public void setSpawnLocation() {
		this.setSpawnPoint(new ChunkCoordinates(8, 64, 8));
	}

	protected void updateBlocksAndPlayCaveSounds() {
	}

	public void scheduleBlockUpdate(int i1, int i2, int i3, int i4, int i5) {
	}

	public boolean TickUpdates(boolean z1) {
		return false;
	}

	public void doPreChunk(int i1, int i2, boolean z3) {
		if(z3) {
			this.chunkProviderClient.prepareChunk(i1, i2);
		} else {
			this.chunkProviderClient.unloadChunk(i1, i2);
		}

		if(!z3) {
			this.markBlocksDirty(i1 * 16, 0, i2 * 16, i1 * 16 + 15, 128, i2 * 16 + 15);
		}

	}

	public boolean entityJoinedWorld(Entity entity1) {
		boolean z2 = super.entityJoinedWorld(entity1);
		this.entityList.add(entity1);
		if(!z2) {
			this.entitySpawnQueue.add(entity1);
		}

		return z2;
	}

	public void setEntityDead(Entity entity1) {
		super.setEntityDead(entity1);
		this.entityList.remove(entity1);
	}

	protected void obtainEntitySkin(Entity entity1) {
		super.obtainEntitySkin(entity1);
		if(this.entitySpawnQueue.contains(entity1)) {
			this.entitySpawnQueue.remove(entity1);
		}

	}

	protected void releaseEntitySkin(Entity entity1) {
		super.releaseEntitySkin(entity1);
		if(this.entityList.contains(entity1)) {
			this.entitySpawnQueue.add(entity1);
		}

	}

	public void func_712_a(int i1, Entity entity2) {
		Entity entity3 = this.func_709_b(i1);
		if(entity3 != null) {
			this.setEntityDead(entity3);
		}

		this.entityList.add(entity2);
		entity2.entityId = i1;
		if(!this.entityJoinedWorld(entity2)) {
			this.entitySpawnQueue.add(entity2);
		}

		this.entityHashSet.addKey(i1, entity2);
	}

	public Entity func_709_b(int i1) {
		return (Entity)this.entityHashSet.lookup(i1);
	}

	public Entity removeEntityFromWorld(int i1) {
		Entity entity2 = (Entity)this.entityHashSet.removeObject(i1);
		if(entity2 != null) {
			this.entityList.remove(entity2);
			this.setEntityDead(entity2);
		}

		return entity2;
	}

	public boolean setBlockMetadata(int i1, int i2, int i3, int i4) {
		int i5 = this.getBlockId(i1, i2, i3);
		int i6 = this.getBlockMetadata(i1, i2, i3);
		if(super.setBlockMetadata(i1, i2, i3, i4)) {
			this.blocksToReceive.add(new WorldBlockPositionType(this, i1, i2, i3, i5, i6));
			return true;
		} else {
			return false;
		}
	}

	public boolean setBlockAndMetadata(int i1, int i2, int i3, int i4, int i5) {
		int i6 = this.getBlockId(i1, i2, i3);
		int i7 = this.getBlockMetadata(i1, i2, i3);
		if(super.setBlockAndMetadata(i1, i2, i3, i4, i5)) {
			this.blocksToReceive.add(new WorldBlockPositionType(this, i1, i2, i3, i6, i7));
			return true;
		} else {
			return false;
		}
	}

	public boolean setBlock(int i1, int i2, int i3, int i4) {
		int i5 = this.getBlockId(i1, i2, i3);
		int i6 = this.getBlockMetadata(i1, i2, i3);
		if(super.setBlock(i1, i2, i3, i4)) {
			this.blocksToReceive.add(new WorldBlockPositionType(this, i1, i2, i3, i5, i6));
			return true;
		} else {
			return false;
		}
	}

	public boolean setBlockAndMetadataAndInvalidate(int i1, int i2, int i3, int i4, int i5) {
		this.invalidateBlockReceiveRegion(i1, i2, i3, i1, i2, i3);
		
		// r1.2.5 does this:
		return super.setBlockAndMetadataWithNotify(i1, i2, i3, i4, i5);
		// rather than	
		/*
		if(super.setBlockAndMetadata(i1, i2, i3, i4, i5)) {
			this.notifyBlockChange(i1, i2, i3, i4);
			return true;
		} else {
			return false;
		}
		*/
	}

	public void sendQuittingDisconnectingPacket() {
		this.sendQueue.func_28117_a(new Packet255KickDisconnect("Quitting"));
	}

	protected void updateWeather() {
		if(!this.worldProvider.hasNoSky) {
			if(this.lastLightningBolt > 0) {
				--this.lastLightningBolt;
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
}
