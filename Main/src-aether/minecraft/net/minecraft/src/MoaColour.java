package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MoaColour {
	public int ID;
	public int colour;
	public int jumps;
	public int chance;
	public String name;
	private static int totalChance;
	public static List colours = new ArrayList();
	private static Random random = new Random();

	public MoaColour(int i, int j, int k, int l, String s) {
		this.ID = i;
		this.colour = j;
		this.jumps = k;
		this.chance = l;
		totalChance += l;
		this.name = s;
		colours.add(this);
	}

	public String getTexture(boolean saddled) {
		return "/aether/mobs/" + this.name + (saddled ? "MoaSaddle.png" : "Moa.png");
	}

	public static MoaColour pickRandomMoa() {
		int i = random.nextInt(totalChance);

		for(int j = 0; j < colours.size(); ++j) {
			if(i < ((MoaColour)colours.get(j)).chance) {
				return (MoaColour)colours.get(j);
			}

			i -= ((MoaColour)colours.get(j)).chance;
		}

		return (MoaColour)colours.get(0);
	}

	public static MoaColour getColour(int ID) {
		for(int i = 0; i < colours.size(); ++i) {
			if(((MoaColour)colours.get(i)).ID == ID) {
				return (MoaColour)colours.get(i);
			}
		}

		return (MoaColour)colours.get(0);
	}

	static {
		new MoaColour(0, 7829503, 3, 100, "Blue");
		new MoaColour(1, 2236962, 8, 5, "Black");
		new MoaColour(2, 0xFFFFFF, 4, 20, "White");
	}
}
