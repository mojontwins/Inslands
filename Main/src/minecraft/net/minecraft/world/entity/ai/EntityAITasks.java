package net.minecraft.world.entity.ai;

import java.util.ArrayList;
import java.util.Iterator;

public class EntityAITasks {
	private ArrayList<EntityAITaskEntry> tasksToDo = new ArrayList<EntityAITaskEntry>();
	private ArrayList<EntityAITaskEntry> executingTasks = new ArrayList<EntityAITaskEntry>();

	public void addTask(int priority, EntityAIBase action) {
		this.tasksToDo.add(new EntityAITaskEntry(this, priority, action));
	}

	public void onUpdateTasks() {
		ArrayList<EntityAITaskEntry> toStart = new ArrayList<EntityAITaskEntry>();
		Iterator<EntityAITaskEntry> iter = this.tasksToDo.iterator();

		while (true) {
			EntityAITaskEntry entry;
			while (true) {
				if (!iter.hasNext()) {
					boolean debug = false;
					if (debug && toStart.size() > 0) {
						System.out.println("Starting: ");
					}

					Iterator<EntityAITaskEntry> it2;
					EntityAITaskEntry entry2;
					for (it2 = toStart.iterator(); it2.hasNext(); entry2.action.startExecuting()) {
						entry2 = it2.next();
						if (debug) {
							System.out.println(entry2.action.toString() + ", ");
						}
					}

					if (debug && this.executingTasks.size() > 0) {
						System.out.println("Running: ");
					}

					for (it2 = this.executingTasks.iterator(); it2.hasNext(); entry2.action.updateTask()) {
						entry2 = it2.next();
						if (debug) {
							System.out.println(entry2.action.toString());
						}
					}

					return;
				}

				entry = iter.next();
				boolean executing = this.executingTasks.contains(entry);
				if (!executing) {
					break;
				}

				if (!this.isSchedulable(entry) || !entry.action.continueExecuting()) {
					entry.action.resetTask();
					this.executingTasks.remove(entry);
					break;
				}
			}

			if (this.isSchedulable(entry) && entry.action.shouldExecute()) {
				toStart.add(entry);
				this.executingTasks.add(entry);
			}
		}
	}

	private boolean isSchedulable(EntityAITaskEntry entry) {
		Iterator<EntityAITaskEntry> iter = this.tasksToDo.iterator();

		while (iter.hasNext()) {
			EntityAITaskEntry other = iter.next();
			if (other != entry) {
				if (entry.priority >= other.priority) {
					if (this.executingTasks.contains(other) && !this.areTasksCompatible(entry, other)) {
						return false;
					}
				} else if (this.executingTasks.contains(other) && !other.action.isContinuous()) {
					return false;
				}
			}
		}

		return true;
	}

	private boolean areTasksCompatible(EntityAITaskEntry entry1, EntityAITaskEntry entry2) {
		return (entry1.action.getMutexBits() & entry2.action.getMutexBits()) == 0;
	}
}
