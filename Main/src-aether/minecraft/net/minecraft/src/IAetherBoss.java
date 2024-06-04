package net.minecraft.src;

public interface IAetherBoss {
	int getBossHP();

	int getBossMaxHP();

	boolean isCurrentBoss();

	int getBossEntityID();

	String getBossTitle();
}
