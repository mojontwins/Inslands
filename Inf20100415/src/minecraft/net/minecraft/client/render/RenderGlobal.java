package net.minecraft.client.render;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.effect.EntityBubbleFX;
import net.minecraft.client.effect.EntityExplodeFX;
import net.minecraft.client.effect.EntityFlameFX;
import net.minecraft.client.effect.EntityLavaFX;
import net.minecraft.client.effect.EntitySmokeFX;
import net.minecraft.client.effect.EntitySplashFX;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.render.camera.Frustrum;
import net.minecraft.client.render.entity.RenderManager;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.IWorldAccess;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBOcclusionQuery;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import util.MathHelper;
import org.lwjgl.opengl.GL15;

public final class RenderGlobal implements IWorldAccess {
	private World worldObj;
	private RenderEngine renderEngine;
	private List worlRenderersToUpdate = new ArrayList();
	private WorldRenderer[] sortedWorldRenderers;
	private WorldRenderer[] worldRenderers;
	private int renderChunksWide;
	private int renderChunksTall;
	private int renderChunksDeep;
	private int glRenderListBase;
	private Minecraft mc;
	private RenderBlocks globalRenderBlocks;
	private IntBuffer glOcclusionQueryBase;
	private boolean occlusionEnabled = false;
	private int cloudOffsetX = 0;
	private int glSkyList;
	private int glSkyList2;
	private int minBlockX;
	private int minBlockY;
	private int minBlockZ;
	private int maxBlockX;
	private int maxBlockY;
	private int maxBlockZ;
	private int renderDistance = -1;
	private int countEntitiesTotal;
	private int countEntitiesRendered;
	private int countEntitiesHidden;
	private IntBuffer occlusionResult = BufferUtils.createIntBuffer(64);
	private int renderersLoaded;
	private int renderersBeingClipped;
	private int renderersBeingOccluded;
	private int renderersBeingRendered;
	private List glRenderLists = new ArrayList();
	private double prevSortX = -9999.0D;
	private double prevSortY = -9999.0D;
	private double prevSortZ = -9999.0D;
	public float damagePartialTime;

	public RenderGlobal(Minecraft minecraft1, RenderEngine renderEngine2) {
		this.mc = minecraft1;
		this.renderEngine = renderEngine2;
		this.glRenderListBase = GL11.glGenLists(786432);
		this.occlusionEnabled = GLContext.getCapabilities().GL_ARB_occlusion_query;
		if(this.occlusionEnabled) {
			this.occlusionResult.clear();
			GL11.glGetInteger(GL15.GL_QUERY_COUNTER_BITS, this.occlusionResult);
			if(this.occlusionResult.get(0) == 0) {
				this.occlusionEnabled = false;
			} else {
				this.glOcclusionQueryBase = BufferUtils.createIntBuffer(262144);
				this.glOcclusionQueryBase.clear();
				this.glOcclusionQueryBase.position(0);
				this.glOcclusionQueryBase.limit(262144);
				ARBOcclusionQuery.glGenQueriesARB(this.glOcclusionQueryBase);
			}
		}

		this.glSkyList = GL11.glGenLists(1);
		GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
		Random random5 = new Random(10842L);

		Tessellator tessellator3;
		int i7;
		for(i7 = 0; i7 < 500; ++i7) {
			GL11.glRotatef(random5.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(random5.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(random5.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
			tessellator3 = Tessellator.instance;
			float f4 = 0.25F + random5.nextFloat() * 0.25F;
			tessellator3.startDrawingQuads();
			tessellator3.addVertexWithUV((double)(-f4), -100.0D, (double)f4, 1.0D, 1.0D);
			tessellator3.addVertexWithUV((double)f4, -100.0D, (double)f4, 0.0D, 1.0D);
			tessellator3.addVertexWithUV((double)f4, -100.0D, (double)(-f4), 0.0D, 0.0D);
			tessellator3.addVertexWithUV((double)(-f4), -100.0D, (double)(-f4), 1.0D, 0.0D);
			tessellator3.draw();
		}

		GL11.glEndList();
		this.glSkyList2 = GL11.glGenLists(1);
		GL11.glNewList(this.glSkyList2, GL11.GL_COMPILE);
		tessellator3 = Tessellator.instance;
		Tessellator.instance.startDrawingQuads();

		for(int i6 = -256; i6 <= 256; i6 += 32) {
			for(i7 = -256; i7 <= 256; i7 += 32) {
				tessellator3.addVertex((double)i6, 16.0D, (double)i7);
				tessellator3.addVertex((double)(i6 + 32), 16.0D, (double)i7);
				tessellator3.addVertex((double)(i6 + 32), 16.0D, (double)(i7 + 32));
				tessellator3.addVertex((double)i6, 16.0D, (double)(i7 + 32));
			}
		}

		tessellator3.draw();
		GL11.glEndList();
	}

	public final void changeWorld(World world1) {
		if(this.worldObj != null) {
			this.worldObj.removeWorldAccess(this);
		}

		this.prevSortX = -9999.0D;
		this.prevSortY = -9999.0D;
		this.prevSortZ = -9999.0D;
		RenderManager.instance.set(world1);
		this.worldObj = world1;
		this.globalRenderBlocks = new RenderBlocks(world1);
		if(world1 != null) {
			world1.addWorldAccess(this);
			this.loadRenderers();
		}

	}

	private void loadRenderers() {
		this.renderDistance = this.mc.gameSettings.renderDistance;
		int i1;
		if(this.worldRenderers != null) {
			for(i1 = 0; i1 < this.worldRenderers.length; ++i1) {
				this.worldRenderers[i1].stopRendering();
			}
		}

		if((i1 = 5 << 3 - this.renderDistance) > 28) {
			i1 = 28;
		}

		this.renderChunksWide = i1;
		this.renderChunksTall = 8;
		this.renderChunksDeep = i1;
		this.worldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
		this.sortedWorldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
		i1 = 0;
		int i2 = 0;
		this.minBlockX = 0;
		this.minBlockY = 0;
		this.minBlockZ = 0;
		this.maxBlockX = this.renderChunksWide;
		this.maxBlockY = this.renderChunksTall;
		this.maxBlockZ = this.renderChunksDeep;

		int i3;
		for(i3 = 0; i3 < this.worlRenderersToUpdate.size(); ++i3) {
			((WorldRenderer)this.worlRenderersToUpdate.get(i3)).needsUpdate = false;
		}

		this.worlRenderersToUpdate.clear();

		for(i3 = 0; i3 < this.renderChunksWide; ++i3) {
			for(int i4 = 0; i4 < this.renderChunksTall; ++i4) {
				for(int i5 = 0; i5 < this.renderChunksDeep; ++i5) {
					this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3] = new WorldRenderer(this.worldObj, i3 << 4, i4 << 4, i5 << 4, 16, this.glRenderListBase + i1);
					if(this.occlusionEnabled) {
						this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3].glOcclusionQuery = this.glOcclusionQueryBase.get(i2);
					}

					this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3].isWaitingOnOcclusionQuery = false;
					this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3].isVisible = true;
					this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3].isInFrustum = true;
					++i2;
					this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3].needsUpdate = true;
					this.sortedWorldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3] = this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3];
					this.worlRenderersToUpdate.add(this.worldRenderers[(i5 * this.renderChunksTall + i4) * this.renderChunksWide + i3]);
					i1 += 3;
				}
			}
		}

		Entity entity6 = this.worldObj.playerEntity;
		this.markRenderersForNewPosition(MathHelper.floor_double(entity6.posX), MathHelper.floor_double(entity6.posY), MathHelper.floor_double(entity6.posZ));
		Arrays.sort(this.sortedWorldRenderers, new EntitySorter(entity6));
	}

	public final void renderEntities(Vec3D vec3D1, Frustrum frustrum2, float f3) {
		RenderManager.instance.cacheActiveRenderInfo(this.worldObj, this.renderEngine, this.mc.thePlayer, f3);
		this.countEntitiesTotal = 0;
		this.countEntitiesRendered = 0;
		this.countEntitiesHidden = 0;
		Entity entity4 = this.worldObj.playerEntity;
		RenderManager.renderPosX = this.worldObj.playerEntity.lastTickPosX + (entity4.posX - entity4.lastTickPosX) * (double)f3;
		RenderManager.renderPosY = entity4.lastTickPosY + (entity4.posY - entity4.lastTickPosY) * (double)f3;
		RenderManager.renderPosZ = entity4.lastTickPosZ + (entity4.posZ - entity4.lastTickPosZ) * (double)f3;
		List list30 = this.worldObj.getLoadedEntityList();
		this.countEntitiesTotal = list30.size();

		for(int i5 = 0; i5 < list30.size(); ++i5) {
			Entity entity6;
			Entity entity7;
			double d10 = (entity7 = entity6 = (Entity)list30.get(i5)).posX - vec3D1.xCoord;
			double d12 = entity7.posY - vec3D1.yCoord;
			double d14 = entity7.posZ - vec3D1.zCoord;
			double d16 = d10 * d10 + d12 * d12 + d14 * d14;
			AxisAlignedBB axisAlignedBB31;
			double d24 = (axisAlignedBB31 = entity7.boundingBox).maxX - axisAlignedBB31.minX;
			double d26 = axisAlignedBB31.maxY - axisAlignedBB31.minY;
			double d28 = axisAlignedBB31.maxZ - axisAlignedBB31.minZ;
			double d21 = (d24 + d26 + d28) / 3.0D * 64.0D;
			if(d16 < d21 * d21 && frustrum2.isBoundingBoxInFrustrum(entity6.boundingBox) && (entity6 != this.worldObj.playerEntity || this.mc.gameSettings.thirdPersonView)) {
				++this.countEntitiesRendered;
				RenderManager.instance.renderEntity(entity6, f3);
			}
		}

	}

	public final String getDebugInfoRenders() {
		return "C: " + this.renderersBeingRendered + "/" + this.renderersLoaded + ". F: " + this.renderersBeingClipped + ", O: " + this.renderersBeingOccluded;
	}

	public final String getDebugInfoEntities() {
		return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ". B: " + 0 + ", I: " + (this.countEntitiesTotal - this.countEntitiesRendered);
	}

	private void markRenderersForNewPosition(int i1, int i2, int i3) {
		i1 -= 8;
		i3 -= 8;
		this.minBlockX = Integer.MAX_VALUE;
		this.minBlockY = Integer.MAX_VALUE;
		this.minBlockZ = Integer.MAX_VALUE;
		this.maxBlockX = Integer.MIN_VALUE;
		this.maxBlockY = Integer.MIN_VALUE;
		this.maxBlockZ = Integer.MIN_VALUE;
		int i4 = (i2 = this.renderChunksWide << 4) / 2;

		for(int i5 = 0; i5 < this.renderChunksWide; ++i5) {
			int i6;
			int i7;
			if((i7 = (i6 = i5 << 4) + i4 - i1) < 0) {
				i7 -= i2 - 1;
			}

			i7 /= i2;
			if((i6 -= i7 * i2) < this.minBlockX) {
				this.minBlockX = i6;
			}

			if(i6 > this.maxBlockX) {
				this.maxBlockX = i6;
			}

			for(i7 = 0; i7 < this.renderChunksDeep; ++i7) {
				int i8;
				int i9;
				if((i9 = (i8 = i7 << 4) + i4 - i3) < 0) {
					i9 -= i2 - 1;
				}

				i9 /= i2;
				if((i8 -= i9 * i2) < this.minBlockZ) {
					this.minBlockZ = i8;
				}

				if(i8 > this.maxBlockZ) {
					this.maxBlockZ = i8;
				}

				for(i9 = 0; i9 < this.renderChunksTall; ++i9) {
					int i10;
					if((i10 = i9 << 4) < this.minBlockY) {
						this.minBlockY = i10;
					}

					if(i10 > this.maxBlockY) {
						this.maxBlockY = i10;
					}

					WorldRenderer worldRenderer11;
					boolean z12 = (worldRenderer11 = this.worldRenderers[(i7 * this.renderChunksTall + i9) * this.renderChunksWide + i5]).needsUpdate;
					worldRenderer11.setPosition(i6, i10, i8);
					if(!z12 && worldRenderer11.needsUpdate) {
						this.worlRenderersToUpdate.add(worldRenderer11);
					}
				}
			}
		}

	}

	public final int sortAndRender(EntityPlayer entityPlayer1, int i2, double d3) {
		if(this.mc.gameSettings.renderDistance != this.renderDistance) {
			this.loadRenderers();
		}

		if(i2 == 0) {
			this.renderersLoaded = 0;
			this.renderersBeingClipped = 0;
			this.renderersBeingOccluded = 0;
			this.renderersBeingRendered = 0;
		}

		double d5 = entityPlayer1.lastTickPosX + (entityPlayer1.posX - entityPlayer1.lastTickPosX) * d3;
		double d7 = entityPlayer1.lastTickPosY + (entityPlayer1.posY - entityPlayer1.lastTickPosY) * d3;
		double d9 = entityPlayer1.lastTickPosZ + (entityPlayer1.posZ - entityPlayer1.lastTickPosZ) * d3;
		double d11 = entityPlayer1.posX - this.prevSortX;
		double d13 = entityPlayer1.posY - this.prevSortY;
		double d15 = entityPlayer1.posZ - this.prevSortZ;
		if(d11 * d11 + d13 * d13 + d15 * d15 > 16.0D) {
			this.prevSortX = entityPlayer1.posX;
			this.prevSortY = entityPlayer1.posY;
			this.prevSortZ = entityPlayer1.posZ;
			this.markRenderersForNewPosition(MathHelper.floor_double(entityPlayer1.posX), MathHelper.floor_double(entityPlayer1.posY), MathHelper.floor_double(entityPlayer1.posZ));
			Arrays.sort(this.sortedWorldRenderers, new EntitySorter(entityPlayer1));
		}

		int i21;
		if(this.occlusionEnabled && !this.mc.gameSettings.anaglyph && i2 == 0) {
			int i22 = 16;
			this.checkOcclusionQueryResult(0, 16);

			for(int i14 = 0; i14 < 16; ++i14) {
				this.sortedWorldRenderers[i14].isVisible = true;
			}

			i21 = 0 + this.renderSortedRenderers(0, 16, i2, d3);

			do {
				int i12 = i22;
				if((i22 <<= 1) > this.sortedWorldRenderers.length) {
					i22 = this.sortedWorldRenderers.length;
				}

				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glDisable(GL11.GL_FOG);
				GL11.glColorMask(false, false, false, false);
				GL11.glDepthMask(false);
				this.checkOcclusionQueryResult(i12, i22);
				GL11.glPushMatrix();
				float f23 = 0.0F;
				float f24 = 0.0F;
				float f16 = 0.0F;

				for(int i17 = i12; i17 < i22; ++i17) {
					if(this.sortedWorldRenderers[i17].skipAllRenderPasses()) {
						this.sortedWorldRenderers[i17].isInFrustum = false;
					} else {
						if(!this.sortedWorldRenderers[i17].isInFrustum) {
							this.sortedWorldRenderers[i17].isVisible = true;
						}

						if(this.sortedWorldRenderers[i17].isInFrustum && !this.sortedWorldRenderers[i17].isWaitingOnOcclusionQuery) {
							float f18 = MathHelper.sqrt_float(this.sortedWorldRenderers[i17].distanceToEntitySquared(entityPlayer1));
							int i25 = (int)(1.0F + f18 / 64.0F);
							if(this.cloudOffsetX % i25 == i17 % i25) {
								WorldRenderer worldRenderer26;
								float f19 = (float)((double)(worldRenderer26 = this.sortedWorldRenderers[i17]).posXMinus - d5);
								float f20 = (float)((double)worldRenderer26.posYMinus - d7);
								f18 = (float)((double)worldRenderer26.posZMinus - d9);
								f19 -= f23;
								f20 -= f24;
								f18 -= f16;
								if(f19 != 0.0F || f20 != 0.0F || f18 != 0.0F) {
									GL11.glTranslatef(f19, f20, f18);
									f23 += f19;
									f24 += f20;
									f16 += f18;
								}

								ARBOcclusionQuery.glBeginQueryARB(GL15.GL_SAMPLES_PASSED, this.sortedWorldRenderers[i17].glOcclusionQuery);
								this.sortedWorldRenderers[i17].callOcclusionQueryList();
								ARBOcclusionQuery.glEndQueryARB(GL15.GL_SAMPLES_PASSED);
								this.sortedWorldRenderers[i17].isWaitingOnOcclusionQuery = true;
							}
						}
					}
				}

				GL11.glPopMatrix();
				GL11.glColorMask(true, true, true, true);
				GL11.glDepthMask(true);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_FOG);
				i21 += this.renderSortedRenderers(i12, i22, i2, d3);
			} while(i22 < this.sortedWorldRenderers.length);
		} else {
			i21 = 0 + this.renderSortedRenderers(0, this.sortedWorldRenderers.length, i2, d3);
		}

		return i21;
	}

	private void checkOcclusionQueryResult(int i1, int i2) {
		for(i1 = i1; i1 < i2; ++i1) {
			if(this.sortedWorldRenderers[i1].isWaitingOnOcclusionQuery) {
				this.occlusionResult.clear();
				ARBOcclusionQuery.glGetQueryObjectuARB(this.sortedWorldRenderers[i1].glOcclusionQuery, GL15.GL_QUERY_RESULT_AVAILABLE, this.occlusionResult);
				if(this.occlusionResult.get(0) != 0) {
					this.sortedWorldRenderers[i1].isWaitingOnOcclusionQuery = false;
					this.occlusionResult.clear();
					ARBOcclusionQuery.glGetQueryObjectuARB(this.sortedWorldRenderers[i1].glOcclusionQuery, GL15.GL_QUERY_RESULT, this.occlusionResult);
					this.sortedWorldRenderers[i1].isVisible = this.occlusionResult.get(0) != 0;
				}
			}
		}

	}

	private int renderSortedRenderers(int i1, int i2, int i3, double d4) {
		this.glRenderLists.clear();
		int i6 = 0;

		for(i1 = i1; i1 < i2; ++i1) {
			if(i3 == 0) {
				++this.renderersLoaded;
				if(!this.sortedWorldRenderers[i1].isInFrustum) {
					++this.renderersBeingClipped;
				}

				if(this.sortedWorldRenderers[i1].isInFrustum && !this.sortedWorldRenderers[i1].isVisible) {
					++this.renderersBeingOccluded;
				}

				if(this.sortedWorldRenderers[i1].isInFrustum && this.sortedWorldRenderers[i1].isVisible) {
					++this.renderersBeingRendered;
				}
			}

			if(this.sortedWorldRenderers[i1].isInFrustum && this.sortedWorldRenderers[i1].isVisible && this.sortedWorldRenderers[i1].getGLCallListForPass(i3) >= 0) {
				this.glRenderLists.add(this.sortedWorldRenderers[i1]);
				++i6;
			}
		}

		this.renderAllRenderLists(i3, d4);
		return i6;
	}

	public final void renderAllRenderLists(int i1, double d2) {
		EntityPlayerSP entityPlayerSP4 = this.mc.thePlayer;
		double d5 = this.mc.thePlayer.lastTickPosX + (entityPlayerSP4.posX - entityPlayerSP4.lastTickPosX) * d2;
		double d7 = entityPlayerSP4.lastTickPosY + (entityPlayerSP4.posY - entityPlayerSP4.lastTickPosY) * d2;
		double d9 = entityPlayerSP4.lastTickPosZ + (entityPlayerSP4.posZ - entityPlayerSP4.lastTickPosZ) * d2;
		GL11.glPushMatrix();
		float f16 = 0.0F;
		float f3 = 0.0F;
		float f17 = 0.0F;

		for(int i11 = 0; i11 < this.glRenderLists.size(); ++i11) {
			WorldRenderer worldRenderer12;
			float f13 = (float)((double)(worldRenderer12 = (WorldRenderer)this.glRenderLists.get(i11)).posXMinus - d5);
			float f14 = (float)((double)worldRenderer12.posYMinus - d7);
			float f15 = (float)((double)worldRenderer12.posZMinus - d9);
			f13 -= f16;
			f14 -= f3;
			f15 -= f17;
			if(f13 != 0.0F || f14 != 0.0F || f15 != 0.0F) {
				GL11.glTranslatef(f13, f14, f15);
				f16 += f13;
				f3 += f14;
				f17 += f15;
			}

			GL11.glCallList(worldRenderer12.getGLCallListForPass(i1));
		}

		GL11.glPopMatrix();
	}

	public final void updateClouds() {
		++this.cloudOffsetX;
	}

	public final void renderSky(float f1) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		float f2 = (float)(this.mc.thePlayer.lastTickPosY + (this.mc.thePlayer.posY - this.mc.thePlayer.lastTickPosY) * (double)f1);
		Vec3D vec3D3;
		float f4 = (float)(vec3D3 = this.worldObj.getSkyColor(f1)).xCoord;
		float f5 = (float)vec3D3.yCoord;
		float f20 = (float)vec3D3.zCoord;
		float f6;
		if(this.mc.gameSettings.anaglyph) {
			f6 = (f4 * 30.0F + f5 * 59.0F + f20 * 11.0F) / 100.0F;
			f5 = (f4 * 30.0F + f5 * 70.0F) / 100.0F;
			f20 = (f4 * 30.0F + f20 * 70.0F) / 100.0F;
			f4 = f6;
			f5 = f5;
			f20 = f20;
		}

		GL11.glColor3f(f4, f5, f20);
		Tessellator tessellator21 = Tessellator.instance;
		GL11.glDepthMask(false);
		GL11.glCallList(this.glSkyList2);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslatef(0.0F, 0.0F, 0.0F);
		GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(this.worldObj.getCelestialAngle(f1) * 360.0F, 1.0F, 0.0F, 0.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain/sun.png"));
		tessellator21.startDrawingQuads();
		tessellator21.addVertexWithUV(-30.0D, 100.0D, -30.0D, 0.0D, 0.0D);
		tessellator21.addVertexWithUV(30.0D, 100.0D, -30.0D, 1.0D, 0.0D);
		tessellator21.addVertexWithUV(30.0D, 100.0D, 30.0D, 1.0D, 1.0D);
		tessellator21.addVertexWithUV(-30.0D, 100.0D, 30.0D, 0.0D, 1.0D);
		tessellator21.draw();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain/moon.png"));
		tessellator21.startDrawingQuads();
		tessellator21.addVertexWithUV(-20.0D, -100.0D, 20.0D, 1.0D, 1.0D);
		tessellator21.addVertexWithUV(20.0D, -100.0D, 20.0D, 0.0D, 1.0D);
		tessellator21.addVertexWithUV(20.0D, -100.0D, -20.0D, 0.0D, 0.0D);
		tessellator21.addVertexWithUV(-20.0D, -100.0D, -20.0D, 1.0D, 0.0D);
		tessellator21.draw();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		float f7;
		GL11.glColor4f(f7 = this.worldObj.getStarBrightness(f1), f7, f7, f7);
		GL11.glCallList(this.glSkyList);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_FOG);
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/clouds.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Vec3D vec3D22;
		f5 = (float)(vec3D22 = this.worldObj.getCloudColor(f1)).xCoord;
		f6 = (float)vec3D22.yCoord;
		f4 = (float)vec3D22.zCoord;
		if(this.mc.gameSettings.anaglyph) {
			f7 = (f5 * 30.0F + f6 * 59.0F + f4 * 11.0F) / 100.0F;
			float f15 = (f5 * 30.0F + f6 * 70.0F) / 100.0F;
			float f16 = (f5 * 30.0F + f4 * 70.0F) / 100.0F;
			f5 = f7;
			f6 = f15;
			f4 = f16;
		}

		double d26 = this.worldObj.playerEntity.prevPosX + (this.worldObj.playerEntity.posX - this.worldObj.playerEntity.prevPosX) * (double)f1 + (double)(((float)this.cloudOffsetX + f1) * 0.03F);
		double d17 = this.worldObj.playerEntity.prevPosZ + (this.worldObj.playerEntity.posZ - this.worldObj.playerEntity.prevPosZ) * (double)f1;
		int i19 = MathHelper.floor_double(d26 / 2048.0D);
		int i25 = MathHelper.floor_double(d17 / 2048.0D);
		d26 -= (double)(i19 << 11);
		d17 -= (double)(i25 << 11);
		f1 = 120.0F - f2 + 0.33F;
		f2 = (float)(d26 * 4.8828125E-4D);
		f7 = (float)(d17 * 4.8828125E-4D);
		tessellator21.startDrawingQuads();
		tessellator21.setColorOpaque_F(f5, f6, f4);

		for(int i23 = -256; i23 < 256; i23 += 32) {
			for(int i24 = -256; i24 < 256; i24 += 32) {
				tessellator21.addVertexWithUV((double)i23, (double)f1, (double)(i24 + 32), (double)((float)i23 * 4.8828125E-4F + f2), (double)((float)(i24 + 32) * 4.8828125E-4F + f7));
				tessellator21.addVertexWithUV((double)(i23 + 32), (double)f1, (double)(i24 + 32), (double)((float)(i23 + 32) * 4.8828125E-4F + f2), (double)((float)(i24 + 32) * 4.8828125E-4F + f7));
				tessellator21.addVertexWithUV((double)(i23 + 32), (double)f1, (double)i24, (double)((float)(i23 + 32) * 4.8828125E-4F + f2), (double)((float)i24 * 4.8828125E-4F + f7));
				tessellator21.addVertexWithUV((double)i23, (double)f1, (double)i24, (double)((float)i23 * 4.8828125E-4F + f2), (double)((float)i24 * 4.8828125E-4F + f7));
				tessellator21.addVertexWithUV((double)i23, (double)f1, (double)i24, (double)((float)i23 * 4.8828125E-4F + f2), (double)((float)i24 * 4.8828125E-4F + f7));
				tessellator21.addVertexWithUV((double)(i23 + 32), (double)f1, (double)i24, (double)((float)(i23 + 32) * 4.8828125E-4F + f2), (double)((float)i24 * 4.8828125E-4F + f7));
				tessellator21.addVertexWithUV((double)(i23 + 32), (double)f1, (double)(i24 + 32), (double)((float)(i23 + 32) * 4.8828125E-4F + f2), (double)((float)(i24 + 32) * 4.8828125E-4F + f7));
				tessellator21.addVertexWithUV((double)i23, (double)f1, (double)(i24 + 32), (double)((float)i23 * 4.8828125E-4F + f2), (double)((float)(i24 + 32) * 4.8828125E-4F + f7));
			}
		}

		tessellator21.draw();
	}

	public final void updateRenderers(EntityPlayer entityPlayer1) {
		Collections.sort(this.worlRenderersToUpdate, new RenderSorter(entityPlayer1));
		int i2 = this.worlRenderersToUpdate.size() - 1;
		int i3 = this.worlRenderersToUpdate.size();

		for(int i4 = 0; i4 < i3; ++i4) {
			WorldRenderer worldRenderer5;
			if((worldRenderer5 = (WorldRenderer)this.worlRenderersToUpdate.get(i2 - i4)).distanceToEntitySquared(entityPlayer1) > 2500.0F && i4 > 2) {
				return;
			}

			this.worlRenderersToUpdate.remove(worldRenderer5);
			worldRenderer5.updateRenderer();
			worldRenderer5.needsUpdate = false;
		}

	}

	public final void drawBlockBreaking(EntityPlayer entityPlayer1, MovingObjectPosition movingObjectPosition2, int i3, ItemStack itemStack4, float f5) {
		Tessellator tessellator16 = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, (MathHelper.sin((float)System.currentTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F);
		if(this.damagePartialTime > 0.0F) {
			GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
			int i17 = this.renderEngine.getTexture("/terrain.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, i17);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
			GL11.glPushMatrix();
			Block block18 = (i17 = this.worldObj.getBlockId(movingObjectPosition2.blockX, movingObjectPosition2.blockY, movingObjectPosition2.blockZ)) > 0 ? Block.blocksList[i17] : null;
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glPolygonOffset(-1.0F, -1.0F);
			GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
			tessellator16.startDrawingQuads();
			double d10 = entityPlayer1.lastTickPosX + (entityPlayer1.posX - entityPlayer1.lastTickPosX) * (double)f5;
			double d12 = entityPlayer1.lastTickPosY + (entityPlayer1.posY - entityPlayer1.lastTickPosY) * (double)f5;
			double d14 = entityPlayer1.lastTickPosZ + (entityPlayer1.posZ - entityPlayer1.lastTickPosZ) * (double)f5;
			tessellator16.setTranslationD(-d10, -d12, -d14);
			tessellator16.disableColor();
			if(block18 == null) {
				block18 = Block.stone;
			}

			this.globalRenderBlocks.renderBlockUsingTexture(block18, movingObjectPosition2.blockX, movingObjectPosition2.blockY, movingObjectPosition2.blockZ, 240 + (int)(this.damagePartialTime * 10.0F));
			tessellator16.draw();
			tessellator16.setTranslationD(0.0D, 0.0D, 0.0D);
			GL11.glPolygonOffset(0.0F, 0.0F);
			GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glDepthMask(true);
			GL11.glPopMatrix();
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}

	public final void drawSelectionBox(EntityPlayer entityPlayer1, MovingObjectPosition movingObjectPosition2, int i3, float f4) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(false);
		if((i3 = this.worldObj.getBlockId(movingObjectPosition2.blockX, movingObjectPosition2.blockY, movingObjectPosition2.blockZ)) > 0) {
			double d6 = entityPlayer1.lastTickPosX + (entityPlayer1.posX - entityPlayer1.lastTickPosX) * (double)f4;
			double d8 = entityPlayer1.lastTickPosY + (entityPlayer1.posY - entityPlayer1.lastTickPosY) * (double)f4;
			double d10 = entityPlayer1.lastTickPosZ + (entityPlayer1.posZ - entityPlayer1.lastTickPosZ) * (double)f4;
			AxisAlignedBB axisAlignedBB12 = Block.blocksList[i3].getSelectedBoundingBoxFromPool(movingObjectPosition2.blockX, movingObjectPosition2.blockY, movingObjectPosition2.blockZ).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offsetCopy(-d6, -d8, -d10);
			Tessellator tessellator13 = Tessellator.instance;
			Tessellator.instance.startDrawing(3);
			tessellator13.addVertex(axisAlignedBB12.minX, axisAlignedBB12.minY, axisAlignedBB12.minZ);
			tessellator13.addVertex(axisAlignedBB12.maxX, axisAlignedBB12.minY, axisAlignedBB12.minZ);
			tessellator13.addVertex(axisAlignedBB12.maxX, axisAlignedBB12.minY, axisAlignedBB12.maxZ);
			tessellator13.addVertex(axisAlignedBB12.minX, axisAlignedBB12.minY, axisAlignedBB12.maxZ);
			tessellator13.addVertex(axisAlignedBB12.minX, axisAlignedBB12.minY, axisAlignedBB12.minZ);
			tessellator13.draw();
			tessellator13.startDrawing(3);
			tessellator13.addVertex(axisAlignedBB12.minX, axisAlignedBB12.maxY, axisAlignedBB12.minZ);
			tessellator13.addVertex(axisAlignedBB12.maxX, axisAlignedBB12.maxY, axisAlignedBB12.minZ);
			tessellator13.addVertex(axisAlignedBB12.maxX, axisAlignedBB12.maxY, axisAlignedBB12.maxZ);
			tessellator13.addVertex(axisAlignedBB12.minX, axisAlignedBB12.maxY, axisAlignedBB12.maxZ);
			tessellator13.addVertex(axisAlignedBB12.minX, axisAlignedBB12.maxY, axisAlignedBB12.minZ);
			tessellator13.draw();
			tessellator13.startDrawing(1);
			tessellator13.addVertex(axisAlignedBB12.minX, axisAlignedBB12.minY, axisAlignedBB12.minZ);
			tessellator13.addVertex(axisAlignedBB12.minX, axisAlignedBB12.maxY, axisAlignedBB12.minZ);
			tessellator13.addVertex(axisAlignedBB12.maxX, axisAlignedBB12.minY, axisAlignedBB12.minZ);
			tessellator13.addVertex(axisAlignedBB12.maxX, axisAlignedBB12.maxY, axisAlignedBB12.minZ);
			tessellator13.addVertex(axisAlignedBB12.maxX, axisAlignedBB12.minY, axisAlignedBB12.maxZ);
			tessellator13.addVertex(axisAlignedBB12.maxX, axisAlignedBB12.maxY, axisAlignedBB12.maxZ);
			tessellator13.addVertex(axisAlignedBB12.minX, axisAlignedBB12.minY, axisAlignedBB12.maxZ);
			tessellator13.addVertex(axisAlignedBB12.minX, axisAlignedBB12.maxY, axisAlignedBB12.maxZ);
			tessellator13.draw();
		}

		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void markBlocksForUpdate(int i1, int i2, int i3, int i4, int i5, int i6) {
		i1 >>= 4;
		i2 >>= 4;
		i3 >>= 4;
		i4 >>= 4;
		i5 >>= 4;
		i6 >>= 4;

		for(i1 = i1; i1 <= i4; ++i1) {
			int i7;
			if((i7 = i1 % this.renderChunksWide) < 0) {
				i7 += this.renderChunksWide;
			}

			for(int i8 = i2; i8 <= i5; ++i8) {
				int i9;
				if((i9 = i8 % this.renderChunksTall) < 0) {
					i9 += this.renderChunksTall;
				}

				for(int i10 = i3; i10 <= i6; ++i10) {
					int i11;
					if((i11 = i10 % this.renderChunksDeep) < 0) {
						i11 += this.renderChunksDeep;
					}

					i11 = (i11 * this.renderChunksTall + i9) * this.renderChunksWide + i7;
					WorldRenderer worldRenderer12;
					if(!(worldRenderer12 = this.worldRenderers[i11]).needsUpdate) {
						worldRenderer12.needsUpdate = true;
						this.worlRenderersToUpdate.add(worldRenderer12);
					}
				}
			}
		}

	}

	public final void markBlockAndNeighborsNeedsUpdate(int i1, int i2, int i3) {
		this.markBlocksForUpdate(i1 - 1, i2 - 1, i3 - 1, i1 + 1, i2 + 1, i3 + 1);
	}

	public final void markBlockRangeNeedsUpdate(int i1, int i2, int i3, int i4, int i5, int i6) {
		this.markBlocksForUpdate(i1 - 1, i2 - 1, i3 - 1, i4 + 1, i5 + 1, i6 + 1);
	}

	public final void clipRenderersByFrustrum(Frustrum frustrum1) {
		for(int i2 = 0; i2 < this.worldRenderers.length; ++i2) {
			this.worldRenderers[i2].updateInFrustrum(frustrum1);
		}

	}

	public final void playSound(String string1, double d2, double d4, double d6, float f8, float f9) {
		this.mc.sndManager.playSound(string1, (float)d2, (float)d4, (float)d6, f8, f9);
	}

	public final void spawnParticle(String string1, double d2, double d4, double d6, double d8, double d10, double d12) {
		double d14 = this.worldObj.playerEntity.posX - d2;
		double d16 = this.worldObj.playerEntity.posY - d4;
		double d18 = this.worldObj.playerEntity.posZ - d6;
		if(d14 * d14 + d16 * d16 + d18 * d18 <= 256.0D) {
			if(string1 == "bubble") {
				this.mc.effectRenderer.addEffect(new EntityBubbleFX(this.worldObj, d2, d4, d6, d8, d10, d12));
			} else if(string1 == "smoke") {
				this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, d2, d4, d6));
			} else if(string1 == "explode") {
				this.mc.effectRenderer.addEffect(new EntityExplodeFX(this.worldObj, d2, d4, d6, d8, d10, d12));
			} else if(string1 == "flame") {
				this.mc.effectRenderer.addEffect(new EntityFlameFX(this.worldObj, d2, d4, d6));
			} else if(string1 == "lava") {
				this.mc.effectRenderer.addEffect(new EntityLavaFX(this.worldObj, d2, d4, d6));
			} else if(string1 == "splash") {
				this.mc.effectRenderer.addEffect(new EntitySplashFX(this.worldObj, d2, d4, d6));
			} else {
				if(string1 == "largesmoke") {
					this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, d2, d4, d6, 2.5F));
				}

			}
		}
	}

	public final void obtainEntitySkin(Entity entity1) {
		if(entity1.skinUrl != null) {
			this.renderEngine.obtainImageData(entity1.skinUrl, new ImageBufferDownload());
		}

	}

	public final void releaseEntitySkin(Entity entity1) {
		if(entity1.skinUrl != null) {
			this.renderEngine.releaseImageData(entity1.skinUrl);
		}

	}

	public final void updateAllRenderers() {
		for(int i1 = 0; i1 < this.worldRenderers.length; ++i1) {
			if(!this.worldRenderers[i1].needsUpdate && this.worldRenderers[i1].isChunkLit) {
				this.worldRenderers[i1].needsUpdate = true;
				this.worlRenderersToUpdate.add(this.worldRenderers[i1]);
			}
		}

	}
}