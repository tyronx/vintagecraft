package at.tyron.vintagecraft.WorldProperties;


public class DepositOccurence {
	public int weight; // How common the ore is 
	public int height = 2;
	
	public int mindepth;
	public int maxdepth;
	
	public DepositOccurenceType type;
	
	public int untilyheight = 255;  // For type = ANY_UNTIL_Y_HEIGHT
	public float belowSealLevelRatio = 0f; // For type = MIXEDDEPTHS
	
	
	private DepositOccurence(DepositOccurenceType type, int weight, int height, int mindepth, int maxdepth) {
		this(type, weight, height, mindepth, maxdepth, 255);
	}
	
	private DepositOccurence(DepositOccurenceType type, int weight, int height, int mindepth, int maxdepth, int untilyheight) {
		this.type = type;
		this.weight = weight;
		this.height = height;
		this.mindepth = mindepth;
		this.maxdepth = maxdepth;
		this.untilyheight = untilyheight;
	}
	
	
	public static DepositOccurence noDeposit(int weight) {
		return new DepositOccurence(DepositOccurenceType.NODEPOSIT, weight, 0, 0, 1);
	}
	
	
	public static DepositOccurence inTopSoil(int weight, int height) {
		return new DepositOccurence(DepositOccurenceType.INTOPSOIL, weight, height, 0, 1, 255);
	}

	public static DepositOccurence inTopSoil(int weight, int height, int untilyheight) {
		return new DepositOccurence(DepositOccurenceType.INTOPSOIL, weight, height, 0, 1, untilyheight);
	}

	
	public static DepositOccurence inDeposit(EnumMaterialDeposit deposit, int weight) {
		return new DepositOccurence(DepositOccurenceType.INDEPOSIT, weight, 1, 0, 255);	
	}
	
	
	public static DepositOccurence anyRelativeDepth(int weight, int height, int mindepth, int maxdepth) {
		return anyRelativeDepth(weight, height, mindepth, maxdepth, 255);
	}
	
	public static DepositOccurence anyRelativeDepth(int weight, int height, int mindepth, int maxdepth, int untilyheight) {
		return new DepositOccurence(DepositOccurenceType.ANYRELATIVEDEPTH, weight, height, mindepth, maxdepth, untilyheight);
	}
	
	public static DepositOccurence anyBelowSealevel(int weight, int height, int mindepth, int maxdepth) {
		return new DepositOccurence(DepositOccurenceType.ANYBELOWSEALEVEL, weight, height, mindepth, maxdepth);
	}

	
	public static DepositOccurence mixedDepths(int weight, int height, int mindepth, int maxdepth, float belowsealevelratio) {
		DepositOccurence occ = new DepositOccurence(DepositOccurenceType.MIXEDDEPTHS, weight, height, mindepth, maxdepth);
		occ.belowSealLevelRatio = belowsealevelratio;
		return occ;
	}

	

	
}
