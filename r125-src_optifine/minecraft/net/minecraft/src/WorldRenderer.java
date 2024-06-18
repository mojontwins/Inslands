package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class WorldRenderer {
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
	public List tileEntityRenderers = new ArrayList();
	protected List tileEntities;
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

	public WorldRenderer(World par1World, List par2List, int par3, int par4, int par5, int par6) {
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

	private void setupGLTranslation() {
		GL11.glTranslatef((float)this.posXClip, (float)this.posYClip, (float)this.posZClip);
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

				for(int hashset = 0; hashset < 2; ++hashset) {
					this.skipRenderPass[hashset] = true;
				}

				if(Reflector.hasClass(3)) {
					Object object25 = Reflector.getFieldValue(30);
					Reflector.callVoid(object25, 30, new Object[0]);
					Reflector.callVoid(40, new Object[0]);
				}

				Chunk.isLit = false;
				HashSet hashSet26 = new HashSet();
				hashSet26.addAll(this.tileEntityRenderers);
				this.tileEntityRenderers.clear();
				byte one = 1;
				ChunkCache chunkcache = new ChunkCache(this.worldObj, i24 - one, yMin - one, zMin - one, xMax + one, yMax + one, zMax + one);
				if(!chunkcache.func_48452_a()) {
					++chunksUpdated;
					RenderBlocks hashset1 = new RenderBlocks(chunkcache);
					this.bytesDrawn = 0;
					Tessellator tessellator = Tessellator.instance;
					boolean hasForge = Reflector.hasClass(1);

					for(int renderPass = 0; renderPass < 2; ++renderPass) {
						boolean renderNextPass = false;
						boolean hasRenderedBlocks = false;
						boolean hasGlList = false;

						for(int y = yMin; y < yMax; ++y) {
							for(int z = zMin; z < zMax; ++z) {
								for(int x = i24; x < xMax; ++x) {
									int i3 = chunkcache.getBlockId(x, y, z);
									if(i3 > 0) {
										if(!hasGlList) {
											hasGlList = true;
											GL11.glNewList(this.glRenderList + renderPass, GL11.GL_COMPILE);
											tessellator.setRenderingChunk(true);
											if(hasForge) {
												Reflector.callVoid(13, new Object[]{renderPass});
											}

											tessellator.startDrawingQuads();
											tessellator.setTranslation((double)(-globalChunkOffsetX), 0.0D, (double)(-globalChunkOffsetZ));
										}

										Block block = Block.blocksList[i3];
										if(renderPass == 0 && block.hasTileEntity()) {
											TileEntity blockPass = chunkcache.getBlockTileEntity(x, y, z);
											if(TileEntityRenderer.instance.hasSpecialRenderer(blockPass)) {
												this.tileEntityRenderers.add(blockPass);
											}
										}

										int i28 = block.getRenderBlockPass();
										boolean canRender = true;
										if(i28 != renderPass) {
											renderNextPass = true;
											canRender = false;
										}

										if(hasForge) {
											canRender = Reflector.callBoolean(11, new Object[]{block, renderPass});
										}

										if(canRender) {
											if(hasForge) {
												Reflector.callVoid(15, new Object[]{block, hashset1});
											}

											hasRenderedBlocks |= hashset1.renderBlockByRenderType(block, x, y, z);
											if(hasForge) {
												Reflector.callVoid(16, new Object[]{block, hashset1});
											}
										}
									}
								}
							}
						}

						if(hasGlList) {
							if(hasForge) {
								Reflector.callVoid(14, new Object[]{renderPass});
							}

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
				}

				HashSet hashSet27 = new HashSet();
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
