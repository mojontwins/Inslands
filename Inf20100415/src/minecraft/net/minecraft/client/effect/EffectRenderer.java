package net.minecraft.client.effect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

import org.lwjgl.opengl.GL11;

import util.MathHelper;

public final class EffectRenderer {
	private World worldObj;
	private List[] fxLayers = new List[3];
	private RenderEngine renderer;
	private Random rand = new Random();

	public EffectRenderer(World world1, RenderEngine renderEngine2) {
		if(world1 != null) {
			this.worldObj = world1;
		}

		this.renderer = renderEngine2;

		for(int i3 = 0; i3 < 3; ++i3) {
			this.fxLayers[i3] = new ArrayList();
		}

	}

	public final void addEffect(EntityFX entityFX1) {
		int i2 = entityFX1.getFXLayer();
		this.fxLayers[i2].add(entityFX1);
	}

	public final void updateEffects() {
		for(int i1 = 0; i1 < 3; ++i1) {
			for(int i2 = 0; i2 < this.fxLayers[i1].size(); ++i2) {
				EntityFX entityFX3;
				(entityFX3 = (EntityFX)this.fxLayers[i1].get(i2)).onUpdate();
				if(entityFX3.isDead) {
					this.fxLayers[i1].remove(i2--);
				}
			}
		}

	}

	public final void renderParticles(Entity entity1, float f2) {
		float f3 = MathHelper.cos(entity1.rotationYaw * (float)Math.PI / 180.0F);
		float f4;
		float f5 = -(f4 = MathHelper.sin(entity1.rotationYaw * (float)Math.PI / 180.0F)) * MathHelper.sin(entity1.rotationPitch * (float)Math.PI / 180.0F);
		float f6 = f3 * MathHelper.sin(entity1.rotationPitch * (float)Math.PI / 180.0F);
		float f7 = MathHelper.cos(entity1.rotationPitch * (float)Math.PI / 180.0F);
		EntityFX.interpPosX = entity1.lastTickPosX + (entity1.posX - entity1.lastTickPosX) * (double)f2;
		EntityFX.interpPosY = entity1.lastTickPosY + (entity1.posY - entity1.lastTickPosY) * (double)f2;
		EntityFX.interpPosZ = entity1.lastTickPosZ + (entity1.posZ - entity1.lastTickPosZ) * (double)f2;

		for(int i11 = 0; i11 < 2; ++i11) {
			if(this.fxLayers[i11].size() != 0) {
				int i8 = 0;
				if(i11 == 0) {
					i8 = this.renderer.getTexture("/particles.png");
				}

				if(i11 == 1) {
					i8 = this.renderer.getTexture("/terrain.png");
				}

				GL11.glBindTexture(GL11.GL_TEXTURE_2D, i8);
				Tessellator tessellator12 = Tessellator.instance;
				Tessellator.instance.startDrawingQuads();

				for(int i9 = 0; i9 < this.fxLayers[i11].size(); ++i9) {
					((EntityFX)this.fxLayers[i11].get(i9)).renderParticle(tessellator12, f2, f3, f7, f4, f5, f6);
				}

				tessellator12.draw();
			}
		}

	}

	public final void renderLitParticles(float f1) {
		if(this.fxLayers[2].size() != 0) {
			Tessellator tessellator2 = Tessellator.instance;

			for(int i3 = 0; i3 < this.fxLayers[2].size(); ++i3) {
				((EntityFX)this.fxLayers[2].get(i3)).renderParticle(tessellator2, f1, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
			}

		}
	}

	public final void clearEffects(World world1) {
		this.worldObj = world1;

		for(int i2 = 0; i2 < 3; ++i2) {
			this.fxLayers[i2].clear();
		}

	}

	public final void addBlockDestroyEffects(int i1, int i2, int i3) {
		int i4;
		if((i4 = this.worldObj.getBlockId(i1, i2, i3)) != 0) {
			Block block15 = Block.blocksList[i4];

			for(int i5 = 0; i5 < 4; ++i5) {
				for(int i6 = 0; i6 < 4; ++i6) {
					for(int i7 = 0; i7 < 4; ++i7) {
						double d9 = (double)i1 + ((double)i5 + 0.5D) / 4.0D;
						double d11 = (double)i2 + ((double)i6 + 0.5D) / 4.0D;
						double d13 = (double)i3 + ((double)i7 + 0.5D) / 4.0D;
						this.addEffect(new EntityDiggingFX(this.worldObj, d9, d11, d13, d9 - (double)i1 - 0.5D, d11 - (double)i2 - 0.5D, d13 - (double)i3 - 0.5D, block15));
					}
				}
			}

		}
	}

	public final void addBlockHitEffects(int i1, int i2, int i3, int i4) {
		int i5;
		if((i5 = this.worldObj.getBlockId(i1, i2, i3)) != 0) {
			Block block15 = Block.blocksList[i5];
			double d7 = (double)i1 + this.rand.nextDouble() * (block15.maxX - block15.minX - (double)0.2F) + (double)0.1F + block15.minX;
			double d9 = (double)i2 + this.rand.nextDouble() * (block15.maxY - block15.minY - (double)0.2F) + (double)0.1F + block15.minY;
			double d11 = (double)i3 + this.rand.nextDouble() * (block15.maxZ - block15.minZ - (double)0.2F) + (double)0.1F + block15.minZ;
			if(i4 == 0) {
				d9 = (double)i2 + block15.minY - (double)0.1F;
			}

			if(i4 == 1) {
				d9 = (double)i2 + block15.maxY + (double)0.1F;
			}

			if(i4 == 2) {
				d11 = (double)i3 + block15.minZ - (double)0.1F;
			}

			if(i4 == 3) {
				d11 = (double)i3 + block15.maxZ + (double)0.1F;
			}

			if(i4 == 4) {
				d7 = (double)i1 + block15.minX - (double)0.1F;
			}

			if(i4 == 5) {
				d7 = (double)i1 + block15.maxX + (double)0.1F;
			}

			EntityDiggingFX entityDiggingFX10001 = new EntityDiggingFX(this.worldObj, d7, d9, d11, 0.0D, 0.0D, 0.0D, block15);
			float f13 = 0.2F;
			EntityDiggingFX entityDiggingFX14 = entityDiggingFX10001;
			entityDiggingFX10001.motionX *= (double)0.2F;
			entityDiggingFX14.motionY = (entityDiggingFX14.motionY - (double)0.1F) * (double)0.2F + (double)0.1F;
			entityDiggingFX14.motionZ *= (double)0.2F;
			this.addEffect(entityDiggingFX14.multiplyParticleScaleBy(0.6F));
		}
	}

	public final String getStatistics() {
		return "" + (this.fxLayers[0].size() + this.fxLayers[1].size() + this.fxLayers[2].size());
	}
}