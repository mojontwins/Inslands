package net.minecraft.src;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.benimatic.twilightforest.EntityTFHedgeSpider;
import com.benimatic.twilightforest.EntityTFKobold;
import com.benimatic.twilightforest.EntityTFMinoshroom;
import com.benimatic.twilightforest.EntityTFMinotaur;
import com.benimatic.twilightforest.EntityTFNatureBolt;
import com.benimatic.twilightforest.EntityTFRedcap;
import com.benimatic.twilightforest.EntityTFSkeletonDruid;
import com.benimatic.twilightforest.EntityTFSwarmSpider;
import com.benimatic.twilightforest.EntityTFWraith;
import com.benimatic.twilightforest.EntityTwilightBighorn;
import com.benimatic.twilightforest.EntityTwilightBoar;
import com.benimatic.twilightforest.EntityTwilightDeer;
import com.benimatic.twilightforest.ModelBighornFur;
import com.benimatic.twilightforest.ModelTFKobold;
import com.benimatic.twilightforest.ModelTFMinoshroom;
import com.benimatic.twilightforest.ModelTFMinotaur;
import com.benimatic.twilightforest.ModelTFRedcap;
import com.benimatic.twilightforest.ModelTFSkeletonDruid;
import com.benimatic.twilightforest.ModelTwilightBighorn;
import com.benimatic.twilightforest.ModelTwilightBoar;
import com.benimatic.twilightforest.ModelTwilightDeer;
import com.benimatic.twilightforest.RenderTFMinoshroom;
import com.benimatic.twilightforest.RenderTFWraith;
import com.bigbang87.deadlymonsters.EntityHauntedCow;
import com.bigbang87.deadlymonsters.ModelHauntedCow;
import com.bigbang87.deadlymonsters.RenderHauntedCow;
import com.chocolatin.betterdungeons.EntityPirate;
import com.chocolatin.betterdungeons.EntityPirateArcher;
import com.chocolatin.betterdungeons.EntityPirateBoss;
import com.chocolatin.betterdungeons.ModelArcher;
import com.chocolatin.betterdungeons.ModelHuman;
import com.chocolatin.betterdungeons.RenderHuman;
import com.hippoplatimus.pistons.EntityMovingPiston;
import com.hippoplatimus.pistons.MovingPistonRenderer;
import com.misc.aether.EntityFlyingCow;
import com.misc.aether.EntityMimic;
import com.misc.aether.EntityPhyg;
import com.misc.aether.EntitySheepuff;
import com.misc.aether.EntitySlider;
import com.misc.aether.ModelFlyingCow1;
import com.misc.aether.ModelFlyingCow2;
import com.misc.aether.ModelFlyingPig1;
import com.misc.aether.ModelFlyingPig2;
import com.misc.aether.ModelSheepuff1;
import com.misc.aether.ModelSheepuff2;
import com.misc.aether.ModelSheepuff3;
import com.misc.aether.ModelSlider;
import com.misc.aether.RenderFlyingCow;
import com.misc.aether.RenderMimic;
import com.misc.aether.RenderPhyg;
import com.misc.aether.RenderSheepuff;
import com.misc.aether.RenderSlider;
import com.mojang.minecraft.ocelot.EntityBetaOcelot;
import com.mojang.minecraft.ocelot.EntityCatBlack;
import com.mojang.minecraft.ocelot.EntityCatRed;
import com.mojang.minecraft.ocelot.EntityCatSiamese;
import com.mojang.minecraft.ocelot.ModelOcelot;
import com.mojang.minecraft.ocelot.RenderOcelot;
import com.mojang.minecraft.witch.EntityAlphaWitch;
import com.mojang.minecraft.witch.RenderWitch;
import com.mojontwins.minecraft.amazonvillage.EntityAmazon;
import com.mojontwins.minecraft.amazonvillage.RenderAmazon;
import com.mojontwins.minecraft.icepalace.EntityIceArcher;
import com.mojontwins.minecraft.icepalace.EntityIceBall;
import com.mojontwins.minecraft.icepalace.EntityIceBoss;
import com.mojontwins.minecraft.icepalace.EntityIceWarrior;
import com.mojontwins.minecraft.icepalace.ModelIceBoss;
import com.mojontwins.minecraft.icepalace.RenderIceBall;
import com.mojontwins.minecraft.icepalace.RenderIceBoss;
import com.mojontwins.minecraft.monsters.EntityFungalCalamity;
import com.mojontwins.minecraft.monsters.EntityThrowableToxicFungus;
import com.mojontwins.minecraft.oceanruins.EntityTriton;
import com.mojontwins.minecraft.oceanruins.RenderTriton;

public class RenderManager {
	private Map<Class<?>, Render> entityRenderMap = new HashMap<Class<?>, Render>();
	public static RenderManager instance = new RenderManager();
	private FontRenderer fontRenderer;
	public static double renderPosX;
	public static double renderPosY;
	public static double renderPosZ;
	public RenderEngine renderEngine;
	public ItemRenderer itemRenderer;
	public World worldObj;
	public EntityLiving livingPlayer;
	public float playerViewY;
	public float playerViewX;
	public GameSettings options;
	public double field_1222_l;
	public double field_1221_m;
	public double field_1220_n;

	private RenderManager() {
		this.entityRenderMap.put(EntitySpider.class, new RenderSpider());
		this.entityRenderMap.put(EntityPig.class, new RenderPig(new ModelPig(), new ModelPig(0.5F), 0.7F));
		this.entityRenderMap.put(EntitySheep.class, new RenderSheep(new ModelSheep2(), new ModelSheep1(), 0.7F));
		this.entityRenderMap.put(EntityCow.class, new RenderCow(new ModelCow(), 0.7F));
		this.entityRenderMap.put(EntityWolf.class, new RenderWolf(new ModelWolf(), 0.5F));
		this.entityRenderMap.put(EntityChicken.class, new RenderChicken(new ModelChicken(), 0.3F));
		this.entityRenderMap.put(EntityCreeper.class, new RenderCreeper());
		this.entityRenderMap.put(EntitySkeleton.class, new RenderBiped(new ModelSkeleton(), 0.5F));
		this.entityRenderMap.put(EntityZombie.class, new RenderBiped(new ModelZombie(), 0.5F));
		this.entityRenderMap.put(EntitySlime.class, new RenderSlime(new ModelSlime(16), new ModelSlime(0), 0.25F));
		this.entityRenderMap.put(EntityPlayer.class, new RenderPlayer());
		this.entityRenderMap.put(EntityGiantZombie.class, new RenderGiantZombie(new ModelZombie(), 0.5F, 6.0F));
		this.entityRenderMap.put(EntityGhast.class, new RenderGhast());
		this.entityRenderMap.put(EntitySquid.class, new RenderSquid(new ModelSquid(), 0.7F));
		this.entityRenderMap.put(EntityLiving.class, new RenderLiving(new ModelBiped(), 0.5F));
		this.entityRenderMap.put(Entity.class, new RenderEntity());
		this.entityRenderMap.put(EntityPainting.class, new RenderPainting());
		this.entityRenderMap.put(EntityArrow.class, new RenderArrow());
		this.entityRenderMap.put(EntitySnowball.class, new RenderSnowball(Item.snowball.getIconFromDamage(0)));
		this.entityRenderMap.put(EntityEgg.class, new RenderSnowball(Item.egg.getIconFromDamage(0)));
		this.entityRenderMap.put(EntityFireball.class, new RenderFireball());
		this.entityRenderMap.put(EntityIceBall.class, new RenderIceBall());
		this.entityRenderMap.put(EntityItem.class, new RenderItem());
		this.entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed());
		this.entityRenderMap.put(EntityFallingSand.class, new RenderFallingSand());
		this.entityRenderMap.put(EntityMinecart.class, new RenderMinecart());
		this.entityRenderMap.put(EntityBoat.class, new RenderBoat());
		this.entityRenderMap.put(EntityFish.class, new RenderFish());
		this.entityRenderMap.put(EntityLightningBolt.class, new RenderLightningBolt());

		// Release vanilla
		this.entityRenderMap.put(EntityMooshroom.class, new RenderMooshroom(new ModelCow(), 0.7F));

		// Mine
		this.entityRenderMap.put(EntityPebble.class, new RenderSnowball(Item.pebble.getIconFromDamage(0)));
		this.entityRenderMap.put(EntityThrowablePotion.class, new RenderThrowablePotion());
		this.entityRenderMap.put(EntityIceSkeleton.class, new RenderBiped(new ModelSkeleton(), 0.5F));
		this.entityRenderMap.put(EntityZombieAlex.class, new RenderZombie(new ModelZombie(), 0.5F, "zombie_alex"));
		this.entityRenderMap.put(EntityDrowned.class, new RenderZombie(new ModelZombie(), 0.5F, "drowned"));
		this.entityRenderMap.put(EntityHusk.class, new RenderZombie(new ModelZombie(), 0.5F, "husk"));
		this.entityRenderMap.put(EntityCityHusk.class, new RenderZombie(new ModelZombie(), 0.5F, "zombie"));
		this.entityRenderMap.put(EntityToxicZombie.class, new RenderZombie(new ModelZombie(), 0.5F, "zombie"));
		this.entityRenderMap.put(EntityBuilderZombie.class, new RenderZombie(new ModelZombie(), 0.5F, "zombie"));
		this.entityRenderMap.put(EntityExplodingZombie.class, new RenderExplodingZombie(new ModelZombie(), 0.5F));
		this.entityRenderMap.put(EntityAlphaWitch.class, new RenderWitch());
		this.entityRenderMap.put(EntityBetaOcelot.class, new RenderOcelot(new ModelOcelot(), 0.5F));
		this.entityRenderMap.put(EntityCatBlack.class, new RenderOcelot(new ModelOcelot(), 0.5F));
		this.entityRenderMap.put(EntityCatRed.class, new RenderOcelot(new ModelOcelot(), 0.5F));
		this.entityRenderMap.put(EntityCatSiamese.class, new RenderOcelot(new ModelOcelot(), 0.5F));
		this.entityRenderMap.put(EntityAmazon.class, new RenderAmazon());
		
		// Twilight Forest
		this.entityRenderMap.put(EntityTFRedcap.class, new RenderBiped(new ModelTFRedcap(), 0.625F));
		this.entityRenderMap.put(EntityTFWraith.class, new RenderTFWraith(new ModelZombie(), 0.5F));
		this.entityRenderMap.put(EntityTFMinotaur.class, new RenderBiped(new ModelTFMinotaur(), 0.625F));
		this.entityRenderMap.put(EntityTFMinoshroom.class, new RenderTFMinoshroom(new ModelTFMinoshroom(), 0.625F));
		this.entityRenderMap.put(EntityTFKobold.class, new RenderBiped(new ModelTFKobold(), 0.625F));
		this.entityRenderMap.put(EntityTFSwarmSpider.class, new RenderSpider());
		this.entityRenderMap.put(EntityTwilightBoar.class, new RenderPig(new ModelTwilightBoar(), new ModelPig(0.5F), 0.7F));
		this.entityRenderMap.put(EntityTwilightBighorn.class, new RenderSheep(new ModelTwilightBighorn(), new ModelBighornFur(), 0.7F));
		this.entityRenderMap.put(EntityTwilightDeer.class, new RenderCow(new ModelTwilightDeer(), 0.7F));
		this.entityRenderMap.put(EntityTFSkeletonDruid.class, new RenderBiped(new ModelTFSkeletonDruid(), 0.5F));
		this.entityRenderMap.put(EntityTFNatureBolt.class, new RenderSnowball(Item.slimeBall.getIconFromDamage(0)));
		this.entityRenderMap.put(EntityTFHedgeSpider.class, new RenderSpider());
		
		// Better Dungeons
		this.entityRenderMap.put(EntityPirate.class, new RenderHuman(new ModelHuman(), 0.5F));
		this.entityRenderMap.put(EntityPirateArcher.class, new RenderBiped(new ModelArcher(), 0.5F));
		this.entityRenderMap.put(EntityPirateBoss.class, new RenderBiped(new ModelArcher(), 0.5F));
		
		// Classic pistons
		this.entityRenderMap.put(EntityMovingPiston.class, new MovingPistonRenderer());
		
		// More stuff
		this.entityRenderMap.put(EntityTriton.class, new RenderTriton());
		this.entityRenderMap.put(EntityHauntedCow.class, new RenderHauntedCow(new ModelHauntedCow(), 0.7F));
		
		// Ice palace
		this.entityRenderMap.put(EntityIceWarrior.class,  new RenderHuman(new ModelHuman(), 0.5F));
		this.entityRenderMap.put(EntityIceArcher.class,  new RenderBiped(new ModelArcher(), 0.5F));
		this.entityRenderMap.put(EntityIceBoss.class,  new RenderIceBoss(new ModelIceBoss(), 0.5F));
		
		// Aether
		this.entityRenderMap.put(EntityMimic.class,  new RenderMimic());
		this.entityRenderMap.put(EntitySlider.class, new RenderSlider(new ModelSlider(), 0.5F));
		this.entityRenderMap.put(EntityPhyg.class, new RenderPhyg(new ModelFlyingPig1(), new ModelFlyingPig2(), 0.7F));
		this.entityRenderMap.put(EntityFlyingCow.class, new RenderFlyingCow(new ModelFlyingCow1(), new ModelFlyingCow2(), 0.7F));
		this.entityRenderMap.put(EntitySheepuff.class, new RenderSheepuff(new ModelSheepuff1(), new ModelSheepuff2(), new ModelSheepuff3(), 0.7F));
		
		this.entityRenderMap.put(EntityFungalCalamity.class, new RenderTwoLayeredBiped());
		this.entityRenderMap.put(EntityThrowableToxicFungus.class, new RenderSnowball(Item.fungaInfection.getIconFromDamage(0)));
		
		// Traders
		/*
		this.entityRenderMap.put(EntityPigman.class, new RenderBiped(new ModelBiped(), 0.5F));
		this.entityRenderMap.put(EntityCowman.class, new RenderBiped(new ModelBiped(), 0.5F));
		*/
		
		Iterator<Render> iterator1 = this.entityRenderMap.values().iterator();

		while(iterator1.hasNext()) {
			Render render2 = (Render)iterator1.next();
			render2.setRenderManager(this);
		}

	}

	public Render getEntityClassRenderObject(Class<?> class1) {
		Render render2 = (Render)this.entityRenderMap.get(class1);
		if(render2 == null && class1 != Entity.class) {
			render2 = this.getEntityClassRenderObject(class1.getSuperclass());
			this.entityRenderMap.put(class1, render2);
		}

		return render2;
	}

	public Render getEntityRenderObject(Entity entity1) {
		return this.getEntityClassRenderObject(entity1.getClass());
	}

	public void cacheActiveRenderInfo(World world1, RenderEngine renderEngine2, FontRenderer fontRenderer3, EntityLiving entityLiving4, GameSettings gameSettings5, float f6) {
		this.worldObj = world1;
		this.renderEngine = renderEngine2;
		this.options = gameSettings5;
		this.livingPlayer = entityLiving4;
		this.fontRenderer = fontRenderer3;
		if(entityLiving4.isPlayerSleeping()) {
			int i7 = world1.getBlockId(MathHelper.floor_double(entityLiving4.posX), MathHelper.floor_double(entityLiving4.posY), MathHelper.floor_double(entityLiving4.posZ));
			if(i7 == Block.blockBed.blockID) {
				int i8 = world1.getBlockMetadata(MathHelper.floor_double(entityLiving4.posX), MathHelper.floor_double(entityLiving4.posY), MathHelper.floor_double(entityLiving4.posZ));
				int i9 = i8 & 3;
				this.playerViewY = (float)(i9 * 90 + 180);
				this.playerViewX = 0.0F;
			}
		} else {
			this.playerViewY = entityLiving4.prevRotationYaw + (entityLiving4.rotationYaw - entityLiving4.prevRotationYaw) * f6;
			this.playerViewX = entityLiving4.prevRotationPitch + (entityLiving4.rotationPitch - entityLiving4.prevRotationPitch) * f6;
		}

		this.field_1222_l = entityLiving4.lastTickPosX + (entityLiving4.posX - entityLiving4.lastTickPosX) * (double)f6;
		this.field_1221_m = entityLiving4.lastTickPosY + (entityLiving4.posY - entityLiving4.lastTickPosY) * (double)f6;
		this.field_1220_n = entityLiving4.lastTickPosZ + (entityLiving4.posZ - entityLiving4.lastTickPosZ) * (double)f6;
	}

	public void renderEntity(Entity entity1, float f2) {
		double d3 = entity1.lastTickPosX + (entity1.posX - entity1.lastTickPosX) * (double)f2;
		double d5 = entity1.lastTickPosY + (entity1.posY - entity1.lastTickPosY) * (double)f2;
		double d7 = entity1.lastTickPosZ + (entity1.posZ - entity1.lastTickPosZ) * (double)f2;
		float f9 = entity1.prevRotationYaw + (entity1.rotationYaw - entity1.prevRotationYaw) * f2;
		int i10 = entity1.getBrightnessForRender(f2);
		if(entity1.isBurning()) {
			i10 = 15728880;
		}

		int i11 = i10 % 65536;
		int i12 = i10 / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)i11 / 1.0F, (float)i12 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.renderEntityWithPosYaw(entity1, d3 - renderPosX, d5 - renderPosY, d7 - renderPosZ, f9, f2);
	}

	public void renderEntityWithPosYaw(Entity entity1, double d2, double d4, double d6, float f8, float f9) {
		Render render10 = this.getEntityRenderObject(entity1);
		if(render10 != null) {
			render10.doRender(entity1, d2, d4, d6, f8, f9);
			render10.doRenderShadowAndFire(entity1, d2, d4, d6, f8, f9);
		}

	}

	public void set(World world1) {
		this.worldObj = world1;
	}

	public double getDistanceToCamera(double d1, double d3, double d5) {
		double d7 = d1 - this.field_1222_l;
		double d9 = d3 - this.field_1221_m;
		double d11 = d5 - this.field_1220_n;
		return d7 * d7 + d9 * d9 + d11 * d11;
	}

	public FontRenderer getFontRenderer() {
		return this.fontRenderer;
	}
}
