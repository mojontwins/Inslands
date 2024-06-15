package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class PlayerControllerSP extends PlayerController {
	private int field_1074_c = -1;
	private int field_1073_d = -1;
	private int field_1072_e = -1;
	private float curBlockDamage = 0.0F;
	private float prevBlockDamage = 0.0F;
	private float field_1069_h = 0.0F;
	private int blockHitWait = 0;

	public PlayerControllerSP(Minecraft paramMinecraft) {
		super(paramMinecraft);
	}

	public void flipPlayer(EntityPlayer paramgs) {
		paramgs.rotationYaw = -180.0F;
	}

	public boolean sendBlockRemoved(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
		int j = this.mc.theWorld.getBlockId(paramInt1, paramInt2, paramInt3);
		int k = this.mc.theWorld.getBlockMetadata(paramInt1, paramInt2, paramInt3);
		boolean bool1 = super.sendBlockRemoved(paramInt1, paramInt2, paramInt3, paramInt4);
		ItemStack localiz = this.mc.thePlayer.getCurrentEquippedItem();
		boolean bool2 = this.mc.thePlayer.canHarvestBlock(Block.blocksList[j]);
		if(localiz != null) {
			localiz.onDestroyBlock(j, paramInt1, paramInt2, paramInt3, this.mc.thePlayer);
			if(localiz.stackSize == 0) {
				localiz.func_1097_a(this.mc.thePlayer);
				this.mc.thePlayer.destroyCurrentEquippedItem();
			}
		}

		if(bool1 && bool2) {
			if(SAPI.interceptHarvest(this.mc.theWorld, this.mc.thePlayer, new Loc(paramInt1, paramInt2, paramInt3), j, k)) {
				return bool1;
			}

			Block.blocksList[j].harvestBlock(this.mc.theWorld, this.mc.thePlayer, paramInt1, paramInt2, paramInt3, k);
		}

		return bool1;
	}

	public void clickBlock(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
		this.mc.theWorld.onBlockHit(this.mc.thePlayer, paramInt1, paramInt2, paramInt3, paramInt4);
		int j = this.mc.theWorld.getBlockId(paramInt1, paramInt2, paramInt3);
		if(j > 0 && this.curBlockDamage == 0.0F) {
			Block.blocksList[j].onBlockClicked(this.mc.theWorld, paramInt1, paramInt2, paramInt3, this.mc.thePlayer);
		}

		if(j > 0 && Block.blocksList[j].blockStrength(this.mc.thePlayer) >= 1.0F) {
			this.sendBlockRemoved(paramInt1, paramInt2, paramInt3, paramInt4);
		}

	}

	public void resetBlockRemoving() {
		this.curBlockDamage = 0.0F;
		this.blockHitWait = 0;
	}

	public void sendBlockRemoving(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
		if(this.blockHitWait > 0) {
			--this.blockHitWait;
		} else {
			if(paramInt1 == this.field_1074_c && paramInt2 == this.field_1073_d && paramInt3 == this.field_1072_e) {
				int j = this.mc.theWorld.getBlockId(paramInt1, paramInt2, paramInt3);
				if(j == 0) {
					return;
				}

				Block localuu = Block.blocksList[j];
				this.curBlockDamage += localuu.blockStrength(this.mc.thePlayer);
				if(this.field_1069_h % 4.0F == 0.0F && localuu != null) {
					this.mc.sndManager.playSound(localuu.stepSound.func_1145_d(), (float)paramInt1 + 0.5F, (float)paramInt2 + 0.5F, (float)paramInt3 + 0.5F, (localuu.stepSound.getVolume() + 1.0F) / 8.0F, localuu.stepSound.getPitch() * 0.5F);
				}

				++this.field_1069_h;
				if(this.curBlockDamage >= 1.0F) {
					this.sendBlockRemoved(paramInt1, paramInt2, paramInt3, paramInt4);
					this.curBlockDamage = 0.0F;
					this.prevBlockDamage = 0.0F;
					this.field_1069_h = 0.0F;
					this.blockHitWait = 5;
				}
			} else {
				this.curBlockDamage = 0.0F;
				this.prevBlockDamage = 0.0F;
				this.field_1069_h = 0.0F;
				this.field_1074_c = paramInt1;
				this.field_1073_d = paramInt2;
				this.field_1072_e = paramInt3;
			}

		}
	}

	public void setPartialTime(float paramFloat) {
		if(this.curBlockDamage <= 0.0F) {
			this.mc.ingameGUI.damageGuiPartialTime = 0.0F;
			this.mc.renderGlobal.damagePartialTime = 0.0F;
		} else {
			float f1 = this.prevBlockDamage + (this.curBlockDamage - this.prevBlockDamage) * paramFloat;
			this.mc.ingameGUI.damageGuiPartialTime = f1;
			this.mc.renderGlobal.damagePartialTime = f1;
		}

	}

	public float getBlockReachDistance() {
		return SAPI.reachGet();
	}

	public void func_717_a(World paramfd) {
		super.func_717_a(paramfd);
	}

	public void updateController() {
		this.prevBlockDamage = this.curBlockDamage;
		this.mc.sndManager.playRandomMusicIfReady();
	}
}
