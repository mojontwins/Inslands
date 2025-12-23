package net.minecraft.world.level.tile;

import net.minecraft.world.entity.EntityTemporarySeat;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.World;
import net.minecraft.world.level.material.Material;

public class BlockSeat extends Block {
	public boolean debug = true;

	public BlockSeat(int id, Material material) {
		super(id, material);
	}

	public BlockSeat(int id, int tex, Material material) {
		super(id, tex, material);
	}

	// Make the entityPlayer seat on this
	public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer) {
		
		// Check if we are already sitting on this block
		if(entityPlayer.ridingEntity != null && entityPlayer.ridingEntity instanceof EntityTemporarySeat) {
			EntityTemporarySeat currentTs = (EntityTemporarySeat)entityPlayer.ridingEntity;
			if(currentTs.at(x, y, z)) {
				if(this.debug) System.out.println ("Already sitting @ " + x + " " + y + " " + z);		
				entityPlayer.mountEntity(null);
				currentTs.setEntityDead();
				return true;
				
			} else {
				// Wants to sit on another tile, so we have to dismount this first.
				if(this.debug) System.out.println ("Changing seat to " + x + " " + y + " " + z);		
				entityPlayer.mountEntity(null);
				currentTs.setEntityDead();
			}
		}
		
		// Create a temporary entity
		EntityTemporarySeat ts = new EntityTemporarySeat(world);
		ts.setLocationAndAngles(x + .5D, y, z + .5D, 0, 0);
		ts.setTilePos(x, y, z);
		world.spawnEntityInWorld(ts);
		entityPlayer.mountEntity(ts);
		
		System.out.println ("Player mounted " + ts);
		
		return true;
	}
}
