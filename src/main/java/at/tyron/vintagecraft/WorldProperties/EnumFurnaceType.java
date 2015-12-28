package at.tyron.vintagecraft.WorldProperties;

public enum EnumFurnaceType {
	BLOOMERY (1500),
	BLASTFURNACE (1800)
	;
	
	
	public int maxTemperature;
	
	EnumFurnaceType(int maxTemperature) {
		this.maxTemperature = maxTemperature;
	}
}
