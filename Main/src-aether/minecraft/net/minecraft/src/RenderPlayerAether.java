package net.minecraft.src;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderPlayerAether extends RenderPlayer {
	private ModelBiped modelEnergyShield = new ModelBiped(1.25F);
	private ModelBiped modelCape = new ModelBiped(0.0F);
	private ModelBiped modelMisc = new ModelBiped(0.6F);

	protected boolean setEnergyShieldBrightness(EntityPlayer player, int i, float f) {
		if(i != 0) {
			return false;
		} else {
			InventoryAether inv = mod_Aether.getPlayer().inv;
			boolean flag = inv != null && inv.slots[2] != null && inv.slots[2].itemID == AetherItems.RepShield.shiftedIndex;
			if(!flag) {
				return false;
			} else {
				if((player.onGround || player.ridingEntity != null && player.ridingEntity.onGround) && player.moveForward == 0.0F && player.moveStrafing == 0.0F) {
					this.loadTexture("/aether/mobs/energyGlow.png");
					GL11.glEnable(GL11.GL_NORMALIZE);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				} else {
					GL11.glEnable(GL11.GL_NORMALIZE);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					this.loadTexture("/aether/mobs/energyNotGlow.png");
				}

				return true;
			}
		}
	}

	public void renderEnergyShield(EntityPlayer entityplayer, double d, double d1, double d2, float f, float f1) {
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		this.modelEnergyShield.field_1278_i = itemstack != null;
		this.modelEnergyShield.isSneak = entityplayer.isSneaking();
		double d3 = d1 - (double)entityplayer.yOffset;
		if(entityplayer.isSneaking() && !(entityplayer instanceof EntityPlayerSP)) {
			d3 -= 0.125D;
		}

		this.doRenderEnergyShield(entityplayer, d, d3, d2, f, f1);
		this.modelEnergyShield.isSneak = false;
		this.modelEnergyShield.field_1278_i = false;
	}

	public void renderMisc(EntityPlayer entityplayer, double d, double d1, double d2, float f, float f1) {
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		this.modelMisc.field_1278_i = itemstack != null;
		this.modelMisc.isSneak = entityplayer.isSneaking();
		double d3 = d1 - (double)entityplayer.yOffset;
		if(entityplayer.isSneaking() && !(entityplayer instanceof EntityPlayerSP)) {
			d3 -= 0.125D;
		}

		this.doRenderMisc(entityplayer, d, d3, d2, f, f1);
		this.modelMisc.isSneak = false;
		this.modelMisc.field_1278_i = false;
	}

	public void doRenderMisc(EntityPlayer player, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_CULL_FACE);
		this.modelMisc.onGround = this.func_167_c(player, f1);
		this.modelMisc.isRiding = player.isRiding();

		try {
			float exception = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * f1;
			float f3 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f1;
			float f4 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f1;
			this.func_22012_b(player, d, d1, d2);
			float f5 = this.func_170_d(player, f1);
			this.rotateCorpse(player, f5, exception, f1);
			float f6 = 0.0625F;
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glScalef(-1.0F, -1.0F, 1.0F);
			this.preRenderCallback(player, f1);
			GL11.glTranslatef(0.0F, -24.0F * f6 - 0.0078125F, 0.0F);
			float f7 = player.field_705_Q + (player.field_704_R - player.field_705_Q) * f1;
			float f8 = player.field_703_S - player.field_704_R * (1.0F - f1);
			if(f7 > 1.0F) {
				f7 = 1.0F;
			}

			GL11.glEnable(GL11.GL_ALPHA_TEST);
			this.modelMisc.setRotationAngles(f8, f7, f5, f3 - exception, f4, f6);
			float brightness = player.getEntityBrightness(f);
			InventoryAether inv = mod_Aether.getPlayer(player).inv;
			ItemMoreArmor glove;
			int colour;
			float red;
			float green;
			float blue;
			if(inv.slots[0] != null) {
				glove = (ItemMoreArmor)((ItemMoreArmor)inv.slots[0].getItem());
				this.loadTexture(glove.texture);
				colour = glove.getColorFromDamage(0);
				red = (float)(colour >> 16 & 255) / 255.0F;
				green = (float)(colour >> 8 & 255) / 255.0F;
				blue = (float)(colour & 255) / 255.0F;
				if(glove.colouriseRender) {
					GL11.glColor3f(red * brightness, green * brightness, blue * brightness);
				} else {
					GL11.glColor3f(brightness, brightness, brightness);
				}

				this.modelMisc.bipedBody.render(f6);
			}

			if(inv.slots[6] != null) {
				glove = (ItemMoreArmor)((ItemMoreArmor)inv.slots[6].getItem());
				this.loadTexture(glove.texture);
				colour = glove.getColorFromDamage(0);
				red = (float)(colour >> 16 & 255) / 255.0F;
				green = (float)(colour >> 8 & 255) / 255.0F;
				blue = (float)(colour & 255) / 255.0F;
				if(glove.colouriseRender) {
					GL11.glColor3f(red * brightness, green * brightness, blue * brightness);
				} else {
					GL11.glColor3f(brightness, brightness, brightness);
				}

				this.modelMisc.bipedLeftArm.render(f6);
				this.modelMisc.bipedRightArm.render(f6);
			}

			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		} catch (Exception exception24) {
			exception24.printStackTrace();
		}

		GL11.glPopMatrix();
	}

	public void drawFirstPersonHand() {
		if(this.renderManager.renderEngine != null && !this.invisible(ModLoader.getMinecraftInstance().thePlayer)) {
			super.drawFirstPersonHand();
			EntityPlayerSP player = ModLoader.getMinecraftInstance().thePlayer;
			InventoryAether inv = mod_Aether.getPlayer(player).inv;
			if(inv.slots[6] != null) {
				float brightness = player.getEntityBrightness(1.0F);
				this.modelMisc.onGround = 0.0F;
				this.modelMisc.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
				this.modelMisc.bipedRightArm.render(0.0625F);
				ItemMoreArmor glove = (ItemMoreArmor)((ItemMoreArmor)inv.slots[6].getItem());
				this.loadTexture(glove.texture);
				int colour = glove.getColorFromDamage(0);
				float red = (float)(colour >> 16 & 255) / 255.0F;
				float green = (float)(colour >> 8 & 255) / 255.0F;
				float blue = (float)(colour & 255) / 255.0F;
				if(glove.colouriseRender) {
					GL11.glColor3f(red * brightness, green * brightness, blue * brightness);
				} else {
					GL11.glColor3f(brightness, brightness, brightness);
				}

				this.modelMisc.bipedRightArm.render(0.0625F);
			}
		}

	}

	protected void renderEquippedItems(EntityLiving entityliving, float f) {
		this.renderSpecials((EntityPlayer)entityliving, f);
		this.renderCape((EntityPlayer)entityliving, f);
	}

	public void renderCape(EntityPlayer entityplayer, float f) {
		InventoryAether inv = mod_Aether.getPlayer(entityplayer).inv;
		if(inv.slots[1] != null) {
			ItemStack cape = inv.slots[1];
			if(cape.itemID == AetherItems.RepShield.shiftedIndex) {
				return;
			}

			this.loadTexture(((ItemMoreArmor)((ItemMoreArmor)cape.getItem())).texture);
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, 0.0F, 0.125F);
			double d = entityplayer.field_20066_r + (entityplayer.field_20063_u - entityplayer.field_20066_r) * (double)f - (entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double)f);
			double d1 = entityplayer.field_20065_s + (entityplayer.field_20062_v - entityplayer.field_20065_s) * (double)f - (entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * (double)f);
			double d2 = entityplayer.field_20064_t + (entityplayer.field_20061_w - entityplayer.field_20064_t) * (double)f - (entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double)f);
			float f8 = entityplayer.prevRenderYawOffset + (entityplayer.renderYawOffset - entityplayer.prevRenderYawOffset) * f;
			double d3 = (double)MathHelper.sin(f8 * 3.141593F / 180.0F);
			double d4 = (double)(-MathHelper.cos(f8 * 3.141593F / 180.0F));
			float f9 = (float)d1 * 10.0F;
			if(f9 < -6.0F) {
				f9 = -6.0F;
			}

			if(f9 > 32.0F) {
				f9 = 32.0F;
			}

			float f10 = (float)(d * d3 + d2 * d4) * 100.0F;
			float f11 = (float)(d * d4 - d2 * d3) * 100.0F;
			if(f10 < 0.0F) {
				f10 = 0.0F;
			}

			float f12 = entityplayer.field_775_e + (entityplayer.field_774_f - entityplayer.field_775_e) * f;
			f9 += MathHelper.sin((entityplayer.prevDistanceWalkedModified + (entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified) * f) * 6.0F) * 32.0F * f12;
			if(entityplayer.isSneaking()) {
				f9 += 25.0F;
			}

			GL11.glRotatef(6.0F + f10 / 2.0F + f9, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(f11 / 2.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-f11 / 2.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			this.modelCape.renderCloak(0.0625F);
			GL11.glPopMatrix();
		}

	}

	public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, float f, float f1) {
		if(!this.invisible((EntityPlayer)entityliving)) {
			super.doRenderLiving(entityliving, d, d1, d2, f, f1);
			this.renderEnergyShield((EntityPlayer)entityliving, d, d1, d2, f, f1);
			this.renderMisc((EntityPlayer)entityliving, d, d1, d2, f, f1);
		}

	}

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		if(!this.invisible((EntityPlayer)entity)) {
			super.doRender(entity, d, d1, d2, f, f1);
			this.renderEnergyShield((EntityPlayer)entity, d, d1, d2, f, f1);
			this.renderMisc((EntityPlayer)entity, d, d1, d2, f, f1);
		}

	}

	public boolean invisible(EntityPlayer player) {
		InventoryAether inv = mod_Aether.getPlayer(player).inv;
		return !player.isSwinging && inv.slots[1] != null && inv.slots[1].itemID == AetherItems.InvisibilityCloak.shiftedIndex ? true : GuiMainMenu.mmactive;
	}

	public void doRenderEnergyShield(EntityLiving entityliving, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_CULL_FACE);
		this.modelEnergyShield.onGround = this.func_167_c(entityliving, f1);
		this.modelEnergyShield.isRiding = entityliving.isRiding();

		try {
			float exception = entityliving.prevRenderYawOffset + (entityliving.renderYawOffset - entityliving.prevRenderYawOffset) * f1;
			float f3 = entityliving.prevRotationYaw + (entityliving.rotationYaw - entityliving.prevRotationYaw) * f1;
			float f4 = entityliving.prevRotationPitch + (entityliving.rotationPitch - entityliving.prevRotationPitch) * f1;
			this.func_22012_b(entityliving, d, d1, d2);
			float f5 = this.func_170_d(entityliving, f1);
			this.rotateCorpse(entityliving, f5, exception, f1);
			float f6 = 0.0625F;
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glScalef(-1.0F, -1.0F, 1.0F);
			this.preRenderCallback(entityliving, f1);
			GL11.glTranslatef(0.0F, -24.0F * f6 - 0.0078125F, 0.0F);
			float f7 = entityliving.field_705_Q + (entityliving.field_704_R - entityliving.field_705_Q) * f1;
			float f8 = entityliving.field_703_S - entityliving.field_704_R * (1.0F - f1);
			if(f7 > 1.0F) {
				f7 = 1.0F;
			}

			GL11.glEnable(GL11.GL_ALPHA_TEST);
			if(this.setEnergyShieldBrightness((EntityPlayer)entityliving, 0, f1)) {
				this.modelEnergyShield.render(f8, f7, f5, f3 - exception, f4, f6);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		} catch (Exception exception17) {
			exception17.printStackTrace();
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}
}
