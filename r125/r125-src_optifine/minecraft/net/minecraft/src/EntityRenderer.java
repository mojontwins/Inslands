package net.minecraft.src;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public class EntityRenderer {
	public static boolean anaglyphEnable = false;
	public static int anaglyphField;
	private Minecraft mc;
	private float farPlaneDistance = 0.0F;
	public ItemRenderer itemRenderer;
	private int rendererUpdateCount;
	private Entity pointedEntity = null;
	private MouseFilter mouseFilterXAxis = new MouseFilter();
	private MouseFilter mouseFilterYAxis = new MouseFilter();
	private MouseFilter mouseFilterDummy1 = new MouseFilter();
	private MouseFilter mouseFilterDummy2 = new MouseFilter();
	private MouseFilter mouseFilterDummy3 = new MouseFilter();
	private MouseFilter mouseFilterDummy4 = new MouseFilter();
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
	private float fovModifierHand;
	private float fovModifierHandPrev;
	private float fovMultiplierTemp;
	private boolean cloudFog = false;
	private double cameraZoom = 1.0D;
	private double cameraYaw = 0.0D;
	private double cameraPitch = 0.0D;
	private long prevFrameTime = System.currentTimeMillis();
	private long renderEndNanoTime = 0L;
	private boolean lightmapUpdateNeeded = false;
	float torchFlickerX = 0.0F;
	float torchFlickerDX = 0.0F;
	float torchFlickerY = 0.0F;
	float torchFlickerDY = 0.0F;
	private Random random = new Random();
	private int rainSoundCounter = 0;
	float[] rainXCoords;
	float[] rainYCoords;
	volatile int field_1394_b = 0;
	volatile int field_1393_c = 0;
	FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
	float fogColorRed;
	float fogColorGreen;
	float fogColorBlue;
	private float fogColor2;
	private float fogColor1;
	public int debugViewDirection;
	private World updatedWorld = null;
	private boolean showDebugInfo = false;
	private boolean zoomMode = false;
	private boolean fullscreenModeChecked = false;
	private boolean desktopModeChecked = false;
	private String lastTexturePack = null;

	public EntityRenderer(Minecraft par1Minecraft) {
		this.mc = par1Minecraft;
		this.itemRenderer = new ItemRenderer(par1Minecraft);
		this.lightmapTexture = par1Minecraft.renderEngine.allocateAndSetupTexture(new BufferedImage(16, 16, 1));
		this.lightmapColors = new int[256];
	}

	public void updateRenderer() {
		this.updateFovModifierHand();
		this.updateTorchFlicker();
		this.fogColor2 = this.fogColor1;
		this.thirdPersonDistanceTemp = this.thirdPersonDistance;
		this.prevDebugCamYaw = this.debugCamYaw;
		this.prevDebugCamPitch = this.debugCamPitch;
		this.prevDebugCamFOV = this.debugCamFOV;
		this.prevCamRoll = this.camRoll;
		float f1;
		float f3;
		if(this.mc.gameSettings.smoothCamera) {
			f1 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			f3 = f1 * f1 * f1 * 8.0F;
			this.smoothCamFilterX = this.mouseFilterXAxis.func_22386_a(this.smoothCamYaw, 0.05F * f3);
			this.smoothCamFilterY = this.mouseFilterYAxis.func_22386_a(this.smoothCamPitch, 0.05F * f3);
			this.smoothCamPartialTicks = 0.0F;
			this.smoothCamYaw = 0.0F;
			this.smoothCamPitch = 0.0F;
		}

		if(this.mc.renderViewEntity == null) {
			this.mc.renderViewEntity = this.mc.thePlayer;
		}

		f1 = this.mc.theWorld.getLightBrightness(MathHelper.floor_double(this.mc.renderViewEntity.posX), MathHelper.floor_double(this.mc.renderViewEntity.posY), MathHelper.floor_double(this.mc.renderViewEntity.posZ));
		f3 = (float)(3 - this.mc.gameSettings.renderDistance) / 3.0F;
		float f4 = f1 * (1.0F - f3) + f3;
		this.fogColor1 += (f4 - this.fogColor1) * 0.1F;
		++this.rendererUpdateCount;
		this.itemRenderer.updateEquippedItem();
		this.addRainParticles();
	}

	public void getMouseOver(float par1) {
		if(this.mc.renderViewEntity != null) {
			if(this.mc.theWorld != null) {
				double d = (double)this.mc.playerController.getBlockReachDistance();
				this.mc.objectMouseOver = this.mc.renderViewEntity.rayTrace(d, par1);
				double d1 = d;
				Vec3D vec3d = this.mc.renderViewEntity.getPosition(par1);
				if(this.mc.playerController.extendedReach()) {
					d = 6.0D;
					d1 = 6.0D;
				} else {
					if(d > 3.0D) {
						d1 = 3.0D;
					}

					d = d1;
				}

				if(this.mc.objectMouseOver != null) {
					d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3d);
				}

				Vec3D vec3d1 = this.mc.renderViewEntity.getLook(par1);
				Vec3D vec3d2 = vec3d.addVector(vec3d1.xCoord * d, vec3d1.yCoord * d, vec3d1.zCoord * d);
				this.pointedEntity = null;
				float f = 1.0F;
				List list = this.mc.theWorld.getEntitiesWithinAABBExcludingEntity(this.mc.renderViewEntity, this.mc.renderViewEntity.boundingBox.addCoord(vec3d1.xCoord * d, vec3d1.yCoord * d, vec3d1.zCoord * d).expand((double)f, (double)f, (double)f));
				double d2 = d1;

				for(int i = 0; i < list.size(); ++i) {
					Entity entity = (Entity)list.get(i);
					if(entity.canBeCollidedWith()) {
						float f1 = entity.getCollisionBorderSize();
						AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double)f1, (double)f1, (double)f1);
						MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3d, vec3d2);
						if(axisalignedbb.isVecInside(vec3d)) {
							if(0.0D < d2 || d2 == 0.0D) {
								this.pointedEntity = entity;
								d2 = 0.0D;
							}
						} else if(movingobjectposition != null) {
							double d3 = vec3d.distanceTo(movingobjectposition.hitVec);
							if(d3 < d2 || d2 == 0.0D) {
								this.pointedEntity = entity;
								d2 = d3;
							}
						}
					}
				}

				if(this.pointedEntity != null && (d2 < d1 || this.mc.objectMouseOver == null)) {
					this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity);
				}

			}
		}
	}

	private void updateFovModifierHand() {
		if(this.mc.renderViewEntity instanceof EntityPlayerSP) {
			EntityPlayerSP entityplayersp = (EntityPlayerSP)this.mc.renderViewEntity;
			this.fovMultiplierTemp = entityplayersp.getFOVMultiplier();
		} else {
			this.fovMultiplierTemp = this.mc.thePlayer.getFOVMultiplier();
		}

		this.fovModifierHandPrev = this.fovModifierHand;
		this.fovModifierHand += (this.fovMultiplierTemp - this.fovModifierHand) * 0.5F;
	}

	private float getFOVModifier(float par1, boolean par2) {
		if(this.debugViewDirection > 0) {
			return 90.0F;
		} else {
			EntityLiving entityplayer = this.mc.renderViewEntity;
			float f = 70.0F;
			if(par2) {
				f += this.mc.gameSettings.fovSetting * 40.0F;
				f *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * par1;
			}

			boolean zoomActive = false;
			if(this.mc.gameSettings.ofKeyBindZoom.keyCode < 0) {
				zoomActive = Mouse.isButtonDown(this.mc.gameSettings.ofKeyBindZoom.keyCode + 100);
			} else {
				zoomActive = Keyboard.isKeyDown(this.mc.gameSettings.ofKeyBindZoom.keyCode);
			}

			if(zoomActive) {
				if(!this.zoomMode) {
					this.zoomMode = true;
					this.mc.gameSettings.smoothCamera = true;
				}

				if(this.zoomMode) {
					f /= 4.0F;
				}
			} else if(this.zoomMode) {
				this.zoomMode = false;
				this.mc.gameSettings.smoothCamera = false;
				this.mouseFilterXAxis = new MouseFilter();
				this.mouseFilterYAxis = new MouseFilter();
			}

			if(entityplayer.getHealth() <= 0) {
				float i = (float)entityplayer.deathTime + par1;
				f /= (1.0F - 500.0F / (i + 500.0F)) * 2.0F + 1.0F;
			}

			int i1 = ActiveRenderInfo.getBlockIdAtEntityViewpoint(this.mc.theWorld, entityplayer, par1);
			if(i1 != 0 && Block.blocksList[i1].blockMaterial == Material.water) {
				f = f * 60.0F / 70.0F;
			}

			return f + this.prevDebugCamFOV + (this.debugCamFOV - this.prevDebugCamFOV) * par1;
		}
	}

	private void hurtCameraEffect(float par1) {
		EntityLiving entityliving = this.mc.renderViewEntity;
		float f = (float)entityliving.hurtTime - par1;
		float f2;
		if(entityliving.getHealth() <= 0) {
			f2 = (float)entityliving.deathTime + par1;
			GL11.glRotatef(40.0F - 8000.0F / (f2 + 200.0F), 0.0F, 0.0F, 1.0F);
		}

		if(f >= 0.0F) {
			f /= (float)entityliving.maxHurtTime;
			f = MathHelper.sin(f * f * f * f * (float)Math.PI);
			f2 = entityliving.attackedAtYaw;
			GL11.glRotatef(-f2, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-f * 14.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(f2, 0.0F, 1.0F, 0.0F);
		}
	}

	private void setupViewBobbing(float par1) {
		if(this.mc.renderViewEntity instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer)this.mc.renderViewEntity;
			float f = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
			float f1 = -(entityplayer.distanceWalkedModified + f * par1);
			float f2 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * par1;
			float f3 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * par1;
			GL11.glTranslatef(MathHelper.sin(f1 * (float)Math.PI) * f2 * 0.5F, -Math.abs(MathHelper.cos(f1 * (float)Math.PI) * f2), 0.0F);
			GL11.glRotatef(MathHelper.sin(f1 * (float)Math.PI) * f2 * 3.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(Math.abs(MathHelper.cos(f1 * (float)Math.PI - 0.2F) * f2) * 5.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(f3, 1.0F, 0.0F, 0.0F);
		}
	}

	private void orientCamera(float par1) {
		EntityLiving entityliving = this.mc.renderViewEntity;
		float f = entityliving.yOffset - 1.62F;
		double d = entityliving.prevPosX + (entityliving.posX - entityliving.prevPosX) * (double)par1;
		double d1 = entityliving.prevPosY + (entityliving.posY - entityliving.prevPosY) * (double)par1 - (double)f;
		double d2 = entityliving.prevPosZ + (entityliving.posZ - entityliving.prevPosZ) * (double)par1;
		GL11.glRotatef(this.prevCamRoll + (this.camRoll - this.prevCamRoll) * par1, 0.0F, 0.0F, 1.0F);
		if(entityliving.isPlayerSleeping()) {
			f = (float)((double)f + 1.0D);
			GL11.glTranslatef(0.0F, 0.3F, 0.0F);
			if(!this.mc.gameSettings.debugCamEnable) {
				int d3 = this.mc.theWorld.getBlockId(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posY), MathHelper.floor_double(entityliving.posZ));
				boolean isForgeBed = false;
				int f2;
				int f4;
				if(Reflector.hasMethod(51)) {
					f2 = MathHelper.floor_double(entityliving.posX);
					f4 = MathHelper.floor_double(entityliving.posY);
					int d4 = MathHelper.floor_double(entityliving.posZ);
					Block block = Block.blocksList[this.mc.theWorld.getBlockId(f2, f4, d4)];
					isForgeBed = Reflector.callBoolean(block, 51, new Object[]{this.mc.theWorld, f2, f4, d4, entityliving});
				}

				if(d3 == Block.bed.blockID || isForgeBed) {
					f2 = this.mc.theWorld.getBlockMetadata(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posY), MathHelper.floor_double(entityliving.posZ));
					f4 = f2 & 3;
					GL11.glRotatef((float)(f4 * 90), 0.0F, 1.0F, 0.0F);
				}

				GL11.glRotatef(entityliving.prevRotationYaw + (entityliving.rotationYaw - entityliving.prevRotationYaw) * par1 + 180.0F, 0.0F, -1.0F, 0.0F);
				GL11.glRotatef(entityliving.prevRotationPitch + (entityliving.rotationPitch - entityliving.prevRotationPitch) * par1, -1.0F, 0.0F, 0.0F);
			}
		} else if(this.mc.gameSettings.thirdPersonView > 0) {
			double d27 = (double)(this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * par1);
			float f28;
			float f29;
			if(this.mc.gameSettings.debugCamEnable) {
				f28 = this.prevDebugCamYaw + (this.debugCamYaw - this.prevDebugCamYaw) * par1;
				f29 = this.prevDebugCamPitch + (this.debugCamPitch - this.prevDebugCamPitch) * par1;
				GL11.glTranslatef(0.0F, 0.0F, (float)(-d27));
				GL11.glRotatef(f29, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(f28, 0.0F, 1.0F, 0.0F);
			} else {
				f28 = entityliving.rotationYaw;
				f29 = entityliving.rotationPitch;
				if(this.mc.gameSettings.thirdPersonView == 2) {
					f29 += 180.0F;
				}

				double d30 = (double)(-MathHelper.sin(f28 / 180.0F * (float)Math.PI) * MathHelper.cos(f29 / 180.0F * (float)Math.PI)) * d27;
				double d5 = (double)(MathHelper.cos(f28 / 180.0F * (float)Math.PI) * MathHelper.cos(f29 / 180.0F * (float)Math.PI)) * d27;
				double d6 = (double)(-MathHelper.sin(f29 / 180.0F * (float)Math.PI)) * d27;

				for(int l = 0; l < 8; ++l) {
					float f5 = (float)((l & 1) * 2 - 1);
					float f6 = (float)((l >> 1 & 1) * 2 - 1);
					float f7 = (float)((l >> 2 & 1) * 2 - 1);
					f5 *= 0.1F;
					f6 *= 0.1F;
					f7 *= 0.1F;
					MovingObjectPosition movingobjectposition = this.mc.theWorld.rayTraceBlocks(Vec3D.createVector(d + (double)f5, d1 + (double)f6, d2 + (double)f7), Vec3D.createVector(d - d30 + (double)f5 + (double)f7, d1 - d6 + (double)f6, d2 - d5 + (double)f7));
					if(movingobjectposition != null) {
						double d7 = movingobjectposition.hitVec.distanceTo(Vec3D.createVector(d, d1, d2));
						if(d7 < d27) {
							d27 = d7;
						}
					}
				}

				if(this.mc.gameSettings.thirdPersonView == 2) {
					GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				}

				GL11.glRotatef(entityliving.rotationPitch - f29, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(entityliving.rotationYaw - f28, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(0.0F, 0.0F, (float)(-d27));
				GL11.glRotatef(f28 - entityliving.rotationYaw, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(f29 - entityliving.rotationPitch, 1.0F, 0.0F, 0.0F);
			}
		} else {
			GL11.glTranslatef(0.0F, 0.0F, -0.1F);
		}

		if(!this.mc.gameSettings.debugCamEnable) {
			GL11.glRotatef(entityliving.prevRotationPitch + (entityliving.rotationPitch - entityliving.prevRotationPitch) * par1, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(entityliving.prevRotationYaw + (entityliving.rotationYaw - entityliving.prevRotationYaw) * par1 + 180.0F, 0.0F, 1.0F, 0.0F);
		}

		GL11.glTranslatef(0.0F, f, 0.0F);
		d = entityliving.prevPosX + (entityliving.posX - entityliving.prevPosX) * (double)par1;
		d1 = entityliving.prevPosY + (entityliving.posY - entityliving.prevPosY) * (double)par1 - (double)f;
		d2 = entityliving.prevPosZ + (entityliving.posZ - entityliving.prevPosZ) * (double)par1;
		this.cloudFog = this.mc.renderGlobal.func_27307_a(d, d1, d2, par1);
	}

	private void setupCameraTransform(float par1, int par2) {
		this.farPlaneDistance = (float)(32 << 3 - this.mc.gameSettings.renderDistance);
		this.farPlaneDistance = (float)this.mc.gameSettings.ofRenderDistanceFine;
		if(Config.isFogFancy()) {
			this.farPlaneDistance *= 0.95F;
		}

		if(Config.isFogFast()) {
			this.farPlaneDistance *= 0.83F;
		}

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		float f = 0.07F;
		if(this.mc.gameSettings.anaglyph) {
			GL11.glTranslatef((float)(-(par2 * 2 - 1)) * f, 0.0F, 0.0F);
		}

		float clipDistance = this.farPlaneDistance * 2.0F;
		if(clipDistance < 128.0F) {
			clipDistance = 128.0F;
		}

		if(this.cameraZoom != 1.0D) {
			GL11.glTranslatef((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0F);
			GL11.glScaled(this.cameraZoom, this.cameraZoom, 1.0D);
		}

		GLU.gluPerspective(this.getFOVModifier(par1, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, clipDistance);
		float f2;
		if(this.mc.playerController.func_35643_e()) {
			f2 = 0.6666667F;
			GL11.glScalef(1.0F, f2, 1.0F);
		}

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		if(this.mc.gameSettings.anaglyph) {
			GL11.glTranslatef((float)(par2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
		}

		this.hurtCameraEffect(par1);
		if(this.mc.gameSettings.viewBobbing) {
			this.setupViewBobbing(par1);
		}

		f2 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * par1;
		if(f2 > 0.0F) {
			byte j = 20;
			if(this.mc.thePlayer.isPotionActive(Potion.confusion)) {
				j = 7;
			}

			float f3 = 5.0F / (f2 * f2 + 5.0F) - f2 * 0.04F;
			f3 *= f3;
			GL11.glRotatef(((float)this.rendererUpdateCount + par1) * (float)j, 0.0F, 1.0F, 1.0F);
			GL11.glScalef(1.0F / f3, 1.0F, 1.0F);
			GL11.glRotatef(-((float)this.rendererUpdateCount + par1) * (float)j, 0.0F, 1.0F, 1.0F);
		}

		this.orientCamera(par1);
		if(this.debugViewDirection > 0) {
			int j1 = this.debugViewDirection - 1;
			if(j1 == 1) {
				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			}

			if(j1 == 2) {
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			}

			if(j1 == 3) {
				GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			}

			if(j1 == 4) {
				GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			}

			if(j1 == 5) {
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
			}
		}

	}

	private void renderHand(float par1, int par2) {
		if(this.debugViewDirection <= 0) {
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			float f = 0.07F;
			if(this.mc.gameSettings.anaglyph) {
				GL11.glTranslatef((float)(-(par2 * 2 - 1)) * f, 0.0F, 0.0F);
			}

			if(this.cameraZoom != 1.0D) {
				GL11.glTranslatef((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0F);
				GL11.glScaled(this.cameraZoom, this.cameraZoom, 1.0D);
			}

			GLU.gluPerspective(this.getFOVModifier(par1, false), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
			if(this.mc.playerController.func_35643_e()) {
				float f1 = 0.6666667F;
				GL11.glScalef(1.0F, f1, 1.0F);
			}

			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			if(this.mc.gameSettings.anaglyph) {
				GL11.glTranslatef((float)(par2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
			}

			GL11.glPushMatrix();
			this.hurtCameraEffect(par1);
			if(this.mc.gameSettings.viewBobbing) {
				this.setupViewBobbing(par1);
			}

			if(this.mc.gameSettings.thirdPersonView == 0 && !this.mc.renderViewEntity.isPlayerSleeping() && !this.mc.gameSettings.hideGUI && !this.mc.playerController.func_35643_e()) {
				this.enableLightmap((double)par1);
				this.itemRenderer.renderItemInFirstPerson(par1);
				this.disableLightmap((double)par1);
			}

			GL11.glPopMatrix();
			if(this.mc.gameSettings.thirdPersonView == 0 && !this.mc.renderViewEntity.isPlayerSleeping()) {
				this.itemRenderer.renderOverlays(par1);
				this.hurtCameraEffect(par1);
			}

			if(this.mc.gameSettings.viewBobbing) {
				this.setupViewBobbing(par1);
			}

		}
	}

	public void disableLightmap(double par1) {
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	public void enableLightmap(double par1) {
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glLoadIdentity();
		float f = 0.00390625F;
		GL11.glScalef(f, f, f);
		GL11.glTranslatef(8.0F, 8.0F, 8.0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		this.mc.renderEngine.bindTexture(this.lightmapTexture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	private void updateTorchFlicker() {
		this.torchFlickerDX = (float)((double)this.torchFlickerDX + (Math.random() - Math.random()) * Math.random() * Math.random());
		this.torchFlickerDY = (float)((double)this.torchFlickerDY + (Math.random() - Math.random()) * Math.random() * Math.random());
		this.torchFlickerDX = (float)((double)this.torchFlickerDX * 0.9D);
		this.torchFlickerDY = (float)((double)this.torchFlickerDY * 0.9D);
		this.torchFlickerX += (this.torchFlickerDX - this.torchFlickerX) * 1.0F;
		this.torchFlickerY += (this.torchFlickerDY - this.torchFlickerY) * 1.0F;
		this.lightmapUpdateNeeded = true;
	}

	private void updateLightmap() {
		World world = this.mc.theWorld;
		if(world != null) {
			if(CustomColorizer.updateLightmap(world, this, this.lightmapColors)) {
				this.mc.renderEngine.createTextureFromBytes(this.lightmapColors, 16, 16, this.lightmapTexture);
			} else {
				for(int i = 0; i < 256; ++i) {
					float f = world.func_35464_b(1.0F) * 0.95F + 0.05F;
					float f1 = world.worldProvider.lightBrightnessTable[i / 16] * f;
					float f2 = world.worldProvider.lightBrightnessTable[i % 16] * (this.torchFlickerX * 0.1F + 1.5F);
					if(world.lightningFlash > 0) {
						f1 = world.worldProvider.lightBrightnessTable[i / 16];
					}

					float f3 = f1 * (world.func_35464_b(1.0F) * 0.65F + 0.35F);
					float f4 = f1 * (world.func_35464_b(1.0F) * 0.65F + 0.35F);
					float f7 = f2 * ((f2 * 0.6F + 0.4F) * 0.6F + 0.4F);
					float f8 = f2 * (f2 * f2 * 0.6F + 0.4F);
					float f9 = f3 + f2;
					float f10 = f4 + f7;
					float f11 = f1 + f8;
					f9 = f9 * 0.96F + 0.03F;
					f10 = f10 * 0.96F + 0.03F;
					f11 = f11 * 0.96F + 0.03F;
					if(world.worldProvider.worldType == 1) {
						f9 = 0.22F + f2 * 0.75F;
						f10 = 0.28F + f7 * 0.75F;
						f11 = 0.25F + f8 * 0.75F;
					}

					float f12 = this.mc.gameSettings.gammaSetting;
					if(f9 > 1.0F) {
						f9 = 1.0F;
					}

					if(f10 > 1.0F) {
						f10 = 1.0F;
					}

					if(f11 > 1.0F) {
						f11 = 1.0F;
					}

					float f13 = 1.0F - f9;
					float f14 = 1.0F - f10;
					float f15 = 1.0F - f11;
					f13 = 1.0F - f13 * f13 * f13 * f13;
					f14 = 1.0F - f14 * f14 * f14 * f14;
					f15 = 1.0F - f15 * f15 * f15 * f15;
					f9 = f9 * (1.0F - f12) + f13 * f12;
					f10 = f10 * (1.0F - f12) + f14 * f12;
					f11 = f11 * (1.0F - f12) + f15 * f12;
					f9 = f9 * 0.96F + 0.03F;
					f10 = f10 * 0.96F + 0.03F;
					f11 = f11 * 0.96F + 0.03F;
					if(f9 > 1.0F) {
						f9 = 1.0F;
					}

					if(f10 > 1.0F) {
						f10 = 1.0F;
					}

					if(f11 > 1.0F) {
						f11 = 1.0F;
					}

					if(f9 < 0.0F) {
						f9 = 0.0F;
					}

					if(f10 < 0.0F) {
						f10 = 0.0F;
					}

					if(f11 < 0.0F) {
						f11 = 0.0F;
					}

					short c = 255;
					int j = (int)(f9 * 255.0F);
					int k = (int)(f10 * 255.0F);
					int l = (int)(f11 * 255.0F);
					this.lightmapColors[i] = c << 24 | j << 16 | k << 8 | l;
				}

				this.mc.renderEngine.createTextureFromBytes(this.lightmapColors, 16, 16, this.lightmapTexture);
			}
		}
	}

	public void updateCameraAndRender(float par1) {
		Profiler.startSection("lightTex");
		World world = this.mc.theWorld;
		this.checkDisplayMode();
		if(world != null && Config.getNewRelease() != null) {
			String targetPrio = "HD_U " + Config.getNewRelease();
			this.mc.ingameGUI.addChatMessage("A new \u00a7eOptiFine\u00a7f version is available: \u00a7e" + targetPrio + "\u00a7f");
			Config.setNewRelease((String)null);
		}

		if(this.mc.currentScreen instanceof GuiMainMenu) {
			this.updateMainMenu((GuiMainMenu)this.mc.currentScreen);
		}

		if(this.updatedWorld != world) {
			RandomMobs.worldChanged(this.updatedWorld, world);
			this.updatedWorld = world;
		}

		Profiler.profilerGlobalEnabled = this.mc.gameSettings.ofProfiler;
		byte targetPrio1 = 10;
		if(this.mc.gameSettings.ofSmoothInput) {
			targetPrio1 = 5;
		}

		if(Thread.currentThread().getPriority() != targetPrio1) {
			Thread.currentThread().setPriority(targetPrio1);
		}

		Minecraft.hasPaidCheckTime = 0L;
		if(this.lastTexturePack == null) {
			this.lastTexturePack = this.mc.texturePackList.selectedTexturePack.texturePackFileName;
		}

		if(!this.lastTexturePack.equals(this.mc.texturePackList.selectedTexturePack.texturePackFileName)) {
			this.mc.renderGlobal.loadRenderers();
			this.lastTexturePack = this.mc.texturePackList.selectedTexturePack.texturePackFileName;
		}

		RenderBlocks.fancyGrass = Config.isGrassFancy() || Config.isBetterGrassFancy();
		Block.leaves.setGraphicsLevel(Config.isTreesFancy());
		if(Config.getIconWidthTerrain() > 0 && !(this.itemRenderer instanceof ItemRendererHD)) {
			this.itemRenderer = new ItemRendererHD(this.mc);
			RenderManager.instance.itemRenderer = this.itemRenderer;
		}

		if(world != null) {
			world.autosavePeriod = this.mc.gameSettings.ofAutoSaveTicks;
		}

		if(!Config.isWeatherEnabled() && world != null && world.worldInfo != null) {
			world.worldInfo.setRaining(false);
		}

		if(world != null && !world.isRemote && world.worldInfo != null && world.worldInfo.getGameType() == 1) {
			long scaledresolution = world.getWorldTime();
			long j = scaledresolution % 24000L;
			if(Config.isTimeDayOnly()) {
				if(j <= 1000L) {
					world.setWorldTime(scaledresolution - j + 1001L);
				}

				if(j >= 11000L) {
					world.setWorldTime(scaledresolution - j + 24001L);
				}
			}

			if(Config.isTimeNightOnly()) {
				if(j <= 14000L) {
					world.setWorldTime(scaledresolution - j + 14001L);
				}

				if(j >= 22000L) {
					world.setWorldTime(scaledresolution - j + 24000L + 14001L);
				}
			}
		}

		if(this.lightmapUpdateNeeded) {
			this.updateLightmap();
		}

		Profiler.endSection();
		if(!Display.isActive()) {
			if(System.currentTimeMillis() - this.prevFrameTime > 500L) {
				this.mc.displayInGameMenu();
			}
		} else {
			this.prevFrameTime = System.currentTimeMillis();
		}

		Profiler.startSection("mouse");
		if(this.mc.inGameHasFocus) {
			this.mc.mouseHelper.mouseXYChange();
			float scaledresolution1 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			float i = scaledresolution1 * scaledresolution1 * scaledresolution1 * 8.0F;
			float j1 = (float)this.mc.mouseHelper.deltaX * i;
			float k = (float)this.mc.mouseHelper.deltaY * i;
			byte i1 = 1;
			if(this.mc.gameSettings.invertMouse) {
				i1 = -1;
			}

			if(this.mc.gameSettings.smoothCamera) {
				this.smoothCamYaw += j1;
				this.smoothCamPitch += k;
				float fpsLimit = par1 - this.smoothCamPartialTicks;
				this.smoothCamPartialTicks = par1;
				j1 = this.smoothCamFilterX * fpsLimit;
				k = this.smoothCamFilterY * fpsLimit;
				this.mc.thePlayer.setAngles(j1, k * (float)i1);
			} else {
				this.mc.thePlayer.setAngles(j1, k * (float)i1);
			}
		}

		Profiler.endSection();
		if(!this.mc.skipRenderWorld) {
			anaglyphEnable = this.mc.gameSettings.anaglyph;
			ScaledResolution scaledresolution2 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
			int i1 = scaledresolution2.getScaledWidth();
			int j2 = scaledresolution2.getScaledHeight();
			int k1 = Mouse.getX() * i1 / this.mc.displayWidth;
			int i11 = j2 - Mouse.getY() * j2 / this.mc.displayHeight - 1;
			short fpsLimit1 = 200;
			if(this.mc.gameSettings.limitFramerate == 1) {
				fpsLimit1 = 120;
			}

			if(this.mc.gameSettings.limitFramerate == 2) {
				fpsLimit1 = 40;
			}

			long l2;
			if(this.mc.theWorld != null) {
				Profiler.startSection("level");
				if(this.mc.gameSettings.limitFramerate == 0) {
					this.renderWorld(par1, 0L);
				} else {
					this.renderWorld(par1, this.renderEndNanoTime + (long)(1.0E9D / (double)fpsLimit1));
				}

				Profiler.endStartSection("sleep");
				if(this.mc.gameSettings.limitFramerate == 2) {
					l2 = (this.renderEndNanoTime + (long)(1.0E9D / (double)fpsLimit1) - System.nanoTime()) / 1000000L;
					if(l2 > 0L && l2 < 500L) {
						try {
							Thread.sleep(l2);
						} catch (InterruptedException interruptedException14) {
							interruptedException14.printStackTrace();
						}
					}
				}

				this.renderEndNanoTime = System.nanoTime();
				Profiler.endStartSection("gui");
				if(!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
					if(this.mc.gameSettings.ofFastDebugInfo) {
						Minecraft minecraft10000 = this.mc;
						if(Minecraft.isDebugInfoEnabled()) {
							this.showDebugInfo = !this.showDebugInfo;
						}

						if(this.showDebugInfo) {
							this.mc.gameSettings.showDebugInfo = true;
						}
					}

					this.mc.ingameGUI.renderGameOverlay(par1, this.mc.currentScreen != null, k1, i11);
					if(this.mc.gameSettings.ofFastDebugInfo) {
						this.mc.gameSettings.showDebugInfo = false;
					}
				}

				Profiler.endSection();
			} else {
				GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
				this.setupOverlayRendering();
				l2 = (this.renderEndNanoTime + (long)(1000000000 / fpsLimit1) - System.nanoTime()) / 1000000L;
				if(l2 < 0L) {
					l2 += 10L;
				}

				if(l2 > 0L && l2 < 500L) {
					try {
						Thread.sleep(l2);
					} catch (InterruptedException interruptedException13) {
						interruptedException13.printStackTrace();
					}
				}

				this.renderEndNanoTime = System.nanoTime();
			}

			if(this.mc.currentScreen != null) {
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				this.mc.currentScreen.drawScreen(k1, i11, par1);
				if(this.mc.currentScreen != null && this.mc.currentScreen.guiParticles != null) {
					this.mc.currentScreen.guiParticles.draw(par1);
				}
			}

		}
	}

	private void updateMainMenu(GuiMainMenu mainGui) {
		try {
			String e = null;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			int day = calendar.get(5);
			int month = calendar.get(2) + 1;
			if(day == 8 && month == 4) {
				e = "Happy birthday, OptiFine!";
			}

			if(day == 14 && month == 8) {
				e = "Happy birthday, sp614x!";
			}

			if(e == null) {
				return;
			}

			Field[] fs = GuiMainMenu.class.getDeclaredFields();

			for(int i = 0; i < fs.length; ++i) {
				if(fs[i].getType() == String.class) {
					fs[i].setAccessible(true);
					fs[i].set(mainGui, e);
					break;
				}
			}
		} catch (Throwable throwable8) {
		}

	}

	private void checkDisplayMode() {
		try {
			DisplayMode e;
			if(Display.isFullscreen()) {
				if(this.fullscreenModeChecked) {
					return;
				}

				this.fullscreenModeChecked = true;
				this.desktopModeChecked = false;
				e = Display.getDisplayMode();
				Dimension dim = Config.getFullscreenDimension();
				if(e.getWidth() == dim.width && e.getHeight() == dim.height) {
					return;
				}

				DisplayMode newMode = Config.getDisplayMode(dim);
				Display.setDisplayMode(newMode);
				this.mc.displayWidth = Display.getDisplayMode().getWidth();
				this.mc.displayHeight = Display.getDisplayMode().getHeight();
				if(this.mc.displayWidth <= 0) {
					this.mc.displayWidth = 1;
				}

				if(this.mc.displayHeight <= 0) {
					this.mc.displayHeight = 1;
				}

				Display.setFullscreen(true);
				Display.update();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			} else {
				if(this.desktopModeChecked) {
					return;
				}

				this.desktopModeChecked = true;
				this.fullscreenModeChecked = false;
				if(Config.getDesktopDisplayMode() == null) {
					Config.setDesktopDisplayMode(Display.getDesktopDisplayMode());
				}

				e = Display.getDisplayMode();
				if(e.equals(Config.getDesktopDisplayMode())) {
					return;
				}

				Display.setDisplayMode(Config.getDesktopDisplayMode());
				if(this.mc.mcCanvas != null) {
					this.mc.displayWidth = this.mc.mcCanvas.getWidth();
					this.mc.displayHeight = this.mc.mcCanvas.getHeight();
				}

				if(this.mc.displayWidth <= 0) {
					this.mc.displayWidth = 1;
				}

				if(this.mc.displayHeight <= 0) {
					this.mc.displayHeight = 1;
				}

				Display.setFullscreen(false);
				Display.update();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
		} catch (Exception exception4) {
			exception4.printStackTrace();
		}

	}

	public void renderWorld(float par1, long par2) {
		Profiler.startSection("lightTex");
		if(this.lightmapUpdateNeeded) {
			this.updateLightmap();
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		if(this.mc.renderViewEntity == null) {
			this.mc.renderViewEntity = this.mc.thePlayer;
		}

		Profiler.endStartSection("pick");
		this.getMouseOver(par1);
		EntityLiving entityliving = this.mc.renderViewEntity;
		RenderGlobal renderglobal = this.mc.renderGlobal;
		EffectRenderer effectrenderer = this.mc.effectRenderer;
		double d = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)par1;
		double d1 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)par1;
		double d2 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)par1;
		Profiler.endStartSection("center");
		IChunkProvider ichunkprovider = this.mc.theWorld.getChunkProvider();
		if(ichunkprovider instanceof ChunkProviderLoadOrGenerate) {
			ChunkProviderLoadOrGenerate i = (ChunkProviderLoadOrGenerate)ichunkprovider;
			int frustrum = MathHelper.floor_float((float)((int)d)) >> 4;
			int hasForge = MathHelper.floor_float((float)((int)d2)) >> 4;
			i.setCurrentChunkOver(frustrum, hasForge);
		}

		for(int i18 = 0; i18 < 2; ++i18) {
			if(this.mc.gameSettings.anaglyph) {
				anaglyphField = i18;
				if(anaglyphField == 0) {
					GL11.glColorMask(false, true, true, false);
				} else {
					GL11.glColorMask(true, false, false, false);
				}
			}

			Profiler.endStartSection("clear");
			GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
			this.updateFogColor(par1);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glEnable(GL11.GL_CULL_FACE);
			Profiler.endStartSection("camera");
			this.setupCameraTransform(par1, i18);
			ActiveRenderInfo.updateRenderInfo(this.mc.thePlayer, this.mc.gameSettings.thirdPersonView == 2);
			Profiler.endStartSection("frustrum");
			ClippingHelperImpl.getInstance();
			if(!Config.isSkyEnabled() && !Config.isSunMoonEnabled() && !Config.isStarsEnabled()) {
				GL11.glDisable(GL11.GL_BLEND);
			} else {
				this.setupFog(-1, par1);
				Profiler.endStartSection("sky");
				renderglobal.renderSky(par1);
			}

			GL11.glEnable(GL11.GL_FOG);
			this.setupFog(1, par1);
			if(this.mc.gameSettings.ambientOcclusion) {
				GL11.glShadeModel(GL11.GL_SMOOTH);
			}

			Profiler.endStartSection("culling");
			Frustrum frustrum19 = new Frustrum();
			frustrum19.setPosition(d, d1, d2);
			this.mc.renderGlobal.clipRenderersByFrustum(frustrum19, par1);
			if(i18 == 0) {
				Profiler.endStartSection("updatechunks");

				while(!this.mc.renderGlobal.updateRenderers(entityliving, false) && par2 != 0L) {
					long j20 = par2 - System.nanoTime();
					if(j20 < 0L || (double)j20 > 1.0E9D) {
						break;
					}
				}
			}

			this.setupFog(0, par1);
			GL11.glEnable(GL11.GL_FOG);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
			RenderHelper.disableStandardItemLighting();
			if(Config.isUseAlphaFunc()) {
				GL11.glAlphaFunc(GL11.GL_GREATER, Config.getAlphaFuncLevel());
			}

			Profiler.endStartSection("terrain");
			renderglobal.sortAndRender(entityliving, 0, (double)par1);
			GL11.glShadeModel(GL11.GL_FLAT);
			boolean z21 = Reflector.hasClass(1);
			EntityPlayer entityplayer1;
			if(this.debugViewDirection == 0) {
				RenderHelper.enableStandardItemLighting();
				Profiler.endStartSection("entities");
				renderglobal.renderEntities(entityliving.getPosition(par1), frustrum19, par1);
				this.enableLightmap((double)par1);
				Profiler.endStartSection("litParticles");
				effectrenderer.func_1187_b(entityliving, par1);
				RenderHelper.disableStandardItemLighting();
				this.setupFog(0, par1);
				Profiler.endStartSection("particles");
				effectrenderer.renderParticles(entityliving, par1);
				this.disableLightmap((double)par1);
				if(this.mc.objectMouseOver != null && entityliving.isInsideOfMaterial(Material.water) && entityliving instanceof EntityPlayer && !this.mc.gameSettings.hideGUI) {
					entityplayer1 = (EntityPlayer)entityliving;
					GL11.glDisable(GL11.GL_ALPHA_TEST);
					Profiler.endStartSection("outline");
					if(!z21 || !Reflector.callBoolean(10, new Object[]{renderglobal, entityplayer1, this.mc.objectMouseOver, 0, entityplayer1.inventory.getCurrentItem(), par1})) {
						renderglobal.drawBlockBreaking(entityplayer1, this.mc.objectMouseOver, 0, entityplayer1.inventory.getCurrentItem(), par1);
						if(!this.mc.gameSettings.hideGUI) {
							renderglobal.drawSelectionBox(entityplayer1, this.mc.objectMouseOver, 0, entityplayer1.inventory.getCurrentItem(), par1);
						}
					}

					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}
			}

			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDepthMask(true);
			this.setupFog(0, par1);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/terrain.png"));
			WrUpdates.resumeBackgroundUpdates();
			if(Config.isWaterFancy()) {
				Profiler.endStartSection("water");
				if(this.mc.gameSettings.ambientOcclusion) {
					GL11.glShadeModel(GL11.GL_SMOOTH);
				}

				GL11.glColorMask(false, false, false, false);
				int i22 = renderglobal.renderAllSortedRenderers(1, (double)par1);
				if(this.mc.gameSettings.anaglyph) {
					if(anaglyphField == 0) {
						GL11.glColorMask(false, true, true, true);
					} else {
						GL11.glColorMask(true, false, false, true);
					}
				} else {
					GL11.glColorMask(true, true, true, true);
				}

				if(i22 > 0) {
					renderglobal.renderAllSortedRenderers(1, (double)par1);
				}

				GL11.glShadeModel(GL11.GL_FLAT);
			} else {
				Profiler.endStartSection("water");
				renderglobal.renderAllSortedRenderers(1, (double)par1);
			}

			WrUpdates.pauseBackgroundUpdates();
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			if(this.cameraZoom == 1.0D && entityliving instanceof EntityPlayer && !this.mc.gameSettings.hideGUI && this.mc.objectMouseOver != null && !entityliving.isInsideOfMaterial(Material.water)) {
				entityplayer1 = (EntityPlayer)entityliving;
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				Profiler.endStartSection("outline");
				if(!z21 || !Reflector.callBoolean(10, new Object[]{renderglobal, entityplayer1, this.mc.objectMouseOver, 0, entityplayer1.inventory.getCurrentItem(), par1})) {
					renderglobal.drawBlockBreaking(entityplayer1, this.mc.objectMouseOver, 0, entityplayer1.inventory.getCurrentItem(), par1);
					if(!this.mc.gameSettings.hideGUI) {
						renderglobal.drawSelectionBox(entityplayer1, this.mc.objectMouseOver, 0, entityplayer1.inventory.getCurrentItem(), par1);
					}
				}

				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}

			Profiler.endStartSection("weather");
			this.renderRainSnow(par1);
			GL11.glDisable(GL11.GL_FOG);
			if(this.pointedEntity == null) {
				;
			}

			if(this.mc.gameSettings.shouldRenderClouds()) {
				Profiler.endStartSection("clouds");
				GL11.glPushMatrix();
				this.setupFog(0, par1);
				GL11.glEnable(GL11.GL_FOG);
				renderglobal.renderClouds(par1);
				GL11.glDisable(GL11.GL_FOG);
				this.setupFog(1, par1);
				GL11.glPopMatrix();
			}

			if(z21) {
				Profiler.endStartSection("fhooks");
				Reflector.callVoid(17, new Object[]{renderglobal, par1});
			}

			Profiler.endStartSection("hand");
			if(this.cameraZoom == 1.0D) {
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				this.renderHand(par1, i18);
			}

			if(!this.mc.gameSettings.anaglyph) {
				Profiler.endSection();
				return;
			}
		}

		GL11.glColorMask(true, true, true, false);
		Profiler.endSection();
	}

	private void addRainParticles() {
		float f = this.mc.theWorld.getRainStrength(1.0F);
		if(!Config.isRainFancy()) {
			f /= 2.0F;
		}

		if(f != 0.0F) {
			if(Config.isRainSplash()) {
				this.random.setSeed((long)this.rendererUpdateCount * 312987231L);
				EntityLiving entityliving = this.mc.renderViewEntity;
				World world = this.mc.theWorld;
				int i = MathHelper.floor_double(entityliving.posX);
				int j = MathHelper.floor_double(entityliving.posY);
				int k = MathHelper.floor_double(entityliving.posZ);
				byte byte0 = 10;
				double d = 0.0D;
				double d1 = 0.0D;
				double d2 = 0.0D;
				int l = 0;
				int i1 = (int)(100.0F * f * f);
				if(this.mc.gameSettings.particleSetting == 1) {
					i1 >>= 1;
				} else if(this.mc.gameSettings.particleSetting == 2) {
					i1 = 0;
				}

				for(int j1 = 0; j1 < i1; ++j1) {
					int k1 = i + this.random.nextInt(byte0) - this.random.nextInt(byte0);
					int l1 = k + this.random.nextInt(byte0) - this.random.nextInt(byte0);
					int i2 = world.getPrecipitationHeight(k1, l1);
					int j2 = world.getBlockId(k1, i2 - 1, l1);
					BiomeGenBase biomegenbase = world.getBiomeGenForCoords(k1, l1);
					if(i2 <= j + byte0 && i2 >= j - byte0 && biomegenbase.canSpawnLightningBolt() && biomegenbase.getFloatTemperature() > 0.2F) {
						float f1 = this.random.nextFloat();
						float f2 = this.random.nextFloat();
						if(j2 > 0) {
							if(Block.blocksList[j2].blockMaterial == Material.lava) {
								this.mc.effectRenderer.addEffect(new EntitySmokeFX(world, (double)((float)k1 + f1), (double)((float)i2 + 0.1F) - Block.blocksList[j2].minY, (double)((float)l1 + f2), 0.0D, 0.0D, 0.0D));
							} else {
								++l;
								if(this.random.nextInt(l) == 0) {
									d = (double)((float)k1 + f1);
									d1 = (double)((float)i2 + 0.1F) - Block.blocksList[j2].minY;
									d2 = (double)((float)l1 + f2);
								}

								EntityRainFX fx = new EntityRainFX(world, (double)((float)k1 + f1), (double)((float)i2 + 0.1F) - Block.blocksList[j2].minY, (double)((float)l1 + f2));
								CustomColorizer.updateWaterFX(fx, world);
								this.mc.effectRenderer.addEffect(fx);
							}
						}
					}
				}

				if(l > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
					this.rainSoundCounter = 0;
					if(d1 > entityliving.posY + 1.0D && world.getPrecipitationHeight(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posZ)) > MathHelper.floor_double(entityliving.posY)) {
						this.mc.theWorld.playSoundEffect(d, d1, d2, "ambient.weather.rain", 0.1F, 0.5F);
					} else {
						this.mc.theWorld.playSoundEffect(d, d1, d2, "ambient.weather.rain", 0.2F, 1.0F);
					}
				}

			}
		}
	}

	protected void renderRainSnow(float par1) {
		float f = this.mc.theWorld.getRainStrength(par1);
		if(f > 0.0F) {
			this.enableLightmap((double)par1);
			if(this.rainXCoords == null) {
				this.rainXCoords = new float[1024];
				this.rainYCoords = new float[1024];

				for(int entityliving = 0; entityliving < 32; ++entityliving) {
					for(int world = 0; world < 32; ++world) {
						float k = (float)(world - 16);
						float l = (float)(entityliving - 16);
						float i1 = MathHelper.sqrt_float(k * k + l * l);
						this.rainXCoords[entityliving << 5 | world] = -l / i1;
						this.rainYCoords[entityliving << 5 | world] = k / i1;
					}
				}
			}

			if(!Config.isRainOff()) {
				EntityLiving entityLiving41 = this.mc.renderViewEntity;
				World world42 = this.mc.theWorld;
				int i43 = MathHelper.floor_double(entityLiving41.posX);
				int i44 = MathHelper.floor_double(entityLiving41.posY);
				int i45 = MathHelper.floor_double(entityLiving41.posZ);
				Tessellator tessellator = Tessellator.instance;
				GL11.glDisable(GL11.GL_CULL_FACE);
				GL11.glNormal3f(0.0F, 1.0F, 0.0F);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.01F);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/environment/snow.png"));
				double d = entityLiving41.lastTickPosX + (entityLiving41.posX - entityLiving41.lastTickPosX) * (double)par1;
				double d1 = entityLiving41.lastTickPosY + (entityLiving41.posY - entityLiving41.lastTickPosY) * (double)par1;
				double d2 = entityLiving41.lastTickPosZ + (entityLiving41.posZ - entityLiving41.lastTickPosZ) * (double)par1;
				int j1 = MathHelper.floor_double(d1);
				byte k1 = 5;
				if(Config.isRainFancy()) {
					k1 = 10;
				}

				boolean flag = false;
				byte byte0 = -1;
				float f4 = (float)this.rendererUpdateCount + par1;
				if(Config.isRainFancy()) {
					k1 = 10;
				}

				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				flag = false;

				for(int l1 = i45 - k1; l1 <= i45 + k1; ++l1) {
					for(int i2 = i43 - k1; i2 <= i43 + k1; ++i2) {
						int j2 = (l1 - i45 + 16) * 32 + i2 - i43 + 16;
						float f5 = this.rainXCoords[j2] * 0.5F;
						float f6 = this.rainYCoords[j2] * 0.5F;
						BiomeGenBase biomegenbase = world42.getBiomeGenForCoords(i2, l1);
						if(biomegenbase.canSpawnLightningBolt() || biomegenbase.getEnableSnow()) {
							int k2 = world42.getPrecipitationHeight(i2, l1);
							int l2 = i44 - k1;
							int i3 = i44 + k1;
							if(l2 < k2) {
								l2 = k2;
							}

							if(i3 < k2) {
								i3 = k2;
							}

							float f7 = 1.0F;
							int j3 = k2;
							if(k2 < j1) {
								j3 = j1;
							}

							if(l2 != i3) {
								this.random.setSeed((long)(i2 * i2 * 3121 + i2 * 45238971 ^ l1 * l1 * 418711 + l1 * 13761));
								float f8 = biomegenbase.getFloatTemperature();
								float f10;
								double d5;
								if(world42.getWorldChunkManager().getTemperatureAtHeight(f8, k2) >= 0.15F) {
									if(byte0 != 0) {
										if(byte0 >= 0) {
											tessellator.draw();
										}

										byte0 = 0;
										GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/environment/rain.png"));
										tessellator.startDrawingQuads();
									}

									f10 = ((float)(this.rendererUpdateCount + i2 * i2 * 3121 + i2 * 45238971 + l1 * l1 * 418711 + l1 * 13761 & 31) + par1) / 32.0F * (3.0F + this.random.nextFloat());
									double f11 = (double)((float)i2 + 0.5F) - entityLiving41.posX;
									d5 = (double)((float)l1 + 0.5F) - entityLiving41.posZ;
									float d6 = MathHelper.sqrt_double(f11 * f11 + d5 * d5) / (float)k1;
									float f14 = 1.0F;
									tessellator.setBrightness(world42.getLightBrightnessForSkyBlocks(i2, j3, l1, 0));
									tessellator.setColorRGBA_F(f14, f14, f14, ((1.0F - d6 * d6) * 0.5F + 0.5F) * f);
									tessellator.setTranslation(-d * 1.0D, -d1 * 1.0D, -d2 * 1.0D);
									tessellator.addVertexWithUV((double)((float)i2 - f5) + 0.5D, (double)l2, (double)((float)l1 - f6) + 0.5D, (double)(0.0F * f7), (double)((float)l2 * f7 / 4.0F + f10 * f7));
									tessellator.addVertexWithUV((double)((float)i2 + f5) + 0.5D, (double)l2, (double)((float)l1 + f6) + 0.5D, (double)(1.0F * f7), (double)((float)l2 * f7 / 4.0F + f10 * f7));
									tessellator.addVertexWithUV((double)((float)i2 + f5) + 0.5D, (double)i3, (double)((float)l1 + f6) + 0.5D, (double)(1.0F * f7), (double)((float)i3 * f7 / 4.0F + f10 * f7));
									tessellator.addVertexWithUV((double)((float)i2 - f5) + 0.5D, (double)i3, (double)((float)l1 - f6) + 0.5D, (double)(0.0F * f7), (double)((float)i3 * f7 / 4.0F + f10 * f7));
									tessellator.setTranslation(0.0D, 0.0D, 0.0D);
								} else {
									if(byte0 != 1) {
										if(byte0 >= 0) {
											tessellator.draw();
										}

										byte0 = 1;
										GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/environment/snow.png"));
										tessellator.startDrawingQuads();
									}

									f10 = ((float)(this.rendererUpdateCount & 511) + par1) / 512.0F;
									float f46 = this.random.nextFloat() + f4 * 0.01F * (float)this.random.nextGaussian();
									float f12 = this.random.nextFloat() + f4 * (float)this.random.nextGaussian() * 0.001F;
									d5 = (double)((float)i2 + 0.5F) - entityLiving41.posX;
									double d47 = (double)((float)l1 + 0.5F) - entityLiving41.posZ;
									float f15 = MathHelper.sqrt_double(d5 * d5 + d47 * d47) / (float)k1;
									float f16 = 1.0F;
									tessellator.setBrightness((world42.getLightBrightnessForSkyBlocks(i2, j3, l1, 0) * 3 + 15728880) / 4);
									tessellator.setColorRGBA_F(f16, f16, f16, ((1.0F - f15 * f15) * 0.3F + 0.5F) * f);
									tessellator.setTranslation(-d * 1.0D, -d1 * 1.0D, -d2 * 1.0D);
									tessellator.addVertexWithUV((double)((float)i2 - f5) + 0.5D, (double)l2, (double)((float)l1 - f6) + 0.5D, (double)(0.0F * f7 + f46), (double)((float)l2 * f7 / 4.0F + f10 * f7 + f12));
									tessellator.addVertexWithUV((double)((float)i2 + f5) + 0.5D, (double)l2, (double)((float)l1 + f6) + 0.5D, (double)(1.0F * f7 + f46), (double)((float)l2 * f7 / 4.0F + f10 * f7 + f12));
									tessellator.addVertexWithUV((double)((float)i2 + f5) + 0.5D, (double)i3, (double)((float)l1 + f6) + 0.5D, (double)(1.0F * f7 + f46), (double)((float)i3 * f7 / 4.0F + f10 * f7 + f12));
									tessellator.addVertexWithUV((double)((float)i2 - f5) + 0.5D, (double)i3, (double)((float)l1 - f6) + 0.5D, (double)(0.0F * f7 + f46), (double)((float)i3 * f7 / 4.0F + f10 * f7 + f12));
									tessellator.setTranslation(0.0D, 0.0D, 0.0D);
								}
							}
						}
					}
				}

				if(byte0 >= 0) {
					tessellator.draw();
				}

				GL11.glEnable(GL11.GL_CULL_FACE);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
				this.disableLightmap((double)par1);
			}
		}
	}

	public void setupOverlayRendering() {
		ScaledResolution scaledresolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, scaledresolution.scaledWidthD, scaledresolution.scaledHeightD, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
	}

	private void updateFogColor(float par1) {
		World world = this.mc.theWorld;
		EntityLiving entityliving = this.mc.renderViewEntity;
		float f = 1.0F / (float)(4 - this.mc.gameSettings.renderDistance);
		f = 1.0F - (float)Math.pow((double)f, 0.25D);
		Vec3D vec3d = world.getSkyColor(this.mc.renderViewEntity, par1);
		int worldType = world.worldProvider.worldType;
		switch(worldType) {
		case 0:
			vec3d = CustomColorizer.getSkyColor(vec3d, this.mc.theWorld, this.mc.renderViewEntity.posX, this.mc.renderViewEntity.posY + 1.0D, this.mc.renderViewEntity.posZ);
			break;
		case 1:
			vec3d = CustomColorizer.getSkyColorEnd(vec3d);
		}

		float f1 = (float)vec3d.xCoord;
		float f2 = (float)vec3d.yCoord;
		float f3 = (float)vec3d.zCoord;
		Vec3D vec3d1 = world.getFogColor(par1);
		switch(worldType) {
		case -1:
			vec3d1 = CustomColorizer.getFogColorNether(vec3d1);
			break;
		case 0:
			vec3d1 = CustomColorizer.getFogColor(vec3d1, this.mc.theWorld, this.mc.renderViewEntity.posX, this.mc.renderViewEntity.posY + 1.0D, this.mc.renderViewEntity.posZ);
			break;
		case 1:
			vec3d1 = CustomColorizer.getFogColorEnd(vec3d1);
		}

		this.fogColorRed = (float)vec3d1.xCoord;
		this.fogColorGreen = (float)vec3d1.yCoord;
		this.fogColorBlue = (float)vec3d1.zCoord;
		float f7;
		if(this.mc.gameSettings.renderDistance < 2) {
			Vec3D f4 = MathHelper.sin(world.getCelestialAngleRadians(par1)) <= 0.0F ? Vec3D.createVector(1.0D, 0.0D, 0.0D) : Vec3D.createVector(-1.0D, 0.0D, 0.0D);
			f7 = (float)entityliving.getLook(par1).dotProduct(f4);
			if(f7 < 0.0F) {
				f7 = 0.0F;
			}

			if(f7 > 0.0F) {
				float[] i = world.worldProvider.calcSunriseSunsetColors(world.getCelestialAngle(par1), par1);
				if(i != null) {
					f7 *= i[3];
					this.fogColorRed = this.fogColorRed * (1.0F - f7) + i[0] * f7;
					this.fogColorGreen = this.fogColorGreen * (1.0F - f7) + i[1] * f7;
					this.fogColorBlue = this.fogColorBlue * (1.0F - f7) + i[2] * f7;
				}
			}
		}

		this.fogColorRed += (f1 - this.fogColorRed) * f;
		this.fogColorGreen += (f2 - this.fogColorGreen) * f;
		this.fogColorBlue += (f3 - this.fogColorBlue) * f;
		float f41 = world.getRainStrength(par1);
		float i1;
		if(f41 > 0.0F) {
			f7 = 1.0F - f41 * 0.5F;
			i1 = 1.0F - f41 * 0.4F;
			this.fogColorRed *= f7;
			this.fogColorGreen *= f7;
			this.fogColorBlue *= i1;
		}

		f7 = world.getWeightedThunderStrength(par1);
		if(f7 > 0.0F) {
			i1 = 1.0F - f7 * 0.5F;
			this.fogColorRed *= i1;
			this.fogColorGreen *= i1;
			this.fogColorBlue *= i1;
		}

		int i2 = ActiveRenderInfo.getBlockIdAtEntityViewpoint(this.mc.theWorld, entityliving, par1);
		Vec3D f10;
		if(this.cloudFog) {
			f10 = world.drawClouds(par1);
			this.fogColorRed = (float)f10.xCoord;
			this.fogColorGreen = (float)f10.yCoord;
			this.fogColorBlue = (float)f10.zCoord;
		} else if(i2 != 0 && Block.blocksList[i2].blockMaterial == Material.water) {
			this.fogColorRed = 0.02F;
			this.fogColorGreen = 0.02F;
			this.fogColorBlue = 0.2F;
			f10 = CustomColorizer.getUnderwaterColor(this.mc.theWorld, this.mc.renderViewEntity.posX, this.mc.renderViewEntity.posY + 1.0D, this.mc.renderViewEntity.posZ);
			if(f10 != null) {
				this.fogColorRed = (float)f10.xCoord;
				this.fogColorGreen = (float)f10.yCoord;
				this.fogColorBlue = (float)f10.zCoord;
			}
		} else if(i2 != 0 && Block.blocksList[i2].blockMaterial == Material.lava) {
			this.fogColorRed = 0.6F;
			this.fogColorGreen = 0.1F;
			this.fogColorBlue = 0.0F;
		}

		float f101 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * par1;
		this.fogColorRed *= f101;
		this.fogColorGreen *= f101;
		this.fogColorBlue *= f101;
		double d = (entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)par1) * world.worldProvider.getVoidFogYFactor();
		if(entityliving.isPotionActive(Potion.blindness)) {
			int f11 = entityliving.getActivePotionEffect(Potion.blindness).getDuration();
			if(f11 < 20) {
				d *= (double)(1.0F - (float)f11 / 20.0F);
			} else {
				d = 0.0D;
			}
		}

		if(d < 1.0D) {
			if(d < 0.0D) {
				d = 0.0D;
			}

			d *= d;
			this.fogColorRed = (float)((double)this.fogColorRed * d);
			this.fogColorGreen = (float)((double)this.fogColorGreen * d);
			this.fogColorBlue = (float)((double)this.fogColorBlue * d);
		}

		if(this.mc.gameSettings.anaglyph) {
			float f111 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
			float f12 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
			float f13 = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
			this.fogColorRed = f111;
			this.fogColorGreen = f12;
			this.fogColorBlue = f13;
		}

		GL11.glClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
	}

	private void setupFog(int par1, float par2) {
		EntityLiving entityliving = this.mc.renderViewEntity;
		boolean flag = false;
		if(entityliving instanceof EntityPlayer) {
			flag = ((EntityPlayer)entityliving).capabilities.isCreativeMode;
		}

		if(par1 == 999) {
			GL11.glFog(GL11.GL_FOG_COLOR, this.setFogColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
			GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
			GL11.glFogf(GL11.GL_FOG_START, 0.0F);
			GL11.glFogf(GL11.GL_FOG_END, 8.0F);
			if(GLContext.getCapabilities().GL_NV_fog_distance) {
				GL11.glFogi(34138, 34139);
			}

			GL11.glFogf(GL11.GL_FOG_START, 0.0F);
		} else {
			GL11.glFog(GL11.GL_FOG_COLOR, this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
			GL11.glNormal3f(0.0F, -1.0F, 0.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			int i = ActiveRenderInfo.getBlockIdAtEntityViewpoint(this.mc.theWorld, entityliving, par2);
			float f4;
			if(entityliving.isPotionActive(Potion.blindness)) {
				f4 = 5.0F;
				int fogStart = entityliving.getActivePotionEffect(Potion.blindness).getDuration();
				if(fogStart < 20) {
					f4 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - (float)fogStart / 20.0F);
				}

				GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
				if(par1 < 0) {
					GL11.glFogf(GL11.GL_FOG_START, 0.0F);
					GL11.glFogf(GL11.GL_FOG_END, f4 * 0.8F);
				} else {
					GL11.glFogf(GL11.GL_FOG_START, f4 * 0.25F);
					GL11.glFogf(GL11.GL_FOG_END, f4);
				}

				if(Config.isFogFancy()) {
					GL11.glFogi(34138, 34139);
				}
			} else {
				float fogEnd;
				float f14;
				float f17;
				float f20;
				float fogStart1;
				if(this.cloudFog) {
					GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
					GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
					f4 = 1.0F;
					fogStart1 = 1.0F;
					fogEnd = 1.0F;
					if(this.mc.gameSettings.anaglyph) {
						f14 = (f4 * 30.0F + fogStart1 * 59.0F + fogEnd * 11.0F) / 100.0F;
						f17 = (f4 * 30.0F + fogStart1 * 70.0F) / 100.0F;
						f20 = (f4 * 30.0F + fogEnd * 70.0F) / 100.0F;
					}
				} else if(i > 0 && Block.blocksList[i].blockMaterial == Material.water) {
					GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
					f4 = 0.1F;
					if(!entityliving.isPotionActive(Potion.waterBreathing)) {
						f4 = 0.1F;
					} else {
						f4 = 0.05F;
					}

					if(Config.isClearWater()) {
						f4 /= 5.0F;
					}

					GL11.glFogf(GL11.GL_FOG_DENSITY, f4);
					fogStart1 = 0.4F;
					fogEnd = 0.4F;
					f14 = 0.9F;
					if(this.mc.gameSettings.anaglyph) {
						f17 = (fogStart1 * 30.0F + fogEnd * 59.0F + f14 * 11.0F) / 100.0F;
						f20 = (fogStart1 * 30.0F + fogEnd * 70.0F) / 100.0F;
						float f19 = (fogStart1 * 30.0F + f14 * 70.0F) / 100.0F;
					}
				} else if(i > 0 && Block.blocksList[i].blockMaterial == Material.lava) {
					GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
					GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
					f4 = 0.4F;
					fogStart1 = 0.3F;
					fogEnd = 0.3F;
					if(this.mc.gameSettings.anaglyph) {
						f14 = (f4 * 30.0F + fogStart1 * 59.0F + fogEnd * 11.0F) / 100.0F;
						f17 = (f4 * 30.0F + fogStart1 * 70.0F) / 100.0F;
						f20 = (f4 * 30.0F + fogEnd * 70.0F) / 100.0F;
					}
				} else {
					f4 = this.farPlaneDistance;
					if(Config.isDepthFog() && this.mc.theWorld.worldProvider.getWorldHasNoSky() && !flag) {
						double fogStart2 = (double)((entityliving.getBrightnessForRender(par2) & 15728640) >> 20) / 16.0D + (entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)par2 + 4.0D) / 32.0D;
						if(fogStart2 < 1.0D) {
							if(fogStart2 < 0.0D) {
								fogStart2 = 0.0D;
							}

							fogStart2 *= fogStart2;
							f14 = 100.0F * (float)fogStart2;
							if(f14 < 5.0F) {
								f14 = 5.0F;
							}

							if(f4 > f14) {
								f4 = f14;
							}
						}
					}

					GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
					if(GLContext.getCapabilities().GL_NV_fog_distance) {
						if(Config.isFogFancy()) {
							GL11.glFogi(34138, 34139);
						}

						if(Config.isFogFast()) {
							GL11.glFogi(34138, 34140);
						}
					}

					fogStart1 = Config.getFogStart();
					fogEnd = 1.0F;
					if(par1 < 0) {
						fogStart1 = 0.0F;
						fogEnd = 0.8F;
					}

					if(this.mc.theWorld.worldProvider.func_48218_b((int)entityliving.posX, (int)entityliving.posZ)) {
						fogStart1 = 0.05F;
						fogEnd = 1.0F;
						f4 = this.farPlaneDistance;
					}

					GL11.glFogf(GL11.GL_FOG_START, f4 * fogStart1);
					GL11.glFogf(GL11.GL_FOG_END, f4 * fogEnd);
				}
			}

			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
		}
	}

	private FloatBuffer setFogColorBuffer(float par1, float par2, float par3, float par4) {
		this.fogColorBuffer.clear();
		this.fogColorBuffer.put(par1).put(par2).put(par3).put(par4);
		this.fogColorBuffer.flip();
		return this.fogColorBuffer;
	}
}
