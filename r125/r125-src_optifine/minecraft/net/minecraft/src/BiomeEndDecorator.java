package net.minecraft.src;

public class BiomeEndDecorator extends BiomeDecorator {
	protected WorldGenerator spikeGen = new WorldGenSpikes(Block.whiteStone.blockID);

	public BiomeEndDecorator(BiomeGenBase biomeGenBase1) {
		super(biomeGenBase1);
	}

	protected void decorate() {
		this.generateOres();
		if(this.randomGenerator.nextInt(5) == 0) {
			int i1 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
			int i2 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
			int i3 = this.currentWorld.getTopSolidOrLiquidBlock(i1, i2);
			if(i3 > 0) {
				;
			}

			this.spikeGen.generate(this.currentWorld, this.randomGenerator, i1, i3, i2);
		}

		if(this.chunk_X == 0 && this.chunk_Z == 0) {
			EntityDragon entityDragon4 = new EntityDragon(this.currentWorld);
			entityDragon4.setLocationAndAngles(0.0D, 128.0D, 0.0D, this.randomGenerator.nextFloat() * 360.0F, 0.0F);
			this.currentWorld.spawnEntityInWorld(entityDragon4);
		}

	}
}
