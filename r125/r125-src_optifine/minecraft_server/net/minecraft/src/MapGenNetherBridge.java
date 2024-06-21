package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class MapGenNetherBridge extends MapGenStructure {
	private List spawnList = new ArrayList();

	public MapGenNetherBridge() {
		this.spawnList.add(new SpawnListEntry(EntityBlaze.class, 10, 2, 3));
		this.spawnList.add(new SpawnListEntry(EntityPigZombie.class, 10, 4, 4));
		this.spawnList.add(new SpawnListEntry(EntityMagmaCube.class, 3, 4, 4));
	}

	public List getSpawnList() {
		return this.spawnList;
	}

	protected boolean canSpawnStructureAtCoords(int i1, int i2) {
		int i3 = i1 >> 4;
		int i4 = i2 >> 4;
		this.rand.setSeed((long)(i3 ^ i4 << 4) ^ this.worldObj.getSeed());
		this.rand.nextInt();
		return this.rand.nextInt(3) != 0 ? false : (i1 != (i3 << 4) + 4 + this.rand.nextInt(8) ? false : i2 == (i4 << 4) + 4 + this.rand.nextInt(8));
	}

	protected StructureStart getStructureStart(int i1, int i2) {
		return new StructureNetherBridgeStart(this.worldObj, this.rand, i1, i2);
	}
}
