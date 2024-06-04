package net.minecraft.src;

import java.util.List;

public class EntityDartPoison extends EntityDartGolden {
	public EntityLiving victim;
	public int poisonTime;
	public static int texfxindex = 94;

	public EntityDartPoison(World world) {
		super(world);
	}

	public EntityDartPoison(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityDartPoison(World world, EntityLiving ent) {
		super(world, ent);
	}

	public void entityInit() {
		super.entityInit();
		this.item = new ItemStack(AetherItems.Dart, 1, 1);
		this.dmg = 2;
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
					if(lr2 instanceof EntityDartPoison) {
						EntityDartPoison arr = (EntityDartPoison)lr2;
						if(arr.victim == ent) {
							arr.poisonTime = 500;
							arr.isDead = false;
							ent.attackEntityFrom(this.shooter, this.dmg);
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
				if(this.poisonTime % 50 == 0) {
					this.victim.attackEntityFrom(this.shooter, 1);
				}
			}

		}
	}
}
