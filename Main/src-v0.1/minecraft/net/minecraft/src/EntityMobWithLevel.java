package net.minecraft.src;

public class EntityMobWithLevel extends EntityMob {
	protected int level;
	
	public EntityMobWithLevel(World world) {
		super(world);
		this.level = this.rand.nextInt(2);
	}

	public EntityMobWithLevel(World world, int level) {
		super(world);
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
