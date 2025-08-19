package net.minecraft.game.item;

import java.util.Random;

import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.world.World;
import net.minecraft.game.world.block.Block;

public class Item {
	protected static Random itemRand = new Random();
	public static Item[] itemsList = new Item[1024];
	public static Item shovel;
	public static Item pickaxeSteel;
	public static Item axeSteel;
	public static Item flintAndSteel;
	public static Item apple;
	public static Item bow;
	public static Item arrow;
	public static Item coal;
	public static Item diamod;
	public static Item ingotIron;
	public static Item ingotGold;
	public static Item swordSteel;
	public static Item swordWood;
	public static Item shovelWood;
	public static Item pickaxeWood;
	public static Item axeWood;
	public static Item swordStone;
	public static Item shovelStone;
	public static Item pickaxeStone;
	public static Item axeStone;
	public static Item swordDiamond;
	public static Item shovelDiamond;
	public static Item pickaxeDiamond;
	public static Item axeDiamond;
	public static Item stick;
	public static Item bowlEmpty;
	public static Item bowlSoup;
	public static Item swordGold;
	public static Item shovelGold;
	public static Item pickaxeGold;
	public static Item axeGold;
	public static Item silk;
	public static Item feather;
	public static Item gunpowder;
	public static Item hoeWood;
	public static Item hoeStone;
	public static Item hoeSteel;
	public static Item hoeDiamond;
	public static Item hoeGold;
	public static Item seeds;
	public static Item wheat;
	public static Item bread;
	public static Item helmetLeather;
	public static Item plateLeather;
	public static Item legsLeather;
	public static Item bootsLeather;
	public static Item helmetChain;
	public static Item plateChain;
	public static Item legsChain;
	public static Item bootsChain;
	public static Item helmetSteel;
	public static Item plateSteel;
	public static Item legsSteel;
	public static Item bootsSteel;
	public static Item helmetDiamond;
	public static Item plateDiamond;
	public static Item legsDiamond;
	public static Item bootsDiamond;
	public static Item helmetGold;
	public static Item plateGold;
	public static Item legsGold;
	public static Item bootsGold;
	public static Item flint;
	public static Item porkRaw;
	public static Item porkCooked;
	public static Item painting;
	public static Item appleGold;
	public final int shiftedIndex;
	protected int maxStackSize = 64;
	protected int maxDamage = 32;
	private int iconIndex;

	protected Item(int i1) {
		this.shiftedIndex = i1 + 256;
		if(itemsList[i1 + 256] != null) {
			System.out.println("CONFLICT @ " + i1);
		}

		itemsList[i1 + 256] = this;
	}

	public final Item setIconIndex(int i1) {
		this.iconIndex = i1;
		return this;
	}

	public final int getIconFromDamage() {
		return this.iconIndex;
	}

	public boolean onItemUse(ItemStack itemStack1, World world2, int i3, int i4, int i5, int i6) {
		return false;
	}

	public float getStrVsBlock(Block block1) {
		return 1.0F;
	}

	public ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		return itemStack1;
	}

	public final int getItemStackLimit() {
		return this.maxStackSize;
	}

	public final int getMaxDamage() {
		return this.maxDamage;
	}

	public void hitEntity(ItemStack itemStack1) {
	}

	public void onBlockDestroyed(ItemStack itemStack1) {
	}

	public int getDamageVsEntity() {
		return 1;
	}

	public boolean canHarvestBlock(Block block1) {
		return false;
	}

	static {
		ItemSpade itemSpade10000 = new ItemSpade(0, 2);
		byte b1 = 82;
		ItemSpade itemSpade0 = itemSpade10000;
		itemSpade10000.setIconIndex(b1);
		shovel = itemSpade0;
		ItemPickaxe itemPickaxe15 = new ItemPickaxe(1, 2);
		b1 = 98;
		ItemPickaxe itemPickaxe2 = itemPickaxe15;
		itemPickaxe15.setIconIndex(b1);
		pickaxeSteel = itemPickaxe2;
		ItemAxe itemAxe16 = new ItemAxe(2, 2);
		b1 = 114;
		ItemAxe itemAxe3 = itemAxe16;
		itemAxe16.setIconIndex(b1);
		axeSteel = itemAxe3;
		ItemFlintAndSteel itemFlintAndSteel17 = new ItemFlintAndSteel(3);
		b1 = 5;
		ItemFlintAndSteel itemFlintAndSteel4 = itemFlintAndSteel17;
		itemFlintAndSteel17.setIconIndex(b1);
		flintAndSteel = itemFlintAndSteel4;
		ItemFood itemFood18 = new ItemFood(4, 4);
		b1 = 10;
		ItemFood itemFood5 = itemFood18;
		itemFood18.setIconIndex(b1);
		apple = itemFood5;
		ItemBow itemBow19 = new ItemBow(5);
		b1 = 21;
		ItemBow itemBow6 = itemBow19;
		itemBow19.setIconIndex(b1);
		bow = itemBow6;
		Item item20 = new Item(6);
		b1 = 37;
		Item item7 = item20;
		item20.setIconIndex(b1);
		arrow = item7;
		item20 = new Item(7);
		b1 = 7;
		item7 = item20;
		item20.setIconIndex(b1);
		coal = item7;
		item20 = new Item(8);
		b1 = 55;
		item7 = item20;
		item20.setIconIndex(b1);
		diamod = item7;
		item20 = new Item(9);
		b1 = 23;
		item7 = item20;
		item20.setIconIndex(b1);
		ingotIron = item7;
		item20 = new Item(10);
		b1 = 39;
		item7 = item20;
		item20.setIconIndex(b1);
		ingotGold = item7;
		ItemSword itemSword21 = new ItemSword(11, 2);
		b1 = 66;
		ItemSword itemSword8 = itemSword21;
		itemSword21.setIconIndex(b1);
		swordSteel = itemSword8;
		itemSword21 = new ItemSword(12, 0);
		b1 = 64;
		itemSword8 = itemSword21;
		itemSword21.setIconIndex(b1);
		swordWood = itemSword8;
		itemSpade10000 = new ItemSpade(13, 0);
		b1 = 80;
		itemSpade0 = itemSpade10000;
		itemSpade10000.setIconIndex(b1);
		shovelWood = itemSpade0;
		itemPickaxe15 = new ItemPickaxe(14, 0);
		b1 = 96;
		itemPickaxe2 = itemPickaxe15;
		itemPickaxe15.setIconIndex(b1);
		pickaxeWood = itemPickaxe2;
		itemAxe16 = new ItemAxe(15, 0);
		b1 = 112;
		itemAxe3 = itemAxe16;
		itemAxe16.setIconIndex(b1);
		axeWood = itemAxe3;
		itemSword21 = new ItemSword(16, 1);
		b1 = 65;
		itemSword8 = itemSword21;
		itemSword21.setIconIndex(b1);
		swordStone = itemSword8;
		itemSpade10000 = new ItemSpade(17, 1);
		b1 = 81;
		itemSpade0 = itemSpade10000;
		itemSpade10000.setIconIndex(b1);
		shovelStone = itemSpade0;
		itemPickaxe15 = new ItemPickaxe(18, 1);
		b1 = 97;
		itemPickaxe2 = itemPickaxe15;
		itemPickaxe15.setIconIndex(b1);
		pickaxeStone = itemPickaxe2;
		itemAxe16 = new ItemAxe(19, 1);
		b1 = 113;
		itemAxe3 = itemAxe16;
		itemAxe16.setIconIndex(b1);
		axeStone = itemAxe3;
		itemSword21 = new ItemSword(20, 3);
		b1 = 67;
		itemSword8 = itemSword21;
		itemSword21.setIconIndex(b1);
		swordDiamond = itemSword8;
		itemSpade10000 = new ItemSpade(21, 3);
		b1 = 83;
		itemSpade0 = itemSpade10000;
		itemSpade10000.setIconIndex(b1);
		shovelDiamond = itemSpade0;
		itemPickaxe15 = new ItemPickaxe(22, 3);
		b1 = 99;
		itemPickaxe2 = itemPickaxe15;
		itemPickaxe15.setIconIndex(b1);
		pickaxeDiamond = itemPickaxe2;
		itemAxe16 = new ItemAxe(23, 3);
		b1 = 115;
		itemAxe3 = itemAxe16;
		itemAxe16.setIconIndex(b1);
		axeDiamond = itemAxe3;
		item20 = new Item(24);
		b1 = 53;
		item7 = item20;
		item20.setIconIndex(b1);
		stick = item7;
		item20 = new Item(25);
		b1 = 71;
		item7 = item20;
		item20.setIconIndex(b1);
		bowlEmpty = item7;
		ItemSoup itemSoup22 = new ItemSoup(26, 10);
		b1 = 72;
		ItemSoup itemSoup9 = itemSoup22;
		itemSoup22.setIconIndex(b1);
		bowlSoup = itemSoup9;
		itemSword21 = new ItemSword(27, 0);
		b1 = 68;
		itemSword8 = itemSword21;
		itemSword21.setIconIndex(b1);
		swordGold = itemSword8;
		itemSpade10000 = new ItemSpade(28, 0);
		b1 = 84;
		itemSpade0 = itemSpade10000;
		itemSpade10000.setIconIndex(b1);
		shovelGold = itemSpade0;
		itemPickaxe15 = new ItemPickaxe(29, 0);
		b1 = 100;
		itemPickaxe2 = itemPickaxe15;
		itemPickaxe15.setIconIndex(b1);
		pickaxeGold = itemPickaxe2;
		itemAxe16 = new ItemAxe(30, 0);
		b1 = 116;
		itemAxe3 = itemAxe16;
		itemAxe16.setIconIndex(b1);
		axeGold = itemAxe3;
		item20 = new Item(31);
		b1 = 8;
		item7 = item20;
		item20.setIconIndex(b1);
		silk = item7;
		item20 = new Item(32);
		b1 = 24;
		item7 = item20;
		item20.setIconIndex(b1);
		feather = item7;
		item20 = new Item(33);
		b1 = 40;
		item7 = item20;
		item20.setIconIndex(b1);
		gunpowder = item7;
		ItemHoe itemHoe23 = new ItemHoe(34, 0);
		short s11 = 128;
		ItemHoe itemHoe10 = itemHoe23;
		itemHoe23.setIconIndex(s11);
		hoeWood = itemHoe10;
		itemHoe23 = new ItemHoe(35, 1);
		s11 = 129;
		itemHoe10 = itemHoe23;
		itemHoe23.setIconIndex(s11);
		hoeStone = itemHoe10;
		itemHoe23 = new ItemHoe(36, 2);
		s11 = 130;
		itemHoe10 = itemHoe23;
		itemHoe23.setIconIndex(s11);
		hoeSteel = itemHoe10;
		itemHoe23 = new ItemHoe(37, 3);
		s11 = 131;
		itemHoe10 = itemHoe23;
		itemHoe23.setIconIndex(s11);
		hoeDiamond = itemHoe10;
		itemHoe23 = new ItemHoe(38, 4);
		s11 = 132;
		itemHoe10 = itemHoe23;
		itemHoe23.setIconIndex(s11);
		hoeGold = itemHoe10;
		ItemSeeds itemSeeds24 = new ItemSeeds(39, Block.crops.blockID);
		b1 = 9;
		ItemSeeds itemSeeds12 = itemSeeds24;
		itemSeeds24.setIconIndex(b1);
		seeds = itemSeeds12;
		item20 = new Item(40);
		b1 = 25;
		item7 = item20;
		item20.setIconIndex(b1);
		wheat = item7;
		itemFood18 = new ItemFood(41, 5);
		b1 = 41;
		itemFood5 = itemFood18;
		itemFood18.setIconIndex(b1);
		bread = itemFood5;
		ItemArmor itemArmor25 = new ItemArmor(42, 0, 0, 0);
		b1 = 0;
		ItemArmor itemArmor13 = itemArmor25;
		itemArmor25.setIconIndex(b1);
		helmetLeather = itemArmor13;
		itemArmor25 = new ItemArmor(43, 0, 0, 1);
		b1 = 16;
		itemArmor13 = itemArmor25;
		itemArmor25.setIconIndex(b1);
		plateLeather = itemArmor13;
		itemArmor25 = new ItemArmor(44, 0, 0, 2);
		b1 = 32;
		itemArmor13 = itemArmor25;
		itemArmor25.setIconIndex(b1);
		legsLeather = itemArmor13;
		itemArmor25 = new ItemArmor(45, 0, 0, 3);
		b1 = 48;
		itemArmor13 = itemArmor25;
		itemArmor25.setIconIndex(b1);
		bootsLeather = itemArmor13;
		itemArmor25 = new ItemArmor(46, 1, 1, 0);
		b1 = 1;
		itemArmor13 = itemArmor25;
		itemArmor25.setIconIndex(b1);
		helmetChain = itemArmor13;
		itemArmor25 = new ItemArmor(47, 1, 1, 1);
		b1 = 17;
		itemArmor13 = itemArmor25;
		itemArmor25.setIconIndex(b1);
		plateChain = itemArmor13;
		itemArmor25 = new ItemArmor(48, 1, 1, 2);
		b1 = 33;
		itemArmor13 = itemArmor25;
		itemArmor25.setIconIndex(b1);
		legsChain = itemArmor13;
		itemArmor25 = new ItemArmor(49, 1, 1, 3);
		b1 = 49;
		itemArmor13 = itemArmor25;
		itemArmor25.setIconIndex(b1);
		bootsChain = itemArmor13;
		itemArmor25 = new ItemArmor(50, 2, 2, 0);
		b1 = 2;
		itemArmor13 = itemArmor25;
		itemArmor25.setIconIndex(b1);
		helmetSteel = itemArmor13;
		itemArmor25 = new ItemArmor(51, 2, 2, 1);
		b1 = 18;
		itemArmor13 = itemArmor25;
		itemArmor25.setIconIndex(b1);
		plateSteel = itemArmor13;
		itemArmor25 = new ItemArmor(52, 2, 2, 2);
		b1 = 34;
		itemArmor13 = itemArmor25;
		itemArmor25.setIconIndex(b1);
		legsSteel = itemArmor13;
		itemArmor25 = new ItemArmor(53, 2, 2, 3);
		b1 = 50;
		itemArmor13 = itemArmor25;
		itemArmor25.setIconIndex(b1);
		bootsSteel = itemArmor13;
		itemArmor25 = new ItemArmor(54, 3, 3, 0);
		b1 = 3;
		itemArmor13 = itemArmor25;
		itemArmor25.setIconIndex(b1);
		helmetDiamond = itemArmor13;
		itemArmor25 = new ItemArmor(55, 3, 3, 1);
		b1 = 19;
		itemArmor13 = itemArmor25;
		itemArmor25.setIconIndex(b1);
		plateDiamond = itemArmor13;
		itemArmor25 = new ItemArmor(56, 3, 3, 2);
		b1 = 35;
		itemArmor13 = itemArmor25;
		itemArmor25.setIconIndex(b1);
		legsDiamond = itemArmor13;
		itemArmor25 = new ItemArmor(57, 3, 3, 3);
		b1 = 51;
		itemArmor13 = itemArmor25;
		itemArmor25.setIconIndex(b1);
		bootsDiamond = itemArmor13;
		itemArmor25 = new ItemArmor(58, 1, 4, 0);
		b1 = 4;
		itemArmor13 = itemArmor25;
		itemArmor25.setIconIndex(b1);
		helmetGold = itemArmor13;
		itemArmor25 = new ItemArmor(59, 1, 4, 1);
		b1 = 20;
		itemArmor13 = itemArmor25;
		itemArmor25.setIconIndex(b1);
		plateGold = itemArmor13;
		itemArmor25 = new ItemArmor(60, 1, 4, 2);
		b1 = 36;
		itemArmor13 = itemArmor25;
		itemArmor25.setIconIndex(b1);
		legsGold = itemArmor13;
		itemArmor25 = new ItemArmor(61, 1, 4, 3);
		b1 = 52;
		itemArmor13 = itemArmor25;
		itemArmor25.setIconIndex(b1);
		bootsGold = itemArmor13;
		item20 = new Item(62);
		b1 = 6;
		item7 = item20;
		item20.setIconIndex(b1);
		flint = item7;
		itemFood18 = new ItemFood(63, 3);
		b1 = 87;
		itemFood5 = itemFood18;
		itemFood18.setIconIndex(b1);
		porkRaw = itemFood5;
		itemFood18 = new ItemFood(64, 8);
		b1 = 88;
		itemFood5 = itemFood18;
		itemFood18.setIconIndex(b1);
		porkCooked = itemFood5;
		ItemPainting itemPainting26 = new ItemPainting(65);
		b1 = 26;
		ItemPainting itemPainting14 = itemPainting26;
		itemPainting26.setIconIndex(b1);
		painting = itemPainting14;
		itemFood18 = new ItemFood(66, 42);
		b1 = 11;
		itemFood5 = itemFood18;
		itemFood18.setIconIndex(b1);
		appleGold = itemFood5;
	}
}
