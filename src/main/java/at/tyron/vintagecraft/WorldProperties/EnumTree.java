package at.tyron.vintagecraft.WorldProperties;

import at.tyron.vintagecraft.interfaces.IEnumState;
import net.minecraft.util.IStringSerializable;

public enum EnumTree implements IEnumState, IStringSerializable {
	MOUNTAINDOGWOOD (0),
	
	;
	
	
	public int meta;
	
	private EnumTree(int meta) {
		this.meta = meta;
	}

	@Override
	public int getMetaData() {
		return meta;
	}

	@Override
	public String getStateName() {
		return name().toLowerCase();
	}
	
	
	@Override
	public String getName() {
		return name().toLowerCase();
	}

	public static EnumTree byMeta(int meta) {
		for (EnumTree tree : EnumTree.values()) {
			if (tree.meta == meta) return tree;
		}
		return null;
	}
}
