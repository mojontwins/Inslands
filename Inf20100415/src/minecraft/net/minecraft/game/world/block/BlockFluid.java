package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class BlockFluid extends Block {
	protected int stillId;
	protected int movingId;

	protected BlockFluid(int i1, Material material2) {
		super(i1, material2);
		this.blockIndexInTexture = 14;
		if(material2 == Material.lava) {
			this.blockIndexInTexture = 30;
		}

		Block.isBlockContainer[i1] = true;
		this.movingId = i1;
		this.stillId = i1 + 1;
		this.setBlockBounds(0.01F, -0.09F, 0.01F, 1.01F, 0.90999997F, 1.01F);
		this.setTickOnLoad(true);
		this.setResistance(2.0F);
	}

	public final int getBlockTextureFromSide(int i1) {
		return this.blockMaterial == Material.lava ? this.blockIndexInTexture : (i1 == 1 ? this.blockIndexInTexture : (i1 == 0 ? this.blockIndexInTexture : this.blockIndexInTexture + 32));
	}

	public final boolean renderAsNormalBlock() {
		return false;
	}

	public void onBlockAdded(World world1, int i2, int i3, int i4) {
		world1.scheduleBlockUpdate(i2, i3, i4, this.movingId);
	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
		this.update(world1, i2, i3, i4, 0);
	}

	public boolean update(World world1, int i2, int i3, int i4, int i5) {
		boolean z7 = false;
		liquidSolidCheck(world1, i2, i3, i4);

		boolean z6;
		do {
			--i3;
			if(!this.canFlow(world1, i2, i3, i4)) {
				break;
			}

			liquidSolidCheck(world1, i2, i3, i4);
			if(z6 = world1.setBlockWithNotify(i2, i3, i4, this.movingId)) {
				z7 = true;
			}
		} while(z6 && this.blockMaterial != Material.lava);

		++i3;
		if(this.blockMaterial == Material.water || !z7) {
			z7 = z7 | this.flow(world1, i2 - 1, i3, i4) | this.flow(world1, i2 + 1, i3, i4) | this.flow(world1, i2, i3, i4 - 1) | this.flow(world1, i2, i3, i4 + 1);
		}

		if(this.blockMaterial == Material.lava) {
			z7 = z7 | extinguishFireLava(world1, i2 - 1, i3, i4) | extinguishFireLava(world1, i2 + 1, i3, i4) | extinguishFireLava(world1, i2, i3, i4 - 1) | extinguishFireLava(world1, i2, i3, i4 + 1);
		}

		if(!z7) {
			world1.setTileNoUpdate(i2, i3, i4, this.stillId);
		} else {
			world1.scheduleBlockUpdate(i2, i3, i4, this.movingId);
		}

		return z7;
	}

	private static boolean liquidSolidCheck(World world0, int i1, int i2, int i3) {
		return world0.getBlockMaterial(i1, i2 - 1, i3).liquidSolidCheck();
	}

	protected final boolean canFlow(World world1, int i2, int i3, int i4) {
		if(!world1.getBlockMaterial(i2, i3, i4).liquidSolidCheck()) {
			return false;
		} else {
			if(this.blockMaterial == Material.water) {
				for(int i5 = i2 - 2; i5 <= i2 + 2; ++i5) {
					for(int i6 = i3 - 2; i6 <= i3 + 2; ++i6) {
						for(int i7 = i4 - 2; i7 <= i4 + 2; ++i7) {
							if(world1.getBlockId(i5, i6, i7) == Block.sponge.blockID) {
								return false;
							}
						}
					}
				}
			}

			return true;
		}
	}

	private static boolean extinguishFireLava(World world0, int i1, int i2, int i3) {
		if(Block.fire.getChanceOfNeighborsEncouragingFire(world0.getBlockId(i1, i2, i3))) {
			Block.fire.fireSpread(world0, i1, i2, i3);
			return true;
		} else {
			return false;
		}
	}

	private boolean flow(World world1, int i2, int i3, int i4) {
		if(!this.canFlow(world1, i2, i3, i4)) {
			return false;
		} else {
			if(world1.setBlockWithNotify(i2, i3, i4, this.movingId)) {
				world1.scheduleBlockUpdate(i2, i3, i4, this.movingId);
			}

			return false;
		}
	}

	public final float getBlockBrightness(World world1, int i2, int i3, int i4) {
		return this.blockMaterial == Material.lava ? 100.0F : super.getBlockBrightness(world1, i2, i3, i4);
	}

	public boolean shouldSideBeRendered(World world1, int i2, int i3, int i4, int i5) {
		int i6;
		return (i6 = world1.getBlockId(i2, i3, i4)) != this.movingId && i6 != this.stillId ? (i5 != 1 || world1.getBlockId(i2 - 1, i3, i4) != 0 && world1.getBlockId(i2 + 1, i3, i4) != 0 && world1.getBlockId(i2, i3, i4 - 1) != 0 && world1.getBlockId(i2, i3, i4 + 1) != 0 ? super.shouldSideBeRendered(world1, i2, i3, i4, i5) : true) : false;
	}

	public boolean isCollidable() {
		return false;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(int i1, int i2, int i3) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
		if(i5 != 0) {
			Material material6 = Block.blocksList[i5].blockMaterial;
			if(this.blockMaterial == Material.water && material6 == Material.lava || material6 == Material.water && this.blockMaterial == Material.lava) {
				world1.setBlockWithNotify(i2, i3, i4, Block.stone.blockID);
			}
		}

		world1.scheduleBlockUpdate(i2, i3, i4, this.blockID);
	}

	public int tickRate() {
		return this.blockMaterial == Material.lava ? 25 : 5;
	}

	public int quantityDropped(Random random1) {
		return 0;
	}

	public int getRenderBlockPass() {
		return this.blockMaterial == Material.water ? 1 : 0;
	}

	public final void randomDisplayTick(World world1, int i2, int i3, int i4, Random random5) {
		if(random5.nextInt(128) == -1 && world1.getBlockMaterial(i2, i3 + 1, i4).getIsSolid()) {
			if(this.blockMaterial == Material.lava) {
				world1.playSoundEffect((double)((float)i2 + 0.5F), (double)((float)i3 + 0.5F), (double)((float)i4 + 0.5F), "liquid.lava", random5.nextFloat() * 0.25F + 0.75F, random5.nextFloat() * 0.5F + 0.3F);
			}

			if(this.blockMaterial == Material.water) {
				world1.playSoundEffect((double)((float)i2 + 0.5F), (double)((float)i3 + 0.5F), (double)((float)i4 + 0.5F), "liquid.water", random5.nextFloat() * 0.25F + 0.75F, random5.nextFloat() + 0.5F);
			}
		}

		if(this.blockMaterial == Material.lava && world1.getBlockMaterial(i2, i3 + 1, i4) == Material.air && !world1.isSolid(i2, i3 + 1, i4) && random5.nextInt(100) == 0) {
			double d6 = (double)((float)i2 + random5.nextFloat());
			double d8 = (double)i3 + this.maxY;
			double d10 = (double)((float)i4 + random5.nextFloat());
			world1.spawnParticle("lava", d6, d8, d10, 0.0D, 0.0D, 0.0D);
		}

		if(this.blockMaterial == Material.water) {
			int i7;
			if(liquidAirCheck(world1, i2 + 1, i3, i4)) {
				for(i7 = 0; i7 < 4; ++i7) {
					world1.spawnParticle("splash", (double)((float)(i2 + 1) + 0.125F), (double)i3, (double)((float)i4 + random5.nextFloat()), 0.0D, 0.0D, 0.0D);
				}
			}

			if(liquidAirCheck(world1, i2 - 1, i3, i4)) {
				for(i7 = 0; i7 < 4; ++i7) {
					world1.spawnParticle("splash", (double)((float)i2 - 0.125F), (double)i3, (double)((float)i4 + random5.nextFloat()), 0.0D, 0.0D, 0.0D);
				}
			}

			if(liquidAirCheck(world1, i2, i3, i4 + 1)) {
				for(i7 = 0; i7 < 4; ++i7) {
					world1.spawnParticle("splash", (double)((float)i2 + random5.nextFloat()), (double)i3, (double)((float)(i4 + 1) + 0.125F), 0.0D, 0.0D, 0.0D);
				}
			}

			if(liquidAirCheck(world1, i2, i3, i4 - 1)) {
				for(i7 = 0; i7 < 4; ++i7) {
					world1.spawnParticle("splash", (double)((float)i2 + random5.nextFloat()), (double)i3, (double)((float)i4 - 0.125F), 0.0D, 0.0D, 0.0D);
				}
			}
		}

	}

	private static boolean liquidAirCheck(World world0, int i1, int i2, int i3) {
		Material material4 = world0.getBlockMaterial(i1, i2, i3);
		Material material5 = world0.getBlockMaterial(i1, i2 - 1, i3);
		return !material4.getIsSolid() && !material4.getIsLiquid() ? material5.getIsSolid() || material5.getIsLiquid() : false;
	}
}