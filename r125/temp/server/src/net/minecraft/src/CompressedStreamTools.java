package net.minecraft.src;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressedStreamTools {
	public static NBTTagCompound readCompressed(InputStream inputStream0) throws IOException {
		DataInputStream dataInputStream1 = new DataInputStream(new BufferedInputStream(new GZIPInputStream(inputStream0)));

		NBTTagCompound nBTTagCompound2;
		try {
			nBTTagCompound2 = read(dataInputStream1);
		} finally {
			dataInputStream1.close();
		}

		return nBTTagCompound2;
	}

	public static void writeCompressed(NBTTagCompound nBTTagCompound0, OutputStream outputStream1) throws IOException {
		DataOutputStream dataOutputStream2 = new DataOutputStream(new GZIPOutputStream(outputStream1));

		try {
			write(nBTTagCompound0, dataOutputStream2);
		} finally {
			dataOutputStream2.close();
		}

	}

	public static NBTTagCompound decompress(byte[] b0) throws IOException {
		DataInputStream dataInputStream1 = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(b0))));

		NBTTagCompound nBTTagCompound2;
		try {
			nBTTagCompound2 = read(dataInputStream1);
		} finally {
			dataInputStream1.close();
		}

		return nBTTagCompound2;
	}

	public static byte[] compress(NBTTagCompound nBTTagCompound0) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream2 = new DataOutputStream(new GZIPOutputStream(byteArrayOutputStream1));

		try {
			write(nBTTagCompound0, dataOutputStream2);
		} finally {
			dataOutputStream2.close();
		}

		return byteArrayOutputStream1.toByteArray();
	}

	public static NBTTagCompound read(DataInput dataInput0) throws IOException {
		NBTBase nBTBase1 = NBTBase.readNamedTag(dataInput0);
		if(nBTBase1 instanceof NBTTagCompound) {
			return (NBTTagCompound)nBTBase1;
		} else {
			throw new IOException("Root tag must be a named compound tag");
		}
	}

	public static void write(NBTTagCompound nBTTagCompound0, DataOutput dataOutput1) throws IOException {
		NBTBase.writeNamedTag(nBTTagCompound0, dataOutput1);
	}
}
