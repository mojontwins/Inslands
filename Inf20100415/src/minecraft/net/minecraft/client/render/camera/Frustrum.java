package net.minecraft.client.render.camera;

import net.minecraft.game.physics.AxisAlignedBB;

public class Frustrum {
	private ClippingHelper clippingHelper = ClippingHelperImplementation.init();
	private double xPosition;
	private double yPosition;
	private double zPosition;

	public boolean isBoundingBoxInFrustrum(AxisAlignedBB axisAlignedBB1) {
		return this.isBoxInFrustum(axisAlignedBB1.minX, axisAlignedBB1.minY, axisAlignedBB1.minZ, axisAlignedBB1.maxX, axisAlignedBB1.maxY, axisAlignedBB1.maxZ);
	}

	public void setPosition(double d1, double d3, double d5) {
		this.xPosition = d1;
		this.yPosition = d3;
		this.zPosition = d5;
	}

	public boolean isBoxInFrustum(double d1, double d3, double d5, double d7, double d9, double d11) {
		double d10001 = d1 - this.xPosition;
		double d10002 = d3 - this.yPosition;
		double d10003 = d5 - this.zPosition;
		double d10004 = d7 - this.xPosition;
		double d10005 = d9 - this.yPosition;
		double d24 = d11 - this.zPosition;
		double d22 = d10005;
		double d20 = d10004;
		double d18 = d10003;
		double d16 = d10002;
		double d14 = d10001;
		ClippingHelper clippingHelper26 = this.clippingHelper;

		for(int i2 = 0; i2 < 6; ++i2) {
			if((double)clippingHelper26.frustrum[i2][0] * d14 + (double)clippingHelper26.frustrum[i2][1] * d16 + (double)clippingHelper26.frustrum[i2][2] * d18 + (double)clippingHelper26.frustrum[i2][3] <= 0.0D && (double)clippingHelper26.frustrum[i2][0] * d20 + (double)clippingHelper26.frustrum[i2][1] * d16 + (double)clippingHelper26.frustrum[i2][2] * d18 + (double)clippingHelper26.frustrum[i2][3] <= 0.0D && (double)clippingHelper26.frustrum[i2][0] * d14 + (double)clippingHelper26.frustrum[i2][1] * d22 + (double)clippingHelper26.frustrum[i2][2] * d18 + (double)clippingHelper26.frustrum[i2][3] <= 0.0D && (double)clippingHelper26.frustrum[i2][0] * d20 + (double)clippingHelper26.frustrum[i2][1] * d22 + (double)clippingHelper26.frustrum[i2][2] * d18 + (double)clippingHelper26.frustrum[i2][3] <= 0.0D && (double)clippingHelper26.frustrum[i2][0] * d14 + (double)clippingHelper26.frustrum[i2][1] * d16 + (double)clippingHelper26.frustrum[i2][2] * d24 + (double)clippingHelper26.frustrum[i2][3] <= 0.0D && (double)clippingHelper26.frustrum[i2][0] * d20 + (double)clippingHelper26.frustrum[i2][1] * d16 + (double)clippingHelper26.frustrum[i2][2] * d24 + (double)clippingHelper26.frustrum[i2][3] <= 0.0D && (double)clippingHelper26.frustrum[i2][0] * d14 + (double)clippingHelper26.frustrum[i2][1] * d22 + (double)clippingHelper26.frustrum[i2][2] * d24 + (double)clippingHelper26.frustrum[i2][3] <= 0.0D && (double)clippingHelper26.frustrum[i2][0] * d20 + (double)clippingHelper26.frustrum[i2][1] * d22 + (double)clippingHelper26.frustrum[i2][2] * d24 + (double)clippingHelper26.frustrum[i2][3] <= 0.0D) {
				return false;
			}
		}

		return true;
	}
}