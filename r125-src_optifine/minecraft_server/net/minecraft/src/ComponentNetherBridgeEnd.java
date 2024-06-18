package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentNetherBridgeEnd extends ComponentNetherBridgePiece {
	private int fillSeed;

	public ComponentNetherBridgeEnd(int i1, Random random2, StructureBoundingBox structureBoundingBox3, int i4) {
		super(i1);
		this.coordBaseMode = i4;
		this.boundingBox = structureBoundingBox3;
		this.fillSeed = random2.nextInt();
	}

	public void buildComponent(StructureComponent structureComponent1, List list2, Random random3) {
	}

	public static ComponentNetherBridgeEnd func_40301_a(List list0, Random random1, int i2, int i3, int i4, int i5, int i6) {
		StructureBoundingBox structureBoundingBox7 = StructureBoundingBox.getComponentToAddBoundingBox(i2, i3, i4, -1, -3, 0, 5, 10, 8, i5);
		return isAboveGround(structureBoundingBox7) && StructureComponent.findIntersecting(list0, structureBoundingBox7) == null ? new ComponentNetherBridgeEnd(i6, random1, structureBoundingBox7, i5) : null;
	}

	public boolean addComponentParts(World world1, Random random2, StructureBoundingBox structureBoundingBox3) {
		Random random4 = new Random((long)this.fillSeed);

		int i5;
		int i6;
		int i7;
		for(i5 = 0; i5 <= 4; ++i5) {
			for(i6 = 3; i6 <= 4; ++i6) {
				i7 = random4.nextInt(8);
				this.fillWithBlocks(world1, structureBoundingBox3, i5, i6, 0, i5, i6, i7, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
			}
		}

		i5 = random4.nextInt(8);
		this.fillWithBlocks(world1, structureBoundingBox3, 0, 5, 0, 0, 5, i5, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		i5 = random4.nextInt(8);
		this.fillWithBlocks(world1, structureBoundingBox3, 4, 5, 0, 4, 5, i5, Block.netherBrick.blockID, Block.netherBrick.blockID, false);

		for(i5 = 0; i5 <= 4; ++i5) {
			i6 = random4.nextInt(5);
			this.fillWithBlocks(world1, structureBoundingBox3, i5, 2, 0, i5, 2, i6, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
		}

		for(i5 = 0; i5 <= 4; ++i5) {
			for(i6 = 0; i6 <= 1; ++i6) {
				i7 = random4.nextInt(3);
				this.fillWithBlocks(world1, structureBoundingBox3, i5, i6, 0, i5, i6, i7, Block.netherBrick.blockID, Block.netherBrick.blockID, false);
			}
		}

		return true;
	}
}
