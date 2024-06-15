package net.minecraft.src;

public class BlockAercloud extends Block {
	public static final int bouncingMeta = 1;

	protected BlockAercloud(int blockID) {
		super(blockID, ModLoader.addOverride("/terrain.png", "/aether/blocks/Aercloud.png"), Material.ice);
	}

	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		entity.fallDistance = 0.0F;
		if(world.getBlockMetadata(i, j, k) == 1) {
			entity.motionY = 2.0D;
			if(entity instanceof EntityPlayer) {
				mod_Aether.giveAchievement(AetherAchievements.blueCloud, (EntityPlayer)entity);
			}
		} else if(entity.motionY < 0.0D) {
			entity.motionY *= 0.005D;
		}

	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderBlockPass() {
		return 1;
	}

	protected int damageDropped(int i) {
		return i;
	}

	public int getRenderColor(int i) {
		return i == 1 ? 3714284 : (i == 2 ? 16777088 : 0xFFFFFF);
	}

	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
		return this.getRenderColor(iblockaccess.getBlockMetadata(i, j, k));
	}

	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return super.shouldSideBeRendered(iblockaccess, i, j, k, 1 - l);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return world.getBlockMetadata(i, j, k) == 1 ? AxisAlignedBB.getBoundingBoxFromPool((double)i, (double)j, (double)k, (double)i, (double)j, (double)k) : AxisAlignedBB.getBoundingBoxFromPool((double)i, (double)j, (double)k, (double)(i + 1), (double)j, (double)(k + 1));
	}
}
