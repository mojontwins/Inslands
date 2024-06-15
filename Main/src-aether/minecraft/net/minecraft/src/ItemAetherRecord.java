package net.minecraft.src;

public class ItemAetherRecord extends ItemRecord {
	protected ItemAetherRecord(int i, String s) {
		super(i, s);
	}

	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l) {
		if(world.getBlockId(i, j, k) == Block.jukebox.blockID && world.getBlockMetadata(i, j, k) == 0) {
			if(world.multiplayerWorld) {
				return true;
			} else {
				((BlockJukeBox)Block.jukebox).ejectRecord(world, i, j, k, this.shiftedIndex);
				world.func_28107_a((EntityPlayer)null, 1005, i, j, k, this.shiftedIndex);
				ModLoader.getMinecraftInstance().ingameGUI.setRecordPlayingMessage("Noisestorm - " + this.recordName);
				--itemstack.stackSize;
				return true;
			}
		} else {
			return false;
		}
	}
}
