package net.minecraft.world.level.tile;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.monster.EntitySpider;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.World;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.MovingObjectPosition;
import net.minecraft.world.phys.Vec3D;

public class BlockTFHedge extends Block {
	public int damageDone;
	public static final int hedgeColors[] = { 0x508F3D, 0x3D6345 };
	
	public BlockTFHedge(int i) {
		super(i, Material.cactus);
		this.blockIndexInTexture = 12*16+1;
		this.damageDone = 3;
		this.setHardness(5.0F);
		this.setResistance(20.0F);
		this.setStepSound(Block.soundGrassFootstep);
		this.displayOnCreativeTab = CreativeTabs.tabDeco;
	}
	
	@Override
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		return this.getRenderColor(world.getBlockMetadata(x, y, z));
	}

	@Override
	public int getRenderColor(int meta) {
		return BlockTFHedge.hedgeColors[meta];
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z); 
		switch(meta) {
		case 0:
			float f = 0.0625F;
			return AxisAlignedBB.getBoundingBoxFromPool((double)((float)x), (double)y, (double)((float)z), (double)((float)(x + 1)), (double)((float)(y + 1) - f), (double)((float)(z + 1)));
		case 1:
		default:
			return AxisAlignedBB.getBoundingBoxFromPool((double)((float)x), (double)y, (double)((float)z), (double)((float)(x + 1)), (double)((float)(y + 1)), (double)((float)(z + 1)));
		}
	}

	@Override
	protected int damageDropped(int meta) {
		return meta;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		int meta = world.getBlockMetadata(x, y, z);
		if(meta == 0 && this.shouldDamage(entity)) {
			entity.attackEntityFrom(null, this.damageDone);
		}

	}

	@Override
	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
		int meta = world.getBlockMetadata(x, y, z);
		if(meta == 0 && this.shouldDamage(entity)) {
			entity.attackEntityFrom(null, this.damageDone);
		}

	}

	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer entityplayer) {
		int meta = world.getBlockMetadata(x, y, z);
		if(meta == 0 && !world.isRemote) {
			world.scheduleBlockUpdate(x, y, z, this.blockID, 10);
		}

	}

	@Override
	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int meta) {
		super.harvestBlock(world, entityplayer, i, j, k, meta);
		if(meta == 0) {
			entityplayer.attackEntityFrom(null, this.damageDone);
		}

	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		double range = 4.0D;
		List<Entity> nearbyPlayers = world.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBoxFromPool((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1)).expand(range, range, range));
		Iterator<Entity> i$ = nearbyPlayers.iterator();

		while(i$.hasNext()) {
			EntityPlayer player = (EntityPlayer)i$.next();
			if(player.isSwinging) {
				MovingObjectPosition mop = this.getPlayerPointVec(world, player, range);
				if(mop != null && world.getBlockId(mop.blockX, mop.blockY, mop.blockZ) == this.blockID) {
					player.attackEntityFrom(null, this.damageDone);
					world.scheduleBlockUpdate(x, y, z, this.blockID, 10);
				}
			}
		}

	}

	private MovingObjectPosition getPlayerPointVec(World worldObj, EntityPlayer player, double range) {
		Vec3D position = Vec3D.createVector(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
		Vec3D look = player.getLook(1.0F);
		Vec3D dest = position.addVector(look.xCoord * range, look.yCoord * range, look.zCoord * range);
		return worldObj.rayTraceBlocks(position, dest);
	}

	private boolean shouldDamage(Entity entity) {
		return !(entity instanceof EntitySpider) && !(entity instanceof EntityItem);
	}
	
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));

	}
}
