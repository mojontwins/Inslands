package com.mojang.minecraft.structure;

import java.util.Random;

import net.minecraft.src.World;

public class StructureMineshaftStart extends StructureStart {
	public StructureMineshaftStart(World world1, Random random2, int i3, int i4) {
		ComponentMineshaftRoom componentMineshaftRoom5 = new ComponentMineshaftRoom(0, random2, (i3 << 4) + 2, (i4 << 4) + 2);
		this.components.add(componentMineshaftRoom5);
		componentMineshaftRoom5.buildComponent(componentMineshaftRoom5, this.components, random2);
		this.updateBoundingBox();
		this.markAvailableHeight(world1, random2, 10);
	}
}
