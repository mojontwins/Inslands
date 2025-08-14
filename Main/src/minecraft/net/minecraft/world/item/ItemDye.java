package net.minecraft.world.item;

import java.util.List;

import net.minecraft.src.AchievementList;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.animal.EntitySheep;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.World;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockAcorn;
import net.minecraft.world.level.tile.BlockCloth;
import net.minecraft.world.level.tile.BlockCrops;
import net.minecraft.world.level.tile.BlockSapling;

public class ItemDye extends Item {
	public static final String[] dyeColorNames = new String[]{"black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "lightBlue", "magenta", "orange", "white"};
	public static final int[] dyeColors = new int[]{1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 2651799, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320};

	public ItemDye(int i1) {
		super(i1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		
		this.displayOnCreativeTab = CreativeTabs.tabMaterials;
	}

	public int getIconFromDamage(int i1) {
		return this.iconIndex + i1 % 8 * 16 + i1 / 8;
	}

	public String getItemNameIS(ItemStack itemStack1) {
		return super.getItemName() + "." + dyeColorNames[itemStack1.getItemDamage()];
	}

	public boolean onItemUse(ItemStack itemStack, EntityPlayer thePlayer, World world, int x, int y, int z, int side) {
		if(itemStack.getItemDamage() == 15) {

			int blockID = world.getBlockId(x, y, z);
			if(blockID == Block.sapling.blockID) {
				if(!world.isRemote) {
					((BlockSapling)Block.sapling).growTree(world, x, y, z, world.rand);
					this.particles(world, "glowdust", x, y, z);
					if(!thePlayer.isCreative) --itemStack.stackSize;
				}

				return true;
			}
			
			if(blockID == Block.sapling2.blockID) {
				if(!world.isRemote) {
					((BlockSapling)Block.sapling2).growTree(world, x, y, z, world.rand);
					this.particles(world, "glowdust", x, y, z);
					if(!thePlayer.isCreative) --itemStack.stackSize;
				}

				return true;
			}

			if(blockID == Block.crops.blockID) {
				if(!world.isRemote) {
					((BlockCrops)Block.crops).fertilize(world, x, y, z);
					this.particles(world, "glowdust", x, y, z);
					if(!thePlayer.isCreative) --itemStack.stackSize;
				}

				return true;
			}
			
			if(blockID == Block.acorn.blockID) {
				if(!world.isRemote) {
					((BlockAcorn)Block.acorn).fertilize(world, x, y, z);
					this.particles(world, "glowdust", x, y, z);
					thePlayer.triggerAchievement(AchievementList.fertilizeAcorn);
					if(!thePlayer.isCreative) --itemStack.stackSize;
				}
			}
			
			if(blockID == Block.dirt.blockID) {
				if(!world.isRemote) {
					if (rand.nextInt(16) == 0) {
						// Produce grass!
						for(int attempts = 0; attempts < 32; ++attempts) {
							int xx = x;
							int yy = y;
							int zz = z;

							for(int i13 = 0; i13 < attempts / 16; ++i13) {
								xx += rand.nextInt(2) - 1;
								yy += (rand.nextInt(2) - 1) * rand.nextInt(3) / 2;
								zz += rand.nextInt(2) - 1;
								if(world.getBlockId(xx, yy, zz) == Block.dirt.blockID) {
									break;
								}
							}

							if(world.getBlockId(xx, yy, zz) == Block.dirt.blockID && !Block.opaqueCubeLookup[world.getBlockId(xx, yy + 1, zz)]) {
								world.setBlockWithNotify(xx, yy, zz, Block.grass.blockID);
								thePlayer.triggerAchievement(AchievementList.grassFromSoil);
								this.particles(world, "glowdust", x, y, z);
							}
						}
						
					} else if (rand.nextBoolean()) {
						// Produce some mushrooms
						label53b:
						for(int attempts = 0; attempts < 32; ++attempts) {
							int xx = x;
							int yy = y + 1;
							int zz = z;
	
							for(int i13 = 0; i13 < attempts / 16; ++i13) {
								xx += rand.nextInt(3) - 1;
								yy += (rand.nextInt(3) - 1) * rand.nextInt(3) / 2;
								zz += rand.nextInt(3) - 1;
								if(world.getBlockId(xx, yy - 1, zz) != Block.dirt.blockID || world.getBlockId(xx, yy, zz) != 0) {
									continue label53b;
								}
							}
	
							if(world.getBlockId(xx, yy, zz) == 0 && Block.mushroomRed.canBlockStay(world, xx, yy, zz)) {
								if(rand.nextInt(4) == 0) {
									world.setBlockWithNotify(xx, yy, zz, Block.mushroomRed.blockID);
									this.particles(world, "glowdust", xx, yy, zz);
								} else if(rand.nextInt(4) == 0) {
									world.setBlockWithNotify(xx, yy, zz, Block.mushroomBrown.blockID);
									this.particles(world, "glowdust", xx, yy, zz);
								} 
							}
						}
					} else {
						this.particles(world, "smoke", x, y, z);
					}
						
					if(!thePlayer.isCreative) --itemStack.stackSize;

					return true;
				}
			}


			if(blockID == Block.grass.blockID) {
				if(!world.isRemote) {
					if(!thePlayer.isCreative) --itemStack.stackSize;

					label53:
					for(int attempts = 0; attempts < 128; ++attempts) {
						int xx = x;
						int yy = y + 1;
						int zz = z;

						for(int i13 = 0; i13 < attempts / 16; ++i13) {
							xx += rand.nextInt(3) - 1;
							yy += (rand.nextInt(3) - 1) * rand.nextInt(3) / 2;
							zz += rand.nextInt(3) - 1;
							if(world.getBlockId(xx, yy - 1, zz) != Block.grass.blockID || world.isBlockNormalCube(xx, yy, zz)) {
								continue label53;
							}
						}

						if(world.getBlockId(xx, yy, zz) == 0) {
							if(rand.nextInt(10) != 0) {
								world.setBlockWithNotify(xx, yy, zz, Block.tallGrass.blockID);
							} else if(rand.nextInt(3) != 0) {
								world.setBlockWithNotify(xx, yy, zz, Block.plantYellow.blockID);
							} else {
								world.setBlockWithNotify(xx, yy, zz, Block.plantRed.blockID);
							}
							this.particles(world, "glowdust", xx, yy, zz);
						}
					}
				}

				return true;
			}
		}

		return false;
	}

	public void saddleEntity(ItemStack itemStack1, EntityLiving entityLiving2) {
		if(entityLiving2 instanceof EntitySheep) {
			EntitySheep entitySheep3 = (EntitySheep)entityLiving2;
			int i4 = BlockCloth.getBlockFromDye(itemStack1.getItemDamage());
			if(!entitySheep3.getSheared() && entitySheep3.getFleeceColor() != i4) {
				entitySheep3.setFleeceColor(i4);
				--itemStack1.stackSize;
			}
		}

	}
	
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int i = 0; i < dyeColors.length; i ++) {
			par3List.add(new ItemStack(par1, 1, i));
		}
	}
	
	public void particles(World world, String particle, int x, int y, int z) {
		for(int i = 0; i < 7; ++i) {
			world.spawnParticle(particle, 
				(double)x + world.rand.nextDouble (), 
				(double)y + world.rand.nextDouble (),
				(double)z + world.rand.nextDouble (),
				0.0, 0.0, 0.0);
		}
	}
}
