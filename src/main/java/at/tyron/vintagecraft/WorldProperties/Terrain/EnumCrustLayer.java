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
	
	
	// Size of transition between two layers in Blocks  
	static int transitionSize = 2;
	
	boolean valid(int []climate, int y, Random rand) {
		int distycoord = maxy - y;
		
		int temperature = VCraftWorld.deScaleTemperature(climate[0]);
		
		int tempdistance = tmin(
			Math.abs(mintemperature_descaled - temperature),
			Math.abs(temperature - maxtemperature_descaled),
			(temperature > mintemperature_descaled + transitionSize && temperature < maxtemperature_descaled - transitionSize) ? 0 : 999 
		);
		int fertdistance = Math.max(0, minfertility + transitionSize - climate[1]);
		int raindistance = Math.max(0, climate[2] - maxrain - transitionSize);
		
		
		int totalDistance = tempdistance + raindistance + fertdistance;

		
		return
			(totalDistance == 0 || (totalDistance < 4 && rand.nextInt(1 + (2 * totalDistance)) == 0))
			&& distycoord >= 0
			&& rand.nextFloat() <= placementchance;
	}
	
	
	
	int tmin(int n1, int n2, int n3) {
		return Math.min(n3, Math.min(n1, n2));
	}

}
