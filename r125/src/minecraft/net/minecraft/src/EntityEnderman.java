package net.minecraft.src;

public class EntityEnderman extends EntityMob {
	private static boolean[] canCarryBlocks = new boolean[256];
	public boolean isAttacking = false;
	private int teleportDelay = 0;
	private int field_35185_e = 0;

	public EntityEnderman(World world1) {
		super(world1);
		this.texture = "/mob/enderman.png";
		this.moveSpeed = 0.2F;
		this.attackStrength = 7;
		this.setSize(0.6F, 2.9F);
		this.stepHeight = 1.0F;
	}

	public int getMaxHealth() {
		return 40;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, new Byte((byte)0));
		this.dataWatcher.addObject(17, new Byte((byte)0));
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
		nBTTagCompound1.setShort("carried", (short)this.getCarried());
		nBTTagCompound1.setShort("carriedData", (short)this.getCarryingData());
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
		this.setCarried(nBTTagCompound1.getShort("carried"));
		this.setCarryingData(nBTTagCompound1.getShort("carriedData"));
	}

	protected Entity findPlayerToAttack() {
		EntityPlayer entityPlayer1 = this.worldObj.getClosestVulnerablePlayerToEntity(this, 64.0D);
		if(entityPlayer1 != null) {
			if(this.shouldAttackPlayer(entityPlayer1)) {
				if(this.field_35185_e++ == 5) {
					this.field_35185_e = 0;
					return entityPlayer1;
				}
			} else {
				this.field_35185_e = 0;
			}
		}

		return null;
	}

	public int getBrightnessForRender(float f1) {
		return super.getBrightnessForRender(f1);
	}

	public float getBrightness(float f1) {
		return super.getBrightness(f1);
	}

	private boolean shouldAttackPlayer(EntityPlayer entityPlayer1) {
		ItemStack itemStack2 = entityPlayer1.inventory.armorInventory[3];
		if(itemStack2 != null && itemStack2.itemID == Block.pumpkin.blockID) {
			return false;
		} else {
			Vec3D vec3D3 = entityPlayer1.getLook(1.0F).normalize();
			Vec3D vec3D4 = Vec3D.createVector(this.posX - entityPlayer1.posX, this.boundingBox.minY + (double)(this.height / 2.0F) - (entityPlayer1.posY + (double)entityPlayer1.getEyeHeight()), this.posZ - entityPlayer1.posZ);
			double d5 = vec3D4.lengthVector();
			vec3D4 = vec3D4.normalize();
			double d7 = vec3D3.dotProduct(vec3D4);
			return d7 > 1.0D - 0.025D / d5 ? entityPlayer1.canEntityBeSeen(this) : false;
		}
	}

	public void onLivingUpdate() {
		if(this.isWet()) {
			this.attackEntityFrom(DamageSource.drown, 1);
		}

		this.isAttacking = this.entityToAttack != null;
		this.moveSpeed = this.entityToAttack != null ? 6.5F : 0.3F;
		int i1;
		if(!this.worldObj.isRemote) {
			int i2;
			int i3;
			int i4;
			if(this.getCarried() == 0) {
				if(this.rand.nextInt(20) == 0) {
					i1 = MathHelper.floor_double(this.posX - 2.0D + this.rand.nextDouble() * 4.0D);
					i2 = MathHelper.floor_double(this.posY + this.rand.nextDouble() * 3.0D);
					i3 = MathHelper.floor_double(this.posZ - 2.0D + this.rand.nextDouble() * 4.0D);
					i4 = this.worldObj.getBlockId(i1, i2, i3);
					if(canCarryBlocks[i4]) {
						this.setCarried(this.worldObj.getBlockId(i1, i2, i3));
						this.setCarryingData(this.worldObj.getBlockMetadata(i1, i2, i3));
						this.worldObj.setBlockWithNotify(i1, i2, i3, 0);
					}
				}
			} else if(this.rand.nextInt(2000) == 0) {
				i1 = MathHelper.floor_double(this.posX - 1.0D + this.rand.nextDouble() * 2.0D);
				i2 = MathHelper.floor_double(this.posY + this.rand.nextDouble() * 2.0D);
				i3 = MathHelper.floor_double(this.posZ - 1.0D + this.rand.nextDouble() * 2.0D);
				i4 = this.worldObj.getBlockId(i1, i2, i3);
				int i5 = this.worldObj.getBlockId(i1, i2 - 1, i3);
				if(i4 == 0 && i5 > 0 && Block.blocksList[i5].renderAsNormalBlock()) {
					this.worldObj.setBlockAndMetadataWithNotify(i1, i2, i3, this.getCarried(), this.getCarryingData());
					this.setCarried(0);
				}
			}
		}

		for(i1 = 0; i1 < 2; ++i1) {
			this.worldObj.spawnParticle("portal", this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
		}

		if(this.worldObj.isDaytime() && !this.worldObj.isRemote) {
			float f6 = this.getBrightness(1.0F);
			if(f6 > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) && this.rand.nextFloat() * 30.0F < (f6 - 0.4F) * 2.0F) {
				this.entityToAttack = null;
				this.teleportRandomly();
			}
		}

		if(this.isWet()) {
			this.entityToAttack = null;
			this.teleportRandomly();
		}

		this.isJumping = false;
		if(this.entityToAttack != null) {
			this.faceEntity(this.entityToAttack, 100.0F, 100.0F);
		}

		if(!this.worldObj.isRemote && this.isEntityAlive()) {
			if(this.entityToAttack != null) {
				if(this.entityToAttack instanceof EntityPlayer && this.shouldAttackPlayer((EntityPlayer)this.entityToAttack)) {
					this.moveStrafing = this.moveForward = 0.0F;
					this.moveSpeed = 0.0F;
					if(this.entityToAttack.getDistanceSqToEntity(this) < 16.0D) {
						this.teleportRandomly();
					}

					this.teleportDelay = 0;
				} else if(this.entityToAttack.getDistanceSqToEntity(this) > 256.0D && this.teleportDelay++ >= 30 && this.teleportToEntity(this.entityToAttack)) {
					this.teleportDelay = 0;
				}
			} else {
				this.teleportDelay = 0;
			}
		}

		super.onLivingUpdate();
	}

	protected boolean teleportRandomly() {
		double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
		double d3 = this.posY + (double)(this.rand.nextInt(64) - 32);
		double d5 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
		return this.teleportTo(d1, d3, d5);
	}

	protected boolean teleportToEntity(Entity entity1) {
		Vec3D vec3D2 = Vec3D.createVector(this.posX - entity1.posX, this.boundingBox.minY + (double)(this.height / 2.0F) - entity1.posY + (double)entity1.getEyeHeight(), this.posZ - entity1.posZ);
		vec3D2 = vec3D2.normalize();
		double d3 = 16.0D;
		double d5 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3D2.xCoord * d3;
		double d7 = this.posY + (double)(this.rand.nextInt(16) - 8) - vec3D2.yCoord * d3;
		double d9 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3D2.zCoord * d3;
		return this.teleportTo(d5, d7, d9);
	}

	protected boolean teleportTo(double d1, double d3, double d5) {
		double d7 = this.posX;
		double d9 = this.posY;
		double d11 = this.posZ;
		this.posX = d1;
		this.posY = d3;
		this.posZ = d5;
		boolean z13 = false;
		int i14 = MathHelper.floor_double(this.posX);
		int i15 = MathHelper.floor_double(this.posY);
		int i16 = MathHelper.floor_double(this.posZ);
		int i18;
		if(this.worldObj.blockExists(i14, i15, i16)) {
			boolean z17 = false;

			while(true) {
				while(!z17 && i15 > 0) {
					i18 = this.worldObj.getBlockId(i14, i15 - 1, i16);
					if(i18 != 0 && Block.blocksList[i18].blockMaterial.blocksMovement()) {
						z17 = true;
					} else {
						--this.posY;
						--i15;
					}
				}

				if(z17) {
					this.setPosition(this.posX, this.posY, this.posZ);
					if(this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.isAnyLiquid(this.boundingBox)) {
						z13 = true;
					}
				}
				break;
			}
		}

		if(!z13) {
			this.setPosition(d7, d9, d11);
			return false;
		} else {
			short s30 = 128;

			for(i18 = 0; i18 < s30; ++i18) {
				double d19 = (double)i18 / ((double)s30 - 1.0D);
				float f21 = (this.rand.nextFloat() - 0.5F) * 0.2F;
				float f22 = (this.rand.nextFloat() - 0.5F) * 0.2F;
				float f23 = (this.rand.nextFloat() - 0.5F) * 0.2F;
				double d24 = d7 + (this.posX - d7) * d19 + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.0D;
				double d26 = d9 + (this.posY - d9) * d19 + this.rand.nextDouble() * (double)this.height;
				double d28 = d11 + (this.posZ - d11) * d19 + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.0D;
				this.worldObj.spawnParticle("portal", d24, d26, d28, (double)f21, (double)f22, (double)f23);
			}

			this.worldObj.playSoundEffect(d7, d9, d11, "mob.endermen.portal", 1.0F, 1.0F);
			this.worldObj.playSoundAtEntity(this, "mob.endermen.portal", 1.0F, 1.0F);
			return true;
		}
	}

	protected String getLivingSound() {
		return "mob.endermen.idle";
	}

	protected String getHurtSound() {
		return "mob.endermen.hit";
	}

	protected String getDeathSound() {
		return "mob.endermen.death";
	}

	protected int getDropItemId() {
		return Item.enderPearl.shiftedIndex;
	}

	protected void dropFewItems(boolean z1, int i2) {
		int i3 = this.getDropItemId();
		if(i3 > 0) {
			int i4 = this.rand.nextInt(2 + i2);

			for(int i5 = 0; i5 < i4; ++i5) {
				this.dropItem(i3, 1);
			}
		}

	}

	public void setCarried(int i1) {
		this.dataWatcher.updateObject(16, (byte)(i1 & 255));
	}

	public int getCarried() {
		return this.dataWatcher.getWatchableObjectByte(16);
	}

	public void setCarryingData(int i1) {
		this.dataWatcher.updateObject(17, (byte)(i1 & 255));
	}

	public int getCarryingData() {
		return this.dataWatcher.getWatchableObjectByte(17);
	}

	public boolean attackEntityFrom(DamageSource damageSource1, int i2) {
		if(damageSource1 instanceof EntityDamageSourceIndirect) {
			for(int i3 = 0; i3 < 64; ++i3) {
				if(this.teleportRandomly()) {
					return true;
				}
			}

			return false;
		} else {
			return super.attackEntityFrom(damageSource1, i2);
		}
	}

	static {
		canCarryBlocks[Block.grass.blockID] = true;
		canCarryBlocks[Block.dirt.blockID] = true;
		canCarryBlocks[Block.sand.blockID] = true;
		canCarryBlocks[Block.gravel.blockID] = true;
		canCarryBlocks[Block.plantYellow.blockID] = true;
		canCarryBlocks[Block.plantRed.blockID] = true;
		canCarryBlocks[Block.mushroomBrown.blockID] = true;
		canCarryBlocks[Block.mushroomRed.blockID] = true;
		canCarryBlocks[Block.tnt.blockID] = true;
		canCarryBlocks[Block.cactus.blockID] = true;
		canCarryBlocks[Block.blockClay.blockID] = true;
		canCarryBlocks[Block.pumpkin.blockID] = true;
		canCarryBlocks[Block.melon.blockID] = true;
		canCarryBlocks[Block.mycelium.blockID] = true;
	}
}
