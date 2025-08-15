package net.minecraft.world.level.dimension;

import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldSize;
import net.minecraft.world.level.chunk.IChunkProvider;
import net.minecraft.world.level.tile.Block;

public class Teleporter {
	private Random rand = new Random();

	public void setExitLocation(World world1, Entity entity2) {
		if(!this.findExitLocation(world1, entity2)) {
			this.createExitLocation(world1, entity2);
			this.findExitLocation(world1, entity2);
		}
	}

	public boolean findExitLocation(World world, Entity entity) {
		IChunkProvider chunkProvider = world.getChunkProvider().getChunkProviderGenerate();
		short searchRange = 128;
		double distance = -1.0D;
		int x = 0;
		int y = 0;
		int z = 0;

		int x0 = MathHelper.floor_double(entity.posX);
		int z0 = MathHelper.floor_double(entity.posZ);

		double posYi;
		for(int xI = x0 - searchRange; xI <= x0 + searchRange; ++xI) {
			if(xI < WorldSize.getXChunkMinForRealBlocks(chunkProvider) || xI >= WorldSize.getXChunkMaxForRealBlocks(chunkProvider)) continue;
			double posXi = (double)xI + 0.5D - entity.posX;

			for(int zI = z0 - searchRange; zI <= z0 + searchRange; ++zI) {
				if(zI < WorldSize.getZChunkMinForRealBlocks(chunkProvider) || zI >= WorldSize.getZChunkMaxForRealBlocks(chunkProvider)) continue;
				double posZi = (double)zI + 0.5D - entity.posZ;

				for(int yI = 127; yI >= 0; --yI) {
					if(world.getBlockId(xI, yI, zI) == Block.portal.blockID) {
						while(world.getBlockId(xI, yI - 1, zI) == Block.portal.blockID) {
							--yI;
						}

						posYi = (double)yI + 0.5D - entity.posY;
						double curDistance = posXi * posXi + posYi * posYi + posZi * posZi;
						if(distance < 0.0D || curDistance < distance) {
							distance = curDistance;
							x = xI;
							y = yI;
							z = zI;
						}
					}
				}
			}
		}

		if(distance >= 0.0D) {
			double xPos = (double)x + 0.5D;
			double yPos = (double)y + 0.5D;
			double zPos = (double)z + 0.5D;
			if(world.getBlockId(x - 1, y, z) == Block.portal.blockID) {
				xPos -= 0.5D;
			}

			if(world.getBlockId(x + 1, y, z) == Block.portal.blockID) {
				xPos += 0.5D;
			}

			if(world.getBlockId(x, y, z - 1) == Block.portal.blockID) {
				zPos -= 0.5D;
			}

			if(world.getBlockId(x, y, z + 1) == Block.portal.blockID) {
				zPos += 0.5D;
			}

			entity.setLocationAndAngles(xPos, yPos, zPos, entity.rotationYaw, 0.0F);
			entity.motionX = entity.motionY = entity.motionZ = 0.0D;
			return true;
		} else {
			return false;
		}
	}

	public boolean createExitLocation(World world, Entity entity) {
		System.out.println ("Creating exit location");

		IChunkProvider chunkProvider = world.getChunkProvider().getChunkProviderGenerate();
		System.out.println ("Chunk provider is " + chunkProvider.makeString() + 
				", WorldSize real limits are (" + WorldSize.getXChunkMinForRealBlocks(chunkProvider) + ", " + WorldSize.getZChunkMinForRealBlocks(chunkProvider) + ") to " +
				"(" + WorldSize.getXChunkMaxForRealBlocks(chunkProvider) + ", " + WorldSize.getZChunkMaxForRealBlocks(chunkProvider) + ")");
		
		byte range = 16;
		double distance = -1.0D;
		int x0 = MathHelper.floor_double(entity.posX);
		int y0 = MathHelper.floor_double(entity.posY);
		int z0 = MathHelper.floor_double(entity.posZ);

		int x = x0;
		int y = y0;
		int z = z0;
		
		int i12 = 0;
		int i13 = this.rand.nextInt(4);

		int xI;
		double posXI;
		int zI;
		double posZI;
		int yI;
		int i21;
		int i22;
		int i23;
		int i24;
		int i25;
		int i26;
		int i27;
		int i28;
		double posYI;
		double curDistance;

		// New for limited levels: Make sure the search range fits in the level!
		int x1 = x0 - range; 
		int x2 = x0 + range;
		if(x1 < WorldSize.getXChunkMinForRealBlocks(chunkProvider)) {
			x1 = WorldSize.getXChunkMinForRealBlocks(chunkProvider);
			x2 = x1 + 2 * range;
		} else if(x2 >= WorldSize.getXChunkMaxForRealBlocks(chunkProvider)) {
			x2 = WorldSize.getXChunkMaxForRealBlocks(chunkProvider) - 1;
			x1 = x2 - 2 * range;
		}

		int z1 = z0 - range;
		int z2 = z0 + range;
		if(z1 < WorldSize.getZChunkMinForRealBlocks(chunkProvider)) {
			z1 = WorldSize.getZChunkMinForRealBlocks(chunkProvider);
			z2 = z1 + 2 * range;
		} else if(z2 >= WorldSize.getZChunkMaxForRealBlocks(chunkProvider)) {
			z2 = WorldSize.getZChunkMaxForRealBlocks(chunkProvider) - 1;
			z1 = z2 - 2 * range;
		}

		System.out.println ("Search range is XZ (" + x1 + ", " + z1 + ") to (" + x2 + ", " + z2 + ")");
		
		for(xI = x1; xI <= x2; ++xI) {
			posXI = (double)xI + 0.5D - entity.posX;

			for(zI = z1; zI <= z2; ++zI) {
				posZI = (double)zI + 0.5D - entity.posZ;

				label293:
				for(yI = 127; yI >= 0; --yI) {
					
					// Found air?
					if(world.isAirBlock(xI, yI, zI)) {
						
						// Down until floor
						while(yI > 0 && world.isAirBlock(xI, yI - 1, zI)) {
							--yI;
						}

						// Find somewhere to fit the portal
						for(i21 = i13; i21 < i13 + 4; ++i21) {
							i22 = i21 % 2;
							i23 = 1 - i22;
							if(i21 % 4 >= 2) {
								i22 = -i22;
								i23 = -i23;
							}

							for(i24 = 0; i24 < 3; ++i24) {
								for(i25 = 0; i25 < 4; ++i25) {
									for(i26 = -1; i26 < 4; ++i26) {
										i27 = xI + (i25 - 1) * i22 + i24 * i23;
										i28 = yI + i26;
										int i29 = zI + (i25 - 1) * i23 - i24 * i22;
										if(i26 < 0 && !world.getBlockMaterial(i27, i28, i29).isSolid() || i26 >= 0 && !world.isAirBlock(i27, i28, i29)) {
											continue label293;
										}
									}
								}
							}

							posYI = (double)yI + 0.5D - entity.posY;
							curDistance = posXI * posXI + posYI * posYI + posZI * posZI;
							if(distance < 0.0D || curDistance < distance) {
								distance = curDistance;
								x = xI;
								y = yI;
								z = zI;
								i12 = i21 % 4;
							}
						}
					}
				}
			}
		}

		if(distance < 0.0D) {
			for(xI = x1; xI <= x2; ++xI) {
				posXI = (double)xI + 0.5D - entity.posX;

				for(zI = z1; zI <= z2; ++zI) {
					posZI = (double)zI + 0.5D - entity.posZ;

					label231:
					for(yI = 127; yI >= 0; --yI) {
						if(world.isAirBlock(xI, yI, zI)) {
							while(world.isAirBlock(xI, yI - 1, zI)) {
								--yI;
							}

							for(i21 = i13; i21 < i13 + 2; ++i21) {
								i22 = i21 % 2;
								i23 = 1 - i22;

								for(i24 = 0; i24 < 4; ++i24) {
									for(i25 = -1; i25 < 4; ++i25) {
										i26 = xI + (i24 - 1) * i22;
										i27 = yI + i25;
										i28 = zI + (i24 - 1) * i23;
										if(i25 < 0 && !world.getBlockMaterial(i26, i27, i28).isSolid() || i25 >= 0 && !world.isAirBlock(i26, i27, i28)) {
											continue label231;
										}
									}
								}

								posYI = (double)yI + 0.5D - entity.posY;
								curDistance = posXI * posXI + posYI * posYI + posZI * posZI;
								if(distance < 0.0D || curDistance < distance) {
									distance = curDistance;
									x = xI;
									y = yI;
									z = zI;
									i12 = i21 % 2;
								}
							}
						}
					}
				}
			}
		}

		System.out.println ("Drawing portal in world");

		int i30 = x;
		int i16 = y;
		zI = z;
		int i31 = i12 % 2;
		int i19 = 1 - i31;
		if(i12 % 4 >= 2) {
			i31 = -i31;
			i19 = -i19;
		}

		boolean z34;
		if(distance < 0.0D) {
			if(y < 70) {
				y = 70;
			}

			if(y > 118) {
				y = 118;
			}

			i16 = y;

			for(yI = -1; yI <= 1; ++yI) {
				for(i21 = 1; i21 < 3; ++i21) {
					for(i22 = -1; i22 < 3; ++i22) {
						i23 = i30 + (i21 - 1) * i31 + yI * i19;
						i24 = i16 + i22;
						i25 = zI + (i21 - 1) * i19 - yI * i31;
						z34 = i22 < 0;
						world.setBlockWithNotify(i23, i24, i25, z34 ? Block.obsidian.blockID : 0);
					}
				}
			}
		}

		for(yI = 0; yI < 4; ++yI) {
			world.editingBlocks = true;

			for(i21 = 0; i21 < 4; ++i21) {
				for(i22 = -1; i22 < 4; ++i22) {
					i23 = i30 + (i21 - 1) * i31;
					i24 = i16 + i22;
					i25 = zI + (i21 - 1) * i19;
					z34 = i21 == 0 || i21 == 3 || i22 == -1 || i22 == 3;
					world.setBlockWithNotify(i23, i24, i25, z34 ? Block.obsidian.blockID : Block.portal.blockID);
				}
			}

			world.editingBlocks = false;

			for(i21 = 0; i21 < 4; ++i21) {
				for(i22 = -1; i22 < 4; ++i22) {
					i23 = i30 + (i21 - 1) * i31;
					i24 = i16 + i22;
					i25 = zI + (i21 - 1) * i19;
					world.notifyBlocksOfNeighborChange(i23, i24, i25, world.getBlockId(i23, i24, i25));
				}
			}
		}

		return true;
	}
}
