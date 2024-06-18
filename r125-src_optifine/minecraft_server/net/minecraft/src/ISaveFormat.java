package net.minecraft.src;

public interface ISaveFormat {
	boolean isOldMapFormat(String string1);

	boolean convertMapFormat(String string1, IProgressUpdate iProgressUpdate2);
}
