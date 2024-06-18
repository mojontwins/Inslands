package net.minecraft.src;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.Pbuffer;

public class WrUpdateThread extends Thread {
	private Pbuffer pbuffer = null;
	private Object lock = new Object();
	private List updateList = new LinkedList();
	private List updatedList = new LinkedList();
	private int updateCount = 0;
	private Tessellator mainTessellator = Tessellator.instance;
	private Tessellator threadTessellator = new Tessellator(2097152);
	private boolean working = false;
	private WorldRendererThreaded currentRenderer = null;
	private boolean canWork = false;
	private boolean canWorkToEndOfUpdate = false;
	private boolean terminated = false;
	private static final int MAX_UPDATE_CAPACITY = 10;

	public WrUpdateThread(Pbuffer pbuffer) {
		super("WrUpdateThread");
		this.pbuffer = pbuffer;
	}

	public void run() {
		try {
			this.pbuffer.makeCurrent();
		} catch (Exception exception8) {
			exception8.printStackTrace();
		}

		WrUpdateThread.ThreadUpdateListener updateListener = new WrUpdateThread.ThreadUpdateListener();

		while(!Thread.interrupted() && !this.terminated) {
			try {
				WorldRendererThreaded e = this.getRendererToUpdate();
				if(e == null) {
					return;
				}

				this.checkCanWork((IWrUpdateControl)null);

				try {
					Tessellator.instance = this.threadTessellator;
					e.updateRenderer(updateListener);
				} finally {
					Tessellator.instance = this.mainTessellator;
				}

				this.rendererUpdated(e);
			} catch (Exception exception9) {
				exception9.printStackTrace();
				if(this.currentRenderer != null) {
					this.currentRenderer.isUpdating = false;
					this.currentRenderer.needsUpdate = true;
				}

				this.currentRenderer = null;
				this.working = false;
			}
		}

	}

	public void addRendererToUpdate(WorldRenderer wr, boolean first) {
		Object object3 = this.lock;
		synchronized(this.lock) {
			if(wr.isUpdating) {
				throw new IllegalArgumentException("Renderer already updating");
			} else {
				if(first) {
					this.updateList.add(0, wr);
				} else {
					this.updateList.add(wr);
				}

				wr.isUpdating = true;
				this.lock.notifyAll();
			}
		}
	}

	private WorldRendererThreaded getRendererToUpdate() {
		Object object1 = this.lock;
		synchronized(this.lock) {
			while(this.updateList.size() <= 0) {
				try {
					this.lock.wait(2000L);
					if(this.terminated) {
						Object object10000 = null;
						return (WorldRendererThreaded)object10000;
					}
				} catch (InterruptedException interruptedException4) {
				}
			}

			this.currentRenderer = (WorldRendererThreaded)this.updateList.remove(0);
			this.lock.notifyAll();
			return this.currentRenderer;
		}
	}

	public boolean hasWorkToDo() {
		Object object1 = this.lock;
		synchronized(this.lock) {
			return this.updateList.size() > 0 ? true : (this.currentRenderer != null ? true : this.working);
		}
	}

	public int getUpdateCapacity() {
		Object object1 = this.lock;
		synchronized(this.lock) {
			return this.updateList.size() > 10 ? 0 : 10 - this.updateList.size();
		}
	}

	private void rendererUpdated(WorldRenderer wr) {
		Object object2 = this.lock;
		synchronized(this.lock) {
			this.updatedList.add(wr);
			++this.updateCount;
			this.currentRenderer = null;
			this.working = false;
			this.lock.notifyAll();
		}
	}

	private void finishUpdatedRenderers() {
		Object object1 = this.lock;
		synchronized(this.lock) {
			for(int i = 0; i < this.updatedList.size(); ++i) {
				WorldRendererThreaded wr = (WorldRendererThreaded)this.updatedList.get(i);
				wr.finishUpdate();
				wr.isUpdating = false;
			}

			this.updatedList.clear();
		}
	}

	public void pause() {
		Object object1 = this.lock;
		synchronized(this.lock) {
			this.canWork = false;
			this.canWorkToEndOfUpdate = false;
			this.lock.notifyAll();

			while(this.working) {
				try {
					this.lock.wait();
				} catch (InterruptedException interruptedException4) {
				}
			}

			this.finishUpdatedRenderers();
		}
	}

	public void unpause() {
		Object object1 = this.lock;
		synchronized(this.lock) {
			if(this.working) {
				Config.dbg("UpdateThread still working in unpause()!!!");
			}

			this.canWork = true;
			this.canWorkToEndOfUpdate = false;
			this.lock.notifyAll();
		}
	}

	public void unpauseToEndOfUpdate() {
		Object object1 = this.lock;
		synchronized(this.lock) {
			if(this.working) {
				Config.dbg("UpdateThread still working in unpause()!!!");
			}

			if(this.currentRenderer != null) {
				while(this.currentRenderer != null) {
					this.canWork = false;
					this.canWorkToEndOfUpdate = true;
					this.lock.notifyAll();

					try {
						this.lock.wait();
					} catch (InterruptedException interruptedException4) {
					}
				}

				this.pause();
			}
		}
	}

	private void checkCanWork(IWrUpdateControl uc) {
		Thread.yield();
		Object object2 = this.lock;
		synchronized(this.lock) {
			while(!this.canWork && (!this.canWorkToEndOfUpdate || this.currentRenderer == null)) {
				if(uc != null) {
					uc.pause();
				}

				this.working = false;
				this.lock.notifyAll();

				try {
					this.lock.wait();
				} catch (InterruptedException interruptedException5) {
				}
			}

			this.working = true;
			if(uc != null) {
				uc.resume();
			}

			this.lock.notifyAll();
		}
	}

	public void clearAllUpdates() {
		Object object1 = this.lock;
		synchronized(this.lock) {
			this.unpauseToEndOfUpdate();
			this.updateList.clear();
			this.lock.notifyAll();
		}
	}

	public int getPendingUpdatesCount() {
		Object object1 = this.lock;
		synchronized(this.lock) {
			int count = this.updateList.size();
			if(this.currentRenderer != null) {
				++count;
			}

			return count;
		}
	}

	public int resetUpdateCount() {
		Object object1 = this.lock;
		synchronized(this.lock) {
			int count = this.updateCount;
			this.updateCount = 0;
			return count;
		}
	}

	public void terminate() {
		this.terminated = true;
	}

	static class SyntheticClass_1 {
	}

	private class ThreadUpdateListener implements IWrUpdateListener {
		private WrUpdateThread.ThreadUpdateControl tuc;

		private ThreadUpdateListener() {
			this.tuc = WrUpdateThread.this.new ThreadUpdateControl();
		}

		public void updating(IWrUpdateControl uc) {
			this.tuc.setUpdateControl(uc);
			WrUpdateThread.this.checkCanWork(this.tuc);
		}

		ThreadUpdateListener(WrUpdateThread.SyntheticClass_1 x1) {
			this();
		}
	}

	private class ThreadUpdateControl implements IWrUpdateControl {
		private IWrUpdateControl updateControl;
		private boolean paused;

		private ThreadUpdateControl() {
			this.updateControl = null;
			this.paused = false;
		}

		public void pause() {
			if(!this.paused) {
				this.paused = true;
				this.updateControl.pause();
				Tessellator.instance = WrUpdateThread.this.mainTessellator;
			}

		}

		public void resume() {
			if(this.paused) {
				this.paused = false;
				Tessellator.instance = WrUpdateThread.this.threadTessellator;
				this.updateControl.resume();
			}

		}

		public void setUpdateControl(IWrUpdateControl updateControl) {
			this.updateControl = updateControl;
		}

		ThreadUpdateControl(WrUpdateThread.SyntheticClass_1 x1) {
			this();
		}
	}
}
