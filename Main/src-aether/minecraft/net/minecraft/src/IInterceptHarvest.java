package net.minecraft.src;

public interface IInterceptHarvest {
	boolean canIntercept(World world1, EntityPlayer entityPlayer2, Loc loc3, int i4, int i5);

	void intercept(World world1, EntityPlayer entityPlayer2, Loc loc3, int i4, int i5);
}
