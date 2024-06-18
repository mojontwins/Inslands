package net.minecraft.src;

import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class ConnectedTextures {
	private static ConnectedProperties[][] blockProperties = (ConnectedProperties[][])null;
	private static ConnectedProperties[][] terrainProperties = (ConnectedProperties[][])null;
	private static boolean matchingCtmPng = false;
	private static final int BOTTOM = 0;
	private static final int TOP = 1;
	private static final int EAST = 2;
	private static final int WEST = 3;
	private static final int NORTH = 4;
	private static final int SOUTH = 5;
	private static final String[] propSuffixes = new String[]{"", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

	public static void update(RenderEngine re) {
		blockProperties = (ConnectedProperties[][])null;
		terrainProperties = (ConnectedProperties[][])null;
		matchingCtmPng = false;
		blockProperties = readConnectedProperties("/ctm/block", 256, re, 1);
		terrainProperties = readConnectedProperties("/ctm/terrain", 256, re, 2);
		matchingCtmPng = getMatchingCtmPng(re);
		Config.dbg("MatchingCtmPng: " + matchingCtmPng);
		if(blockProperties == null && terrainProperties == null && matchingCtmPng) {
			Config.dbg("Registering default ConnectedTextures");
			blockProperties = new ConnectedProperties[256][];
			blockProperties[Block.glass.blockID] = new ConnectedProperties[1];
			blockProperties[Block.glass.blockID][0] = makeDefaultProperties("ctm", re);
			blockProperties[Block.bookShelf.blockID] = new ConnectedProperties[1];
			blockProperties[Block.bookShelf.blockID][0] = makeDefaultProperties("horizontal", re);
			terrainProperties = new ConnectedProperties[256][];
			terrainProperties[Block.sandStone.blockIndexInTexture] = new ConnectedProperties[1];
			terrainProperties[Block.sandStone.blockIndexInTexture][0] = makeDefaultProperties("top", re);
		}

	}

	private static ConnectedProperties[][] readConnectedProperties(String prefix, int num, RenderEngine re, int defConnect) {
		ConnectedProperties[][] cps = (ConnectedProperties[][])null;

		for(int i = 0; i < num; ++i) {
			ArrayList listIndexProps = new ArrayList();

			for(int is = 0; is < propSuffixes.length; ++is) {
				String suff = propSuffixes[is];
				String path = prefix + i + suff + ".properties";
				InputStream in = re.texturePack.selectedTexturePack.getResourceAsStream(path);
				if(in == null) {
					break;
				}

				try {
					Properties e = new Properties();
					e.load(in);
					Config.dbg("Connected texture: " + path);
					ConnectedProperties cp = new ConnectedProperties(e);
					if(cp.connect == 0) {
						cp.connect = defConnect;
					}

					if(cp.isValid(path)) {
						cp.textureId = re.getTexture(cp.source);
						listIndexProps.add(cp);
						in.close();
					}
				} catch (IOException iOException13) {
					iOException13.printStackTrace();
				}
			}

			if(listIndexProps.size() > 0) {
				if(cps == null) {
					cps = new ConnectedProperties[num][0];
				}

				cps[i] = (ConnectedProperties[])((ConnectedProperties[])listIndexProps.toArray(new ConnectedProperties[listIndexProps.size()]));
			}
		}

		return cps;
	}

	public static int getConnectedTexture(IBlockAccess blockAccess, Block block, int x, int y, int z, int side, int tileNum) {
		if(blockAccess == null) {
			return -1;
		} else {
			if(terrainProperties != null && Tessellator.instance.defaultTexture && tileNum >= 0 && tileNum < terrainProperties.length) {
				ConnectedProperties[] blockId = terrainProperties[tileNum];
				if(blockId != null) {
					int cps = getConnectedTexture(blockId, blockAccess, block, x, y, z, side, tileNum);
					if(cps >= 0) {
						return cps;
					}
				}
			}

			if(blockProperties != null) {
				int blockId1 = block.blockID;
				if(blockId1 >= 0 && blockId1 < blockProperties.length) {
					ConnectedProperties[] cps1 = blockProperties[blockId1];
					if(cps1 != null) {
						int texNum = getConnectedTexture(cps1, blockAccess, block, x, y, z, side, tileNum);
						if(texNum >= 0) {
							return texNum;
						}
					}
				}
			}

			return -1;
		}
	}

	private static int getConnectedTexture(ConnectedProperties[] cps, IBlockAccess blockAccess, Block block, int x, int y, int z, int side, int tileNum) {
		for(int i = 0; i < cps.length; ++i) {
			ConnectedProperties cp = cps[i];
			if(cp != null) {
				int texNum = getConnectedTexture(cp, blockAccess, block, x, y, z, side, tileNum);
				if(texNum >= 0) {
					return texNum;
				}
			}
		}

		return -1;
	}

	private static int getConnectedTexture(ConnectedProperties cp, IBlockAccess blockAccess, Block block, int x, int y, int z, int side, int tileNum) {
		if(side >= 0 && (1 << side & cp.faces) == 0) {
			return -1;
		} else {
			if(cp.metadata != null) {
				int[] mds = cp.metadata;
				int md = blockAccess.getBlockMetadata(x, y, z);
				boolean metadataFound = false;

				for(int i = 0; i < mds.length; ++i) {
					if(mds[i] == md) {
						metadataFound = true;
						break;
					}
				}

				if(!metadataFound) {
					return -1;
				}
			}

			switch(cp.method) {
			case 1:
				return getConnectedTextureCtm(cp, blockAccess, block, x, y, z, side, tileNum);
			case 2:
				return getConnectedTextureHorizontal(cp, blockAccess, block, x, y, z, side, tileNum);
			case 3:
				return getConnectedTextureTop(cp, blockAccess, block, x, y, z, side, tileNum);
			case 4:
				return getConnectedTextureRandom(cp, x, y, z, side);
			case 5:
				return getConnectedTextureRepeat(cp, x, y, z, side);
			case 6:
				return getConnectedTextureVertical(cp, blockAccess, block, x, y, z, side, tileNum);
			default:
				return -1;
			}
		}
	}

	private static int getConnectedTextureRandom(ConnectedProperties cp, int x, int y, int z, int side) {
		int face = side / cp.symmetry * cp.symmetry;
		int rand = Config.getRandom(x, y, z, side) & Integer.MAX_VALUE;
		int index = 0;
		if(cp.weights == null) {
			index = rand % cp.tiles.length;
		} else {
			int randWeight = rand % cp.sumAllWeights;
			int[] sumWeights = cp.sumWeights;

			for(int i = 0; i < sumWeights.length; ++i) {
				if(randWeight < sumWeights[i]) {
					index = i;
					break;
				}
			}
		}

		return cp.textureId * 256 + cp.tiles[index];
	}

	private static int getConnectedTextureRepeat(ConnectedProperties cp, int x, int y, int z, int side) {
		int nx = 0;
		int ny = 0;
		switch(side) {
		case 0:
			nx = x;
			ny = z;
			break;
		case 1:
			nx = x;
			ny = z;
			break;
		case 2:
			nx = -x - 1;
			ny = -y;
			break;
		case 3:
			nx = x;
			ny = -y;
			break;
		case 4:
			nx = z;
			ny = -y;
			break;
		case 5:
			nx = -z - 1;
			ny = -y;
		}

		nx %= cp.width;
		ny %= cp.height;
		if(nx < 0) {
			nx += cp.width;
		}

		if(ny < 0) {
			ny += cp.height;
		}

		int index = ny * cp.width + nx;
		return cp.textureId * 256 + cp.tiles[index];
	}

	private static int getConnectedTextureCtm(ConnectedProperties cp, IBlockAccess blockAccess, Block block, int x, int y, int z, int side, int tileNum) {
		boolean[] borders = new boolean[6];
		int id = block.blockID;
		switch(side) {
		case 0:
		case 1:
			borders[0] = isNeighbour(cp, blockAccess, x - 1, y, z, id, side, tileNum);
			borders[1] = isNeighbour(cp, blockAccess, x + 1, y, z, id, side, tileNum);
			borders[2] = isNeighbour(cp, blockAccess, x, y, z + 1, id, side, tileNum);
			borders[3] = isNeighbour(cp, blockAccess, x, y, z - 1, id, side, tileNum);
			break;
		case 2:
			borders[0] = isNeighbour(cp, blockAccess, x + 1, y, z, id, side, tileNum);
			borders[1] = isNeighbour(cp, blockAccess, x - 1, y, z, id, side, tileNum);
			borders[2] = isNeighbour(cp, blockAccess, x, y - 1, z, id, side, tileNum);
			borders[3] = isNeighbour(cp, blockAccess, x, y + 1, z, id, side, tileNum);
			break;
		case 3:
			borders[0] = isNeighbour(cp, blockAccess, x - 1, y, z, id, side, tileNum);
			borders[1] = isNeighbour(cp, blockAccess, x + 1, y, z, id, side, tileNum);
			borders[2] = isNeighbour(cp, blockAccess, x, y - 1, z, id, side, tileNum);
			borders[3] = isNeighbour(cp, blockAccess, x, y + 1, z, id, side, tileNum);
			break;
		case 4:
			borders[0] = isNeighbour(cp, blockAccess, x, y, z - 1, id, side, tileNum);
			borders[1] = isNeighbour(cp, blockAccess, x, y, z + 1, id, side, tileNum);
			borders[2] = isNeighbour(cp, blockAccess, x, y - 1, z, id, side, tileNum);
			borders[3] = isNeighbour(cp, blockAccess, x, y + 1, z, id, side, tileNum);
			break;
		case 5:
			borders[0] = isNeighbour(cp, blockAccess, x, y, z + 1, id, side, tileNum);
			borders[1] = isNeighbour(cp, blockAccess, x, y, z - 1, id, side, tileNum);
			borders[2] = isNeighbour(cp, blockAccess, x, y - 1, z, id, side, tileNum);
			borders[3] = isNeighbour(cp, blockAccess, x, y + 1, z, id, side, tileNum);
		}

		byte index = 0;
		if(borders[0] & !borders[1] & !borders[2] & !borders[3]) {
			index = 3;
		} else if(!borders[0] & borders[1] & !borders[2] & !borders[3]) {
			index = 1;
		} else if(!borders[0] & !borders[1] & borders[2] & !borders[3]) {
			index = 12;
		} else if(!borders[0] & !borders[1] & !borders[2] & borders[3]) {
			index = 36;
		} else if(borders[0] & borders[1] & !borders[2] & !borders[3]) {
			index = 2;
		} else if(!borders[0] & !borders[1] & borders[2] & borders[3]) {
			index = 24;
		} else if(borders[0] & !borders[1] & borders[2] & !borders[3]) {
			index = 15;
		} else if(borders[0] & !borders[1] & !borders[2] & borders[3]) {
			index = 39;
		} else if(!borders[0] & borders[1] & borders[2] & !borders[3]) {
			index = 13;
		} else if(!borders[0] & borders[1] & !borders[2] & borders[3]) {
			index = 37;
		} else if(!borders[0] & borders[1] & borders[2] & borders[3]) {
			index = 25;
		} else if(borders[0] & !borders[1] & borders[2] & borders[3]) {
			index = 27;
		} else if(borders[0] & borders[1] & !borders[2] & borders[3]) {
			index = 38;
		} else if(borders[0] & borders[1] & borders[2] & !borders[3]) {
			index = 14;
		} else if(borders[0] & borders[1] & borders[2] & borders[3]) {
			index = 26;
		}

		if(!Config.isConnectedTexturesFancy()) {
			return cp.textureId * 256 + cp.tiles[index];
		} else {
			boolean[] edges = new boolean[6];
			switch(side) {
			case 0:
			case 1:
				edges[0] = !isNeighbour(cp, blockAccess, x + 1, y, z + 1, id, side, tileNum);
				edges[1] = !isNeighbour(cp, blockAccess, x - 1, y, z + 1, id, side, tileNum);
				edges[2] = !isNeighbour(cp, blockAccess, x + 1, y, z - 1, id, side, tileNum);
				edges[3] = !isNeighbour(cp, blockAccess, x - 1, y, z - 1, id, side, tileNum);
				break;
			case 2:
				edges[0] = !isNeighbour(cp, blockAccess, x - 1, y - 1, z, id, side, tileNum);
				edges[1] = !isNeighbour(cp, blockAccess, x + 1, y - 1, z, id, side, tileNum);
				edges[2] = !isNeighbour(cp, blockAccess, x - 1, y + 1, z, id, side, tileNum);
				edges[3] = !isNeighbour(cp, blockAccess, x + 1, y + 1, z, id, side, tileNum);
				break;
			case 3:
				edges[0] = !isNeighbour(cp, blockAccess, x + 1, y - 1, z, id, side, tileNum);
				edges[1] = !isNeighbour(cp, blockAccess, x - 1, y - 1, z, id, side, tileNum);
				edges[2] = !isNeighbour(cp, blockAccess, x + 1, y + 1, z, id, side, tileNum);
				edges[3] = !isNeighbour(cp, blockAccess, x - 1, y + 1, z, id, side, tileNum);
				break;
			case 4:
				edges[0] = !isNeighbour(cp, blockAccess, x, y - 1, z + 1, id, side, tileNum);
				edges[1] = !isNeighbour(cp, blockAccess, x, y - 1, z - 1, id, side, tileNum);
				edges[2] = !isNeighbour(cp, blockAccess, x, y + 1, z + 1, id, side, tileNum);
				edges[3] = !isNeighbour(cp, blockAccess, x, y + 1, z - 1, id, side, tileNum);
				break;
			case 5:
				edges[0] = !isNeighbour(cp, blockAccess, x, y - 1, z - 1, id, side, tileNum);
				edges[1] = !isNeighbour(cp, blockAccess, x, y - 1, z + 1, id, side, tileNum);
				edges[2] = !isNeighbour(cp, blockAccess, x, y + 1, z - 1, id, side, tileNum);
				edges[3] = !isNeighbour(cp, blockAccess, x, y + 1, z + 1, id, side, tileNum);
			}

			if(index == 13 && edges[0]) {
				index = 4;
			}

			if(index == 15 && edges[1]) {
				index = 5;
			}

			if(index == 37 && edges[2]) {
				index = 16;
			}

			if(index == 39 && edges[3]) {
				index = 17;
			}

			if(index == 14 && edges[0] && edges[1]) {
				index = 7;
			}

			if(index == 25 && edges[0] && edges[2]) {
				index = 6;
			}

			if(index == 27 && edges[3] && edges[1]) {
				index = 19;
			}

			if(index == 38 && edges[3] && edges[2]) {
				index = 18;
			}

			if(index == 14 && !edges[0] && edges[1]) {
				index = 31;
			}

			if(index == 25 && edges[0] && !edges[2]) {
				index = 30;
			}

			if(index == 27 && !edges[3] && edges[1]) {
				index = 41;
			}

			if(index == 38 && edges[3] && !edges[2]) {
				index = 40;
			}

			if(index == 14 && edges[0] && !edges[1]) {
				index = 29;
			}

			if(index == 25 && !edges[0] && edges[2]) {
				index = 28;
			}

			if(index == 27 && edges[3] && !edges[1]) {
				index = 43;
			}

			if(index == 38 && !edges[3] && edges[2]) {
				index = 42;
			}

			if(index == 26 && edges[0] && edges[1] && edges[2] && edges[3]) {
				index = 46;
			}

			if(index == 26 && !edges[0] && edges[1] && edges[2] && edges[3]) {
				index = 9;
			}

			if(index == 26 && edges[0] && !edges[1] && edges[2] && edges[3]) {
				index = 21;
			}

			if(index == 26 && edges[0] && edges[1] && !edges[2] && edges[3]) {
				index = 8;
			}

			if(index == 26 && edges[0] && edges[1] && edges[2] && !edges[3]) {
				index = 20;
			}

			if(index == 26 && edges[0] && edges[1] && !edges[2] && !edges[3]) {
				index = 11;
			}

			if(index == 26 && !edges[0] && !edges[1] && edges[2] && edges[3]) {
				index = 22;
			}

			if(index == 26 && !edges[0] && edges[1] && !edges[2] && edges[3]) {
				index = 23;
			}

			if(index == 26 && edges[0] && !edges[1] && edges[2] && !edges[3]) {
				index = 10;
			}

			if(index == 26 && edges[0] && !edges[1] && !edges[2] && edges[3]) {
				index = 34;
			}

			if(index == 26 && !edges[0] && edges[1] && edges[2] && !edges[3]) {
				index = 35;
			}

			if(index == 26 && edges[0] && !edges[1] && !edges[2] && !edges[3]) {
				index = 32;
			}

			if(index == 26 && !edges[0] && edges[1] && !edges[2] && !edges[3]) {
				index = 33;
			}

			if(index == 26 && !edges[0] && !edges[1] && edges[2] && !edges[3]) {
				index = 44;
			}

			if(index == 26 && !edges[0] && !edges[1] && !edges[2] && edges[3]) {
				index = 45;
			}

			return cp.textureId * 256 + cp.tiles[index];
		}
	}

	private static boolean isNeighbour(ConnectedProperties cp, IBlockAccess iblockaccess, int x, int y, int z, int id, int side, int tileNum) {
		int blockId = iblockaccess.getBlockId(x, y, z);
		if(cp.connect == 2) {
			Block neighbourBlock = Block.blocksList[blockId];
			if(neighbourBlock == null) {
				return false;
			} else {
				int neighbourTileNum = neighbourBlock.getBlockTexture(iblockaccess, x, y, z, side);
				return neighbourTileNum == tileNum;
			}
		} else {
			return blockId == id;
		}
	}

	private static int getConnectedTextureHorizontal(ConnectedProperties cp, IBlockAccess blockAccess, Block block, int x, int y, int z, int side, int tileNum) {
		if(side != 0 && side != 1) {
			boolean left = false;
			boolean right = false;
			int id = block.blockID;
			switch(side) {
			case 2:
				left = isNeighbour(cp, blockAccess, x + 1, y, z, id, side, tileNum);
				right = isNeighbour(cp, blockAccess, x - 1, y, z, id, side, tileNum);
				break;
			case 3:
				left = isNeighbour(cp, blockAccess, x - 1, y, z, id, side, tileNum);
				right = isNeighbour(cp, blockAccess, x + 1, y, z, id, side, tileNum);
				break;
			case 4:
				left = isNeighbour(cp, blockAccess, x, y, z - 1, id, side, tileNum);
				right = isNeighbour(cp, blockAccess, x, y, z + 1, id, side, tileNum);
				break;
			case 5:
				left = isNeighbour(cp, blockAccess, x, y, z + 1, id, side, tileNum);
				right = isNeighbour(cp, blockAccess, x, y, z - 1, id, side, tileNum);
			}

			boolean index = true;
			byte index1;
			if(left) {
				if(right) {
					index1 = 1;
				} else {
					index1 = 2;
				}
			} else if(right) {
				index1 = 0;
			} else {
				index1 = 3;
			}

			return cp.textureId * 256 + cp.tiles[index1];
		} else {
			return -1;
		}
	}

	private static int getConnectedTextureVertical(ConnectedProperties cp, IBlockAccess blockAccess, Block block, int x, int y, int z, int side, int tileNum) {
		if(side != 0 && side != 1) {
			int id = block.blockID;
			boolean bottom = isNeighbour(cp, blockAccess, x, y - 1, z, id, side, tileNum);
			boolean top = isNeighbour(cp, blockAccess, x, y + 1, z, id, side, tileNum);
			boolean index = true;
			byte index1;
			if(bottom) {
				if(top) {
					index1 = 1;
				} else {
					index1 = 2;
				}
			} else if(top) {
				index1 = 0;
			} else {
				index1 = 3;
			}

			return cp.textureId * 256 + cp.tiles[index1];
		} else {
			return -1;
		}
	}

	private static int getConnectedTextureTop(ConnectedProperties cp, IBlockAccess blockAccess, Block block, int x, int y, int z, int side, int tileNum) {
		if(side != 0 && side != 1) {
			int blockId = block.blockID;
			return isNeighbour(cp, blockAccess, x, y + 1, z, blockId, side, tileNum) ? cp.textureId * 256 + cp.tiles[0] : -1;
		} else {
			return -1;
		}
	}

	public static boolean isConnectedGlassPanes() {
		return Config.isConnectedTextures() && matchingCtmPng;
	}

	private static boolean getMatchingCtmPng(RenderEngine re) {
		Dimension dimCtm = re.getTextureDimensions(re.getTexture("/ctm.png"));
		if(dimCtm == null) {
			return false;
		} else {
			Dimension dimTerrain = re.getTextureDimensions(re.getTexture("/terrain.png"));
			return dimTerrain == null ? false : dimCtm.width == dimTerrain.width && dimCtm.height == dimTerrain.height;
		}
	}

	private static ConnectedProperties makeDefaultProperties(String methodStr, RenderEngine re) {
		Properties props = new Properties();
		props.put("source", "/ctm.png");
		props.put("method", methodStr);
		ConnectedProperties cp = new ConnectedProperties(props);
		cp.isValid("(default)");
		cp.textureId = re.getTexture(cp.source);
		return cp;
	}
}
