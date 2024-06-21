package net.minecraft.src;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.opengl.GL12;

public class GuiEnchantment extends GuiContainer {
	private static ModelBook bookModel = new ModelBook();
	private Random field_40230_x = new Random();
	private ContainerEnchantment containerEnchantment = (ContainerEnchantment)this.inventorySlots;
	public int field_40227_h;
	public float field_40229_i;
	public float field_40225_j;
	public float field_40226_k;
	public float field_40223_l;
	public float field_40224_m;
	public float field_40221_n;
	ItemStack field_40222_o;

	public GuiEnchantment(InventoryPlayer inventoryPlayer1, World world2, int i3, int i4, int i5) {
		super(new ContainerEnchantment(inventoryPlayer1, world2, i3, i4, i5));
	}

	public void onGuiClosed() {
		super.onGuiClosed();
	}

	protected void drawGuiContainerForegroundLayer() {
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.enchant"), 12, 6, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	public void updateScreen() {
		super.updateScreen();
		this.func_40219_x_();
	}

	protected void mouseClicked(int i1, int i2, int i3) {
		super.mouseClicked(i1, i2, i3);
		int i4 = (this.width - this.xSize) / 2;
		int i5 = (this.height - this.ySize) / 2;

		for(int i6 = 0; i6 < 3; ++i6) {
			int i7 = i1 - (i4 + 60);
			int i8 = i2 - (i5 + 14 + 19 * i6);
			if(i7 >= 0 && i8 >= 0 && i7 < 108 && i8 < 19 && this.containerEnchantment.enchantItem(this.mc.thePlayer, i6)) {
				this.mc.playerController.func_40593_a(this.containerEnchantment.windowId, i6);
			}
		}

	}

	protected void drawGuiContainerBackgroundLayer(float f1, int i2, int i3) {
		int i4 = this.mc.renderEngine.getTexture("/gui/enchant.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(i4);
		int i5 = (this.width - this.xSize) / 2;
		int i6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i5, i6, 0, 0, this.xSize, this.ySize);
		GL11.glPushMatrix();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		ScaledResolution scaledResolution7 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		GL11.glViewport((scaledResolution7.getScaledWidth() - 320) / 2 * scaledResolution7.scaleFactor, (scaledResolution7.getScaledHeight() - 240) / 2 * scaledResolution7.scaleFactor, 320 * scaledResolution7.scaleFactor, 240 * scaledResolution7.scaleFactor);
		GL11.glTranslatef(-0.34F, 0.23F, 0.0F);
		GLU.gluPerspective(90.0F, 1.3333334F, 9.0F, 80.0F);
		float f8 = 1.0F;
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		RenderHelper.enableStandardItemLighting();
		GL11.glTranslatef(0.0F, 3.3F, -16.0F);
		GL11.glScalef(f8, f8, f8);
		float f9 = 5.0F;
		GL11.glScalef(f9, f9, f9);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/item/book.png"));
		GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
		float f10 = this.field_40221_n + (this.field_40224_m - this.field_40221_n) * f1;
		GL11.glTranslatef((1.0F - f10) * 0.2F, (1.0F - f10) * 0.1F, (1.0F - f10) * 0.25F);
		GL11.glRotatef(-(1.0F - f10) * 90.0F - 90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		float f11 = this.field_40225_j + (this.field_40229_i - this.field_40225_j) * f1 + 0.25F;
		float f12 = this.field_40225_j + (this.field_40229_i - this.field_40225_j) * f1 + 0.75F;
		f11 = (f11 - (float)MathHelper.func_40346_b((double)f11)) * 1.6F - 0.3F;
		f12 = (f12 - (float)MathHelper.func_40346_b((double)f12)) * 1.6F - 0.3F;
		if(f11 < 0.0F) {
			f11 = 0.0F;
		}

		if(f12 < 0.0F) {
			f12 = 0.0F;
		}

		if(f11 > 1.0F) {
			f11 = 1.0F;
		}

		if(f12 > 1.0F) {
			f12 = 1.0F;
		}

		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		bookModel.render((Entity)null, 0.0F, f11, f12, f10, 0.0F, 0.0625F);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(i4);
		EnchantmentNameParts.instance.setRandSeed(this.containerEnchantment.nameSeed);

		for(int i13 = 0; i13 < 3; ++i13) {
			String string14 = EnchantmentNameParts.instance.generateRandomEnchantName();
			this.zLevel = 0.0F;
			this.mc.renderEngine.bindTexture(i4);
			int i15 = this.containerEnchantment.enchantLevels[i13];
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			if(i15 == 0) {
				this.drawTexturedModalRect(i5 + 60, i6 + 14 + 19 * i13, 0, 185, 108, 19);
			} else {
				String string16 = "" + i15;
				FontRenderer fontRenderer17 = this.mc.standardGalacticFontRenderer;
				int i18 = 6839882;
				if(this.mc.thePlayer.experienceLevel < i15 && !this.mc.thePlayer.capabilities.isCreativeMode) {
					this.drawTexturedModalRect(i5 + 60, i6 + 14 + 19 * i13, 0, 185, 108, 19);
					fontRenderer17.drawSplitString(string14, i5 + 62, i6 + 16 + 19 * i13, 104, (i18 & 16711422) >> 1);
					fontRenderer17 = this.mc.fontRenderer;
					i18 = 4226832;
					fontRenderer17.drawStringWithShadow(string16, i5 + 62 + 104 - fontRenderer17.getStringWidth(string16), i6 + 16 + 19 * i13 + 7, i18);
				} else {
					int i19 = i2 - (i5 + 60);
					int i20 = i3 - (i6 + 14 + 19 * i13);
					if(i19 >= 0 && i20 >= 0 && i19 < 108 && i20 < 19) {
						this.drawTexturedModalRect(i5 + 60, i6 + 14 + 19 * i13, 0, 204, 108, 19);
						i18 = 16777088;
					} else {
						this.drawTexturedModalRect(i5 + 60, i6 + 14 + 19 * i13, 0, 166, 108, 19);
					}

					fontRenderer17.drawSplitString(string14, i5 + 62, i6 + 16 + 19 * i13, 104, i18);
					fontRenderer17 = this.mc.fontRenderer;
					i18 = 8453920;
					fontRenderer17.drawStringWithShadow(string16, i5 + 62 + 104 - fontRenderer17.getStringWidth(string16), i6 + 16 + 19 * i13 + 7, i18);
				}
			}
		}

	}

	public void func_40219_x_() {
		ItemStack itemStack1 = this.inventorySlots.getSlot(0).getStack();
		if(!ItemStack.areItemStacksEqual(itemStack1, this.field_40222_o)) {
			this.field_40222_o = itemStack1;

			do {
				this.field_40226_k += (float)(this.field_40230_x.nextInt(4) - this.field_40230_x.nextInt(4));
			} while(this.field_40229_i <= this.field_40226_k + 1.0F && this.field_40229_i >= this.field_40226_k - 1.0F);
		}

		++this.field_40227_h;
		this.field_40225_j = this.field_40229_i;
		this.field_40221_n = this.field_40224_m;
		boolean z2 = false;

		for(int i3 = 0; i3 < 3; ++i3) {
			if(this.containerEnchantment.enchantLevels[i3] != 0) {
				z2 = true;
			}
		}

		if(z2) {
			this.field_40224_m += 0.2F;
		} else {
			this.field_40224_m -= 0.2F;
		}

		if(this.field_40224_m < 0.0F) {
			this.field_40224_m = 0.0F;
		}

		if(this.field_40224_m > 1.0F) {
			this.field_40224_m = 1.0F;
		}

		float f5 = (this.field_40226_k - this.field_40229_i) * 0.4F;
		float f4 = 0.2F;
		if(f5 < -f4) {
			f5 = -f4;
		}

		if(f5 > f4) {
			f5 = f4;
		}

		this.field_40223_l += (f5 - this.field_40223_l) * 0.9F;
		this.field_40229_i += this.field_40223_l;
	}
}
