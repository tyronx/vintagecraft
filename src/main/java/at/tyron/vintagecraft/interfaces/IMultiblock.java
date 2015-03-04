package at.tyron.vintagecraft.interfaces;

import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import at.tyron.vintagecraft.block.BlockVC;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.ItemBlock;

public interface IMultiblock {
	public int multistateAvailableTypes();
	
	public IProperty getTypeProperty();
	public void setTypeProperty(PropertyBlockClass property);
	
	public BlockClass getBlockClass();
	
	public Block registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IEnumState []types);
	public Block registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IEnumState []types, String folderprefix);
}
