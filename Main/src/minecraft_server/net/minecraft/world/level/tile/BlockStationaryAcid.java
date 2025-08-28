package net.minecraft.world.level.tile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.EntityBoat;
import net.minecraft.world.level.World;
import net.minecraft.world.level.material.Material;

public class BlockStationaryAcid extends BlockStationary {

	public BlockStationaryAcid(int id, int blockIndexInTexture) {
		super(id, Material.acid);
		this.blockIndexInTexture = blockIndexInTexture;
		this.setTickOnLoad(true);
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	public int getBlockTextureFromSide(int side) {
		return this.blockIndexInTexture;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if(entity.ridingEntity instanceof EntityBoat) return;
		
		entity.attackEntityFrom((Entity)null, 1);
	}
	
}
