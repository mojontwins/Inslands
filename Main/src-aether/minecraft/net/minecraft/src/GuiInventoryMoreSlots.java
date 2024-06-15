package net.minecraft.src;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiInventoryMoreSlots extends GuiContainer {
	private float xSize_lo;
	private float ySize_lo;

	public GuiInventoryMoreSlots(EntityPlayer entityplayer) {
		super(entityplayer.inventorySlots);
		this.field_948_f = true;
		entityplayer.addStat(AchievementList.openInventory, 1);
	}

	public void initGui() {
		this.controlList.clear();
	}

	protected void drawGuiContainerForegroundLayer() {
	}

	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
		this.xSize_lo = (float)i;
		this.ySize_lo = (float)j;
	}

	protected void drawGuiContainerBackgroundLayer(float f) {
		int i = this.mc.renderEngine.getTexture("/aether/gui/inventory.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(i);
		int j = (this.width - this.xSize) / 2;
		int k = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(j + 33), (float)(k + 75), 50.0F);
		float f1 = 30.0F;
		GL11.glScalef(-f1, f1, f1);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		float f2 = this.mc.thePlayer.renderYawOffset;
		float f3 = this.mc.thePlayer.rotationYaw;
		float f4 = this.mc.thePlayer.rotationPitch;
		float f5 = (float)(j + 33) - this.xSize_lo;
		float f6 = (float)(k + 75 - 50) - this.ySize_lo;
		GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-((float)Math.atan((double)(f6 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
		this.mc.thePlayer.renderYawOffset = (float)Math.atan((double)(f5 / 40.0F)) * 20.0F;
		this.mc.thePlayer.rotationYaw = (float)Math.atan((double)(f5 / 40.0F)) * 40.0F;
		this.mc.thePlayer.rotationPitch = -((float)Math.atan((double)(f6 / 40.0F))) * 20.0F;
		this.mc.thePlayer.entityBrightness = 1.0F;
		GL11.glTranslatef(0.0F, this.mc.thePlayer.yOffset, 0.0F);
		RenderManager.instance.playerViewY = 180.0F;
		RenderManager.instance.renderEntityWithPosYaw(this.mc.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
		this.mc.thePlayer.entityBrightness = 0.0F;
		this.mc.thePlayer.renderYawOffset = f2;
		this.mc.thePlayer.rotationYaw = f3;
		this.mc.thePlayer.rotationPitch = f4;
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	}

	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.id == 0) {
			this.mc.displayGuiScreen(new GuiAchievements(this.mc.statFileWriter));
		}

		if(guibutton.id == 1) {
			this.mc.displayGuiScreen(new GuiStats(this, this.mc.statFileWriter));
		}

	}
}
