package net.minecraft.src;

public interface IInterceptBlockSet {
	boolean canIntercept(World world1, Loc loc2, int i3);

	int intercept(World world1, Loc loc2, int i3);
}
