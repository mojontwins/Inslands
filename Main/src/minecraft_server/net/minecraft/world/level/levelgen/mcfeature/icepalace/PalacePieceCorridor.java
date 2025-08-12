package net.minecraft.world.level.levelgen.mcfeature.icepalace;

public class PalacePieceCorridor extends PalacePiece {

	public PalacePieceCorridor(boolean rotated, FeatureIcePalace featureIcePalace) {
		super(rotated, featureIcePalace);
	}

	@Override
	public short[][][] getPiece() {
		return new short [][][] {
			{
				{ 165, 165, 165}, 
				{ 79 | (1 << 8), 0, 0}, 
				{ 165, 165, 165}, 
			},
			{
				{ 165, 85, 0}, 
				{ 166, 0, 0}, 
				{ 165, 85, 0}, 
			},
			{
				{ 165, 85, 0}, 
				{ 79 | (1 << 8), 0, 0}, 
				{ 165, 85, 0}, 
			},
			{
				{ 165, 85, 0}, 
				{ 147, 0, 0}, 
				{ 165, 85, 0}, 
			},
			{
				{ 165, 85, 0}, 
				{ 79 | (1 << 8), 0, 0}, 
				{ 165, 85, 0}, 
			},
			{
				{ 165, 85, 0}, 
				{ 166, 0, 0}, 
				{ 165, 85, 0}, 
			},
			{
				{ 165, 165, 165}, 
				{ 79 | (1 << 8), 0, 0}, 
				{ 165, 165, 165}, 
			},
		};
	}

}
