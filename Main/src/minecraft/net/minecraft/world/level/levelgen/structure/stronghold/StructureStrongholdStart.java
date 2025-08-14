package net.minecraft.world.level.levelgen.structure.stronghold;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.world.level.World;
import net.minecraft.world.level.levelgen.structure.StructureComponent;
import net.minecraft.world.level.levelgen.structure.StructureStart;

class StructureStrongholdStart extends StructureStart {
	public StructureStrongholdStart(World world1, Random random2, int cX, int cZ) {
		StructureStrongholdPieces.prepareStructurePieces();
		ComponentStrongholdStairs2 componentStrongholdStairs25 = new ComponentStrongholdStairs2(world1, 0, random2, (cX << 4) + 2, (cZ << 4) + 2);
		this.components.add(componentStrongholdStairs25);
		componentStrongholdStairs25.buildComponent(componentStrongholdStairs25, this.components, random2);
		ArrayList<StructureComponent> arrayList6 = componentStrongholdStairs25.field_35037_b;

		while(!arrayList6.isEmpty()) {
			int i7 = random2.nextInt(arrayList6.size());
			StructureComponent structureComponent8 = (StructureComponent)arrayList6.remove(i7);
			structureComponent8.buildComponent(componentStrongholdStairs25, this.components, random2);
		}

		this.updateBoundingBox();
		//this.markAvailableHeight(world1, random2, 10);
	}
}
