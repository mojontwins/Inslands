package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

class StructureVillageStart extends StructureStart {
	private boolean hasMoreThanTwoComponents = false;

	public StructureVillageStart(World world1, Random random2, int i3, int i4, int i5) {
		ArrayList arrayList7 = StructureVillagePieces.getStructureVillageWeightedPieceList(random2, i5);
		ComponentVillageStartPiece componentVillageStartPiece8 = new ComponentVillageStartPiece(world1.getWorldChunkManager(), 0, random2, (i3 << 4) + 2, (i4 << 4) + 2, arrayList7, i5);
		this.components.add(componentVillageStartPiece8);
		componentVillageStartPiece8.buildComponent(componentVillageStartPiece8, this.components, random2);
		ArrayList arrayList9 = componentVillageStartPiece8.field_35106_f;
		ArrayList arrayList10 = componentVillageStartPiece8.field_35108_e;

		int i11;
		while(!arrayList9.isEmpty() || !arrayList10.isEmpty()) {
			StructureComponent structureComponent12;
			if(!arrayList9.isEmpty()) {
				i11 = random2.nextInt(arrayList9.size());
				structureComponent12 = (StructureComponent)arrayList9.remove(i11);
				structureComponent12.buildComponent(componentVillageStartPiece8, this.components, random2);
			} else {
				i11 = random2.nextInt(arrayList10.size());
				structureComponent12 = (StructureComponent)arrayList10.remove(i11);
				structureComponent12.buildComponent(componentVillageStartPiece8, this.components, random2);
			}
		}

		this.updateBoundingBox();
		i11 = 0;
		Iterator iterator14 = this.components.iterator();

		while(iterator14.hasNext()) {
			StructureComponent structureComponent13 = (StructureComponent)iterator14.next();
			if(!(structureComponent13 instanceof ComponentVillageRoadPiece)) {
				++i11;
			}
		}

		this.hasMoreThanTwoComponents = i11 > 2;
	}

	public boolean isSizeableStructure() {
		return this.hasMoreThanTwoComponents;
	}
}
