package net.minecraft.src.smoothbeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.src.smoothbeta.gl.Program;

public class Shaders {
    private static Shader terrainShader;

    private static class Application {
        Runnable clearCache;
        Supplier<Shader> shaderFactory;

        public Application(Runnable clearCache, Supplier<Shader> shaderFactory) {
            this.clearCache = clearCache;
            this.shaderFactory = shaderFactory;
        }
    }

    private Application loadShaders() {
        List<Program> list = new ArrayList<>();
        list.addAll(Program.Type.FRAGMENT.getProgramCache().values());
        list.addAll(Program.Type.VERTEX.getProgramCache().values());

        Supplier<Shader> shaderFactory = () -> {
            try {
                return new Shader("terrain", VertexFormats.POSITION_TEXTURE_COLOR_NORMAL);
            } catch (IOException e) {
                throw new RuntimeException("Could not reload terrain shader", e);
            }
        };

        return new Application(() -> list.forEach(Program::release), shaderFactory);
    }

    private static void applyShader(Application application) {
        application.clearCache.run();

        if (terrainShader != null) {
            terrainShader.close();
        }

        terrainShader = application.shaderFactory.get();
    }

    public static Shader getTerrainShader() {
        return terrainShader;
    }

    static {
        Shaders shaders = new Shaders();
        Application application = shaders.loadShaders();
        applyShader(application);
    }
}