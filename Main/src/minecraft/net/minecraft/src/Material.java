package net.minecraft.src;

public class Material {
	public static final Material air = new MaterialTransparent(MapColor.airColor);
	public static final Material grass = new Material(MapColor.grassColor);
	public static final Material ground = new Material(MapColor.dirtColor);
	public static final Material wood = (new Material(MapColor.woodColor)).setBurning();
	public static final Material rock = (new Material(MapColor.stoneColor)).setNoHarvest();
	public static final Material iron = (new Material(MapColor.ironColor)).setNoHarvest();
	public static final Material water = (new MaterialLiquid(MapColor.waterColor)).setNoPushMobility();
	public static final Material lava = (new MaterialLiquid(MapColor.tntColor)).setNoPushMobility();
	public static final Material leaves = (new Material(MapColor.foliageColor)).setBurning().setIsTranslucent().setNoPushMobility();
	public static final Material plants = (new MaterialLogic(MapColor.foliageColor)).setNoPushMobility();
	public static final Material sponge = new Material(MapColor.clothColor);
	public static final Material cloth = (new Material(MapColor.clothColor)).setBurning();
	public static final Material fire = (new MaterialTransparent(MapColor.airColor)).setNoPushMobility();
	public static final Material sand = new Material(MapColor.sandColor);
	public static final Material circuits = (new MaterialLogic(MapColor.airColor)).setNoPushMobility();
	public static final Material glass = (new Material(MapColor.airColor)).setIsTranslucent();
	public static final Material tnt = (new Material(MapColor.tntColor)).setBurning().setIsTranslucent();
	public static final Material wug = (new Material(MapColor.foliageColor)).setNoPushMobility();
	public static final Material ice = (new Material(MapColor.iceColor)).setIsTranslucent();
	public static final Material snow = (new MaterialLogic(MapColor.snowColor)).setIsGroundCover().setIsTranslucent().setNoHarvest().setNoPushMobility();
	public static final Material builtSnow = (new Material(MapColor.snowColor)).setNoHarvest();
	public static final Material cactus = (new Material(MapColor.foliageColor)).setIsTranslucent().setNoPushMobility();
	public static final Material clay = new Material(MapColor.clayColor);
	public static final Material pumpkin = (new Material(MapColor.foliageColor)).setNoPushMobility();
	public static final Material portal = (new MaterialPortal(MapColor.airColor)).setImmovableMobility();
	public static final Material cakeMaterial = (new Material(MapColor.airColor)).setNoPushMobility();
	public static final Material web = (new MaterialWeb(MapColor.clothColor)).setNoHarvest().setNoPushMobility();
	public static final Material piston = (new Material(MapColor.stoneColor)).setImmovableMobility();
	public static final Material slime = (new Material(MapColor.foliageColor)).setIsTranslucent();
	public static final Material flesh = (new Material(MapColor.tntColor)).setNoPushMobility();
	public static final Material vine = new Material(MapColor.grassColor);
	public static final Material bone = (new Material(MapColor.iceColor));
	private boolean canBurn;
	private boolean groundCover;
	private boolean isOpaque;
	public final MapColor materialMapColor;
	private boolean canHarvest = true;
	private int mobilityFlag;

	public Material(MapColor mapColor1) {
		this.materialMapColor = mapColor1;
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
}
