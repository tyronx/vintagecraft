package at.tyron.vintagecraft.BlockClass;

import at.tyron.vintagecraft.WorldProperties.EnumFlora;
import at.tyron.vintagecraft.interfaces.IEnumState;

public class FlowerClassEntry<E> extends BlockClassEntry<E> {
	boolean doubleHigh = false;
	EnumFlora group;

	public FlowerClassEntry(IEnumState key, boolean doubleHigh, EnumFlora group) {
		super(key);
		this.doubleHigh = doubleHigh;
		this.group = group;
	}

}
