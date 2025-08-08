/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelBiped
 *  net.minecraft.client.model.ModelRenderer
 */
package net.minecraft.client.model.gwdm;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelGhoul
extends ModelBiped {
    public ModelGhoul() {
        super(0.0f);
        this.bipedHead = null;
        this.bipedHead = new ModelRenderer(0, 0);
        this.bipedHead.addBox(-4.0f, -4.0f, -7.0f, 8, 8, 8, 0.0f);
        this.bipedHead.setRotationPoint(0.0f, 0.0f, 0.0f);
    }
}

