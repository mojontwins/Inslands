package net.minecraft.src;

public class EntityAIMoveTwardsRestriction extends EntityAIBase {
	private EntityCreature theEntity;
	private double movePosX;
	private double movePosY;
	private double movePosZ;
	private float field_48149_e;

	public EntityAIMoveTwardsRestriction(EntityCreature entityCreature1, float f2) {
		this.theEntity = entityCreature1;
		this.field_48149_e = f2;
		this.setMutexBits(1);
	}

	public boolean shouldExecute() {
		if(this.theEntity.isWithinHomeDistanceCurrentPosition()) {
			return false;
		} else {
			ChunkCoordinates chunkCoordinates1 = this.theEntity.getHomePosition();
			Vec3D vec3D2 = RandomPositionGenerator.func_48395_a(this.theEntity, 16, 7, Vec3D.createVector((double)chunkCoordinates1.posX, (double)chunkCoordinates1.posY, (double)chunkCoordinates1.posZ));
			if(vec3D2 == null) {
				return false;
			} else {
				this.movePosX = vec3D2.xCoord;
				this.movePosY = vec3D2.yCoord;
				this.movePosZ = vec3D2.zCoord;
				return true;
			}
		}
	}

	public boolean continueExecuting() {
		return !this.theEntity.getNavigator().noPath();
	}

	public void startExecuting() {
		this.theEntity.getNavigator().func_48658_a(this.movePosX, this.movePosY, this.movePosZ, this.field_48149_e);
	}
}
