package net.minecraft.src;

public class EntityBlockEntity extends EntityLiving {
	public int xTile;
	public int yTile;
	public int zTile;

	public EntityBlockEntity(World world1) {
		super(world1);
	}

	public void setTilePosition(int xTile, int yTile, int zTile) {
		this.xTile = xTile;
		this.yTile = yTile;
		this.zTile = zTile;
	}
	
	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
		nBTTagCompound1.setShort("xTile", (short)this.xTile);
		nBTTagCompound1.setShort("yTile", (short)this.yTile);
		nBTTagCompound1.setShort("zTile", (short)this.zTile);
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
		this.xTile = nBTTagCompound1.getShort("xTile");
		this.yTile = nBTTagCompound1.getShort("yTile");
		this.zTile = nBTTagCompound1.getShort("zTile");
	}
}
