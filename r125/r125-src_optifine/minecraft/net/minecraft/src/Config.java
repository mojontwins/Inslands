package net.minecraft.src;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import net.minecraft.client.Minecraft;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public class Config {
	public static final String OF_NAME = "OptiFine";
	public static final String MC_VERSION = "1.2.5";
	public static final String OF_EDITION = "HD_U";
	public static final String OF_RELEASE = "C7";
	public static final String VERSION = "OptiFine_1.2.5_HD_U_C7";
	private static String newRelease = null;
	private static GameSettings gameSettings = null;
	private static Minecraft minecraft = null;
	private static int iconWidthTerrain = 16;
	private static int iconWidthItems = 16;
	private static Map foundClassesMap = new HashMap();
	private static long textureUpdateTime = 0L;
	private static DisplayMode desktopDisplayMode = null;
	private static int antialiasingLevel = 0;
	private static File logFile = null;
	public static final Boolean DEF_FOG_FANCY = true;
	public static final Float DEF_FOG_START = 0.2F;
	public static final Boolean DEF_OPTIMIZE_RENDER_DISTANCE = false;
	public static final Boolean DEF_OCCLUSION_ENABLED = false;
	public static final Integer DEF_MIPMAP_LEVEL = 0;
	public static final Integer DEF_MIPMAP_TYPE = 9984;
	public static final Float DEF_ALPHA_FUNC_LEVEL = 0.1F;
	public static final Boolean DEF_LOAD_CHUNKS_FAR = false;
	public static final Integer DEF_PRELOADED_CHUNKS = 0;
	public static final Integer DEF_CHUNKS_LIMIT = 25;
	public static final Integer DEF_UPDATES_PER_FRAME = 3;
	public static final Boolean DEF_DYNAMIC_UPDATES = false;

	public static String getVersion() {
		return "OptiFine_1.2.5_HD_U_C7";
	}

	private static void checkOpenGlCaps() {
		log("");
		log(getVersion());
		log("" + new Date());
		log("OS: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version"));
		log("Java: " + System.getProperty("java.version") + ", " + System.getProperty("java.vendor"));
		log("VM: " + System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor"));
		log("LWJGL: " + Sys.getVersion());
		log("OpenGL: " + GL11.glGetString(GL11.GL_RENDERER) + " version " + GL11.glGetString(GL11.GL_VERSION) + ", " + GL11.glGetString(GL11.GL_VENDOR));
		int ver = getOpenGlVersion();
		String verStr = "" + ver / 10 + "." + ver % 10;
		log("OpenGL Version: " + verStr);
		if(!GLContext.getCapabilities().OpenGL12) {
			log("OpenGL Mipmap levels: Not available (GL12.GL_TEXTURE_MAX_LEVEL)");
		}

		if(!GLContext.getCapabilities().GL_NV_fog_distance) {
			log("OpenGL Fancy fog: Not available (GL_NV_fog_distance)");
		}

		if(!GLContext.getCapabilities().GL_ARB_occlusion_query) {
			log("OpenGL Occlussion culling: Not available (GL_ARB_occlusion_query)");
		}

	}

	public static boolean isFancyFogAvailable() {
		return GLContext.getCapabilities().GL_NV_fog_distance;
	}

	public static boolean isOcclusionAvailable() {
		return GLContext.getCapabilities().GL_ARB_occlusion_query;
	}

	private static int getOpenGlVersion() {
		return !GLContext.getCapabilities().OpenGL11 ? 10 : (!GLContext.getCapabilities().OpenGL12 ? 11 : (!GLContext.getCapabilities().OpenGL13 ? 12 : (!GLContext.getCapabilities().OpenGL14 ? 13 : (!GLContext.getCapabilities().OpenGL15 ? 14 : (!GLContext.getCapabilities().OpenGL20 ? 15 : (!GLContext.getCapabilities().OpenGL21 ? 20 : (!GLContext.getCapabilities().OpenGL30 ? 21 : (!GLContext.getCapabilities().OpenGL31 ? 30 : (!GLContext.getCapabilities().OpenGL32 ? 31 : (!GLContext.getCapabilities().OpenGL33 ? 32 : (!GLContext.getCapabilities().OpenGL40 ? 33 : 40)))))))))));
	}

	public static void setGameSettings(GameSettings options) {
		if(gameSettings == null) {
			checkOpenGlCaps();
			startVersionCheckThread();
		}

		gameSettings = options;
		minecraft = gameSettings.mc;
		if(gameSettings != null) {
			antialiasingLevel = gameSettings.ofAaLevel;
		}

	}

	private static void startVersionCheckThread() {
		VersionCheckThread vct = new VersionCheckThread();
		vct.start();
	}

	public static boolean isUseMipmaps() {
		int mipmapLevel = getMipmapLevel();
		return mipmapLevel > 0;
	}

	public static int getMipmapLevel() {
		return gameSettings == null ? DEF_MIPMAP_LEVEL.intValue() : gameSettings.ofMipmapLevel;
	}

	public static int getMipmapType() {
		return gameSettings == null ? DEF_MIPMAP_TYPE.intValue() : (gameSettings.ofMipmapLinear ? 9986 : 9984);
	}

	public static boolean isUseAlphaFunc() {
		float alphaFuncLevel = getAlphaFuncLevel();
		return alphaFuncLevel > DEF_ALPHA_FUNC_LEVEL.floatValue() + 1.0E-5F;
	}

	public static float getAlphaFuncLevel() {
		return DEF_ALPHA_FUNC_LEVEL.floatValue();
	}

	public static boolean isFogFancy() {
		return !isFancyFogAvailable() ? false : (gameSettings == null ? false : gameSettings.ofFogType == 2);
	}

	public static boolean isFogFast() {
		return gameSettings == null ? false : gameSettings.ofFogType == 1;
	}

	public static boolean isFogOff() {
		return gameSettings == null ? false : gameSettings.ofFogType == 3;
	}

	public static float getFogStart() {
		return gameSettings == null ? DEF_FOG_START.floatValue() : gameSettings.ofFogStart;
	}

	public static boolean isOcclusionEnabled() {
		return gameSettings == null ? DEF_OCCLUSION_ENABLED.booleanValue() : gameSettings.advancedOpengl;
	}

	public static boolean isOcclusionFancy() {
		return !isOcclusionEnabled() ? false : (gameSettings == null ? false : gameSettings.ofOcclusionFancy);
	}

	public static boolean isLoadChunksFar() {
		return gameSettings == null ? DEF_LOAD_CHUNKS_FAR.booleanValue() : gameSettings.ofLoadFar;
	}

	public static int getPreloadedChunks() {
		return gameSettings == null ? DEF_PRELOADED_CHUNKS.intValue() : gameSettings.ofPreloadedChunks;
	}

	public static void dbg(String s) {
		System.out.println(s);
	}

	public static void log(String s) {
		dbg(s);

		try {
			if(logFile == null) {
				logFile = new File(Minecraft.getMinecraftDir(), "optifog.log");
				logFile.delete();
				logFile.createNewFile();
			}

			FileOutputStream e = new FileOutputStream(logFile, true);
			OutputStreamWriter logFileWriter = new OutputStreamWriter(e, "ASCII");

			try {
				logFileWriter.write(s);
				logFileWriter.write("\n");
				logFileWriter.flush();
			} finally {
				logFileWriter.close();
			}
		} catch (IOException iOException7) {
			iOException7.printStackTrace();
		}

	}

	public static int getUpdatesPerFrame() {
		return gameSettings != null ? gameSettings.ofChunkUpdates : 1;
	}

	public static boolean isDynamicUpdates() {
		return gameSettings != null ? gameSettings.ofChunkUpdatesDynamic : true;
	}

	public static boolean isRainFancy() {
		return gameSettings.ofRain == 0 ? gameSettings.fancyGraphics : gameSettings.ofRain == 2;
	}

	public static boolean isWaterFancy() {
		return gameSettings.ofWater == 0 ? gameSettings.fancyGraphics : gameSettings.ofWater == 2;
	}

	public static boolean isRainOff() {
		return gameSettings.ofRain == 3;
	}

	public static boolean isCloudsFancy() {
		return gameSettings.ofClouds == 0 ? gameSettings.fancyGraphics : gameSettings.ofClouds == 2;
	}

	public static boolean isCloudsOff() {
		return gameSettings.ofClouds == 3;
	}

	public static boolean isTreesFancy() {
		return gameSettings.ofTrees == 0 ? gameSettings.fancyGraphics : gameSettings.ofTrees == 2;
	}

	public static boolean isGrassFancy() {
		return gameSettings.ofGrass == 0 ? gameSettings.fancyGraphics : gameSettings.ofGrass == 2;
	}

	public static int limit(int val, int min, int max) {
		return val < min ? min : (val > max ? max : val);
	}

	public static float limit(float val, float min, float max) {
		return val < min ? min : (val > max ? max : val);
	}

	public static float limitTo1(float val) {
		return val < 0.0F ? 0.0F : (val > 1.0F ? 1.0F : val);
	}

	public static boolean isAnimatedWater() {
		return gameSettings != null ? gameSettings.ofAnimatedWater != 2 : true;
	}

	public static boolean isGeneratedWater() {
		return gameSettings != null ? gameSettings.ofAnimatedWater == 1 : true;
	}

	public static boolean isAnimatedPortal() {
		return gameSettings != null ? gameSettings.ofAnimatedPortal : true;
	}

	public static boolean isAnimatedLava() {
		return gameSettings != null ? gameSettings.ofAnimatedLava != 2 : true;
	}

	public static boolean isGeneratedLava() {
		return gameSettings != null ? gameSettings.ofAnimatedLava == 1 : true;
	}

	public static boolean isAnimatedFire() {
		return gameSettings != null ? gameSettings.ofAnimatedFire : true;
	}

	public static boolean isAnimatedRedstone() {
		return gameSettings != null ? gameSettings.ofAnimatedRedstone : true;
	}

	public static boolean isAnimatedExplosion() {
		return gameSettings != null ? gameSettings.ofAnimatedExplosion : true;
	}

	public static boolean isAnimatedFlame() {
		return gameSettings != null ? gameSettings.ofAnimatedFlame : true;
	}

	public static boolean isAnimatedSmoke() {
		return gameSettings != null ? gameSettings.ofAnimatedSmoke : true;
	}

	public static boolean isVoidParticles() {
		return gameSettings != null ? gameSettings.ofVoidParticles : true;
	}

	public static boolean isWaterParticles() {
		return gameSettings != null ? gameSettings.ofWaterParticles : true;
	}

	public static boolean isRainSplash() {
		return gameSettings != null ? gameSettings.ofRainSplash : true;
	}

	public static boolean isPortalParticles() {
		return gameSettings != null ? gameSettings.ofPortalParticles : true;
	}

	public static boolean isDepthFog() {
		return gameSettings != null ? gameSettings.ofDepthFog : true;
	}

	public static float getAmbientOcclusionLevel() {
		return gameSettings != null ? gameSettings.ofAoLevel : 0.0F;
	}

	private static Method getMethod(Class cls, String methodName, Object[] params) {
		Method[] methods = cls.getMethods();

		for(int i = 0; i < methods.length; ++i) {
			Method m = methods[i];
			if(m.getName().equals(methodName) && m.getParameterTypes().length == params.length) {
				return m;
			}
		}

		dbg("No method found for: " + cls.getName() + "." + methodName + "(" + arrayToString(params) + ")");
		return null;
	}

	public static String arrayToString(Object[] arr) {
		StringBuffer buf = new StringBuffer(arr.length * 5);

		for(int i = 0; i < arr.length; ++i) {
			Object obj = arr[i];
			if(i > 0) {
				buf.append(", ");
			}

			buf.append(String.valueOf(obj));
		}

		return buf.toString();
	}

	public static Minecraft getMinecraft() {
		return minecraft;
	}

	public static int getIconWidthTerrain() {
		return iconWidthTerrain;
	}

	public static int getIconWidthItems() {
		return iconWidthItems;
	}

	public static void setIconWidthItems(int iconWidth) {
		iconWidthItems = iconWidth;
	}

	public static void setIconWidthTerrain(int iconWidth) {
		iconWidthTerrain = iconWidth;
	}

	public static int getMaxDynamicTileWidth() {
		return 64;
	}

	public static int getSideGrassTexture(IBlockAccess blockAccess, int x, int y, int z, int side, int tileNum) {
		if(!isBetterGrass()) {
			return tileNum;
		} else {
			byte fullTileNum = 0;
			byte destBlockId = 2;
			if(tileNum == 77) {
				fullTileNum = 78;
				destBlockId = 110;
			}

			if(isBetterGrassFancy()) {
				--y;
				switch(side) {
				case 2:
					--z;
					break;
				case 3:
					++z;
					break;
				case 4:
					--x;
					break;
				case 5:
					++x;
				}

				int blockId = blockAccess.getBlockId(x, y, z);
				if(blockId != destBlockId) {
					return tileNum;
				}
			}

			return fullTileNum;
		}
	}

	public static int getSideSnowGrassTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		if(!isBetterGrass()) {
			return 68;
		} else {
			if(isBetterGrassFancy()) {
				switch(side) {
				case 2:
					--z;
					break;
				case 3:
					++z;
					break;
				case 4:
					--x;
					break;
				case 5:
					++x;
				}

				int blockId = blockAccess.getBlockId(x, y, z);
				if(blockId != 78 && blockId != 80) {
					return 68;
				}
			}

			return 66;
		}
	}

	public static boolean isBetterGrass() {
		return gameSettings == null ? false : gameSettings.ofBetterGrass != 3;
	}

	public static boolean isBetterGrassFancy() {
		return gameSettings == null ? false : gameSettings.ofBetterGrass == 2;
	}

	public static long getTextureUpdateTime() {
		return textureUpdateTime;
	}

	public static void setTextureUpdateTime(long fontRendererUpdateTime) {
		textureUpdateTime = fontRendererUpdateTime;
	}

	public static boolean isWeatherEnabled() {
		return gameSettings == null ? true : gameSettings.ofWeather;
	}

	public static boolean isSkyEnabled() {
		return gameSettings == null ? true : gameSettings.ofSky;
	}

	public static boolean isSunMoonEnabled() {
		return gameSettings == null ? true : gameSettings.ofSunMoon;
	}

	public static boolean isStarsEnabled() {
		return gameSettings == null ? true : gameSettings.ofStars;
	}

	public static void sleep(long ms) {
		try {
			Thread.currentThread();
			Thread.sleep(ms);
		} catch (InterruptedException interruptedException3) {
			interruptedException3.printStackTrace();
		}

	}

	public static boolean isTimeDayOnly() {
		return gameSettings == null ? false : gameSettings.ofTime == 1;
	}

	public static boolean isTimeNightOnly() {
		return gameSettings == null ? false : gameSettings.ofTime == 3;
	}

	public static boolean isClearWater() {
		return gameSettings == null ? false : gameSettings.ofClearWater;
	}

	public static int getAnisotropicFilterLevel() {
		return gameSettings == null ? 1 : gameSettings.ofAfLevel;
	}

	public static int getAntialiasingLevel() {
		return antialiasingLevel;
	}

	public static boolean between(int val, int min, int max) {
		return val >= min && val <= max;
	}

	public static boolean isMultiTexture() {
		return getAnisotropicFilterLevel() > 1 ? true : getAntialiasingLevel() > 0;
	}

	public static boolean isDrippingWaterLava() {
		return gameSettings == null ? false : gameSettings.ofDrippingWaterLava;
	}

	public static boolean isBetterSnow() {
		return gameSettings == null ? false : gameSettings.ofBetterSnow;
	}

	public static Dimension getFullscreenDimension() {
		if(gameSettings == null) {
			return new Dimension(desktopDisplayMode.getWidth(), desktopDisplayMode.getHeight());
		} else {
			String dimStr = gameSettings.ofFullscreenMode;
			if(dimStr.equals("Default")) {
				return new Dimension(desktopDisplayMode.getWidth(), desktopDisplayMode.getHeight());
			} else {
				String[] dimStrs = tokenize(dimStr, " x");
				return dimStrs.length < 2 ? new Dimension(desktopDisplayMode.getWidth(), desktopDisplayMode.getHeight()) : new Dimension(parseInt(dimStrs[0], -1), parseInt(dimStrs[1], -1));
			}
		}
	}

	public static int parseInt(String str, int defVal) {
		try {
			return str == null ? defVal : Integer.parseInt(str);
		} catch (NumberFormatException numberFormatException3) {
			return defVal;
		}
	}

	public static float parseFloat(String str, float defVal) {
		try {
			return str == null ? defVal : Float.parseFloat(str);
		} catch (NumberFormatException numberFormatException3) {
			return defVal;
		}
	}

	public static String[] tokenize(String str, String delim) {
		StringTokenizer tok = new StringTokenizer(str, delim);
		ArrayList list = new ArrayList();

		while(tok.hasMoreTokens()) {
			String strs = tok.nextToken();
			list.add(strs);
		}

		String[] strs1 = (String[])((String[])list.toArray(new String[list.size()]));
		return strs1;
	}

	public static DisplayMode getDesktopDisplayMode() {
		return desktopDisplayMode;
	}

	public static void setDesktopDisplayMode(DisplayMode desktopDisplayMode) {
		desktopDisplayMode = desktopDisplayMode;
	}

	public static DisplayMode[] getFullscreenDisplayModes() {
		try {
			DisplayMode[] e = Display.getAvailableDisplayModes();
			ArrayList list = new ArrayList();

			for(int fsModes = 0; fsModes < e.length; ++fsModes) {
				DisplayMode comp = e[fsModes];
				if(desktopDisplayMode == null || comp.getBitsPerPixel() == desktopDisplayMode.getBitsPerPixel() && comp.getFrequency() == desktopDisplayMode.getFrequency()) {
					list.add(comp);
				}
			}

			DisplayMode[] displayMode5 = (DisplayMode[])((DisplayMode[])list.toArray(new DisplayMode[list.size()]));
			Comparator comparator6 = new Comparator() {
				public int compare(Object o1, Object o2) {
					DisplayMode dm1 = (DisplayMode)o1;
					DisplayMode dm2 = (DisplayMode)o2;
					return dm1.getWidth() != dm2.getWidth() ? dm2.getWidth() - dm1.getWidth() : (dm1.getHeight() != dm2.getHeight() ? dm2.getHeight() - dm1.getHeight() : 0);
				}
			};
			Arrays.sort(displayMode5, comparator6);
			return displayMode5;
		} catch (Exception exception4) {
			exception4.printStackTrace();
			return new DisplayMode[]{desktopDisplayMode};
		}
	}

	public static String[] getFullscreenModes() {
		DisplayMode[] modes = getFullscreenDisplayModes();
		String[] names = new String[modes.length];

		for(int i = 0; i < modes.length; ++i) {
			DisplayMode mode = modes[i];
			String name = "" + mode.getWidth() + "x" + mode.getHeight();
			names[i] = name;
		}

		return names;
	}

	public static DisplayMode getDisplayMode(Dimension dim) throws LWJGLException {
		DisplayMode[] modes = Display.getAvailableDisplayModes();

		for(int i = 0; i < modes.length; ++i) {
			DisplayMode dm = modes[i];
			if(dm.getWidth() == dim.width && dm.getHeight() == dim.height && (desktopDisplayMode == null || dm.getBitsPerPixel() == desktopDisplayMode.getBitsPerPixel() && dm.getFrequency() == desktopDisplayMode.getFrequency())) {
				return dm;
			}
		}

		return desktopDisplayMode;
	}

	public static boolean isAnimatedTerrain() {
		return gameSettings != null ? gameSettings.ofAnimatedTerrain : true;
	}

	public static boolean isAnimatedItems() {
		return gameSettings != null ? gameSettings.ofAnimatedItems : true;
	}

	public static boolean isSwampColors() {
		return gameSettings != null ? gameSettings.ofSwampColors : true;
	}

	public static boolean isRandomMobs() {
		return gameSettings != null ? gameSettings.ofRandomMobs : true;
	}

	public static void checkGlError(String loc) {
		int i = GL11.glGetError();
		if(i != 0) {
			String text = GLU.gluErrorString(i);
			System.out.println("OpenGlError: " + i + " (" + text + "), at: " + loc);
		}

	}

	public static boolean isSmoothBiomes() {
		return gameSettings != null ? gameSettings.ofSmoothBiomes : true;
	}

	public static boolean isCustomColors() {
		return gameSettings != null ? gameSettings.ofCustomColors : true;
	}

	public static boolean isShowCapes() {
		return gameSettings != null ? gameSettings.ofShowCapes : true;
	}

	public static boolean isConnectedTextures() {
		return gameSettings != null ? gameSettings.ofConnectedTextures != 3 : false;
	}

	public static boolean isNaturalTextures() {
		return gameSettings != null ? gameSettings.ofNaturalTextures : false;
	}

	public static boolean isConnectedTexturesFancy() {
		return gameSettings != null ? gameSettings.ofConnectedTextures == 2 : false;
	}

	public static String[] readLines(File file) throws IOException {
		ArrayList list = new ArrayList();
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, "ASCII");
		BufferedReader br = new BufferedReader(isr);

		while(true) {
			String lines = br.readLine();
			if(lines == null) {
				String[] lines1 = (String[])((String[])list.toArray(new String[list.size()]));
				return lines1;
			}

			list.add(lines);
		}
	}

	public static String readFile(File file) throws IOException {
		FileInputStream fin = new FileInputStream(file);
		return readInputStream(fin, "ASCII");
	}

	public static String readInputStream(InputStream in) throws IOException {
		return readInputStream(in, "ASCII");
	}

	public static String readInputStream(InputStream in, String encoding) throws IOException {
		InputStreamReader inr = new InputStreamReader(in, encoding);
		BufferedReader br = new BufferedReader(inr);
		StringBuffer sb = new StringBuffer();

		while(true) {
			String line = br.readLine();
			if(line == null) {
				return sb.toString();
			}

			sb.append(line);
			sb.append("\n");
		}
	}

	public static GameSettings getGameSettings() {
		return gameSettings;
	}

	public static String getNewRelease() {
		return newRelease;
	}

	public static void setNewRelease(String newRelease) {
		newRelease = newRelease;
	}

	public static int compareRelease(String rel1, String rel2) {
		String[] rels1 = splitRelease(rel1);
		String[] rels2 = splitRelease(rel2);
		String branch1 = rels1[0];
		String branch2 = rels2[0];
		if(!branch1.equals(branch2)) {
			return branch1.compareTo(branch2);
		} else {
			int rev1 = parseInt(rels1[1], -1);
			int rev2 = parseInt(rels2[1], -1);
			if(rev1 != rev2) {
				return rev1 - rev2;
			} else {
				String suf1 = rels1[2];
				String suf2 = rels2[2];
				return suf1.compareTo(suf2);
			}
		}
	}

	private static String[] splitRelease(String relStr) {
		if(relStr != null && relStr.length() > 0) {
			String branch = relStr.substring(0, 1);
			if(relStr.length() <= 1) {
				return new String[]{branch, "", ""};
			} else {
				int pos;
				for(pos = 1; pos < relStr.length() && Character.isDigit(relStr.charAt(pos)); ++pos) {
				}

				String revision = relStr.substring(1, pos);
				if(pos >= relStr.length()) {
					return new String[]{branch, revision, ""};
				} else {
					String suffix = relStr.substring(pos);
					return new String[]{branch, revision, suffix};
				}
			}
		} else {
			return new String[]{"", "", ""};
		}
	}

	public static int intHash(int x) {
		x = x ^ 61 ^ x >> 16;
		x += x << 3;
		x ^= x >> 4;
		x *= 668265261;
		x ^= x >> 15;
		return x;
	}

	public static int getRandom(int x, int y, int z, int face) {
		int rand = intHash(face + 37);
		rand = intHash(rand + x);
		rand = intHash(rand + z);
		rand = intHash(rand + y);
		return rand;
	}
}
