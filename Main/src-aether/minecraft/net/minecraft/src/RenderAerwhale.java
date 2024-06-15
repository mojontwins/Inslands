package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderAerwhale extends Render {
	private ModelBase model = new ModelAerwhale();

	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		this.loadTexture("/aether/mobs/Mob_Aerwhale.png");
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glRotatef(90.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(180.0F - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(5.0F, 5.0F, 5.0F);
		this.model.render(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}
}
