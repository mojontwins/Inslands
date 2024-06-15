package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class ACPage {
	private static int nextID = 1;
	final int id;
	public final String title;
	ArrayList list = new ArrayList();

	public ACPage() {
		this.id = 0;
		this.title = "Minecraft";
		SAPI.acPageAdd(this);
	}

	public ACPage(String title) {
		this.id = nextID++;
		this.title = title;
		SAPI.acPageAdd(this);
	}

	public void addAchievements(Achievement... achievements) {
		Achievement[] achievement5 = achievements;
		int i4 = achievements.length;

		for(int i3 = 0; i3 < i4; ++i3) {
			Achievement achievement = achievement5[i3];
			this.list.add(achievement.statId);
		}

	}

	public int bgGetSprite(Random random, int x, int y) {
		int sprite = Block.sand.blockIndexInTexture;
		int rnd = random.nextInt(1 + y) + y / 2;
		if(rnd <= 37 && y != 35) {
			if(rnd == 22) {
				sprite = random.nextInt(2) == 0 ? Block.oreDiamond.blockIndexInTexture : Block.oreRedstone.blockIndexInTexture;
			} else if(rnd == 10) {
				sprite = Block.oreIron.blockIndexInTexture;
			} else if(rnd == 8) {
				sprite = Block.oreCoal.blockIndexInTexture;
			} else if(rnd > 4) {
				sprite = Block.stone.blockIndexInTexture;
			} else if(rnd > 0) {
				sprite = Block.dirt.blockIndexInTexture;
			}
		} else {
			sprite = Block.bedrock.blockIndexInTexture;
		}

		return sprite;
	}
}
