package net.minecraft.src;

public class MetadataChunkBlock {
	public final EnumSkyBlock enumSkyBlock;
	public int x1;
	public int y1;
	public int z1;
	public int x2;
	public int y2;
	public int z2;

	public MetadataChunkBlock(EnumSkyBlock enumSkyBlock1, int i2, int i3, int i4, int i5, int i6, int i7) {
		this.enumSkyBlock = enumSkyBlock1;
		this.x1 = i2;
		this.y1 = i3;
		this.z1 = i4;
		this.x2 = i5;
		this.y2 = i6;
		this.z2 = i7;
	}

	public void recalculateLighting(World world1) {
		int width = this.x2 - this.x1 + 1;
		int height = this.y2 - this.y1 + 1;
		int length = this.z2 - this.z1 + 1;
		int volume = width * height * length;
		if(volume > 32768) {
			System.out.println("Light too large, skipping!");
		} else {
			int lastXChunk = 0;
			int lastZChunk = 0;
			boolean z8 = false;
			boolean lastDoRelight = false;

			for(int x = this.x1; x <= this.x2; ++x) {
				for(int z = this.z1; z <= this.z2; ++z) {
					int xChunk = x >> 4;
					int zChunk = z >> 4;
					boolean doRelight = false;
					if(z8 && xChunk == lastXChunk && zChunk == lastZChunk) {
						doRelight = lastDoRelight;
					} else {
						doRelight = world1.doChunksNearChunkExist(x, 0, z, 1);
						if(doRelight) {
							Chunk chunk15 = world1.getChunkFromChunkCoords(x >> 4, z >> 4);
							if(chunk15.getIsChunkRendered()) {
								doRelight = false;
							}
						}

						lastDoRelight = doRelight;
						lastXChunk = xChunk;
						lastZChunk = zChunk;
					}

					if(doRelight) {
						if(this.y1 < 0) {
							this.y1 = 0;
						}

						if(this.y2 >= 128) {
							this.y2 = 127;
						}

						for(int y = this.y1; y <= this.y2; ++y) {
							int lightValue = world1.getSavedLightValue(this.enumSkyBlock, x, y, z);
							int blockID = world1.getBlockId(x, y, z);
							int lightOpacity = Block.lightOpacity[blockID];
							if(lightOpacity == 0) {
								lightOpacity = 1;
							}

							int i20 = 0;
							if(this.enumSkyBlock == EnumSkyBlock.Sky) {
								if(world1.canExistingBlockSeeTheSky(x, y, z)) {
									i20 = 15;
								}
							} else if(this.enumSkyBlock == EnumSkyBlock.Block) {
								i20 = Block.lightValue[blockID];
							}

							int lightWest;
							int newLightValue;
							if(lightOpacity >= 15 && i20 == 0) {
								newLightValue = 0;
							} else {
								lightWest = world1.getSavedLightValue(this.enumSkyBlock, x - 1, y, z);
								int lightEast = world1.getSavedLightValue(this.enumSkyBlock, x + 1, y, z);
								int lightBottom = world1.getSavedLightValue(this.enumSkyBlock, x, y - 1, z);
								int lightTop = world1.getSavedLightValue(this.enumSkyBlock, x, y + 1, z);
								int lightNorth = world1.getSavedLightValue(this.enumSkyBlock, x, y, z - 1);
								int lightSouth = world1.getSavedLightValue(this.enumSkyBlock, x, y, z + 1);
								newLightValue = lightWest;
								if(lightEast > lightWest) {
									newLightValue = lightEast;
								}

								if(lightBottom > newLightValue) {
									newLightValue = lightBottom;
								}

								if(lightTop > newLightValue) {
									newLightValue = lightTop;
								}

								if(lightNorth > newLightValue) {
									newLightValue = lightNorth;
								}

								if(lightSouth > newLightValue) {
									newLightValue = lightSouth;
								}

								// Underwater vision googles should pinch here!
								newLightValue -= lightOpacity;
								
								if(newLightValue < 0) {
									newLightValue = 0;
								}

								if(i20 > newLightValue) {
									newLightValue = i20;
								}
							}

							if(lightValue != newLightValue) {
								world1.setLightValue(this.enumSkyBlock, x, y, z, newLightValue);
								lightWest = newLightValue - 1;
								if(lightWest < 0) {
									lightWest = 0;
								}

								world1.neighborLightPropagationChanged(this.enumSkyBlock, x - 1, y, z, lightWest);
								world1.neighborLightPropagationChanged(this.enumSkyBlock, x, y - 1, z, lightWest);
								world1.neighborLightPropagationChanged(this.enumSkyBlock, x, y, z - 1, lightWest);
								if(x + 1 >= this.x2) {
									world1.neighborLightPropagationChanged(this.enumSkyBlock, x + 1, y, z, lightWest);
								}

								if(y + 1 >= this.y2) {
									world1.neighborLightPropagationChanged(this.enumSkyBlock, x, y + 1, z, lightWest);
								}

								if(z + 1 >= this.z2) {
									world1.neighborLightPropagationChanged(this.enumSkyBlock, x, y, z + 1, lightWest);
								}
							}
						}
					}
				}
			}

		}
	}

	public boolean insideCurrentArea(int i1, int i2, int i3, int i4, int i5, int i6) {
		if(i1 >= this.x1 && i2 >= this.y1 && i3 >= this.z1 && i4 <= this.x2 && i5 <= this.y2 && i6 <= this.z2) {
			return true;
		} else {
			byte b7 = 1;
			if(i1 >= this.x1 - b7 && i2 >= this.y1 - b7 && i3 >= this.z1 - b7 && i4 <= this.x2 + b7 && i5 <= this.y2 + b7 && i6 <= this.z2 + b7) {
				int i8 = this.x2 - this.x1;
				int i9 = this.y2 - this.y1;
				int i10 = this.z2 - this.z1;
				if(i1 > this.x1) {
					i1 = this.x1;
				}

				if(i2 > this.y1) {
					i2 = this.y1;
				}

				if(i3 > this.z1) {
					i3 = this.z1;
				}

				if(i4 < this.x2) {
					i4 = this.x2;
				}

				if(i5 < this.y2) {
					i5 = this.y2;
				}

				if(i6 < this.z2) {
					i6 = this.z2;
				}

				int i11 = i4 - i1;
				int i12 = i5 - i2;
				int i13 = i6 - i3;
				int i14 = i8 * i9 * i10;
				int i15 = i11 * i12 * i13;
				if(i15 - i14 <= 2) {
					this.x1 = i1;
					this.y1 = i2;
					this.z1 = i3;
					this.x2 = i4;
					this.y2 = i5;
					this.z2 = i6;
					return true;
				}
			}

			return false;
		}
	}
}
