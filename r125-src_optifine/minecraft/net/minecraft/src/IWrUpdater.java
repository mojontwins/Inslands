package net.minecraft.src;

import java.util.List;

public interface IWrUpdater {
	void initialize();

	WorldRenderer makeWorldRenderer(World world1, List list2, int i3, int i4, int i5, int i6);

	void preRender(RenderGlobal renderGlobal1, EntityLiving entityLiving2);

	void postRender();

	boolean updateRenderers(RenderGlobal renderGlobal1, EntityLiving entityLiving2, boolean z3);

	void resumeBackgroundUpdates();

	void pauseBackgroundUpdates();

	void finishCurrentUpdate();

	void terminate();
}
