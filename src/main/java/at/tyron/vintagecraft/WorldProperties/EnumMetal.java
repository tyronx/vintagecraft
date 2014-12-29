package at.tyron.vintagecraft.WorldProperties;

import net.minecraft.util.IStringSerializable;

public enum EnumMetal implements IStringSerializable {
	
	COPPER (0, 1084, 2.5f),
	TIN (1, 232, 1.5f), 
	BRONZE (2, 950, 4f),
	
	IRON (3, 1482, 5f),
	STEEL (4, 1510, 7f),
	
	PALLADIUM (5, 1555, 4.8f),
	PLATINUM (6, 1770, 4.3f),
	RHODIUM (7, 1965, 6.5f),
	IRIDIUM (8, 2450, 6.5f),
	OSMIUM (9, 3025, 7f),
	
	SILVER (10, 961, 3f),
	GOLD (11, 1063, 2.75f),
	
	URANIUM (12, 1132, 9f)
	
	;
	
	
	
	
	
	public int id;
	public int meltingpoint; // °C
	public float hardness; 
	
	private EnumMetal(int id, int meltingpoint, float hardness) {
		this.id = id;
		this.meltingpoint = meltingpoint;
		this.hardness = hardness;
	}

	public static EnumMetal byId(int meta) {
		for (EnumMetal metal : EnumMetal.values()) {
			if (metal.id == meta) {
				return metal;
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return name().toLowerCase();
	}
}

