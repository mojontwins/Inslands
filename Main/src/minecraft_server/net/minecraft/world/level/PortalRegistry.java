package net.minecraft.world.level;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mojang.nbt.CompressedStreamTools;
import com.mojang.nbt.NBTTagCompound;

import net.minecraft.world.level.chunk.storage.ISaveHandler;
import net.minecraft.world.level.chunk.storage.SaveHandler;

/**
 * Portal linking and linked-world identity. Allocates permanent world ids
 * (2..255), persists the counter in portals.dat, resolves the base save folder
 * and main seed for any world (including linked worlds and the shared nether),
 * and picks random linked worlds for portal travel.
 */
public class PortalRegistry {
	public static final int FIRST_WORLD_ID = 2;
	public static final int LAST_WORLD_ID = 255;

	private final File saveDirectory;
	private int nextId;

	public PortalRegistry(File saveDirectory) {
		this.saveDirectory = saveDirectory;
		this.nextId = FIRST_WORLD_ID;
		if(this.saveDirectory != null) {
			this.load();
		}
	}

	public int getNextId() {
		return this.nextId;
	}

	public boolean isFull() {
		return this.nextId > LAST_WORLD_ID;
	}

	/**
	 * Allocates the next sequential linked-world id (2..255). World ids are
	 * permanent and never freed, so once the table is full this returns -1 and
	 * the caller links to an existing world instead.
	 */
	public int allocateWorldId() {
		if(this.nextId > LAST_WORLD_ID) {
			return -1;
		}
		int id = this.nextId;
		this.nextId ++;
		this.save();
		return id;
	}

	/**
	 * Returns a random existing linked world id (2..255) with the given frozen
	 * theme, or any existing world if none matches the theme (white-wool
	 * behaviour). Returns -1 if no linked world exists at all.
	 */
	public int getRandomExistingWorldId(int themeId) {
		List<Integer> any = new ArrayList<Integer>();
		List<Integer> matching = new ArrayList<Integer>();
		for(int id = FIRST_WORLD_ID; id <= LAST_WORLD_ID; id ++) {
			File dir = new File(this.saveDirectory, "DIM-" + id);
			if(!dir.isDirectory()) continue;
			File levelFile = new File(dir, "level.dat");
			if(!levelFile.exists()) continue;
			any.add(id);
			if(themeId >= 0) {
				try {
					NBTTagCompound nbt = CompressedStreamTools.readCompressed(new FileInputStream(levelFile));
					int frozenTheme = nbt.getCompoundTag("Data").getInteger("ThemeId");
					if(frozenTheme == themeId) {
						matching.add(id);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		List<Integer> pool = matching.isEmpty() ? any : matching;
		if(pool.isEmpty()) return -1;
		return pool.get((new Random()).nextInt(pool.size()));
	}

	public static boolean isLinkedWorldId(int worldId) {
		return worldId >= FIRST_WORLD_ID && worldId <= LAST_WORLD_ID;
	}

	/**
	 * The save-root folder of the given world (the folder holding level.dat for
	 * world 0). For linked worlds this is the parent of their DIM-<id> folder.
	 * Returns null for non-standard save handlers (e.g. multiplayer).
	 */
	public static File getBaseSaveDirectory(World world) {
		if(world == null) return null;
		ISaveHandler handler = world.getWorldFile();
		if(handler instanceof SaveHandler) {
			File dir = ((SaveHandler)handler).getSaveDirectory();
			String name = dir.getName();
			if(name != null && name.matches("DIM-\\d+") && dir.getParentFile() != null) {
				return dir.getParentFile();
			}
			return dir;
		}
		return null;
	}

	/**
	 * The main world's seed, i.e. world 0's RandomSeed. Read from the save
	 * root's level.dat when possible (needed while inside a linked world);
	 * falls back to the current world's seed.
	 */
	public static long getBaseSeed(World world) {
		File base = getBaseSaveDirectory(world);
		if(base != null) {
			try {
				File levelFile = new File(base, "level.dat");
				if(levelFile.exists()) {
					NBTTagCompound nbt = CompressedStreamTools.readCompressed(new FileInputStream(levelFile));
					long seed = nbt.getCompoundTag("Data").getLong("RandomSeed");
					if(seed != 0L) return seed;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return world.getRandomSeed();
	}

	private void load() {
		try {
			File file1 = new File(this.saveDirectory, "portals.dat");
			if(file1.exists()) {
				NBTTagCompound nbt = CompressedStreamTools.readCompressed(new FileInputStream(file1));
				this.nextId = nbt.getCompoundTag("Data").getInteger("NextWorldId");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(this.nextId < FIRST_WORLD_ID) this.nextId = FIRST_WORLD_ID;
	}

	private void save() {
		if(this.saveDirectory == null) return;
		try {
			NBTTagCompound data = new NBTTagCompound();
			data.setInteger("NextWorldId", this.nextId);
			NBTTagCompound root = new NBTTagCompound();
			root.setTag("Data", data);
			CompressedStreamTools.writeCompressed(root, new FileOutputStream(new File(this.saveDirectory, "portals.dat")));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}