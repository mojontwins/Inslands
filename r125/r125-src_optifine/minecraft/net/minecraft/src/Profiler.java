package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Profiler {
	public static boolean profilingEnabled = false;
	private static List sectionList = new ArrayList();
	private static List timestampList = new ArrayList();
	private static String profilingSection = "";
	private static Map profilingMap = new HashMap();
	public static boolean profilerGlobalEnabled = true;
	private static boolean profilerLocalEnabled = profilerGlobalEnabled;

	public static void clearProfiling() {
		profilingMap.clear();
		profilerLocalEnabled = profilerGlobalEnabled;
	}

	public static void startSection(String par0Str) {
		if(profilerLocalEnabled) {
			if(profilingEnabled) {
				if(profilingSection.length() > 0) {
					profilingSection = profilingSection + ".";
				}

				profilingSection = profilingSection + par0Str;
				sectionList.add(profilingSection);
				timestampList.add(System.nanoTime());
			}
		}
	}

	public static void endSection() {
		if(profilerLocalEnabled) {
			if(profilingEnabled) {
				long l = System.nanoTime();
				long l1 = ((Long)timestampList.remove(timestampList.size() - 1)).longValue();
				sectionList.remove(sectionList.size() - 1);
				long l2 = l - l1;
				if(profilingMap.containsKey(profilingSection)) {
					profilingMap.put(profilingSection, ((Long)profilingMap.get(profilingSection)).longValue() + l2);
				} else {
					profilingMap.put(profilingSection, l2);
				}

				profilingSection = sectionList.size() <= 0 ? "" : (String)sectionList.get(sectionList.size() - 1);
				if(l2 > 100000000L) {
					;
				}

			}
		}
	}

	public static List getProfilingData(String par0Str) {
		profilerLocalEnabled = profilerGlobalEnabled;
		if(!profilerLocalEnabled) {
			return new ArrayList(Arrays.asList(new ProfilerResult[]{new ProfilerResult("root", 0.0D, 0.0D)}));
		} else if(!profilingEnabled) {
			return null;
		} else {
			long l = profilingMap.containsKey("root") ? ((Long)profilingMap.get("root")).longValue() : 0L;
			long l1 = profilingMap.containsKey(par0Str) ? ((Long)profilingMap.get(par0Str)).longValue() : -1L;
			ArrayList arraylist = new ArrayList();
			if(par0Str.length() > 0) {
				par0Str = par0Str + ".";
			}

			long l2 = 0L;
			Iterator iterator = profilingMap.keySet().iterator();

			while(iterator.hasNext()) {
				String f = (String)iterator.next();
				if(f.length() > par0Str.length() && f.startsWith(par0Str) && f.indexOf(".", par0Str.length() + 1) < 0) {
					l2 += ((Long)profilingMap.get(f)).longValue();
				}
			}

			float f1 = (float)l2;
			if(l2 < l1) {
				l2 = l1;
			}

			if(l < l2) {
				l = l2;
			}

			Iterator iterator1 = profilingMap.keySet().iterator();

			String s3;
			while(iterator1.hasNext()) {
				s3 = (String)iterator1.next();
				if(s3.length() > par0Str.length() && s3.startsWith(par0Str) && s3.indexOf(".", par0Str.length() + 1) < 0) {
					long iterator2 = ((Long)profilingMap.get(s3)).longValue();
					double d = (double)iterator2 * 100.0D / (double)l2;
					double d1 = (double)iterator2 * 100.0D / (double)l;
					String s4 = s3.substring(par0Str.length());
					arraylist.add(new ProfilerResult(s4, d, d1));
				}
			}

			Iterator iterator21 = profilingMap.keySet().iterator();

			while(iterator21.hasNext()) {
				s3 = (String)iterator21.next();
				profilingMap.put(s3, ((Long)profilingMap.get(s3)).longValue() * 999L / 1000L);
			}

			if((float)l2 > f1) {
				arraylist.add(new ProfilerResult("unspecified", (double)((float)l2 - f1) * 100.0D / (double)l2, (double)((float)l2 - f1) * 100.0D / (double)l));
			}

			Collections.sort(arraylist);
			arraylist.add(0, new ProfilerResult(par0Str, 100.0D, (double)l2 * 100.0D / (double)l));
			return arraylist;
		}
	}

	public static void endStartSection(String par0Str) {
		if(profilerLocalEnabled) {
			endSection();
			startSection(par0Str);
		}
	}
}
