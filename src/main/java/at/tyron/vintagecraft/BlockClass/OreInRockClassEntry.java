package at.tyron.vintagecraft.BlockClass;

import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;

public class OreInRockClassEntry<E> extends BlockClassEntry<E> {
	EnumRockType rocktype;
	EnumOreType oretype;


	public OreInRockClassEntry(IStateEnum key, EnumRockType rocktype, EnumOreType oretype) {
		super(key);
		this.rocktype = rocktype;
		this.oretype = oretype;
	}

}
