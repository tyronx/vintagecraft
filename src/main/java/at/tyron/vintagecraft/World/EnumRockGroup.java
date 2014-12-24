package at.tyron.vintagecraft.World;

public enum EnumRockGroup {
	SEDIMENTARY (new EnumCrustLayer[]{EnumCrustLayer.ROCK_1_THIN, EnumCrustLayer.ROCK_2_THIN, EnumCrustLayer.ROCK_3_THIN}),
	SEDIMENTARY2 (new EnumCrustLayer[]{EnumCrustLayer.ROCK_2_THIN, EnumCrustLayer.ROCK_3_THIN}),
	METAMORPHIC (new EnumCrustLayer[]{EnumCrustLayer.ROCK_1_THIN, EnumCrustLayer.ROCK_2_THIN, EnumCrustLayer.ROCK_3_THIN, EnumCrustLayer.ROCK_4_MEDIUM, EnumCrustLayer.ROCK_6_THIN}),
	IGNEOUS_INTRUSIVE (new EnumCrustLayer[]{EnumCrustLayer.ROCK_5_LARGE, EnumCrustLayer.ROCK_6_THIN, EnumCrustLayer.ROCK_7_LARGE}),
	IGNEOUS_EXTRUSIVE  (new EnumCrustLayer[]{EnumCrustLayer.ROCK_2_THIN, EnumCrustLayer.ROCK_3_THIN, EnumCrustLayer.ROCK_4_MEDIUM, EnumCrustLayer.ROCK_5_LARGE, EnumCrustLayer.ROCK_6_THIN})
	;
	
	
	
	public EnumCrustLayer []crustlayers;
	
	private EnumRockGroup(EnumCrustLayer []crustlayers) {
		this.crustlayers = crustlayers;
	}
	


    
}
