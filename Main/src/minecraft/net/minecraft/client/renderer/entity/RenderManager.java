package net.minecraft.client.renderer.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GameSettings;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelPig;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.model.aether.ModelFlyingCow1;
import net.minecraft.client.model.aether.ModelFlyingCow2;
import net.minecraft.client.model.aether.ModelFlyingPig1;
import net.minecraft.client.model.aether.ModelFlyingPig2;
import net.minecraft.client.model.aether.ModelSheepuff1;
import net.minecraft.client.model.aether.ModelSheepuff2;
import net.minecraft.client.model.aether.ModelSheepuff3;
import net.minecraft.client.model.aether.ModelSlider;
import net.minecraft.client.model.betterdungeons.ModelArcher;
import net.minecraft.client.model.betterdungeons.ModelHuman;
import net.minecraft.client.model.deadlymonsters.ModelHauntedCow;
import net.minecraft.client.model.gwdm.ModelGhoul;
import net.minecraft.client.model.mojontwins.ModelIceBoss;
import net.minecraft.client.model.twilight.ModelBighornFur;
import net.minecraft.client.model.twilight.ModelTFKobold;
import net.minecraft.client.model.twilight.ModelTFMinoshroom;
import net.minecraft.client.model.twilight.ModelTFMinotaur;
import net.minecraft.client.model.twilight.ModelTFRedcap;
import net.minecraft.client.model.twilight.ModelTFSkeletonDruid;
import net.minecraft.client.model.twilight.ModelTwilightBighorn;
import net.minecraft.client.model.twilight.ModelTwilightBoar;
import net.minecraft.client.model.twilight.ModelTwilightDeer;
import net.minecraft.client.renderer.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.aether.RenderFlyingCow;
import net.minecraft.client.renderer.entity.aether.RenderMimic;
import net.minecraft.client.renderer.entity.aether.RenderPhyg;
import net.minecraft.client.renderer.entity.aether.RenderSheepuff;
import net.minecraft.client.renderer.entity.aether.RenderSlider;
import net.minecraft.client.renderer.entity.betterdungeons.RenderHuman;
import net.minecraft.client.renderer.entity.deadlymonsters.RenderHauntedCow;
import net.minecraft.client.renderer.entity.gwdm.RenderGhoul;
import net.minecraft.client.renderer.entity.mojontwins.RenderAmazon;
import net.minecraft.client.renderer.entity.mojontwins.RenderIceBall;
import net.minecraft.client.renderer.entity.mojontwins.RenderIceBoss;
import net.minecraft.client.renderer.entity.mojontwins.RenderThrowableBottle;
import net.minecraft.client.renderer.entity.mojontwins.RenderTriton;
import net.minecraft.client.renderer.entity.twilight.RenderTFMinoshroom;
import net.minecraft.client.renderer.entity.twilight.RenderTFWraith;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDerp;
import net.minecraft.world.entity.EntityLightningBolt;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityPainting;
import net.minecraft.world.entity.animal.EntityBetaOcelot;
import net.minecraft.world.entity.animal.EntityCatBlack;
import net.minecraft.world.entity.animal.EntityCatRed;
import net.minecraft.world.entity.animal.EntityCatSiamese;
import net.minecraft.world.entity.animal.EntityChicken;
import net.minecraft.world.entity.animal.EntityCow;
import net.minecraft.world.entity.animal.EntityFlyingCow;
import net.minecraft.world.entity.animal.EntityHauntedCow;
import net.minecraft.world.entity.animal.EntityMooshroom;
import net.minecraft.world.entity.animal.EntityPhyg;
import net.minecraft.world.entity.animal.EntityPig;
import net.minecraft.world.entity.animal.EntitySheep;
import net.minecraft.world.entity.animal.EntitySheepuff;
import net.minecraft.world.entity.animal.EntitySquid;
import net.minecraft.world.entity.animal.EntityTwilightBighorn;
import net.minecraft.world.entity.animal.EntityTwilightBoar;
import net.minecraft.world.entity.animal.EntityTwilightDeer;
import net.minecraft.world.entity.animal.EntityWolf;
import net.minecraft.world.entity.item.EntityBoat;
import net.minecraft.world.entity.item.EntityFallingSand;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.item.EntityMinecart;
import net.minecraft.world.entity.item.EntityMovingPiston;
import net.minecraft.world.entity.item.EntityTNTPrimed;
import net.minecraft.world.entity.monster.EntityAlphaWitch;
import net.minecraft.world.entity.monster.EntityBuilderZombie;
import net.minecraft.world.entity.monster.EntityCityHusk;
import net.minecraft.world.entity.monster.EntityCreeper;
import net.minecraft.world.entity.monster.EntityDrowned;
import net.minecraft.world.entity.monster.EntityExplodingZombie;
import net.minecraft.world.entity.monster.EntityFungalCalamity;
import net.minecraft.world.entity.monster.EntityGhast;
import net.minecraft.world.entity.monster.EntityGhoul;
import net.minecraft.world.entity.monster.EntityGiantZombie;
import net.minecraft.world.entity.monster.EntityHusk;
import net.minecraft.world.entity.monster.EntityIceSkeleton;
import net.minecraft.world.entity.monster.EntityMimic;
import net.minecraft.world.entity.monster.EntityPirate;
import net.minecraft.world.entity.monster.EntityPirateArcher;
import net.minecraft.world.entity.monster.EntityPirateBoss;
import net.minecraft.world.entity.monster.EntitySkeleton;
import net.minecraft.world.entity.monster.EntitySlider;
import net.minecraft.world.entity.monster.EntitySlime;
import net.minecraft.world.entity.monster.EntitySnowball;
import net.minecraft.world.entity.monster.EntitySpider;
import net.minecraft.world.entity.monster.EntityTFHedgeSpider;
import net.minecraft.world.entity.monster.EntityTFKobold;
import net.minecraft.world.entity.monster.EntityTFMinoshroom;
import net.minecraft.world.entity.monster.EntityTFMinotaur;
import net.minecraft.world.entity.monster.EntityTFRedcap;
import net.minecraft.world.entity.monster.EntityTFSkeletonDruid;
import net.minecraft.world.entity.monster.EntityTFSwarmSpider;
import net.minecraft.world.entity.monster.EntityTFWraith;
import net.minecraft.world.entity.monster.EntityToxicZombie;
import net.minecraft.world.entity.monster.EntityTriton;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.entity.monster.EntityZombieAlex;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.entity.projectile.EntityEgg;
import net.minecraft.world.entity.projectile.EntityFireball;
import net.minecraft.world.entity.projectile.EntityFish;
import net.minecraft.world.entity.projectile.EntityPebble;
import net.minecraft.world.entity.projectile.EntityTFNatureBolt;
import net.minecraft.world.entity.projectile.EntityThrowableBottle;
import net.minecraft.world.entity.projectile.EntityThrowablePotion;
import net.minecraft.world.entity.projectile.EntityThrowableToxicFungus;
import net.minecraft.world.entity.sentient.EntityAmazon;
import net.minecraft.world.entity.sentient.EntityIceArcher;
import net.minecraft.world.entity.sentient.EntityIceBall;
import net.minecraft.world.entity.sentient.EntityIceBoss;
import net.minecraft.world.entity.sentient.EntityIceWarrior;
import net.minecraft.world.entity.sentient.EntityPoisonWitch;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;

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
		
		// Hell
		this.entityRenderMap.put(EntityGhoul.class, new RenderGhoul(new ModelGhoul(), 0.5F));
		
		// Traders 
		/*
		this.entityRenderMap.put(EntityPigman.class, new RenderBiped(new ModelBiped(), 0.5F));
		this.entityRenderMap.put(EntityCowman.class, new RenderBiped(new ModelBiped(), 0.5F));
		*/
		
		// Poison
		this.entityRenderMap.put(EntityThrowableBottle.class, new RenderThrowableBottle());
		this.entityRenderMap.put(EntityPoisonWitch.class, new RenderTwoLayeredBiped());
		
		this.entityRenderMap.put(EntityDerp.class, new RenderBiped(new ModelBiped(), 0.5F));
		
		// Set this as the render manager to all Render objects
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
