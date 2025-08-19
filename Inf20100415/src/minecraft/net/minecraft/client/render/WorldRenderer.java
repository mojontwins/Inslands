package net.minecraft.client.render;

import net.minecraft.client.render.camera.Frustrum;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.chunk.Chunk;

import org.lwjgl.opengl.GL11;

import util.MathHelper;

public final class WorldRenderer {
	private World worldObj;
	private int glRenderList = -1;
	private static Tessellator tessellator = Tessellator.instance;
	public static int chunksUpdated = 0;
	private int posX;
	private int posY;
	private int posZ;
	private int sizeWidth;
	private int sizeHeight;
	private int sizeDepth;
	public int posXMinus;
	public int posYMinus;
	public int posZMinus;
	private int posXClip;
	private int posYClip;
	private int posZClip;
	public boolean isInFrustum = false;
	private boolean[] skipRenderPass = new boolean[2];
	private int posXPlus;
	private int posYPlus;
	private int posZPlus;
	public boolean needsUpdate;
	private AxisAlignedBB rendererBoundingBox;
	private RenderBlocks renderBlocks;
	public boolean isVisible = true;
	public boolean isWaitingOnOcclusionQuery;
	public int glOcclusionQuery;
	public boolean isChunkLit;

	public WorldRenderer(World world1, int i2, int i3, int i4, int i5, int i6) {
		this.renderBlocks = new RenderBlocks(world1);
		this.worldObj = world1;
		this.sizeWidth = this.sizeHeight = this.sizeDepth = 16;
		MathHelper.sqrt_float((float)(this.sizeWidth * this.sizeWidth + this.sizeHeight * this.sizeHeight + this.sizeDepth * this.sizeDepth));
		this.glRenderList = i6;
		this.posX = -999;
		this.setPosition(i2, i3, i4);
		this.needsUpdate = false;
	}

	public final void setPosition(int i1, int i2, int i3) {
		if(i1 != this.posX || i2 != this.posY || i3 != this.posZ) {
			this.setDontDraw();
			this.posX = i1;
			this.posY = i2;
			this.posZ = i3;
			this.posXPlus = i1 + this.sizeWidth / 2;
			this.posYPlus = i2 + this.sizeHeight / 2;
			this.posZPlus = i3 + this.sizeDepth / 2;
			this.posXClip = i1 & 511;
			this.posYClip = i2 & 511;
			this.posZClip = i3 & 511;
			this.posXMinus = i1 - this.posXClip;
			this.posYMinus = i2 - this.posYClip;
			this.posZMinus = i3 - this.posZClip;
			this.rendererBoundingBox = (new AxisAlignedBB((double)i1, (double)i2, (double)i3, (double)(i1 + this.sizeWidth), (double)(i2 + this.sizeHeight), (double)(i3 + this.sizeDepth))).expand(2.0D, 2.0D, 2.0D);
			GL11.glNewList(this.glRenderList + 2, GL11.GL_COMPILE);
			AxisAlignedBB axisAlignedBB4 = new AxisAlignedBB((double)((float)this.posXClip - 2.0F), (double)((float)this.posYClip - 2.0F), (double)((float)this.posZClip - 2.0F), (double)((float)(this.posXClip + this.sizeWidth) + 2.0F), (double)((float)(this.posYClip + this.sizeHeight) + 2.0F), (double)((float)(this.posZClip + this.sizeDepth) + 2.0F));
			Tessellator tessellator5 = Tessellator.instance;
			Tessellator.instance.startDrawingQuads();
			tessellator5.addVertex(axisAlignedBB4.minX, axisAlignedBB4.maxY, axisAlignedBB4.minZ);
			tessellator5.addVertex(axisAlignedBB4.maxX, axisAlignedBB4.maxY, axisAlignedBB4.minZ);
			tessellator5.addVertex(axisAlignedBB4.maxX, axisAlignedBB4.minY, axisAlignedBB4.minZ);
			tessellator5.addVertex(axisAlignedBB4.minX, axisAlignedBB4.minY, axisAlignedBB4.minZ);
			tessellator5.addVertex(axisAlignedBB4.minX, axisAlignedBB4.minY, axisAlignedBB4.maxZ);
			tessellator5.addVertex(axisAlignedBB4.maxX, axisAlignedBB4.minY, axisAlignedBB4.maxZ);
			tessellator5.addVertex(axisAlignedBB4.maxX, axisAlignedBB4.maxY, axisAlignedBB4.maxZ);
			tessellator5.addVertex(axisAlignedBB4.minX, axisAlignedBB4.maxY, axisAlignedBB4.maxZ);
			tessellator5.addVertex(axisAlignedBB4.minX, axisAlignedBB4.minY, axisAlignedBB4.minZ);
			tessellator5.addVertex(axisAlignedBB4.maxX, axisAlignedBB4.minY, axisAlignedBB4.minZ);
			tessellator5.addVertex(axisAlignedBB4.maxX, axisAlignedBB4.minY, axisAlignedBB4.maxZ);
			tessellator5.addVertex(axisAlignedBB4.minX, axisAlignedBB4.minY, axisAlignedBB4.maxZ);
			tessellator5.addVertex(axisAlignedBB4.minX, axisAlignedBB4.maxY, axisAlignedBB4.maxZ);
			tessellator5.addVertex(axisAlignedBB4.maxX, axisAlignedBB4.maxY, axisAlignedBB4.maxZ);
			tessellator5.addVertex(axisAlignedBB4.maxX, axisAlignedBB4.maxY, axisAlignedBB4.minZ);
			tessellator5.addVertex(axisAlignedBB4.minX, axisAlignedBB4.maxY, axisAlignedBB4.minZ);
			tessellator5.addVertex(axisAlignedBB4.minX, axisAlignedBB4.minY, axisAlignedBB4.maxZ);
			tessellator5.addVertex(axisAlignedBB4.minX, axisAlignedBB4.maxY, axisAlignedBB4.maxZ);
			tessellator5.addVertex(axisAlignedBB4.minX, axisAlignedBB4.maxY, axisAlignedBB4.minZ);
			tessellator5.addVertex(axisAlignedBB4.minX, axisAlignedBB4.minY, axisAlignedBB4.minZ);
			tessellator5.addVertex(axisAlignedBB4.maxX, axisAlignedBB4.minY, axisAlignedBB4.minZ);
			tessellator5.addVertex(axisAlignedBB4.maxX, axisAlignedBB4.maxY, axisAlignedBB4.minZ);
			tessellator5.addVertex(axisAlignedBB4.maxX, axisAlignedBB4.maxY, axisAlignedBB4.maxZ);
			tessellator5.addVertex(axisAlignedBB4.maxX, axisAlignedBB4.minY, axisAlignedBB4.maxZ);
			tessellator5.draw();
			GL11.glEndList();
			this.needsUpdate = true;
		}
	}

	public final void updateRenderer() {
		if(this.needsUpdate) {
			++chunksUpdated;
			int i1 = this.posX;
			int i2 = this.posY;
			int i3 = this.posZ;
			int i4 = this.posX + this.sizeWidth;
			int i5 = this.posY + this.sizeHeight;
			int i6 = this.posZ + this.sizeDepth;

			int i7;
			for(i7 = 0; i7 < 2; ++i7) {
				this.skipRenderPass[i7] = true;
			}

			Chunk.isLit = false;

			for(i7 = 0; i7 < 2; ++i7) {
				boolean z8 = false;
				boolean z9 = false;
				GL11.glNewList(this.glRenderList + i7, GL11.GL_COMPILE);
				GL11.glPushMatrix();
				GL11.glTranslatef((float)this.posXClip, (float)this.posYClip, (float)this.posZClip);
				tessellator.startDrawingQuads();
				tessellator.setTranslationD((double)(-this.posX), (double)(-this.posY), (double)(-this.posZ));

				for(int i10 = i2; i10 < i5; ++i10) {
					for(int i11 = i3; i11 < i6; ++i11) {
						for(int i12 = i1; i12 < i4; ++i12) {
							int i13;
							if((i13 = this.worldObj.getBlockId(i12, i10, i11)) > 0) {
								Block block14;
								if((block14 = Block.blocksList[i13]).getRenderBlockPass() != i7) {
									z8 = true;
								} else {
									z9 |= this.renderBlocks.renderBlockByRenderType(block14, i12, i10, i11);
								}
							}
						}
					}
				}

				tessellator.draw();
				GL11.glPopMatrix();
				GL11.glEndList();
				tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
				if(z9) {
					this.skipRenderPass[i7] = false;
				}

				if(!z8) {
					break;
				}
			}

			this.isChunkLit = Chunk.isLit;
		}
	}

	public final float distanceToEntitySquared(Entity entity1) {
		float f2 = (float)(entity1.posX - (double)this.posXPlus);
		float f3 = (float)(entity1.posY - (double)this.posYPlus);
		float f4 = (float)(entity1.posZ - (double)this.posZPlus);
		return f2 * f2 + f3 * f3 + f4 * f4;
	}

	private void setDontDraw() {
		for(int i1 = 0; i1 < 2; ++i1) {
			this.skipRenderPass[i1] = true;
		}

	}

	public final void stopRendering() {
		this.setDontDraw();
		this.worldObj = null;
	}

	public final int getGLCallListForPass(int i1) {
		return !this.isInFrustum ? -1 : (!this.skipRenderPass[i1] ? this.glRenderList + i1 : -1);
	}

	public final void updateInFrustrum(Frustrum frustrum1) {
		this.isInFrustum = frustrum1.isBoundingBoxInFrustrum(this.rendererBoundingBox);
	}

	public final void callOcclusionQueryList() {
		GL11.glCallList(this.glRenderList + 2);
	}

	public final boolean skipAllRenderPasses() {
		return this.skipRenderPass[0] && this.skipRenderPass[1];
	}
}