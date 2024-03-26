package net.minecraft.src.smoothbeta.gl;

import org.lwjgl.opengl.GL20;

import net.minecraft.src.GLAllocation;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class GlUniform extends Uniform implements AutoCloseable {
	public static final int INT1 = 0;
	public static final int INT2 = 1;
	public static final int INT3 = 2;
	public static final int INT4 = 3;
	public static final int FLOAT1 = 4;
	public static final int FLOAT2 = 5;
	public static final int FLOAT3 = 6;
	public static final int FLOAT4 = 7;
	public static final int MAT2X2 = 8;
	public static final int MAT3X3 = 9;
	public static final int MAT4X4 = 10;
	private static final boolean TRANSPOSE = false;
	private int location;
	private final int count;
	private final int dataType;
	private final IntBuffer intData;
	private final FloatBuffer floatData;
	private final String name;

	public GlUniform(String name, int dataType, int count) {
		this.name = name;
		this.count = count;
		this.dataType = dataType;
		if (dataType <= INT4) {
			this.intData = GLAllocation.createDirectIntBuffer(count);
			this.floatData = null;
		} else {
			this.intData = null;
			this.floatData = GLAllocation.createDirectFloatBuffer(count);
		}

		this.location = -1;
		this.markStateDirty();
	}

	public static int getUniformLocation(int program, CharSequence name) {
		return GL20.glGetUniformLocation(program, name);
	}

	public static void uniform1(int location, int value) {
		GL20.glUniform1i(location, value);
	}

	public static void bindAttribLocation(int program, int index, CharSequence name) {
		GL20.glBindAttribLocation(program, index, name);
	}

	public void close() {}

	private void markStateDirty() {}

	public static int getTypeIndex(String typeName) {
		int i = -1;
		if ("int".equals(typeName)) i = INT1;
		else if ("float".equals(typeName)) i = FLOAT1;
		else if (typeName.startsWith("matrix")) if (typeName.endsWith("2x2")) i = MAT2X2;
		else if (typeName.endsWith("3x3")) i = MAT3X3;
		else if (typeName.endsWith("4x4")) i = MAT4X4;

		return i;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public String getName() {
		return this.name;
	}

	public final void set(float value1, float value2, float value3) {
		this.floatData.position(0);
		this.floatData.put(0, value1);
		this.floatData.put(1, value2);
		this.floatData.put(2, value3);
		this.markStateDirty();
	}

	public final void setForDataType(float value1, float value2, float value3, float value4) {
		this.floatData.position(0);
		if (this.dataType >= FLOAT1) this.floatData.put(0, value1);

		if (this.dataType >= FLOAT2) this.floatData.put(1, value2);

		if (this.dataType >= FLOAT3) this.floatData.put(2, value3);

		if (this.dataType >= FLOAT4) this.floatData.put(3, value4);

		this.markStateDirty();
	}

	public final void setForDataType(int value1, int value2, int value3, int value4) {
		this.intData.position(0);
		if (this.dataType >= INT1) this.intData.put(0, value1);

		if (this.dataType >= INT2) this.intData.put(1, value2);

		if (this.dataType >= INT3) this.intData.put(2, value3);

		if (this.dataType >= INT4) this.intData.put(3, value4);

		this.markStateDirty();
	}

	public final void set(int value) {
		this.intData.position(0);
		this.intData.put(0, value);
		this.markStateDirty();
	}

	public final void set(float[] values) {
		if (values.length < this.count) {
			System.err.printf("Uniform.set called with a too-small value array (expected %s, got %s). Ignoring.%n", this.count, values.length);
		} else {
			this.floatData.position(0);
			this.floatData.put(values);
			this.floatData.position(0);
			this.markStateDirty();
		}
	}

	public final void set(FloatBuffer matrix) {
		floatData.position(0);
		floatData.put(matrix);
		markStateDirty();
	}

	public void upload() {
		if (this.dataType <= INT4) this.uploadInts();
		else if (this.dataType <= FLOAT4) this.uploadFloats();
		else {
			if (this.dataType > MAT4X4) {
				System.err.printf("Uniform.upload called, but type value (%s) is not a valid type. Ignoring.%n", this.dataType);
				return;
			}

			this.uploadMatrix();
		}
	}

	private void uploadInts() {
		this.intData.rewind();
		switch (this.dataType) {
			case INT1: {
				GL20.glUniform1(this.location, this.intData);
				break;
			}
			case INT2: {
				GL20.glUniform2(this.location, this.intData);
				break;
			}
			case INT3: {
				GL20.glUniform3(this.location, this.intData);
				break;
			}
			case INT4: {
				GL20.glUniform4(this.location, this.intData);
				break;
			}
			default: {
				System.err.printf("Uniform.upload(I) called, but count value (%s) is not in the range of 1 to 4. Ignoring.%n", this.count);
			}
		}
	}

	private void uploadFloats() {
		this.floatData.rewind();
		switch (this.dataType) {
			case FLOAT1: {
				GL20.glUniform1(this.location, this.floatData);
				break;
			}
			case FLOAT2: {
				GL20.glUniform2(this.location, this.floatData);
				break;
			}
			case FLOAT3: {
				GL20.glUniform3(this.location, this.floatData);
				break;
			}
			case FLOAT4: {
				GL20.glUniform4(this.location, this.floatData);
				break;
			}
			default: {
				System.err.printf("Uniform.upload(F) called, but count value (%s) is not in the range of 1 to 4. Ignoring.%n", this.count);
			}
		}
	}

	private void uploadMatrix() {
		this.floatData.clear();
		switch (this.dataType) {
			case MAT2X2: {
				GL20.glUniformMatrix2(this.location, TRANSPOSE, this.floatData);
				break;
			}
			case MAT3X3: {
				GL20.glUniformMatrix3(this.location, TRANSPOSE, this.floatData);
				break;
			}
			case MAT4X4: {
				GL20.glUniformMatrix4(this.location, TRANSPOSE, this.floatData);
				break;
			}
		}
	}
}