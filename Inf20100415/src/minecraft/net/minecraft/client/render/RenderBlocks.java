package net.minecraft.client.render;

import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.material.Material;

import org.lwjgl.opengl.GL11;

public final class RenderBlocks {
	private World blockAccess;
	private int overrideBlockTexture = -1;
	private boolean flipTexture = false;

	public RenderBlocks(World world1) {
		this.blockAccess = world1;
	}

	public RenderBlocks() {
	}

	public final void renderBlockUsingTexture(Block block1, int i2, int i3, int i4, int i5) {
		this.overrideBlockTexture = i5;
		this.renderBlockByRenderType(block1, i2, i3, i4);
		this.overrideBlockTexture = -1;
	}

	public final void renderBlockAllFaces(Block block1, int i2, int i3, int i4) {
		this.flipTexture = true;
		this.renderBlockByRenderType(block1, i2, i3, i4);
		this.flipTexture = false;
	}

	public final boolean renderBlockByRenderType(Block block1, int i2, int i3, int i4) {
		int i5;
		Tessellator tessellator6;
		boolean z45;
		if((i5 = block1.getRenderType()) == 0) {
			tessellator6 = Tessellator.instance;
			z45 = false;
			float f47 = block1.getBlockBrightness(this.blockAccess, i2, i3, i4);
			float f49;
			if(this.flipTexture || block1.shouldSideBeRendered(this.blockAccess, i2, i3 - 1, i4, 0)) {
				f49 = block1.getBlockBrightness(this.blockAccess, i2, i3 - 1, i4);
				if(Block.lightValue[block1.blockID] > 0) {
					f49 = 1.0F;
				}

				tessellator6.setColorOpaque_F(0.5F * f49, 0.5F * f49, 0.5F * f49);
				this.renderBlockBottom(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 0));
				z45 = true;
			}

			if(this.flipTexture || block1.shouldSideBeRendered(this.blockAccess, i2, i3 + 1, i4, 1)) {
				f49 = block1.getBlockBrightness(this.blockAccess, i2, i3 + 1, i4);
				if(block1.maxY != 1.0D && !block1.blockMaterial.getIsLiquid()) {
					f49 = f47;
				}

				if(Block.lightValue[block1.blockID] > 0) {
					f49 = 1.0F;
				}

				tessellator6.setColorOpaque_F(f49 * 1.0F, f49 * 1.0F, f49 * 1.0F);
				this.renderBlockTop(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 1));
				z45 = true;
			}

			if(this.flipTexture || block1.shouldSideBeRendered(this.blockAccess, i2, i3, i4 - 1, 2)) {
				f49 = block1.getBlockBrightness(this.blockAccess, i2, i3, i4 - 1);
				if(Block.lightValue[block1.blockID] > 0) {
					f49 = 1.0F;
				}

				tessellator6.setColorOpaque_F(0.8F * f49, 0.8F * f49, 0.8F * f49);
				this.renderBlockNorth(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 2));
				z45 = true;
			}

			if(this.flipTexture || block1.shouldSideBeRendered(this.blockAccess, i2, i3, i4 + 1, 3)) {
				f49 = block1.getBlockBrightness(this.blockAccess, i2, i3, i4 + 1);
				if(Block.lightValue[block1.blockID] > 0) {
					f49 = 1.0F;
				}

				tessellator6.setColorOpaque_F(0.8F * f49, 0.8F * f49, 0.8F * f49);
				this.renderBlockSouth(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 3));
				z45 = true;
			}

			if(this.flipTexture || block1.shouldSideBeRendered(this.blockAccess, i2 - 1, i3, i4, 4)) {
				f49 = block1.getBlockBrightness(this.blockAccess, i2 - 1, i3, i4);
				if(Block.lightValue[block1.blockID] > 0) {
					f49 = 1.0F;
				}

				tessellator6.setColorOpaque_F(0.6F * f49, 0.6F * f49, 0.6F * f49);
				this.renderBlockWest(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 4));
				z45 = true;
			}

			if(this.flipTexture || block1.shouldSideBeRendered(this.blockAccess, i2 + 1, i3, i4, 5)) {
				f49 = block1.getBlockBrightness(this.blockAccess, i2 + 1, i3, i4);
				if(Block.lightValue[block1.blockID] > 0) {
					f49 = 1.0F;
				}

				tessellator6.setColorOpaque_F(0.6F * f49, 0.6F * f49, 0.6F * f49);
				this.renderBlockEast(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTexture(this.blockAccess, i2, i3, i4, 5));
				z45 = true;
			}

			return z45;
		} else {
			double d17;
			double d19;
			if(i5 == 4) {
				tessellator6 = Tessellator.instance;
				z45 = false;
				d17 = block1.minY;
				d19 = block1.maxY;
				block1.maxY = d19 - (double)this.materialNotWater(i2, i3, i4);
				float f48;
				if(this.flipTexture || block1.shouldSideBeRendered(this.blockAccess, i2, i3 - 1, i4, 0)) {
					f48 = block1.getBlockBrightness(this.blockAccess, i2, i3 - 1, i4);
					tessellator6.setColorOpaque_F(0.5F * f48, 0.5F * f48, 0.5F * f48);
					this.renderBlockBottom(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTextureFromSide(0));
					z45 = true;
				}

				if(this.flipTexture || block1.shouldSideBeRendered(this.blockAccess, i2, i3 + 1, i4, 1)) {
					f48 = block1.getBlockBrightness(this.blockAccess, i2, i3 + 1, i4);
					tessellator6.setColorOpaque_F(f48 * 1.0F, f48 * 1.0F, f48 * 1.0F);
					this.renderBlockTop(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTextureFromSide(1));
					z45 = true;
				}

				block1.minY = d19 - (double)this.materialNotWater(i2, i3, i4 - 1);
				if(this.flipTexture || block1.maxY > block1.minY || block1.shouldSideBeRendered(this.blockAccess, i2, i3, i4 - 1, 2)) {
					f48 = block1.getBlockBrightness(this.blockAccess, i2, i3, i4 - 1);
					tessellator6.setColorOpaque_F(0.8F * f48, 0.8F * f48, 0.8F * f48);
					this.renderBlockNorth(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTextureFromSide(2));
					z45 = true;
				}

				block1.minY = d19 - (double)this.materialNotWater(i2, i3, i4 + 1);
				if(this.flipTexture || block1.maxY > block1.minY || block1.shouldSideBeRendered(this.blockAccess, i2, i3, i4 + 1, 3)) {
					f48 = block1.getBlockBrightness(this.blockAccess, i2, i3, i4 + 1);
					tessellator6.setColorOpaque_F(0.8F * f48, 0.8F * f48, 0.8F * f48);
					this.renderBlockSouth(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTextureFromSide(3));
					z45 = true;
				}

				block1.minY = d19 - (double)this.materialNotWater(i2 - 1, i3, i4);
				if(this.flipTexture || block1.maxY > block1.minY || block1.shouldSideBeRendered(this.blockAccess, i2 - 1, i3, i4, 4)) {
					f48 = block1.getBlockBrightness(this.blockAccess, i2 - 1, i3, i4);
					tessellator6.setColorOpaque_F(0.6F * f48, 0.6F * f48, 0.6F * f48);
					this.renderBlockWest(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTextureFromSide(4));
					z45 = true;
				}

				block1.minY = d19 - (double)this.materialNotWater(i2 + 1, i3, i4);
				if(this.flipTexture || block1.maxY > block1.minY || block1.shouldSideBeRendered(this.blockAccess, i2 + 1, i3, i4, 5)) {
					f48 = block1.getBlockBrightness(this.blockAccess, i2 + 1, i3, i4);
					tessellator6.setColorOpaque_F(0.6F * f48, 0.6F * f48, 0.6F * f48);
					this.renderBlockEast(block1, (double)i2, (double)i3, (double)i4, block1.getBlockTextureFromSide(5));
					z45 = true;
				}

				block1.minY = d17;
				block1.maxY = d19;
				return z45;
			} else {
				float f44;
				if(i5 == 1) {
					tessellator6 = Tessellator.instance;
					f44 = block1.getBlockBrightness(this.blockAccess, i2, i3, i4);
					tessellator6.setColorOpaque_F(f44, f44, f44);
					this.renderBlockPlant(block1, this.blockAccess.getBlockMetadata(i2, i3, i4), (double)i2, (double)i3, (double)i4);
					return true;
				} else if(i5 == 6) {
					tessellator6 = Tessellator.instance;
					f44 = block1.getBlockBrightness(this.blockAccess, i2, i3, i4);
					tessellator6.setColorOpaque_F(f44, f44, f44);
					this.renderBlockCrops(block1, this.blockAccess.getBlockMetadata(i2, i3, i4), (double)i2, (double)((float)i3 - 0.0625F), (double)i4);
					return true;
				} else {
					float f8;
					if(i5 == 2) {
						int i41 = this.blockAccess.getBlockMetadata(i2, i3, i4);
						Tessellator tessellator43 = Tessellator.instance;
						f8 = block1.getBlockBrightness(this.blockAccess, i2, i3, i4);
						if(Block.lightValue[block1.blockID] > 0) {
							f8 = 1.0F;
						}

						tessellator43.setColorOpaque_F(f8, f8, f8);
						if(i41 == 1) {
							this.renderBlockTorch(block1, (double)i2 - 0.09999999403953552D, (double)i3 + (double)0.2F, (double)i4, -0.4000000059604645D, 0.0D);
						} else if(i41 == 2) {
							this.renderBlockTorch(block1, (double)i2 + 0.09999999403953552D, (double)i3 + (double)0.2F, (double)i4, (double)0.4F, 0.0D);
						} else if(i41 == 3) {
							this.renderBlockTorch(block1, (double)i2, (double)i3 + (double)0.2F, (double)i4 - 0.09999999403953552D, 0.0D, -0.4000000059604645D);
						} else if(i41 == 4) {
							this.renderBlockTorch(block1, (double)i2, (double)i3 + (double)0.2F, (double)i4 + 0.09999999403953552D, 0.0D, (double)0.4F);
						} else {
							this.renderBlockTorch(block1, (double)i2, (double)i3, (double)i4, 0.0D, 0.0D);
						}

						return true;
					} else {
						int i7;
						double d25;
						double d27;
						double d29;
						double d31;
						int i42;
						if(i5 == 3) {
							i5 = i4;
							i4 = i3;
							i3 = i2;
							tessellator6 = Tessellator.instance;
							i7 = block1.getBlockTextureFromSide(0);
							if(this.overrideBlockTexture >= 0) {
								i7 = this.overrideBlockTexture;
							}

							f8 = block1.getBlockBrightness(this.blockAccess, i2, i4, i5);
							tessellator6.setColorOpaque_F(f8, f8, f8);
							i2 = (i7 & 15) << 4;
							i42 = i7 & 240;
							double d46 = (double)((float)i2 / 256.0F);
							double d18 = (double)(((float)i2 + 15.99F) / 256.0F);
							double d20 = (double)((float)i42 / 256.0F);
							double d22 = (double)(((float)i42 + 15.99F) / 256.0F);
							double d33;
							if(!this.blockAccess.isSolid(i3, i4 - 1, i5) && !Block.fire.canBlockCatchFire(this.blockAccess, i3, i4 - 1, i5)) {
								if((i3 + i4 + i5 & 1) == 1) {
									d46 = (double)((float)i2 / 256.0F);
									d18 = (double)(((float)i2 + 15.99F) / 256.0F);
									d20 = (double)((float)(i42 + 16) / 256.0F);
									d22 = (double)(((float)i42 + 15.99F + 16.0F) / 256.0F);
								}

								if((i3 / 2 + i4 / 2 + i5 / 2 & 1) == 1) {
									d27 = d18;
									d18 = d46;
									d46 = d27;
								}

								if(Block.fire.canBlockCatchFire(this.blockAccess, i3 - 1, i4, i5)) {
									tessellator6.addVertexWithUV((double)((float)i3 + 0.2F), (double)((float)i4 + 1.4F + 0.0625F), (double)(i5 + 1), d18, d20);
									tessellator6.addVertexWithUV((double)i3, (double)((float)i4 + 0.0625F), (double)(i5 + 1), d18, d22);
									tessellator6.addVertexWithUV((double)i3, (double)((float)i4 + 0.0625F), (double)i5, d46, d22);
									tessellator6.addVertexWithUV((double)((float)i3 + 0.2F), (double)((float)i4 + 1.4F + 0.0625F), (double)i5, d46, d20);
									tessellator6.addVertexWithUV((double)((float)i3 + 0.2F), (double)((float)i4 + 1.4F + 0.0625F), (double)i5, d46, d20);
									tessellator6.addVertexWithUV((double)i3, (double)((float)i4 + 0.0625F), (double)i5, d46, d22);
									tessellator6.addVertexWithUV((double)i3, (double)((float)i4 + 0.0625F), (double)(i5 + 1), d18, d22);
									tessellator6.addVertexWithUV((double)((float)i3 + 0.2F), (double)((float)i4 + 1.4F + 0.0625F), (double)(i5 + 1), d18, d20);
								}

								if(Block.fire.canBlockCatchFire(this.blockAccess, i3 + 1, i4, i5)) {
									tessellator6.addVertexWithUV((double)((float)(i3 + 1) - 0.2F), (double)((float)i4 + 1.4F + 0.0625F), (double)i5, d46, d20);
									tessellator6.addVertexWithUV((double)(i3 + 1), (double)((float)i4 + 0.0625F), (double)i5, d46, d22);
									tessellator6.addVertexWithUV((double)(i3 + 1), (double)((float)i4 + 0.0625F), (double)(i5 + 1), d18, d22);
									tessellator6.addVertexWithUV((double)((float)(i3 + 1) - 0.2F), (double)((float)i4 + 1.4F + 0.0625F), (double)(i5 + 1), d18, d20);
									tessellator6.addVertexWithUV((double)((float)(i3 + 1) - 0.2F), (double)((float)i4 + 1.4F + 0.0625F), (double)(i5 + 1), d18, d20);
									tessellator6.addVertexWithUV((double)(i3 + 1), (double)((float)i4 + 0.0625F), (double)(i5 + 1), d18, d22);
									tessellator6.addVertexWithUV((double)(i3 + 1), (double)((float)i4 + 0.0625F), (double)i5, d46, d22);
									tessellator6.addVertexWithUV((double)((float)(i3 + 1) - 0.2F), (double)((float)i4 + 1.4F + 0.0625F), (double)i5, d46, d20);
								}

								if(Block.fire.canBlockCatchFire(this.blockAccess, i3, i4, i5 - 1)) {
									tessellator6.addVertexWithUV((double)i3, (double)((float)i4 + 1.4F + 0.0625F), (double)((float)i5 + 0.2F), d18, d20);
									tessellator6.addVertexWithUV((double)i3, (double)((float)i4 + 0.0625F), (double)i5, d18, d22);
									tessellator6.addVertexWithUV((double)(i3 + 1), (double)((float)i4 + 0.0625F), (double)i5, d46, d22);
									tessellator6.addVertexWithUV((double)(i3 + 1), (double)((float)i4 + 1.4F + 0.0625F), (double)((float)i5 + 0.2F), d46, d20);
									tessellator6.addVertexWithUV((double)(i3 + 1), (double)((float)i4 + 1.4F + 0.0625F), (double)((float)i5 + 0.2F), d46, d20);
									tessellator6.addVertexWithUV((double)(i3 + 1), (double)((float)i4 + 0.0625F), (double)i5, d46, d22);
									tessellator6.addVertexWithUV((double)i3, (double)((float)i4 + 0.0625F), (double)i5, d18, d22);
									tessellator6.addVertexWithUV((double)i3, (double)((float)i4 + 1.4F + 0.0625F), (double)((float)i5 + 0.2F), d18, d20);
								}

								if(Block.fire.canBlockCatchFire(this.blockAccess, i3, i4, i5 + 1)) {
									tessellator6.addVertexWithUV((double)(i3 + 1), (double)((float)i4 + 1.4F + 0.0625F), (double)((float)(i5 + 1) - 0.2F), d46, d20);
									tessellator6.addVertexWithUV((double)(i3 + 1), (double)((float)i4 + 0.0625F), (double)(i5 + 1), d46, d22);
									tessellator6.addVertexWithUV((double)i3, (double)((float)i4 + 0.0625F), (double)(i5 + 1), d18, d22);
									tessellator6.addVertexWithUV((double)i3, (double)((float)i4 + 1.4F + 0.0625F), (double)((float)(i5 + 1) - 0.2F), d18, d20);
									tessellator6.addVertexWithUV((double)i3, (double)((float)i4 + 1.4F + 0.0625F), (double)((float)(i5 + 1) - 0.2F), d18, d20);
									tessellator6.addVertexWithUV((double)i3, (double)((float)i4 + 0.0625F), (double)(i5 + 1), d18, d22);
									tessellator6.addVertexWithUV((double)(i3 + 1), (double)((float)i4 + 0.0625F), (double)(i5 + 1), d46, d22);
									tessellator6.addVertexWithUV((double)(i3 + 1), (double)((float)i4 + 1.4F + 0.0625F), (double)((float)(i5 + 1) - 0.2F), d46, d20);
								}

								if(Block.fire.canBlockCatchFire(this.blockAccess, i3, i4 + 1, i5)) {
									d27 = (double)i3 + 0.5D + 0.5D;
									d29 = (double)i3 + 0.5D - 0.5D;
									d31 = (double)i5 + 0.5D + 0.5D;
									d33 = (double)i5 + 0.5D - 0.5D;
									d46 = (double)((float)i2 / 256.0F);
									d18 = (double)(((float)i2 + 15.99F) / 256.0F);
									d20 = (double)((float)i42 / 256.0F);
									d22 = (double)(((float)i42 + 15.99F) / 256.0F);
									++i4;
									if((i3 + i4 + i5 & 1) == 0) {
										tessellator6.addVertexWithUV(d29, (double)((float)i4 + -0.2F), (double)i5, d18, d20);
										tessellator6.addVertexWithUV(d27, (double)i4, (double)i5, d18, d22);
										tessellator6.addVertexWithUV(d27, (double)i4, (double)(i5 + 1), d46, d22);
										tessellator6.addVertexWithUV(d29, (double)((float)i4 + -0.2F), (double)(i5 + 1), d46, d20);
										d46 = (double)((float)i2 / 256.0F);
										d18 = (double)(((float)i2 + 15.99F) / 256.0F);
										d20 = (double)((float)(i42 + 16) / 256.0F);
										d22 = (double)(((float)i42 + 15.99F + 16.0F) / 256.0F);
										tessellator6.addVertexWithUV(d27, (double)((float)i4 + -0.2F), (double)(i5 + 1), d18, d20);
										tessellator6.addVertexWithUV(d29, (double)i4, (double)(i5 + 1), d18, d22);
										tessellator6.addVertexWithUV(d29, (double)i4, (double)i5, d46, d22);
										tessellator6.addVertexWithUV(d27, (double)((float)i4 + -0.2F), (double)i5, d46, d20);
									} else {
										tessellator6.addVertexWithUV((double)i3, (double)((float)i4 + -0.2F), d31, d18, d20);
										tessellator6.addVertexWithUV((double)i3, (double)i4, d33, d18, d22);
										tessellator6.addVertexWithUV((double)(i3 + 1), (double)i4, d33, d46, d22);
										tessellator6.addVertexWithUV((double)(i3 + 1), (double)((float)i4 + -0.2F), d31, d46, d20);
										d46 = (double)((float)i2 / 256.0F);
										d18 = (double)(((float)i2 + 15.99F) / 256.0F);
										d20 = (double)((float)(i42 + 16) / 256.0F);
										d22 = (double)(((float)i42 + 15.99F + 16.0F) / 256.0F);
										tessellator6.addVertexWithUV((double)(i3 + 1), (double)((float)i4 + -0.2F), d33, d18, d20);
										tessellator6.addVertexWithUV((double)(i3 + 1), (double)i4, d31, d18, d22);
										tessellator6.addVertexWithUV((double)i3, (double)i4, d31, d46, d22);
										tessellator6.addVertexWithUV((double)i3, (double)((float)i4 + -0.2F), d33, d46, d20);
									}
								}
							} else {
								d25 = (double)i3 + 0.5D + 0.2D;
								d27 = (double)i3 + 0.5D - 0.2D;
								d29 = (double)i5 + 0.5D + 0.2D;
								d31 = (double)i5 + 0.5D - 0.2D;
								d33 = (double)i3 + 0.5D - 0.3D;
								double d35 = (double)i3 + 0.5D + 0.3D;
								double d37 = (double)i5 + 0.5D - 0.3D;
								double d39 = (double)i5 + 0.5D + 0.3D;
								tessellator6.addVertexWithUV(d33, (double)((float)i4 + 1.4F), (double)(i5 + 1), d18, d20);
								tessellator6.addVertexWithUV(d25, (double)i4, (double)(i5 + 1), d18, d22);
								tessellator6.addVertexWithUV(d25, (double)i4, (double)i5, d46, d22);
								tessellator6.addVertexWithUV(d33, (double)((float)i4 + 1.4F), (double)i5, d46, d20);
								tessellator6.addVertexWithUV(d35, (double)((float)i4 + 1.4F), (double)i5, d18, d20);
								tessellator6.addVertexWithUV(d27, (double)i4, (double)i5, d18, d22);
								tessellator6.addVertexWithUV(d27, (double)i4, (double)(i5 + 1), d46, d22);
								tessellator6.addVertexWithUV(d35, (double)((float)i4 + 1.4F), (double)(i5 + 1), d46, d20);
								d46 = (double)((float)i2 / 256.0F);
								d18 = (double)(((float)i2 + 15.99F) / 256.0F);
								d20 = (double)((float)(i42 + 16) / 256.0F);
								d22 = (double)(((float)i42 + 15.99F + 16.0F) / 256.0F);
								tessellator6.addVertexWithUV((double)(i3 + 1), (double)((float)i4 + 1.4F), d39, d18, d20);
								tessellator6.addVertexWithUV((double)(i3 + 1), (double)i4, d31, d18, d22);
								tessellator6.addVertexWithUV((double)i3, (double)i4, d31, d46, d22);
								tessellator6.addVertexWithUV((double)i3, (double)((float)i4 + 1.4F), d39, d46, d20);
								tessellator6.addVertexWithUV((double)i3, (double)((float)i4 + 1.4F), d37, d18, d20);
								tessellator6.addVertexWithUV((double)i3, (double)i4, d29, d18, d22);
								tessellator6.addVertexWithUV((double)(i3 + 1), (double)i4, d29, d46, d22);
								tessellator6.addVertexWithUV((double)(i3 + 1), (double)((float)i4 + 1.4F), d37, d46, d20);
								d25 = (double)i3 + 0.5D - 0.5D;
								d27 = (double)i3 + 0.5D + 0.5D;
								d29 = (double)i5 + 0.5D - 0.5D;
								d31 = (double)i5 + 0.5D + 0.5D;
								d33 = (double)i3 + 0.5D - 0.4D;
								d35 = (double)i3 + 0.5D + 0.4D;
								d37 = (double)i5 + 0.5D - 0.4D;
								d39 = (double)i5 + 0.5D + 0.4D;
								tessellator6.addVertexWithUV(d33, (double)((float)i4 + 1.4F), (double)i5, d46, d20);
								tessellator6.addVertexWithUV(d25, (double)i4, (double)i5, d46, d22);
								tessellator6.addVertexWithUV(d25, (double)i4, (double)(i5 + 1), d18, d22);
								tessellator6.addVertexWithUV(d33, (double)((float)i4 + 1.4F), (double)(i5 + 1), d18, d20);
								tessellator6.addVertexWithUV(d35, (double)((float)i4 + 1.4F), (double)(i5 + 1), d46, d20);
								tessellator6.addVertexWithUV(d27, (double)i4, (double)(i5 + 1), d46, d22);
								tessellator6.addVertexWithUV(d27, (double)i4, (double)i5, d18, d22);
								tessellator6.addVertexWithUV(d35, (double)((float)i4 + 1.4F), (double)i5, d18, d20);
								d46 = (double)((float)i2 / 256.0F);
								d18 = (double)(((float)i2 + 15.99F) / 256.0F);
								d20 = (double)((float)i42 / 256.0F);
								d22 = (double)(((float)i42 + 15.99F) / 256.0F);
								tessellator6.addVertexWithUV((double)i3, (double)((float)i4 + 1.4F), d39, d46, d20);
								tessellator6.addVertexWithUV((double)i3, (double)i4, d31, d46, d22);
								tessellator6.addVertexWithUV((double)(i3 + 1), (double)i4, d31, d18, d22);
								tessellator6.addVertexWithUV((double)(i3 + 1), (double)((float)i4 + 1.4F), d39, d18, d20);
								tessellator6.addVertexWithUV((double)(i3 + 1), (double)((float)i4 + 1.4F), d37, d46, d20);
								tessellator6.addVertexWithUV((double)(i3 + 1), (double)i4, d29, d46, d22);
								tessellator6.addVertexWithUV((double)i3, (double)i4, d29, d18, d22);
								tessellator6.addVertexWithUV((double)i3, (double)((float)i4 + 1.4F), d37, d18, d20);
							}

							return true;
						} else if(i5 == 5) {
							i5 = i4;
							i4 = i3;
							i3 = i2;
							tessellator6 = Tessellator.instance;
							i7 = block1.getBlockTextureFromSide(0);
							if(this.overrideBlockTexture >= 0) {
								i7 = this.overrideBlockTexture;
							}

							f8 = block1.getBlockBrightness(this.blockAccess, i2, i4, i5);
							tessellator6.setColorOpaque_F(f8, f8, f8);
							i2 = ((i7 & 15) << 4) + 16;
							i42 = (i7 & 15) << 4;
							int i16 = i7 & 240;
							if((i3 + i4 + i5 & 1) == 1) {
								i2 = (i7 & 15) << 4;
								i42 = ((i7 & 15) << 4) + 16;
							}

							d17 = (double)((float)i2 / 256.0F);
							d19 = (double)(((float)i2 + 15.99F) / 256.0F);
							double d21 = (double)((float)i16 / 256.0F);
							double d23 = (double)(((float)i16 + 15.99F) / 256.0F);
							d25 = (double)((float)i42 / 256.0F);
							d27 = (double)(((float)i42 + 15.99F) / 256.0F);
							d29 = (double)((float)i16 / 256.0F);
							d31 = (double)(((float)i16 + 15.99F) / 256.0F);
							if(this.blockAccess.isSolid(i3 - 1, i4, i5)) {
								tessellator6.addVertexWithUV((double)((float)i3 + 0.05F), (double)((float)(i4 + 1) + 0.125F), (double)((float)(i5 + 1) + 0.125F), d17, d21);
								tessellator6.addVertexWithUV((double)((float)i3 + 0.05F), (double)((float)i4 - 0.125F), (double)((float)(i5 + 1) + 0.125F), d17, d23);
								tessellator6.addVertexWithUV((double)((float)i3 + 0.05F), (double)((float)i4 - 0.125F), (double)((float)i5 - 0.125F), d19, d23);
								tessellator6.addVertexWithUV((double)((float)i3 + 0.05F), (double)((float)(i4 + 1) + 0.125F), (double)((float)i5 - 0.125F), d19, d21);
							}

							if(this.blockAccess.isSolid(i3 + 1, i4, i5)) {
								tessellator6.addVertexWithUV((double)((float)(i3 + 1) - 0.05F), (double)((float)i4 - 0.125F), (double)((float)(i5 + 1) + 0.125F), d19, d23);
								tessellator6.addVertexWithUV((double)((float)(i3 + 1) - 0.05F), (double)((float)(i4 + 1) + 0.125F), (double)((float)(i5 + 1) + 0.125F), d19, d21);
								tessellator6.addVertexWithUV((double)((float)(i3 + 1) - 0.05F), (double)((float)(i4 + 1) + 0.125F), (double)((float)i5 - 0.125F), d17, d21);
								tessellator6.addVertexWithUV((double)((float)(i3 + 1) - 0.05F), (double)((float)i4 - 0.125F), (double)((float)i5 - 0.125F), d17, d23);
							}

							if(this.blockAccess.isSolid(i3, i4, i5 - 1)) {
								tessellator6.addVertexWithUV((double)((float)(i3 + 1) + 0.125F), (double)((float)i4 - 0.125F), (double)((float)i5 + 0.05F), d27, d31);
								tessellator6.addVertexWithUV((double)((float)(i3 + 1) + 0.125F), (double)((float)(i4 + 1) + 0.125F), (double)((float)i5 + 0.05F), d27, d29);
								tessellator6.addVertexWithUV((double)((float)i3 - 0.125F), (double)((float)(i4 + 1) + 0.125F), (double)((float)i5 + 0.05F), d25, d29);
								tessellator6.addVertexWithUV((double)((float)i3 - 0.125F), (double)((float)i4 - 0.125F), (double)((float)i5 + 0.05F), d25, d31);
							}

							if(this.blockAccess.isSolid(i3, i4, i5 + 1)) {
								tessellator6.addVertexWithUV((double)((float)(i3 + 1) + 0.125F), (double)((float)(i4 + 1) + 0.125F), (double)((float)(i5 + 1) - 0.05F), d25, d29);
								tessellator6.addVertexWithUV((double)((float)(i3 + 1) + 0.125F), (double)((float)i4 - 0.125F), (double)((float)(i5 + 1) - 0.05F), d25, d31);
								tessellator6.addVertexWithUV((double)((float)i3 - 0.125F), (double)((float)i4 - 0.125F), (double)((float)(i5 + 1) - 0.05F), d27, d31);
								tessellator6.addVertexWithUV((double)((float)i3 - 0.125F), (double)((float)(i4 + 1) + 0.125F), (double)((float)(i5 + 1) - 0.05F), d27, d29);
							}

							return true;
						} else {
							return false;
						}
					}
				}
			}
		}
	}

	private void renderBlockTorch(Block block1, double d2, double d4, double d6, double d8, double d10) {
		Tessellator tessellator12 = Tessellator.instance;
		int i36 = block1.getBlockTextureFromSide(0);
		if(this.overrideBlockTexture >= 0) {
			i36 = this.overrideBlockTexture;
		}

		int i13 = (i36 & 15) << 4;
		i36 &= 240;
		float f14 = (float)i13 / 256.0F;
		float f38 = ((float)i13 + 15.99F) / 256.0F;
		float f15 = (float)i36 / 256.0F;
		float f37 = ((float)i36 + 15.99F) / 256.0F;
		double d20 = (double)f14 + 7.0D / 256D;
		double d22 = (double)f15 + 6.0D / 256D;
		double d24 = (double)f14 + 9.0D / 256D;
		double d26 = (double)f15 + 8.0D / 256D;
		d2 += 0.5D;
		d6 += 0.5D;
		double d28 = d2 - 0.5D;
		double d30 = d2 + 0.5D;
		double d32 = d6 - 0.5D;
		double d34 = d6 + 0.5D;
		tessellator12.addVertexWithUV(d2 + d8 * 0.375D - 0.0625D, d4 + 0.625D, d6 + d10 * 0.375D - 0.0625D, d20, d22);
		tessellator12.addVertexWithUV(d2 + d8 * 0.375D - 0.0625D, d4 + 0.625D, d6 + d10 * 0.375D + 0.0625D, d20, d26);
		tessellator12.addVertexWithUV(d2 + d8 * 0.375D + 0.0625D, d4 + 0.625D, d6 + d10 * 0.375D + 0.0625D, d24, d26);
		tessellator12.addVertexWithUV(d2 + d8 * 0.375D + 0.0625D, d4 + 0.625D, d6 + d10 * 0.375D - 0.0625D, d24, d22);
		tessellator12.addVertexWithUV(d2 - 0.0625D, d4 + 1.0D, d32, (double)f14, (double)f15);
		tessellator12.addVertexWithUV(d2 - 0.0625D + d8, d4, d32 + d10, (double)f14, (double)f37);
		tessellator12.addVertexWithUV(d2 - 0.0625D + d8, d4, d34 + d10, (double)f38, (double)f37);
		tessellator12.addVertexWithUV(d2 - 0.0625D, d4 + 1.0D, d34, (double)f38, (double)f15);
		tessellator12.addVertexWithUV(d2 + 0.0625D, d4 + 1.0D, d34, (double)f14, (double)f15);
		tessellator12.addVertexWithUV(d2 + d8 + 0.0625D, d4, d34 + d10, (double)f14, (double)f37);
		tessellator12.addVertexWithUV(d2 + d8 + 0.0625D, d4, d32 + d10, (double)f38, (double)f37);
		tessellator12.addVertexWithUV(d2 + 0.0625D, d4 + 1.0D, d32, (double)f38, (double)f15);
		tessellator12.addVertexWithUV(d28, d4 + 1.0D, d6 + 0.0625D, (double)f14, (double)f15);
		tessellator12.addVertexWithUV(d28 + d8, d4, d6 + 0.0625D + d10, (double)f14, (double)f37);
		tessellator12.addVertexWithUV(d30 + d8, d4, d6 + 0.0625D + d10, (double)f38, (double)f37);
		tessellator12.addVertexWithUV(d30, d4 + 1.0D, d6 + 0.0625D, (double)f38, (double)f15);
		tessellator12.addVertexWithUV(d30, d4 + 1.0D, d6 - 0.0625D, (double)f14, (double)f15);
		tessellator12.addVertexWithUV(d30 + d8, d4, d6 - 0.0625D + d10, (double)f14, (double)f37);
		tessellator12.addVertexWithUV(d28 + d8, d4, d6 - 0.0625D + d10, (double)f38, (double)f37);
		tessellator12.addVertexWithUV(d28, d4 + 1.0D, d6 - 0.0625D, (double)f38, (double)f15);
	}

	private void renderBlockPlant(Block block1, int i2, double d3, double d5, double d7) {
		Tessellator tessellator9 = Tessellator.instance;
		int i29 = block1.getBlockTextureFromSideAndMetadata(0, i2);
		if(this.overrideBlockTexture >= 0) {
			i29 = this.overrideBlockTexture;
		}

		i2 = (i29 & 15) << 4;
		i29 &= 240;
		double d13 = (double)((float)i2 / 256.0F);
		double d15 = (double)(((float)i2 + 15.99F) / 256.0F);
		double d17 = (double)((float)i29 / 256.0F);
		double d19 = (double)(((float)i29 + 15.99F) / 256.0F);
		double d21 = d3 + 0.5D - (double)0.45F;
		double d23 = d3 + 0.5D + (double)0.45F;
		double d25 = d7 + 0.5D - (double)0.45F;
		double d27 = d7 + 0.5D + (double)0.45F;
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d25, d13, d17);
		tessellator9.addVertexWithUV(d21, d5, d25, d13, d19);
		tessellator9.addVertexWithUV(d23, d5, d27, d15, d19);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d27, d15, d17);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d27, d13, d17);
		tessellator9.addVertexWithUV(d23, d5, d27, d13, d19);
		tessellator9.addVertexWithUV(d21, d5, d25, d15, d19);
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d25, d15, d17);
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d27, d13, d17);
		tessellator9.addVertexWithUV(d21, d5, d27, d13, d19);
		tessellator9.addVertexWithUV(d23, d5, d25, d15, d19);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d25, d15, d17);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d25, d13, d17);
		tessellator9.addVertexWithUV(d23, d5, d25, d13, d19);
		tessellator9.addVertexWithUV(d21, d5, d27, d15, d19);
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d27, d15, d17);
	}

	private void renderBlockCrops(Block block1, int i2, double d3, double d5, double d7) {
		Tessellator tessellator9 = Tessellator.instance;
		int i29 = block1.getBlockTextureFromSideAndMetadata(0, i2);
		if(this.overrideBlockTexture >= 0) {
			i29 = this.overrideBlockTexture;
		}

		i2 = (i29 & 15) << 4;
		i29 &= 240;
		double d13 = (double)((float)i2 / 256.0F);
		double d15 = (double)(((float)i2 + 15.99F) / 256.0F);
		double d17 = (double)((float)i29 / 256.0F);
		double d19 = (double)(((float)i29 + 15.99F) / 256.0F);
		double d21 = d3 + 0.5D - 0.25D;
		double d23 = d3 + 0.5D + 0.25D;
		double d25 = d7 + 0.5D - 0.5D;
		double d27 = d7 + 0.5D + 0.5D;
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d25, d13, d17);
		tessellator9.addVertexWithUV(d21, d5, d25, d13, d19);
		tessellator9.addVertexWithUV(d21, d5, d27, d15, d19);
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d27, d15, d17);
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d27, d13, d17);
		tessellator9.addVertexWithUV(d21, d5, d27, d13, d19);
		tessellator9.addVertexWithUV(d21, d5, d25, d15, d19);
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d25, d15, d17);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d27, d13, d17);
		tessellator9.addVertexWithUV(d23, d5, d27, d13, d19);
		tessellator9.addVertexWithUV(d23, d5, d25, d15, d19);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d25, d15, d17);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d25, d13, d17);
		tessellator9.addVertexWithUV(d23, d5, d25, d13, d19);
		tessellator9.addVertexWithUV(d23, d5, d27, d15, d19);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d27, d15, d17);
		d21 = d3 + 0.5D - 0.5D;
		d23 = d3 + 0.5D + 0.5D;
		d25 = d7 + 0.5D - 0.25D;
		d27 = d7 + 0.5D + 0.25D;
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d25, d13, d17);
		tessellator9.addVertexWithUV(d21, d5, d25, d13, d19);
		tessellator9.addVertexWithUV(d23, d5, d25, d15, d19);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d25, d15, d17);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d25, d13, d17);
		tessellator9.addVertexWithUV(d23, d5, d25, d13, d19);
		tessellator9.addVertexWithUV(d21, d5, d25, d15, d19);
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d25, d15, d17);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d27, d13, d17);
		tessellator9.addVertexWithUV(d23, d5, d27, d13, d19);
		tessellator9.addVertexWithUV(d21, d5, d27, d15, d19);
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d27, d15, d17);
		tessellator9.addVertexWithUV(d21, d5 + 1.0D, d27, d13, d17);
		tessellator9.addVertexWithUV(d21, d5, d27, d13, d19);
		tessellator9.addVertexWithUV(d23, d5, d27, d15, d19);
		tessellator9.addVertexWithUV(d23, d5 + 1.0D, d27, d15, d17);
	}

	private float materialNotWater(int i1, int i2, int i3) {
		return this.blockAccess.getBlockMaterial(i1, i2, i3) != Material.water ? 1.0F : (float)this.blockAccess.getBlockMetadata(i1, i2, i3) / 9.0F;
	}

	private void renderBlockBottom(Block block1, double d2, double d4, double d6, int i8) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i8 = this.overrideBlockTexture;
		}

		int i10 = (i8 & 15) << 4;
		i8 &= 240;
		double d12 = (double)((float)i10 / 256.0F);
		double d14 = (double)(((float)i10 + 15.99F) / 256.0F);
		double d16 = (double)((float)i8 / 256.0F);
		double d18 = (double)(((float)i8 + 15.99F) / 256.0F);
		double d20 = d2 + block1.minX;
		double d22 = d2 + block1.maxX;
		double d24 = d4 + block1.minY;
		double d26 = d6 + block1.minZ;
		double d28 = d6 + block1.maxZ;
		tessellator9.addVertexWithUV(d20, d24, d28, d12, d18);
		tessellator9.addVertexWithUV(d20, d24, d26, d12, d16);
		tessellator9.addVertexWithUV(d22, d24, d26, d14, d16);
		tessellator9.addVertexWithUV(d22, d24, d28, d14, d18);
	}

	private void renderBlockTop(Block block1, double d2, double d4, double d6, int i8) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i8 = this.overrideBlockTexture;
		}

		int i10 = (i8 & 15) << 4;
		i8 &= 240;
		double d12 = (double)((float)i10 / 256.0F);
		double d14 = (double)(((float)i10 + 15.99F) / 256.0F);
		double d16 = (double)((float)i8 / 256.0F);
		double d18 = (double)(((float)i8 + 15.99F) / 256.0F);
		double d20 = d2 + block1.minX;
		double d22 = d2 + block1.maxX;
		double d24 = d4 + block1.maxY;
		double d26 = d6 + block1.minZ;
		double d28 = d6 + block1.maxZ;
		tessellator9.addVertexWithUV(d22, d24, d28, d14, d18);
		tessellator9.addVertexWithUV(d22, d24, d26, d14, d16);
		tessellator9.addVertexWithUV(d20, d24, d26, d12, d16);
		tessellator9.addVertexWithUV(d20, d24, d28, d12, d18);
	}

	private void renderBlockNorth(Block block1, double d2, double d4, double d6, int i8) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i8 = this.overrideBlockTexture;
		}

		int i10 = (i8 & 15) << 4;
		i8 &= 240;
		double d12 = (double)((float)i10 / 256.0F);
		double d14 = ((double)i10 + 15.99D) / 256.0D;
		double d16;
		double d18;
		if(block1.minY >= 0.0D && block1.maxY <= 1.0D) {
			d16 = ((double)i8 + block1.minY * 15.989999771118164D) / 256.0D;
			d18 = ((double)i8 + block1.maxY * 15.989999771118164D) / 256.0D;
		} else {
			d16 = (double)((float)i8 / 256.0F);
			d18 = (double)(((float)i8 + 15.99F) / 256.0F);
		}

		double d20 = d2 + block1.minX;
		double d22 = d2 + block1.maxX;
		double d24 = d4 + block1.minY;
		double d26 = d4 + block1.maxY;
		double d28 = d6 + block1.minZ;
		tessellator9.addVertexWithUV(d20, d26, d28, d14, d16);
		tessellator9.addVertexWithUV(d22, d26, d28, d12, d16);
		tessellator9.addVertexWithUV(d22, d24, d28, d12, d18);
		tessellator9.addVertexWithUV(d20, d24, d28, d14, d18);
	}

	private void renderBlockSouth(Block block1, double d2, double d4, double d6, int i8) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i8 = this.overrideBlockTexture;
		}

		int i10 = (i8 & 15) << 4;
		i8 &= 240;
		double d12 = (double)((float)i10 / 256.0F);
		double d14 = (double)(((float)i10 + 15.99F) / 256.0F);
		double d16;
		double d18;
		if(block1.minY >= 0.0D && block1.maxY <= 1.0D) {
			d16 = ((double)i8 + block1.minY * 15.989999771118164D) / 256.0D;
			d18 = ((double)i8 + block1.maxY * 15.989999771118164D) / 256.0D;
		} else {
			d16 = (double)((float)i8 / 256.0F);
			d18 = (double)(((float)i8 + 15.99F) / 256.0F);
		}

		double d20 = d2 + block1.minX;
		double d22 = d2 + block1.maxX;
		double d24 = d4 + block1.minY;
		double d26 = d4 + block1.maxY;
		double d28 = d6 + block1.maxZ;
		tessellator9.addVertexWithUV(d20, d26, d28, d12, d16);
		tessellator9.addVertexWithUV(d20, d24, d28, d12, d18);
		tessellator9.addVertexWithUV(d22, d24, d28, d14, d18);
		tessellator9.addVertexWithUV(d22, d26, d28, d14, d16);
	}

	private void renderBlockWest(Block block1, double d2, double d4, double d6, int i8) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i8 = this.overrideBlockTexture;
		}

		int i10 = (i8 & 15) << 4;
		i8 &= 240;
		double d12 = (double)((float)i10 / 256.0F);
		double d14 = (double)(((float)i10 + 15.99F) / 256.0F);
		double d16;
		double d18;
		if(block1.minY >= 0.0D && block1.maxY <= 1.0D) {
			d16 = ((double)i8 + block1.minY * 15.989999771118164D) / 256.0D;
			d18 = ((double)i8 + block1.maxY * 15.989999771118164D) / 256.0D;
		} else {
			d16 = (double)((float)i8 / 256.0F);
			d18 = (double)(((float)i8 + 15.99F) / 256.0F);
		}

		double d20 = d2 + block1.minX;
		double d22 = d4 + block1.minY;
		double d24 = d4 + block1.maxY;
		double d26 = d6 + block1.minZ;
		double d28 = d6 + block1.maxZ;
		tessellator9.addVertexWithUV(d20, d24, d28, d14, d16);
		tessellator9.addVertexWithUV(d20, d24, d26, d12, d16);
		tessellator9.addVertexWithUV(d20, d22, d26, d12, d18);
		tessellator9.addVertexWithUV(d20, d22, d28, d14, d18);
	}

	private void renderBlockEast(Block block1, double d2, double d4, double d6, int i8) {
		Tessellator tessellator9 = Tessellator.instance;
		if(this.overrideBlockTexture >= 0) {
			i8 = this.overrideBlockTexture;
		}

		int i10 = (i8 & 15) << 4;
		i8 &= 240;
		double d12 = (double)((float)i10 / 256.0F);
		double d14 = (double)(((float)i10 + 15.99F) / 256.0F);
		double d16;
		double d18;
		if(block1.minY >= 0.0D && block1.maxY <= 1.0D) {
			d16 = ((double)i8 + block1.minY * 15.989999771118164D) / 256.0D;
			d18 = ((double)i8 + block1.maxY * 15.989999771118164D) / 256.0D;
		} else {
			d16 = (double)((float)i8 / 256.0F);
			d18 = (double)(((float)i8 + 15.99F) / 256.0F);
		}

		double d20 = d2 + block1.maxX;
		double d22 = d4 + block1.minY;
		double d24 = d4 + block1.maxY;
		double d26 = d6 + block1.minZ;
		double d28 = d6 + block1.maxZ;
		tessellator9.addVertexWithUV(d20, d22, d28, d12, d18);
		tessellator9.addVertexWithUV(d20, d22, d26, d14, d18);
		tessellator9.addVertexWithUV(d20, d24, d26, d14, d16);
		tessellator9.addVertexWithUV(d20, d24, d28, d12, d16);
	}

	public final void renderBlockOnInventory(Block block1) {
		Tessellator tessellator2 = Tessellator.instance;
		int i3;
		if((i3 = block1.getRenderType()) == 0) {
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tessellator2.startDrawingQuads();
			Tessellator.setNormal(0.0F, -1.0F, 0.0F);
			this.renderBlockBottom(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(0));
			tessellator2.draw();
			tessellator2.startDrawingQuads();
			Tessellator.setNormal(0.0F, 1.0F, 0.0F);
			this.renderBlockTop(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(1));
			tessellator2.draw();
			tessellator2.startDrawingQuads();
			Tessellator.setNormal(0.0F, 0.0F, -1.0F);
			this.renderBlockNorth(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(2));
			tessellator2.draw();
			tessellator2.startDrawingQuads();
			Tessellator.setNormal(0.0F, 0.0F, 1.0F);
			this.renderBlockSouth(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(3));
			tessellator2.draw();
			tessellator2.startDrawingQuads();
			Tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			this.renderBlockWest(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(4));
			tessellator2.draw();
			tessellator2.startDrawingQuads();
			Tessellator.setNormal(1.0F, 0.0F, 0.0F);
			this.renderBlockEast(block1, 0.0D, 0.0D, 0.0D, block1.getBlockTextureFromSide(5));
			tessellator2.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		} else if(i3 == 1) {
			tessellator2.startDrawingQuads();
			Tessellator.setNormal(0.0F, -1.0F, 0.0F);
			this.renderBlockPlant(block1, -1, -0.5D, -0.5D, -0.5D);
			tessellator2.draw();
		} else if(i3 == 6) {
			tessellator2.startDrawingQuads();
			Tessellator.setNormal(0.0F, -1.0F, 0.0F);
			this.renderBlockCrops(block1, -1, -0.5D, -0.5D, -0.5D);
			tessellator2.draw();
		} else {
			if(i3 == 2) {
				tessellator2.startDrawingQuads();
				Tessellator.setNormal(0.0F, -1.0F, 0.0F);
				this.renderBlockTorch(block1, -0.5D, -0.5D, -0.5D, 0.0D, 0.0D);
				tessellator2.draw();
			}

		}
	}
}