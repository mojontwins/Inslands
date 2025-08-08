package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import net.minecraft.client.Config;

import org.lwjgl.opengl.GL15;

public class Tessellator {
	private static boolean convertQuadsToTriangles = false;
	private static boolean tryVBO = false;
	private ByteBuffer byteBuffer;
	private IntBuffer intBuffer;
	private FloatBuffer floatBuffer;
	private ShortBuffer shortBuffer;
	private int[] rawBuffer;
	private int vertexCount;
	private double textureU;
	private double textureV;
	private int brightness;
	private int color;
	private boolean hasColor;
	private boolean hasTexture;
	private boolean hasBrightness;
	private boolean hasNormals;
	private int rawBufferIndex;
	private int addedVertices;
	private boolean isColorDisabled;
	public int drawMode;
	public double xOffset;
	public double yOffset;
	public double zOffset;
	private int normal;
	public static Tessellator instance = new Tessellator(524288);
	public boolean isDrawing;
	private boolean useVBO;
	private IntBuffer vertexBuffers;
	private int vboIndex;
	private int vboCount;
	private int bufferSize;
	private boolean renderingChunk;
	private static boolean littleEndianByteOrder = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN;
	public static boolean renderingWorldRenderer = false;
	public boolean defaultTexture;
	public int textureID;
	public boolean autoGrow;
	private Tessellator[] subTessellators;
	private int[] subTextures;
	private static int terrainTexture = 0;
	//private long textureUpdateTime;
	public static int[][] atlasSubTextures = new int[0][];
	private VertexData[] vertexDatas;
	private boolean[] drawnIcons;
	private int[] vertexIconIndex;
	private int[] tileTextures;

	public Tessellator() {
		this(65536);
		this.defaultTexture = false;
	}

	public Tessellator(int par1) {
		this.renderingChunk = false;
		this.defaultTexture = true;
		this.textureID = 0;
		this.autoGrow = true;
		this.subTessellators = new Tessellator[0];
		this.subTextures = new int[0];
		//this.textureUpdateTime = 0L;
		this.vertexDatas = null;
		this.drawnIcons = new boolean[256];
		this.vertexIconIndex = null;
		this.tileTextures = null;
		this.vertexCount = 0;
		this.hasColor = false;
		this.hasTexture = false;
		this.hasBrightness = false;
		this.hasNormals = false;
		this.rawBufferIndex = 0;
		this.addedVertices = 0;
		this.isColorDisabled = false;
		this.isDrawing = false;
		this.useVBO = false;
		this.vboIndex = 0;
		this.vboCount = 10;
		this.bufferSize = par1;
		this.byteBuffer = GLAllocation.createDirectByteBuffer(par1 * 4);
		this.intBuffer = this.byteBuffer.asIntBuffer();
		this.floatBuffer = this.byteBuffer.asFloatBuffer();
		this.shortBuffer = this.byteBuffer.asShortBuffer();
		this.rawBuffer = new int[par1];
		this.useVBO = tryVBO && GLContext.getCapabilities().GL_ARB_vertex_buffer_object;
		if(this.useVBO) {
			this.vertexBuffers = GLAllocation.createDirectIntBuffer(this.vboCount);
			ARBVertexBufferObject.glGenBuffersARB(this.vertexBuffers);
		}

		this.vertexDatas = new VertexData[4];

		for(int ix = 0; ix < this.vertexDatas.length; ++ix) {
			this.vertexDatas[ix] = new VertexData();
		}

		this.vertexIconIndex = new int[this.bufferSize];
	}

	private void draw(int startVertex, int endVertex) {
		int vxCount = endVertex - startVertex;
		if(vxCount > 0) {
			if(vxCount % 4 == 0) {
				if(this.useVBO) {
					throw new IllegalStateException("VBO not implemented");
				} else {
					// Texture
					this.floatBuffer.position(3);
					GL11.glTexCoordPointer(2, 32, this.floatBuffer);
					
					// Brightness
					OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
					this.shortBuffer.position(14);
					GL11.glTexCoordPointer(2, 32, this.shortBuffer);
					GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
					OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
					
					// Color
					this.byteBuffer.position(20);
					GL11.glColorPointer(4, true, 32, this.byteBuffer);
					
					// Normals
					this.floatBuffer.position(0);
					GL11.glVertexPointer(3, 32, this.floatBuffer);
					if(this.drawMode == 7 && convertQuadsToTriangles) {
						GL11.glDrawArrays(GL11.GL_TRIANGLES, startVertex, vxCount);
					} else {
						GL11.glDrawArrays(this.drawMode, startVertex, vxCount);
					}

				}
			}
		}
	}

	private int drawForIcon(int iconIndex, int startPos) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.tileTextures[iconIndex]);
		int firstRegionEnd = -1;
		int lastPos = -1;

		for(int i = startPos; i < this.addedVertices; ++i) {
			int icon = this.vertexIconIndex[i];
			if(icon == iconIndex) {
				if(lastPos < 0) {
					lastPos = i;
				}
			} else if(lastPos >= 0) {
				this.draw(lastPos, i);
				lastPos = -1;
				if(firstRegionEnd < 0) {
					firstRegionEnd = i;
				}
			}
		}

		if(lastPos >= 0) {
			this.draw(lastPos, this.addedVertices);
		}

		if(firstRegionEnd < 0) {
			firstRegionEnd = this.addedVertices;
		}

		return firstRegionEnd;
	}

	public int draw() {
		if(!this.isDrawing) {
			throw new IllegalStateException("Not tesselating!");
		} else {
			int i1;
			int iconIndex;
			if(this.renderingChunk && this.subTessellators.length > 0) {
				boolean i = false;

				for(i1 = 0; i1 < this.subTessellators.length; ++i1) {
					iconIndex = this.subTextures[i1];
					if(iconIndex <= 0) {
						break;
					}

					Tessellator tess = this.subTessellators[i1];
					if(tess.isDrawing) {
						GL11.glBindTexture(GL11.GL_TEXTURE_2D, iconIndex);
						tess.draw();
						i = true;
					}
				}

				if(i) {
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, getTerrainTexture());
				}
			}

			this.isDrawing = false;
			int i5;
			if(this.vertexCount > 0) {
				if(Config.isMultiTexture() && this.tileTextures != null) {
					this.intBuffer.clear();
					this.intBuffer.put(this.rawBuffer, 0, this.rawBufferIndex);
					this.byteBuffer.position(0);
					this.byteBuffer.limit(this.rawBufferIndex * 4);
					GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
					GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
					GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
					Arrays.fill(this.drawnIcons, false);
					i5 = 0;

					for(i1 = 0; i1 < this.addedVertices; ++i1) {
						iconIndex = this.vertexIconIndex[i1];
						if(!this.drawnIcons[iconIndex]) {
							i1 = this.drawForIcon(iconIndex, i1) - 1;
							++i5;
							this.drawnIcons[iconIndex] = true;
						}
					}

					GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
					OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
					GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
					OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
					GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
					GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
				} else {
					this.intBuffer.clear();
					this.intBuffer.put(this.rawBuffer, 0, this.rawBufferIndex);
					this.byteBuffer.position(0);
					this.byteBuffer.limit(this.rawBufferIndex * 4);
					if(this.useVBO) {
						this.vboIndex = (this.vboIndex + 1) % this.vboCount;
						ARBVertexBufferObject.glBindBufferARB(GL15.GL_ARRAY_BUFFER, this.vertexBuffers.get(this.vboIndex));
						ARBVertexBufferObject.glBufferDataARB(GL15.GL_ARRAY_BUFFER, this.byteBuffer, GL15.GL_STREAM_DRAW);
					}

					if(this.hasTexture) {
						if(this.useVBO) {
							GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 32, 12L);
						} else {
							this.floatBuffer.position(3);
							GL11.glTexCoordPointer(2, 32, this.floatBuffer);
						}

						GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
					}

					if(this.hasBrightness) {

						OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
						if(this.useVBO) {
							GL11.glTexCoordPointer(2, GL11.GL_SHORT, 32, 28L);
						} else {
							this.shortBuffer.position(14);
							GL11.glTexCoordPointer(2, 32, this.shortBuffer);
						}

						GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
						OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
					}

					if(this.hasColor) {
						if(this.useVBO) {
							GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 32, 20L);
						} else {
							this.byteBuffer.position(20);
							GL11.glColorPointer(4, true, 32, this.byteBuffer);
						}

						GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
					}

					if(this.hasNormals) {
						if(this.useVBO) {
							GL11.glNormalPointer(GL11.GL_UNSIGNED_BYTE, 32, 24L);
						} else {
							this.byteBuffer.position(24);
							GL11.glNormalPointer(32, this.byteBuffer);
						}

						GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
					}

					if(this.useVBO) {
						GL11.glVertexPointer(3, GL11.GL_FLOAT, 32, 0L);
					} else {
						this.floatBuffer.position(0);
						GL11.glVertexPointer(3, 32, this.floatBuffer);
					}

					GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
					if(this.drawMode == 7 && convertQuadsToTriangles) {
						GL11.glDrawArrays(GL11.GL_TRIANGLES, GL11.GL_POINTS, this.vertexCount);
					} else {
						GL11.glDrawArrays(this.drawMode, GL11.GL_POINTS, this.vertexCount);
					}

					GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
					if(this.hasTexture) {
						GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
					}

					if(this.hasBrightness) {
						OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
						GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
						OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
					}

					if(this.hasColor) {
						GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
					}

					if(this.hasNormals) {
						GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
					}
				}
			}

			i5 = this.rawBufferIndex * 4;
			this.reset();
			return i5;
		}
	}

	private void reset() {
		this.vertexCount = 0;
		this.byteBuffer.clear();
		this.rawBufferIndex = 0;
		this.addedVertices = 0;
	}

	public void startDrawingQuads() {
		this.startDrawing(7);
	}

	public void startDrawing(int par1) {
		if(this.isDrawing) {
			throw new IllegalStateException("Already tesselating!");
		} else {
			this.isDrawing = true;
			this.reset();
			this.drawMode = par1;
			this.hasNormals = false;
			this.hasColor = false;
			this.hasTexture = false;
			this.hasBrightness = false;
			this.isColorDisabled = false;
			if(this.renderingChunk && this.textureID == 0) {
				this.tileTextures = getTileTextures(getTerrainTexture());
			} else {
				this.tileTextures = getTileTextures(this.textureID);
			}

		}
	}

	public void setTextureUV(double par1, double par3) {
		this.hasTexture = true;
		this.textureU = par1;
		this.textureV = par3;
	}

	public void setBrightness(int par1) {
		this.hasBrightness = true;
		this.brightness = par1;
	}

	public void setColorOpaque_F(float par1, float par2, float par3) {
		this.setColorOpaque((int)(par1 * 255.0F), (int)(par2 * 255.0F), (int)(par3 * 255.0F));
	}

	public void setColorRGBA_F(float par1, float par2, float par3, float par4) {
		this.setColorRGBA((int)(par1 * 255.0F), (int)(par2 * 255.0F), (int)(par3 * 255.0F), (int)(par4 * 255.0F));
	}

	public void setColorOpaque(int par1, int par2, int par3) {
		this.setColorRGBA(par1, par2, par3, 255);
	}

	public void setColorRGBA(int par1, int par2, int par3, int par4) {
		if(!this.isColorDisabled) {
			if(par1 > 255) {
				par1 = 255;
			}

			if(par2 > 255) {
				par2 = 255;
			}

			if(par3 > 255) {
				par3 = 255;
			}

			if(par4 > 255) {
				par4 = 255;
			}

			if(par1 < 0) {
				par1 = 0;
			}

			if(par2 < 0) {
				par2 = 0;
			}

			if(par3 < 0) {
				par3 = 0;
			}

			if(par4 < 0) {
				par4 = 0;
			}

			this.hasColor = true;
			if(littleEndianByteOrder) {
				this.color = par4 << 24 | par3 << 16 | par2 << 8 | par1;
			} else {
				this.color = par1 << 24 | par2 << 16 | par3 << 8 | par4;
			}

		}
	}

	public void addVertexWithUV(double x, double y, double z, double u, double v) {
		if(Config.isMultiTexture() && this.tileTextures != null) {
			int numInQuad = this.addedVertices % 4;
			VertexData vd = this.vertexDatas[numInQuad];
			vd.x = x;
			vd.y = y;
			vd.z = z;
			vd.u = u;
			vd.v = v;
			vd.color = this.color;
			vd.brightness = this.brightness;
			if(numInQuad != 3) {
				++this.addedVertices;
			} else {
				this.addedVertices -= 3;
				double cu = (this.vertexDatas[0].u + this.vertexDatas[1].u + this.vertexDatas[2].u + this.vertexDatas[3].u) / 4.0D;
				double cv = (this.vertexDatas[0].v + this.vertexDatas[1].v + this.vertexDatas[2].v + this.vertexDatas[3].v) / 4.0D;
				
				/*
				if(cu > 0.875D && cu < 1.0D && cv > 0.75D && cv < 0.875D) {
					boolean tu = true;
				}
				*/

				int i29 = (int)(cu * 16.0D);
				int tv = (int)(cv * 16.0D);
				int iconIndex = tv * 16 + i29;
				double tu16 = (double)i29 / 16.0D;
				double tv16 = (double)tv / 16.0D;
				int vxNum = this.addedVertices;

				for(int i = 0; i < 4; ++i) {
					VertexData vdi = this.vertexDatas[i];
					x = vdi.x;
					y = vdi.y;
					z = vdi.z;
					u = vdi.u;
					v = vdi.v;
					this.vertexIconIndex[vxNum + i] = iconIndex;
					u -= tu16;
					v -= tv16;
					u *= 16.0D;
					v *= 16.0D;
					int oldColor = this.color;
					this.color = vdi.color;
					int oldBrightness = this.brightness;
					this.brightness = vdi.brightness;
					this.setTextureUV(u, v);
					this.addVertex(x, y, z);
					this.color = oldColor;
					this.brightness = oldBrightness;
				}

			}
		} else {
			this.setTextureUV(u, v);
			this.addVertex(x, y, z);
		}
	}

	public void addVertex(double par1, double par3, double par5) {
		if(this.autoGrow && this.rawBufferIndex >= this.bufferSize - 32) {
			Config.dbg("Expand tessellator buffer, old: " + this.bufferSize + ", new: " + this.bufferSize * 2);
			this.bufferSize *= 2;
			int[] i = new int[this.bufferSize];
			System.arraycopy(this.rawBuffer, 0, i, 0, this.rawBuffer.length);
			this.rawBuffer = i;
			this.byteBuffer = GLAllocation.createDirectByteBuffer(this.bufferSize * 4);
			this.intBuffer = this.byteBuffer.asIntBuffer();
			this.floatBuffer = this.byteBuffer.asFloatBuffer();
			this.shortBuffer = this.byteBuffer.asShortBuffer();
			int[] j = new int[this.bufferSize];
			System.arraycopy(this.vertexIconIndex, 0, j, 0, this.vertexIconIndex.length);
			this.vertexIconIndex = j;
		}

		++this.addedVertices;
		if(this.drawMode == 7 && convertQuadsToTriangles && this.addedVertices % 4 == 0) {
			for(int i9 = 0; i9 < 2; ++i9) {
				int i10 = 8 * (3 - i9);
				if(this.hasTexture) {
					this.rawBuffer[this.rawBufferIndex + 3] = this.rawBuffer[this.rawBufferIndex - i10 + 3];
					this.rawBuffer[this.rawBufferIndex + 4] = this.rawBuffer[this.rawBufferIndex - i10 + 4];
				}

				if(this.hasBrightness) {
					this.rawBuffer[this.rawBufferIndex + 7] = this.rawBuffer[this.rawBufferIndex - i10 + 7];
				}

				if(this.hasColor) {
					this.rawBuffer[this.rawBufferIndex + 5] = this.rawBuffer[this.rawBufferIndex - i10 + 5];
				}

				this.rawBuffer[this.rawBufferIndex + 0] = this.rawBuffer[this.rawBufferIndex - i10 + 0];
				this.rawBuffer[this.rawBufferIndex + 1] = this.rawBuffer[this.rawBufferIndex - i10 + 1];
				this.rawBuffer[this.rawBufferIndex + 2] = this.rawBuffer[this.rawBufferIndex - i10 + 2];
				++this.vertexCount;
				this.rawBufferIndex += 8;
			}
		}

		if(this.hasTexture) {
			this.rawBuffer[this.rawBufferIndex + 3] = Float.floatToRawIntBits((float)this.textureU);
			this.rawBuffer[this.rawBufferIndex + 4] = Float.floatToRawIntBits((float)this.textureV);
		}

		if(this.hasBrightness) {
			this.rawBuffer[this.rawBufferIndex + 7] = this.brightness;
		}

		if(this.hasColor) {
			this.rawBuffer[this.rawBufferIndex + 5] = this.color;
		}

		if(this.hasNormals) {
			this.rawBuffer[this.rawBufferIndex + 6] = this.normal;
		}

		this.rawBuffer[this.rawBufferIndex + 0] = Float.floatToRawIntBits((float)(par1 + this.xOffset));
		this.rawBuffer[this.rawBufferIndex + 1] = Float.floatToRawIntBits((float)(par3 + this.yOffset));
		this.rawBuffer[this.rawBufferIndex + 2] = Float.floatToRawIntBits((float)(par5 + this.zOffset));
		this.rawBufferIndex += 8;
		++this.vertexCount;
		if(!this.autoGrow && this.addedVertices % 4 == 0 && this.rawBufferIndex >= this.bufferSize - 32) {
			this.draw();
			this.isDrawing = true;
		}

	}

	public void setColorOpaque_I(int par1) {
		int i = par1 >> 16 & 255;
		int j = par1 >> 8 & 255;
		int k = par1 & 255;
		this.setColorOpaque(i, j, k);
	}

	public void setColorRGBA_I(int par1, int par2) {
		int i = par1 >> 16 & 255;
		int j = par1 >> 8 & 255;
		int k = par1 & 255;
		this.setColorRGBA(i, j, k, par2);
	}

	public void disableColor() {
		this.isColorDisabled = true;
	}

	public void setNormal(float par1, float par2, float par3) {
		this.hasNormals = true;
		byte byte0 = (byte)((int)(par1 * 127.0F));
		byte byte1 = (byte)((int)(par2 * 127.0F));
		byte byte2 = (byte)((int)(par3 * 127.0F));
		this.normal = byte0 & 255 | (byte1 & 255) << 8 | (byte2 & 255) << 16;
	}

	public void setTranslation(double par1, double par3, double par5) {
		this.xOffset = par1;
		this.yOffset = par3;
		this.zOffset = par5;
	}

	public void addTranslation(float par1, float par2, float par3) {
		this.xOffset += (double)par1;
		this.yOffset += (double)par2;
		this.zOffset += (double)par3;
	}

	public boolean isRenderingChunk() {
		return this.renderingChunk;
	}

	public void setRenderingChunk(boolean renderingChunk) {
		if(this.renderingChunk != renderingChunk) {
			for(int i = 0; i < this.subTextures.length; ++i) {
				this.subTextures[i] = 0;
			}
		}

		this.renderingChunk = renderingChunk;
	}

	public Tessellator getSubTessellator(int tex) {
		Tessellator newTess = this.getSubTessellatorImpl(tex);
		if(!newTess.isDrawing) {
			newTess.startDrawing(this.drawMode);
		}

		newTess.brightness = this.brightness;
		newTess.hasBrightness = this.hasBrightness;
		newTess.color = this.color;
		newTess.hasColor = this.hasColor;
		newTess.normal = this.normal;
		newTess.hasNormals = this.hasNormals;
		newTess.renderingChunk = this.renderingChunk;
		newTess.defaultTexture = false;
		newTess.xOffset = this.xOffset;
		newTess.yOffset = this.yOffset;
		newTess.zOffset = this.zOffset;
		return newTess;
	}

	public Tessellator getSubTessellatorImpl(int tex) {
		int newTess;
		int oldTess;
		Tessellator oldTexs;
		for(newTess = 0; newTess < this.subTextures.length; ++newTess) {
			oldTess = this.subTextures[newTess];
			if(oldTess == tex) {
				oldTexs = this.subTessellators[newTess];
				return oldTexs;
			}
		}

		for(newTess = 0; newTess < this.subTextures.length; ++newTess) {
			oldTess = this.subTextures[newTess];
			if(oldTess <= 0) {
				oldTexs = this.subTessellators[newTess];
				this.subTextures[newTess] = tex;
				return oldTexs;
			}
		}

		Tessellator tessellator5 = new Tessellator();
		tessellator5.textureID = tex;
		Tessellator[] tessellator6 = this.subTessellators;
		int[] i7 = this.subTextures;
		this.subTessellators = new Tessellator[tessellator6.length + 1];
		this.subTextures = new int[i7.length + 1];
		System.arraycopy(tessellator6, 0, this.subTessellators, 0, tessellator6.length);
		System.arraycopy(i7, 0, this.subTextures, 0, i7.length);
		this.subTessellators[tessellator6.length] = tessellator5;
		this.subTextures[i7.length] = tex;
		Config.dbg("Allocated subtessellator, count: " + this.subTessellators.length);
		return tessellator5;
	}

	public static int getTerrainTexture() {
		if(terrainTexture == 0) {
			terrainTexture = Config.getMinecraft().renderEngine.getTexture("/terrain.png");
		}

		return terrainTexture;
	}

	public static void setTileTextures(int texId, int[] tileTextures) {
		if(texId >= atlasSubTextures.length) {
			int[][] newSubTextures = new int[texId + 1][];
			System.arraycopy(atlasSubTextures, 0, newSubTextures, 0, atlasSubTextures.length);
			atlasSubTextures = newSubTextures;
		}

		atlasSubTextures[texId] = tileTextures;
	}

	public static int[] getTileTextures(int texId) {
		return texId >= atlasSubTextures.length ? null : atlasSubTextures[texId];
	}
}
