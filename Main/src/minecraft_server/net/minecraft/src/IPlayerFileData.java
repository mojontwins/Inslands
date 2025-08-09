package net.minecraft.src;

import net.minecraft.world.entity.player.EntityPlayer;

public interface IPlayerFileData {
	void writePlayerData(EntityPlayer entityPlayer1);

	void readPlayerData(EntityPlayer entityPlayer1);
}
