package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profiler {
	public static boolean profilingEnabled = false;
	private static List sectionList = new ArrayList();
	private static List timestampList = new ArrayList();
	private static String profilingSection = "";
	private static Map profilingMap = new HashMap();

	public static void startSection(String string0) {
		if(profilingEnabled) {
			if(profilingSection.length() > 0) {
				profilingSection = profilingSection + ".";
			}

			profilingSection = profilingSection + string0;
			sectionList.add(profilingSection);
			timestampList.add(System.nanoTime());
		}
	}

	public static void endSection() {
		if(profilingEnabled) {
			long j0 = System.nanoTime();
			long j2 = ((Long)timestampList.remove(timestampList.size() - 1)).longValue();
			sectionList.remove(sectionList.size() - 1);
			long j4 = j0 - j2;
			if(profilingMap.containsKey(profilingSection)) {
				profilingMap.put(profilingSection, ((Long)profilingMap.get(profilingSection)).longValue() + j4);
			} else {
				profilingMap.put(profilingSection, j4);
			}

			profilingSection = sectionList.size() > 0 ? (String)sectionList.get(sectionList.size() - 1) : "";
			if(j4 > 100000000L) {
				System.out.println(profilingSection + " " + j4);
			}

		}
	}

	public static void endStartSection(String string0) {
		endSection();
		startSection(string0);
	}
}
