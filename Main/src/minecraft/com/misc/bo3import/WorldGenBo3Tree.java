package com.misc.bo3import;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.src.World;
import net.minecraft.src.WorldGenerator;

public class WorldGenBo3Tree extends WorldGenerator {
	private static HashMap<String, Bo3Schematic> treePool = new HashMap<String, Bo3Schematic>();
	public static final String bo3ResourceDirectory = "/resources/bo3";
	private String treeName = "";
	private boolean forceSpawn;

	public WorldGenBo3Tree() {
		this("", false);
	}

	public WorldGenBo3Tree(boolean forceSpawn) {
		this("", forceSpawn);
	}

	public WorldGenBo3Tree(String treeName) {
		this(treeName, false);
	}

	public WorldGenBo3Tree(String treeName, boolean forceSpawn) {
		this.treeName = treeName;
		this.setForceSpawn(forceSpawn);
	}

	/*
	 * This is a raw, broken, incomplete & limited implementation of BO3. Files read
	 * from the folder, parsed & stored as Bo3Schematic objects, which only supports
	 * a very, very limited subset of BO3 features.
	 */

	public void setTreeName(String treeName) {
		this.treeName = treeName;
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		if (this.treeName == null || "".equals(this.treeName))
			return false;

		Bo3Schematic schematic = treePool.get(this.treeName);

		if (schematic == null) {
			System.out.println(this.treeName + " does not exist!");
			return false;
		}

		// Random rotation
		schematic.setOrthoAngle(rand.nextInt(4));

		if (forceSpawn || schematic.canSpawnHere(world, x, y, z)) {
			schematic.render(world, x, y, z);
			return true;
		}

		return false;
	}

	boolean generateRandomTreeOf(String category, World world, Random rand, int x, int y, int z) {
		this.treeName = getRamdomTreeTypeOf(rand, category);
		return this.generate(world, rand, x, y, z);
	}
	
	public boolean isForceSpawn() {
		return forceSpawn;
	}

	public void setForceSpawn(boolean forceSpawn) {
		this.forceSpawn = forceSpawn;
	}

	static {
		// Find an alternative that works with Java 6!
		/*
		// Read & parse all schematics
		URI uri;
		try {
			uri = WorldGenBo3Tree.class.getResource(bo3ResourceDirectory).toURI();

			Path myPath;
			if (uri.getScheme().equals("jar")) {
				FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
				myPath = fileSystem.getPath("/resources");
			} else {
				myPath = Paths.get(uri);
			}

			Files.walkFileTree(myPath, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

					// 1st tokenize file URI to get just the object name
					// This code is extremely unsafe but the data set is extremely controlled
					// so nothing more fancy is needed. Sorry!

					StringTokenizer tokenizer = new StringTokenizer(file.toString(), File.separator);
					String lastPart = "a.b";
					String prevPart = "none";
					while (tokenizer.hasMoreTokens()) {
						prevPart = lastPart;
						lastPart = tokenizer.nextToken();
					}
					tokenizer = new StringTokenizer(lastPart, ".");
					String name = tokenizer.nextToken();

					// Parse schematic
					Bo3Schematic schematic = new Bo3Schematic().fromFile(file).withCategoryAndName(prevPart, name);

					// Put in pool
					treePool.put(schematic.toString(), schematic);

					System.out.println("Added " + schematic.toString());

					return FileVisitResult.CONTINUE;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}

	public static String getRamdomTreeTypeOf(Random rand, String category) {
		List<String> thisCategoryTrees = new ArrayList<String> ();
		
		for(String treeName : treePool.keySet()) {
			if (treeName.startsWith(category)) {
				thisCategoryTrees.add(treeName.substring(treeName.indexOf(':') + 1));
			}
		}
		
		return thisCategoryTrees.get(rand.nextInt(thisCategoryTrees.size()));
	}

}
