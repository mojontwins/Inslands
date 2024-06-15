package net.minecraft.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class AetherNBT {
	public static void save(World world) {
		try {
			File e = GetWorldSaveLocation(world);
			File f2 = new File(e, "aether.dat");
			if(!f2.exists()) {
				CompressedStreamTools.writeGzippedCompoundToOutputStream(new NBTTagCompound(), new FileOutputStream(f2));
			}

			NBTTagCompound compound = CompressedStreamTools.func_1138_a(new FileInputStream(f2));
			compound.setBoolean("LoreOverworld", mod_Aether.hasLoreOverworld);
			compound.setBoolean("LoreNether", mod_Aether.hasLoreNether);
			compound.setBoolean("LoreAether", mod_Aether.hasLoreAether);
			CompressedStreamTools.writeGzippedCompoundToOutputStream(compound, new FileOutputStream(f2));
		} catch (Exception exception4) {
			exception4.printStackTrace();
		}

	}

	public static void load(World world) {
		try {
			File e = GetWorldSaveLocation(world);
			File f2 = new File(e, "aether.dat");
			if(!f2.exists()) {
				CompressedStreamTools.writeGzippedCompoundToOutputStream(new NBTTagCompound(), new FileOutputStream(f2));
			}

			NBTTagCompound compound = CompressedStreamTools.func_1138_a(new FileInputStream(f2));
			mod_Aether.hasLoreOverworld = compound.getBoolean("LoreOverworld");
			mod_Aether.hasLoreNether = compound.getBoolean("LoreNether");
			mod_Aether.hasLoreAether = compound.getBoolean("LoreAether");
		} catch (Exception exception4) {
			exception4.printStackTrace();
		}

	}

	public static File GetWorldSaveLocation(World world) {
		return world.saveHandler instanceof SaveHandler ? ((SaveHandler)world.saveHandler).getSaveDirectory() : null;
	}
}
