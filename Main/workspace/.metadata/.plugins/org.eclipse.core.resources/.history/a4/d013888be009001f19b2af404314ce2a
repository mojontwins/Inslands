package net.minecraft.src.smoothbeta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lwjgl.opengl.GL13;

import net.minecraft.src.J_JdomParser;
import net.minecraft.src.J_JsonNode;
import net.minecraft.src.smoothbeta.gl.GLImportProcessor;
import net.minecraft.src.smoothbeta.gl.GlBlendState;
import net.minecraft.src.smoothbeta.gl.GlProgramManager;
import net.minecraft.src.smoothbeta.gl.GlShader;
import net.minecraft.src.smoothbeta.gl.GlStateManager;
import net.minecraft.src.smoothbeta.gl.GlUniform;
import net.minecraft.src.smoothbeta.gl.Program;
import net.minecraft.src.smoothbeta.gl.ShaderParseException;

// DID : Remove GSON dependency, changed it to use built in J_JSon, somehow.

public class Shader implements GlShader, AutoCloseable {
	private static final String CORE_DIRECTORY = "/shaders/core/";
	private static final String INCLUDE_DIRECTORY = "/shaders/include/";
	private static int activeShaderId;
	private final Map<String, Object> samplers = new HashMap<String, Object>();
	private final List<String> samplerNames = new ArrayList<String>();
	private final List<Integer> loadedSamplerIds = new ArrayList<Integer>();
	private final List<GlUniform> uniforms = new ArrayList<GlUniform>();
	private final Map<String, GlUniform> loadedUniforms = new HashMap<String, GlUniform>();
	private final int programId;
	private final String name;
	private final GlBlendState blendState;
	private final Program vertexShader;
	private final Program fragmentShader;
	public final GlUniform
			modelViewMat,
			projectionMat,
			fogMode,
			chunkOffset;
	
	public static J_JdomParser jdomParser = new J_JdomParser();
	
	public static boolean debug = true;

	public Shader(String name, VertexFormat format) throws IOException {
		this.name = name;
		String shader = CORE_DIRECTORY + name + ".json";
		try (InputStream stream = Shader.class.getResourceAsStream(shader)) {
			
			J_JsonNode json = jdomParser.parseFromInputStream(stream);
			
			String vertex = json.getStringValue("vertex");			if(Shader.debug) System.out.println ("Vertex: " + vertex);	
			String fragment = json.getStringValue("fragment");		if(Shader.debug) System.out.println ("Fragment: " + fragment); 
			
			// Read samplers
			if(Shader.debug) System.out.println ("Samplers: ");
			
			List<J_JsonNode> samplers = json.getArrayNode("samplers"); 	 
			if (samplers != null) {
				int i = 0;
				for (J_JsonNode sampler : samplers) {
					try {
						this.readSampler(sampler);
					} catch (Exception exception) {
						ShaderParseException shaderParseException = ShaderParseException.wrap(exception);
						shaderParseException.addFaultyElement("samplers[" + i + "]");
						throw shaderParseException;
					}
					++ i;
				}
			}
			
			// Read attributes
			if(Shader.debug) System.out.println ("Attributes: ");
			
			List<String> attributeNames;
			List<Integer> loadedAttributeIds;
			
			List<J_JsonNode> attributes = json.getArrayNode("attributes");
			if (attributes != null) {
				int j = 0;
				loadedAttributeIds = new ArrayList<Integer>(attributes.size());
				attributeNames = new ArrayList<String>(attributes.size());
				
				for (J_JsonNode attribute : attributes) {
					try {
						if(Shader.debug) System.out.println ("    " + j + ": " + attribute.getText());
						attributeNames.add(attribute.getText());
					} catch (Exception exception2) {
						ShaderParseException shaderParseException2 = ShaderParseException.wrap(exception2);
						shaderParseException2.addFaultyElement("attributes[" + j + "]");
						throw shaderParseException2;
					}
					++j;
				}
			} else {
				loadedAttributeIds = null;
				attributeNames = null;
			}
			
			// Read uniforms
			if(Shader.debug) System.out.println ("Uniforms: ");
						
			List<J_JsonNode> uniforms = json.getArrayNode("uniforms");
			if (uniforms != null) {
				int k = 0;
				for (J_JsonNode uniform : uniforms) {
					try {
						this.addUniform(uniform);
					} catch (Exception exception3) {
						ShaderParseException shaderParseException3 = ShaderParseException.wrap(exception3);
						shaderParseException3.addFaultyElement("uniforms[" + k + "]");
						throw shaderParseException3;
					}
					++k;
				}
			}
			
			this.blendState = Shader.readBlendState(json);
			this.vertexShader = Shader.loadProgram(Program.Type.VERTEX, vertex);
			this.fragmentShader = Shader.loadProgram(Program.Type.FRAGMENT, fragment);
			this.programId = GlProgramManager.createProgram();
			if (attributeNames != null) {
				int k = 0;
				for (String attributeName : format.getAttributeNames()) {
					GlUniform.bindAttribLocation(this.programId, k, attributeName);
					loadedAttributeIds.add(k);
					++k;
				}
			}
			GlProgramManager.linkProgram(this);
			this.loadReferences();
		}
		catch (Exception exception4) {
			ShaderParseException shaderParseException4 = ShaderParseException.wrap(exception4);
			shaderParseException4.addFaultyFile(shader);
			throw shaderParseException4;
		}
		this.modelViewMat = this.getUniform("ModelViewMat");
		this.projectionMat = this.getUniform("ProjMat");
		this.fogMode = this.getUniform("FogMode");
		this.chunkOffset = this.getUniform("ChunkOffset");
	}

	public static String asString(InputStream inputStream) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			StringBuilder result = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line).append("\n");
			}
			return result.toString();
		}
	}

	private static Program loadProgram(Program.Type type, String name) throws IOException {
		Program program2;
		Program program = type.getProgramCache().get(name);
		if (program == null) {
			String string = CORE_DIRECTORY + name + type.getFileExtension();
			try (InputStream inputStream = Shader.class.getResourceAsStream(string)) {
				final String string2 = string;
				program2 = Program.createFromResource(type, name, inputStream, "Minecraft", new GLImportProcessor() {
					private final Set<String> visitedImports = new HashSet<>();

					@Override
					public String loadImport(boolean inline, String name) {
						String string = "";
						name = (inline ? string2 : Shader.INCLUDE_DIRECTORY) + name;
						if (!this.visitedImports.add(name)) return null;
						try {
							string = asString(Shader.class.getResourceAsStream(name));
						} catch (Throwable throwable) {
							throwable.printStackTrace();
						}
						return string;
					}
				});
			}
		} else program2 = program;
		return program2;
	}

	public static GlBlendState readBlendState(J_JsonNode json) {
		if(debug) System.out.println ("blend: ");
		
		if (json == null) {
			return new GlBlendState();
		} else {
			int i = 32774;
			int j = 1;
			int k = 0;
			int l = 1;
			int m = 0;
			boolean bl = true;
			boolean bl2 = false;
			
			String func = null;
			try {
				func = json.getStringValue("blend", "func");
				if(debug) System.out.println ("    func: " + func);
			} catch (Exception e) {}
			
			if (func != null && !"".equals(func)) {
				i = GlBlendState.getFuncFromString(func);
				if (i != 32774) bl = false;
			}

			String srcrgb = null;
			try {
				srcrgb = json.getStringValue("blend", "srcrgb");
				if(debug) System.out.println ("    srcrgb: " + srcrgb);
			} catch (Exception e) {}
			
			if (srcrgb != null && !"".equals(srcrgb)) {
				j = GlBlendState.getComponentFromString(srcrgb);
				if (j != 1) bl = false;
			}

			String dstrgb = null;
			try {
				dstrgb = json.getStringValue("blend", "dstrgb");
				if(debug) System.out.println ("    dstrgb: " + dstrgb);
			} catch (Exception e) {}
			
			if (srcrgb != null && !"".equals(dstrgb)) {
				k = GlBlendState.getComponentFromString(dstrgb);
				if (k != 0) bl = false;
			}

			String srcalpha = null;
			try {
				srcalpha = json.getStringValue("blend", "srcalpha");
				if(debug) System.out.println ("    srcalpha: " + srcalpha);
			} catch (Exception e) {}
			
			if (srcalpha != null && !"".equals(srcalpha)) {
				l = GlBlendState.getComponentFromString(srcalpha);
				if (l != 1) bl = false;

				bl2 = true;
			}

			String dstalpha = null;
			try {
				dstalpha = json.getStringValue("blend", "dstalpha");
				if(debug) System.out.println ("    dstalpha: " + dstalpha);
			} catch (Exception e) {}
			
			if (dstalpha != null && !"".equals(dstalpha)) {
				m = GlBlendState.getComponentFromString(dstalpha);
				if (m != 0) bl = false;

				bl2 = true;
			}

			if (bl) return new GlBlendState();
			else return bl2 ? new GlBlendState(j, k, l, m, i) : new GlBlendState(j, k, i);
		}
	}

	public void close() {

		for (GlUniform glUniform : this.uniforms) glUniform.close();

		GlProgramManager.deleteProgram(this);
	}

	public void unbind() {
		GlProgramManager.useProgram(0);
		activeShaderId = -1;
		int i = GlStateManager._getActiveTexture();

		for(int j = 0; j < this.loadedSamplerIds.size(); ++j)
			if (this.samplers.get(this.samplerNames.get(j)) != null) {
				GlStateManager._activeTexture(GL13.GL_TEXTURE0 + j);
				GlStateManager._bindTexture(0);
			}

		GlStateManager._activeTexture(i);
	}

	public void bind() {
		this.blendState.enable();
		if (this.programId != activeShaderId) {
			GlProgramManager.useProgram(this.programId);
			activeShaderId = this.programId;
		}

		int i = GlStateManager._getActiveTexture();

		for(int j = 0; j < this.loadedSamplerIds.size(); ++j) {
			String string = this.samplerNames.get(j);
			if (this.samplers.get(string) != null) {
				int k = GlUniform.getUniformLocation(this.programId, string);
				GlUniform.uniform1(k, j);
				GlStateManager._activeTexture(GL13.GL_TEXTURE0 + j);
				GlStateManager._enableTexture();
				Object object = this.samplers.get(string);
				int l = -1;
				//if (object instanceof AbstractTexture) l = ((AbstractTexture) object).getGlId();
				/*else*/
				if (object instanceof Integer) l = (Integer) object;

				if (l != -1) GlStateManager._bindTexture(l);
			}
		}

		GlStateManager._activeTexture(i);

		for (GlUniform glUniform : this.uniforms) glUniform.upload();

	}

	public GlUniform getUniform(String name) {
		return this.loadedUniforms.get(name);
	}

	private void loadReferences() {
		List<Integer> intList = new ArrayList<Integer>();

		int i;
		for(i = 0; i < this.samplerNames.size(); ++i) {
			String string = this.samplerNames.get(i);
			int j = GlUniform.getUniformLocation(this.programId, string);
			if (j == -1) {
				System.err.printf("Shader %s could not find sampler named %s in the specified shader program.%n", this.name, string);
				this.samplers.remove(string);
				intList.add(i);
			} else this.loadedSamplerIds.add(j);
		}

		for(i = intList.size() - 1; i >= 0; --i) {
			int k = intList.get(i);
			this.samplerNames.remove(k);
		}

		for (GlUniform glUniform : this.uniforms) {
			String string2 = glUniform.getName();
			int l = GlUniform.getUniformLocation(this.programId, string2);
			if (l == -1)
				System.err.printf("Shader %s could not find uniform named %s in the specified shader program.%n", this.name, string2);
			else {
				glUniform.setLocation(l);
				this.loadedUniforms.put(string2, glUniform);
			}
		}

	}

	private void readSampler(J_JsonNode json) {		
		String string = json.getStringValue("name");		

		String file = null;
		try {
			file = json.getStringValue("file");
		} catch (Exception e) {			
		}
		
		if (file == null || "".equals(file)) {
			this.samplers.put(string, null);
			this.samplerNames.add(string);
		} else this.samplerNames.add(string);
		
		if(Shader.debug) System.out.println ("    name: " + string + ", file: " + file);
	}

	public void addSampler(String name, Object sampler) {
		this.samplers.put(name, sampler);
	}

	private void addUniform(J_JsonNode json) throws ShaderParseException {
		String name = json.getStringValue("name");							if(Shader.debug) System.out.print("    Name: " + name);
		
		int i = GlUniform.getTypeIndex(json.getStringValue("type"));		if(Shader.debug) System.out.print(" Type: " + json.getStringValue("type") + " (" + i + ")");
		int j = Integer.parseInt(json.getNumberValue("count"));				if(Shader.debug) System.out.print(" Count: " + json.getNumberValue("count") + " (" + j + ")");
		
		float[] fs = new float[Math.max(j, 16)];
		
		List<J_JsonNode>values = json.getArrayNode("values");
		
		if (values.size() != j && values.size() > 1) {
			throw new ShaderParseException("Invalid amount of values specified (expected " + j + ", found " + values.size() + ")");
		} else {
			int k = 0;

			if(Shader.debug) System.out.print(" [");
			
			for(J_JsonNode value : values) {
				try {
					String valueText = value.getText();
					float valueFloat = Float.parseFloat(valueText);
					
					fs [k] = valueFloat;
					
					if(Shader.debug) System.out.print(k + ":" + valueFloat + " ");
				} catch (Exception e) {
					ShaderParseException shaderParseException = ShaderParseException.wrap(e);
					shaderParseException.addFaultyElement("values[" + k + "]");
					throw shaderParseException;
				}
				
				++k;
			}
			
			if(Shader.debug) System.out.println("]");
			
			if (j > 1 && values.size() == 1) while (k < j) {
				fs[k] = fs[0];
				++k;
			}

			int l = j > 1 && j <= 4 && i < 8 ? j - 1 : 0;
			GlUniform glUniform = new GlUniform(name, i + l, j);
			if (i <= 3) glUniform.setForDataType((int) fs[0], (int) fs[1], (int) fs[2], (int) fs[3]);
			else if (i <= 7) glUniform.setForDataType(fs[0], fs[1], fs[2], fs[3]);
			else glUniform.set(Arrays.copyOfRange(fs, 0, j));

			this.uniforms.add(glUniform);
		}
	}

	public Program getVertexShader() {
		return this.vertexShader;
	}

	public Program getFragmentShader() {
		return this.fragmentShader;
	}

	public void attachReferencedShaders() {
		this.fragmentShader.attachTo(this);
		this.vertexShader.attachTo(this);
	}

	public int getProgramRef() {
		return this.programId;
	}
}