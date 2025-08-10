package net.minecraft.client.renderer.entity.betterdungeons;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.betterdungeons.ModelHuman;
import net.minecraft.client.renderer.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.src.Block;
import net.minecraft.src.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.monster.EntityHumanBase;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RenderHuman extends Render {
	protected ModelHuman mainModel;
	protected ModelBase renderPassModel;

	public RenderHuman(ModelBase modelbase, float f) {
		this.mainModel = (ModelHuman)modelbase;
		this.shadowSize = f;
	}

	public void setRenderPassModel(ModelBase modelbase) {
		this.renderPassModel = modelbase;
	}

	protected void renderModel(EntityLiving entityliving, float f, float f1, float f2, float f3, float f4, float f5) {
		EntityHumanBase z = (EntityHumanBase)entityliving;
		this.renderManager.renderEngine.bindTexture(this.renderManager.renderEngine.getTexture(entityliving.getEntityTexture()));
		this.mainModel.render(z, f, f1, f2, f3, f4, f5);
		if(z.renderEars) {
			this.mainModel.renderEars(f5);
		}

		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		this.renderManager.renderEngine.bindTexture(this.renderManager.renderEngine.getTexture(z.armorTexture + "_1.png"));
		this.mainModel.renderArmor(z, f, f1, f2, f3, f4, f5);
		this.renderManager.renderEngine.bindTexture(this.renderManager.renderEngine.getTexture(z.armorTexture + "_2.png"));
		this.mainModel.renderArmorLegs(z, f, f1, f2, f3, f4, f5);
	}

	public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		this.mainModel.swingProgress = this.renderSwingProgress(entityliving, f1);
		if(this.renderPassModel != null) {
			this.renderPassModel.swingProgress = this.mainModel.swingProgress;
		}

		this.mainModel.isRiding = entityliving.isRiding();
		if(this.renderPassModel != null) {
			this.renderPassModel.isRiding = this.mainModel.isRiding;
		}

		/*
		this.mainModel.isRiding = entityliving.isChild();
		if(this.renderPassModel != null) {
			this.renderPassModel.isRiding = this.mainModel.isRiding;
		}
		*/

		try {
			float exception = entityliving.prevRenderYawOffset + (entityliving.renderYawOffset - entityliving.prevRenderYawOffset) * f1;
			float f3 = entityliving.prevRotationYaw + (entityliving.rotationYaw - entityliving.prevRotationYaw) * f1;
			float f4 = entityliving.prevRotationPitch + (entityliving.rotationPitch - entityliving.prevRotationPitch) * f1;
			this.renderLivingAt(entityliving, d, d1, d2);
			float f5 = this.handleRotationFloat(entityliving, f1);
			this.rotateCorpse(entityliving, f5, exception, f1);
			float f6 = 0.0625F;
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glScalef(-1.0F, -1.0F, 1.0F);
			this.preRenderCallback(entityliving, f1);
			GL11.glTranslatef(0.0F, -24.0F * f6 - 0.0078125F, 0.0F);
			float f7 = entityliving.prevLimbYaw + (entityliving.limbYaw - entityliving.prevLimbYaw) * f1;
			float f8 = entityliving.limbSwing - entityliving.limbYaw * (1.0F - f1);
			
			/*
			if(entityliving.isChild()) {
				f8 *= 3.0F;
			}
			*/

			if(f7 > 1.0F) {
				f7 = 1.0F;
			}

			GL11.glEnable(GL11.GL_ALPHA_TEST);
			this.mainModel.setLivingAnimations(entityliving, f8, f7, f1);
			this.renderModel(entityliving, f8, f7, f5, f3 - exception, f4, f6);
			this.renderEquippedItems(entityliving, f1);
			float f9 = entityliving.getEntityBrightness(f1);
			int k = this.getColorMultiplier(entityliving, f9, f1);
			
			OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
			
			if((k >> 24 & 255) > 0 || entityliving.hurtTime > 0 || entityliving.deathTime > 0) {
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glDepthFunc(GL11.GL_EQUAL);
				if(entityliving.hurtTime > 0 || entityliving.deathTime > 0) {
					GL11.glColor4f(f9, 0.0F, 0.0F, 0.4F);
					this.mainModel.render(entityliving, f8, f7, f5, f3 - exception, f4, f6);

					for(int f11 = 0; f11 < 4; ++f11) {
						if(this.inheritRenderPass(entityliving, f11, f1)) {
							GL11.glColor4f(f9, 0.0F, 0.0F, 0.4F);
							this.renderPassModel.render(f8, f7, f5, f3 - exception, f4, f6);
						}
					}
				}

				if((k >> 24 & 255) > 0) {
					float f25 = (float)(k >> 16 & 255) / 255.0F;
					float f13 = (float)(k >> 8 & 255) / 255.0F;
					float f14 = (float)(k & 255) / 255.0F;
					float f16 = (float)(k >> 24 & 255) / 255.0F;
					GL11.glColor4f(f25, f13, f14, f16);
					this.mainModel.render(entityliving, f8, f7, f5, f3 - exception, f4, f6);

					for(int j1 = 0; j1 < 4; ++j1) {
						if(this.inheritRenderPass(entityliving, j1, f1)) {
							GL11.glColor4f(f25, f13, f14, f16);
							this.renderPassModel.render(f8, f7, f5, f3 - exception, f4, f6);
						}
					}
				}

				GL11.glDepthFunc(GL11.GL_LEQUAL);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}

			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		} catch (Exception exception24) {
			exception24.printStackTrace();
		}

		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
		this.passSpecialRender(entityliving, d, d1, d2);
	}

	protected void renderEquippedItems(EntityLiving entityliving, float f) {
		ItemStack itemstack = entityliving.getHeldItem();
		if(itemstack != null) {
			GL11.glPushMatrix();
			this.mainModel.bipedRightArm.postRender(0.0625F);
			GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
			float f4;
			if(itemstack.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType())) {
				f4 = 0.5F;
				GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
				f4 *= 0.75F;
				GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(f4, -f4, f4);
			} else if(itemstack.itemID == Item.bow.shiftedIndex) {
				f4 = 0.625F;
				GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
				GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(f4, -f4, f4);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else if(Item.itemsList[itemstack.itemID].isFull3D()) {
				f4 = 0.625F;
				GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
				GL11.glScalef(f4, -f4, f4);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else {
				f4 = 0.375F;
				GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
				GL11.glScalef(f4, f4, f4);
				GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			this.renderManager.itemRenderer.renderItem(entityliving, itemstack);
			
			/*
			if(itemstack.itemID == Item.potion.shiftedIndex) {
				this.renderManager.itemRenderer.renderItem(entityliving, itemstack, 1);
			}
			*/

			GL11.glPopMatrix();
		}

	}

	protected void renderLivingAt(EntityLiving entityliving, double d, double d1, double d2) {
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
	}

	protected void rotateCorpse(EntityLiving entityliving, float f, float f1, float f2) {
		GL11.glRotatef(180.0F - f1, 0.0F, 1.0F, 0.0F);
		if(entityliving.deathTime > 0) {
			float f3 = ((float)entityliving.deathTime + f2 - 1.0F) / 20.0F * 1.6F;
			f3 = MathHelper.sqrt_float(f3);
			if(f3 > 1.0F) {
				f3 = 1.0F;
			}

			GL11.glRotatef(f3 * this.getDeathMaxRotation(entityliving), 0.0F, 0.0F, 1.0F);
		}

	}

	protected float renderSwingProgress(EntityLiving entityliving, float f) {
		return entityliving.getSwingProgress(f);
	}

	protected float handleRotationFloat(EntityLiving entityliving, float f) {
		return (float)entityliving.ticksExisted + f;
	}

	protected boolean inheritRenderPass(EntityLiving entityliving, int i, float f) {
		return this.shouldRenderPass(entityliving, i, f);
	}

	protected boolean shouldRenderPass(EntityLiving entityliving, int i, float f) {
		return false;
	}

	protected float getDeathMaxRotation(EntityLiving entityliving) {
		return 90.0F;
	}

	protected int getColorMultiplier(EntityLiving entityliving, float f, float f1) {
		return 0;
	}

	protected void preRenderCallback(EntityLiving entityliving, float f) {
	}

	protected void passSpecialRender(EntityLiving entityliving, double d, double d1, double d2) {
		if(Minecraft.isDebugInfoEnabled()) {
			this.renderLivingLabel(entityliving, Integer.toString(entityliving.entityId)  + " " + Integer.toString(entityliving.health), d, d1, d2, 64);
		}

	}

	protected void renderLivingLabel(EntityLiving entityliving, String s, double d, double d1, double d2, int i) {
		float f = entityliving.getDistanceToEntity(this.renderManager.livingPlayer);
		if(f <= (float)i) {
			FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
			float f1 = 1.6F;
			float f2 = 0.01666667F * f1;
			GL11.glPushMatrix();
			GL11.glTranslatef((float)d + 0.0F, (float)d1 + 2.3F, (float)d2);
			GL11.glNormal3f(0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
			GL11.glScalef(-f2, -f2, f2);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDepthMask(false);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			Tessellator tessellator = Tessellator.instance;
			byte byte0 = 0;
			if(s.equals("deadmau5")) {
				byte0 = -10;
			}

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			tessellator.startDrawingQuads();
			int j = fontrenderer.getStringWidth(s) / 2;
			tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
			tessellator.addVertex((double)(-j - 1), (double)(-1 + byte0), 0.0D);
			tessellator.addVertex((double)(-j - 1), (double)(8 + byte0), 0.0D);
			tessellator.addVertex((double)(j + 1), (double)(8 + byte0), 0.0D);
			tessellator.addVertex((double)(j + 1), (double)(-1 + byte0), 0.0D);
			tessellator.draw();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, byte0, 553648127);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(true);
			fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, byte0, -1);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}
	}

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		this.doRenderLiving((EntityLiving)entity, d, d1, d2, f, f1);
	}
}
