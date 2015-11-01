package at.tyron.vintagecraft.WorldProperties;

import java.util.Locale;

public enum EnumObjectCategory {
	Misc,
	Carpentry,
	Flora,
	Mechanics,
	Metalworking,
	Stoneworking,
	Terrafirma
	
	;
	
	
	
	public String getFolderPart() {
		if (this != Misc) {
			return this.name().toLowerCase(Locale.ROOT) + "/";
		}
		return "";
	}
}
