package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

import util.MathHelper;

public class EntityRainFX extends EntityFX {
	public EntityRainFX(World world1, double d2, double d4, double d6) {
		super(world1, d2, d4, d6, 0.0D, 0.0D, 0.0D);
		this.motionX *= (double)0.3F;
		this.motionY = (double)((float)Math.random() * 0.2F + 0.1F);
		this.motionZ *= (double)0.3F;
		this.particleRed = 1.0F;
		this.particleGreen = 1.0F;
		this.particleBlue = 1.0F;
		this.particleTextureIndex = 16;
		this.setSize(0.01F, 0.01F);
		this.particleGravity = 0.06F;
		this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
	}

	public final void renderParticle(Tessellator tessellator1, float f2, float f3, float f4, float f5, float f6, float f7) {
		super.renderParticle(tessellator1, f2, f3, f4, f5, f6, f7);
	}

	public final void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= (double)this.particleGravity;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= (double)0.98F;
		this.motionY *= (double)0.98F;
		this.motionZ *= (double)0.98F;
		if(this.particleMaxAge-- <= 0) {
			super.isDead = true;
		}

		if(this.onGround) {
			if(Math.random() < 0.5D) {
				super.isDead = true;
			}

			this.motionX *= (double)0.7F;
			this.motionZ *= (double)0.7F;
		}

		Material material1;
		if((material1 = this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))).getIsLiquid() || material1.isSolid()) {
			super.isDead = true;
		}

	}
}