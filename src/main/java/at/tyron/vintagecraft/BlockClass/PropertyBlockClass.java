package at.tyron.vintagecraft.BlockClass;

import java.util.Collection;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.util.IStringSerializable;

public class PropertyBlockClass extends PropertyHelper {
	ImmutableSet<BlockClassEntry> allowedValues;
	
	protected PropertyBlockClass(String name, Class valueClass, BlockClassEntry[] allowedentries) {
		super(name, valueClass);
		
		this.allowedValues = ImmutableSet.copyOf(allowedentries);		
	}


	@Override
	public Collection getAllowedValues() {
		return allowedValues;
	}

	@Override
	public String getName(Comparable value) {
		return ((IStringSerializable)value).getName();
	}

}
