package net.minecraft.src;

import java.util.List;

public class EntityAIAvoidEntity extends EntityAIBase {
	private EntityCreature theEntity;
	private float field_48235_b;
	private float field_48236_c;
	private Entity field_48233_d;
	private float field_48234_e;
	private PathEntity field_48231_f;
	private PathNavigate entityPathNavigate;
	private Class targetEntityClass;

	public EntityAIAvoidEntity(EntityCreature entityCreature1, Class class2, float f3, float f4, float f5) {
		this.theEntity = entityCreature1;
		this.targetEntityClass = class2;
		this.field_48234_e = f3;
		this.field_48235_b = f4;
		this.field_48236_c = f5;
		this.entityPathNavigate = entityCreature1.getNavigator();
		this.setMutexBits(1);
	}

	public boolean shouldExecute() {
		if(this.targetEntityClass == EntityPlayer.class) {
			if(this.theEntity instanceof EntityTameable && ((EntityTameable)this.theEntity).isTamed()) {
				return false;
			}

			this.field_48233_d = this.theEntity.worldObj.getClosestPlayerToEntity(this.theEntity, (double)this.field_48234_e);
			if(this.field_48233_d == null) {
				return false;
			}
		} else {
			List list1 = this.theEntity.worldObj.getEntitiesWithinAABB(this.targetEntityClass, this.theEntity.boundingBox.expand((double)this.field_48234_e, 3.0D, (double)this.field_48234_e));
			if(list1.size() == 0) {
				return false;
			}

			this.field_48233_d = (Entity)list1.get(0);
		}

		if(!this.theEntity.func_48318_al().canSee(this.field_48233_d)) {
			return false;
		} else {
			Vec3D vec3D2 = RandomPositionGenerator.func_48394_b(this.theEntity, 16, 7, Vec3D.createVector(this.field_48233_d.posX, this.field_48233_d.posY, this.field_48233_d.posZ));
			if(vec3D2 == null) {
				return false;
			} else if(this.field_48233_d.getDistanceSq(vec3D2.xCoord, vec3D2.yCoord, vec3D2.zCoord) < this.field_48233_d.getDistanceSqToEntity(this.theEntity)) {
				return false;
			} else {
				this.field_48231_f = this.entityPathNavigate.func_48650_a(vec3D2.xCoord, vec3D2.yCoord, vec3D2.zCoord);
				return this.field_48231_f == null ? false : this.field_48231_f.func_48426_a(vec3D2);
			}
		}
	}

	public boolean continueExecuting() {
		return !this.entityPathNavigate.noPath();
	}

	public void startExecuting() {
		this.entityPathNavigate.setPath(this.field_48231_f, this.field_48235_b);
	}

	public void resetTask() {
		this.field_48233_d = null;
	}

	public void updateTask() {
		if(this.theEntity.getDistanceSqToEntity(this.field_48233_d) < 49.0D) {
			this.theEntity.getNavigator().func_48654_a(this.field_48236_c);
		} else {
			this.theEntity.getNavigator().func_48654_a(this.field_48235_b);
		}

	}
}
