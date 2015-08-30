package at.tyron.vintagecraft.WorldProperties;

import java.util.Locale;

public enum EnumTool {
	PICKAXE (0, true, true, true),
	AXE (1, true, true, true),
	SHOVEL (2, true, true, true),
	SWORD (3, false, true, true),
	//CHISEL (4),
	HOE (5, true, true, true),
	SAW (6, false, true, true),
	SHEARS (7, false, false, true),
	HAMMER (8, true, true, true),
	CARPENTERSTOOLSET (9, false, false, true),
	SICKLE (9, false, true, true),
	;
	
	public int meta;
	public boolean canBeMadeFromStone;
	public boolean requiresWoodenHandle;
	public boolean isUpgradable;
	
	private EnumTool(int meta, boolean canBeMadeFromStone, boolean requiresWoodenHandle, boolean isUpgradable) {
		this.meta = meta;
		this.canBeMadeFromStone = canBeMadeFromStone;
		this.requiresWoodenHandle = requiresWoodenHandle;
		this.isUpgradable = isUpgradable;
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
