package net.minecraft.src;

import java.util.Map;

public class AetherMobs {
	public AetherMobs() {
		ModLoader.RegisterEntityID(EntityAerwhale.class, "Aerwhale", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(EntityCockatrice.class, "Cockatrice", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(EntitySwet.class, "Swets", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(EntityZephyr.class, "Zephyr", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(EntitySheepuff.class, "Sheepuff", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(EntityPhyg.class, "FlyingPig", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(EntityAechorPlant.class, "AechorPlant", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(EntitySentry.class, "Sentry", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(EntitySlider.class, "Slider", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(EntityValkyrie.class, "Valkyrie", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(EntityHomeShot.class, "HomeShot", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(EntityFireMonster.class, "Fire Monster", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(EntityFireMinion.class, "Fire Minion", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(EntityFiroBall.class, "Firo Ball", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(EntityMoa.class, "MoaBase", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(EntityFlyingCow.class, "FlyingCow", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(EntityAerbunny.class, "Aerbunny", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(Whirly.class, "Whirlwind", ModLoader.getUniqueEntityId());
	}

	public static void AddRenderer(Map map) {
		map.put(EntityZephyr.class, new RenderZephyr());
		map.put(EntityAerwhale.class, new RenderAerwhale());
		map.put(EntityCockatrice.class, new RenderCockatrice(new ModelCockatrice(), 1.0F));
		map.put(EntitySheepuff.class, new RenderSheepuff(new ModelSheepuff1(), new ModelSheepuff2(), new ModelSheepuff3(), 0.7F));
		map.put(EntityPhyg.class, new RenderPhyg(new ModelFlyingPig1(), new ModelFlyingPig2(), 0.7F));
		map.put(EntitySwet.class, new RenderSwet(new ModelSlime(16), new ModelSlime(0), 0.3F));
		map.put(EntityAechorPlant.class, new RenderAechorPlant(new ModelAechorPlant(), 0.3F));
		map.put(EntityZephyrSnowball.class, new RenderZephyrSnowball());
		map.put(EntitySentry.class, new RenderSentry(new ModelSlime(0), 0.2F));
		map.put(EntitySlider.class, new RenderSlider(new ModelSlider(0.0F, 12.0F), 1.5F));
		map.put(EntityValkyrie.class, new RenderValkyrie(new ModelValkyrie(), 0.3F));
		map.put(EntityHomeShot.class, new RenderHomeShot(new ModelHomeShot(0.0F, 0.0F), 0.2F));
		map.put(EntityFireMonster.class, new RenderBiped(new ModelFireMonster(0.0F, 0.0F), 0.4F));
		map.put(EntityFireMinion.class, new RenderBiped(new ModelFireMinion(0.0F, 0.0F), 0.4F));
		map.put(EntityFiroBall.class, new RenderFiroBall(new ModelHomeShot(0.5F, 0.0F), 0.25F));
		map.put(EntityMoa.class, new RenderMoa(new ModelMoa(), 1.0F));
		map.put(EntityFlyingCow.class, new RenderFlyingCow(new ModelFlyingCow1(), new ModelFlyingCow2(), 0.7F));
		map.put(EntityAerbunny.class, new RenderAerbunny(new ModelAerbunny(), 0.3F));
		map.put(Whirly.class, new RenderWhirly());
	}
}
