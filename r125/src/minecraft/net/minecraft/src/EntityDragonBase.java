package net.minecraft.src;

public class EntityDragonBase extends EntityLiving {
	protected int maxHealth = 100;

	public EntityDragonBase(World world1) {
		super(world1);
	}

	public int getMaxHealth() {
		return this.maxHealth;
	}

	public boolean attackEntityFromPart(EntityDragonPart entityDragonPart1, DamageSource damageSource2, int i3) {
		return this.attackEntityFrom(damageSource2, i3);
	}

	public boolean attackEntityFrom(DamageSource damageSource1, int i2) {
		return false;
	}

	protected boolean superAttackFrom(DamageSource damageSource1, int i2) {
		return super.attackEntityFrom(damageSource1, i2);
	}
}
