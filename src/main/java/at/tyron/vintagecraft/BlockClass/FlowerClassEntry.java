package at.tyron.vintagecraft.BlockClass;

import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFlowerGroup;

public class FlowerClassEntry<E> extends BlockClassEntry<E> {
	boolean doubleHigh = false;
	EnumFlowerGroup group;

	public FlowerClassEntry(IStateEnum key, boolean doubleHigh, EnumFlowerGroup group) {
		super(key);
		this.doubleHigh = doubleHigh;
		this.group = group;
	}

}
