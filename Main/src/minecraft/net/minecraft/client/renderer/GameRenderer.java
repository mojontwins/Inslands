package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.particle.EntityRainFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.player.PlayerControllerTest;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.entity.status.Status;
import net.minecraft.world.level.Weather;
import net.minecraft.world.level.World;
import net.minecraft.world.level.biome.BiomeGenBase;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.MovingObjectPosition;
import net.minecraft.world.phys.Vec3D;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public class GameRenderer {
	public static boolean anaglyphEnable = false;
	public static int anaglyphField;
	private Minecraft mc;
	private float farPlaneDistance = 0.0F;
	public ItemRenderer itemRenderer;
	private int rendererUpdateCount;
	private Entity pointedEntity = null;
	private MouseFilter mouseFilterXAxis = new MouseFilter();
	private MouseFilter mouseFilterYAxis = new MouseFilter();
	private float thirdPersonDistance = 4.0F;
	private float thirdPersonDistanceTemp = 4.0F;
	private float debugCamYaw = 0.0F;
	private float prevDebugCamYaw = 0.0F;
	private float debugCamPitch = 0.0F;
	private float prevDebugCamPitch = 0.0F;
	private float smoothCamYaw;
	private float smoothCamPitch;
	private float smoothCamFilterX;
	private float smoothCamFilterY;
	private float smoothCamPartialTicks;
	private float debugCamFOV = 0.0F;
	private float prevDebugCamFOV = 0.0F;
	private float camRoll = 0.0F;
	private float prevCamRoll = 0.0F;
	public int lightmapTexture;
	private int[] lightmapColors;
	private boolean cloudFog = false;
	private double cameraZoom = 1.0D;
	private double cameraYaw = 0.0D;
	private double cameraPitch = 0.0D;
	private long prevFrameTime = System.currentTimeMillis();
	private long renderEndNanoTime = 0L;
	private boolean lightmapUpdateNeeded = false;
	private Random random = new Random();
	private int rainSoundCounter = 0;
	volatile int field_1394_b = 0;
	volatile int field_1393_c = 0;
	FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
	float fogColorRed;
	float fogColorGreen;
	float fogColorBlue;
	private float fogColor2;
	private float fogColor1;
	
	float rainXCoords[] = new float[1024];
	float rainYCoords[] = new float[1024];

	public GameRenderer(Minecraft minecraft1) {
		this.mc = minecraft1;
		this.itemRenderer = new ItemRenderer(minecraft1);
		this.lightmapTexture = minecraft1.renderEngine.allocateAndSetupTexture(new BufferedImage(16, 16, 1));
		this.lightmapColors = new int[256];
		
		// LUT
		int idx = 0;
		for (int x = -16; x < 16; x ++) for (int z = -16; z < 16; z ++) {
			float distance = MathHelper.sqrt_float((float)(x * x) + (z * z));
			this.rainXCoords [idx] = -((float)x) / distance;
			this.rainYCoords [idx] = ((float)z) / distance;
			idx ++;
		}
	}

	public void updateRenderer() {
		this.lightmapUpdateNeeded = true;
		this.fogColor2 = this.fogColor1;
		this.thirdPersonDistanceTemp = this.thirdPersonDistance;
		this.prevDebugCamYaw = this.debugCamYaw;
		this.prevDebugCamPitch = this.debugCamPitch;
		this.prevDebugCamFOV = this.debugCamFOV;
		this.prevCamRoll = this.camRoll;
		
		if(this.mc.gameSettings.smoothCamera) {
			float f1 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			float f2 = f1 * f1 * f1 * 8.0F;
			this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05F * f2);
			this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05F * f2);
			this.smoothCamPartialTicks = 0.0F;
			this.smoothCamYaw = 0.0F;
			this.smoothCamPitch = 0.0F;
		}
		
		if(this.mc.renderViewEntity == null) {
			this.mc.renderViewEntity = this.mc.thePlayer;
		}

		float f1 = this.mc.theWorld.getLightBrightness(MathHelper.floor_double(this.mc.renderViewEntity.posX), MathHelper.floor_double(this.mc.renderViewEntity.posY), MathHelper.floor_double(this.mc.renderViewEntity.posZ));
		float f2 = (float)(3 - this.mc.gameSettings.renderDistance) / 3.0F;
		float f3 = f1 * (1.0F - f2) + f2;
		this.fogColor1 += (f3 - this.fogColor1) * 0.1F;
		++this.rendererUpdateCount;
		this.itemRenderer.updateEquippedItem();
		this.addRainParticles();
	}

	public void getMouseOver(float f1) {
		if(this.mc.renderViewEntity != null) {
			if(this.mc.theWorld != null) {
				double d2 = (double)this.mc.playerController.getBlockReachDistance();
				this.mc.objectMouseOver = this.mc.renderViewEntity.rayTrace(d2, f1);
				double d4 = d2;
				Vec3D vec3D6 = this.mc.renderViewEntity.getPosition(f1);
				if(this.mc.objectMouseOver != null) {
					d4 = this.mc.objectMouseOver.hitVec.distanceTo(vec3D6);
				}

				if(this.mc.playerController instanceof PlayerControllerTest) {
					d2 = 32.0D;
					d4 = 32.0D;
				} else {
					if(d4 > 3.0D) {
						d4 = 3.0D;
					}

					d2 = d4;
				}

				Vec3D vec3D7 = this.mc.renderViewEntity.getLook(f1);
				Vec3D vec3D8 = vec3D6.addVector(vec3D7.xCoord * d2, vec3D7.yCoord * d2, vec3D7.zCoord * d2);
				this.pointedEntity = null;
				float f9 = 1.0F;
				List<Entity> list10 = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(this.mc.renderViewEntity, this.mc.renderViewEntity.boundingBox.addCoord(vec3D7.xCoord * d2, vec3D7.yCoord * d2, vec3D7.zCoord * d2).expand((double)f9, (double)f9, (double)f9));
				double d11 = 0.0D;

				for(int i13 = 0; i13 < list10.size(); ++i13) {
					Entity entity14 = (Entity)list10.get(i13);
					if(entity14.canBeCollidedWith()) {
						float f15 = entity14.getCollisionBorderSize();
						AxisAlignedBB axisAlignedBB16 = entity14.boundingBox.expand((double)f15, (double)f15, (double)f15);
						MovingObjectPosition movingObjectPosition17 = axisAlignedBB16.raytrace(vec3D6, vec3D8);
						if(axisAlignedBB16.isVecInside(vec3D6)) {
							if(0.0D < d11 || d11 == 0.0D) {
								this.pointedEntity = entity14;
								d11 = 0.0D;
							}
						} else if(movingObjectPosition17 != null) {
							double d18 = vec3D6.distanceTo(movingObjectPosition17.hitVec);
							if(d18 < d11 || d11 == 0.0D) {
								this.pointedEntity = entity14;
								d11 = d18;
							}
						}
					}
				}

				if(this.pointedEntity != null && !(this.mc.playerController instanceof PlayerControllerTest)) {
					this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity);
				}

			}
		}
	}

	// TODO: Pinch here: fov modifier.
	private float getFOVModifier(float f1) {
		EntityLiving entityLiving2 = this.mc.renderViewEntity;
		float f3 = 70.0F;
		if(entityLiving2.isInsideOfMaterial(Material.water)) {
			if(this.mc.thePlayer.divingHelmetOn()) {
				f3 = 50.0F;
			} else {
				f3 = 60.0F;				
			}
		}

		if(entityLiving2.health <= 0) {
			float f4 = (float)entityLiving2.deathTime + f1;
			f3 /= (1.0F - 500.0F / (f4 + 500.0F)) * 2.0F + 1.0F;
		}

		return f3 + this.prevDebugCamFOV + (this.debugCamFOV - this.prevDebugCamFOV) * f1 + this.mc.gameSettings.FOV;
	}

	private void hurtCameraEffect(float f1) {
		EntityLiving entityLiving2 = this.mc.renderViewEntity;
		
		if(entityLiving2 instanceof EntityPlayer && ((EntityPlayer) entityLiving2).isCreative) return;
		
		float f3 = (float)entityLiving2.hurtTime - f1;
		float f4;
		if(entityLiving2.health <= 0) {
			f4 = (float)entityLiving2.deathTime + f1;
			GL11.glRotatef(40.0F - 8000.0F / (f4 + 200.0F), 0.0F, 0.0F, 1.0F);
		}

		if(f3 >= 0.0F) {
			f3 /= (float)entityLiving2.maxHurtTime;
			f3 = MathHelper.sin(f3 * f3 * f3 * f3 * (float)Math.PI);
			f4 = entityLiving2.attackedAtYaw;
			GL11.glRotatef(-f4, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-f3 * 14.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(f4, 0.0F, 1.0F, 0.0F);
		}
	}

	private void setupViewBobbing(float f1) {
		if(this.mc.renderViewEntity instanceof EntityPlayer) {
			EntityPlayer entityPlayer2 = (EntityPlayer)this.mc.renderViewEntity;
			float f3 = entityPlayer2.distanceWalkedModified - entityPlayer2.prevDistanceWalkedModified;
			float f4 = -(entityPlayer2.distanceWalkedModified + f3 * f1);
			float f5 = entityPlayer2.prevCameraYaw + (entityPlayer2.cameraYaw - entityPlayer2.prevCameraYaw) * f1;
			float f6 = entityPlayer2.cameraPitch + (entityPlayer2.field_9328_R - entityPlayer2.cameraPitch) * f1;
			GL11.glTranslatef(MathHelper.sin(f4 * (float)Math.PI) * f5 * 0.5F, -Math.abs(MathHelper.cos(f4 * (float)Math.PI) * f5), 0.0F);
			GL11.glRotatef(MathHelper.sin(f4 * (float)Math.PI) * f5 * 3.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(Math.abs(MathHelper.cos(f4 * (float)Math.PI - 0.2F) * f5) * 5.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(f6, 1.0F, 0.0F, 0.0F);
		}
	}

	private void orientCamera(float f1) {
		EntityLiving entityLiving2 = this.mc.renderViewEntity;
		float f3 = entityLiving2.yOffset - 1.62F;
		double d4 = entityLiving2.prevPosX + (entityLiving2.posX - entityLiving2.prevPosX) * (double)f1;
		double d6 = entityLiving2.prevPosY + (entityLiving2.posY - entityLiving2.prevPosY) * (double)f1 - (double)f3;
		double d8 = entityLiving2.prevPosZ + (entityLiving2.posZ - entityLiving2.prevPosZ) * (double)f1;
		GL11.glRotatef(this.prevCamRoll + (this.camRoll - this.prevCamRoll) * f1, 0.0F, 0.0F, 1.0F);
		if(entityLiving2.isPlayerSleeping()) {
			f3 = (float)((double)f3 + 1.0D);
			GL11.glTranslatef(0.0F, 0.3F, 0.0F);
			if(!this.mc.gameSettings.field_22273_E) {
				int i10 = this.mc.theWorld.getBlockId(MathHelper.floor_double(entityLiving2.posX), MathHelper.floor_double(entityLiving2.posY), MathHelper.floor_double(entityLiving2.posZ));
				if(i10 == Block.blockBed.blockID) {
					int i11 = this.mc.theWorld.getBlockMetadata(MathHelper.floor_double(entityLiving2.posX), MathHelper.floor_double(entityLiving2.posY), MathHelper.floor_double(entityLiving2.posZ));
					int i12 = i11 & 3;
					GL11.glRotatef((float)(i12 * 90), 0.0F, 1.0F, 0.0F);
				}

				GL11.glRotatef(entityLiving2.prevRotationYaw + (entityLiving2.rotationYaw - entityLiving2.prevRotationYaw) * f1 + 180.0F, 0.0F, -1.0F, 0.0F);
				GL11.glRotatef(entityLiving2.prevRotationPitch + (entityLiving2.rotationPitch - entityLiving2.prevRotationPitch) * f1, -1.0F, 0.0F, 0.0F);
			}
		} else if(this.mc.gameSettings.thirdPersonView) {
			double d27 = (double)(this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * f1);
			float f13;
			float f28;
			if(this.mc.gameSettings.field_22273_E) {
				f28 = this.prevDebugCamYaw + (this.debugCamYaw - this.prevDebugCamYaw) * f1;
				f13 = this.prevDebugCamPitch + (this.debugCamPitch - this.prevDebugCamPitch) * f1;
				GL11.glTranslatef(0.0F, 0.0F, (float)(-d27));
				GL11.glRotatef(f13, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(f28, 0.0F, 1.0F, 0.0F);
			} else {
				f28 = entityLiving2.rotationYaw;
				f13 = entityLiving2.rotationPitch;
				double d14 = (double)(-MathHelper.sin(f28 / 180.0F * (float)Math.PI) * MathHelper.cos(f13 / 180.0F * (float)Math.PI)) * d27;
				double d16 = (double)(MathHelper.cos(f28 / 180.0F * (float)Math.PI) * MathHelper.cos(f13 / 180.0F * (float)Math.PI)) * d27;
				double d18 = (double)(-MathHelper.sin(f13 / 180.0F * (float)Math.PI)) * d27;

				for(int i20 = 0; i20 < 8; ++i20) {
					float f21 = (float)((i20 & 1) * 2 - 1);
					float f22 = (float)((i20 >> 1 & 1) * 2 - 1);
					float f23 = (float)((i20 >> 2 & 1) * 2 - 1);
					f21 *= 0.1F;
					f22 *= 0.1F;
					f23 *= 0.1F;
					MovingObjectPosition movingObjectPosition24 = this.mc.theWorld.rayTraceBlocks(Vec3D.createVector(d4 + (double)f21, d6 + (double)f22, d8 + (double)f23), Vec3D.createVector(d4 - d14 + (double)f21 + (double)f23, d6 - d18 + (double)f22, d8 - d16 + (double)f23));
					if(movingObjectPosition24 != null) {
						double d25 = movingObjectPosition24.hitVec.distanceTo(Vec3D.createVector(d4, d6, d8));
						if(d25 < d27) {
							d27 = d25;
						}
					}
				}

				GL11.glRotatef(entityLiving2.rotationPitch - f13, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(entityLiving2.rotationYaw - f28, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(0.0F, 0.0F, (float)(-d27));
				GL11.glRotatef(f28 - entityLiving2.rotationYaw, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(f13 - entityLiving2.rotationPitch, 1.0F, 0.0F, 0.0F);
			}
		} else {
			GL11.glTranslatef(0.0F, 0.0F, -0.1F);
		}

		if(!this.mc.gameSettings.field_22273_E) {
			GL11.glRotatef(entityLiving2.prevRotationPitch + (entityLiving2.rotationPitch - entityLiving2.prevRotationPitch) * f1, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(entityLiving2.prevRotationYaw + (entityLiving2.rotationYaw - entityLiving2.prevRotationYaw) * f1 + 180.0F, 0.0F, 1.0F, 0.0F);
		}

		GL11.glTranslatef(0.0F, f3, 0.0F);
		d4 = entityLiving2.prevPosX + (entityLiving2.posX - entityLiving2.prevPosX) * (double)f1;
		d6 = entityLiving2.prevPosY + (entityLiving2.posY - entityLiving2.prevPosY) * (double)f1 - (double)f3;
		d8 = entityLiving2.prevPosZ + (entityLiving2.posZ - entityLiving2.prevPosZ) * (double)f1;
		this.cloudFog = this.mc.renderGlobal.clipRenderersByFrustrum(d4, d6, d8, f1);
	}

	private void setupCameraTransform(float f1, int i2) {
		this.farPlaneDistance = (float)(256 >> this.mc.gameSettings.renderDistance);
		if(Config.isFogFancy()) {
			this.farPlaneDistance *= 0.95F;
		}

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		float f3 = 0.07F;
		if(this.mc.gameSettings.anaglyph) {
			GL11.glTranslatef((float)(-(i2 * 2 - 1)) * f3, 0.0F, 0.0F);
		}

		if(this.cameraZoom != 1.0D) {
			GL11.glTranslatef((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0F);
			GL11.glScaled(this.cameraZoom, this.cameraZoom, 1.0D);
			GLU.gluPerspective(this.getFOVModifier(f1), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
		} else {
			GLU.gluPerspective(this.getFOVModifier(f1), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
		}

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		if(this.mc.gameSettings.anaglyph) {
			GL11.glTranslatef((float)(i2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
		}

		this.hurtCameraEffect(f1);
		if(this.mc.gameSettings.viewBobbing) {
			this.setupViewBobbing(f1);
		}

		float f4 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * f1;
		if(f4 > 0.0F) {
			float f5 = 5.0F / (f4 * f4 + 5.0F) - f4 * 0.04F;
			f5 *= f5;
			float multiplier = 20.0F;
			
			if(this.mc.thePlayer.isStatusActive(Status.statusDizzy)) multiplier = 7.0F;
			
			GL11.glRotatef(((float)this.rendererUpdateCount + f1) * multiplier, 0.0F, 1.0F, 1.0F);
			GL11.glScalef(1.0F / f5, 1.0F, 1.0F);
			GL11.glRotatef(-((float)this.rendererUpdateCount + f1) * multiplier, 0.0F, 1.0F, 1.0F);
		}

		this.orientCamera(f1);
	}

	private void renderHand(float f1, int i2) {
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		if(this.mc.gameSettings.anaglyph) {
			GL11.glTranslatef((float)(i2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
		}

		GL11.glPushMatrix();
		this.hurtCameraEffect(f1);
		if(this.mc.gameSettings.viewBobbing) {
			this.setupViewBobbing(f1);
		}

		if(!this.mc.gameSettings.thirdPersonView && !this.mc.renderViewEntity.isPlayerSleeping() && !this.mc.gameSettings.hideGUI) {
			this.enableLightmap((double)f1);
			this.itemRenderer.renderItemInFirstPerson(f1);
			this.disableLightmap((double)f1);
		}

		GL11.glPopMatrix();
		if(!this.mc.gameSettings.thirdPersonView && !this.mc.renderViewEntity.isPlayerSleeping()) {
			this.itemRenderer.renderOverlays(f1);
			this.hurtCameraEffect(f1);
		}

		if(this.mc.gameSettings.viewBobbing) {
			this.setupViewBobbing(f1);
		}

	}

	public void disableLightmap(double d1) {
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	public void enableLightmap(double d1) {
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glLoadIdentity();
		float f3 = 0.00390625F;
		GL11.glScalef(f3, f3, f3);
		GL11.glTranslatef(8.0F, 8.0F, 8.0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		this.mc.renderEngine.bindTexture(this.lightmapTexture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	private void updateLightmap() {
		World world1 = this.mc.theWorld;
		if(world1 != null) {
			this.lightmapColors = world1.worldProvider.updateLightmap(this.mc.thePlayer, this.mc.gameSettings.gammaSetting);
			
			this.mc.renderEngine.createTextureFromBytes(this.lightmapColors, 16, 16, this.lightmapTexture);
			this.lightmapUpdateNeeded = false; // NEW
		}
	}

	public void updateCameraAndRender(float f1) {
		if(this.lightmapUpdateNeeded) {
			this.updateLightmap();
		}
		if(!Display.isActive()) {
			if(System.currentTimeMillis() - this.prevFrameTime > 500L) {
				this.mc.displayInGameMenu();
			}
		} else {
			this.prevFrameTime = System.currentTimeMillis();
		}

		if(this.mc.inGameHasFocus) {
			this.mc.mouseHelper.mouseXYChange();
			float f2 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			float f3 = f2 * f2 * f2 * 8.0F;
			float f4 = (float)this.mc.mouseHelper.deltaX * f3;
			float f5 = (float)this.mc.mouseHelper.deltaY * f3;
			byte b6 = 1;
			if(this.mc.gameSettings.invertMouse) {
				b6 = -1;
			}

			if(this.mc.gameSettings.smoothCamera) {
				this.smoothCamYaw += f4;
				this.smoothCamPitch += f5;
				float f7 = f1 - this.smoothCamPartialTicks;
				this.smoothCamPartialTicks = f1;
				f4 = this.smoothCamFilterX * f7;
				f5 = this.smoothCamFilterY * f7;
				this.mc.thePlayer.setAngles(f4, f5 * (float)b6);
			} else {
				this.mc.thePlayer.setAngles(f4, f5 * (float)b6);
			}
		}

		if(!this.mc.skipRenderWorld) {
			anaglyphEnable = this.mc.gameSettings.anaglyph;
			ScaledResolution scaledResolution13 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
			int i14 = scaledResolution13.getScaledWidth();
			int i15 = scaledResolution13.getScaledHeight();
			int i16 = Mouse.getX() * i14 / this.mc.displayWidth;
			int i17 = i15 - Mouse.getY() * i15 / this.mc.displayHeight - 1;
			short s7 = 200;
			if(this.mc.gameSettings.limitFramerate == 1) {
				s7 = 120;
			}
			if(this.mc.gameSettings.limitFramerate == 2) {
				s7 = 40;
			}

			long j8;
			if(this.mc.theWorld != null) {
				if(this.mc.gameSettings.limitFramerate == 0) {
					this.renderWorld(f1, 0L);
				} else {
					this.renderWorld(f1, this.renderEndNanoTime + (long)(1000000000 / s7));
				}

				if(this.mc.gameSettings.limitFramerate == 2) {
					j8 = (this.renderEndNanoTime + (long)(1000000000 / s7) - System.nanoTime()) / 1000000L;
					if(j8 > 0L && j8 < 500L) {
						try {
							Thread.sleep(j8);
						} catch (InterruptedException interruptedException12) {
							interruptedException12.printStackTrace();
						}
					}
				}

				this.renderEndNanoTime = System.nanoTime();
				if(!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
					this.mc.ingameGUI.renderGameOverlay(f1, this.mc.currentScreen != null, i16, i17);
				}
			} else {
				GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
				this.setupOverlayRendering();
				if(this.mc.gameSettings.limitFramerate == 2) {
					j8 = (this.renderEndNanoTime + (long)(1000000000 / s7) - System.nanoTime()) / 1000000L;
					if(j8 < 0L) {
						j8 += 10L;
					}

					if(j8 > 0L && j8 < 500L) {
						try {
							Thread.sleep(j8);
						} catch (InterruptedException interruptedException11) {
							interruptedException11.printStackTrace();
						}
					}
				}

				this.renderEndNanoTime = System.nanoTime();
			}

			if(this.mc.currentScreen != null) {
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				this.mc.currentScreen.drawScreen(i16, i17, f1);
				if(this.mc.currentScreen != null && this.mc.currentScreen.guiParticles != null) {
					this.mc.currentScreen.guiParticles.draw(f1);
				}
			}

		}
	}

	public void renderWorld(float f1, long j2) {
		if(this.lightmapUpdateNeeded) {
			this.updateLightmap();
		}
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		if(this.mc.renderViewEntity == null) {
			this.mc.renderViewEntity = this.mc.thePlayer;
		}

		this.getMouseOver(f1);
		EntityLiving entityliving = this.mc.renderViewEntity;
		LevelRenderer renderglobal = this.mc.renderGlobal;
		EffectRenderer effectrenderer = this.mc.effectRenderer;
		double d7 = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)f1;
		double d9 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)f1;
		double d11 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)f1;

		for(int i18 = 0; i18 < 2; ++i18) {
			if(this.mc.gameSettings.anaglyph) {
				anaglyphField = i18;
				if(anaglyphField == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			this.updateFogColor(f1);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glEnable(GL11.GL_CULL_FACE);
			this.setupCameraTransform(f1, i18);
			ClippingHelperImpl.getInstance();
			if(!Config.isSkyEnabled() && !Config.isStarsEnabled()) {
				GL11.glDisable(GL11.GL_BLEND); 
			} else {
				this.setupFog(-1, f1);
				renderglobal.renderSky(f1);
			}

			GL11.glEnable(GL11.GL_FOG);
			this.setupFog(1, f1);
			if(this.mc.gameSettings.ambientOcclusion) {
				GL11.glShadeModel(GL11.GL_SMOOTH);
			}

			Frustrum frustrum19 = new Frustrum();
			frustrum19.setPosition(d7, d9, d11);
			this.mc.renderGlobal.clipRenderersByFrustum(frustrum19, f1);
			if(i18 == 0) {
				while(!this.mc.renderGlobal.updateRenderers(entityliving, false) && j2 != 0L) {
					long j20 = j2 - System.nanoTime();
					if(j20 < 0L || j20 > 1000000000L) {
						break;
					}
				}
			}

			this.setupFog(0, f1);
			GL11.glEnable(GL11.GL_FOG);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
			RenderHelper.disableStandardItemLighting();
			renderglobal.sortAndRender(entityliving, 0, (double)f1);
			GL11.glShadeModel(GL11.GL_FLAT);
			EntityPlayer entityPlayer21;
			RenderHelper.enableStandardItemLighting();
			
			// Entities
			renderglobal.renderEntities(entityliving.getPosition(f1), frustrum19, f1);
			
			// Lit particles
			this.enableLightmap((double)f1);				
			effectrenderer.func_1187_b(entityliving, f1);
			RenderHelper.disableStandardItemLighting();
		
			// Particles
			this.setupFog(0, f1);
			effectrenderer.renderParticles(entityliving, f1);
			this.disableLightmap((double)f1);
			
			if(this.mc.objectMouseOver != null && entityliving.isInsideOfMaterial(Material.water) && entityliving instanceof EntityPlayer) {
				entityPlayer21 = (EntityPlayer)entityliving;
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				
				// Outline
				renderglobal.drawBlockBreaking(entityPlayer21, this.mc.objectMouseOver, 0, entityPlayer21.inventory.getCurrentItem(), f1);
				renderglobal.drawSelectionBox(entityPlayer21, this.mc.objectMouseOver, 0, entityPlayer21.inventory.getCurrentItem(), f1);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_CULL_FACE);
			
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDepthMask(true);
			this.setupFog(0, f1);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
			
			// Water
			if(this.mc.gameSettings.fancyGraphics || this.mc.gameSettings.clearWaters) {
				if(this.mc.gameSettings.ambientOcclusion) {
					GL11.glShadeModel(GL11.GL_SMOOTH);
				}

				GL11.glColorMask(false, false, false, false);
				int i16 = renderglobal.renderAllSortedRenderers(1, (double)f1);
				if(this.mc.gameSettings.anaglyph) {
					if(anaglyphField == 0) {
						GL11.glColorMask(false, true, true, true);
					} else {
						GL11.glColorMask(true, false, false, true);
					}
				} else {
					GL11.glColorMask(true, true, true, true);
				}

				if(i16 > 0) {
					renderglobal.renderAllSortedRenderers(1, (double)f1);
				}

				GL11.glShadeModel(GL11.GL_FLAT);
			} else {
				renderglobal.renderAllSortedRenderers(1, (double)f1);
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			if(this.cameraZoom == 1.0D && entityliving instanceof EntityPlayer && this.mc.objectMouseOver != null && !entityliving.isInsideOfMaterial(Material.water)) {
				entityPlayer21 = (EntityPlayer)entityliving;
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				renderglobal.drawBlockBreaking(entityPlayer21, this.mc.objectMouseOver, 0, entityPlayer21.inventory.getCurrentItem(), f1);
				renderglobal.drawSelectionBox(entityPlayer21, this.mc.objectMouseOver, 0, entityPlayer21.inventory.getCurrentItem(), f1);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			// Weather
			if(
				!Config.isRainOff() && (
					this.mc.theWorld.rainingStrength > 0.0F || 
					this.mc.theWorld.snowingStrength > 0.0F
				)
			) {
				this.renderWeather(f1);
			}
			
			GL11.glDisable(GL11.GL_FOG);
			if(this.pointedEntity == null) {
				;
			}

			if(this.mc.gameSettings.shouldRenderClouds()) {
				// Clouds
			
				GL11.glPushMatrix();
				this.setupFog(0, f1);
				GL11.glEnable(GL11.GL_FOG);
				renderglobal.renderClouds(f1);
				GL11.glDisable(GL11.GL_FOG);
				this.setupFog(1, f1);
				GL11.glPopMatrix();
			}
			
			if(this.cameraZoom == 1.0D) {
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				this.renderHand(f1, i18);
			}

			if(!this.mc.gameSettings.anaglyph) {
				return;
			}
		}

		GL11.glColorMask(true, true, true, false);
	}

	private void addRainParticles() {
		World world = this.mc.theWorld;
		
		float f1 = this.mc.theWorld.getRainStrength(1.0F);
		if(!this.mc.gameSettings.fancyGraphics && !this.mc.gameSettings.clearWaters) {
			f1 /= 2.0F;
		}

		if(f1 != 0.0F) {
			this.random.setSeed((long)this.rendererUpdateCount * 312987231L);
			EntityLiving entityLiving2 = this.mc.renderViewEntity;
			World world3 = this.mc.theWorld;
			int i4 = MathHelper.floor_double(entityLiving2.posX);
			int i5 = MathHelper.floor_double(entityLiving2.posY);
			int i6 = MathHelper.floor_double(entityLiving2.posZ);
			byte b7 = 10;
			double d8 = 0.0D;
			double d10 = 0.0D;
			double d12 = 0.0D;
			int i14 = 0;

			for(int i15 = 0; i15 < (int)(100.0F * f1 * f1); ++i15) {
				int i16 = i4 + this.random.nextInt(b7) - this.random.nextInt(b7);
				int i17 = i6 + this.random.nextInt(b7) - this.random.nextInt(b7);
				
				BiomeGenBase biomegenbase = world.getBiomeGenAt(i16, i17);
				int particleType = Weather.particleDecide(biomegenbase, world);
				if(particleType != Weather.RAIN) continue;
				
				int i18 = world3.findTopSolidBlockUsingBlockMaterial(i16, i17);
				int i19 = world3.getBlockId(i16, i18 - 1, i17);
				if(i18 <= i5 + b7 && i18 >= i5 - b7 /*&& world3.getWorldChunkManager().getBiomeGenAt(i16, i17).canSpawnLightningBolt()*/) {
					float f20 = this.random.nextFloat();
					float f21 = this.random.nextFloat();
					if(i19 > 0) {
						if(Block.blocksList[i19].blockMaterial == Material.lava) {
							this.mc.effectRenderer.addEffect(new EntitySmokeFX(world3, (double)((float)i16 + f20), (double)((float)i18 + 0.1F) - Block.blocksList[i19].minY, (double)((float)i17 + f21), 0.0D, 0.0D, 0.0D));
						} else {
							++i14;
							if(this.random.nextInt(i14) == 0) {
								d8 = (double)((float)i16 + f20);
								d10 = (double)((float)i18 + 0.1F) - Block.blocksList[i19].minY;
								d12 = (double)((float)i17 + f21);
							}

							this.mc.effectRenderer.addEffect(new EntityRainFX(world3, (double)((float)i16 + f20), (double)((float)i18 + 0.1F) - Block.blocksList[i19].minY, (double)((float)i17 + f21)));
						}
					}
				}
			}

			if(i14 > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
				this.rainSoundCounter = 0;
				if(d10 > entityLiving2.posY + 1.0D && world3.findTopSolidBlockUsingBlockMaterial(MathHelper.floor_double(entityLiving2.posX), MathHelper.floor_double(entityLiving2.posZ)) > MathHelper.floor_double(entityLiving2.posY)) {
					this.mc.theWorld.playSoundEffect(d8, d10, d12, "ambient.weather.rain", 0.1F, 0.5F);
				} else {
					this.mc.theWorld.playSoundEffect(d8, d10, d12, "ambient.weather.rain", 0.2F, 1.0F);
				}
			}

		}
	}

	protected void renderWeather(float renderPartialTick) {
	
		this.enableLightmap((double)renderPartialTick);
		
		EntityLiving entityPlayerSP = this.mc.renderViewEntity;
		World world = this.mc.theWorld;
		float fRain = world.getRainStrength(renderPartialTick);
		float fSnow = world.getSnowStrength(renderPartialTick);
		
		// player block coordinates
		int playerX = MathHelper.floor_double(entityPlayerSP.posX);
		int playerY = MathHelper.floor_double(entityPlayerSP.posY);
		int playerZ = MathHelper.floor_double(entityPlayerSP.posZ);
		
		// Prepare tessellator & texture
		Tessellator tessellator = Tessellator.instance;
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.01F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/snow.png"));

		double interpolatedX = entityPlayerSP.lastTickPosX + (entityPlayerSP.posX - entityPlayerSP.lastTickPosX) * (double)renderPartialTick;
		double interpolatedY = entityPlayerSP.lastTickPosY + (entityPlayerSP.posY - entityPlayerSP.lastTickPosY) * (double)renderPartialTick;
		double interpolatedZ = entityPlayerSP.lastTickPosZ + (entityPlayerSP.posZ - entityPlayerSP.lastTickPosZ) * (double)renderPartialTick;

		byte radius = 12;

		byte lastParticle = -1;
		float f4 = (float)rendererUpdateCount + renderPartialTick;

		for(int x = playerX - radius; x <= playerX + radius; ++x) {
			for(int z = playerZ - radius; z <= playerZ + radius; ++z) {
				
				int idx = ((z - playerZ) + 16) * 32 + ((x - playerX) + 16);
				float distanceX = rainXCoords[idx] * 0.5F;
				float distanceZ = rainYCoords[idx] * 0.5F;				
				
				BiomeGenBase biomegenbase = world.getBiomeGenAt(x, z);
				int particleType = Weather.particleDecide(biomegenbase, world);
				if(particleType == 0) continue;
				int y = world.findTopSolidBlockUsingBlockMaterial(x, z);
				if(y < 0) {
					y = 0;
				}
				
				int y1 = playerY - radius;
				int y2 = playerY + radius;
				
				if(y1 < y) {
					y1 = y;
				}

				if(y2 < y) {
					y2 = y;
				}
				
				if(y1 != y2) {
					
					this.random.setSeed(x * x * 3121 + x * 0x2b24abb ^ z * z * 0x66397 + z * 13761);
					
					switch(particleType) {
						case Weather.RAIN:
							if(lastParticle != 0) {
								if(lastParticle > 0) {
									tessellator.draw();
								}
								
								lastParticle = 0;
								GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/environment/rain.png"));
								tessellator.startDrawingQuads();
							}
							
							float f9 = (((float)(rendererUpdateCount + x * x * 3121 + x * 0x2b24abb + z * z * 0x66397 + z * 13761 & 0x1f) + renderPartialTick) / 32F) * (3F + this.random.nextFloat());
							double ddX = (double)((float)x + 0.5F) - entityPlayerSP.posX;
							double ddZ = (double)((float)z + 0.5F) - entityPlayerSP.posZ;
							float hypotenuse = MathHelper.sqrt_double(ddX * ddX + ddZ * ddZ) / (float)radius;
							
							tessellator.setBrightness(world.getLightBrightnessForSkyBlocks(x, y, z, 0));
							tessellator.setColorRGBA_F(1, 1, 1, ((1.0F - hypotenuse * hypotenuse) * 0.5F + 0.5F) * fRain);
							
							tessellator.setTranslation(-interpolatedX * 1.0D, -interpolatedY * 1.0D, -interpolatedZ * 1.0D);
							tessellator.addVertexWithUV((double)((float)x - distanceX) + 0.5D, y1, (double)((float)z - distanceZ) + 0.5D, 0.0F, ((float)y1) / 4F + f9);
							tessellator.addVertexWithUV((double)((float)x + distanceX) + 0.5D, y1, (double)((float)z + distanceZ) + 0.5D, 1.0F, ((float)y1) / 4F + f9);
							tessellator.addVertexWithUV((double)((float)x + distanceX) + 0.5D, y2, (double)((float)z + distanceZ) + 0.5D, 1.0F, ((float)y2) / 4F + f9);
							tessellator.addVertexWithUV((double)((float)x - distanceX) + 0.5D, y2, (double)((float)z - distanceZ) + 0.5D, 0.0F, ((float)y2) / 4F + f9);
							tessellator.setTranslation(0.0D, 0.0D, 0.0D);
							break;
							
						case Weather.SNOW:
							if (lastParticle != 1)	{
								if (lastParticle >= 0)	{
									tessellator.draw();
								}

								lastParticle = 1;
								GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/environment/snow.png"));
								tessellator.startDrawingQuads();
							}
							
							// Sometimes (in cold biomes during winter) rain is substituted for snow
							if(fSnow < fRain) fSnow = fRain;
							
							float f10 = ((float)(rendererUpdateCount & 0x1ff) + renderPartialTick) / 512F;
							float f11 = this.random.nextFloat() + f4 * 0.01F * (float)this.random.nextGaussian();
							float f12 = this.random.nextFloat() + f4 * (float)this.random.nextGaussian() * 0.001F;
							double dddX = (double)((float)x + 0.5F) - entityPlayerSP.posX;
							double dddZ = (double)((float)z + 0.5F) - entityPlayerSP.posZ;
							float hypotenuse2 = MathHelper.sqrt_double(dddX * dddX + dddZ * dddZ) / (float)radius;
							
							tessellator.setBrightness(world.getLightBrightnessForSkyBlocks(x, y, z, 0));
							tessellator.setColorRGBA_F(1, 1, 1, ((1.0F - hypotenuse2 * hypotenuse2) * 0.3F + 0.5F) * fSnow);
							
							tessellator.setTranslation(-interpolatedX * 1.0D, -interpolatedY * 1.0D, -interpolatedZ * 1.0D);
							tessellator.addVertexWithUV((double)((float)x - distanceX) + 0.5D, y1, (double)((float)z - distanceZ) + 0.5D, 0.0F + f11, ((float)y1) / 4F + f10 + f12);
							tessellator.addVertexWithUV((double)((float)x + distanceX) + 0.5D, y1, (double)((float)z + distanceZ) + 0.5D, 1.0F + f11, ((float)y1) / 4F + f10 + f12);
							tessellator.addVertexWithUV((double)((float)x + distanceX) + 0.5D, y2, (double)((float)z + distanceZ) + 0.5D, 1.0F + f11, ((float)y2) / 4F + f10 + f12);
							tessellator.addVertexWithUV((double)((float)x - distanceX) + 0.5D, y2, (double)((float)z - distanceZ) + 0.5D, 0.0F + f11, ((float)y2) / 4F + f10 + f12);
							tessellator.setTranslation(0.0D, 0.0D, 0.0D);
							break;

					}
				}
			}
		}
		
		if (lastParticle >= 0)	{
			tessellator.draw();
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		this.disableLightmap((double)renderPartialTick);
		
	}

	public void setupOverlayRendering() {
		ScaledResolution scaledResolution1 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, scaledResolution1.scaledWidthD, scaledResolution1.scaledHeightD, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
	}

	private void updateFogColor(float f1) {
		World world2 = this.mc.theWorld;
		EntityLiving entityLiving3 = this.mc.renderViewEntity;
		float f4 = 1.0F / (float)(4 - this.mc.gameSettings.renderDistance);
		f4 = 1.0F - (float)Math.pow((double)f4, 0.25D);
		Vec3D vec3D5 = world2.getSkyColor(this.mc.renderViewEntity, f1);
		float f6 = (float)vec3D5.xCoord;
		float f7 = (float)vec3D5.yCoord;
		float f8 = (float)vec3D5.zCoord;
		Vec3D vec3D9 = world2.getFogColor(f1, entityLiving3);
		this.fogColorRed = (float)vec3D9.xCoord;
		this.fogColorGreen = (float)vec3D9.yCoord;
		this.fogColorBlue = (float)vec3D9.zCoord;
		this.fogColorRed += (f6 - this.fogColorRed) * f4;
		this.fogColorGreen += (f7 - this.fogColorGreen) * f4;
		this.fogColorBlue += (f8 - this.fogColorBlue) * f4;
		float f10 = world2.getRainStrength(f1);
		float f11;
		float f12;
		if(f10 > 0.0F) {
			f11 = 1.0F - f10 * 0.5F;
			f12 = 1.0F - f10 * 0.4F;
			this.fogColorRed *= f11;
			this.fogColorGreen *= f11;
			this.fogColorBlue *= f12;
		}

		f11 = world2.getWeightedThunderStrength(f1);
		if(f11 > 0.0F) {
			f12 = 1.0F - f11 * 0.5F;
			this.fogColorRed *= f12;
			this.fogColorGreen *= f12;
			this.fogColorBlue *= f12;
		}

		if(this.cloudFog) {
			Vec3D vec3D16 = world2.getCloudColor(f1, entityLiving3);
			this.fogColorRed = (float)vec3D16.xCoord;
			this.fogColorGreen = (float)vec3D16.yCoord;
			this.fogColorBlue = (float)vec3D16.zCoord;
		} else if(entityLiving3.isInsideOfMaterial(Material.water)) {
			this.fogColorRed = 0.02F;
			this.fogColorGreen = 0.02F;
			this.fogColorBlue = 0.2F;
		} else if(entityLiving3.isInsideOfMaterial(Material.acid)) {
			this.fogColorRed = 0.02F;
			this.fogColorGreen = 0.2F;
			this.fogColorBlue = 0.02F;
		} else if(entityLiving3.isInsideOfMaterial(Material.lava)) {
			this.fogColorRed = 0.6F;
			this.fogColorGreen = 0.1F;
			this.fogColorBlue = 0.0F;
		}
		
		// Blindness, ported back from r1.2.5
		double d14 = (entityLiving3.lastTickPosY + (entityLiving3.posY - entityLiving3.lastTickPosY) * (double)f1);
		if(entityLiving3.isStatusActive(Status.statusBlind)) {
			int i16 = entityLiving3.getActiveStatusEffect(Status.statusBlind).duration;
			if(i16 < 20) {
				d14 *= (double)(1.0F - (float)i16 / 20.0F);
			} else {
				d14 = 0.0D;
			}
		}

		if(d14 < 1.0D) {
			if(d14 < 0.0D) {
				d14 = 0.0D;
			}

			d14 *= d14;
			this.fogColorRed = (float)((double)this.fogColorRed * d14);
			this.fogColorGreen = (float)((double)this.fogColorGreen * d14);
			this.fogColorBlue = (float)((double)this.fogColorBlue * d14);
		}
		
		f12 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * f1;
		this.fogColorRed *= f12;
		this.fogColorGreen *= f12;
		this.fogColorBlue *= f12;
		
		// Night vision, ported back from r1.5.2
		if(((EntityPlayer)entityLiving3).divingHelmetOn()) {
			float nVB = 0.5F;
			
			float fNV = 1.0F / this.fogColorRed;
			if(fNV > 1.0F / this.fogColorGreen) fNV = 1.0F / this.fogColorGreen;
			if(fNV > 1.0F / this.fogColorBlue) fNV = 1.0F / this.fogColorBlue;
			
			this.fogColorRed   = this.fogColorRed   * (1.0F - nVB) + this.fogColorRed   * fNV * nVB;
			this.fogColorGreen = this.fogColorGreen * (1.0F - nVB) + this.fogColorGreen * fNV * nVB;
			this.fogColorBlue  = this.fogColorBlue  * (1.0F - nVB) + this.fogColorBlue  * fNV * nVB;
		}

		if(this.mc.gameSettings.anaglyph) {
			float f13 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
			float f14 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
			float f15 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
			this.fogColorRed = f13;
			this.fogColorGreen = f14;
			this.fogColorBlue = f15;
		}

		GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
	}

	private void setupFog(int i1, float f2) {
		EntityLiving entityLiving3 = this.mc.renderViewEntity;
		
		GL11.glFog(GL11.GL_FOG_COLOR, this.func_908_a(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
		GL11.glNormal3f(0.0F, -1.0F, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		// Blindness, ported back from r1.2.5
		if(entityLiving3.isStatusActive(Status.statusBlind)) {
			float f6 = 5.0F;
			int i7 = entityLiving3.getActiveStatusEffect(Status.statusBlind).duration;
			if(i7 < 20) {
				f6 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - (float)i7 / 20.0F);
			}

			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
			if(i1 < 0) {
				GL11.glFogf(GL11.GL_FOG_START, 0.0F);
				GL11.glFogf(GL11.GL_FOG_END, f6 * 0.8F);
			} else {
				GL11.glFogf(GL11.GL_FOG_START, f6 * 0.25F);
				GL11.glFogf(GL11.GL_FOG_END, f6);
			}

			if(GLContext.getCapabilities().GL_NV_fog_distance) {
				GL11.glFogi(34138, 34139);
			}
		} else {
			
			if(this.cloudFog) {
				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
				GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
				if(this.mc.gameSettings.anaglyph) {
				}
			} else if(entityLiving3.isInsideOfMaterial(Material.water) && !((EntityPlayer)entityLiving3).divingHelmetOn()) {
				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
				GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
				if(this.mc.gameSettings.anaglyph) {
				}
			} else if(entityLiving3.isInsideOfMaterial(Material.acid)) {
				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
				GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
				if(this.mc.gameSettings.anaglyph) {
				}
			} else if(entityLiving3.isInsideOfMaterial(Material.lava)) {
				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
				GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
				if(this.mc.gameSettings.anaglyph) {
				}
			} else {
				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
				GL11.glFogf(GL11.GL_FOG_START, this.farPlaneDistance * 0.25F);
				GL11.glFogf(GL11.GL_FOG_END, this.farPlaneDistance);
				if(i1 < 0) {
					GL11.glFogf(GL11.GL_FOG_START, 0.0F);
					GL11.glFogf(GL11.GL_FOG_END, this.farPlaneDistance * 0.8F);
				}
	
				if(GLContext.getCapabilities().GL_NV_fog_distance) {
					GL11.glFogi(34138, 34139);
				}
	
				if(this.mc.theWorld.worldProvider.isNether) {
					GL11.glFogf(GL11.GL_FOG_START, 0.0F);
				}
			}
		}
		
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
	}

	private FloatBuffer func_908_a(float f1, float f2, float f3, float f4) {
		this.fogColorBuffer.clear();
		this.fogColorBuffer.put(f1).put(f2).put(f3).put(f4);
		this.fogColorBuffer.flip();
		return this.fogColorBuffer;
	}
}
