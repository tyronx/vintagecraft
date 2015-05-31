package at.tyron.vintagecraft.BlockClass;

import java.util.List;

import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.Block.Organic.BlockFlowerVC;
import at.tyron.vintagecraft.Block.Organic.BlockLeavesBranchy;
import at.tyron.vintagecraft.Block.Organic.BlockLeavesVC;
import at.tyron.vintagecraft.Block.Organic.BlockLogVC;
import at.tyron.vintagecraft.Interfaces.IMultiblock;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.Item.ItemFlowerVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IStringSerializable;


public class TreeClass extends BaseBlockClass {
	String getBlockClassName() { return name; }
	Class<? extends Block> getBlockClass() { return blockclass; }
	Class<? extends ItemBlock> getItemClass() { return itemclass; }
	float getHardness() { return hardness; }
	SoundType getStepSound() { return stepsound; }
	String getHarvestTool() { return harvesttool; }
	int getHarvestLevel() { return harvestlevel; }
	String getTypeName() { return "treetype"; }
	
	public TreeClass(String name, Class<? extends Block> blockclass, Class<? extends ItemBlock> itemclass, float hardness, SoundType stepsound, String harvesLevelTool, int harvestLevel) {
		this.name = name;
		this.blockclass = blockclass;
		this.itemclass = itemclass;
		this.hardness = hardness;
		this.stepsound = stepsound;
		this.harvesttool = harvesLevelTool;
		this.harvestlevel = harvestLevel;
	}
	
	
	public TreeClass init() {
		for (EnumTree tree : EnumTree.values()) {
			if (tree.isBush && !name.equals("leaves") && !name.equals("leavesbranchy")) {
				continue;
			}
			
			values.put(tree, new BlockClassEntry(tree));
		}
		
		initBlocks(getBlockClassName(), getBlockClass(), getItemClass(), getHardness(), getStepSound(), getHarvestTool(), getHarvestLevel());
		return this;
	}
}

