package net.minecraft.src.smoothbeta.gl;

import java.nio.ByteBuffer;

import net.minecraft.src.smoothbeta.VboPool;
import net.minecraft.src.smoothbeta.VertexFormat;

public class VertexBuffer {
    private final VboPool pool;
    private final VboPool.Pos poolPos = new VboPool.Pos();

    public VertexBuffer(VboPool pool) {
        this.pool = pool;
    }

    public void upload(ByteBuffer buffer) {
        pool.bufferData(buffer, poolPos);
    }

    public void uploadToPool() {
        pool.upload(VertexFormat.DrawMode.QUADS, poolPos);
    }
}