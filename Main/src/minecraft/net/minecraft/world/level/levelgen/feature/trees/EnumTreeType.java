package net.minecraft.world.level.levelgen.feature.trees;

import java.util.Random;

import net.minecraft.world.level.BlockState;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.tile.Block;

public enum EnumTreeType {
	OAK("Oak", new BlockState(Block.leaves, 0), new BlockState(Block.wood, 0), new BlockState(Block.sapling, 0)) {
		@Override
		public WorldGenerator getGen(Random rand) {
			return rand.nextInt(8) == 0 ? new WorldGenBigTree() : new WorldGenTrees();
		}
	},
	
	BIRCH("Birch", new BlockState(Block.leaves, 0x10), new BlockState(Block.wood, 0x10), new BlockState(Block.sapling, 0x10)) {
		@Override
		public WorldGenerator getGen(Random rand) {
			return new WorldGenForest();
		}
	},
	
	TAIGA("Taiga", new BlockState(Block.leaves, 0x20), new BlockState(Block.wood, 0x20), new BlockState(Block.sapling, 0x20)) {
		@Override
		public WorldGenerator getGen(Random rand) {
			return rand.nextBoolean() ? new WorldGenTaiga1() : new WorldGenTaiga2();
		}
	},
	
	FIR("Fir", new BlockState(Block.leaves, 0x30), new BlockState(Block.wood, 0x30), new BlockState(Block.sapling, 0x30)) {
		@Override
		public WorldGenerator getGen(Random rand) {
			if(rand.nextInt(10) == 0) return new WorldGenFir(4 + rand.nextInt(7), true);
			else return new WorldGenFir(3 + rand.nextInt(3), false);
		}
	},
	
	BAOBAB("Baobab", new BlockState(Block.leaves, 0x40), new BlockState(Block.wood, 0x40), new BlockState(Block.sapling, 0x40)) {
		@Override
		public WorldGenerator getGen(Random rand) {
			return rand.nextInt(10) == 0 ? new WorldGenBaobab(6 + rand.nextInt(7)) : new WorldGenBaobab(2 + rand.nextInt(3));
		}
	},
	
	BOG("Bog", new BlockState(Block.leaves, 0x50), new BlockState(Block.wood, 0x50), new BlockState(Block.sapling, 0x50)) {
		@Override
		public WorldGenerator getGen(Random rand) {
			return new WorldGenBog1();
		}
	},
	
	CYPRESS("Cypress", new BlockState(Block.leaves, 0x60), new BlockState(Block.wood, 0x60), new BlockState(Block.sapling, 0x60)) {
		@Override
		public WorldGenerator getGen(Random rand) {
			if(rand.nextInt(32) == 0) return new WorldGenCypress(5+rand.nextInt(5));
			return null;
		}
	},
	
	HUGE("Huge", new BlockState(Block.leaves, 0x70), new BlockState(Block.wood, 0x70), new BlockState(Block.sapling, 0x70)) {
		@Override
		public WorldGenerator getGen(Random rand) {
			if(rand.nextInt(64) == 0) return new WorldGenHugeTrees(16 + rand.nextInt(16));
			return null;
		}
	},
	
	PALM("Palm", new BlockState(Block.leaves, 0x80), new BlockState(Block.wood, 0x80), new BlockState(Block.sapling, 0x80)) {
		@Override
		public WorldGenerator getGen(Random rand) {
			return new WorldGenPalmTree(false);
		}
	},
	
	PINE("Pine", new BlockState(Block.leaves, 0x90), new BlockState(Block.wood, 0x90), new BlockState(Block.sapling, 0x90)) {
		@Override
		public WorldGenerator getGen(Random rand) {
			if(rand.nextInt(32) == 0) return new WorldGenPineTree(6 + rand.nextInt(8), false);
			return null;
		}
	},
	
	SHRUB("Shrub", new BlockState(Block.leaves, 0xA0), new BlockState(Block.wood, 0xA0), new BlockState(Block.sapling, 0xA0)) {
		@Override
		public WorldGenerator getGen(Random rand) {
			return new WorldGenShrub();
		}
	},
	
	SWAMP("Swamp", new BlockState(Block.leaves, 0xB0), new BlockState(Block.wood, 0xB0), new BlockState(Block.sapling, 0xB0)) {
		@Override
		public WorldGenerator getGen(Random rand) {
			return new WorldGenSwamp();
		}
	},
	
	WILLOW("Willow", new BlockState(Block.leaves, 0xC0), new BlockState(Block.wood, 0xC0), new BlockState(Block.sapling, 0xC0)) {
		@Override
		public WorldGenerator getGen(Random rand) {
			return new WorldGenWillow(4 + rand.nextInt(4));
		}
	},
	
	CANOPY("Canopy", new BlockState(Block.leaves, 0xD0), new BlockState(Block.wood, 0xD0), new BlockState(Block.sapling, 0xD0)) {
		@Override
		public WorldGenerator getGen(Random rand) {
			return new TFGenCanopyTree();
		}
	},
	
	MANGROVE("Mangrove", new BlockState(Block.leaves, 0xE0), new BlockState(Block.wood, 0xE0), new BlockState(Block.sapling, 0xE0)) {
		@Override
		public WorldGenerator getGen(Random rand) {
			return new WorldGenMangrove(false);
		}
	},
	
	BLOOD("Blood", new BlockState(Block.leaves2, 0x0), new BlockState(Block.wood2, 0x0), new BlockState(Block.sapling2, 0x0)) {
		@Override
		public WorldGenerator getGen(Random rand) {
			return new TFGenCanopyTree();
		}
	},
	;
	
	public final BlockState leaves;
	public final BlockState wood;
	public final BlockState sapling;
	
	public final String name;
	
	public WorldGenerator getGen(Random rand) {
		return new WorldGenTrees();
	}
	
	public static BlockState getSaplingFromLeaves(BlockState leaves) {
		return new BlockState(Block.sapling, leaves.getMetadata());
	}
	
	public static EnumTreeType findTreeTypeFromLeaves(BlockState leaves) {
		for(EnumTreeType e : EnumTreeType.values()) {
			if(leaves.getBlock().blockID == e.leaves.getBlock().blockID && leaves.getMetadata() == e.leaves.getMetadata()) {
				return e;
			}
		}
		
		return OAK;
	}
	
	public static EnumTreeType findTreeTypeFromSapling(BlockState sapling) {
		for(EnumTreeType e : EnumTreeType.values()) {
			if(sapling.getBlock().blockID == e.sapling.getBlock().blockID && sapling.getMetadata() == e.sapling.getMetadata()) {
				return e;
			}
		}
		
		return OAK;
	}
	
	
	EnumTreeType(String name, BlockState leaves, BlockState wood, BlockState sapling) {
		this.name = name;
		this.leaves = leaves;
		this.wood = wood;
		this.sapling = sapling;
	}
	
}
