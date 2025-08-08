package net.minecraft.server;

public enum EnumLevelThemes {
	NORMAL(0),
	HELL(1),
	FOREST(2),
	PARADISE(3);
	
	private final int id;
	
	private EnumLevelThemes(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
