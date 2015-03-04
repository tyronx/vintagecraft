package at.tyron.vintagecraft.WorldProperties;

import net.minecraft.util.IStringSerializable;

public enum EnumOreType implements IStringSerializable {
	NATIVECOPPER (4, "NativeCopper"),
	NATIVEGOLD (6, "NativeGold"),
	LIMONITE (5, "Limonite"),
	BITUMINOUSCOAL (3, "BituminousCoal"),
	LIGNITE (2, "Lignite"),
	REDSTONE (7, "Redstone"),
	CASSITERITE (8, "Cassiterite")
	;
	
	public int meta;
	String name;
	
	private EnumOreType(int meta, String name) {
		this.meta = meta;
		this.name = name;
	}
	

	@Override
	public String getName() {
		return name.toLowerCase();
	}
	
}
