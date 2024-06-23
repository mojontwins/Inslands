package net.minecraft.src;

public enum EnumOptions {
	MUSIC("options.music", true, false),
	SOUND("options.sound", true, false),
	INVERT_MOUSE("options.invertMouse", false, true),
	SENSITIVITY("options.sensitivity", true, false),
	RENDER_DISTANCE("options.renderDistance", false, false),
	VIEW_BOBBING("options.viewBobbing", false, true),
	ANAGLYPH("options.anaglyph", false, true),
	ADVANCED_OPENGL("options.advancedOpengl", false, true),
	FRAMERATE_LIMIT("options.framerateLimit", false, false),
	DIFFICULTY("options.difficulty", false, false),
	GRAPHICS("options.graphics", false, false),
	AMBIENT_OCCLUSION("options.ao", false, true),
	GUI_SCALE("options.guiScale", false, false),
	
	IS_CREATIVE("options.isCreative", false, false),
	ENABLE_CHEATS("options.enableCheats", false, false),
	CRAFT_GUIDE("options.craftingGuide", false, false),
	
	CLEAR_WATERS("options.clearWaters", false, false),
	FOV("options.fov", true, false),
	GAMMA("options.gamma", true, false),
	COLOURED_ATHMOSPHERICS("options.colouredAthmospherics", false, false),
	DISPLAY_MODES("options.displayModes", false, false),
	MELTBUILD("options.meltBuild", false, true), 
	HAND("options.hand", false, true),
	THREADED_LIGHT("options.threaded_light", false, true)
	;

	private final boolean enumFloat;
	private final boolean enumBoolean;
	private final String enumString;

	public static EnumOptions getEnumOptions(int i0) {
		EnumOptions[] enumOptions1 = values();
		int i2 = enumOptions1.length;

		for(int i3 = 0; i3 < i2; ++i3) {
			EnumOptions enumOptions4 = enumOptions1[i3];
			if(enumOptions4.returnEnumOrdinal() == i0) {
				return enumOptions4;
			}
		}

		return null;
	}

	private EnumOptions(String caption, boolean isFloat, boolean isBoolean) {
		this.enumString = caption;
		this.enumFloat = isFloat;
		this.enumBoolean = isBoolean;
	}

	public boolean getEnumFloat() {
		return this.enumFloat;
	}

	public boolean getEnumBoolean() {
		return this.enumBoolean;
	}

	public int returnEnumOrdinal() {
		return this.ordinal();
	}

	public String getEnumString() {
		return this.enumString;
	}
}
