package com.mojontwins.minecraft.poisonisland;

import net.minecraft.src.Block;
import net.minecraft.src.EntitySkeleton;
import net.minecraft.src.Item;
import net.minecraft.src.World;

public class EntityPoisonSkeleton extends EntitySkeleton {

	public EntityPoisonSkeleton(World var1) {
		super(var1);
		this.texture = "/mob/poison_skeleton.png";
		this.scoreValue = 20;
	}

	@Override
	protected void dropFewItems() {
		if (rand.nextInt(16) == 0) {
			this.dropItem(Block.skeletonHead.blockID, 1);
		} else if(rand.nextInt(16) == 0) {
			this.dropItem(Item.bow.shiftedIndex, 1);
		} else {
			
			int i1 = this.rand.nextInt(3);
	
			int i2;
			for(i2 = 0; i2 < i1; ++i2) {
				this.dropItem(Item.arrow.shiftedIndex, 1);
			}
	
			i1 = this.rand.nextInt(3);
	
			for(i2 = 0; i2 < i1; ++i2) {
				this.dropItem(Item.bone.shiftedIndex, 1);
			}
		}
	}
}
