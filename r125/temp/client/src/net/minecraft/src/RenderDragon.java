package net.minecraft.src;

import java.util.Random;

import org.lwjgl.opengl.GL11;

public class RenderDragon extends RenderLiving {
	public static EntityDragon entityDragon;
	private static int field_40284_d = 0;
	protected ModelDragon modelDragon = (ModelDragon)this.mainModel;

	public RenderDragon() {
		super(new ModelDragon(0.0F), 0.5F);
		this.setRenderPassModel(this.mainModel);
	}

	protected void rotateDragonBody(EntityDragon entityDragon1, float f2, float f3, float f4) {
		float f5 = (float)entityDragon1.func_40160_a(7, f4)[0];
		float f6 = (float)(entityDragon1.func_40160_a(5, f4)[1] - entityDragon1.func_40160_a(10, f4)[1]);
		GL11.glRotatef(-f5, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(f6 * 10.0F, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(0.0F, 0.0F, 1.0F);
		if(entityDragon1.deathTime > 0) {
			float f7 = ((float)entityDragon1.deathTime + f4 - 1.0F) / 20.0F * 1.6F;
			f7 = MathHelper.sqrt_float(f7);
			if(f7 > 1.0F) {
				f7 = 1.0F;
			}

			GL11.glRotatef(f7 * this.getDeathMaxRotation(entityDragon1), 0.0F, 0.0F, 1.0F);
		}

	}

	protected void func_40280_a(EntityDragon entityDragon1, float f2, float f3, float f4, float f5, float f6, float f7) {
		if(entityDragon1.field_40178_aA > 0) {
			float f8 = (float)entityDragon1.field_40178_aA / 200.0F;
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glAlphaFunc(GL11.GL_GREATER, f8);
			this.loadDownloadableImageTexture(entityDragon1.skinUrl, "/mob/enderdragon/shuffle.png");
			this.mainModel.render(entityDragon1, f2, f3, f4, f5, f6, f7);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			GL11.glDepthFunc(GL11.GL_EQUAL);
		}

		this.loadDownloadableImageTexture(entityDragon1.skinUrl, entityDragon1.getTexture());
		this.mainModel.render(entityDragon1, f2, f3, f4, f5, f6, f7);
		if(entityDragon1.hurtTime > 0) {
			GL11.glDepthFunc(GL11.GL_EQUAL);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.5F);
			this.mainModel.render(entityDragon1, f2, f3, f4, f5, f6, f7);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
		}

	}

	public void renderDragon(EntityDragon entityDragon1, double d2, double d4, double d6, float f8, float f9) {
		entityDragon = entityDragon1;
		if(field_40284_d != 4) {
			this.mainModel = new ModelDragon(0.0F);
			field_40284_d = 4;
		}

		super.doRenderLiving(entityDragon1, d2, d4, d6, f8, f9);
		if(entityDragon1.healingEnderCrystal != null) {
			float f10 = (float)entityDragon1.healingEnderCrystal.innerRotation + f9;
			float f11 = MathHelper.sin(f10 * 0.2F) / 2.0F + 0.5F;
			f11 = (f11 * f11 + f11) * 0.2F;
			float f12 = (float)(entityDragon1.healingEnderCrystal.posX - entityDragon1.posX - (entityDragon1.prevPosX - entityDragon1.posX) * (double)(1.0F - f9));
			float f13 = (float)((double)f11 + entityDragon1.healingEnderCrystal.posY - 1.0D - entityDragon1.posY - (entityDragon1.prevPosY - entityDragon1.posY) * (double)(1.0F - f9));
			float f14 = (float)(entityDragon1.healingEnderCrystal.posZ - entityDragon1.posZ - (entityDragon1.prevPosZ - entityDragon1.posZ) * (double)(1.0F - f9));
			float f15 = MathHelper.sqrt_float(f12 * f12 + f14 * f14);
			float f16 = MathHelper.sqrt_float(f12 * f12 + f13 * f13 + f14 * f14);
			GL11.glPushMatrix();
			GL11.glTranslatef((float)d2, (float)d4 + 2.0F, (float)d6);
			GL11.glRotatef((float)(-Math.atan2((double)f14, (double)f12)) * 180.0F / (float)Math.PI - 90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef((float)(-Math.atan2((double)f15, (double)f13)) * 180.0F / (float)Math.PI - 90.0F, 1.0F, 0.0F, 0.0F);
			Tessellator tessellator17 = Tessellator.instance;
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_CULL_FACE);
			this.loadTexture("/mob/enderdragon/beam.png");
			GL11.glShadeModel(GL11.GL_SMOOTH);
			float f18 = 0.0F - ((float)entityDragon1.ticksExisted + f9) * 0.01F;
			float f19 = MathHelper.sqrt_float(f12 * f12 + f13 * f13 + f14 * f14) / 32.0F - ((float)entityDragon1.ticksExisted + f9) * 0.01F;
			tessellator17.startDrawing(5);
			byte b20 = 8;

			for(int i21 = 0; i21 <= b20; ++i21) {
				float f22 = MathHelper.sin((float)(i21 % b20) * (float)Math.PI * 2.0F / (float)b20) * 0.75F;
				float f23 = MathHelper.cos((float)(i21 % b20) * (float)Math.PI * 2.0F / (float)b20) * 0.75F;
				float f24 = (float)(i21 % b20) * 1.0F / (float)b20;
				tessellator17.setColorOpaque_I(0);
				tessellator17.addVertexWithUV((double)(f22 * 0.2F), (double)(f23 * 0.2F), 0.0D, (double)f24, (double)f19);
				tessellator17.setColorOpaque_I(0xFFFFFF);
				tessellator17.addVertexWithUV((double)f22, (double)f23, (double)f16, (double)f24, (double)f18);
			}

			tessellator17.draw();
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glShadeModel(GL11.GL_FLAT);
			RenderHelper.enableStandardItemLighting();
			GL11.glPopMatrix();
		}

	}

	protected void renderDragonDying(EntityDragon entityDragon1, float f2) {
		super.renderEquippedItems(entityDragon1, f2);
		Tessellator tessellator3 = Tessellator.instance;
		if(entityDragon1.field_40178_aA > 0) {
			RenderHelper.disableStandardItemLighting();
			float f4 = ((float)entityDragon1.field_40178_aA + f2) / 200.0F;
			float f5 = 0.0F;
			if(f4 > 0.8F) {
				f5 = (f4 - 0.8F) / 0.2F;
			}

			Random random6 = new Random(432L);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDepthMask(false);
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, -1.0F, -2.0F);

			for(int i7 = 0; (float)i7 < (f4 + f4 * f4) / 2.0F * 60.0F; ++i7) {
				GL11.glRotatef(random6.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(random6.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(random6.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(random6.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(random6.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(random6.nextFloat() * 360.0F + f4 * 90.0F, 0.0F, 0.0F, 1.0F);
				tessellator3.startDrawing(6);
				float f8 = random6.nextFloat() * 20.0F + 5.0F + f5 * 10.0F;
				float f9 = random6.nextFloat() * 2.0F + 1.0F + f5 * 2.0F;
				tessellator3.setColorRGBA_I(0xFFFFFF, (int)(255.0F * (1.0F - f5)));
				tessellator3.addVertex(0.0D, 0.0D, 0.0D);
				tessellator3.setColorRGBA_I(16711935, 0);
				tessellator3.addVertex(-0.866D * (double)f9, (double)f8, (double)(-0.5F * f9));
				tessellator3.addVertex(0.866D * (double)f9, (double)f8, (double)(-0.5F * f9));
				tessellator3.addVertex(0.0D, (double)f8, (double)(1.0F * f9));
				tessellator3.addVertex(-0.866D * (double)f9, (double)f8, (double)(-0.5F * f9));
				tessellator3.draw();
			}

			GL11.glPopMatrix();
			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glShadeModel(GL11.GL_FLAT);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			RenderHelper.enableStandardItemLighting();
		}

	}

	protected int func_40283_a(EntityDragon entityDragon1, int i2, float f3) {
		if(i2 == 1) {
			GL11.glDepthFunc(GL11.GL_LEQUAL);
		}

		if(i2 != 0) {
			return -1;
		} else {
			this.loadTexture("/mob/enderdragon/ender_eyes.png");
			float f4 = 1.0F;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDepthFunc(GL11.GL_EQUAL);
			int i5 = 61680;
			int i6 = i5 % 65536;
			int i7 = i5 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)i6 / 1.0F, (float)i7 / 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, f4);
			return 1;
		}
	}

	protected int shouldRenderPass(EntityLiving entityLiving1, int i2, float f3) {
		return this.func_40283_a((EntityDragon)entityLiving1, i2, f3);
	}

	protected void renderEquippedItems(EntityLiving entityLiving1, float f2) {
		this.renderDragonDying((EntityDragon)entityLiving1, f2);
	}

	protected void rotateCorpse(EntityLiving entityLiving1, float f2, float f3, float f4) {
		this.rotateDragonBody((EntityDragon)entityLiving1, f2, f3, f4);
	}

	protected void renderModel(EntityLiving entityLiving1, float f2, float f3, float f4, float f5, float f6, float f7) {
		this.func_40280_a((EntityDragon)entityLiving1, f2, f3, f4, f5, f6, f7);
	}

	public void doRenderLiving(EntityLiving entityLiving1, double d2, double d4, double d6, float f8, float f9) {
		this.renderDragon((EntityDragon)entityLiving1, d2, d4, d6, f8, f9);
	}

	public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.renderDragon((EntityDragon)entity1, d2, d4, d6, f8, f9);
	}
}
