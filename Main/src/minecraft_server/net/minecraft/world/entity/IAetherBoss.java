package net.minecraft.world.entity;

public interface IAetherBoss {
	int getBossHP();

	int getBossMaxHP();

	boolean isCurrentBoss();

	int getBossEntityID();

	String getBossTitle();
}
