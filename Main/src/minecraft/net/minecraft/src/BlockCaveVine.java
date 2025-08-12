package net.minecraft.src;

import java.util.Random;

import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.creative.CreativeTabs;

public class BlockCaveVine extends Block {
	public BlockCaveVine(int par1) {
		super(par1, Material.vine);
		this.blockIndexInTexture = 11*16+2;
		this.setLightValue(0.5F);
		
		this.displayOnCreativeTab = CreativeTabs.tabDeco;
	}

	/**
	 * Sets the block's bounds for rendering it as an item
	 */
	@Override
	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType() {
		return 1; // was : 20;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether or
	 * not to render the shared face of two adjacent blocks and also whether the
	 * player can attach torches, redstone wire, etc to this block.
	 */
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	/*
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
		int var6 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
		float var7 = 1.0F;
		float var8 = 1.0F;
		float var9 = 1.0F;
		float var10 = 0.0F;
		float var11 = 0.0F;
		float var12 = 0.0F;
		boolean var13 = var6 > 0;

		if ((var6 & 2) != 0) {
			var10 = Math.max(var10, 0.0625F);
			var7 = 0.0F;
			var8 = 0.0F;
			var11 = 1.0F;
			var9 = 0.0F;
			var12 = 1.0F;
			var13 = true;
		}

		if ((var6 & 8) != 0) {
			var7 = Math.min(var7, 0.9375F);
			var10 = 1.0F;
			var8 = 0.0F;
			var11 = 1.0F;
			var9 = 0.0F;
			var12 = 1.0F;
			var13 = true;
		}

		if ((var6 & 4) != 0) {
			var12 = Math.max(var12, 0.0625F);
			var9 = 0.0F;
			var7 = 0.0F;
			var10 = 1.0F;
			var8 = 0.0F;
			var11 = 1.0F;
			var13 = true;
		}

		if ((var6 & 1) != 0) {
			var9 = Math.min(var9, 0.9375F);
			var12 = 1.0F;
			var7 = 0.0F;
			var10 = 1.0F;
			var8 = 0.0F;
			var11 = 1.0F;
			var13 = true;
		}

		if (!var13 && this.canBePlacedOn(par1IBlockAccess.getBlockId(par2, par3 + 1, par4))) {
			var8 = Math.min(var8, 0.9375F);
			var11 = 1.0F;
			var7 = 0.0F;
			var10 = 1.0F;
			var9 = 0.0F;
			var12 = 1.0F;
		}

		this.setBlockBounds(var7, var8, var9, var10, var11, var12);
	}
	*/

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this box
	 * can change after the pool has been cleared to be reused)
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return null;
	}

	/**
	 * checks to see if you can place this block can be placed on that side of a
	 * block: BlockLever overrides
	 */
	@Override
	public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5) {
		switch (par5) {
		case 1:
			return this.canBePlacedOn(par1World.getBlockId(par2, par3 + 1, par4));

		case 2:
			return this.canBePlacedOn(par1World.getBlockId(par2, par3, par4 + 1));

		case 3:
			return this.canBePlacedOn(par1World.getBlockId(par2, par3, par4 - 1));

		case 4:
			return this.canBePlacedOn(par1World.getBlockId(par2 + 1, par3, par4));

		case 5:
			return this.canBePlacedOn(par1World.getBlockId(par2 - 1, par3, par4));

		default:
			return false;
		}
	}

	/**
	 * returns true if a vine can be placed on that block (checks for render as
	 * normal block and if it is solid)
	 */
	private boolean canBePlacedOn(int par1) {
		if(par1 == this.blockID) return true;
		if (par1 == 0)
			return false;
		else {
			Block var2 = Block.blocksList[par1];
			return var2.renderAsNormalBlock() && var2.blockMaterial.blocksMovement();
		}
	}

	/**
	 * Returns if the vine can stay in the world. It also changes the metadata
	 * according to neighboring blocks.
	 */
	private boolean canVineStay(World par1World, int par2, int par3, int par4) {
		return this.canBePlacedOn(par1World.getBlockId(par2, par3 + 1, par4));
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z, neighbor
	 * blockID
	 */
	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		if (!par1World.isRemote && !this.canVineStay(par1World, par2, par3, par4)) {
			par1World.setBlock(par2, par3, par4, 0);
		}
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
	}

	/**
	 * called before onBlockPlacedBy by ItemBlock and ItemReed
	 */
	@Override
	public void onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7,
			float par8) {
		byte var9 = 0;

		switch (par5) {
		case 2:
			var9 = 1;
			break;

		case 3:
			var9 = 4;
			break;

		case 4:
			var9 = 8;
			break;

		case 5:
			var9 = 2;
		}

		if (var9 != 0) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, var9);
		}
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	@Override
	public int idDropped(int par1, Random par2Random) {
		return 0;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random par1Random) {
		return 0;
	}

	/**
	 * Called when the player destroys a block with an item that can harvest it. (i,
	 * j, k) are the coordinates of the block and l is the block's subtype/damage.
	 */
	@Override
	public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6) {
		super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
	}

	@Override
	public boolean isClimbable() {
		return true;
	}
	
	public boolean seeThrough() {
		return true; 
	}

}
