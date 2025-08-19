package net.minecraft.client;

import java.awt.Canvas;
import java.awt.Component;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import javax.swing.JOptionPane;

import net.minecraft.client.controller.PlayerController;
import net.minecraft.client.controller.PlayerControllerCreative;
import net.minecraft.client.controller.PlayerControllerSP;
import net.minecraft.client.effect.EffectRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.container.GuiInventory;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.client.player.MovementInputFromOptions;
import net.minecraft.client.render.EntityRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.RenderGlobal;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.texture.TextureFlamesFX;
import net.minecraft.client.render.texture.TextureGearsFX;
import net.minecraft.client.render.texture.TextureLavaFX;
import net.minecraft.client.render.texture.TextureWaterFX;
import net.minecraft.client.render.texture.TextureWaterFlowFX;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import util.MathHelper;

public final class Minecraft implements Runnable {
	public PlayerController playerController = new PlayerControllerSP(this);
	private boolean fullscreen = false;
	public int displayWidth;
	public int displayHeight;
	private OpenGlCapsChecker glCapabilities;
	private Timer timer = new Timer(20.0F);
	public World theWorld;
	public RenderGlobal renderGlobal;
	public EntityPlayerSP thePlayer;
	public EffectRenderer effectRenderer;
	public Session session = null;
	public String minecraftUri;
	private Canvas mcCanvas;
	public boolean appletMode = true;
	public volatile boolean isGamePaused = false;
	public RenderEngine renderEngine;
	public FontRenderer fontRenderer;
	public GuiScreen currentScreen = null;
	private LoadingScreenRenderer loadingScreen = new LoadingScreenRenderer(this);
	public EntityRenderer entityRenderer = new EntityRenderer(this);
	private ThreadDownloadResources downloadResourcesThread;
	private int ticksRan = 0;
	private int leftClickCounter = 0;
	private int tempDisplayWidth;
	private int tempDisplayHeight;
	public String loadMapUser = null;
	public int loadMapID = 0;
	public GuiIngame ingameGUI;
	public boolean skipRenderWorld = false;
	public MovingObjectPosition objectMouseOver;
	public GameSettings gameSettings;
	public SoundManager sndManager;
	public MouseHelper mouseHelper;
	private File mcDataDir;
	private String server;
	private TextureWaterFX textureWaterFX;
	private TextureLavaFX textureLavaFX;
	private File minecraftDir;
	volatile boolean running;
	public String debug;
	public boolean inventoryScreen;
	private int prevFrameTime;
	public boolean inGameHasFocus;
	private long systemTime;

	public Minecraft(Canvas canvas1, MinecraftApplet minecraftApplet2, int i3, int i4, boolean z5) {
		new ModelBiped(0.0F);
		this.objectMouseOver = null;
		this.sndManager = new SoundManager();
		this.server = null;
		this.textureWaterFX = new TextureWaterFX();
		this.textureLavaFX = new TextureLavaFX();
		this.minecraftDir = null;
		this.running = false;
		this.debug = "";
		this.inventoryScreen = false;
		this.prevFrameTime = 0;
		this.inGameHasFocus = false;
		this.systemTime = System.currentTimeMillis();
		this.tempDisplayWidth = i3;
		this.tempDisplayHeight = i4;
		this.fullscreen = z5;
		new ThreadSleepForever(this, "Timer hack thread");
		this.mcCanvas = canvas1;
		this.displayWidth = i3;
		this.displayHeight = i4;
		this.fullscreen = z5;
	}

	public final void setServer(String string1, int i2) {
		this.server = string1;
	}

	public final File getAppDir() {
		if(this.minecraftDir == null) {
			String string2 = "minecraft";
			String string1 = System.getProperty("user.home", ".");
			String string3;
			File file4;
			switch(Minecraft.SyntheticClass_1.$SwitchMap$net$minecraft$client$EnumOS[((string3 = System.getProperty("os.name").toLowerCase()).contains("win") ? EnumOS.windows : (string3.contains("mac") ? EnumOS.macos : (string3.contains("solaris") ? EnumOS.solaris : (string3.contains("sunos") ? EnumOS.solaris : (string3.contains("linux") ? EnumOS.linux : (string3.contains("unix") ? EnumOS.linux : EnumOS.unknown)))))).ordinal()]) {
			case 1:
			case 2:
				file4 = new File(string1, '.' + string2 + '/');
				break;
			case 3:
				if((string3 = System.getenv("APPDATA")) != null) {
					file4 = new File(string3, "." + string2 + '/');
				} else {
					file4 = new File(string1, '.' + string2 + '/');
				}
				break;
			case 4:
				file4 = new File(string1, "Library/Application Support/" + string2);
				break;
			default:
				file4 = new File(string1, string2 + '/');
			}

			if(!file4.exists() && !file4.mkdirs()) {
				throw new RuntimeException("The working directory could not be created: " + file4);
			}

			this.minecraftDir = file4;
		}

		return this.minecraftDir;
	}

	public final void displayGuiScreen(GuiScreen guiScreen1) {
		if(!(this.currentScreen instanceof GuiErrorScreen)) {
			if(this.currentScreen != null) {
				this.currentScreen.onGuiClosed();
			}

			if(guiScreen1 == null && this.theWorld == null) {
				guiScreen1 = new GuiMainMenu();
			} else if(guiScreen1 == null && this.thePlayer.health <= 0) {
				guiScreen1 = new GuiGameOver();
			}

			this.currentScreen = (GuiScreen)guiScreen1;
			if(guiScreen1 != null) {
				this.inputLock();
				ScaledResolution scaledResolution2;
				int i3 = (scaledResolution2 = new ScaledResolution(this.displayWidth, this.displayHeight)).getScaledWidth();
				int i4 = scaledResolution2.getScaledHeight();
				((GuiScreen)guiScreen1).setWorldAndResolution(this, i3, i4);
				this.skipRenderWorld = false;
			} else {
				this.setIngameFocus();
			}
		}
	}

	public final void shutdownMinecraftApplet() {
		try {
			if(this.downloadResourcesThread != null) {
				this.downloadResourcesThread.closeMinecraft();
			}
		} catch (Exception exception5) {
		}

		try {
			System.out.println("Stopping!");
			Object object1 = null;
			this.changeWorld2((World)null, "");
			this.sndManager.closeMinecraft();
			Mouse.destroy();
			Keyboard.destroy();
		} finally {
			Display.destroy();
		}

	}

	public final void run() {
		this.running = true;

		try {
			Minecraft minecraft1 = this;
			if(this.mcCanvas != null) {
				Display.setParent(this.mcCanvas);
			} else if(this.fullscreen) {
				Display.setFullscreen(true);
				this.displayWidth = Display.getDisplayMode().getWidth();
				this.displayHeight = Display.getDisplayMode().getHeight();
			} else {
				Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
			}

			Display.setTitle("Minecraft Minecraft Infdev");

			ContextCapabilities contextCapabilities2;
			try {
				Display.create();
				System.out.println("LWJGL version: " + Sys.getVersion());
				System.out.println("GL RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
				System.out.println("GL VENDOR: " + GL11.glGetString(GL11.GL_VENDOR));
				System.out.println("GL VERSION: " + GL11.glGetString(GL11.GL_VERSION));
				contextCapabilities2 = GLContext.getCapabilities();
				System.out.println("OpenGL 3.0: " + contextCapabilities2.OpenGL30);
				System.out.println("OpenGL 3.1: " + contextCapabilities2.OpenGL31);
				System.out.println("OpenGL 3.2: " + contextCapabilities2.OpenGL32);
				System.out.println("ARB_compatibility: " + contextCapabilities2.GL_ARB_compatibility);
				if(contextCapabilities2.OpenGL32) {
					IntBuffer intBuffer24 = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
					GL11.glGetInteger(37158, intBuffer24);
					int i25 = intBuffer24.get(0);
					System.out.println("PROFILE MASK: " + Integer.toBinaryString(i25));
					System.out.println("CORE PROFILE: " + ((i25 & 1) != 0));
					System.out.println("COMPATIBILITY PROFILE: " + ((i25 & 2) != 0));
				}
			} catch (LWJGLException lWJGLException17) {
				lWJGLException17.printStackTrace();

				try {
					Thread.sleep(1000L);
				} catch (InterruptedException interruptedException16) {
				}

				Display.create();
			}

			Keyboard.create();
			Mouse.create();
			this.mouseHelper = new MouseHelper(this.mcCanvas);

			try {
				Controllers.create();
			} catch (Exception exception15) {
				exception15.printStackTrace();
			}

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
			this.glCapabilities = new OpenGlCapsChecker();
			this.mcDataDir = this.getAppDir();
			this.gameSettings = new GameSettings(this, this.mcDataDir);
			this.sndManager.loadSoundSettings(this.gameSettings);
			this.renderEngine = new RenderEngine(this.gameSettings);
			this.renderEngine.registerTextureFX(this.textureLavaFX);
			this.renderEngine.registerTextureFX(this.textureWaterFX);
			this.renderEngine.registerTextureFX(new TextureWaterFlowFX());
			this.renderEngine.registerTextureFX(new TextureFlamesFX(0));
			this.renderEngine.registerTextureFX(new TextureFlamesFX(1));
			this.renderEngine.registerTextureFX(new TextureGearsFX(0));
			this.renderEngine.registerTextureFX(new TextureGearsFX(1));
			this.fontRenderer = new FontRenderer(this.gameSettings, "/default.png", this.renderEngine);
			BufferUtils.createIntBuffer(256).clear().limit(256);
			this.renderGlobal = new RenderGlobal(this, this.renderEngine);
			GL11.glViewport(0, 0, this.displayWidth, this.displayHeight);
			if(this.server != null && this.session != null) {
				contextCapabilities2 = null;
				this.changeWorld2((World)null, "");
			} else if(this.theWorld == null) {
				this.displayGuiScreen(new GuiMainMenu());
			}

			this.effectRenderer = new EffectRenderer(this.theWorld, this.renderEngine);

			try {
				minecraft1.downloadResourcesThread = new ThreadDownloadResources(minecraft1.mcDataDir, minecraft1);
				minecraft1.downloadResourcesThread.start();
			} catch (Exception exception14) {
			}

			this.ingameGUI = new GuiIngame(this);
		} catch (Exception exception22) {
			exception22.printStackTrace();
			JOptionPane.showMessageDialog((Component)null, exception22.toString(), "Failed to start Minecraft", 0);
			return;
		}

		long j23 = System.currentTimeMillis();
		int i3 = 0;

		try {
			while(this.running) {
				if(this.mcCanvas == null && Display.isCloseRequested()) {
					this.running = false;
				}

				try {
					if(this.isGamePaused) {
						float f4 = this.timer.renderPartialTicks;
						this.timer.updateTimer();
						this.timer.renderPartialTicks = f4;
					} else {
						this.timer.updateTimer();
					}

					int i26 = 0;

					while(true) {
						if(i26 >= this.timer.elapsedTicks) {
							if(this.isGamePaused) {
								this.timer.renderPartialTicks = 1.0F;
							}

							this.sndManager.setListener(this.thePlayer, this.timer.renderPartialTicks);
							GL11.glEnable(GL11.GL_TEXTURE_2D);
							if(this.theWorld != null) {
								while(this.theWorld.updatingLighting()) {
								}
							}

							this.playerController.setPartialTime(this.timer.renderPartialTicks);
							this.entityRenderer.updateCameraAndRender(this.timer.renderPartialTicks);
							if(!Display.isActive()) {
								if(this.fullscreen) {
									this.toggleFullscreen();
								}

								Thread.sleep(10L);
							}

							if(this.mcCanvas != null && !this.fullscreen && (this.mcCanvas.getWidth() != this.displayWidth || this.mcCanvas.getHeight() != this.displayHeight)) {
								this.displayWidth = this.mcCanvas.getWidth();
								this.displayHeight = this.mcCanvas.getHeight();
								this.resize(this.displayWidth, this.displayHeight);
							}

							if(this.gameSettings.limitFramerate) {
								Thread.sleep(5L);
							}

							++i3;
							this.isGamePaused = this.currentScreen != null && this.currentScreen.doesGuiPauseGame();
							break;
						}

						++this.ticksRan;
						this.runTick();
						++i26;
					}
				} catch (Exception exception18) {
					this.displayGuiScreen(new GuiErrorScreen("Client error", "The game broke! [" + exception18 + "]"));
					exception18.printStackTrace();
					return;
				}

				while(System.currentTimeMillis() >= j23 + 1000L) {
					this.debug = i3 + " fps, " + WorldRenderer.chunksUpdated + " chunk updates";
					WorldRenderer.chunksUpdated = 0;
					j23 += 1000L;
					i3 = 0;
				}
			}

			return;
		} catch (MinecraftError minecraftError19) {
		} catch (Exception exception20) {
			exception20.printStackTrace();
			return;
		} finally {
			this.shutdownMinecraftApplet();
		}

	}

	public final void setIngameFocus() {
		if(Display.isActive()) {
			if(!this.inventoryScreen) {
				this.inventoryScreen = true;
				this.mouseHelper.grabMouseCursor();
				this.displayGuiScreen((GuiScreen)null);
				this.prevFrameTime = this.ticksRan + 10000;
			}
		}
	}

	private void inputLock() {
		if(this.inventoryScreen) {
			if(this.thePlayer != null) {
				EntityPlayerSP entityPlayerSP1 = this.thePlayer;
				this.thePlayer.movementInput.resetKeyState();
			}

			this.inventoryScreen = false;

			try {
				Mouse.setNativeCursor((Cursor)null);
			} catch (LWJGLException lWJGLException2) {
				lWJGLException2.printStackTrace();
			}
		}
	}

	public final void displayInGameMenu() {
		if(this.currentScreen == null) {
			this.displayGuiScreen(new GuiIngameMenu());
		}
	}

	private void clickMouse(int i1) {
		if(i1 != 0 || this.leftClickCounter <= 0) {
			if(i1 == 0) {
				this.entityRenderer.itemRenderer.equippedItemRender();
			}

			ItemStack itemStack2;
			int i3;
			World world5;
			if(i1 == 1 && (itemStack2 = this.thePlayer.inventory.getCurrentItem()) != null) {
				i3 = itemStack2.stackSize;
				EntityPlayerSP entityPlayerSP7 = this.thePlayer;
				world5 = this.theWorld;
				ItemStack itemStack4;
				if((itemStack4 = itemStack2.getItem().onItemRightClick(itemStack2, world5, entityPlayerSP7)) != itemStack2 || itemStack4 != null && itemStack4.stackSize != i3) {
					this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = itemStack4;
					this.entityRenderer.itemRenderer.resetEquippedProgress();
					if(itemStack4.stackSize == 0) {
						this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
					}
				}
			}

			if(this.objectMouseOver == null) {
				if(i1 == 0 && !(this.playerController instanceof PlayerControllerCreative)) {
					this.leftClickCounter = 10;
				}

			} else {
				ItemStack itemStack9;
				if(this.objectMouseOver.typeOfHit == 1) {
					if(i1 == 0) {
						Entity entity14 = this.objectMouseOver.entityHit;
						EntityPlayerSP entityPlayerSP12 = this.thePlayer;
						InventoryPlayer inventoryPlayer11;
						int i10000 = (itemStack9 = (inventoryPlayer11 = this.thePlayer.inventory).getStackInSlot(inventoryPlayer11.currentItem)) != null ? Item.itemsList[itemStack9.itemID].getDamageVsEntity() : 1;
						int i17 = i10000;
						if(i10000 > 0) {
							entity14.attackEntityFrom(entityPlayerSP12, i17);
							if((itemStack2 = entityPlayerSP12.inventory.getCurrentItem()) != null && entity14 instanceof EntityLiving) {
								EntityLiving entityLiving8 = (EntityLiving)entity14;
								Item.itemsList[itemStack2.itemID].hitEntity(itemStack2);
								if(itemStack2.stackSize <= 0) {
									entityPlayerSP12.displayInventoryGUI();
								}
							}
						}

						return;
					}
				} else if(this.objectMouseOver.typeOfHit == 0) {
					int i10 = this.objectMouseOver.blockX;
					i3 = this.objectMouseOver.blockY;
					int i13 = this.objectMouseOver.blockZ;
					int i15 = this.objectMouseOver.sideHit;
					Block block6 = Block.blocksList[this.theWorld.getBlockId(i10, i3, i13)];
					if(i1 == 0) {
						this.theWorld.extinguishFire(i10, i3, i13, this.objectMouseOver.sideHit);
						if(block6 != Block.bedrock) {
							this.playerController.clickBlock(i10, i3, i13);
							return;
						}
					} else {
						itemStack9 = this.thePlayer.inventory.getCurrentItem();
						int i16;
						if((i16 = this.theWorld.getBlockId(i10, i3, i13)) > 0 && Block.blocksList[i16].blockActivated(this.theWorld, i10, i3, i13, this.thePlayer)) {
							return;
						}

						if(itemStack9 == null) {
							return;
						}

						i16 = itemStack9.stackSize;
						int i18 = i15;
						world5 = this.theWorld;
						if(itemStack9.getItem().onItemUse(itemStack9, world5, i10, i3, i13, i18)) {
							this.entityRenderer.itemRenderer.equippedItemRender();
						}

						if(itemStack9.stackSize == 0) {
							this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
							return;
						}

						if(itemStack9.stackSize != i16) {
							this.entityRenderer.itemRenderer.resetEquippedProgress2();
						}
					}
				}

			}
		}
	}

	public final void toggleFullscreen() {
		try {
			this.fullscreen = !this.fullscreen;
			System.out.println("Toggle fullscreen!");
			if(this.fullscreen) {
				Display.setDisplayMode(Display.getDesktopDisplayMode());
				this.displayWidth = Display.getDisplayMode().getWidth();
				this.displayHeight = Display.getDisplayMode().getHeight();
			} else {
				if(this.mcCanvas != null) {
					this.displayWidth = this.mcCanvas.getWidth();
					this.displayHeight = this.mcCanvas.getHeight();
				} else {
					this.displayWidth = this.tempDisplayWidth;
					this.displayHeight = this.tempDisplayHeight;
				}

				Display.setDisplayMode(new DisplayMode(this.tempDisplayWidth, this.tempDisplayHeight));
			}

			this.inputLock();
			Display.setFullscreen(this.fullscreen);
			Display.update();
			Thread.sleep(1000L);
			if(this.fullscreen) {
				this.setIngameFocus();
			}

			if(this.currentScreen != null) {
				this.inputLock();
				this.resize(this.displayWidth, this.displayHeight);
			}

			System.out.println("Size: " + this.displayWidth + ", " + this.displayHeight);
		} catch (Exception exception2) {
			exception2.printStackTrace();
		}
	}

	private void resize(int i1, int i2) {
		this.displayWidth = i1;
		this.displayHeight = i2;
		if(this.currentScreen != null) {
			ScaledResolution scaledResolution3;
			i2 = (scaledResolution3 = new ScaledResolution(i1, i2)).getScaledWidth();
			i1 = scaledResolution3.getScaledHeight();
			this.currentScreen.setWorldAndResolution(this, i2, i1);
		}

	}

	private void runTick() {
		this.ingameGUI.updateTick();
		if(!this.isGamePaused && this.theWorld != null) {
			this.playerController.onUpdate();
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture("/terrain.png"));
		if(!this.isGamePaused) {
			this.renderEngine.updateDynamicTextures();
		}

		if(this.currentScreen == null && this.thePlayer != null && this.thePlayer.health <= 0) {
			this.displayGuiScreen((GuiScreen)null);
		}

		if(this.currentScreen == null || this.currentScreen.allowUserInput) {
			label286:
			while(true) {
				while(true) {
					while(true) {
						int i1;
						int i2;
						do {
							if(!Mouse.next()) {
								if(this.leftClickCounter > 0) {
									--this.leftClickCounter;
								}

								while(true) {
									while(true) {
										do {
											boolean z3;
											if(!Keyboard.next()) {
												if(this.currentScreen == null) {
													if(Mouse.isButtonDown(0) && (float)(this.ticksRan - this.prevFrameTime) >= this.timer.ticksPerSecond / 4.0F && this.inventoryScreen) {
														this.clickMouse(0);
														this.prevFrameTime = this.ticksRan;
													}

													if(Mouse.isButtonDown(1) && (float)(this.ticksRan - this.prevFrameTime) >= this.timer.ticksPerSecond / 4.0F && this.inventoryScreen) {
														this.clickMouse(1);
														this.prevFrameTime = this.ticksRan;
													}
												}

												z3 = this.currentScreen == null && Mouse.isButtonDown(0) && this.inventoryScreen;
												boolean z8 = false;
												if(!this.playerController.isInTestMode && this.leftClickCounter <= 0) {
													if(z3 && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == 0) {
														i2 = this.objectMouseOver.blockX;
														int i7 = this.objectMouseOver.blockY;
														int i4 = this.objectMouseOver.blockZ;
														this.playerController.sendBlockRemoving(i2, i7, i4, this.objectMouseOver.sideHit);
														this.effectRenderer.addBlockHitEffects(i2, i7, i4, this.objectMouseOver.sideHit);
													} else {
														this.playerController.resetBlockRemoving();
													}
												}
												break label286;
											}

											EntityPlayerSP entityPlayerSP10000 = this.thePlayer;
											int i10001 = Keyboard.getEventKey();
											z3 = Keyboard.getEventKeyState();
											i2 = i10001;
											entityPlayerSP10000.movementInput.checkKeyForMovementInput(i2, z3);
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

												if(this.playerController instanceof PlayerControllerCreative) {
													Keyboard.getEventKey();
													Keyboard.getEventKey();
												}

												if(Keyboard.getEventKey() == Keyboard.KEY_F5) {
													this.gameSettings.thirdPersonView = !this.gameSettings.thirdPersonView;
													this.inGameHasFocus = !this.inGameHasFocus;
												}

												if(Keyboard.getEventKey() == this.gameSettings.keyBindInventory.keyCode) {
													this.displayGuiScreen(new GuiInventory(this.thePlayer.inventory));
												}

												if(Keyboard.getEventKey() == this.gameSettings.keyBindDrop.keyCode) {
													this.thePlayer.dropPlayerItemWithRandomChoice(this.thePlayer.inventory.decrStackSize(this.thePlayer.inventory.currentItem, 1), false);
												}
											}

											for(i1 = 0; i1 < 9; ++i1) {
												if(Keyboard.getEventKey() == i1 + Keyboard.KEY_1) {
													this.thePlayer.inventory.currentItem = i1;
												}
											}

											if(Keyboard.getEventKey() == this.gameSettings.keyBindToggleFog.keyCode) {
												this.gameSettings.setOptionFloatValue(4, !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ? 1 : -1);
											}
										}
									}
								}
							}
						} while(System.currentTimeMillis() - this.systemTime > 200L);

						if((i1 = Mouse.getEventDWheel()) != 0) {
							i2 = i1;
							InventoryPlayer inventoryPlayer5 = this.thePlayer.inventory;
							if(i1 > 0) {
								i2 = 1;
							}

							if(i2 < 0) {
								i2 = -1;
							}

							for(inventoryPlayer5.currentItem -= i2; inventoryPlayer5.currentItem < 0; inventoryPlayer5.currentItem += 9) {
							}

							while(inventoryPlayer5.currentItem >= 9) {
								inventoryPlayer5.currentItem -= 9;
							}
						}

						if(this.currentScreen == null) {
							if(!this.inventoryScreen && Mouse.getEventButtonState()) {
								this.setIngameFocus();
							} else {
								if(Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
									this.clickMouse(0);
									this.prevFrameTime = this.ticksRan;
								}

								if(Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
									this.clickMouse(1);
									this.prevFrameTime = this.ticksRan;
								}

								if(Mouse.getEventButton() == 2 && Mouse.getEventButtonState() && this.objectMouseOver != null) {
									if((i2 = this.theWorld.getBlockId(this.objectMouseOver.blockX, this.objectMouseOver.blockY, this.objectMouseOver.blockZ)) == Block.grass.blockID) {
										i2 = Block.dirt.blockID;
									}

									if(i2 == Block.stairDouble.blockID) {
										i2 = Block.stairSingle.blockID;
									}

									if(i2 == Block.bedrock.blockID) {
										i2 = Block.stone.blockID;
									}

									this.thePlayer.inventory.getFirstEmptyStack(i2);
								}
							}
						} else if(this.currentScreen != null) {
							this.currentScreen.handleMouseInput();
						}
					}
				}
			}
		}

		if(this.currentScreen != null) {
			this.prevFrameTime = this.ticksRan + 10000;
		}

		if(this.currentScreen != null) {
			GuiScreen guiScreen6 = this.currentScreen;

			while(Mouse.next()) {
				guiScreen6.handleMouseInput();
			}

			while(Keyboard.next()) {
				guiScreen6.handleKeyboardInput();
			}

			if(this.currentScreen != null) {
				this.currentScreen.updateScreen();
			}
		}

		if(this.theWorld != null) {
			this.theWorld.difficultySetting = this.gameSettings.difficulty;
			if(!this.isGamePaused) {
				this.entityRenderer.updateRenderer();
			}

			if(!this.isGamePaused) {
				this.renderGlobal.updateClouds();
			}

			if(!this.isGamePaused) {
				this.theWorld.levelEntities();
			}

			if(!this.isGamePaused) {
				this.theWorld.restartTimeOfDay();
			}

			if(!this.isGamePaused) {
				this.theWorld.randomDisplayUpdates(MathHelper.floor_double(this.thePlayer.posX), MathHelper.floor_double(this.thePlayer.posY), MathHelper.floor_double(this.thePlayer.posZ));
			}

			if(!this.isGamePaused) {
				this.effectRenderer.updateEffects();
			}
		}

		this.systemTime = System.currentTimeMillis();
	}

	public final void startWorld(String string1) {
		Object object2 = null;
		this.changeWorld2((World)null, "");
		System.gc();
		World world3;
		if((world3 = new World(new File(this.getAppDir(), "saves"), string1)).isNewWorld) {
			this.changeWorld2(world3, "Generating level");
		} else {
			this.changeWorld2(world3, "Loading level");
		}

		this.loadingScreen.setText("Preparing lights");
		int i4 = 0;

		while(world3.lightUpdatesNeeded() > 0) {
			this.loadingScreen.setProgress(i4++ % 100);
			world3.updatingLighting();
		}

	}

	public final void closeWorld(World world1) {
		this.changeWorld2((World)null, "");
	}

	private void changeWorld2(World world1, String string2) {
		if(this.theWorld != null) {
			this.theWorld.saveWorldIndirectly();
		}

		this.theWorld = world1;
		if(world1 != null) {
			this.thePlayer = null;
			world1.playerEntity = this.thePlayer;
			if(this.thePlayer == null) {
				this.thePlayer = new EntityPlayerSP(this, world1, this.session);
				this.thePlayer.preparePlayerToSpawn();
			}

			this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
			this.playerController.onRespawn(this.thePlayer);
			if(this.renderGlobal != null) {
				this.renderGlobal.changeWorld(world1);
			}

			if(this.effectRenderer != null) {
				this.effectRenderer.clearEffects(world1);
			}

			this.changeWorld1(string2);
			world1.playerEntity = this.thePlayer;
			world1.spawnPlayer();
		}

		System.gc();
		this.systemTime = 0L;
	}

	private void changeWorld1(String string1) {
		this.loadingScreen.setTitle(string1);
		this.loadingScreen.setText("Preparing chunks");

		for(int i5 = -196; i5 <= 196; i5 += 16) {
			this.loadingScreen.setProgress((i5 + 196) * 100 / 392);
			int i2 = MathHelper.floor_double(this.thePlayer.posX);
			int i3 = MathHelper.floor_double(this.thePlayer.posZ);

			for(int i4 = -196; i4 <= 196; i4 += 16) {
				this.theWorld.getBlockId(i2 + i5, 64, i3 + i4);
			}
		}

	}

	public final void respawn() {
		if(this.thePlayer != null && this.theWorld != null) {
			World.setEntityDead(this.thePlayer);
		}

		this.thePlayer = new EntityPlayerSP(this, this.theWorld, this.session);
		this.thePlayer.preparePlayerToSpawn();
		if(this.theWorld != null) {
			this.theWorld.playerEntity = this.thePlayer;
			this.theWorld.spawnPlayer();
		}

		this.thePlayer.movementInput = new MovementInputFromOptions(this.gameSettings);
		this.playerController.onRespawn(this.thePlayer);
		this.changeWorld1("Respawning");
	}

	static final class SyntheticClass_1 {
		static final int[] $SwitchMap$net$minecraft$client$EnumOS = new int[EnumOS.values().length];

		static {
			try {
				$SwitchMap$net$minecraft$client$EnumOS[EnumOS.linux.ordinal()] = 1;
			} catch (NoSuchFieldError noSuchFieldError3) {
			}

			try {
				$SwitchMap$net$minecraft$client$EnumOS[EnumOS.solaris.ordinal()] = 2;
			} catch (NoSuchFieldError noSuchFieldError2) {
			}

			try {
				$SwitchMap$net$minecraft$client$EnumOS[EnumOS.windows.ordinal()] = 3;
			} catch (NoSuchFieldError noSuchFieldError1) {
			}

			try {
				$SwitchMap$net$minecraft$client$EnumOS[EnumOS.macos.ordinal()] = 4;
			} catch (NoSuchFieldError noSuchFieldError0) {
			}
		}
	}
}