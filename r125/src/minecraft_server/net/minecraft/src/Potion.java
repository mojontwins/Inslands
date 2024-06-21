package net.minecraft.src;

public class Potion {
	public static final Potion[] potionTypes = new Potion[32];
	public static final Potion field_35453_b = null;
	public static final Potion moveSpeed = (new Potion(1, false, 8171462)).setPotionName("potion.moveSpeed").setIconIndex(0, 0);
	public static final Potion moveSlowdown = (new Potion(2, true, 5926017)).setPotionName("potion.moveSlowdown").setIconIndex(1, 0);
	public static final Potion digSpeed = (new Potion(3, false, 14270531)).setPotionName("potion.digSpeed").setIconIndex(2, 0).setEffectiveness(1.5D);
	public static final Potion digSlowdown = (new Potion(4, true, 4866583)).setPotionName("potion.digSlowDown").setIconIndex(3, 0);
	public static final Potion damageBoost = (new Potion(5, false, 9643043)).setPotionName("potion.damageBoost").setIconIndex(4, 0);
	public static final Potion heal = (new PotionHealth(6, false, 16262179)).setPotionName("potion.heal");
	public static final Potion harm = (new PotionHealth(7, true, 4393481)).setPotionName("potion.harm");
	public static final Potion jump = (new Potion(8, false, 7889559)).setPotionName("potion.jump").setIconIndex(2, 1);
	public static final Potion confusion = (new Potion(9, true, 5578058)).setPotionName("potion.confusion").setIconIndex(3, 1).setEffectiveness(0.25D);
	public static final Potion regeneration = (new Potion(10, false, 13458603)).setPotionName("potion.regeneration").setIconIndex(7, 0).setEffectiveness(0.25D);
	public static final Potion resistance = (new Potion(11, false, 10044730)).setPotionName("potion.resistance").setIconIndex(6, 1);
	public static final Potion fireResistance = (new Potion(12, false, 14981690)).setPotionName("potion.fireResistance").setIconIndex(7, 1);
	public static final Potion waterBreathing = (new Potion(13, false, 3035801)).setPotionName("potion.waterBreathing").setIconIndex(0, 2);
	public static final Potion invisibility = (new Potion(14, false, 8356754)).setPotionName("potion.invisibility").setIconIndex(0, 1).setPotionUnusable();
	public static final Potion blindness = (new Potion(15, true, 2039587)).setPotionName("potion.blindness").setIconIndex(5, 1).setEffectiveness(0.25D);
	public static final Potion nightVision = (new Potion(16, false, 2039713)).setPotionName("potion.nightVision").setIconIndex(4, 1).setPotionUnusable();
	public static final Potion hunger = (new Potion(17, true, 5797459)).setPotionName("potion.hunger").setIconIndex(1, 1);
	public static final Potion weakness = (new Potion(18, true, 4738376)).setPotionName("potion.weakness").setIconIndex(5, 0);
	public static final Potion poison = (new Potion(19, true, 5149489)).setPotionName("potion.poison").setIconIndex(6, 0).setEffectiveness(0.25D);
	public static final Potion field_35465_v = null;
	public static final Potion field_35464_w = null;
	public static final Potion field_35474_x = null;
	public static final Potion field_35473_y = null;
	public static final Potion field_35472_z = null;
	public static final Potion field_35444_A = null;
	public static final Potion field_35445_B = null;
	public static final Potion field_35446_C = null;
	public static final Potion field_35440_D = null;
	public static final Potion field_35441_E = null;
	public static final Potion field_35442_F = null;
	public static final Potion field_35443_G = null;
	public final int id;
	private String name = "";
	private int statusIconIndex = -1;
	private final boolean isBadEffect;
	private double effectiveness;
	private boolean usable;
	private final int liquidColor;

	protected Potion(int i1, boolean z2, int i3) {
		this.id = i1;
		potionTypes[i1] = this;
		this.isBadEffect = z2;
		if(z2) {
			this.effectiveness = 0.5D;
		} else {
			this.effectiveness = 1.0D;
		}

		this.liquidColor = i3;
	}

	protected Potion setIconIndex(int i1, int i2) {
		this.statusIconIndex = i1 + i2 * 8;
		return this;
	}

	public int getId() {
		return this.id;
	}

	public void performEffect(EntityLiving entityLiving1, int i2) {
		if(this.id == regeneration.id) {
			if(entityLiving1.getHealth() < entityLiving1.getMaxHealth()) {
				entityLiving1.heal(1);
			}
		} else if(this.id == poison.id) {
			if(entityLiving1.getHealth() > 1) {
				entityLiving1.attackEntityFrom(DamageSource.magic, 1);
			}
		} else if(this.id == hunger.id && entityLiving1 instanceof EntityPlayer) {
			((EntityPlayer)entityLiving1).addExhaustion(0.025F * (float)(i2 + 1));
		} else if(this.id == heal.id && !entityLiving1.isEntityUndead() || this.id == harm.id && entityLiving1.isEntityUndead()) {
			entityLiving1.heal(6 << i2);
		} else if(this.id == harm.id && !entityLiving1.isEntityUndead() || this.id == heal.id && entityLiving1.isEntityUndead()) {
			entityLiving1.attackEntityFrom(DamageSource.magic, 6 << i2);
		}

	}

	public void affectEntity(EntityLiving entityLiving1, EntityLiving entityLiving2, int i3, double d4) {
		int i6;
		if((this.id != heal.id || entityLiving2.isEntityUndead()) && (this.id != harm.id || !entityLiving2.isEntityUndead())) {
			if(this.id == harm.id && !entityLiving2.isEntityUndead() || this.id == heal.id && entityLiving2.isEntityUndead()) {
				i6 = (int)(d4 * (double)(6 << i3) + 0.5D);
				if(entityLiving1 == null) {
					entityLiving2.attackEntityFrom(DamageSource.magic, i6);
				} else {
					entityLiving2.attackEntityFrom(DamageSource.causeIndirectMagicDamage(entityLiving2, entityLiving1), i6);
				}
			}
		} else {
			i6 = (int)(d4 * (double)(6 << i3) + 0.5D);
			entityLiving2.heal(i6);
		}

	}

	public boolean isInstant() {
		return false;
	}

	public boolean isReady(int i1, int i2) {
		if(this.id != regeneration.id && this.id != poison.id) {
			return this.id == hunger.id;
		} else {
			int i3 = 25 >> i2;
			return i3 > 0 ? i1 % i3 == 0 : true;
		}
	}

	public Potion setPotionName(String string1) {
		this.name = string1;
		return this;
	}

	public String getName() {
		return this.name;
	}

	protected Potion setEffectiveness(double d1) {
		this.effectiveness = d1;
		return this;
	}

	public double getEffectiveness() {
		return this.effectiveness;
	}

	public Potion setPotionUnusable() {
		this.usable = true;
		return this;
	}

	public boolean isUsable() {
		return this.usable;
	}

	public int getLiquidColor() {
		return this.liquidColor;
	}
}
