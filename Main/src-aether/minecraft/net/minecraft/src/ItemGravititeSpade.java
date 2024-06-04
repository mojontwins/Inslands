package net.minecraft.src;

import java.util.Random;

public class ItemGravititeSpade extends ItemTool {
	private static Block[] blocksEffectiveAgainst = new Block[]{AetherBlocks.Quicksoil, AetherBlocks.Dirt, AetherBlocks.Grass, AetherBlocks.Aercloud};
	private static Random random = new Random();

	public ItemGravititeSpade(int i, EnumToolMaterial enumtoolmaterial) {
		super(i, 1, enumtoolmaterial, blocksEffectiveAgainst);
	}

	public boolean canHarvestBlock(Block block) {
		for(int i = 0; i < blocksEffectiveAgainst.length; ++i) {
			if(blocksEffectiveAgainst[i].blockID == block.blockID) {
				return true;
			}
		}

		return false;
	}

	public ToolBase getToolBase() {
		return ToolBase.Shovel;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		float f1 = entityplayer.rotationPitch;
		float f2 = entityplayer.rotationYaw;
		double d = entityplayer.posX;
		double d1 = entityplayer.posY + 1.62D - (double)entityplayer.yOffset;
		double d2 = entityplayer.posZ;
		Vec3D vec3d = Vec3D.createVector(d, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.01745329F - 3.141593F);
		float f4 = MathHelper.sin(-f2 * 0.01745329F - 3.141593F);
		float f5 = -MathHelper.cos(-f1 * 0.01745329F);
		float f6 = MathHelper.sin(-f1 * 0.01745329F);
		float f7 = f4 * f5;
		float f9 = f3 * f5;
		double d3 = 5.0D;
		Vec3D vec3d1 = vec3d.addVector((double)f7 * d3, (double)f6 * d3, (double)f9 * d3);
		MovingObjectPosition movingobjectposition = world.rayTraceBlocks_do(vec3d, vec3d1, false);
		if(movingobjectposition == null) {
			return itemstack;
		} else {
			if(movingobjectposition.typeOfHit == EnumMovingObjectType.TILE) {
				int i = movingobjectposition.blockX;
				int j = movingobjectposition.blockY;
				int k = movingobjectposition.blockZ;
				if(!world.multiplayerWorld) {
					int blockID = world.getBlockId(i, j, k);
					int metadata = world.getBlockMetadata(i, j, k);

					for(int n = 0; n < blocksEffectiveAgainst.length; ++n) {
						if(blockID == blocksEffectiveAgainst[n].blockID) {
							if(blockID == AetherBlocks.Grass.blockID) {
								blockID = AetherBlocks.Dirt.blockID;
							}

							EntityFloatingBlock floating = new EntityFloatingBlock(world, (double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), blockID, metadata);
							world.entityJoinedWorld(floating);
						}
					}
				}

				itemstack.damageItem(4, entityplayer);
			}

			return itemstack;
		}
	}
}
