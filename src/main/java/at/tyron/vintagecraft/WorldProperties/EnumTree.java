package at.tyron.vintagecraft.WorldProperties;

import at.tyron.vintagecraft.interfaces.IEnumState;
import net.minecraft.util.IStringSerializable;

public enum EnumTree implements IEnumState, IStringSerializable {
	MOUNTAINDOGWOOD (0),
	
	;
	
	
	public int id;
	
	private EnumTree(int id) {
		this.id = id;
	}

	@Override
	public int getMetaData() {
		return id;
	}

	@Override
	public String getStateName() {
		return name().toLowerCase();
	}
	
	
	@Override
	public String getName() {
		return name().toLowerCase();
	}

	public static EnumTree byId(int meta) {
		for (EnumTree tree : EnumTree.values()) {
			if (tree.id == meta) return tree;
		}
		return null;
	}
}
