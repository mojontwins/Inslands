package net.minecraft.src;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Whirly extends EntityAetherAnimal {
	public int entcount = 0;
	public int Life;
	public List fluffies;
	public EffectRenderer Enty;
	public static final float pie = 3.141593F;
	public static final float pia = 0.01745329F;
	public float Angle;
	public float Speed;
	public float Curve;
	public boolean evil;

	public Whirly(World world) {
		super(world);
		this.setSize(0.6F, 0.8F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.moveSpeed = 0.6F;
		this.Angle = this.rand.nextFloat() * 360.0F;
		this.Speed = this.rand.nextFloat() * 0.025F + 0.025F;
		this.Curve = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
		this.Life = this.rand.nextInt(512) + 512;
		if(this.rand.nextInt(10) == 0 && !this.shouldStopEvil()) {
			this.evil = true;
			this.Life /= 2;
		}

		this.fluffies = new ArrayList();
		this.Enty = ModLoader.getMinecraftInstance().effectRenderer;
	}

	public boolean canTriggerWalking() {
		return false;
	}

	public boolean shouldStopEvil() {
		if(!(this.worldObj.saveHandler instanceof SaveHandler)) {
			return false;
		} else {
			File file = new File(((SaveHandler)this.worldObj.saveHandler).getSaveDirectory(), "stopevil.txt");
			return file.exists();
		}
	}

	public void updatePlayerActionState() {
		boolean flag = false;
		if(this.evil) {
			EntityPlayer list = (EntityPlayer)this.getPlayer();
			if(list != null && list.onGround) {
				this.playerToAttack = list;
				flag = true;
			}
		}

		if(this.playerToAttack == null) {
			this.motionX = Math.cos((double)(0.01745329F * this.Angle)) * (double)this.Speed;
			this.motionZ = -Math.sin((double)(0.01745329F * this.Angle)) * (double)this.Speed;
			this.Angle += this.Curve;
		} else {
			super.updatePlayerActionState();
		}

		List list27 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(2.5D, 2.5D, 2.5D));
		if(this.Life-- <= 0 || this.handleWaterMovement()) {
			this.setEntityDead();
		}

		if(this.getPlayer() != null) {
			++this.entcount;
		}

		int d;
		if(this.entcount >= 128) {
			if(this.evil && this.playerToAttack != null) {
				EntityCreeper entityCreeper28 = new EntityCreeper(this.worldObj);
				entityCreeper28.setLocationAndAngles(this.posX, this.posY - 0.75D, this.posZ, this.rand.nextFloat() * 360.0F, 0.0F);
				entityCreeper28.motionX = (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
				entityCreeper28.motionZ = (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.125D;
				this.worldObj.entityJoinedWorld(entityCreeper28);
				this.entcount = 0;
			} else {
				d = this.loot();
				if(d != 0) {
					this.dropItem(d, 1);
					this.entcount = 0;
				}
			}
		}

		double d2;
		double d5;
		double d8;
		float entityfx;
		if(!flag && this.playerToAttack == null) {
			for(d = 0; d < 2; ++d) {
				d2 = (double)((float)this.posX + this.rand.nextFloat() * 0.25F);
				d5 = (double)((float)this.posY + this.height + 0.125F);
				d8 = (double)((float)this.posZ + this.rand.nextFloat() * 0.25F);
				entityfx = this.rand.nextFloat() * 360.0F;
				EntityExplodeFX entityExplodeFX32 = new EntityExplodeFX(this.worldObj, -Math.sin((double)(0.01745329F * entityfx)) * 0.75D, d5 - 0.25D, Math.cos((double)(0.01745329F * entityfx)) * 0.75D, d2, 0.125D, d8);
				this.Enty.addEffect(entityExplodeFX32);
				this.fluffies.add(entityExplodeFX32);
				entityExplodeFX32.renderDistanceWeight = 10.0D;
				entityExplodeFX32.noClip = true;
				entityExplodeFX32.setSize(0.25F, 0.25F);
				entityExplodeFX32.setPosition(this.posX, this.posY, this.posZ);
				entityExplodeFX32.posY = d5;
			}
		} else {
			for(d = 0; d < 3; ++d) {
				d2 = (double)((float)this.posX + this.rand.nextFloat() * 0.25F);
				d5 = (double)((float)this.posY + this.height + 0.125F);
				d8 = (double)((float)this.posZ + this.rand.nextFloat() * 0.25F);
				entityfx = this.rand.nextFloat() * 360.0F;
				EntitySmokeFX d10 = new EntitySmokeFX(this.worldObj, -Math.sin((double)(0.01745329F * entityfx)) * 0.75D, d5 - 0.25D, Math.cos((double)(0.01745329F * entityfx)) * 0.75D, d2, 0.125D, d8, 3.5F);
				this.Enty.addEffect(d10);
				this.fluffies.add(d10);
				d10.renderDistanceWeight = 10.0D;
				d10.noClip = true;
				d10.setSize(0.25F, 0.25F);
				d10.setPosition(this.posX, this.posY, this.posZ);
				d10.posY = d5;
			}
		}

		double d29 = (double)((float)this.posX);
		double d3 = (double)((float)this.posY);
		double d6 = (double)((float)this.posZ);

		int i1;
		double d12;
		double d14;
		double d16;
		double d18;
		double d21;
		double d33;
		for(i1 = 0; i1 < list27.size(); ++i1) {
			Entity entity30 = (Entity)list27.get(i1);
			d33 = (double)((float)entity30.posX);
			d12 = (double)((float)entity30.posY - entity30.yOffset * 0.6F);
			d14 = (double)((float)entity30.posZ);
			d16 = (double)this.getDistanceToEntity(entity30);
			d18 = d12 - d3;
			if(d16 <= 1.5D + d18) {
				entity30.motionY = (double)0.15F;
				entity30.fallDistance = 0.0F;
				if(d18 > 1.5D) {
					entity30.motionY = -0.44999998807907104D + d18 * (double)0.35F;
					d16 += d18 * 1.5D;
					if(entity30 == this.playerToAttack) {
						this.playerToAttack = null;
						this.setPathToEntity((PathEntity)null);
					}
				} else {
					entity30.motionY = 0.125D;
				}

				d21 = Math.atan2(d29 - d33, d6 - d14) / 0.01745329424738884D;
				d21 += 160.0D;
				entity30.motionX = -Math.cos(0.01745329424738884D * d21) * (d16 + 0.25D) * (double)0.1F;
				entity30.motionZ = Math.sin(0.01745329424738884D * d21) * (d16 + 0.25D) * (double)0.1F;
				if(entity30 instanceof Whirly) {
					entity30.setEntityDead();
					if(!this.shouldStopEvil() && !this.evil) {
						this.evil = true;
						this.Life /= 2;
					}
				}
			} else {
				d21 = Math.atan2(d29 - d33, d6 - d14) / 0.01745329424738884D;
				entity30.motionX += Math.sin(0.01745329424738884D * d21) * (double)0.01F;
				entity30.motionZ += Math.cos(0.01745329424738884D * d21) * (double)0.01F;
			}

			int i34 = MathHelper.floor_double(this.posX);
			int k1 = MathHelper.floor_double(this.posY);
			int l1 = MathHelper.floor_double(this.posZ);
			if(this.worldObj.getBlockId(i34, k1 + 1, l1) != 0) {
				this.Life -= 50;
			}

			int i2 = i34 - 1 + this.rand.nextInt(3);
			int j2 = k1 + this.rand.nextInt(5);
			int k2 = l1 - 1 + this.rand.nextInt(3);
			if(this.worldObj.getBlockId(i2, j2, k2) == Block.leaves.blockID) {
				this.worldObj.setBlockWithNotify(i2, j2, k2, 0);
			}
		}

		if(this.fluffies.size() > 0) {
			for(i1 = 0; i1 < this.fluffies.size(); ++i1) {
				EntityFX entityFX31 = (EntityFX)this.fluffies.get(i1);
				if(entityFX31.isDead) {
					this.fluffies.remove(entityFX31);
				} else {
					d33 = (double)((float)entityFX31.posX);
					d12 = (double)((float)entityFX31.boundingBox.minY);
					d14 = (double)((float)entityFX31.posZ);
					d16 = (double)this.getDistanceToEntity(entityFX31);
					d18 = d12 - d3;
					entityFX31.motionY = 0.11500000208616257D;
					d21 = Math.atan2(d29 - d33, d6 - d14) / 0.01745329424738884D;
					d21 += 160.0D;
					entityFX31.motionX = -Math.cos(0.01745329424738884D * d21) * (d16 * 2.5D - d18) * (double)0.1F;
					entityFX31.motionZ = Math.sin(0.01745329424738884D * d21) * (d16 * 2.5D - d18) * (double)0.1F;
				}
			}
		}

	}

	public int loot() {
		int i = this.rand.nextInt(100) + 1;
		return i == 100 ? Item.diamond.shiftedIndex : (i >= 96 ? Item.ingotIron.shiftedIndex : (i >= 91 ? Item.ingotGold.shiftedIndex : (i >= 82 ? Item.coal.shiftedIndex : (i >= 75 ? Block.gravel.blockID : (i >= 64 ? Block.blockClay.blockID : (i >= 52 ? Item.stick.shiftedIndex : (i >= 38 ? Item.flint.shiftedIndex : (i > 20 ? Block.wood.blockID : Block.sand.blockID))))))));
	}

	public void setEntityDead() {
		if(this.fluffies.size() > 0) {
			for(int i = 0; i < this.fluffies.size(); ++i) {
				EntityFX entityfx = (EntityFX)this.fluffies.get(i);
				entityfx.motionX *= 0.5D;
				entityfx.motionY *= 0.75D;
				entityfx.motionZ *= 0.5D;
				this.fluffies.remove(entityfx);
			}
		}

		super.setEntityDead();
	}

	public boolean getCanSpawnHere() {
		if(this.posY < 64.0D) {
			this.posY += 64.0D;
		}

		if(this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox)) {
			int i = MathHelper.floor_double(this.posX);
			int j = MathHelper.floor_double(this.boundingBox.minY);
			int k = MathHelper.floor_double(this.posZ);
			boolean flag = true;

			for(int l = 1; l < 20 && l + j < 125; ++l) {
				if(this.worldObj.getBlockLightValue(i, j + l, k) <= 12 || this.worldObj.getBlockId(i, j + l, k) != 0) {
					flag = false;
					break;
				}
			}

			return flag;
		} else {
			return false;
		}
	}

	public Entity getPlayer() {
		EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
		return entityplayer != null && this.canEntityBeSeen(entityplayer) ? entityplayer : null;
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setFloat("Angle", this.Angle);
		nbttagcompound.setFloat("Speed", this.Speed);
		nbttagcompound.setFloat("Curve", this.Curve);
		nbttagcompound.setShort("Life", (short)this.Life);
		nbttagcompound.setShort("Counter", (short)this.entcount);
		nbttagcompound.setBoolean("Evil", this.evil);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.Angle = nbttagcompound.getFloat("Angle");
		this.Speed = nbttagcompound.getFloat("Speed");
		this.Curve = nbttagcompound.getFloat("Curve");
		this.Life = nbttagcompound.getShort("Life");
		this.entcount = nbttagcompound.getShort("Counter");
		this.evil = nbttagcompound.getBoolean("Evil");
	}

	public boolean attackEntityFrom(Entity entity, int i) {
		return false;
	}

	public void applyEntityCollision(Entity entity) {
	}

	public int getMaxSpawnedInChunk() {
		return 1;
	}

	public boolean isOnLadder() {
		return this.isCollidedHorizontally;
	}
}
