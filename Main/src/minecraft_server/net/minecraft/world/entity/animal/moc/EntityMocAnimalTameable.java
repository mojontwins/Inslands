package net.minecraft.world.entity.animal.moc;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.world.entity.Datawatchers;
import net.minecraft.world.level.World;

public class EntityMocAnimalTameable extends EntityMoCAnimal {
	public EntityMocAnimalTameable(World world) {
		super(world);
		this.setIsTamed(false);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(Datawatchers.DW_TAMED, Byte.valueOf((byte)0));
	}

	public boolean getIsTamed() {
		return (this.dataWatcher.getWatchableObjectByte(Datawatchers.DW_TAMED) == 1);
	}

	public void setIsTamed(boolean flag) {
		byte input = (byte) (flag ? 1 : 0);
		this.dataWatcher.updateObject(Datawatchers.DW_TAMED, Byte.valueOf(input));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("Tamed", this.getIsTamed());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.setIsTamed(nbttagcompound.getBoolean("Tamed"));
	}

}
