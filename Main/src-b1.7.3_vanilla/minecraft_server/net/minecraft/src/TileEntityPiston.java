package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TileEntityPiston extends TileEntity {
	private int storedBlockID;
	private int storedMetadata;
	private int storedOrientation;
	private boolean isExtending;
	private boolean field_31018_j;
	private float progress;
	private float lastProgress;
	private static List field_31013_m = new ArrayList();

	public TileEntityPiston() {
	}

	public TileEntityPiston(int i1, int i2, int i3, boolean z4, boolean z5) {
		this.storedBlockID = i1;
		this.storedMetadata = i2;
		this.storedOrientation = i3;
		this.isExtending = z4;
		this.field_31018_j = z5;
	}

	public int getStoredBlockID() {
		return this.storedBlockID;
	}

	public int func_31005_e() {
		return this.storedMetadata;
	}

	public boolean func_31010_c() {
		return this.isExtending;
	}

	public int func_31008_d() {
		return this.storedOrientation;
	}

	public float func_31007_a(float f1) {
		if(f1 > 1.0F) {
			f1 = 1.0F;
		}

		return this.lastProgress + (this.progress - this.lastProgress) * f1;
	}

	private void func_31009_a(float f1, float f2) {
		if(!this.isExtending) {
			--f1;
		} else {
			f1 = 1.0F - f1;
		}

		AxisAlignedBB axisAlignedBB3 = Block.pistonMoving.func_31032_a(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.storedBlockID, f1, this.storedOrientation);
		if(axisAlignedBB3 != null) {
			List list4 = this.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)null, axisAlignedBB3);
			if(!list4.isEmpty()) {
				field_31013_m.addAll(list4);
				Iterator iterator5 = field_31013_m.iterator();

				while(iterator5.hasNext()) {
					Entity entity6 = (Entity)iterator5.next();
					entity6.moveEntity((double)(f2 * (float)PistonBlockTextures.field_31051_b[this.storedOrientation]), (double)(f2 * (float)PistonBlockTextures.field_31054_c[this.storedOrientation]), (double)(f2 * (float)PistonBlockTextures.field_31053_d[this.storedOrientation]));
				}

				field_31013_m.clear();
			}
		}

	}

	public void clearPistonTileEntity() {
		if(this.lastProgress < 1.0F) {
			this.lastProgress = this.progress = 1.0F;
			this.worldObj.removeBlockTileEntity(this.xCoord, this.yCoord, this.zCoord);
			this.invalidate();
			if(this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord) == Block.pistonMoving.blockID) {
				this.worldObj.setBlockAndMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.storedBlockID, this.storedMetadata);
			}
		}

	}

	public void updateEntity() {
		this.lastProgress = this.progress;
		if(this.lastProgress >= 1.0F) {
			this.func_31009_a(1.0F, 0.25F);
			this.worldObj.removeBlockTileEntity(this.xCoord, this.yCoord, this.zCoord);
			this.invalidate();
			if(this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord) == Block.pistonMoving.blockID) {
				this.worldObj.setBlockAndMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.storedBlockID, this.storedMetadata);
			}

		} else {
			this.progress += 0.5F;
			if(this.progress >= 1.0F) {
				this.progress = 1.0F;
			}

			if(this.isExtending) {
				this.func_31009_a(this.progress, this.progress - this.lastProgress + 0.0625F);
			}

		}
	}

	public void readFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readFromNBT(nBTTagCompound1);
		this.storedBlockID = nBTTagCompound1.getInteger("blockId");
		this.storedMetadata = nBTTagCompound1.getInteger("blockData");
		this.storedOrientation = nBTTagCompound1.getInteger("facing");
		this.lastProgress = this.progress = nBTTagCompound1.getFloat("progress");
		this.isExtending = nBTTagCompound1.getBoolean("extending");
	}

	public void writeToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeToNBT(nBTTagCompound1);
		nBTTagCompound1.setInteger("blockId", this.storedBlockID);
		nBTTagCompound1.setInteger("blockData", this.storedMetadata);
		nBTTagCompound1.setInteger("facing", this.storedOrientation);
		nBTTagCompound1.setFloat("progress", this.lastProgress);
		nBTTagCompound1.setBoolean("extending", this.isExtending);
	}
}
