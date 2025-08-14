package net.minecraft.world.level.chunk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import com.mojang.nbt.CompressedStreamTools;
import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.NBTTagList;

import net.minecraft.src.TileEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityList;
import net.minecraft.world.entity.block.EntityBlockEntity;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldInfo;

public class ChunkLoader implements IChunkLoader {
	private File saveDir;
	private boolean createIfNecessary;

	public ChunkLoader(File saveDir, boolean createIfNecessary) {
		this.saveDir = saveDir;
		this.createIfNecessary = createIfNecessary;
	}

	private File chunkFileForXZ(int chunkX, int chunkZ) {
		String string3 = "c." + Integer.toString(chunkX, 36) + "." + Integer.toString(chunkZ) + ".dat";
		String string4 = Integer.toString(chunkX & 63, 36);
		String string5 = Integer.toString(chunkZ & 63, 36);
		File file6 = new File(this.saveDir, string4);
		if(!file6.exists()) {
			if(!this.createIfNecessary) {
				return null;
			}

			file6.mkdir();
		}

		file6 = new File(file6, string5);
		if(!file6.exists()) {
			if(!this.createIfNecessary) {
				return null;
			}

			file6.mkdir();
		}

		file6 = new File(file6, string3);
		return !file6.exists() && !this.createIfNecessary ? null : file6;
	}

	public Chunk loadChunk(World world, int x, int z) throws IOException {
		File file4 = this.chunkFileForXZ(x, z);
		if(file4 != null && file4.exists()) {
			try {
				FileInputStream fileInputStream5 = new FileInputStream(file4);
				NBTTagCompound nBTTagCompound6 = CompressedStreamTools.readCompressed(fileInputStream5);
				if(!nBTTagCompound6.hasKey("Level")) {
					System.out.println("Chunk file at " + x + "," + z + " is missing level data, skipping");
					return null;
				}

				if(!nBTTagCompound6.getCompoundTag("Level").hasKey("Blocks")) {
					System.out.println("Chunk file at " + x + "," + z + " is missing block data, skipping");
					return null;
				}

				Chunk chunk7 = loadChunkIntoWorldFromCompound(world, nBTTagCompound6.getCompoundTag("Level"));
				if(!chunk7.isAtLocation(x, z)) {
					System.out.println("Chunk file at " + x + "," + z + " is in the wrong location; relocating. (Expected " + x + ", " + z + ", got " + chunk7.xPosition + ", " + chunk7.zPosition + ")");
					nBTTagCompound6.setInteger("xPos", x);
					nBTTagCompound6.setInteger("zPos", z);
					chunk7 = loadChunkIntoWorldFromCompound(world, nBTTagCompound6.getCompoundTag("Level"));
				}

				chunk7.removeUnknownBlocks();
				chunk7.generateLandSurfaceHeightMap();
				return chunk7;
			} catch (Exception exception8) {
				exception8.printStackTrace();
			}
		}

		return null;
	}

	public void saveChunk(World world, Chunk chunk) throws IOException {
		world.checkSessionLock();
		File file3 = this.chunkFileForXZ(chunk.xPosition, chunk.zPosition);
		if(file3.exists()) {
			WorldInfo worldInfo4 = world.getWorldInfo();
			worldInfo4.setSizeOnDisk(worldInfo4.getSizeOnDisk() - file3.length());
		}

		try {
			File file10 = new File(this.saveDir, "tmp_chunk.dat");
			FileOutputStream fileOutputStream5 = new FileOutputStream(file10);
			NBTTagCompound nBTTagCompound6 = new NBTTagCompound();
			NBTTagCompound nBTTagCompound7 = new NBTTagCompound();
			nBTTagCompound6.setTag("Level", nBTTagCompound7);
			storeChunkInCompound(chunk, world, nBTTagCompound7);
			CompressedStreamTools.writeCompressed(nBTTagCompound6, fileOutputStream5);
			fileOutputStream5.close();
			if(file3.exists()) {
				file3.delete();
			}

			file10.renameTo(file3);
			WorldInfo worldInfo8 = world.getWorldInfo();
			worldInfo8.setSizeOnDisk(worldInfo8.getSizeOnDisk() + file3.length());
		} catch (Exception exception9) {
			exception9.printStackTrace();
		}

	}

	public static void storeChunkInCompound(Chunk chunk0, World world1, NBTTagCompound nBTTagCompound2) {
		world1.checkSessionLock();
		nBTTagCompound2.setBoolean("NewFormat", true);
		nBTTagCompound2.setInteger("xPos", chunk0.xPosition);
		nBTTagCompound2.setInteger("zPos", chunk0.zPosition);
		nBTTagCompound2.setLong("LastUpdate", world1.getWorldTime());
		nBTTagCompound2.setByteArray("Blocks", chunk0.blocks);
		nBTTagCompound2.setByteArray("Data", chunk0.data);
		nBTTagCompound2.setByteArray("SkyLight", chunk0.skylightMap.data);
		nBTTagCompound2.setByteArray("BlockLight", chunk0.blocklightMap.data);
		nBTTagCompound2.setByteArray("HeightMap", chunk0.heightMap);
		nBTTagCompound2.setBoolean("TerrainPopulated", chunk0.isTerrainPopulated);
		chunk0.hasEntities = false;
		
		NBTTagList nBTTagList3 = new NBTTagList();
		NBTTagCompound nBTTagCompound7;
		{
			Iterator<Entity> iterator5;
			for(int i4 = 0; i4 < chunk0.entities.length; ++i4) {
				iterator5 = chunk0.entities[i4].iterator();
	
				while(iterator5.hasNext()) {
					Entity entity6 = (Entity)iterator5.next();
					chunk0.hasEntities = true;
					nBTTagCompound7 = new NBTTagCompound();
					if(entity6.addEntityID(nBTTagCompound7)) {
						nBTTagList3.setTag(nBTTagCompound7);
					}
				}
			}
		}
		nBTTagCompound2.setTag("Entities", nBTTagList3);
		
		NBTTagList nBTTagList8 = new NBTTagList();
		{
			Iterator<TileEntity> iterator6 = chunk0.chunkTileEntityMap.values().iterator();
	
			while(iterator6.hasNext()) {
				TileEntity tileEntity9 = (TileEntity)iterator6.next();
				nBTTagCompound7 = new NBTTagCompound();
				tileEntity9.writeToNBT(nBTTagCompound7);
				nBTTagList8.setTag(nBTTagCompound7);
			}
		}
		nBTTagCompound2.setTag("TileEntities", nBTTagList8);
		
		NBTTagList nBTTagList = new NBTTagList();
		{
			Iterator<EntityBlockEntity>iterator = chunk0.chunkSpecialEntityMap.values().iterator();
	
			while(iterator.hasNext()) { 
				EntityBlockEntity entity = iterator.next();
				NBTTagCompound nBTTagCompound = new NBTTagCompound();
				if(entity.addEntityID(nBTTagCompound)) {
					nBTTagList.setTag(nBTTagCompound);
				}
			}
		}	
		nBTTagCompound2.setTag("SpecialEntities", nBTTagList);
	}

	public static Chunk loadChunkIntoWorldFromCompound(World world0, NBTTagCompound nBTTagCompound1) {
		int i2 = nBTTagCompound1.getInteger("xPos");
		int i3 = nBTTagCompound1.getInteger("zPos");
		Chunk chunk4 = new Chunk(world0, i2, i3);
		chunk4.blocks = nBTTagCompound1.getByteArray("Blocks");
		if(nBTTagCompound1.getBoolean("NewFormat")) {
			chunk4.data = nBTTagCompound1.getByteArray("Data");
		} else {
			System.out.println ("Converting chunk metadata from old format");
			NibbleArray temp = new NibbleArray(nBTTagCompound1.getByteArray("Data"));
			chunk4.data = temp.asByteArray();
			chunk4.isModified = true; 		// So it gets saved right away
		}
		chunk4.skylightMap = new NibbleArray(nBTTagCompound1.getByteArray("SkyLight"));
		chunk4.blocklightMap = new NibbleArray(nBTTagCompound1.getByteArray("BlockLight"));
		chunk4.heightMap = nBTTagCompound1.getByteArray("HeightMap");
		chunk4.isTerrainPopulated = nBTTagCompound1.getBoolean("TerrainPopulated");
		
		if(chunk4.heightMap == null || !chunk4.skylightMap.isValid()) {
			chunk4.heightMap = new byte[256];
			chunk4.skylightMap = new NibbleArray(chunk4.blocks.length);
			chunk4.generateSkylightMap();
		}

		if(!chunk4.blocklightMap.isValid()) {
			chunk4.blocklightMap = new NibbleArray(chunk4.blocks.length);
		}

		NBTTagList nBTTagList5 = nBTTagCompound1.getTagList("Entities");
		if(nBTTagList5 != null) {
			for(int i6 = 0; i6 < nBTTagList5.tagCount(); ++i6) {
				NBTTagCompound nBTTagCompound7 = (NBTTagCompound)nBTTagList5.tagAt(i6);
				Entity entity8 = EntityList.createEntityFromNBT(nBTTagCompound7, world0);
				if(entity8 instanceof EntityBlockEntity) {
				} else {
					chunk4.hasEntities = true;
					if(entity8 != null) {
						chunk4.addEntity(entity8);
					}
				}
			}
		}

		NBTTagList nBTTagList10 = nBTTagCompound1.getTagList("TileEntities");
		if(nBTTagList10 != null) {
			for(int i11 = 0; i11 < nBTTagList10.tagCount(); ++i11) {
				NBTTagCompound nBTTagCompound12 = (NBTTagCompound)nBTTagList10.tagAt(i11);
				TileEntity tileEntity9 = TileEntity.createAndLoadEntity(nBTTagCompound12);
				if(tileEntity9 != null) {
					chunk4.addTileEntity(tileEntity9);
				}
			}
		}

		NBTTagList nBTTagList = nBTTagCompound1.getTagList("SpecialEntities");
		if(nBTTagList != null) {
			for(int i = 0; i < nBTTagList.tagCount(); i ++) {
				NBTTagCompound nBTTagCompound = (NBTTagCompound)nBTTagList.tagAt(i);
				EntityBlockEntity entity = (EntityBlockEntity) EntityList.createEntityFromNBT(nBTTagCompound, world0);
				// System.out.println ("Loaded " + entity);
				if(entity != null) {
					chunk4.addSpecialEntity(entity);
				}
			}
		}
		
		return chunk4;
	}

	public void chunkTick() {
	}

	public void saveExtraData() {
	}

	public void saveExtraChunkData(World world1, Chunk chunk2) throws IOException {
	}
}
