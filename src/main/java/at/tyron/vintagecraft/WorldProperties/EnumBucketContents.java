package at.tyron.vintagecraft.WorldProperties;

import java.util.Locale;

public enum EnumBucketContents {
	EMPTY,
	WATER
	
	;
	
	
	
	public int getId() {
		return ordinal();
	}
	
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
	
	public static EnumBucketContents byId(int id) {
		return values()[id];
	}
}
