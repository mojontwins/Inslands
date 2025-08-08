package net.minecraft.client.skins;

import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.Minecraft;

public abstract class TexturePackBase {
	public String texturePackFileName;
	public String firstDescriptionLine;
	public String secondDescriptionLine;
	public String field_6488_d;

	public void readZipFile() {
	}

	public void closeTexturePackFile() {
	}

	public void readTexturePackInfo(Minecraft minecraft1) throws IOException {
	}

	public void closeTexturePack(Minecraft minecraft1) {
	}

	public void bindThumbnailTexture(Minecraft minecraft1) {
	}

	public InputStream getResourceAsStream(String string1) {
		return TexturePackBase.class.getResourceAsStream(string1);
	}
}
