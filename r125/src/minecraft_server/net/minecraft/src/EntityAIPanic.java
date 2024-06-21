package net.minecraft.src;

public class EntityAIPanic extends EntityAIBase {
	private EntityCreature field_48208_a;
	private float field_48206_b;
	private double field_48207_c;
	private double field_48204_d;
	private double field_48205_e;

	public EntityAIPanic(EntityCreature entityCreature1, float f2) {
		this.field_48208_a = entityCreature1;
		this.field_48206_b = f2;
		this.setMutexBits(1);
	}

	public boolean shouldExecute() {
		if(this.field_48208_a.getAITarget() == null) {
			return false;
		} else {
			Vec3D vec3D1 = RandomPositionGenerator.func_48396_a(this.field_48208_a, 5, 4);
			if(vec3D1 == null) {
				return false;
			} else {
				this.field_48207_c = vec3D1.xCoord;
				this.field_48204_d = vec3D1.yCoord;
				this.field_48205_e = vec3D1.zCoord;
				return true;
			}
		}
	}

	public void startExecuting() {
		this.field_48208_a.getNavigator().func_48658_a(this.field_48207_c, this.field_48204_d, this.field_48205_e, this.field_48206_b);
	}

	public boolean continueExecuting() {
		return !this.field_48208_a.getNavigator().noPath();
	}
}
