package net.minecraft.world.entity.monster;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.util.MathHelper;
import net.minecraft.world.GlobalVars;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.IAetherBoss;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemTool;
import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.stats.AchievementList;

public class EntitySlider extends EntityFlying implements IAetherBoss {
	public int moveTimer;
	public int dennis;
	public int rennis;
	public int chatTime;
	public Entity target;
	public boolean awake;
	public boolean gotMovement;
	public boolean crushed;
	public float speedy;
	public float harvey;
	public int direction;
	private int dungeonX;
	private int dungeonY;
	private int dungeonZ;
	public String bossName;

	public EntitySlider(World world) {
		super(world);
		this.rotationYaw = 0.0F;
		this.rotationPitch = 0.0F;
		this.setSize(2.0F, 2.0F);
		this.health = 500;
		this.dennis = 1;
		this.texture = "/mob/sliderSleep.png";
		this.chatTime = 60;
		this.bossName = "Golgothoth";
	}

	public void entityInit() {
		super.entityInit();
		this.posX = Math.floor(this.posX + 0.5D);
		this.posY = Math.floor(this.posY + 0.5D);
		this.posZ = Math.floor(this.posZ + 0.5D);
	}

	public boolean canDespawn() {
		return false;
	}

	protected String getLivingSound() {
		return "ambient.cave.cave";
	}

	protected String getHurtSound() {
		return "step.stone";
	}

	protected String getDeathSound() {
		return "aether.sound.bosses.slider.sliderDeath";
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setFloat("Speedy", this.speedy);
		nbttagcompound.setShort("MoveTimer", (short)this.moveTimer);
		nbttagcompound.setShort("Direction", (short)this.direction);
		nbttagcompound.setBoolean("GotMovement", this.gotMovement);
		nbttagcompound.setBoolean("Awake", this.awake);
		nbttagcompound.setInteger("DungeonX", this.dungeonX);
		nbttagcompound.setInteger("DungeonY", this.dungeonY);
		nbttagcompound.setInteger("DungeonZ", this.dungeonZ);
		nbttagcompound.setBoolean("IsCurrentBoss", this.isCurrentBoss());
		nbttagcompound.setString("BossName", this.bossName);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.speedy = nbttagcompound.getFloat("Speedy");
		this.moveTimer = nbttagcompound.getShort("MoveTimer");
		this.direction = nbttagcompound.getShort("Direction");
		this.gotMovement = nbttagcompound.getBoolean("GotMovement");
		this.awake = nbttagcompound.getBoolean("Awake");
		this.dungeonX = nbttagcompound.getInteger("DungeonX");
		this.dungeonY = nbttagcompound.getInteger("DungeonY");
		this.dungeonZ = nbttagcompound.getInteger("DungeonZ");
		if(nbttagcompound.getBoolean("IsCurrentBoss")) {
			GlobalVars.currentBoss = this;
		}

		this.bossName = nbttagcompound.getString("BossName");
		if(this.awake) {
			if(this.criticalCondition()) {
				this.texture = "/mob/sliderAwake_red.png";
			} else {
				this.texture = "/mob/sliderAwake.png";
			}
		}

	}

	public boolean criticalCondition() {
		return this.health <= 125;
	}

	public void onUpdate() {
		super.onUpdate();
		this.renderYawOffset = this.rotationPitch = this.rotationYaw = 0.0F;
		if(this.awake) {
			if(this.target != null && this.target instanceof EntityLiving) {
				EntityLiving a = (EntityLiving)this.target;
				if(a.health <= 0) {
					this.awake = false;
					GlobalVars.currentBoss = null;
					this.target = null;
					this.texture = "/mob/sliderSleep.png";
					this.stop();
					this.openDoor();
					this.moveTimer = 0;
					return;
				}
			} else {
				if(this.target != null && this.target.isDead) {
					this.awake = false;
					GlobalVars.currentBoss = null;
					this.target = null;
					this.texture = "/mob/sliderSleep.png";
					this.stop();
					this.openDoor();
					this.moveTimer = 0;
					return;
				}

				if(this.target == null) {
					this.target = this.worldObj.getClosestPlayerToEntity(this, -1.0D);
					if(this.target == null) {
						this.awake = false;
						GlobalVars.currentBoss = null;
						this.target = null;
						this.texture = "/mob/sliderSleep.png";
						this.stop();
						this.openDoor();
						this.moveTimer = 0;
						return;
					}
				}
			}

			double b;
			double c;
			double d12;
			if(this.gotMovement) {
				if(this.isCollided) {
					d12 = this.posX - 0.5D;
					b = this.boundingBox.minY + 0.75D;
					c = this.posZ - 0.5D;
					this.crushed = false;
					if(b < 124.0D && b > 4.0D) {
						int i;
						double a1;
						double b1;
						if(this.direction == 0) {
							for(i = 0; i < 25; ++i) {
								a1 = (double)(i / 5 - 2) * 0.75D;
								b1 = (double)(i % 5 - 2) * 0.75D;
								this.blockCrush((int)(d12 + a1), (int)(b + 1.5D), (int)(c + b1));
							}
						} else if(this.direction == 1) {
							for(i = 0; i < 25; ++i) {
								a1 = (double)(i / 5 - 2) * 0.75D;
								b1 = (double)(i % 5 - 2) * 0.75D;
								this.blockCrush((int)(d12 + a1), (int)(b - 1.5D), (int)(c + b1));
							}
						} else if(this.direction == 2) {
							for(i = 0; i < 25; ++i) {
								a1 = (double)(i / 5 - 2) * 0.75D;
								b1 = (double)(i % 5 - 2) * 0.75D;
								this.blockCrush((int)(d12 + 1.5D), (int)(b + a1), (int)(c + b1));
							}
						} else if(this.direction == 3) {
							for(i = 0; i < 25; ++i) {
								a1 = (double)(i / 5 - 2) * 0.75D;
								b1 = (double)(i % 5 - 2) * 0.75D;
								this.blockCrush((int)(d12 - 1.5D), (int)(b + a1), (int)(c + b1));
							}
						} else if(this.direction == 4) {
							for(i = 0; i < 25; ++i) {
								a1 = (double)(i / 5 - 2) * 0.75D;
								b1 = (double)(i % 5 - 2) * 0.75D;
								this.blockCrush((int)(d12 + a1), (int)(b + b1), (int)(c + 1.5D));
							}
						} else if(this.direction == 5) {
							for(i = 0; i < 25; ++i) {
								a1 = (double)(i / 5 - 2) * 0.75D;
								b1 = (double)(i % 5 - 2) * 0.75D;
								this.blockCrush((int)(d12 + a1), (int)(b + b1), (int)(c - 1.5D));
							}
						}
					}

					if(this.crushed) {
						this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.explode", 3.0F, (0.625F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
						this.worldObj.playSoundAtEntity(this, "aether.sound.bosses.slider.sliderCollide", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
					}

					this.stop();
				} else {
					if(this.speedy < 2.0F) {
						this.speedy += this.criticalCondition() ? 0.0325F : 0.025F;
					}

					this.motionX = 0.0D;
					this.motionY = 0.0D;
					this.motionZ = 0.0D;
					if(this.direction == 0) {
						this.motionY = (double)this.speedy;
						if(this.boundingBox.minY > this.target.boundingBox.minY + 0.35D) {
							this.stop();
							this.moveTimer = 8;
						}
					} else if(this.direction == 1) {
						this.motionY = (double)(-this.speedy);
						if(this.boundingBox.minY < this.target.boundingBox.minY - 0.25D) {
							this.stop();
							this.moveTimer = 8;
						}
					} else if(this.direction == 2) {
						this.motionX = (double)this.speedy;
						if(this.posX > this.target.posX + 0.125D) {
							this.stop();
							this.moveTimer = 8;
						}
					} else if(this.direction == 3) {
						this.motionX = (double)(-this.speedy);
						if(this.posX < this.target.posX - 0.125D) {
							this.stop();
							this.moveTimer = 8;
						}
					} else if(this.direction == 4) {
						this.motionZ = (double)this.speedy;
						if(this.posZ > this.target.posZ + 0.125D) {
							this.stop();
							this.moveTimer = 8;
						}
					} else if(this.direction == 5) {
						this.motionZ = (double)(-this.speedy);
						if(this.posZ < this.target.posZ - 0.125D) {
							this.stop();
							this.moveTimer = 8;
						}
					}
				}
			} else if(this.moveTimer > 0) {
				--this.moveTimer;
				if(this.criticalCondition() && this.rand.nextInt(2) == 0) {
					--this.moveTimer;
				}

				this.motionX = 0.0D;
				this.motionY = 0.0D;
				this.motionZ = 0.0D;
			} else {
				d12 = Math.abs(this.posX - this.target.posX);
				b = Math.abs(this.boundingBox.minY - this.target.boundingBox.minY);
				c = Math.abs(this.posZ - this.target.posZ);
				if(d12 > c) {
					this.direction = 2;
					if(this.posX > this.target.posX) {
						this.direction = 3;
					}
				} else {
					this.direction = 4;
					if(this.posZ > this.target.posZ) {
						this.direction = 5;
					}
				}

				if(b > d12 && b > c || b > 0.25D && this.rand.nextInt(5) == 0) {
					this.direction = 0;
					if(this.posY > this.target.posY) {
						this.direction = 1;
					}
				}

				this.worldObj.playSoundAtEntity(this, "aether.sound.bosses.slider.sliderMove", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
				this.gotMovement = true;
			}
		}

		if(this.harvey > 0.01F) {
			this.harvey *= 0.8F;
		}

		if(this.chatTime > 0) {
			--this.chatTime;
		}

	}

	private void openDoor() {
		int x = this.dungeonX + 15;

		for(int y = this.dungeonY + 1; y < this.dungeonY + 5; ++y) {
			for(int z = this.dungeonZ + 6; z < this.dungeonZ + 10; ++z) {
				this.worldObj.setBlock(x, y, z, 0);
			}
		}

	}

	public void applyEntityCollision(Entity entity) {
		if(this.awake && this.gotMovement) {
			boolean flag = entity.attackEntityFrom(this, 6);
			if(flag && entity instanceof EntityLiving) {
				this.worldObj.playSoundAtEntity(this, "aether.sound.bosses.slider.sliderCollide", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
				if(entity instanceof EntityCreature || entity instanceof EntityPlayer) {
					EntityLiving ek = (EntityLiving)entity;
					ek.motionY += 0.35D;
					ek.motionX *= 2.0D;
					ek.motionZ *= 2.0D;
				}

				this.stop();
			}
		}

	}

	protected void dropFewItems() {
		for(int i = 0; i < 7 + this.rand.nextInt(3); ++i) {
			this.dropItem(Block.dungeonStone.blockID, 1);
		}

		this.entityDropItem(new ItemStack(Item.key, 1, 0), 0.0F);
	}

	public boolean getCanSpawnHere() {
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.boundingBox.minY);
		int k = MathHelper.floor_double(this.posZ);
		return this.worldObj.getBlockId(i, j - 1, k) == Block.grass.blockID && this.worldObj.getFullBlockLightValue(i, j, k) > 8 && super.getCanSpawnHere();
	}

	public void stop() {
		this.gotMovement = false;
		this.moveTimer = 12;
		this.direction = 0;
		this.speedy = 0.0F;
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
	}

	private void chatItUp(String s) {
		if(this.chatTime <= 0) {
			//TODO
			this.chatTime = 60;
		}

	}

	public boolean attackEntityFrom(Entity e1, int i) {
		if(e1 != null && e1 instanceof EntityPlayer) {
			EntityPlayer p1 = (EntityPlayer)e1;
			ItemStack stack = p1.getCurrentEquippedItem();
			if(stack != null && stack.getItem() != null) {
				if(stack.getItem() instanceof ItemTool) {
					ItemTool flag = (ItemTool)stack.getItem();
					if(!flag.canHarvestBlock(Block.stone)) {
						this.chatItUp("Hmm. Perhaps I need to attack it with a Pickaxe?");
						return false;
					} else {
						boolean z13 = super.attackEntityFrom(e1, Math.max(0, i));
						if(z13) {
							int a;
							for(a = 0; a < (this.health <= 0 ? 16 : 48); ++a) {
								double y = this.posX + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 1.5D;
								double b = this.boundingBox.minY + 1.75D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 1.5D;
								double c1 = this.posZ + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 1.5D;
								if(this.health <= 0) {
									this.worldObj.spawnParticle("explode", y, b, c1, 0.0D, 0.0D, 0.0D);
								}
							}

							if(this.health <= 0) {
								this.isDead = true;
								this.openDoor();
								this.unlockBlock(this.dungeonX, this.dungeonY, this.dungeonZ);
								this.worldObj.setBlockAndMetadata(this.dungeonX + 7, this.dungeonY + 1, this.dungeonZ + 7, Block.trapdoor.blockID, 3);
								this.worldObj.setBlockAndMetadata(this.dungeonX + 8, this.dungeonY + 1, this.dungeonZ + 7, Block.trapdoor.blockID, 2);
								this.worldObj.setBlockAndMetadata(this.dungeonX + 7, this.dungeonY + 1, this.dungeonZ + 8, Block.trapdoor.blockID, 3);
								this.worldObj.setBlockAndMetadata(this.dungeonX + 8, this.dungeonY + 1, this.dungeonZ + 8, Block.trapdoor.blockID, 2);
								((EntityPlayer)e1).triggerAchievement(AchievementList.defeatBronze);
								GlobalVars.currentBoss = null;
							} else if(this.awake) {
								if(this.gotMovement) {
									this.speedy *= 0.75F;
								}
							} else {
								this.worldObj.playSoundAtEntity(this, "aether.sound.bosses.slider.sliderAwaken", 2.5F, 1.0F / (this.rand.nextFloat() * 0.2F + 0.9F));
								this.awake = true;
								this.target = e1;
								this.texture = "/mob/sliderAwake.png";
								a = this.dungeonX + 15;
								int i15 = this.dungeonY + 1;

								while(true) {
									if(i15 >= this.dungeonY + 8) {
										GlobalVars.currentBoss = this;
										break;
									}

									for(int c = this.dungeonZ + 5; c < this.dungeonZ + 11; ++c) {
										this.worldObj.setBlock(a, i15, c, Block.lockedDungeonStone.blockID);
									}

									++i15;
								}
							}

							double d14 = Math.abs(this.posX - e1.posX);
							double d16 = Math.abs(this.posZ - e1.posZ);
							if(d14 > d16) {
								this.dennis = 1;
								this.rennis = 0;
								if(this.posX > e1.posX) {
									this.dennis = -1;
								}
							} else {
								this.rennis = 1;
								this.dennis = 0;
								if(this.posZ > e1.posZ) {
									this.rennis = -1;
								}
							}

							this.harvey = 0.7F - (float)this.health / 875.0F;
							if(this.criticalCondition()) {
								this.texture = "/mob/sliderAwake_red.png";
							} else {
								this.texture = "/mob/sliderAwake.png";
							}
						}

						return z13;
					}
				} else {
					this.chatItUp("Hmm. Perhaps I need to attack it with a Pickaxe?");
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private void unlockBlock(int i, int j, int k) {
		int id = this.worldObj.getBlockId(i, j, k);
		if(id == Block.lockedDungeonStone.blockID) {
			this.worldObj.setBlockAndMetadata(i, j, k, Block.dungeonStone.blockID, this.worldObj.getBlockMetadata(i, j, k));
			this.unlockBlock(i + 1, j, k);
			this.unlockBlock(i - 1, j, k);
			this.unlockBlock(i, j + 1, k);
			this.unlockBlock(i, j - 1, k);
			this.unlockBlock(i, j, k + 1);
			this.unlockBlock(i, j, k - 1);
		}

		if(id == Block.lockedLightDungeonStone.blockID) {
			this.worldObj.setBlockAndMetadata(i, j, k, Block.lightDungeonStone.blockID, this.worldObj.getBlockMetadata(i, j, k));
			this.unlockBlock(i + 1, j, k);
			this.unlockBlock(i - 1, j, k);
			this.unlockBlock(i, j + 1, k);
			this.unlockBlock(i, j - 1, k);
			this.unlockBlock(i, j, k + 1);
			this.unlockBlock(i, j, k - 1);
		}

	}

	public void addVelocity(double d, double d1, double d2) {
	}

	public void knockBack(Entity entity, int i, double d, double d1) {
	}

	public void blockCrush(int x, int y, int z) {
		int a = this.worldObj.getBlockId(x, y, z);
		int b = this.worldObj.getBlockMetadata(x, y, z);
		if(a != 0 && a != Block.lockedDungeonStone.blockID && a != Block.lockedLightDungeonStone.blockID) {
			this.addCrackedPebbles(x, y, z);
			Block.blocksList[a].onBlockRemoval(this.worldObj, x, y, z);
			Block.blocksList[a].dropBlockAsItem(this.worldObj, x, y, z, b);
			this.worldObj.setBlockWithNotify(x, y, z, 0);
			this.crushed = true;
			this.addSquirrelButts(x, y, z);
		

		}
	}

	public void addCrackedPebbles(int x, int y, int z) {
		double a = (double)x + 0.5D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
		double b = (double)y + 0.5D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
		double c = (double)z + 0.5D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
		this.worldObj.spawnParticle("crackedPebble", a, b, c, 0.0D, 0.0D, 0.0D);
	}
	
	public void addSquirrelButts(int x, int y, int z) {
		double a = (double)x + 0.5D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
		double b = (double)y + 0.5D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
		double c = (double)z + 0.5D + (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 0.375D;
		this.worldObj.spawnParticle("explode", a, b, c, 0.0D, 0.0D, 0.0D);
	}

	public void setDungeon(int i, int j, int k) {
		this.dungeonX = i;
		this.dungeonY = j;
		this.dungeonZ = k;
	}

	public int getBossHP() {
		return this.health;
	}

	public int getBossMaxHP() {
		return 500;
	}

	public boolean isCurrentBoss() {
		return GlobalVars.currentBoss == null ? false : this.equals(GlobalVars.currentBoss);
	}

	public int getBossEntityID() {
		return this.entityId;
	}

	public String getBossTitle() {
		return this.bossName + ", the Slider";
	}
}
