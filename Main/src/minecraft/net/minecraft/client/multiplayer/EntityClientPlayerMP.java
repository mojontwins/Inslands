package net.minecraft.client.multiplayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.player.EntityPlayerSP;
import net.minecraft.network.packet.Packet101CloseWindow;
import net.minecraft.network.packet.Packet10Flying;
import net.minecraft.network.packet.Packet11PlayerPosition;
import net.minecraft.network.packet.Packet12PlayerLook;
import net.minecraft.network.packet.Packet13PlayerLookMove;
import net.minecraft.network.packet.Packet14BlockDig;
import net.minecraft.network.packet.Packet18Animation;
import net.minecraft.network.packet.Packet19EntityAction;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.src.MathHelper;
import net.minecraft.src.StatBase;
import net.minecraft.src.World;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.item.ItemStack;

public class EntityClientPlayerMP extends EntityPlayerSP {
	public NetClientHandler sendQueue;
	private int inventoryUpdateTickCounter = 0;
	private boolean updatingHealth = false;
	private double oldPosX;
	private double oldBasePos;
	private double oldPosY;
	private double oldPosZ;
	private float oldRotationYaw;
	private float oldRotationPitch;
	private boolean wasOnGround = false;
	private boolean wasSneaking = false;
	private int ticksIdle = 0;

	public EntityClientPlayerMP(Minecraft minecraft1, World world2, User session3, NetClientHandler netClientHandler4) {
		super(minecraft1, world2, session3, 0);
		this.sendQueue = netClientHandler4;
	}

	public boolean attackEntityFrom(Entity entity1, int i2) {
		return false;
	}

	public void heal(int i1) {
	}

	public void onUpdate() {
		if(this.worldObj.blockExists(MathHelper.floor_double(this.posX), 64, MathHelper.floor_double(this.posZ))) {
			super.onUpdate();
			this.func_4056_N();
		}
	}

	public void func_4056_N() {
		if(this.inventoryUpdateTickCounter++ == 20) {
			this.sendInventoryChanged();
			this.inventoryUpdateTickCounter = 0;
		}

		boolean z1 = this.isSneaking();
		if(z1 != this.wasSneaking) {
			if(z1) {
				this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 1));
			} else {
				this.sendQueue.addToSendQueue(new Packet19EntityAction(this, 2));
			}

			this.wasSneaking = z1;
		}

		double d2 = this.posX - this.oldPosX;
		double d4 = this.boundingBox.minY - this.oldBasePos;
		double d6 = this.posY - this.oldPosY;
		double d8 = this.posZ - this.oldPosZ;
		double d10 = (double)(this.rotationYaw - this.oldRotationYaw);
		double d12 = (double)(this.rotationPitch - this.oldRotationPitch);
		boolean z14 = d4 != 0.0D || d6 != 0.0D || d2 != 0.0D || d8 != 0.0D;
		boolean z15 = d10 != 0.0D || d12 != 0.0D;
		if(this.ridingEntity != null) {
			if(z15) {
				this.sendQueue.addToSendQueue(new Packet11PlayerPosition(this.motionX, -999.0D, -999.0D, this.motionZ, this.onGround));
			} else {
				this.sendQueue.addToSendQueue(new Packet13PlayerLookMove(this.motionX, -999.0D, -999.0D, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
			}

			z14 = false;
		} else if(z14 && z15) {
			this.sendQueue.addToSendQueue(new Packet13PlayerLookMove(this.posX, this.boundingBox.minY, this.posY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround));
			this.ticksIdle = 0;
		} else if(z14) {
			this.sendQueue.addToSendQueue(new Packet11PlayerPosition(this.posX, this.boundingBox.minY, this.posY, this.posZ, this.onGround));
			this.ticksIdle = 0;
		} else if(z15) {
			this.sendQueue.addToSendQueue(new Packet12PlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
			this.ticksIdle = 0;
		} else {
			this.sendQueue.addToSendQueue(new Packet10Flying(this.onGround));
			if(this.wasOnGround == this.onGround && this.ticksIdle <= 200) {
				++this.ticksIdle;
			} else {
				this.ticksIdle = 0;
			}
		}

		this.wasOnGround = this.onGround;
		if(z14) {
			this.oldPosX = this.posX;
			this.oldBasePos = this.boundingBox.minY;
			this.oldPosY = this.posY;
			this.oldPosZ = this.posZ;
		}

		if(z15) {
			this.oldRotationYaw = this.rotationYaw;
			this.oldRotationPitch = this.rotationPitch;
		}

	}

	public EntityItem dropCurrentItem() {
		this.sendQueue.addToSendQueue(new Packet14BlockDig(4, 0, 0, 0, 0, null, 0, 0, 0));
		return null;
	}

	private void sendInventoryChanged() {
	}

	protected void joinEntityItemWithWorld(EntityItem entityItem1) {
	}

	public void sendChatMessage(String string1) {
		this.sendQueue.addToSendQueue(new Packet3Chat(string1));
	}

	public void swingItem() {
		super.swingItem();
		this.sendQueue.addToSendQueue(new Packet18Animation(this, 1));
	}

	public void respawnPlayer() {
		this.sendInventoryChanged();
		this.sendQueue.addToSendQueue(new Packet9Respawn((byte)this.dimension));
	}

	protected void damageEntity(int i1) {
		this.health -= i1;
	}

	public void closeScreen() {
		this.sendQueue.addToSendQueue(new Packet101CloseWindow(this.craftingInventory.windowId));
		this.inventory.setItemStack((ItemStack)null);
		super.closeScreen();
	}

	public void setHealth(int i1) {
		if(this.updatingHealth) {
			super.setHealth(i1);
		} else {
			this.health = i1;
			this.updatingHealth = true;
		}

	}

	public void addStat(StatBase statBase1, int i2) {
		if(statBase1 != null) {
			if(statBase1.isIndependent) {
				super.addStat(statBase1, i2);
			}

		}
	}

	public void func_27027_b(StatBase statBase1, int i2) {
		if(statBase1 != null) {
			if(!statBase1.isIndependent) {
				super.addStat(statBase1, i2);
			}

		}
	}
}
