package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.world.World;
import util.MathHelper;

public class EntityFX extends Entity {
	protected int particleTextureIndex;
	protected float particleTextureJitterX;
	protected float particleTextureJitterY;
	protected int particleAge = 0;
	protected int particleMaxAge = 0;
	protected float particleScale;
	protected float particleGravity;
	protected float particleRed;
	protected float particleGreen;
	protected float particleBlue;

	public EntityFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
		super(var1);
		this.setSize(0.2F, 0.2F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(var2, var4, var6);
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.motionX = var8 + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.4F);
		this.motionY = var10 + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.4F);
		this.motionZ = var12 + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.4F);
		float var14 = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
		float var15 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
		this.motionX = this.motionX / (double)var15 * (double)var14 * (double)0.4F;
		this.motionY = this.motionY / (double)var15 * (double)var14 * (double)0.4F + (double)0.1F;
		this.motionZ = this.motionZ / (double)var15 * (double)var14 * (double)0.4F;
		this.particleTextureJitterX = this.rand.nextFloat() * 3.0F;
		this.particleTextureJitterY = this.rand.nextFloat() * 3.0F;
		this.particleScale = (this.rand.nextFloat() * 0.5F + 0.5F) * 2.0F;
		this.particleMaxAge = (int)(4.0F / (this.rand.nextFloat() * 0.9F + 0.1F));
		this.particleAge = 0;
		this.canTriggerWalking = false;
	}

	public final EntityFX multipleParticleScaleBy(float var1) {
		this.setSize(0.120000005F, 0.120000005F);
		this.particleScale *= 0.6F;
		return this;
	}

	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if(this.particleAge++ >= this.particleMaxAge) {
			this.setEntityDead();
		}

		this.motionY -= 0.04D * (double)this.particleGravity;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= (double)0.98F;
		this.motionY *= (double)0.98F;
		this.motionZ *= (double)0.98F;
		if(this.onGround) {
			this.motionX *= (double)0.7F;
			this.motionZ *= (double)0.7F;
		}

	}

	public void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		float var8 = (float)(this.particleTextureIndex % 16) / 16.0F;
		float var9 = var8 + 0.999F / 16.0F;
		float var10 = (float)(this.particleTextureIndex / 16) / 16.0F;
		float var11 = var10 + 0.999F / 16.0F;
		float var12 = 0.1F * this.particleScale;
		float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)var2);
		float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)var2);
		float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)var2);
		var2 = this.getBrightness(var2);
		var1.setColorOpaque_F(this.particleRed * var2, this.particleGreen * var2, this.particleBlue * var2);
		var1.addVertexWithUV(var13 - var3 * var12 - var6 * var12, var14 - var4 * var12, var15 - var5 * var12 - var7 * var12, var8, var11);
		var1.addVertexWithUV(var13 - var3 * var12 + var6 * var12, var14 + var4 * var12, var15 - var5 * var12 + var7 * var12, var8, var10);
		var1.addVertexWithUV(var13 + var3 * var12 + var6 * var12, var14 + var4 * var12, var15 + var5 * var12 + var7 * var12, var9, var10);
		var1.addVertexWithUV(var13 + var3 * var12 - var6 * var12, var14 - var4 * var12, var15 + var5 * var12 - var7 * var12, var9, var11);
	}

	public int getFXLayer() {
		return 0;
	}
}
