package net.minecraft.src;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;

public class ImageBufferDownload implements ImageBuffer {
	private int[] imageData;
	private int imageWidth;
	private int imageHeight;

	public BufferedImage parseUserSkin(BufferedImage par1BufferedImage) {
		if(par1BufferedImage == null) {
			return null;
		} else {
			this.imageWidth = 64;
			this.imageHeight = 32;

			for(BufferedImage srcImg = par1BufferedImage; this.imageWidth < srcImg.getWidth() || this.imageHeight < srcImg.getHeight(); this.imageHeight *= 2) {
				this.imageWidth *= 2;
			}

			BufferedImage bufferedimage = new BufferedImage(this.imageWidth, this.imageHeight, 2);
			Graphics g = bufferedimage.getGraphics();
			g.drawImage(par1BufferedImage, 0, 0, (ImageObserver)null);
			g.dispose();
			this.imageData = ((DataBufferInt)bufferedimage.getRaster().getDataBuffer()).getData();
			int w = this.imageWidth;
			int h = this.imageHeight;
			this.func_884_b(0, 0, w / 2, h / 2);
			this.func_885_a(w / 2, 0, w, h);
			this.func_884_b(0, h / 2, w, h);
			boolean flag = false;

			int j;
			int l;
			int j1;
			for(j = w / 2; j < w; ++j) {
				for(l = 0; l < h / 2; ++l) {
					j1 = this.imageData[j + l * w];
					if((j1 >> 24 & 255) < 128) {
						flag = true;
					}
				}
			}

			if(!flag) {
				for(j = w / 2; j < w; ++j) {
					for(l = 0; l < h / 2; ++l) {
						j1 = this.imageData[j + l * w];
						if((j1 >> 24 & 255) < 128) {
							boolean flag1 = true;
						}
					}
				}
			}

			return bufferedimage;
		}
	}

	private void func_885_a(int par1, int par2, int par3, int par4) {
		if(!this.func_886_c(par1, par2, par3, par4)) {
			for(int i = par1; i < par3; ++i) {
				for(int j = par2; j < par4; ++j) {
					this.imageData[i + j * this.imageWidth] &= 0xFFFFFF;
				}
			}

		}
	}

	private void func_884_b(int par1, int par2, int par3, int par4) {
		for(int i = par1; i < par3; ++i) {
			for(int j = par2; j < par4; ++j) {
				this.imageData[i + j * this.imageWidth] |= 0xFF000000;
			}
		}

	}

	private boolean func_886_c(int par1, int par2, int par3, int par4) {
		for(int i = par1; i < par3; ++i) {
			for(int j = par2; j < par4; ++j) {
				int k = this.imageData[i + j * this.imageWidth];
				if((k >> 24 & 255) < 128) {
					return true;
				}
			}
		}

		return false;
	}
}
