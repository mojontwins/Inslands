package net.minecraft.world.item;

import java.util.List;
import java.util.Random;

import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldType;
import net.minecraft.world.level.creative.CreativeTabs;
import net.minecraft.world.level.theme.LevelThemeSettings;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockCloth;
import net.minecraft.world.level.tile.BlockWorldPortal;

public class ItemWorldPortal extends ItemBlock {

	public ItemWorldPortal(int id) {
		super(id);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public CreativeTabs getCreativeTab() {
		return this.displayOnCreativeTab;
	}

	@Override
	public int getPlacedBlockMetadata(int i) {
		return i;
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(int i = 0; i < 16; ++i) {
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	@Override
	public String getItemNameIS(ItemStack itemStack1) {
		return super.getItemName() + "." + ItemDye.dyeColorNames[BlockCloth.getBlockFromDye(itemStack1.getItemDamage())];
	}

	private int resolveThemeId(int woolColor, Random random) {
		switch(woolColor) {
			case 14: return 1;
			case 11: return 3;
			case 13: return 2;
			case 5:  return 5;
			case 7:  return 6;
			case 0:
				List<LevelThemeSettings> randomThemes = new java.util.ArrayList<LevelThemeSettings>();
				for(int i = 0; i < LevelThemeSettings.allThemeSettings.size(); i++) {
					LevelThemeSettings s = LevelThemeSettings.allThemeSettings.get(i);
					if(s != null && s.isRandomWorldTheme) randomThemes.add(s);
				}
				return randomThemes.isEmpty() ? 0 : randomThemes.get(random.nextInt(randomThemes.size())).id;
			default: return 4;
		}
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xWithinFace, float yWithinFace, float zWithinFace) {
		if(world == null) return false;
		if(world.worldProvider == null || world.worldProvider.isNether) return false;

		if(world.getBlockID(x, y, z) == Block.snow.blockID) {
			side = 0;
		} else {
			if(side == 0) --y;
			if(side == 1) ++y;
			if(side == 2) --z;
			if(side == 3) ++z;
			if(side == 4) --x;
			if(side == 5) ++x;
		}

		if(stack.stackSize == 0) {
			return false;
		} else if(y == 127 && Block.blocksList[this.blockID].blockMaterial.isSolid()) {
			return false;
		} else if(!world.canBlockBePlacedAt(this.blockID, x, y, z, false, side)) {
			return false;
		}

		Random random = new Random();

		int themeId = this.resolveThemeId(stack.getItemDamage(), random);
		if(themeId < 0 || themeId >= LevelThemeSettings.allThemeSettings.size()) return false;
		LevelThemeSettings themeSettings = LevelThemeSettings.allThemeSettings.get(themeId);
		if(themeSettings == null) return false;

		int worldTypeId;
		if(themeSettings.forcedWorldType && themeSettings.preferredWorldType >= 0 && themeSettings.preferredWorldType < WorldType.worldTypes.length) {
			worldTypeId = themeSettings.preferredWorldType;
		} else {
			List<Integer> creatable = new java.util.ArrayList<Integer>();
			for(int i = 0; i < WorldType.worldTypes.length; i++) {
				if(WorldType.worldTypes[i] != null && WorldType.worldTypes[i].getCanBeCreated()) creatable.add(i);
			}
			worldTypeId = creatable.isEmpty() ? WorldType.DEFAULT.id : creatable.get(random.nextInt(creatable.size()));
		}

		int sizeId = random.nextInt(5);

		int worldId = BlockWorldPortal.placeNewWorldPortal(world, x, y, z, themeId, sizeId, worldTypeId);
		if(worldId < 0) return false;

		Block block = Block.blocksList[this.blockID];
		world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block.stepSound.getStepSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
		if(!player.isCreative) --stack.stackSize;
		return true;
	}
}