package at.tyron.vintagecraft.Interfaces;

import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.ItemBlock;

public interface IMultiblock {
	public int multistateAvailableTypes();
	
	public IProperty getTypeProperty();
	public void setTypeProperty(PropertyBlockClass property);
	
	public BlockClass getBlockClass();
	
	public Block registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IStateEnum []types);
	public Block registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IStateEnum []types, String folderprefix);
}
