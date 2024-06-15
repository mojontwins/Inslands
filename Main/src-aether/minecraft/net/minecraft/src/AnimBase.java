package net.minecraft.src;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;

public abstract class AnimBase extends TextureFX {
	protected int[][] fileBuf = new int[this.size][this.size];
	protected int[][] frame = new int[this.size][this.size];
	protected int size = (int)Math.sqrt((double)(this.imageData.length / 4));
	public AnimBase.Mode mdSet = new AnimBase.Mode() {
		public void draw(int x, int y, Color color) {
			AnimBase.this.setPixel(x, y, color);
		}
	};
	public AnimBase.Mode mdAdd = new AnimBase.Mode() {
		public void draw(int x, int y, Color color) {
			AnimBase.this.setPixel(x, y, AnimBase.add(new Color(AnimBase.this.frame[x][y]), color));
		}
	};
	public AnimBase.Mode mdSubtract = new AnimBase.Mode() {
		public void draw(int x, int y, Color color) {
			AnimBase.this.setPixel(x, y, AnimBase.subtract(new Color(AnimBase.this.frame[x][y]), color));
		}
	};
	public AnimBase.Mode mdBlend = new AnimBase.Mode() {
		public void draw(int x, int y, Color color) {
			AnimBase.this.setPixel(x, y, AnimBase.blend(new Color(AnimBase.this.frame[x][y]), color));
		}
	};

	public AnimBase(int spriteID, String spritePath) {
		super(spriteID);

		try {
			BufferedImage e;
			int y;
			int x;
			if(spritePath.isEmpty()) {
				e = ImageIO.read(Minecraft.class.getResource(this.tileImage == 0 ? "/terrain.png" : "/gui/items.png"));
				y = spriteID % 16 * this.size;
				x = (int)Math.floor((double)(spriteID / 16)) * this.size;

				for(int y1 = 0; y1 < this.size; ++y1) {
					for(int x1 = 0; x1 < this.size; ++x1) {
						this.fileBuf[x1][y1] = e.getRGB(y + x1, x + y1);
					}
				}
			} else {
				e = ImageIO.read(Minecraft.class.getResource(spritePath));

				for(y = 0; y < this.size; ++y) {
					for(x = 0; x < this.size; ++x) {
						this.fileBuf[x][y] = e.getRGB(x, y);
					}
				}
			}
		} catch (IOException iOException8) {
			iOException8.printStackTrace();
		}

	}

	public void onTick() {
		this.getCleanFrame();
		this.animFrame();
		this.copyFrameToArray();
	}

	public abstract void animFrame();

	protected void getCleanFrame() {
		for(int y = 0; y < this.size; ++y) {
			for(int x = 0; x < this.size; ++x) {
				this.frame[x][y] = this.fileBuf[x][y];
			}
		}

	}

	protected void copyFrameToArray() {
		for(int y = 0; y < this.size; ++y) {
			for(int x = 0; x < this.size; ++x) {
				int index = this.getXYIndex(x, y);
				this.imageData[index * 4 + 0] = (byte)(this.frame[x][y] >> 16 & 255);
				this.imageData[index * 4 + 1] = (byte)(this.frame[x][y] >> 8 & 255);
				this.imageData[index * 4 + 2] = (byte)(this.frame[x][y] & 255);
				this.imageData[index * 4 + 3] = (byte)(this.frame[x][y] >> 24 & 255);
			}
		}

	}

	private void setPixel(int x, int y, Color color) {
		if(this.inImage(x, y)) {
			this.frame[x][y] = color.getRGB();
		}
	}

	protected int getXYIndex(int x, int y) {
		return y * this.size + x;
	}

	protected boolean inImage(int x, int y) {
		return x >= 0 && y >= 0 && x < this.size && y < this.size;
	}

	protected void drawPoint(int x, int y, Color color) {
		this.drawPoint(x, y, color, this.mdSet);
	}

	protected void drawPoint(int x, int y, Color color, AnimBase.Mode mode) {
		mode.draw(x, y, color);
	}

	protected void drawRect(int x1, int y1, int x2, int y2, Color color) {
		this.drawRect(x1, y1, x2, y2, color, this.mdSet);
	}

	protected void drawRect(int x1, int y1, int x2, int y2, Color color, AnimBase.Mode mode) {
		int xS = Math.min(x1, x2);
		int yS = Math.min(y1, y2);
		int xE = Math.max(x1, x2);
		int yE = Math.max(y1, y2);

		for(int y = yS; y < yE; ++y) {
			for(int x = xS; x < xE; ++x) {
				this.drawPoint(x, y, color, mode);
			}
		}

	}

	protected void shiftFrame(int h, int v, boolean wrapH, boolean wrapV) {
		int[] line = new int[this.size];
		if(wrapH) {
			while(true) {
				if(h >= 0) {
					h %= this.size;
					break;
				}

				h += this.size;
			}
		}

		if(wrapV) {
			while(v < 0) {
				v += this.size;
			}

			v %= this.size;
		}

		int x;
		int y;
		if(h != 0) {
			if(wrapH) {
				for(x = 0; x < this.size; ++x) {
					for(y = 0; y < this.size; ++y) {
						line[y] = this.frame[y][x];
					}

					for(y = 0; y < this.size; ++y) {
						this.frame[y][x] = line[(y + h) % this.size];
					}
				}
			} else {
				for(x = 0; x < this.size; ++x) {
					for(y = 0; y < this.size; ++y) {
						line[y] = this.frame[y][x];
						this.frame[y][x] = 0;
					}

					for(y = 0; y < this.size; ++y) {
						if(this.inImage(y + h, x)) {
							this.frame[y + h][x] = line[y];
						}
					}
				}
			}
		}

		if(v != 0) {
			if(wrapV) {
				for(x = 0; x < this.size; ++x) {
					for(y = 0; y < this.size; ++y) {
						line[y] = this.frame[x][y];
					}

					for(y = 0; y < this.size; ++y) {
						this.frame[x][y] = line[(y + v) % this.size];
					}
				}
			} else {
				for(x = 0; x < this.size; ++x) {
					for(y = 0; y < this.size; ++y) {
						line[y] = this.frame[x][y];
						this.frame[x][y] = 0;
					}

					for(y = 0; y < this.size; ++y) {
						if(this.inImage(x, y + v)) {
							this.frame[x][y + v] = line[y];
						}
					}
				}
			}
		}

	}

	protected void flipFrame(boolean h, boolean v) {
		int swap;
		int y;
		int x;
		if(h) {
			for(y = 0; y < this.size / 2; ++y) {
				for(x = 0; x < this.size; ++x) {
					swap = this.frame[y][x];
					this.frame[y][x] = this.frame[this.size - 1 - y][x];
					this.frame[this.size - 1 - y][x] = swap;
				}
			}
		}

		if(v) {
			for(y = 0; y < this.size / 2; ++y) {
				for(x = 0; x < this.size; ++x) {
					swap = this.frame[x][y];
					this.frame[x][y] = this.frame[x][this.size - 1 - y];
					this.frame[x][this.size - 1 - y] = swap;
				}
			}
		}

	}

	public static Color add(Color c1, Color c2) {
		float value = (float)c2.getAlpha() / 255.0F;
		int R = c1.getRed();
		R = (int)((float)R + (float)c2.getRed() * value);
		R = Math.min(R, 255);
		int G = c1.getGreen();
		G = (int)((float)G + (float)c2.getGreen() * value);
		G = Math.min(G, 255);
		int B = c1.getBlue();
		B = (int)((float)B + (float)c2.getBlue() * value);
		B = Math.min(B, 255);
		int A = c1.getAlpha();
		return new Color(R, G, B, A);
	}

	public static Color subtract(Color c1, Color c2) {
		float value = (float)c2.getAlpha() / 255.0F;
		int R = c1.getRed();
		R = (int)((float)R - (float)c2.getRed() * value);
		R = Math.max(R, 0);
		int G = c1.getGreen();
		G = (int)((float)G - (float)c2.getGreen() * value);
		G = Math.max(G, 0);
		int B = c1.getBlue();
		B = (int)((float)B - (float)c2.getBlue() * value);
		B = Math.max(B, 0);
		int A = c1.getAlpha();
		return new Color(R, G, B, A);
	}

	public static Color merge(Color c1, Color c2, float value) {
		value = Math.min(Math.max(value, 0.0F), 1.0F);
		float R = (float)c1.getRed() - ((float)c1.getRed() - (float)c2.getRed()) * value;
		float G = (float)c1.getGreen() - ((float)c1.getGreen() - (float)c2.getGreen()) * value;
		float B = (float)c1.getBlue() - ((float)c1.getBlue() - (float)c2.getBlue()) * value;
		float A = (float)c1.getAlpha() - ((float)c1.getAlpha() - (float)c2.getAlpha()) * value;
		return new Color(R / 255.0F, G / 255.0F, B / 255.0F, A / 255.0F);
	}

	public static Color blend(Color c1, Color c2) {
		float R = (float)c1.getRed() / 255.0F * ((float)c2.getRed() / 255.0F);
		float G = (float)c1.getGreen() / 255.0F * ((float)c2.getGreen() / 255.0F);
		float B = (float)c1.getBlue() / 255.0F * ((float)c2.getBlue() / 255.0F);
		float A = (float)c1.getAlpha() / 255.0F * ((float)c2.getAlpha() / 255.0F);
		return new Color(R, G, B, A);
	}

	public abstract class Mode {
		public abstract void draw(int i1, int i2, Color color3);
	}
}
