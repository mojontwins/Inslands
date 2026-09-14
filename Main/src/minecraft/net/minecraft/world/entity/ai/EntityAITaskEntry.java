package net.minecraft.world.entity.ai;

class EntityAITaskEntry {
	public EntityAIBase action;
	public int priority;
	final EntityAITasks tasks;

	public EntityAITaskEntry(EntityAITasks tasks, int priority, EntityAIBase action) {
		this.tasks = tasks;
		this.priority = priority;
		this.action = action;
	}
}
