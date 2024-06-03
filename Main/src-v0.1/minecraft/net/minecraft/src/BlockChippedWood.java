package net.minecraft.src;

import java.util.Random;

public class BlockChippedWood extends BlockLog {
	protected BlockChippedWood(int id, int blockIndex) {
		super(id);
		this.blockIndexInTexture = blockIndex;
	}
	
    public int getRenderType() {
    	return 105;
    }
    
    public boolean isOpaqueCube() {
        return false;
    }
    
	public int idDropped(int i1, Random random2) {
		return Block.chippedWood.blockID;
	} 
	
}
