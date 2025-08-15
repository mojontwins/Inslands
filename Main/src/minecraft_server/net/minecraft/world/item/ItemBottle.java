package net.minecraft.world.item;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.entity.projectile.EntityThrowableBottle;
import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.phys.EnumMovingObjectType;
import net.minecraft.world.phys.MovingObjectPosition;
import net.minecraft.world.phys.Vec3D;

public class ItemBottle extends Item {
	public final int contents;
	public final int damage;

	public ItemBottle(int shiftedIndex, int contents, int damage) {
		super(shiftedIndex);
		this.contents = contents;
		this.damage = damage;
	}

	@Override 
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer thePlayer) {
		float d = 1.0F;
		float pitch = thePlayer.prevRotationPitch + (thePlayer.rotationPitch - thePlayer.prevRotationPitch) * d;
		float yaw = thePlayer.prevRotationYaw + (thePlayer.rotationYaw - thePlayer.prevRotationYaw) * d;
		
		double posX = thePlayer.prevPosX + (thePlayer.posX - thePlayer.prevPosX) * (double)d;
		double posY = thePlayer.prevPosY + (thePlayer.posY - thePlayer.prevPosY) * (double)d + 1.62D - (double)thePlayer.yOffset;
		double posZ = thePlayer.prevPosZ + (thePlayer.posZ - thePlayer.prevPosZ) * (double)d;
		
		Vec3D vector = Vec3D.createVector(posX, posY, posZ);
		
		float f14 = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
		float f15 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
		float f16 = -MathHelper.cos(-pitch * 0.017453292F);
		float dy = MathHelper.sin(-pitch * 0.017453292F);
		float dx = f15 * f16;
		float dz = f14 * f16;
		double reach = 5.0D;
		
		Vec3D vectorDest = vector.addVector((double)dx * reach, (double)dy * reach, (double)dz * reach);
		
		MovingObjectPosition movingObjectPosition = world.rayTraceBlocks(vector, vectorDest, this.contents == 0);
		
		if(movingObjectPosition != null && movingObjectPosition.typeOfHit == EnumMovingObjectType.TILE) {
			int x = movingObjectPosition.blockX;
			int y = movingObjectPosition.blockY;
			int z = movingObjectPosition.blockZ;
			
			int blockID = world.getBlockId(x, y, z);
			int metadata = world.getBlockMetadata(x, y, z);

			if(this.contents == 0) {
				
				// Bottle is empty
				
				if(blockID == Block.cauldron.blockID) {
					// Use on cauldron
					
					switch(metadata >> 2) {
					case 1: itemStack = new ItemStack(Item.bottleWater); break;
					case 2: itemStack = new ItemStack(Item.bottleAcid); break;
					case 3: itemStack = new ItemStack(Item.bottleSoup); break;
					case 4: itemStack = new ItemStack(Item.bottleGoo); break;
					case 5: itemStack = new ItemStack(Item.bottlePoison); break;
					}
					
					world.setBlockAndMetadataWithNotify(x, y, z, blockID, metadata & 3);
					
					return itemStack;
				} else if(blockID == Block.waterStill.blockID) {
					// Use on water
					world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, "random.splash", world.rand.nextFloat() * 0.25F + 0.75F,  world.rand.nextFloat() + 0.5F);
	            	
	        		// Substitute the hit block with air
	        		world.setBlockWithNotify(x, y, z, 0);
	        		
	        		// Replace this item with a filled bottle
	        		return new ItemStack (Item.bottleWater);
				} else if(blockID == Block.acidStill.blockID) {
					// Use on acidr
					world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, "random.splash", world.rand.nextFloat() * 0.25F + 0.75F,  world.rand.nextFloat() + 0.5F);
	            	
	        		// Substitute the hit block with air
	        		world.setBlockWithNotify(x, y, z, 0);
	        		
	        		// Replace this item with a filled bottle
	        		return new ItemStack (Item.bottleAcid);
				}
				
			} else {
				
				// Use full bottle on cauldron
				
				if(blockID == Block.cauldron.blockID) {
					if(metadata >> 2 == 0) {
						// Cauldron is empty
						
						world.setBlockAndMetadataWithNotify(x, y, z, blockID, metadata | this.contents << 2);
						world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, "random.splash", world.rand.nextFloat() * 0.25F + 0.75F,  world.rand.nextFloat() + 0.5F);
						return new ItemStack(Item.bottleEmpty);
					}
				}
			}
			
			return itemStack;
		}
		
		if(this.contents == 3) {
			thePlayer.setItemInUse(itemStack, 32);
			return itemStack;
			
		}
		
		// Gotten to this point, throw the bottle
		
		if(thePlayer.isCreative || thePlayer.inventory.consumeInventoryItem(Item.pebble.shiftedIndex)) {
			world.playSoundAtEntity(thePlayer, "random.bow", 1.0F, 1.0F / (Item.rand.nextFloat() * 0.4F + 0.8F));
			if(!world.isRemote) {
				world.spawnEntityInWorld(new EntityThrowableBottle(world, thePlayer, this));
			}
		}
		
		return itemStack;
	}
	
}
