package net.minecraft.src;

public class EntityValkyrie extends EntityDungeonMob implements IAetherBoss {
	public boolean isSwinging;
	public boolean boss;
	public boolean duel;
	public boolean hasDungeon;
	public int teleTimer;
	public int angerLevel;
	public int timeLeft;
	public int chatTime;
	public int dungeonX;
	public int dungeonY;
	public int dungeonZ;
	public int dungeonEntranceZ;
	public double safeX;
	public double safeY;
	public double safeZ;
	public float sinage;
	public double lastMotionY;
	public String bossName;

	public EntityValkyrie(World world) {
		super(world);
		this.setSize(0.8F, 1.6F);
		this.texture = "/aether/mobs/valkyrie.png";
		this.teleTimer = this.rand.nextInt(250);
		this.health = 50;
		this.moveSpeed = 0.5F;
		this.timeLeft = 1200;
		this.attackStrength = 7;
		this.safeX = this.posX;
		this.safeY = this.posY;
		this.safeZ = this.posZ;
	}

	public EntityValkyrie(World world, double x, double y, double z, boolean flag) {
		super(world);
		this.setSize(0.8F, 1.6F);
		this.bossName = NameGen.gen();
		this.texture = "/aether/mobs/valkyrie.png";
		if(flag) {
			this.texture = "/aether/mobs/valkyrie2.png";
			this.health = 500;
			this.boss = true;
		} else {
			this.health = 50;
		}

		this.teleTimer = this.rand.nextInt(250);
		this.moveSpeed = 0.5F;
		this.timeLeft = 1200;
		this.attackStrength = 7;
		this.safeX = this.posX = x;
		this.safeY = this.posY = y;
		this.safeZ = this.posZ = z;
		this.hasDungeon = false;
	}

	public void fall(float f) {
	}

	public void onUpdate() {
		this.lastMotionY = this.motionY;
		super.onUpdate();
		if(!this.onGround && this.playerToAttack != null && this.lastMotionY >= 0.0D && this.motionY < 0.0D && this.getDistanceToEntity(this.playerToAttack) <= 16.0F && this.canEntityBeSeen(this.playerToAttack)) {
			double a = this.playerToAttack.posX - this.posX;
			double b = this.playerToAttack.posZ - this.posZ;
			double angle = Math.atan2(a, b);
			this.motionX = Math.sin(angle) * 0.25D;
			this.motionZ = Math.cos(angle) * 0.25D;
		}

		if(!this.onGround && !this.isOnLadder() && Math.abs(this.motionY - this.lastMotionY) > 0.07D && Math.abs(this.motionY - this.lastMotionY) < 0.09D) {
			this.motionY += 0.054999999701976776D;
			if(this.motionY < -0.2750000059604645D) {
				this.motionY = -0.2750000059604645D;
			}
		}

		this.moveSpeed = this.playerToAttack == null ? 0.5F : 1.0F;
		if(this.worldObj.difficultySetting <= 0 && (this.playerToAttack != null || this.angerLevel > 0)) {
			this.angerLevel = 0;
			this.playerToAttack = null;
		}

		if(this.isSwinging) {
			this.prevSwingProgress += 0.15F;
			this.swingProgress += 0.15F;
			if(this.prevSwingProgress > 1.0F || this.swingProgress > 1.0F) {
				this.isSwinging = false;
				this.prevSwingProgress = 0.0F;
				this.swingProgress = 0.0F;
			}
		}

		if(!this.onGround) {
			this.sinage += 0.75F;
		} else {
			this.sinage += 0.15F;
		}

		if(this.sinage > 6.283186F) {
			this.sinage -= 6.283186F;
		}

		if(!this.otherDimension()) {
			--this.timeLeft;
			if(this.timeLeft <= 0) {
				this.isDead = true;
				this.spawnExplosionParticle();
			}
		}

	}

	public boolean otherDimension() {
		return true;
	}

	public void teleport(double x, double y, double z, int rad) {
		int a = this.rand.nextInt(rad + 1);
		int b = this.rand.nextInt(rad / 2);
		int c = rad - a;
		a *= this.rand.nextInt(2) * 2 - 1;
		b *= this.rand.nextInt(2) * 2 - 1;
		c *= this.rand.nextInt(2) * 2 - 1;
		x += (double)a;
		y += (double)b;
		z += (double)c;
		int newX = (int)Math.floor(x - 0.5D);
		int newY = (int)Math.floor(y - 0.5D);
		int newZ = (int)Math.floor(z - 0.5D);
		boolean flag = false;

		for(int q = 0; q < 32 && !flag; ++q) {
			int i = newX + (this.rand.nextInt(rad / 2) - this.rand.nextInt(rad / 2));
			int j = newY + (this.rand.nextInt(rad / 2) - this.rand.nextInt(rad / 2));
			int k = newZ + (this.rand.nextInt(rad / 2) - this.rand.nextInt(rad / 2));
			if(j <= 124 && j >= 5 && this.isAirySpace(i, j, k) && this.isAirySpace(i, j + 1, k) && !this.isAirySpace(i, j - 1, k) && (!this.hasDungeon || i > this.dungeonX && i < this.dungeonX + 20 && j > this.dungeonY && j < this.dungeonY + 12 && k > this.dungeonZ && k < this.dungeonZ + 20)) {
				newX = i;
				newY = j;
				newZ = k;
				flag = true;
			}
		}

		if(!flag) {
			this.teleFail();
		} else {
			this.spawnExplosionParticle();
			this.setPosition((double)newX + 0.5D, (double)newY + 0.5D, (double)newZ + 0.5D);
			this.motionX = 0.0D;
			this.motionY = 0.0D;
			this.motionZ = 0.0D;
			this.moveForward = 0.0F;
			this.moveStrafing = 0.0F;
			this.isJumping = false;
			this.rotationPitch = 0.0F;
			this.rotationYaw = 0.0F;
			this.setPathToEntity((PathEntity)null);
			this.renderYawOffset = this.rand.nextFloat() * 360.0F;
			this.spawnExplosionParticle();
			this.teleTimer = this.rand.nextInt(40);
		}

	}

	public boolean isAirySpace(int x, int y, int z) {
		int p = this.worldObj.getBlockId(x, y, z);
		return p == 0 || Block.blocksList[p].getCollisionBoundingBoxFromPool(this.worldObj, x, y, z) == null;
	}

	public boolean canDespawn() {
		return !this.boss;
	}

	public boolean interact(EntityPlayer entityplayer) {
		this.faceEntity(entityplayer, 180.0F, 180.0F);
		ItemStack itemstack;
		if(!this.boss) {
			if(this.timeLeft >= 1200) {
				itemstack = entityplayer.getCurrentEquippedItem();
				if(itemstack != null && itemstack.itemID == AetherItems.VictoryMedal.shiftedIndex && itemstack.stackSize >= 0) {
					if(itemstack.stackSize >= 10) {
						this.chatItUp("Umm... that\'s a nice pile of medallions you have there...");
					} else if(itemstack.stackSize >= 5) {
						this.chatItUp("That\'s pretty impressive, but you won\'t defeat me.");
					} else {
						this.chatItUp("You think you\'re a tough guy, eh? Well, bring it on!");
					}
				} else {
					int pokey = this.rand.nextInt(3);
					if(pokey == 2) {
						this.chatItUp("What\'s that? You want to fight? Aww, what a cute little human.");
					} else if(pokey == 1) {
						this.chatItUp("You\'re not thinking of fighting a big, strong Valkyrie are you?");
					} else {
						this.chatItUp("I don\'t think you should bother me, you could get really hurt.");
					}
				}
			}
		} else if(this.duel) {
			this.chatItUp("If you wish to challenge me, strike at any time.");
		} else if(!this.duel) {
			itemstack = entityplayer.getCurrentEquippedItem();
			if(itemstack != null && itemstack.itemID == AetherItems.VictoryMedal.shiftedIndex && itemstack.stackSize >= 10) {
				itemstack.stackSize -= 10;
				if(itemstack.stackSize <= 0) {
					itemstack.func_1097_a(entityplayer);
					entityplayer.destroyCurrentEquippedItem();
					this.chatTime = 0;
					this.chatItUp("Very well, attack me when you wish to begin.");
					this.duel = true;
				}
			} else {
				this.chatItUp("Show me 10 victory medals, and I will fight you.");
			}
		}

		return true;
	}

	private void chatItUp(String s) {
		if(this.chatTime <= 0 && this.otherDimension()) {
			ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(s);
			this.chatTime = 60;
		}

	}

	public void makeHomeShot(int shots, EntityLiving ep) {
		for(int i = 0; i < shots; ++i) {
			EntityHomeShot e1 = new EntityHomeShot(this.worldObj, this.posX - this.motionX / 2.0D, this.posY, this.posZ - this.motionZ / 2.0D, ep);
			this.worldObj.entityJoinedWorld(e1);
		}

	}

	protected void dropFewItems() {
		if(this.boss) {
			this.entityDropItem(new ItemStack(AetherItems.Key, 1, 1), 0.0F);
			this.dropItem(Item.swordGold.shiftedIndex, 1);
		} else {
			this.dropItem(AetherItems.VictoryMedal.shiftedIndex, 1);
		}

	}

	public void updatePlayerActionState() {
		super.updatePlayerActionState();
		++this.teleTimer;
		if(this.teleTimer >= 450) {
			if(this.playerToAttack != null) {
				if(this.boss && this.onGround && this.rand.nextInt(2) == 0 && this.playerToAttack != null && this.playerToAttack instanceof EntityLiving) {
					this.makeHomeShot(1, (EntityLiving)this.playerToAttack);
					this.teleTimer = -100;
				} else {
					this.teleport(this.playerToAttack.posX, this.playerToAttack.posY, this.playerToAttack.posZ, 7);
				}
			} else if(this.onGround && !this.boss) {
				this.teleport(this.posX, this.posY, this.posZ, 12 + this.rand.nextInt(12));
			} else {
				this.teleport(this.safeX, this.safeY, this.safeZ, 6);
			}
		} else if(this.teleTimer >= 446 || this.posY > 0.0D && this.posY > this.safeY - 16.0D) {
			if(this.teleTimer % 5 == 0 && this.playerToAttack != null && !this.canEntityBeSeen(this.playerToAttack)) {
				this.teleTimer += 100;
			}
		} else {
			this.teleTimer = 446;
		}

		if(this.onGround && this.teleTimer % 10 == 0 && !this.boss) {
			this.safeX = this.posX;
			this.safeY = this.posY;
			this.safeZ = this.posZ;
		}

		if(this.playerToAttack != null && this.playerToAttack.isDead) {
			this.playerToAttack = null;
			if(this.boss) {
				this.unlockDoor();
				mod_Aether.currentBoss = null;
			}

			this.angerLevel = 0;
		}

		if(this.chatTime > 0) {
			--this.chatTime;
		}

	}

	public void swingArm() {
		if(!this.isSwinging) {
			this.isSwinging = true;
			this.prevSwingProgress = 0.0F;
			this.swingProgress = 0.0F;
		}

	}

	public void teleFail() {
		this.teleTimer -= this.rand.nextInt(40) + 40;
		if(this.posY <= 0.0D) {
			this.teleTimer = 446;
		}

	}

	public boolean getCanSpawnHere() {
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.boundingBox.minY);
		int k = MathHelper.floor_double(this.posZ);
		return this.worldObj.getFullBlockLightValue(i, j, k) > 8 && this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox);
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setShort("Anger", (short)this.angerLevel);
		nbttagcompound.setShort("TeleTimer", (short)this.teleTimer);
		nbttagcompound.setShort("TimeLeft", (short)this.timeLeft);
		nbttagcompound.setBoolean("Boss", this.boss);
		nbttagcompound.setBoolean("Duel", this.duel);
		nbttagcompound.setInteger("DungeonX", this.dungeonX);
		nbttagcompound.setInteger("DungeonY", this.dungeonY);
		nbttagcompound.setInteger("DungeonZ", this.dungeonZ);
		nbttagcompound.setInteger("DungeonEntranceZ", this.dungeonEntranceZ);
		nbttagcompound.setTag("SafePos", this.newDoubleNBTList(new double[]{this.safeX, this.safeY, this.safeZ}));
		nbttagcompound.setBoolean("IsCurrentBoss", this.isCurrentBoss());
		nbttagcompound.setString("BossName", this.bossName);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.angerLevel = nbttagcompound.getShort("Anger");
		this.teleTimer = nbttagcompound.getShort("TeleTimer");
		this.timeLeft = nbttagcompound.getShort("TimeLeft");
		this.duel = nbttagcompound.getBoolean("Duel");
		this.boss = nbttagcompound.getBoolean("Boss");
		this.dungeonX = nbttagcompound.getInteger("DungeonX");
		this.dungeonY = nbttagcompound.getInteger("DungeonY");
		this.dungeonZ = nbttagcompound.getInteger("DungeonZ");
		this.dungeonEntranceZ = nbttagcompound.getInteger("DungeonEntranceZ");
		if(this.boss) {
			this.texture = "/aether/mobs/valkyrie2.png";
		}

		NBTTagList nbttaglist = nbttagcompound.getTagList("SafePos");
		this.safeX = ((NBTTagDouble)nbttaglist.tagAt(0)).doubleValue;
		this.safeY = ((NBTTagDouble)nbttaglist.tagAt(1)).doubleValue;
		this.safeZ = ((NBTTagDouble)nbttaglist.tagAt(2)).doubleValue;
		if(nbttagcompound.getBoolean("IsCurrentBoss")) {
			mod_Aether.currentBoss = this;
		}

		this.bossName = nbttagcompound.getString("BossName");
	}

	protected Entity findPlayerToAttack() {
		return !this.otherDimension() || this.worldObj.difficultySetting > 0 && (!this.boss || this.duel) && this.angerLevel > 0 ? super.findPlayerToAttack() : null;
	}

	public boolean attackEntityFrom(Entity entity, int i) {
		if(entity instanceof EntityPlayer && this.worldObj.difficultySetting > 0) {
			int flag;
			if(this.boss && (!this.duel || this.worldObj.difficultySetting <= 0)) {
				this.spawnExplosionParticle();
				flag = this.rand.nextInt(2);
				if(flag == 2) {
					this.chatItUp("Sorry, I don\'t fight with weaklings.");
				} else {
					this.chatItUp("Try defeating some weaker valkyries first.");
				}

				return false;
			} else {
				if(this.boss) {
					if(this.playerToAttack == null) {
						mod_Aether.currentBoss = this;
						this.chatTime = 0;
						this.chatItUp("This will be your final battle!");
					} else {
						this.teleTimer += 60;
					}
				} else if(this.playerToAttack == null) {
					this.chatTime = 0;
					flag = this.rand.nextInt(3);
					if(flag == 2) {
						this.chatItUp("I\'m not going easy on you!");
					} else if(flag == 1) {
						this.chatItUp("You\'re gonna regret that!");
					} else {
						this.chatItUp("Now you\'re in for it!");
					}
				} else {
					this.teleTimer -= 10;
				}

				this.becomeAngryAt(entity);
				boolean flag1 = super.attackEntityFrom(entity, i);
				if(flag1 && this.health <= 0) {
					int pokey = this.rand.nextInt(3);
					this.isDead = true;
					if(this.boss) {
						this.isDead = false;
						this.unlockDoor();
						this.unlockTreasure();
						this.chatItUp("You are truly... a mighty warrior...");
						mod_Aether.currentBoss = null;
					} else if(pokey == 2) {
						this.chatItUp("Alright, alright! You win!");
					} else if(pokey == 1) {
						this.chatItUp("Okay, I give up! Geez!");
					} else {
						this.chatItUp("Oww! Fine, here\'s your medal...");
					}

					this.spawnExplosionParticle();
				}

				return flag1;
			}
		} else {
			this.teleport(this.posX, this.posY, this.posZ, 8);
			this.fire = 0;
			return false;
		}
	}

	protected void attackEntity(Entity entity, float f) {
		if(this.attackTime <= 0 && f < 2.75F && entity.boundingBox.maxY > this.boundingBox.minY && entity.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			this.swingArm();
			entity.attackEntityFrom(this, this.attackStrength);
			if(entity != null && this.playerToAttack != null && entity == this.playerToAttack && entity instanceof EntityLiving) {
				EntityLiving e1 = (EntityLiving)entity;
				if(e1.health <= 0) {
					this.playerToAttack = null;
					this.angerLevel = 0;
					int pokey = this.rand.nextInt(3);
					this.chatTime = 0;
					if(this.boss) {
						this.chatItUp("As expected of a human.");
						this.unlockDoor();
						mod_Aether.currentBoss = null;
					} else if(pokey == 2) {
						this.chatItUp("You want a medallion? Try being less pathetic.");
					} else if(pokey == 1 && e1 instanceof EntityPlayer) {
						EntityPlayer ep = (EntityPlayer)e1;
						String s = ep.username;
						this.chatItUp("Maybe some day, " + s + "... maybe some day.");
					} else {
						this.chatItUp("Humans aren\'t nearly as cute when they\'re dead.");
					}
				}
			}
		}

	}

	private void becomeAngryAt(Entity entity) {
		this.playerToAttack = entity;
		this.angerLevel = 200 + this.rand.nextInt(200);
		if(this.boss) {
			for(int k = this.dungeonZ + 2; k < this.dungeonZ + 23; k += 7) {
				if(this.worldObj.getBlockId(this.dungeonX - 1, this.dungeonY, k) == 0) {
					this.dungeonEntranceZ = k;
					this.worldObj.setBlockAndMetadata(this.dungeonX - 1, this.dungeonY, k, AetherBlocks.LockedDungeonStone.blockID, 1);
					this.worldObj.setBlockAndMetadata(this.dungeonX - 1, this.dungeonY, k + 1, AetherBlocks.LockedDungeonStone.blockID, 1);
					this.worldObj.setBlockAndMetadata(this.dungeonX - 1, this.dungeonY + 1, k + 1, AetherBlocks.LockedDungeonStone.blockID, 1);
					this.worldObj.setBlockAndMetadata(this.dungeonX - 1, this.dungeonY + 1, k, AetherBlocks.LockedDungeonStone.blockID, 1);
					return;
				}
			}
		}

	}

	private void unlockDoor() {
		this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY, this.dungeonEntranceZ, 0);
		this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY, this.dungeonEntranceZ + 1, 0);
		this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY + 1, this.dungeonEntranceZ + 1, 0);
		this.worldObj.setBlock(this.dungeonX - 1, this.dungeonY + 1, this.dungeonEntranceZ, 0);
	}

	private void unlockTreasure() {
		this.worldObj.setBlockAndMetadata(this.dungeonX + 16, this.dungeonY + 1, this.dungeonZ + 9, Block.trapdoor.blockID, 3);
		this.worldObj.setBlockAndMetadata(this.dungeonX + 17, this.dungeonY + 1, this.dungeonZ + 9, Block.trapdoor.blockID, 2);
		this.worldObj.setBlockAndMetadata(this.dungeonX + 16, this.dungeonY + 1, this.dungeonZ + 10, Block.trapdoor.blockID, 3);
		this.worldObj.setBlockAndMetadata(this.dungeonX + 17, this.dungeonY + 1, this.dungeonZ + 10, Block.trapdoor.blockID, 2);
		mod_Aether.giveAchievement(AetherAchievements.defeatSilver);

		for(int x = this.dungeonX - 26; x < this.dungeonX + 29; ++x) {
			for(int y = this.dungeonY - 1; y < this.dungeonY + 22; ++y) {
				for(int z = this.dungeonZ - 5; z < this.dungeonZ + 25; ++z) {
					int id = this.worldObj.getBlockId(x, y, z);
					if(id == AetherBlocks.LockedDungeonStone.blockID) {
						this.worldObj.setBlockAndMetadata(x, y, z, AetherBlocks.DungeonStone.blockID, this.worldObj.getBlockMetadata(x, y, z));
					}

					if(id == AetherBlocks.Trap.blockID) {
						this.worldObj.setBlockAndMetadata(x, y, z, AetherBlocks.DungeonStone.blockID, this.worldObj.getBlockMetadata(x, y, z));
					}

					if(id == AetherBlocks.LockedLightDungeonStone.blockID) {
						this.worldObj.setBlockAndMetadata(x, y, z, AetherBlocks.LightDungeonStone.blockID, this.worldObj.getBlockMetadata(x, y, z));
					}
				}
			}
		}

	}

	public void setDungeon(int i, int j, int k) {
		this.hasDungeon = true;
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
		return mod_Aether.currentBoss == null ? false : this.equals(mod_Aether.currentBoss);
	}

	public int getBossEntityID() {
		return this.entityId;
	}

	public String getBossTitle() {
		return this.bossName + ", the Valkyrie Queen";
	}
}
