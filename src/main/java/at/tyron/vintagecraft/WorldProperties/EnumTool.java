package at.tyron.vintagecraft.WorldProperties;

import java.util.Locale;

public enum EnumTool {
	PICKAXE (0, true, true),
	AXE (1, true, true),
	SHOVEL (2, true, true),
	SWORD (3, false, true),
	//CHISEL (4),
	HOE (5, true, true),
	SAW (6, false, true),
	SHEARS (7, false, false),
	HAMMER (8, true, true),
	CARPENTERSTOOLSET (9, false, false)
	;
	
	public int meta;
	public boolean canBeMadeFromStone;
	public boolean requiresWoodenHandle;
	
	private EnumTool(int meta, boolean canBeMadeFromStone, boolean requiresWoodenHandle) {
		this.meta = meta;
		this.canBeMadeFromStone = canBeMadeFromStone;
		this.requiresWoodenHandle = requiresWoodenHandle;
	}
	
	public static EnumTool fromMeta(int meta) {
		for (EnumTool tool : EnumTool.values()) {
			if (meta == tool.meta) return tool;
		}
		return null;
	}
	
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
	
	
	public boolean canBeMadeOf(String material) {
		if (this == CARPENTERSTOOLSET) {
			return material.equals("iron") || material.equals("steel") || material.equals("tinbronze") || material.equals("bismuthbronze"); 
		}
		return true;
	}
}
