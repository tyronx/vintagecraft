package at.tyron.vintagecraft.WorldProperties;

public enum EnumDepositOccurenceType {
	NODEPOSIT,
	FOLLOWSURFACE,				// Follows y-Coordinate of the landscape
	INDEPOSIT,  				// Within another deposit
	
	ANYBELOWSEALEVEL,			// Anywhere below sealevel. Mindepth = fixed coordinate below sealevel
	ANYRELATIVEDEPTH,			// Anywhere below surface certain y-level. Mindepth = relative coordinate below surface
	
	//GRADIENTBELOWSEALEVEL,		// Same as ANYBELOWSEALEVEL but deposit weight is proportional to the ore depth (=the deeper the more common)
	//GRADIENTRELATIVEDEPTH,		// Same as ANYRELATIVEDEPTH but deposit weight is proportional to the ore depth (=the deeper the more common)
	
	MIXEDDEPTHS					// x Part below sealave, y  Part below relative depth
	
	
}
