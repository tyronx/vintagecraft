package at.tyron.vintagecraft.BlockClass;

import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.item.ItemBlock;
import at.tyron.vintagecraft.WorldProperties.EnumFlora;
import at.tyron.vintagecraft.WorldProperties.EnumFlower;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.block.BlockDoubleFlowerVC;
import at.tyron.vintagecraft.block.BlockFlowerVC;
import at.tyron.vintagecraft.block.BlockOreVC;
import at.tyron.vintagecraft.block.BlockVC;
import at.tyron.vintagecraft.interfaces.EnumStateImplementation;
import at.tyron.vintagecraft.interfaces.IEnumState;
import at.tyron.vintagecraft.item.ItemFlowerVC;
import at.tyron.vintagecraft.item.ItemOreVC;

public class OreClass extends BlockClass {
	String getBlockClassName() { return name; }
	Class<? extends BlockVC> getBlockClass() { return blockclass; }
	Class<? extends ItemBlock> getItemClass() { return itemclass; }
	float getHardness() { return 2f; }
	SoundType getStepSound() { return Block.soundTypeStone; }
	String getHarvestTool() { return "pickaxe"; }
	int getHarvestLevel() { return 1; }
	String getTypeName() { return "oretype"; }
	
	
	public OreClass init() {
		name="rawore";
		blockclass = BlockOreVC.class;
		itemclass = ItemBlock.class;
		
		int i = 0;
		for (EnumRockType rocktype : EnumRockType.values()) {
			for (EnumMaterialDeposit oretype : EnumMaterialDeposit.values()) {
				EnumStateImplementation key = new EnumStateImplementation(i++, 0, name);
				
				values.put(key, new OreClassEntry(key, rocktype, oretype));
			}
		}
		
		initBlocks(getBlockClassName(), getBlockClass(), getItemClass(), getHardness(), getStepSound(), getHarvestTool(), getHarvestLevel());
		
		return this;
	}

}
