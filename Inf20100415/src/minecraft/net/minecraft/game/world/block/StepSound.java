package net.minecraft.game.world.block;

public class StepSound {
	private String stepSoundName;
	public final float stepSoundVolume;
	public final float stepSoundPitch;

	public StepSound(String string1, float f2, float f3) {
		this.stepSoundName = string1;
		this.stepSoundVolume = f2;
		this.stepSoundPitch = f3;
	}

	public String getBreakSound() {
		return "step." + this.stepSoundName;
	}

	public final String getStepSound() {
		return "step." + this.stepSoundName;
	}
}