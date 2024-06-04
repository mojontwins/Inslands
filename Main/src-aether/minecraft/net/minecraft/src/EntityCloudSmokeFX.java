package net.minecraft.src;

public class EntityCloudSmokeFX extends EntityFX {
	float field_671_a;

	public EntityCloudSmokeFX(World world, double x, double y, double z, double initialMotionX, double initialMotionY, double intialMotionZ, float size, float red, float blue, float green) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);
		this.motionX *= (double)0.1F;
		this.motionY *= (double)0.1F;
		this.motionZ *= (double)0.1F;
		this.motionX += initialMotionX;
		this.motionY += initialMotionY;
		this.motionZ += intialMotionZ;
		this.particleRed = red;
		this.particleBlue = blue;
		this.particleGreen = green;
		this.particleScale *= 0.75F;
		this.particleScale *= size;
		this.field_671_a = this.particleScale;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
		this.particleMaxAge = (int)((float)this.particleMaxAge * size);
		this.noClip = false;
	}

	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
		float f6 = ((float)this.particleAge + f) / (float)this.particleMaxAge * 32.0F;
		if(f6 < 0.0F) {
			f6 = 0.0F;
		}

		if(f6 > 1.0F) {
			f6 = 1.0F;
		}

		this.particleScale = this.field_671_a * f6;
		super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
	}

	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if(this.particleAge++ >= this.particleMaxAge) {
			this.setEntityDead();
		}

		this.particleTextureIndex = 7 - this.particleAge * 8 / this.particleMaxAge;
		this.motionY += 0.004D;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		if(this.posY == this.prevPosY) {
			this.motionX *= 1.1D;
			this.motionZ *= 1.1D;
		}

		this.motionX *= (double)0.96F;
		this.motionY *= (double)0.96F;
		this.motionZ *= (double)0.96F;
		if(this.onGround) {
			this.motionX *= (double)0.7F;
			this.motionZ *= (double)0.7F;
		}

	}
}
