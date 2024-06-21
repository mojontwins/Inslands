package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class PotionHelper {
	public static final String field_40367_a = null;
	public static final String sugarEffect;
	public static final String ghastTearEffect = "+0-1-2-3&4-4+13";
	public static final String spiderEyeEffect;
	public static final String fermentedSpiderEyeEffect;
	public static final String speckledMelonEffect;
	public static final String blazePowderEffect;
	public static final String magmaCreamEffect;
	public static final String redstoneEffect;
	public static final String glowstoneEffect;
	public static final String gunpowderEffect;
	private static final HashMap potionRequirements = new HashMap();
	private static final HashMap field_40371_m = new HashMap();
	private static final HashMap field_40368_n;
	private static final String[] potionPrefixes;

	public static boolean checkFlag(int i0, int i1) {
		return (i0 & 1 << i1) != 0;
	}

	private static int isFlagSet(int i0, int i1) {
		return checkFlag(i0, i1) ? 1 : 0;
	}

	private static int isFlagUnset(int i0, int i1) {
		return checkFlag(i0, i1) ? 0 : 1;
	}

	public static int func_40352_a(int i0) {
		return func_40351_a(i0, 5, 4, 3, 2, 1);
	}

	public static int func_40354_a(Collection collection0) {
		int i1 = 3694022;
		if(collection0 != null && !collection0.isEmpty()) {
			float f2 = 0.0F;
			float f3 = 0.0F;
			float f4 = 0.0F;
			float f5 = 0.0F;
			Iterator iterator6 = collection0.iterator();

			while(iterator6.hasNext()) {
				PotionEffect potionEffect7 = (PotionEffect)iterator6.next();
				int i8 = Potion.potionTypes[potionEffect7.getPotionID()].getLiquidColor();

				for(int i9 = 0; i9 <= potionEffect7.getAmplifier(); ++i9) {
					f2 += (float)(i8 >> 16 & 255) / 255.0F;
					f3 += (float)(i8 >> 8 & 255) / 255.0F;
					f4 += (float)(i8 >> 0 & 255) / 255.0F;
					++f5;
				}
			}

			f2 = f2 / f5 * 255.0F;
			f3 = f3 / f5 * 255.0F;
			f4 = f4 / f5 * 255.0F;
			return (int)f2 << 16 | (int)f3 << 8 | (int)f4;
		} else {
			return i1;
		}
	}

	public static int func_40358_a(int i0, boolean z1) {
		if(!z1) {
			if(field_40368_n.containsKey(i0)) {
				return ((Integer)field_40368_n.get(i0)).intValue();
			} else {
				int i2 = func_40354_a(getPotionEffects(i0, false));
				field_40368_n.put(i0, i2);
				return i2;
			}
		} else {
			return func_40354_a(getPotionEffects(i0, z1));
		}
	}

	public static String func_40359_b(int i0) {
		int i1 = func_40352_a(i0);
		return potionPrefixes[i1];
	}

	private static int func_40347_a(boolean z0, boolean z1, boolean z2, int i3, int i4, int i5, int i6) {
		int i7 = 0;
		if(z0) {
			i7 = isFlagUnset(i6, i4);
		} else if(i3 != -1) {
			if(i3 == 0 && countSetFlags(i6) == i4) {
				i7 = 1;
			} else if(i3 == 1 && countSetFlags(i6) > i4) {
				i7 = 1;
			} else if(i3 == 2 && countSetFlags(i6) < i4) {
				i7 = 1;
			}
		} else {
			i7 = isFlagSet(i6, i4);
		}

		if(z1) {
			i7 *= i5;
		}

		if(z2) {
			i7 *= -1;
		}

		return i7;
	}

	private static int countSetFlags(int i0) {
		int i1;
		for(i1 = 0; i0 > 0; ++i1) {
			i0 &= i0 - 1;
		}

		return i1;
	}

	private static int func_40355_a(String string0, int i1, int i2, int i3) {
		if(i1 < string0.length() && i2 >= 0 && i1 < i2) {
			int i4 = string0.indexOf(124, i1);
			int i5;
			int i17;
			if(i4 >= 0 && i4 < i2) {
				i5 = func_40355_a(string0, i1, i4 - 1, i3);
				if(i5 > 0) {
					return i5;
				} else {
					i17 = func_40355_a(string0, i4 + 1, i2, i3);
					return i17 > 0 ? i17 : 0;
				}
			} else {
				i5 = string0.indexOf(38, i1);
				if(i5 >= 0 && i5 < i2) {
					i17 = func_40355_a(string0, i1, i5 - 1, i3);
					if(i17 <= 0) {
						return 0;
					} else {
						int i18 = func_40355_a(string0, i5 + 1, i2, i3);
						return i18 <= 0 ? 0 : (i17 > i18 ? i17 : i18);
					}
				} else {
					boolean z6 = false;
					boolean z7 = false;
					boolean z8 = false;
					boolean z9 = false;
					boolean z10 = false;
					byte b11 = -1;
					int i12 = 0;
					int i13 = 0;
					int i14 = 0;

					for(int i15 = i1; i15 < i2; ++i15) {
						char c16 = string0.charAt(i15);
						if(c16 >= 48 && c16 <= 57) {
							if(z6) {
								i13 = c16 - 48;
								z7 = true;
							} else {
								i12 *= 10;
								i12 += c16 - 48;
								z8 = true;
							}
						} else if(c16 == 42) {
							z6 = true;
						} else if(c16 == 33) {
							if(z8) {
								i14 += func_40347_a(z9, z7, z10, b11, i12, i13, i3);
								z9 = false;
								z10 = false;
								z6 = false;
								z7 = false;
								z8 = false;
								i13 = 0;
								i12 = 0;
								b11 = -1;
							}

							z9 = true;
						} else if(c16 == 45) {
							if(z8) {
								i14 += func_40347_a(z9, z7, z10, b11, i12, i13, i3);
								z9 = false;
								z10 = false;
								z6 = false;
								z7 = false;
								z8 = false;
								i13 = 0;
								i12 = 0;
								b11 = -1;
							}

							z10 = true;
						} else if(c16 != 61 && c16 != 60 && c16 != 62) {
							if(c16 == 43 && z8) {
								i14 += func_40347_a(z9, z7, z10, b11, i12, i13, i3);
								z9 = false;
								z10 = false;
								z6 = false;
								z7 = false;
								z8 = false;
								i13 = 0;
								i12 = 0;
								b11 = -1;
							}
						} else {
							if(z8) {
								i14 += func_40347_a(z9, z7, z10, b11, i12, i13, i3);
								z9 = false;
								z10 = false;
								z6 = false;
								z7 = false;
								z8 = false;
								i13 = 0;
								i12 = 0;
								b11 = -1;
							}

							if(c16 == 61) {
								b11 = 0;
							} else if(c16 == 60) {
								b11 = 2;
							} else if(c16 == 62) {
								b11 = 1;
							}
						}
					}

					if(z8) {
						i14 += func_40347_a(z9, z7, z10, b11, i12, i13, i3);
					}

					return i14;
				}
			}
		} else {
			return 0;
		}
	}

	public static List getPotionEffects(int i0, boolean z1) {
		ArrayList arrayList2 = null;
		Potion[] potion3 = Potion.potionTypes;
		int i4 = potion3.length;

		for(int i5 = 0; i5 < i4; ++i5) {
			Potion potion6 = potion3[i5];
			if(potion6 != null && (!potion6.isUsable() || z1)) {
				String string7 = (String)potionRequirements.get(potion6.getId());
				if(string7 != null) {
					int i8 = func_40355_a(string7, 0, string7.length(), i0);
					if(i8 > 0) {
						int i9 = 0;
						String string10 = (String)field_40371_m.get(potion6.getId());
						if(string10 != null) {
							i9 = func_40355_a(string10, 0, string10.length(), i0);
							if(i9 < 0) {
								i9 = 0;
							}
						}

						if(potion6.isInstant()) {
							i8 = 1;
						} else {
							i8 = 1200 * (i8 * 3 + (i8 - 1) * 2);
							i8 >>= i9;
							i8 = (int)Math.round((double)i8 * potion6.getEffectiveness());
							if((i0 & 16384) != 0) {
								i8 = (int)Math.round((double)i8 * 0.75D + 0.5D);
							}
						}

						if(arrayList2 == null) {
							arrayList2 = new ArrayList();
						}

						arrayList2.add(new PotionEffect(potion6.getId(), i8, i9));
					}
				}
			}
		}

		return arrayList2;
	}

	private static int brewBitOperations(int i0, int i1, boolean z2, boolean z3, boolean z4) {
		if(z4) {
			if(!checkFlag(i0, i1)) {
				return 0;
			}
		} else if(z2) {
			i0 &= ~(1 << i1);
		} else if(z3) {
			if((i0 & 1 << i1) != 0) {
				i0 &= ~(1 << i1);
			} else {
				i0 |= 1 << i1;
			}
		} else {
			i0 |= 1 << i1;
		}

		return i0;
	}

	public static int applyIngredient(int i0, String string1) {
		byte b2 = 0;
		int i3 = string1.length();
		boolean z4 = false;
		boolean z5 = false;
		boolean z6 = false;
		boolean z7 = false;
		int i8 = 0;

		for(int i9 = b2; i9 < i3; ++i9) {
			char c10 = string1.charAt(i9);
			if(c10 >= 48 && c10 <= 57) {
				i8 *= 10;
				i8 += c10 - 48;
				z4 = true;
			} else if(c10 == 33) {
				if(z4) {
					i0 = brewBitOperations(i0, i8, z6, z5, z7);
					z7 = false;
					z5 = false;
					z6 = false;
					z4 = false;
					i8 = 0;
				}

				z5 = true;
			} else if(c10 == 45) {
				if(z4) {
					i0 = brewBitOperations(i0, i8, z6, z5, z7);
					z7 = false;
					z5 = false;
					z6 = false;
					z4 = false;
					i8 = 0;
				}

				z6 = true;
			} else if(c10 == 43) {
				if(z4) {
					i0 = brewBitOperations(i0, i8, z6, z5, z7);
					z7 = false;
					z5 = false;
					z6 = false;
					z4 = false;
					i8 = 0;
				}
			} else if(c10 == 38) {
				if(z4) {
					i0 = brewBitOperations(i0, i8, z6, z5, z7);
					z7 = false;
					z5 = false;
					z6 = false;
					z4 = false;
					i8 = 0;
				}

				z7 = true;
			}
		}

		if(z4) {
			i0 = brewBitOperations(i0, i8, z6, z5, z7);
		}

		return i0 & 32767;
	}

	public static int func_40351_a(int i0, int i1, int i2, int i3, int i4, int i5) {
		return (checkFlag(i0, i1) ? 16 : 0) | (checkFlag(i0, i2) ? 8 : 0) | (checkFlag(i0, i3) ? 4 : 0) | (checkFlag(i0, i4) ? 2 : 0) | (checkFlag(i0, i5) ? 1 : 0);
	}

	static {
		potionRequirements.put(Potion.regeneration.getId(), "0 & !1 & !2 & !3 & 0+6");
		sugarEffect = "-0+1-2-3&4-4+13";
		potionRequirements.put(Potion.moveSpeed.getId(), "!0 & 1 & !2 & !3 & 1+6");
		magmaCreamEffect = "+0+1-2-3&4-4+13";
		potionRequirements.put(Potion.fireResistance.getId(), "0 & 1 & !2 & !3 & 0+6");
		speckledMelonEffect = "+0-1+2-3&4-4+13";
		potionRequirements.put(Potion.heal.getId(), "0 & !1 & 2 & !3");
		spiderEyeEffect = "-0-1+2-3&4-4+13";
		potionRequirements.put(Potion.poison.getId(), "!0 & !1 & 2 & !3 & 2+6");
		fermentedSpiderEyeEffect = "-0+3-4+13";
		potionRequirements.put(Potion.weakness.getId(), "!0 & !1 & !2 & 3 & 3+6");
		potionRequirements.put(Potion.harm.getId(), "!0 & !1 & 2 & 3");
		potionRequirements.put(Potion.moveSlowdown.getId(), "!0 & 1 & !2 & 3 & 3+6");
		blazePowderEffect = "+0-1-2+3&4-4+13";
		potionRequirements.put(Potion.damageBoost.getId(), "0 & !1 & !2 & 3 & 3+6");
		glowstoneEffect = "+5-6-7";
		field_40371_m.put(Potion.moveSpeed.getId(), "5");
		field_40371_m.put(Potion.digSpeed.getId(), "5");
		field_40371_m.put(Potion.damageBoost.getId(), "5");
		field_40371_m.put(Potion.regeneration.getId(), "5");
		field_40371_m.put(Potion.harm.getId(), "5");
		field_40371_m.put(Potion.heal.getId(), "5");
		field_40371_m.put(Potion.resistance.getId(), "5");
		field_40371_m.put(Potion.poison.getId(), "5");
		redstoneEffect = "-5+6-7";
		gunpowderEffect = "+14&13-13";
		field_40368_n = new HashMap();
		potionPrefixes = new String[]{"potion.prefix.mundane", "potion.prefix.uninteresting", "potion.prefix.bland", "potion.prefix.clear", "potion.prefix.milky", "potion.prefix.diffuse", "potion.prefix.artless", "potion.prefix.thin", "potion.prefix.awkward", "potion.prefix.flat", "potion.prefix.bulky", "potion.prefix.bungling", "potion.prefix.buttered", "potion.prefix.smooth", "potion.prefix.suave", "potion.prefix.debonair", "potion.prefix.thick", "potion.prefix.elegant", "potion.prefix.fancy", "potion.prefix.charming", "potion.prefix.dashing", "potion.prefix.refined", "potion.prefix.cordial", "potion.prefix.sparkling", "potion.prefix.potent", "potion.prefix.foul", "potion.prefix.odorless", "potion.prefix.rank", "potion.prefix.harsh", "potion.prefix.acrid", "potion.prefix.gross", "potion.prefix.stinky"};
	}
}
