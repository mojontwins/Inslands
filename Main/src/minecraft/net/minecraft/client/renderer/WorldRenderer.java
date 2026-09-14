package net.minecraft.client.renderer;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Config;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.World;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.ChunkCache;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.entity.TileEntity;
import net.minecraft.world.phys.AxisAlignedBB;

public class WorldRenderer {
	
	/*
	 * This class should be called ChunkRenderer, btw!
	 */
	
	public World worldObj;
	protected int glRenderList = -1;
	public static volatile int chunksUpdated = 0;
	public int posX;
	public int posY;
	public int posZ;
	public int posXMinus;
	public int posYMinus;
	public int posZMinus;
	public int posXClip;
	public int posYClip;
	public int posZClip;
	public boolean isInFrustum = false;
	public boolean[] skipRenderPass = new boolean[2];
	public int posXPlus;
	public int posYPlus;
	public int posZPlus;
	public volatile boolean needsUpdate;
	public AxisAlignedBB rendererBoundingBox;
	public int chunkIndex;
	public boolean isVisible = true;
	public boolean isWaitingOnOcclusionQuery;
	public int glOcclusionQuery;
	public boolean isChunkLit;
	protected boolean isInitialized = false;
	public List<TileEntity> tileEntityRenderers = new ArrayList<TileEntity>();
	protected List<TileEntity> tileEntities;
	protected int bytesDrawn;
	public boolean isVisibleFromPosition = false;
	public double visibleFromX;
	public double visibleFromY;
	public double visibleFromZ;
	public boolean isInFrustrumFully = false;
	protected boolean needsBoxUpdate = false;
	public volatile boolean isUpdating = false;
	public static int globalChunkOffsetX = 0;
	public static int globalChunkOffsetZ = 0;

	public WorldRenderer(World par1World, List<TileEntity> par2List, int par3, int par4, int par5, int par6) {
		this.worldObj = par1World;
		this.tileEntities = par2List;
		this.glRenderList = par6;
		this.posX = -999;
		this.setPosition(par3, par4, par5);
		this.needsUpdate = false;
	}

	public void setPosition(int px, int py, int pz) {
		if(px != this.posX || py != this.posY || pz != this.posZ) {
			this.setDontDraw();
			this.posX = px;
			this.posY = py;
			this.posZ = pz;
			this.posXPlus = px + 8;
			this.posYPlus = py + 8;
			this.posZPlus = pz + 8;
			this.posXClip = px & 1023;
			this.posYClip = py;
			this.posZClip = pz & 1023;
			this.posXMinus = px - this.posXClip;
			this.posYMinus = py - this.posYClip;
			this.posZMinus = pz - this.posZClip;
			this.rendererBoundingBox = AxisAlignedBB.getBoundingBox(px, py, pz, px + 16, py + 16, pz + 16);
			this.needsBoxUpdate = true;
			this.markDirty();
			this.isVisibleFromPosition = false;
		}
	}

	public void updateRenderer() {
		if(this.worldObj != null) {
			if(this.needsUpdate) {
				if(this.needsBoxUpdate) {
					GL11.glNewList(this.glRenderList + 2, GL11.GL_COMPILE);
					RenderItem.renderAABB(AxisAlignedBB.getBoundingBoxFromPool(this.posXClip, this.posYClip, this.posZClip, this.posXClip + 16, this.posYClip + 16, this.posZClip + 16));
					GL11.glEndList();
					this.needsBoxUpdate = false;
				}

				this.isVisible = true;
				this.isVisibleFromPosition = false;
				this.needsUpdate = false;
				int xMin = this.posX;
				int yMin = this.posY;
				int zMin = this.posZ;
				int xMax = this.posX + 16;
				int yMax = this.posY + 16;
				int zMax = this.posZ + 16;

				this.skipRenderPass[0] = true;
				this.skipRenderPass[1] = true;

				Chunk.isLit = false;
				List<TileEntity> oldRenderers = new ArrayList<TileEntity>(this.tileEntityRenderers);
				this.tileEntityRenderers.clear();
				ChunkCache chunkcache = new ChunkCache(this.worldObj, xMin - 1, yMin - 1, zMin - 1, xMax + 1, yMax + 1, zMax + 1);
				Chunk centerChunk = this.worldObj.getChunkFromChunkCoords(this.posX >> 4, this.posZ >> 4);

				++chunksUpdated;
				RenderBlocks renderblocks = new RenderBlocks(chunkcache);
				this.bytesDrawn = 0;
				Tessellator tessellator = Tessellator.instance;

				for(int renderPass = 0; renderPass < 2; ++renderPass) {

					boolean renderNextPass = false;
					boolean hasRenderedBlocks = false;
					boolean hasGlList = false;

					renderblocks.setActiveRenderPass(renderPass);

					for(int y = yMin; y < yMax; ++y) {
						for(int z = zMin; z < zMax; ++z) {
							for(int x = xMin; x < xMax; ++x) {
								int blockId = centerChunk.getBlockID(x & 15, y, z & 15);
								if(blockId > 0) {
									if(!hasGlList) {
										hasGlList = true;
										GL11.glNewList(this.glRenderList + renderPass, GL11.GL_COMPILE);
										tessellator.setRenderingChunk(true);
										tessellator.startDrawingQuads();
										tessellator.setTranslation(-globalChunkOffsetX, 0.0D, -globalChunkOffsetZ);
									}

									Block block = Block.blocksList[blockId];
									if(renderPass == 0 && Block.isBlockContainer[blockId]) {
										TileEntity tileEnt = chunkcache.getBlockTileEntity(x, y, z);
										if(TileEntityRenderer.instance.hasSpecialRenderer(tileEnt)) {
											this.tileEntityRenderers.add(tileEnt);
										}
									}

									int blockRenderPass = block.getRenderBlockPass();

									boolean canRender = true;
									if(blockRenderPass == 2) {
										renderNextPass = true;
									} else if(blockRenderPass != renderPass) {
										renderNextPass = true;
										canRender = false;
									}

									if(canRender) {
										hasRenderedBlocks |= renderblocks.renderBlockByRenderType(block, x, y, z);
									}
								}
							}
						}
					}

					if(hasGlList) {
						this.bytesDrawn += tessellator.draw();
						GL11.glEndList();
						tessellator.setRenderingChunk(false);
						tessellator.setTranslation(0.0D, 0.0D, 0.0D);
					} else {
						hasRenderedBlocks = false;
					}

					if(hasRenderedBlocks) {
						this.skipRenderPass[renderPass] = false;
					}

					if(!renderNextPass) {
						break;
					}
				}

				for(TileEntity te : this.tileEntityRenderers) {
					if(!oldRenderers.contains(te)) {
						this.tileEntities.add(te);
					}
				}
				for(TileEntity te : oldRenderers) {
					if(!this.tileEntityRenderers.contains(te)) {
						this.tileEntities.remove(te);
					}
				}
				this.isChunkLit = Chunk.isLit;
				this.isInitialized = true;
			}
		}
	}

	public float distanceToEntitySquared(Entity par1Entity) {
		float f = (float)(par1Entity.posX - this.posXPlus);
		float f1 = (float)(par1Entity.posY - this.posYPlus);
		float f2 = (float)(par1Entity.posZ - this.posZPlus);
		return f * f + f1 * f1 + f2 * f2;
	}

	public void setDontDraw() {
		this.skipRenderPass[0] = true;
		this.skipRenderPass[1] = true;
		this.isInFrustum = false;
		this.isInitialized = false;
	}

	public void stopRendering() {
		this.setDontDraw();
		this.worldObj = null;
	}

	public int getGLCallListForPass(int par1) {
		return !this.isInFrustum ? -1 : (!this.skipRenderPass[par1] ? this.glRenderList + par1 : -1);
	}

	public void updateInFrustum(ICamera par1ICamera) {
		this.isInFrustum = par1ICamera.isBoundingBoxInFrustum(this.rendererBoundingBox);
		if(this.isInFrustum && Config.isOcclusionEnabled() && Config.isOcclusionFancy()) {
			this.isInFrustrumFully = par1ICamera.isBoundingBoxInFrustumFully(this.rendererBoundingBox);
		} else {
			this.isInFrustrumFully = false;
		}

	}

	public void callOcclusionQueryList() {
		GL11.glCallList(this.glRenderList + 2);
	}

	public boolean skipAllRenderPasses() {
		return this.isInitialized && this.skipRenderPass[0] && this.skipRenderPass[1];
	}

	public void markDirty() {
		this.needsUpdate = true;
	}
}
