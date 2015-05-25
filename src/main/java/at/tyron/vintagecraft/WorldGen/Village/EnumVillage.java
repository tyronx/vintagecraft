package at.tyron.vintagecraft.WorldGen.Village;

public enum EnumVillage {

	DEFAULT
	
	;
	
	DynVillageGen generator;
	
	public void setGenerator(DynVillageGen generator) {
		this.generator = generator;
	}
	
	public DynVillageGen getGenerator() {
		return generator;
	}
	
	
}
