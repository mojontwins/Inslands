package net.minecraft.client.multiplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.world.level.World;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.ChunkCoordIntPair;
import net.minecraft.world.level.chunk.IChunkProvider;
import net.minecraft.world.level.chunk.storage.IProgressUpdate;

public class ChunkProviderClient implements IChunkProvider {
	private Chunk blankChunk;
	private Map<ChunkCoordIntPair,Chunk> chunkMapping = new HashMap<ChunkCoordIntPair, Chunk>();
	private List<Chunk> chunkListing = new ArrayList<Chunk>();
	private World worldObj;

	public ChunkProviderClient(World world) {
		// this.blankChunk = new EmptyChunk(world1, new byte[32768], new byte[32768], 0, 0);
		// Todo: get the proper ChunkProviderGenerate somehow and call `getBlankChunk`.
		// If we have a worldType, we can call worldType.getChunkGenerator.
		
		IChunkProvider provider = world.getWorldInfo().getTerrainType().getChunkGenerator(world);
		this.blankChunk = provider.makeBlank(world);
		this.blankChunk.isFakeChunk = true;
		this.worldObj = world;
	}
	
	public IChunkProvider getChunkProviderGenerate() {
		return this;
	}

	@SuppressWarnings("unused")
	public boolean chunkExists(int i1, int i2) {
		if(this != null) {
			return true;
		} else {
			ChunkCoordIntPair chunkCoordIntPair3 = new ChunkCoordIntPair(i1, i2);
			return this.chunkMapping.containsKey(chunkCoordIntPair3);
		}
	}

	public void unloadChunk(int i1, int i2) {
		Chunk chunk3 = this.provideChunk(i1, i2);
		if(!chunk3.getIsChunkRendered()) {
			chunk3.onChunkUnload();
		}

		this.chunkMapping.remove(new ChunkCoordIntPair(i1, i2));
		this.chunkListing.remove(chunk3);
	}

	public Chunk prepareChunk(int chunkX, int chunkZ) {
		ChunkCoordIntPair coords = new ChunkCoordIntPair(chunkX, chunkZ);
		byte[] blocks = new byte[32768];
		byte[] meta = new byte[32768];
		
		Chunk chunk = new Chunk(this.worldObj, blocks, meta, chunkX, chunkZ);
		Arrays.fill(chunk.skylightMap.data, (byte)-1);
		this.chunkMapping.put(coords, chunk);
		chunk.isChunkLoaded = true;
		return chunk;
	}

	public Chunk provideChunk(int chunkX, int chunkZ) {
		ChunkCoordIntPair chunkCoordIntPair3 = new ChunkCoordIntPair(chunkX, chunkZ);
		Chunk chunk = (Chunk)this.chunkMapping.get(chunkCoordIntPair3);

		return chunk == null ? this.blankChunk : chunk;
	}
	
	public Chunk justGenerateForHeight(int chunkX, int chunkZ) {
		return this.provideChunk(chunkX, chunkZ);
	}

	public Chunk makeBlank(World world) {
		return this.blankChunk; // TODO: Do this properly
	}
	
	public boolean saveChunks(boolean z1, IProgressUpdate iProgressUpdate2) {
		return true;
	}

	public boolean unload100OldestChunks() {
		return false;
	}

	public boolean canSave() {
		return false;
	}

	public void populate(IChunkProvider iChunkProvider1, int i2, int i3) {
	}

	public String makeString() {
		return "MultiplayerChunkCache: " + this.chunkMapping.size();
	}
}