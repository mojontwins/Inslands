package net.minecraft.client.renderer.block;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.level.tile.Block;

public interface BlockRenderHandler {
	boolean renderBlock(RenderBlocks renderBlocks, Block block, int x, int y, int z);

	default void renderBlockOnInventory(RenderBlocks renderBlocks, Block block, int meta, float brightness) {
		RenderBlockUtil.renderCubeOnInventory(renderBlocks, block, meta, brightness);
	}

	default boolean renderItemIn3d() {
		return true;
	}
}