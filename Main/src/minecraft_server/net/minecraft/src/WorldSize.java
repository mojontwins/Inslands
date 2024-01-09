package net.minecraft.src;

public class WorldSize {
	public static int xChunks;
	public static int zChunks;
	
	public static int width;
	public static int length;
	
	public static int sizeID;
	
	private static final String sizeNames[] = new String[] { "small", "normal", "big" };
	
	public static void setSize(int xChunks, int zChunks) {
		WorldSize.xChunks = xChunks; 
		WorldSize.zChunks = zChunks;
		WorldSize.width = xChunks * 16;
		WorldSize.length = zChunks * 16;
		
		System.out.println ("Size set: " + xChunks + " x " + zChunks);
	}
	
	public static int coords2hash(int x, int z) {
		return x + z * xChunks;
	}
	
	public double getDistanceFromCenter(int x, int z) {
		double xx = (double)(width / 2 - x);
		double zz = (double)(length / 2 - z);
		return Math.sqrt(xx * xx + zz * zz);
	}
	
	public static int getTotalChunks() {
		return xChunks * zChunks;
	}

	public static void setSizeById(int a) {
		switch(a) {
			case 0: setSize(8, 8); break;
			case 1: setSize(16, 16); break;
			case 2: setSize(32, 32); break;
		}
		WorldSize.sizeID = a;
	}

	public static void setSizeByName(String stringProperty) {
		if(stringProperty == null || "".equals(stringProperty)) {
			setSizeById(1);
			return;
		} 

		for(int i = 0; i < sizeNames.length; i ++) {
			if(stringProperty.equals(sizeNames[i])) {
				setSizeById(i);
				return;
			}
		}
		
		setSizeById(1);
	}
}
