package net.minecraft.src;

public class PotionEffect {
	private int potionID;
	private int duration;
	private int amplifier;

	public PotionEffect(int i1, int i2, int i3) {
		this.potionID = i1;
		this.duration = i2;
		this.amplifier = i3;
	}

	public PotionEffect(PotionEffect potionEffect1) {
		this.potionID = potionEffect1.potionID;
		this.duration = potionEffect1.duration;
		this.amplifier = potionEffect1.amplifier;
	}

	public void combine(PotionEffect potionEffect1) {
		if(this.potionID != potionEffect1.potionID) {
			System.err.println("This method should only be called for matching effects!");
		}

		if(potionEffect1.amplifier > this.amplifier) {
			this.amplifier = potionEffect1.amplifier;
			this.duration = potionEffect1.duration;
		} else if(potionEffect1.amplifier == this.amplifier && this.duration < potionEffect1.duration) {
			this.duration = potionEffect1.duration;
		}

	}

	public int getPotionID() {
		return this.potionID;
	}

	public int getDuration() {
		return this.duration;
	}

	public int getAmplifier() {
		return this.amplifier;
	}

	public boolean onUpdate(EntityLiving entityLiving1) {
		if(this.duration > 0) {
			if(Potion.potionTypes[this.potionID].isReady(this.duration, this.amplifier)) {
				this.performEffect(entityLiving1);
			}

			this.deincrementDuration();
		}

		return this.duration > 0;
	}

	private int deincrementDuration() {
		return --this.duration;
	}

	public void performEffect(EntityLiving entityLiving1) {
		if(this.duration > 0) {
			Potion.potionTypes[this.potionID].performEffect(entityLiving1, this.amplifier);
		}

	}

	public String getEffectName() {
		return Potion.potionTypes[this.potionID].getName();
	}

	public int hashCode() {
		return this.potionID;
	}

	public String toString() {
		String string1 = "";
		if(this.getAmplifier() > 0) {
			string1 = this.getEffectName() + " x " + (this.getAmplifier() + 1) + ", Duration: " + this.getDuration();
		} else {
			string1 = this.getEffectName() + ", Duration: " + this.getDuration();
		}

		return Potion.potionTypes[this.potionID].isUsable() ? "(" + string1 + ")" : string1;
	}

	public boolean equals(Object object1) {
		if(!(object1 instanceof PotionEffect)) {
			return false;
		} else {
			PotionEffect potionEffect2 = (PotionEffect)object1;
			return this.potionID == potionEffect2.potionID && this.amplifier == potionEffect2.amplifier && this.duration == potionEffect2.duration;
		}
	}
}
