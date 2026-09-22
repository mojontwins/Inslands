package net.minecraft.world.level;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.mojang.nbt.CompressedStreamTools;
import com.mojang.nbt.NBTTagCompound;

import net.minecraft.world.entity.player.EntityPlayer;

/**
 * Single-player player-data persistence. The canonical snapshot of the player
 * (inventory, stats, XP, health, exact position/rotation, CurrentWorldId,
 * Dimension) lives in a single player.dat in the save root folder, separate
 * from every level.dat. Written on every save of the active world (autosave
 * and explicit), read once when a world boots.
 *
 * The snapshot is intended to be consumed exactly once per session: read()
 * sets the consumed flag even when the file is missing or unreadable, so a
 * respawn (which re-enters spawnPlayerWithLoadedChunks after death) never
 * re-hydrates from a stale snapshot. Session flag semantics assumed by
 * Minecraft.startWorld().
 */
public class PlayerSaveData {
	private static boolean loadedThisSession = false;

	/**
	 * Resets the once-per-session consume flag. Called before any world is
	 * built at startup, so a fresh session is eligible for hydration again.
	 */
	public static void startSession() {
		loadedThisSession = false;
	}

	/**
	 * Marks the snapshot consumed for the rest of the session without reading
	 * it. Used at startup when player.dat references a world that no longer
	 * exists: the boot falls back to world 0 but must not later re-hydrate.
	 */
	public static void markConsumed() {
		loadedThisSession = true;
	}

	public static File getFile(File baseDir) {
		if(baseDir == null) return null;
		return new File(baseDir, "player.dat");
	}

	public static File getFile(World world) {
		if(world == null) return null;
		File baseDir = PortalRegistry.getBaseSaveDirectory(world);
		return getFile(baseDir);
	}

	/**
	 * Raw read of player.dat (no consume semantics). Falls back to
	 * player.dat_old when player.dat is missing. Returns null when the file
	 * does not exist or cannot be read.
	 */
	public static NBTTagCompound readTag(File baseDir) {
		if(baseDir == null) return null;
		File levelFile = getFile(baseDir);
		if(!levelFile.isFile()) {
			levelFile = new File(baseDir, "player.dat_old");
		}
		if(!levelFile.isFile()) {
			return null;
		}
		try {
			return CompressedStreamTools.readCompressed(new FileInputStream(levelFile));
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Consuming read for World.spawnPlayerWithLoadedChunks. Marks the snapshot
	 * consumed whether or not a file exists, so a later respawn in the same
	 * session does not re-hydrate the player. Returns null when consumed,
	 * missing, or the world's base directory is unavailable.
	 */
	public static NBTTagCompound read(World world) {
		if(loadedThisSession) {
			return null;
		}
		loadedThisSession = true;
		File base = PortalRegistry.getBaseSaveDirectory(world);
		if(base == null) {
			return null;
		}
		return readTag(base);
	}

	/**
	 * Writes the live player state to the save root's player.dat, atomically
	 * via player.dat_new/player.dat_old rotation (mirroring
	 * SaveHandler.writeLevelData). Written straight to the file: going through
	 * ISaveFormat getSaveLoader() would construct a second SaveHandler for the
	 * base folder, overwrite its session.lock and trip the running world's
	 * checkSessionLock ("Level save conflict").
	 */
	public static void write(World world, EntityPlayer player) {
		try {
			File baseDir = PortalRegistry.getBaseSaveDirectory(world);
			if(baseDir == null || !baseDir.exists()) {
				return;
			}
			File levelFile = getFile(baseDir);
			File newFile = new File(baseDir, "player.dat_new");
			File oldFile = new File(baseDir, "player.dat_old");

			NBTTagCompound playerTag = new NBTTagCompound();
			player.writeToNBT(playerTag);

			CompressedStreamTools.writeCompressed(playerTag, new FileOutputStream(newFile));
			if(oldFile.exists()) oldFile.delete();
			if(levelFile.exists()) levelFile.renameTo(oldFile);
			newFile.renameTo(levelFile);
			if(newFile.exists()) newFile.delete();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}