package net.minecraft.src;

import java.util.HashSet;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class WorldRendererThreaded extends WorldRenderer {
	private int glRenderListStable = this.glRenderList + 393216;
	private int glRenderListBoundingBox = this.glRenderList + 2;

	public WorldRendererThreaded(World par1World, List par2List, int par3, int par4, int par5, int par6) {
		super(par1World, par2List, par3, par4, par5, par6);
	}

	public void updateRenderer() {
		if(this.worldObj != null) {
			this.updateRenderer((IWrUpdateListener)null);
			this.finishUpdate();
		}
	}

	public void updateRenderer(IWrUpdateListener updateListener) {
		if(this.worldObj != null) {
			this.needsUpdate = false;
			int xMin = this.posX;
			int yMin = this.posY;
			int zMin = this.posZ;
			int xMax = this.posX + 16;
			int yMax = this.posY + 16;
			int zMax = this.posZ + 16;
			boolean[] tempSkipRenderPass = new boolean[2];

			for(int hashset = 0; hashset < tempSkipRenderPass.length; ++hashset) {
				tempSkipRenderPass[hashset] = true;
			}

			if(Reflector.hasClass(3)) {
				Object object27 = Reflector.getFieldValue(30);
				Reflector.callVoid(object27, 30, new Object[0]);
				Reflector.callVoid(40, new Object[0]);
			}

			Chunk.isLit = false;
			HashSet hashSet28 = new HashSet();
			hashSet28.addAll(this.tileEntityRenderers);
			this.tileEntityRenderers.clear();
			byte one = 1;
			ChunkCache chunkcache = new ChunkCache(this.worldObj, xMin - one, yMin - one, zMin - one, xMax + one, yMax + one, zMax + one);
			if(!chunkcache.func_48452_a()) {
				++chunksUpdated;
				RenderBlocks hashset1 = new RenderBlocks(chunkcache);
				this.bytesDrawn = 0;
				Tessellator tessellator = Tessellator.instance;
				boolean hasForge = Reflector.hasClass(1);
				WrUpdateControl uc = new WrUpdateControl();

				for(int renderPass = 0; renderPass < 2; ++renderPass) {
					uc.setRenderPass(renderPass);
					boolean renderNextPass = false;
					boolean hasRenderedBlocks = false;
					boolean hasGlList = false;

					for(int y = yMin; y < yMax; ++y) {
						if(hasRenderedBlocks && updateListener != null) {
							updateListener.updating(uc);
						}

						for(int z = zMin; z < zMax; ++z) {
							for(int x = xMin; x < xMax; ++x) {
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

									int i31 = block.getRenderBlockPass();
									boolean canRender = true;
									if(i31 != renderPass) {
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
						if(updateListener != null) {
							updateListener.updating(uc);
						}

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
						tempSkipRenderPass[renderPass] = false;
					}

					if(!renderNextPass) {
						break;
					}
				}
			}

			for(int i29 = 0; i29 < 2; ++i29) {
				this.skipRenderPass[i29] = tempSkipRenderPass[i29];
			}

			HashSet hashSet30 = new HashSet();
			hashSet30.addAll(this.tileEntityRenderers);
			hashSet30.removeAll(hashSet28);
			this.tileEntities.addAll(hashSet30);
			hashSet28.removeAll(this.tileEntityRenderers);
			this.tileEntities.removeAll(hashSet28);
			this.isChunkLit = Chunk.isLit;
			this.isInitialized = true;
			this.isVisible = true;
			this.isVisibleFromPosition = false;
		}
	}

	public void finishUpdate() {
		int temp = this.glRenderList;
		this.glRenderList = this.glRenderListStable;
		this.glRenderListStable = temp;

		for(int f = 0; f < 2; ++f) {
			if(!this.skipRenderPass[f]) {
				GL11.glNewList(this.glRenderList + f, GL11.GL_COMPILE);
				GL11.glEndList();
			}
		}

		if(this.needsBoxUpdate && !this.skipAllRenderPasses()) {
			float f3 = 0.0F;
			GL11.glNewList(this.glRenderListBoundingBox, GL11.GL_COMPILE);
			RenderItem.renderAABB(AxisAlignedBB.getBoundingBoxFromPool((double)((float)this.posXClip - f3), (double)((float)this.posYClip - f3), (double)((float)this.posZClip - f3), (double)((float)(this.posXClip + 16) + f3), (double)((float)(this.posYClip + 16) + f3), (double)((float)(this.posZClip + 16) + f3)));
			GL11.glEndList();
		}

	}

	public int getGLCallListForPass(int par1) {
		return !this.isInFrustum ? -1 : (!this.skipRenderPass[par1] ? this.glRenderListStable + par1 : -1);
	}

	public void callOcclusionQueryList() {
		GL11.glCallList(this.glRenderListBoundingBox);
	}
}
