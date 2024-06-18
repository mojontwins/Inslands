package net.minecraft.src;

public class TextureHDCustomFX extends TextureFX implements TextureHDFX {
	private TexturePackBase texturePackBase;
	private int tileWidth = 0;

	public TextureHDCustomFX(int index, int tileImage) {
		super(index);
		this.tileImage = tileImage;
		this.tileWidth = 16;
		this.imageData = null;
	}

	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	public void setTexturePackBase(TexturePackBase tpb) {
		this.texturePackBase = tpb;
	}

	public void onTick() {
	}
}
