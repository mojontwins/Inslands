package net.minecraft.src.smoothbeta;

import java.util.List;

// Re-implementation of Guava methods for SmoothBeta
public class StringUtils {
    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean isBlank(String string) {
        return string.isEmpty();
    }

    public static boolean endsWithLineBreak(String string) {
        return string.endsWith("\n") || string.endsWith("\r\n");
    }

    // Not exactly the best way to do this...
    public static int countLines(String string) {
        if (string.contains("\r\n")) {
            string = string.replaceAll("\r\n", "\n");
        }
        String[] splitLines = string.split("\n");
        return splitLines.length;
    }

    public static String join(List<String> strings, String joiner) {
        StringBuilder combinedString = new StringBuilder();
        for (String string : strings) {
            combinedString.append(String.join(string, joiner));
        }
        return combinedString.toString();
    }
}
