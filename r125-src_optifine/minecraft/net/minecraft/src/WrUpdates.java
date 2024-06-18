package net.minecraft.src;

import java.util.List;

public class WrUpdates {
	private static IWrUpdater wrUpdater = null;

	public static void setWrUpdater(IWrUpdater updater) {
		if(wrUpdater != null) {
			wrUpdater.terminate();
		}

		wrUpdater = updater;
		if(wrUpdater != null) {
			wrUpdater.initialize();
		}

	}

	public static boolean hasWrUpdater() {
		return wrUpdater != null;
	}

	public static IWrUpdater getWrUpdater() {
		return wrUpdater;
	}

	public static WorldRenderer makeWorldRenderer(World worldObj, List tileEntities, int x, int y, int z, int glRenderListBase) {
		return wrUpdater == null ? new WorldRenderer(worldObj, tileEntities, x, y, z, glRenderListBase) : wrUpdater.makeWorldRenderer(worldObj, tileEntities, x, y, z, glRenderListBase);
	}

	public static boolean updateRenderers(RenderGlobal rg, EntityLiving entityliving, boolean flag) {
		return wrUpdater.updateRenderers(rg, entityliving, flag);
	}

	public static void resumeBackgroundUpdates() {
		if(wrUpdater != null) {
			wrUpdater.resumeBackgroundUpdates();
		}

	}

	public static void pauseBackgroundUpdates() {
		if(wrUpdater != null) {
			wrUpdater.pauseBackgroundUpdates();
		}

	}

	public static void finishCurrentUpdate() {
		if(wrUpdater != null) {
			wrUpdater.finishCurrentUpdate();
		}

	}

	public static void preRender(RenderGlobal rg, EntityLiving player) {
		if(wrUpdater != null) {
			wrUpdater.preRender(rg, player);
		}

	}

	public static void postRender() {
		if(wrUpdater != null) {
			wrUpdater.postRender();
		}

	}
}
