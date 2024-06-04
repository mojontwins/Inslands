package net.minecraft.src;

import java.util.ArrayList;

public class Loc {
	public final double x;
	public final double y;
	public final double z;

	public Loc() {
		this(0, 0, 0);
	}

	public Loc(int x, int z) {
		this(x, 0, z);
	}

	public Loc(int x, int y, int z) {
		this((double)x, (double)y, (double)z);
	}

	public Loc(double x, double z) {
		this(x, 0.0D, z);
	}

	public Loc(World world) {
		this(world.getSpawnPoint().x, world.getSpawnPoint().y, world.getSpawnPoint().z);
	}

	public Loc(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int x() {
		return (int)this.x;
	}

	public int y() {
		return (int)this.y;
	}

	public int z() {
		return (int)this.z;
	}

	public Loc add(int x, int y, int z) {
		return new Loc(this.x + (double)x, this.y + (double)y, this.z + (double)z);
	}

	public Loc add(double x, double y, double z) {
		return new Loc(this.x + x, this.y + y, this.z + z);
	}

	public Loc add(Loc other) {
		return new Loc(this.x + other.x, this.y + other.y, this.z + other.z);
	}

	public Loc subtract(int x, int y, int z) {
		return new Loc(this.x - (double)x, this.y - (double)y, this.z - (double)z);
	}

	public Loc subtract(double x, double y, double z) {
		return new Loc(this.x - x, this.y - y, this.z - z);
	}

	public Loc subtract(Loc other) {
		return new Loc(this.x - other.x, this.y - other.y, this.z - other.z);
	}

	public Loc multiply(double xMult, double yMult, double zMult) {
		return new Loc(this.x * xMult, this.y * yMult, this.z * zMult);
	}

	public Loc getSide(int side) {
		return side == 0 ? new Loc(this.x, this.y - 1.0D, this.z) : (side == 1 ? new Loc(this.x, this.y + 1.0D, this.z) : (side == 2 ? new Loc(this.x, this.y, this.z - 1.0D) : (side == 3 ? new Loc(this.x, this.y, this.z + 1.0D) : (side == 4 ? new Loc(this.x - 1.0D, this.y, this.z) : (side == 5 ? new Loc(this.x + 1.0D, this.y, this.z) : this)))));
	}

	public boolean equals(Object other) {
		if(other instanceof Loc) {
			Loc otherLoc = (Loc)other;
			return this.x == otherLoc.x && this.y == otherLoc.y && this.z == otherLoc.z;
		} else {
			return false;
		}
	}

	public String toString() {
		return "(" + this.x + "," + this.y + "," + this.z + ")";
	}

	public int distSimple(Loc other) {
		return (int)(Math.abs(this.x - other.x) + Math.abs(this.y - other.y) + Math.abs(this.z - other.z));
	}

	public double distAdv(Loc other) {
		return Math.sqrt(Math.pow(this.x - other.x, 2.0D) + Math.pow(this.y - other.y, 2.0D) + Math.pow(this.z - other.z, 2.0D));
	}

	public static Loc[] vecAdjacent() {
		Loc[] array = new Loc[]{new Loc(0, 0, 1), new Loc(0, 0, -1), new Loc(0, 1, 0), new Loc(0, -1, 0), new Loc(1, 0, 0), new Loc(-1, 0, 0)};
		return array;
	}

	public Loc[] adjacent() {
		Loc[] array = vecAdjacent();

		for(int i = 0; i < array.length; ++i) {
			array[i] = this.add(array[i]);
		}

		return array;
	}

	public static Loc[] vecAdjacent2D() {
		Loc[] array = new Loc[]{new Loc(0, 1), new Loc(0, -1), new Loc(1, 0), new Loc(-1, 0)};
		return array;
	}

	public Loc[] adjacent2D() {
		Loc[] array = vecAdjacent();

		for(int i = 0; i < array.length; ++i) {
			array[i] = this.add(array[i]);
		}

		return array;
	}

	public static ArrayList vecInRadius(int maxR, boolean advanced) {
		ArrayList toReturn = new ArrayList();
		Loc start = new Loc();

		for(int x = -maxR; x <= maxR; ++x) {
			for(int z = -maxR; z <= maxR; ++z) {
				for(int y = -maxR; y <= maxR; ++y) {
					Loc check = new Loc(x, y, z);
					double dist = advanced ? start.distAdv(check) : (double)start.distSimple(check);
					if(dist <= (double)maxR) {
						toReturn.add(check);
					}
				}
			}
		}

		return toReturn;
	}

	public ArrayList inRadius(int maxR, boolean advanced) {
		ArrayList toReturn = new ArrayList();

		for(int x = -maxR; x <= maxR; ++x) {
			for(int z = -maxR; z <= maxR; ++z) {
				for(int y = -maxR; y <= maxR; ++y) {
					Loc check = (new Loc(x, y, z)).add(this);
					double dist = advanced ? this.distAdv(check) : (double)this.distSimple(check);
					if(dist <= (double)maxR) {
						toReturn.add(check);
					}
				}
			}
		}

		return toReturn;
	}

	public static ArrayList vecInRadius2D(int maxR, boolean advanced) {
		ArrayList toReturn = new ArrayList();
		Loc start = new Loc();

		for(int x = -maxR; x <= maxR; ++x) {
			for(int z = -maxR; z <= maxR; ++z) {
				Loc check = new Loc(x, z);
				double dist = advanced ? start.distAdv(check) : (double)start.distSimple(check);
				if(dist <= (double)maxR) {
					toReturn.add(check);
				}
			}
		}

		return toReturn;
	}

	public ArrayList inRadius2D(int maxR, boolean advanced) {
		ArrayList toReturn = new ArrayList();

		for(int x = -maxR; x <= maxR; ++x) {
			for(int z = -maxR; z <= maxR; ++z) {
				Loc check = (new Loc(x, z)).add(this);
				double dist = advanced ? this.distAdv(check) : (double)this.distSimple(check);
				if(dist <= (double)maxR) {
					toReturn.add(check);
				}
			}
		}

		return toReturn;
	}

	public int getBlock(IBlockAccess blockAc) {
		return blockAc.getBlockId(this.x(), this.y(), this.z());
	}

	public Loc setBlockNotify(World world, int blockID) {
		world.setBlockWithNotify(this.x(), this.y(), this.z(), blockID);
		return this;
	}

	public Loc setBlock(World world, int blockID) {
		world.setBlock(this.x(), this.y(), this.z(), blockID);
		return this;
	}

	public int getMeta(IBlockAccess blockAc) {
		return blockAc.getBlockMetadata(this.x(), this.y(), this.z());
	}

	public Loc setMeta(World world, int meta) {
		world.setBlockMetadata(this.x(), this.y(), this.z(), meta);
		return this;
	}

	public Loc setMetaNotify(World world, int meta) {
		world.setBlockMetadataWithNotify(this.x(), this.y(), this.z(), meta);
		return this;
	}

	public Loc setBlockAndMeta(World world, int blockID, int meta) {
		world.setBlockAndMetadata(this.x(), this.y(), this.z(), blockID, meta);
		return this;
	}

	public Loc setBlockAndMetaNotify(World world, int blockID, int meta) {
		world.setBlockAndMetadataWithNotify(this.x(), this.y(), this.z(), blockID, meta);
		return this;
	}

	public TileEntity getTileEntity(IBlockAccess blockAc) {
		return blockAc.getBlockTileEntity(this.x(), this.y(), this.z());
	}

	public Loc setTileEntity(World world, TileEntity tileEntity) {
		world.setBlockTileEntity(this.x(), this.y(), this.z(), tileEntity);
		return this;
	}

	public Loc removeTileEntity(World world) {
		world.removeBlockTileEntity(this.x(), this.y(), this.z());
		return this;
	}

	public int getLight(World world) {
		return world.getFullBlockLightValue(this.x(), this.y(), this.z());
	}

	public Loc notify(World world) {
		world.notifyBlockChange(this.x(), this.y(), this.z(), this.getBlock(world));
		return this;
	}

	public Loc setSpawnPoint(World world) {
		world.setSpawnPoint(new ChunkCoordinates(this.x(), this.y(), this.z()));
		return this;
	}
}
