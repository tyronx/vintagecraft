package at.tyron.vintagecraft.BlockClass;

import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;

public class OreClassEntry<E> extends BlockClassEntry<E> {
	EnumRockType rocktype;
	EnumOreType oretype;


	public OreClassEntry(IStateEnum key, EnumRockType rocktype, EnumOreType oretype) {
		super(key);
		this.rocktype = rocktype;
		this.oretype = oretype;
	}

}
