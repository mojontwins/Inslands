package net.minecraft.client.renderer;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.client.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityBreakingFX;
import net.minecraft.client.particle.EntityBubbleFX;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.particle.EntityExplodeFX;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.client.particle.EntityFootStepFX;
import net.minecraft.client.particle.EntityGlowdustFX;
import net.minecraft.client.particle.EntityHeartFX;
import net.minecraft.client.particle.EntityLavaFX;
import net.minecraft.client.particle.EntityNoteFX;
import net.minecraft.client.particle.EntityPortalFX;
import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.client.particle.EntitySlimeFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.particle.EntitySnowShovelFX;
import net.minecraft.client.particle.EntitySplashFX;
import net.minecraft.client.particle.EntityStatusEffectFX;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.EntitySorter;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemRecord;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.IWorldAccess;
import net.minecraft.world.level.World;
import net.minecraft.world.level.theme.LevelThemeGlobalSettings;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockLeavesBase;
import net.minecraft.world.level.tile.entity.TileEntity;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.EnumMovingObjectType;
import net.minecraft.world.phys.MovingObjectPosition;
import net.minecraft.world.phys.Vec3D;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ARBOcclusionQuery;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class LevelRenderer implements IWorldAccess {
	public List<TileEntity> tileEntities = new ArrayList<TileEntity>();
	public World worldObj;
	public RenderEngine renderEngine;
	public List<WorldRenderer> worldRenderersToUpdate = new ArrayList<WorldRenderer>();
	private final Set<WorldRenderer> worldRenderersToUpdateSet = new HashSet<WorldRenderer>();
	private WorldRenderer[] sortedWorldRenderers;
	private WorldRenderer[] worldRenderers;
	private int renderChunksWide;
	private int renderChunksTall;
	private int renderChunksDeep;
	private int glRenderListBase;
	public Minecraft mc;
	public RenderBlocks globalRenderBlocks;
	private IntBuffer glOcclusionQueryBase;
	private boolean occlusionEnabled = false;
	private int cloudOffsetX = 0;
	private int starGLCallList;
	private int glSkyList;
	private int glSkyList2;
	private int minBlockX;
	private int minBlockY;
	private int minBlockZ;
	private int maxBlockX;
	private int maxBlockY;
	private int maxBlockZ;
	private int renderDistance = -1;
	private int renderEntitiesStartupCounter = 2;
	private int countEntitiesTotal;
	private int countEntitiesRendered;
	private int countEntitiesHidden;
	int[] dummyBuf50k = new int[50000];
	IntBuffer occlusionResult = GLAllocation.createDirectIntBuffer(64);
	private int renderersLoaded;
	private int renderersBeingClipped;
	private int renderersBeingOccluded;
	private int renderersBeingRendered;
	private int renderersSkippingRenderPass;
	private int worldRenderersCheckIndex;
	private IntBuffer glListBuffer = BufferUtils.createIntBuffer(65536);
	double prevSortX = -9999.0D;
	double prevSortY = -9999.0D;
	double prevSortZ = -9999.0D;
	public float damagePartialTime;
	int frustumCheckOffset = 0;
	double prevReposX;
	double prevReposY;
	double prevReposZ;
	private long lastMovedTime = System.currentTimeMillis();
	private long lastActionTime = System.currentTimeMillis();

	// Fixed-name particle types keyed by the exact string World.spawnParticle() passes in.
	// Lava, footstep and the cracked_* variants are handled specially because their spawners
	// take different constructor arguments from the rest.
	private static final HashMap<String, Integer> PARTICLE_TYPE_BY_NAME = new HashMap<String, Integer>();
	static {
		PARTICLE_TYPE_BY_NAME.put("bubble", Integer.valueOf(0));
		PARTICLE_TYPE_BY_NAME.put("smoke", Integer.valueOf(1));
		PARTICLE_TYPE_BY_NAME.put("note", Integer.valueOf(2));
		PARTICLE_TYPE_BY_NAME.put("portal", Integer.valueOf(3));
		PARTICLE_TYPE_BY_NAME.put("explode", Integer.valueOf(4));
		PARTICLE_TYPE_BY_NAME.put("flame", Integer.valueOf(5));
		PARTICLE_TYPE_BY_NAME.put("lava", Integer.valueOf(6));
		PARTICLE_TYPE_BY_NAME.put("footstep", Integer.valueOf(7));
		PARTICLE_TYPE_BY_NAME.put("splash", Integer.valueOf(8));
		PARTICLE_TYPE_BY_NAME.put("largesmoke", Integer.valueOf(9));
		PARTICLE_TYPE_BY_NAME.put("reddust", Integer.valueOf(10));
		PARTICLE_TYPE_BY_NAME.put("glowdust", Integer.valueOf(11));
		PARTICLE_TYPE_BY_NAME.put("snowballpoof", Integer.valueOf(12));
		PARTICLE_TYPE_BY_NAME.put("crackedpebble", Integer.valueOf(13));
		PARTICLE_TYPE_BY_NAME.put("crackedpotion", Integer.valueOf(14));
		PARTICLE_TYPE_BY_NAME.put("snowshovel", Integer.valueOf(15));
		PARTICLE_TYPE_BY_NAME.put("slime", Integer.valueOf(16));
		PARTICLE_TYPE_BY_NAME.put("heart", Integer.valueOf(17));
		PARTICLE_TYPE_BY_NAME.put("status_effect", Integer.valueOf(18));
	}

	public LevelRenderer(Minecraft minecraft, RenderEngine renderengine) {
		this.mc = minecraft;
		this.renderEngine = renderengine;
		byte maxChunkDim = 65;
		byte maxChunkHeight = 16;
		this.glRenderListBase = GLAllocation.generateDisplayLists(maxChunkDim * maxChunkDim * maxChunkHeight * 3);
		this.occlusionEnabled = minecraft.getOpenGlCapsChecker().checkARBOcclusion();
		if(this.occlusionEnabled) {
			this.occlusionResult.clear();
			this.glOcclusionQueryBase = GLAllocation.createDirectIntBuffer(maxChunkDim * maxChunkDim * maxChunkHeight);
			this.glOcclusionQueryBase.clear();
			this.glOcclusionQueryBase.position(0);
			this.glOcclusionQueryBase.limit(maxChunkDim * maxChunkDim * maxChunkHeight);
			ARBOcclusionQuery.glGenQueriesARB(this.glOcclusionQueryBase);
		}

		this.starGLCallList = GLAllocation.generateDisplayLists(3);
		GL11.glPushMatrix();
		GL11.glNewList(this.starGLCallList, GL11.GL_COMPILE);
		this.renderStars();
		GL11.glEndList();
		GL11.glPopMatrix();
		Tessellator tessellator = Tessellator.instance;
		this.glSkyList = this.starGLCallList + 1;
		GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
		byte byte1 = 64;
		int i = 256 / byte1 + 2;
		float f = 16.0F;

		int k;
		int i1;
		for(k = -byte1 * i; k <= byte1 * i; k += byte1) {
			for(i1 = -byte1 * i; i1 <= byte1 * i; i1 += byte1) {
				tessellator.startDrawingQuads();
				tessellator.addVertex((double)(k + 0), (double)f, (double)(i1 + 0));
				tessellator.addVertex((double)(k + byte1), (double)f, (double)(i1 + 0));
				tessellator.addVertex((double)(k + byte1), (double)f, (double)(i1 + byte1));
				tessellator.addVertex((double)(k + 0), (double)f, (double)(i1 + byte1));
				tessellator.draw();
			}
		}

		GL11.glEndList();
		this.glSkyList2 = this.starGLCallList + 2;
		GL11.glNewList(this.glSkyList2, GL11.GL_COMPILE);
		f = -16.0F;
		tessellator.startDrawingQuads();

		for(k = -byte1 * i; k <= byte1 * i; k += byte1) {
			for(i1 = -byte1 * i; i1 <= byte1 * i; i1 += byte1) {
				tessellator.addVertex((double)(k + byte1), (double)f, (double)(i1 + 0));
				tessellator.addVertex((double)(k + 0), (double)f, (double)(i1 + 0));
				tessellator.addVertex((double)(k + 0), (double)f, (double)(i1 + byte1));
				tessellator.addVertex((double)(k + byte1), (double)f, (double)(i1 + byte1));
			}
		}

		tessellator.draw();
		GL11.glEndList();
		this.renderEngine.updateDynamicTextures();
	}

	private void renderStars() {
		Random random = new Random(10842L);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		for(int i = 0; i < 1500; ++i) {
			double d = (double)(random.nextFloat() * 2.0F - 1.0F);
			double d1 = (double)(random.nextFloat() * 2.0F - 1.0F);
			double d2 = (double)(random.nextFloat() * 2.0F - 1.0F);
			double d3 = (double)(0.25F + random.nextFloat() * 0.25F);
			double d4 = d * d + d1 * d1 + d2 * d2;
			if(d4 < 1.0D && d4 > 0.01D) {
				d4 = 1.0D / Math.sqrt(d4);
				d *= d4;
				d1 *= d4;
				d2 *= d4;
				double d5 = d * 100.0D;
				double d6 = d1 * 100.0D;
				double d7 = d2 * 100.0D;
				double d8 = Math.atan2(d, d2);
				double d9 = Math.sin(d8);
				double d10 = Math.cos(d8);
				double d11 = Math.atan2(Math.sqrt(d * d + d2 * d2), d1);
				double d12 = Math.sin(d11);
				double d13 = Math.cos(d11);
				double d14 = random.nextDouble() * Math.PI * 2.0D;
				double d15 = Math.sin(d14);
				double d16 = Math.cos(d14);

				for(int j = 0; j < 4; ++j) {
					double d17 = 0.0D;
					double d18 = (double)((j & 2) - 1) * d3;
					double d19 = (double)((j + 1 & 2) - 1) * d3;
					double d20 = d18 * d16 - d19 * d15;
					double d21 = d19 * d16 + d18 * d15;
					double d22 = d20 * d12 + d17 * d13;
					double d23 = d17 * d12 - d20 * d13;
					double d24 = d23 * d9 - d21 * d10;
					double d25 = d21 * d9 + d23 * d10;
					tessellator.addVertex(d5 + d24, d6 + d22, d7 + d25);
				}
			}
		}

		tessellator.draw();
	}

	public void changeWorld(World world) {
		if(this.worldObj != null) {
			this.worldObj.removeWorldAccess(this);
		}

		this.prevSortX = -9999.0D;
		this.prevSortY = -9999.0D;
		this.prevSortZ = -9999.0D;
		RenderManager.instance.set(world);
		this.worldObj = world;
		this.globalRenderBlocks = new RenderBlocks(world);
		if(world != null) {
			world.addWorldAccess(this);
			this.loadRenderers();
		}

	}

	public void loadRenderers() {
		if(this.worldObj != null) {
			BlockLeavesBase.setGraphicsLevel(Config.isTreesFancy());
			
			this.renderDistance = this.mc.gameSettings.renderDistance;
			int numBlocks;
			if(this.worldRenderers != null) {
				for(numBlocks = 0; numBlocks < this.worldRenderers.length; ++numBlocks) {
					this.worldRenderers[numBlocks].stopRendering();
				}
			}

			numBlocks = 64 << 3 - this.renderDistance;
			if(numBlocks > 400) {
				numBlocks = 400;
			}
	
			this.prevReposX = -9999.0D;
			this.prevReposY = -9999.0D;
			this.prevReposZ = -9999.0D;
			this.renderChunksWide = numBlocks / 16 + 1;
			this.renderChunksTall = 8;
			this.renderChunksDeep = numBlocks / 16 + 1;
			this.worldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
			this.sortedWorldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
			int i2 = 0;
			int i3 = 0;
			this.minBlockX = 0;
			this.minBlockY = 0;
			this.minBlockZ = 0;
			this.maxBlockX = this.renderChunksWide;
			this.maxBlockY = this.renderChunksTall;
			this.maxBlockZ = this.renderChunksDeep;

			int i4;
			for(i4 = 0; i4 < this.worldRenderersToUpdate.size(); ++i4) {
				WorldRenderer wr = (WorldRenderer)this.worldRenderersToUpdate.get(i4);
				if(wr != null) {
					wr.needsUpdate = false;
				}
			}

			this.worldRenderersToUpdateSet.clear();
			this.worldRenderersToUpdate.clear();
			this.tileEntities.clear();

			for(i4 = 0; i4 < this.renderChunksWide; ++i4) {
				for(int i5 = 0; i5 < this.renderChunksTall; ++i5) {
					for(int i6 = 0; i6 < this.renderChunksDeep; ++i6) {
						int wri = (i6 * this.renderChunksTall + i5) * this.renderChunksWide + i4;
						this.worldRenderers[wri] = new WorldRenderer(this.worldObj, this.tileEntities, i4 * 16, i5 * 16, i6 * 16, this.glRenderListBase + i2);
						if(this.occlusionEnabled) {
							this.worldRenderers[wri].glOcclusionQuery = this.glOcclusionQueryBase.get(i3);
						}

						this.worldRenderers[wri].isWaitingOnOcclusionQuery = false;
						this.worldRenderers[wri].isVisible = true;
						this.worldRenderers[wri].isInFrustum = false;
						this.worldRenderers[wri].chunkIndex = i3++;
						this.worldRenderers[wri].markDirty();
						this.sortedWorldRenderers[wri] = this.worldRenderers[wri];
						this.worldRenderersToUpdateSet.add(this.worldRenderers[wri]);
						this.worldRenderersToUpdate.add(this.worldRenderers[wri]);
						i2 += 3;
					}
				}
			}

			if(this.worldObj != null) {
				EntityLiving entityLiving7 = this.mc.renderViewEntity;				
				if (entityLiving7 == null) {
					entityLiving7 = this.mc.thePlayer;
				}

				if(entityLiving7 != null) {
					this.markRenderersForNewPosition(MathHelper.floor_double(entityLiving7.posX), MathHelper.floor_double(entityLiving7.posY), MathHelper.floor_double(entityLiving7.posZ));
					Arrays.sort(this.sortedWorldRenderers, new EntitySorter(entityLiving7));
				}
			}

			this.renderEntitiesStartupCounter = 2;
		}
	}

	public void renderEntities(Vec3D renderPos, ICamera camera, float partialTicks) {
		if(this.renderEntitiesStartupCounter > 0) {
			--this.renderEntitiesStartupCounter;
		} else {
			TileEntityRenderer.instance.cacheActiveRenderInfo(this.worldObj, this.renderEngine, this.mc.fontRenderer, this.mc.renderViewEntity, partialTicks);
			RenderManager.instance.cacheActiveRenderInfo(this.worldObj, this.renderEngine, this.mc.fontRenderer, this.mc.renderViewEntity, this.mc.gameSettings, partialTicks);
			this.countEntitiesTotal = 0;
			this.countEntitiesRendered = 0;
			this.countEntitiesHidden = 0;
			EntityLiving viewEntity = this.mc.renderViewEntity;
			RenderManager.renderPosX = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * (double)partialTicks;
			RenderManager.renderPosY = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * (double)partialTicks;
			RenderManager.renderPosZ = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * (double)partialTicks;
			TileEntityRenderer.staticPlayerX = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * (double)partialTicks;
			TileEntityRenderer.staticPlayerY = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * (double)partialTicks;
			TileEntityRenderer.staticPlayerZ = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * (double)partialTicks;
			this.mc.entityRenderer.enableLightmap((double)partialTicks);
			List<Entity> entities = this.worldObj.getLoadedEntityList();
			this.countEntitiesTotal = entities.size();

			int i;
			Entity entity;
			for(i = 0; i < this.worldObj.weatherEffects.size(); ++i) {
				entity = (Entity)this.worldObj.weatherEffects.get(i);
				++this.countEntitiesRendered;
				if(entity.isInRangeToRenderVec3D(renderPos)) {
					RenderManager.instance.renderEntity(entity, partialTicks);
				}
			}

			// PERRO
	
			for(i = 0; i < entities.size(); ++i) {
				entity = (Entity)entities.get(i);
				if(entity.isInRangeToRenderVec3D(renderPos) && (entity.ignoreFrustumCheck || camera.isBoundingBoxInFrustum(entity.boundingBox)) && (entity != this.mc.renderViewEntity || this.mc.gameSettings.thirdPersonView || this.mc.renderViewEntity.isPlayerSleeping()) && this.worldObj.blockExists(MathHelper.floor_double(entity.posX), 0, MathHelper.floor_double(entity.posZ))) {
					++this.countEntitiesRendered;
					RenderManager.instance.renderEntity(entity, partialTicks);
				}
			}

			RenderHelper.enableStandardItemLighting();

			for(i = 0; i < this.tileEntities.size(); ++i) {
				TileEntityRenderer.instance.renderTileEntity((TileEntity)this.tileEntities.get(i), partialTicks);
			}

			this.mc.entityRenderer.disableLightmap((double)partialTicks);
		}
	}

	public String getDebugInfoRenders() {
		return "C: " + this.renderersBeingRendered + "/" + this.renderersLoaded + ". F: " + this.renderersBeingClipped + ", O: " + this.renderersBeingOccluded + ", E: " + this.renderersSkippingRenderPass;
	}

	public String getDebugInfoEntities() {
		return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ". B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered);
	}

	private void markRenderersForNewPosition(int x, int y, int z) {
		x -= 8;
		y -= 8;
		z -= 8;
		this.minBlockX = Integer.MAX_VALUE;
		this.minBlockY = Integer.MAX_VALUE;
		this.minBlockZ = Integer.MAX_VALUE;
		this.maxBlockX = Integer.MIN_VALUE;
		this.maxBlockY = Integer.MIN_VALUE;
		this.maxBlockZ = Integer.MIN_VALUE;
		int blocksWide = this.renderChunksWide * 16;
		int blocksWide2 = blocksWide / 2;

		for(int ix = 0; ix < this.renderChunksWide; ++ix) {
			int blockX = ix * 16;
			int blockXAbs = blockX + blocksWide2 - x;
			if(blockXAbs < 0) {
				blockXAbs -= blocksWide - 1;
			}

			blockXAbs /= blocksWide;
			blockX -= blockXAbs * blocksWide;
			if(blockX < this.minBlockX) {
				this.minBlockX = blockX;
			}

			if(blockX > this.maxBlockX) {
				this.maxBlockX = blockX;
			}

			for(int iz = 0; iz < this.renderChunksDeep; ++iz) {
				int blockZ = iz * 16;
				int blockZAbs = blockZ + blocksWide2 - z;
				if(blockZAbs < 0) {
					blockZAbs -= blocksWide - 1;
				}

				blockZAbs /= blocksWide;
				blockZ -= blockZAbs * blocksWide;
				if(blockZ < this.minBlockZ) {
					this.minBlockZ = blockZ;
				}

				if(blockZ > this.maxBlockZ) {
					this.maxBlockZ = blockZ;
				}

				for(int iy = 0; iy < this.renderChunksTall; ++iy) {
					int blockY = iy * 16;
					if(blockY < this.minBlockY) {
						this.minBlockY = blockY;
					}

					if(blockY > this.maxBlockY) {
						this.maxBlockY = blockY;
					}

					WorldRenderer worldrenderer = this.worldRenderers[(iz * this.renderChunksTall + iy) * this.renderChunksWide + ix];
					boolean wasNeedingUpdate = worldrenderer.needsUpdate;
					worldrenderer.setPosition(blockX, blockY, blockZ);
					if(!wasNeedingUpdate && worldrenderer.needsUpdate) {
						this.worldRenderersToUpdateSet.add(worldrenderer);
						this.worldRenderersToUpdate.add(worldrenderer);
					}
				}
			}
		}

	}

	public int sortAndRender(EntityLiving viewEntity, int renderPass, double partialTicks) {
		if(this.worldRenderersToUpdate.size() < 10) {
			// Keep the update queue topped up: every frame a few more renderers are scanned and
			// added when dirty. Membership is O(1) via the mirror HashSet.
			for(int i = 0; i < 10; ++i) {
				this.worldRenderersCheckIndex = (this.worldRenderersCheckIndex + 1) % this.worldRenderers.length;
				WorldRenderer renderer = this.worldRenderers[this.worldRenderersCheckIndex];
				if(renderer.needsUpdate && !this.worldRenderersToUpdateSet.contains(renderer)) {
					this.worldRenderersToUpdateSet.add(renderer);
					this.worldRenderersToUpdate.add(renderer);
				}
			}
		}

		if(this.mc.gameSettings.renderDistance != this.renderDistance && !Config.isLoadChunksFar()) {
			this.loadRenderers();
		}

		if(renderPass == 0) {
			this.renderersLoaded = 0;
			this.renderersBeingClipped = 0;
			this.renderersBeingOccluded = 0;
			this.renderersBeingRendered = 0;
			this.renderersSkippingRenderPass = 0;
		}

		// Interpolated camera position for this frame.
		double interpX = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * partialTicks;
		double interpY = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks;
		double interpZ = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * partialTicks;
		double deltaSortX = viewEntity.posX - this.prevSortX;
		double deltaSortY = viewEntity.posY - this.prevSortY;
		double deltaSortZ = viewEntity.posZ - this.prevSortZ;
		double distSqSort = deltaSortX * deltaSortX + deltaSortY * deltaSortY + deltaSortZ * deltaSortZ;
		int renderCount;
		if(distSqSort > 16.0D) {
			// The viewport moved far enough to warrant re-sorting the renderers back-to-front.
			this.prevSortX = viewEntity.posX;
			this.prevSortY = viewEntity.posY;
			this.prevSortZ = viewEntity.posZ;
			int preloadedBlocks = Config.getPreloadedChunks() * 16;
			double deltaReposX = viewEntity.posX - this.prevReposX;
			double deltaReposY = viewEntity.posY - this.prevReposY;
			double deltaReposZ = viewEntity.posZ - this.prevReposZ;
			double reposDistSq = deltaReposX * deltaReposX + deltaReposY * deltaReposY + deltaReposZ * deltaReposZ;
			if(reposDistSq > (double)(preloadedBlocks * preloadedBlocks) + 16.0D) {
				this.prevReposX = viewEntity.posX;
				this.prevReposY = viewEntity.posY;
				this.prevReposZ = viewEntity.posZ;
				this.markRenderersForNewPosition(MathHelper.floor_double(viewEntity.posX), MathHelper.floor_double(viewEntity.posY), MathHelper.floor_double(viewEntity.posZ));
			}

			Arrays.sort(this.sortedWorldRenderers, new EntitySorter(viewEntity));
			// When the player roams too far from the chunk grid origin, rebuild against the new grid.
			int viewChunkX = (int)viewEntity.posX;
			int viewChunkZ = (int)viewEntity.posZ;
			short chunkOffsetLimit = 2000;
			if(Math.abs(viewChunkX - WorldRenderer.globalChunkOffsetX) > chunkOffsetLimit || Math.abs(viewChunkZ - WorldRenderer.globalChunkOffsetZ) > chunkOffsetLimit) {
				WorldRenderer.globalChunkOffsetX = viewChunkX;
				WorldRenderer.globalChunkOffsetZ = viewChunkZ;
				this.loadRenderers();
			}
		}

		RenderHelper.disableStandardItemLighting();
		if(this.mc.gameSettings.ofSmoothFps && renderPass == 0) {
			GL11.glFinish();
		}

		renderCount = 0;
		if(this.occlusionEnabled && this.mc.gameSettings.advancedOpengl && !this.mc.gameSettings.anaglyph && renderPass == 0) {
			// Advanced OpenGL: walk the back-to-front list in growing bands, issuing occlusion
			// queries for far-away chunks while rendering the closest ones immediately.
			byte firstIndex = 0;
			byte initialBandEnd = 20;
			this.checkOcclusionQueryResult(firstIndex, initialBandEnd, viewEntity.posX, viewEntity.posY, viewEntity.posZ);

			int endIndex;
			for(endIndex = firstIndex; endIndex < initialBandEnd; ++endIndex) {
				this.sortedWorldRenderers[endIndex].isVisible = true;
			}

			renderCount = this.renderSortedRenderers(firstIndex, initialBandEnd, renderPass, partialTicks);
			endIndex = initialBandEnd;
			int bandStep = 0;
			byte bandSize = 30;

			int startIndex;
			for(int halfBandWidth = this.renderChunksWide / 2; endIndex < this.sortedWorldRenderers.length; renderCount += this.renderSortedRenderers(startIndex, endIndex, renderPass, partialTicks)) {
				startIndex = endIndex;
				if(bandStep < halfBandWidth) {
					++bandStep;
				} else {
					--bandStep;
				}

				endIndex += bandStep * bandSize;
				if(endIndex <= startIndex) {
					endIndex = startIndex + 10;
				}

				if(endIndex > this.sortedWorldRenderers.length) {
					endIndex = this.sortedWorldRenderers.length;
				}

				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glDisable(GL11.GL_FOG);
				GL11.glColorMask(false, false, false, false);
				GL11.glDepthMask(false);
				this.checkOcclusionQueryResult(startIndex, endIndex, viewEntity.posX, viewEntity.posY, viewEntity.posZ);
				GL11.glPushMatrix();
				// Translate between consecutive queried chunks so every query is issued from the
				// world origin rather than compounding translations.
				float accX = 0.0F;
				float accY = 0.0F;
				float accZ = 0.0F;

				for(int k = startIndex; k < endIndex; ++k) {
					WorldRenderer renderer = this.sortedWorldRenderers[k];
					if(renderer.skipAllRenderPasses()) {
						renderer.isInFrustum = false;
					} else if(renderer.isUpdating) {
						renderer.isVisible = true;
					} else if(renderer.isInFrustum) {
						if(Config.isOcclusionFancy() && !renderer.isInFrustrumFully) {
							renderer.isVisible = true;
						} else if(renderer.isInFrustum && !renderer.isWaitingOnOcclusionQuery) {
							// Skip the query when the camera is near the spot where the renderer
							// was last observed visible (scale the leeway with the renderer index).
							float camDeltaX;
							float camDeltaY;
							float camDeltaZ;
							float deltaMag;
							if(renderer.isVisibleFromPosition) {
								camDeltaX = Math.abs((float)(renderer.visibleFromX - viewEntity.posX));
								camDeltaY = Math.abs((float)(renderer.visibleFromY - viewEntity.posY));
								camDeltaZ = Math.abs((float)(renderer.visibleFromZ - viewEntity.posZ));
								deltaMag = camDeltaX + camDeltaY + camDeltaZ;
								if((double)deltaMag < 10.0D + (double)k / 1000.0D) {
									renderer.isVisible = true;
									continue;
								}

								renderer.isVisibleFromPosition = false;
							}

							camDeltaX = (float)((double)renderer.posXMinus - interpX);
							camDeltaY = (float)((double)renderer.posYMinus - interpY);
							camDeltaZ = (float)((double)renderer.posZMinus - interpZ);
							deltaMag = camDeltaX - accX;
							float deltaY = camDeltaY - accY;
							float deltaZ = camDeltaZ - accZ;
							if(deltaMag != 0.0F || deltaY != 0.0F || deltaZ != 0.0F) {
								GL11.glTranslatef(deltaMag, deltaY, deltaZ);
								accX += deltaMag;
								accY += deltaY;
								accZ += deltaZ;
							}

							ARBOcclusionQuery.glBeginQueryARB(GL15.GL_SAMPLES_PASSED, renderer.glOcclusionQuery);
							renderer.callOcclusionQueryList();
							ARBOcclusionQuery.glEndQueryARB(GL15.GL_SAMPLES_PASSED);
							renderer.isWaitingOnOcclusionQuery = true;
						}
					}
				}

				GL11.glPopMatrix();
				GL11.glColorMask(true, true, true, true);
				GL11.glDepthMask(true);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_FOG);
			}
		} else {
			renderCount = this.renderSortedRenderers(0, this.sortedWorldRenderers.length, renderPass, partialTicks);
		}

		return renderCount;
	}

	private void checkOcclusionQueryResult(int startIndex, int endIndex, double cameraX, double cameraY, double cameraZ) {
		for(int k = startIndex; k < endIndex; ++k) {
			WorldRenderer renderer = this.sortedWorldRenderers[k];
			if(renderer.isWaitingOnOcclusionQuery) {
				this.occlusionResult.clear();
				ARBOcclusionQuery.glGetQueryObjectuARB(renderer.glOcclusionQuery, GL15.GL_QUERY_RESULT_AVAILABLE, this.occlusionResult);
				if(this.occlusionResult.get(0) != 0) {
					// Query finished: snap back the visible flag. When a renderer stays visible it
					// records the camera position it was seen from, letting sortAndRender skip its
					// next query while the camera stays close.
					renderer.isWaitingOnOcclusionQuery = false;
					this.occlusionResult.clear();
					ARBOcclusionQuery.glGetQueryObjectuARB(renderer.glOcclusionQuery, GL15.GL_QUERY_RESULT, this.occlusionResult);
					boolean wasVisible = renderer.isVisible;
					renderer.isVisible = this.occlusionResult.get(0) > 0;
					if(wasVisible && renderer.isVisible) {
						renderer.isVisibleFromPosition = true;
						renderer.visibleFromX = cameraX;
						renderer.visibleFromY = cameraY;
						renderer.visibleFromZ = cameraZ;
					}
				}
			}
		}

	}

	private int renderSortedRenderers(int startIndex, int endIndex, int renderPass, double partialTicks) {
		this.glListBuffer.clear();
		int l = 0;

		for(int entityliving = startIndex; entityliving < endIndex; ++entityliving) {
			WorldRenderer renderer = this.sortedWorldRenderers[entityliving];
			if(renderPass == 0) {
				++this.renderersLoaded;
				if(renderer.skipRenderPass[renderPass]) {
					++this.renderersSkippingRenderPass;
				} else if(!renderer.isInFrustum) {
					++this.renderersBeingClipped;
				} else if(this.occlusionEnabled && !renderer.isVisible) {
					++this.renderersBeingOccluded;
				} else {
					++this.renderersBeingRendered;
				}
			}

			if(!renderer.skipRenderPass[renderPass] && renderer.isInFrustum && (!this.occlusionEnabled || renderer.isVisible)) {
				int glCallList = renderer.getGLCallListForPass(renderPass);
				if(glCallList >= 0) {
					this.glListBuffer.put(glCallList);
					++l;
				}
			}
		}

		this.glListBuffer.flip();
		EntityLiving entityLiving14 = this.mc.renderViewEntity;
		double d15 = entityLiving14.lastTickPosX + (entityLiving14.posX - entityLiving14.lastTickPosX) * partialTicks - (double)WorldRenderer.globalChunkOffsetX;
		double partialY = entityLiving14.lastTickPosY + (entityLiving14.posY - entityLiving14.lastTickPosY) * partialTicks;
		double partialZ = entityLiving14.lastTickPosZ + (entityLiving14.posZ - entityLiving14.lastTickPosZ) * partialTicks - (double)WorldRenderer.globalChunkOffsetZ;
		this.mc.entityRenderer.enableLightmap(partialTicks);
		GL11.glTranslatef((float)(-d15), (float)(-partialY), (float)(-partialZ));
		GL11.glCallLists(this.glListBuffer);
		GL11.glTranslatef((float)d15, (float)partialY, (float)partialZ);
		this.mc.entityRenderer.disableLightmap(partialTicks);
		return l;
	}

	public void renderAllRenderLists(int renderPass, double partialTicks) {
	}

	public void updateClouds() {
		++this.cloudOffsetX;
	}

	public void renderSky(float partialTicks) {
		if(!this.mc.theWorld.worldProvider.isNether) {
			// ## SKY ##
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			// 
			Vec3D skyColor = this.worldObj.getSkyColor(this.mc.renderViewEntity, partialTicks);
			float skyRed = (float)skyColor.xCoord;
			float skyGreen = (float)skyColor.yCoord;
			float skyBlue = (float)skyColor.zCoord;
			
			GL11.glColor3f(skyRed, skyGreen, skyBlue);
			
			Tessellator tessellator = Tessellator.instance;
			GL11.glDepthMask(false);
			if(Config.isSkyEnabled()) {
				GL11.glEnable(GL11.GL_FOG);
				GL11.glColor3f(skyRed, skyGreen, skyBlue);
				GL11.glCallList(this.glSkyList);
			}

			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			RenderHelper.disableStandardItemLighting();

			if(LevelThemeGlobalSettings.sunriseSunsetColors) {
				// Draw the horizontal sunrise/sunset glow as a GL_TRIANGLE_FAN over the horizon.
				float[] sunsetColors = this.worldObj.worldProvider.calcSunriseSunsetColors(this.worldObj.getCelestialAngle(partialTicks), partialTicks);
				if(sunsetColors != null) {
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					GL11.glShadeModel(GL11.GL_SMOOTH);
					GL11.glPushMatrix();
					GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
					float celestialAngle = this.worldObj.getCelestialAngle(partialTicks);
					GL11.glRotatef(celestialAngle > 0.5F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
					float glowRed = sunsetColors[0];
					float glowGreen = sunsetColors[1];
					float glowBlue = sunsetColors[2];
					if(this.mc.gameSettings.anaglyph) {
						float luma = (glowRed * 30.0F + glowGreen * 59.0F + glowBlue * 11.0F) / 100.0F;
						float redChannel = (glowRed * 30.0F + glowGreen * 70.0F) / 100.0F;
						float blueChannel = (glowRed * 30.0F + glowBlue * 70.0F) / 100.0F;
						glowRed = luma;
						glowGreen = redChannel;
						glowBlue = blueChannel;
					}

					tessellator.startDrawing(6);
					tessellator.setColorRGBA_F(glowRed, glowGreen, glowBlue, sunsetColors[3]);
					tessellator.addVertex(0.0D, 100.0D, 0.0D);
					byte glowSegments = 16;
					tessellator.setColorRGBA_F(sunsetColors[0], sunsetColors[1], sunsetColors[2], 0.0F);

					for(int i = 0; i <= glowSegments; ++i) {
						float angle = (float)i * (float)Math.PI * 2.0F / (float)glowSegments;
						float sinAngle = MathHelper.sin(angle);
						float cosAngle = MathHelper.cos(angle);
						tessellator.addVertex((double)(sinAngle * 120.0F), (double)(cosAngle * 120.0F), (double)(-cosAngle * 40.0F * sunsetColors[3]));
					}

					tessellator.draw();
					GL11.glPopMatrix();
					GL11.glShadeModel(GL11.GL_FLAT);
				}
			}

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			GL11.glPushMatrix();
			
			// Sun and moon with additive blending, dimmed while it rains. During a blood moon the
			// moon turns red and grows to 2.5x its normal size.
			
			float daylightFactor = 1.0F - this.worldObj.getRainStrength(partialTicks);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, daylightFactor);
			GL11.glRotatef(this.worldObj.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);

			float sunMoonSize = 30.0F;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain/sun.png"));
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV((double)(-sunMoonSize), 100.0D, (double)(-sunMoonSize), 0.0D, 0.0D);
			tessellator.addVertexWithUV((double)sunMoonSize, 100.0D, (double)(-sunMoonSize), 1.0D, 0.0D);
			tessellator.addVertexWithUV((double)sunMoonSize, 100.0D, (double)sunMoonSize, 1.0D, 1.0D);
			tessellator.addVertexWithUV((double)(-sunMoonSize), 100.0D, (double)sunMoonSize, 0.0D, 1.0D);
			tessellator.draw();
			sunMoonSize = 20.0F;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain/moon.png"));
			if(this.worldObj.worldInfo.isBloodMoon()) {
				GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0f);
				sunMoonSize = 50.0F;
			}

			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV((double)(-sunMoonSize), -100.0D, (double)sunMoonSize, 1.0D, 1.0D);
			tessellator.addVertexWithUV((double)sunMoonSize, -100.0D, (double)sunMoonSize, 0.0D, 1.0D);
			tessellator.addVertexWithUV((double)sunMoonSize, -100.0D, (double)(-sunMoonSize), 0.0D, 0.0D);
			tessellator.addVertexWithUV((double)(-sunMoonSize), -100.0D, (double)(-sunMoonSize), 1.0D, 0.0D);
			tessellator.draw();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			
			// Stars
			
			float starBrightness = this.worldObj.getStarBrightness(partialTicks) * daylightFactor;
			if(starBrightness > 0.0F) {
				GL11.glColor4f(starBrightness, starBrightness, starBrightness, starBrightness);
				if(Config.isStarsEnabled()) {
					GL11.glCallList(this.starGLCallList);
				}
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_FOG);
			GL11.glPopMatrix();
			
			GL11.glColor3f(skyRed, skyGreen, skyBlue);
	
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			
			if(Config.isSkyEnabled()) {
				GL11.glCallList(this.glSkyList2);
			}

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(true);
		}
	}

	public void renderClouds(float f1) {
		if(!this.mc.theWorld.worldProvider.isNether) {
			if(this.mc.gameSettings.ofClouds != 3) {
				if(Config.isCloudsFancy()) {
					this.renderCloudsFancy(f1);
				} else {
					GL11.glDisable(GL11.GL_CULL_FACE);
					float f2 = (float)(this.mc.renderViewEntity.lastTickPosY + (this.mc.renderViewEntity.posY - this.mc.renderViewEntity.lastTickPosY) * (double)f1);
					byte b3 = 32;
					int i4 = 256 / b3;
					Tessellator tessellator5 = Tessellator.instance;
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/environment/clouds.png"));
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					Vec3D vec3D6 = this.worldObj.getCloudColor(f1, this.mc.renderViewEntity);
					float f7 = (float)vec3D6.xCoord;
					float f8 = (float)vec3D6.yCoord;
					float f9 = (float)vec3D6.zCoord;
					float f10;
					if(this.mc.gameSettings.anaglyph) {
						f10 = (f7 * 30.0F + f8 * 59.0F + f9 * 11.0F) / 100.0F;
						float f11 = (f7 * 30.0F + f8 * 70.0F) / 100.0F;
						float f12 = (f7 * 30.0F + f9 * 70.0F) / 100.0F;
						f7 = f10;
						f8 = f11;
						f9 = f12;
					}
	
					f10 = 4.8828125E-4F;
					
					double d22 = this.mc.renderViewEntity.prevPosX + (this.mc.renderViewEntity.posX - this.mc.renderViewEntity.prevPosX) * (double)f1 + (double)(((float)this.cloudOffsetX + f1) * 0.03F);
					double d13 = this.mc.renderViewEntity.prevPosZ + (this.mc.renderViewEntity.posZ - this.mc.renderViewEntity.prevPosZ) * (double)f1;
					int i15 = MathHelper.floor_double(d22 / 2048.0D);
					int i16 = MathHelper.floor_double(d13 / 2048.0D);
					d22 -= (double)(i15 * 2048);
					d13 -= (double)(i16 * 2048);
					float f17 = this.worldObj.worldProvider.getCloudHeight() - f2 + 0.33F;
					f17 += this.mc.gameSettings.ofCloudsHeight * 128.0F;
					float f18 = (float)(d22 * (double)f10);
					float f19 = (float)(d13 * (double)f10);
					tessellator5.startDrawingQuads();
					tessellator5.setColorRGBA_F(f7, f8, f9, 0.8F);
	
					for(int i20 = -b3 * i4; i20 < b3 * i4; i20 += b3) {
						for(int i21 = -b3 * i4; i21 < b3 * i4; i21 += b3) {
							tessellator5.addVertexWithUV((double)(i20 + 0), (double)f17, (double)(i21 + b3), (double)((float)(i20 + 0) * f10 + f18), (double)((float)(i21 + b3) * f10 + f19));
							tessellator5.addVertexWithUV((double)(i20 + b3), (double)f17, (double)(i21 + b3), (double)((float)(i20 + b3) * f10 + f18), (double)((float)(i21 + b3) * f10 + f19));
							tessellator5.addVertexWithUV((double)(i20 + b3), (double)f17, (double)(i21 + 0), (double)((float)(i20 + b3) * f10 + f18), (double)((float)(i21 + 0) * f10 + f19));
							tessellator5.addVertexWithUV((double)(i20 + 0), (double)f17, (double)(i21 + 0), (double)((float)(i20 + 0) * f10 + f18), (double)((float)(i21 + 0) * f10 + f19));
						}
					}
	
					tessellator5.draw();
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_CULL_FACE);
				}
			}
		}
	}

	public boolean clipRenderersByFrustrum(double d1, double d3, double d5, float f7) {
		return false;
	}

	public void renderCloudsFancy(float f) {
		GL11.glDisable(GL11.GL_CULL_FACE);
		float f1 = (float)(this.mc.renderViewEntity.lastTickPosY + (this.mc.renderViewEntity.posY - this.mc.renderViewEntity.lastTickPosY) * (double)f);
		Tessellator tessellator = Tessellator.instance;
		float f2 = 12.0F;
		float f3 = 4.0F;
		
		double d = (this.mc.renderViewEntity.prevPosX + (this.mc.renderViewEntity.posX - this.mc.renderViewEntity.prevPosX) * (double)f + (double)(((float)this.cloudOffsetX + f) * 0.03F)) / (double)f2;
		double d1 = (this.mc.renderViewEntity.prevPosZ + (this.mc.renderViewEntity.posZ - this.mc.renderViewEntity.prevPosZ) * (double)f) / (double)f2 + (double)0.33F;
		float f4 = this.worldObj.worldProvider.getCloudHeight() - f1 + 0.33F;
		f4 += this.mc.gameSettings.ofCloudsHeight * 25.0F;
		int i = MathHelper.floor_double(d / 2048.0D);
		int j = MathHelper.floor_double(d1 / 2048.0D);
		d -= (double)(i * 2048);
		d1 -= (double)(j * 2048);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/environment/clouds.png"));
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Vec3D vec3d = this.worldObj.getCloudColor(f1, this.mc.renderViewEntity);
		float f5 = (float)vec3d.xCoord;
		float f6 = (float)vec3d.yCoord;
		float f7 = (float)vec3d.zCoord;
		float f9;
		float f11;
		float f13;
		if(this.mc.gameSettings.anaglyph) {
			f9 = (f5 * 30.0F + f6 * 59.0F + f7 * 11.0F) / 100.0F;
			f11 = (f5 * 30.0F + f6 * 70.0F) / 100.0F;
			f13 = (f5 * 30.0F + f7 * 70.0F) / 100.0F;
			f5 = f9;
			f6 = f11;
			f7 = f13;
		}

		f9 = (float)(d * 0.0D);
		f11 = (float)(d1 * 0.0D);
		f13 = 0.00390625F;
		f9 = (float)MathHelper.floor_double(d) * f13;
		f11 = (float)MathHelper.floor_double(d1) * f13;
		float f14 = (float)(d - (double)MathHelper.floor_double(d));
		float f15 = (float)(d1 - (double)MathHelper.floor_double(d1));
		byte k = 8;
		byte byte0 = 3;
		float f16 = 9.765625E-4F;
		GL11.glScalef(f2, 1.0F, f2);

		for(int l = 0; l < 2; ++l) {
			if(l == 0) {
				GL11.glColorMask(false, false, false, false);
			} else if(this.mc.gameSettings.anaglyph) {
				if(GameRenderer.anaglyphField == 0) {
					GL11.glColorMask(false, true, true, true);
				} else {
					GL11.glColorMask(true, false, false, true);
				}
			} else {
				GL11.glColorMask(true, true, true, true);
			}

			double dd = 0.02D;

			for(int i1 = -byte0 + 1; i1 <= byte0; ++i1) {
				for(int j1 = -byte0 + 1; j1 <= byte0; ++j1) {
					tessellator.startDrawingQuads();
					float f17 = (float)(i1 * k);
					float f18 = (float)(j1 * k);
					float f19 = f17 - f14;
					float f20 = f18 - f15;
					tessellator.setColorRGBA_F(f5 * 0.9F, f6 * 0.9F, f7 * 0.9F, 0.8F);
					int j2;
					if(i1 > -1) {
						tessellator.setNormal(-1.0F, 0.0F, 0.0F);

						for(j2 = 0; j2 < k; ++j2) {
							tessellator.addVertexWithUV((double)(f19 + (float)j2 + 0.0F), (double)(f4 + 0.0F) + dd, (double)(f20 + (float)k), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + (float)j2 + 0.0F), (double)(f4 + f3) - dd, (double)(f20 + (float)k), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + (float)j2 + 0.0F), (double)(f4 + f3) - dd, (double)(f20 + 0.0F), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + (float)j2 + 0.0F), (double)(f4 + 0.0F) + dd, (double)(f20 + 0.0F), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
						}
					}

					if(i1 <= 1) {
						tessellator.setNormal(1.0F, 0.0F, 0.0F);

						for(j2 = 0; j2 < k; ++j2) {
							tessellator.addVertexWithUV((double)(f19 + (float)j2 + 1.0F - f16), (double)(f4 + 0.0F) + dd, (double)(f20 + (float)k), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + (float)j2 + 1.0F - f16), (double)(f4 + f3) - dd, (double)(f20 + (float)k), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + (float)j2 + 1.0F - f16), (double)(f4 + f3) - dd, (double)(f20 + 0.0F), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + (float)j2 + 1.0F - f16), (double)(f4 + 0.0F) + dd, (double)(f20 + 0.0F), (double)((f17 + (float)j2 + 0.5F) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
						}
					}

					tessellator.setColorRGBA_F(f5 * 0.8F, f6 * 0.8F, f7 * 0.8F, 0.8F);
					if(j1 > -1) {
						tessellator.setNormal(0.0F, 0.0F, -1.0F);

						for(j2 = 0; j2 < k; ++j2) {
							tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + f3) - dd, (double)(f20 + (float)j2 + 0.0F), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + f3) - dd, (double)(f20 + (float)j2 + 0.0F), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + 0.0F) + dd, (double)(f20 + (float)j2 + 0.0F), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + 0.0F) + dd, (double)(f20 + (float)j2 + 0.0F), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
						}
					}

					if(j1 <= 1) {
						tessellator.setNormal(0.0F, 0.0F, 1.0F);

						for(j2 = 0; j2 < k; ++j2) {
							tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + f3) - dd, (double)(f20 + (float)j2 + 1.0F - f16), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + f3) - dd, (double)(f20 + (float)j2 + 1.0F - f16), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + 0.0F) + dd, (double)(f20 + (float)j2 + 1.0F - f16), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
							tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + 0.0F) + dd, (double)(f20 + (float)j2 + 1.0F - f16), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + (float)j2 + 0.5F) * f13 + f11));
						}
					}

					if(f4 > -f3 - 1.0F) {
						tessellator.setColorRGBA_F(f5 * 0.7F, f6 * 0.7F, f7 * 0.7F, 0.8F);
						tessellator.setNormal(0.0F, -1.0F, 0.0F);
						tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + 0.0F), (double)(f20 + (float)k), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
						tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + 0.0F), (double)(f20 + (float)k), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
						tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + 0.0F), (double)(f20 + 0.0F), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
						tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + 0.0F), (double)(f20 + 0.0F), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
					}

					if(f4 <= f3 + 1.0F) {
						tessellator.setColorRGBA_F(f5, f6, f7, 0.8F);
						tessellator.setNormal(0.0F, 1.0F, 0.0F);
						tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + f3 - f16), (double)(f20 + (float)k), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
						tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + f3 - f16), (double)(f20 + (float)k), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + (float)k) * f13 + f11));
						tessellator.addVertexWithUV((double)(f19 + (float)k), (double)(f4 + f3 - f16), (double)(f20 + 0.0F), (double)((f17 + (float)k) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
						tessellator.addVertexWithUV((double)(f19 + 0.0F), (double)(f4 + f3 - f16), (double)(f20 + 0.0F), (double)((f17 + 0.0F) * f13 + f9), (double)((f18 + 0.0F) * f13 + f11));
					}

					tessellator.draw();
				}
			}
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
	}

	public boolean updateRenderers(EntityLiving entity, boolean flag) {
		if(this.worldRenderersToUpdate.size() <= 0) {
			return false;
		} else {
			int numUpdated = 0;
			int maxUpdates = Config.getUpdatesPerFrame();
			if(Config.isDynamicUpdates() && !this.isMoving(entity)) {
				maxUpdates *= 3;
			}

			// Out-of-frustum chunks are far less urgent than visible ones: multiply their
			// (squared) distance by this factor so they are deprioritized during selection.
			byte outOfFrustrumFactor = 4;
			int numValid = 0;
			WorldRenderer nearestRenderer = null;
			float nearestDistSq = Float.MAX_VALUE;
			int nearestIndex = -1;
			float[] distSqCached = new float[this.worldRenderersToUpdate.size()];

			int dstIndex;
			for(dstIndex = 0; dstIndex < this.worldRenderersToUpdate.size(); ++dstIndex) {
				WorldRenderer renderer = this.worldRenderersToUpdate.get(dstIndex);
				if(renderer != null) {
					++numValid;
					if(!renderer.needsUpdate) {
						this.worldRenderersToUpdateSet.remove(renderer);
						this.worldRenderersToUpdate.set(dstIndex, (WorldRenderer)null);
					} else {
						float distSq = renderer.distanceToEntitySquared(entity);
						distSqCached[dstIndex] = distSq;
						if(distSq <= 256.0F && this.isActingNow()) {
							renderer.updateRenderer();
							renderer.needsUpdate = false;
							this.worldRenderersToUpdateSet.remove(renderer);
							this.worldRenderersToUpdate.set(dstIndex, (WorldRenderer)null);
							++numUpdated;
						} else {
							if(distSq > 256.0F && numUpdated >= maxUpdates) {
								break;
							}

							if(!renderer.isInFrustum) {
								distSq *= (float)outOfFrustrumFactor;
							}

							if(nearestRenderer == null) {
								nearestRenderer = renderer;
								nearestDistSq = distSq;
								nearestIndex = dstIndex;
							} else if(distSq < nearestDistSq) {
								nearestRenderer = renderer;
								nearestDistSq = distSq;
								nearestIndex = dstIndex;
							}
						}
					}
				}
			}

			int i16;
			if(nearestRenderer != null) {
				// Always rebuild the single nearest renderer, then rebuild any queue entries whose
				// distance is close enough to it (they share a lot of recompiled geometry).
				nearestRenderer.updateRenderer();
				nearestRenderer.needsUpdate = false;
				this.worldRenderersToUpdateSet.remove(nearestRenderer);
				this.worldRenderersToUpdate.set(nearestIndex, (WorldRenderer)null);
				++numUpdated;
				float thresholdDistSq = nearestDistSq / 5.0F;

				for(i16 = 0; i16 < this.worldRenderersToUpdate.size() && numUpdated < maxUpdates; ++i16) {
					WorldRenderer renderer = this.worldRenderersToUpdate.get(i16);
					if(renderer != null) {
						// Distances were computed in the first pass above; reuse them.
						float distSq = distSqCached[i16];
						if(!renderer.isInFrustum) {
							distSq *= (float)outOfFrustrumFactor;
						}

						float diffDistSq = Math.abs(distSq - nearestDistSq);
						if(diffDistSq < thresholdDistSq) {
							renderer.updateRenderer();
							renderer.needsUpdate = false;
							this.worldRenderersToUpdateSet.remove(renderer);
							this.worldRenderersToUpdate.set(i16, (WorldRenderer)null);
							++numUpdated;
						}
					}
				}
			}

			if(numValid == 0) {
				this.worldRenderersToUpdateSet.clear();
				this.worldRenderersToUpdate.clear();
			}

			if(this.worldRenderersToUpdate.size() > 100 && numValid < this.worldRenderersToUpdate.size() * 4 / 5) {
				// Compact the queue: slide live entries to the front, then drop the trailing nulls
				// in one O(n) pass instead of removing each one individually.
				dstIndex = 0;

				for(i16 = 0; i16 < this.worldRenderersToUpdate.size(); ++i16) {
					WorldRenderer renderer = this.worldRenderersToUpdate.get(i16);
					if(renderer != null) {
						if(i16 != dstIndex)  {
							this.worldRenderersToUpdate.set(dstIndex, renderer);
						}
						++dstIndex;
					}
				}

				this.worldRenderersToUpdate.subList(dstIndex, this.worldRenderersToUpdate.size()).clear();
			}

			return true;
		}
	}
	
	public void drawBlockBreaking(EntityPlayer entityPlayer1, MovingObjectPosition movingObjectPosition2, int i3, ItemStack itemStack4, float f5) {
		Tessellator tessellator6 = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, (MathHelper.sin((float)System.currentTimeMillis() / 100.0F) * 0.2F + 0.4F) * 0.5F);
		int i8;
		if(i3 == 0) {
			if(this.damagePartialTime > 0.0F) {
				GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
				int i7 = this.renderEngine.getTexture("/terrain.png");
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, i7);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
				GL11.glPushMatrix();
				i8 = this.worldObj.getBlockID(movingObjectPosition2.blockX, movingObjectPosition2.blockY, movingObjectPosition2.blockZ);
				Block block9 = i8 > 0 ? Block.blocksList[i8] : null;
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glPolygonOffset(-3.0F, -3.0F);
				GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
				double d10 = entityPlayer1.lastTickPosX + (entityPlayer1.posX - entityPlayer1.lastTickPosX) * (double)f5;
				double d12 = entityPlayer1.lastTickPosY + (entityPlayer1.posY - entityPlayer1.lastTickPosY) * (double)f5;
				double d14 = entityPlayer1.lastTickPosZ + (entityPlayer1.posZ - entityPlayer1.lastTickPosZ) * (double)f5;
				if(block9 == null) {
					block9 = Block.stone;
				}

				GL11.glEnable(GL11.GL_ALPHA_TEST);
				tessellator6.startDrawingQuads();
				tessellator6.setTranslation(-d10, -d12, -d14);
				tessellator6.disableColor();
				this.globalRenderBlocks.renderBlockUsingTexture(block9, movingObjectPosition2.blockX, movingObjectPosition2.blockY, movingObjectPosition2.blockZ, 240 + (int)(this.damagePartialTime * 10.0F));
				tessellator6.draw();
				tessellator6.setTranslation(0.0D, 0.0D, 0.0D);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glPolygonOffset(0.0F, 0.0F);
				GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glDepthMask(true);
				GL11.glPopMatrix();
			}
		} else if(itemStack4 != null) {
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			float f16 = MathHelper.sin((float)System.currentTimeMillis() / 100.0F) * 0.2F + 0.8F;
			GL11.glColor4f(f16, f16, f16, MathHelper.sin((float)System.currentTimeMillis() / 200.0F) * 0.2F + 0.5F);
			i8 = this.renderEngine.getTexture("/terrain.png");
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, i8);
			if(movingObjectPosition2.sideHit == 0) {
			}

			if(movingObjectPosition2.sideHit == 1) {
			}

			if(movingObjectPosition2.sideHit == 2) {
			}

			if(movingObjectPosition2.sideHit == 3) {
			}

			if(movingObjectPosition2.sideHit == 4) {
			}

			if(movingObjectPosition2.sideHit == 5) {
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}

	public void drawSelectionBox(EntityPlayer entityPlayer1, MovingObjectPosition movingObjectPosition2, int i3, ItemStack itemStack4, float f5) {
		if(i3 == 0 && movingObjectPosition2.typeOfHit == EnumMovingObjectType.TILE) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
			GL11.glLineWidth(2.0F);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(false);
			float f6 = 0.002F;
			int i7 = this.worldObj.getBlockID(movingObjectPosition2.blockX, movingObjectPosition2.blockY, movingObjectPosition2.blockZ);
			if(i7 > 0) {
				Block.blocksList[i7].setBlockBoundsBasedOnState(this.worldObj, movingObjectPosition2.blockX, movingObjectPosition2.blockY, movingObjectPosition2.blockZ);
				double d8 = entityPlayer1.lastTickPosX + (entityPlayer1.posX - entityPlayer1.lastTickPosX) * (double)f5;
				double d10 = entityPlayer1.lastTickPosY + (entityPlayer1.posY - entityPlayer1.lastTickPosY) * (double)f5;
				double d12 = entityPlayer1.lastTickPosZ + (entityPlayer1.posZ - entityPlayer1.lastTickPosZ) * (double)f5;
				this.drawOutlinedBoundingBox(Block.blocksList[i7].getSelectedBoundingBoxFromPool(this.worldObj, movingObjectPosition2.blockX, movingObjectPosition2.blockY, movingObjectPosition2.blockZ).expand((double)f6, (double)f6, (double)f6).getOffsetBoundingBox(-d8, -d10, -d12));
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
		}

	}

	private void drawOutlinedBoundingBox(AxisAlignedBB par1AxisAlignedBB) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawing(3);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		tessellator.draw();
		tessellator.startDrawing(3);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.draw();
		tessellator.startDrawing(1);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		tessellator.draw();
	}

	public void markBlocksForUpdate(int par1, int par2, int par3, int par4, int par5, int par6) {
		int i = MathHelper.bucketInt(par1, 16);
		int j = MathHelper.bucketInt(par2, 16);
		int k = MathHelper.bucketInt(par3, 16);
		int l = MathHelper.bucketInt(par4, 16);
		int i1 = MathHelper.bucketInt(par5, 16);
		int j1 = MathHelper.bucketInt(par6, 16);

		for(int k1 = i; k1 <= l; ++k1) {
			int l1 = k1 % this.renderChunksWide;
			if(l1 < 0) {
				l1 += this.renderChunksWide;
			}

			for(int i2 = j; i2 <= i1; ++i2) {
				int j2 = i2 % this.renderChunksTall;
				if(j2 < 0) {
					j2 += this.renderChunksTall;
				}

				for(int k2 = k; k2 <= j1; ++k2) {
					int l2 = k2 % this.renderChunksDeep;
					if(l2 < 0) {
						l2 += this.renderChunksDeep;
					}

					int i3 = (l2 * this.renderChunksTall + j2) * this.renderChunksWide + l1;
					WorldRenderer worldrenderer = this.worldRenderers[i3];
					if(!worldrenderer.needsUpdate) {
						this.worldRenderersToUpdateSet.add(worldrenderer);
						this.worldRenderersToUpdate.add(worldrenderer);
						worldrenderer.markDirty();
					}
				}
			}
		}

	}

	public void markBlockNeedsUpdate(int i1, int i2, int i3) {
		this.markBlocksForUpdate(i1 - 1, i2 - 1, i3 - 1, i1 + 1, i2 + 1, i3 + 1);
	}

	public void markBlockRangeNeedsUpdate(int i1, int i2, int i3, int i4, int i5, int i6) {
		this.markBlocksForUpdate(i1 - 1, i2 - 1, i3 - 1, i4 + 1, i5 + 1, i6 + 1);
	}

	public void clipRenderersByFrustum(ICamera par1ICamera, float par2) {
		for(int i = 0; i < this.worldRenderers.length; ++i) {
			if(!this.worldRenderers[i].skipAllRenderPasses()) {
				this.worldRenderers[i].updateInFrustum(par1ICamera);
			}
		}

		++this.frustumCheckOffset;
	}

	public void playRecord(String string1, int i2, int i3, int i4) {
		if(string1 != null) {
			this.mc.ingameGUI.setRecordPlayingMessage("C418 - " + string1);
		}

		this.mc.sndManager.playStreaming(string1, (float)i2, (float)i3, (float)i4, 1.0F, 1.0F);
	}

	public void playSound(String string1, double d2, double d4, double d6, float f8, float f9) {
		float f10 = 16.0F;
		if(f8 > 1.0F) {
			f10 *= f8;
		}

		if(this.mc.renderViewEntity.getDistanceSq(d2, d4, d6) < (double)(f10 * f10)) {
			this.mc.sndManager.playSound(string1, (float)d2, (float)d4, (float)d6, f8, f9);
		}
	}
	
	public void showString(String s) {
		if (s != null) {
			this.mc.ingameGUI.showString(s);
		}
	}
	
	public void showChatMessage(String s) {
		if (s != null) {
			this.mc.ingameGUI.addChatMessage(s);
		}
	}

	public void spawnParticle(String particle, double posX, double posY, double posZ, double motionX, double motionY, double motionZ) {
		if(this.mc != null && this.mc.renderViewEntity != null && this.mc.effectRenderer != null) {
			double dX = this.mc.renderViewEntity.posX - posX;
			double dY = this.mc.renderViewEntity.posY - posY;
			double dZ = this.mc.renderViewEntity.posZ - posZ;
			double dist = 16.0D;
			if(dX * dX + dY * dY + dZ * dZ <= dist * dist) {
				// The dynamic "iconcrack_<itemId>" / "tilecrack_<blockId>" names can't be pre-registered in
				// the fixed-name map above, so handle them before the dispatch lookup.
				int itemId;
				if(particle.startsWith("iconcrack_")) {
					itemId = Integer.parseInt(particle.substring(particle.indexOf("_") + 1));
					this.mc.effectRenderer.addEffect(new EntityBreakingFX(this.worldObj, posX, posY, posZ, motionX, motionY, motionZ, Item.itemsList[itemId]));
				} else if(particle.startsWith("tilecrack_")) {
					itemId = Integer.parseInt(particle.substring(particle.indexOf("_") + 1));
					this.mc.effectRenderer.addEffect(new EntityDiggingFX(this.worldObj, posX, posY, posZ, motionX, motionY, motionZ, Block.blocksList[itemId], 0, 0));
				} else {
					Integer particleType = PARTICLE_TYPE_BY_NAME.get(particle);
					if(particleType != null) {
						switch(particleType.intValue()) {
						case 0:
							this.mc.effectRenderer.addEffect(new EntityBubbleFX(this.worldObj, posX, posY, posZ, motionX, motionY, motionZ));
							break;
						case 1:
							if(Config.isAnimatedSmoke()) {
								this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, posX, posY, posZ, motionX, motionY, motionZ));
							}
							break;
						case 2:
							this.mc.effectRenderer.addEffect(new EntityNoteFX(this.worldObj, posX, posY, posZ, motionX, motionY, motionZ));
							break;
						case 3:
							this.mc.effectRenderer.addEffect(new EntityPortalFX(this.worldObj, posX, posY, posZ, motionX, motionY, motionZ));
							break;
						case 4:
							if(Config.isAnimatedExplosion()) {
								this.mc.effectRenderer.addEffect(new EntityExplodeFX(this.worldObj, posX, posY, posZ, motionX, motionY, motionZ));
							}
							break;
						case 5:
							if(Config.isAnimatedFlame()) {
								this.mc.effectRenderer.addEffect(new EntityFlameFX(this.worldObj, posX, posY, posZ, motionX, motionY, motionZ));
							}
							break;
						case 6:
							this.mc.effectRenderer.addEffect(new EntityLavaFX(this.worldObj, posX, posY, posZ));
							break;
						case 7:
							this.mc.effectRenderer.addEffect(new EntityFootStepFX(this.renderEngine, this.worldObj, posX, posY, posZ));
							break;
						case 8:
							this.mc.effectRenderer.addEffect(new EntitySplashFX(this.worldObj, posX, posY, posZ, motionX, motionY, motionZ));
							break;
						case 9:
							if(Config.isAnimatedSmoke()) {
								this.mc.effectRenderer.addEffect(new EntitySmokeFX(this.worldObj, posX, posY, posZ, motionX, motionY, motionZ, 2.5F));
							}
							break;
						case 10:
							if(Config.isAnimatedRedstone()) {
								this.mc.effectRenderer.addEffect(new EntityReddustFX(this.worldObj, posX, posY, posZ, (float)motionX, (float)motionY, (float)motionZ));
							}
							break;
						case 11:
							if(Config.isAnimatedRedstone()) {
								this.mc.effectRenderer.addEffect(new EntityGlowdustFX(this.worldObj, posX, posY, posZ));
							}
							break;
						case 12:
							this.mc.effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, posX, posY, posZ, Item.snowball));
							break;
						case 13:
							this.mc.effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, posX, posY, posZ, Item.pebble));
							break;
						case 14:
							this.mc.effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, posX, posY, posZ, Item.potionEmpty));
							break;
						case 15:
							this.mc.effectRenderer.addEffect(new EntitySnowShovelFX(this.worldObj, posX, posY, posZ, motionX, motionY, motionZ));
							break;
						case 16:
							this.mc.effectRenderer.addEffect(new EntitySlimeFX(this.worldObj, posX, posY, posZ, Item.slimeBall));
							break;
						case 17:
							this.mc.effectRenderer.addEffect(new EntityHeartFX(this.worldObj, posX, posY, posZ, motionX, motionY, motionZ));
							break;
						case 18:
							EntityStatusEffectFX entityFX = new EntityStatusEffectFX(this.worldObj, posX, posY, posZ, 0, 0, 0);
							entityFX.setParticleColor((float)motionX, (float)motionY, (float)motionZ);
							this.mc.effectRenderer.addEffect(entityFX);
							break;
						}
					}
				}

			}
		}
	}

	public void obtainEntitySkin(Entity entity1) {
		entity1.updateCloak();
		if(entity1.skinUrl != null) {
			this.renderEngine.obtainImageData(entity1.skinUrl, new ImageBufferDownload());
		}

		if(entity1.cloakUrl != null) {
			this.renderEngine.obtainImageData(entity1.cloakUrl, new ImageBufferDownload());
		}

	}

	public void releaseEntitySkin(Entity entity1) {
		if(entity1.skinUrl != null) {
			this.renderEngine.releaseImageData(entity1.skinUrl);
		}

		if(entity1.cloakUrl != null) {
			this.renderEngine.releaseImageData(entity1.cloakUrl);
		}

	}

	public void updateAllRenderers() {
		if(this.worldRenderers != null) {
			for(int i = 0; i < this.worldRenderers.length; ++i) {
				if(this.worldRenderers[i].isChunkLit && !this.worldRenderers[i].needsUpdate) {
					this.worldRenderersToUpdateSet.add(this.worldRenderers[i]);
					this.worldRenderersToUpdate.add(this.worldRenderers[i]);
					this.worldRenderers[i].markDirty();
				}
			}

		}
	}

	public void setAllRenderesVisible() {
		if(this.worldRenderers != null) {
			for(int i = 0; i < this.worldRenderers.length; ++i) {
				this.worldRenderers[i].isVisible = true;
			}

		}
	}

	public void doNothingWithTileEntity(int i1, int i2, int i3, TileEntity tileEntity4) {
	}

	public void deleteDisplayLists() {
		GLAllocation.deleteDisplayLists(this.glRenderListBase);
	}

	public void playAuxSFX(EntityPlayer entityPlayer1, int i2, int i3, int i4, int i5, int i6) {
		Random random7 = this.worldObj.rand;
		int i16;
		switch(i2) {
		case 1000:
			this.worldObj.playSoundEffect((double)i3, (double)i4, (double)i5, "random.click", 1.0F, 1.0F);
			break;
		case 1001:
			this.worldObj.playSoundEffect((double)i3, (double)i4, (double)i5, "random.click", 1.0F, 1.2F);
			break;
		case 1002:
			this.worldObj.playSoundEffect((double)i3, (double)i4, (double)i5, "random.bow", 1.0F, 1.2F);
			break;
		case 1003:
			if(Math.random() < 0.5D) {
				this.worldObj.playSoundEffect((double)i3 + 0.5D, (double)i4 + 0.5D, (double)i5 + 0.5D, "random.door_open", 1.0F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			} else {
				this.worldObj.playSoundEffect((double)i3 + 0.5D, (double)i4 + 0.5D, (double)i5 + 0.5D, "random.door_close", 1.0F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}
			break;
		case 1004:
			this.worldObj.playSoundEffect((double)((float)i3 + 0.5F), (double)((float)i4 + 0.5F), (double)((float)i5 + 0.5F), "random.fizz", 0.5F, 2.6F + (random7.nextFloat() - random7.nextFloat()) * 0.8F);
			break;
		case 1005:
			if(Item.itemsList[i6] instanceof ItemRecord) {
				this.worldObj.playRecord(((ItemRecord)Item.itemsList[i6]).recordName, i3, i4, i5);
			} else {
				this.worldObj.playRecord((String)null, i3, i4, i5);
			}
			break;
		case 2000:
			int i8 = i6 % 3 - 1;
			int i9 = i6 / 3 % 3 - 1;
			double d10 = (double)i3 + (double)i8 * 0.6D + 0.5D;
			double d12 = (double)i4 + 0.5D;
			double d14 = (double)i5 + (double)i9 * 0.6D + 0.5D;

			for(i16 = 0; i16 < 10; ++i16) {
				double d31 = random7.nextDouble() * 0.2D + 0.01D;
				double d19 = d10 + (double)i8 * 0.01D + (random7.nextDouble() - 0.5D) * (double)i9 * 0.5D;
				double d21 = d12 + (random7.nextDouble() - 0.5D) * 0.5D;
				double d23 = d14 + (double)i9 * 0.01D + (random7.nextDouble() - 0.5D) * (double)i8 * 0.5D;
				double d25 = (double)i8 * d31 + random7.nextGaussian() * 0.01D;
				double d27 = -0.03D + random7.nextGaussian() * 0.01D;
				double d29 = (double)i9 * d31 + random7.nextGaussian() * 0.01D;
				this.spawnParticle("smoke", d19, d21, d23, d25, d27, d29);
			}

			return;
		case 2001:
			i16 = i6 & 255;
			if(i16 > 0) {
				Block block17 = Block.blocksList[i16];
				this.mc.sndManager.playSound(block17.stepSound.getBreakSound(), (float)i3 + 0.5F, (float)i4 + 0.5F, (float)i5 + 0.5F, (block17.stepSound.getVolume() + 1.0F) / 2.0F, block17.stepSound.getPitch() * 0.8F);
			}

			this.mc.effectRenderer.addBlockDestroyEffects(i3, i4, i5, i6 & 255, i6 >> 8 & 255);
		}

	}

	public boolean isMoving(EntityLiving entityliving) {
		boolean moving = this.isMovingNow(entityliving);
		if(moving) {
			this.lastMovedTime = System.currentTimeMillis();
			return true;
		} else {
			return System.currentTimeMillis() - this.lastMovedTime < 2000L;
		}
	}

	private boolean isMovingNow(EntityLiving entityliving) {
		double maxDiff = 0.001D;
		return entityliving.isJumping ? true : (entityliving.isSneaking() ? true : ((double)entityliving.prevSwingProgress > maxDiff ? true : (this.mc.mouseHelper.deltaX != 0 ? true : (this.mc.mouseHelper.deltaY != 0 ? true : (Math.abs(entityliving.posX - entityliving.prevPosX) > maxDiff ? true : (Math.abs(entityliving.posY - entityliving.prevPosY) > maxDiff ? true : Math.abs(entityliving.posZ - entityliving.prevPosZ) > maxDiff))))));
	}

	public boolean isActing() {
		boolean acting = this.isActingNow();
		if(acting) {
			this.lastActionTime = System.currentTimeMillis();
			return true;
		} else {
			return System.currentTimeMillis() - this.lastActionTime < 500L;
		}
	}

	public boolean isActingNow() {
		return Mouse.isButtonDown(0) ? true : Mouse.isButtonDown(1);
	}

	public int renderAllSortedRenderers(int renderPass, double partialTicks) {
		return this.renderSortedRenderers(0, this.sortedWorldRenderers.length, renderPass, partialTicks);
	}
}
