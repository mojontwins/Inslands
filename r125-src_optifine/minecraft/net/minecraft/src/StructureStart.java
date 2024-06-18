package net.minecraft.src;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public abstract class StructureStart {
	protected LinkedList components = new LinkedList();
	protected StructureBoundingBox boundingBox;

	public StructureBoundingBox getBoundingBox() {
		return this.boundingBox;
	}

	public LinkedList getComponents() {
		return this.components;
	}

	public void generateStructure(World world1, Random random2, StructureBoundingBox structureBoundingBox3) {
		Iterator iterator4 = this.components.iterator();

		while(iterator4.hasNext()) {
			StructureComponent structureComponent5 = (StructureComponent)iterator4.next();
			if(structureComponent5.getBoundingBox().intersectsWith(structureBoundingBox3) && !structureComponent5.addComponentParts(world1, random2, structureBoundingBox3)) {
				iterator4.remove();
			}
		}

	}

	protected void updateBoundingBox() {
		this.boundingBox = StructureBoundingBox.getNewBoundingBox();
		Iterator iterator1 = this.components.iterator();

		while(iterator1.hasNext()) {
			StructureComponent structureComponent2 = (StructureComponent)iterator1.next();
			this.boundingBox.expandTo(structureComponent2.getBoundingBox());
		}

	}

	protected void markAvailableHeight(World world1, Random random2, int i3) {
		int i4 = 63 - i3;
		int i5 = this.boundingBox.getYSize() + 1;
		if(i5 < i4) {
			i5 += random2.nextInt(i4 - i5);
		}

		int i6 = i5 - this.boundingBox.maxY;
		this.boundingBox.offset(0, i6, 0);
		Iterator iterator7 = this.components.iterator();

		while(iterator7.hasNext()) {
			StructureComponent structureComponent8 = (StructureComponent)iterator7.next();
			structureComponent8.getBoundingBox().offset(0, i6, 0);
		}

	}

	protected void setRandomHeight(World world1, Random random2, int i3, int i4) {
		int i5 = i4 - i3 + 1 - this.boundingBox.getYSize();
		boolean z6 = true;
		int i10;
		if(i5 > 1) {
			i10 = i3 + random2.nextInt(i5);
		} else {
			i10 = i3;
		}

		int i7 = i10 - this.boundingBox.minY;
		this.boundingBox.offset(0, i7, 0);
		Iterator iterator8 = this.components.iterator();

		while(iterator8.hasNext()) {
			StructureComponent structureComponent9 = (StructureComponent)iterator8.next();
			structureComponent9.getBoundingBox().offset(0, i7, 0);
		}

	}

	public boolean isSizeableStructure() {
		return true;
	}
}
