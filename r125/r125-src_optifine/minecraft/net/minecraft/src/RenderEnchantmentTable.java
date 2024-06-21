package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderEnchantmentTable extends TileEntitySpecialRenderer {
	private ModelBook field_40450_a = new ModelBook();

	public void func_40449_a(TileEntityEnchantmentTable tileEntityEnchantmentTable1, double d2, double d4, double d6, float f8) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d2 + 0.5F, (float)d4 + 0.75F, (float)d6 + 0.5F);
		float f9 = (float)tileEntityEnchantmentTable1.tickCount + f8;
		GL11.glTranslatef(0.0F, 0.1F + MathHelper.sin(f9 * 0.1F) * 0.01F, 0.0F);

		float f10;
		for(f10 = tileEntityEnchantmentTable1.bookRotation2 - tileEntityEnchantmentTable1.bookRotationPrev; f10 >= (float)Math.PI; f10 -= 6.2831855F) {
		}

		while(f10 < -3.1415927F) {
			f10 += 6.2831855F;
		}

		float f11 = tileEntityEnchantmentTable1.bookRotationPrev + f10 * f8;
		GL11.glRotatef(-f11 * 180.0F / (float)Math.PI, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(80.0F, 0.0F, 0.0F, 1.0F);
		this.bindTextureByName("/item/book.png");
		float f12 = tileEntityEnchantmentTable1.pageFlipPrev + (tileEntityEnchantmentTable1.pageFlip - tileEntityEnchantmentTable1.pageFlipPrev) * f8 + 0.25F;
		float f13 = tileEntityEnchantmentTable1.pageFlipPrev + (tileEntityEnchantmentTable1.pageFlip - tileEntityEnchantmentTable1.pageFlipPrev) * f8 + 0.75F;
		f12 = (f12 - (float)MathHelper.func_40346_b((double)f12)) * 1.6F - 0.3F;
		f13 = (f13 - (float)MathHelper.func_40346_b((double)f13)) * 1.6F - 0.3F;
		if(f12 < 0.0F) {
			f12 = 0.0F;
		}

		if(f13 < 0.0F) {
			f13 = 0.0F;
		}

		if(f12 > 1.0F) {
			f12 = 1.0F;
		}

		if(f13 > 1.0F) {
			f13 = 1.0F;
		}

		float f14 = tileEntityEnchantmentTable1.bookSpreadPrev + (tileEntityEnchantmentTable1.bookSpread - tileEntityEnchantmentTable1.bookSpreadPrev) * f8;
		this.field_40450_a.render((Entity)null, f9, f12, f13, f14, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}

	public void renderTileEntityAt(TileEntity tileEntity1, double d2, double d4, double d6, float f8) {
		this.func_40449_a((TileEntityEnchantmentTable)tileEntity1, d2, d4, d6, f8);
	}
}
