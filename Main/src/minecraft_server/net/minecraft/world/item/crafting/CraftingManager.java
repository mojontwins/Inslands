package net.minecraft.world.item.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.minecraft.world.inventory.InventoryCrafting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockState;
import net.minecraft.world.level.tile.Block;

public class CraftingManager {
	private static final CraftingManager instance = new CraftingManager();
	private List<IRecipe> recipes = new ArrayList<IRecipe>();

	public static final CraftingManager getInstance() {
		return instance;
	}

	private CraftingManager() {
		(new RecipesTools()).addRecipes(this);
		(new RecipesWeapons()).addRecipes(this);
		(new RecipesIngots()).addRecipes(this);
		(new RecipesFood()).addRecipes(this);
		(new RecipesCrafting()).addRecipes(this);
		(new RecipesArmor()).addRecipes(this);
		(new RecipesDyes()).addRecipes(this);
		this.addRecipe(new ItemStack(Item.paper, 3), new Object[]{"###", '#', Item.reed});
		this.addRecipe(new ItemStack(Item.book, 1), new Object[]{"#", "#", "#", '#', Item.paper});
		this.addRecipe(new ItemStack(Block.fence, 2), new Object[]{"###", "###", '#', Item.stick});
		this.addRecipe(new ItemStack(Block.jukebox, 1), new Object[]{"###", "#X#", "###", '#', Block.planks, 'X', Item.diamond});
		this.addRecipe(new ItemStack(Block.musicBlock, 1), new Object[]{"###", "#X#", "###", '#', Block.planks, 'X', Item.redstone});
		this.addRecipe(new ItemStack(Block.bookShelf, 1), new Object[]{"###", "XXX", "###", '#', Block.planks, 'X', Item.book});
		this.addRecipe(new ItemStack(Block.blockSnow, 1), new Object[]{"##", "##", '#', Item.snowball});
		this.addRecipe(new ItemStack(Block.blockClay, 1), new Object[]{"##", "##", '#', Item.clay});
		this.addRecipe(new ItemStack(Block.brick, 1), new Object[]{"##", "##", '#', Item.brick});
		this.addRecipe(new ItemStack(Block.glowStone, 1), new Object[]{"##", "##", '#', Item.lightStoneDust});
		this.addRecipe(new ItemStack(Block.cloth, 1), new Object[]{"##", "##", '#', Item.silk});
		this.addRecipe(new ItemStack(Block.tnt, 1), new Object[]{"X#X", "#X#", "X#X", 'X', Item.gunpowder, '#', Block.sand});
		this.addRecipe(new ItemStack(Block.stairSingle, 6, 3), new Object[]{"###", '#', Block.cobblestone});
		this.addRecipe(new ItemStack(Block.stairSingle, 6, 0), new Object[]{"###", '#', Block.stone});
		this.addRecipe(new ItemStack(Block.stairSingle, 6, 1), new Object[]{"###", '#', Block.sandStone});
		this.addRecipe(new ItemStack(Block.stairSingle, 6, 2), new Object[]{"###", '#', Block.planks});
		this.addRecipe(new ItemStack(Block.ladder, 2), new Object[]{"# #", "###", "# #", '#', Item.stick});
		this.addRecipe(new ItemStack(Item.doorWood, 1), new Object[]{"##", "##", "##", '#', Block.planks});
		
		// Softlocked for b1.5
		this.addRecipe(new ItemStack(Block.trapdoor, 2), new Object[]{"###", "###", '#', Block.planks});
		
		this.addRecipe(new ItemStack(Item.doorSteel, 1), new Object[]{"##", "##", "##", '#', Item.ingotIron});
		this.addRecipe(new ItemStack(Item.sign, 1), new Object[]{"###", "###", " X ", '#', Block.planks, 'X', Item.stick});
		this.addRecipe(new ItemStack(Item.cake, 1), new Object[]{"AAA", "BEB", "CCC", 'A', Item.bucketMilk, 'B', Item.sugar, 'C', Item.wheat, 'E', Item.egg});
		this.addRecipe(new ItemStack(Item.sugar, 1), new Object[]{"#", '#', Item.reed});
		this.addRecipe(new ItemStack(Block.planks, 4), new Object[]{"#", '#', Block.wood});
		this.addRecipe(new ItemStack(Item.stick, 4), new Object[]{"#", "#", '#', Block.planks});
		this.addRecipe(new ItemStack(Block.torchWood, 4), new Object[]{"X", "#", 'X', Item.coal, '#', Item.stick});
		this.addRecipe(new ItemStack(Item.bowlEmpty, 4), new Object[]{"# #", " # ", '#', Block.planks});
		this.addRecipe(new ItemStack(Block.rail, 16), new Object[]{"X X", "X#X", "X X", 'X', Item.ingotIron, '#', Item.stick});
		
		// Softlocked for b1.4
		this.addRecipe(new ItemStack(Block.railPowered, 6), new Object[]{"X X", "X#X", "XRX", 'X', Item.ingotGold, 'R', Item.redstone, '#', Item.stick});
		this.addRecipe(new ItemStack(Block.railDetector, 6), new Object[]{"X X", "X#X", "XRX", 'X', Item.ingotIron, 'R', Item.redstone, '#', Block.pressurePlateStone});
		
		this.addRecipe(new ItemStack(Item.minecartEmpty, 1), new Object[]{"# #", "###", '#', Item.ingotIron});
		this.addRecipe(new ItemStack(Block.pumpkinLantern, 1), new Object[]{"A", "B", 'A', Block.pumpkin, 'B', Block.torchWood});
		this.addRecipe(new ItemStack(Item.minecartCrate, 1), new Object[]{"A", "B", 'A', Block.chest, 'B', Item.minecartEmpty});
		this.addRecipe(new ItemStack(Item.minecartPowered, 1), new Object[]{"A", "B", 'A', Block.stoneOvenIdle, 'B', Item.minecartEmpty});
		this.addRecipe(new ItemStack(Item.boat, 1), new Object[]{"# #", "###", '#', Block.planks});
		this.addRecipe(new ItemStack(Item.bucketEmpty, 1), new Object[]{"# #", " # ", '#', Item.ingotIron});
		this.addRecipe(new ItemStack(Item.flintAndSteel, 1), new Object[]{"A ", " B", 'A', Item.ingotIron, 'B', Item.flint});
		this.addRecipe(new ItemStack(Item.bread, 1), new Object[]{"###", '#', Item.wheat});
		this.addRecipe(new ItemStack(Block.stairCompactPlanks, 4), new Object[]{"#  ", "## ", "###", '#', Block.planks});
		this.addRecipe(new ItemStack(Item.fishingRod, 1), new Object[]{"  #", " #X", "# X", '#', Item.stick, 'X', Item.silk});
		this.addRecipe(new ItemStack(Block.stairCompactCobblestone, 4), new Object[]{"#  ", "## ", "###", '#', Block.cobblestone});
		this.addRecipe(new ItemStack(Item.painting, 1), new Object[]{"###", "#X#", "###", '#', Item.stick, 'X', Block.cloth});
		this.addRecipe(new ItemStack(Item.appleGold, 1), new Object[]{"###", "#X#", "###", '#', Block.blockGold, 'X', Item.appleRed});
		this.addRecipe(new ItemStack(Block.lever, 1), new Object[]{"X", "#", '#', Block.cobblestone, 'X', Item.stick});
		this.addRecipe(new ItemStack(Block.torchRedstoneActive, 1), new Object[]{"X", "#", '#', Item.stick, 'X', Item.redstone});
		this.addRecipe(new ItemStack(Item.redstoneRepeater, 1), new Object[]{"#X#", "III", '#', Block.torchRedstoneActive, 'X', Item.redstone, 'I', Block.stone});
		this.addRecipe(new ItemStack(Item.pocketSundial, 1), new Object[]{" # ", "#X#", " # ", '#', Item.ingotGold, 'X', Item.redstone});
		this.addRecipe(new ItemStack(Item.compass, 1), new Object[]{" # ", "#X#", " # ", '#', Item.ingotIron, 'X', Item.redstone});
		
		// Softlocked for b1.5
		//this.addRecipe(new ItemStack(Item.mapItem, 1), new Object[]{"###", "#X#", "###", '#', Item.paper, 'X', Item.compass});
		
		this.addRecipe(new ItemStack(Block.button, 1), new Object[]{"#", "#", '#', Block.stone});
		this.addRecipe(new ItemStack(Block.pressurePlateStone, 1), new Object[]{"##", '#', Block.stone});
		this.addRecipe(new ItemStack(Block.pressurePlatePlanks, 1), new Object[]{"##", '#', Block.planks});
		this.addRecipe(new ItemStack(Block.dispenser, 1), new Object[]{"###", "#X#", "#R#", '#', Block.cobblestone, 'X', Item.bow, 'R', Item.redstone});
		
		// Softlocked for b1.6.6
		/*
		this.addRecipe(new ItemStack(Block.pistonBase, 1), new Object[]{"TTT", "#X#", "#R#", '#', Block.cobblestone, 'X', Item.ingotIron, 'R', Item.redstone, 'T', Block.planks});
		this.addRecipe(new ItemStack(Block.pistonStickyBase, 1), new Object[]{"S", "P", 'S', Item.slimeBall, 'P', Block.pistonBase});
		*/
		
		// Softlocked for b1.2
		//this.addRecipe(new ItemStack(Item.bed, 1), new Object[]{"###", "XXX", '#', Block.cloth, 'X', Block.planks});
		
		// Custom
		(new RecipesMine()).addRecipes(this);
		(new RecipesNether()).addRecipes(this);
		RecipesPoisonIsland.addRecipes(this);
		
		Collections.sort(this.recipes, new RecipeSorter(this));
		System.out.println(this.recipes.size() + " recipes");
	}

	public void addRecipe(ItemStack resultStack, Object... objV) {
		String recipeString = "";
		int idx = 0;
		int columns = 0;
		int lines = 0;

		if(objV[idx] instanceof String[]) {
			String[] recipeLines = (String[])((String[])objV[idx++]);

			for(int i = 0; i < recipeLines.length; ++i) {
				String recipeLine = recipeLines[i];
				++lines;
				columns = recipeLine.length();
				recipeString = recipeString + recipeLine;
			}

		} else {
			while(objV[idx] instanceof String) {
				String recipeLine = (String)objV[idx++];
				++lines;
				columns = recipeLine.length();
				recipeString = recipeString + recipeLine;
			}

		}

		HashMap<Character,ItemStack> recipeMap = new HashMap<Character,ItemStack>();
		for(; idx < objV.length; idx += 2) {
			Character c = (Character)objV[idx];
			ItemStack stack = null;
			if(objV[idx + 1] instanceof Item) {
				stack = new ItemStack((Item)objV[idx + 1]);
			} else if(objV[idx + 1] instanceof Block) {
				stack = new ItemStack((Block)objV[idx + 1], 1, -1);
			} else if(objV[idx + 1] instanceof ItemStack) {
				stack = (ItemStack)objV[idx + 1];
			} else if(objV[idx + 1] instanceof BlockState) {
				BlockState bs = (BlockState)objV[idx + 1];
				stack = new ItemStack(bs.getBlock().blockID, bs.getMetadata());
			}

			recipeMap.put(c, stack);
		}

		ItemStack[] stacks = new ItemStack[columns * lines];

		for(int i = 0; i < columns * lines; ++i) {
			char c = recipeString.charAt(i);
			if(recipeMap.containsKey(c)) {
				stacks[i] = ((ItemStack)recipeMap.get(c)).copy();
			} else {
				stacks[i] = null;
			}
		}

		this.recipes.add(new ShapedRecipes(columns, lines, stacks, resultStack));
	}

	void addShapelessRecipe(ItemStack resultStack, Object... objV) {
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
		Object[] objArr = objV;
		int numObjs = objV.length;

		for(int i = 0; i < numObjs; ++i) {
			Object obj = objArr[i];
			if(obj instanceof ItemStack) {
				stacks.add(((ItemStack)obj).copy());
			} else if(obj instanceof Item) {
				stacks.add(new ItemStack((Item)obj));
			} else if(obj instanceof BlockState) {
				BlockState bs = (BlockState)obj;
				stacks.add(new ItemStack(bs.getBlock().blockID, bs.getMetadata()));
			} else {
				if(!(obj instanceof Block)) {
					throw new RuntimeException("Invalid shapeless recipy!");
				}

				stacks.add(new ItemStack((Block)obj));
			}
		}

		this.recipes.add(new ShapelessRecipes(resultStack, stacks));
	}

	public ItemStack findMatchingRecipe(InventoryCrafting inventoryCrafting1) {
		for(int i2 = 0; i2 < this.recipes.size(); ++i2) {
			IRecipe iRecipe3 = (IRecipe)this.recipes.get(i2);
			if(iRecipe3.matches(inventoryCrafting1)) {
				return iRecipe3.getCraftingResult(inventoryCrafting1);
			}
		}

		return null;
	}

	public List<IRecipe> getRecipeList() {
		return this.recipes;
	}
}
