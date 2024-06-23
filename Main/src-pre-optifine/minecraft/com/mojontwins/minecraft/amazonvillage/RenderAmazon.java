package com.mojontwins.minecraft.amazonvillage;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Item;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderLiving;

public class RenderAmazon extends RenderLiving {
	private ModelAmazon modelBipedMain = (ModelAmazon)this.mainModel;
	private ModelBiped modelArmorChestplate = new ModelBiped(1.0F);
	private ModelBiped modelArmor = new ModelBiped(0.5F);
	private static final String[] armorFilenamePrefix = new String[]{"cloth", "chain", "iron", "diamond", "gold", "pirate", "rags"};
	
	public RenderAmazon() {
		super(new ModelAmazon(0.0F), 0.5F);
	}
	protected boolean setArmorModel(EntityAmazon entityAmazon1, int i2, float f3) {
		ItemStack itemStack4 = entityAmazon1.inventory.armorItemInSlot(3 - i2);
		if(itemStack4 != null) {
			Item item5 = itemStack4.getItem();
			if(item5 instanceof ItemArmor) {
				ItemArmor itemArmor6 = (ItemArmor)item5;
				this.loadTexture("/armor/" + armorFilenamePrefix[itemArmor6.renderIndex] + "_" + (i2 == 2 ? 2 : 1) + ".png");
				ModelBiped modelBiped7 = i2 == 2 ? this.modelArmor : this.modelArmorChestplate;
				modelBiped7.bipedHead.showModel = i2 == 0;
				modelBiped7.bipedHeadwear.showModel = i2 == 0;
				modelBiped7.bipedBody.showModel = i2 == 1 || i2 == 2;
				modelBiped7.bipedRightArm.showModel = i2 == 1;
				modelBiped7.bipedLeftArm.showModel = i2 == 1;
				modelBiped7.bipedRightLeg.showModel = i2 == 2 || i2 == 3;
				modelBiped7.bipedLeftLeg.showModel = i2 == 2 || i2 == 3;
				this.setRenderPassModel(modelBiped7);
				return true;
			}
		}

		return false;
	}

	public void renderAmazon(EntityAmazon entityAmazon1, double d2, double d4, double d6, float f8, float f9) {
		ItemStack itemStack10 = entityAmazon1.inventory.getCurrentItem();
		
		this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = itemStack10 != null;
		this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = entityAmazon1.isSneaking();
		this.modelArmorChestplate.isSitting = this.modelArmor.isSitting = this.modelBipedMain.isSitting = entityAmazon1.getIsSitting();
		
		double d11 = d4 - (double)entityAmazon1.yOffset;
		if(entityAmazon1.isSneaking()) {
			d11 -= 0.125D;
		} else if(entityAmazon1.getIsSitting()) {
			d11 -= 0.5D;
		}

		super.doRenderLiving(entityAmazon1, d2, d11, d6, f8, f9);
		this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = false;
		this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = false;
		this.modelArmorChestplate.isSitting = this.modelArmor.isSitting = this.modelBipedMain.isSitting = false;;
		
	}

	protected void renderSpecials(EntityAmazon entityAmazon1, float f2) {
		ItemStack itemStack3 = entityAmazon1.inventory.armorItemInSlot(3);
		if(itemStack3 != null && itemStack3.getItem().shiftedIndex < 256) {
			GL11.glPushMatrix();
			this.modelBipedMain.bipedHead.postRender(0.0625F);
			if(RenderBlocks.renderItemIn3d(Block.blocksList[itemStack3.itemID].getRenderType())) {
				float f4 = 0.625F;
				GL11.glTranslatef(0.0F, -0.25F, 0.0F);
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(f4, -f4, f4);
			}

			this.renderManager.itemRenderer.renderItem(entityAmazon1, itemStack3);
			GL11.glPopMatrix();
		}

		float f5;

		ItemStack itemStack21 = entityAmazon1.inventory.getCurrentItem();
		if(itemStack21 != null) {
			GL11.glPushMatrix();
			this.modelBipedMain.bipedRightArm.postRender(0.0625F);
			GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

			if(itemStack21.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemStack21.itemID].getRenderType())) {
				f5 = 0.5F;
				GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
				f5 *= 0.75F;
				GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(f5, -f5, f5);
			} else if(Item.itemsList[itemStack21.itemID].isFull3D()) {
				f5 = 0.625F;
				if(Item.itemsList[itemStack21.itemID].shouldRotateAroundWhenRendering()) {
					GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(0.0F, -0.125F, 0.0F);
				}

				GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
				GL11.glScalef(f5, -f5, f5);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else {
				f5 = 0.375F;
				GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
				GL11.glScalef(f5, f5, f5);
				GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			this.renderManager.itemRenderer.renderItem(entityAmazon1, itemStack21);
			GL11.glPopMatrix();
		}

	}

	protected void renderPlayerScale(EntityAmazon entityAmazon1, float f2) {
		float f3 = 0.9375F;
		GL11.glScalef(f3, f3, f3);
	}

	public void drawFirstPersonHand() {
		this.modelBipedMain.swingProgress = 0.0F;
		this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		this.modelBipedMain.bipedRightArm.render(0.0625F);
	}

	protected void renderPlayerSleep(EntityAmazon entityAmazon1, double d2, double d4, double d6) {
		super.renderLivingAt(entityAmazon1, d2, d4, d6);
	}

	protected void rotatePlayer(EntityAmazon entityAmazon1, float f2, float f3, float f4) {
		super.rotateCorpse(entityAmazon1, f2, f3, f4);
	}

	protected void preRenderCallback(EntityLiving entityLiving1, float f2) {
		this.renderPlayerScale((EntityAmazon)entityLiving1, f2);
	}

	protected boolean shouldRenderPass(EntityLiving entityLiving1, int i2, float f3) {
		return this.setArmorModel((EntityAmazon)entityLiving1, i2, f3);
	}

	protected void renderEquippedItems(EntityLiving entityLiving1, float f2) {
		this.renderSpecials((EntityAmazon)entityLiving1, f2);
	}

	protected void rotateCorpse(EntityLiving entityLiving1, float f2, float f3, float f4) {
		this.rotatePlayer((EntityAmazon)entityLiving1, f2, f3, f4);
	}

	protected void renderLivingAt(EntityLiving entityLiving1, double d2, double d4, double d6) {
		this.renderPlayerSleep((EntityAmazon)entityLiving1, d2, d4, d6);
	}

	public void doRenderLiving(EntityLiving entityLiving1, double d2, double d4, double d6, float f8, float f9) {
		this.renderAmazon((EntityAmazon)entityLiving1, d2, d4, d6, f8, f9);
	}

	public void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		this.renderAmazon((EntityAmazon)entity1, d2, d4, d6, f8, f9);
	}
}
