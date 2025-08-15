package net.minecraft.world.item;

import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.World;
import net.minecraft.world.level.chunk.ChunkCoordinates;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.stats.AchievementList;

public class ItemCryingObsidianWand extends Item {

	public ItemCryingObsidianWand(int i1) {
		super(i1);
		
		this.displayOnCreativeTab = CreativeTabs.tabTools;
	}
	
	public ItemStack onItemRightClick(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		ChunkCoordinates spawnCoordinates = entityPlayer3.getPlayerSpawnCoordinate();
		if(spawnCoordinates == null) spawnCoordinates = entityPlayer3.worldObj.getSpawnPoint();
		entityPlayer3.setPosition(spawnCoordinates.posX, spawnCoordinates.posY, spawnCoordinates.posZ);
		entityPlayer3.preparePlayerToSpawn();
		World world = entityPlayer3.worldObj;
		for(int i = 0; i < 7; ++i) {
			world.spawnParticle("largesmoke", 
				(double)entityPlayer3.posX + world.rand.nextDouble (), 
				(double)entityPlayer3.posY + world.rand.nextDouble (),
				(double)entityPlayer3.posZ + world.rand.nextDouble (),
				0.0, 0.0, 0.0);
		}
		
		world.playSoundEffect((double)spawnCoordinates.posX, (double)spawnCoordinates.posY, (double)spawnCoordinates.posZ, "random.drr", 1.0F, 0.25F);
		world.playSoundEffect((double)spawnCoordinates.posX, (double)spawnCoordinates.posY, (double)spawnCoordinates.posZ, "random.breath", 0.5F, 0.15F);
		
		entityPlayer3.triggerAchievement(AchievementList.usedCryingObsidianWand);
		
		return itemStack1;
	}

}
