package at.tyron.vintagecraft.interfaces;

public class EnumStateImplementation implements IEnumState {
	int metadata;
	String statename;
	
	
	public EnumStateImplementation(int meta, String name) {
		this.metadata = meta;
		this.statename = name;
	}
		
	@Override
	public int getMetaData() {
		return metadata;
	}

	@Override
	public String getStateName() {
		return statename;
	}

}
