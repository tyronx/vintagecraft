package at.tyron.vintagecraft.WorldProperties;

import at.tyron.vintagecraft.Interfaces.IStateEnum;
import net.minecraft.block.Block;
import net.minecraft.util.IStringSerializable;

public enum EnumMetal implements IStringSerializable, IStateEnum {
	
	COPPER (0, 1084, 2.5f),
	TIN (1, 232, 1.5f), 
	TINBRONZE (2, 950, 4f),
	
	IRON (3, 1482, 5f),
	STEEL (4, 1510, 7f),
	
	PALLADIUM (5, 1555, 4.8f),
	PLATINUM (6, 1770, 4.3f),
	RHODIUM (7, 1965, 6.5f),
	IRIDIUM (8, 2450, 6.5f),
	OSMIUM (9, 3025, 7f),
	
	SILVER (10, 961, 3f),
	GOLD (11, 1063, 2.75f),
	
	URANIUM (12, 1132, 9f), 
	ZINC (13, 419, 2.5f), 
	BISMUTH (14, 271, 2.2f),
	
	BISMUTHBRONZE (15, 1031, 2.1f)
	;
	
	
	
	
	
	public int id;
	public int meltingpoint; // °C
	public float hardness; 
	
	private EnumMetal(int id, int meltingpoint, float hardness) {
		this.id = id;
		this.meltingpoint = meltingpoint;
		this.hardness = hardness;
	}

	public static EnumMetal byId(int id) {
		for (EnumMetal metal : EnumMetal.values()) {
			if (metal.id == id) {
				return metal;
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return name().toLowerCase();
	}
	
	public String getCode() {
		return getName().substring(0, 2);
	}
	
	public int getId() {
		return id;
	}

	@Override
	public int getMetaData(Block block) {
		return id;
	}

	@Override
	public String getStateName() {
		return getName();
	}

	@Override
	public void init(Block block, int meta) {
		// TODO Auto-generated method stub
		
	}
}

