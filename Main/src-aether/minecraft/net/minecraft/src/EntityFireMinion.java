package net.minecraft.src;

public class EntityFireMinion extends EntityMob {
	public EntityFireMinion(World world) {
		super(world);
		this.texture = "/aether/mobs/firemonster.png";
		this.moveSpeed = 1.5F;
		this.attackStrength = 5;
		this.health = 40;
		this.isImmuneToFire = true;
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.health > 0) {
			for(int j = 0; j < 4; ++j) {
				double a = (double)(this.rand.nextFloat() - 0.5F);
				double b = (double)this.rand.nextFloat();
				double c = (double)(this.rand.nextFloat() - 0.5F);
				double d = this.posX + a * b;
				double e = this.boundingBox.minY + b - 0.5D;
				double f = this.posZ + c * b;
				this.worldObj.spawnParticle("flame", d, e, f, 0.0D, -0.07500000298023224D, 0.0D);
			}
		}

	}
}
