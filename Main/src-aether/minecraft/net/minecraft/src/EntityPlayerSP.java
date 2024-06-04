package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;

public class EntityPlayerSP extends EntityPlayer {
	public MovementInput movementInput;
	protected Minecraft mc;
	private MouseFilter field_21903_bJ = new MouseFilter();
	private MouseFilter field_21904_bK = new MouseFilter();
	private MouseFilter field_21902_bL = new MouseFilter();
	public List playerBases = new ArrayList();

	public EntityPlayerSP(Minecraft minecraft, World world, Session session, int i) {
		super(world);
		this.mc = minecraft;
		this.dimension = i;
		if(session != null && session.username != null && session.username.length() > 0) {
			this.skinUrl = "http://s3.amazonaws.com/MinecraftSkins/" + session.username + ".png";
		}

		this.username = session.username;
		this.playerBases = PlayerAPI.playerInit(this);
	}

	public boolean attackEntityFrom(Entity entity, int i) {
		return PlayerAPI.attackEntityFrom(this, entity, i) ? false : super.attackEntityFrom(entity, i);
	}

	public void onDeath(Entity entity) {
		if(!PlayerAPI.onDeath(this, entity)) {
			super.onDeath(entity);
		}
	}

	public void updatePlayerActionState() {
		if(!PlayerAPI.updatePlayerActionState(this)) {
			super.updatePlayerActionState();
			this.moveStrafing = this.movementInput.moveStrafe;
			this.moveForward = this.movementInput.moveForward;
			this.isJumping = this.movementInput.jump;
		}
	}

	public void superUpdatePlayerActionState() {
		super.updatePlayerActionState();
	}

	public void onLivingUpdate() {
		if(!PlayerAPI.onLivingUpdate(this)) {
			if(!this.mc.statFileWriter.hasAchievementUnlocked(AchievementList.openInventory)) {
				this.mc.guiAchievement.queueAchievementInformation(AchievementList.openInventory);
			}

			this.prevTimeInPortal = this.timeInPortal;
			if(this.inPortal) {
				if(!this.worldObj.multiplayerWorld && this.ridingEntity != null) {
					this.mountEntity((Entity)null);
				}

				if(this.mc.currentScreen != null) {
					this.mc.displayGuiScreen((GuiScreen)null);
				}

				if(this.timeInPortal == 0.0F) {
					this.mc.sndManager.playSoundFX("portal.trigger", 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
				}

				this.timeInPortal += 0.0125F;
				if(this.timeInPortal >= 1.0F) {
					this.timeInPortal = 1.0F;
					if(!this.worldObj.multiplayerWorld) {
						this.timeUntilPortal = 10;
						this.mc.sndManager.playSoundFX("portal.travel", 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
						this.mc.usePortal();
					}
				}

				this.inPortal = false;
			} else {
				if(this.timeInPortal > 0.0F) {
					this.timeInPortal -= 0.05F;
				}

				if(this.timeInPortal < 0.0F) {
					this.timeInPortal = 0.0F;
				}
			}

			if(this.timeUntilPortal > 0) {
				--this.timeUntilPortal;
			}

			this.movementInput.updatePlayerMoveState(this);
			if(this.movementInput.sneak && this.ySize < 0.2F) {
				this.ySize = 0.2F;
			}

			this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ + (double)this.width * 0.35D);
			this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ - (double)this.width * 0.35D);
			this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ - (double)this.width * 0.35D);
			this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ + (double)this.width * 0.35D);
			super.onLivingUpdate();
		}
	}

	public void superOnLivingUpdate() {
		super.onLivingUpdate();
	}

	public void superOnUpdate() {
		super.onUpdate();
	}

	public void moveFlying(float f, float f1, float f2) {
		if(!PlayerAPI.moveFlying(this, f, f1, f2)) {
			super.moveFlying(f, f1, f2);
		}
	}

	protected boolean canTriggerWalking() {
		return PlayerAPI.canTriggerWalking(this, true);
	}

	public void resetPlayerKeyState() {
		this.movementInput.resetKeyState();
	}

	public void handleKeyPress(int i, boolean flag) {
		if(!PlayerAPI.handleKeyPress(this, i, flag)) {
			this.movementInput.checkKeyForMovementInput(i, flag);
		}
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		if(!PlayerAPI.writeEntityToNBT(this, nbttagcompound)) {
			super.writeEntityToNBT(nbttagcompound);
			nbttagcompound.setInteger("Score", this.score);
		}
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		if(!PlayerAPI.readEntityFromNBT(this, nbttagcompound)) {
			super.readEntityFromNBT(nbttagcompound);
			this.score = nbttagcompound.getInteger("Score");
		}
	}

	public void closeScreen() {
		if(!PlayerAPI.onExitGUI(this)) {
			super.closeScreen();
			this.mc.displayGuiScreen((GuiScreen)null);
		}
	}

	public void displayGUIEditSign(TileEntitySign tileentitysign) {
		if(!PlayerAPI.displayGUIEditSign(this, tileentitysign)) {
			this.mc.displayGuiScreen(new GuiEditSign(tileentitysign));
		}
	}

	public void displayGUIChest(IInventory iinventory) {
		if(!PlayerAPI.displayGUIChest(this, iinventory)) {
			this.mc.displayGuiScreen(new GuiChest(this.inventory, iinventory));
		}
	}

	public void displayWorkbenchGUI(int i, int j, int k) {
		if(!PlayerAPI.displayWorkbenchGUI(this, i, j, k)) {
			this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj, i, j, k));
		}
	}

	public void displayGUIFurnace(TileEntityFurnace tileentityfurnace) {
		if(!PlayerAPI.displayGUIFurnace(this, tileentityfurnace)) {
			this.mc.displayGuiScreen(new GuiFurnace(this.inventory, tileentityfurnace));
		}
	}

	public void displayGUIDispenser(TileEntityDispenser tileentitydispenser) {
		if(!PlayerAPI.displayGUIDispenser(this, tileentitydispenser)) {
			this.mc.displayGuiScreen(new GuiDispenser(this.inventory, tileentitydispenser));
		}
	}

	public void onItemPickup(Entity entity, int i) {
		this.mc.effectRenderer.addEffect(new EntityPickupFX(this.mc.theWorld, entity, this, -0.5F));
	}

	public int getPlayerArmorValue() {
		return PlayerAPI.getPlayerArmorValue(this, this.inventory.getTotalArmorValue());
	}

	public void setEntityDead() {
		if(!PlayerAPI.setEntityDead(this)) {
			super.setEntityDead();
		}
	}

	public double getDistanceSq(double d, double d1, double d2) {
		return PlayerAPI.getDistanceSq(this, d, d1, d2, super.getDistanceSq(d, d1, d2));
	}

	public boolean isInWater() {
		return PlayerAPI.isInWater(this, this.inWater);
	}

	public boolean isSneaking() {
		return PlayerAPI.isSneaking(this, this.movementInput.sneak && !this.sleeping);
	}

	public float getCurrentPlayerStrVsBlock(Block block) {
		float f = this.inventory.getStrVsBlock(block);
		if(this.isInsideOfMaterial(Material.water)) {
			f /= 5.0F;
		}

		if(!this.onGround) {
			f /= 5.0F;
		}

		return PlayerAPI.getCurrentPlayerStrVsBlock(this, block, f);
	}

	public void heal(int i) {
		if(!PlayerAPI.heal(this, i)) {
			super.heal(i);
		}
	}

	public void setHealth(int i) {
		int j = this.health - i;
		if(j <= 0) {
			this.health = i;
			if(j < 0) {
				this.heartsLife = this.heartsHalvesLife / 2;
			}
		} else {
			this.field_9346_af = j;
			this.prevHealth = this.health;
			this.heartsLife = this.heartsHalvesLife;
			this.damageEntity(j);
			this.hurtTime = this.maxHurtTime = 10;
		}

	}

	public void respawnPlayer() {
		if(!PlayerAPI.respawn(this)) {
			this.mc.respawn(false, 0);
		}
	}

	public void func_6420_o() {
	}

	public void addChatMessage(String s) {
		this.mc.ingameGUI.addChatMessageTranslate(s);
	}

	public void addStat(StatBase statbase, int i) {
		if(statbase != null) {
			if(statbase.func_25067_a()) {
				Achievement achievement = (Achievement)statbase;
				if(achievement.parentAchievement == null || this.mc.statFileWriter.hasAchievementUnlocked(achievement.parentAchievement)) {
					if(!this.mc.statFileWriter.hasAchievementUnlocked(achievement)) {
						this.mc.guiAchievement.queueTakenAchievement(achievement);
					}

					this.mc.statFileWriter.readStat(statbase, i);
				}
			} else {
				this.mc.statFileWriter.readStat(statbase, i);
			}

		}
	}

	private boolean isBlockTranslucent(int i, int j, int k) {
		return this.worldObj.isBlockNormalCube(i, j, k);
	}

	protected boolean pushOutOfBlocks(double d, double d1, double d2) {
		if(PlayerAPI.pushOutOfBlocks(this, d, d1, d2)) {
			return false;
		} else {
			int i = MathHelper.floor_double(d);
			int j = MathHelper.floor_double(d1);
			int k = MathHelper.floor_double(d2);
			double d3 = d - (double)i;
			double d4 = d2 - (double)k;
			if(this.isBlockTranslucent(i, j, k) || this.isBlockTranslucent(i, j + 1, k)) {
				boolean flag = !this.isBlockTranslucent(i - 1, j, k) && !this.isBlockTranslucent(i - 1, j + 1, k);
				boolean flag1 = !this.isBlockTranslucent(i + 1, j, k) && !this.isBlockTranslucent(i + 1, j + 1, k);
				boolean flag2 = !this.isBlockTranslucent(i, j, k - 1) && !this.isBlockTranslucent(i, j + 1, k - 1);
				boolean flag3 = !this.isBlockTranslucent(i, j, k + 1) && !this.isBlockTranslucent(i, j + 1, k + 1);
				byte byte0 = -1;
				double d5 = 9999.0D;
				if(flag && d3 < d5) {
					d5 = d3;
					byte0 = 0;
				}

				if(flag1 && 1.0D - d3 < d5) {
					d5 = 1.0D - d3;
					byte0 = 1;
				}

				if(flag2 && d4 < d5) {
					d5 = d4;
					byte0 = 4;
				}

				if(flag3 && 1.0D - d4 < d5) {
					double f = 1.0D - d4;
					byte0 = 5;
				}

				float f1 = 0.1F;
				if(byte0 == 0) {
					this.motionX = (double)(-f1);
				}

				if(byte0 == 1) {
					this.motionX = (double)f1;
				}

				if(byte0 == 4) {
					this.motionZ = (double)(-f1);
				}

				if(byte0 == 5) {
					this.motionZ = (double)f1;
				}
			}

			return false;
		}
	}

	public EnumStatus superSleepInBedAt(int i, int j, int k) {
		return super.sleepInBedAt(i, j, k);
	}

	public Minecraft getMc() {
		return this.mc;
	}

	public void superMoveEntity(double d, double d1, double d2) {
		super.moveEntity(d, d1, d2);
	}

	public void setMoveForward(float f) {
		this.moveForward = f;
	}

	public void setMoveStrafing(float f) {
		this.moveStrafing = f;
	}

	public void setIsJumping(boolean flag) {
		this.isJumping = flag;
	}

	public float getEntityBrightness(float f) {
		return PlayerAPI.getEntityBrightness(this, f, super.getEntityBrightness(f));
	}

	public void onUpdate() {
		PlayerAPI.beforeUpdate(this);
		if(!PlayerAPI.onUpdate(this)) {
			super.onUpdate();
		}

		PlayerAPI.afterUpdate(this);
	}

	public void superMoveFlying(float f, float f1, float f2) {
		super.moveFlying(f, f1, f2);
	}

	public void moveEntity(double d, double d1, double d2) {
		PlayerAPI.beforeMoveEntity(this, d, d1, d2);
		if(!PlayerAPI.moveEntity(this, d, d1, d2)) {
			super.moveEntity(d, d1, d2);
		}

		PlayerAPI.afterMoveEntity(this, d, d1, d2);
	}

	public EnumStatus sleepInBedAt(int i, int j, int k) {
		PlayerAPI.beforeSleepInBedAt(this, i, j, k);
		EnumStatus enumstatus = PlayerAPI.sleepInBedAt(this, i, j, k);
		return enumstatus == null ? super.sleepInBedAt(i, j, k) : enumstatus;
	}

	public void doFall(float fallDist) {
		super.fall(fallDist);
	}

	public float getFallDistance() {
		return this.fallDistance;
	}

	public boolean getSleeping() {
		return this.sleeping;
	}

	public boolean getJumping() {
		return this.isJumping;
	}

	public void doJump() {
		this.jump();
	}

	public Random getRandom() {
		return this.rand;
	}

	public void setFallDistance(float f) {
		this.fallDistance = f;
	}

	public void setYSize(float f) {
		this.ySize = f;
	}

	public void moveEntityWithHeading(float f, float f1) {
		if(!PlayerAPI.moveEntityWithHeading(this, f, f1)) {
			super.moveEntityWithHeading(f, f1);
		}
	}

	public boolean isOnLadder() {
		return PlayerAPI.isOnLadder(this, super.isOnLadder());
	}

	public void setActionState(float newMoveStrafing, float newMoveForward, boolean newIsJumping) {
		this.moveStrafing = newMoveStrafing;
		this.moveForward = newMoveForward;
		this.isJumping = newIsJumping;
	}

	public boolean isInsideOfMaterial(Material material) {
		return PlayerAPI.isInsideOfMaterial(this, material, super.isInsideOfMaterial(material));
	}

	public void dropCurrentItem() {
		if(!PlayerAPI.dropCurrentItem(this)) {
			super.dropCurrentItem();
		}
	}

	public void dropPlayerItem(ItemStack itemstack) {
		if(!PlayerAPI.dropPlayerItem(this, itemstack)) {
			super.dropPlayerItem(itemstack);
		}
	}

	public boolean superIsInsideOfMaterial(Material material) {
		return super.isInsideOfMaterial(material);
	}

	public float superGetEntityBrightness(float f) {
		return super.getEntityBrightness(f);
	}

	public void sendChatMessage(String s) {
		PlayerAPI.sendChatMessage(this, s);
	}

	protected String getHurtSound() {
		String result = PlayerAPI.getHurtSound(this);
		return result != null ? result : super.getHurtSound();
	}

	public String superGetHurtSound() {
		return super.getHurtSound();
	}

	public float superGetCurrentPlayerStrVsBlock(Block block) {
		return super.getCurrentPlayerStrVsBlock(block);
	}

	public boolean canHarvestBlock(Block block) {
		Boolean result = PlayerAPI.canHarvestBlock(this, block);
		return result != null ? result.booleanValue() : super.canHarvestBlock(block);
	}

	public boolean superCanHarvestBlock(Block block) {
		return super.canHarvestBlock(block);
	}

	protected void fall(float f) {
		if(!PlayerAPI.fall(this, f)) {
			super.fall(f);
		}

	}

	public void superFall(float f) {
		super.fall(f);
	}

	protected void jump() {
		if(!PlayerAPI.jump(this)) {
			super.jump();
		}

	}

	public void superJump() {
		super.jump();
	}

	protected void damageEntity(int i) {
		if(!PlayerAPI.damageEntity(this, i)) {
			super.damageEntity(i);
		}

	}

	protected void superDamageEntity(int i) {
		super.damageEntity(i);
	}

	public double getDistanceSqToEntity(Entity entity) {
		Double result = PlayerAPI.getDistanceSqToEntity(this, entity);
		return result != null ? result.doubleValue() : super.getDistanceSqToEntity(entity);
	}

	public double superGetDistanceSqToEntity(Entity entity) {
		return super.getDistanceSqToEntity(entity);
	}

	public void attackTargetEntityWithCurrentItem(Entity entity) {
		if(!PlayerAPI.attackTargetEntityWithCurrentItem(this, entity)) {
			super.attackTargetEntityWithCurrentItem(entity);
		}

	}

	public void superAttackTargetEntityWithCurrentItem(Entity entity) {
		super.attackTargetEntityWithCurrentItem(entity);
	}

	public boolean handleWaterMovement() {
		Boolean result = PlayerAPI.handleWaterMovement(this);
		return result != null ? result.booleanValue() : super.handleWaterMovement();
	}

	public boolean superHandleWaterMovement() {
		return super.handleWaterMovement();
	}

	public boolean handleLavaMovement() {
		Boolean result = PlayerAPI.handleLavaMovement(this);
		return result != null ? result.booleanValue() : super.handleLavaMovement();
	}

	public boolean superHandleLavaMovement() {
		return super.handleLavaMovement();
	}

	public void dropPlayerItemWithRandomChoice(ItemStack itemstack, boolean flag) {
		if(!PlayerAPI.dropPlayerItemWithRandomChoice(this, itemstack, flag)) {
			super.dropPlayerItemWithRandomChoice(itemstack, flag);
		}

	}

	public void superDropPlayerItemWithRandomChoice(ItemStack itemstack, boolean flag) {
		super.dropPlayerItemWithRandomChoice(itemstack, flag);
	}
}
