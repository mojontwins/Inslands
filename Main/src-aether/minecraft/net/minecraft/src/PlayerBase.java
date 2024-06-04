package net.minecraft.src;

public abstract class PlayerBase {
	public EntityPlayerSP player;

	public PlayerBase(EntityPlayerSP p) {
		this.player = p;
	}

	public void playerInit() {
	}

	public boolean onLivingUpdate() {
		return false;
	}

	public boolean updatePlayerActionState() {
		return false;
	}

	public boolean handleKeyPress(int i, boolean flag) {
		return false;
	}

	public boolean writeEntityToNBT(NBTTagCompound tag) {
		return false;
	}

	public boolean readEntityFromNBT(NBTTagCompound tag) {
		return false;
	}

	public boolean setEntityDead() {
		return false;
	}

	public boolean onDeath(Entity killer) {
		return false;
	}

	public boolean respawn() {
		return false;
	}

	public boolean attackEntityFrom(Entity attacker, int damage) {
		return false;
	}

	public double getDistanceSq(double d, double d1, double d2, double answer) {
		return answer;
	}

	public boolean isInWater(boolean inWater) {
		return inWater;
	}

	public boolean onExitGUI() {
		return false;
	}

	public boolean heal(int i) {
		return false;
	}

	public boolean canTriggerWalking(boolean canTrigger) {
		return canTrigger;
	}

	public int getPlayerArmorValue(int armor) {
		return armor;
	}

	public float getCurrentPlayerStrVsBlock(Block block, float f) {
		return f;
	}

	public boolean moveFlying(float x, float y, float z) {
		return false;
	}

	public boolean moveEntity(double x, double y, double z) {
		return false;
	}

	public EnumStatus sleepInBedAt(int x, int y, int z, EnumStatus status) {
		return status;
	}

	public float getEntityBrightness(float f, float brightness) {
		return brightness;
	}

	public boolean pushOutOfBlocks(double x, double y, double z) {
		return false;
	}

	public boolean onUpdate() {
		return false;
	}

	public void afterUpdate() {
	}

	public boolean moveEntityWithHeading(float f, float f1) {
		return false;
	}

	public boolean isOnLadder(boolean onLadder) {
		return onLadder;
	}

	public boolean isInsideOfMaterial(Material material, boolean inMaterial) {
		return inMaterial;
	}

	public boolean isSneaking(boolean sneaking) {
		return sneaking;
	}

	public boolean dropCurrentItem() {
		return false;
	}

	public boolean dropPlayerItem(ItemStack itemstack) {
		return false;
	}

	public boolean displayGUIEditSign(TileEntitySign sign) {
		return false;
	}

	public boolean displayGUIChest(IInventory iinventory) {
		return false;
	}

	public boolean displayWorkbenchGUI(int i, int j, int k) {
		return false;
	}

	public boolean displayGUIFurnace(TileEntityFurnace furnace) {
		return false;
	}

	public boolean displayGUIDispenser(TileEntityDispenser dispenser) {
		return false;
	}

	public boolean sendChatMessage(String s) {
		return false;
	}

	public String getHurtSound(String previous) {
		return null;
	}

	public Boolean canHarvestBlock(Block block, Boolean previous) {
		return null;
	}

	public boolean fall(float f) {
		return false;
	}

	public boolean jump() {
		return false;
	}

	public boolean damageEntity(int i) {
		return false;
	}

	public Double getDistanceSqToEntity(Entity entity, Double previous) {
		return null;
	}

	public boolean attackTargetEntityWithCurrentItem(Entity entity) {
		return false;
	}

	public Boolean handleWaterMovement(Boolean previous) {
		return null;
	}

	public Boolean handleLavaMovement(Boolean previous) {
		return null;
	}

	public boolean dropPlayerItemWithRandomChoice(ItemStack itemstack, boolean flag) {
		return false;
	}

	public void beforeUpdate() {
	}

	public void beforeMoveEntity(double d, double d1, double d2) {
	}

	public void afterMoveEntity(double d, double d1, double d2) {
	}

	public void beforeSleepInBedAt(int i, int j, int k) {
	}
}
