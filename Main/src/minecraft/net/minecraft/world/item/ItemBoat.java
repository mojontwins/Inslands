package net.minecraft.world.item;

import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Vec3D;
import net.minecraft.world.entity.item.EntityBoat;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.World;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.tile.Block;

public class ItemBoat extends Item {
	boolean fireResistant = false;
	
	public ItemBoat(int i1, boolean fireResistant) {
		super(i1);
		this.maxStackSize = 1;
		this.fireResistant = fireResistant;
		
		this.displayOnCreativeTab = CreativeTabs.tabTransport;
	}

	public ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		float f4 = 1.0F;
		float f5 = entityPlayer3.prevRotationPitch + (entityPlayer3.rotationPitch - entityPlayer3.prevRotationPitch) * f4;
		float f6 = entityPlayer3.prevRotationYaw + (entityPlayer3.rotationYaw - entityPlayer3.prevRotationYaw) * f4;
		double d7 = entityPlayer3.prevPosX + (entityPlayer3.posX - entityPlayer3.prevPosX) * (double)f4;
		double d9 = entityPlayer3.prevPosY + (entityPlayer3.posY - entityPlayer3.prevPosY) * (double)f4 + 1.62D - (double)entityPlayer3.yOffset;
		double d11 = entityPlayer3.prevPosZ + (entityPlayer3.posZ - entityPlayer3.prevPosZ) * (double)f4;
		Vec3D vec3D13 = Vec3D.createVector(d7, d9, d11);
		float f14 = MathHelper.cos(-f6 * 0.017453292F - (float)Math.PI);
		float f15 = MathHelper.sin(-f6 * 0.017453292F - (float)Math.PI);
		float f16 = -MathHelper.cos(-f5 * 0.017453292F);
		float f17 = MathHelper.sin(-f5 * 0.017453292F);
		float f18 = f15 * f16;
		float f20 = f14 * f16;
		double d21 = 5.0D;
		Vec3D vec3D23 = vec3D13.addVector((double)f18 * d21, (double)f17 * d21, (double)f20 * d21);
		MovingObjectPosition movingObjectPosition24 = world2.rayTraceBlocks(vec3D13, vec3D23, true);
		if(movingObjectPosition24 == null) {
			return itemStack1;
		} else {
			if(movingObjectPosition24.typeOfHit == EnumMovingObjectType.TILE) {
				int i25 = movingObjectPosition24.blockX;
				int i26 = movingObjectPosition24.blockY;
				int i27 = movingObjectPosition24.blockZ;
				if(!world2.isRemote) {
					if(world2.getBlockId(i25, i26, i27) == Block.snow.blockID) {
						--i26;
					}

					world2.spawnEntityInWorld(new EntityBoat(world2, i25 + 0.5D, i26 + 1.0D, i27 + 0.5D, this.fireResistant));
				}

				if(!entityPlayer3.isCreative) --itemStack1.stackSize;
			}

			return itemStack1;
		}
	}
}
