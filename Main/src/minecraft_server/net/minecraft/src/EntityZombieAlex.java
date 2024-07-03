package net.minecraft.src;

public class EntityZombieAlex extends EntityZombie implements IMob {
	
	public EntityZombieAlex(World world) {
		super(world);
		this.texture = "/mob/zombie_alex1.png";
		this.texturePrefix = "zombie_alex";
		this.moveSpeed = 0.8F;
		this.attackStrength = 3;
		this.scoreValue = 20;
	}
	
	protected int getMaxTextureVariations() {
		return 3;
	}
	
	protected int getDropItemId() {
		switch(this.rand.nextInt(15)) {
			case 0:
			case 1:
			case 2: return Item.rottenFlesh.shiftedIndex;
			case 3: 
			case 4:
			case 5: return Item.feather.shiftedIndex;
			case 6: return Item.appleGold.shiftedIndex;
			default: return Item.appleRed.shiftedIndex;
		}
	}
}
