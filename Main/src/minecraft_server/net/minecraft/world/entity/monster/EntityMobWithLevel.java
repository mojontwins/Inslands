package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.IMobWithLevel;
import net.minecraft.world.level.World;

public class EntityMobWithLevel extends EntityMob implements IMobWithLevel {
	public EntityMobWithLevel(World world) {
		super(world);
	}
	
	protected void entityInit() {
		super.entityInit();
		
		// Store "lvl" in DWO #16
		this.dataWatcher.addObject(16, new Byte((byte) 0));
	}
	
	public int getLvl() {
		return (int)this.dataWatcher.getWatchableObjectByte(16);
	}
	
	public void setLvl(int lvl) {
		this.dataWatcher.updateObject(16, (byte)lvl);
	}
	
	public EntityMobWithLevel(World world, int level) {
		super(world);
		this.setLvl(level);
	}

	public int getLevel() {
		return this.getLvl();
	}

	public void setLevel(int level) {
		this.setLvl(level);
	}

	public int getMaxLevel() {
		return 5;
	}
}
