package net.minecraft.src;

import java.util.Map;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class AetherPoison {
	public static long clock;
	public static final float poisonRed = 1.0F;
	public static final float poisonBlue = 1.0F;
	public static final float poisonGreen = 0.0F;
	public static final float cureRed = 0.0F;
	public static final float cureBlue = 0.1F;
	public static final float cureGreen = 1.0F;
	public static int poisonTime;
	public static final int poisonInterval = 50;
	public static final int poisonDmg = 1;
	public static final int poisonHurts = 10;
	public static final int maxPoisonTime = 500;
	public static World world;
	public static Minecraft mc = ModLoader.getMinecraftInstance();
	public static double rotDFac = 0.7853981633974483D;
	public static double rotD;
	public static double rotTaper = 0.125D;
	public static double motTaper = 0.2D;
	public static double motD;
	public static double motDFac = 0.1D;

	public AetherPoison() {
		ModLoader.RegisterEntityID(EntityPoisonNeedle.class, "PoisonNeedle", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(EntityDartPoison.class, "PoisonDart", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(EntityDartGolden.class, "GoldenDart", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(EntityDartEnchanted.class, "EnchantedDart", ModLoader.getUniqueEntityId());
	}

	public static boolean canPoison(Entity entity) {
		return !(entity instanceof EntitySlider) && !(entity instanceof EntitySentry) && !(entity instanceof EntityMiniCloud) && !(entity instanceof EntityFireMonster) && !(entity instanceof EntityAechorPlant) && !(entity instanceof EntityFiroBall) && !(entity instanceof EntityCockatrice) && !(entity instanceof EntityHomeShot);
	}

	public static void distractEntity(Entity ent) {
		double gauss = mc.theWorld.rand.nextGaussian();
		double newMotD = motDFac * gauss;
		motD = motTaper * newMotD + (1.0D - motTaper) * motD;
		ent.motionX += motD;
		ent.motionZ += motD;
		double newRotD = rotDFac * gauss;
		rotD = rotTaper * newRotD + (1.0D - rotTaper) * rotD;
		ent.rotationYaw = (float)((double)ent.rotationYaw + rotD);
		ent.rotationPitch = (float)((double)ent.rotationPitch + rotD);
	}

	public static void tickRender(Minecraft game) {
		if(world == game.theWorld && (game.thePlayer == null || !game.thePlayer.isDead && game.thePlayer.health > 0)) {
			if(world != null) {
				if(poisonTime < 0) {
					++poisonTime;
					displayCureEffect();
				} else if(poisonTime != 0) {
					long time = mc.theWorld.getWorldTime();
					int mod = poisonTime % 50;
					if(clock != time) {
						distractEntity(game.thePlayer);
						if(mod == 0) {
							game.thePlayer.attackEntityFrom((Entity)null, 1);
						}

						--poisonTime;
						clock = time;
					}

					displayPoisonEffect(mod);
				}
			}
		} else {
			world = game.theWorld;
			poisonTime = 0;
		}
	}

	public static boolean afflictPoison() {
		if(poisonTime < 0) {
			return false;
		} else {
			poisonTime = 500;
			world = mc.theWorld;
			return true;
		}
	}

	public static boolean curePoison() {
		if(poisonTime == -500) {
			return false;
		} else {
			poisonTime = -500;
			world = mc.theWorld;
			return true;
		}
	}

	public static float getPoisonAlpha(float f) {
		return f * f / 5.0F + 0.4F;
	}

	public static float getCureAlpha(float f) {
		return f * f / 10.0F + 0.4F;
	}

	public static void displayCureEffect() {
		if(mc.currentScreen == null) {
			flashColor("%blur%/aether/poison/curevignette.png", getCureAlpha(-((float)poisonTime) / 500.0F));
		}
	}

	public static void displayPoisonEffect(int mod) {
		if(mc.currentScreen == null) {
			flashColor("%blur%/aether/poison/poisonvignette.png", getPoisonAlpha((float)mod / 50.0F));
		}
	}

	public static void flashColor(String file, float a) {
		ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		int width = scaledresolution.getScaledWidth();
		int height = scaledresolution.getScaledHeight();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, a);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(file));
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0.0D, (double)height, -90.0D, 0.0D, 1.0D);
		tessellator.addVertexWithUV((double)width, (double)height, -90.0D, 1.0D, 1.0D);
		tessellator.addVertexWithUV((double)width, 0.0D, -90.0D, 1.0D, 0.0D);
		tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, a);
	}

	public static void AddRenderer(Map map) {
		map.put(EntityPoisonNeedle.class, new RenderPoisonNeedle());
		map.put(EntityDartPoison.class, new RenderDartPoison());
		map.put(EntityDartGolden.class, new RenderDartGolden());
		map.put(EntityDartEnchanted.class, new RenderDartEnchanted());
	}
}
