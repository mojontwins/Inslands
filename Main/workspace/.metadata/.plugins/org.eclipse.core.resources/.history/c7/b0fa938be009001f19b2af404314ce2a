package net.minecraft.src.smoothbeta;

import java.util.LinkedHashMap;
import java.util.Map;

public class VertexFormats {
    public static final VertexFormatElement POSITION_ELEMENT = new VertexFormatElement(0, VertexFormatElement.ComponentType.FLOAT, VertexFormatElement.Type.POSITION, 3);
    public static final VertexFormatElement COLOR_ELEMENT = new VertexFormatElement(0, VertexFormatElement.ComponentType.UBYTE, VertexFormatElement.Type.COLOR, 4);
    public static final VertexFormatElement TEXTURE_0_ELEMENT = new VertexFormatElement(0, VertexFormatElement.ComponentType.FLOAT, VertexFormatElement.Type.UV, 2);
    public static final VertexFormatElement NORMAL_ELEMENT = new VertexFormatElement(0, VertexFormatElement.ComponentType.BYTE, VertexFormatElement.Type.NORMAL, 3);
    public static final VertexFormatElement PADDING_ELEMENT = new VertexFormatElement(0, VertexFormatElement.ComponentType.BYTE, VertexFormatElement.Type.PADDING, 1);
    public static final VertexFormat POSITION_TEXTURE_COLOR_NORMAL;

    static {
        Map<String, VertexFormatElement> elementMap = new LinkedHashMap<>();
        elementMap.put("Position", POSITION_ELEMENT);
        elementMap.put("UV0", TEXTURE_0_ELEMENT);
        elementMap.put("Color", COLOR_ELEMENT);
        elementMap.put("Normal", NORMAL_ELEMENT);
        elementMap.put("Padding", PADDING_ELEMENT);
        POSITION_TEXTURE_COLOR_NORMAL = new VertexFormat(elementMap);
    }
}
