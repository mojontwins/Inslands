package net.minecraft.src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.opengl.GL12;

public class RenderEngine {
	public static boolean useMipmaps = true;
	private HashMap textureMap;
	private HashMap textureContentsMap;
	private IntHashMap textureNameToImageMap;
	private IntBuffer singleIntBuffer;
	private ByteBuffer imageData;
	public List textureList;
	private Map urlToImageDataMap;
	private GameSettings options;
	public boolean clampTexture;
	public boolean blurTexture;
	public TexturePackList texturePack;
	private BufferedImage missingTextureImage;
	private int field_48512_n;
	public int terrainTextureId = -1;
	public int guiItemsTextureId = -1;
	public int ctmTextureId = -1;
	private boolean hdTexturesInstalled = false;
	private Map textureDimensionsMap = new HashMap();
	private Map textureDataMap = new HashMap();
	private int tickCounter = 0;
	private ByteBuffer[] mipImageDatas;
	private boolean dynamicTexturesUpdated = false;
	private Map textureFxMap = new IdentityHashMap();
	private Map mipDataBufsMap = new HashMap();
	private boolean singleTileTexture = false;
	private Map customAnimationMap = new HashMap();
	private CustomAnimation[] textureAnimations = null;
	public static Logger log = Logger.getAnonymousLogger();

	public RenderEngine(TexturePackList par1TexturePackList, GameSettings par2GameSettings) {
		if(Config.isMultiTexture()) {
			int g = Config.getAntialiasingLevel();
			Config.dbg("FSAA Samples: " + g);

			try {
				Display.destroy();
				Display.create(new PixelFormat(0, 8, 0, g));
			} catch (LWJGLException lWJGLException7) {
				Config.dbg("Error setting FSAA: " + g + "x");
				lWJGLException7.printStackTrace();

				try {
					Display.create();
				} catch (LWJGLException lWJGLException6) {
					lWJGLException6.printStackTrace();
				}
			}
		}

		this.textureMap = new HashMap();
		this.textureContentsMap = new HashMap();
		this.textureNameToImageMap = new IntHashMap();
		this.singleIntBuffer = GLAllocation.createDirectIntBuffer(1);
		this.allocateImageData(256, 256);
		this.textureList = new ArrayList();
		this.urlToImageDataMap = new HashMap();
		this.clampTexture = false;
		this.blurTexture = false;
		this.missingTextureImage = new BufferedImage(64, 64, 2);
		this.field_48512_n = 16;
		this.texturePack = par1TexturePackList;
		this.options = par2GameSettings;
		Graphics g1 = this.missingTextureImage.getGraphics();
		g1.setColor(Color.WHITE);
		g1.fillRect(0, 0, 64, 64);
		g1.setColor(Color.BLACK);
		g1.drawString("missingtex", 1, 10);
		g1.dispose();
	}

	public int[] getTextureContents(String par1Str) {
		TexturePackBase texturepackbase = this.texturePack.selectedTexturePack;
		int[] ai = (int[])((int[])this.textureContentsMap.get(par1Str));
		if(ai != null) {
			return ai;
		} else {
			int[] ai2;
			try {
				Object ai21 = null;
				if(par1Str.startsWith("##")) {
					ai2 = this.getImageContentsAndAllocate(this.unwrapImageByColumns(this.readTextureImage(texturepackbase.getResourceAsStream(par1Str.substring(2)))));
				} else if(par1Str.startsWith("%clamp%")) {
					this.clampTexture = true;
					ai2 = this.getImageContentsAndAllocate(this.readTextureImage(texturepackbase.getResourceAsStream(par1Str.substring(7))));
					this.clampTexture = false;
				} else if(par1Str.startsWith("%blur%")) {
					this.blurTexture = true;
					this.clampTexture = true;
					ai2 = this.getImageContentsAndAllocate(this.readTextureImage(texturepackbase.getResourceAsStream(par1Str.substring(6))));
					this.clampTexture = false;
					this.blurTexture = false;
				} else {
					InputStream inputstream = texturepackbase.getResourceAsStream(par1Str);
					if(inputstream == null) {
						ai2 = this.getImageContentsAndAllocate(this.missingTextureImage);
					} else {
						ai2 = this.getImageContentsAndAllocate(this.readTextureImage(inputstream));
					}
				}

				this.textureContentsMap.put(par1Str, ai2);
				return ai2;
			} catch (IOException iOException6) {
				iOException6.printStackTrace();
				ai2 = this.getImageContentsAndAllocate(this.missingTextureImage);
				this.textureContentsMap.put(par1Str, ai2);
				return ai2;
			}
		}
	}

	private int[] getImageContentsAndAllocate(BufferedImage par1BufferedImage) {
		int i = par1BufferedImage.getWidth();
		int j = par1BufferedImage.getHeight();
		int[] ai = new int[i * j];
		par1BufferedImage.getRGB(0, 0, i, j, ai, 0, i);
		return ai;
	}

	private int[] getImageContents(BufferedImage par1BufferedImage, int[] par2ArrayOfInteger) {
		int i = par1BufferedImage.getWidth();
		int j = par1BufferedImage.getHeight();
		par1BufferedImage.getRGB(0, 0, i, j, par2ArrayOfInteger, 0, i);
		return par2ArrayOfInteger;
	}

	public int getTexture(String par1Str) {
		TexturePackBase texturepackbase = this.texturePack.selectedTexturePack;
		Integer integer = (Integer)this.textureMap.get(par1Str);
		if(integer != null) {
			return integer.intValue();
		} else {
			int j;
			try {
				if(Reflector.hasClass(1)) {
					Reflector.callVoid(18, new Object[]{par1Str});
				}

				this.singleIntBuffer.clear();
				GLAllocation.generateTextureNames(this.singleIntBuffer);
				if(Tessellator.renderingWorldRenderer) {
					System.out.printf("Warning: Texture %s not preloaded, will cause render glitches!\n", new Object[]{par1Str});
				}

				j = this.singleIntBuffer.get(0);
				Config.dbg("setupTexture: \"" + par1Str + "\", id: " + j);
				if(par1Str.startsWith("##")) {
					this.setupTexture(this.unwrapImageByColumns(this.readTextureImage(texturepackbase.getResourceAsStream(par1Str.substring(2)))), j);
				} else if(par1Str.startsWith("%clamp%")) {
					this.clampTexture = true;
					this.setupTexture(this.readTextureImage(texturepackbase.getResourceAsStream(par1Str.substring(7))), j);
					this.clampTexture = false;
				} else if(par1Str.startsWith("%blur%")) {
					this.blurTexture = true;
					this.setupTexture(this.readTextureImage(texturepackbase.getResourceAsStream(par1Str.substring(6))), j);
					this.blurTexture = false;
				} else if(par1Str.startsWith("%blurclamp%")) {
					this.blurTexture = true;
					this.clampTexture = true;
					this.setupTexture(this.readTextureImage(texturepackbase.getResourceAsStream(par1Str.substring(11))), j);
					this.blurTexture = false;
					this.clampTexture = false;
				} else {
					InputStream inputstream = texturepackbase.getResourceAsStream(par1Str);
					if(inputstream == null) {
						this.setupTexture(this.missingTextureImage, j);
					} else {
						if(par1Str.equals("/terrain.png")) {
							this.terrainTextureId = j;
						}

						if(par1Str.equals("/gui/items.png")) {
							this.guiItemsTextureId = j;
						}

						if(par1Str.equals("/ctm.png")) {
							this.ctmTextureId = j;
						}

						this.setupTexture(this.readTextureImage(inputstream), j);
					}
				}

				this.textureMap.put(par1Str, j);
				if(Reflector.hasClass(1)) {
					Reflector.callVoid(19, new Object[]{par1Str, j});
				}

				return j;
			} catch (Exception exception6) {
				exception6.printStackTrace();
				GLAllocation.generateTextureNames(this.singleIntBuffer);
				j = this.singleIntBuffer.get(0);
				this.setupTexture(this.missingTextureImage, j);
				this.textureMap.put(par1Str, j);
				return j;
			}
		}
	}

	private BufferedImage unwrapImageByColumns(BufferedImage par1BufferedImage) {
		int i = par1BufferedImage.getWidth() / 16;
		BufferedImage bufferedimage = new BufferedImage(16, par1BufferedImage.getHeight() * i, 2);
		Graphics g = bufferedimage.getGraphics();

		for(int j = 0; j < i; ++j) {
			g.drawImage(par1BufferedImage, -j * 16, j * par1BufferedImage.getHeight(), (ImageObserver)null);
		}

		g.dispose();
		return bufferedimage;
	}

	public int allocateAndSetupTexture(BufferedImage par1BufferedImage) {
		this.singleIntBuffer.clear();
		GLAllocation.generateTextureNames(this.singleIntBuffer);
		int i = this.singleIntBuffer.get(0);
		this.setupTexture(par1BufferedImage, i);
		this.textureNameToImageMap.addKey(i, par1BufferedImage);
		return i;
	}

	public void setupTexture(BufferedImage par1BufferedImage, int par2) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, par2);
		boolean mipmapsActive = useMipmaps && Config.isUseMipmaps();
		int width;
		int height;
		if(mipmapsActive && par2 != this.guiItemsTextureId) {
			width = Config.getMipmapType();
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, width);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			if(GLContext.getCapabilities().OpenGL12) {
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_BASE_LEVEL, 0);
				height = Config.getMipmapLevel();
				if(height >= 4) {
					int ai = Math.min(par1BufferedImage.getWidth(), par1BufferedImage.getHeight());
					height = this.getMaxMipmapLevel(ai);
					if(!this.singleTileTexture) {
						height -= 4;
					}

					if(height < 0) {
						height = 0;
					}
				}

				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, height);
			}

			if(GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
				FloatBuffer floatBuffer21 = BufferUtils.createFloatBuffer(16);
				floatBuffer21.rewind();
				GL11.glGetFloat(34047, floatBuffer21);
				float f19 = floatBuffer21.get(0);
				float byteBuf = (float)Config.getAnisotropicFilterLevel();
				byteBuf = Math.min(byteBuf, f19);
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, 34046, byteBuf);
			}
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		}

		if(this.blurTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		}

		if(this.clampTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}

		width = par1BufferedImage.getWidth();
		height = par1BufferedImage.getHeight();
		this.setTextureDimension(par2, new Dimension(width, height));
		if(Reflector.hasClass(7)) {
			Reflector.callVoid(70, new Object[]{par2, width, height, this.textureList});
		}

		int[] i20 = new int[width * height];
		byte[] b22 = new byte[width * height * 4];
		par1BufferedImage.getRGB(0, 0, width, height, i20, 0, width);
		int[] bgColors = null;
		int iconTextures;
		int iconWidth;
		if(mipmapsActive) {
			if(this.isTextureAtlas16x16(par2)) {
				bgColors = new int[256];

				for(iconTextures = 0; iconTextures < 16; ++iconTextures) {
					for(iconWidth = 0; iconWidth < 16; ++iconWidth) {
						bgColors[iconTextures * 16 + iconWidth] = this.getAverageOpaqueColor(i20, iconWidth, iconTextures, width, height);
					}
				}
			}

			if(this.singleTileTexture) {
				bgColors = new int[]{this.getAverageOpaqueColor(i20)};
			}
		}

		int iconHeight;
		int cy;
		int cx;
		int x0;
		int y0;
		int iconIndex;
		for(iconTextures = 0; iconTextures < i20.length; ++iconTextures) {
			iconWidth = i20[iconTextures] >> 24 & 255;
			iconHeight = i20[iconTextures] >> 16 & 255;
			cy = i20[iconTextures] >> 8 & 255;
			cx = i20[iconTextures] & 255;
			int iconImage;
			if(this.options != null && this.options.anaglyph) {
				x0 = (iconHeight * 30 + cy * 59 + cx * 11) / 100;
				y0 = (iconHeight * 30 + cy * 70) / 100;
				iconImage = (iconHeight * 30 + cx * 70) / 100;
				iconHeight = x0;
				cy = y0;
				cx = iconImage;
			}

			if(iconWidth == 0) {
				iconHeight = 0;
				cy = 0;
				cx = 0;
				if(this.isTextureAtlas16x16(par2) || this.singleTileTexture) {
					iconHeight = 255;
					cy = 255;
					cx = 255;
					if(mipmapsActive) {
						boolean z24 = false;
						if(this.singleTileTexture) {
							x0 = bgColors[0];
						} else {
							y0 = iconTextures % width;
							iconImage = iconTextures / width;
							iconIndex = y0 / (width / 16);
							int ty = iconImage / (height / 16);
							x0 = bgColors[ty * 16 + iconIndex];
						}

						if(x0 != 0) {
							iconHeight = x0 >> 16 & 255;
							cy = x0 >> 8 & 255;
							cx = x0 & 255;
						}
					}
				}
			}

			b22[iconTextures * 4 + 0] = (byte)iconHeight;
			b22[iconTextures * 4 + 1] = (byte)cy;
			b22[iconTextures * 4 + 2] = (byte)cx;
			b22[iconTextures * 4 + 3] = (byte)iconWidth;
		}

		this.checkImageDataSize(width, height);
		this.imageData.clear();
		this.imageData.put(b22);
		this.imageData.position(0).limit(b22.length);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
		if(useMipmaps) {
			this.generateMipMaps(this.imageData, width, height);
		}

		if(Config.isMultiTexture() && this.isTextureAtlas16x16(par2)) {
			int[] i23 = Tessellator.getTileTextures(par2);
			if(i23 == null) {
				i23 = new int[256];
			}

			iconWidth = width / 16;
			iconHeight = height / 16;

			for(cy = 0; cy < 16; ++cy) {
				for(cx = 0; cx < 16; ++cx) {
					x0 = cx * iconWidth;
					y0 = cy * iconHeight;
					BufferedImage bufferedImage25 = par1BufferedImage.getSubimage(x0, y0, iconWidth, iconHeight);
					iconIndex = cy * 16 + cx;
					if(i23[iconIndex] <= 0) {
						this.singleIntBuffer.clear();
						GLAllocation.generateTextureNames(this.singleIntBuffer);
						i23[iconIndex] = this.singleIntBuffer.get(0);
					}

					this.clampTexture = this.isTileClamped(par2, iconIndex);
					this.singleTileTexture = true;
					this.setupTexture(bufferedImage25, i23[iconIndex]);
					this.singleTileTexture = false;
				}
			}

			this.clampTexture = false;
			Tessellator.setTileTextures(par2, i23);
		}

	}

	private int getAverageOpaqueIconColor(int[] ai, int ix, int iy, int width, int height) {
		return -1;
	}

	private int getAverageOpaqueColor(int[] ai) {
		long redSum = 0L;
		long greenSum = 0L;
		long blueSum = 0L;
		long count = 0L;

		int redAvg;
		int greenAvg;
		int blueAvg;
		for(int alpha = 0; alpha < ai.length; ++alpha) {
			redAvg = ai[alpha];
			greenAvg = redAvg >> 24 & 255;
			if(greenAvg != 0) {
				blueAvg = redAvg >> 16 & 255;
				int green = redAvg >> 8 & 255;
				int blue = redAvg & 255;
				redSum += (long)blueAvg;
				greenSum += (long)green;
				blueSum += (long)blue;
				++count;
			}
		}

		if(count <= 0L) {
			return -1;
		} else {
			short s16 = 255;
			redAvg = (int)(redSum / count);
			greenAvg = (int)(greenSum / count);
			blueAvg = (int)(blueSum / count);
			return s16 << 24 | redAvg << 16 | greenAvg << 8 | blueAvg;
		}
	}

	private void fixAlpha(BufferedImage iconImage) {
		long redSum = 0L;
		long greenSum = 0L;
		long blueSum = 0L;
		long count = 0L;
		int width = iconImage.getWidth();
		int height = iconImage.getHeight();

		int redAvg;
		int greenAvg;
		int blueAvg;
		int y;
		int x;
		int col;
		int alpha;
		for(redAvg = 0; redAvg < height; ++redAvg) {
			for(greenAvg = 0; greenAvg < width; ++greenAvg) {
				blueAvg = iconImage.getRGB(greenAvg, redAvg);
				y = blueAvg >> 24 & 255;
				if(y != 0) {
					x = blueAvg >> 16 & 255;
					col = blueAvg >> 8 & 255;
					alpha = blueAvg & 255;
					redSum += (long)x;
					greenSum += (long)col;
					blueSum += (long)alpha;
					++count;
				}
			}
		}

		if(count > 0L) {
			redAvg = (int)(redSum / count);
			greenAvg = (int)(greenSum / count);
			blueAvg = (int)(blueSum / count);

			for(y = 0; y < height; ++y) {
				for(x = 0; x < width; ++x) {
					col = iconImage.getRGB(x, y);
					alpha = col >> 24 & 255;
					if(alpha == 0) {
						col = alpha << 24 | redAvg << 16 | greenAvg << 8 | blueAvg << 0;
						iconImage.setRGB(x, y, col);
					}
				}
			}

		}
	}

	private int getAverageOpaqueColor(int[] ai, int tx, int ty, int width, int height) {
		int tileWidth = width / 16;
		int tileHeight = height / 16;
		int startPos = ty * tileHeight * width + tx * tileWidth;
		long redSum = 0L;
		long greenSum = 0L;
		long blueSum = 0L;
		long count = 0L;

		int redAvg;
		int greenAvg;
		int blueAvg;
		for(int alpha = 0; alpha < tileHeight; ++alpha) {
			for(redAvg = 0; redAvg < tileWidth; ++redAvg) {
				greenAvg = startPos + alpha * width + redAvg;
				blueAvg = ai[greenAvg] >> 24 & 255;
				if(blueAvg != 0) {
					int red = ai[greenAvg] >> 16 & 255;
					int green = ai[greenAvg] >> 8 & 255;
					int blue = ai[greenAvg] & 255;
					redSum += (long)red;
					greenSum += (long)green;
					blueSum += (long)blue;
					++count;
				}
			}
		}

		if(count <= 0L) {
			return 0;
		} else {
			short s24 = 255;
			redAvg = (int)(redSum / count);
			greenAvg = (int)(greenSum / count);
			blueAvg = (int)(blueSum / count);
			return s24 << 24 | redAvg << 16 | greenAvg << 8 | blueAvg;
		}
	}

	private boolean isTextureAtlas16x16(int i) {
		return i == this.terrainTextureId ? true : (i == this.guiItemsTextureId ? true : i == this.ctmTextureId);
	}

	private void generateMipMaps(ByteBuffer data, int width, int height) {
		ByteBuffer parMipData = data;

		for(int level = 1; level <= 16; ++level) {
			int parWidth = width >> level - 1;
			int mipWidth = width >> level;
			int mipHeight = height >> level;
			if(mipWidth <= 0 || mipHeight <= 0) {
				break;
			}

			ByteBuffer mipData = this.mipImageDatas[level - 1];
			mipData.limit(mipWidth * mipHeight * 4);

			for(int mipX = 0; mipX < mipWidth; ++mipX) {
				for(int mipY = 0; mipY < mipHeight; ++mipY) {
					int p1 = parMipData.getInt((mipX * 2 + 0 + (mipY * 2 + 0) * parWidth) * 4);
					int p2 = parMipData.getInt((mipX * 2 + 1 + (mipY * 2 + 0) * parWidth) * 4);
					int p3 = parMipData.getInt((mipX * 2 + 1 + (mipY * 2 + 1) * parWidth) * 4);
					int p4 = parMipData.getInt((mipX * 2 + 0 + (mipY * 2 + 1) * parWidth) * 4);
					int pixel = this.alphaBlend(p1, p2, p3, p4);
					mipData.putInt((mipX + mipY * mipWidth) * 4, pixel);
				}
			}

			mipData.rewind();
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, level, GL11.GL_RGBA, mipWidth, mipHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, mipData);
			parMipData = mipData;
		}

	}

	public void createTextureFromBytes(int[] par1ArrayOfInteger, int par2, int par3, int par4) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, par4);
		if(useMipmaps && Config.isUseMipmaps()) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		}

		if(this.blurTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		}

		if(this.clampTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}

		byte[] abyte0 = new byte[par2 * par3 * 4];

		for(int i = 0; i < par1ArrayOfInteger.length; ++i) {
			int j = par1ArrayOfInteger[i] >> 24 & 255;
			int k = par1ArrayOfInteger[i] >> 16 & 255;
			int l = par1ArrayOfInteger[i] >> 8 & 255;
			int i1 = par1ArrayOfInteger[i] & 255;
			if(this.options != null && this.options.anaglyph) {
				int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
				int k1 = (k * 30 + l * 70) / 100;
				int l1 = (k * 30 + i1 * 70) / 100;
				k = j1;
				l = k1;
				i1 = l1;
			}

			abyte0[i * 4 + 0] = (byte)k;
			abyte0[i * 4 + 1] = (byte)l;
			abyte0[i * 4 + 2] = (byte)i1;
			abyte0[i * 4 + 3] = (byte)j;
		}

		this.imageData.clear();
		this.imageData.put(abyte0);
		this.imageData.position(0).limit(abyte0.length);
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, par2, par3, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
	}

	public void deleteTexture(int par1) {
		this.textureNameToImageMap.removeObject(par1);
		this.singleIntBuffer.clear();
		this.singleIntBuffer.put(par1);
		this.singleIntBuffer.flip();
		GL11.glDeleteTextures(this.singleIntBuffer);
	}

	public int getTextureForDownloadableImage(String par1Str, String par2Str) {
		if(Config.isRandomMobs()) {
			int threaddownloadimagedata = RandomMobs.getTexture(par1Str, par2Str);
			if(threaddownloadimagedata >= 0) {
				return threaddownloadimagedata;
			}
		}

		ThreadDownloadImageData threaddownloadimagedata1 = (ThreadDownloadImageData)this.urlToImageDataMap.get(par1Str);
		if(threaddownloadimagedata1 != null && threaddownloadimagedata1.image != null && !threaddownloadimagedata1.textureSetupComplete) {
			if(threaddownloadimagedata1.textureName < 0) {
				threaddownloadimagedata1.textureName = this.allocateAndSetupTexture(threaddownloadimagedata1.image);
			} else {
				this.setupTexture(threaddownloadimagedata1.image, threaddownloadimagedata1.textureName);
			}

			threaddownloadimagedata1.textureSetupComplete = true;
		}

		return threaddownloadimagedata1 != null && threaddownloadimagedata1.textureName >= 0 ? threaddownloadimagedata1.textureName : (par2Str == null ? -1 : this.getTexture(par2Str));
	}

	public ThreadDownloadImageData obtainImageData(String par1Str, ImageBuffer par2ImageBuffer) {
		ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData)this.urlToImageDataMap.get(par1Str);
		if(threaddownloadimagedata == null) {
			this.urlToImageDataMap.put(par1Str, new ThreadDownloadImageData(par1Str, par2ImageBuffer));
		} else {
			++threaddownloadimagedata.referenceCount;
		}

		return threaddownloadimagedata;
	}

	public void releaseImageData(String par1Str) {
		ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData)this.urlToImageDataMap.get(par1Str);
		if(threaddownloadimagedata != null) {
			--threaddownloadimagedata.referenceCount;
			if(threaddownloadimagedata.referenceCount == 0) {
				if(threaddownloadimagedata.textureName >= 0) {
					this.deleteTexture(threaddownloadimagedata.textureName);
				}

				this.urlToImageDataMap.remove(par1Str);
			}
		}

	}

	public void registerTextureFX(TextureFX par1TextureFX) {
		if(Reflector.hasClass(7)) {
			Reflector.callVoid(71, new Object[]{par1TextureFX});
		}

		int newTexId = this.getTextureId(par1TextureFX);

		for(int texHdFx = 0; texHdFx < this.textureList.size(); ++texHdFx) {
			TextureFX dim = (TextureFX)this.textureList.get(texHdFx);
			int texId = this.getTextureId(dim);
			if(texId == newTexId && dim.iconIndex == par1TextureFX.iconIndex) {
				this.textureList.remove(texHdFx);
				--texHdFx;
				Config.log("TextureFX removed: " + dim + ", texId: " + texId + ", index: " + dim.iconIndex);
			}
		}

		if(par1TextureFX instanceof TextureHDFX) {
			TextureHDFX textureHDFX6 = (TextureHDFX)par1TextureFX;
			textureHDFX6.setTexturePackBase(this.texturePack.selectedTexturePack);
			Dimension dimension7 = this.getTextureDimensions(newTexId);
			if(dimension7 != null) {
				textureHDFX6.setTileWidth(dimension7.width / 16);
			}
		}

		this.textureList.add(par1TextureFX);
		par1TextureFX.onTick();
		Config.log("TextureFX registered: " + par1TextureFX + ", texId: " + newTexId + ", index: " + par1TextureFX.iconIndex);
		this.dynamicTexturesUpdated = false;
	}

	private int getTextureId(TextureFX fx) {
		Integer texId = (Integer)this.textureFxMap.get(fx);
		if(texId != null) {
			return texId.intValue();
		} else {
			int oldTexId = this.getBoundTexture();
			fx.bindImage(this);
			int texIdInt = this.getBoundTexture();
			this.bindTexture(oldTexId);
			this.textureFxMap.put(fx, new Integer(texIdInt));
			return texIdInt;
		}
	}

	private int getBoundTexture() {
		int texId = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
		return texId;
	}

	private void generateMipMapsSub(int xOffset, int yOffset, int width, int height, ByteBuffer data, int numTiles, boolean fastColor, int tid, int iconIndex, String dataId) {
		ByteBuffer parMipData = data;
		byte[][] mipDataBufs = (byte[][])null;
		if(dataId.length() > 0) {
			mipDataBufs = (byte[][])((byte[][])this.mipDataBufsMap.get(dataId));
			if(mipDataBufs == null) {
				mipDataBufs = new byte[17][];
				this.mipDataBufsMap.put(dataId, mipDataBufs);
			}
		}

		for(int level = 1; level <= 16; ++level) {
			int parWidth = width >> level - 1;
			int mipWidth = width >> level;
			int mipHeight = height >> level;
			int xMipOffset = xOffset >> level;
			int yMipOffset = yOffset >> level;
			if(mipWidth <= 0 || mipHeight <= 0) {
				break;
			}

			ByteBuffer mipData = this.mipImageDatas[level - 1];
			mipData.limit(mipWidth * mipHeight * 4);
			byte[] mipDataBuf = null;
			if(mipDataBufs != null) {
				mipDataBuf = mipDataBufs[level];
			}

			if(mipDataBuf != null && mipDataBuf.length != mipWidth * mipHeight * 4) {
				mipDataBuf = null;
			}

			int ix;
			int iy;
			int dx;
			int dy;
			int di;
			if(mipDataBuf == null) {
				if(mipDataBufs != null) {
					mipDataBuf = new byte[mipWidth * mipHeight * 4];
				}

				for(ix = 0; ix < mipWidth; ++ix) {
					for(iy = 0; iy < mipHeight; ++iy) {
						dx = parMipData.getInt((ix * 2 + 0 + (iy * 2 + 0) * parWidth) * 4);
						dy = parMipData.getInt((ix * 2 + 1 + (iy * 2 + 0) * parWidth) * 4);
						di = parMipData.getInt((ix * 2 + 1 + (iy * 2 + 1) * parWidth) * 4);
						int p4 = parMipData.getInt((ix * 2 + 0 + (iy * 2 + 1) * parWidth) * 4);
						int pixel;
						if(fastColor) {
							pixel = this.averageColor(this.averageColor(dx, dy), this.averageColor(di, p4));
						} else {
							pixel = this.alphaBlend(dx, dy, di, p4);
						}

						mipData.putInt((ix + iy * mipWidth) * 4, pixel);
					}
				}

				if(mipDataBufs != null) {
					mipData.rewind();
					mipData.get(mipDataBuf);
					mipDataBufs[level] = mipDataBuf;
				}
			}

			if(mipDataBuf != null) {
				mipData.rewind();
				mipData.put(mipDataBuf);
			}

			mipData.rewind();

			for(ix = 0; ix < numTiles; ++ix) {
				for(iy = 0; iy < numTiles; ++iy) {
					dx = ix * mipWidth;
					dy = iy * mipHeight;
					if(Config.isMultiTexture() && tid == this.terrainTextureId) {
						di = iy * 16 + ix;
						GL11.glBindTexture(GL11.GL_TEXTURE_2D, Tessellator.getTileTextures(this.terrainTextureId)[iconIndex + di]);
						dx = 0;
						dy = 0;
					}

					GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, level, xMipOffset + dx, yMipOffset + dy, mipWidth, mipHeight, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, mipData);
				}
			}

			parMipData = mipData;
		}

	}

	public void updateDynamicTextures() {
		boolean mipmapsActive = useMipmaps && Config.isUseMipmaps();
		this.checkHdTextures();
		++this.tickCounter;
		this.terrainTextureId = this.getTexture("/terrain.png");
		this.guiItemsTextureId = this.getTexture("/gui/items.png");
		this.ctmTextureId = this.getTexture("/ctm.png");
		StringBuffer dataIdBuf = new StringBuffer();
		int boundTextureId = -1;

		for(int animatedTextures = 0; animatedTextures < this.textureList.size(); ++animatedTextures) {
			TextureFX i = (TextureFX)this.textureList.get(animatedTextures);
			i.anaglyphEnabled = this.options.anaglyph;
			if(!i.getClass().getName().equals("ModTextureStatic") || !this.dynamicTexturesUpdated) {
				int anim = this.getTextureId(i);
				Dimension tid = this.getTextureDimensions(anim);
				if(tid == null) {
					throw new IllegalArgumentException("Unknown dimensions for texture id: " + anim);
				}

				int dim = tid.width / 16;
				int ok = tid.height / 16;
				this.checkImageDataSize(tid.width, tid.height);
				this.imageData.limit(0);
				dataIdBuf.setLength(0);
				boolean customOk = this.updateCustomTexture(i, anim, this.imageData, tid.width / 16, dataIdBuf);
				if(!customOk || this.imageData.limit() > 0) {
					boolean fastColor;
					if(this.imageData.limit() <= 0) {
						fastColor = this.updateDefaultTexture(i, anim, this.imageData, tid.width / 16, dataIdBuf);
						if(fastColor && this.imageData.limit() <= 0) {
							continue;
						}
					}

					if(this.imageData.limit() <= 0) {
						i.onTick();
						if(Reflector.hasClass(7) && !Reflector.callBoolean(72, new Object[]{i}) || i.imageData == null) {
							continue;
						}

						int i26 = dim * ok * 4;
						if(i.imageData.length == i26) {
							this.imageData.clear();
							this.imageData.put(i.imageData);
							this.imageData.position(0).limit(i.imageData.length);
						} else {
							this.copyScaled(i.imageData, this.imageData, dim);
						}
					}

					if(anim != boundTextureId) {
						i.bindImage(this);
						boundTextureId = anim;
					}

					fastColor = this.scalesWithFastColor(i);

					int ix;
					int iy;
					for(ix = 0; ix < i.tileSize; ++ix) {
						for(iy = 0; iy < i.tileSize; ++iy) {
							int xOffset = i.iconIndex % 16 * dim + ix * dim;
							int yOffset = i.iconIndex / 16 * ok + iy * ok;
							GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, xOffset, yOffset, dim, ok, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
							if(mipmapsActive && anim != this.guiItemsTextureId) {
								String di = dataIdBuf.toString();
								if(ix == 0 && iy == 0) {
									this.generateMipMapsSub(xOffset, yOffset, dim, ok, this.imageData, i.tileSize, fastColor, 0, 0, di);
								}
							}
						}
					}

					if(Config.isMultiTexture() && anim == this.terrainTextureId) {
						for(ix = 0; ix < i.tileSize; ++ix) {
							for(iy = 0; iy < i.tileSize; ++iy) {
								byte b27 = 0;
								byte b28 = 0;
								int i29 = iy * 16 + ix;
								int[] tileTextures = Tessellator.getTileTextures(this.terrainTextureId);
								int texId = tileTextures[i.iconIndex + i29];
								GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
								boundTextureId = texId;
								GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, b27, b28, dim, ok, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
								if(mipmapsActive) {
									String dataId = dataIdBuf.toString();
									if(ix == 0 && iy == 0) {
										this.generateMipMapsSub(b27, b28, dim, ok, this.imageData, i.tileSize, fastColor, anim, i.iconIndex, dataId);
									}
								}
							}
						}
					}
				}
			}
		}

		if(this.textureAnimations != null) {
			boolean z20 = this.options.ofAnimatedTextures;

			for(int i21 = 0; i21 < this.textureAnimations.length; ++i21) {
				CustomAnimation customAnimation22 = this.textureAnimations[i21];
				int i23 = this.getTexture(customAnimation22.destTexture);
				if(i23 >= 0) {
					Dimension dimension24 = this.getTextureDimensions(i23);
					if(dimension24 != null) {
						this.checkImageDataSize(dimension24.width, dimension24.height);
						this.imageData.limit(0);
						dataIdBuf.setLength(0);
						boolean z25 = customAnimation22.updateCustomTexture(this.imageData, z20, this.dynamicTexturesUpdated, dataIdBuf);
						if((!z25 || this.imageData.limit() > 0) && this.imageData.limit() > 0) {
							this.bindTexture(i23);
							GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, customAnimation22.destX, customAnimation22.destY, customAnimation22.frameWidth, customAnimation22.frameHeight, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
						}
					}
				}
			}
		}

		this.dynamicTexturesUpdated = true;
	}

	private int averageColor(int i, int j) {
		int k = (i & 0xFF000000) >> 24 & 255;
		int l = (j & 0xFF000000) >> 24 & 255;
		return (k + l >> 1 << 24) + ((i & 16711422) + (j & 16711422) >> 1);
	}

	private int alphaBlend(int c1, int c2, int c3, int c4) {
		int cx1 = this.alphaBlend(c1, c2);
		int cx2 = this.alphaBlend(c3, c4);
		int cx = this.alphaBlend(cx1, cx2);
		return cx;
	}

	private int alphaBlend(int c1, int c2) {
		int a1 = (c1 & 0xFF000000) >> 24 & 255;
		int a2 = (c2 & 0xFF000000) >> 24 & 255;
		int ax = (a1 + a2) / 2;
		if(a1 == 0 && a2 == 0) {
			a1 = 1;
			a2 = 1;
		} else {
			if(a1 == 0) {
				c1 = c2;
				ax /= 2;
			}

			if(a2 == 0) {
				c2 = c1;
				ax /= 2;
			}
		}

		int r1 = (c1 >> 16 & 255) * a1;
		int g1 = (c1 >> 8 & 255) * a1;
		int b1 = (c1 & 255) * a1;
		int r2 = (c2 >> 16 & 255) * a2;
		int g2 = (c2 >> 8 & 255) * a2;
		int b2 = (c2 & 255) * a2;
		int rx = (r1 + r2) / (a1 + a2);
		int gx = (g1 + g2) / (a1 + a2);
		int bx = (b1 + b2) / (a1 + a2);
		return ax << 24 | rx << 16 | gx << 8 | bx;
	}

	public void refreshTextures() {
		this.textureDataMap.clear();
		this.textureFxMap.clear();
		this.dynamicTexturesUpdated = false;
		Config.setTextureUpdateTime(System.currentTimeMillis());
		WrUpdates.finishCurrentUpdate();
		RandomMobs.resetTextures();
		this.mipDataBufsMap.clear();
		this.customAnimationMap.clear();
		TexturePackBase texturepackbase = this.texturePack.selectedTexturePack;
		Iterator iterator3 = this.textureNameToImageMap.getKeySet().iterator();

		while(iterator3.hasNext()) {
			int i = ((Integer)iterator3.next()).intValue();
			BufferedImage bufferedimage = (BufferedImage)this.textureNameToImageMap.lookup(i);
			this.setupTexture(bufferedimage, i);
		}

		ThreadDownloadImageData s1;
		for(iterator3 = this.urlToImageDataMap.values().iterator(); iterator3.hasNext(); s1.textureSetupComplete = false) {
			s1 = (ThreadDownloadImageData)iterator3.next();
		}

		iterator3 = this.textureMap.keySet().iterator();

		BufferedImage e;
		String s11;
		while(iterator3.hasNext()) {
			s11 = (String)iterator3.next();

			try {
				if(s11.startsWith("##")) {
					e = this.unwrapImageByColumns(this.readTextureImage(texturepackbase.getResourceAsStream(s11.substring(2))));
				} else if(s11.startsWith("%clamp%")) {
					this.clampTexture = true;
					e = this.readTextureImage(texturepackbase.getResourceAsStream(s11.substring(7)));
				} else if(s11.startsWith("%blur%")) {
					this.blurTexture = true;
					e = this.readTextureImage(texturepackbase.getResourceAsStream(s11.substring(6)));
				} else if(s11.startsWith("%blurclamp%")) {
					this.blurTexture = true;
					this.clampTexture = true;
					e = this.readTextureImage(texturepackbase.getResourceAsStream(s11.substring(11)));
				} else {
					e = this.readTextureImage(texturepackbase.getResourceAsStream(s11));
				}

				int j = ((Integer)this.textureMap.get(s11)).intValue();
				this.setupTexture(e, j);
				this.blurTexture = false;
				this.clampTexture = false;
			} catch (Exception exception9) {
				if(!"input == null!".equals(exception9.getMessage())) {
					exception9.printStackTrace();
				}
			}
		}

		iterator3 = this.textureContentsMap.keySet().iterator();

		while(iterator3.hasNext()) {
			s11 = (String)iterator3.next();

			try {
				if(s11.startsWith("##")) {
					e = this.unwrapImageByColumns(this.readTextureImage(texturepackbase.getResourceAsStream(s11.substring(2))));
				} else if(s11.startsWith("%clamp%")) {
					this.clampTexture = true;
					e = this.readTextureImage(texturepackbase.getResourceAsStream(s11.substring(7)));
				} else if(s11.startsWith("%blur%")) {
					this.blurTexture = true;
					e = this.readTextureImage(texturepackbase.getResourceAsStream(s11.substring(6)));
				} else {
					e = this.readTextureImage(texturepackbase.getResourceAsStream(s11));
				}

				this.getImageContents(e, (int[])((int[])this.textureContentsMap.get(s11)));
				this.blurTexture = false;
				this.clampTexture = false;
			} catch (Exception exception8) {
				if(!"input == null!".equals(exception8.getMessage())) {
					exception8.printStackTrace();
				}
			}
		}

		this.registerCustomTexturesFX();
		CustomColorizer.update(this);
		ConnectedTextures.update(this);
		NaturalTextures.update(this);
		if(Reflector.hasClass(7)) {
			Reflector.callVoid(73, new Object[]{this, texturepackbase, this.textureList});
		}

		this.updateDynamicTextures();
	}

	private BufferedImage readTextureImage(InputStream par1InputStream) throws IOException {
		BufferedImage bufferedimage = ImageIO.read(par1InputStream);
		par1InputStream.close();
		return bufferedimage;
	}

	public void bindTexture(int par1) {
		if(par1 >= 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1);
		}
	}

	private void setTextureDimension(int texId, Dimension dim) {
		this.textureDimensionsMap.put(new Integer(texId), dim);
		if(texId == this.terrainTextureId) {
			Config.setIconWidthTerrain(dim.width / 16);
		}

		if(texId == this.guiItemsTextureId) {
			Config.setIconWidthItems(dim.width / 16);
		}

		this.updateDinamicTextures(texId, dim);
	}

	public Dimension getTextureDimensions(int id) {
		Dimension dim = (Dimension)this.textureDimensionsMap.get(new Integer(id));
		return dim;
	}

	private void updateDinamicTextures(int texId, Dimension dim) {
		for(int i = 0; i < this.textureList.size(); ++i) {
			TextureFX fx = (TextureFX)this.textureList.get(i);
			int fxTexId = this.getTextureId(fx);
			if(fxTexId == texId && fx instanceof TextureHDFX) {
				TextureHDFX texHD = (TextureHDFX)fx;
				texHD.setTexturePackBase(this.texturePack.selectedTexturePack);
				texHD.setTileWidth(dim.width / 16);
				texHD.onTick();
			}
		}

	}

	public boolean updateCustomTexture(TextureFX texturefx, int texId, ByteBuffer imgData, int tileWidth, StringBuffer dataIdBuf) {
		if(texId == this.terrainTextureId) {
			if(texturefx.iconIndex == Block.waterStill.blockIndexInTexture) {
				if(Config.isGeneratedWater()) {
					return false;
				}

				return this.updateCustomTexture(texturefx, "/custom_water_still.png", imgData, tileWidth, Config.isAnimatedWater(), 1, dataIdBuf);
			}

			if(texturefx.iconIndex == Block.waterStill.blockIndexInTexture + 1) {
				if(Config.isGeneratedWater()) {
					return false;
				}

				return this.updateCustomTexture(texturefx, "/custom_water_flowing.png", imgData, tileWidth, Config.isAnimatedWater(), 1, dataIdBuf);
			}

			if(texturefx.iconIndex == Block.lavaStill.blockIndexInTexture) {
				if(Config.isGeneratedLava()) {
					return false;
				}

				return this.updateCustomTexture(texturefx, "/custom_lava_still.png", imgData, tileWidth, Config.isAnimatedLava(), 1, dataIdBuf);
			}

			if(texturefx.iconIndex == Block.lavaStill.blockIndexInTexture + 1) {
				if(Config.isGeneratedLava()) {
					return false;
				}

				return this.updateCustomTexture(texturefx, "/custom_lava_flowing.png", imgData, tileWidth, Config.isAnimatedLava(), 1, dataIdBuf);
			}

			if(texturefx.iconIndex == Block.portal.blockIndexInTexture) {
				return this.updateCustomTexture(texturefx, "/custom_portal.png", imgData, tileWidth, Config.isAnimatedPortal(), 1, dataIdBuf);
			}

			if(texturefx.iconIndex == Block.fire.blockIndexInTexture) {
				return this.updateCustomTexture(texturefx, "/custom_fire_n_s.png", imgData, tileWidth, Config.isAnimatedFire(), 1, dataIdBuf);
			}

			if(texturefx.iconIndex == Block.fire.blockIndexInTexture + 16) {
				return this.updateCustomTexture(texturefx, "/custom_fire_e_w.png", imgData, tileWidth, Config.isAnimatedFire(), 1, dataIdBuf);
			}

			if(Config.isAnimatedTerrain()) {
				return this.updateCustomTexture(texturefx, "/custom_terrain_" + texturefx.iconIndex + ".png", imgData, tileWidth, Config.isAnimatedTerrain(), 1, dataIdBuf);
			}
		}

		return texId == this.guiItemsTextureId && Config.isAnimatedItems() ? this.updateCustomTexture(texturefx, "/custom_item_" + texturefx.iconIndex + ".png", imgData, tileWidth, Config.isAnimatedTerrain(), 1, dataIdBuf) : false;
	}

	private boolean updateDefaultTexture(TextureFX texturefx, int texId, ByteBuffer imgData, int tileWidth, StringBuffer dataIdBuf) {
		return texId != this.terrainTextureId ? false : (this.texturePack.selectedTexturePack instanceof TexturePackDefault ? false : (texturefx.iconIndex == Block.waterStill.blockIndexInTexture ? (Config.isGeneratedWater() ? false : this.updateDefaultTexture(texturefx, imgData, tileWidth, false, 1, dataIdBuf)) : (texturefx.iconIndex == Block.waterStill.blockIndexInTexture + 1 ? (Config.isGeneratedWater() ? false : this.updateDefaultTexture(texturefx, imgData, tileWidth, Config.isAnimatedWater(), 1, dataIdBuf)) : (texturefx.iconIndex == Block.lavaStill.blockIndexInTexture ? (Config.isGeneratedLava() ? false : this.updateDefaultTexture(texturefx, imgData, tileWidth, false, 1, dataIdBuf)) : (texturefx.iconIndex == Block.lavaStill.blockIndexInTexture + 1 ? (Config.isGeneratedLava() ? false : this.updateDefaultTexture(texturefx, imgData, tileWidth, Config.isAnimatedLava(), 3, dataIdBuf)) : false)))));
	}

	private boolean updateDefaultTexture(TextureFX texturefx, ByteBuffer imgData, int tileWidth, boolean scrolling, int scrollDiv, StringBuffer dataIdBuf) {
		int iconIndex = texturefx.iconIndex;
		if(!scrolling && this.dynamicTexturesUpdated) {
			return true;
		} else {
			byte[] tileData = this.getTerrainIconData(iconIndex, tileWidth, dataIdBuf);
			if(tileData == null) {
				return false;
			} else {
				imgData.clear();
				int imgLen = tileData.length;
				if(scrolling) {
					int movNum = tileWidth - this.tickCounter / scrollDiv % tileWidth;
					int offset = movNum * tileWidth * 4;
					imgData.put(tileData, offset, imgLen - offset);
					imgData.put(tileData, 0, offset);
					dataIdBuf.append(":");
					dataIdBuf.append(movNum);
				} else {
					imgData.put(tileData, 0, imgLen);
				}

				imgData.position(0).limit(imgLen);
				return true;
			}
		}
	}

	private boolean updateCustomTexture(TextureFX texturefx, String imagePath, ByteBuffer imgData, int tileWidth, boolean animated, int animDiv, StringBuffer dataIdBuf) {
		CustomAnimation anim = this.getCustomAnimation(imagePath, tileWidth, tileWidth, animDiv);
		return anim == null ? false : anim.updateCustomTexture(imgData, animated, this.dynamicTexturesUpdated, dataIdBuf);
	}

	private CustomAnimation getCustomAnimation(String imagePath, int tileWidth, int tileHeight, int durMul) {
		CustomAnimation anim = (CustomAnimation)this.customAnimationMap.get(imagePath);
		if(anim == null) {
			if(this.customAnimationMap.containsKey(imagePath)) {
				return null;
			}

			byte[] data = this.getCustomTextureData(imagePath, tileWidth);
			if(data == null) {
				this.customAnimationMap.put(imagePath, (Object)null);
				return null;
			}

			Properties props = new Properties();
			String propName = this.makePropertiesName(imagePath);
			if(propName != null) {
				try {
					InputStream e = this.texturePack.selectedTexturePack.getResourceAsStream(propName);
					if(e == null) {
						e = this.texturePack.selectedTexturePack.getResourceAsStream("/anim" + propName);
					}

					if(e != null) {
						props.load(e);
					}
				} catch (IOException iOException10) {
					iOException10.printStackTrace();
				}
			}

			anim = new CustomAnimation(imagePath, data, tileWidth, tileHeight, props, durMul);
			this.customAnimationMap.put(imagePath, anim);
		}

		return anim;
	}

	private String makePropertiesName(String imagePath) {
		if(!imagePath.endsWith(".png")) {
			return null;
		} else {
			int pos = imagePath.lastIndexOf(".png");
			if(pos < 0) {
				return null;
			} else {
				String propsName = imagePath.substring(0, pos) + ".properties";
				return propsName;
			}
		}
	}

	private byte[] getTerrainIconData(int tileNum, int tileWidth, StringBuffer dataIdBuf) {
		String tileIdStr = "Tile-" + tileNum;
		byte[] tileData = this.getCustomTextureData(tileIdStr, tileWidth);
		if(tileData != null) {
			dataIdBuf.append(tileIdStr);
			return tileData;
		} else {
			byte[] terrainData = this.getCustomTextureData("/terrain.png", tileWidth * 16);
			if(terrainData == null) {
				return null;
			} else {
				tileData = new byte[tileWidth * tileWidth * 4];
				int tx = tileNum % 16;
				int ty = tileNum / 16;
				int xMin = tx * tileWidth;
				int yMin = ty * tileWidth;
				int i10000 = xMin + tileWidth;
				i10000 = yMin + tileWidth;

				for(int y = 0; y < tileWidth; ++y) {
					int ys = yMin + y;

					for(int x = 0; x < tileWidth; ++x) {
						int xs = xMin + x;
						int posSrc = 4 * (xs + ys * tileWidth * 16);
						int posDst = 4 * (x + y * tileWidth);
						tileData[posDst + 0] = terrainData[posSrc + 0];
						tileData[posDst + 1] = terrainData[posSrc + 1];
						tileData[posDst + 2] = terrainData[posSrc + 2];
						tileData[posDst + 3] = terrainData[posSrc + 3];
					}
				}

				this.setCustomTextureData(tileIdStr, tileData);
				dataIdBuf.append(tileIdStr);
				return tileData;
			}
		}
	}

	public byte[] getCustomTextureData(String imagePath, int tileWidth) {
		byte[] imageBytes = (byte[])((byte[])this.textureDataMap.get(imagePath));
		if(imageBytes == null) {
			if(this.textureDataMap.containsKey(imagePath)) {
				return null;
			}

			imageBytes = this.loadImage(imagePath, tileWidth);
			if(imageBytes == null) {
				imageBytes = this.loadImage("/anim" + imagePath, tileWidth);
			}

			this.textureDataMap.put(imagePath, imageBytes);
		}

		return imageBytes;
	}

	private void setCustomTextureData(String imagePath, byte[] data) {
		this.textureDataMap.put(imagePath, data);
	}

	private byte[] loadImage(String name, int targetWidth) {
		try {
			TexturePackBase e = this.texturePack.selectedTexturePack;
			if(e == null) {
				return null;
			} else {
				InputStream in = e.getResourceAsStream(name);
				if(in == null) {
					return null;
				} else {
					BufferedImage image = this.readTextureImage(in);
					if(image == null) {
						return null;
					} else {
						if(targetWidth > 0 && image.getWidth() != targetWidth) {
							double width = (double)(image.getHeight() / image.getWidth());
							int ai = (int)((double)targetWidth * width);
							image = scaleBufferedImage(image, targetWidth, ai);
						}

						int i19 = image.getWidth();
						int height = image.getHeight();
						int[] i20 = new int[i19 * height];
						byte[] byteBuf = new byte[i19 * height * 4];
						image.getRGB(0, 0, i19, height, i20, 0, i19);

						for(int l = 0; l < i20.length; ++l) {
							int alpha = i20[l] >> 24 & 255;
							int red = i20[l] >> 16 & 255;
							int green = i20[l] >> 8 & 255;
							int blue = i20[l] & 255;
							if(this.options != null && this.options.anaglyph) {
								int j3 = (red * 30 + green * 59 + blue * 11) / 100;
								int l3 = (red * 30 + green * 70) / 100;
								int j4 = (red * 30 + blue * 70) / 100;
								red = j3;
								green = l3;
								blue = j4;
							}

							byteBuf[l * 4 + 0] = (byte)red;
							byteBuf[l * 4 + 1] = (byte)green;
							byteBuf[l * 4 + 2] = (byte)blue;
							byteBuf[l * 4 + 3] = (byte)alpha;
						}

						return byteBuf;
					}
				}
			}
		} catch (Exception exception18) {
			exception18.printStackTrace();
			return null;
		}
	}

	public static BufferedImage scaleBufferedImage(BufferedImage image, int width, int height) {
		BufferedImage scaledImage = new BufferedImage(width, height, 2);
		Graphics2D gr = scaledImage.createGraphics();
		gr.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		gr.drawImage(image, 0, 0, width, height, (ImageObserver)null);
		return scaledImage;
	}

	private void checkImageDataSize(int width, int height) {
		if(this.imageData != null) {
			int len = width * height * 4;
			if(this.imageData.capacity() >= len) {
				return;
			}
		}

		this.allocateImageData(width, height);
	}

	private void allocateImageData(int width, int height) {
		int imgLen = width * height * 4;
		this.imageData = GLAllocation.createDirectByteBuffer(imgLen);
		ArrayList list = new ArrayList();
		int mipWidth = width / 2;

		for(int mipHeight = height / 2; mipWidth > 0 && mipHeight > 0; mipHeight /= 2) {
			int mipLen = mipWidth * mipHeight * 4;
			ByteBuffer buf = GLAllocation.createDirectByteBuffer(mipLen);
			list.add(buf);
			mipWidth /= 2;
		}

		this.mipImageDatas = (ByteBuffer[])((ByteBuffer[])list.toArray(new ByteBuffer[list.size()]));
	}

	public void checkHdTextures() {
		if(!this.hdTexturesInstalled) {
			Minecraft mc = Config.getMinecraft();
			if(mc != null) {
				this.hdTexturesInstalled = true;
				this.registerTextureFX(new TextureHDLavaFX());
				this.registerTextureFX(new TextureHDWaterFX());
				this.registerTextureFX(new TextureHDPortalFX());
				this.registerTextureFX(new TextureHDWaterFlowFX());
				this.registerTextureFX(new TextureHDLavaFlowFX());
				this.registerTextureFX(new TextureHDFlamesFX(0));
				this.registerTextureFX(new TextureHDFlamesFX(1));
				this.registerTextureFX(new TextureHDCompassFX(mc));
				this.registerTextureFX(new TextureHDWatchFX(mc));
				this.registerCustomTexturesFX();
				CustomColorizer.update(this);
				ConnectedTextures.update(this);
				NaturalTextures.update(this);
			}
		}
	}

	private void registerCustomTexturesFX() {
		TextureFX[] customTextures = this.getRegisteredTexturesFX(TextureHDCustomFX.class);

		int i;
		for(i = 0; i < customTextures.length; ++i) {
			TextureFX fx = customTextures[i];
			this.unregisterTextureFX(fx);
		}

		if(Config.isAnimatedTerrain()) {
			for(i = 0; i < 256; ++i) {
				this.registerCustomTextureFX("/custom_terrain_" + i + ".png", i, 0);
			}
		}

		if(Config.isAnimatedItems()) {
			for(i = 0; i < 256; ++i) {
				this.registerCustomTextureFX("/custom_item_" + i + ".png", i, 1);
			}
		}

		this.textureAnimations = this.getTextureAnimations();
	}

	private CustomAnimation[] getTextureAnimations() {
		String tpName = this.texturePack.selectedTexturePack.texturePackFileName;
		File dirTexturepacks = new File(Config.getMinecraft().mcDataDir, "texturepacks");
		File tpFile = new File(dirTexturepacks, tpName);
		if(!tpFile.exists()) {
			return null;
		} else {
			Properties[] animProps = null;
			if(tpFile.isFile()) {
				animProps = this.getAnimationPropertiesZip(tpFile);
			} else {
				animProps = this.getAnimationPropertiesDir(tpFile);
			}

			if(animProps == null) {
				return null;
			} else {
				ArrayList list = new ArrayList();

				for(int anims = 0; anims < animProps.length; ++anims) {
					Properties props = animProps[anims];
					CustomAnimation anim = this.makeTextureAnimation(props);
					if(anim != null) {
						list.add(anim);
					}
				}

				CustomAnimation[] customAnimation9 = (CustomAnimation[])((CustomAnimation[])list.toArray(new CustomAnimation[list.size()]));
				return customAnimation9;
			}
		}
	}

	private CustomAnimation makeTextureAnimation(Properties props) {
		String texFrom = props.getProperty("from");
		String texTo = props.getProperty("to");
		int x = Config.parseInt(props.getProperty("x"), -1);
		int y = Config.parseInt(props.getProperty("y"), -1);
		int width = Config.parseInt(props.getProperty("w"), -1);
		int height = Config.parseInt(props.getProperty("h"), -1);
		if(texFrom != null && texTo != null) {
			if(x >= 0 && y >= 0 && width >= 0 && height >= 0) {
				byte[] imageBytes = this.getCustomTextureData(texFrom, width);
				if(imageBytes == null) {
					return null;
				} else {
					CustomAnimation anim = new CustomAnimation(texFrom, imageBytes, width, height, props, 1);
					anim.destTexture = texTo;
					anim.destX = x;
					anim.destY = y;
					return anim;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private Properties[] getAnimationPropertiesDir(File tpDir) {
		File dirAnim = new File(tpDir, "anim");
		if(!dirAnim.exists()) {
			return null;
		} else if(!dirAnim.isDirectory()) {
			return null;
		} else {
			File[] propFiles = dirAnim.listFiles();
			if(propFiles == null) {
				return null;
			} else {
				try {
					ArrayList e = new ArrayList();

					for(int props = 0; props < propFiles.length; ++props) {
						File file = propFiles[props];
						String name = file.getName();
						if(!name.startsWith("custom_") && name.endsWith(".properties") && file.isFile() && file.canRead()) {
							FileInputStream fin = new FileInputStream(file);
							Properties props1 = new Properties();
							props1.load(fin);
							fin.close();
							e.add(props1);
						}
					}

					Properties[] properties11 = (Properties[])((Properties[])e.toArray(new Properties[e.size()]));
					return properties11;
				} catch (IOException iOException10) {
					iOException10.printStackTrace();
					return null;
				}
			}
		}
	}

	private Properties[] getAnimationPropertiesZip(File tpFile) {
		try {
			ZipFile e = new ZipFile(tpFile);
			Enumeration en = e.entries();
			ArrayList list = new ArrayList();

			while(en.hasMoreElements()) {
				ZipEntry props = (ZipEntry)en.nextElement();
				String name = props.getName();
				if(name.startsWith("anim/") && !name.startsWith("anim/custom_") && name.endsWith(".properties")) {
					InputStream in = e.getInputStream(props);
					Properties props1 = new Properties();
					props1.load(in);
					in.close();
					list.add(props1);
				}
			}

			Properties[] props2 = (Properties[])((Properties[])list.toArray(new Properties[list.size()]));
			return props2;
		} catch (IOException iOException9) {
			iOException9.printStackTrace();
			return null;
		}
	}

	private void unregisterTextureFX(TextureFX texFX) {
		for(int i = 0; i < this.textureList.size(); ++i) {
			TextureFX fx = (TextureFX)this.textureList.get(i);
			if(fx == texFX) {
				this.textureList.remove(i);
				--i;
			}
		}

	}

	private TextureFX[] getRegisteredTexturesFX(Class cls) {
		ArrayList list = new ArrayList();

		for(int texs = 0; texs < this.textureList.size(); ++texs) {
			TextureFX fx = (TextureFX)this.textureList.get(texs);
			if(cls.isAssignableFrom(fx.getClass())) {
				list.add(fx);
			}
		}

		TextureFX[] textureFX5 = (TextureFX[])((TextureFX[])list.toArray(new TextureFX[list.size()]));
		return textureFX5;
	}

	private void registerCustomTextureFX(String path, int index, int tileImage) {
		Object data = null;
		byte[] data1;
		if(tileImage == 0) {
			data1 = this.getCustomTextureData(path, Config.getIconWidthTerrain());
		} else {
			data1 = this.getCustomTextureData(path, Config.getIconWidthItems());
		}

		if(data1 != null) {
			this.registerTextureFX(new TextureHDCustomFX(index, tileImage));
		}
	}

	private int getMaxMipmapLevel(int size) {
		int level;
		for(level = 0; size > 0; ++level) {
			size /= 2;
		}

		return level - 1;
	}

	private void copyScaled(byte[] buf, ByteBuffer dstBuf, int dstWidth) {
		int srcWidth = (int)Math.sqrt((double)(buf.length / 4));
		int scale = dstWidth / srcWidth;
		byte[] buf4 = new byte[4];
		int len = dstWidth * dstWidth;
		dstBuf.clear();
		if(scale > 1) {
			for(int y = 0; y < srcWidth; ++y) {
				int yMul = y * srcWidth;
				int ty = y * scale;
				int tyMul = ty * dstWidth;

				for(int x = 0; x < srcWidth; ++x) {
					int srcPos = (x + yMul) * 4;
					buf4[0] = buf[srcPos];
					buf4[1] = buf[srcPos + 1];
					buf4[2] = buf[srcPos + 2];
					buf4[3] = buf[srcPos + 3];
					int tx = x * scale;
					int dstPosBase = tx + tyMul;

					for(int tdy = 0; tdy < scale; ++tdy) {
						int dstPosY = dstPosBase + tdy * dstWidth;
						dstBuf.position(dstPosY * 4);

						for(int tdx = 0; tdx < scale; ++tdx) {
							dstBuf.put(buf4);
						}
					}
				}
			}
		}

		dstBuf.position(0).limit(dstWidth * dstWidth * 4);
	}

	private boolean scalesWithFastColor(TextureFX texturefx) {
		return !texturefx.getClass().getName().equals("ModTextureStatic");
	}

	private boolean isTileClamped(int texId, int iconIndex) {
		return texId != this.terrainTextureId || !Config.between(iconIndex, 0, 2) && !Config.between(iconIndex, 4, 10) && !Config.between(iconIndex, 16, 21) && !Config.between(iconIndex, 32, 37) && !Config.between(iconIndex, 40, 40) && !Config.between(iconIndex, 48, 53) && !Config.between(iconIndex, 64, 67) && !Config.between(iconIndex, 69, 75) && !Config.between(iconIndex, 86, 87) && !Config.between(iconIndex, 102, 107) && !Config.between(iconIndex, 109, 110) && !Config.between(iconIndex, 113, 114) && !Config.between(iconIndex, 116, 121) && !Config.between(iconIndex, 129, 133) && !Config.between(iconIndex, 144, 147) && !Config.between(iconIndex, 160, 165) && !Config.between(iconIndex, 176, 181) && !Config.between(iconIndex, 192, 195) && !Config.between(iconIndex, 205, 207) && !Config.between(iconIndex, 208, 210) && !Config.between(iconIndex, 222, 223) && !Config.between(iconIndex, 225, 225) && !Config.between(iconIndex, 237, 239) && !Config.between(iconIndex, 240, 249) && !Config.between(iconIndex, 254, 255);
	}

	public TexturePackList getTexturePack() {
		return this.texturePack;
	}
}
