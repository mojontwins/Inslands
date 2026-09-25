package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.world.level.WorldSize;
import net.minecraft.world.level.chunk.ChunkCoordIntPair;
import net.minecraft.world.level.chunk.IChunkProvider;

public class PlayerManager {
	public List<EntityPlayerMP> players = new ArrayList<EntityPlayerMP>();
	private PlayerHash playerInstances = new PlayerHash();
	private List<PlayerInstance> playerInstancesToUpdate = new ArrayList<PlayerInstance>();
	private MinecraftServer mcServer;
	private int playerDimension;
	private int playerViewRadius;
	private final int[][] xzDirectionsConst = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
	private int islandMinX;
	private int islandMaxX;
	private int islandMinZ;
	private int islandMaxZ;

	private void fetchIslandBounds() {
		IChunkProvider generator = this.getMinecraftServer().getChunkProvider().getChunkProviderGenerate();
		this.islandMinX = WorldSize.getXChunkMinForReal(generator);
		this.islandMaxX = WorldSize.getXChunkMaxForReal(generator);
		this.islandMinZ = WorldSize.getZChunkMinForReal(generator);
		this.islandMaxZ = WorldSize.getZChunkMaxForReal(generator);
	}

	public PlayerManager(MinecraftServer minecraftServer1, int i2, int i3) {
		if(i3 > 15) {
			throw new IllegalArgumentException("Too big view radius!");
		} else if(i3 < 3) {
			throw new IllegalArgumentException("Too small view radius!");
		} else {
			this.playerViewRadius = i3;
			this.mcServer = minecraftServer1;
			this.playerDimension = i2;
		}
	}

	public WorldServer getMinecraftServer() {
		return this.mcServer.getWorldManager(this.playerDimension);
	}

	public void updatePlayerInstances() {
		for(int i1 = 0; i1 < this.playerInstancesToUpdate.size(); ++i1) {
			((PlayerInstance)this.playerInstancesToUpdate.get(i1)).onUpdate();
		}

		this.playerInstancesToUpdate.clear();
	}

	private PlayerInstance getPlayerInstance(int i1, int i2, boolean z3) {
		long j4 = (long)i1 + 2147483647L | (long)i2 + 2147483647L << 32;
		PlayerInstance playerInstance6 = (PlayerInstance)this.playerInstances.getValueByKey(j4);
		if(playerInstance6 == null && z3) {
			playerInstance6 = new PlayerInstance(this, i1, i2);
			this.playerInstances.add(j4, playerInstance6);
		}

		return playerInstance6;
	}

	public void markBlockNeedsUpdate(int i1, int i2, int i3) {
		int i4 = i1 >> 4;
		int i5 = i3 >> 4;
		PlayerInstance playerInstance6 = this.getPlayerInstance(i4, i5, false);
		if(playerInstance6 != null) {
			playerInstance6.markBlockNeedsUpdate(i1 & 15, i2, i3 & 15);
		}

	}

	public void addPlayer(EntityPlayerMP entityPlayerMP1) {
		entityPlayerMP1.managedPosX = entityPlayerMP1.posX;
		entityPlayerMP1.managedPosZ = entityPlayerMP1.posZ;
		this.fetchIslandBounds();

		int playerCX = (int)entityPlayerMP1.posX >> 4;
		int playerCZ = (int)entityPlayerMP1.posZ >> 4;
		for(int dx = -this.playerViewRadius; dx <= this.playerViewRadius; ++dx) {
			for(int dz = -this.playerViewRadius; dz <= this.playerViewRadius; ++dz) {
				int cx = playerCX + dx;
				int cz = playerCZ + dz;
				if(cx >= this.islandMinX && cx < this.islandMaxX && cz >= this.islandMinZ && cz < this.islandMaxZ) {
					this.getPlayerInstance(cx, cz, true).addPlayer(entityPlayerMP1);
				}
			}
		}

		this.players.add(entityPlayerMP1);
	}

	public void removePlayer(EntityPlayerMP entityPlayerMP1) {
		this.fetchIslandBounds();

		int playerCX = (int)entityPlayerMP1.posX >> 4;
		int playerCZ = (int)entityPlayerMP1.posZ >> 4;
		for(int dx = -this.playerViewRadius; dx <= this.playerViewRadius; ++dx) {
			for(int dz = -this.playerViewRadius; dz <= this.playerViewRadius; ++dz) {
				int cx = playerCX + dx;
				int cz = playerCZ + dz;
				if(cx >= this.islandMinX && cx < this.islandMaxX && cz >= this.islandMinZ && cz < this.islandMaxZ) {
					PlayerInstance playerInstance6 = this.getPlayerInstance(cx, cz, false);
					if(playerInstance6 != null) {
						playerInstance6.removePlayer(entityPlayerMP1);
					}
				}
			}
		}

		this.players.remove(entityPlayerMP1);
	}

	private boolean isOutsidePlayerViewRadius(int i1, int i2, int i3, int i4) {
		int i5 = i1 - i3;
		int i6 = i2 - i4;
		return i5 >= -this.playerViewRadius && i5 <= this.playerViewRadius ? i6 >= -this.playerViewRadius && i6 <= this.playerViewRadius : false;
	}

	public void updateMountedMovingPlayer(EntityPlayerMP entityPlayerMP1) {
		int playerCX = (int)entityPlayerMP1.posX >> 4;
		int playerCZ = (int)entityPlayerMP1.posZ >> 4;
		int managedCX = (int)entityPlayerMP1.managedPosX >> 4;
		int managedCZ = (int)entityPlayerMP1.managedPosZ >> 4;
		if(playerCX == managedCX && playerCZ == managedCZ) {
			return;
		}

		this.fetchIslandBounds();
		int radius = this.playerViewRadius;
		int dCX = playerCX - managedCX;
		int dCZ = playerCZ - managedCZ;

		for(int dx = -radius; dx <= radius; ++dx) {
			for(int dz = -radius; dz <= radius; ++dz) {
				int newX = playerCX + dx;
				int newZ = playerCZ + dz;
				int oldX = newX - dCX;
				int oldZ = newZ - dCZ;
				boolean inNew = newX >= this.islandMinX && newX < this.islandMaxX && newZ >= this.islandMinZ && newZ < this.islandMaxZ;
				boolean inOld = oldX >= this.islandMinX && oldX < this.islandMaxX && oldZ >= this.islandMinZ && oldZ < this.islandMaxZ;

				if(inNew && !inOld) {
					this.getPlayerInstance(newX, newZ, true).addPlayer(entityPlayerMP1);
				} else if(!inNew && inOld) {
					PlayerInstance playerInstance6 = this.getPlayerInstance(oldX, oldZ, false);
					if(playerInstance6 != null) {
						playerInstance6.removePlayer(entityPlayerMP1);
					}
				}
			}
		}

		Iterator<ChunkCoordIntPair> iterator = new ArrayList<ChunkCoordIntPair>(entityPlayerMP1.listeningChunks).iterator();
		while(iterator.hasNext()) {
			ChunkCoordIntPair pair = iterator.next();
			int dx = pair.chunkXPos - playerCX;
			int dz = pair.chunkZPos - playerCZ;
			if(dx < -radius || dx > radius || dz < -radius || dz > radius || pair.chunkXPos < this.islandMinX || pair.chunkXPos >= this.islandMaxX || pair.chunkZPos < this.islandMinZ || pair.chunkZPos >= this.islandMaxZ) {
				PlayerInstance playerInstance6 = this.getPlayerInstance(pair.chunkXPos, pair.chunkZPos, false);
				if(playerInstance6 != null) {
					playerInstance6.removePlayer(entityPlayerMP1);
				}
				entityPlayerMP1.listeningChunks.remove(pair);
			}
		}

		entityPlayerMP1.managedPosX = entityPlayerMP1.posX;
		entityPlayerMP1.managedPosZ = entityPlayerMP1.posZ;
	}

	public int getMaxTrackingDistance() {
		return this.playerViewRadius * 16 - 16;
	}

	static PlayerHash getPlayerInstances(PlayerManager playerManager0) {
		return playerManager0.playerInstances;
	}

	static List<PlayerInstance> getPlayerInstancesToUpdate(PlayerManager playerManager0) {
		return playerManager0.playerInstancesToUpdate;
	}
}
