package net.minecraft.src;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

import paulscode.sound.SoundSystem;

public class GuiMainMenu extends GuiScreen {
	private static final Random rand = new Random();
	public static boolean mmactive = false;
	private float updateCounter = 0.0F;
	private String splashText = "missingno";
	private GuiButton multiplayerButton;
	public static boolean renderOption = mod_Aether.worldMenu;
	public static boolean themeOption = mod_Aether.aetherMenu;
	private List saveList;
	private int selectedWorld;
	public int renderSplit = 4;
	public int closeTicks;
	public static int musicId = -1;
	private String hoverText = "";
	public static boolean loadingWorld = false;
	public static GuiAchievement ach;

	public GuiMainMenu() {
		try {
			ArrayList var5 = new ArrayList();
			BufferedReader var2 = new BufferedReader(new InputStreamReader(GuiMainMenu.class.getResourceAsStream("/title/splashes.txt"), Charset.forName("UTF-8")));
			String var3 = "";

			String var4;
			while((var4 = var2.readLine()) != null) {
				var4 = var4.trim();
				if(var4.length() > 0) {
					var5.add(var4);
				}
			}

			this.splashText = (String)var5.get(rand.nextInt(var5.size()));
		} catch (Exception exception5) {
		}

	}

	public boolean OnTickInGame(Minecraft game) {
		++this.closeTicks;
		return true;
	}

	public void updateScreen() {
		++this.updateCounter;
		if(renderOption && ModLoader.getMinecraftInstance().thePlayer != null && !ModLoader.getMinecraftInstance().thePlayer.isDead) {
			EntityPlayerSP entityPlayerSP10000 = ModLoader.getMinecraftInstance().thePlayer;
			entityPlayerSP10000.rotationYaw += 0.2F;
			ModLoader.getMinecraftInstance().thePlayer.rotationPitch = 0.0F;
			ModLoader.getMinecraftInstance().thePlayer.fallDistance = 0.0F;
		}

	}

	protected void keyTyped(char var1, int var2) {
	}

	public void initGui() {
		mmactive = true;
		this.mc.guiAchievement = new GuiAchievementAether(this.mc);
		this.mc.ingameGUI = new GuiIngameAether(this.mc);
		if(musicId == -1 && !loadingWorld) {
			this.mc.sndManager.playSoundFX("aether.music.menu", 1.0F, 1.0F);

			try {
				musicId = ((Integer)ModLoader.getPrivateValue(SoundManager.class, this.mc.sndManager, "e")).intValue().intValue();
				ModLoader.setPrivateValue(SoundManager.class, this.mc.sndManager, "i", 999999999);
			} catch (Exception exception6) {
				if(exception6 instanceof NoSuchFieldException) {
					try {
						musicId = ((Integer)ModLoader.getPrivateValue(SoundManager.class, this.mc.sndManager, "field_587_e")).intValue().intValue();
						ModLoader.setPrivateValue(SoundManager.class, this.mc.sndManager, "ticksBeforeMusic", 999999999);
					} catch (Exception exception5) {
						exception5.printStackTrace();
					}
				} else {
					exception6.printStackTrace();
				}
			}
		}

		if(loadingWorld) {
			loadingWorld = false;
		}

		ModLoader.getMinecraftInstance().gameSettings.hideGUI = true;
		ModLoader.getMinecraftInstance().gameSettings.thirdPersonView = true;
		StringTranslate var1 = StringTranslate.getInstance();
		if(renderOption) {
			this.mc.playerController = new PlayerControllerSP(this.mc);
			if(this.mc.theWorld == null) {
				this.loadSaves();
				String var4 = this.getSaveFileName(0);
				String var5 = this.getSaveName(0);
				if(var5 != null && var4 != null) {
					this.mc.startWorld(var4, var5, 0L);
					this.mc.theWorld.autosavePeriod = 999999999;
				} else {
					renderOption = false;
				}
			}
		} else if(themeOption) {
			this.drawAetherDefaultBackground();
		} else {
			this.drawDefaultBackground();
		}

		Calendar var41 = Calendar.getInstance();
		var41.setTime(new Date());
		if(var41.get(2) + 1 == 11 && var41.get(5) == 9) {
			this.splashText = "Happy birthday, ez!";
		} else if(var41.get(2) + 1 == 6 && var41.get(5) == 1) {
			this.splashText = "Happy birthday, Notch!";
		} else if(var41.get(2) + 1 == 12 && var41.get(5) == 24) {
			this.splashText = "Merry X-mas!";
		} else if(var41.get(2) + 1 == 1 && var41.get(5) == 1) {
			this.splashText = "Happy new year!";
		} else if(var41.get(2) + 1 == 8 && var41.get(5) == 3) {
			this.splashText = "We miss you, Ripsand :(";
		}

		int var51;
		if(renderOption) {
			var51 = this.height / 4 + 20;
			this.controlList.clear();
			this.controlList.add(new GuiAetherButton(1, this.width / 4 - 100, var51, var1.translateKey("menu.singleplayer")));
			this.controlList.add(this.multiplayerButton = new GuiAetherButton(2, this.width / 4 - 100, var51 + 24, var1.translateKey("menu.multiplayer")));
			this.controlList.add(new GuiAetherButton(3, this.width / 4 - 100, var51 + 48, var1.translateKey("menu.mods")));
			this.controlList.add(new GuiButton(5, this.width - 24, 4, 20, 20, var1.translateKey("W")));
			this.controlList.add(new GuiButton(6, this.width - 48, 4, 20, 20, var1.translateKey("T")));
			this.controlList.add(new GuiButton(7, this.width - 72, 4, 20, 20, var1.translateKey("Q")));
			this.controlList.add(new GuiAetherButton(0, this.width / 4 - 100, var51 + 72, var1.translateKey("menu.options")));
			this.controlList.add(new GuiAetherButton(4, this.width / 4 - 100, var51 + 96, var1.translateKey("menu.quit")));
			if(this.mc.session == null) {
				this.multiplayerButton.enabled = false;
			}
		} else if(!renderOption) {
			var51 = this.height / 4 + 40;
			int var6 = this.width / 2 + 100;
			this.controlList.clear();
			this.controlList.add(new GuiAetherButton(1, this.width / 2 - 110, var51, var1.translateKey("menu.singleplayer")));
			this.controlList.add(this.multiplayerButton = new GuiAetherButton(2, this.width / 2 - 110, var51 + 24, var1.translateKey("menu.multiplayer")));
			this.controlList.add(new GuiAetherButton(3, this.width / 2 - 110, var51 + 48, var1.translateKey("menu.mods")));
			this.controlList.add(new GuiButton(5, this.width - 24, 4, 20, 20, var1.translateKey("W")));
			this.controlList.add(new GuiButton(6, this.width - 48, 4, 20, 20, var1.translateKey("T")));
			this.controlList.add(new GuiAetherButton(0, this.width / 2 - 110, var51 + 72, 98, 20, var1.translateKey("menu.options")));
			this.controlList.add(new GuiAetherButton(4, this.width / 2 + 2 - 10, var51 + 72, 98, 20, var1.translateKey("menu.quit")));
			if(this.mc.session == null) {
				this.multiplayerButton.enabled = false;
			}
		}

	}

	protected String getSaveName(int i) {
		if(this.saveList.size() < i + 1) {
			return null;
		} else {
			String s = ((SaveFormatComparator)this.saveList.get(i)).getDisplayName();
			if(s == null || MathHelper.stringNullOrLengthZero(s)) {
				StringTranslate stringtranslate = StringTranslate.getInstance();
				s = stringtranslate.translateKey("selectWorld.world") + " " + (i + 1);
			}

			return s;
		}
	}

	private void loadSaves() {
		ISaveFormat isaveformat = this.mc.getSaveLoader();
		this.saveList = isaveformat.func_22176_b();
		Collections.sort(this.saveList);
		this.selectedWorld = -1;
	}

	protected String getSaveFileName(int i) {
		return this.saveList.size() < i + 1 ? null : ((SaveFormatComparator)this.saveList.get(i)).getFileName();
	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}

		if(var1.id == 1) {
			this.mc.displayGuiScreen(new GuiSelectWorldAether(this, musicId));
		}

		if(var1.id == 2) {
			this.mc.displayGuiScreen(new GuiMultiplayerAether(this, musicId));
		}

		if(var1.id == 3) {
			this.mc.displayGuiScreen(new GuiTexturePacks(this));
		}

		if(var1.id == 4) {
			this.mc.shutdown();
		}

		if(var1.id == 6) {
			themeOption = !themeOption;
		}

		if(var1.id == 7) {
			this.mc.displayGuiScreen((GuiScreen)null);
			mmactive = false;

			try {
				SoundSystem var11 = (SoundSystem)ModLoader.getPrivateValue(SoundManager.class, (Object)null, 0);
				var11.stop("sound_" + musicId);
				ModLoader.setPrivateValue(SoundManager.class, this.mc.sndManager, "i", 6000);
			} catch (Exception exception7) {
				if(exception7 instanceof NoSuchFieldException) {
					try {
						ModLoader.setPrivateValue(SoundManager.class, this.mc.sndManager, "ticksBeforeMusic", 6000);
					} catch (Exception exception6) {
						exception6.printStackTrace();
					}
				} else {
					exception7.printStackTrace();
				}
			}

			musicId = -1;
		}

		if(var1.id == 5) {
			StringTranslate var111 = StringTranslate.getInstance();
			if(!renderOption) {
				renderOption = true;
				this.loadSaves();
				String var5 = this.getSaveFileName(0);
				String var3 = this.getSaveName(0);
				if(var3 == null) {
					renderOption = false;
					return;
				}

				this.mc.startWorld(var5, var3, 0L);
				int var51 = this.height / 4 + 20;
				this.controlList.clear();
				this.controlList.add(new GuiAetherButton(1, this.width / 4 - 100, var51, var111.translateKey("menu.singleplayer")));
				this.controlList.add(this.multiplayerButton = new GuiAetherButton(2, this.width / 4 - 100, var51 + 24, var111.translateKey("menu.multiplayer")));
				this.controlList.add(new GuiAetherButton(3, this.width / 4 - 100, var51 + 48, var111.translateKey("menu.mods")));
				this.controlList.add(new GuiButton(5, this.width - 24, 4, 20, 20, var111.translateKey("W")));
				this.controlList.add(new GuiButton(6, this.width - 48, 4, 20, 20, var111.translateKey("T")));
				this.controlList.add(new GuiButton(7, this.width - 72, 4, 20, 20, var111.translateKey("Q")));
				this.controlList.add(new GuiAetherButton(0, this.width / 4 - 100, var51 + 72, var111.translateKey("menu.options")));
				this.controlList.add(new GuiAetherButton(4, this.width / 4 - 100, var51 + 96, var111.translateKey("menu.quit")));
				if(this.mc.session == null) {
					this.multiplayerButton.enabled = false;
				}
			} else {
				renderOption = false;
				this.mc.theWorld = null;
				this.mc.thePlayer = null;
				int var52 = this.height / 4 + 40;
				this.controlList.clear();
				this.controlList.add(new GuiAetherButton(1, this.width / 2 - 110, var52, var111.translateKey("menu.singleplayer")));
				this.controlList.add(this.multiplayerButton = new GuiAetherButton(2, this.width / 2 - 110, var52 + 24, var111.translateKey("menu.multiplayer")));
				this.controlList.add(new GuiAetherButton(3, this.width / 2 - 110, var52 + 48, var111.translateKey("menu.mods")));
				this.controlList.add(new GuiButton(5, this.width - 24, 4, 20, 20, var111.translateKey("W")));
				this.controlList.add(new GuiButton(6, this.width - 48, 4, 20, 20, var111.translateKey("T")));
				this.controlList.add(new GuiAetherButton(0, this.width / 2 - 110, var52 + 72, 98, 20, var111.translateKey("menu.options")));
				this.controlList.add(new GuiAetherButton(4, this.width / 2 + 2 - 10, var52 + 72, 98, 20, var111.translateKey("menu.quit")));
				if(this.mc.session == null) {
					this.multiplayerButton.enabled = false;
				}
			}
		}

	}

	public void drawScreen(int var1, int var2, float var3) {
		Tessellator var4;
		boolean var5;
		byte var7;
		String var9;
		short var51;
		int var61;
		float var81;
		if(themeOption) {
			if(renderOption) {
				var4 = Tessellator.instance;
				var5 = true;
				byte var6 = 15;
				var7 = 15;
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/aether/title/mclogomod1.png"));
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.drawTexturedModalRect(var6 + 0, var7 + 0, 0, 0, 155, 44);
				this.drawTexturedModalRect(var6 + 155, var7 + 0, 0, 45, 155, 44);
				var4.setColorOpaque_I(0xFFFFFF);
				String var8 = "Minecraft Beta 1.7.3";
				this.drawString(this.fontRenderer, var8, this.width - this.fontRenderer.getStringWidth(var8) - 5, this.height - 20, 0xFFFFFF);
				var9 = "Copyright Mojang AB. Do not distribute.";
				this.drawString(this.fontRenderer, var9, this.width - this.fontRenderer.getStringWidth(var9) - 5, this.height - 10, 5263440);
			} else {
				this.drawAetherDefaultBackground();
				var4 = Tessellator.instance;
				var51 = 274;
				var61 = this.width / 2 - var51 / 2;
				var7 = 30;
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/aether/title/mclogomod1.png"));
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				this.drawTexturedModalRect(var61 + 30, var7 + 0, 0, 0, 155, 44);
				this.drawTexturedModalRect(var61 + 185, var7 + 0, 0, 45, 155, 44);
				var4.setColorOpaque_I(0xFFFFFF);
				GL11.glPushMatrix();
				GL11.glTranslatef((float)(this.width / 2 + 90), 70.0F, 0.0F);
				GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
				var81 = 1.8F - MathHelper.abs(MathHelper.sin((float)(System.currentTimeMillis() % 1000L) / 1000.0F * (float)Math.PI * 2.0F) * 0.1F);
				var81 = var81 * 100.0F / (float)(this.fontRenderer.getStringWidth(this.splashText) + 32);
				GL11.glScalef(var81, var81, var81);
				this.drawCenteredString(this.fontRenderer, this.splashText, 0, -8, 16776960);
				GL11.glPopMatrix();
				this.drawString(this.fontRenderer, "Minecraft Beta 1.7.3", 2, 2, 5263440);
				var9 = "Copyright Mojang AB. Do not distribute.";
				this.drawString(this.fontRenderer, var9, this.width - this.fontRenderer.getStringWidth(var9) - 2, this.height - 10, 0xFFFFFF);
			}
		} else if(renderOption) {
			var4 = Tessellator.instance;
			var5 = true;
			String var62 = "Minecraft Beta 1.7.3";
			this.drawString(this.fontRenderer, var62, this.width - this.fontRenderer.getStringWidth(var62) - 5, this.height - 20, 0xFFFFFF);
			String var71 = "Copyright Mojang AB. Do not distribute.";
			this.drawString(this.fontRenderer, var71, this.width - this.fontRenderer.getStringWidth(var71) - 5, this.height - 10, 5263440);
			this.drawMiniLogo();
		} else {
			this.drawDefaultBackground();
			var4 = Tessellator.instance;
			var51 = 274;
			var61 = this.width / 2 - var51 / 2;
			var7 = 30;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/title/mclogo.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(var61 + 0, var7 + 0, 0, 0, 155, 44);
			this.drawTexturedModalRect(var61 + 155, var7 + 0, 0, 45, 155, 44);
			var4.setColorOpaque_I(0xFFFFFF);
			GL11.glPushMatrix();
			GL11.glTranslatef((float)(this.width / 2 + 90), 70.0F, 0.0F);
			GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
			var81 = 1.8F - MathHelper.abs(MathHelper.sin((float)(System.currentTimeMillis() % 1000L) / 1000.0F * (float)Math.PI * 2.0F) * 0.1F);
			var81 = var81 * 100.0F / (float)(this.fontRenderer.getStringWidth(this.splashText) + 32);
			GL11.glScalef(var81, var81, var81);
			this.drawCenteredString(this.fontRenderer, this.splashText, 0, -8, 16776960);
			GL11.glPopMatrix();
			this.drawString(this.fontRenderer, "Minecraft Beta 1.7.3", 2, 2, 5263440);
			var9 = "Copyright Mojang AB. Do not distribute.";
			this.drawString(this.fontRenderer, var9, this.width - this.fontRenderer.getStringWidth(var9) - 2, this.height - 10, 0xFFFFFF);
		}

		this.drawString(this.fontRenderer, this.hoverText, this.width - 72, 28, 0xFFFFFF);
		super.drawScreen(var1, var2, var3);
	}

	protected void mouseMovedOrUp(int i, int j, int k) {
		this.hoverText = "";

		for(int l = 0; l < this.controlList.size(); ++l) {
			GuiButton button = (GuiButton)this.controlList.get(l);
			if(i >= button.xPosition && j >= button.yPosition && i < button.xPosition + button.width && j < button.yPosition + button.height) {
				switch(button.id) {
				case 5:
					this.hoverText = "Toggle World";
					break;
				case 6:
					if(themeOption) {
						this.hoverText = "Normal Theme";
					} else {
						this.hoverText = "Aether Theme";
					}
					break;
				case 7:
					this.hoverText = "Quick Load";
				}
			}
		}

	}

	public void drawMiniLogo() {
		Tessellator var4 = Tessellator.instance;
		byte var6 = 15;
		byte var7 = 15;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/title/mclogo.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPushMatrix();
		GL11.glScalef(0.8F, 0.8F, 0.8F);
		this.drawTexturedModalRect(var6 + 0, var7 + 0, 0, 0, 155, 44);
		this.drawTexturedModalRect(var6 + 155, var7 + 0, 0, 45, 155, 44);
		GL11.glPopMatrix();
		var4.setColorOpaque_I(0xFFFFFF);
	}

	public void drawAetherDefaultBackground() {
		this.drawAetherWorldBackground(0);
	}

	public void drawAetherWorldBackground(int i) {
		this.drawAetherBackground(i);
	}

	public void drawAetherBackground(int i) {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		Tessellator tessellator = Tessellator.instance;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/aether/gui/aetherBG.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f = 32.0F;
		tessellator.startDrawingQuads();
		tessellator.setColorOpaque_I(10066329);
		tessellator.addVertexWithUV(0.0D, (double)this.height, 0.0D, 0.0D, (double)((float)this.height / f + (float)i));
		tessellator.addVertexWithUV((double)this.width, (double)this.height, 0.0D, (double)((float)this.width / f), (double)((float)this.height / f + (float)i));
		tessellator.addVertexWithUV((double)this.width, 0.0D, 0.0D, (double)((float)this.width / f), (double)(0 + i));
		tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, (double)(0 + i));
		tessellator.draw();
	}

	public boolean doesGuiPauseGame() {
		return this.closeTicks >= 30;
	}

	public void onGuiClosed() {
		this.mc.gameSettings.hideGUI = false;
		this.mc.gameSettings.thirdPersonView = false;
	}
}
