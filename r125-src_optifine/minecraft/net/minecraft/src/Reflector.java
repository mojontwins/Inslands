package net.minecraft.src;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reflector {
	private static Class[] classes = new Class[16];
	private static boolean[] classesChecked = new boolean[16];
	private static Map classNameMap = null;
	private static Method[] methods = new Method[256];
	private static boolean[] methodsChecked = new boolean[256];
	private static Map methodNameMap = null;
	private static Field[] fields = new Field[256];
	private static boolean[] fieldsChecked = new boolean[256];
	private static Map fieldNameMap = null;
	public static final int ModLoader = 0;
	public static final int ForgeHooksClient = 1;
	public static final int MinecraftForgeClient = 2;
	public static final int LightCache = 3;
	public static final int BlockCoord = 4;
	public static final int ForgeBlock = 5;
	public static final int ItemRenderType = 6;
	public static final int FMLRender = 7;
	public static final int ForgeEffectRenderer = 8;
	public static final int ForgeHooks = 9;
	public static final int Entity = 10;
	public static final int List = 11;
	public static final int ModLoader_renderWorldBlock = 0;
	public static final int ModLoader_renderInvBlock = 1;
	public static final int ModLoader_renderBlockIsItemFull3D = 2;
	public static final int ForgeHooksClient_onBlockHighlight = 10;
	public static final int ForgeHooksClient_canRenderInPass = 11;
	public static final int ForgeHooksClient_getTexture = 12;
	public static final int ForgeHooksClient_beforeRenderPass = 13;
	public static final int ForgeHooksClient_afterRenderPass = 14;
	public static final int ForgeHooksClient_beforeBlockRender = 15;
	public static final int ForgeHooksClient_afterBlockRender = 16;
	public static final int ForgeHooksClient_onRenderWorldLast = 17;
	public static final int ForgeHooksClient_onTextureLoadPre = 18;
	public static final int ForgeHooksClient_onTextureLoad = 19;
	public static final int MinecraftForgeClient_getItemRenderer = 20;
	public static final int LightCache_clear = 30;
	public static final int BlockCoord_resetPool = 40;
	public static final int ForgeBlock_isLadder = 50;
	public static final int ForgeBlock_isBed = 51;
	public static final int FMLRender_setTextureDimensions = 70;
	public static final int FMLRender_preRegisterEffect = 71;
	public static final int FMLRender_onUpdateTextureEffect = 72;
	public static final int FMLRender_onTexturePackChange = 73;
	public static final int ForgeEffectRenderer_addEffect = 80;
	public static final int ForgeHooks_onEntityLivingSetAttackTarget = 90;
	public static final int ForgeHooks_onEntityLivingUpdate = 91;
	public static final int ForgeHooks_onEntityLivingAttacked = 92;
	public static final int ForgeHooks_onEntityLivingHurt = 93;
	public static final int ForgeHooks_onEntityLivingDeath = 94;
	public static final int ForgeHooks_onEntityLivingDrops = 95;
	public static final int ForgeHooks_onEntityLivingFall = 96;
	public static final int ForgeHooks_onEntityLivingJump = 97;
	public static final int List_clear = 110;
	public static final int LightCache_cache = 30;
	public static final int ItemRenderType_EQUIPPED = 60;
	public static final int Entity_captureDrops = 100;
	public static final int Entity_capturedDrops = 101;

	private static Map getClassNameMap() {
		if(classNameMap == null) {
			classNameMap = new HashMap();
			classNameMap.put(0, "ModLoader");
			classNameMap.put(1, "forge.ForgeHooksClient");
			classNameMap.put(2, "forge.MinecraftForgeClient");
			classNameMap.put(3, "LightCache");
			classNameMap.put(4, "BlockCoord");
			classNameMap.put(5, Block.class);
			classNameMap.put(6, "forge.IItemRenderer$ItemRenderType");
			classNameMap.put(7, "FMLRenderAccessLibrary");
			classNameMap.put(8, EffectRenderer.class);
			classNameMap.put(9, "forge.ForgeHooks");
			classNameMap.put(10, Entity.class);
			classNameMap.put(11, List.class);
		}

		return classNameMap;
	}

	private static Map getMethodNameMap() {
		if(methodNameMap == null) {
			methodNameMap = new HashMap();
			methodNameMap.put(0, "renderWorldBlock");
			methodNameMap.put(1, "renderInvBlock");
			methodNameMap.put(2, "renderBlockIsItemFull3D");
			methodNameMap.put(10, "onBlockHighlight");
			methodNameMap.put(11, "canRenderInPass");
			methodNameMap.put(12, "getTexture");
			methodNameMap.put(13, "beforeRenderPass");
			methodNameMap.put(14, "afterRenderPass");
			methodNameMap.put(15, "beforeBlockRender");
			methodNameMap.put(16, "afterBlockRender");
			methodNameMap.put(17, "onRenderWorldLast");
			methodNameMap.put(18, "onTextureLoadPre");
			methodNameMap.put(19, "onTextureLoad");
			methodNameMap.put(20, "getItemRenderer");
			methodNameMap.put(30, "clear");
			methodNameMap.put(40, "resetPool");
			methodNameMap.put(50, "isLadder");
			methodNameMap.put(51, "isBed");
			methodNameMap.put(70, "setTextureDimensions");
			methodNameMap.put(71, "preRegisterEffect");
			methodNameMap.put(72, "onUpdateTextureEffect");
			methodNameMap.put(73, "onTexturePackChange");
			methodNameMap.put(80, "addEffect");
			methodNameMap.put(90, "onEntityLivingSetAttackTarget");
			methodNameMap.put(91, "onEntityLivingUpdate");
			methodNameMap.put(92, "onEntityLivingAttacked");
			methodNameMap.put(93, "onEntityLivingHurt");
			methodNameMap.put(94, "onEntityLivingDeath");
			methodNameMap.put(95, "onEntityLivingDrops");
			methodNameMap.put(96, "onEntityLivingFall");
			methodNameMap.put(97, "onEntityLivingJump");
			methodNameMap.put(110, "clear");
		}

		return methodNameMap;
	}

	private static Map getFieldNameMap() {
		if(fieldNameMap == null) {
			fieldNameMap = new HashMap();
			fieldNameMap.put(30, "cache");
			fieldNameMap.put(60, "EQUIPPED");
			fieldNameMap.put(100, "captureDrops");
			fieldNameMap.put(101, "capturedDrops");
		}

		return fieldNameMap;
	}

	public static void callVoid(int methodId, Object[] params) {
		try {
			Method e = getMethod(methodId);
			if(e == null) {
				return;
			}

			e.invoke((Object)null, params);
		} catch (Exception exception3) {
			exception3.printStackTrace();
		}

	}

	public static int callInt(int methodId, Object[] params) {
		Integer val = (Integer)call(methodId, params);
		return val.intValue();
	}

	public static String callString(int methodId, Object[] params) {
		return (String)call(methodId, params);
	}

	public static boolean callBoolean(int methodId, Object[] params) {
		try {
			Method e = getMethod(methodId);
			if(e == null) {
				return false;
			} else {
				Boolean retVal = (Boolean)e.invoke((Object)null, params);
				return retVal.booleanValue();
			}
		} catch (Throwable throwable4) {
			throwable4.printStackTrace();
			return false;
		}
	}

	public static boolean callBoolean(Object obj, int methodId, Object[] params) {
		try {
			Method e = getMethod(methodId);
			if(e == null) {
				return false;
			} else {
				Boolean retVal = (Boolean)e.invoke(obj, params);
				return retVal.booleanValue();
			}
		} catch (Throwable throwable5) {
			throwable5.printStackTrace();
			return false;
		}
	}

	public static Object call(int methodId, Object[] params) {
		try {
			Method e = getMethod(methodId);
			if(e == null) {
				return false;
			} else {
				Object retVal = e.invoke((Object)null, params);
				return retVal;
			}
		} catch (Throwable throwable4) {
			throwable4.printStackTrace();
			return null;
		}
	}

	public static void callVoid(Object obj, int methodId, Object[] params) {
		try {
			if(obj == null) {
				return;
			}

			Method e = getMethod(methodId);
			if(e == null) {
				return;
			}

			e.invoke(obj, params);
		} catch (Throwable throwable4) {
			throwable4.printStackTrace();
		}

	}

	private static Method getMethod(int methodId) {
		Method m = methods[methodId];
		if(m == null) {
			if(methodsChecked[methodId]) {
				return null;
			}

			methodsChecked[methodId] = true;
			m = findMethod(methodId);
			methods[methodId] = m;
		}

		return m;
	}

	private static Method findMethod(int methodId) {
		int classId = methodId / 10;
		Class cls = getClass(classId);
		if(cls == null) {
			return null;
		} else {
			String methodName = (String)getMethodNameMap().get(methodId);
			if(methodName == null) {
				Config.log("Method name not found for id: " + methodId);
				return null;
			} else {
				Method[] ms = cls.getMethods();

				for(int i = 0; i < ms.length; ++i) {
					Method m = ms[i];
					if(m.getName().equals(methodName)) {
						return m;
					}
				}

				Config.log("Method not found: " + cls.getName() + "." + methodName);
				return null;
			}
		}
	}

	private static Field getField(int fieldId) {
		Field f = fields[fieldId];
		if(f == null) {
			if(fieldsChecked[fieldId]) {
				return null;
			}

			fieldsChecked[fieldId] = true;
			f = findField(fieldId);
			fields[fieldId] = f;
		}

		return f;
	}

	private static Field findField(int fieldId) {
		int classId = fieldId / 10;
		Class cls = getClass(classId);
		if(cls == null) {
			return null;
		} else {
			String fieldName = (String)getFieldNameMap().get(fieldId);
			if(fieldName == null) {
				Config.log("Field name not found for id: " + fieldId);
				return null;
			} else {
				try {
					Field e = cls.getDeclaredField(fieldName);
					return e;
				} catch (SecurityException securityException5) {
					securityException5.printStackTrace();
				} catch (NoSuchFieldException noSuchFieldException6) {
					Config.log("Field not found: " + cls.getName() + "." + fieldName);
				}

				return null;
			}
		}
	}

	private static Class getClass(int classId) {
		Class cls = classes[classId];
		if(cls == null) {
			if(classesChecked[classId]) {
				return null;
			}

			classesChecked[classId] = true;
			Object classValue = getClassNameMap().get(classId);
			if(classValue instanceof Class) {
				cls = (Class)classValue;
				classes[classId] = cls;
				return cls;
			}

			String className = (String)classValue;
			if(className == null) {
				Config.log("Class name not found for id: " + classId);
				return null;
			}

			try {
				cls = Class.forName(className);
				classes[classId] = cls;
			} catch (ClassNotFoundException classNotFoundException5) {
				Config.log("Class not present: " + className);
			} catch (Throwable throwable6) {
				throwable6.printStackTrace();
			}
		}

		return cls;
	}

	public static boolean hasClass(int classId) {
		Class cls = getClass(classId);
		return cls != null;
	}

	public static boolean hasMethod(int methodId) {
		Method m = getMethod(methodId);
		return m != null;
	}

	public static Object getFieldValue(int fieldId) {
		return getFieldValue((Object)null, fieldId);
	}

	public static Object getFieldValue(Object obj, int fieldId) {
		try {
			Field e = getField(fieldId);
			if(e == null) {
				return null;
			} else {
				Object value = e.get(obj);
				return value;
			}
		} catch (Throwable throwable4) {
			throwable4.printStackTrace();
			return null;
		}
	}

	public static void setFieldValue(int fieldId, Object value) {
		setFieldValue((Object)null, fieldId, value);
	}

	public static void setFieldValue(Object obj, int fieldId, Object value) {
		try {
			Field e = getField(fieldId);
			if(e == null) {
				return;
			}

			e.set(obj, value);
		} catch (Throwable throwable4) {
			throwable4.printStackTrace();
		}

	}
}
