package at.tyron.vintagecraft.WorldProperties;

public enum EnumDepositOccurenceType {
	NODEPOSIT,
	INTOPSOIL,					// Within Topsoil
	INDEPOSIT,  				// Within another deposit
	
	ANYBELOWSEALEVEL,			// Anywhere below sealevel. Mindepth = fixed coordinate below sealevel
	ANYRELATIVEDEPTH,			// Anywhere until certain y-level. Mindepth = relative coordinate below surface
	
	MIXEDDEPTHS					// x Part below sealave, y  Part below relative depth
}
