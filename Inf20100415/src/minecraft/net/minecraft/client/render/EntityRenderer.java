package net.minecraft.client.render;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.RenderHelper;
import net.minecraft.client.controller.PlayerControllerCreative;
import net.minecraft.client.effect.EffectRenderer;
import net.minecraft.client.effect.EntityRainFX;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.render.camera.ClippingHelperImplementation;
import net.minecraft.client.render.camera.Frustrum;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.material.Material;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

import util.MathHelper;

public final class EntityRenderer {
	private Minecraft mc;
	private boolean anaglyphEnabled = false;
	private float farPlaneDistance = 0.0F;
	public ItemRenderer itemRenderer;
	private int rendererUpdateCount;
	private Entity pointedEntity = null;
	private int entityRendererInt1;
	private int entityRendererInt2;
	private Random random = new Random();
	private volatile int unusedVolatile1 = 0;
	private volatile int unusedVolatile2 = 0;
	private FloatBuffer fogColorBuffer = BufferUtils.createFloatBuffer(16);
	private float fogColorRed;
	private float fogColorGreen;
	private float fogColorBlue;
	private float fogColor2;
	private float fogColor1;

	public EntityRenderer(Minecraft minecraft1) {
		this.mc = minecraft1;
		this.itemRenderer = new ItemRenderer(minecraft1);
	}

	public final void updateRenderer() {
		this.fogColor2 = this.fogColor1;
		float f1 = this.mc.theWorld.getBrightness(MathHelper.floor_double(this.mc.thePlayer.posX), MathHelper.floor_double(this.mc.thePlayer.posY), MathHelper.floor_double(this.mc.thePlayer.posZ));
		float f2 = (float)(3 - this.mc.gameSettings.renderDistance) / 3.0F;
		f1 = f1 * (1.0F - f2) + f2;
		this.fogColor1 += (f1 - this.fogColor1) * 0.1F;
		++this.rendererUpdateCount;
		this.itemRenderer.updateEquippedItem();
		if(this.mc.inGameHasFocus) {
			EntityRenderer entityRenderer12 = this;
			EntityPlayerSP entityPlayerSP13 = this.mc.thePlayer;
			World world3 = this.mc.theWorld;
			int i4 = MathHelper.floor_double(entityPlayerSP13.posX);
			int i5 = MathHelper.floor_double(entityPlayerSP13.posY);
			int i14 = MathHelper.floor_double(entityPlayerSP13.posZ);

			for(int i6 = 0; i6 < 50; ++i6) {
				int i7 = i4 + entityRenderer12.random.nextInt(9) - 4;
				int i8 = i14 + entityRenderer12.random.nextInt(9) - 4;
				int i9 = world3.getBlockId(i7, 63, i8);
				if(64 <= i5 + 4 && 64 >= i5 - 4) {
					float f10 = entityRenderer12.random.nextFloat();
					float f11 = entityRenderer12.random.nextFloat();
					if(i9 > 0) {
						entityRenderer12.mc.effectRenderer.addEffect(new EntityRainFX(world3, (double)((float)i7 + f10), 64.0999984741211D - Block.blocksList[i9].minY, (double)((float)i8 + f11)));
					}
				}
			}
		}

	}

	private Vec3D orientCamera(float f1) {
		EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
		double d3 = this.mc.thePlayer.prevPosX + (entityPlayerSP2.posX - entityPlayerSP2.prevPosX) * (double)f1;
		double d5 = entityPlayerSP2.prevPosY + (entityPlayerSP2.posY - entityPlayerSP2.prevPosY) * (double)f1;
		double d7 = entityPlayerSP2.prevPosZ + (entityPlayerSP2.posZ - entityPlayerSP2.prevPosZ) * (double)f1;
		return new Vec3D(d3, d5, d7);
	}

	private void hurtCameraEffect(float f1) {
		EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
		float f3 = (float)this.mc.thePlayer.hurtTime - f1;
		if(entityPlayerSP2.health <= 0) {
			f1 += (float)entityPlayerSP2.deathTime;
			GL11.glRotatef(40.0F - 8000.0F / (f1 + 200.0F), 0.0F, 0.0F, 1.0F);
		}

		if(f3 >= 0.0F) {
			f3 = MathHelper.sin((f3 /= (float)entityPlayerSP2.maxHurtTime) * f3 * f3 * f3 * (float)Math.PI);
			f1 = entityPlayerSP2.attackedAtYaw;
			GL11.glRotatef(-entityPlayerSP2.attackedAtYaw, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-f3 * 14.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(f1, 0.0F, 1.0F, 0.0F);
		}
	}

	private void setupViewBobbing(float f1) {
		if(!this.mc.gameSettings.thirdPersonView) {
			EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
			float f3 = this.mc.thePlayer.distanceWalkedModified - entityPlayerSP2.prevDistanceWalkedModified;
			f3 = entityPlayerSP2.distanceWalkedModified + f3 * f1;
			float f4 = entityPlayerSP2.prevCameraYaw + (entityPlayerSP2.cameraYaw - entityPlayerSP2.prevCameraYaw) * f1;
			f1 = entityPlayerSP2.prevCameraPitch + (entityPlayerSP2.cameraPitch - entityPlayerSP2.prevCameraPitch) * f1;
			GL11.glTranslatef(MathHelper.sin(f3 * (float)Math.PI) * f4 * 0.5F, -Math.abs(MathHelper.cos(f3 * (float)Math.PI) * f4), 0.0F);
			GL11.glRotatef(MathHelper.sin(f3 * (float)Math.PI) * f4 * 3.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(Math.abs(MathHelper.cos(f3 * (float)Math.PI + 0.2F) * f4) * 5.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(f1, 1.0F, 0.0F, 0.0F);
		}
	}

	public final void updateCameraAndRender(float f1) {
		if(this.anaglyphEnabled && !Display.isActive()) {
			this.mc.displayInGameMenu();
		}

		this.anaglyphEnabled = Display.isActive();
		int i5;
		int i6;
		if(this.mc.inventoryScreen) {
			Mouse.getDX();
			byte b2 = 0;
			Mouse.getDY();
			byte b3 = 0;
			this.mc.mouseHelper.ungrabMouseCursor();
			byte b4 = 1;
			if(this.mc.gameSettings.invertMouse) {
				b4 = -1;
			}

			i5 = b2 + this.mc.mouseHelper.deltaX;
			i6 = b3 - this.mc.mouseHelper.deltaY;
			if(b2 != 0 || this.entityRendererInt1 != 0) {
				System.out.println("xxo: " + b2 + ", " + this.entityRendererInt1 + ": " + this.entityRendererInt1 + ", xo: " + i5);
			}

			if(this.entityRendererInt1 != 0) {
				this.entityRendererInt1 = 0;
			}

			if(this.entityRendererInt2 != 0) {
				this.entityRendererInt2 = 0;
			}

			if(b2 != 0) {
				this.entityRendererInt1 = b2;
			}

			if(b3 != 0) {
				this.entityRendererInt2 = b3;
			}

			float f10001 = (float)i5;
			float f11 = (float)(i6 * b4);
			float f9 = f10001;
			EntityPlayerSP entityPlayerSP7 = this.mc.thePlayer;
			float f13 = this.mc.thePlayer.rotationPitch;
			float f14 = entityPlayerSP7.rotationYaw;
			entityPlayerSP7.rotationYaw = (float)((double)entityPlayerSP7.rotationYaw + (double)f9 * 0.15D);
			entityPlayerSP7.rotationPitch = (float)((double)entityPlayerSP7.rotationPitch - (double)f11 * 0.15D);
			if(entityPlayerSP7.rotationPitch < -90.0F) {
				entityPlayerSP7.rotationPitch = -90.0F;
			}

			if(entityPlayerSP7.rotationPitch > 90.0F) {
				entityPlayerSP7.rotationPitch = 90.0F;
			}

			entityPlayerSP7.prevRotationPitch += entityPlayerSP7.rotationPitch - f13;
			entityPlayerSP7.prevRotationYaw += entityPlayerSP7.rotationYaw - f14;
		}

		ScaledResolution scaledResolution8;
		int i10 = (scaledResolution8 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight)).getScaledWidth();
		int i12 = scaledResolution8.getScaledHeight();
		i5 = Mouse.getX() * i10 / this.mc.displayWidth;
		i6 = i12 - Mouse.getY() * i12 / this.mc.displayHeight - 1;
		if(this.mc.theWorld != null) {
			this.getMouseOver(f1);
			this.mc.ingameGUI.renderGameOverlay(f1);
		} else {
			GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			this.setupOverlayRendering();
		}

		if(this.mc.currentScreen != null) {
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			this.mc.currentScreen.drawScreen(i5, i6, f1);
		}

		Thread.yield();
		Display.update();
	}

	private void getMouseOver(float f1) {
		EntityRenderer entityRenderer13 = this;
		EntityPlayerSP entityPlayerSP15;
		float f16 = (entityPlayerSP15 = this.mc.thePlayer).prevRotationPitch + (entityPlayerSP15.rotationPitch - entityPlayerSP15.prevRotationPitch) * f1;
		float f17 = entityPlayerSP15.prevRotationYaw + (entityPlayerSP15.rotationYaw - entityPlayerSP15.prevRotationYaw) * f1;
		Vec3D vec3D18 = this.orientCamera(f1);
		float f19 = MathHelper.cos(-f17 * 0.017453292F - (float)Math.PI);
		float f29 = MathHelper.sin(-f17 * 0.017453292F - (float)Math.PI);
		float f30 = -MathHelper.cos(-f16 * 0.017453292F);
		float f31 = MathHelper.sin(-f16 * 0.017453292F);
		float f32 = f29 * f30;
		float f34 = f19 * f30;
		double d35 = (double)this.mc.playerController.getBlockReachDistance();
		Vec3D vec3D37 = vec3D18.addVector((double)f32 * d35, (double)f31 * d35, (double)f34 * d35);
		this.mc.objectMouseOver = this.mc.theWorld.rayTraceBlocks(vec3D18, vec3D37);
		double d38 = d35;
		vec3D18 = this.orientCamera(f1);
		if(this.mc.objectMouseOver != null) {
			d38 = this.mc.objectMouseOver.hitVec.distance(vec3D18);
		}

		if(this.mc.playerController instanceof PlayerControllerCreative) {
			d35 = 32.0D;
		} else {
			if(d38 > 3.0D) {
				d38 = 3.0D;
			}

			d35 = d38;
		}

		vec3D37 = vec3D18.addVector((double)f32 * d35, (double)f31 * d35, (double)f34 * d35);
		this.pointedEntity = null;
		List list40 = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(entityPlayerSP15, entityPlayerSP15.boundingBox.addCoord((double)f32 * d35, (double)f31 * d35, (double)f34 * d35));
		double d41 = 0.0D;

		int i14;
		double d48;
		MovingObjectPosition movingObjectPosition56;
		for(i14 = 0; i14 < list40.size(); ++i14) {
			Entity entity53;
			if((entity53 = (Entity)list40.get(i14)).canBeCollidedWith() && (movingObjectPosition56 = entity53.boundingBox.expand((double)0.1F, (double)0.1F, (double)0.1F).calculateIntercept(vec3D18, vec3D37)) != null && ((d48 = vec3D18.distance(movingObjectPosition56.hitVec)) < d41 || d41 == 0.0D)) {
				entityRenderer13.pointedEntity = entity53;
				d41 = d48;
			}
		}

		if(entityRenderer13.pointedEntity != null && !(entityRenderer13.mc.playerController instanceof PlayerControllerCreative)) {
			entityRenderer13.mc.objectMouseOver = new MovingObjectPosition(entityRenderer13.pointedEntity);
		}

		EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
		World world3 = this.mc.theWorld;
		RenderGlobal renderGlobal4 = this.mc.renderGlobal;
		EffectRenderer effectRenderer5 = this.mc.effectRenderer;
		double d6 = entityPlayerSP2.lastTickPosX + (entityPlayerSP2.posX - entityPlayerSP2.lastTickPosX) * (double)f1;
		double d8 = entityPlayerSP2.lastTickPosY + (entityPlayerSP2.posY - entityPlayerSP2.lastTickPosY) * (double)f1;
		double d10 = entityPlayerSP2.lastTickPosZ + (entityPlayerSP2.posZ - entityPlayerSP2.lastTickPosZ) * (double)f1;

		for(int i12 = 0; i12 < 2; ++i12) {
			if(this.mc.gameSettings.anaglyph) {
				if(i12 == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			World world54 = this.mc.theWorld;
			EntityPlayerSP entityPlayerSP57 = this.mc.thePlayer;
			f17 = 1.0F / (float)(4 - this.mc.gameSettings.renderDistance);
			f17 = 1.0F - (float)Math.pow((double)f17, 0.25D);
			f19 = (float)(vec3D18 = world54.getSkyColor(f1)).xCoord;
			f29 = (float)vec3D18.yCoord;
			f30 = (float)vec3D18.zCoord;
			Vec3D vec3D66 = world54.getFogColor(f1);
			this.fogColorRed = (float)vec3D66.xCoord;
			this.fogColorGreen = (float)vec3D66.yCoord;
			this.fogColorBlue = (float)vec3D66.zCoord;
			this.fogColorRed += (f19 - this.fogColorRed) * f17;
			this.fogColorGreen += (f29 - this.fogColorGreen) * f17;
			this.fogColorBlue += (f30 - this.fogColorBlue) * f17;
			Block block71;
			if((block71 = Block.blocksList[world54.getBlockId(MathHelper.floor_double(entityPlayerSP57.posX), MathHelper.floor_double(entityPlayerSP57.posY + (double)0.12F), MathHelper.floor_double(entityPlayerSP57.posZ))]) != null && block71.blockMaterial != Material.air) {
				Material material33 = block71.blockMaterial;
				if(block71.blockMaterial == Material.water) {
					this.fogColorRed = 0.02F;
					this.fogColorGreen = 0.02F;
					this.fogColorBlue = 0.2F;
				} else if(material33 == Material.lava) {
					this.fogColorRed = 0.6F;
					this.fogColorGreen = 0.1F;
					this.fogColorBlue = 0.0F;
				}
			}

			float f73 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * f1;
			this.fogColorRed *= f73;
			this.fogColorGreen *= f73;
			this.fogColorBlue *= f73;
			if(this.mc.gameSettings.anaglyph) {
				f34 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
				float f76 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
				float f36 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
				this.fogColorRed = f34;
				this.fogColorGreen = f76;
				this.fogColorBlue = f36;
			}

			GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glEnable(GL11.GL_CULL_FACE);
			float f52 = f1;
			this.farPlaneDistance = (float)(256 >> this.mc.gameSettings.renderDistance);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			if(this.mc.gameSettings.anaglyph) {
				GL11.glTranslatef((float)(-((i12 << 1) - 1)) * 0.07F, 0.0F, 0.0F);
			}

			EntityPlayerSP entityPlayerSP64 = this.mc.thePlayer;
			f29 = 70.0F;
			if(entityPlayerSP64.isInsideOfMaterial()) {
				f29 = 60.0F;
			}

			if(entityPlayerSP64.health <= 0) {
				f30 = (float)entityPlayerSP64.deathTime + f1;
				f29 /= (1.0F - 500.0F / (f30 + 500.0F)) * 2.0F + 1.0F;
			}

			GLU.gluPerspective(f29, (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			if(this.mc.gameSettings.anaglyph) {
				GL11.glTranslatef((float)((i12 << 1) - 1) * 0.1F, 0.0F, 0.0F);
			}

			this.hurtCameraEffect(f1);
			if(this.mc.gameSettings.fancyGraphics) {
				this.setupViewBobbing(f1);
			}

			EntityRenderer entityRenderer59 = this;
			double d10001 = entityPlayerSP64.posX - entityPlayerSP64.prevPosX;
			double d67 = (entityPlayerSP64 = this.mc.thePlayer).prevPosX + d10001 * (double)f1;
			double d68 = entityPlayerSP64.prevPosY + (entityPlayerSP64.posY - entityPlayerSP64.prevPosY) * (double)f1;
			double d74 = entityPlayerSP64.prevPosZ + (entityPlayerSP64.posZ - entityPlayerSP64.prevPosZ) * (double)f1;
			if(!this.mc.gameSettings.thirdPersonView) {
				GL11.glTranslatef(0.0F, 0.0F, -0.1F);
			} else {
				d35 = 4.0D;
				double d78 = (double)(-MathHelper.sin(entityPlayerSP64.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(entityPlayerSP64.rotationPitch / 180.0F * (float)Math.PI)) * 4.0D;
				double d39 = (double)(MathHelper.cos(entityPlayerSP64.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(entityPlayerSP64.rotationPitch / 180.0F * (float)Math.PI)) * 4.0D;
				d41 = (double)(-MathHelper.sin(entityPlayerSP64.rotationPitch / 180.0F * (float)Math.PI)) * 4.0D;
				
				for(int i53 = 0; i53 < 8; ++i53) {
					float f55 = (float)(((i53 & 1) << 1) - 1);
					float f50 = (float)(((i53 >> 1 & 1) << 1) - 1);
					f16 = (float)(((i53 >> 2 & 1) << 1) - 1);
					f55 *= 0.1F;
					f50 *= 0.1F;
					f16 *= 0.1F;
					if((movingObjectPosition56 = entityRenderer59.mc.theWorld.rayTraceBlocks(new Vec3D(d67 + (double)f55, d68 + (double)f50, d74 + (double)f16), new Vec3D(d67 - d78 + (double)f55 + (double)f16, d68 - d41 + (double)f50, d74 - d39 + (double)f16))) != null && (d48 = movingObjectPosition56.hitVec.distance(new Vec3D(d67, d68, d74))) < d35) {
						d35 = d48;
					}
				}

				GL11.glTranslatef(0.0F, 0.0F, (float)(-d35));
			}

			GL11.glRotatef(entityPlayerSP64.prevRotationPitch + (entityPlayerSP64.rotationPitch - entityPlayerSP64.prevRotationPitch) * f52, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(entityPlayerSP64.prevRotationYaw + (entityPlayerSP64.rotationYaw - entityPlayerSP64.prevRotationYaw) * f52 + 180.0F, 0.0F, 1.0F, 0.0F);
			ClippingHelperImplementation.init();
			this.setupFog();
			GL11.glEnable(GL11.GL_FOG);
			renderGlobal4.renderSky(f1);
			this.setupFog();
			Frustrum frustrum51;
			(frustrum51 = new Frustrum()).setPosition(d6, d8, d10);
			this.mc.renderGlobal.clipRenderersByFrustrum(frustrum51);
			this.mc.renderGlobal.updateRenderers(entityPlayerSP2);
			this.setupFog();
			GL11.glEnable(GL11.GL_FOG);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
			RenderHelper.disableStandardItemLighting();
			renderGlobal4.sortAndRender(entityPlayerSP2, 0, (double)f1);
			i14 = MathHelper.floor_double(entityPlayerSP2.posX);
			int i58 = MathHelper.floor_double(entityPlayerSP2.posY);
			int i60 = MathHelper.floor_double(entityPlayerSP2.posZ);
			int i63;
			int i65;
			if(world3.isSolid(i14, i58, i60)) {
				RenderBlocks renderBlocks61 = new RenderBlocks(world3);

				for(i63 = i14 - 1; i63 <= i14 + 1; ++i63) {
					for(i65 = i58 - 1; i65 <= i58 + 1; ++i65) {
						for(int i20 = i60 - 1; i20 <= i60 + 1; ++i20) {
							int i21;
							if((i21 = world3.getBlockId(i63, i65, i20)) > 0) {
								renderBlocks61.renderBlockAllFaces(Block.blocksList[i21], i63, i65, i20);
							}
						}
					}
				}
			}

			RenderHelper.enableStandardItemLighting();
			GL11.glPushMatrix();
			renderGlobal4.renderEntities(this.orientCamera(f1), frustrum51, f1);
			effectRenderer5.renderLitParticles(f1);
			GL11.glPopMatrix();
			RenderHelper.disableStandardItemLighting();
			this.setupFog();
			effectRenderer5.renderParticles(entityPlayerSP2, f1);
			if(this.mc.objectMouseOver != null && entityPlayerSP2.isInsideOfMaterial()) {
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				renderGlobal4.drawBlockBreaking(entityPlayerSP2, this.mc.objectMouseOver, 0, entityPlayerSP2.inventory.getCurrentItem(), f1);
				renderGlobal4.drawSelectionBox(entityPlayerSP2, this.mc.objectMouseOver, 0, f1);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			this.setupFog();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glColorMask(false, false, false, false);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
			int i62 = renderGlobal4.sortAndRender(entityPlayerSP2, 1, (double)f1);
			GL11.glColorMask(true, true, true, true);
			if(this.mc.gameSettings.anaglyph) {
				if(i12 == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			if(i62 > 0) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
				renderGlobal4.renderAllRenderLists(1, (double)f1);
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			if(this.mc.objectMouseOver != null && !entityPlayerSP2.isInsideOfMaterial()) {
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				renderGlobal4.drawBlockBreaking(entityPlayerSP2, this.mc.objectMouseOver, 0, entityPlayerSP2.inventory.getCurrentItem(), f1);
				renderGlobal4.drawSelectionBox(entityPlayerSP2, this.mc.objectMouseOver, 0, f1);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			GL11.glDisable(GL11.GL_FOG);
			if(this.mc.inGameHasFocus) {
				f52 = f1;
				entityRenderer13 = this;
				i62 = MathHelper.floor_double((entityPlayerSP15 = this.mc.thePlayer).posX);
				i63 = MathHelper.floor_double(entityPlayerSP15.posY);
				i65 = MathHelper.floor_double(entityPlayerSP15.posZ);
				Tessellator tessellator69 = Tessellator.instance;
				GL11.glDisable(GL11.GL_CULL_FACE);
				GL11.glNormal3f(0.0F, 1.0F, 0.0F);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/rain.png"));
				int i70 = i62 - 5;

				while(true) {
					if(i70 > i62 + 5) {
						GL11.glEnable(GL11.GL_CULL_FACE);
						GL11.glDisable(GL11.GL_BLEND);
						break;
					}

					for(int i72 = i65 - 5; i72 <= i65 + 5; ++i72) {
						int i75 = i63 - 5;
						int i77 = i63 + 5;
						if(i75 < 64) {
							i75 = 64;
						}

						if(i77 < 64) {
							i77 = 64;
						}

						if(i75 != i77) {
							float f79 = ((float)((entityRenderer13.rendererUpdateCount + i70 * 3121 + i72 * 418711) % 32) + f52) / 32.0F;
							d38 = (double)((float)i70 + 0.5F) - entityPlayerSP15.posX;
							double d80 = (double)((float)i72 + 0.5F) - entityPlayerSP15.posZ;
							float f42 = MathHelper.sqrt_double(d38 * d38 + d80 * d80) / 5.0F;
							GL11.glColor4f(1.0F, 1.0F, 1.0F, (1.0F - f42 * f42) * 0.7F);
							tessellator69.startDrawingQuads();
							tessellator69.addVertexWithUV((double)i70, (double)i75, (double)i72, 0.0D, (double)((float)i75 * 2.0F / 8.0F + f79 * 2.0F));
							tessellator69.addVertexWithUV((double)(i70 + 1), (double)i75, (double)(i72 + 1), 2.0D, (double)((float)i75 * 2.0F / 8.0F + f79 * 2.0F));
							tessellator69.addVertexWithUV((double)(i70 + 1), (double)i77, (double)(i72 + 1), 2.0D, (double)((float)i77 * 2.0F / 8.0F + f79 * 2.0F));
							tessellator69.addVertexWithUV((double)i70, (double)i77, (double)i72, 0.0D, (double)((float)i77 * 2.0F / 8.0F + f79 * 2.0F));
							tessellator69.addVertexWithUV((double)i70, (double)i75, (double)(i72 + 1), 0.0D, (double)((float)i75 * 2.0F / 8.0F + f79 * 2.0F));
							tessellator69.addVertexWithUV((double)(i70 + 1), (double)i75, (double)i72, 2.0D, (double)((float)i75 * 2.0F / 8.0F + f79 * 2.0F));
							tessellator69.addVertexWithUV((double)(i70 + 1), (double)i77, (double)i72, 2.0D, (double)((float)i77 * 2.0F / 8.0F + f79 * 2.0F));
							tessellator69.addVertexWithUV((double)i70, (double)i77, (double)(i72 + 1), 0.0D, (double)((float)i77 * 2.0F / 8.0F + f79 * 2.0F));
							tessellator69.draw();
						}
					}

					++i70;
				}
			}

			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();
			if(this.mc.gameSettings.anaglyph) {
				GL11.glTranslatef((float)((i12 << 1) - 1) * 0.1F, 0.0F, 0.0F);
			}

			GL11.glPushMatrix();
			this.hurtCameraEffect(f1);
			if(this.mc.gameSettings.fancyGraphics) {
				this.setupViewBobbing(f1);
			}

			if(!this.mc.gameSettings.thirdPersonView) {
				this.itemRenderer.renderItemInFirstPerson(f1);
			}

			GL11.glPopMatrix();
			if(!this.mc.gameSettings.thirdPersonView) {
				this.itemRenderer.renderOverlays(f1);
				this.hurtCameraEffect(f1);
			}

			if(this.mc.gameSettings.fancyGraphics) {
				this.setupViewBobbing(f1);
			}

			if(!this.mc.gameSettings.anaglyph) {
				return;
			}
		}

		GL11.glColorMask(true, true, true, false);
	}

	public final void setupOverlayRendering() {
		ScaledResolution scaledResolution1;
		int i2 = (scaledResolution1 = new ScaledResolution(this.mc.displayWidth, this.mc.displayHeight)).getScaledWidth();
		int i3 = scaledResolution1.getScaledHeight();
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)i2, (double)i3, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
	}

	private void setupFog() {
		World world1 = this.mc.theWorld;
		EntityPlayerSP entityPlayerSP2 = this.mc.thePlayer;
		float f3 = 1.0F;
		float f6 = this.fogColorBlue;
		float f5 = this.fogColorGreen;
		float f4 = this.fogColorRed;
		this.fogColorBuffer.clear();
		this.fogColorBuffer.put(f4).put(f5).put(f6).put(1.0F);
		this.fogColorBuffer.flip();
		GL11.glFog(GL11.GL_FOG_COLOR, this.fogColorBuffer);
		GL11.glNormal3f(0.0F, -1.0F, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Block block7;
		if((block7 = Block.blocksList[world1.getBlockId(MathHelper.floor_double(entityPlayerSP2.posX), MathHelper.floor_double(entityPlayerSP2.posY + (double)0.12F), MathHelper.floor_double(entityPlayerSP2.posZ))]) != null && block7.blockMaterial.getIsLiquid()) {
			Material material8 = block7.blockMaterial;
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
			if(material8 == Material.water) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
			} else if(material8 == Material.lava) {
				GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
			}
		} else {
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
			GL11.glFogf(GL11.GL_FOG_START, this.farPlaneDistance * 0.25F);
			GL11.glFogf(GL11.GL_FOG_END, this.farPlaneDistance);
			if(GLContext.getCapabilities().GL_NV_fog_distance) {
				GL11.glFogi(34138, 34139);
			}
		}

		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
	}
}
