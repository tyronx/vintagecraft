package at.tyron.vintagecraft.WorldProperties.Terrain;


public enum EnumRockGroup {
	SEDIMENTARY (0.75f),
	METAMORPHIC (1f),
	IGNEOUS_INTRUSIVE  (1.25f),
	IGNEOUS_EXTRUSIVE  (1.1f), 
	SPECIAL (1f);					// Kimberlite

	float hardness;
	
	private EnumRockGroup(float hardness) {
		this.hardness = hardness;
	}
	
	public float getHardNess() {
		return hardness;
	}   
}
