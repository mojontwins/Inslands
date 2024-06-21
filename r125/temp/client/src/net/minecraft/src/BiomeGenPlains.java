package net.minecraft.src;

public class BiomeGenPlains extends BiomeGenBase {
	protected BiomeGenPlains(int i1) {
		super(i1);
		this.biomeDecorator.treesPerChunk = -999;
		this.biomeDecorator.flowersPerChunk = 4;
		this.biomeDecorator.grassPerChunk = 10;
	}
}
