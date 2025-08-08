package net.minecraft.client.renderer.entity.mojontwins;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.mojontwins.minecraft.poisonisland.EntityThrowableBottle;
import com.mojontwins.minecraft.poisonisland.ItemBottle;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.src.Entity;

public class RenderThrowableBottle extends Render {

	@Override
	public void doRender(Entity entity, double xPos, double yPos, double zPos, float yaw, float pitch) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)xPos, (float)yPos, (float)zPos);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
	
		ItemBottle itemBottle = ((EntityThrowableBottle)entity).itemBottle;
		if(itemBottle == null) return;
		
		int i7 = itemBottle.getColorFromDamage(0);
		float r = (float)(i7 >> 16 & 255) / 255.0F;
		float g = (float)(i7 >> 8 & 255) / 255.0F;
		float b = (float)(i7 & 255) / 255.0F;
		float brightness = 1.0F; // entity.getEntityBrightness(pitch);
		GL11.glColor4f(r * brightness, g * brightness, b * brightness, 1.0F);
		
		this.loadTexture("/gui/items.png");
		Tessellator tessellator10 = Tessellator.instance;
		float f11 = (float)(itemBottle.iconIndex % 16 * 16 + 0) / 256.0F;
		float f12 = (float)(itemBottle.iconIndex % 16 * 16 + 16) / 256.0F;
		float f13 = (float)(itemBottle.iconIndex / 16 * 16 + 0) / 256.0F;
		float f14 = (float)(itemBottle.iconIndex / 16 * 16 + 16) / 256.0F;
		float f15 = 1.0F;
		float f16 = 0.5F;
		float f17 = 0.25F;
		GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		tessellator10.startDrawingQuads();
		tessellator10.setNormal(0.0F, 1.0F, 0.0F);
		tessellator10.addVertexWithUV((double)(0.0F - f16), (double)(0.0F - f17), 0.0D, (double)f11, (double)f14);
		tessellator10.addVertexWithUV((double)(f15 - f16), (double)(0.0F - f17), 0.0D, (double)f12, (double)f14);
		tessellator10.addVertexWithUV((double)(f15 - f16), (double)(1.0F - f17), 0.0D, (double)f12, (double)f13);
		tessellator10.addVertexWithUV((double)(0.0F - f16), (double)(1.0F - f17), 0.0D, (double)f11, (double)f13);
		tessellator10.draw();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

}
