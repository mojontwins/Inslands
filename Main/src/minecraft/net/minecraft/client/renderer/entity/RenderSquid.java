package net.minecraft.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.animal.water.EntitySquid;

public class RenderSquid extends RenderLiving {
	public RenderSquid(ModelBase modelBase, float shadowSize) {
		super(modelBase, shadowSize);
	}

	public void doRenderSquid(EntitySquid entitySquid, double x, double y, double z, float yaw, float partialTickTime) {
		super.doRenderLiving(entitySquid, x, y, z, yaw, partialTickTime);
	}

	protected void rotateSquidCorpse(EntitySquid entitySquid, float f2, float yaw, float partialTickTime) {
		float pitch = entitySquid.prevSquidPitch + (entitySquid.squidPitch - entitySquid.prevSquidPitch) * partialTickTime;
		float yawRot = entitySquid.prevSquidYaw + (entitySquid.squidYaw - entitySquid.prevSquidYaw) * partialTickTime;
		GL11.glTranslatef(0.0F, 0.5F, 0.0F);
		GL11.glRotatef(180.0F - yaw, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(yawRot, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(0.0F, -1.2F, 0.0F);
	}

	protected void preRenderSquid(EntitySquid entitySquid, float f2) {
	}

	protected float getSquidRotation(EntitySquid entitySquid, float partialTickTime) {
		float rotation = entitySquid.lastTentacleAngle + (entitySquid.tentacleAngle - entitySquid.lastTentacleAngle) * partialTickTime;
		return rotation;
	}

	protected void preRenderCallback(EntityLiving entityLiving, float f2) {
		this.preRenderSquid((EntitySquid) entityLiving, f2);
	}

	protected float handleRotationFloat(EntityLiving entityLiving, float f2) {
		return this.getSquidRotation((EntitySquid) entityLiving, f2);
	}

	protected void rotateCorpse(EntityLiving entityLiving, float f2, float f3, float f4) {
		this.rotateSquidCorpse((EntitySquid) entityLiving, f2, f3, f4);
	}

	public void doRenderLiving(EntityLiving entityLiving, double x, double y, double z, float f8, float f9) {
		this.doRenderSquid((EntitySquid) entityLiving, x, y, z, f8, f9);
	}

	public void doRender(Entity entity, double x, double y, double z, float f8, float f9) {
		this.doRenderSquid((EntitySquid) entity, x, y, z, f8, f9);
	}
}
