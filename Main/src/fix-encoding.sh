# Busca archivos de texto recurrentemente y procesa cada uno
find . -type f \( -name "*.txt" -o -name "*.java" -o -name "*.lang" -o -name "*.bo3" \) | while read -r file; do
    # Detecta la codificación actual del archivo
    ENCODING=$(file -b --mime-encoding "$file")
    
    # Convierte solo si no es UTF-8, ASCII o Binario
    if [ "$ENCODING" != "utf-8" ] && [ "$ENCODING" != "us-ascii" ] && [ "$ENCODING" != "binary" ]; then
        echo "Convirtiendo: $file ($ENCODING -> utf-8)"
        
        # Realiza la conversión de forma segura usando un archivo temporal
        iconv -f "$ENCODING" -t UTF-8 "$file" > "$file.tmp" 2>/dev/null
        
        if [ $? -eq 0 ]; then
            mv "$file.tmp" "$file"
        else
            echo "Error al convertir: $file. Restaurando original..."
            rm -f "$file.tmp"
        fi
    fi
done
