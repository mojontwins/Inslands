/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.renderer.entity.RenderLiving
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.util.ResourceLocation
 */
package com.gw.dm;

import net.minecraft.src.ModelBase;
import net.minecraft.src.RenderLiving;

public class RenderGhoul
extends RenderLiving {
    protected ModelGhoul model;
    
    public RenderGhoul(ModelGhoul modelG, float f) {
        super((ModelBase)modelG, f);
        this.model = (ModelGhoul)this.mainModel;
    }

}

