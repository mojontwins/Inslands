package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public final class EntityDiggingFX extends EntityFX {
	public EntityDiggingFX(World world1, double d2, double d4, double d6, double d8, double d10, double d12, Block block14) {
		super(world1, d2, d4, d6, d8, d10, d12);
		this.particleTextureIndex = block14.blockIndexInTexture;
		this.particleGravity = block14.blockParticleGravity;
		this.particleRed = this.particleGreen = this.particleBlue = 0.6F;
		this.particleScale /= 2.0F;
	}

	public final int getFXLayer() {
		return 1;
	}

	public final void renderParticle(Tessellator tessellator1, float f2, float f3, float f4, float f5, float f6, float f7) {
		float f8;
		float f9 = (f8 = ((float)(this.particleTextureIndex % 16) + this.particleTextureJitterX / 4.0F) / 16.0F) + 0.015609375F;
		float f10;
		float f11 = (f10 = ((float)(this.particleTextureIndex / 16) + this.particleTextureJitterY / 4.0F) / 16.0F) + 0.015609375F;
		float f12 = 0.1F * this.particleScale;
		float f13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)f2 - interpPosX);
		float f14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)f2 - interpPosY);
		float f15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)f2 - interpPosZ);
		f2 = this.getEntityBrightness(f2);
		tessellator1.setColorOpaque_F(f2 * this.particleRed, f2 * this.particleGreen, f2 * this.particleBlue);
		tessellator1.addVertexWithUV((double)(f13 - f3 * f12 - f6 * f12), (double)(f14 - f4 * f12), (double)(f15 - f5 * f12 - f7 * f12), (double)f8, (double)f11);
		tessellator1.addVertexWithUV((double)(f13 - f3 * f12 + f6 * f12), (double)(f14 + f4 * f12), (double)(f15 - f5 * f12 + f7 * f12), (double)f8, (double)f10);
		tessellator1.addVertexWithUV((double)(f13 + f3 * f12 + f6 * f12), (double)(f14 + f4 * f12), (double)(f15 + f5 * f12 + f7 * f12), (double)f9, (double)f10);
		tessellator1.addVertexWithUV((double)(f13 + f3 * f12 - f6 * f12), (double)(f14 - f4 * f12), (double)(f15 + f5 * f12 - f7 * f12), (double)f9, (double)f11);
	}
}