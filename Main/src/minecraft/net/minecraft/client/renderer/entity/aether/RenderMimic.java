package net.minecraft.client.renderer.entity.aether;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.aether.ModelMimic;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.EntityMimic;

public class RenderMimic extends Render {
	private ModelMimic model = new ModelMimic();

	public void render(EntityMimic entityMimic, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glRotatef(180.0F - f, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		this.loadTexture("/mob/Mimic1.png");
		this.model.render1(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, entityMimic);
		this.loadTexture("/mob/Mimic2.png");
		this.model.render2(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, entityMimic);
		GL11.glPopMatrix();
	}

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		this.render((EntityMimic)entity, d, d1, d2, f, f1);
	}
}
