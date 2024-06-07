package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

import com.benimatic.twilightforest.EntityTFMinoshroom;
import com.benimatic.twilightforest.EntityTFMinotaur;
import com.benimatic.twilightforest.EntityTFRedcap;
import com.benimatic.twilightforest.EntityTFSwarmSpider;
import com.benimatic.twilightforest.EntityTFWraith;
import com.bigbang87.deadlymonsters.EntityHauntedCow;
import com.chocolatin.betterdungeons.EntityPirate;
import com.chocolatin.betterdungeons.EntityPirateArcher;
import com.chocolatin.betterdungeons.EntityPirateBoss;
import com.chocolatin.betterdungeons.EntitySecretBoss;
import com.hippoplatimus.pistons.EntityMovingPiston;
import com.misc.aether.EntityFlyingCow;
import com.misc.aether.EntityMimic;
import com.misc.aether.EntityPhyg;
import com.misc.aether.EntitySheepuff;
import com.misc.aether.EntitySlider;
import com.mojang.minecraft.ocelot.EntityBetaOcelot;
import com.mojang.minecraft.ocelot.EntityCatBlack;
import com.mojang.minecraft.ocelot.EntityCatRed;
import com.mojang.minecraft.ocelot.EntityCatSiamese;
import com.mojang.minecraft.witch.EntityAlphaWitch;
import com.mojontwins.minecraft.amazonvillage.EntityAmazon;
import com.mojontwins.minecraft.icepalace.EntityIceArcher;
import com.mojontwins.minecraft.icepalace.EntityIceBoss;
import com.mojontwins.minecraft.icepalace.EntityIceWarrior;
import com.mojontwins.minecraft.oceanruins.EntityTriton;

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

	public static Entity createEntity(int i0, World world1) {
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
		
		// Better Dungeons
		addMapping(EntityPirate.class, "PirateArmored", 70);
		addMapping(EntityPirateArcher.class, "PirateArcher", 71);
		addMapping(EntityPirateBoss.class, "PirateBoss", 72);
		addMapping(EntitySecretBoss.class, "SlimeBoss", 73);
		
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
		
		// Aether
		addMapping(EntityMimic.class, "Mimic", 60);
		addMapping(EntitySlider.class, "Slider", 61);
		addMapping(EntityPhyg.class, "Phyg", 62);
		addMapping(EntityFlyingCow.class, "FlyingCow", 63);
		addMapping(EntitySheepuff.class, "Sheepuff", 64);
		
		// Trading
		/*
		addMapping(EntityPigman.class, "Pigman", 126);
		addMapping(EntityCowman.class, "Cowman", 127);
		*/
	}
}
