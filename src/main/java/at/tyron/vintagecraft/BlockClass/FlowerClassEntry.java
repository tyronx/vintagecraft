package at.tyron.vintagecraft.BlockClass;

import at.tyron.vintagecraft.WorldProperties.EnumFlowerGroup;
import at.tyron.vintagecraft.interfaces.IEnumState;

public class FlowerClassEntry<E> extends BlockClassEntry<E> {
	boolean doubleHigh = false;
	EnumFlowerGroup group;

	public FlowerClassEntry(IEnumState key, boolean doubleHigh, EnumFlowerGroup group) {
		super(key);
		this.doubleHigh = doubleHigh;
		this.group = group;
	}

}
