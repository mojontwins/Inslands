package net.minecraft.src;

public class EntityAIWander extends EntityAIBase {
	private EntityCreature entity;
	private double field_46102_b;
	private double field_46103_c;
	private double field_46101_d;
	private float field_48209_e;

	public EntityAIWander(EntityCreature entityCreature1, float f2) {
		this.entity = entityCreature1;
		this.field_48209_e = f2;
		this.setMutexBits(1);
	}

	public boolean shouldExecute() {
		if(this.entity.getAge() >= 100) {
			return false;
		} else if(this.entity.getRNG().nextInt(120) != 0) {
			return false;
		} else {
			Vec3D vec3D1 = RandomPositionGenerator.func_48396_a(this.entity, 10, 7);
			if(vec3D1 == null) {
				return false;
			} else {
				this.field_46102_b = vec3D1.xCoord;
				this.field_46103_c = vec3D1.yCoord;
				this.field_46101_d = vec3D1.zCoord;
				return true;
			}
		}
	}

	public boolean continueExecuting() {
		return !this.entity.getNavigator().noPath();
	}

	public void startExecuting() {
		this.entity.getNavigator().func_48658_a(this.field_46102_b, this.field_46103_c, this.field_46101_d, this.field_48209_e);
	}
}
