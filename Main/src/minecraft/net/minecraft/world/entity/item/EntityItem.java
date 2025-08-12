package net.minecraft.world.entity.item;

import net.minecraft.src.AchievementList;
import net.minecraft.src.Block;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Material;

public class EntityItem extends Entity {
	public ItemStack item;
	public int age = 0;
	public int delayBeforeCanPickup;
	private int health = 5;
	public float baseRenderingAngle = (float)(Math.random() * Math.PI * 2.0D);

	public EntityItem(World world1, double d2, double d4, double d6, ItemStack itemStack8) {
		super(world1);
		this.setSize(0.25F, 0.25F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(d2, d4, d6);
		this.item = itemStack8;
		this.rotationYaw = (float)(Math.random() * 360.0D);
		this.motionX = (double)((float)(Math.random() * (double)0.2F - (double)0.1F));
		this.motionY = (double)0.2F;
		this.motionZ = (double)((float)(Math.random() * (double)0.2F - (double)0.1F));
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	public EntityItem(World world1) {
		super(world1);
		this.setSize(0.25F, 0.25F);
		this.yOffset = this.height / 2.0F;
	}

	protected void entityInit() {
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.delayBeforeCanPickup > 0) {
			--this.delayBeforeCanPickup;
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= (double)0.04F;
		
		Material material = this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
		
		if(
				material == Material.acid
		) {
			this.worldObj.playSoundAtEntity(this, "random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
			for(int i = 0; i < 7; ++i) {
				this.worldObj.spawnParticle("largesmoke", 
					this.posX + this.rand.nextDouble (), 
					this.posY + this.rand.nextDouble (),
					this.posZ + this.rand.nextDouble (),
					0.0, 0.0, 0.0);
			}
			this.setEntityDead();
		}
		
		if(
				material == Material.lava
			) {
			this.motionY = (double)0.2F;
			this.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			this.worldObj.playSoundAtEntity(this, "random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
		}

		this.pushOutOfBlocks(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ);
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		float f1 = 0.98F;
		if(this.onGround) {
			f1 = 0.58800006F;
			int i2 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
			if(i2 > 0) {
				Block block =  Block.blocksList[i2];
				f1 = block == null ? 0 : block.slipperiness * 0.98F;
			}
		}

		this.motionX *= (double)f1;
		this.motionY *= (double)0.98F;
		this.motionZ *= (double)f1;
		if(this.onGround) {
			this.motionY *= -0.5D;
		}

		++this.age;
		if(this.age >= 6000) {
			this.setEntityDead();
		}

	}

	public boolean handleWaterMovement() {
		return this.worldObj.handleMaterialAcceleration(this.boundingBox, Material.water, this) ||
				this.worldObj.handleMaterialAcceleration(this.boundingBox, Material.acid, this);
	}

	protected void dealFireDamage(int i1) {
		this.attackEntityFrom((Entity)null, i1);
	}

	public boolean attackEntityFrom(Entity entity1, int i2) {
		this.setBeenAttacked();
		this.health -= i2;
		if(this.health <= 0) {
			this.setEntityDead();
		}

		return false;
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		nBTTagCompound1.setShort("Health", (short)((byte)this.health));
		nBTTagCompound1.setShort("Age", (short)this.age);
		nBTTagCompound1.setCompoundTag("Item", this.item.writeToNBT(new NBTTagCompound()));
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		this.health = nBTTagCompound1.getShort("Health") & 255;
		this.age = nBTTagCompound1.getShort("Age");
		NBTTagCompound nBTTagCompound2 = nBTTagCompound1.getCompoundTag("Item");
		this.item = new ItemStack(nBTTagCompound2);
	}

	public void onCollideWithPlayer(EntityPlayer entityPlayer1) {
		if(!this.worldObj.isRemote) {
			int i2 = this.item.stackSize;
			if(this.delayBeforeCanPickup == 0 && entityPlayer1.inventory.addItemStackToInventory(this.item)) {
				if(this.item.itemID == Block.wood.blockID) {
					entityPlayer1.triggerAchievement(AchievementList.mineWood);
				}

				if(this.item.itemID == Item.leather.shiftedIndex) {
					entityPlayer1.triggerAchievement(AchievementList.killCow);
				}
				
				if(this.item.itemID == Item.diamond.shiftedIndex) {
					entityPlayer1.triggerAchievement(AchievementList.diamonds);
				}
				
				if(this.item.itemID == Item.ruby.shiftedIndex || this.item.itemID == Item.emerald.shiftedIndex) {
					entityPlayer1.triggerAchievement(AchievementList.currency);
				}
				
				if(this.item.itemID == Block.cryingObsidian.blockID) {
					entityPlayer1.triggerAchievement(AchievementList.cryingObsidian);
				}
				
				if(this.item.itemID == Item.rottenFlesh.shiftedIndex) {
					entityPlayer1.triggerAchievement(AchievementList.zombieMeat);
				}

				this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				entityPlayer1.onItemPickup(this, i2);
				if(this.item.stackSize <= 0) {
					this.setEntityDead();
				}
			}

		}
	}
	
	public void makeOld() {
		this.age = 4800;
	}
}
