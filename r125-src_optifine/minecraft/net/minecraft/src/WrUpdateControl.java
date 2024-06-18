package net.minecraft.src;

public class WrUpdateControl implements IWrUpdateControl {
	private boolean hasForge = Reflector.hasClass(1);
	private int renderPass = 0;

	public void resume() {
		if(this.hasForge) {
			Reflector.callVoid(13, new Object[]{this.renderPass});
		}

	}

	public void pause() {
		if(this.hasForge) {
			Reflector.callVoid(14, new Object[]{this.renderPass});
		}

	}

	public void setRenderPass(int renderPass) {
		this.renderPass = renderPass;
	}
}
