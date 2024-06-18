package net.minecraft.src;

import java.util.ArrayList;
import java.util.Properties;

public class ConnectedProperties {
	public int method = 0;
	public String source = null;
	public int[] tiles = null;
	public int connect = 0;
	public int faces = 63;
	public int[] metadata = null;
	public int[] weights = null;
	public int symmetry = 1;
	public int width = 0;
	public int height = 0;
	public int[] sumWeights = null;
	public int sumAllWeights = 0;
	public int textureId = -1;
	public static final int METHOD_NONE = 0;
	public static final int METHOD_CTM = 1;
	public static final int METHOD_HORIZONTAL = 2;
	public static final int METHOD_TOP = 3;
	public static final int METHOD_RANDOM = 4;
	public static final int METHOD_REPEAT = 5;
	public static final int METHOD_VERTICAL = 6;
	public static final int CONNECT_NONE = 0;
	public static final int CONNECT_BLOCK = 1;
	public static final int CONNECT_TILE = 2;
	public static final int FACE_NONE = 0;
	public static final int FACE_BOTTOM = 1;
	public static final int FACE_TOP = 2;
	public static final int FACE_EAST = 4;
	public static final int FACE_WEST = 8;
	public static final int FACE_NORTH = 16;
	public static final int FACE_SOUTH = 32;
	public static final int FACE_SIDES = 60;
	public static final int FACE_ALL = 63;
	public static final int SYMMETRY_NONE = 1;
	public static final int SYMMETRY_OPPOSITE = 2;
	public static final int SYMMETRY_ALL = 6;

	public ConnectedProperties(Properties props) {
		this.method = this.parseMethod(props.getProperty("method"));
		this.source = props.getProperty("source");
		this.tiles = this.parseInts(props.getProperty("tiles"));
		this.connect = this.parseConnect(props.getProperty("connect"));
		this.faces = this.parseFaces(props.getProperty("faces"));
		this.metadata = this.parseInts(props.getProperty("metadata"));
		this.weights = this.parseInts(props.getProperty("weights"));
		this.symmetry = this.parseSymmetry(props.getProperty("symmetry"));
		this.width = this.parseInt(props.getProperty("width"));
		this.height = this.parseInt(props.getProperty("height"));
	}

	private int parseInt(String str) {
		if(str == null) {
			return -1;
		} else {
			int num = Config.parseInt(str, -1);
			if(num < 0) {
				Config.dbg("Invalid number: " + str);
			}

			return num;
		}
	}

	private int parseSymmetry(String str) {
		if(str == null) {
			return 1;
		} else if(str.equals("opposite")) {
			return 2;
		} else if(str.equals("all")) {
			return 6;
		} else {
			Config.dbg("Unknown symmetry: " + str);
			return 1;
		}
	}

	private int parseFaces(String str) {
		if(str == null) {
			return 63;
		} else {
			String[] faceStrs = Config.tokenize(str, " ,");
			int facesMask = 0;

			for(int i = 0; i < faceStrs.length; ++i) {
				String faceStr = faceStrs[i];
				int faceMask = this.parseFace(faceStr);
				facesMask |= faceMask;
			}

			return facesMask;
		}
	}

	private int parseFace(String str) {
		if(str.equals("bottom")) {
			return 1;
		} else if(str.equals("top")) {
			return 2;
		} else if(str.equals("north")) {
			return 4;
		} else if(str.equals("south")) {
			return 8;
		} else if(str.equals("east")) {
			return 32;
		} else if(str.equals("west")) {
			return 16;
		} else if(str.equals("sides")) {
			return 60;
		} else if(str.equals("all")) {
			return 63;
		} else {
			Config.dbg("Unknown face: " + str);
			return 0;
		}
	}

	private int parseConnect(String str) {
		if(str == null) {
			return 0;
		} else if(str.equals("block")) {
			return 1;
		} else if(str.equals("tile")) {
			return 2;
		} else {
			Config.dbg("Unknown connect: " + str);
			return 0;
		}
	}

	private int[] parseInts(String str) {
		if(str == null) {
			return null;
		} else {
			ArrayList list = new ArrayList();
			String[] intStrs = Config.tokenize(str, " ,");

			for(int ints = 0; ints < intStrs.length; ++ints) {
				String i = intStrs[ints];
				if(i.contains("-")) {
					String[] string12 = Config.tokenize(i, "-");
					if(string12.length != 2) {
						Config.dbg("Invalid interval: " + i + ", when parsing: " + str);
					} else {
						int min = Config.parseInt(string12[0], -1);
						int max = Config.parseInt(string12[1], -1);
						if(min >= 0 && max >= 0 && min <= max) {
							for(int n = min; n <= max; ++n) {
								list.add(n);
							}
						} else {
							Config.dbg("Invalid interval: " + i + ", when parsing: " + str);
						}
					}
				} else {
					int val = Config.parseInt(i, -1);
					if(val < 0) {
						Config.dbg("Invalid number: " + i + ", when parsing: " + str);
					} else {
						list.add(val);
					}
				}
			}

			int[] i10 = new int[list.size()];

			for(int i11 = 0; i11 < i10.length; ++i11) {
				i10[i11] = ((Integer)list.get(i11)).intValue();
			}

			return i10;
		}
	}

	private int parseMethod(String str) {
		if(str == null) {
			return 1;
		} else if(str.equals("ctm")) {
			return 1;
		} else if(str.equals("horizontal")) {
			return 2;
		} else if(str.equals("vertical")) {
			return 6;
		} else if(str.equals("top")) {
			return 3;
		} else if(str.equals("random")) {
			return 4;
		} else if(str.equals("repeat")) {
			return 5;
		} else {
			Config.dbg("Unknown method: " + str);
			return 0;
		}
	}

	public boolean isValid(String path) {
		if(this.source == null) {
			Config.dbg("No source texture: " + path);
			return false;
		} else if(this.method == 0) {
			Config.dbg("No method: " + path);
			return false;
		} else {
			if(this.tiles != null) {
				for(int i = 0; i < this.tiles.length; ++i) {
					int tileNum = this.tiles[i];
					if(tileNum < 0 || tileNum > 255) {
						Config.dbg("Invalid tile: " + tileNum + ", in " + path);
						return false;
					}
				}
			}

			switch(this.method) {
			case 1:
				return this.isValidCtm(path);
			case 2:
				return this.isValidHorizontal(path);
			case 3:
				return this.isValidTop(path);
			case 4:
				return this.isValidRandom(path);
			case 5:
				return this.isValidRepeat(path);
			case 6:
				return this.isValidVertical(path);
			default:
				Config.dbg("Unknown method: " + path);
				return false;
			}
		}
	}

	private boolean isValidCtm(String path) {
		if(this.tiles == null) {
			this.tiles = this.parseInts("0-11 16-27 32-43 48-59");
		}

		if(this.tiles.length != 48) {
			Config.dbg("Invalid tiles, must be exactly 48: " + path);
			return false;
		} else {
			return true;
		}
	}

	private boolean isValidHorizontal(String path) {
		if(this.tiles == null) {
			this.tiles = this.parseInts("12-15");
		}

		if(this.tiles.length != 4) {
			Config.dbg("Invalid tiles, must be exactly 4: " + path);
			return false;
		} else {
			return true;
		}
	}

	private boolean isValidVertical(String path) {
		if(this.tiles == null) {
			Config.dbg("No tiles defined for vertical: " + path);
			return false;
		} else if(this.tiles.length != 4) {
			Config.dbg("Invalid tiles, must be exactly 4: " + path);
			return false;
		} else {
			return true;
		}
	}

	private boolean isValidRandom(String path) {
		if(this.tiles == null) {
			Config.dbg("Tiles not defined: " + path);
			return false;
		} else if(this.weights != null && this.weights.length != this.tiles.length) {
			Config.dbg("Number of weights must equal the number of tiles: " + path);
			return false;
		} else {
			if(this.weights != null) {
				this.sumWeights = new int[this.weights.length];
				int sum = 0;

				for(int i = 0; i < this.weights.length; ++i) {
					sum += this.weights[i];
					this.sumWeights[i] = sum;
				}

				this.sumAllWeights = sum;
			}

			return true;
		}
	}

	private boolean isValidRepeat(String path) {
		if(this.tiles == null) {
			Config.dbg("Tiles not defined: " + path);
			return false;
		} else if(this.width >= 0 && this.width <= 16) {
			if(this.height >= 0 && this.height <= 16) {
				if(this.tiles.length != this.width * this.height) {
					Config.dbg("Number of tiles does not equal width x height: " + path);
					return false;
				} else {
					return true;
				}
			} else {
				Config.dbg("Invalid height: " + path);
				return false;
			}
		} else {
			Config.dbg("Invalid width: " + path);
			return false;
		}
	}

	private boolean isValidTop(String path) {
		if(this.tiles == null) {
			this.tiles = this.parseInts("66");
		}

		if(this.tiles.length != 1) {
			Config.dbg("Invalid tiles, must be exactly 1: " + path);
			return false;
		} else {
			return true;
		}
	}
}
