package net.minecraft.game.world.block;

import java.util.Random;

import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.player.EntityPlayer;
import net.minecraft.game.entity.player.InventoryPlayer;
import net.minecraft.game.item.Item;
import net.minecraft.game.item.ItemBlock;
import net.minecraft.game.item.ItemStack;
import net.minecraft.game.physics.AxisAlignedBB;
import net.minecraft.game.physics.MovingObjectPosition;
import net.minecraft.game.physics.Vec3D;
import net.minecraft.game.world.World;
import net.minecraft.game.world.material.Material;

public class Block {
	private static StepSound soundPowderFootstep = new StepSound("stone", 1.0F, 1.0F);
	private static StepSound soundWoodFootstep = new StepSound("wood", 1.0F, 1.0F);
	private static StepSound soundGravelFootstep = new StepSound("gravel", 1.0F, 1.0F);
	private static StepSound soundGrassFootstep = new StepSound("grass", 1.0F, 1.0F);
	private static StepSound soundStoneFootstep = new StepSound("stone", 1.0F, 1.0F);
	private static StepSound soundMetalFootstep = new StepSound("stone", 1.0F, 1.5F);
	private static StepSound soundGlassFootstep = new StepSoundGlass("stone", 1.0F, 1.0F);
	private static StepSound soundClothFootstep = new StepSound("cloth", 1.0F, 1.0F);
	private static StepSound soundSandFootstep = new StepSoundSand("sand", 1.0F, 1.0F);
	public static final Block[] blocksList = new Block[256];
	public static final boolean[] tickOnLoad = new boolean[256];
	public static final boolean[] opaqueCubeLookup = new boolean[256];
	public static final int[] lightOpacity = new int[256];
	private static boolean[] canBlockGrass = new boolean[256];
	public static final boolean[] isBlockContainer = new boolean[256];
	public static final int[] lightValue = new int[256];
	public static final Block stone;
	public static final BlockGrass grass;
	public static final Block dirt;
	public static final Block cobblestone;
	public static final Block planks;
	public static final Block sapling;
	public static final Block bedrock;
	public static final Block waterMoving;
	public static final Block waterStill;
	public static final Block lavaMoving;
	public static final Block lavaStill;
	public static final Block sand;
	public static final Block gravel;
	public static final Block oreGold;
	public static final Block oreIron;
	public static final Block oreCoal;
	public static final Block wood;
	public static final Block leaves;
	public static final Block sponge;
	public static final Block glass;
	public static final Block clothRed;
	public static final Block clothOrange;
	public static final Block clothYellow;
	public static final Block clothChartreuse;
	public static final Block clothGreen;
	public static final Block clothSpringGreen;
	public static final Block clothCyan;
	public static final Block clothCapri;
	public static final Block clothUltramarine;
	public static final Block clothViolet;
	public static final Block clothPurple;
	public static final Block clothMagenta;
	public static final Block clothRose;
	public static final Block clothDarkGray;
	public static final Block clothGray;
	public static final Block clothWhite;
	public static final BlockFlower plantYellow;
	public static final BlockFlower plantRed;
	public static final BlockFlower mushroomBrown;
	public static final BlockFlower mushroomRed;
	public static final Block blockGold;
	public static final Block blockSteel;
	public static final Block stairDouble;
	public static final Block stairSingle;
	public static final Block brick;
	public static final Block tnt;
	public static final Block bookshelf;
	public static final Block cobblestoneMossy;
	public static final Block obsidian;
	public static final Block torch;
	public static final BlockFire fire;
	public static final Block chest;
	public static final Block cog;
	public static final Block oreDiamond;
	public static final Block blockDiamond;
	public static final Block workbench;
	public static final Block crops;
	public static final Block tilledField;
	public static final Block stoneOvenIdle;
	public static final Block stoneOvenActive;
	public int blockIndexInTexture;
	public final int blockID;
	private float blockHardness;
	private float blockResistance;
	public double minX;
	public double minY;
	public double minZ;
	public double maxX;
	public double maxY;
	public double maxZ;
	public StepSound stepSound;
	public float blockParticleGravity;
	public final Material blockMaterial;

	protected Block(int i1, Material material2) {
		this.stepSound = soundPowderFootstep;
		this.blockParticleGravity = 1.0F;
		if(blocksList[i1] != null) {
			throw new IllegalArgumentException("Slot " + i1 + " is already occupied by " + blocksList[i1] + " when adding " + this);
		} else {
			this.blockMaterial = material2;
			blocksList[i1] = this;
			this.blockID = i1;
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			opaqueCubeLookup[i1] = this.isOpaqueCube();
			lightOpacity[i1] = this.isOpaqueCube() ? 255 : 0;
			canBlockGrass[i1] = this.renderAsNormalBlock();
			isBlockContainer[i1] = false;
		}
	}

	protected Block(int i1, int i2, Material material3) {
		this(i1, material3);
		this.blockIndexInTexture = i2;
	}

	protected final Block setLightOpacity(int i1) {
		lightOpacity[this.blockID] = i1;
		return this;
	}

	private Block setLightValue(float f1) {
		lightValue[this.blockID] = (int)(15.0F * f1);
		return this;
	}

	protected final Block setResistance(float f1) {
		this.blockResistance = f1 * 3.0F;
		return this;
	}

	public boolean renderAsNormalBlock() {
		return true;
	}

	public int getRenderType() {
		return 0;
	}

	protected final Block setHardness(float f1) {
		this.blockHardness = f1;
		if(this.blockResistance < f1 * 5.0F) {
			this.blockResistance = f1 * 5.0F;
		}

		return this;
	}

	protected final void setTickOnLoad(boolean z1) {
		tickOnLoad[this.blockID] = z1;
	}

	protected final void setBlockBounds(float f1, float f2, float f3, float f4, float f5, float f6) {
		this.minX = (double)f1;
		this.minY = (double)f2;
		this.minZ = (double)f3;
		this.maxX = (double)f4;
		this.maxY = (double)f5;
		this.maxZ = (double)f6;
	}

	public float getBlockBrightness(World world1, int i2, int i3, int i4) {
		return world1.getBrightness(i2, i3, i4);
	}

	public boolean shouldSideBeRendered(World world1, int i2, int i3, int i4, int i5) {
		return !world1.isSolid(i2, i3, i4);
	}

	public int getBlockTexture(World world1, int i2, int i3, int i4, int i5) {
		return this.getBlockTextureFromSideAndMetadata(i5, world1.getBlockMetadata(i2, i3, i4));
	}

	public int getBlockTextureFromSideAndMetadata(int i1, int i2) {
		return this.getBlockTextureFromSide(i1);
	}

	public int getBlockTextureFromSide(int i1) {
		return this.blockIndexInTexture;
	}

	public final AxisAlignedBB getSelectedBoundingBoxFromPool(int i1, int i2, int i3) {
		return new AxisAlignedBB((double)i1 + this.minX, (double)i2 + this.minY, (double)i3 + this.minZ, (double)i1 + this.maxX, (double)i2 + this.maxY, (double)i3 + this.maxZ);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(int i1, int i2, int i3) {
		return new AxisAlignedBB((double)i1 + this.minX, (double)i2 + this.minY, (double)i3 + this.minZ, (double)i1 + this.maxX, (double)i2 + this.maxY, (double)i3 + this.maxZ);
	}

	public boolean isOpaqueCube() {
		return true;
	}

	public boolean isCollidable() {
		return true;
	}

	public void updateTick(World world1, int i2, int i3, int i4, Random random5) {
	}

	public void randomDisplayTick(World world1, int i2, int i3, int i4, Random random5) {
	}

	public void onBlockDestroyedByPlayer(World world1, int i2, int i3, int i4, int i5) {
	}

	public void onNeighborBlockChange(World world1, int i2, int i3, int i4, int i5) {
	}

	public int tickRate() {
		return 5;
	}

	public void onBlockAdded(World world1, int i2, int i3, int i4) {
	}

	public void onBlockRemoval(World world1, int i2, int i3, int i4) {
	}

	public int quantityDropped(Random random1) {
		return 1;
	}

	public int idDropped(int i1, Random random2) {
		return this.blockID;
	}

	public final float blockStrength(EntityPlayer entityPlayer1) {
		if(this.blockHardness < 0.0F) {
			return 0.0F;
		} else if(!entityPlayer1.canHarvestBlock(this)) {
			return 1.0F / this.blockHardness / 100.0F;
		} else {
			InventoryPlayer inventoryPlayer2 = (entityPlayer1 = entityPlayer1).inventory;
			float f4 = 1.0F;
			if(inventoryPlayer2.mainInventory[inventoryPlayer2.currentItem] != null) {
				f4 = 1.0F * inventoryPlayer2.mainInventory[inventoryPlayer2.currentItem].getItem().getStrVsBlock(this);
			}

			float f5 = f4;
			if(entityPlayer1.isInsideOfMaterial()) {
				f5 = f4 / 5.0F;
			}

			if(!entityPlayer1.onGround) {
				f5 /= 5.0F;
			}

			return f5 / this.blockHardness / 30.0F;
		}
	}

	public final void dropBlockAsItem(World world1, int i2, int i3, int i4, int i5) {
		this.dropBlockAsItemWithChance(world1, i2, i3, i4, i5, 1.0F);
	}

	public final void dropBlockAsItemWithChance(World world1, int i2, int i3, int i4, int i5, float f6) {
		int i7 = this.quantityDropped(world1.rand);

		for(int i8 = 0; i8 < i7; ++i8) {
			int i9;
			if(world1.rand.nextFloat() <= f6 && (i9 = this.idDropped(i5, world1.rand)) > 0) {
				double d10 = (double)(world1.rand.nextFloat() * 0.7F) + (double)0.15F;
				double d12 = (double)(world1.rand.nextFloat() * 0.7F) + (double)0.15F;
				double d14 = (double)(world1.rand.nextFloat() * 0.7F) + (double)0.15F;
				EntityItem entityItem16;
				(entityItem16 = new EntityItem(world1, (double)i2 + d10, (double)i3 + d12, (double)i4 + d14, new ItemStack(i9))).delayBeforeCanPickup = 10;
				world1.spawnEntityInWorld(entityItem16);
			}
		}

	}

	public final float getExplosionResistance() {
		return this.blockResistance / 5.0F;
	}

	public MovingObjectPosition collisionRayTrace(World world1, int i2, int i3, int i4, Vec3D vec3D5, Vec3D vec3D6) {
		vec3D5 = vec3D5.addVector((double)(-i2), (double)(-i3), (double)(-i4));
		vec3D6 = vec3D6.addVector((double)(-i2), (double)(-i3), (double)(-i4));
		Vec3D vec3D12 = vec3D5.getIntermediateWithXValue(vec3D6, this.minX);
		Vec3D vec3D7 = vec3D5.getIntermediateWithXValue(vec3D6, this.maxX);
		Vec3D vec3D8 = vec3D5.getIntermediateWithYValue(vec3D6, this.minY);
		Vec3D vec3D9 = vec3D5.getIntermediateWithYValue(vec3D6, this.maxY);
		Vec3D vec3D10 = vec3D5.getIntermediateWithZValue(vec3D6, this.minZ);
		vec3D6 = vec3D5.getIntermediateWithZValue(vec3D6, this.maxZ);
		if(!this.isVecInsideYZBounds(vec3D12)) {
			vec3D12 = null;
		}

		if(!this.isVecInsideYZBounds(vec3D7)) {
			vec3D7 = null;
		}

		if(!this.isVecInsideXZBounds(vec3D8)) {
			vec3D8 = null;
		}

		if(!this.isVecInsideXZBounds(vec3D9)) {
			vec3D9 = null;
		}

		if(!this.isVecInsideXYBounds(vec3D10)) {
			vec3D10 = null;
		}

		if(!this.isVecInsideXYBounds(vec3D6)) {
			vec3D6 = null;
		}

		Vec3D vec3D11 = null;
		if(vec3D12 != null) {
			vec3D11 = vec3D12;
		}

		if(vec3D7 != null && (vec3D11 == null || vec3D5.distance(vec3D7) < vec3D5.distance(vec3D11))) {
			vec3D11 = vec3D7;
		}

		if(vec3D8 != null && (vec3D11 == null || vec3D5.distance(vec3D8) < vec3D5.distance(vec3D11))) {
			vec3D11 = vec3D8;
		}

		if(vec3D9 != null && (vec3D11 == null || vec3D5.distance(vec3D9) < vec3D5.distance(vec3D11))) {
			vec3D11 = vec3D9;
		}

		if(vec3D10 != null && (vec3D11 == null || vec3D5.distance(vec3D10) < vec3D5.distance(vec3D11))) {
			vec3D11 = vec3D10;
		}

		if(vec3D6 != null && (vec3D11 == null || vec3D5.distance(vec3D6) < vec3D5.distance(vec3D11))) {
			vec3D11 = vec3D6;
		}

		if(vec3D11 == null) {
			return null;
		} else {
			byte b13 = -1;
			if(vec3D11 == vec3D12) {
				b13 = 4;
			}

			if(vec3D11 == vec3D7) {
				b13 = 5;
			}

			if(vec3D11 == vec3D8) {
				b13 = 0;
			}

			if(vec3D11 == vec3D9) {
				b13 = 1;
			}

			if(vec3D11 == vec3D10) {
				b13 = 2;
			}

			if(vec3D11 == vec3D6) {
				b13 = 3;
			}

			return new MovingObjectPosition(i2, i3, i4, b13, vec3D11.addVector((double)i2, (double)i3, (double)i4));
		}
	}

	private boolean isVecInsideYZBounds(Vec3D vec3D1) {
		return vec3D1 == null ? false : vec3D1.yCoord >= this.minY && vec3D1.yCoord <= this.maxY && vec3D1.zCoord >= this.minZ && vec3D1.zCoord <= this.maxZ;
	}

	private boolean isVecInsideXZBounds(Vec3D vec3D1) {
		return vec3D1 == null ? false : vec3D1.xCoord >= this.minX && vec3D1.xCoord <= this.maxX && vec3D1.zCoord >= this.minZ && vec3D1.zCoord <= this.maxZ;
	}

	private boolean isVecInsideXYBounds(Vec3D vec3D1) {
		return vec3D1 == null ? false : vec3D1.xCoord >= this.minX && vec3D1.xCoord <= this.maxX && vec3D1.yCoord >= this.minY && vec3D1.yCoord <= this.maxY;
	}

	public void onBlockDestroyedByExplosion(World world1, int i2, int i3, int i4) {
	}

	public int getRenderBlockPass() {
		return 0;
	}

	public boolean canPlaceBlockAt(World world1, int i2, int i3, int i4) {
		return true;
	}

	public boolean blockActivated(World world1, int i2, int i3, int i4, EntityPlayer entityPlayer5) {
		return false;
	}

	public void onEntityWalking(World world1, int i2, int i3, int i4) {
	}

	public void onBlockPlaced(World world1, int i2, int i3, int i4, int i5) {
	}

	static {
		Block block10000 = (new BlockStone(1, 1)).setHardness(1.5F).setResistance(10.0F);
		StepSound stepSound1 = soundStoneFootstep;
		Block block0 = block10000;
		block10000.stepSound = stepSound1;
		stone = block0;
		block10000 = (new BlockGrass(2)).setHardness(0.6F);
		stepSound1 = soundGrassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		grass = (BlockGrass)block0;
		block10000 = (new BlockDirt(3, 2)).setHardness(0.5F);
		stepSound1 = soundGravelFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		dirt = block0;
		block10000 = (new Block(4, 16, Material.rock)).setHardness(2.0F).setResistance(10.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		cobblestone = block0;
		block10000 = (new Block(5, 4, Material.wood)).setHardness(2.0F).setResistance(5.0F);
		stepSound1 = soundWoodFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		planks = block0;
		block10000 = (new BlockSapling(6, 15)).setHardness(0.0F);
		stepSound1 = soundGrassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		sapling = block0;
		block10000 = (new Block(7, 17, Material.rock)).setHardness(-1.0F).setResistance(6000000.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		bedrock = block0;
		waterMoving = (new BlockFlowing(8, Material.water)).setHardness(100.0F).setLightOpacity(3);
		waterStill = (new BlockStationary(9, Material.water)).setHardness(100.0F).setLightOpacity(3);
		lavaMoving = (new BlockFlowing(10, Material.lava)).setHardness(0.0F).setLightValue(1.0F).setLightOpacity(255);
		lavaStill = (new BlockStationary(11, Material.lava)).setHardness(100.0F).setLightValue(1.0F).setLightOpacity(255);
		block10000 = (new BlockSand(12, 18)).setHardness(0.5F);
		stepSound1 = soundSandFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		sand = block0;
		block10000 = (new BlockGravel(13, 19)).setHardness(0.6F);
		stepSound1 = soundGravelFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		gravel = block0;
		block10000 = (new BlockOre(14, 32)).setHardness(3.0F).setResistance(5.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		oreGold = block0;
		block10000 = (new BlockOre(15, 33)).setHardness(3.0F).setResistance(5.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		oreIron = block0;
		block10000 = (new BlockOre(16, 34)).setHardness(3.0F).setResistance(5.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		oreCoal = block0;
		block10000 = (new BlockLog(17)).setHardness(2.0F);
		stepSound1 = soundWoodFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		wood = block0;
		block10000 = (new BlockLeaves(18, 52)).setHardness(0.2F).setLightOpacity(1);
		stepSound1 = soundGrassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		leaves = block0;
		block10000 = (new BlockSponge(19)).setHardness(0.6F);
		stepSound1 = soundGrassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		sponge = block0;
		block10000 = (new BlockGlass(20, 49, Material.glass, false)).setHardness(0.3F);
		stepSound1 = soundGlassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		glass = block0;
		block10000 = (new Block(21, 64, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothRed = block0;
		block10000 = (new Block(22, 65, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothOrange = block0;
		block10000 = (new Block(23, 66, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothYellow = block0;
		block10000 = (new Block(24, 67, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothChartreuse = block0;
		block10000 = (new Block(25, 68, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothGreen = block0;
		block10000 = (new Block(26, 69, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothSpringGreen = block0;
		block10000 = (new Block(27, 70, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothCyan = block0;
		block10000 = (new Block(28, 71, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothCapri = block0;
		block10000 = (new Block(29, 72, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothUltramarine = block0;
		block10000 = (new Block(30, 73, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothViolet = block0;
		block10000 = (new Block(31, 74, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothPurple = block0;
		block10000 = (new Block(32, 75, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothMagenta = block0;
		block10000 = (new Block(33, 76, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothRose = block0;
		block10000 = (new Block(34, 77, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothDarkGray = block0;
		block10000 = (new Block(35, 78, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothGray = block0;
		block10000 = (new Block(36, 79, Material.cloth)).setHardness(0.8F);
		stepSound1 = soundClothFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		clothWhite = block0;
		block10000 = (new BlockFlower(37, 13)).setHardness(0.0F);
		stepSound1 = soundGrassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		plantYellow = (BlockFlower)block0;
		block10000 = (new BlockFlower(38, 12)).setHardness(0.0F);
		stepSound1 = soundGrassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		plantRed = (BlockFlower)block0;
		block10000 = (new BlockMushroom(39, 29)).setHardness(0.0F);
		stepSound1 = soundGrassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		mushroomBrown = (BlockFlower)block0.setLightValue(0.125F);
		block10000 = (new BlockMushroom(40, 28)).setHardness(0.0F);
		stepSound1 = soundGrassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		mushroomRed = (BlockFlower)block0;
		block10000 = (new BlockOreStorage(41, 39)).setHardness(3.0F).setResistance(10.0F);
		stepSound1 = soundMetalFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		blockGold = block0;
		block10000 = (new BlockOreStorage(42, 38)).setHardness(5.0F).setResistance(10.0F);
		stepSound1 = soundMetalFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		blockSteel = block0;
		block10000 = (new BlockStep(43, true)).setHardness(2.0F).setResistance(10.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		stairDouble = block0;
		block10000 = (new BlockStep(44, false)).setHardness(2.0F).setResistance(10.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		stairSingle = block0;
		block10000 = (new Block(45, 7, Material.rock)).setHardness(2.0F).setResistance(10.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		brick = block0;
		block10000 = (new BlockTNT(46, 8)).setHardness(0.0F);
		stepSound1 = soundGrassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		tnt = block0;
		block10000 = (new BlockBookshelf(47, 35)).setHardness(1.5F);
		stepSound1 = soundWoodFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		bookshelf = block0;
		block10000 = (new Block(48, 36, Material.rock)).setHardness(2.0F).setResistance(10.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		cobblestoneMossy = block0;
		block10000 = (new BlockStone(49, 37)).setHardness(10.0F).setResistance(10.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		obsidian = block0;
		block10000 = (new BlockTorch(50, 80)).setHardness(0.0F).setLightValue(0.9375F);
		stepSound1 = soundWoodFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		torch = block0;
		block10000 = (new BlockFire(51, 31)).setHardness(0.0F).setLightValue(1.0F);
		stepSound1 = soundWoodFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		fire = (BlockFire)block0;
		block10000 = (new BlockSource(52, waterMoving.blockID)).setHardness(0.0F);
		stepSound1 = soundWoodFootstep;
		block10000.stepSound = stepSound1;
		block10000 = (new BlockSource(53, lavaMoving.blockID)).setHardness(0.0F);
		stepSound1 = soundWoodFootstep;
		block10000.stepSound = stepSound1;
		block10000 = (new BlockChest(54)).setHardness(2.5F);
		stepSound1 = soundWoodFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		chest = block0;
		block10000 = (new BlockGears(55, 62)).setHardness(0.5F);
		stepSound1 = soundMetalFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		cog = block0;
		block10000 = (new BlockOre(56, 50)).setHardness(3.0F).setResistance(5.0F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		oreDiamond = block0;
		block10000 = (new BlockOreStorage(57, 40)).setHardness(5.0F).setResistance(10.0F);
		stepSound1 = soundMetalFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		blockDiamond = block0;
		block10000 = (new BlockWorkbench(58)).setHardness(2.5F);
		stepSound1 = soundWoodFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		workbench = block0;
		block10000 = (new BlockCrops(59, 88)).setHardness(0.0F);
		stepSound1 = soundGrassFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		crops = block0;
		block10000 = (new BlockFarmland(60)).setHardness(0.6F);
		stepSound1 = soundGravelFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		tilledField = block0;
		block10000 = (new BlockFurnace(61, false)).setHardness(3.5F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		stoneOvenIdle = block0;
		block10000 = (new BlockFurnace(62, true)).setHardness(3.5F);
		stepSound1 = soundStoneFootstep;
		block0 = block10000;
		block10000.stepSound = stepSound1;
		stoneOvenActive = block0.setLightValue(0.875F);

		for(int i2 = 0; i2 < 256; ++i2) {
			if(blocksList[i2] != null) {
				Item.itemsList[i2] = new ItemBlock(i2 - 256);
			}
		}

	}
}