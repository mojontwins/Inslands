package net.minecraft.src;

public abstract class Enchantment {
	public static final Enchantment[] enchantmentsList = new Enchantment[256];
	public static final Enchantment protection = new EnchantmentProtection(0, 10, 0);
	public static final Enchantment fireProtection = new EnchantmentProtection(1, 5, 1);
	public static final Enchantment featherFalling = new EnchantmentProtection(2, 5, 2);
	public static final Enchantment blastProtection = new EnchantmentProtection(3, 2, 3);
	public static final Enchantment projectileProtection = new EnchantmentProtection(4, 5, 4);
	public static final Enchantment respiration = new EnchantmentOxygen(5, 2);
	public static final Enchantment aquaAffinity = new EnchantmentWaterWorker(6, 2);
	public static final Enchantment sharpness = new EnchantmentDamage(16, 10, 0);
	public static final Enchantment smite = new EnchantmentDamage(17, 5, 1);
	public static final Enchantment baneOfArthropods = new EnchantmentDamage(18, 5, 2);
	public static final Enchantment knockback = new EnchantmentKnockback(19, 5);
	public static final Enchantment fireAspect = new EnchantmentFireAspect(20, 2);
	public static final Enchantment looting = new EnchantmentLootBonus(21, 2, EnumEnchantmentType.weapon);
	public static final Enchantment efficiency = new EnchantmentDigging(32, 10);
	public static final Enchantment silkTouch = new EnchantmentUntouching(33, 1);
	public static final Enchantment unbreaking = new EnchantmentDurability(34, 5);
	public static final Enchantment fortune = new EnchantmentLootBonus(35, 2, EnumEnchantmentType.digger);
	public static final Enchantment power = new EnchantmentArrowDamage(48, 10);
	public static final Enchantment punch = new EnchantmentArrowKnockback(49, 2);
	public static final Enchantment flame = new EnchantmentArrowFire(50, 2);
	public static final Enchantment infinity = new EnchantmentArrowInfinite(51, 1);
	public final int effectId;
	private final int weight;
	public EnumEnchantmentType type;
	protected String name;

	protected Enchantment(int i1, int i2, EnumEnchantmentType enumEnchantmentType3) {
		this.effectId = i1;
		this.weight = i2;
		this.type = enumEnchantmentType3;
		if(enchantmentsList[i1] != null) {
			throw new IllegalArgumentException("Duplicate enchantment id!");
		} else {
			enchantmentsList[i1] = this;
		}
	}

	public int getWeight() {
		return this.weight;
	}

	public int getMinLevel() {
		return 1;
	}

	public int getMaxLevel() {
		return 1;
	}

	public int getMinEnchantability(int i1) {
		return 1 + i1 * 10;
	}

	public int getMaxEnchantability(int i1) {
		return this.getMinEnchantability(i1) + 5;
	}

	public int calcModifierDamage(int i1, DamageSource damageSource2) {
		return 0;
	}

	public int calcModifierLiving(int i1, EntityLiving entityLiving2) {
		return 0;
	}

	public boolean canApplyTogether(Enchantment enchantment1) {
		return this != enchantment1;
	}

	public Enchantment setName(String string1) {
		this.name = string1;
		return this;
	}

	public String getName() {
		return "enchantment." + this.name;
	}

	public String getTranslatedName(int i1) {
		String string2 = StatCollector.translateToLocal(this.getName());
		return string2 + " " + StatCollector.translateToLocal("enchantment.level." + i1);
	}
}
