package at.tyron.vintagecraft.WorldProperties;


public enum EnumCrustLayerGroup {
	TOPSOIL (1, 1, new EnumCrustLayer[]{EnumCrustLayer.TOPSOIL}),
	SUBSOIL (1, 2, new EnumCrustLayer[]{EnumCrustLayer.SUBSOIL}),
	REGOLITH (1, 2, new EnumCrustLayer[]{EnumCrustLayer.REGOLITH}),
	
	SEDIMENTARY (0, 10, new EnumCrustLayer[]{EnumCrustLayer.ROCK_1, EnumCrustLayer.ROCK_2, EnumCrustLayer.ROCK_3}),
	SEDIMENTARY2 (0, 4, new EnumCrustLayer[]{EnumCrustLayer.ROCK_2, EnumCrustLayer.ROCK_3}),
	METAMORPHIC (0, 40, new EnumCrustLayer[]{EnumCrustLayer.ROCK_1, EnumCrustLayer.ROCK_2, EnumCrustLayer.ROCK_3, EnumCrustLayer.ROCK_4, EnumCrustLayer.ROCK_6}),
	IGNEOUS_INTRUSIVE (0, 80, new EnumCrustLayer[]{EnumCrustLayer.ROCK_5, EnumCrustLayer.ROCK_6, EnumCrustLayer.ROCK_7}),
	IGNEOUS_EXTRUSIVE  (0, 80, new EnumCrustLayer[]{EnumCrustLayer.ROCK_2, EnumCrustLayer.ROCK_3, EnumCrustLayer.ROCK_4, EnumCrustLayer.ROCK_5, EnumCrustLayer.ROCK_6})
	;
	
	
	
	public int minThickness;
	public int maxThickness;
	
	public EnumCrustLayer []crustlayers;
	
	private EnumCrustLayerGroup(int minThickness, int maxThickness, EnumCrustLayer []crustlayers) {
		this.crustlayers = crustlayers;
		this.minThickness = minThickness;
		this.maxThickness = maxThickness;
	}
	


    
}
