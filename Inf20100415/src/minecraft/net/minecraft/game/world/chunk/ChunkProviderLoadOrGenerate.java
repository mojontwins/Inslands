package net.minecraft.game.world.chunk;

import com.mojang.nbt.NBTTagCompound;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.game.world.World;

public final class ChunkProviderLoadOrGenerate implements IChunkProvider {
	private IChunkProvider chunkProvider;
	private Chunk[] chunks = new Chunk[1024];
	private File saveDirectory;
	private World worldObj;

	public ChunkProviderLoadOrGenerate(World world1, File file2, IChunkProvider iChunkProvider3) {
		this.worldObj = world1;
		this.chunkProvider = iChunkProvider3;
		this.saveDirectory = file2;
	}

	public final boolean chunkExists(int i1, int i2) {
		int i3 = i1 & 31 | (i2 & 31) << 5;
		if(this.chunks[i3] != null) {
			Chunk chunk10000 = this.chunks[i3];
			i3 = i2;
			i2 = i1;
			Chunk chunk4 = chunk10000;
			if(i2 == chunk4.xPosition && i3 == chunk4.zPosition) {
				return true;
			}
		}

		return false;
	}

	public final Chunk provideChunk(int i1, int i2) {
		int i3 = i1 & 31 | (i2 & 31) << 5;
		if(!this.chunkExists(i1, i2)) {
			if(this.chunks[i3] != null) {
				this.chunks[i3].unloadEntities();
				this.saveChunk(this.chunks[i3]);
			}

			Chunk chunk4;
			if((chunk4 = this.loadChunk(i1, i2)) == null) {
				chunk4 = this.chunkProvider.provideChunk(i1, i2);
				this.saveChunk(chunk4);
			}

			this.chunks[i3] = chunk4;
			if(this.chunks[i3] != null) {
				this.chunks[i3].loadEntities();
			}

			if(this.chunkExists(i1 + 1, i2 + 1) && this.chunkExists(i1, i2 + 1) && this.chunkExists(i1 + 1, i2)) {
				this.populate(this, i1, i2);
			}

			if(this.chunkExists(i1 - 1, i2 + 1) && this.chunkExists(i1, i2 + 1) && this.chunkExists(i1 - 1, i2)) {
				this.populate(this, i1 - 1, i2);
			}

			if(this.chunkExists(i1 + 1, i2 - 1) && this.chunkExists(i1, i2 - 1) && this.chunkExists(i1 + 1, i2)) {
				this.populate(this, i1, i2 - 1);
			}

			if(this.chunkExists(i1 - 1, i2 - 1) && this.chunkExists(i1, i2 - 1) && this.chunkExists(i1 - 1, i2)) {
				this.populate(this, i1 - 1, i2 - 1);
			}
		}

		return this.chunks[i3];
	}

	private File chunkFileForXZ(int i1, int i2) {
		String string3 = "c." + Integer.toString(i1, 36) + "." + Integer.toString(i2, 36) + ".dat";
		String string4 = Integer.toString(i1 & 63, 36);
		String string6 = Integer.toString(i2 & 63, 36);
		File file5;
		(file5 = new File(this.saveDirectory, string4)).mkdirs();
		(file5 = new File(file5, string6)).mkdirs();
		return new File(file5, string3);
	}

	private Chunk loadChunk(int i1, int i2) {
		File file4;
		if((file4 = this.chunkFileForXZ(i1, i2)).exists()) {
			try {
				NBTTagCompound nBTTagCompound5 = LoadingScreenRenderer.read(new FileInputStream(file4));
				return Chunk.readChunkNBTData(this.worldObj, nBTTagCompound5.getCompoundTag("Level"));
			} catch (Exception exception3) {
				exception3.printStackTrace();
			}
		}

		return null;
	}

	private void saveChunk(Chunk chunk1) {
		File file2;
		if((file2 = this.chunkFileForXZ(chunk1.xPosition, chunk1.zPosition)).exists()) {
			this.worldObj.sizeOnDisk -= file2.length();
		}

		try {
			FileOutputStream fileOutputStream3 = new FileOutputStream(file2);
			NBTTagCompound nBTTagCompound4 = new NBTTagCompound();
			NBTTagCompound nBTTagCompound5 = new NBTTagCompound();
			nBTTagCompound4.setTag("Level", nBTTagCompound5);
			chunk1.writeChunkNBTData(nBTTagCompound5);
			LoadingScreenRenderer.write(nBTTagCompound4, fileOutputStream3);
			this.worldObj.sizeOnDisk += file2.length();
		} catch (Exception exception6) {
			exception6.printStackTrace();
		}
	}

	public final void populate(IChunkProvider iChunkProvider1, int i2, int i3) {
		Chunk chunk4;
		if(!(chunk4 = this.provideChunk(i2, i3)).isTerrainPopulated) {
			chunk4.isTerrainPopulated = true;
			this.chunkProvider.populate(iChunkProvider1, i2, i3);
		}

	}

	public final void saveChunks(boolean z1) {
		int i2 = 0;

		for(int i3 = 0; i3 < this.chunks.length; ++i3) {
			if(this.chunks[i3] != null && this.chunks[i3].needsSaving(z1)) {
				this.saveChunk(this.chunks[i3]);
				this.chunks[i3].isModified = false;
				++i2;
				if(i2 == 10 && !z1) {
					return;
				}
			}
		}

	}
}