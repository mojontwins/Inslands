package net.minecraft.src;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

abstract class ComponentNetherBridgePiece extends StructureComponent {
	protected ComponentNetherBridgePiece(int i1) {
		super(i1);
	}

	private int getTotalWeight(List list1) {
		boolean z2 = false;
		int i3 = 0;

		StructureNetherBridgePieceWeight structureNetherBridgePieceWeight5;
		for(Iterator iterator4 = list1.iterator(); iterator4.hasNext(); i3 += structureNetherBridgePieceWeight5.field_40697_b) {
			structureNetherBridgePieceWeight5 = (StructureNetherBridgePieceWeight)iterator4.next();
			if(structureNetherBridgePieceWeight5.field_40695_d > 0 && structureNetherBridgePieceWeight5.field_40698_c < structureNetherBridgePieceWeight5.field_40695_d) {
				z2 = true;
			}
		}

		return z2 ? i3 : -1;
	}

	private ComponentNetherBridgePiece getNextComponent(ComponentNetherBridgeStartPiece componentNetherBridgeStartPiece1, List list2, List list3, Random random4, int i5, int i6, int i7, int i8, int i9) {
		int i10 = this.getTotalWeight(list2);
		boolean z11 = i10 > 0 && i9 <= 30;
		int i12 = 0;

		while(i12 < 5 && z11) {
			++i12;
			int i13 = random4.nextInt(i10);
			Iterator iterator14 = list2.iterator();

			while(iterator14.hasNext()) {
				StructureNetherBridgePieceWeight structureNetherBridgePieceWeight15 = (StructureNetherBridgePieceWeight)iterator14.next();
				i13 -= structureNetherBridgePieceWeight15.field_40697_b;
				if(i13 < 0) {
					if(!structureNetherBridgePieceWeight15.func_40693_a(i9) || structureNetherBridgePieceWeight15 == componentNetherBridgeStartPiece1.field_40037_a && !structureNetherBridgePieceWeight15.field_40696_e) {
						break;
					}

					ComponentNetherBridgePiece componentNetherBridgePiece16 = StructureNetherBridgePieces.createNextComponent(structureNetherBridgePieceWeight15, list3, random4, i5, i6, i7, i8, i9);
					if(componentNetherBridgePiece16 != null) {
						++structureNetherBridgePieceWeight15.field_40698_c;
						componentNetherBridgeStartPiece1.field_40037_a = structureNetherBridgePieceWeight15;
						if(!structureNetherBridgePieceWeight15.func_40694_a()) {
							list2.remove(structureNetherBridgePieceWeight15);
						}

						return componentNetherBridgePiece16;
					}
				}
			}
		}

		ComponentNetherBridgeEnd componentNetherBridgeEnd17 = ComponentNetherBridgeEnd.func_40023_a(list3, random4, i5, i6, i7, i8, i9);
		return componentNetherBridgeEnd17;
	}

	private StructureComponent getNextComponent(ComponentNetherBridgeStartPiece componentNetherBridgeStartPiece1, List list2, Random random3, int i4, int i5, int i6, int i7, int i8, boolean z9) {
		if(Math.abs(i4 - componentNetherBridgeStartPiece1.getBoundingBox().minX) <= 112 && Math.abs(i6 - componentNetherBridgeStartPiece1.getBoundingBox().minZ) <= 112) {
			List list12 = componentNetherBridgeStartPiece1.field_40035_b;
			if(z9) {
				list12 = componentNetherBridgeStartPiece1.field_40036_c;
			}

			ComponentNetherBridgePiece componentNetherBridgePiece11 = this.getNextComponent(componentNetherBridgeStartPiece1, list12, list2, random3, i4, i5, i6, i7, i8 + 1);
			if(componentNetherBridgePiece11 != null) {
				list2.add(componentNetherBridgePiece11);
				componentNetherBridgeStartPiece1.field_40034_d.add(componentNetherBridgePiece11);
			}

			return componentNetherBridgePiece11;
		} else {
			ComponentNetherBridgeEnd componentNetherBridgeEnd10 = ComponentNetherBridgeEnd.func_40023_a(list2, random3, i4, i5, i6, i7, i8);
			return componentNetherBridgeEnd10;
		}
	}

	protected StructureComponent getNextComponentNormal(ComponentNetherBridgeStartPiece componentNetherBridgeStartPiece1, List list2, Random random3, int i4, int i5, boolean z6) {
		switch(this.coordBaseMode) {
		case 0:
			return this.getNextComponent(componentNetherBridgeStartPiece1, list2, random3, this.boundingBox.minX + i4, this.boundingBox.minY + i5, this.boundingBox.maxZ + 1, this.coordBaseMode, this.getComponentType(), z6);
		case 1:
			return this.getNextComponent(componentNetherBridgeStartPiece1, list2, random3, this.boundingBox.minX - 1, this.boundingBox.minY + i5, this.boundingBox.minZ + i4, this.coordBaseMode, this.getComponentType(), z6);
		case 2:
			return this.getNextComponent(componentNetherBridgeStartPiece1, list2, random3, this.boundingBox.minX + i4, this.boundingBox.minY + i5, this.boundingBox.minZ - 1, this.coordBaseMode, this.getComponentType(), z6);
		case 3:
			return this.getNextComponent(componentNetherBridgeStartPiece1, list2, random3, this.boundingBox.maxX + 1, this.boundingBox.minY + i5, this.boundingBox.minZ + i4, this.coordBaseMode, this.getComponentType(), z6);
		default:
			return null;
		}
	}

	protected StructureComponent getNextComponentX(ComponentNetherBridgeStartPiece componentNetherBridgeStartPiece1, List list2, Random random3, int i4, int i5, boolean z6) {
		switch(this.coordBaseMode) {
		case 0:
			return this.getNextComponent(componentNetherBridgeStartPiece1, list2, random3, this.boundingBox.minX - 1, this.boundingBox.minY + i4, this.boundingBox.minZ + i5, 1, this.getComponentType(), z6);
		case 1:
			return this.getNextComponent(componentNetherBridgeStartPiece1, list2, random3, this.boundingBox.minX + i5, this.boundingBox.minY + i4, this.boundingBox.minZ - 1, 2, this.getComponentType(), z6);
		case 2:
			return this.getNextComponent(componentNetherBridgeStartPiece1, list2, random3, this.boundingBox.minX - 1, this.boundingBox.minY + i4, this.boundingBox.minZ + i5, 1, this.getComponentType(), z6);
		case 3:
			return this.getNextComponent(componentNetherBridgeStartPiece1, list2, random3, this.boundingBox.minX + i5, this.boundingBox.minY + i4, this.boundingBox.minZ - 1, 2, this.getComponentType(), z6);
		default:
			return null;
		}
	}

	protected StructureComponent getNextComponentZ(ComponentNetherBridgeStartPiece componentNetherBridgeStartPiece1, List list2, Random random3, int i4, int i5, boolean z6) {
		switch(this.coordBaseMode) {
		case 0:
			return this.getNextComponent(componentNetherBridgeStartPiece1, list2, random3, this.boundingBox.maxX + 1, this.boundingBox.minY + i4, this.boundingBox.minZ + i5, 3, this.getComponentType(), z6);
		case 1:
			return this.getNextComponent(componentNetherBridgeStartPiece1, list2, random3, this.boundingBox.minX + i5, this.boundingBox.minY + i4, this.boundingBox.maxZ + 1, 0, this.getComponentType(), z6);
		case 2:
			return this.getNextComponent(componentNetherBridgeStartPiece1, list2, random3, this.boundingBox.maxX + 1, this.boundingBox.minY + i4, this.boundingBox.minZ + i5, 3, this.getComponentType(), z6);
		case 3:
			return this.getNextComponent(componentNetherBridgeStartPiece1, list2, random3, this.boundingBox.minX + i5, this.boundingBox.minY + i4, this.boundingBox.maxZ + 1, 0, this.getComponentType(), z6);
		default:
			return null;
		}
	}

	protected static boolean isAboveGround(StructureBoundingBox structureBoundingBox0) {
		return structureBoundingBox0 != null && structureBoundingBox0.minY > 10;
	}
}
