package at.tyron.vintagecraft.World;

import net.minecraft.util.IStringSerializable;

public enum EnumOreType implements IStringSerializable {
	NATIVECOPPER ("NativeCopper"),
	NATIVEGOLD ("NativeGold"),
	LIMONITE ("Limonite"),
	COAL ("Coal"),
	LIGNITE ("Lignite"),
	;
	

	String name;
	
	private EnumOreType(String name) {
		this.name = name;
	}
	

	@Override
	public String getName() {
		return name;
	}
	
}
