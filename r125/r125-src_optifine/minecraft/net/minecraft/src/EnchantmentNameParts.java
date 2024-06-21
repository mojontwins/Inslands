package net.minecraft.src;

import java.util.Random;

public class EnchantmentNameParts {
	public static final EnchantmentNameParts instance = new EnchantmentNameParts();
	private Random rand = new Random();
	private String[] wordList = "the elder scrolls klaatu berata niktu xyzzy bless curse light darkness fire air earth water hot dry cold wet ignite snuff embiggen twist shorten stretch fiddle destroy imbue galvanize enchant free limited range of towards inside sphere cube self other ball mental physical grow shrink demon elemental spirit animal creature beast humanoid undead fresh stale ".split(" ");

	public String generateRandomEnchantName() {
		int i1 = this.rand.nextInt(2) + 3;
		String string2 = "";

		for(int i3 = 0; i3 < i1; ++i3) {
			if(i3 > 0) {
				string2 = string2 + " ";
			}

			string2 = string2 + this.wordList[this.rand.nextInt(this.wordList.length)];
		}

		return string2;
	}

	public void setRandSeed(long j1) {
		this.rand.setSeed(j1);
	}
}
