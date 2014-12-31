package at.tyron.vintagecraft.WorldProperties;

import net.minecraft.util.IStringSerializable;
import at.tyron.vintagecraft.interfaces.IEnumState;

public enum EnumGrass implements IEnumState, IStringSerializable {

	NORMAL (0),
	OXEYEDAISY (1),  // normal size grass
	CORNFLOWER (2),  // normal size grass
	CORNFLOWER2 (3)  // normal size grass
	
	
	;
	
	int meta;
	
	private EnumGrass (int meta) {
		this.meta = meta;
	}

	
	@Override
	public String getName() {
		return name().toLowerCase();
	}

	@Override
	public int getMetaData() {
		return meta;
	}

	@Override
	public String getStateName() {
		return getName();
	}


	public static EnumGrass fromMeta(int meta) {
		for (EnumGrass grass : EnumGrass.values()) {
			if (meta == grass.meta) return grass;
		}
		return null;
	}
	
	
}
