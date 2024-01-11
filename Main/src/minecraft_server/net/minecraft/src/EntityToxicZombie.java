package net.minecraft.src;

import com.mojontwins.minecraft.entity.status.Status;
import com.mojontwins.minecraft.entity.status.StatusEffect;

public class EntityToxicZombie extends EntityCityHusk {

	public EntityToxicZombie(World world) {
		super(world);
		this.texture = "/mob/toxiczombie1.png";
		this.moveSpeed = 0.6F;
		this.attackStrength = 2;
		this.scoreValue = 20;
	}
	
	protected int getMaxTextureVariations() {
		return 2;
	}
	
	public void onLivingUpdate() {
		this.worldObj.spawnParticle(
			"status_effect",
			posX + (this.rand.nextFloat() - 0.5F) * (float) width, 
			(posY + this.rand.nextFloat() * (float) height) - (float) yOffset, 
			posZ + (this.rand.nextFloat() - 0.5F) * (float) width,
			0.439F,
			0.705F,
			0.2F
		   );
		
		super.onLivingUpdate();
	}
	
	protected void attackEntity(Entity entity, float distance) {
		if((double)distance < 2.0D && entity.boundingBox.maxY > this.boundingBox.minY && entity.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			entity.attackEntityFrom(this, this.attackStrength);
			if(entity instanceof EntityLiving) {
				if(!((EntityLiving)entity).isStatusActive(Status.statusPoisoned)) {
					((EntityLiving)entity).addStatusEffect(new StatusEffect(Status.statusPoisoned.id, 100, 1));
				}
			}
		}

	}
}
