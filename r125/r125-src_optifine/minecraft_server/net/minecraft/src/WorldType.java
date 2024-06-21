package net.minecraft.src;

public class WorldType {
	public static final WorldType[] worldTypes = new WorldType[16];
	public static final WorldType DEFAULT = (new WorldType(0, "default", 1)).func_48448_d();
	public static final WorldType FLAT = new WorldType(1, "flat");
	public static final WorldType DEFAULT_1_1 = (new WorldType(8, "default_1_1", 0)).setCanBeCreated(false);
	private final String worldType;
	private final int generatorVersion;
	private boolean canBeCreated;
	private boolean field_48460_h;

	private WorldType(int i1, String string2) {
		this(i1, string2, 0);
	}

	private WorldType(int i1, String string2, int i3) {
		this.worldType = string2;
		this.generatorVersion = i3;
		this.canBeCreated = true;
		worldTypes[i1] = this;
	}

	public String func_48449_a() {
		return this.worldType;
	}

	public int getGeneratorVersion() {
		return this.generatorVersion;
	}

	public WorldType func_48451_a(int i1) {
		return this == DEFAULT && i1 == 0 ? DEFAULT_1_1 : this;
	}

	private WorldType setCanBeCreated(boolean z1) {
		this.canBeCreated = z1;
		return this;
	}

	private WorldType func_48448_d() {
		this.field_48460_h = true;
		return this;
	}

	public boolean func_48453_c() {
		return this.field_48460_h;
	}

	public static WorldType parseWorldType(String string0) {
		for(int i1 = 0; i1 < worldTypes.length; ++i1) {
			if(worldTypes[i1] != null && worldTypes[i1].worldType.equalsIgnoreCase(string0)) {
				return worldTypes[i1];
			}
		}

		return null;
	}
}
