package net.minecraft.world.entity.block;

import com.mojang.nbt.NBTTagCompound;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.level.World;

public class EntityBlockEntity extends EntityLiving {
	public int xTile;
	public int yTile;
	public int zTile;

	public EntityBlockEntity(World world) {
		super(world);
	}

	public void setTilePosition(int xTile, int yTile, int zTile) {
		this.xTile = xTile;
		this.yTile = yTile;
		this.zTile = zTile;
	}
	
	public void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
		super.writeEntityToNBT(nbtTagCompound);
		nbtTagCompound.setShort("xTile", (short) this.xTile);
		nbtTagCompound.setShort("yTile", (short) this.yTile);
		nbtTagCompound.setShort("zTile", (short) this.zTile);
	}

	public void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
		super.readEntityFromNBT(nbtTagCompound);
		this.xTile = nbtTagCompound.getShort("xTile");
		this.yTile = nbtTagCompound.getShort("yTile");
		this.zTile = nbtTagCompound.getShort("zTile");
	}
}
