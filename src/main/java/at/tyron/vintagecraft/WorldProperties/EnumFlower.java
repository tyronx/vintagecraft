package at.tyron.vintagecraft.WorldProperties;

import at.tyron.vintagecraft.interfaces.IEnumState;
import net.minecraft.util.IStringSerializable;

public enum EnumFlower implements IStringSerializable, IEnumState {
	
	// http://en.wikipedia.org/wiki/Asclepias_tuberosa
	ORANGEBUTTERFLYMILKWEED (0),
	PURPLEBUTTERFLYMILKWEED (1),
	CATMINT (2),
	CALENDULA (3),
	CORNFLOWER (4),
	CORNFLOWER2 (5)
	
	;
	
	int meta;
	
	private EnumFlower (int meta) {
		this.meta = meta;
	}
	
	
	public static EnumFlower fromMeta(int meta) {
		for (EnumFlower flower : EnumFlower.values()) {
			if (meta == flower.meta) return flower;
		}
		return null;
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
}
