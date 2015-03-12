package at.tyron.vintagecraft.WorldProperties;

import java.util.Random;

public enum EnumTallGrassGroup {

	VERYSHORT 				(0, 30, new EnumTallGrass[] { EnumTallGrass.VERYSHORT, EnumTallGrass.SHORT, EnumTallGrass.MEDIUM, EnumTallGrass.LONG }, new int[] { 80, 20, 5, 1}),
	SHORT 					(31, 60, new EnumTallGrass[] { EnumTallGrass.VERYSHORT, EnumTallGrass.SHORT, EnumTallGrass.MEDIUM, EnumTallGrass.LONG }, new int[] { 20, 80, 10, 1 }),
	MEDIUM 					(61, 90, new EnumTallGrass[] { EnumTallGrass.VERYSHORT, EnumTallGrass.SHORT, EnumTallGrass.MEDIUM, EnumTallGrass.LONG }, new int[] { 10, 15, 80, 3 }),
	LONG 					(91, 120, new EnumTallGrass[] { EnumTallGrass.VERYSHORT, EnumTallGrass.SHORT, EnumTallGrass.MEDIUM, EnumTallGrass.LONG, EnumTallGrass.VERYLONG }, new int[] { 1, 3, 10, 80, 5 }),
	VERYLONG 				(121, 150, new EnumTallGrass[] { 
								EnumTallGrass.LONG,
								EnumTallGrass.VERYLONG, 
								EnumTallGrass.VERYLONG_CORNFLOWER, 
								EnumTallGrass.VERYLONG_CORNFLOWER2, 
								EnumTallGrass.VERYLONG_OXEYEDAISY, 
								/*EnumTallGrass.VERYLONG_FORGETMENOT, 
								EnumTallGrass.VERYLONG_FORGETMENOT2, 
								EnumTallGrass.VERYLONG_FORGETMENOT3,*/
								EnumTallGrass.VERYLONG_FLOWERING
							}, 
							new int[] { 25, 250, 1, 1, 1, 35}),
	VERYLONG_FLOWERING 		(151, 185, new EnumTallGrass[] { 
								EnumTallGrass.VERYLONG,
								EnumTallGrass.VERYLONG_FLOWERING, 
								EnumTallGrass.VERYLONG_CORNFLOWER, 
								EnumTallGrass.VERYLONG_CORNFLOWER2, 
								EnumTallGrass.VERYLONG_OXEYEDAISY }, new int[] { 60, 125, 1, 1, 1 }),
	FERN					(186, 255, new EnumTallGrass[] {
								EnumTallGrass.VERYLONG,
								EnumTallGrass.LONG,
								EnumTallGrass.FERN, 
								EnumTallGrass.FERN2, 
								EnumTallGrass.FERN3 
							}, new int[] { 2, 2, 100, 100, 100 })
	
	;
	
	
	
	//int averagefertility;
	int minfertility;
	int maxfertility;
	EnumTallGrass[] grasses;
	int[] weights;
	int totalweight;
	
	
	
	
	private EnumTallGrassGroup(int minfertility, int maxfertility, EnumTallGrass[] grasses, int[] weights) {
		this.minfertility = minfertility;
		this.maxfertility = maxfertility;
		//this.averagefertility = averagefertility;
		this.grasses = grasses;
		this.weights = weights;
		
		totalweight = 0;
		for (int i = 0; i < weights.length; i++) {
			totalweight += weights[i];
			
			weights[i] = totalweight; 
		}
	}
	
	
	public static EnumTallGrass fromClimate(int fertility, int temperature, Random rand) {
		if (fertility < 150) {
			fertility += Math.max(0, temperature);
		}
		
		for (EnumTallGrassGroup group : values()) {
			if (group.minfertility <= fertility && group.maxfertility > fertility) {
				int rnd = rand.nextInt(group.totalweight);
				
				for (int i = 0; i < group.weights.length; i++) {
					if (rnd < group.weights[i]) return group.grasses[i];
				}
			}
		}
		return null;
	}
	
	
	public static float getDensity(int forestdensity, int rain, int temperature) {
		float density = (255 - forestdensity) / 50;
		
		if (rain < 20) return 0;
		
		return density / (Math.max(1f, (temperature - 20 - rain/30)));
	}
	
	
	
	

}
