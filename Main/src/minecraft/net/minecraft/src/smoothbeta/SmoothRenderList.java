package net.minecraft.src.smoothbeta;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.RenderGlobal;
import net.minecraft.src.RenderList;
import net.minecraft.src.smoothbeta.gl.GlUniform;
import net.minecraft.src.smoothbeta.gl.VertexBuffer;

public class SmoothRenderList extends RenderList {
    private final RenderGlobal renderGlobal;
    private final List<VertexBuffer> buffers = new ArrayList<>();

    public SmoothRenderList(RenderGlobal renderGlobal) {
        this.intBuffer = IntBuffer.allocate(0);
        this.renderGlobal = renderGlobal;
    }

    @Override
    public void updatePosition(int x, int y, int z, double playerX, double playerY, double playerZ) {
        super.updatePosition(x, y, z, playerX, playerY, playerZ);
        buffers.clear();
    }

    @Override
    public void addCallListToIntBuffer(int pass) {
        throw new UnsupportedOperationException("Call lists can't be added to VBO regions!");
    }

    public void addBuffer(VertexBuffer buffer) {
        buffers.add(buffer);
    }

    @Override
    public void flip() {
        if (!this.positionUpdated || buffers.isEmpty()) return;

        Shader shader = Shaders.getTerrainShader();
        GlUniform chunkOffset = shader.chunkOffset;
        // Fix this being float-casted...
        chunkOffset.set((float)this.posXMinus - this.dXf, (float)this.posYMinus - this.dYf, (float)this.posZMinus - this.dZf);
		chunkOffset.upload();
        for (VertexBuffer vertexBuffer : buffers) vertexBuffer.uploadToPool();
        renderGlobal.getTerrainVboPool().drawAll();
        chunkOffset.set(0.0F, 0.0F, 0.0F);
    }
}
