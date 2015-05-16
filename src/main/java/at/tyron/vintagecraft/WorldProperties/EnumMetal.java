package at.tyron.vintagecraft.WorldProperties;

import java.util.ArrayList;

import at.tyron.vintagecraft.Interfaces.IStateEnum;
import net.minecraft.block.Block;
import net.minecraft.util.IStringSerializable;

public enum EnumMetal implements IStringSerializable, IStateEnum {
	
	COPPER 			(0, 1, 1084, 2.5f, true, true, true),
	TIN 			(1, 0, 232, 1.5f), 
	TINBRONZE	 	(2, 2, 950, 4f, true, true, true),
	
	IRON 			(3, 3, 1482, 5f, true, true, true),
	STEEL 			(4, 4, 1510, 7f),
	
	PALLADIUM 		(5, 5, 1555, 4.8f),
	PLATINUM 		(6, 6, 1770, 4.3f),
	RHODIUM 		(7, 5, 1965, 6.5f),
	IRIDIUM 		(8, 5, 2450, 6.5f),
	OSMIUM 			(9, 7, 3025, 7f),
	
	SILVER 			(10, 1, 961, 3f),
	GOLD 			(11, 1, 1063, 2.75f),
	
	URANIUM 		(12, 8, 1132, 9f), 
	ZINC 			(13, 0, 419, 2.5f), 
	BISMUTH 		(14, 0, 271, 2.2f),
	
	BISMUTHBRONZE	(15, 2, 1031, 2.1f, true, true, true)
	;
	
	
	
	
	
	public int id;
	public int tier;
	public int meltingpoint; // °C
	public float hardness; 
	public boolean hasTools;
	public boolean hasArmor;
	public boolean hasAnvil;
	
	private EnumMetal(int id, int tier, int meltingpoint, float hardness) {
		this.id = id;
		this.tier = tier;
		this.meltingpoint = meltingpoint;
		this.hardness = hardness;
		this.hasTools = false;
		this.hasArmor = false;
	}

	
	private EnumMetal(int id, int tier, int meltingpoint, float hardness, boolean hasTools, boolean hasArmor, boolean hasAnvil) {
		this.id = id;
		this.tier = tier;
		this.meltingpoint = meltingpoint;
		this.hardness = hardness;
		this.hasTools = hasTools;
		this.hasArmor = hasArmor;
		this.hasAnvil = hasAnvil;
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
	}
	
	public static EnumMetal[] anvilValues() {
		ArrayList<EnumMetal> metals = new ArrayList<EnumMetal>();
		
		for (EnumMetal metal : values()) {
			if (metal.hasAnvil) metals.add(metal);
		}
		
		return metals.toArray(new EnumMetal[0]);
	}
	

	
	public int getMaxWorkingTemperature() {
		return meltingpoint - 10;
	}
	
	// The lower limit of the hot working temperature is determined by its recrystallization temperature. As a guideline, the lower limit of the hot working temperature of a material is 60% its melting temperature (on an absolute temperature scale).
	// http://en.wikipedia.org/wiki/Hot_working
	public int getMinWorkableTemperature() {
		return (int) (meltingpoint * 0.6f);
	}
	
	
}

