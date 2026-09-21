package net.minecraft.client.renderer.block;

public class BlockRenderType {
	private int renderType;
	private BlockRenderHandler handler;

	private BlockRenderType(int renderType, BlockRenderHandler handler) {
		this.renderType = renderType;
		this.handler = handler;
	}

	public static BlockRenderType TYPE(int renderType, BlockRenderHandler handler) {
		return new BlockRenderType(renderType, handler);
	}

	public int renderType() {
		return this.renderType;
	}

	public BlockRenderHandler handler() {
		return this.handler;
	}

	private static BlockRenderType[] BY_RENDER_TYPE = new BlockRenderType[256];

	static {
		add(0, new RenderBlockNormal());
		add(1, new RenderBlockPlant());
		add(2, new RenderBlockTorch());
		add(3, new RenderBlockFire());
		add(4, new RenderBlockFluid());
		add(5, new RenderBlockRedstoneWire());
		add(6, new RenderBlockCrops());
		add(7, new RenderBlockDoor());
		add(8, new RenderBlockLadder());
		add(9, new RenderBlockRail());
		add(10, new RenderBlockStairs());
		add(11, new RenderBlockFence());
		add(12, new RenderBlockLever());
		add(13, new RenderBlockCactus());
		add(14, new RenderBlockBed());
		add(15, new RenderBlockRepeater());
		add(16, new RenderBlockLegacyPiston());
		add(18, new RenderBlockPane());
		add(20, new RenderBlockVine());
		add(23, new RenderBlockLilyPad());
		add(100, new RenderBlockWall());
		add(101, new RenderBlockBarbedWire());
		add(102, new RenderBlockHollowLog());
		add(103, new RenderBlockDirtPath());
		add(104, new RenderBlockStreetLantern());
		add(105, new RenderBlockChippedWood());
		add(106, new RenderBlockSlime());
		add(107, new RenderBlockAxisOriented());
		add(108, new RenderBlockWoodOriented());
		add(109, new RenderBlockClassicPiston());
		add(110, new RenderBlockChain());
		add(111, new RenderBlockSnowloggedPlant());
		add(112, new RenderBlockThinFeature());
		add(113, new RenderBlockWorldPortal());
		add(250, new RenderBlockModel());
	}

	private static void add(int renderType, BlockRenderHandler handler) {
		BlockRenderType type = TYPE(renderType, handler);
		if (renderType < BY_RENDER_TYPE.length) {
			BY_RENDER_TYPE[renderType] = type;
		}
	}

	public static BlockRenderHandler get(int renderType) {
		if (renderType == 255) {
			renderType = 0;
		}
		if (renderType < 0 || renderType >= BY_RENDER_TYPE.length) {
			return null;
		}
		BlockRenderType type = BY_RENDER_TYPE[renderType];
		return type != null ? type.handler() : null;
	}
}