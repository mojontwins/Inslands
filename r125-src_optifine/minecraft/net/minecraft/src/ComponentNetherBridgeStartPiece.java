package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComponentNetherBridgeStartPiece extends ComponentNetherBridgeCrossing3 {
	public StructureNetherBridgePieceWeight field_40037_a;
	public List field_40035_b = new ArrayList();
	public List field_40036_c;
	public ArrayList field_40034_d = new ArrayList();

	public ComponentNetherBridgeStartPiece(Random random1, int i2, int i3) {
		super(random1, i2, i3);
		StructureNetherBridgePieceWeight[] structureNetherBridgePieceWeight4 = StructureNetherBridgePieces.getPrimaryComponents();
		int i5 = structureNetherBridgePieceWeight4.length;

		int i6;
		StructureNetherBridgePieceWeight structureNetherBridgePieceWeight7;
		for(i6 = 0; i6 < i5; ++i6) {
			structureNetherBridgePieceWeight7 = structureNetherBridgePieceWeight4[i6];
			structureNetherBridgePieceWeight7.field_40698_c = 0;
			this.field_40035_b.add(structureNetherBridgePieceWeight7);
		}

		this.field_40036_c = new ArrayList();
		structureNetherBridgePieceWeight4 = StructureNetherBridgePieces.getSecondaryComponents();
		i5 = structureNetherBridgePieceWeight4.length;

		for(i6 = 0; i6 < i5; ++i6) {
			structureNetherBridgePieceWeight7 = structureNetherBridgePieceWeight4[i6];
			structureNetherBridgePieceWeight7.field_40698_c = 0;
			this.field_40036_c.add(structureNetherBridgePieceWeight7);
		}

	}
}
