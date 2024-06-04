package net.minecraft.src;

import java.util.List;

public class EntityFireMonster extends EntityFlying implements IAetherBoss {
	public int wideness;
	public int orgX;
	public int orgY;
	public int orgZ;
	public int motionTimer;
	public int entCount;
	public int flameCount;
	public int ballCount;
	public int chatLog;
	public int chatCount;
	public int hurtness;
	public int direction;
	public double rotary;
	public double speedness;
	public Entity target;
	public boolean gotTarget;
	public String bossName;
	public static final float jimz = 57.295773F;

	public EntityFireMonster(World world) {
		super(world);
		this.texture = "/aether/mobs/firemonster.png";
		this.setSize(2.25F, 2.5F);
		this.noClip = true;
		this.orgX = MathHelper.floor_double(this.posX);
		this.orgY = MathHelper.floor_double(this.boundingBox.minY) + 1;
		this.orgZ = MathHelper.floor_double(this.posZ);
		this.wideness = 10;
		this.health = 50;
		this.speedness = 0.5D - (double)this.health / 70.0D * 0.2D;
		this.direction = 0;
		this.entCount = this.rand.nextInt(6);
		this.bossName = NameGen.gen();
	}

	public EntityFireMonster(World world, int x, int y, int z, int rad, int dir) {
		super(world);
		this.texture = "/aether/mobs/firemonster.png";
		this.setSize(2.25F, 2.5F);
		this.setPosition((double)x + 0.5D, (double)y, (double)z + 0.5D);
		this.wideness = rad - 2;
		this.orgX = x;
		this.orgY = y;
		this.orgZ = z;
		this.noClip = true;
		this.rotary = (double)this.rand.nextFloat() * 360.0D;
		this.health = 50;
		this.speedness = 0.5D - (double)this.health / 70.0D * 0.2D;
		this.direction = dir;
		this.entCount = this.rand.nextInt(6);
		this.bossName = NameGen.gen();
	}

	public boolean canDespawn() {
		return false;
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.health > 0) {
			double a = (double)(this.rand.nextFloat() - 0.5F);
			double b = (double)this.rand.nextFloat();
			double c = (double)(this.rand.nextFloat() - 0.5F);
			double d = this.posX + a * b;
			double e = this.boundingBox.minY + b - 0.5D;
			double f = this.posZ + c * b;
			this.worldObj.spawnParticle("flame", d, e, f, 0.0D, -0.07500000298023224D, 0.0D);
			++this.entCount;
			if(this.entCount >= 3) {
				this.burnEntities();
				this.evapWater();
				this.entCount = 0;
			}

			if(this.hurtness > 0) {
				--this.hurtness;
				if(this.hurtness == 0) {
					this.texture = "/aether/mobs/firemonster.png";
				}
			}
		}

		if(this.chatCount > 0) {
			--this.chatCount;
		}

	}

	protected Entity findPlayerToAttack() {
		EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 32.0D);
		return entityplayer != null && this.canEntityBeSeen(entityplayer) ? entityplayer : null;
	}

	public void updatePlayerActionState() {
		super.updatePlayerActionState();
		if(this.gotTarget && this.target == null) {
			this.target = this.findPlayerToAttack();
			this.gotTarget = false;
		}

		if(this.target == null) {
			this.setPosition((double)this.orgX + 0.5D, (double)this.orgY, (double)this.orgZ + 0.5D);
			this.setDoor(0);
		} else {
			this.renderYawOffset = this.rotationYaw;
			this.setPosition(this.posX, (double)this.orgY, this.posZ);
			this.motionY = 0.0D;
			boolean pool = false;
			if(this.motionX > 0.0D && (int)Math.floor(this.posX) > this.orgX + this.wideness) {
				this.rotary = 360.0D - this.rotary;
				pool = true;
			} else if(this.motionX < 0.0D && (int)Math.floor(this.posX) < this.orgX - this.wideness) {
				this.rotary = 360.0D - this.rotary;
				pool = true;
			}

			if(this.motionZ > 0.0D && (int)Math.floor(this.posZ) > this.orgZ + this.wideness) {
				this.rotary = 180.0D - this.rotary;
				pool = true;
			} else if(this.motionZ < 0.0D && (int)Math.floor(this.posZ) < this.orgZ - this.wideness) {
				this.rotary = 180.0D - this.rotary;
				pool = true;
			}

			if(this.rotary > 360.0D) {
				this.rotary -= 360.0D;
			} else if(this.rotary < 0.0D) {
				this.rotary += 360.0D;
			}

			if(this.target != null) {
				this.faceEntity(this.target, 20.0F, 20.0F);
			}

			double crazy = this.rotary / 57.295772552490234D;
			this.motionX = Math.sin(crazy) * this.speedness;
			this.motionZ = Math.cos(crazy) * this.speedness;
			++this.motionTimer;
			if(this.motionTimer >= 20 || pool) {
				this.motionTimer = 0;
				if(this.rand.nextInt(3) == 0) {
					this.rotary += (double)(this.rand.nextFloat() - this.rand.nextFloat()) * 60.0D;
				}
			}

			++this.flameCount;
			if(this.flameCount == 40 && this.rand.nextInt(2) == 0) {
				this.poopFire();
			} else if(this.flameCount >= 55 + this.health / 2 && this.target != null && this.target instanceof EntityLiving) {
				this.makeFireBall(1);
				this.flameCount = 0;
			}

			if(this.target != null && this.target.isDead) {
				this.setPosition((double)this.orgX + 0.5D, (double)this.orgY, (double)this.orgZ + 0.5D);
				this.motionX = 0.0D;
				this.motionY = 0.0D;
				this.motionZ = 0.0D;
				this.target = null;
				this.chatLine("\u00a7cSuch is the fate of a being who opposes the might of the sun.");
				this.setDoor(0);
				mod_Aether.currentBoss = null;
				this.gotTarget = false;
			}

		}
	}

	public void burnEntities() {
		List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.0D, 4.0D, 0.0D));

		for(int j = 0; j < list.size(); ++j) {
			Entity entity1 = (Entity)list.get(j);
			if(entity1 instanceof EntityLiving && !entity1.isImmuneToFire) {
				entity1.attackEntityFrom(this, 10);
				entity1.fire = 300;
			}
		}

	}

	public void evapWater() {
		int x = MathHelper.floor_double(this.posX);
		int z = MathHelper.floor_double(this.posZ);

		for(int i = 0; i < 8; ++i) {
			int b = this.orgY - 2 + i;
			if(this.worldObj.getBlockMaterial(x, b, z) == Material.water) {
				this.worldObj.setBlockWithNotify(x, b, z, 0);
				this.worldObj.playSoundEffect((double)((float)x + 0.5F), (double)((float)b + 0.5F), (double)((float)z + 0.5F), "random.fizz", 0.5F, 2.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.8F);

				for(int l = 0; l < 8; ++l) {
					this.worldObj.spawnParticle("largesmoke", (double)x + Math.random(), (double)b + 0.75D, (double)z + Math.random(), 0.0D, 0.0D, 0.0D);
				}
			}
		}

	}

	public void makeFireBall(int shots) {
		this.worldObj.playSoundAtEntity(this, "mob.ghast.fireball", 5.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
		boolean flag = false;
		++this.ballCount;
		if(this.ballCount >= 3 + this.rand.nextInt(3)) {
			flag = true;
			this.ballCount = 0;
		}

		for(int i = 0; i < shots; ++i) {
			EntityFiroBall e1 = new EntityFiroBall(this.worldObj, this.posX - this.motionX / 2.0D, this.posY, this.posZ - this.motionZ / 2.0D, flag);
			this.worldObj.entityJoinedWorld(e1);
		}

	}

	public void poopFire() {
		int x = MathHelper.floor_double(this.posX);
		int z = MathHelper.floor_double(this.posZ);
		int b = this.orgY - 2;
		if(AetherBlocks.isGood(this.worldObj.getBlockId(x, b, z), this.worldObj.getBlockMetadata(x, b, z))) {
			this.worldObj.setBlockWithNotify(x, b, z, Block.fire.blockID);
		}

	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setShort("OriginX", (short)this.orgX);
		nbttagcompound.setShort("OriginY", (short)this.orgY);
		nbttagcompound.setShort("OriginZ", (short)this.orgZ);
		nbttagcompound.setShort("Wideness", (short)this.wideness);
		nbttagcompound.setShort("FlameCount", (short)this.flameCount);
		nbttagcompound.setShort("BallCount", (short)this.ballCount);
		nbttagcompound.setShort("ChatLog", (short)this.chatLog);
		nbttagcompound.setFloat("Rotary", (float)this.rotary);
		this.gotTarget = this.target != null;
		nbttagcompound.setBoolean("GotTarget", this.gotTarget);
		nbttagcompound.setBoolean("IsCurrentBoss", this.isCurrentBoss());
		nbttagcompound.setString("BossName", this.bossName);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.orgX = nbttagcompound.getShort("OriginX");
		this.orgY = nbttagcompound.getShort("OriginY");
		this.orgZ = nbttagcompound.getShort("OriginZ");
		this.wideness = nbttagcompound.getShort("Wideness");
		this.flameCount = nbttagcompound.getShort("FlameCount");
		this.ballCount = nbttagcompound.getShort("BallCount");
		this.chatLog = nbttagcompound.getShort("ChatLog");
		this.rotary = (double)nbttagcompound.getFloat("Rotary");
		this.gotTarget = nbttagcompound.getBoolean("GotTarget");
		this.speedness = 0.5D - (double)this.health / 70.0D * 0.2D;
		if(nbttagcompound.getBoolean("IsCurrentBoss")) {
			mod_Aether.currentBoss = this;
		}

		this.bossName = nbttagcompound.getString("BossName");
	}

	public void chatLine(String s) {
		ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(s);
	}

	public boolean chatWithMe() {
		if(this.chatCount <= 0) {
			if(this.chatLog == 0) {
				this.chatLine("\u00a7cYou are certainly a brave soul to have entered this chamber.");
				this.chatLog = 1;
				this.chatCount = 100;
			} else if(this.chatLog == 1) {
				this.chatLine("\u00a7cBegone human, you serve no purpose here.");
				this.chatLog = 2;
				this.chatCount = 100;
			} else if(this.chatLog == 2) {
				this.chatLine("\u00a7cYour presence annoys me. Do you not fear my burning aura?");
				this.chatLog = 3;
				this.chatCount = 100;
			} else if(this.chatLog == 3) {
				this.chatLine("\u00a7cI have nothing to offer you, fool. Leave me at peace.");
				this.chatLog = 4;
				this.chatCount = 100;
			} else if(this.chatLog == 4) {
				this.chatLine("\u00a7cPerhaps you are ignorant. Do you wish to know who I am?");
				this.chatLog = 5;
				this.chatCount = 100;
			} else if(this.chatLog == 5) {
				this.chatLine("\u00a7cI am a sun spirit, embodiment of Aether\'s eternal daylight.");
				this.chatLine("\u00a7cAs long as I am alive, the sun will never set on this world.");
				this.chatLog = 6;
				this.chatCount = 100;
			} else if(this.chatLog == 6) {
				this.chatLine("\u00a7cMy body burns with the anger of a thousand beasts.");
				this.chatLine("\u00a7cNo man, hero, or villain can harm me. You are no exception.");
				this.chatLog = 7;
				this.chatCount = 100;
			} else if(this.chatLog == 7) {
				this.chatLine("\u00a7cYou wish to challenge the might of the sun? You are mad.");
				this.chatLine("\u00a7cDo not further insult me or you will feel my wrath.");
				this.chatLog = 8;
				this.chatCount = 100;
			} else if(this.chatLog == 8) {
				this.chatLine("\u00a7cThis is your final warning. Leave now, or prepare to burn.");
				this.chatLog = 9;
				this.chatCount = 100;
			} else {
				if(this.chatLog == 9) {
					this.chatLine("\u00a76As you wish, your death will be slow and agonizing.");
					this.chatLog = 10;
					mod_Aether.currentBoss = this;
					return true;
				}

				if(this.chatLog == 10 && this.target == null) {
					this.chatLine("\u00a7cDid your previous death not satisfy your curiosity, human?");
					this.chatLog = 9;
					this.chatCount = 100;
				}
			}
		}

		return false;
	}

	public boolean interact(EntityPlayer ep) {
		if(this.chatWithMe()) {
			this.rotary = 57.295772552490234D * Math.atan2(this.posX - ep.posX, this.posZ - ep.posZ);
			this.target = ep;
			this.setDoor(AetherBlocks.LockedDungeonStone.blockID);
			return true;
		} else {
			return false;
		}
	}

	public void addVelocity(double d, double d1, double d2) {
	}

	public void knockBack(Entity entity, int i, double d, double d1) {
	}

	public boolean attackEntityFrom(Entity e, int i) {
		if(e != null && e instanceof EntityFiroBall) {
			this.speedness = 0.5D - (double)this.health / 70.0D * 0.2D;
			boolean flag = super.attackEntityFrom(e, i);
			if(flag) {
				this.hurtness = 15;
				this.texture = "/aether/mobs/firemonsterHurt.png";
				EntityFireMinion minion = new EntityFireMinion(this.worldObj);
				minion.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
				this.worldObj.entityJoinedWorld(minion);
				this.worldObj.entityJoinedWorld(minion);
				this.worldObj.entityJoinedWorld(minion);
				if(this.health <= 0) {
					mod_Aether.currentBoss = null;
					this.chatLine("\u00a7bSuch bitter cold... is this the feeling... of pain?");
					this.setDoor(0);
					this.unlockTreasure();
				}
			}

			return flag;
		} else {
			return false;
		}
	}

	protected void dropFewItems() {
		this.entityDropItem(new ItemStack(AetherItems.Key, 1, 2), 0.0F);
	}

	private void setDoor(int ID) {
		int y;
		int x;
		if(this.direction / 2 == 0) {
			for(y = this.orgY - 1; y < this.orgY + 2; ++y) {
				for(x = this.orgZ - 1; x < this.orgZ + 2; ++x) {
					this.worldObj.setBlockAndMetadata(this.orgX + (this.direction == 0 ? -11 : 11), y, x, ID, 2);
				}
			}
		} else {
			for(y = this.orgY - 1; y < this.orgY + 2; ++y) {
				for(x = this.orgX - 1; x < this.orgX + 2; ++x) {
					this.worldObj.setBlockAndMetadata(x, y, this.orgZ + (this.direction == 3 ? 11 : -11), ID, 2);
				}
			}
		}

	}

	private void unlockTreasure() {
		int x;
		int y;
		if(this.direction / 2 == 0) {
			for(x = this.orgY - 1; x < this.orgY + 2; ++x) {
				for(y = this.orgZ - 1; y < this.orgZ + 2; ++y) {
					this.worldObj.setBlock(this.orgX + (this.direction == 0 ? 11 : -11), x, y, 0);
				}
			}
		} else {
			for(x = this.orgY - 1; x < this.orgY + 2; ++x) {
				for(y = this.orgX - 1; y < this.orgX + 2; ++y) {
					this.worldObj.setBlock(y, x, this.orgZ + (this.direction == 3 ? -11 : 11), 0);
				}
			}
		}

		mod_Aether.giveAchievement(AetherAchievements.defeatGold);

		for(x = this.orgX - 20; x < this.orgX + 20; ++x) {
			for(y = this.orgY - 3; y < this.orgY + 6; ++y) {
				for(int z = this.orgZ - 20; z < this.orgZ + 20; ++z) {
					int id = this.worldObj.getBlockId(x, y, z);
					if(id == AetherBlocks.LockedDungeonStone.blockID) {
						this.worldObj.setBlockAndMetadata(x, y, z, AetherBlocks.DungeonStone.blockID, this.worldObj.getBlockMetadata(x, y, z));
					}

					if(id == AetherBlocks.LockedLightDungeonStone.blockID) {
						this.worldObj.setBlockAndMetadata(x, y, z, AetherBlocks.LightDungeonStone.blockID, this.worldObj.getBlockMetadata(x, y, z));
					}
				}
			}
		}

	}

	public int getBossHP() {
		return this.health;
	}

	public int getBossMaxHP() {
		return 50;
	}

	public boolean isCurrentBoss() {
		return mod_Aether.currentBoss == null ? false : this.equals(mod_Aether.currentBoss);
	}

	public int getBossEntityID() {
		return this.entityId;
	}

	public String getBossTitle() {
		return this.bossName + ", the Sun Spirit";
	}
}
