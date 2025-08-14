package net.minecraft.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.monster.EntityAlphaWitch;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.tile.Block;

public class RenderWitch extends RenderLiving {
	private ModelWitch currentWitchModel;

	public RenderWitch() {
		super(new ModelWitch(0.0F), 0.5F);
		this.currentWitchModel = (ModelWitch)this.mainModel;
	}

	public void renderWitch(EntityAlphaWitch par1EntityWitch, double par2, double par4, double par6, float par8, float par9) {
		ItemStack var10 = par1EntityWitch.getHeldItem();

		this.currentWitchModel.isHoldingItem = var10 != null;
		super.doRenderLiving(par1EntityWitch, par2, par4, par6, par8, par9);
	}

	protected void renderWitchEquippedItems(EntityAlphaWitch par1EntityWitch, float par2) {
		float var3 = 1.0F;
		GL11.glColor3f(var3, var3, var3);
		super.renderEquippedItems(par1EntityWitch, par2);
		ItemStack var4 = par1EntityWitch.getHeldItem();

		if (var4 != null) {
			GL11.glPushMatrix();
			float var5;

			this.currentWitchModel.villagerNose.postRender(0.0625F);
			GL11.glTranslatef(-0.0625F, 0.53125F, 0.21875F);
			
			if (var4.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[var4.itemID].getRenderType())) {
				var5 = 0.5F;
				GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
				var5 *= 0.75F;
				GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(var5, -var5, var5);
			} else if (var4.itemID == Item.bow.shiftedIndex) {
				var5 = 0.625F;
				GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
				GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(var5, -var5, var5);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else if (Item.itemsList[var4.itemID].isFull3D()) {
				var5 = 0.625F;

				if (Item.itemsList[var4.itemID].shouldRotateAroundWhenRendering()) {
					GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(0.0F, -0.125F, 0.0F);
				}

				this.func_82410_b();
				GL11.glScalef(var5, -var5, var5);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else {
				int i7 = Item.itemsList[var4.itemID].getColorFromDamage(0);
				float r = (float)(i7 >> 16 & 255) / 255.0F;
				float g = (float)(i7 >> 8 & 255) / 255.0F;
				float b = (float)(i7 & 255) / 255.0F;
				float brightness = par1EntityWitch.getEntityBrightness(par2);
				GL11.glColor4f(r * brightness, g * brightness, b * brightness, 1.0F);
				
				var5 = 0.375F;
				GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
				GL11.glScalef(var5, var5, var5);
				GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			GL11.glRotatef(-15.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(40.0F, 0.0F, 0.0F, 1.0F);
			this.renderManager.itemRenderer.renderItem(par1EntityWitch, var4);

			GL11.glPopMatrix();
		}
	}

	protected void func_82410_b() {
		GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
	}

	protected void witchModelScale(EntityAlphaWitch par1EntityWitch, float par2) {
		float var3 = 0.9375F;
		GL11.glScalef(var3, var3, var3);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
	 * entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
		this.witchModelScale((EntityAlphaWitch)par1EntityLiving, par2);
	}

	protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
		this.renderWitchEquippedItems((EntityAlphaWitch)par1EntityLiving, par2);
	}

	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		this.renderWitch((EntityAlphaWitch)par1EntityLiving, par2, par4, par6, par8, par9);
	}

	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.renderWitch((EntityAlphaWitch)par1Entity, par2, par4, par6, par8, par9);
	}
}
