package net.minecraft.src.smoothbeta;

public class VertexFormatElement {
    private final ComponentType componentType;
    private final Type type;
    private final int uvIndex;
    private final int componentCount;
    /**
     * The total length of this element (in bytes).
     */
    private final int byteLength;

    public VertexFormatElement(int uvIndex, ComponentType componentType, Type type, int componentCount) {
        if (!this.isValidType(uvIndex, type))
            throw new IllegalStateException("Multiple vertex elements of the same type other than UVs are not supported");
        this.type = type;
        this.componentType = componentType;
        this.uvIndex = uvIndex;
        this.componentCount = componentCount;
        this.byteLength = componentType.getByteLength() * this.componentCount;
    }

    private boolean isValidType(int uvIndex, Type type) {
        return uvIndex == 0 || type == Type.UV;
    }

    public String toString() {
        return this.componentCount + "," + this.type.getName() + "," + this.componentType.getName();
    }

    public final int getByteLength() {
        return this.byteLength;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        VertexFormatElement vertexFormatElement = (VertexFormatElement)o;
        if (this.componentCount != vertexFormatElement.componentCount) return false;
        if (this.uvIndex != vertexFormatElement.uvIndex) return false;
        if (this.componentType != vertexFormatElement.componentType) return false;
        return this.type == vertexFormatElement.type;
    }

    public int hashCode() {
        int i = this.componentType.hashCode();
        i = 31 * i + this.type.hashCode();
        i = 31 * i + this.uvIndex;
        i = 31 * i + this.componentCount;
        return i;
    }

    public enum Type {
        POSITION("Position"),
        NORMAL("Normal"),
        COLOR("Vertex Color"),
        UV("UV"),
        PADDING("Padding");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum ComponentType {
        FLOAT(4, "Float"),
        UBYTE(1, "Unsigned Byte"),
        BYTE(1, "Byte");

        private final int byteLength;
        private final String name;

        ComponentType(int byteLength, String name) {
            this.byteLength = byteLength;
            this.name = name;
        }

        public int getByteLength() {
            return this.byteLength;
        }

        public String getName() {
            return this.name;
        }
    }
}