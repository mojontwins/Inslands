package net.minecraft.src;

import java.util.HashSet;
import java.util.Map;

import net.minecraft.client.Minecraft;

public class AetherItems {
	public static final String dir = "/aether/items/";
	public static double motionOffset = 0.05D;
	public static double ybuff = 0.3D;
	public static Item VictoryMedal;
	public static Item Key;
	public static Item LoreBook;
	public static Item MoaEgg;
	public static Item AechorPetal;
	public static Item GoldenAmber;
	public static Item Stick;
	public static Item Dart;
	public static Item DartShooter;
	public static Item AmbrosiumShard;
	public static Item Zanite;
	public static Item BlueMusicDisk;
	public static Item Bucket;
	public static Item PickSkyroot;
	public static Item PickHolystone;
	public static Item PickZanite;
	public static Item PickGravitite;
	public static Item ShovelSkyroot;
	public static Item ShovelHolystone;
	public static Item ShovelZanite;
	public static Item ShovelGravitite;
	public static Item AxeSkyroot;
	public static Item AxeHolystone;
	public static Item AxeZanite;
	public static Item AxeGravitite;
	public static Item SwordSkyroot;
	public static Item SwordHolystone;
	public static Item SwordZanite;
	public static Item SwordGravitite;
	public static Item IronBubble;
	public static Item PigSlayer;
	public static Item VampireBlade;
	public static Item NatureStaff;
	public static Item SwordFire;
	public static Item SwordLightning;
	public static Item SwordHoly;
	public static Item LightningKnife;
	public static Item GummieSwet;
	public static Item HammerNotch;
	public static Item PhoenixBow;
	public static Item PhoenixHelm;
	public static Item PhoenixBody;
	public static Item PhoenixLegs;
	public static Item PhoenixBoots;
	public static Item ObsidianHelm;
	public static Item ObsidianBody;
	public static Item ObsidianLegs;
	public static Item ObsidianBoots;
	public static Item CloudStaff;
	public static Item CloudParachute;
	public static Item CloudParachuteGold;
	public static Item GravititeHelmet;
	public static Item GravititeBodyplate;
	public static Item GravititePlatelegs;
	public static Item GravititeBoots;
	public static Item ZaniteHelmet;
	public static Item ZaniteChestplate;
	public static Item ZaniteLeggings;
	public static Item ZaniteBoots;
	public static Item LifeShard;
	public static Item GoldenFeather;
	public static Item Lance;
	public static Item RepShield;
	public static Item AetherCape;
	public static Item IronRing;
	public static Item GoldRing;
	public static Item ZaniteRing;
	public static Item IronPendant;
	public static Item GoldPendant;
	public static Item ZanitePendant;
	public static Item LeatherGlove;
	public static Item IronGlove;
	public static Item GoldGlove;
	public static Item DiamondGlove;
	public static Item ZaniteGlove;
	public static Item GravititeGlove;
	public static Item PhoenixGlove;
	public static Item ObsidianGlove;
	public static Item NeptuneGlove;
	public static Item NeptuneHelmet;
	public static Item NeptuneChestplate;
	public static Item NeptuneLeggings;
	public static Item NeptuneBoots;
	public static Item RegenerationStone;
	public static Item InvisibilityCloak;
	public static Item AxeValkyrie;
	public static Item PickValkyrie;
	public static Item ShovelValkyrie;
	public static Item HealingStone;
	public static Item AgilityCape;
	public static Item WhiteCape;
	public static Item RedCape;
	public static Item YellowCape;
	public static Item BlueCape;
	public static Item IceRing;
	public static Item IcePendant;
	private static int ticks = 0;
	private static boolean jumpBoosted;
	public static int ElementalSwordIcon = ModLoader.addOverride("/gui/items.png", "/aether/items/ElementalSword.png");
	public static int gravArmour = ModLoader.AddArmor("Gravitite");
	public static int zaniteArmour = ModLoader.AddArmor("Zanite");
	public static int neptuneArmour = ModLoader.AddArmor("Neptune");
	private static boolean debug = false;

	public AetherItems() {
		VictoryMedal = (new Item(mod_Aether.idItemVictoryMedal)).setIconIndex(this.override("VictoryMedal.png")).setMaxStackSize(10).setItemName("VictoryMedal");
		Key = (new ItemAetherKey(mod_Aether.idItemKey)).setItemName("AetherKey");
		LoreBook = (new ItemLoreBook(mod_Aether.idItemLoreBook)).setIconIndex(59).setItemName("LoreBook");
		MoaEgg = (new ItemMoaEgg(mod_Aether.idItemMoaEgg)).setItemName("MoaEgg");
		AechorPetal = (new Item(mod_Aether.idItemAechorPetal)).setIconIndex(this.override("AechorPetal.png")).setItemName("AechorPetal");
		GoldenAmber = (new Item(mod_Aether.idItemGoldenAmber)).setIconIndex(this.override("GoldenAmber.png")).setItemName("GoldenAmber");
		Stick = (new Item(mod_Aether.idItemStick)).setIconIndex(this.override("Stick.png")).setItemName("SkyrootStick");
		Dart = (new ItemDart(mod_Aether.idItemDart)).setHasSubtypes(true).setItemName("Dart");
		DartShooter = (new ItemDartShooter(mod_Aether.idItemDartShooter)).setItemName("DartShooter");
		AmbrosiumShard = (new ItemAmbrosium(mod_Aether.idItemAmbrosiumShard, 1)).setIconIndex(this.override("AmbrosiumShard.png")).setItemName("AmbrosiumShard");
		HealingStone = (new ItemAmbrosium(mod_Aether.idItemHealingStone, 4)).setIconIndex(this.override("HealingStone.png")).setItemName("Healing_Stone");
		Zanite = (new Item(mod_Aether.idItemZanite)).setIconIndex(this.override("Zanite.png")).setItemName("Zanite");
		BlueMusicDisk = (new ItemAetherRecord(mod_Aether.idItemBlueMusicDisk, "AetherTune")).setIconIndex(this.override("BlueMusicDisk.png")).setItemName("BlueMusicDisk");
		Bucket = (new ItemSkyrootBucket(mod_Aether.idItemBucket)).setItemName("SkyrootBucket");
		EnumToolMaterial mat = EnumToolMaterial.WOOD;
		PickSkyroot = (new ItemSkyrootPickaxe(mod_Aether.idItemPickSkyroot, mat)).setIconIndex(this.override("PickSkyroot.png")).setItemName("PickSkyroot");
		ShovelSkyroot = (new ItemSkyrootSpade(mod_Aether.idItemShovelSkyroot, mat)).setIconIndex(this.override("ShovelSkyroot.png")).setItemName("ShovelSkyroot");
		AxeSkyroot = (new ItemSkyrootAxe(mod_Aether.idItemAxeSkyroot, mat)).setIconIndex(this.override("AxeSkyroot.png")).setItemName("AxeSkyroot");
		SwordSkyroot = (new ItemSword(mod_Aether.idItemSwordSkyroot, mat)).setIconIndex(this.override("SwordSkyroot.png")).setItemName("SwordSkyroot");
		mat = EnumToolMaterial.STONE;
		PickHolystone = (new ItemHolystonePickaxe(mod_Aether.idItemPickHolystone, mat)).setIconIndex(this.override("PickHolystone.png")).setItemName("PickHolystone");
		ShovelHolystone = (new ItemHolystoneSpade(mod_Aether.idItemShovelHolystone, mat)).setIconIndex(this.override("ShovelHolystone.png")).setItemName("ShovelHolystone");
		AxeHolystone = (new ItemHolystoneSpade(mod_Aether.idItemAxeHolystone, mat)).setIconIndex(this.override("AxeHolystone.png")).setItemName("AxeHolystone");
		SwordHolystone = (new ItemSwordHolystone(mod_Aether.idItemSwordHolystone, mat)).setIconIndex(this.override("SwordHolystone.png")).setItemName("SwordHolystone");
		mat = EnumToolMaterial.IRON;
		PickZanite = (new ItemZanitePickaxe(mod_Aether.idItemPickZanite, mat)).setIconIndex(this.override("PickZanite.png")).setItemName("PickZanite");
		ShovelZanite = (new ItemZaniteSpade(mod_Aether.idItemShovelZanite, mat)).setIconIndex(this.override("ShovelZanite.png")).setItemName("ShovelZanite");
		AxeZanite = (new ItemZaniteAxe(mod_Aether.idItemAxeZanite, mat)).setIconIndex(this.override("AxeZanite.png")).setItemName("AxeZanite");
		SwordZanite = (new ItemSwordZanite(mod_Aether.idItemSwordZanite, mat)).setIconIndex(this.override("SwordZanite.png")).setItemName("SwordZanite");
		mat = EnumToolMaterial.EMERALD;
		PickGravitite = (new ItemGravititePickaxe(mod_Aether.idItemPickGravitite, mat)).setIconIndex(this.override("PickGravitite.png")).setItemName("PickGravitite");
		ShovelGravitite = (new ItemGravititeSpade(mod_Aether.idItemShovelGravitite, mat)).setIconIndex(this.override("ShovelGravitite.png")).setItemName("ShovelGravitite");
		AxeGravitite = (new ItemGravititeAxe(mod_Aether.idItemAxeGravitite, mat)).setIconIndex(this.override("AxeGravitite.png")).setItemName("AxeGravitite");
		SwordGravitite = (new ItemSwordGravitite(mod_Aether.idItemSwordGravitite, mat)).setIconIndex(this.override("SwordGravitite.png")).setItemName("SwordGravitite");
		PickValkyrie = (new ItemValkyriePickaxe(mod_Aether.idItemPickValkyrie, mat)).setIconIndex(this.override("ValkyriePickaxe.png")).setItemName("PickValkyrie");
		ShovelValkyrie = (new ItemValkyrieSpade(mod_Aether.idItemShovelValkyrie, mat)).setIconIndex(this.override("ValkyrieShovel.png")).setItemName("ShovelValkyrie");
		AxeValkyrie = (new ItemValkyrieAxe(mod_Aether.idItemAxeValkyrie, mat)).setIconIndex(this.override("ValkyrieAxe.png")).setItemName("AxeValkyrie");
		IronBubble = (new ItemMoreArmor(mod_Aether.idItemIronBubble, 0, 0, 7)).setIconIndex(this.override("IronBubble.png")).setItemName("IronBubble");
		PigSlayer = (new ItemPigSlayer(mod_Aether.idItemPigSlayer)).setIconIndex(this.override("PigSlayer.png")).setItemName("PigSlayer");
		VampireBlade = (new ItemVampireBlade(mod_Aether.idItemVampireBlade)).setIconIndex(this.override("VampireBlade.png")).setItemName("VampireBlade");
		NatureStaff = (new Item(mod_Aether.idItemNatureStaff)).setIconIndex(this.override("NatureStaff.png")).setMaxStackSize(1).setItemName("NatureStaff");
		SwordFire = (new ItemSwordElemental(mod_Aether.idItemSwordFire, EnumElement.Fire, -20609)).setItemName("SwordFire");
		SwordHoly = (new ItemSwordElemental(mod_Aether.idItemSwordHoly, EnumElement.Holy, -81)).setItemName("SwordHoly");
		SwordLightning = (new ItemSwordElemental(mod_Aether.idItemSwordLightning, EnumElement.Lightning, -5242881)).setItemName("SwordLightning");
		LightningKnife = (new ItemLightningKnife(mod_Aether.idItemLightningKnife)).setIconIndex(this.override("LightningKnife.png")).setItemName("LightningKnife");
		GummieSwet = (new ItemGummieSwet(mod_Aether.idItemGummieSwet)).setIconIndex(this.override("GummieSwet.png")).setItemName("GummieSwet");
		HammerNotch = (new ItemNotchHammer(mod_Aether.idItemHammerNotch)).setIconIndex(this.override("HammerNotch.png")).setItemName("HammerNotch");
		CloudStaff = (new ItemCloudStaff(mod_Aether.idItemCloudStaff)).setIconIndex(this.override("CloudStaff.png")).setItemName("CloudStaff");
		PhoenixBow = (new ItemPhoenixBow(mod_Aether.idItemPhoenixBow)).setIconIndex(this.override("PhoenixBow.png")).setItemName("PhoenixBow");
		PhoenixHelm = (new ItemColouredArmor(mod_Aether.idItemPhoenixHelm, 3, ModLoader.AddArmor("Phoenix"), 0, 16742144)).setIconIndex(1).setItemName("PhoenixHelm");
		PhoenixBody = (new ItemColouredArmor(mod_Aether.idItemPhoenixBody, 3, ModLoader.AddArmor("Phoenix"), 1, 16742144)).setIconIndex(17).setItemName("PhoenixBody");
		PhoenixLegs = (new ItemColouredArmor(mod_Aether.idItemPhoenixLegs, 3, ModLoader.AddArmor("Phoenix"), 2, 16742144)).setIconIndex(33).setItemName("PhoenixLegs");
		PhoenixBoots = (new ItemColouredArmor(mod_Aether.idItemPhoenixBoots, 3, ModLoader.AddArmor("Phoenix"), 3, 16742144)).setIconIndex(49).setItemName("PhoenixBoots");
		ObsidianHelm = (new ItemColouredArmor(mod_Aether.idItemObsidianHelm, 4, ModLoader.AddArmor("Obsidian"), 0, 1774663)).setIconIndex(2).setItemName("ObsidianHelm");
		ObsidianBody = (new ItemColouredArmor(mod_Aether.idItemObsidianBody, 4, ModLoader.AddArmor("Obsidian"), 1, 1774663)).setIconIndex(18).setItemName("ObsidianBody");
		ObsidianLegs = (new ItemColouredArmor(mod_Aether.idItemObsidianLegs, 4, ModLoader.AddArmor("Obsidian"), 2, 1774663)).setIconIndex(34).setItemName("ObsidianLegs");
		ObsidianBoots = (new ItemColouredArmor(mod_Aether.idItemObsidianBoots, 4, ModLoader.AddArmor("Obsidian"), 3, 1774663)).setIconIndex(50).setItemName("ObsidianBoots");
		GravititeHelmet = (new ItemColouredArmor(mod_Aether.idItemGravititeHelmet, 3, gravArmour, 0, 15160027)).setIconIndex(2).setItemName("GravHelm");
		GravititeBodyplate = (new ItemColouredArmor(mod_Aether.idItemGravititeBodyplate, 3, gravArmour, 1, 15160027)).setIconIndex(18).setItemName("GravBody");
		GravititePlatelegs = (new ItemColouredArmor(mod_Aether.idItemGravititePlatelegs, 3, gravArmour, 2, 15160027)).setIconIndex(34).setItemName("GravLegs");
		GravititeBoots = (new ItemColouredArmor(mod_Aether.idItemGravititeBoots, 3, gravArmour, 3, 15160027)).setIconIndex(50).setItemName("GravBoots");
		ZaniteHelmet = (new ItemColouredArmor(mod_Aether.idItemZaniteHelmet, 2, zaniteArmour, 0, 7412456)).setIconIndex(2).setItemName("ZaniteHelm");
		ZaniteChestplate = (new ItemColouredArmor(mod_Aether.idItemZaniteChestplate, 2, zaniteArmour, 1, 7412456)).setIconIndex(18).setItemName("ZaniteBody");
		ZaniteLeggings = (new ItemColouredArmor(mod_Aether.idItemZaniteLeggings, 2, zaniteArmour, 2, 7412456)).setIconIndex(34).setItemName("ZaniteLegs");
		ZaniteBoots = (new ItemColouredArmor(mod_Aether.idItemZaniteBoots, 2, zaniteArmour, 3, 7412456)).setIconIndex(50).setItemName("ZaniteBoots");
		NeptuneHelmet = (new ItemColouredArmor(mod_Aether.idItemNeptuneHelmet, 3, neptuneArmour, 0, 2512127)).setIconIndex(1).setItemName("NeptuneHelm");
		NeptuneChestplate = (new ItemColouredArmor(mod_Aether.idItemNeptuneChestplate, 3, neptuneArmour, 1, 2512127)).setIconIndex(17).setItemName("NeptuneBody");
		NeptuneLeggings = (new ItemColouredArmor(mod_Aether.idItemNeptuneLeggings, 3, neptuneArmour, 2, 2512127)).setIconIndex(33).setItemName("NeptuneLegs");
		NeptuneBoots = (new ItemColouredArmor(mod_Aether.idItemNeptuneBoots, 3, neptuneArmour, 3, 2512127)).setIconIndex(49).setItemName("NeptuneBoots");
		LifeShard = (new ItemLifeShard(mod_Aether.idItemLifeShard)).setIconIndex(this.override("LifeShard.png")).setItemName("LifeShard");
		GoldenFeather = (new ItemMoreArmor(mod_Aether.idItemGoldenFeather, 0, 0, 7)).setIconIndex(this.override("GoldenFeather.png")).setItemName("GoldenFeather");
		Lance = (new ItemLance(mod_Aether.idItemLance, mat)).setIconIndex(this.override("Lance.png")).setItemName("Lance");
		RepShield = (new ItemMoreArmor(mod_Aether.idItemRepShield, 0, 0, 6, 0xFFFFFF)).setIconIndex(this.override("RepulsionShield.png")).setItemName("RepShield").setMaxDamage(512);
		int Ring = this.override("Ring.png");
		IronRing = (new ItemMoreArmor(mod_Aether.idItemIronRing, 0, "/armor/Accessories.png", 8, 0xFFFFFF)).setIconIndex(Ring).setItemName("IronRing");
		GoldRing = (new ItemMoreArmor(mod_Aether.idItemGoldRing, 0, "/armor/Accessories.png", 8, 16776994)).setIconIndex(Ring).setItemName("GoldRing");
		ZaniteRing = (new ItemMoreArmor(mod_Aether.idItemZaniteRing, 0, "/armor/Accessories.png", 8, 7412456)).setIconIndex(Ring).setItemName("ZaniteRing");
		IceRing = (new ItemMoreArmor(mod_Aether.idItemIceRing, 0, "/armor/Accessories.png", 8, 9823975)).setIconIndex(Ring).setItemName("IceRing");
		int Pendant = this.override("Pendant.png");
		IronPendant = (new ItemMoreArmor(mod_Aether.idItemIronPendant, 0, "/armor/Accessories.png", 4, 0xFFFFFF)).setIconIndex(Pendant).setItemName("IronPendant");
		GoldPendant = (new ItemMoreArmor(mod_Aether.idItemGoldPendant, 0, "/armor/Accessories.png", 4, 16776994)).setIconIndex(Pendant).setItemName("GoldPendant");
		ZanitePendant = (new ItemMoreArmor(mod_Aether.idItemZanitePendant, 0, "/armor/Accessories.png", 4, 7412456)).setIconIndex(Pendant).setItemName("ZanitePendant");
		IcePendant = (new ItemMoreArmor(mod_Aether.idItemIcePendant, 0, "/armor/Accessories.png", 4, 9823975)).setIconIndex(Pendant).setItemName("IcePendant");
		AetherCape = (new ItemMoreArmor(mod_Aether.idItemAetherCape, 0, "/aether/other/AetherCape.png", 5)).setIconIndex(this.override("AetherCape.png")).setItemName("AetherCape");
		RegenerationStone = (new ItemMoreArmor(mod_Aether.idItemRegenerationStone, 0, 0, 7)).setIconIndex(this.override("RegenerationStone.png")).setItemName("RegenerationStone");
		InvisibilityCloak = (new ItemMoreArmor(mod_Aether.idItemInvisibilityCloak, 0, 0, 5)).setIconIndex(this.override("InvisibilityCloak.png")).setItemName("InvisibilityCloak");
		AgilityCape = (new ItemMoreArmor(mod_Aether.idItemAgilityCape, 0, "/aether/other/AgilityCape.png", 5)).setIconIndex(this.override("AgilityCape.png")).setItemName("AgilityCape");
		int CapeTexture = this.override("Cape.png");
		WhiteCape = (new ItemMoreArmor(mod_Aether.idItemWhiteCape, 0, "/aether/other/WhiteCape.png", 5)).setIconIndex(CapeTexture).setItemName("WhiteCape");
		RedCape = (new ItemMoreArmor(mod_Aether.idItemRedCape, 0, "/aether/other/RedCape.png", 5, 15208721)).setIconIndex(CapeTexture).setItemName("RedCape");
		YellowCape = (new ItemMoreArmor(mod_Aether.idItemYellowCape, 0, "/aether/other/YellowCape.png", 5, 13486862)).setIconIndex(CapeTexture).setItemName("YellowCape");
		BlueCape = (new ItemMoreArmor(mod_Aether.idItemBlueCape, 0, "/aether/other/BlueCape.png", 5, 1277879)).setIconIndex(CapeTexture).setItemName("BlueCape");
		int Glove = this.override("Glove.png");
		int GloveChain = this.override("GloveChain.png");
		LeatherGlove = (new ItemMoreArmor(mod_Aether.idItemLeatherGlove, 0, "/armor/Accessories.png", 10, 12999733)).setIconIndex(Glove).setItemName("LeatherGlove");
		IronGlove = (new ItemMoreArmor(mod_Aether.idItemIronGlove, 2, "/armor/Accessories.png", 10, 14540253)).setIconIndex(Glove).setItemName("IronGlove");
		GoldGlove = (new ItemMoreArmor(mod_Aether.idItemGoldGlove, 1, "/armor/Accessories.png", 10, 15396439)).setIconIndex(Glove).setItemName("GoldGlove");
		DiamondGlove = (new ItemMoreArmor(mod_Aether.idItemDiamondGlove, 3, "/armor/Accessories.png", 10, 3402699)).setIconIndex(Glove).setItemName("DiamondGlove");
		ZaniteGlove = (new ItemMoreArmor(mod_Aether.idItemZaniteGlove, 2, "/armor/Accessories.png", 10, 7412456)).setIconIndex(Glove).setItemName("ZaniteGlove");
		GravititeGlove = (new ItemMoreArmor(mod_Aether.idItemGravititeGlove, 3, "/armor/Accessories.png", 10, 15160027)).setIconIndex(Glove).setItemName("GravititeGlove");
		PhoenixGlove = (new ItemMoreArmor(mod_Aether.idItemPhoenixGlove, 3, "/armor/Phoenix.png", 10, 16742144, false)).setIconIndex(GloveChain).setItemName("PhoenixGlove");
		ObsidianGlove = (new ItemMoreArmor(mod_Aether.idItemObsidianGlove, 4, "/armor/Accessories.png", 10, 1774663)).setIconIndex(Glove).setItemName("ObsidianGlove");
		NeptuneGlove = (new ItemMoreArmor(mod_Aether.idItemNeptuneGlove, 3, "/armor/Accessories.png", 10, 2512127)).setIconIndex(GloveChain).setItemName("NeptuneGlove");
		CloudParachute = (new ItemCloudParachute(mod_Aether.idItemCloudParachute)).setItemName("CloudParachute");
		CloudParachuteGold = (new ItemCloudParachute(mod_Aether.idItemCloudParachuteGold)).setItemName("CloudParachuteGold");
		ModLoader.RegisterEntityID(EntityFlamingArrow.class, "FlamingArrow", ModLoader.getUniqueEntityId());
		ModLoader.RegisterEntityID(EntityMiniCloud.class, "Mini Cloud", ModLoader.getUniqueEntityId());
		ModLoader.AddName(VictoryMedal, "\u00a7aVictory Medal");
		ModLoader.AddName(new ItemStack(Key, 1, 0), "\u00a7aBronze Key");
		ModLoader.AddName(new ItemStack(Key, 1, 1), "\u00a7aSilver Key");
		ModLoader.AddName(new ItemStack(Key, 1, 2), "\u00a7aGold Key");
		ModLoader.AddName(new ItemStack(LoreBook, 1, 0), "\u00a7aBook of Lore : Volume 1");
		ModLoader.AddName(new ItemStack(LoreBook, 1, 1), "\u00a7aBook of Lore : Volume 2");
		ModLoader.AddName(new ItemStack(LoreBook, 1, 2), "\u00a7aBook of Lore : Volume 3");
		ModLoader.AddName(AechorPetal, "Aechor Petal");
		ModLoader.AddName(GoldenAmber, "Golden Amber");
		ModLoader.AddName(Stick, "Skyroot Stick");
		ModLoader.AddName(new ItemStack(Dart, 1, 0), "Golden Dart");
		ModLoader.AddName(new ItemStack(Dart, 1, 1), "Poison Dart");
		ModLoader.AddName(new ItemStack(Dart, 1, 2), "\u00a7bEnchanted Dart");
		ModLoader.AddName(new ItemStack(DartShooter, 1, 0), "Dart Shooter");
		ModLoader.AddName(new ItemStack(DartShooter, 1, 1), "Poison Shooter");
		ModLoader.AddName(new ItemStack(DartShooter, 1, 2), "\u00a7bEnchanted Shooter");
		ModLoader.AddName(AmbrosiumShard, "Ambrosium Shard");
		ModLoader.AddName(Zanite, "Zanite Gemstone");
		ModLoader.AddName(BlueMusicDisk, "\u00a7bBlue Music Disk");
		ModLoader.AddName(new ItemStack(Bucket, 1, 0), "Skyroot Bucket");
		ModLoader.AddName(new ItemStack(Bucket, 1, Block.waterMoving.blockID), "Water Bucket");
		ModLoader.AddName(new ItemStack(Bucket, 1, 1), "Milk Bucket");
		ModLoader.AddName(new ItemStack(Bucket, 1, 2), "Poison Bucket");
		ModLoader.AddName(new ItemStack(Bucket, 1, 3), "\u00a7bRemedy Bucket");
		ModLoader.AddName(PickSkyroot, "Skyroot Pickaxe");
		ModLoader.AddName(ShovelSkyroot, "Skyroot Shovel");
		ModLoader.AddName(AxeSkyroot, "Skyroot Axe");
		ModLoader.AddName(SwordSkyroot, "Skyroot Sword");
		ModLoader.AddName(PickHolystone, "Holystone Pickaxe");
		ModLoader.AddName(ShovelHolystone, "Holystone Shovel");
		ModLoader.AddName(AxeHolystone, "Holystone Axe");
		ModLoader.AddName(SwordHolystone, "Holystone Sword");
		ModLoader.AddName(PickZanite, "Zanite Pickaxe");
		ModLoader.AddName(ShovelZanite, "Zanite Shovel");
		ModLoader.AddName(AxeZanite, "Zanite Axe");
		ModLoader.AddName(SwordZanite, "Zanite Sword");
		ModLoader.AddName(PickGravitite, "Gravitite Pickaxe");
		ModLoader.AddName(ShovelGravitite, "Gravitite Shovel");
		ModLoader.AddName(AxeGravitite, "Gravitite Axe");
		ModLoader.AddName(SwordGravitite, "Gravitite Sword");
		ModLoader.AddName(IronBubble, "\u00a7aIron Bubble");
		ModLoader.AddName(PigSlayer, "\u00a7aPig Slayer");
		ModLoader.AddName(VampireBlade, "\u00a7aVampire Blade");
		ModLoader.AddName(NatureStaff, "Nature Staff");
		ModLoader.AddName(SwordFire, "\u00a7aFlaming Sword");
		ModLoader.AddName(SwordHoly, "\u00a7aHoly Sword");
		ModLoader.AddName(SwordLightning, "\u00a7aLightning Sword");
		ModLoader.AddName(LightningKnife, "\u00a7aLightning Knife");
		ModLoader.AddName(GummieSwet, "\u00a7aGummie Swet");
		ModLoader.AddName(HammerNotch, "\u00a7aHammer of Notch");
		ModLoader.AddName(CloudStaff, "\u00a7aCloud Staff");
		ModLoader.AddName(PhoenixBow, "\u00a7aPhoenix Bow");
		ModLoader.AddName(PhoenixHelm, "\u00a7aPhoenix Helmet");
		ModLoader.AddName(PhoenixBody, "\u00a7aPhoenix Chestplate");
		ModLoader.AddName(PhoenixLegs, "\u00a7aPhoenix Leggings");
		ModLoader.AddName(PhoenixBoots, "\u00a7aPhoenix Boots");
		ModLoader.AddName(ObsidianHelm, "\u00a7aObsidian Helmet");
		ModLoader.AddName(ObsidianBody, "\u00a7aObsidian Chestplate");
		ModLoader.AddName(ObsidianLegs, "\u00a7aObsidian Leggings");
		ModLoader.AddName(ObsidianBoots, "\u00a7aObsidian Boots");
		ModLoader.AddName(CloudParachute, "Cloud Parachute");
		ModLoader.AddName(CloudParachuteGold, "Golden Parachute");
		ModLoader.AddName(GravititeHelmet, "Gravitite Helmet");
		ModLoader.AddName(GravititeBodyplate, "Gravitite Chestplate");
		ModLoader.AddName(GravititePlatelegs, "Gravitite Leggings");
		ModLoader.AddName(GravititeBoots, "Gravitite Boots");
		ModLoader.AddName(ZaniteHelmet, "Zanite Helmet");
		ModLoader.AddName(ZaniteChestplate, "Zanite Chestplate");
		ModLoader.AddName(ZaniteLeggings, "Zanite Leggings");
		ModLoader.AddName(ZaniteBoots, "Zanite Boots");
		ModLoader.AddName(NeptuneHelmet, "\u00a7aNeptune Helmet");
		ModLoader.AddName(NeptuneChestplate, "\u00a7aNeptune Chestplate");
		ModLoader.AddName(NeptuneLeggings, "\u00a7aNeptune Leggings");
		ModLoader.AddName(NeptuneBoots, "\u00a7aNeptune Boots");
		ModLoader.AddName(LifeShard, "\u00a7aLife Shard");
		ModLoader.AddName(GoldenFeather, "\u00a7aGolden Feather");
		ModLoader.AddName(Lance, "\u00a7aValkyrie Lance");
		ModLoader.AddName(RepShield, "\u00a7aShield of Repulsion");
		ModLoader.AddName(IronRing, "Iron Ring");
		ModLoader.AddName(GoldRing, "Gold Ring");
		ModLoader.AddName(ZaniteRing, "Zanite Ring");
		ModLoader.AddName(IronPendant, "Iron Pendant");
		ModLoader.AddName(GoldPendant, "Gold Pendant");
		ModLoader.AddName(ZanitePendant, "Zanite Pendant");
		ModLoader.AddName(AetherCape, "Swet Cape");
		ModLoader.AddName(LeatherGlove, "Leather Glove");
		ModLoader.AddName(IronGlove, "Iron Gloves");
		ModLoader.AddName(GoldGlove, "Gold Gloves");
		ModLoader.AddName(DiamondGlove, "Diamond Gloves");
		ModLoader.AddName(ZaniteGlove, "Zanite Gloves");
		ModLoader.AddName(GravititeGlove, "Gravitite Gloves");
		ModLoader.AddName(PhoenixGlove, "\u00a7aPhoenix Gloves");
		ModLoader.AddName(ObsidianGlove, "\u00a7aObsidian Gloves");
		ModLoader.AddName(NeptuneGlove, "\u00a7aNeptune Gloves");
		ModLoader.AddName(new ItemStack(GummieSwet, 1, 0), "\u00a7aBlue Gummie Swet");
		ModLoader.AddName(new ItemStack(GummieSwet, 1, 1), "\u00a7aGold Gummie Swet");
		ModLoader.AddName(RegenerationStone, "\u00a7aRegeneration Stone");
		ModLoader.AddName(InvisibilityCloak, "\u00a7aInvisibility Cloak");
		ModLoader.AddName(AgilityCape, "\u00a7aAgility Cape");
		ModLoader.AddName(WhiteCape, "White Cape");
		ModLoader.AddName(RedCape, "Red Cape");
		ModLoader.AddName(YellowCape, "Yellow Cape");
		ModLoader.AddName(BlueCape, "Blue Cape");
		ModLoader.AddName(HealingStone, "\u00a7bHealing Stone");
		ModLoader.AddName(IcePendant, "Ice Pendant");
		ModLoader.AddName(IceRing, "Ice Ring");
		ModLoader.AddName(PickValkyrie, "\u00a7aValkyrie Pickaxe");
		ModLoader.AddName(ShovelValkyrie, "\u00a7aValkyrie Shovel");
		ModLoader.AddName(AxeValkyrie, "\u00a7aValkyrie Axe");

		for(int e = 0; e < MoaColour.colours.size(); ++e) {
			ModLoader.AddName(new ItemStack(MoaEgg, 1, e), MoaColour.getColour(e).name + " Moa Egg");
		}

		if(mod_Aether.TMIhidden) {
			try {
				Class.forName("mod_TooManyItems");
				HashSet hashSet11 = (HashSet)ModLoader.getPrivateValue(TMIConfig.class, (Object)null, "excludeIds");
				hashSet11.add(IronBubble.shiftedIndex);
				hashSet11.add(PigSlayer.shiftedIndex);
				hashSet11.add(VampireBlade.shiftedIndex);
				hashSet11.add(NatureStaff.shiftedIndex);
				hashSet11.add(SwordFire.shiftedIndex);
				hashSet11.add(SwordHoly.shiftedIndex);
				hashSet11.add(SwordLightning.shiftedIndex);
				hashSet11.add(LightningKnife.shiftedIndex);
				hashSet11.add(GummieSwet.shiftedIndex);
				hashSet11.add(HammerNotch.shiftedIndex);
				hashSet11.add(CloudStaff.shiftedIndex);
				hashSet11.add(PhoenixBow.shiftedIndex);
				hashSet11.add(PhoenixHelm.shiftedIndex);
				hashSet11.add(PhoenixBody.shiftedIndex);
				hashSet11.add(PhoenixLegs.shiftedIndex);
				hashSet11.add(PhoenixBoots.shiftedIndex);
				hashSet11.add(NeptuneHelmet.shiftedIndex);
				hashSet11.add(NeptuneChestplate.shiftedIndex);
				hashSet11.add(NeptuneLeggings.shiftedIndex);
				hashSet11.add(NeptuneBoots.shiftedIndex);
				hashSet11.add(ObsidianHelm.shiftedIndex);
				hashSet11.add(ObsidianBody.shiftedIndex);
				hashSet11.add(ObsidianLegs.shiftedIndex);
				hashSet11.add(ObsidianBoots.shiftedIndex);
				hashSet11.add(Key.shiftedIndex);
				hashSet11.add(VictoryMedal.shiftedIndex);
				hashSet11.add(LoreBook.shiftedIndex);
				hashSet11.add(BlueMusicDisk.shiftedIndex);
				hashSet11.add(LifeShard.shiftedIndex);
				hashSet11.add(GoldenFeather.shiftedIndex);
				hashSet11.add(Lance.shiftedIndex);
				hashSet11.add(RepShield.shiftedIndex);
				hashSet11.add(AetherCape.shiftedIndex);
				hashSet11.add(NeptuneGlove.shiftedIndex);
				hashSet11.add(PhoenixGlove.shiftedIndex);
				hashSet11.add(ObsidianGlove.shiftedIndex);
				hashSet11.add(AetherBlocks.Aerogel.blockID);
				hashSet11.add(RegenerationStone.shiftedIndex);
				hashSet11.add(InvisibilityCloak.shiftedIndex);
				hashSet11.add(PickValkyrie.shiftedIndex);
				hashSet11.add(AxeValkyrie.shiftedIndex);
				hashSet11.add(ShovelValkyrie.shiftedIndex);
				hashSet11.add(AgilityCape.shiftedIndex);
				ModLoader.setPrivateValue(TMIConfig.class, (Object)null, "excludeIds", hashSet11);
			} catch (Exception exception10) {
				try {
					Class.forName("mod_TooManyItems");
					HashSet e2 = (HashSet)ModLoader.getPrivateValue(TMIConfig.class, (Object)null, "excludeIds");
					e2.add(IronBubble.shiftedIndex);
					e2.add(PigSlayer.shiftedIndex);
					e2.add(VampireBlade.shiftedIndex);
					e2.add(NatureStaff.shiftedIndex);
					e2.add(SwordFire.shiftedIndex);
					e2.add(SwordHoly.shiftedIndex);
					e2.add(SwordLightning.shiftedIndex);
					e2.add(LightningKnife.shiftedIndex);
					e2.add(GummieSwet.shiftedIndex);
					e2.add(HammerNotch.shiftedIndex);
					e2.add(CloudStaff.shiftedIndex);
					e2.add(PhoenixBow.shiftedIndex);
					e2.add(PhoenixHelm.shiftedIndex);
					e2.add(PhoenixBody.shiftedIndex);
					e2.add(PhoenixLegs.shiftedIndex);
					e2.add(PhoenixBoots.shiftedIndex);
					e2.add(NeptuneHelmet.shiftedIndex);
					e2.add(NeptuneChestplate.shiftedIndex);
					e2.add(NeptuneLeggings.shiftedIndex);
					e2.add(NeptuneBoots.shiftedIndex);
					e2.add(ObsidianHelm.shiftedIndex);
					e2.add(ObsidianBody.shiftedIndex);
					e2.add(ObsidianLegs.shiftedIndex);
					e2.add(ObsidianBoots.shiftedIndex);
					e2.add(Key.shiftedIndex);
					e2.add(VictoryMedal.shiftedIndex);
					e2.add(LoreBook.shiftedIndex);
					e2.add(BlueMusicDisk.shiftedIndex);
					e2.add(LifeShard.shiftedIndex);
					e2.add(GoldenFeather.shiftedIndex);
					e2.add(Lance.shiftedIndex);
					e2.add(RepShield.shiftedIndex);
					e2.add(AetherCape.shiftedIndex);
					e2.add(NeptuneGlove.shiftedIndex);
					e2.add(PhoenixGlove.shiftedIndex);
					e2.add(ObsidianGlove.shiftedIndex);
					e2.add(AetherBlocks.Aerogel.blockID);
					e2.add(RegenerationStone.shiftedIndex);
					e2.add(InvisibilityCloak.shiftedIndex);
					ModLoader.setPrivateValue(TMIConfig.class, (Object)null, "excludeIds", e2);
				} catch (Exception exception9) {
				}
			}
		}

	}

	public static void tick(Minecraft game) {
		if(!game.theWorld.multiplayerWorld) {
			EntityPlayerSP player = game.thePlayer;
			InventoryAether inv = mod_Aether.getPlayer(player).inv;
			if(player.inventory.armorInventory[3] != null && player.inventory.armorInventory[3].itemID == PhoenixHelm.shiftedIndex && player.inventory.armorInventory[2] != null && player.inventory.armorInventory[2].itemID == PhoenixBody.shiftedIndex && player.inventory.armorInventory[1] != null && player.inventory.armorInventory[1].itemID == PhoenixLegs.shiftedIndex && player.inventory.armorInventory[0] != null && player.inventory.armorInventory[0].itemID == PhoenixBoots.shiftedIndex && inv.slots[6] != null && inv.slots[6].itemID == PhoenixGlove.shiftedIndex) {
				player.isImmuneToFire = true;
				player.fire = 0;
				if(!GuiMainMenu.mmactive) {
					game.theWorld.spawnParticle("flame", player.posX + player.rand.nextGaussian() / 5.0D, player.posY - 0.5D + player.rand.nextGaussian() / 5.0D, player.posZ + player.rand.nextGaussian() / 3.0D, 0.0D, 0.0D, 0.0D);
				}
			} else {
				player.isImmuneToFire = false;
			}

			int i;
			if(player.isWet()) {
				i = game.theWorld.getBlockId(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
				if(player.inventory.armorInventory[0] != null && player.inventory.armorInventory[0].itemID == PhoenixBoots.shiftedIndex) {
					player.inventory.armorInventory[0].damageItem(1, player);
					if(i == Block.waterStill.blockID) {
						player.inventory.armorInventory[0].damageItem(4, player);
						game.theWorld.setBlockWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ), 0);
					}

					if(player.inventory.armorInventory[0] == null || player.inventory.armorInventory[0].stackSize < 1) {
						player.inventory.armorInventory[0] = new ItemStack(ObsidianBoots, 1, 0);
					}
				}

				if(player.inventory.armorInventory[1] != null && player.inventory.armorInventory[1].itemID == PhoenixLegs.shiftedIndex) {
					player.inventory.armorInventory[1].damageItem(1, player);
					if(i == Block.waterStill.blockID) {
						player.inventory.armorInventory[1].damageItem(4, player);
						game.theWorld.setBlockWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ), 0);
					}

					if(player.inventory.armorInventory[1] == null || player.inventory.armorInventory[1].stackSize < 1) {
						player.inventory.armorInventory[1] = new ItemStack(ObsidianLegs, 1, 0);
					}
				}

				if(player.inventory.armorInventory[2] != null && player.inventory.armorInventory[2].itemID == PhoenixBody.shiftedIndex) {
					player.inventory.armorInventory[2].damageItem(1, player);
					if(i == Block.waterStill.blockID) {
						player.inventory.armorInventory[2].damageItem(4, player);
						game.theWorld.setBlockWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ), 0);
					}

					if(player.inventory.armorInventory[2] == null || player.inventory.armorInventory[2].stackSize < 1) {
						player.inventory.armorInventory[2] = new ItemStack(ObsidianBody, 1, 0);
					}
				}

				if(player.inventory.armorInventory[3] != null && player.inventory.armorInventory[3].itemID == PhoenixHelm.shiftedIndex) {
					player.inventory.armorInventory[3].damageItem(1, player);
					if(i == Block.waterStill.blockID) {
						player.inventory.armorInventory[3].damageItem(4, player);
						game.theWorld.setBlockWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ), 0);
					}

					if(player.inventory.armorInventory[3] == null || player.inventory.armorInventory[3].stackSize < 1) {
						player.inventory.armorInventory[3] = new ItemStack(ObsidianHelm, 1, 0);
					}
				}

				if(inv.slots[6] != null && inv.slots[6].itemID == PhoenixGlove.shiftedIndex) {
					inv.slots[6].damageItem(1, player);
					if(i == Block.waterStill.blockID) {
						inv.slots[6].damageItem(4, player);
						game.theWorld.setBlockWithNotify(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ), 0);
					}

					if(inv.slots[6] == null || inv.slots[6].stackSize < 1) {
						inv.slots[6] = new ItemStack(ObsidianGlove, 1, 0);
					}
				}
			}

			if(player.inventory.armorInventory[3] != null && player.inventory.armorInventory[3].itemID == GravititeHelmet.shiftedIndex && player.inventory.armorInventory[2] != null && player.inventory.armorInventory[2].itemID == GravititeBodyplate.shiftedIndex && player.inventory.armorInventory[1] != null && player.inventory.armorInventory[1].itemID == GravititePlatelegs.shiftedIndex && player.inventory.armorInventory[0] != null && player.inventory.armorInventory[0].itemID == GravititeBoots.shiftedIndex && inv.slots[6] != null && inv.slots[6].itemID == GravititeGlove.shiftedIndex) {
				if(player.isJumping && !jumpBoosted) {
					player.motionY = 1.0D;
					jumpBoosted = true;
				}

				player.fallDistance = -1.0F;
			}

			if(!player.isJumping && player.onGround) {
				jumpBoosted = false;
			}

			if(inv.slots[3] != null && inv.slots[3].itemID == IronBubble.shiftedIndex || inv.slots[7] != null && inv.slots[7].itemID == IronBubble.shiftedIndex) {
				player.air = 20;
			}

			if(inv.slots[0] != null && inv.slots[0].itemID == IcePendant.shiftedIndex || inv.slots[4] != null && inv.slots[4].itemID == IceRing.shiftedIndex || inv.slots[5] != null && inv.slots[5].itemID == IceRing.shiftedIndex) {
				i = MathHelper.floor_double(player.posX);
				int j = MathHelper.floor_double(player.boundingBox.minY);
				int k = MathHelper.floor_double(player.posZ);
				double d10000 = player.posY - (double)j;
				game.theWorld.getBlockMaterial(i, j, k);
				game.theWorld.getBlockMaterial(i, j - 1, k);

				for(int l = i - 1; l <= i + 1; ++l) {
					for(int i1 = j - 1; i1 <= j + 1; ++i1) {
						for(int j1 = k - 1; j1 <= k + 1; ++j1) {
							if(game.theWorld.getBlockId(l, i1, j1) == 8) {
								game.theWorld.setBlockWithNotify(l, i1, j1, 79);
							} else if(game.theWorld.getBlockId(l, i1, j1) == 9) {
								game.theWorld.setBlockWithNotify(l, i1, j1, 79);
							} else if(game.theWorld.getBlockId(l, i1, j1) == 10) {
								game.theWorld.setBlockWithNotify(l, i1, j1, 49);
							} else if(game.theWorld.getBlockId(l, i1, j1) == 11) {
								game.theWorld.setBlockWithNotify(l, i1, j1, 49);
							}
						}
					}
				}
			}

			if(inv.slots[3] != null && inv.slots[3].itemID == GoldenFeather.shiftedIndex || inv.slots[7] != null && inv.slots[7].itemID == GoldenFeather.shiftedIndex) {
				if(!player.onGround && player.motionY < 0.0D && !player.inWater) {
					player.motionY *= 0.6D;
				}

				player.fallDistance = -1.0F;
			}

			if(inv.slots[1] != null && inv.slots[1].itemID == AgilityCape.shiftedIndex) {
				player.stepHeight = 1.0F;
			} else {
				player.stepHeight = 0.5F;
			}

			if(ticks % 200 == 0 && player.health < mod_Aether.getPlayer(player).maxHealth && (inv.slots[3] != null && inv.slots[3].itemID == RegenerationStone.shiftedIndex || inv.slots[7] != null && inv.slots[7].itemID == RegenerationStone.shiftedIndex)) {
				player.heal(1);
			}

			++ticks;
		}

	}

	public static void AddRenderer(Map map) {
		map.put(EntityCloudParachute.class, new RenderCloudParachute());
		map.put(EntityFlamingArrow.class, new RenderFlamingArrow());
		map.put(EntityNotchWave.class, new RenderNotchWave());
		map.put(EntityAetherLightning.class, new RenderLightningBolt());
		map.put(EntityLightningKnife.class, new RenderLightningKnife());
		map.put(EntityMiniCloud.class, new RenderLiving(new ModelMiniCloud(0.0F, 20.0F), 0.35F));
	}

	public static void takenCrafting(EntityPlayer player, ItemStack stack) {
		if(stack.itemID == AetherBlocks.Enchanter.blockID) {
			mod_Aether.giveAchievement(AetherAchievements.enchanter, player);
		}

		if(stack.itemID == PickGravitite.shiftedIndex || stack.itemID == ShovelGravitite.shiftedIndex || stack.itemID == AxeGravitite.shiftedIndex || stack.itemID == SwordGravitite.shiftedIndex) {
			mod_Aether.giveAchievement(AetherAchievements.gravTools, player);
		}

	}

	public int override(String path) {
		return ModLoader.addOverride("/gui/items.png", "/aether/items/" + path);
	}
}
