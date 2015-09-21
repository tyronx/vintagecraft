package at.tyron.vintagecraft.WorldProperties;

import java.util.ArrayList;
import java.util.Locale;

import at.tyron.vintagecraft.Interfaces.IStateEnum;
import net.minecraft.block.Block;
import net.minecraft.util.IStringSerializable;

// To add:
// Brass
// Chromium
// Tungsten
// Cobalt?
// Nickel?
// Stainless Steel (Steel, Chromium)
// High Speed Steel (Chromium, Tungsten, Cobalt?)
// Maraging Steel (Steel, Nickel, Cobalt, Titanium) 

public enum EnumMetal implements IStringSerializable, IStateEnum {
	
	COPPER 			(0, 1, 1084, 2.5f, true, true, true),
	TIN 			(1, 0, 232, 1.5f), 
	TINBRONZE	 	(2, 2, 950, 4f, true, true, true),
	
	IRON 			(3, 3, 1482, 5f, true, true, true),
	STEEL 			(4, 4, 1510, 7f, true, true, true),
	
	PALLADIUM 		(5, 5, 1555, 4.8f),
	PLATINUM 		(6, 6, 1770, 4.3f),
	TITANIUM 		(7, 5, 1668, 6f),
	CHROMIUM 		(8, 5, 1907, 8.5f),
	OSMIUM 			(9, 7, 3025, 7f),
	
	SILVER 			(10, 1, 961, 3f),
	GOLD 			(11, 1, 1063, 2.75f),
	
	URANIUM 		(12, 8, 1132, 9f), 
	ZINC 			(13, 0, 419, 2.5f), 
	BISMUTH 		(14, 0, 271, 2.2f),
	
	BISMUTHBRONZE	(15, 2, 1031, 2.1f, true, true, true),
	
	LEAD            (16, 0, 327, 1.5f)
	;
	
	
	
	
	
	public int id;
	public int tier;
	public int meltingpoint; // °C
	public float hardness; // Mohs Hardness
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
		this.hasAnvil = false;
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
		return name().toLowerCase(Locale.ROOT);
	}

	public String getNameUcFirst() {
		return name().toUpperCase(Locale.ROOT).substring(0, 1) + name().toLowerCase(Locale.ROOT).substring(1);
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
		if (this == TITANIUM) return (int) (meltingpoint * 0.9f);
		
		return (int) (meltingpoint * 0.6f);
	}
	
	
	
	// According to https://en.wikipedia.org/wiki/Incandescence
	public static int[] getIncandescenceColor(int temperature) {
		if (temperature < 520) return new int[]{0,0,0,0};
		
		return new int[]{
			Math.max(0, Math.min(255, ((temperature - 500) * 255) / 400)), 
			Math.max(0, Math.min(255, ((temperature - 900) * 255) / 200)), 
			Math.max(0, Math.min(255, ((temperature - 1100) * 255) / 200)), 
			Math.max(0, Math.min(96, (temperature - 525) / 2))
		};
	}
	
	public static float[] getIncandescenceColorAsColor4f(int temperature) {
		if (temperature < 500) return new float[]{0f,0f,0f,0f};
		
		return new float[]{
			Math.max(0f, Math.min(1, (temperature - 500) / 400f)), 
			Math.max(0f, Math.min(1, (temperature - 900) / 200f)), 
			Math.max(0f, Math.min(1, (temperature - 1100)/ 200f)), 
			Math.max(0f, Math.min(0.38f, (temperature - 525) / 2f))
		};
	}
	
	public static int getIncandescenceColorAsInt(int temperature) {
		int[] colors = getIncandescenceColor(temperature);
		
		return 
			(colors[3] << 24) |
			(colors[0] << 16) |
			(colors[1] << 8) |
			(colors[2])
		;
	}


	public static EnumMetal byString(String name) {
		for (EnumMetal metal : values()) {
			if (name.toLowerCase(Locale.ROOT).equals(metal.getName())) {
				return metal;
			}
		}
		return null;
	}
	
	
	
}

