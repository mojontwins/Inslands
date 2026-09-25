package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet50PreChunk;
import net.minecraft.network.packet.Packet52MultiBlockChange;
import net.minecraft.network.packet.Packet53BlockChange;
import net.minecraft.world.level.chunk.ChunkCoordIntPair;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.entity.TileEntity;

class PlayerInstance {
	private List<EntityPlayerMP> players;
	private int chunkX;
	private int chunkZ;
	private ChunkCoordIntPair currentChunk;
	private short[] blocksToUpdate;
	private int numBlocksToUpdate;
	private int minX;
	private int maxX;
	private int minY;
	private int maxY;
	private int minZ;
	private int maxZ;
	final PlayerManager playerManager;

	public PlayerInstance(PlayerManager playerManager1, int i2, int i3) {
		this.playerManager = playerManager1;
		this.players = new ArrayList<EntityPlayerMP>();
		this.blocksToUpdate = new short[64];
		this.numBlocksToUpdate = 0;
		this.chunkX = i2;
		this.chunkZ = i3;
		this.currentChunk = new ChunkCoordIntPair(i2, i3);
		playerManager1.getMinecraftServer().chunkProviderServer.prepareChunk(i2, i3);
	}

	public void addPlayer(EntityPlayerMP entityPlayerMP1) {
		if(this.players.contains(entityPlayerMP1)) {
			return;
		}

		entityPlayerMP1.listeningChunks.add(this.currentChunk);
		entityPlayerMP1.playerNetServerHandler.sendPacket(new Packet50PreChunk(this.currentChunk.chunkXPos, this.currentChunk.chunkZPos, true));
		this.players.add(entityPlayerMP1);
		entityPlayerMP1.queueChunkForSync(this.currentChunk);
	}

	public void removePlayer(EntityPlayerMP entityPlayerMP1) {
		if(this.players.contains(entityPlayerMP1)) {
			this.players.remove(entityPlayerMP1);
			if(this.players.size() == 0) {
				long j2 = (long)this.chunkX + 2147483647L | (long)this.chunkZ + 2147483647L << 32;
				PlayerManager.getPlayerInstances(this.playerManager).remove(j2);
				if(this.numBlocksToUpdate > 0) {
					PlayerManager.getPlayerInstancesToUpdate(this.playerManager).remove(this);
				}

//Removed
			//this.playerManager.getMinecraftServer().chunkProviderServer.dropChunk(this.chunkX, this.chunkZ);
			}

			entityPlayerMP1.loadedChunks.remove(this.currentChunk);
			if(entityPlayerMP1.listeningChunks.contains(this.currentChunk)) {
				entityPlayerMP1.listeningChunks.remove(this.currentChunk);
				entityPlayerMP1.playerNetServerHandler.sendPacket(new Packet50PreChunk(this.chunkX, this.chunkZ, false));
			}

		}
	}

	public void markBlockNeedsUpdate(int i1, int i2, int i3) {
		if(this.numBlocksToUpdate == 0) {
			PlayerManager.getPlayerInstancesToUpdate(this.playerManager).add(this);
			this.minX = this.maxX = i1;
			this.minY = this.maxY = i2;
			this.minZ = this.maxZ = i3;
		}

		if(this.minX > i1) {
			this.minX = i1;
		}

		if(this.maxX < i1) {
			this.maxX = i1;
		}

		if(this.minY > i2) {
			this.minY = i2;
		}

		if(this.maxY < i2) {
			this.maxY = i2;
		}

		if(this.minZ > i3) {
			this.minZ = i3;
		}

		if(this.maxZ < i3) {
			this.maxZ = i3;
		}

		if(this.numBlocksToUpdate < 64) {
			short s4 = (short)(i1 << 12 | i3 << 8 | i2);

			for(int i5 = 0; i5 < this.numBlocksToUpdate; ++i5) {
				if(this.blocksToUpdate[i5] == s4) {
					return;
				}
			}

			this.blocksToUpdate[this.numBlocksToUpdate++] = s4;
		}

	}

	public void sendPacketToPlayersInInstance(Packet packet1) {
		// Fixed-size worlds are always in memory, so every member of this chunk's
		// instance is fully synced at all times: no streaming gate here.
		for(int i2 = 0; i2 < this.players.size(); ++i2) {
			EntityPlayerMP entityPlayerMP3 = (EntityPlayerMP)this.players.get(i2);
			entityPlayerMP3.playerNetServerHandler.sendPacket(packet1);
		}

	}

	public void onUpdate() {
		WorldServer worldServer1 = this.playerManager.getMinecraftServer();
		if(this.numBlocksToUpdate != 0) {
			int i2;
			int i3;
			int i4;
			int i5;
			if(this.numBlocksToUpdate == 1) {
				i2 = this.chunkX * 16 + this.minX;
				i3 = this.minY;
				i4 = this.chunkZ * 16 + this.minZ;
				this.sendPacketToPlayersInInstance(new Packet53BlockChange(i2, i3, i4, worldServer1));
				if(Block.isBlockContainer[worldServer1.getBlockID(i2, i3, i4)]) {
					this.updateTileEntity(worldServer1.getBlockTileEntity(i2, i3, i4));
				}
			} else {
				this.sendPacketToPlayersInInstance(new Packet52MultiBlockChange(this.chunkX, this.chunkZ, this.blocksToUpdate, this.numBlocksToUpdate, worldServer1));

				for(i2 = 0; i2 < this.numBlocksToUpdate; ++i2) {
					short s4 = this.blocksToUpdate[i2];
					i3 = this.chunkX * 16 + (s4 >> 12 & 15);
					i4 = s4 & 255;
					i5 = this.chunkZ * 16 + (s4 >> 8 & 15);
					if(Block.isBlockContainer[worldServer1.getBlockID(i3, i4, i5)]) {
						this.updateTileEntity(worldServer1.getBlockTileEntity(i3, i4, i5));
					}
				}
			}

			this.numBlocksToUpdate = 0;
		}
	}

	private void updateTileEntity(TileEntity tileEntity1) {
		if(tileEntity1 != null) {
			Packet packet2 = tileEntity1.getDescriptionPacket();
			if(packet2 != null) {
				this.sendPacketToPlayersInInstance(packet2);
			}
		}

	}
}
