package net.minecraft.src;

import java.util.HashSet;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class WorldRendererSmooth extends WorldRenderer {
	private WrUpdateState updateState = new WrUpdateState();
	public int activeSet = 0;
	public int[] activeListIndex = new int[]{0, 0};
	public int[][][] glWorkLists = new int[2][2][16];
	public boolean[] tempSkipRenderPass = new boolean[2];

	public WorldRendererSmooth(World par1World, List par2List, int par3, int par4, int par5, int par6) {
		super(par1World, par2List, par3, par4, par5, par6);
		int glWorkBase = 393216 + 64 * (this.glRenderList / 3);

		for(int set = 0; set < 2; ++set) {
			int setBase = glWorkBase + set * 2 * 16;

			for(int pass = 0; pass < 2; ++pass) {
				int passBase = setBase + pass * 16;

				for(int t = 0; t < 16; ++t) {
					this.glWorkLists[set][pass][t] = passBase + t;
				}
			}
		}

	}

	public void setPosition(int px, int py, int pz) {
		if(this.isUpdating) {
			this.updateRenderer();
		}

		super.setPosition(px, py, pz);
	}

	public void updateRenderer() {
		if(this.worldObj != null) {
			this.updateRenderer(0L);
			this.finishUpdate();
		}
	}

	public boolean updateRenderer(long finishTime) {
		if(this.worldObj == null) {
			return true;
		} else {
			this.needsUpdate = false;
			if(!this.isUpdating) {
				if(this.needsBoxUpdate) {
					float xMin = 0.0F;
					GL11.glNewList(this.glRenderList + 2, GL11.GL_COMPILE);
					RenderItem.renderAABB(AxisAlignedBB.getBoundingBoxFromPool((double)((float)this.posXClip - xMin), (double)((float)this.posYClip - xMin), (double)((float)this.posZClip - xMin), (double)((float)(this.posXClip + 16) + xMin), (double)((float)(this.posYClip + 16) + xMin), (double)((float)(this.posZClip + 16) + xMin)));
					GL11.glEndList();
					this.needsBoxUpdate = false;
				}

				if(Reflector.hasClass(3)) {
					Object object25 = Reflector.getFieldValue(30);
					Reflector.callVoid(object25, 30, new Object[0]);
					Reflector.callVoid(40, new Object[0]);
				}

				Chunk.isLit = false;
			}

			int i26 = this.posX;
			int yMin = this.posY;
			int zMin = this.posZ;
			int xMax = this.posX + 16;
			int yMax = this.posY + 16;
			int zMax = this.posZ + 16;
			ChunkCache chunkcache = null;
			RenderBlocks renderblocks = null;
			HashSet setOldEntityRenders = null;
			if(!this.isUpdating) {
				for(int setNewEntityRenderers = 0; setNewEntityRenderers < 2; ++setNewEntityRenderers) {
					this.tempSkipRenderPass[setNewEntityRenderers] = true;
				}

				byte b27 = 1;
				chunkcache = new ChunkCache(this.worldObj, i26 - b27, yMin - b27, zMin - b27, xMax + b27, yMax + b27, zMax + b27);
				renderblocks = new RenderBlocks(chunkcache);
				setOldEntityRenders = new HashSet();
				setOldEntityRenders.addAll(this.tileEntityRenderers);
				this.tileEntityRenderers.clear();
			}

			if(this.isUpdating || !chunkcache.func_48452_a()) {
				this.bytesDrawn = 0;
				Tessellator tessellator28 = Tessellator.instance;
				boolean hasForge = Reflector.hasClass(1);

				for(int renderPass = 0; renderPass < 2; ++renderPass) {
					boolean renderNextPass = false;
					boolean hasRenderedBlocks = false;
					boolean hasGlList = false;

					for(int y = yMin; y < yMax; ++y) {
						if(this.isUpdating) {
							this.isUpdating = false;
							chunkcache = this.updateState.chunkcache;
							renderblocks = this.updateState.renderblocks;
							setOldEntityRenders = this.updateState.setOldEntityRenders;
							renderPass = this.updateState.renderPass;
							y = this.updateState.y;
							renderNextPass = this.updateState.flag;
							hasRenderedBlocks = this.updateState.hasRenderedBlocks;
							hasGlList = this.updateState.hasGlList;
							if(hasGlList) {
								GL11.glNewList(this.glWorkLists[this.activeSet][renderPass][this.activeListIndex[renderPass]], GL11.GL_COMPILE);
								if(hasForge) {
									Reflector.callVoid(13, new Object[]{renderPass});
								}

								tessellator28.setRenderingChunk(true);
								tessellator28.startDrawingQuads();
								tessellator28.setTranslation((double)(-globalChunkOffsetX), 0.0D, (double)(-globalChunkOffsetZ));
							}
						} else if(hasGlList && finishTime != 0L && System.nanoTime() - finishTime > 0L && this.activeListIndex[renderPass] < 15) {
							if(hasForge) {
								Reflector.callVoid(14, new Object[]{renderPass});
							}

							tessellator28.draw();
							GL11.glEndList();
							tessellator28.setRenderingChunk(false);
							tessellator28.setTranslation(0.0D, 0.0D, 0.0D);
							++this.activeListIndex[renderPass];
							this.updateState.chunkcache = chunkcache;
							this.updateState.renderblocks = renderblocks;
							this.updateState.setOldEntityRenders = setOldEntityRenders;
							this.updateState.renderPass = renderPass;
							this.updateState.y = y;
							this.updateState.flag = renderNextPass;
							this.updateState.hasRenderedBlocks = hasRenderedBlocks;
							this.updateState.hasGlList = hasGlList;
							this.isUpdating = true;
							return false;
						}

						for(int z = zMin; z < zMax; ++z) {
							for(int x = i26; x < xMax; ++x) {
								int i3 = chunkcache.getBlockId(x, y, z);
								if(i3 > 0) {
									if(!hasGlList) {
										hasGlList = true;
										GL11.glNewList(this.glWorkLists[this.activeSet][renderPass][this.activeListIndex[renderPass]], GL11.GL_COMPILE);
										if(hasForge) {
											Reflector.callVoid(13, new Object[]{renderPass});
										}

										tessellator28.setRenderingChunk(true);
										tessellator28.startDrawingQuads();
										tessellator28.setTranslation((double)(-globalChunkOffsetX), 0.0D, (double)(-globalChunkOffsetZ));
									}

									Block block = Block.blocksList[i3];
									if(renderPass == 0 && block.hasTileEntity()) {
										TileEntity blockPass = chunkcache.getBlockTileEntity(x, y, z);
										if(TileEntityRenderer.instance.hasSpecialRenderer(blockPass)) {
											this.tileEntityRenderers.add(blockPass);
										}
									}

									int i30 = block.getRenderBlockPass();
									boolean canRender = true;
									if(i30 != renderPass) {
										renderNextPass = true;
										canRender = false;
									}

									if(hasForge) {
										canRender = Reflector.callBoolean(11, new Object[]{block, renderPass});
									}

									if(canRender) {
										if(hasForge) {
											Reflector.callVoid(15, new Object[]{block, renderblocks});
										}

										hasRenderedBlocks |= renderblocks.renderBlockByRenderType(block, x, y, z);
										if(hasForge) {
											Reflector.callVoid(16, new Object[]{block, renderblocks});
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

						this.bytesDrawn += tessellator28.draw();
						GL11.glEndList();
						tessellator28.setRenderingChunk(false);
						tessellator28.setTranslation(0.0D, 0.0D, 0.0D);
					} else {
						hasRenderedBlocks = false;
					}

					if(hasRenderedBlocks) {
						this.tempSkipRenderPass[renderPass] = false;
					}

					if(!renderNextPass) {
						break;
					}
				}
			}

			HashSet hashSet29 = new HashSet();
			hashSet29.addAll(this.tileEntityRenderers);
			hashSet29.removeAll(setOldEntityRenders);
			this.tileEntities.addAll(hashSet29);
			setOldEntityRenders.removeAll(this.tileEntityRenderers);
			this.tileEntities.removeAll(setOldEntityRenders);
			this.isChunkLit = Chunk.isLit;
			this.isInitialized = true;
			++chunksUpdated;
			this.isVisible = true;
			this.isVisibleFromPosition = false;
			this.skipRenderPass[0] = this.tempSkipRenderPass[0];
			this.skipRenderPass[1] = this.tempSkipRenderPass[1];
			this.isUpdating = false;
			return true;
		}
	}

	public void finishUpdate() {
		int pass;
		int i;
		int list;
		for(pass = 0; pass < 2; ++pass) {
			if(!this.skipRenderPass[pass]) {
				GL11.glNewList(this.glRenderList + pass, GL11.GL_COMPILE);

				for(i = 0; i <= this.activeListIndex[pass]; ++i) {
					list = this.glWorkLists[this.activeSet][pass][i];
					GL11.glCallList(list);
				}

				GL11.glEndList();
			}
		}

		if(this.activeSet == 0) {
			this.activeSet = 1;
		} else {
			this.activeSet = 0;
		}

		for(pass = 0; pass < 2; ++pass) {
			if(!this.skipRenderPass[pass]) {
				for(i = 0; i <= this.activeListIndex[pass]; ++i) {
					list = this.glWorkLists[this.activeSet][pass][i];
					GL11.glNewList(list, GL11.GL_COMPILE);
					GL11.glEndList();
				}
			}
		}

		for(pass = 0; pass < 2; ++pass) {
			this.activeListIndex[pass] = 0;
		}

	}
}
