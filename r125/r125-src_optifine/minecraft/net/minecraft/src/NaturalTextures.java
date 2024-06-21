package net.minecraft.src;

import java.io.InputStream;
import java.util.ArrayList;

public class NaturalTextures {
	private static RenderEngine renderEngine = null;
	private static NaturalProperties[][] propertiesByTex = new NaturalProperties[0][];

	public static void update(RenderEngine re) {
		propertiesByTex = new NaturalProperties[0][];
		renderEngine = re;
		if(Config.isNaturalTextures()) {
			InputStream in = re.texturePack.selectedTexturePack.getResourceAsStream("/natural.properties");
			if(in == null) {
				Config.dbg("natural.properties not found");
				propertiesByTex = makeDefaultProperties();
			} else {
				try {
					ArrayList e = new ArrayList(1024);
					String configStr = Config.readInputStream(in);
					in.close();
					String[] configLines = Config.tokenize(configStr, "\n\r");
					Config.dbg("Parsing natural.properties");

					for(int i = 0; i < configLines.length; ++i) {
						String line = configLines[i].trim();
						if(!line.startsWith("#")) {
							String[] strs = Config.tokenize(line, "=");
							if(strs.length != 2) {
								Config.dbg("Invalid natural.properties line: " + line);
							} else {
								String key = strs[0].trim();
								String type = strs[1].trim();
								String[] keyStrs = Config.tokenize(key, ":");
								if(keyStrs.length != 2) {
									Config.dbg("Invalid natural.properties line: " + line);
								} else {
									String texName = keyStrs[0];
									String tileNumStr = keyStrs[1];
									int tileNum = Config.parseInt(tileNumStr, -1);
									if(tileNum >= 0 && tileNum <= 255) {
										NaturalProperties props = new NaturalProperties(type);
										if(props.isValid()) {
											int texNum = re.getTexture(texName);
											if(texNum >= 0) {
												while(e.size() <= texNum) {
													e.add((Object)null);
												}

												NaturalProperties[] propsByTile = (NaturalProperties[])((NaturalProperties[])e.get(texNum));
												if(propsByTile == null) {
													propsByTile = new NaturalProperties[256];
													e.set(texNum, propsByTile);
												}

												propsByTile[tileNum] = props;
											}
										}
									} else {
										Config.dbg("Invalid natural.properties line: " + line);
									}
								}
							}
						}
					}

					propertiesByTex = (NaturalProperties[][])((NaturalProperties[][])e.toArray(new NaturalProperties[e.size()][]));
				} catch (Exception exception17) {
					exception17.printStackTrace();
				}

			}
		}
	}

	public static NaturalProperties getNaturalProperties(int texId, int tileNum) {
		if(texId < 0) {
			return null;
		} else {
			if(texId == 0) {
				texId = renderEngine.terrainTextureId;
			}

			if(texId > propertiesByTex.length) {
				return null;
			} else {
				NaturalProperties[] propsByTile = propertiesByTex[texId];
				if(propsByTile == null) {
					return null;
				} else if(tileNum >= 0 && tileNum < propsByTile.length) {
					NaturalProperties props = propsByTile[tileNum];
					return props;
				} else {
					return null;
				}
			}
		}
	}

	private static NaturalProperties[][] makeDefaultProperties() {
		if(!renderEngine.texturePack.selectedTexturePack.texturePackFileName.equals("Default")) {
			Config.dbg("Texture pack is not default, ignoring default configuration for Natural Textures.");
			return new NaturalProperties[0][];
		} else {
			Config.dbg("Using default configuration for Natural Textures.");
			NaturalProperties[] terrainProps = new NaturalProperties[256];
			terrainProps[0] = new NaturalProperties("4F");
			terrainProps[1] = new NaturalProperties("2F");
			terrainProps[2] = new NaturalProperties("4F");
			terrainProps[3] = new NaturalProperties("F");
			terrainProps[38] = new NaturalProperties("F");
			terrainProps[6] = new NaturalProperties("F");
			terrainProps[17] = new NaturalProperties("2F");
			terrainProps[18] = new NaturalProperties("4F");
			terrainProps[19] = new NaturalProperties("4");
			terrainProps[20] = new NaturalProperties("2F");
			terrainProps[21] = new NaturalProperties("4F");
			terrainProps[32] = new NaturalProperties("2F");
			terrainProps[33] = new NaturalProperties("2F");
			terrainProps[34] = new NaturalProperties("2F");
			terrainProps[50] = new NaturalProperties("2F");
			terrainProps[51] = new NaturalProperties("2F");
			terrainProps[160] = new NaturalProperties("2F");
			terrainProps[37] = new NaturalProperties("4F");
			terrainProps[52] = new NaturalProperties("2F");
			terrainProps[53] = new NaturalProperties("2F");
			terrainProps[196] = new NaturalProperties("2");
			terrainProps[197] = new NaturalProperties("2");
			terrainProps[66] = new NaturalProperties("4F");
			terrainProps[68] = new NaturalProperties("F");
			terrainProps[70] = new NaturalProperties("2F");
			terrainProps[72] = new NaturalProperties("4F");
			terrainProps[77] = new NaturalProperties("F");
			terrainProps[78] = new NaturalProperties("4F");
			terrainProps[86] = new NaturalProperties("2F");
			terrainProps[87] = new NaturalProperties("2F");
			terrainProps[103] = new NaturalProperties("4F");
			terrainProps[104] = new NaturalProperties("4F");
			terrainProps[105] = new NaturalProperties("4");
			terrainProps[116] = new NaturalProperties("2F");
			terrainProps[117] = new NaturalProperties("F");
			terrainProps[132] = new NaturalProperties("2F");
			terrainProps[133] = new NaturalProperties("2F");
			terrainProps[153] = new NaturalProperties("2F");
			terrainProps[175] = new NaturalProperties("4");
			terrainProps[176] = new NaturalProperties("4");
			terrainProps[208] = new NaturalProperties("4F");
			terrainProps[211] = new NaturalProperties("4F");
			terrainProps[212] = new NaturalProperties("4F");
			int terrainTexId = renderEngine.terrainTextureId;
			NaturalProperties[][] defPropsByTex = new NaturalProperties[terrainTexId + 1][];
			defPropsByTex[terrainTexId] = terrainProps;
			return defPropsByTex;
		}
	}
}
