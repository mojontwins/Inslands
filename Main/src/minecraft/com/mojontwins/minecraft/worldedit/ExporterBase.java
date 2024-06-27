package com.mojontwins.minecraft.worldedit;

import java.util.HashMap;

import net.minecraft.src.Vec3i;

public abstract class ExporterBase {
	public static HashMap<String,ExporterBase> exporterList = new HashMap<String,ExporterBase> ();
	public static int lastId = 0;
	
	public static ExporterBase raw = new ExporterBaseRaw().register();
	
	public int id;
	
	public ExporterBase() {
		this.id = lastId++;
	}
	
	public ExporterBase register() {
		exporterList.put(this.getName(), this);
		return this;
	}
	
	public static ExporterBase getByName(String name) {
		return exporterList.get(name);
	}
	
	public abstract String getName();
	
	public abstract boolean export(int [][][] buffer, Vec3i dims, String fileName);
}
