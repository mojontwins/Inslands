package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Arrays;
import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GameSettings;
import net.minecraft.util.ChatAllowedCharacters;

public class FontRenderer {
	public static final int FONT_HEIGHT = 8;
	private static final char SECTION_SIGN = '\u00a7';
	private static final String FORMAT_CHARS = "0123456789abcdef";
	private static final int TEXTURE_SIZE = 128;
	private static final int CHAR_SIZE = 8;
	private static final int CHARS_PER_ROW = TEXTURE_SIZE / CHAR_SIZE;
	private static final int NUM_DISPLAY_LISTS = 256 + 32;

	private int[] charWidth = new int[256];
	private int[] charLookup = new int[256];
	public int fontTextureName = 0;
	private int fontDisplayLists;
	private IntBuffer buffer = GLAllocation.createDirectIntBuffer(1024);

	public FontRenderer(GameSettings gameSettings, String fontFile, RenderEngine renderEngine) {
		Arrays.fill(charLookup, -1);

		BufferedImage fontImage;
		try {
			fontImage = ImageIO.read(RenderEngine.class.getResourceAsStream(fontFile));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		int imageWidth = fontImage.getWidth();
		int imageHeight = fontImage.getHeight();
		int[] pixels = new int[imageWidth * imageHeight];
		fontImage.getRGB(0, 0, imageWidth, imageHeight, pixels, 0, imageWidth);

		for (int charIndex = 0; charIndex < 256; charIndex++) {
			int col = charIndex % CHARS_PER_ROW;
			int row = charIndex / CHARS_PER_ROW;

			int rightEdge = CHAR_SIZE - 1;
			for (; rightEdge >= 0; rightEdge--) {
				int pixelX = col * CHAR_SIZE + rightEdge;
				boolean columnEmpty = true;

				for (int checkY = 0; checkY < CHAR_SIZE && columnEmpty; checkY++) {
					int pixelIndex = (row * CHAR_SIZE + checkY) * imageWidth + pixelX;
					if ((pixels[pixelIndex] & 0xFF) > 0) {
						columnEmpty = false;
					}
				}

				if (!columnEmpty) {
					break;
				}
			}

			if (charIndex == ' ') {
				rightEdge = 2;
			}

			this.charWidth[charIndex] = rightEdge + 2;
		}

		this.fontTextureName = renderEngine.allocateAndSetupTexture(fontImage);
		this.fontDisplayLists = GLAllocation.generateDisplayLists(NUM_DISPLAY_LISTS);
		Tessellator tessellator = Tessellator.instance;

		for (int charIndex = 0; charIndex < 256; charIndex++) {
			GL11.glNewList(this.fontDisplayLists + charIndex, GL11.GL_COMPILE);
			tessellator.startDrawingQuads();
			int u = charIndex % CHARS_PER_ROW * CHAR_SIZE;
			int v = charIndex / CHARS_PER_ROW * CHAR_SIZE;
			float size = CHAR_SIZE - 0.01F;
			float texScale = 1.0F / TEXTURE_SIZE;

			tessellator.addVertexWithUV(0.0D, size, 0.0D, u * texScale, (v + size) * texScale);
			tessellator.addVertexWithUV(size, size, 0.0D, (u + size) * texScale, (v + size) * texScale);
			tessellator.addVertexWithUV(size, 0.0D, 0.0D, (u + size) * texScale, v * texScale);
			tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, u * texScale, v * texScale);
			tessellator.draw();
			GL11.glTranslatef(this.charWidth[charIndex], 0.0F, 0.0F);
			GL11.glEndList();
		}

		for (int colorIndex = 0; colorIndex < 32; colorIndex++) {
			int dark = (colorIndex >> 3 & 1) * 85;
			int red = (colorIndex >> 2 & 1) * 170 + dark;
			int green = (colorIndex >> 1 & 1) * 170 + dark;
			int blue = (colorIndex >> 0 & 1) * 170 + dark;

			if (colorIndex == 6) {
				red += 85;
			}

			boolean isDark = colorIndex >= 16;
			if (isDark) {
				red /= 4;
				green /= 4;
				blue /= 4;
			}

			if (gameSettings.anaglyph) {
				int r = (red * 30 + green * 59 + blue * 11) / 100;
				int g = (red * 30 + green * 70) / 100;
				int b = (red * 30 + blue * 70) / 100;
				red = r;
				green = g;
				blue = b;
			}

			GL11.glNewList(this.fontDisplayLists + 256 + colorIndex, GL11.GL_COMPILE);
			GL11.glColor3f(red / 255.0F, green / 255.0F, blue / 255.0F);
			GL11.glEndList();
		}

		String allowed = ChatAllowedCharacters.allowedCharacters;
		for (int i = 0; i < allowed.length(); i++) {
			char c = allowed.charAt(i);
			if (c < charLookup.length) {
				charLookup[c] = i;
			}
		}
	}

	public void drawStringWithShadow(String text, int x, int y, int color) {
		this.renderString(text, x + 1, y + 1, color, true);
		this.drawString(text, x, y, color);
	}

	public void drawCenteredString(String text, int x, int y, int color) {
		this.renderString(text, x - this.getStringWidth(text) / 2, y, color, false);
	}

	public void drawString(String text, int x, int y, int color) {
		this.renderString(text, x, y, color, false);
	}

	public void renderString(String text, int x, int y, int color, boolean shadow) {
		if (text == null) return;

		if (shadow) {
			int alpha = color & 0xFF000000;
			color = (color & 0x00FCFCFC) >> 2;
			color += alpha;
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fontTextureName);
		float r = (float)(color >> 16 & 255) / 255.0F;
		float g = (float)(color >> 8 & 255) / 255.0F;
		float b = (float)(color & 255) / 255.0F;
		float a = (float)(color >> 24 & 255) / 255.0F;
		if (a == 0.0F) a = 1.0F;

		GL11.glColor4f(r, g, b, a);
		this.buffer.clear();
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0.0F);

		String lowerText = null;

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);

			if (c == SECTION_SIGN && i + 1 < text.length()) {
				if (lowerText == null) {
					lowerText = text.toLowerCase();
				}
				int formatIndex = FORMAT_CHARS.indexOf(lowerText.charAt(i + 1));
				if (formatIndex < 0 || formatIndex > 15) {
					formatIndex = 15;
				}

				this.buffer.put(this.fontDisplayLists + 256 + formatIndex + (shadow ? 16 : 0));
				i++;
			} else {
				int lookupIndex = (c < charLookup.length) ? charLookup[c] : -1;
				if (lookupIndex >= 0) {
					this.buffer.put(this.fontDisplayLists + lookupIndex + 32);
				}
			}

			if (this.buffer.remaining() == 0) {
				this.buffer.flip();
				GL11.glCallLists(this.buffer);
				this.buffer.clear();
			}
		}

		this.buffer.flip();
		GL11.glCallLists(this.buffer);
		GL11.glPopMatrix();
	}

	public int getStringWidth(String text) {
		if (text == null) return 0;

		int width = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == SECTION_SIGN) {
				i++;
			} else {
				int lookupIndex = (c < charLookup.length) ? charLookup[c] : -1;
				if (lookupIndex >= 0) {
					width += this.charWidth[lookupIndex + 32];
				}
			}
		}

		return width;
	}

	public void drawSplitString(String text, int x, int y, int maxWidth, int color) {
		String[] lines = text.split("\n");
		if (lines.length > 1) {
			for (int i = 0; i < lines.length; i++) {
				this.drawSplitString(lines[i], x, y, maxWidth, color);
				y += this.splitStringHeight(lines[i], maxWidth);
			}
			return;
		}

		String[] words = text.split(" ");
		StringBuilder currentLine = new StringBuilder();
		int wordIndex = 0;

		while (wordIndex < words.length) {
			currentLine.setLength(0);
			currentLine.append(words[wordIndex++]).append(' ');

			while (wordIndex < words.length && this.getStringWidth(currentLine.toString() + words[wordIndex]) < maxWidth) {
				currentLine.append(words[wordIndex++]).append(' ');
			}

			String lineStr = currentLine.toString();
			while (this.getStringWidth(lineStr) > maxWidth) {
				int breakPos = 0;
				while (this.getStringWidth(lineStr.substring(0, breakPos + 1)) <= maxWidth) {
					breakPos++;
				}

				String segment = lineStr.substring(0, breakPos);
				if (segment.trim().length() > 0) {
					this.drawString(segment, x, y, color);
					y += 8;
				}
				lineStr = lineStr.substring(breakPos);
			}

			if (lineStr.trim().length() > 0) {
				this.drawString(lineStr, x, y, color);
				y += 8;
			}
		}
	}

	public int splitStringHeight(String text, int maxWidth) {
		String[] lines = text.split("\n");
		if (lines.length > 1) {
			int totalHeight = 0;
			for (int i = 0; i < lines.length; i++) {
				totalHeight += this.splitStringHeight(lines[i], maxWidth);
			}
			return totalHeight;
		}

		String[] words = text.split(" ");
		int height = 0;
		int wordIndex = 0;
		StringBuilder currentLine = new StringBuilder();

		while (wordIndex < words.length) {
			currentLine.setLength(0);
			currentLine.append(words[wordIndex++]).append(' ');

			while (wordIndex < words.length && this.getStringWidth(currentLine.toString() + words[wordIndex]) < maxWidth) {
				currentLine.append(words[wordIndex++]).append(' ');
			}

			String lineStr = currentLine.toString();
			while (this.getStringWidth(lineStr) > maxWidth) {
				int breakPos = 0;
				while (this.getStringWidth(lineStr.substring(0, breakPos + 1)) <= maxWidth) {
					breakPos++;
				}

				if (lineStr.substring(0, breakPos).trim().length() > 0) {
					height += 8;
				}
				lineStr = lineStr.substring(breakPos);
			}

			if (lineStr.trim().length() > 0) {
				height += 8;
			}
		}

		if (height < 8) {
			height += 8;
		}

		return height;
	}
}
