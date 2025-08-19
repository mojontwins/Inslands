package net.minecraft.client.sound;

import java.io.File;

import net.minecraft.client.GameSettings;
import net.minecraft.game.entity.EntityLiving;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

import util.MathHelper;

public final class SoundManager {
	private SoundSystem sndSystem;
	private SoundPool soundPoolSounds = new SoundPool();
	private SoundPool soundPoolMusic = new SoundPool();
	private int playedSoundsCount = 0;
	private GameSettings options;
	private boolean loaded = false;

	public final void loadSoundSettings(GameSettings gameSettings1) {
		this.options = gameSettings1;
		if(!this.loaded && (gameSettings1.sound || gameSettings1.music)) {
			this.tryToSetLibraryAndCodecs();
		}

	}

	private void tryToSetLibraryAndCodecs() {
		try {
			boolean z1 = this.options.sound;
			boolean z2 = this.options.music;
			this.options.sound = false;
			this.options.music = false;
			this.options.saveOptions();
			SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
			SoundSystemConfig.setCodec("wav", CodecWav.class);
			this.sndSystem = new SoundSystem();
			this.options.sound = z1;
			this.options.music = z2;
			this.options.saveOptions();
		} catch (Throwable throwable3) {
			System.err.println("error linking with the LibraryJavaSound plug-in");
		}

		this.loaded = true;
	}

	public final void onSoundOptionsChanged() {
		if(!this.loaded && (this.options.sound || this.options.music)) {
			this.tryToSetLibraryAndCodecs();
		}

		if(!this.options.music) {
			this.sndSystem.stop("BgMusic");
		}

	}

	public final void closeMinecraft() {
		if(this.loaded) {
			this.sndSystem.cleanup();
		}

	}

	public final void addSound(String string1, File file2) {
		this.soundPoolSounds.addSound(string1, file2);
	}

	public final void addMusic(String string1, File file2) {
		this.soundPoolMusic.addSound(string1, file2);
	}

	public final void setListener(EntityLiving entityLiving1, float f2) {
		if(this.loaded && this.options.sound) {
			if(entityLiving1 != null) {
				float f3 = entityLiving1.prevRotationPitch + (entityLiving1.rotationPitch - entityLiving1.prevRotationPitch) * f2;
				float f4 = entityLiving1.prevRotationYaw + (entityLiving1.rotationYaw - entityLiving1.prevRotationYaw) * f2;
				double d5 = entityLiving1.prevPosX + (entityLiving1.posX - entityLiving1.prevPosX) * (double)f2;
				double d7 = entityLiving1.prevPosY + (entityLiving1.posY - entityLiving1.prevPosY) * (double)f2;
				double d9 = entityLiving1.prevPosZ + (entityLiving1.posZ - entityLiving1.prevPosZ) * (double)f2;
				float f13 = MathHelper.cos(-f4 * 0.017453292F - (float)Math.PI);
				f2 = MathHelper.sin(-f4 * 0.017453292F - (float)Math.PI);
				f4 = MathHelper.cos(-f3 * 0.017453292F);
				f3 = MathHelper.sin(-f3 * 0.017453292F);
				float f11 = -f2 * f4;
				float f12 = -f13 * f4;
				f2 = -f2 * f3;
				f13 = -f13 * f3;
				this.sndSystem.setListenerPosition((float)d5, (float)d7, (float)d9);
				this.sndSystem.setListenerOrientation(f11, f3, f12, f2, f4, f13);
			}
		}
	}

	public final void playSound(String string1, float f2, float f3, float f4, float f5, float f6) {
		if(this.loaded && this.options.sound) {
			SoundPoolEntry soundPoolEntry9;
			if((soundPoolEntry9 = this.soundPoolSounds.getRandomSoundFromSoundPool(string1)) != null && f5 > 0.0F) {
				this.playedSoundsCount = (this.playedSoundsCount + 1) % 256;
				String string7 = "sound_" + this.playedSoundsCount;
				float f8 = 16.0F;
				if(f5 > 1.0F) {
					f8 = 16.0F * f5;
				}

				this.sndSystem.newSource(f5 > 1.0F, string7, soundPoolEntry9.soundUrl, soundPoolEntry9.soundName, false, f2, f3, f4, 2, f8);
				this.sndSystem.setPitch(string7, f6);
				if(f5 > 1.0F) {
					f5 = 1.0F;
				}

				this.sndSystem.setVolume(string7, f5);
				this.sndSystem.play(string7);
			}

		}
	}

	public final void playSoundFX(String string1, float f2, float f3) {
		if(this.loaded && this.options.sound) {
			SoundPoolEntry soundPoolEntry4;
			if((soundPoolEntry4 = this.soundPoolSounds.getRandomSoundFromSoundPool(string1)) != null) {
				this.playedSoundsCount = (this.playedSoundsCount + 1) % 256;
				String string5 = "sound_" + this.playedSoundsCount;
				this.sndSystem.newSource(false, string5, soundPoolEntry4.soundUrl, soundPoolEntry4.soundName, false, 0.0F, 0.0F, 0.0F, 0, 0.0F);
				this.sndSystem.setPitch(string5, 1.0F);
				this.sndSystem.setVolume(string5, 0.25F);
				this.sndSystem.play(string5);
			}

		}
	}
}