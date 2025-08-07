package net.minecraft.src;

public class Material {
	public static final Material air = new MaterialTransparent(MapColor.airColor).setName("air");
	public static final Material grass = new Material(MapColor.grassColor).setName("grass");
	public static final Material ground = new Material(MapColor.dirtColor).setName("ground");
	public static final Material wood = (new Material(MapColor.woodColor)).setBurning().setName("wood");
	public static final Material rock = (new Material(MapColor.stoneColor)).setNoHarvest().setName("rock");
	public static final Material iron = (new Material(MapColor.ironColor)).setNoHarvest().setName("iron");
	public static final Material water = (new MaterialLiquid(MapColor.waterColor)).setNoPushMobility().setName("water");
	public static final Material lava = (new MaterialLiquid(MapColor.tntColor)).setNoPushMobility().setName("lava");
	public static final Material leaves = (new Material(MapColor.foliageColor)).setBurning().setIsTranslucent().setNoPushMobility().setName("leaves");
	public static final Material plants = (new MaterialLogic(MapColor.foliageColor)).setNoPushMobility().setName("plants");
	public static final Material sponge = new Material(MapColor.clothColor).setName("sponge");
	public static final Material cloth = (new Material(MapColor.clothColor)).setBurning().setName("cloth");
	public static final Material fire = (new MaterialTransparent(MapColor.airColor)).setNoPushMobility().setName("fire");
	public static final Material sand = new Material(MapColor.sandColor).setName("sand");
	public static final Material circuits = (new MaterialLogic(MapColor.airColor)).setNoPushMobility().setName("circuits");
	public static final Material glass = (new Material(MapColor.airColor)).setIsTranslucent().setName("glass");
	public static final Material tnt = (new Material(MapColor.tntColor)).setBurning().setIsTranslucent().setName("tnt");
	public static final Material wug = (new Material(MapColor.foliageColor)).setNoPushMobility().setName("wug");
	public static final Material ice = (new Material(MapColor.iceColor)).setIsTranslucent().setName("ice");
	public static final Material snow = (new MaterialLogic(MapColor.snowColor)).setIsGroundCover().setIsTranslucent().setNoHarvest().setNoPushMobility().setName("snow");
	public static final Material builtSnow = (new Material(MapColor.snowColor)).setNoHarvest().setName("builtSnow");
	public static final Material cactus = (new Material(MapColor.foliageColor)).setIsTranslucent().setNoPushMobility().setName("cactus");
	public static final Material clay = new Material(MapColor.clayColor).setName("clay");
	public static final Material pumpkin = (new Material(MapColor.foliageColor)).setNoPushMobility().setName("pumpkin");
	public static final Material portal = (new MaterialPortal(MapColor.airColor)).setImmovableMobility().setName("portal");
	public static final Material cakeMaterial = (new Material(MapColor.airColor)).setNoPushMobility().setName("cakeMaterial");
	public static final Material web = (new MaterialWeb(MapColor.clothColor)).setNoHarvest().setNoPushMobility().setName("web");
	public static final Material piston = (new Material(MapColor.stoneColor)).setImmovableMobility().setName("piston");
	public static final Material slime = (new Material(MapColor.foliageColor)).setIsTranslucent().setName("slime");
	public static final Material flesh = (new Material(MapColor.tntColor)).setNoPushMobility().setName("flesh");
	public static final Material vine = new Material(MapColor.grassColor).setName("vine");
	public static final Material bone = (new Material(MapColor.iceColor)).setName("bone");
	public static final Material acid = (new MaterialLiquid(MapColor.waterColor)).setNoPushMobility().setName("acid");
	private boolean canBurn;
	private boolean groundCover;
	private boolean isOpaque;
	public final MapColor materialMapColor;
	private boolean canHarvest = true;
	private int mobilityFlag;
	private String materialName;

	public Material(MapColor mapColor1) {
		this.materialMapColor = mapColor1;
	}
	
	public Material setName(String name) {
		this.materialName = name;
		return this;
	}

	public boolean getIsLiquid() {
		return false;
	}

	public boolean isSolid() {
		return true;
	}

	public boolean getCanBlockGrass() {
		return true;
	}
	
	public boolean blocksMovement() {
		return true;
	}

	public boolean getIsSolid() {
		return true;
	}

	private Material setIsTranslucent() {
		this.isOpaque = true;
		return this;
	}

	protected Material setNoHarvest() {
		this.canHarvest = false;
		return this;
	}

	private Material setBurning() {
		this.canBurn = true;
		return this;
	}

	public boolean getBurning() {
		return this.canBurn;
	}

	public Material setIsGroundCover() {
		this.groundCover = true;
		return this;
	}

	public boolean getIsGroundCover() {
		return this.groundCover;
	}

	public boolean getIsTranslucent() {
		return this.isOpaque ? false : this.getIsSolid();
	}

	public boolean getIsHarvestable() {
		return this.canHarvest;
	}

	public int getMaterialMobility() {
		return this.mobilityFlag;
	}

	protected Material setNoPushMobility() {
		this.mobilityFlag = 1;
		return this;
	}

	protected Material setImmovableMobility() {
		this.mobilityFlag = 2;
		return this;
	}
	
	public String toString() {
		return this.materialName;
	}

	public static boolean woa(Material m) {
		return m == Material.water || m == Material.acid;
	}
}
