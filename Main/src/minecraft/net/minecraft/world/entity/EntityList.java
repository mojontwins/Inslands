package net.minecraft.world.entity;

import com.mojang.nbt.NBTTagCompound;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.entity.animal.farm.EntityChicken;
import net.minecraft.world.entity.animal.farm.EntityChickenBlack;
import net.minecraft.world.entity.animal.farm.EntityColdCow;
import net.minecraft.world.entity.animal.farm.EntityCow;
import net.minecraft.world.entity.animal.farm.EntityMooshroom;
import net.minecraft.world.entity.animal.farm.EntityPig;
import net.minecraft.world.entity.animal.farm.EntitySheep;
import net.minecraft.world.entity.animal.farm.EntityTwilightBighorn;
import net.minecraft.world.entity.animal.farm.EntityTwilightBoar;
import net.minecraft.world.entity.animal.farm.EntityTwilightDeer;
import net.minecraft.world.entity.animal.moc.EntityGoat;
import net.minecraft.world.entity.animal.water.EntitySquid;
import net.minecraft.world.entity.animal.wild.EntityBetaOcelot;
import net.minecraft.world.entity.animal.wild.EntityCatBlack;
import net.minecraft.world.entity.animal.wild.EntityCatRed;
import net.minecraft.world.entity.animal.wild.EntityCatSiamese;
import net.minecraft.world.entity.animal.wild.EntityFlyingCow;
import net.minecraft.world.entity.animal.wild.EntityPhyg;
import net.minecraft.world.entity.animal.wild.EntitySheepuff;
import net.minecraft.world.entity.animal.wild.EntityTFHostileWolf;
import net.minecraft.world.entity.animal.wild.EntityWolf;
import net.minecraft.world.entity.block.EntityMeatBlock;
import net.minecraft.world.entity.item.EntityBoat;
import net.minecraft.world.entity.item.EntityFallingSand;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.item.EntityMinecart;
import net.minecraft.world.entity.item.EntityMovingPiston;
import net.minecraft.world.entity.item.EntityTNTPrimed;
import net.minecraft.world.entity.mob.boss.EntityAlphaWitch;
import net.minecraft.world.entity.mob.boss.EntityIceBoss;
import net.minecraft.world.entity.mob.boss.EntityPirateBoss;
import net.minecraft.world.entity.mob.boss.EntitySecretBoss;
import net.minecraft.world.entity.mob.boss.EntitySlider;
import net.minecraft.world.entity.mob.creeper.EntityCreeper;
import net.minecraft.world.entity.mob.creeper.EntityElementalCreeper;
import net.minecraft.world.entity.mob.dungeon.EntityMimic;
import net.minecraft.world.entity.mob.EntityMob;
import net.minecraft.world.entity.mob.flying.EntityGhast;
import net.minecraft.world.entity.mob.flying.EntityTFWraith;
import net.minecraft.world.entity.mob.humanoid.EntityAmazon;
import net.minecraft.world.entity.mob.humanoid.EntityIceArcher;
import net.minecraft.world.entity.mob.humanoid.EntityIceWarrior;
import net.minecraft.world.entity.mob.humanoid.EntityPirate;
import net.minecraft.world.entity.mob.humanoid.EntityPirateArcher;
import net.minecraft.world.entity.mob.humanoid.EntityPoisonWitch;
import net.minecraft.world.entity.mob.slime.EntitySlime;
import net.minecraft.world.entity.mob.spider.EntitySpider;
import net.minecraft.world.entity.mob.spider.EntityTFHedgeSpider;
import net.minecraft.world.entity.mob.spider.EntityTFSwarmSpider;
import net.minecraft.world.entity.mob.twilight.EntityTFKobold;
import net.minecraft.world.entity.mob.twilight.EntityTFMinoshroom;
import net.minecraft.world.entity.mob.twilight.EntityTFMinotaur;
import net.minecraft.world.entity.mob.twilight.EntityTFRedcap;
import net.minecraft.world.entity.mob.undead.EntityBuilderZombie;
import net.minecraft.world.entity.mob.undead.EntityCityHusk;
import net.minecraft.world.entity.mob.undead.EntityDiamondSkeleton;
import net.minecraft.world.entity.mob.undead.EntityDrowned;
import net.minecraft.world.entity.mob.undead.EntityExplodingZombie;
import net.minecraft.world.entity.mob.undead.EntityFungalCalamity;
import net.minecraft.world.entity.mob.undead.EntityGhoul;
import net.minecraft.world.entity.mob.undead.EntityGiantZombie;
import net.minecraft.world.entity.mob.undead.EntityHauntedCow;
import net.minecraft.world.entity.mob.undead.EntityHusk;
import net.minecraft.world.entity.mob.undead.EntityIceSkeleton;
import net.minecraft.world.entity.mob.undead.EntityPigZombie;
import net.minecraft.world.entity.mob.undead.EntityPigZombieVolcanoes;
import net.minecraft.world.entity.mob.undead.EntityPoisonSkeleton;
import net.minecraft.world.entity.mob.undead.EntitySkeleton;
import net.minecraft.world.entity.mob.undead.EntityTFSkeletonDruid;
import net.minecraft.world.entity.mob.undead.EntityToxicZombie;
import net.minecraft.world.entity.mob.undead.EntityZombie;
import net.minecraft.world.entity.mob.undead.EntityZombieAlex;
import net.minecraft.world.entity.mob.water.EntityTriton;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.entity.projectile.EntityPebble;
import net.minecraft.world.entity.projectile.EntitySnowball;
import net.minecraft.world.entity.projectile.EntityThrowablePotion;
import net.minecraft.world.level.World;

public class EntityList {
	private static Map<String,Class<?>> stringToClassMapping = new HashMap<String,Class<?>>();
	private static Map<Class<?>,String> classToStringMapping = new HashMap<Class<?>,String>();
	private static Map<Integer,Class<?>> IDtoClassMapping = new HashMap<Integer,Class<?>>();
	private static Map<Class<?>,Integer> classToIDMapping = new HashMap<Class<?>,Integer>();

	private static void addMapping(Class<?> class0, String string1, int i2) {
		stringToClassMapping.put(string1, class0);
		classToStringMapping.put(class0, string1);
		IDtoClassMapping.put(i2, class0);
		classToIDMapping.put(class0, i2);
	}

	public static Entity createEntityByName(String string0, World world1) {
		Entity entity2 = null;

		try {
			Class<?> class3 = (Class<?>)stringToClassMapping.get(string0);
			if(class3 != null) {
				entity2 = (Entity)class3.getConstructor(new Class[]{World.class}).newInstance(new Object[]{world1});
			}
		} catch (Exception exception4) {
			exception4.printStackTrace();
		}

		return entity2;
	}

	public static Entity createEntityFromNBT(NBTTagCompound nBTTagCompound0, World world1) {
		Entity entity2 = null;

		try {
			Class<?> class3 = (Class<?>)stringToClassMapping.get(nBTTagCompound0.getString("id"));
			if(class3 != null) {
				entity2 = (Entity)class3.getConstructor(new Class[]{World.class}).newInstance(new Object[]{world1});
			}
		} catch (Exception exception4) {
			exception4.printStackTrace();
		}

		if(entity2 != null) {
			entity2.readFromNBT(nBTTagCompound0);
		} else {
			System.out.println("Skipping Entity with id " + nBTTagCompound0.getString("id"));
		}

		return entity2;
	}

	public static Entity createEntityByID(int i0, World world1) {
		Entity entity2 = null;

		try {
			Class<?> class3 = (Class<?>)IDtoClassMapping.get(i0);
			if(class3 != null) {
				entity2 = (Entity)class3.getConstructor(new Class[]{World.class}).newInstance(new Object[]{world1});
			}
		} catch (Exception exception4) {
			exception4.printStackTrace();
		}

		if(entity2 == null) {
			System.out.println("Skipping Entity with id " + i0);
		}

		return entity2;
	}

	public static int getEntityID(Entity entity0) {
		return ((Integer)classToIDMapping.get(entity0.getClass())).intValue();
	}

	public static String getEntityString(Entity entity0) {
		return (String)classToStringMapping.get(entity0.getClass());
	}

	static {
		addMapping(EntityArrow.class, "Arrow", 10);
		addMapping(EntitySnowball.class, "Snowball", 11);
		addMapping(EntityItem.class, "Item", 1);
		addMapping(EntityPainting.class, "Painting", 9);
		addMapping(EntityLiving.class, "Mob", 48);
		addMapping(EntityMob.class, "Monster", 49);
		addMapping(EntityCreeper.class, "Creeper", 50);
		addMapping(EntitySkeleton.class, "Skeleton", 51);
		addMapping(EntitySpider.class, "Spider", 52);
		addMapping(EntityGiantZombie.class, "Giant", 53);
		addMapping(EntityZombie.class, "Zombie", 54);
		addMapping(EntitySlime.class, "Slime", 55);
		addMapping(EntityGhast.class, "Ghast", 56);
		addMapping(EntityPigZombie.class, "PigZombie", 57);
		addMapping(EntityPig.class, "Pig", 90);
		addMapping(EntitySheep.class, "Sheep", 91);
		addMapping(EntityCow.class, "Cow", 92);
		addMapping(EntityChicken.class, "Chicken", 93);
		addMapping(EntitySquid.class, "Squid", 94);
		addMapping(EntityWolf.class, "Wolf", 95);
		addMapping(EntityTNTPrimed.class, "PrimedTnt", 20);
		addMapping(EntityFallingSand.class, "FallingSand", 21);
		addMapping(EntityMinecart.class, "Minecart", 40);
		addMapping(EntityBoat.class, "Boat", 41);
		
		// Mine
		addMapping(EntityMeatBlock.class, "MeatBlock", 100);
		addMapping(EntityPebble.class, "Pebble", 101);
		addMapping(EntityIceSkeleton.class, "IceSkeleton", 102);
		addMapping(EntityHusk.class, "Husk", 103);
		addMapping(EntityDrowned.class, "Drowned", 104);
		addMapping(EntityZombieAlex.class, "ZombieAlex", 105);
		addMapping(EntityCityHusk.class, "CityHusk", 106);
		addMapping(EntityToxicZombie.class, "ToxicZombie", 107);
		addMapping(EntityExplodingZombie.class, "ExplodingZombie", 108);
		addMapping(EntityBuilderZombie.class, "BuilderZombie", 109);
		addMapping(EntityElementalCreeper.class, "ElementalCreeper", 110);
		addMapping(EntityChickenBlack.class, "ChickenBlack", 111);
		addMapping(EntityAlphaWitch.class, "AlphaWitch", 112);
		addMapping(EntityThrowablePotion.class, "ThrowablePotion", 113);
		addMapping(EntityColdCow.class, "ColdCow", 114);
		addMapping(EntityBetaOcelot.class, "BetaOcelot", 115);
		addMapping(EntityCatBlack.class, "BlackCat", 116);
		addMapping(EntityCatRed.class, "RedCat", 117);
		addMapping(EntityCatSiamese.class, "SiameseCat", 118);
		addMapping(EntityAmazon.class, "Amazon", 120);
		
		// Vanilla Release
		addMapping(EntityMooshroom.class, "MooshroomCow", 96);
		
		// Twilight Forest
		addMapping(EntityTFRedcap.class, "Redcap", 30);
		addMapping(EntityTFSwarmSpider.class, "SwarmSpider", 31);
		addMapping(EntityTFWraith.class, "TwilightWraith", 32);
		addMapping(EntityTFMinotaur.class, "Minotaur", 33);
		addMapping(EntityTFMinoshroom.class, "Minoshroom", 34);
		addMapping(EntityTFKobold.class, "Kobold", 35);
		addMapping(EntityTwilightBoar.class, "Boar", 36);
		addMapping(EntityTwilightDeer.class, "Deer", 37);
		addMapping(EntityTwilightBighorn.class, "BigHorn", 38);
		addMapping(EntityTFSkeletonDruid.class, "SkeletonWitch", 39);
		addMapping(EntityTFHostileWolf.class, "HostileWolf", 40);
		addMapping(EntityTFHedgeSpider.class, "HedgeSpider", 41);
		
		// Aether
		addMapping(EntityMimic.class, "Mimic", 60);
		addMapping(EntitySlider.class, "Slider", 61);
		addMapping(EntityPhyg.class, "Phyg", 62);
		addMapping(EntityFlyingCow.class, "FlyingCow", 63);
		addMapping(EntitySheepuff.class, "Sheepuff", 64);
		
		// Better Dungeons
		addMapping(EntityPirate.class, "PirateArmored", 70);
		addMapping(EntityPirateArcher.class, "PirateArcher", 71);
		addMapping(EntityPirateBoss.class, "PirateBoss", 72);
		addMapping(EntitySecretBoss.class, "SlimeBoss", 73);
		
		// Poison island
		addMapping(EntityPoisonSkeleton.class, "PoisonSkeleton", 80);
		addMapping(EntityDiamondSkeleton.class, "DiamondSkeleton", 81);
		addMapping(EntityPoisonWitch.class, "PoisonWitch", 82);
		
		// Classic pistons
		addMapping(EntityMovingPiston.class, "MovingPiston", 119);
		
		// More stuff
		addMapping(EntityTriton.class, "Triton", 120);
		addMapping(EntityHauntedCow.class, "HauntedCow", 121);
		
		// Ice palace
		addMapping(EntityIceWarrior.class, "IceWarrior", 122);
		addMapping(EntityIceArcher.class, "IceArcher", 123);
		addMapping(EntityIceBoss.class, "IceBoss", 124);
		
		addMapping(EntityPigZombieVolcanoes.class, "PigZombieVolcanoes", 125);
		
		addMapping(EntityFungalCalamity.class, "FungalCalamity", 126);
		addMapping(EntityGhoul.class, "Ghoul", 127);
		
		addMapping(EntityTemporarySeat.class, "TemporarySeat", 128);
		
		addMapping(EntityDerp.class, "Derp", 255);
		
		// MoCreatures
		addMapping(EntityGoat.class, "Goat", 150);
		
		// Trading
		/*
		addMapping(EntityPigman.class, "Pigman", 126);
		addMapping(EntityCowman.class, "Cowman", 127);
		*/
	}
}
