package at.tyron.vintagecraft.BlockClass;

import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;

public class SoilRockClassEntry<E> extends BlockClassEntry<E> {
	public EnumRockType rocktype;
	public EnumOrganicLayer organiclayer;
	
	public SoilRockClassEntry(IStateEnum key, EnumRockType rocktype, EnumOrganicLayer organiclayer) {
		super(key);
		this.rocktype = rocktype;
		this.organiclayer = organiclayer;
	}
}
