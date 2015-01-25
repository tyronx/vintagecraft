package at.tyron.vintagecraft.interfaces;

import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;

public interface IMultiblock {

	public IProperty getTypeProperty();
	public void setTypeProperty(PropertyBlockClass property);
	
	public BlockClass getBlockClass();
}
