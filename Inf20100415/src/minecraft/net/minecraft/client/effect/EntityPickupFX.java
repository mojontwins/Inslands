package net.minecraft.client.effect;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.world.World;

import org.lwjgl.opengl.GL11;

import util.MathHelper;

public final class EntityPickupFX extends EntityFX {
	private Entity entityToPickUp;
	private EntityLiving entityPickingUp;
	private int age = 0;
	private int maxAge = 0;
	private float yOffs;

	public EntityPickupFX(World world1, Entity entity2, EntityLiving entityLiving3, float f4) {
		super(world1, entity2.posX, entity2.posY, entity2.posZ, entity2.motionX, entity2.motionY, entity2.motionZ);
		this.entityToPickUp = entity2;
		this.entityPickingUp = entityLiving3;
		this.maxAge = 3;
		this.yOffs = -0.5F;
	}

	public final void renderParticle(Tessellator tessellator1, float f2, float f3, float f4, float f5, float f6, float f7) {
		float f27 = (f27 = ((float)this.age + f2) / (float)this.maxAge) * f27;
		double d9 = this.entityToPickUp.posX;
		double d11 = this.entityToPickUp.posY;
		double d13 = this.entityToPickUp.posZ;
		double d15 = this.entityPickingUp.lastTickPosX + (this.entityPickingUp.posX - this.entityPickingUp.lastTickPosX) * (double)f2;
		double d17 = this.entityPickingUp.lastTickPosY + (this.entityPickingUp.posY - this.entityPickingUp.lastTickPosY) * (double)f2 + (double)this.yOffs;
		double d19 = this.entityPickingUp.lastTickPosZ + (this.entityPickingUp.posZ - this.entityPickingUp.lastTickPosZ) * (double)f2;
		double d21 = d9 + (d15 - d9) * (double)f27;
		double d23 = d11 + (d17 - d11) * (double)f27;
		double d25 = d13 + (d19 - d13) * (double)f27;
		int i28 = MathHelper.floor_double(d21);
		int i29 = MathHelper.floor_double(d23 + (double)(this.yOffset / 2.0F));
		int i30 = MathHelper.floor_double(d25);
		f27 = this.worldObj.getBrightness(i28, i29, i30);
		d21 -= interpPosX;
		d23 -= interpPosY;
		d25 -= interpPosZ;
		GL11.glColor4f(f27, f27, f27, 1.0F);
		RenderManager.instance.renderEntityWithPosYaw(this.entityToPickUp, (double)((float)d21), (double)((float)d23), (double)((float)d25), this.entityToPickUp.rotationYaw, f2);
	}

	public final void onUpdate() {
		++this.age;
		if(this.age == this.maxAge) {
			super.isDead = true;
		}

	}

	public final int getFXLayer() {
		return 2;
	}
}