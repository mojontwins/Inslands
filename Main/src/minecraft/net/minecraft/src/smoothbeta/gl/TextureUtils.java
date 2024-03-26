package net.minecraft.src.smoothbeta.gl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import net.minecraft.src.GLAllocation;

public class TextureUtils {
    private static final int DEFAULT_IMAGE_BUFFER_SIZE = 8192;

    public static ByteBuffer readResource(InputStream inputStream) throws IOException {
        ByteBuffer byteBuffer;
        if (inputStream instanceof FileInputStream) {
            FileInputStream fileInputStream = (FileInputStream) inputStream;
            FileChannel fileChannel = fileInputStream.getChannel();
            byteBuffer = GLAllocation.createDirectByteBuffer((int)fileChannel.size() + 1);
            while (true) {
                if (fileChannel.read(byteBuffer) == -1) break;
            }
        } else {
            byteBuffer = GLAllocation.createDirectByteBuffer(DEFAULT_IMAGE_BUFFER_SIZE);
            ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
            while (readableByteChannel.read(byteBuffer) != -1) {
                if (byteBuffer.remaining() != 0) continue;
                byteBuffer = GLAllocation.createDirectByteBuffer(byteBuffer.capacity() * 2);
            }
        }
        return byteBuffer;
    }

    @SuppressWarnings("deprecation")
	public static String readResourceAsString(InputStream inputStream) {
        ByteBuffer byteBuffer;
        try {
            byteBuffer = readResource(inputStream);
            int i = byteBuffer.position();
            byteBuffer.rewind();
            byte[] ascii = new byte[byteBuffer.capacity()];
            byteBuffer.get(ascii);
            byteBuffer.rewind();
            //noinspection deprecation
            return new String(ascii, 0, 0, i);
        } catch (IOException ignored) {
            return null;
        }
    }
}
