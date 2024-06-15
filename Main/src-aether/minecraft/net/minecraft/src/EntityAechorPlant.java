package net.minecraft.src;

import java.util.List;

public class EntityAechorPlant extends EntityAetherAnimal {
	public EntityLiving target;
	public int size;
	public int attTime;
	public int smokeTime;
	public boolean seeprey;
	public boolean grounded;
	public boolean noDespawn;
	public float sinage;
	private int poisonLeft;

	public EntityAechorPlant(World world1) {
		super(world1);
		this.texture = "/aether/mobs/aechorplant.png";
		this.size = this.rand.nextInt(4) + 1;
		this.health = 10 + this.size * 2;
		this.sinage = this.rand.nextFloat() * 6.0F;
		this.smokeTime = this.attTime = 0;
		this.seeprey = false;
		this.setSize(0.75F + (float)this.size * 0.125F, 0.5F + (float)this.size * 0.075F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.poisonLeft = 2;
	}

	public int getMaxSpawnedInChunk() {
		return 3;
	}

	public boolean getCanSpawnHere() {
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.boundingBox.minY);
		int k = MathHelper.floor_double(this.posZ);
		return this.worldObj.getBlockId(i, j - 1, k) == AetherBlocks.Grass.blockID && this.worldObj.getBlockLightValue(i, j, k) > 8 && super.getCanSpawnHere();
	}

	public void onLivingUpdate() {
		if(this.health > 0 && this.grounded) {
			++this.entityAge;
			this.func_27021_X();
		} else {
			super.onLivingUpdate();
			if(this.health <= 0) {
				return;
			}
		}

		if(this.onGround) {
			this.grounded = true;
		}

		if(this.hurtTime > 0) {
			this.sinage += 0.9F;
		} else if(this.seeprey) {
			this.sinage += 0.3F;
		} else {
			this.sinage += 0.1F;
		}

		if(this.sinage > 6.283186F) {
			this.sinage -= 6.283186F;
		}

		int j;
		if(this.target == null) {
			label107: {
				List i = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(10.0D, 10.0D, 10.0D));
				j = 0;

				Entity k;
				while(true) {
					if(j >= i.size()) {
						break label107;
					}

					k = (Entity)i.get(j);
					if(k instanceof EntityLiving && !(k instanceof EntityAechorPlant) && !(k instanceof EntityCreeper)) {
						if(!(k instanceof EntityPlayer)) {
							break;
						}

						EntityPlayer player1 = (EntityPlayer)k;
						boolean flag = false;
						if(!flag) {
							break;
						}
					}

					++j;
				}

				this.target = (EntityLiving)k;
			}
		}

		if(this.target != null) {
			if(!this.target.isDead && (double)this.target.getDistanceToEntity(this) <= 12.0D) {
				if(this.target instanceof EntityPlayer) {
					EntityPlayer entityPlayer6 = (EntityPlayer)this.target;
					boolean z8 = false;
					if(z8) {
						this.target = null;
						this.attTime = 0;
					}
				}
			} else {
				this.target = null;
				this.attTime = 0;
			}

			if(this.target != null && this.attTime >= 20 && this.canEntityBeSeen(this.target) && (double)this.target.getDistanceToEntity(this) < 5.5D + (double)this.size / 2.0D) {
				this.shootTarget();
				this.attTime = -10;
			}

			if(this.attTime < 20) {
				++this.attTime;
			}
		}

		++this.smokeTime;
		if(this.smokeTime >= (this.seeprey ? 3 : 8)) {
			this.smokeTime = 0;
			int i7 = MathHelper.floor_double(this.posX);
			j = MathHelper.floor_double(this.boundingBox.minY);
			int i9 = MathHelper.floor_double(this.posZ);
			if(this.worldObj.getBlockId(i7, j - 1, i9) != AetherBlocks.Grass.blockID && this.grounded) {
				this.isDead = true;
			}
		}

		this.seeprey = this.target != null;
	}

	public void setEntityDead() {
		if(!this.noDespawn || this.health <= 0) {
			super.setEntityDead();
		}

	}

	public void shootTarget() {
		if(this.worldObj.difficultySetting != 0) {
			double d1 = this.target.posX - this.posX;
			double d2 = this.target.posZ - this.posZ;
			double d3 = 1.5D / Math.sqrt(d1 * d1 + d2 * d2 + 0.1D);
			double d4 = 0.1D + Math.sqrt(d1 * d1 + d2 * d2 + 0.1D) * 0.5D + (this.posY - this.target.posY) * 0.25D;
			d1 *= d3;
			d2 *= d3;
			EntityPoisonNeedle entityarrow = new EntityPoisonNeedle(this.worldObj, this);
			entityarrow.posY = this.posY + 0.5D;
			this.worldObj.playSoundAtEntity(this, "aether.sound.other.dartshooter.shootDart", 2.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));
			this.worldObj.entityJoinedWorld(entityarrow);
			entityarrow.setArrowHeading(d1, d4, d2, 0.285F + (float)d4 * 0.05F, 1.0F);
		}
	}

	protected String getHurtSound() {
		return "damage.hurtflesh";
	}

	protected String getDeathSound() {
		return "damage.fallbig";
	}

	public void knockBack(Entity entity, int ii, double dd, double dd1) {
		for(int i = 0; i < 8; ++i) {
			double d1 = this.posX + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.5D;
			double d2 = this.posY + 0.25D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.5D;
			double d3 = this.posZ + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.5D;
			double d4 = (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.5D;
			double d5 = (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.5D;
			this.worldObj.spawnParticle("portal", d1, d2, d3, d4, 0.25D, d5);
		}

		if(this.health <= 0) {
			super.knockBack(entity, ii, dd, dd1);
		}
	}

	public boolean interact(EntityPlayer entityplayer) {
		boolean flag = false;
		ItemStack stack = entityplayer.inventory.getCurrentItem();
		if(stack != null && stack.itemID == AetherItems.Bucket.shiftedIndex && this.poisonLeft > 0) {
			--this.poisonLeft;
			entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, (ItemStack)null);
			entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(AetherItems.Bucket, 1, 2));
			return true;
		} else {
			if(flag) {
				this.noDespawn = true;
				String s = "heart";

				for(int i = 0; i < 7; ++i) {
					double d = this.rand.nextGaussian() * 0.02D;
					double d1 = this.rand.nextGaussian() * 0.02D;
					double d2 = this.rand.nextGaussian() * 0.02D;
					this.worldObj.spawnParticle(s, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d, d1, d2);
				}
			}

			return false;
		}
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("Grounded", this.grounded);
		nbttagcompound.setBoolean("NoDespawn", this.noDespawn);
		nbttagcompound.setShort("AttTime", (short)this.attTime);
		nbttagcompound.setShort("Size", (short)this.size);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.grounded = nbttagcompound.getBoolean("Grounded");
		this.noDespawn = nbttagcompound.getBoolean("NoDespawn");
		this.attTime = nbttagcompound.getShort("AttTime");
		this.size = nbttagcompound.getShort("Size");
		this.setSize(0.75F + (float)this.size * 0.125F, 0.5F + (float)this.size * 0.075F);
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	protected void dropFewItems() {
		this.dropItem(AetherItems.AechorPetal.shiftedIndex, 2 * (mod_Aether.equippedSkyrootSword() ? 2 : 1));
	}
}
