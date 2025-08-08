package net.minecraft.client.model.mojontwins;

import net.minecraft.client.model.betterdungeons.ModelHuman;

public class ModelIceBoss extends ModelHuman {
	public boolean isAttacking = true;

	public ModelIceBoss() {
		super();
	}

	public ModelIceBoss(float f) {
		super(f);
	}

	public ModelIceBoss(float f, float f1) {
		super(f, f1);
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5);
		
		if(this.isAttacking) {
			this.bipedLeftArm.rotateAngleY = 1.0F;
			this.bipedRightArm.rotateAngleY = 1.0F;
		}
	}

}
