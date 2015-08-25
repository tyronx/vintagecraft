package at.tyron.vintagecraft.WorldProperties.Terrain;

import java.util.Random;

import at.tyron.vintagecraft.World.VCraftWorld;

public enum EnumCrustLayer {
	/* Top Soil Layer */
	L1_TOPSOIL			(3, 40, -15, 99, 230, EnumCrustType.TOPSOIL),
	L1_SAND				(2, 20,  10, 99, 40, 180, EnumCrustType.SAND),
	L1_GRAVEL			(3,  5,  -8, 99, 60, 200, EnumCrustType.GRAVEL),
	L1_SNOW				(99, 0, -30,-16, 255, EnumCrustType.SNOW),
	
	/* Sub Soil Layer */
	L2_SUBSOIL			(1, 60, -15, 99, 170, EnumCrustType.SUBOIL),
	L2_SAND				(1, 20,  13, 99, 40, 170, EnumCrustType.SAND),
	L2_ICE				(99, 0, -30,-20, 170, EnumCrustType.ICE),
	
	L3_SUBSOIL			(1, 60, -10, 99, 170, 255, 0.5f, EnumCrustType.SUBOIL),
	
	
	/* Regolith Layer */
	L3_REGOLITH			(1, 60, -10, 30, 160, EnumCrustType.REGOLITH),
	L3_SAND				(2, 20,  16, 30, 40, 180, EnumCrustType.SAND),
	L3_GRAVEL			(2, 10,  -2, 99, 60, 200, EnumCrustType.GRAVEL),
	L3_PACKEDICE 		(99, 0, -99,-25, 255, EnumCrustType.PACKEDICE),
	
	
	L4_REGOLITH			(1, 60, -10, 99, 160, 255, 0.5f, EnumCrustType.REGOLITH),
	
	
	/* Below Regolith */
	L5_PACKEDICE		(99,  0, -99, -26, 255, EnumCrustType.PACKEDICE),
	L6_PACKEDICE		(99,  0, -99, -27, 255, EnumCrustType.PACKEDICE),
	L7_PACKEDICE		(99,  0, -99, -28, 255, EnumCrustType.PACKEDICE)
	
	
	//L5_ROCK			    (999, 99,  0, -99, 99, 255, EnumCrustType.ROCK)
	
	
	;
	
	
	
	
	int maxstepness;
	int minfertility;
	int mintemperature;
	int maxtemperature;
	int maxrain;
	int maxy;
	float placementchance;
	
	EnumCrustType blocktype;

	int mintemperature_descaled;
	int maxtemperature_descaled;

	
	private EnumCrustLayer(int maxstepness, int minfertility, int mintemperature, int maxtemperature, int maxy, EnumCrustType blocktype) {
		this (maxstepness, minfertility, mintemperature, maxtemperature, maxy, 255, 1f, blocktype);
	}

	private EnumCrustLayer(int maxstepness, int minfertility, int mintemperature, int maxtemperature, int maxrain, int maxy, EnumCrustType blocktype) {
		this (maxstepness, minfertility, mintemperature, maxtemperature, maxy, maxrain, 1f, blocktype);
	}
	
	private EnumCrustLayer(int maxstepness, int minfertility, int mintemperature, int maxtemperature, int maxy, int maxrain, float placementchance, EnumCrustType blocktype) {
		this.maxstepness = maxstepness;
		this.mintemperature = mintemperature;
		this.maxtemperature = maxtemperature;
		this.minfertility = minfertility;
		this.maxrain = maxrain;
		this.maxy = maxy;
		this.blocktype = blocktype;
		this.placementchance = placementchance;
		
		mintemperature_descaled = VCraftWorld.deScaleTemperature(mintemperature);
		maxtemperature_descaled = VCraftWorld.deScaleTemperature(maxtemperature);
		
	}
	

	static int transitionSize = 2;
	
	boolean valid(int []climate, int y, Random rand) {
		/*int distfert = climate[1] - minfertility;
		int disttempmin = climate[0] - mintemperature;
		int disttempmax = maxtemperature - climate[0];
		int distrainmax = maxrain - climate[2];*/
		int distycoord = maxy - y;
		
		
		int temp = VCraftWorld.deScaleTemperature(climate[0]);
		/*
		int lowertempdistance = mintemperature_descaled + transitionSize - temp;
		int uppertempdistance = temp - maxtemperature_descaled - transitionSize; 
		
		int tempdistance = Math.min(
				lowertempdistance < -5 ? 999 : Math.max(0, lowertempdistance),
				uppertempdistance < -5 ? 999 : Math.max(0, uppertempdistance)
		);
		
		*/
		int tempdistance = tmin(
			Math.abs(mintemperature_descaled - temp),
			Math.abs(temp - maxtemperature_descaled),
			(temp > mintemperature_descaled + transitionSize && temp < maxtemperature_descaled - transitionSize) ? 0 : 999 
		);
		
		int fertdistance = Math.max(0, minfertility + transitionSize - climate[1]);
		
		int raindistance = Math.max(0, climate[2] - maxrain - transitionSize);
		

		
		int distance = tempdistance + raindistance + fertdistance;

		
		//if (this == L5_PACKEDICE) System.out.println(temp + " - " + maxtemperature_descaled + " - " + transitionSize);  // System.out.println("for temp = " + climate[0] +"    " + tempdistance + " + " + raindistance + " + " + fertdistance);
		
		return
			(distance == 0 || (distance < 4 && rand.nextInt(1 + (2 * distance)) == 0))
			&& distycoord >= 0
			&& rand.nextFloat() <= placementchance;
		
	}
	
	
	
	int tmin(int n1, int n2, int n3) {
		return Math.min(n3, Math.min(n1, n2));
	}

}
