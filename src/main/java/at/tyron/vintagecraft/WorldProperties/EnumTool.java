package at.tyron.vintagecraft.WorldProperties;

public enum EnumTool {
	PICKAXE (0, true),
	AXE (1, true),
	SHOVEL (2, true),
	SWORD (3, true),
	//CHISEL (4),
	HOE (5, true),
	SAW (6, false)
	;
	
	public int meta;
	public boolean canBeMadeFromStone;
	
	private EnumTool(int meta, boolean canBeMadeFromStone) {
		this.meta = meta;
		this.canBeMadeFromStone = canBeMadeFromStone;
	}
	
	public static EnumTool fromMeta(int meta) {
		for (EnumTool tool : EnumTool.values()) {
			if (meta == tool.meta) return tool;
		}
		return null;
	}
	
	public String getName() {
		return name().toLowerCase();
	}
}
