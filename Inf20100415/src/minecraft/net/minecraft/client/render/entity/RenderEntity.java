package net.minecraft.client.render.entity;

import net.minecraft.client.render.Tessellator;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

public final class RenderEntity extends Render {
	public final void doRender(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(d2 - entity1.lastTickPosX), (float)(d4 - entity1.lastTickPosY), (float)(d6 - entity1.lastTickPosZ));
		AxisAlignedBB axisAlignedBB10 = entity1.boundingBox;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Tessellator tessellator11 = Tessellator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tessellator11.startDrawingQuads();
		Tessellator.setNormal(0.0F, 0.0F, -1.0F);
		tessellator11.addVertex(axisAlignedBB10.minX, axisAlignedBB10.maxY, axisAlignedBB10.minZ);
		tessellator11.addVertex(axisAlignedBB10.maxX, axisAlignedBB10.maxY, axisAlignedBB10.minZ);
		tessellator11.addVertex(axisAlignedBB10.maxX, axisAlignedBB10.minY, axisAlignedBB10.minZ);
		tessellator11.addVertex(axisAlignedBB10.minX, axisAlignedBB10.minY, axisAlignedBB10.minZ);
		Tessellator.setNormal(0.0F, 0.0F, 1.0F);
		tessellator11.addVertex(axisAlignedBB10.minX, axisAlignedBB10.minY, axisAlignedBB10.maxZ);
		tessellator11.addVertex(axisAlignedBB10.maxX, axisAlignedBB10.minY, axisAlignedBB10.maxZ);
		tessellator11.addVertex(axisAlignedBB10.maxX, axisAlignedBB10.maxY, axisAlignedBB10.maxZ);
		tessellator11.addVertex(axisAlignedBB10.minX, axisAlignedBB10.maxY, axisAlignedBB10.maxZ);
		Tessellator.setNormal(0.0F, -1.0F, 0.0F);
		tessellator11.addVertex(axisAlignedBB10.minX, axisAlignedBB10.minY, axisAlignedBB10.minZ);
		tessellator11.addVertex(axisAlignedBB10.maxX, axisAlignedBB10.minY, axisAlignedBB10.minZ);
		tessellator11.addVertex(axisAlignedBB10.maxX, axisAlignedBB10.minY, axisAlignedBB10.maxZ);
		tessellator11.addVertex(axisAlignedBB10.minX, axisAlignedBB10.minY, axisAlignedBB10.maxZ);
		Tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator11.addVertex(axisAlignedBB10.minX, axisAlignedBB10.maxY, axisAlignedBB10.maxZ);
		tessellator11.addVertex(axisAlignedBB10.maxX, axisAlignedBB10.maxY, axisAlignedBB10.maxZ);
		tessellator11.addVertex(axisAlignedBB10.maxX, axisAlignedBB10.maxY, axisAlignedBB10.minZ);
		tessellator11.addVertex(axisAlignedBB10.minX, axisAlignedBB10.maxY, axisAlignedBB10.minZ);
		Tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		tessellator11.addVertex(axisAlignedBB10.minX, axisAlignedBB10.minY, axisAlignedBB10.maxZ);
		tessellator11.addVertex(axisAlignedBB10.minX, axisAlignedBB10.maxY, axisAlignedBB10.maxZ);
		tessellator11.addVertex(axisAlignedBB10.minX, axisAlignedBB10.maxY, axisAlignedBB10.minZ);
		tessellator11.addVertex(axisAlignedBB10.minX, axisAlignedBB10.minY, axisAlignedBB10.minZ);
		Tessellator.setNormal(1.0F, 0.0F, 0.0F);
		tessellator11.addVertex(axisAlignedBB10.maxX, axisAlignedBB10.minY, axisAlignedBB10.minZ);
		tessellator11.addVertex(axisAlignedBB10.maxX, axisAlignedBB10.maxY, axisAlignedBB10.minZ);
		tessellator11.addVertex(axisAlignedBB10.maxX, axisAlignedBB10.maxY, axisAlignedBB10.maxZ);
		tessellator11.addVertex(axisAlignedBB10.maxX, axisAlignedBB10.minY, axisAlignedBB10.maxZ);
		tessellator11.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}
}