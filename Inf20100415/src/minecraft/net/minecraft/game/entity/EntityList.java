package net.minecraft.game.entity;

import com.mojang.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.game.entity.animal.EntityPig;
import net.minecraft.game.entity.animal.EntitySheep;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.misc.EntityTNT;
import net.minecraft.game.entity.monster.EntityCreeper;
import net.minecraft.game.entity.monster.EntityGiant;
import net.minecraft.game.entity.monster.EntityMonster;
import net.minecraft.game.entity.monster.EntitySkeleton;
import net.minecraft.game.entity.monster.EntitySpider;
import net.minecraft.game.entity.monster.EntityZombie;
import net.minecraft.game.entity.projectile.EntityArrow;
import net.minecraft.game.world.World;

public final class EntityList {
	private static Map stringToClassMapping = new HashMap();
	private static Map classToStringMapping = new HashMap();

	private static void addMapping(Class class0, String string1) {
		stringToClassMapping.put(string1, class0);
		classToStringMapping.put(class0, string1);
	}

	public static Entity createEntityFromNBT(NBTTagCompound nBTTagCompound0, World world1) {
		Entity entity2 = null;

		try {
			Class class3;
			if((class3 = (Class)stringToClassMapping.get(nBTTagCompound0.getString("id"))) != null) {
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

	public static String getEntityString(Entity entity0) {
		return (String)classToStringMapping.get(entity0.getClass());
	}

	static {
		addMapping(EntityArrow.class, "Arrow");
		addMapping(EntityItem.class, "Item");
		addMapping(EntityPainting.class, "Painting");
		addMapping(EntityLiving.class, "Mob");
		addMapping(EntityMonster.class, "Monster");
		addMapping(EntityCreeper.class, "Creeper");
		addMapping(EntitySkeleton.class, "Skeleton");
		addMapping(EntitySpider.class, "Spider");
		addMapping(EntityGiant.class, "Giant");
		addMapping(EntityZombie.class, "Zombie");
		addMapping(EntityPig.class, "Pig");
		addMapping(EntitySheep.class, "Sheep");
		addMapping(EntityTNT.class, "PrimedTnt");
	}
}