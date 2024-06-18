package net.minecraft.src;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomMobs {
	private static Map textureVariantsMap = new HashMap();
	private static Random random = new Random();

	public static void entityLoaded(Entity entity) {
		if(entity.skinUrl == null) {
			if(entity instanceof EntityLiving) {
				if(!(entity instanceof EntityPlayer)) {
					EntityLiving el = (EntityLiving)entity;
					int randomId = el.persistentId;
					entity.skinUrl = "" + randomId;
				}
			}
		}
	}

	public static void worldChanged(World oldWorld, World newWorld) {
		if(newWorld != null) {
			List entityList = newWorld.getLoadedEntityList();

			for(int e = 0; e < entityList.size(); ++e) {
				Entity entity = (Entity)entityList.get(e);
				entityLoaded(entity);
			}
		}

	}

	public static int getTexture(String skinUrl, String texture) {
		if(texture == null) {
			return -1;
		} else if(skinUrl == null) {
			return -1;
		} else if(skinUrl.length() <= 1) {
			return -1;
		} else {
			char ch = skinUrl.charAt(0);
			if(ch >= 48 && ch <= 57) {
				int num = Math.abs(skinUrl.hashCode());
				String[] texs = (String[])((String[])textureVariantsMap.get(texture));
				if(texs == null) {
					texs = getTextureVariants(texture);
					textureVariantsMap.put(texture, texs);
				}

				if(texs != null && texs.length > 0) {
					int index = num % texs.length;
					String tex = texs[index];
					return Config.getMinecraft().renderEngine.getTexture(tex);
				} else {
					return -1;
				}
			} else {
				return -1;
			}
		}
	}

	private static String[] getTextureVariants(String texture) {
		RenderEngine renderEngine = Config.getMinecraft().renderEngine;
		String[] texs = new String[]{texture};
		int pointPos = texture.lastIndexOf(46);
		if(pointPos < 0) {
			return texs;
		} else {
			String prefix = texture.substring(0, pointPos);
			String suffix = texture.substring(pointPos);
			int countVariants = getCountTextureVariants(texture, prefix, suffix);
			if(countVariants <= 1) {
				return texs;
			} else {
				texs = new String[countVariants];
				texs[0] = texture;

				for(int i = 1; i < texs.length; ++i) {
					int texNum = i + 1;
					texs[i] = prefix + texNum + suffix;
				}

				Config.dbg("RandomMobs: " + texture + ", variants: " + texs.length);
				return texs;
			}
		}
	}

	private static int getCountTextureVariants(String texture, String prefix, String suffix) {
		RenderEngine renderEngine = Config.getMinecraft().renderEngine;
		short maxNum = 1000;

		for(int num = 2; num < maxNum; ++num) {
			String variant = prefix + num + suffix;

			try {
				InputStream e = renderEngine.texturePack.selectedTexturePack.getResourceAsStream(variant);
				if(e == null) {
					return num - 1;
				}

				e.close();
			} catch (IOException iOException8) {
				return num - 1;
			}
		}

		return maxNum;
	}

	public static void resetTextures() {
		textureVariantsMap.clear();
	}
}
