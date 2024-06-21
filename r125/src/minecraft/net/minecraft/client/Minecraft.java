package net.minecraft.client;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import net.minecraft.src.AchievementList;
import net.minecraft.src.AnvilSaveConverter;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.ChunkProviderLoadOrGenerate;
import net.minecraft.src.ColorizerFoliage;
import net.minecraft.src.ColorizerGrass;
import net.minecraft.src.ColorizerWater;
import net.minecraft.src.EffectRenderer;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.EnumOS2;
import net.minecraft.src.EnumOptions;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GameWindowListener;
import net.minecraft.src.GuiAchievement;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiConflictWarning;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiErrorScreen;
import net.minecraft.src.GuiGameOver;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.GuiIngameMenu;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiMemoryErrorScreen;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSleepMP;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.LoadingScreenRenderer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MinecraftError;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.MinecraftImpl;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.MouseHelper;
import net.minecraft.src.MovementInputFromOptions;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.OpenGlCapsChecker;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.Packet3Chat;
import net.minecraft.src.PlayerController;
import net.minecraft.src.PlayerUsageSnooper;
import net.minecraft.src.Profiler;
import net.minecraft.src.ProfilerResult;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.RenderManager;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.ScreenShotHelper;
import net.minecraft.src.Session;
import net.minecraft.src.SoundManager;
import net.minecraft.src.StatCollector;
import net.minecraft.src.StatFileWriter;
import net.minecraft.src.StatList;
import net.minecraft.src.StatStringFormatKeyInv;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.Teleporter;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TextureCompassFX;
import net.minecraft.src.TextureFlamesFX;
import net.minecraft.src.TextureLavaFX;
import net.minecraft.src.TextureLavaFlowFX;
import net.minecraft.src.TexturePackList;
import net.minecraft.src.TexturePortalFX;
import net.minecraft.src.TextureWatchFX;
import net.minecraft.src.TextureWaterFX;
import net.minecraft.src.TextureWaterFlowFX;
import net.minecraft.src.ThreadCheckHasPaid;
import net.minecraft.src.ThreadClientSleep;
import net.minecraft.src.ThreadDownloadResources;
import net.minecraft.src.Timer;
import net.minecraft.src.UnexpectedThrowable;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
import net.minecraft.src.WorldProvider;
import net.minecraft.src.WorldRenderer;
import net.minecraft.src.WorldSettings;
import net.minecraft.src.WorldType;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

public abstract class Minecraft implements Runnable {
	public static byte[] field_28006_b = new byte[10485760];
	private static Minecraft theMinecraft;
	public PlayerController playerController;
	private boolean fullscreen = false;
	private boolean hasCrashed = false;
	public int displayWidth;
	public int displayHeight;
	private OpenGlCapsChecker glCapabilities;
	private Timer timer = new Timer(20.0F);
	public World theWorld;
	public RenderGlobal renderGlobal;
	public EntityPlayerSP thePlayer;
	public EntityLiving renderViewEntity;
	public EffectRenderer effectRenderer;
	public Session session = null;
	public String minecraftUri;
	public Canvas mcCanvas;
	public boolean hideQuitButton = false;
	public volatile boolean isGamePaused = false;
	public RenderEngine renderEngine;
	public FontRenderer fontRenderer;
	public FontRenderer standardGalacticFontRenderer;
	public GuiScreen currentScreen = null;
	public LoadingScreenRenderer loadingScreen;
	public EntityRenderer entityRenderer;
	private ThreadDownloadResources downloadResourcesThread;
	private int ticksRan = 0;
	private int leftClickCounter = 0;
	private int tempDisplayWidth;
	private int tempDisplayHeight;
	public GuiAchievement guiAchievement = new GuiAchievement(this);
	public GuiIngame ingameGUI;
	public boolean skipRenderWorld = false;
	public ModelBiped playerModelBiped = new ModelBiped(0.0F);
	public MovingObjectPosition objectMouseOver = null;
	public GameSettings gameSettings;
	protected MinecraftApplet mcApplet;
	public SoundManager sndManager = new SoundManager();
	public MouseHelper mouseHelper;
	public TexturePackList texturePackList;
	public File mcDataDir;
	private ISaveFormat saveLoader;
	public static long[] frameTimes = new long[512];
	public static long[] tickTimes = new long[512];
	public static int numRecordedFrameTimes = 0;
	public static long hasPaidCheckTime = 0L;
	private int rightClickDelayTimer = 0;
	public StatFileWriter statFileWriter;
	private String serverName;
	private int serverPort;
	private TextureWaterFX textureWaterFX = new TextureWaterFX();
	private TextureLavaFX textureLavaFX = new TextureLavaFX();
	private static File minecraftDir = null;
	public volatile boolean running = true;
	public String debug = "";
	long debugUpdateTime = System.currentTimeMillis();
	int fpsCounter = 0;
	boolean isTakingScreenshot = false;
	long prevFrameTime = -1L;
	private String debugProfilerName = "root";
	public boolean inGameHasFocus = false;
	public boolean isRaining = false;
	long systemTime = System.currentTimeMillis();
	private int joinPlayerCounter = 0;

	public Minecraft(Component component1, Canvas canvas2, MinecraftApplet minecraftApplet3, int i4, int i5, boolean z6) {
		StatList.func_27360_a();
		this.tempDisplayHeight = i5;
		this.fullscreen = z6;
		this.mcApplet = minecraftApplet3;
		Packet3Chat.field_52010_b = 32767;
		new ThreadClientSleep(this, "Timer hack thread");
		this.mcCanvas = canvas2;
		this.displayWidth = i4;
		this.displayHeight = i5;
		this.fullscreen = z6;
		if(minecraftApplet3 == null || "true".equals(minecraftApplet3.getParameter("stand-alone"))) {
			this.hideQuitButton = false;
		}

		theMinecraft = this;
	}

	public void onMinecraftCrash(UnexpectedThrowable unexpectedThrowable1) {
		this.hasCrashed = true;
		this.displayUnexpectedThrowable(unexpectedThrowable1);
	}

	public abstract void displayUnexpectedThrowable(UnexpectedThrowable unexpectedThrowable1);

	public void setServer(String string1, int i2) {
		this.serverName = string1;
		this.serverPort = i2;
	}

	public void startGame() throws LWJGLException {
		if(this.mcCanvas != null) {
			Graphics graphics1 = this.mcCanvas.getGraphics();
			if(graphics1 != null) {
				graphics1.setColor(Color.BLACK);
				graphics1.fillRect(0, 0, this.displayWidth, this.displayHeight);
				graphics1.dispose();
			}

			Display.setParent(this.mcCanvas);
		} else if(this.fullscreen) {
			Display.setFullscreen(true);
			this.displayWidth = Display.getDisplayMode().getWidth();
			this.displayHeight = Display.getDisplayMode().getHeight();
			if(this.displayWidth <= 0) {
				this.displayWidth = 1;
			}

			if(this.displayHeight <= 0) {
				this.displayHeight = 1;
			}
		} else {
			Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
		}

		Display.setTitle("Minecraft Minecraft 1.2.5");
		System.out.println("LWJGL Version: " + Sys.getVersion());

		try {
			PixelFormat pixelFormat7 = new PixelFormat();
			pixelFormat7 = pixelFormat7.withDepthBits(24);
			Display.create(pixelFormat7);
		} catch (LWJGLException lWJGLException6) {
			lWJGLException6.printStackTrace();

			try {
				Thread.sleep(1000L);
			} catch (InterruptedException interruptedException5) {
			}

			Display.create();
		}

		OpenGlHelper.initializeTextures();
		this.mcDataDir = getMinecraftDir();
		this.saveLoader = new AnvilSaveConverter(new File(this.mcDataDir, "saves"));
		this.gameSettings = new GameSettings(this, this.mcDataDir);
		this.texturePackList = new TexturePackList(this, this.mcDataDir);
		this.renderEngine = new RenderEngine(this.texturePackList, this.gameSettings);
		this.loadScreen();
		this.fontRenderer = new FontRenderer(this.gameSettings, "/font/default.png", this.renderEngine, false);
		this.standardGalacticFontRenderer = new FontRenderer(this.gameSettings, "/font/alternate.png", this.renderEngine, false);
		if(this.gameSettings.language != null) {
			StringTranslate.getInstance().setLanguage(this.gameSettings.language);
			this.fontRenderer.setUnicodeFlag(StringTranslate.getInstance().isUnicode());
			this.fontRenderer.setBidiFlag(StringTranslate.isBidrectional(this.gameSettings.language));
		}

		ColorizerWater.setWaterBiomeColorizer(this.renderEngine.getTextureContents("/misc/watercolor.png"));
		ColorizerGrass.setGrassBiomeColorizer(this.renderEngine.getTextureContents("/misc/grasscolor.png"));
		ColorizerFoliage.getFoilageBiomeColorizer(this.renderEngine.getTextureContents("/misc/foliagecolor.png"));
		this.entityRenderer = new EntityRenderer(this);
		RenderManager.instance.itemRenderer = new ItemRenderer(this);
		this.statFileWriter = new StatFileWriter(this.session, this.mcDataDir);
		AchievementList.openInventory.setStatStringFormatter(new StatStringFormatKeyInv(this));
		this.loadScreen();
		Mouse.create();
		this.mouseHelper = new MouseHelper(this.mcCanvas);

		try {
			Controllers.create();
		} catch (Exception exception4) {
			exception4.printStackTrace();
		}

		func_52004_D();
		this.checkGLError("Pre startup");
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearDepth(1.0D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		this.checkGLError("Startup");
		this.glCapabilities = new OpenGlCapsChecker();
		this.sndManager.loadSoundSettings(this.gameSettings);
		this.renderEngine.registerTextureFX(this.textureLavaFX);
		this.renderEngine.registerTextureFX(this.textureWaterFX);
		this.renderEngine.registerTextureFX(new TexturePortalFX());
		this.renderEngine.registerTextureFX(new TextureCompassFX(this));
		this.renderEngine.registerTextureFX(new TextureWatchFX(this));
		this.renderEngine.registerTextureFX(new TextureWaterFlowFX());
		this.renderEngine.registerTextureFX(new TextureLavaFlowFX());
		this.renderEngine.registerTextureFX(new TextureFlamesFX(0));
		this.renderEngine.registerTextureFX(new TextureFlamesFX(1));
		this.renderGlobal = new RenderGlobal(this, this.renderEngine);
		GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
		this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);

		try {
			this.downloadResourcesThread = new ThreadDownloadResources(this.mcDataDir, this);
			this.downloadResourcesThread.start();
		} catch (Exception exception3) {
		}

		this.checkGLError("Post startup");
		this.ingameGUI = new GuiIngame(this);
		if(this.serverName != null) {
			this.displayGuiScreen(new GuiConnecting(this, this.serverName, this.serverPort));
		} else {
			this.displayGuiScreen(new GuiMainMenu());
		}

		this.loadingScreen = new LoadingScreenRenderer(this);
	}

	private void loadScreen() throws LWJGLException {
		ScaledResolution scaledResolution1 = new ScaledResolution(this.gameSettings, this.displayWidth, this.displayHeight);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, scaledResolution1.scaledWidthD, scaledResolution1.scaledHeightD, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
		GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
		Tessellator tessellator2 = Tessellator.instance;
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/title/mojang.png"));
		tessellator2.startDrawingQuads();
		tessellator2.setColorOpaque_I(0xFFFFFF);
		tessellator2.addVertexWithUV(0.0D, (double)this.displayHeight, 0.0D, 0.0D, 0.0D);
		tessellator2.addVertexWithUV((double)this.displayWidth, (double)this.displayHeight, 0.0D, 0.0D, 0.0D);
		tessellator2.addVertexWithUV((double)this.displayWidth, 0.0D, 0.0D, 0.0D, 0.0D);
		tessellator2.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		tessellator2.draw();
		short s3 = 256;
		short s4 = 256;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tessellator2.setColorOpaque_I(0xFFFFFF);
		this.scaledTessellator((scaledResolution1.getScaledWidth() - s3) / 2, (scaledResolution1.getScaledHeight() - s4) / 2, 0, 0, s3, s4);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		Display.swapBuffers();
	}

	public void scaledTessellator(int i1, int i2, int i3, int i4, int i5, int i6) {
		float f7 = 0.00390625F;
		float f8 = 0.00390625F;
		Tessellator tessellator9 = Tessellator.instance;
		tessellator9.startDrawingQuads();
		tessellator9.addVertexWithUV((double)(i1 + 0), (double)(i2 + i6), 0.0D, (double)((float)(i3 + 0) * f7), (double)((float)(i4 + i6) * f8));
		tessellator9.addVertexWithUV((double)(i1 + i5), (double)(i2 + i6), 0.0D, (double)((float)(i3 + i5) * f7), (double)((float)(i4 + i6) * f8));
		tessellator9.addVertexWithUV((double)(i1 + i5), (double)(i2 + 0), 0.0D, (double)((float)(i3 + i5) * f7), (double)((float)(i4 + 0) * f8));
		tessellator9.addVertexWithUV((double)(i1 + 0), (double)(i2 + 0), 0.0D, (double)((float)(i3 + 0) * f7), (double)((float)(i4 + 0) * f8));
		tessellator9.draw();
	}

	public static File getMinecraftDir() {
		if(minecraftDir == null) {
			minecraftDir = getAppDir("minecraft");
		}

		return minecraftDir;
	}

	public static File getAppDir(String string0) {
		String string1 = System.getProperty("user.home", ".");
		File file2;
		switch(Minecraft.SyntheticClass_1.$SwitchMap$net$minecraft$src$EnumOS2[getOs().ordinal()]) {
		case 1:
		case 2:
			file2 = new File(string1, '.' + string0 + '/');
			break;
		case 3:
			String string3 = System.getenv("APPDATA");
			if(string3 != null) {
				file2 = new File(string3, "." + string0 + '/');
			} else {
				file2 = new File(string1, '.' + string0 + '/');
			}
			break;
		case 4:
			file2 = new File(string1, "Library/Application Support/" + string0);
			break;
		default:
			file2 = new File(string1, string0 + '/');
		}

		if(!file2.exists() && !file2.mkdirs()) {
			throw new RuntimeException("The working directory could not be created: " + file2);
		} else {
			return file2;
		}
	}

	private static EnumOS2 getOs() {
		String string0 = System.getProperty("os.name").toLowerCase();
		return string0.contains("win") ? EnumOS2.windows : (string0.contains("mac") ? EnumOS2.macos : (string0.contains("solaris") ? EnumOS2.solaris : (string0.contains("sunos") ? EnumOS2.solaris : (string0.contains("linux") ? EnumOS2.linux : (string0.contains("unix") ? EnumOS2.linux : EnumOS2.unknown)))));
	}

	public ISaveFormat getSaveLoader() {
		return this.saveLoader;
	}

	public void displayGuiScreen(GuiScreen guiScreen1) {
		if(!(this.currentScreen instanceof GuiErrorScreen)) {
			if(this.currentScreen != null) {
				this.currentScreen.onGuiClosed();
			}

			if(guiScreen1 instanceof GuiMainMenu) {
				this.statFileWriter.func_27175_b();
			}

			this.statFileWriter.syncStats();
			if(guiScreen1 == null && this.theWorld == null) {
				guiScreen1 = new GuiMainMenu();
			} else if(guiScreen1 == null && this.thePlayer.getHealth() <= 0) {
				guiScreen1 = new GuiGameOver();
			}

			if(guiScreen1 instanceof GuiMainMenu) {
				this.gameSettings.showDebugInfo = false;
				this.ingameGUI.clearChatMessages();
			}

			this.currentScreen = (GuiScreen)guiScreen1;
			if(guiScreen1 != null) {
				this.setIngameNotInFocus();
				ScaledResolution scaledResolution2 = new ScaledResolution(this.gameSettings, this.displayWidth, this.displayHeight);
				int i3 = scaledResolution2.getScaledWidth();
				int i4 = scaledResolution2.getScaledHeight();
				((GuiScreen)guiScreen1).setWorldAndResolution(this, i3, i4);
				this.skipRenderWorld = false;
			} else {
				this.setIngameFocus();
			}

		}
	}

	private void checkGLError(String string1) {
		int i2 = GL11.glGetError();
		if(i2 != 0) {
			String string3 = GLU.gluErrorString(i2);
			System.out.println("########## GL ERROR ##########");
			System.out.println("@ " + string1);
			System.out.println(i2 + ": " + string3);
		}

	}

	public void shutdownMinecraftApplet() {
		try {
			this.statFileWriter.func_27175_b();
			this.statFileWriter.syncStats();
			if(this.mcApplet != null) {
				this.mcApplet.clearApplet();
			}

			try {
				if(this.downloadResourcesThread != null) {
					this.downloadResourcesThread.closeMinecraft();
				}
			} catch (Exception exception9) {
			}

			System.out.println("Stopping!");

			try {
				this.changeWorld1((World)null);
			} catch (Throwable throwable8) {
			}

			try {
				GLAllocation.deleteTexturesAndDisplayLists();
			} catch (Throwable throwable7) {
			}

			this.sndManager.closeMinecraft();
			Mouse.destroy();
			Keyboard.destroy();
		} finally {
			Display.destroy();
			if(!this.hasCrashed) {
				System.exit(0);
			}

		}

		System.gc();
	}

	public void run() {
		this.running = true;

		try {
			this.startGame();
		} catch (Exception exception11) {
			exception11.printStackTrace();
			this.onMinecraftCrash(new UnexpectedThrowable("Failed to start game", exception11));
			return;
		}

		try {
			while(this.running) {
				try {
					this.runGameLoop();
				} catch (MinecraftException minecraftException9) {
					this.theWorld = null;
					this.changeWorld1((World)null);
					this.displayGuiScreen(new GuiConflictWarning());
				} catch (OutOfMemoryError outOfMemoryError10) {
					this.freeMemory();
					this.displayGuiScreen(new GuiMemoryErrorScreen());
					System.gc();
				}
			}
		} catch (MinecraftError minecraftError12) {
		} catch (Throwable throwable13) {
			this.freeMemory();
			throwable13.printStackTrace();
			this.onMinecraftCrash(new UnexpectedThrowable("Unexpected error", throwable13));
		} finally {
			this.shutdownMinecraftApplet();
		}

	}

	private void runGameLoop() {
		if(this.mcApplet != null && !this.mcApplet.isActive()) {
			this.running = false;
		} else {
			AxisAlignedBB.clearBoundingBoxPool();
			Vec3D.initialize();
			Profiler.startSection("root");
			if(this.mcCanvas == null && Display.isCloseRequested()) {
				this.shutdown();
			}

			if(this.isGamePaused && this.theWorld != null) {
				float f1 = this.timer.renderPartialTicks;
				this.timer.updateTimer();
				this.timer.renderPartialTicks = f1;
			} else {
				this.timer.updateTimer();
			}

			long j6 = System.nanoTime();
			Profiler.startSection("tick");

			for(int i3 = 0; i3 < this.timer.elapsedTicks; ++i3) {
				++this.ticksRan;

				try {
					this.runTick();
				} catch (MinecraftException minecraftException5) {
					this.theWorld = null;
					this.changeWorld1((World)null);
					this.displayGuiScreen(new GuiConflictWarning());
				}
			}

			Profiler.endSection();
			long j7 = System.nanoTime() - j6;
			this.checkGLError("Pre render");
			RenderBlocks.fancyGrass = this.gameSettings.fancyGraphics;
			Profiler.startSection("sound");
			this.sndManager.setListener(this.thePlayer, this.timer.renderPartialTicks);
			Profiler.endStartSection("updatelights");
			if(this.theWorld != null) {
				this.theWorld.updatingLighting();
			}

			Profiler.endSection();
			Profiler.startSection("render");
			Profiler.startSection("display");
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			if(!Keyboard.isKeyDown(Keyboard.KEY_F7)) {
				Display.update();
			}

			if(this.thePlayer != null && this.thePlayer.isEntityInsideOpaqueBlock()) {
				this.gameSettings.thirdPersonView = 0;
			}

			Profiler.endSection();
			if(!this.skipRenderWorld) {
				Profiler.startSection("gameMode");
				if(this.playerController != null) {
					this.playerController.setPartialTime(this.timer.renderPartialTicks);
				}

				Profiler.endStartSection("gameRenderer");
				this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);
				Profiler.endSection();
			}

			GL11.glFlush();
			Profiler.endSection();
			if(!Display.isActive() && this.fullscreen) {
				this.toggleFullscreen();
			}

			Profiler.endSection();
			if(this.gameSettings.showDebugInfo && this.gameSettings.field_50119_G) {
				if(!Profiler.profilingEnabled) {
					Profiler.clearProfiling();
				}

				Profiler.profilingEnabled = true;
				this.displayDebugInfo(j7);
			} else {
				Profiler.profilingEnabled = false;
				this.prevFrameTime = System.nanoTime();
			}

			this.guiAchievement.updateAchievementWindow();
			Profiler.startSection("root");
			Thread.yield();
			if(Keyboard.isKeyDown(Keyboard.KEY_F7)) {
				Display.update();
			}

			this.screenshotListener();
			if(this.mcCanvas != null && !this.fullscreen && (this.mcCanvas.getWidth() != this.displayWidth || this.mcCanvas.getHeight() != this.displayHeight)) {
				this.displayWidth = this.mcCanvas.getWidth();
				this.displayHeight = this.mcCanvas.getHeight();
				if(this.displayWidth <= 0) {
					this.displayWidth = 1;
				}

				if(this.displayHeight <= 0) {
					this.displayHeight = 1;
				}

				this.resize(this.displayWidth, this.displayHeight);
			}

			this.checkGLError("Post render");
			++this.fpsCounter;

			for(this.isGamePaused = !this.isMultiplayerWorld() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame(); System.currentTimeMillis() >= this.debugUpdateTime + 1000L; this.fpsCounter = 0) {
				this.debug = this.fpsCounter + " fps, " + WorldRenderer.chunksUpdated + " chunk updates";
				WorldRenderer.chunksUpdated = 0;
				this.debugUpdateTime += 1000L;
			}

			Profiler.endSection();
		}
	}

	public void freeMemory() {
		try {
			field_28006_b = new byte[0];
			this.renderGlobal.func_28137_f();
		} catch (Throwable throwable4) {
		}

		try {
			System.gc();
			AxisAlignedBB.clearBoundingBoxes();
			Vec3D.clearVectorList();
		} catch (Throwable throwable3) {
		}

		try {
			System.gc();
			this.changeWorld1((World)null);
		} catch (Throwable throwable2) {
		}

		System.gc();
	}

	private void screenshotListener() {
		if(Keyboard.isKeyDown(Keyboard.KEY_F2)) {
			if(!this.isTakingScreenshot) {
				this.isTakingScreenshot = true;
				this.ingameGUI.addChatMessage(ScreenShotHelper.saveScreenshot(minecraftDir, this.displayWidth, this.displayHeight));
			}
		} else {
			this.isTakingScreenshot = false;
		}

	}

	private void updateDebugProfilerName(int i1) {
		List list2 = Profiler.getProfilingData(this.debugProfilerName);
		if(list2 != null && list2.size() != 0) {
			ProfilerResult profilerResult3 = (ProfilerResult)list2.remove(0);
			if(i1 == 0) {
				if(profilerResult3.name.length() > 0) {
					int i4 = this.debugProfilerName.lastIndexOf(".");
					if(i4 >= 0) {
						this.debugProfilerName = this.debugProfilerName.substring(0, i4);
					}
				}
			} else {
				--i1;
				if(i1 < list2.size() && !((ProfilerResult)list2.get(i1)).name.equals("unspecified")) {
					if(this.debugProfilerName.length() > 0) {
						this.debugProfilerName = this.debugProfilerName + ".";
					}

					this.debugProfilerName = this.debugProfilerName + ((ProfilerResult)list2.get(i1)).name;
				}
			}

		}
	}

	private void displayDebugInfo(long j1) {
		List list3 = Profiler.getProfilingData(this.debugProfilerName);
		ProfilerResult profilerResult4 = (ProfilerResult)list3.remove(0);
		long j5 = 16666666L;
		if(this.prevFrameTime == -1L) {
			this.prevFrameTime = System.nanoTime();
		}

		long j7 = System.nanoTime();
		tickTimes[numRecordedFrameTimes & frameTimes.length - 1] = j1;
		frameTimes[numRecordedFrameTimes++ & frameTimes.length - 1] = j7 - this.prevFrameTime;
		this.prevFrameTime = j7;
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, (double)this.displayWidth, (double)this.displayHeight, 0.0D, 1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glLineWidth(1.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Tessellator tessellator9 = Tessellator.instance;
		tessellator9.startDrawing(7);
		int i10 = (int)(j5 / 200000L);
		tessellator9.setColorOpaque_I(536870912);
		tessellator9.addVertex(0.0D, (double)(this.displayHeight - i10), 0.0D);
		tessellator9.addVertex(0.0D, (double)this.displayHeight, 0.0D);
		tessellator9.addVertex((double)frameTimes.length, (double)this.displayHeight, 0.0D);
		tessellator9.addVertex((double)frameTimes.length, (double)(this.displayHeight - i10), 0.0D);
		tessellator9.setColorOpaque_I(0x20200000);
		tessellator9.addVertex(0.0D, (double)(this.displayHeight - i10 * 2), 0.0D);
		tessellator9.addVertex(0.0D, (double)(this.displayHeight - i10), 0.0D);
		tessellator9.addVertex((double)frameTimes.length, (double)(this.displayHeight - i10), 0.0D);
		tessellator9.addVertex((double)frameTimes.length, (double)(this.displayHeight - i10 * 2), 0.0D);
		tessellator9.draw();
		long j11 = 0L;

		int i13;
		for(i13 = 0; i13 < frameTimes.length; ++i13) {
			j11 += frameTimes[i13];
		}

		i13 = (int)(j11 / 200000L / (long)frameTimes.length);
		tessellator9.startDrawing(7);
		tessellator9.setColorOpaque_I(0x20400000);
		tessellator9.addVertex(0.0D, (double)(this.displayHeight - i13), 0.0D);
		tessellator9.addVertex(0.0D, (double)this.displayHeight, 0.0D);
		tessellator9.addVertex((double)frameTimes.length, (double)this.displayHeight, 0.0D);
		tessellator9.addVertex((double)frameTimes.length, (double)(this.displayHeight - i13), 0.0D);
		tessellator9.draw();
		tessellator9.startDrawing(1);

		int i15;
		int i16;
		for(int i14 = 0; i14 < frameTimes.length; ++i14) {
			i15 = (i14 - numRecordedFrameTimes & frameTimes.length - 1) * 255 / frameTimes.length;
			i16 = i15 * i15 / 255;
			i16 = i16 * i16 / 255;
			int i17 = i16 * i16 / 255;
			i17 = i17 * i17 / 255;
			if(frameTimes[i14] > j5) {
				tessellator9.setColorOpaque_I(0xFF000000 + i16 * 65536);
			} else {
				tessellator9.setColorOpaque_I(0xFF000000 + i16 * 256);
			}

			long j18 = frameTimes[i14] / 200000L;
			long j20 = tickTimes[i14] / 200000L;
			tessellator9.addVertex((double)((float)i14 + 0.5F), (double)((float)((long)this.displayHeight - j18) + 0.5F), 0.0D);
			tessellator9.addVertex((double)((float)i14 + 0.5F), (double)((float)this.displayHeight + 0.5F), 0.0D);
			tessellator9.setColorOpaque_I(0xFF000000 + i16 * 65536 + i16 * 256 + i16 * 1);
			tessellator9.addVertex((double)((float)i14 + 0.5F), (double)((float)((long)this.displayHeight - j18) + 0.5F), 0.0D);
			tessellator9.addVertex((double)((float)i14 + 0.5F), (double)((float)((long)this.displayHeight - (j18 - j20)) + 0.5F), 0.0D);
		}

		tessellator9.draw();
		short s26 = 160;
		i15 = this.displayWidth - s26 - 10;
		i16 = this.displayHeight - s26 * 2;
		GL11.glEnable(GL11.GL_BLEND);
		tessellator9.startDrawingQuads();
		tessellator9.setColorRGBA_I(0, 200);
		tessellator9.addVertex((double)((float)i15 - (float)s26 * 1.1F), (double)((float)i16 - (float)s26 * 0.6F - 16.0F), 0.0D);
		tessellator9.addVertex((double)((float)i15 - (float)s26 * 1.1F), (double)(i16 + s26 * 2), 0.0D);
		tessellator9.addVertex((double)((float)i15 + (float)s26 * 1.1F), (double)(i16 + s26 * 2), 0.0D);
		tessellator9.addVertex((double)((float)i15 + (float)s26 * 1.1F), (double)((float)i16 - (float)s26 * 0.6F - 16.0F), 0.0D);
		tessellator9.draw();
		GL11.glDisable(GL11.GL_BLEND);
		double d27 = 0.0D;

		int i21;
		for(int i19 = 0; i19 < list3.size(); ++i19) {
			ProfilerResult profilerResult29 = (ProfilerResult)list3.get(i19);
			i21 = MathHelper.floor_double(profilerResult29.sectionPercentage / 4.0D) + 1;
			tessellator9.startDrawing(6);
			tessellator9.setColorOpaque_I(profilerResult29.getDisplayColor());
			tessellator9.addVertex((double)i15, (double)i16, 0.0D);

			int i22;
			float f23;
			float f24;
			float f25;
			for(i22 = i21; i22 >= 0; --i22) {
				f23 = (float)((d27 + profilerResult29.sectionPercentage * (double)i22 / (double)i21) * (double)(float)Math.PI * 2.0D / 100.0D);
				f24 = MathHelper.sin(f23) * (float)s26;
				f25 = MathHelper.cos(f23) * (float)s26 * 0.5F;
				tessellator9.addVertex((double)((float)i15 + f24), (double)((float)i16 - f25), 0.0D);
			}

			tessellator9.draw();
			tessellator9.startDrawing(5);
			tessellator9.setColorOpaque_I((profilerResult29.getDisplayColor() & 16711422) >> 1);

			for(i22 = i21; i22 >= 0; --i22) {
				f23 = (float)((d27 + profilerResult29.sectionPercentage * (double)i22 / (double)i21) * (double)(float)Math.PI * 2.0D / 100.0D);
				f24 = MathHelper.sin(f23) * (float)s26;
				f25 = MathHelper.cos(f23) * (float)s26 * 0.5F;
				tessellator9.addVertex((double)((float)i15 + f24), (double)((float)i16 - f25), 0.0D);
				tessellator9.addVertex((double)((float)i15 + f24), (double)((float)i16 - f25 + 10.0F), 0.0D);
			}

			tessellator9.draw();
			d27 += profilerResult29.sectionPercentage;
		}

		DecimalFormat decimalFormat28 = new DecimalFormat("##0.00");
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		String string30 = "";
		if(!profilerResult4.name.equals("unspecified")) {
			string30 = string30 + "[0] ";
		}

		if(profilerResult4.name.length() == 0) {
			string30 = string30 + "ROOT ";
		} else {
			string30 = string30 + profilerResult4.name + " ";
		}

		i21 = 0xFFFFFF;
		this.fontRenderer.drawStringWithShadow(string30, i15 - s26, i16 - s26 / 2 - 16, i21);
		this.fontRenderer.drawStringWithShadow(string30 = decimalFormat28.format(profilerResult4.globalPercentage) + "%", i15 + s26 - this.fontRenderer.getStringWidth(string30), i16 - s26 / 2 - 16, i21);

		for(int i32 = 0; i32 < list3.size(); ++i32) {
			ProfilerResult profilerResult31 = (ProfilerResult)list3.get(i32);
			String string33 = "";
			if(!profilerResult31.name.equals("unspecified")) {
				string33 = string33 + "[" + (i32 + 1) + "] ";
			} else {
				string33 = string33 + "[?] ";
			}

			string33 = string33 + profilerResult31.name;
			this.fontRenderer.drawStringWithShadow(string33, i15 - s26, i16 + s26 / 2 + i32 * 8 + 20, profilerResult31.getDisplayColor());
			this.fontRenderer.drawStringWithShadow(string33 = decimalFormat28.format(profilerResult31.sectionPercentage) + "%", i15 + s26 - 50 - this.fontRenderer.getStringWidth(string33), i16 + s26 / 2 + i32 * 8 + 20, profilerResult31.getDisplayColor());
			this.fontRenderer.drawStringWithShadow(string33 = decimalFormat28.format(profilerResult31.globalPercentage) + "%", i15 + s26 - this.fontRenderer.getStringWidth(string33), i16 + s26 / 2 + i32 * 8 + 20, profilerResult31.getDisplayColor());
		}

	}

	public void shutdown() {
		this.running = false;
	}

	public void setIngameFocus() {
		if(Display.isActive()) {
			if(!this.inGameHasFocus) {
				this.inGameHasFocus = true;
				this.mouseHelper.grabMouseCursor();
				this.displayGuiScreen((GuiScreen)null);
				this.leftClickCounter = 10000;
			}
		}
	}

	public void setIngameNotInFocus() {
		if(this.inGameHasFocus) {
			KeyBinding.unPressAllKeys();
			this.inGameHasFocus = false;
			this.mouseHelper.ungrabMouseCursor();
		}
	}

	public void displayInGameMenu() {
		if(this.currentScreen == null) {
			this.displayGuiScreen(new GuiIngameMenu());
		}
	}

	private void sendClickBlockToController(int i1, boolean z2) {
		if(!z2) {
			this.leftClickCounter = 0;
		}

		if(i1 != 0 || this.leftClickCounter <= 0) {
			if(z2 && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE && i1 == 0) {
				int i3 = this.objectMouseOver.blockX;
				int i4 = this.objectMouseOver.blockY;
				int i5 = this.objectMouseOver.blockZ;
				this.playerController.onPlayerDamageBlock(i3, i4, i5, this.objectMouseOver.sideHit);
				if(this.thePlayer.canPlayerEdit(i3, i4, i5)) {
					this.effectRenderer.addBlockHitEffects(i3, i4, i5, this.objectMouseOver.sideHit);
					this.thePlayer.swingItem();
				}
			} else {
				this.playerController.resetBlockRemoving();
			}

		}
	}

	private void clickMouse(int i1) {
		if(i1 != 0 || this.leftClickCounter <= 0) {
			if(i1 == 0) {
				this.thePlayer.swingItem();
			}

			if(i1 == 1) {
				this.rightClickDelayTimer = 4;
			}

			boolean z2 = true;
			ItemStack itemStack3 = this.thePlayer.inventory.getCurrentItem();
			if(this.objectMouseOver == null) {
				if(i1 == 0 && this.playerController.isNotCreative()) {
					this.leftClickCounter = 10;
				}
			} else if(this.objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY) {
				if(i1 == 0) {
					this.playerController.attackEntity(this.thePlayer, this.objectMouseOver.entityHit);
				}

				if(i1 == 1) {
					this.playerController.interactWithEntity(this.thePlayer, this.objectMouseOver.entityHit);
				}
			} else if(this.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE) {
				int i4 = this.objectMouseOver.blockX;
				int i5 = this.objectMouseOver.blockY;
				int i6 = this.objectMouseOver.blockZ;
				int i7 = this.objectMouseOver.sideHit;
				if(i1 == 0) {
					this.playerController.clickBlock(i4, i5, i6, this.objectMouseOver.sideHit);
				} else {
					int i9 = itemStack3 != null ? itemStack3.stackSize : 0;
					if(this.playerController.onPlayerRightClick(this.thePlayer, this.theWorld, itemStack3, i4, i5, i6, i7)) {
						z2 = false;
						this.thePlayer.swingItem();
					}

					if(itemStack3 == null) {
						return;
					}

					if(itemStack3.stackSize == 0) {
						this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
					} else if(itemStack3.stackSize != i9 || this.playerController.isInCreativeMode()) {
						this.entityRenderer.itemRenderer.func_9449_b();
					}
				}
			}

			if(z2 && i1 == 1) {
				ItemStack itemStack10 = this.thePlayer.inventory.getCurrentItem();
				if(itemStack10 != null && this.playerController.sendUseItem(this.thePlayer, this.theWorld, itemStack10)) {
					this.entityRenderer.itemRenderer.func_9450_c();
				}
			}

		}
	}

	public void toggleFullscreen() {
		try {
			this.fullscreen = !this.fullscreen;
			if(this.fullscreen) {
				Display.setDisplayMode(Display.getDesktopDisplayMode());
				this.displayWidth = Display.getDisplayMode().getWidth();
				this.displayHeight = Display.getDisplayMode().getHeight();
				if(this.displayWidth <= 0) {
					this.displayWidth = 1;
				}

				if(this.displayHeight <= 0) {
					this.displayHeight = 1;
				}
			} else {
				if(this.mcCanvas != null) {
					this.displayWidth = this.mcCanvas.getWidth();
					this.displayHeight = this.mcCanvas.getHeight();
				} else {
					this.displayWidth = this.tempDisplayWidth;
					this.displayHeight = this.tempDisplayHeight;
				}

				if(this.displayWidth <= 0) {
					this.displayWidth = 1;
				}

				if(this.displayHeight <= 0) {
					this.displayHeight = 1;
				}
			}

			if(this.currentScreen != null) {
				this.resize(this.displayWidth, this.displayHeight);
			}

			Display.setFullscreen(this.fullscreen);
			Display.update();
		} catch (Exception exception2) {
			exception2.printStackTrace();
		}

	}

	private void resize(int i1, int i2) {
		if(i1 <= 0) {
			i1 = 1;
		}

		if(i2 <= 0) {
			i2 = 1;
		}

		this.displayWidth = i1;
		this.displayHeight = i2;
		if(this.currentScreen != null) {
			ScaledResolution scaledResolution3 = new ScaledResolution(this.gameSettings, i1, i2);
			int i4 = scaledResolution3.getScaledWidth();
			int i5 = scaledResolution3.getScaledHeight();
			this.currentScreen.setWorldAndResolution(this, i4, i5);
		}

	}

	private void startThreadCheckHasPaid() {
		(new ThreadCheckHasPaid(this)).start();
	}

	public void runTick() {
		if(this.rightClickDelayTimer > 0) {
			--this.rightClickDelayTimer;
		}

		if(this.ticksRan == 6000) {
			this.startThreadCheckHasPaid();
		}

		Profiler.startSection("stats");
		this.statFileWriter.func_27178_d();
		Profiler.endStartSection("gui");
		if(!this.isGamePaused) {
			this.ingameGUI.updateTick();
		}

		Profiler.endStartSection("pick");
		this.entityRenderer.getMouseOver(1.0F);
		Profiler.endStartSection("centerChunkSource");
		int i3;
		if(this.thePlayer != null) {
			IChunkProvider iChunkProvider1 = this.theWorld.getChunkProvider();
			if(iChunkProvider1 instanceof ChunkProviderLoadOrGenerate) {
				ChunkProviderLoadOrGenerate chunkProviderLoadOrGenerate2 = (ChunkProviderLoadOrGenerate)iChunkProvider1;
				i3 = MathHelper.floor_float((float)((int)this.thePlayer.posX)) >> 4;
				int i4 = MathHelper.floor_float((float)((int)this.thePlayer.posZ)) >> 4;
				chunkProviderLoadOrGenerate2.setCurrentChunkOver(i3, i4);
			}
		}

		Profiler.endStartSection("gameMode");
		if(!this.isGamePaused && this.theWorld != null) {
			this.playerController.updateController();
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
		Profiler.endStartSection("textures");
		if(!this.isGamePaused) {
			this.renderEngine.updateDynamicTextures();
		}

		if(this.currentScreen == null && this.thePlayer != null) {
			if(this.thePlayer.getHealth() <= 0) {
				this.displayGuiScreen((GuiScreen)null);
			} else if(this.thePlayer.isPlayerSleeping() && this.theWorld != null && this.theWorld.isRemote) {
				this.displayGuiScreen(new GuiSleepMP());
			}
		} else if(this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !this.thePlayer.isPlayerSleeping()) {
			this.displayGuiScreen((GuiScreen)null);
		}

		if(this.currentScreen != null) {
			this.leftClickCounter = 10000;
		}

		if(this.currentScreen != null) {
			this.currentScreen.handleInput();
			if(this.currentScreen != null) {
				this.currentScreen.guiParticles.update();
				this.currentScreen.updateScreen();
			}
		}

		if(this.currentScreen == null || this.currentScreen.allowUserInput) {
			Profiler.endStartSection("mouse");

			while(Mouse.next()) {
				KeyBinding.setKeyBindState(Mouse.getEventButton() - 100, Mouse.getEventButtonState());
				if(Mouse.getEventButtonState()) {
					KeyBinding.onTick(Mouse.getEventButton() - 100);
				}

				long j5 = System.currentTimeMillis() - this.systemTime;
				if(j5 <= 200L) {
					i3 = Mouse.getEventDWheel();
					if(i3 != 0) {
						this.thePlayer.inventory.changeCurrentItem(i3);
						if(this.gameSettings.noclip) {
							if(i3 > 0) {
								i3 = 1;
							}

							if(i3 < 0) {
								i3 = -1;
							}

							this.gameSettings.noclipRate += (float)i3 * 0.25F;
						}
					}

					if(this.currentScreen == null) {
						if(!this.inGameHasFocus && Mouse.getEventButtonState()) {
							this.setIngameFocus();
						}
					} else if(this.currentScreen != null) {
						this.currentScreen.handleMouseInput();
					}
				}
			}

			if(this.leftClickCounter > 0) {
				--this.leftClickCounter;
			}

			Profiler.endStartSection("keyboard");

			label361:
			while(true) {
				while(true) {
					do {
						if(!Keyboard.next()) {
							while(this.gameSettings.keyBindInventory.isPressed()) {
								this.displayGuiScreen(new GuiInventory(this.thePlayer));
							}

							while(this.gameSettings.keyBindDrop.isPressed()) {
								this.thePlayer.dropOneItem();
							}

							while(this.isMultiplayerWorld() && this.gameSettings.keyBindChat.isPressed()) {
								this.displayGuiScreen(new GuiChat());
							}

							if(this.isMultiplayerWorld() && this.currentScreen == null && (Keyboard.isKeyDown(Keyboard.KEY_SLASH) || Keyboard.isKeyDown(Keyboard.KEY_DIVIDE))) {
								this.displayGuiScreen(new GuiChat("/"));
							}

							if(this.thePlayer.isUsingItem()) {
								if(!this.gameSettings.keyBindUseItem.pressed) {
									this.playerController.onStoppedUsingItem(this.thePlayer);
								}

								while(true) {
									if(!this.gameSettings.keyBindAttack.isPressed()) {
										while(this.gameSettings.keyBindUseItem.isPressed()) {
										}

										while(this.gameSettings.keyBindPickBlock.isPressed()) {
										}
										break;
									}
								}
							} else {
								while(this.gameSettings.keyBindAttack.isPressed()) {
									this.clickMouse(0);
								}

								while(this.gameSettings.keyBindUseItem.isPressed()) {
									this.clickMouse(1);
								}

								while(this.gameSettings.keyBindPickBlock.isPressed()) {
									this.clickMiddleMouseButton();
								}
							}

							if(this.gameSettings.keyBindUseItem.pressed && this.rightClickDelayTimer == 0 && !this.thePlayer.isUsingItem()) {
								this.clickMouse(1);
							}

							this.sendClickBlockToController(0, this.currentScreen == null && this.gameSettings.keyBindAttack.pressed && this.inGameHasFocus);
							break label361;
						}

						KeyBinding.setKeyBindState(Keyboard.getEventKey(), Keyboard.getEventKeyState());
						if(Keyboard.getEventKeyState()) {
							KeyBinding.onTick(Keyboard.getEventKey());
						}
					} while(!Keyboard.getEventKeyState());

					if(Keyboard.getEventKey() == Keyboard.KEY_F11) {
						this.toggleFullscreen();
					} else {
						if(this.currentScreen != null) {
							this.currentScreen.handleKeyboardInput();
						} else {
							if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
								this.displayInGameMenu();
							}

							if(Keyboard.getEventKey() == Keyboard.KEY_S && Keyboard.isKeyDown(Keyboard.KEY_F3)) {
								this.forceReload();
							}

							if(Keyboard.getEventKey() == Keyboard.KEY_T && Keyboard.isKeyDown(Keyboard.KEY_F3)) {
								this.renderEngine.refreshTextures();
							}

							if(Keyboard.getEventKey() == Keyboard.KEY_F && Keyboard.isKeyDown(Keyboard.KEY_F3)) {
								boolean z6 = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) | Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
								this.gameSettings.setOptionValue(EnumOptions.RENDER_DISTANCE, z6 ? -1 : 1);
							}

							if(Keyboard.getEventKey() == Keyboard.KEY_A && Keyboard.isKeyDown(Keyboard.KEY_F3)) {
								this.renderGlobal.loadRenderers();
							}

							if(Keyboard.getEventKey() == Keyboard.KEY_F1) {
								this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
							}

							if(Keyboard.getEventKey() == Keyboard.KEY_F3) {
								this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
								this.gameSettings.field_50119_G = !GuiScreen.func_50049_m();
							}

							if(Keyboard.getEventKey() == Keyboard.KEY_F5) {
								++this.gameSettings.thirdPersonView;
								if(this.gameSettings.thirdPersonView > 2) {
									this.gameSettings.thirdPersonView = 0;
								}
							}

							if(Keyboard.getEventKey() == Keyboard.KEY_F8) {
								this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
							}
						}

						int i7;
						for(i7 = 0; i7 < 9; ++i7) {
							if(Keyboard.getEventKey() == Keyboard.KEY_1 + i7) {
								this.thePlayer.inventory.currentItem = i7;
							}
						}

						if(this.gameSettings.showDebugInfo && this.gameSettings.field_50119_G) {
							if(Keyboard.getEventKey() == Keyboard.KEY_0) {
								this.updateDebugProfilerName(0);
							}

							for(i7 = 0; i7 < 9; ++i7) {
								if(Keyboard.getEventKey() == Keyboard.KEY_1 + i7) {
									this.updateDebugProfilerName(i7 + 1);
								}
							}
						}
					}
				}
			}
		}

		if(this.theWorld != null) {
			if(this.thePlayer != null) {
				++this.joinPlayerCounter;
				if(this.joinPlayerCounter == 30) {
					this.joinPlayerCounter = 0;
					this.theWorld.joinEntityInSurroundings(this.thePlayer);
				}
			}

			if(this.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
				this.theWorld.difficultySetting = 3;
			} else {
				this.theWorld.difficultySetting = this.gameSettings.difficulty;
			}

			if(this.theWorld.isRemote) {
				this.theWorld.difficultySetting = 1;
			}

			Profiler.endStartSection("gameRenderer");
			if(!this.isGamePaused) {
				this.entityRenderer.updateRenderer();
			}

			Profiler.endStartSection("levelRenderer");
			if(!this.isGamePaused) {
				this.renderGlobal.updateClouds();
			}

			Profiler.endStartSection("level");
			if(!this.isGamePaused) {
				if(this.theWorld.lightningFlash > 0) {
					--this.theWorld.lightningFlash;
				}

				this.theWorld.updateEntities();
			}

			if(!this.isGamePaused || this.isMultiplayerWorld()) {
				this.theWorld.setAllowedSpawnTypes(this.theWorld.difficultySetting > 0, true);
				this.theWorld.tick();
			}

			Profiler.endStartSection("animateTick");
			if(!this.isGamePaused && this.theWorld != null) {
				this.theWorld.randomDisplayUpdates(MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
			}

			Profiler.endStartSection("particles");
			if(!this.isGamePaused) {
				this.effectRenderer.updateEffects();
			}
		}

		Profiler.endSection();
		this.systemTime = System.currentTimeMillis();
	}

	private void forceReload() {
		System.out.println("FORCING RELOAD!");
		this.sndManager = new SoundManager();
		this.sndManager.loadSoundSettings(this.gameSettings);
		this.downloadResourcesThread.reloadResources();
	}

	public boolean isMultiplayerWorld() {
		return this.theWorld != null && this.theWorld.isRemote;
	}

	public void startWorld(String string1, String string2, WorldSettings worldSettings3) {
		this.changeWorld1((World)null);
		System.gc();
		if(this.saveLoader.isOldMapFormat(string1)) {
			this.convertMapFormat(string1, string2);
		} else {
			if(this.loadingScreen != null) {
				this.loadingScreen.printText(StatCollector.translateToLocal("menu.switchingLevel"));
				this.loadingScreen.displayLoadingString("");
			}

			ISaveHandler iSaveHandler4 = this.saveLoader.getSaveLoader(string1, false);
			World world5 = null;
			world5 = new World(iSaveHandler4, string2, worldSettings3);
			if(world5.isNewWorld) {
				this.statFileWriter.readStat(StatList.createWorldStat, 1);
				this.statFileWriter.readStat(StatList.startGameStat, 1);
				this.changeWorld2(world5, StatCollector.translateToLocal("menu.generatingLevel"));
			} else {
				this.statFileWriter.readStat(StatList.loadWorldStat, 1);
				this.statFileWriter.readStat(StatList.startGameStat, 1);
				this.changeWorld2(world5, StatCollector.translateToLocal("menu.loadingLevel"));
			}
		}

	}

	public void usePortal(int i1) {
		int i2 = this.thePlayer.dimension;
		this.thePlayer.dimension = i1;
		this.theWorld.setEntityDead(this.thePlayer);
		this.thePlayer.isDead = false;
		double d3 = this.thePlayer.posX;
		double d5 = this.thePlayer.posZ;
		double d7 = 1.0D;
		if(i2 > -1 && this.thePlayer.dimension == -1) {
			d7 = 0.125D;
		} else if(i2 == -1 && this.thePlayer.dimension > -1) {
			d7 = 8.0D;
		}

		d3 *= d7;
		d5 *= d7;
		World world9;
		if(this.thePlayer.dimension == -1) {
			this.thePlayer.setLocationAndAngles(d3, this.thePlayer.posY, d5, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
			if(this.thePlayer.isEntityAlive()) {
				this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
			}

			world9 = null;
			world9 = new World(this.theWorld, WorldProvider.getProviderForDimension(this.thePlayer.dimension));
			this.changeWorld(world9, "Entering the Nether", this.thePlayer);
		} else if(this.thePlayer.dimension == 0) {
			if(this.thePlayer.isEntityAlive()) {
				this.thePlayer.setLocationAndAngles(d3, this.thePlayer.posY, d5, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
				this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
			}

			world9 = null;
			world9 = new World(this.theWorld, WorldProvider.getProviderForDimension(this.thePlayer.dimension));
			if(i2 == -1) {
				this.changeWorld(world9, "Leaving the Nether", this.thePlayer);
			} else {
				this.changeWorld(world9, "Leaving the End", this.thePlayer);
			}
		} else {
			world9 = null;
			world9 = new World(this.theWorld, WorldProvider.getProviderForDimension(this.thePlayer.dimension));
			ChunkCoordinates chunkCoordinates10 = world9.getEntrancePortalLocation();
			d3 = (double)chunkCoordinates10.posX;
			this.thePlayer.posY = (double)chunkCoordinates10.posY;
			d5 = (double)chunkCoordinates10.posZ;
			this.thePlayer.setLocationAndAngles(d3, this.thePlayer.posY, d5, 90.0F, 0.0F);
			if(this.thePlayer.isEntityAlive()) {
				world9.updateEntityWithOptionalForce(this.thePlayer, false);
			}

			this.changeWorld(world9, "Entering the End", this.thePlayer);
		}

		this.thePlayer.worldObj = this.theWorld;
		System.out.println("Teleported to " + this.theWorld.worldProvider.worldType);
		if(this.thePlayer.isEntityAlive() && i2 < 1) {
			this.thePlayer.setLocationAndAngles(d3, this.thePlayer.posY, d5, this.thePlayer.rotationYaw, this.thePlayer.rotationPitch);
			this.theWorld.updateEntityWithOptionalForce(this.thePlayer, false);
			(new Teleporter()).placeInPortal(this.theWorld, this.thePlayer);
		}

	}

	public void exitToMainMenu(String string1) {
		this.theWorld = null;
		this.changeWorld2((World)null, string1);
	}

	public void changeWorld1(World world1) {
		this.changeWorld2(world1, "");
	}

	public void changeWorld2(World world1, String string2) {
		this.changeWorld(world1, string2, (EntityPlayer)null);
	}

	public void changeWorld(World world1, String string2, EntityPlayer entityPlayer3) {
		this.statFileWriter.func_27175_b();
		this.statFileWriter.syncStats();
		this.renderViewEntity = null;
		if(this.loadingScreen != null) {
			this.loadingScreen.printText(string2);
			this.loadingScreen.displayLoadingString("");
		}

		this.sndManager.playStreaming((String)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		if(this.theWorld != null) {
			this.theWorld.saveWorldIndirectly(this.loadingScreen);
		}

		this.theWorld = world1;
		if(world1 != null) {
			if(this.playerController != null) {
				this.playerController.onWorldChange(world1);
			}

			if(!this.isMultiplayerWorld()) {
				if(entityPlayer3 == null) {
					this.thePlayer = (EntityPlayerSP)world1.func_4085_a(EntityPlayerSP.class);
				}
			} else if(this.thePlayer != null) {
				this.thePlayer.preparePlayerToSpawn();
				if(world1 != null) {
					world1.spawnEntityInWorld(this.thePlayer);
				}
			}

			if(!world1.isRemote) {
				this.preloadWorld(string2);
			}

			if(this.thePlayer == null) {
				this.thePlayer = (EntityPlayerSP)this.playerController.createPlayer(world1);
				this.thePlayer.preparePlayerToSpawn();
				this.playerController.flipPlayer(this.thePlayer);
			}

			this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
			if(this.renderGlobal != null) {
				this.renderGlobal.changeWorld(world1);
			}

			if(this.effectRenderer != null) {
				this.effectRenderer.clearEffects(world1);
			}

			if(entityPlayer3 != null) {
				world1.func_6464_c();
			}

			IChunkProvider iChunkProvider4 = world1.getChunkProvider();
			if(iChunkProvider4 instanceof ChunkProviderLoadOrGenerate) {
				ChunkProviderLoadOrGenerate chunkProviderLoadOrGenerate5 = (ChunkProviderLoadOrGenerate)iChunkProvider4;
				int i6 = MathHelper.floor_float((float)((int)this.thePlayer.posX)) >> 4;
				int i7 = MathHelper.floor_float((float)((int)this.thePlayer.posZ)) >> 4;
				chunkProviderLoadOrGenerate5.setCurrentChunkOver(i6, i7);
			}

			world1.spawnPlayerWithLoadedChunks(this.thePlayer);
			this.playerController.func_6473_b(this.thePlayer);
			if(world1.isNewWorld) {
				world1.saveWorldIndirectly(this.loadingScreen);
			}

			this.renderViewEntity = this.thePlayer;
		} else {
			this.saveLoader.flushCache();
			this.thePlayer = null;
		}

		System.gc();
		this.systemTime = 0L;
	}

	private void convertMapFormat(String string1, String string2) {
		this.loadingScreen.printText("Converting World to " + this.saveLoader.getFormatName());
		this.loadingScreen.displayLoadingString("This may take a while :)");
		this.saveLoader.convertMapFormat(string1, this.loadingScreen);
		this.startWorld(string1, string2, new WorldSettings(0L, 0, true, false, WorldType.DEFAULT));
	}

	private void preloadWorld(String string1) {
		if(this.loadingScreen != null) {
			this.loadingScreen.printText(string1);
			this.loadingScreen.displayLoadingString(StatCollector.translateToLocal("menu.generatingTerrain"));
		}

		short s2 = 128;
		if(this.playerController.func_35643_e()) {
			s2 = 64;
		}

		int i3 = 0;
		int i4 = s2 * 2 / 16 + 1;
		i4 *= i4;
		IChunkProvider iChunkProvider5 = this.theWorld.getChunkProvider();
		ChunkCoordinates chunkCoordinates6 = this.theWorld.getSpawnPoint();
		if(this.thePlayer != null) {
			chunkCoordinates6.posX = (int)this.thePlayer.posX;
			chunkCoordinates6.posZ = (int)this.thePlayer.posZ;
		}

		if(iChunkProvider5 instanceof ChunkProviderLoadOrGenerate) {
			ChunkProviderLoadOrGenerate chunkProviderLoadOrGenerate7 = (ChunkProviderLoadOrGenerate)iChunkProvider5;
			chunkProviderLoadOrGenerate7.setCurrentChunkOver(chunkCoordinates6.posX >> 4, chunkCoordinates6.posZ >> 4);
		}

		for(int i10 = -s2; i10 <= s2; i10 += 16) {
			for(int i8 = -s2; i8 <= s2; i8 += 16) {
				if(this.loadingScreen != null) {
					this.loadingScreen.setLoadingProgress(i3++ * 100 / i4);
				}

				this.theWorld.getBlockId(chunkCoordinates6.posX + i10, 64, chunkCoordinates6.posZ + i8);
				if(!this.playerController.func_35643_e()) {
					while(this.theWorld.updatingLighting()) {
					}
				}
			}
		}

		if(!this.playerController.func_35643_e()) {
			if(this.loadingScreen != null) {
				this.loadingScreen.displayLoadingString(StatCollector.translateToLocal("menu.simulating"));
			}

			boolean z9 = true;
			this.theWorld.dropOldChunks();
		}

	}

	public void installResource(String string1, File file2) {
		int i3 = string1.indexOf("/");
		String string4 = string1.substring(0, i3);
		string1 = string1.substring(i3 + 1);
		if(string4.equalsIgnoreCase("sound")) {
			this.sndManager.addSound(string1, file2);
		} else if(string4.equalsIgnoreCase("newsound")) {
			this.sndManager.addSound(string1, file2);
		} else if(string4.equalsIgnoreCase("streaming")) {
			this.sndManager.addStreaming(string1, file2);
		} else if(string4.equalsIgnoreCase("music")) {
			this.sndManager.addMusic(string1, file2);
		} else if(string4.equalsIgnoreCase("newmusic")) {
			this.sndManager.addMusic(string1, file2);
		}

	}

	public String debugInfoRenders() {
		return this.renderGlobal.getDebugInfoRenders();
	}

	public String getEntityDebug() {
		return this.renderGlobal.getDebugInfoEntities();
	}

	public String getWorldProviderName() {
		return this.theWorld.getProviderName();
	}

	public String debugInfoEntities() {
		return "P: " + this.effectRenderer.getStatistics() + ". T: " + this.theWorld.getDebugLoadedEntities();
	}

	public void respawn(boolean z1, int i2, boolean z3) {
		if(!this.theWorld.isRemote && !this.theWorld.worldProvider.canRespawnHere()) {
			this.usePortal(0);
		}

		ChunkCoordinates chunkCoordinates4 = null;
		ChunkCoordinates chunkCoordinates5 = null;
		boolean z6 = true;
		if(this.thePlayer != null && !z1) {
			chunkCoordinates4 = this.thePlayer.getSpawnChunk();
			if(chunkCoordinates4 != null) {
				chunkCoordinates5 = EntityPlayer.verifyRespawnCoordinates(this.theWorld, chunkCoordinates4);
				if(chunkCoordinates5 == null) {
					this.thePlayer.addChatMessage("tile.bed.notValid");
				}
			}
		}

		if(chunkCoordinates5 == null) {
			chunkCoordinates5 = this.theWorld.getSpawnPoint();
			z6 = false;
		}

		IChunkProvider iChunkProvider7 = this.theWorld.getChunkProvider();
		if(iChunkProvider7 instanceof ChunkProviderLoadOrGenerate) {
			ChunkProviderLoadOrGenerate chunkProviderLoadOrGenerate8 = (ChunkProviderLoadOrGenerate)iChunkProvider7;
			chunkProviderLoadOrGenerate8.setCurrentChunkOver(chunkCoordinates5.posX >> 4, chunkCoordinates5.posZ >> 4);
		}

		this.theWorld.setSpawnLocation();
		this.theWorld.updateEntityList();
		int i10 = 0;
		if(this.thePlayer != null) {
			i10 = this.thePlayer.entityId;
			this.theWorld.setEntityDead(this.thePlayer);
		}

		EntityPlayerSP entityPlayerSP9 = this.thePlayer;
		this.renderViewEntity = null;
		this.thePlayer = (EntityPlayerSP)this.playerController.createPlayer(this.theWorld);
		if(z3) {
			this.thePlayer.copyPlayer(entityPlayerSP9);
		}

		this.thePlayer.dimension = i2;
		this.renderViewEntity = this.thePlayer;
		this.thePlayer.preparePlayerToSpawn();
		if(z6) {
			this.thePlayer.setSpawnChunk(chunkCoordinates4);
			this.thePlayer.setLocationAndAngles((double)((float)chunkCoordinates5.posX + 0.5F), (double)((float)chunkCoordinates5.posY + 0.1F), (double)((float)chunkCoordinates5.posZ + 0.5F), 0.0F, 0.0F);
		}

		this.playerController.flipPlayer(this.thePlayer);
		this.theWorld.spawnPlayerWithLoadedChunks(this.thePlayer);
		this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
		this.thePlayer.entityId = i10;
		this.thePlayer.func_6420_o();
		this.playerController.func_6473_b(this.thePlayer);
		this.preloadWorld(StatCollector.translateToLocal("menu.respawning"));
		if(this.currentScreen instanceof GuiGameOver) {
			this.displayGuiScreen((GuiScreen)null);
		}

	}

	public static void startMainThread1(String string0, String string1) throws LWJGLException {
		startMainThread(string0, string1, (String)null);
	}

	public static void startMainThread(String string0, String string1, String string2) throws LWJGLException {
		boolean z3 = false;
		Frame frame5 = new Frame("Minecraft");
		Canvas canvas6 = new Canvas();
		frame5.setLayout(new BorderLayout());
		frame5.add(canvas6, "Center");
		canvas6.setPreferredSize(new Dimension(854, 480));
		frame5.pack();
		frame5.setLocationRelativeTo((Component)null);
		MinecraftImpl minecraftImpl7 = new MinecraftImpl(frame5, canvas6, (MinecraftApplet)null, 854, 480, z3, frame5);
		Thread thread8 = new Thread(minecraftImpl7, "Minecraft main thread");
		thread8.setPriority(10);
		minecraftImpl7.minecraftUri = "www.minecraft.net";
		if(string0 != null && string1 != null) {
			minecraftImpl7.session = new Session(string0, string1);
		} else {
			minecraftImpl7.session = new Session("Player" + System.currentTimeMillis() % 1000L, "");
		}

		if(string2 != null) {
			String[] string9 = string2.split(":");
			minecraftImpl7.setServer(string9[0], Integer.parseInt(string9[1]));
		}

		frame5.setVisible(true);
		frame5.addWindowListener(new GameWindowListener(minecraftImpl7, thread8));
		thread8.start();
	}

	public NetClientHandler getSendQueue() {
		return this.thePlayer instanceof EntityClientPlayerMP ? ((EntityClientPlayerMP)this.thePlayer).sendQueue : null;
	}

	public static void main(String[] string0) throws LWJGLException {
		String string1 = null;
		String string2 = null;
		string1 = "Player" + System.currentTimeMillis() % 1000L;
		if(string0.length > 0) {
			string1 = string0[0];
		}

		string2 = "-";
		if(string0.length > 1) {
			string2 = string0[1];
		}

		startMainThread1(string1, string2);
	}

	public static boolean isGuiEnabled() {
		return theMinecraft == null || !theMinecraft.gameSettings.hideGUI;
	}

	public static boolean isFancyGraphicsEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.fancyGraphics;
	}

	public static boolean isAmbientOcclusionEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.ambientOcclusion;
	}

	public static boolean isDebugInfoEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.showDebugInfo;
	}

	public boolean lineIsCommand(String string1) {
		if(string1.startsWith("/")) {
			;
		}

		return false;
	}

	private void clickMiddleMouseButton() {
		if(this.objectMouseOver != null) {
			boolean z1 = this.thePlayer.capabilities.isCreativeMode;
			int i2 = this.theWorld.getBlockId(this.objectMouseOver.blockX, this.objectMouseOver.blockY, this.objectMouseOver.blockZ);
			if(!z1) {
				if(i2 == Block.grass.blockID) {
					i2 = Block.dirt.blockID;
				}

				if(i2 == Block.stairDouble.blockID) {
					i2 = Block.stairSingle.blockID;
				}

				if(i2 == Block.bedrock.blockID) {
					i2 = Block.stone.blockID;
				}
			}

			int i3 = 0;
			boolean z4 = false;
			if(Item.itemsList[i2] != null && Item.itemsList[i2].getHasSubtypes()) {
				i3 = this.theWorld.getBlockMetadata(this.objectMouseOver.blockX, this.objectMouseOver.blockY, this.objectMouseOver.blockZ);
				z4 = true;
			}

			if(Item.itemsList[i2] != null && Item.itemsList[i2] instanceof ItemBlock) {
				Block block5 = Block.blocksList[i2];
				int i6 = block5.idDropped(i3, this.thePlayer.worldObj.rand, 0);
				if(i6 > 0) {
					i2 = i6;
				}
			}

			this.thePlayer.inventory.setCurrentItem(i2, i3, z4, z1);
			if(z1) {
				int i7 = this.thePlayer.inventorySlots.inventorySlots.size() - 9 + this.thePlayer.inventory.currentItem;
				this.playerController.sendSlotPacket(this.thePlayer.inventory.getStackInSlot(this.thePlayer.inventory.currentItem), i7);
			}
		}

	}

	public static String func_52003_C() {
		return "1.2.5";
	}

	public static void func_52004_D() {
		PlayerUsageSnooper playerUsageSnooper0 = new PlayerUsageSnooper("client");
		playerUsageSnooper0.func_52022_a("version", func_52003_C());
		playerUsageSnooper0.func_52022_a("os_name", System.getProperty("os.name"));
		playerUsageSnooper0.func_52022_a("os_version", System.getProperty("os.version"));
		playerUsageSnooper0.func_52022_a("os_architecture", System.getProperty("os.arch"));
		playerUsageSnooper0.func_52022_a("memory_total", Runtime.getRuntime().totalMemory());
		playerUsageSnooper0.func_52022_a("memory_max", Runtime.getRuntime().maxMemory());
		playerUsageSnooper0.func_52022_a("java_version", System.getProperty("java.version"));
		playerUsageSnooper0.func_52022_a("opengl_version", GL11.glGetString(GL11.GL_VERSION));
		playerUsageSnooper0.func_52022_a("opengl_vendor", GL11.glGetString(GL11.GL_VENDOR));
		playerUsageSnooper0.func_52021_a();
	}

	static final class SyntheticClass_1 {
		public static final int[] $SwitchMap$net$minecraft$src$EnumOS2 = new int[EnumOS2.values().length];

		static {
			try {
				$SwitchMap$net$minecraft$src$EnumOS2[EnumOS2.linux.ordinal()] = 1;
			} catch (NoSuchFieldError noSuchFieldError4) {
			}

			try {
				$SwitchMap$net$minecraft$src$EnumOS2[EnumOS2.solaris.ordinal()] = 2;
			} catch (NoSuchFieldError noSuchFieldError3) {
			}

			try {
				$SwitchMap$net$minecraft$src$EnumOS2[EnumOS2.windows.ordinal()] = 3;
			} catch (NoSuchFieldError noSuchFieldError2) {
			}

			try {
				$SwitchMap$net$minecraft$src$EnumOS2[EnumOS2.macos.ordinal()] = 4;
			} catch (NoSuchFieldError noSuchFieldError1) {
			}

		}
	}
}
