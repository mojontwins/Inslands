package com.mojontwins.minecraft.worldedit;

import java.util.StringTokenizer;

import net.minecraft.client.Minecraft;
import net.minecraft.src.BlockPos;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Vec3i;
import net.minecraft.src.World;

public class WorldEdit {
	public static BlockPos corner1 = new BlockPos();
	public static BlockPos corner2 = new BlockPos();
	public static int clipboard[][][];
	public static Vec3i clipboardDims = Vec3i.NULL_VECTOR.copy();
	
	public static int undo[][][];
	public static BlockPos undoOrigin = new BlockPos();
	public static Vec3i undoDims = Vec3i.NULL_VECTOR.copy();
	public static boolean hasUndo;
	
	public static boolean corner1Set = false;
	public static boolean corner2Set = false;
	
	public static Vec3i relativeOffset = Vec3i.NULL_VECTOR.copy();
	
	public static void init() {
		corner1Set = false;
		corner2Set = false;
		
		clipboard = null;
		clipboardDims = Vec3i.NULL_VECTOR.copy();
		
		undo = null;
		undoDims = Vec3i.NULL_VECTOR.copy();
		hasUndo = false;
		
		relativeOffset = Vec3i.NULL_VECTOR.copy();
	}
	
	public static boolean checkCorners() {
		return corner1Set && corner2Set;
	}
	
	public static void setCorner1(int x, int y, int z) {
		corner1.x = x;
		corner1.y = y;
		corner1.z = z;
		corner1Set = true;
	}
	
	public static void setCorner2(int x, int y, int z) {
		corner2.x = x;
		corner2.y = y;
		corner2.z = z;
		corner2Set = true;
	}
	
	public static BlockPos getFrom() {
		// Sorts out corner1 and corner2 to get the top-left-frontmost coordinate (min x, y, z)
		return new BlockPos().set(Math.min(corner1.x, corner2.x), Math.min(corner1.y, corner2.y), Math.min(corner1.z, corner2.z));
	}
	
	public static BlockPos getTo() {
		// Sorts out corner1 and corner2 to get the bottom-right-behindmost coordinate (max x, y, z)
		return new BlockPos().set(Math.max(corner1.x, corner2.x), Math.max(corner1.y, corner2.y), Math.max(corner1.z, corner2.z));
	}

	public static int[][][] initBuffer(Vec3i dims, BlockPos from, BlockPos to) {
		dims.x = to.x - from.x + 1;
		dims.y = to.y - from.y + 1;
		dims.z = to.z - from.z + 1;
		
		return new int[dims.x][dims.z][dims.y];
	}
	
	public static void copyToBuffer(int[][][] buffer, World world, BlockPos from, BlockPos to) {
		for(int x = from.x; x <= to.x; x ++) {
			for(int z = from.z; z <= to.z; z ++) {
				for(int y = from.y; y <= to.y; y ++) {
					buffer[x-from.x][z-from.z][y-from.y] = world.getBlockId(x, y, z) | world.getBlockMetadata(x, y, z) << 16;
				}
			}
		}
	}
	
	public static void pasteToWorld(int[][][] buffer, Vec3i dims, World world, BlockPos origin) {
		int x0 = origin.x; 
		int y0 = origin.y;
		int z0 = origin.z;
		
		if(clipboard != null) {
			for(int x = 0; x < dims.x; x ++) {
				for(int z = 0; z < dims.z; z ++) {
					for(int y = 0; y < dims.y; y ++) {
						int clip = buffer[x][z][y];
						world.setBlockAndMetadataWithNotify(x0 + x, y0 + y, z0 + z, clip & 0xFFFF, (clip >> 16) & 0xFF);
					}
				}
			}
		}
	}
	
	public static void copy(World world, EntityPlayer entityPlayer) {
		int px = (int) Math.floor(entityPlayer.posX);
		int py = (int) Math.floor(entityPlayer.posY);
		int pz = (int) Math.floor(entityPlayer.posZ);
		
		BlockPos from = getFrom();
		BlockPos to = getTo();
		
		clipboard = initBuffer(clipboardDims, from, to);
		copyToBuffer(clipboard, world, from, to);
		
		// Calculate offset
		relativeOffset.x = px - from.x;
		relativeOffset.y = py - from.y;
		relativeOffset.z = pz - from.z;
	}
	
	public static int clear(World world) {
		return fill(world, 0, 0);
	}
	
	public static int fill(World world, int blockID, int meta) {
		BlockPos from = getFrom();
		BlockPos to = getTo();
		int cleared = 0;
		
		// Save undo
		undoOrigin = from.copy();
		undo = initBuffer(undoDims, undoOrigin, to);
		copyToBuffer(undo, world, undoOrigin, to);
		hasUndo = true;
		
		for(int x = from.x; x <= to.x; x ++) {
			for(int z = from.z; z <= to.z; z ++) {
				for(int y = from.y; y <= to.y; y ++) {
					world.setBlockAndMetadataWithNotify(x, y, z, blockID, meta);
					cleared ++;
				}
			}
		}
		
		return cleared;
	}
	
	public static int substitute(World world, int existingBlockID, int existingMeta, int blockID, int meta) {
		BlockPos from = getFrom();
		BlockPos to = getTo();
		int cleared = 0;
		
		// Save undo
		undoOrigin = from.copy();
		undo = initBuffer(undoDims, undoOrigin, to);
		copyToBuffer(undo, world, undoOrigin, to);
		hasUndo = true;
		
		for(int x = from.x; x <= to.x; x ++) {
			for(int z = from.z; z <= to.z; z ++) {
				for(int y = from.y; y <= to.y; y ++) {
					int worldBlockID = world.getBlockId(x, y, z);
					int worldMeta = world.getBlockMetadata(x, y, z);
					if(worldBlockID == existingBlockID && (existingMeta == -1 || worldMeta == existingMeta)) world.setBlockAndMetadataWithNotify(x, y, z, blockID, meta);
					cleared ++;
				}
			}
		}
		
		return cleared;
	}
	
	public static void cut(World world, EntityPlayer entityPlayer) {
		copy(world, entityPlayer);
		clear(world);
	}
	
	public static void paste(World world, EntityPlayer entityPlayer) {
		int px = (int) Math.floor(entityPlayer.posX);
		int py = (int) Math.floor(entityPlayer.posY);
		int pz = (int) Math.floor(entityPlayer.posZ);
		
		int x0 = px - relativeOffset.x;
		int y0 = py - relativeOffset.y;
		int z0 = pz - relativeOffset.z;
		
		// Save undo
		undoOrigin = new BlockPos().set(x0, y0, z0);
		BlockPos to = new BlockPos().set(x0 + clipboardDims.x - 1, y0 + clipboardDims.y - 1, z0 + clipboardDims.z - 1);
		undo = initBuffer(undoDims, undoOrigin, to);
		copyToBuffer(undo, world, undoOrigin, to);
		hasUndo = true;
		
		pasteToWorld(clipboard, clipboardDims, world, new BlockPos().set(x0, y0, z0));
	}
	
	public static void undo(World world) {
		pasteToWorld(undo, undoDims, world, undoOrigin);
		hasUndo = false;
	}
	
	public static void rotate_ccw() {
		if(clipboard != null) {
			int aux[][][] = new int[clipboardDims.z][clipboardDims.x][clipboardDims.y];
			for(int x = 0; x < clipboardDims.x; x ++) {
				for(int z = 0; z < clipboardDims.z; z ++) {
					aux[clipboardDims.z - 1 - z][x] = clipboard[x][z];
				}
			}
			
			int t = clipboardDims.x;
			clipboardDims.x = clipboardDims.z;
			clipboardDims.z = t;
			
			clipboard = aux;
		}
	}
	
	public static void rotate_cw() {
		if(clipboard != null) {
			int aux[][][] = new int[clipboardDims.z][clipboardDims.x][clipboardDims.y];
			for(int x = 0; x < clipboardDims.x; x ++) {
				for(int z = 0; z < clipboardDims.z; z ++) {
					aux[z][clipboardDims.x - 1 - x] = clipboard[x][z];
				}
			}
			
			int t = clipboardDims.x;
			clipboardDims.x = clipboardDims.z;
			clipboardDims.z = t;
			
			clipboard = aux;
		}
	}
	
	public static boolean export(World world, int args, boolean flag, EntityPlayer entityPlayer, String filename, ExporterBase exporter, String arg) {
		// Make sure it's in the clipboard
		copy(world, entityPlayer);
		return exporter.export(clipboard, clipboardDims, filename, arg);
	}
	
	public static int clipboardSize() {
		return clipboardDims.x * clipboardDims.y * clipboardDims.z;
	}

	/*
	public static void processCommands(Minecraft mc, String cmd, String[] tokens) {
		int x = 0; 
		int y = -1; 
		int z = 0;
		
		if(mc.objectMouseOver != null) {
			x = mc.objectMouseOver.blockX;
			y = mc.objectMouseOver.blockY + 1;
			z = mc.objectMouseOver.blockZ;
		}
		
		int idx = tokens.length;
		
		if("//copy".equals(cmd)) {
			if(checkCorners()) {
				copy(mc.theWorld, mc.thePlayer);
				mc.ingameGUI.addChatMessage(clipboardSize() + " blocks copìed to the clipboard.");
			} else {
				mc.ingameGUI.addChatMessage("Set points first!");
			}
		} else if("//clear".equals(cmd)) {
			if(checkCorners()) {
				int cleared = clear(mc.theWorld);
				mc.ingameGUI.addChatMessage(cleared + " blocks clear.");
			} else {
				mc.ingameGUI.addChatMessage("Set points first!");
			}
		} else if("//fill".equals(cmd)) {
			if(idx > 1) {
				int blockID = 0;
				int meta = 0;
				
				try {
					blockID = Integer.parseInt(tokens[1]);
					if(idx > 2) meta = Integer.parseInt(tokens[2]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(checkCorners()) {
					int cleared = fill(mc.theWorld, blockID, meta);
					mc.ingameGUI.addChatMessage(cleared + " blocks filled.");
				} else {
					mc.ingameGUI.addChatMessage("Set points first!");
				}
			} else {
				mc.ingameGUI.addChatMessage("//fill <blockID> [<meta>]");
			}
		} else if("//replace".equals(cmd)) {
			if(idx > 4) {
				int existingBlockID = -1;
				int existingMeta = -1;
				int blockID = 0;
				int meta = 0;
				
				try {
					existingBlockID = Integer.parseInt(tokens[1]);
					existingMeta = Integer.parseInt(tokens[2]);
					blockID = Integer.parseInt(tokens[3]);
					if(idx > 4) meta = Integer.parseInt(tokens[4]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(checkCorners()) {
					int cleared = substitute(mc.theWorld, existingBlockID, existingMeta, blockID, meta);
					mc.ingameGUI.addChatMessage(cleared + " blocks filled.");
				} else {
					mc.ingameGUI.addChatMessage("Set points first!");
				}
			} else {
				mc.ingameGUI.addChatMessage("Use //replace <existingBlockID> <existingMeta> <blockID> <meta>");
				mc.ingameGUI.addChatMessage("Use -1 as existingMeta for any existing meta");
			}
		} else if("//cut".equals(cmd)) {
			if(checkCorners()) {
				cut(mc.theWorld, mc.thePlayer);
				mc.ingameGUI.addChatMessage(clipboardSize() + " blocks cut to the clipboard.");
			} else {
				mc.ingameGUI.addChatMessage("Set points first!");
			}
		} else if("//paste".equals(cmd)) {
			paste(mc.theWorld, mc.thePlayer);
			mc.ingameGUI.addChatMessage(clipboardSize() + " blocks pasted to the world.");
		} else if("//undo".equals(cmd)) {
			if(hasUndo) {
				mc.ingameGUI.addChatMessage("Undoing to " + undoOrigin);
				undo(mc.theWorld);
			} else {
				mc.ingameGUI.addChatMessage("Nothing to undo");
			}
		} else if("//rotatey_ccw".equals(cmd)) {
			rotate_ccw();
			mc.ingameGUI.addChatMessage("Clipboard rotated ccw around the y axis");
		} else if("//rotatey_cw".equals(cmd)) {
			rotate_cw();
			mc.ingameGUI.addChatMessage("Clipboard rotated cw around the y axis");
		} else if("//export".equals(cmd)) {
			// Syntax rn is //export exporter filename
		
			if(idx == 2 && "list".equals(tokens [1])) {
				mc.ingameGUI.addChatMessage(ExporterBase.getList());
			} else if(idx == 3 && "help".equals(tokens [1])) {
				ExporterBase exporter = ExporterBase.getByName(tokens [2]);
				if(exporter != null) {
					mc.ingameGUI.addChatMessage("[" + exporter.getName() + "] ");
					StringTokenizer tokenizer = new StringTokenizer(exporter.getHelp(), "\n");
					while(tokenizer.hasMoreTokens()) {
						mc.ingameGUI.addChatMessage(tokenizer.nextToken());
					}
				} else {
					mc.ingameGUI.addChatMessage("Unknown exporter \"" + tokens[2] + "\"");
				}
			} else if(idx > 3) {
			if(checkCorners()) {
					String exporterName = tokens [1];
					String fileName = tokens [2];
					String arg = idx > 3 ? tokens [3] : "";
					
					ExporterBase exporter = ExporterBase.getByName(exporterName);
					if (exporter != null) {
						if(export(mc.theWorld, 0, true, mc.thePlayer, fileName, exporter, arg)) {
							mc.ingameGUI.addChatMessage(clipboardSize() + " block exported to " + fileName + " using exporter " + exporterName);
						} else {
							mc.ingameGUI.addChatMessage(clipboardSize() + " Error exporting D: Check console for more info...");
						}
					} else {
						mc.ingameGUI.addChatMessage("Unknown exporter \"" + exporterName + "\"");
					}
					
				} else {
					mc.ingameGUI.addChatMessage("Set points first!");
				}
			} else {
				mc.ingameGUI.addChatMessage("//export <exporter> <filename> [<arg>]");
				mc.ingameGUI.addChatMessage("//export list");
				mc.ingameGUI.addChatMessage("//export help <exporter>");
			}
			
		} else if("//corner1".equals(cmd)) {
			if (idx == 4) {
				x = Integer.parseInt(tokens[1]);
				z = Integer.parseInt(tokens[2]);
				y = Integer.parseInt(tokens[3]);
			}
			if(y == -1) {
				mc.ingameGUI.addChatMessage("No block in range");
			} else {
				setCorner1(x, y, z);
				mc.ingameGUI.addChatMessage("1st point set to " + x + ", " + y + ", " + z);
			}
		} else if("//corner2".equals(cmd)) {
			if (idx == 4) {
				x = Integer.parseInt(tokens[1]);
				z = Integer.parseInt(tokens[2]);
				y = Integer.parseInt(tokens[3]);
			}
			if(y == -1) {
				mc.ingameGUI.addChatMessage("No block in range");
			} else {
				setCorner2(x, y, z);
				mc.ingameGUI.addChatMessage("2nd point set to " + x + ", " + y + ", " + z);
			}
		}
	}
	*/
}
