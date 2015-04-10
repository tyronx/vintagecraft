package at.tyron.vintagecraft.WorldGen.Helper;

import at.tyron.vintagecraft.WorldProperties.EnumDepositOccurenceType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumMaterialDeposit;


public class DepositOccurence {
	public int weight; // How common the ore is 
	public int height = 2;
	
	public int mindepth;
	public int maxdepth;
	
	public EnumDepositOccurenceType type;
	
	public int untilyheight = 255;  // For type = ANY_UNTIL_Y_HEIGHT
	public float belowSealLevelRatio = 0f; // For type = MIXEDDEPTHS
	
	
	private DepositOccurence(EnumDepositOccurenceType type, int weight, int height, int mindepth, int maxdepth) {
		this(type, weight, height, mindepth, maxdepth, 255);
	}
	
	private DepositOccurence(EnumDepositOccurenceType type, int weight, int height, int mindepth, int maxdepth, int untilyheight) {
		this.type = type;
		this.weight = weight;
		this.height = height;
		this.mindepth = mindepth;
		this.maxdepth = maxdepth;
		this.untilyheight = untilyheight;
	}
	
	
	public static DepositOccurence noDeposit(int weight) {
		return new DepositOccurence(EnumDepositOccurenceType.NODEPOSIT, weight, 0, 0, 1);
	}
	
	
	public static DepositOccurence atSurface(int weight, int height) {
		return new DepositOccurence(EnumDepositOccurenceType.FOLLOWSURFACE, weight, height, 0, 1, 255);
	}

	public static DepositOccurence followSurface(int weight, int height, int depth, int untilyheight) {
		return new DepositOccurence(EnumDepositOccurenceType.FOLLOWSURFACE, weight, height, depth, depth+1, untilyheight);
	}

	
	public static DepositOccurence inDeposit(EnumMaterialDeposit deposit, int weight) {
		return new DepositOccurence(EnumDepositOccurenceType.INDEPOSIT, weight, 1, 0, 255);	
	}
	
	
	public static DepositOccurence anyRelativeDepth(int weight, int height, int mindepth, int maxdepth) {
		return anyRelativeDepth(weight, height, mindepth, maxdepth, 255);
	}
	
	public static DepositOccurence anyRelativeDepth(int weight, int height, int mindepth, int maxdepth, int untilyheight) {
		return new DepositOccurence(EnumDepositOccurenceType.ANYRELATIVEDEPTH, weight, height, mindepth, maxdepth, untilyheight);
	}
	
	public static DepositOccurence anyBelowSealevel(int weight, int height, int mindepth, int maxdepth) {
		return new DepositOccurence(EnumDepositOccurenceType.ANYBELOWSEALEVEL, weight, height, mindepth, maxdepth);
	}

	
	public static DepositOccurence mixedDepths(int weight, int height, int mindepth, int maxdepth, float belowsealevelratio) {
		DepositOccurence occ = new DepositOccurence(EnumDepositOccurenceType.MIXEDDEPTHS, weight, height, mindepth, maxdepth);
		occ.belowSealLevelRatio = belowsealevelratio;
		return occ;
	}

	

	
}
