package net.minecraft.src;

public class ItemLance extends Item implements IReach {
	private int weaponDamage;

	public ItemLance(int i, EnumToolMaterial enumtoolmaterial) {
		super(i);
		this.maxStackSize = 1;
		this.setMaxDamage(enumtoolmaterial.getMaxUses());
		this.weaponDamage = 4 + enumtoolmaterial.getDamageVsEntity() * 2;
		SAPI.reachAdd(this);
	}

	public float getStrVsBlock(ItemStack itemstack, Block block) {
		return block.blockID != Block.web.blockID ? 1.5F : 15.0F;
	}

	public boolean hitEntity(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1) {
		itemstack.damageItem(1, entityliving1);
		return true;
	}

	public boolean onBlockDestroyed(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving) {
		itemstack.damageItem(2, entityliving);
		return true;
	}

	public int getDamageVsEntity(Entity entity) {
		return this.weaponDamage;
	}

	public boolean isFull3D() {
		return true;
	}

	public boolean canHarvestBlock(Block block) {
		return block.blockID == Block.web.blockID;
	}

	public boolean reachItemMatches(ItemStack itemstack) {
		return itemstack == null ? false : itemstack.itemID == AetherItems.Lance.shiftedIndex;
	}

	public float getReach(ItemStack itemstack) {
		return 10.0F;
	}
}
