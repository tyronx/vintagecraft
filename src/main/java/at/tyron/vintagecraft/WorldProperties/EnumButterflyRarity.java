package at.tyron.vintagecraft.WorldProperties;

public enum EnumButterflyRarity {
	ExtremelyCommon (0.5f),
	VeryCommon (0.25f),
	Common (0.12f),
	Uncommon (0.04f),
	Rare (0.028f),
	Epic (0.014f),
	Extinct (0.011f),
	Unknown (0f), 
	Event (0f)
	;
	
	
	float chance;
	
	private EnumButterflyRarity(float chance) {
		this.chance = chance;
	}
}
