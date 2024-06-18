package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityAIMoveThroughVillage extends EntityAIBase {
	private EntityCreature theEntity;
	private float field_48281_b;
	private PathEntity field_48282_c;
	private VillageDoorInfo doorInfo;
	private boolean field_48280_e;
	private List doorList = new ArrayList();

	public EntityAIMoveThroughVillage(EntityCreature entityCreature1, float f2, boolean z3) {
		this.theEntity = entityCreature1;
		this.field_48281_b = f2;
		this.field_48280_e = z3;
		this.setMutexBits(1);
	}

	public boolean shouldExecute() {
		this.func_48277_f();
		if(this.field_48280_e && this.theEntity.worldObj.isDaytime()) {
			return false;
		} else {
			Village village1 = this.theEntity.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(this.theEntity.posX), MathHelper.floor_double(this.theEntity.posY), MathHelper.floor_double(this.theEntity.posZ), 0);
			if(village1 == null) {
				return false;
			} else {
				this.doorInfo = this.func_48276_a(village1);
				if(this.doorInfo == null) {
					return false;
				} else {
					boolean z2 = this.theEntity.getNavigator().func_48657_b();
					this.theEntity.getNavigator().setBreakDoors(false);
					this.field_48282_c = this.theEntity.getNavigator().func_48650_a((double)this.doorInfo.posX, (double)this.doorInfo.posY, (double)this.doorInfo.posZ);
					this.theEntity.getNavigator().setBreakDoors(z2);
					if(this.field_48282_c != null) {
						return true;
					} else {
						Vec3D vec3D3 = RandomPositionGenerator.func_48395_a(this.theEntity, 10, 7, Vec3D.createVector((double)this.doorInfo.posX, (double)this.doorInfo.posY, (double)this.doorInfo.posZ));
						if(vec3D3 == null) {
							return false;
						} else {
							this.theEntity.getNavigator().setBreakDoors(false);
							this.field_48282_c = this.theEntity.getNavigator().func_48650_a(vec3D3.xCoord, vec3D3.yCoord, vec3D3.zCoord);
							this.theEntity.getNavigator().setBreakDoors(z2);
							return this.field_48282_c != null;
						}
					}
				}
			}
		}
	}

	public boolean continueExecuting() {
		if(this.theEntity.getNavigator().noPath()) {
			return false;
		} else {
			float f1 = this.theEntity.width + 4.0F;
			return this.theEntity.getDistanceSq((double)this.doorInfo.posX, (double)this.doorInfo.posY, (double)this.doorInfo.posZ) > (double)(f1 * f1);
		}
	}

	public void startExecuting() {
		this.theEntity.getNavigator().setPath(this.field_48282_c, this.field_48281_b);
	}

	public void resetTask() {
		if(this.theEntity.getNavigator().noPath() || this.theEntity.getDistanceSq((double)this.doorInfo.posX, (double)this.doorInfo.posY, (double)this.doorInfo.posZ) < 16.0D) {
			this.doorList.add(this.doorInfo);
		}

	}

	private VillageDoorInfo func_48276_a(Village village1) {
		VillageDoorInfo villageDoorInfo2 = null;
		int i3 = Integer.MAX_VALUE;
		List list4 = village1.getVillageDoorInfoList();
		Iterator iterator5 = list4.iterator();

		while(iterator5.hasNext()) {
			VillageDoorInfo villageDoorInfo6 = (VillageDoorInfo)iterator5.next();
			int i7 = villageDoorInfo6.getDistanceSquared(MathHelper.floor_double(this.theEntity.posX), MathHelper.floor_double(this.theEntity.posY), MathHelper.floor_double(this.theEntity.posZ));
			if(i7 < i3 && !this.func_48275_a(villageDoorInfo6)) {
				villageDoorInfo2 = villageDoorInfo6;
				i3 = i7;
			}
		}

		return villageDoorInfo2;
	}

	private boolean func_48275_a(VillageDoorInfo villageDoorInfo1) {
		Iterator iterator2 = this.doorList.iterator();

		VillageDoorInfo villageDoorInfo3;
		do {
			if(!iterator2.hasNext()) {
				return false;
			}

			villageDoorInfo3 = (VillageDoorInfo)iterator2.next();
		} while(villageDoorInfo1.posX != villageDoorInfo3.posX || villageDoorInfo1.posY != villageDoorInfo3.posY || villageDoorInfo1.posZ != villageDoorInfo3.posZ);

		return true;
	}

	private void func_48277_f() {
		if(this.doorList.size() > 15) {
			this.doorList.remove(0);
		}

	}
}
