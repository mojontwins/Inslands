package net.minecraft.src;

import java.util.List;

public class EntityPoisonNeedle extends EntityProjectileBase {
	public EntityLiving victim;
	public int poisonTime;
	public static int texfxindex = 94;

	public EntityPoisonNeedle(World world) {
		super(world);
	}

	public EntityPoisonNeedle(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityPoisonNeedle(World world, EntityLiving ent) {
		super(world, ent);
	}

	public void entityInit() {
		super.entityInit();
		this.dmg = 0;
		this.speed = 1.5F;
	}

	public boolean handleWaterMovement() {
		return this.victim == null && super.handleWaterMovement();
	}

	public boolean onHitTarget(Entity entity) {
		if(entity instanceof EntityLiving && AetherPoison.canPoison(entity)) {
			EntityLiving ent = (EntityLiving)entity;
			if(ent instanceof EntityPlayerSP) {
				AetherPoison.afflictPoison();
				return super.onHitTarget(entity);
			} else {
				List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, ent.boundingBox.expand(2.0D, 2.0D, 2.0D));

				for(int i = 0; i < list.size(); ++i) {
					Entity lr2 = (Entity)list.get(i);
					if(lr2 instanceof EntityPoisonNeedle) {
						EntityPoisonNeedle arr = (EntityPoisonNeedle)lr2;
						if(arr.victim == ent) {
							arr.poisonTime = 500;
							arr.isDead = false;
							this.setEntityDead();
							return false;
						}
					}
				}

				this.victim = ent;
				ent.attackEntityFrom(this.shooter, this.dmg);
				this.poisonTime = 500;
				return false;
			}
		} else {
			return super.onHitTarget(entity);
		}
	}

	public void setEntityDead() {
		this.victim = null;
		super.setEntityDead();
	}

	public boolean onHitBlock() {
		return this.victim == null;
	}

	public boolean canBeShot(Entity ent) {
		return super.canBeShot(ent) && this.victim == null;
	}

	public void onUpdate() {
		super.onUpdate();
		if(!this.isDead) {
			if(this.victim != null) {
				if(this.victim.isDead || this.poisonTime == 0) {
					this.setEntityDead();
					return;
				}

				EntitySlimeFX fx = new EntitySlimeFX(this.worldObj, this.posX, this.posY, this.posZ, Item.slimeBall);
				fx.renderDistanceWeight = 10.0D;
				fx.particleTextureIndex = texfxindex;
				AetherPoison.mc.effectRenderer.addEffect(fx);
				this.isDead = false;
				this.inGround = false;
				this.posX = this.victim.posX;
				this.posY = this.victim.boundingBox.minY + (double)this.victim.height * 0.8D;
				this.posZ = this.victim.posZ;
				AetherPoison.distractEntity(this.victim);
				--this.poisonTime;
			}

		}
	}
}
