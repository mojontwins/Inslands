package net.minecraft.src;

import java.nio.ByteBuffer;
import java.util.Properties;

public class CustomAnimation {
	private String imagePath = null;
	public byte[] imageBytes = null;
	public int frameWidth = 0;
	public int frameHeight = 0;
	public CustomAnimationFrame[] frames = null;
	public int activeFrame = 0;
	public String destTexture = null;
	public int destX = 0;
	public int destY = 0;

	public CustomAnimation(String imagePath, byte[] data, int frameWidth, int frameHeight, Properties props, int durDef) {
		this.imagePath = imagePath;
		this.imageBytes = data;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		int frameLen = frameWidth * frameHeight * 4;
		if(data.length % frameLen != 0) {
			Config.dbg("Invalid animated texture length: " + data.length + ", frameWidth: " + frameHeight + ", frameHeight: " + frameHeight);
		}

		int numFrames = data.length / frameLen;
		if(props.get("tile.0") != null) {
			for(int durationDefStr = 0; props.get("tile." + durationDefStr) != null; ++durationDefStr) {
				numFrames = durationDefStr + 1;
			}
		}

		String string17 = (String)props.get("duration");
		int durationDef = Config.parseInt(string17, durDef);
		this.frames = new CustomAnimationFrame[numFrames];

		for(int i = 0; i < this.frames.length; ++i) {
			String indexStr = (String)props.get("tile." + i);
			int index = Config.parseInt(indexStr, i);
			String durationStr = (String)props.get("duration." + i);
			int duration = Config.parseInt(durationStr, durationDef);
			CustomAnimationFrame frm = new CustomAnimationFrame(index, duration);
			this.frames[i] = frm;
		}

	}

	public boolean nextFrame() {
		if(this.frames.length <= 0) {
			return false;
		} else {
			if(this.activeFrame >= this.frames.length) {
				this.activeFrame = 0;
			}

			CustomAnimationFrame frame = this.frames[this.activeFrame];
			++frame.counter;
			if(frame.counter < frame.duration) {
				return false;
			} else {
				frame.counter = 0;
				++this.activeFrame;
				if(this.activeFrame >= this.frames.length) {
					this.activeFrame = 0;
				}

				return true;
			}
		}
	}

	public int getActiveFrameIndex() {
		if(this.frames.length <= 0) {
			return 0;
		} else {
			if(this.activeFrame >= this.frames.length) {
				this.activeFrame = 0;
			}

			CustomAnimationFrame frame = this.frames[this.activeFrame];
			return frame.index;
		}
	}

	public int getFrameCount() {
		return this.frames.length;
	}

	public boolean updateCustomTexture(ByteBuffer imgData, boolean animated, boolean dynamicTexturesUpdated, StringBuffer dataIdBuf) {
		if(this.imageBytes == null) {
			return false;
		} else if(!animated && dynamicTexturesUpdated) {
			return true;
		} else if(!this.nextFrame()) {
			return true;
		} else {
			int imgLen = this.frameWidth * this.frameHeight * 4;
			if(this.imageBytes.length < imgLen) {
				return false;
			} else {
				int imgCount = this.getFrameCount();
				int imgNum = this.getActiveFrameIndex();
				int offset = 0;
				if(animated) {
					offset = imgLen * imgNum;
				}

				imgData.clear();
				imgData.put(this.imageBytes, offset, imgLen);
				imgData.position(0).limit(imgLen);
				dataIdBuf.append(this.imagePath);
				dataIdBuf.append(":");
				dataIdBuf.append(imgNum);
				return true;
			}
		}
	}
}
