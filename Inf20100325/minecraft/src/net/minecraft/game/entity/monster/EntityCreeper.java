package net.minecraft.game.entity.monster;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.item.Item;
import net.minecraft.game.world.World;

public class EntityCreeper extends EntityMob {
	private int timeSinceIgnited;
	private int lastActiveTime;
	private int fuseDuration;
	private int creeperState;

	private EntityCreeper(World var1) {
		super(var1);
	}

	protected final void updateEntityActionState() {
		this.lastActiveTime = this.timeSinceIgnited;
		if(this.timeSinceIgnited > 0 && this.creeperState < 0) {
			--this.timeSinceIgnited;
		}

		if(this.creeperState >= 0) {
			this.creeperState = 2;
		}

		super.updateEntityActionState();
		if(this.creeperState != 1) {
			this.creeperState = -1;
		}

	}

	protected final void attackEntity(Entity var1, float var2) {
		if(this.creeperState <= 0 && var2 < 3.0F || this.creeperState > 0 && var2 < 7.0F) {
			if(this.timeSinceIgnited == 0) {
				this.worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
			}

			this.creeperState = 1;
			++this.timeSinceIgnited;
			if(this.timeSinceIgnited == this.fuseDuration) {
				this.setEntityDead();
			}

			this.hasAttacked = true;
		}

	}

	public final float c(float var1) {
		return ((float)this.lastActiveTime + (float)(this.timeSinceIgnited - this.lastActiveTime) * var1) / (float)(this.fuseDuration - 2);
	}

	protected final int getDropItemId() {
		return Item.gunpowder.shiftedIndex;
	}
}
