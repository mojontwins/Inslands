package net.minecraft.client.gui;

import java.util.Random;

public class GameHints {
	public static Random rand = new Random(System.currentTimeMillis());
	
	public static final String[] hints = new String[] {
		"Walk better on snow with leather boots!",
		"Some trees drop apples!",
		"Golden tools do something special!",
		"Some skeletons drop vaulable acorn seeds!",
		"Plant acorn seeds on tilled field!",
		"No water in hell, use bonemeal on crops",
		"Grow acorn sees into saplings!",
		"Wood is valuable in hell, saplings are treasure!",
		"Pure dirt can eventually grow grass with tons of bonemeal",
		"Animals will only spawn on grass...",
		"Fossils are the best source of bonemeal",
		"Put big mushroom blocks in the furnace to get charcoal",
		"Charcoal is cheap but weaker than mineral coal",
		"Make an iron boat to sail the lava ocean",
		"Labyrinths in the forest are dangerous but rewarding",
		"Ride a cow in heaven!",
		"The boss in the labyrinth drops a very valuable tool",
		"Caves in the forest are full of dangerous creatures",
		"It's always twilight in the forest, so be careful",
		"It's always noon in paradise, but caves are still dark!",
		"Turn off 'level checks' if you just wanna play in a sandbox",
		"Fungal Calamity spawn near huge mushrooms in the forest",
		"Winners never kill creepers!",
		"You can travel to the Nether...",
		"Craft a glass bottle using glass panes",
		"Glass bottles can be filled with certain liquids",
		"Give diamonds to the witch to get a cauldron",
		"Fill the cauldron with water to start cooking",
		"Experiment with mushrooms in the cauldron",
		"You can fill a bottle with stuff you brew in the cauldron",
		"Soup from a cauldron can be drink using a bottle",
		"Most bottles can be thrown to mobs to cause damage",
		"Acid burns but can be useful in a bottle",
		"A strong disolvent in the cauldron with the right mushrooms...",
		"Coral can only be replanted in water",
		"Copper is used to create the diving helmet",
		"Explore the oceans with a diving helmet!",
		"Skeletons with red eyes may drop their head",
		"A skeleton head on two diamond blocks summons a boss.",
		"Diamond Skeletons only take damage from bottles of poison.",
		"Black goo is not useless! Fill a bottle and throw it!",
		"Right click cauldron with bottle to interact with it."
		
	};
	
	public static String getRandomHint() {
		return hints[rand.nextInt(hints.length)];
	}
}
