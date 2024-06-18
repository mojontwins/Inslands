package net.minecraft.src;

public class EntityAITargetNonTamed extends EntityAINearestAttackableTarget {
	private EntityTameable field_48299_g;

	public EntityAITargetNonTamed(EntityTameable entityTameable1, Class class2, float f3, int i4, boolean z5) {
		super(entityTameable1, class2, f3, i4, z5);
		this.field_48299_g = entityTameable1;
	}

	public boolean shouldExecute() {
		return this.field_48299_g.isTamed() ? false : super.shouldExecute();
	}
}
