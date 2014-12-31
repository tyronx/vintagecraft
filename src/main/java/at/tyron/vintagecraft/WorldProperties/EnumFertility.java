package at.tyron.vintagecraft.WorldProperties;

import net.minecraft.util.IStringSerializable;
import at.tyron.vintagecraft.interfaces.IEnumState;

public enum EnumFertility implements IEnumState, IStringSerializable {
	LOW (0, "lowf"),
	MEDIUM (1, "medf"),
	HIGH (2, "hif")
	;

	int meta;
	String shortname;
	
	private EnumFertility(int meta, String shortname) {
		this.meta = meta;
		this.shortname = shortname;
	}
	
	@Override
	public String getName() {
		return name().toLowerCase();
	}
	
	public String shortName() {
		return shortname;
	}
	
	@Override
	public int getMetaData() {
		return meta;
	}
	
	@Override
	public String getStateName() {
		return getName();
	}
	
	
	public static EnumFertility fromMeta(int meta) {
		for (EnumFertility fertility : EnumFertility.values()) {
			if (fertility.meta == meta) return fertility;
		}
		return null;
	}

}
