package net.minecraft.client.renderer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Config;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCache;
import net.minecraft.src.Entity;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

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
			float f = 0.0F;
			this.rendererBoundingBox = AxisAlignedBB.getBoundingBox((double)((float)px - f), (double)((float)py - f), (double)((float)pz - f), (double)((float)(px + 16) + f), (double)((float)(py + 16) + f), (double)((float)(pz + 16) + f));
			this.needsBoxUpdate = true;
			this.markDirty();
			this.isVisibleFromPosition = false;
		}
	}

	public void updateRenderer() {
		if(this.worldObj != null) {
			if(this.needsUpdate) {
				if(this.needsBoxUpdate) {
					float xMin = 0.0F;
					GL11.glNewList(this.glRenderList + 2, GL11.GL_COMPILE);
					RenderItem.renderAABB(AxisAlignedBB.getBoundingBoxFromPool((double)((float)this.posXClip - xMin), (double)((float)this.posYClip - xMin), (double)((float)this.posZClip - xMin), (double)((float)(this.posXClip + 16) + xMin), (double)((float)(this.posYClip + 16) + xMin), (double)((float)(this.posZClip + 16) + xMin)));
					GL11.glEndList();
					this.needsBoxUpdate = false;
				}

				this.isVisible = true;
				this.isVisibleFromPosition = false;
				this.needsUpdate = false;
				int i24 = this.posX;
				int yMin = this.posY;
				int zMin = this.posZ;
				int xMax = this.posX + 16;
				int yMax = this.posY + 16;
				int zMax = this.posZ + 16;

				for(int pass = 0; pass < 2; ++pass) {
					this.skipRenderPass[pass] = true;
				}

				Chunk.isLit = false;
				HashSet<TileEntity> hashSet26 = new HashSet<TileEntity>();
				hashSet26.addAll(this.tileEntityRenderers);
				this.tileEntityRenderers.clear();
				byte one = 1;
				ChunkCache chunkcache = new ChunkCache(this.worldObj, i24 - one, yMin - one, zMin - one, xMax + one, yMax + one, zMax + one);

					++chunksUpdated;
					RenderBlocks renderblocks = new RenderBlocks(chunkcache);
					this.bytesDrawn = 0;
					Tessellator tessellator = Tessellator.instance;
					
					for(int renderPass = 0; renderPass < 2; ++renderPass) {

						boolean renderNextPass = false;
						boolean hasRenderedBlocks = false;
						boolean hasGlList = false;
						
						// Added: Make renderBlocks aware of the current render pass
						renderblocks.setActiveRenderPass(renderPass);

						for(int y = yMin; y < yMax; ++y) {
							for(int z = zMin; z < zMax; ++z) {
								for(int x = i24; x < xMax; ++x) {
									int i3 = chunkcache.getBlockId(x, y, z);
									if(i3 > 0) {
										if(!hasGlList) {
											hasGlList = true;
											GL11.glNewList(this.glRenderList + renderPass, GL11.GL_COMPILE);
											tessellator.setRenderingChunk(true);
											
											tessellator.startDrawingQuads();
											tessellator.setTranslation((double)(-globalChunkOffsetX), 0.0D, (double)(-globalChunkOffsetZ));
										}

										Block block = Block.blocksList[i3];
										if(renderPass == 0 && Block.isBlockContainer[i3]) {
											TileEntity blockPass = chunkcache.getBlockTileEntity(x, y, z);
											if(TileEntityRenderer.instance.hasSpecialRenderer(blockPass)) {
												this.tileEntityRenderers.add(blockPass);
											}
										}

										int blockPass = block.getRenderBlockPass();
										// Will return 0 for solid, 1 for translucent, 2 for BOTH (special)
										
										boolean canRender = true;
										if(blockPass == 2) {
											renderNextPass = true;
										} else if(blockPass != renderPass) {
											renderNextPass = true;
											canRender = false;
										}

										if(canRender || blockPass == 2) {
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
				
				HashSet<TileEntity> hashSet27 = new HashSet<TileEntity>();
				hashSet27.addAll(this.tileEntityRenderers);
				hashSet27.removeAll(hashSet26);
				this.tileEntities.addAll(hashSet27);
				hashSet26.removeAll(this.tileEntityRenderers);
				this.tileEntities.removeAll(hashSet26);
				this.isChunkLit = Chunk.isLit;
				this.isInitialized = true;
			}
		}
	}

	public float distanceToEntitySquared(Entity par1Entity) {
		float f = (float)(par1Entity.posX - (double)this.posXPlus);
		float f1 = (float)(par1Entity.posY - (double)this.posYPlus);
		float f2 = (float)(par1Entity.posZ - (double)this.posZPlus);
		return f * f + f1 * f1 + f2 * f2;
	}

	public void setDontDraw() {
		for(int i = 0; i < 2; ++i) {
			this.skipRenderPass[i] = true;
		}

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
		return !this.isInitialized ? false : this.skipRenderPass[0] && this.skipRenderPass[1];
	}

	public void markDirty() {
		this.needsUpdate = true;
	}
}
