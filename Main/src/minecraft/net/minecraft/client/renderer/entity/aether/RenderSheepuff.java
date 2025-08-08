package net.minecraft.client.renderer.entity.aether;

import org.lwjgl.opengl.GL11;

import com.misc.aether.EntitySheepuff;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntitySheep;

public class RenderSheepuff extends RenderLiving {
	private ModelBase wool;
	private ModelBase puffed;

	public RenderSheepuff(ModelBase modelbase, ModelBase modelbase1, ModelBase modelbase2, float f) {
		super(modelbase1, f);
		this.setRenderPassModel(modelbase);
		this.wool = modelbase;
		this.puffed = modelbase2;
	}

	protected boolean setWoolColorAndRender(EntitySheepuff entitysheep, int i, float f) {
		if(i == 0 && !entitysheep.getSheared()) {
			if(entitysheep.getPuffed()) {
				this.setRenderPassModel(this.puffed);
				this.loadTexture("/mob/sheepuff_fur.png");
			} else {
				this.setRenderPassModel(this.wool);
				this.loadTexture("/mob/sheepuff_fur.png");
			}

			float f1 = entitysheep.getEntityBrightness(f);
			int j = entitysheep.getFleeceColor();
			GL11.glColor3f(f1 * EntitySheep.fleeceColorTable[j][0], f1 * EntitySheep.fleeceColorTable[j][1], f1 * EntitySheep.fleeceColorTable[j][2]);
			return true;
		} else {
			return false;
		}
	}

	protected boolean shouldRenderPass(EntityLiving entityliving, int i, float f) {
		return this.setWoolColorAndRender((EntitySheepuff)entityliving, i, f);
	}
}
