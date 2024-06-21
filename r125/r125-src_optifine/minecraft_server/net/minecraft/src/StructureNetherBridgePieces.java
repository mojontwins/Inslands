package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class StructureNetherBridgePieces {
	private static final StructureNetherBridgePieceWeight[] primaryComponents = new StructureNetherBridgePieceWeight[]{new StructureNetherBridgePieceWeight(ComponentNetherBridgeStraight.class, 30, 0, true), new StructureNetherBridgePieceWeight(ComponentNetherBridgeCrossing3.class, 10, 4), new StructureNetherBridgePieceWeight(ComponentNetherBridgeCrossing.class, 10, 4), new StructureNetherBridgePieceWeight(ComponentNetherBridgeStairs.class, 10, 3), new StructureNetherBridgePieceWeight(ComponentNetherBridgeThrone.class, 5, 2), new StructureNetherBridgePieceWeight(ComponentNetherBridgeEntrance.class, 5, 1)};
	private static final StructureNetherBridgePieceWeight[] secondaryComponents = new StructureNetherBridgePieceWeight[]{new StructureNetherBridgePieceWeight(ComponentNetherBridgeCorridor5.class, 25, 0, true), new StructureNetherBridgePieceWeight(ComponentNetherBridgeCrossing2.class, 15, 5), new StructureNetherBridgePieceWeight(ComponentNetherBridgeCorridor2.class, 5, 10), new StructureNetherBridgePieceWeight(ComponentNetherBridgeCorridor.class, 5, 10), new StructureNetherBridgePieceWeight(ComponentNetherBridgeCorridor3.class, 10, 3, true), new StructureNetherBridgePieceWeight(ComponentNetherBridgeCorridor4.class, 7, 2), new StructureNetherBridgePieceWeight(ComponentNetherBridgeNetherStalkRoom.class, 5, 2)};

	private static ComponentNetherBridgePiece createNextComponentRandom(StructureNetherBridgePieceWeight structureNetherBridgePieceWeight0, List list1, Random random2, int i3, int i4, int i5, int i6, int i7) {
		Class class8 = structureNetherBridgePieceWeight0.field_40655_a;
		Object object9 = null;
		if(class8 == ComponentNetherBridgeStraight.class) {
			object9 = ComponentNetherBridgeStraight.createValidComponent(list1, random2, i3, i4, i5, i6, i7);
		} else if(class8 == ComponentNetherBridgeCrossing3.class) {
			object9 = ComponentNetherBridgeCrossing3.createValidComponent(list1, random2, i3, i4, i5, i6, i7);
		} else if(class8 == ComponentNetherBridgeCrossing.class) {
			object9 = ComponentNetherBridgeCrossing.createValidComponent(list1, random2, i3, i4, i5, i6, i7);
		} else if(class8 == ComponentNetherBridgeStairs.class) {
			object9 = ComponentNetherBridgeStairs.createValidComponent(list1, random2, i3, i4, i5, i6, i7);
		} else if(class8 == ComponentNetherBridgeThrone.class) {
			object9 = ComponentNetherBridgeThrone.createValidComponent(list1, random2, i3, i4, i5, i6, i7);
		} else if(class8 == ComponentNetherBridgeEntrance.class) {
			object9 = ComponentNetherBridgeEntrance.createValidComponent(list1, random2, i3, i4, i5, i6, i7);
		} else if(class8 == ComponentNetherBridgeCorridor5.class) {
			object9 = ComponentNetherBridgeCorridor5.createValidComponent(list1, random2, i3, i4, i5, i6, i7);
		} else if(class8 == ComponentNetherBridgeCorridor2.class) {
			object9 = ComponentNetherBridgeCorridor2.createValidComponent(list1, random2, i3, i4, i5, i6, i7);
		} else if(class8 == ComponentNetherBridgeCorridor.class) {
			object9 = ComponentNetherBridgeCorridor.createValidComponent(list1, random2, i3, i4, i5, i6, i7);
		} else if(class8 == ComponentNetherBridgeCorridor3.class) {
			object9 = ComponentNetherBridgeCorridor3.createValidComponent(list1, random2, i3, i4, i5, i6, i7);
		} else if(class8 == ComponentNetherBridgeCorridor4.class) {
			object9 = ComponentNetherBridgeCorridor4.createValidComponent(list1, random2, i3, i4, i5, i6, i7);
		} else if(class8 == ComponentNetherBridgeCrossing2.class) {
			object9 = ComponentNetherBridgeCrossing2.createValidComponent(list1, random2, i3, i4, i5, i6, i7);
		} else if(class8 == ComponentNetherBridgeNetherStalkRoom.class) {
			object9 = ComponentNetherBridgeNetherStalkRoom.createValidComponent(list1, random2, i3, i4, i5, i6, i7);
		}

		return (ComponentNetherBridgePiece)object9;
	}

	static ComponentNetherBridgePiece createNextComponent(StructureNetherBridgePieceWeight structureNetherBridgePieceWeight0, List list1, Random random2, int i3, int i4, int i5, int i6, int i7) {
		return createNextComponentRandom(structureNetherBridgePieceWeight0, list1, random2, i3, i4, i5, i6, i7);
	}

	static StructureNetherBridgePieceWeight[] getPrimaryComponents() {
		return primaryComponents;
	}

	static StructureNetherBridgePieceWeight[] getSecondaryComponents() {
		return secondaryComponents;
	}
}
