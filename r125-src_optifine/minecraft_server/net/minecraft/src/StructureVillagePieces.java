package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class StructureVillagePieces {
	public static ArrayList getStructureVillageWeightedPieceList(Random random0, int i1) {
		ArrayList arrayList2 = new ArrayList();
		arrayList2.add(new StructureVillagePieceWeight(ComponentVillageHouse4_Garden.class, 4, MathHelper.getRandomIntegerInRange(random0, 2 + i1, 4 + i1 * 2)));
		arrayList2.add(new StructureVillagePieceWeight(ComponentVillageChurch.class, 20, MathHelper.getRandomIntegerInRange(random0, 0 + i1, 1 + i1)));
		arrayList2.add(new StructureVillagePieceWeight(ComponentVillageHouse1.class, 20, MathHelper.getRandomIntegerInRange(random0, 0 + i1, 2 + i1)));
		arrayList2.add(new StructureVillagePieceWeight(ComponentVillageWoodHut.class, 3, MathHelper.getRandomIntegerInRange(random0, 2 + i1, 5 + i1 * 3)));
		arrayList2.add(new StructureVillagePieceWeight(ComponentVillageHall.class, 15, MathHelper.getRandomIntegerInRange(random0, 0 + i1, 2 + i1)));
		arrayList2.add(new StructureVillagePieceWeight(ComponentVillageField.class, 3, MathHelper.getRandomIntegerInRange(random0, 1 + i1, 4 + i1)));
		arrayList2.add(new StructureVillagePieceWeight(ComponentVillageField2.class, 3, MathHelper.getRandomIntegerInRange(random0, 2 + i1, 4 + i1 * 2)));
		arrayList2.add(new StructureVillagePieceWeight(ComponentVillageHouse2.class, 15, MathHelper.getRandomIntegerInRange(random0, 0, 1 + i1)));
		arrayList2.add(new StructureVillagePieceWeight(ComponentVillageHouse3.class, 8, MathHelper.getRandomIntegerInRange(random0, 0 + i1, 3 + i1 * 2)));
		Iterator iterator3 = arrayList2.iterator();

		while(iterator3.hasNext()) {
			if(((StructureVillagePieceWeight)iterator3.next()).villagePiecesLimit == 0) {
				iterator3.remove();
			}
		}

		return arrayList2;
	}

	private static int getAvailablePieceWeight(ArrayList arrayList0) {
		boolean z1 = false;
		int i2 = 0;

		StructureVillagePieceWeight structureVillagePieceWeight4;
		for(Iterator iterator3 = arrayList0.iterator(); iterator3.hasNext(); i2 += structureVillagePieceWeight4.villagePieceWeight) {
			structureVillagePieceWeight4 = (StructureVillagePieceWeight)iterator3.next();
			if(structureVillagePieceWeight4.villagePiecesLimit > 0 && structureVillagePieceWeight4.villagePiecesSpawned < structureVillagePieceWeight4.villagePiecesLimit) {
				z1 = true;
			}
		}

		return z1 ? i2 : -1;
	}

	private static ComponentVillage getVillageComponentFromWeightedPiece(StructureVillagePieceWeight structureVillagePieceWeight0, List list1, Random random2, int i3, int i4, int i5, int i6, int i7) {
		Class class8 = structureVillagePieceWeight0.villagePieceClass;
		Object object9 = null;
		if(class8 == ComponentVillageHouse4_Garden.class) {
			object9 = ComponentVillageHouse4_Garden.findValidPlacement(list1, random2, i3, i4, i5, i6, i7);
		} else if(class8 == ComponentVillageChurch.class) {
			object9 = ComponentVillageChurch.findValidPlacement(list1, random2, i3, i4, i5, i6, i7);
		} else if(class8 == ComponentVillageHouse1.class) {
			object9 = ComponentVillageHouse1.findValidPlacement(list1, random2, i3, i4, i5, i6, i7);
		} else if(class8 == ComponentVillageWoodHut.class) {
			object9 = ComponentVillageWoodHut.findValidPlacement(list1, random2, i3, i4, i5, i6, i7);
		} else if(class8 == ComponentVillageHall.class) {
			object9 = ComponentVillageHall.findValidPlacement(list1, random2, i3, i4, i5, i6, i7);
		} else if(class8 == ComponentVillageField.class) {
			object9 = ComponentVillageField.findValidPlacement(list1, random2, i3, i4, i5, i6, i7);
		} else if(class8 == ComponentVillageField2.class) {
			object9 = ComponentVillageField2.findValidPlacement(list1, random2, i3, i4, i5, i6, i7);
		} else if(class8 == ComponentVillageHouse2.class) {
			object9 = ComponentVillageHouse2.findValidPlacement(list1, random2, i3, i4, i5, i6, i7);
		} else if(class8 == ComponentVillageHouse3.class) {
			object9 = ComponentVillageHouse3.findValidPlacement(list1, random2, i3, i4, i5, i6, i7);
		}

		return (ComponentVillage)object9;
	}

	private static ComponentVillage getNextVillageComponent(ComponentVillageStartPiece componentVillageStartPiece0, List list1, Random random2, int i3, int i4, int i5, int i6, int i7) {
		int i8 = getAvailablePieceWeight(componentVillageStartPiece0.structureVillageWeightedPieceList);
		if(i8 <= 0) {
			return null;
		} else {
			int i9 = 0;

			while(i9 < 5) {
				++i9;
				int i10 = random2.nextInt(i8);
				Iterator iterator11 = componentVillageStartPiece0.structureVillageWeightedPieceList.iterator();

				while(iterator11.hasNext()) {
					StructureVillagePieceWeight structureVillagePieceWeight12 = (StructureVillagePieceWeight)iterator11.next();
					i10 -= structureVillagePieceWeight12.villagePieceWeight;
					if(i10 < 0) {
						if(!structureVillagePieceWeight12.canSpawnMoreVillagePiecesOfType(i7) || structureVillagePieceWeight12 == componentVillageStartPiece0.structVillagePieceWeight && componentVillageStartPiece0.structureVillageWeightedPieceList.size() > 1) {
							break;
						}

						ComponentVillage componentVillage13 = getVillageComponentFromWeightedPiece(structureVillagePieceWeight12, list1, random2, i3, i4, i5, i6, i7);
						if(componentVillage13 != null) {
							++structureVillagePieceWeight12.villagePiecesSpawned;
							componentVillageStartPiece0.structVillagePieceWeight = structureVillagePieceWeight12;
							if(!structureVillagePieceWeight12.canSpawnMoreVillagePieces()) {
								componentVillageStartPiece0.structureVillageWeightedPieceList.remove(structureVillagePieceWeight12);
							}

							return componentVillage13;
						}
					}
				}
			}

			StructureBoundingBox structureBoundingBox14 = ComponentVillageTorch.findValidPlacement(list1, random2, i3, i4, i5, i6);
			if(structureBoundingBox14 != null) {
				return new ComponentVillageTorch(i7, random2, structureBoundingBox14, i6);
			} else {
				return null;
			}
		}
	}

	private static StructureComponent getNextVillageStructureComponent(ComponentVillageStartPiece componentVillageStartPiece0, List list1, Random random2, int i3, int i4, int i5, int i6, int i7) {
		if(i7 > 50) {
			return null;
		} else if(Math.abs(i3 - componentVillageStartPiece0.getBoundingBox().minX) <= 112 && Math.abs(i5 - componentVillageStartPiece0.getBoundingBox().minZ) <= 112) {
			ComponentVillage componentVillage8 = getNextVillageComponent(componentVillageStartPiece0, list1, random2, i3, i4, i5, i6, i7 + 1);
			if(componentVillage8 != null) {
				int i9 = (componentVillage8.boundingBox.minX + componentVillage8.boundingBox.maxX) / 2;
				int i10 = (componentVillage8.boundingBox.minZ + componentVillage8.boundingBox.maxZ) / 2;
				int i11 = componentVillage8.boundingBox.maxX - componentVillage8.boundingBox.minX;
				int i12 = componentVillage8.boundingBox.maxZ - componentVillage8.boundingBox.minZ;
				int i13 = i11 > i12 ? i11 : i12;
				if(componentVillageStartPiece0.getWorldChunkManager().areBiomesViable(i9, i10, i13 / 2 + 4, MapGenVillage.villageSpawnBiomes)) {
					list1.add(componentVillage8);
					componentVillageStartPiece0.field_35389_e.add(componentVillage8);
					return componentVillage8;
				}
			}

			return null;
		} else {
			return null;
		}
	}

	private static StructureComponent getNextComponentVillagePath(ComponentVillageStartPiece componentVillageStartPiece0, List list1, Random random2, int i3, int i4, int i5, int i6, int i7) {
		if(i7 > 3 + componentVillageStartPiece0.terrainType) {
			return null;
		} else if(Math.abs(i3 - componentVillageStartPiece0.getBoundingBox().minX) <= 112 && Math.abs(i5 - componentVillageStartPiece0.getBoundingBox().minZ) <= 112) {
			StructureBoundingBox structureBoundingBox8 = ComponentVillagePathGen.func_35378_a(componentVillageStartPiece0, list1, random2, i3, i4, i5, i6);
			if(structureBoundingBox8 != null && structureBoundingBox8.minY > 10) {
				ComponentVillagePathGen componentVillagePathGen9 = new ComponentVillagePathGen(i7, random2, structureBoundingBox8, i6);
				int i10 = (componentVillagePathGen9.boundingBox.minX + componentVillagePathGen9.boundingBox.maxX) / 2;
				int i11 = (componentVillagePathGen9.boundingBox.minZ + componentVillagePathGen9.boundingBox.maxZ) / 2;
				int i12 = componentVillagePathGen9.boundingBox.maxX - componentVillagePathGen9.boundingBox.minX;
				int i13 = componentVillagePathGen9.boundingBox.maxZ - componentVillagePathGen9.boundingBox.minZ;
				int i14 = i12 > i13 ? i12 : i13;
				if(componentVillageStartPiece0.getWorldChunkManager().areBiomesViable(i10, i11, i14 / 2 + 4, MapGenVillage.villageSpawnBiomes)) {
					list1.add(componentVillagePathGen9);
					componentVillageStartPiece0.field_35387_f.add(componentVillagePathGen9);
					return componentVillagePathGen9;
				}
			}

			return null;
		} else {
			return null;
		}
	}

	static StructureComponent getNextStructureComponent(ComponentVillageStartPiece componentVillageStartPiece0, List list1, Random random2, int i3, int i4, int i5, int i6, int i7) {
		return getNextVillageStructureComponent(componentVillageStartPiece0, list1, random2, i3, i4, i5, i6, i7);
	}

	static StructureComponent getNextStructureComponentVillagePath(ComponentVillageStartPiece componentVillageStartPiece0, List list1, Random random2, int i3, int i4, int i5, int i6, int i7) {
		return getNextComponentVillagePath(componentVillageStartPiece0, list1, random2, i3, i4, i5, i6, i7);
	}
}
