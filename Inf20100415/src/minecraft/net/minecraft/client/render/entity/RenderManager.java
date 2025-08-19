package net.minecraft.client.render.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.model.ModelSheep;
import net.minecraft.client.model.ModelSheepFur;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.EntityPainting;
import net.minecraft.game.entity.animal.EntityPig;
import net.minecraft.game.entity.animal.EntitySheep;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.misc.EntityTNT;
import net.minecraft.game.entity.monster.EntityCreeper;
import net.minecraft.game.entity.monster.EntityGiant;
import net.minecraft.game.entity.monster.EntitySkeleton;
import net.minecraft.game.entity.monster.EntitySpider;
import net.minecraft.game.entity.monster.EntityZombie;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.projectile.EntityArrow;
import net.minecraft.game.world.World;

import org.lwjgl.opengl.GL11;

public final class RenderManager {
	private Map entityRenderMap = new HashMap();
	public static RenderManager instance = new RenderManager();
	public static double renderPosX;
	public static double renderPosY;
	public static double renderPosZ;
	public RenderEngine renderEngine;
	public World worldObj;
	public float playerViewY;
	private double tickPosX;
	private double tickPosY;
	private double tickPosZ;

	private RenderManager() {
		this.entityRenderMap.put(EntitySpider.class, new RenderSpider());
		this.entityRenderMap.put(EntityPig.class, new RenderLiving(new ModelPig(), 0.7F));
		this.entityRenderMap.put(EntitySheep.class, new RenderSheep(new ModelSheep(), new ModelSheepFur(), 0.7F));
		this.entityRenderMap.put(EntityCreeper.class, new RenderCreeper());
		this.entityRenderMap.put(EntitySkeleton.class, new RenderLiving(new ModelSkeleton(), 0.5F));
		this.entityRenderMap.put(EntityZombie.class, new RenderLiving(new ModelZombie(), 0.5F));
		this.entityRenderMap.put(EntityPlayer.class, new RenderPlayer());
		this.entityRenderMap.put(EntityGiant.class, new RenderGiantZombie(new ModelZombie(), 0.5F, 6.0F));
		this.entityRenderMap.put(EntityLiving.class, new RenderLiving(new ModelBiped(), 0.5F));
		this.entityRenderMap.put(Entity.class, new RenderEntity());
		this.entityRenderMap.put(EntityPainting.class, new RenderPainting());
		this.entityRenderMap.put(EntityArrow.class, new RenderArrow());
		this.entityRenderMap.put(EntityItem.class, new RenderItem());
		this.entityRenderMap.put(EntityTNT.class, new RenderTNT());
		Iterator iterator1 = this.entityRenderMap.values().iterator();

		while(iterator1.hasNext()) {
			((Render)iterator1.next()).setRenderManager(this);
		}

	}

	public final Render getEntityRenderObject(Entity entity1) {
		Class class2 = entity1.getClass();
		Render render3;
		if((render3 = (Render)this.entityRenderMap.get(class2)) == null && class2 != Entity.class) {
			render3 = (Render)this.entityRenderMap.get(class2.getSuperclass());
			this.entityRenderMap.put(class2, render3);
		}

		return render3;
	}

	public final void cacheActiveRenderInfo(World world1, RenderEngine renderEngine2, EntityPlayer entityPlayer3, float f4) {
		this.worldObj = world1;
		this.renderEngine = renderEngine2;
		this.playerViewY = entityPlayer3.prevRotationYaw + (entityPlayer3.rotationYaw - entityPlayer3.prevRotationYaw) * f4;
		this.tickPosX = entityPlayer3.lastTickPosX + (entityPlayer3.posX - entityPlayer3.lastTickPosX) * (double)f4;
		this.tickPosY = entityPlayer3.lastTickPosY + (entityPlayer3.posY - entityPlayer3.lastTickPosY) * (double)f4;
		this.tickPosZ = entityPlayer3.lastTickPosZ + (entityPlayer3.posZ - entityPlayer3.lastTickPosZ) * (double)f4;
	}

	public final void renderEntity(Entity entity1, float f2) {
		double d3 = entity1.lastTickPosX + (entity1.posX - entity1.lastTickPosX) * (double)f2;
		double d5 = entity1.lastTickPosY + (entity1.posY - entity1.lastTickPosY) * (double)f2;
		double d7 = entity1.lastTickPosZ + (entity1.posZ - entity1.lastTickPosZ) * (double)f2;
		float f9 = entity1.prevRotationYaw + (entity1.rotationYaw - entity1.prevRotationYaw) * f2;
		float f10;
		GL11.glColor3f(f10 = entity1.getEntityBrightness(f2), f10, f10);
		this.renderEntityWithPosYaw(entity1, d3 - renderPosX, d5 - renderPosY, d7 - renderPosZ, f9, f2);
	}

	public final void renderEntityWithPosYaw(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		Render render10;
		if((render10 = this.getEntityRenderObject(entity1)) != null) {
			render10.doRender(entity1, d2, d4, d6, f8, f9);
			render10.renderShadow(entity1, d2, d4, d6, f9);
		}

	}

	public final void set(World world1) {
		this.worldObj = world1;
	}

	public final double getDistanceToCamera(double d1, double d3, double d5) {
		double d7 = d1 - this.tickPosX;
		double d9 = d3 - this.tickPosY;
		double d11 = d5 - this.tickPosZ;
		return d7 * d7 + d9 * d9 + d11 * d11;
	}
}