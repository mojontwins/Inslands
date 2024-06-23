package net.minecraft.src;

public class ItemTallGrass extends ItemBlock {

	public ItemTallGrass(int i1) {
		super(i1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public int getIconFromDamage(int i1) {
		return Block.tallGrass.getBlockTextureFromSideAndMetadata(2, i1);
	}
	
	@Override
	public int getPlacedBlockMetadata(int i1) {
		return i1;
	}
	
	@Override
	public int getColorFromDamage(int i1) {
		return BlockTallGrass.tallGrassColor[(i1 >> 4) & 7];
		
	}
}
