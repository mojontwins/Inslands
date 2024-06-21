package net.minecraft.src;

public class ItemFood extends Item {
	public final int field_35427_a;
	private final int healAmount;
	private final float saturationModifier;
	private final boolean isWolfsFavoriteMeat;
	private boolean alwaysEdible;
	private int potionId;
	private int potionDuration;
	private int potionAmplifier;
	private float potionEffectProbability;

	public ItemFood(int i1, int i2, float f3, boolean z4) {
		super(i1);
		this.field_35427_a = 32;
		this.healAmount = i2;
		this.isWolfsFavoriteMeat = z4;
		this.saturationModifier = f3;
	}

	public ItemFood(int i1, int i2, boolean z3) {
		this(i1, i2, 0.6F, z3);
	}

	public ItemStack onFoodEaten(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		--itemStack1.stackSize;
		entityPlayer3.getFoodStats().addStats(this);
		world2.playSoundAtEntity(entityPlayer3, "random.burp", 0.5F, world2.rand.nextFloat() * 0.1F + 0.9F);
		if(!world2.isRemote && this.potionId > 0 && world2.rand.nextFloat() < this.potionEffectProbability) {
			entityPlayer3.addPotionEffect(new PotionEffect(this.potionId, this.potionDuration * 20, this.potionAmplifier));
		}

		return itemStack1;
	}

	public int getMaxItemUseDuration(ItemStack itemStack1) {
		return 32;
	}

	public EnumAction getItemUseAction(ItemStack itemStack1) {
		return EnumAction.eat;
	}

	public ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		if(entityPlayer3.canEat(this.alwaysEdible)) {
			entityPlayer3.setItemInUse(itemStack1, this.getMaxItemUseDuration(itemStack1));
		}

		return itemStack1;
	}

	public int getHealAmount() {
		return this.healAmount;
	}

	public float getSaturationModifier() {
		return this.saturationModifier;
	}

	public boolean isWolfsFavoriteMeat() {
		return this.isWolfsFavoriteMeat;
	}

	public ItemFood setPotionEffect(int i1, int i2, int i3, float f4) {
		this.potionId = i1;
		this.potionDuration = i2;
		this.potionAmplifier = i3;
		this.potionEffectProbability = f4;
		return this;
	}

	public ItemFood setAlwaysEdible() {
		this.alwaysEdible = true;
		return this;
	}

	public Item setItemName(String string1) {
		return super.setItemName(string1);
	}
}
