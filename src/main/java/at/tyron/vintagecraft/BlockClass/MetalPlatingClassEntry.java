package at.tyron.vintagecraft.BlockClass;

import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;

public class MetalPlatingClassEntry<E> extends BlockClassEntry<E> {
	public String side;
	public EnumMetal metal;

	public MetalPlatingClassEntry(IStateEnum key, String side, EnumMetal metal) {
		super(key);
		this.side = side;
		this.metal = metal;
	}

}
