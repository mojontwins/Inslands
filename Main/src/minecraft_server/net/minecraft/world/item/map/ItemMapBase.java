package net.minecraft.world.item.map;

import net.minecraft.network.packet.Packet;
import net.minecraft.src.World;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemMapBase extends Item {
	protected ItemMapBase(int i1) {
		super(i1);
	}

	public boolean hasContents() {
		return true;
	}

	public Packet s_func_28022_b(ItemStack itemStack1, World world2, EntityPlayer entityPlayer3) {
		return null;
	}
}
