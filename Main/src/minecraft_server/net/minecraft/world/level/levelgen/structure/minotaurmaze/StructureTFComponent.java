package net.minecraft.world.level.levelgen.structure.minotaurmaze;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.TileEntityMobSpawner;
import net.minecraft.src.World;
import net.minecraft.world.level.levelgen.TFTreasure;
import net.minecraft.world.level.levelgen.structure.StructureBoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureComponent;

public abstract class StructureTFComponent extends StructureComponent {
	//private static final StructureTFStrongholdStones strongholdStones = new StructureTFStrongholdStones();

	public StructureTFComponent(int i) {
		super(i);
	}

	public static StructureBoundingBox getComponentToAddBoundingBox(int x, int y, int z, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int dir) {
		switch(dir) {
		case 0:
			return new StructureBoundingBox(x + minX, y + minY, z + minZ, x + maxX + minX, y + maxY + minY, z + maxZ + minZ);
		case 1:
			return new StructureBoundingBox(x - maxZ + minZ, y + minY, z + minX, x + minZ, y + maxY + minY, z + maxX + minX);
		case 2:
			return new StructureBoundingBox(x - maxX - minX, y + minY, z - maxZ - minZ, x - minX, y + maxY + minY, z - minZ);
		case 3:
			return new StructureBoundingBox(x + minZ, y + minY, z - maxX, x + maxZ + minZ, y + maxY + minY, z + minX);
		default:
			return new StructureBoundingBox(x + minX, y + minY, z + minZ, x + maxX + minX, y + maxY + minY, z + maxZ + minZ);
		}
	}

	public void placeSpawnerAtCurrentPosition(World world, Random rand, int x, int y, int z, String monsterID, StructureBoundingBox sbb) {
		int dx = this.getXWithOffset(x, z);
		int dy = this.getYWithOffset(y);
		int dz = this.getZWithOffset(x, z);
		if(sbb.isVecInside(dx, dy, dz) && world.getBlockId(dx, dy, dz) != Block.mobSpawner.blockID) {
			world.setBlockWithNotify(dx, dy, dz, Block.mobSpawner.blockID);
			TileEntityMobSpawner tileEntitySpawner = (TileEntityMobSpawner)world.getBlockTileEntity(dx, dy, dz);
			if(tileEntitySpawner != null) {
				tileEntitySpawner.setMobID(monsterID);
			}
		}

	}

	public void placeTreasureAtCurrentPosition(World world, Random rand, int x, int y, int z, TFTreasure treasureType, StructureBoundingBox sbb) {
		int dx = this.getXWithOffset(x, z);
		int dy = this.getYWithOffset(y);
		int dz = this.getZWithOffset(x, z);
		if(sbb.isVecInside(dx, dy, dz) && world.getBlockId(dx, dy, dz) != Block.chest.blockID) {
			treasureType.generate(world, rand, dx, dy, dz);
		}

	}

	public int[] offsetTowerCoords(int x, int y, int z, int towerSize, int direction) {
		int dx = this.getXWithOffset(x, z);
		int dy = this.getYWithOffset(y);
		int dz = this.getZWithOffset(x, z);
		return direction == 0 ? new int[]{dx + 1, dy - 1, dz - towerSize / 2} : (direction == 1 ? new int[]{dx + towerSize / 2, dy - 1, dz + 1} : (direction == 2 ? new int[]{dx - 1, dy - 1, dz + towerSize / 2} : (direction == 3 ? new int[]{dx - towerSize / 2, dy - 1, dz - 1} : new int[]{x, y, z})));
	}

	public int[] getOffsetAsIfRotated(int[] src, int rotation) {
		int temp = this.getCoordBaseMode();
		int[] dest = new int[3];
		this.setCoordBaseMode(rotation);
		dest[0] = this.getXWithOffset(src[0], src[2]);
		dest[1] = this.getYWithOffset(src[1]);
		dest[2] = this.getZWithOffset(src[0], src[2]);
		this.setCoordBaseMode(temp);
		return dest;
	}

	public int getXWithOffset(int x, int z) {
		switch(this.getCoordBaseMode()) {
		case 0:
			return this.boundingBox.minX + x;
		case 1:
			return this.boundingBox.maxX - z;
		case 2:
			return this.boundingBox.maxX - x;
		case 3:
			return this.boundingBox.minX + z;
		default:
			return x;
		}
	}

	public int getZWithOffset(int x, int z) {
		switch(this.getCoordBaseMode()) {
		case 0:
			return this.boundingBox.minZ + z;
		case 1:
			return this.boundingBox.minZ + x;
		case 2:
			return this.boundingBox.maxZ - z;
		case 3:
			return this.boundingBox.maxZ - x;
		default:
			return z;
		}
	}

	public int getYWithOffset(int y) {
		return super.getYWithOffset(y);
	}

	public int getCoordBaseMode() {
		return this.coordBaseMode;
	}

	public void setCoordBaseMode(int coordBaseMode) {
		this.coordBaseMode = coordBaseMode;
	}

	public int getBlockIdAtCurrentPosition(World par1World, int par2, int par3, int par4, StructureBoundingBox par5StructureBoundingBox) {
		return super.getBlockIdAtCurrentPosition(par1World, par2, par3, par4, par5StructureBoundingBox);
	}

	public void placeBlockAtCurrentPosition(World par1World, int par2, int par3, int par4, int par5, int par6, StructureBoundingBox par7StructureBoundingBox) {
		super.placeBlockAtCurrentPosition(par1World, par2, par3, par4, par5, par6, par7StructureBoundingBox);
	}

	/*
	public static StructurePieceBlockSelector getStrongholdStones() {
		return strongholdStones;
	}
	*/
	
	protected int getStairMeta(int var1) {
		switch ((this.getCoordBaseMode() + var1) % 4) {
			case 0:
				return 0;

			case 1:
				return 2;

			case 2:
				return 1;

			case 3:
				return 3;

			default:
				return -1;
		}
	}
}
