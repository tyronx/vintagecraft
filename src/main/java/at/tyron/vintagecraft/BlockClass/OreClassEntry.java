package at.tyron.vintagecraft.BlockClass;

import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.interfaces.IEnumState;

public class OreClassEntry<E> extends BlockClassEntry<E> {
	EnumRockType rocktype;
	EnumMaterialDeposit oretype;


	public OreClassEntry(IEnumState key, EnumRockType rocktype, EnumMaterialDeposit oretype) {
		super(key);
		this.rocktype = rocktype;
		this.oretype = oretype;
	}

}
