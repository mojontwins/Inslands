package net.minecraft.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CoordXZUtils {
	
	public static CoordXZ center = new CoordXZ(0, 0);
	public static List<CoordXZ> cacheCoordsOrderedFromCenter = null;
	public static int cacheW = 0;
	public static int cacheH = 0;         

	public static Comparator<CoordXZ> offCenterComparator = new Comparator<CoordXZ>() {
		public int compare(CoordXZ a, CoordXZ b) {
			return center.distSqFrom(a) - center.distSqFrom(b);
		}
	};
	
	/*
	 *  Make a list of CoordXZ ordered by dist to the center
	 *  In a w x h area.
	 */
	public static List<CoordXZ> getCoordsOrderedFromCenter(int w, int h, int step) {
		// Reuse cache
		if(
				cacheCoordsOrderedFromCenter != null && 
				w == cacheW && h == cacheH
		) return cacheCoordsOrderedFromCenter;
		
		center = new CoordXZ(w / 2, h / 2);
		
		List<CoordXZ> coords = new ArrayList<>();
		
		for(int x = 0; x < w; x += step) {
			for(int z = 0; z < h; z += step) {
				coords.add(new CoordXZ(x, z));
			}
		}
		
		Collections.sort(coords, offCenterComparator);
		
		// Store in the cache for reuse.
		cacheCoordsOrderedFromCenter = coords;
		
		return coords;
	}
}
