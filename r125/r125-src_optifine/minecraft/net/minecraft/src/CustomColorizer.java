package net.minecraft.src;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

public class CustomColorizer {
	private static int[] grassColors = null;
	private static int[] waterColors = null;
	private static int[] foliageColors = null;
	private static int[] foliagePineColors = null;
	private static int[] foliageBirchColors = null;
	private static int[] swampFoliageColors = null;
	private static int[] swampGrassColors = null;
	private static int[][] blockPalettes = (int[][])null;
	private static int[][] paletteColors = (int[][])null;
	private static int[] skyColors = null;
	private static int[] fogColors = null;
	private static int[] underwaterColors = null;
	private static float[][][] lightMapsColorsRgb = (float[][][])null;
	private static float[][] sunRgbs = new float[16][3];
	private static float[][] torchRgbs = new float[16][3];
	private static int[] redstoneColors = null;
	private static int[] stemColors = null;
	private static int[] myceliumParticleColors = null;
	private static int particleWaterColor = -1;
	private static int particlePortalColor = -1;
	private static int lilyPadColor = -1;
	private static Vec3D fogColorNether = null;
	private static Vec3D fogColorEnd = null;
	private static Vec3D skyColorEnd = null;
	private static final int TYPE_NONE = 0;
	private static final int TYPE_GRASS = 1;
	private static final int TYPE_FOLIAGE = 2;
	private static Random random = new Random();

	public static void update(RenderEngine re) {
		grassColors = null;
		waterColors = null;
		foliageColors = null;
		foliageBirchColors = null;
		foliagePineColors = null;
		swampGrassColors = null;
		swampFoliageColors = null;
		skyColors = null;
		fogColors = null;
		underwaterColors = null;
		redstoneColors = null;
		stemColors = null;
		myceliumParticleColors = null;
		lightMapsColorsRgb = (float[][][])null;
		lilyPadColor = -1;
		particleWaterColor = -1;
		particlePortalColor = -1;
		fogColorNether = null;
		fogColorEnd = null;
		skyColorEnd = null;
		blockPalettes = (int[][])null;
		paletteColors = (int[][])null;
		grassColors = getCustomColors("/misc/grasscolor.png", re, 65536);
		foliageColors = getCustomColors("/misc/foliagecolor.png", re, 65536);
		waterColors = getCustomColors("/misc/watercolorX.png", re, 65536);
		if(Config.isCustomColors()) {
			foliagePineColors = getCustomColors("/misc/pinecolor.png", re, 65536);
			foliageBirchColors = getCustomColors("/misc/birchcolor.png", re, 65536);
			swampGrassColors = getCustomColors("/misc/swampgrasscolor.png", re, 65536);
			swampFoliageColors = getCustomColors("/misc/swampfoliagecolor.png", re, 65536);
			skyColors = getCustomColors("/misc/skycolor0.png", re, 65536);
			fogColors = getCustomColors("/misc/fogcolor0.png", re, 65536);
			underwaterColors = getCustomColors("/misc/underwatercolor.png", re, 65536);
			redstoneColors = getCustomColors("/misc/redstonecolor.png", re, 16);
			stemColors = getCustomColors("/misc/stemcolor.png", re, 8);
			myceliumParticleColors = getCustomColors("/misc/myceliumparticlecolor.png", re, -1);
			int[][] lightMapsColors = new int[3][];
			lightMapsColorsRgb = new float[3][][];

			for(int i = 0; i < lightMapsColors.length; ++i) {
				lightMapsColors[i] = getCustomColors("/environment/lightmap" + (i - 1) + ".png", re, -1);
				if(lightMapsColors[i] != null) {
					lightMapsColorsRgb[i] = toRgb(lightMapsColors[i]);
				}
			}

			readColorProperties("/color.properties", re);
		}
	}

	private static float[][] toRgb(int[] cols) {
		float[][] colsRgb = new float[cols.length][3];

		for(int i = 0; i < cols.length; ++i) {
			int col = cols[i];
			float rf = (float)(col >> 16 & 255) / 255.0F;
			float gf = (float)(col >> 8 & 255) / 255.0F;
			float bf = (float)(col & 255) / 255.0F;
			float[] colRgb = colsRgb[i];
			colRgb[0] = rf;
			colRgb[1] = gf;
			colRgb[2] = bf;
		}

		return colsRgb;
	}

	private static void readColorProperties(String fileName, RenderEngine re) {
		InputStream in = re.getTexturePack().selectedTexturePack.getResourceAsStream(fileName);
		if(in != null) {
			try {
				Config.log("Loading " + fileName);
				Properties e = new Properties();
				e.load(in);
				lilyPadColor = readColor(e, "lilypad");
				particleWaterColor = readColor(e, new String[]{"particle.water", "drop.water"});
				particlePortalColor = readColor(e, "particle.portal");
				fogColorNether = readColorVec3D(e, "fog.nether");
				fogColorEnd = readColorVec3D(e, "fog.end");
				skyColorEnd = readColorVec3D(e, "sky.end");
				readCustomPalettes(e, re);
			} catch (IOException iOException4) {
				iOException4.printStackTrace();
			}

		}
	}

	private static void readCustomPalettes(Properties props, RenderEngine re) {
		blockPalettes = new int[256][1];

		for(int palettePrefix = 0; palettePrefix < 256; ++palettePrefix) {
			blockPalettes[palettePrefix][0] = -1;
		}

		String string17 = "palette.block.";
		HashMap map = new HashMap();
		Set keys = props.keySet();
		Iterator propNames = keys.iterator();

		String name;
		while(propNames.hasNext()) {
			String i = (String)propNames.next();
			name = props.getProperty(i);
			if(i.startsWith(string17)) {
				map.put(i, name);
			}
		}

		String[] string18 = (String[])((String[])map.keySet().toArray(new String[map.size()]));
		paletteColors = new int[string18.length][];

		for(int i19 = 0; i19 < string18.length; ++i19) {
			name = string18[i19];
			String value = props.getProperty(name);
			Config.log("Block palette: " + name + " = " + value);
			String path = name.substring(string17.length());
			int[] colors = getCustomColors(path, re, 65536);
			paletteColors[i19] = colors;
			String[] indexStrs = Config.tokenize(value, " ,;");

			for(int ix = 0; ix < indexStrs.length; ++ix) {
				String blockStr = indexStrs[ix];
				int metadata = -1;
				if(blockStr.contains(":")) {
					String[] blockIndex = Config.tokenize(blockStr, ":");
					blockStr = blockIndex[0];
					String metadataStr = blockIndex[1];
					metadata = Config.parseInt(metadataStr, -1);
					if(metadata < 0 || metadata > 15) {
						Config.log("Invalid block metadata: " + blockStr + " in palette: " + name);
						continue;
					}
				}

				int i20 = Config.parseInt(blockStr, -1);
				if(i20 >= 0 && i20 <= 255) {
					if(i20 != Block.grass.blockID && i20 != Block.tallGrass.blockID && i20 != Block.leaves.blockID && i20 != Block.vine.blockID) {
						if(metadata == -1) {
							blockPalettes[i20][0] = i19;
						} else {
							if(blockPalettes[i20].length < 16) {
								blockPalettes[i20] = new int[16];
								Arrays.fill(blockPalettes[i20], -1);
							}

							blockPalettes[i20][metadata] = i19;
						}
					}
				} else {
					Config.log("Invalid block index: " + i20 + " in palette: " + name);
				}
			}
		}

	}

	private static int readColor(Properties props, String[] names) {
		for(int i = 0; i < names.length; ++i) {
			String name = names[i];
			int col = readColor(props, name);
			if(col >= 0) {
				return col;
			}
		}

		return -1;
	}

	private static int readColor(Properties props, String name) {
		String str = props.getProperty(name);
		if(str == null) {
			return -1;
		} else {
			try {
				int e = Integer.parseInt(str, 16) & 0xFFFFFF;
				Config.log("Custom color: " + name + " = " + str);
				return e;
			} catch (NumberFormatException numberFormatException4) {
				Config.log("Invalid custom color: " + name + " = " + str);
				return -1;
			}
		}
	}

	private static Vec3D readColorVec3D(Properties props, String name) {
		int col = readColor(props, name);
		if(col < 0) {
			return null;
		} else {
			int red = col >> 16 & 255;
			int green = col >> 8 & 255;
			int blue = col & 255;
			float redF = (float)red / 255.0F;
			float greenF = (float)green / 255.0F;
			float blueF = (float)blue / 255.0F;
			return Vec3D.createVectorHelper((double)redF, (double)greenF, (double)blueF);
		}
	}

	private static int[] getCustomColors(String path, RenderEngine re, int length) {
		InputStream in = re.getTexturePack().selectedTexturePack.getResourceAsStream(path);
		if(in == null) {
			return null;
		} else {
			int[] colors = re.getTextureContents(path);
			if(colors == null) {
				return null;
			} else if(length > 0 && colors.length != length) {
				Config.log("Invalid custom colors length: " + colors.length + ", path: " + path);
				return null;
			} else {
				Config.log("Loading custom colors: " + path);
				return colors;
			}
		}
	}

	public static int getColorMultiplier(Block block, IBlockAccess blockAccess, int x, int y, int z) {
		if(foliageBirchColors == null && foliagePineColors == null && swampGrassColors == null && swampFoliageColors == null && blockPalettes == null && Config.isSwampColors() && Config.isSmoothBiomes()) {
			return block.colorMultiplier(blockAccess, x, y, z);
		} else {
			int[] colors = null;
			int[] swampColors = null;
			int metadata;
			if(blockPalettes != null) {
				int useSwampColors = block.blockID;
				if(useSwampColors >= 0 && useSwampColors < 256) {
					int[] smoothColors = blockPalettes[useSwampColors];
					boolean type = true;
					int type1;
					if(smoothColors.length > 1) {
						metadata = blockAccess.getBlockMetadata(x, y, z);
						type1 = smoothColors[metadata];
					} else {
						type1 = smoothColors[0];
					}

					if(type1 >= 0) {
						colors = paletteColors[type1];
					}
				}

				if(colors != null) {
					if(Config.isSmoothBiomes()) {
						return getSmoothColorMultiplier(block, blockAccess, x, y, z, colors, swampColors, 0, 0);
					}

					return getCustomColor(colors, blockAccess, x, y, z);
				}
			}

			boolean useSwampColors1 = Config.isSwampColors();
			boolean smoothColors1 = false;
			byte type2 = 0;
			metadata = 0;
			if(block != Block.grass && block != Block.tallGrass) {
				if(block == Block.leaves) {
					type2 = 2;
					smoothColors1 = Config.isSmoothBiomes();
					metadata = blockAccess.getBlockMetadata(x, y, z);
					if((metadata & 3) == 1) {
						colors = foliagePineColors;
					} else if((metadata & 3) == 2) {
						colors = foliageBirchColors;
					} else {
						colors = foliageColors;
						if(useSwampColors1) {
							swampColors = swampFoliageColors;
						} else {
							swampColors = colors;
						}
					}
				} else if(block == Block.vine) {
					type2 = 2;
					smoothColors1 = Config.isSmoothBiomes();
					colors = foliageColors;
					if(useSwampColors1) {
						swampColors = swampFoliageColors;
					} else {
						swampColors = colors;
					}
				}
			} else {
				type2 = 1;
				smoothColors1 = Config.isSmoothBiomes();
				colors = grassColors;
				if(useSwampColors1) {
					swampColors = swampGrassColors;
				} else {
					swampColors = colors;
				}
			}

			if(smoothColors1) {
				return getSmoothColorMultiplier(block, blockAccess, x, y, z, colors, swampColors, type2, metadata);
			} else {
				if(swampColors != colors && blockAccess.getBiomeGenForCoords(x, z) == BiomeGenBase.swampland) {
					colors = swampColors;
				}

				return colors != null ? getCustomColor(colors, blockAccess, x, y, z) : block.colorMultiplier(blockAccess, x, y, z);
			}
		}
	}

	private static int getSmoothColorMultiplier(Block block, IBlockAccess blockAccess, int x, int y, int z, int[] colors, int[] swampColors, int type, int metadata) {
		int sumRed = 0;
		int sumGreen = 0;
		int sumBlue = 0;

		int r;
		int g;
		for(r = x - 1; r <= x + 1; ++r) {
			for(g = z - 1; g <= z + 1; ++g) {
				int[] b = colors;
				if(swampColors != colors && blockAccess.getBiomeGenForCoords(r, g) == BiomeGenBase.swampland) {
					b = swampColors;
				}

				boolean col = false;
				int i17;
				if(b == null) {
					switch(type) {
					case 1:
						i17 = blockAccess.getBiomeGenForCoords(r, g).getBiomeGrassColor();
						break;
					case 2:
						if((metadata & 3) == 1) {
							i17 = ColorizerFoliage.getFoliageColorPine();
						} else if((metadata & 3) == 2) {
							i17 = ColorizerFoliage.getFoliageColorBirch();
						} else {
							i17 = blockAccess.getBiomeGenForCoords(r, g).getBiomeFoliageColor();
						}
						break;
					default:
						i17 = block.colorMultiplier(blockAccess, r, y, g);
					}
				} else {
					i17 = getCustomColor(b, blockAccess, r, y, g);
				}

				sumRed += i17 >> 16 & 255;
				sumGreen += i17 >> 8 & 255;
				sumBlue += i17 & 255;
			}
		}

		r = sumRed / 9;
		g = sumGreen / 9;
		int i16 = sumBlue / 9;
		return r << 16 | g << 8 | i16;
	}

	public static int getFluidColor(Block block, IBlockAccess blockAccess, int x, int y, int z) {
		return block.blockMaterial != Material.water ? block.colorMultiplier(blockAccess, x, y, z) : (waterColors != null ? (Config.isSmoothBiomes() ? getSmoothColor(waterColors, blockAccess, (double)x, (double)y, (double)z, 3, 1) : getCustomColor(waterColors, blockAccess, x, y, z)) : (!Config.isSwampColors() ? 0xFFFFFF : block.colorMultiplier(blockAccess, x, y, z)));
	}

	private static int getCustomColor(int[] colors, IBlockAccess blockAccess, int x, int y, int z) {
		BiomeGenBase bgb = blockAccess.getBiomeGenForCoords(x, z);
		double temperature = (double)MathHelper.clamp_float(bgb.getFloatTemperature(), 0.0F, 1.0F);
		double rainfall = (double)MathHelper.clamp_float(bgb.getFloatRainfall(), 0.0F, 1.0F);
		rainfall *= temperature;
		int cx = (int)((1.0D - temperature) * 255.0D);
		int cy = (int)((1.0D - rainfall) * 255.0D);
		return colors[cy << 8 | cx] & 0xFFFFFF;
	}

	public static void updatePortalFX(EntityFX fx) {
		if(particlePortalColor >= 0) {
			int col = particlePortalColor;
			int red = col >> 16 & 255;
			int green = col >> 8 & 255;
			int blue = col & 255;
			float redF = (float)red / 255.0F;
			float greenF = (float)green / 255.0F;
			float blueF = (float)blue / 255.0F;
			fx.particleRed = redF;
			fx.particleGreen = greenF;
			fx.particleBlue = blueF;
		}
	}

	public static void updateMyceliumFX(EntityFX fx) {
		if(myceliumParticleColors != null) {
			int col = myceliumParticleColors[random.nextInt(myceliumParticleColors.length)];
			int red = col >> 16 & 255;
			int green = col >> 8 & 255;
			int blue = col & 255;
			float redF = (float)red / 255.0F;
			float greenF = (float)green / 255.0F;
			float blueF = (float)blue / 255.0F;
			fx.particleRed = redF;
			fx.particleGreen = greenF;
			fx.particleBlue = blueF;
		}
	}

	public static void updateReddustFX(EntityFX fx, IBlockAccess blockAccess, double x, double y, double z) {
		if(redstoneColors != null) {
			int level = blockAccess.getBlockMetadata((int)x, (int)y, (int)z);
			int col = getRedstoneColor(level);
			if(col != -1) {
				int red = col >> 16 & 255;
				int green = col >> 8 & 255;
				int blue = col & 255;
				float redF = (float)red / 255.0F;
				float greenF = (float)green / 255.0F;
				float blueF = (float)blue / 255.0F;
				fx.particleRed = redF;
				fx.particleGreen = greenF;
				fx.particleBlue = blueF;
			}
		}
	}

	public static int getRedstoneColor(int level) {
		return redstoneColors == null ? -1 : (level >= 0 && level <= 15 ? redstoneColors[level] & 0xFFFFFF : -1);
	}

	public static void updateWaterFX(EntityFX fx, IBlockAccess blockAccess) {
		if(waterColors != null) {
			int x = (int)fx.posX;
			int y = (int)fx.posY;
			int z = (int)fx.posZ;
			int col = getFluidColor(Block.waterStill, blockAccess, x, y, z);
			int red = col >> 16 & 255;
			int green = col >> 8 & 255;
			int blue = col & 255;
			float redF = (float)red / 255.0F;
			float greenF = (float)green / 255.0F;
			float blueF = (float)blue / 255.0F;
			if(particleWaterColor >= 0) {
				int redDrop = particleWaterColor >> 16 & 255;
				int greenDrop = particleWaterColor >> 8 & 255;
				int blueDrop = particleWaterColor & 255;
				redF *= (float)redDrop / 255.0F;
				greenF *= (float)greenDrop / 255.0F;
				blueF *= (float)blueDrop / 255.0F;
			}

			fx.particleRed = redF;
			fx.particleGreen = greenF;
			fx.particleBlue = blueF;
		}
	}

	public static int getLilypadColor() {
		return lilyPadColor < 0 ? Block.waterlily.getBlockColor() : lilyPadColor;
	}

	public static Vec3D getFogColorNether(Vec3D col) {
		return fogColorNether == null ? col : fogColorNether;
	}

	public static Vec3D getFogColorEnd(Vec3D col) {
		return fogColorEnd == null ? col : fogColorEnd;
	}

	public static Vec3D getSkyColorEnd(Vec3D col) {
		return skyColorEnd == null ? col : skyColorEnd;
	}

	public static Vec3D getSkyColor(Vec3D skyColor3d, IBlockAccess blockAccess, double x, double y, double z) {
		if(skyColors == null) {
			return skyColor3d;
		} else {
			int col = getSmoothColor(skyColors, blockAccess, x, y, z, 10, 1);
			int red = col >> 16 & 255;
			int green = col >> 8 & 255;
			int blue = col & 255;
			float redF = (float)red / 255.0F;
			float greenF = (float)green / 255.0F;
			float blueF = (float)blue / 255.0F;
			float cRed = (float)skyColor3d.xCoord / 0.5F;
			float cGreen = (float)skyColor3d.yCoord / 0.66275F;
			float cBlue = (float)skyColor3d.zCoord;
			redF *= cRed;
			greenF *= cGreen;
			blueF *= cBlue;
			return Vec3D.createVectorHelper((double)redF, (double)greenF, (double)blueF);
		}
	}

	public static Vec3D getFogColor(Vec3D fogColor3d, IBlockAccess blockAccess, double x, double y, double z) {
		if(fogColors == null) {
			return fogColor3d;
		} else {
			int col = getSmoothColor(fogColors, blockAccess, x, y, z, 10, 1);
			int red = col >> 16 & 255;
			int green = col >> 8 & 255;
			int blue = col & 255;
			float redF = (float)red / 255.0F;
			float greenF = (float)green / 255.0F;
			float blueF = (float)blue / 255.0F;
			float cRed = (float)fogColor3d.xCoord / 0.753F;
			float cGreen = (float)fogColor3d.yCoord / 0.8471F;
			float cBlue = (float)fogColor3d.zCoord;
			redF *= cRed;
			greenF *= cGreen;
			blueF *= cBlue;
			return Vec3D.createVectorHelper((double)redF, (double)greenF, (double)blueF);
		}
	}

	public static Vec3D getUnderwaterColor(IBlockAccess blockAccess, double x, double y, double z) {
		if(underwaterColors == null) {
			return null;
		} else {
			int col = getSmoothColor(underwaterColors, blockAccess, x, y, z, 10, 1);
			int red = col >> 16 & 255;
			int green = col >> 8 & 255;
			int blue = col & 255;
			float redF = (float)red / 255.0F;
			float greenF = (float)green / 255.0F;
			float blueF = (float)blue / 255.0F;
			return Vec3D.createVectorHelper((double)redF, (double)greenF, (double)blueF);
		}
	}

	public static int getSmoothColor(int[] colors, IBlockAccess blockAccess, double x, double y, double z, int samples, int step) {
		if(colors == null) {
			return -1;
		} else {
			int x0 = (int)Math.floor(x);
			int y0 = (int)Math.floor(y);
			int z0 = (int)Math.floor(z);
			int n = samples * step / 2;
			int sumRed = 0;
			int sumGreen = 0;
			int sumBlue = 0;
			int count = 0;

			int r;
			int g;
			int b;
			for(r = x0 - n; r <= x0 + n; r += step) {
				for(g = z0 - n; g <= z0 + n; g += step) {
					b = getCustomColor(colors, blockAccess, r, y0, g);
					sumRed += b >> 16 & 255;
					sumGreen += b >> 8 & 255;
					sumBlue += b & 255;
					++count;
				}
			}

			r = sumRed / count;
			g = sumGreen / count;
			b = sumBlue / count;
			return r << 16 | g << 8 | b;
		}
	}

	public static int mixColors(int c1, int c2, float w1) {
		if(w1 <= 0.0F) {
			return c2;
		} else if(w1 >= 1.0F) {
			return c1;
		} else {
			float w2 = 1.0F - w1;
			int r1 = c1 >> 16 & 255;
			int g1 = c1 >> 8 & 255;
			int b1 = c1 & 255;
			int r2 = c2 >> 16 & 255;
			int g2 = c2 >> 8 & 255;
			int b2 = c2 & 255;
			int r = (int)((float)r1 * w1 + (float)r2 * w2);
			int g = (int)((float)g1 * w1 + (float)g2 * w2);
			int b = (int)((float)b1 * w1 + (float)b2 * w2);
			return r << 16 | g << 8 | b;
		}
	}

	private static int averageColor(int c1, int c2) {
		int r1 = c1 >> 16 & 255;
		int g1 = c1 >> 8 & 255;
		int b1 = c1 & 255;
		int r2 = c2 >> 16 & 255;
		int g2 = c2 >> 8 & 255;
		int b2 = c2 & 255;
		int r = (r1 + r2) / 2;
		int g = (g1 + g2) / 2;
		int b = (b1 + b2) / 2;
		return r << 16 | g << 8 | b;
	}

	public static int getStemColorMultiplier(BlockStem blockStem, IBlockAccess blockAccess, int x, int y, int z) {
		if(stemColors == null) {
			return blockStem.colorMultiplier(blockAccess, x, y, z);
		} else {
			int level = blockAccess.getBlockMetadata(x, y, z);
			if(level < 0) {
				level = 0;
			}

			if(level >= stemColors.length) {
				level = stemColors.length - 1;
			}

			return stemColors[level];
		}
	}

	public static boolean updateLightmap(World world, EntityRenderer entityRenderer, int[] lmColors) {
		if(world == null) {
			return false;
		} else if(lightMapsColorsRgb == null) {
			return false;
		} else if(!Config.isCustomColors()) {
			return false;
		} else {
			int worldType = world.worldProvider.worldType;
			if(worldType >= -1 && worldType <= 1) {
				int lightMapIndex = worldType + 1;
				float[][] lightMapRgb = lightMapsColorsRgb[lightMapIndex];
				if(lightMapRgb == null) {
					return false;
				} else {
					byte height = 32;
					int width = lightMapRgb.length / height;
					if(width < 16) {
						Config.dbg("Invalid lightmap width: " + width + " for: /environment/lightmap" + worldType + ".png");
						lightMapsColorsRgb[lightMapIndex] = (float[][])null;
						return false;
					} else {
						float sun = 1.1666666F * (world.func_35464_b(1.0F) - 0.2F);
						if(world.lightningFlash > 0) {
							sun = 1.0F;
						}

						sun = Config.limitTo1(sun);
						float sunX = sun * (float)(width - 1);
						float torchX = Config.limitTo1(entityRenderer.torchFlickerX + 0.5F) * (float)(width - 1);
						float gamma = Config.limitTo1(Config.getMinecraft().gameSettings.gammaSetting);
						boolean hasGamma = gamma > 1.0E-4F;
						getLightMapColumn(lightMapRgb, sunX, 0, width, sunRgbs);
						getLightMapColumn(lightMapRgb, torchX, 16 * width, width, torchRgbs);
						float[] rgb = new float[3];

						for(int is = 0; is < 16; ++is) {
							for(int it = 0; it < 16; ++it) {
								int r;
								for(r = 0; r < 3; ++r) {
									float g = Config.limitTo1(sunRgbs[is][r] + torchRgbs[it][r]);
									if(hasGamma) {
										float b = 1.0F - g;
										b = 1.0F - b * b * b * b;
										g = gamma * b + (1.0F - gamma) * g;
									}

									rgb[r] = g;
								}

								r = (int)(rgb[0] * 255.0F);
								int i19 = (int)(rgb[1] * 255.0F);
								int i20 = (int)(rgb[2] * 255.0F);
								lmColors[is * 16 + it] = 0xFF000000 | r << 16 | i19 << 8 | i20;
							}
						}

						return true;
					}
				}
			} else {
				return false;
			}
		}
	}

	private static void getLightMapColumn(float[][] origMap, float x, int offset, int width, float[][] colRgb) {
		int xLow = (int)Math.floor((double)x);
		int xHigh = (int)Math.ceil((double)x);
		if(xLow == xHigh) {
			for(int i14 = 0; i14 < 16; ++i14) {
				float[] f15 = origMap[offset + i14 * width + xLow];
				float[] f16 = colRgb[i14];

				for(int i17 = 0; i17 < 3; ++i17) {
					f16[i17] = f15[i17];
				}
			}

		} else {
			float dLow = 1.0F - (x - (float)xLow);
			float dHigh = 1.0F - ((float)xHigh - x);

			for(int y = 0; y < 16; ++y) {
				float[] rgbLow = origMap[offset + y * width + xLow];
				float[] rgbHigh = origMap[offset + y * width + xHigh];
				float[] rgb = colRgb[y];

				for(int i = 0; i < 3; ++i) {
					rgb[i] = rgbLow[i] * dLow + rgbHigh[i] * dHigh;
				}
			}

		}
	}
}
