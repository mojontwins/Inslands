package net.minecraft.src;

public interface IPlayerFileData {
	void writePlayerData(EntityPlayer entityPlayer1);

	void readPlayerData(EntityPlayer entityPlayer1);

	String[] func_52007_g();
}
