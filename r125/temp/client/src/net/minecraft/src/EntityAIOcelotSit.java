package net.minecraft.src;

public class EntityAIOcelotSit extends EntityAIBase {
	private final EntityOcelot field_50085_a;
	private final float field_50083_b;
	private int field_50084_c = 0;
	private int field_52011_h = 0;
	private int field_50081_d = 0;
	private int field_50082_e = 0;
	private int field_50079_f = 0;
	private int field_50080_g = 0;

	public EntityAIOcelotSit(EntityOcelot entityOcelot1, float f2) {
		this.field_50085_a = entityOcelot1;
		this.field_50083_b = f2;
		this.setMutexBits(5);
	}

	public boolean shouldExecute() {
		return this.field_50085_a.isTamed() && !this.field_50085_a.isSitting() && this.field_50085_a.getRNG().nextDouble() <= 0.006500000134110451D && this.func_50077_h();
	}

	public boolean continueExecuting() {
		return this.field_50084_c <= this.field_50081_d && this.field_52011_h <= 60 && this.func_50078_a(this.field_50085_a.worldObj, this.field_50082_e, this.field_50079_f, this.field_50080_g);
	}

	public void startExecuting() {
		this.field_50085_a.getNavigator().func_48666_a((double)((float)this.field_50082_e) + 0.5D, (double)(this.field_50079_f + 1), (double)((float)this.field_50080_g) + 0.5D, this.field_50083_b);
		this.field_50084_c = 0;
		this.field_52011_h = 0;
		this.field_50081_d = this.field_50085_a.getRNG().nextInt(this.field_50085_a.getRNG().nextInt(1200) + 1200) + 1200;
		this.field_50085_a.func_50008_ai().func_48407_a(false);
	}

	public void resetTask() {
		this.field_50085_a.func_48140_f(false);
	}

	public void updateTask() {
		++this.field_50084_c;
		this.field_50085_a.func_50008_ai().func_48407_a(false);
		if(this.field_50085_a.getDistanceSq((double)this.field_50082_e, (double)(this.field_50079_f + 1), (double)this.field_50080_g) > 1.0D) {
			this.field_50085_a.func_48140_f(false);
			this.field_50085_a.getNavigator().func_48666_a((double)((float)this.field_50082_e) + 0.5D, (double)(this.field_50079_f + 1), (double)((float)this.field_50080_g) + 0.5D, this.field_50083_b);
			++this.field_52011_h;
		} else if(!this.field_50085_a.isSitting()) {
			this.field_50085_a.func_48140_f(true);
		} else {
			--this.field_52011_h;
		}

	}

	private boolean func_50077_h() {
		int i1 = (int)this.field_50085_a.posY;
		double d2 = 2.147483647E9D;

		for(int i4 = (int)this.field_50085_a.posX - 8; (double)i4 < this.field_50085_a.posX + 8.0D; ++i4) {
			for(int i5 = (int)this.field_50085_a.posZ - 8; (double)i5 < this.field_50085_a.posZ + 8.0D; ++i5) {
				if(this.func_50078_a(this.field_50085_a.worldObj, i4, i1, i5) && this.field_50085_a.worldObj.isAirBlock(i4, i1 + 1, i5)) {
					double d6 = this.field_50085_a.getDistanceSq((double)i4, (double)i1, (double)i5);
					if(d6 < d2) {
						this.field_50082_e = i4;
						this.field_50079_f = i1;
						this.field_50080_g = i5;
						d2 = d6;
					}
				}
			}
		}

		return d2 < 2.147483647E9D;
	}

	private boolean func_50078_a(World world1, int i2, int i3, int i4) {
		int i5 = world1.getBlockId(i2, i3, i4);
		int i6 = world1.getBlockMetadata(i2, i3, i4);
		if(i5 == Block.chest.blockID) {
			TileEntityChest tileEntityChest7 = (TileEntityChest)world1.getBlockTileEntity(i2, i3, i4);
			if(tileEntityChest7.numUsingPlayers < 1) {
				return true;
			}
		} else {
			if(i5 == Block.stoneOvenActive.blockID) {
				return true;
			}

			if(i5 == Block.bed.blockID && !BlockBed.isBlockFootOfBed(i6)) {
				return true;
			}
		}

		return false;
	}
}
