package net.minecraft.client.multiplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.EmptyChunk;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.World;

public class ChunkProviderClient implements IChunkProvider {
	private Chunk blankChunk;
	private Map<ChunkCoordIntPair,Chunk> chunkMapping = new HashMap<ChunkCoordIntPair, Chunk>();
	private List<Chunk> chunkListing = new ArrayList<Chunk>();
	private World worldObj;

	public ChunkProviderClient(World world1) {
		this.blankChunk = new EmptyChunk(world1, new byte[32768], new byte[32768], 0, 0);
		this.worldObj = world1;
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

	public Chunk prepareChunk(int i1, int i2) {
		ChunkCoordIntPair chunkCoordIntPair3 = new ChunkCoordIntPair(i1, i2);
		byte[] b4 = new byte[32768];
		byte[] b5 = new byte[32768];
		Chunk chunk5 = new Chunk(this.worldObj, b4, b5, i1, i2);
		Arrays.fill(chunk5.skylightMap.data, (byte)-1);
		this.chunkMapping.put(chunkCoordIntPair3, chunk5);
		chunk5.isChunkLoaded = true;
		return chunk5;
	}

	public Chunk provideChunk(int i1, int i2) {
		ChunkCoordIntPair chunkCoordIntPair3 = new ChunkCoordIntPair(i1, i2);
		Chunk chunk4 = (Chunk)this.chunkMapping.get(chunkCoordIntPair3);
		return chunk4 == null ? this.blankChunk : chunk4;
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