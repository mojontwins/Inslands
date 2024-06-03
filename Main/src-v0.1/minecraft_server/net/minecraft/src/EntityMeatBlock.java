package net.minecraft.src;

public class EntityMeatBlock extends EntityBlockEntity {
	public EntityMeatBlock(World world) {
		super(world);
		this.texture = "/mob/invisible.png";
	}

	public void onUpdate() {
	}
	
	public boolean attackEntityFrom(Entity entity, int damage) {
		return false;
	}
}
