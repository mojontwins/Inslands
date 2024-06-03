package com.mojang.minecraft.structure;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.ChunkProviderGenerate;
import net.minecraft.src.MapGenBase;
import net.minecraft.src.World;

public abstract class MapGenStructure extends MapGenBase {
	protected HashMap<Long,StructureStart> coordMap = new HashMap<Long,StructureStart>();

	public void generate(ChunkProviderGenerate iChunkProvider1, World world2, int i3, int i4, byte[] b5) {
		super.generate(iChunkProvider1, world2, i3, i4, b5);
	}

	protected void recursiveGenerate(World world1, int i2, int i3, int i4, int i5, byte[] b6) {
		if(!this.coordMap.containsKey(ChunkCoordIntPair.chunkXZ2Long(i2, i3))) {
			this.rand.nextInt();
			if(this.canSpawnStructureAtCoords(i2, i3)) {
				StructureStart structureStart7 = this.getStructureStart(i2, i3);
				this.coordMap.put(ChunkCoordIntPair.chunkXZ2Long(i2, i3), structureStart7);
			}

		}
	}

	public boolean generateStructuresInChunk(World world1, Random random2, int i3, int i4, boolean mostlySolid) {
		int i5 = (i3 << 4) + 8;
		int i6 = (i4 << 4) + 8;
		boolean z7 = false;
		Iterator<StructureStart> iterator8 = this.coordMap.values().iterator();

		while(iterator8.hasNext()) {
			StructureStart structureStart9 = (StructureStart)iterator8.next();
			if(structureStart9.isSizeableStructure() && structureStart9.getBoundingBox().intersectsWith(i5, i6, i5 + 15, i6 + 15)) {
				structureStart9.generateStructure(world1, random2, new StructureBoundingBox(i5, i6, i5 + 15, i6 + 15), mostlySolid);
				z7 = true;
			}
		}

		return z7;
	}

	public boolean func_40483_a(int i1, int i2, int i3) {
		Iterator<StructureStart> iterator4 = this.coordMap.values().iterator();

		while(true) {
			StructureStart structureStart5;
			do {
				do {
					if(!iterator4.hasNext()) {
						return false;
					}

					structureStart5 = (StructureStart)iterator4.next();
				} while(!structureStart5.isSizeableStructure());
			} while(!structureStart5.getBoundingBox().intersectsWith(i1, i3, i1, i3));

			Iterator<StructureComponent> iterator6 = structureStart5.getComponents().iterator();

			while(iterator6.hasNext()) {
				StructureComponent structureComponent7 = (StructureComponent)iterator6.next();
				if(structureComponent7.getBoundingBox().isVecInside(i1, i2, i3)) {
					return true;
				}
			}
		}
	}

	public ChunkPosition getNearestInstance(World world1, int i2, int i3, int i4) {
		this.world = world1;
		this.rand.setSeed(world1.getRandomSeed());
		long j5 = this.rand.nextLong();
		long j7 = this.rand.nextLong();
		long j9 = (long)(i2 >> 4) * j5;
		long j11 = (long)(i4 >> 4) * j7;
		this.rand.setSeed(j9 ^ j11 ^ world1.getRandomSeed());
		this.recursiveGenerate(world1, i2 >> 4, i4 >> 4, 0, 0, (byte[])null);
		double d13 = Double.MAX_VALUE;
		ChunkPosition chunkPosition15 = null;
		Iterator<StructureStart> iterator16 = this.coordMap.values().iterator();

		ChunkPosition chunkPosition19;
		int i20;
		int i21;
		int i22;
		double d23;
		while(iterator16.hasNext()) {
			StructureStart structureStart17 = (StructureStart)iterator16.next();
			if(structureStart17.isSizeableStructure()) {
				StructureComponent structureComponent18 = (StructureComponent)structureStart17.getComponents().get(0);
				chunkPosition19 = structureComponent18.getCenter();
				i20 = chunkPosition19.x - i2;
				i21 = chunkPosition19.y - i3;
				i22 = chunkPosition19.z - i4;
				d23 = (double)(i20 + i20 * i21 * i21 + i22 * i22);
				if(d23 < d13) {
					d13 = d23;
					chunkPosition15 = chunkPosition19;
				}
			}
		}

		if(chunkPosition15 != null) {
			return chunkPosition15;
		} else {
			List<ChunkPosition> list25 = this.func_40482_a();
			if(list25 != null) {
				ChunkPosition chunkPosition26 = null;
				Iterator<ChunkPosition> iterator27 = list25.iterator();

				while(iterator27.hasNext()) {
					chunkPosition19 = (ChunkPosition)iterator27.next();
					i20 = chunkPosition19.x - i2;
					i21 = chunkPosition19.y - i3;
					i22 = chunkPosition19.z - i4;
					d23 = (double)(i20 + i20 * i21 * i21 + i22 * i22);
					if(d23 < d13) {
						d13 = d23;
						chunkPosition26 = chunkPosition19;
					}
				}

				return chunkPosition26;
			} else {
				return null;
			}
		}
	}

	protected List<ChunkPosition> func_40482_a() {
		return null;
	}

	protected abstract boolean canSpawnStructureAtCoords(int i1, int i2);

	protected abstract StructureStart getStructureStart(int i1, int i2);
}
