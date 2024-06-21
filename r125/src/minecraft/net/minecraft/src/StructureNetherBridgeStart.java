package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

class StructureNetherBridgeStart extends StructureStart {
	public StructureNetherBridgeStart(World world1, Random random2, int i3, int i4) {
		ComponentNetherBridgeStartPiece componentNetherBridgeStartPiece5 = new ComponentNetherBridgeStartPiece(random2, (i3 << 4) + 2, (i4 << 4) + 2);
		this.components.add(componentNetherBridgeStartPiece5);
		componentNetherBridgeStartPiece5.buildComponent(componentNetherBridgeStartPiece5, this.components, random2);
		ArrayList arrayList6 = componentNetherBridgeStartPiece5.field_40034_d;

		while(!arrayList6.isEmpty()) {
			int i7 = random2.nextInt(arrayList6.size());
			StructureComponent structureComponent8 = (StructureComponent)arrayList6.remove(i7);
			structureComponent8.buildComponent(componentNetherBridgeStartPiece5, this.components, random2);
		}

		this.updateBoundingBox();
		this.setRandomHeight(world1, random2, 48, 70);
	}
}
