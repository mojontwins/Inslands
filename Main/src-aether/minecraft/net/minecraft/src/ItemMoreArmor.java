package net.minecraft.src;

public class ItemMoreArmor extends Item {
	private static final int[] damageReduceAmountArray = new int[]{3, 8, 6, 3, 0, 1, 0, 0, 0, 0, 2, 0};
	private static final int[] maxDamageArray = new int[]{11, 16, 15, 13, 10, 10, 8, 10, 10, 10, 10, 10};
	public final int armorLevel;
	public final int armorType;
	public final int damageReduceAmount;
	public final int renderIndex;
	private final int colour;
	public String texture;
	public boolean colouriseRender;

	public ItemMoreArmor(int i, int j, int k, int l, int col) {
		super(i);
		this.armorLevel = j;
		this.armorType = l;
		this.renderIndex = k;
		this.damageReduceAmount = damageReduceAmountArray[l];
		this.setMaxDamage(maxDamageArray[l] * 3 << j);
		this.maxStackSize = 1;
		this.colour = col;
		this.colouriseRender = true;
		this.texture = "/armor/Accessories.png";
	}

	public ItemMoreArmor(int i, int j, int k, int l) {
		this(i, j, k, l, 0xFFFFFF);
	}

	public ItemMoreArmor(int i, int j, String path, int l) {
		this(i, j, 0, l);
		this.texture = path;
	}

	public ItemMoreArmor(int i, int j, String path, int l, int m) {
		this(i, j, 0, l, m);
		this.texture = path;
	}

	public ItemMoreArmor(int i, int j, String path, int l, int m, boolean flag) {
		this(i, j, path, l, m);
		this.colouriseRender = flag;
	}

	public boolean isTypeValid(int i) {
		return i == this.armorType ? true : (i != 8 && i != 9 || this.armorType != 8 && this.armorType != 9 ? (i == 7 || i == 11) && (this.armorType == 7 || this.armorType == 11) : true);
	}

	public boolean damageType() {
		return this.damageType(this.armorType);
	}

	public boolean damageType(int i) {
		return i < 4 || i == 6 || i == 10;
	}

	public int getColorFromDamage(int i) {
		return this.colour;
	}
}
