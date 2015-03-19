package at.tyron.vintagecraft.BlockClass;

import java.util.List;

import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.block.BlockFlowerVC;
import at.tyron.vintagecraft.block.BlockLeavesVC;
import at.tyron.vintagecraft.block.BlockLeavesBranchy;
import at.tyron.vintagecraft.block.BlockLogVC;
import at.tyron.vintagecraft.block.BlockVC;
import at.tyron.vintagecraft.interfaces.IEnumState;
import at.tyron.vintagecraft.interfaces.IMultiblock;
import at.tyron.vintagecraft.item.ItemFlowerVC;
import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IStringSerializable;


public class RockClass extends BlockClass {
	String getBlockClassName() { return name; }
	Class<? extends Block> getBlockClass() { return blockclass; }
	Class<? extends ItemBlock> getItemClass() { return itemclass; }
	float getHardness() { return hardness; }
	SoundType getStepSound() { return stepsound; }
	String getHarvestTool() { return harvesttool; }
	int getHarvestLevel() { return harvestlevel; }
	String getTypeName() { return "rocktype"; }
	
	public RockClass(String name, Class<? extends Block> blockclass, Class<? extends ItemBlock> itemclass, float hardness, SoundType stepsound, String harvesLevelTool, int harvestLevel) {
		this.name = name;
		this.blockclass = blockclass;
		this.itemclass = itemclass;
		this.hardness = hardness;
		this.stepsound = stepsound;
		this.harvesttool = harvesLevelTool;
		this.harvestlevel = harvestLevel;
	}
	
	
	public RockClass init() {
		for (Enum item : EnumRockType.values()) {
			values.put((IEnumState) item, new BlockClassEntry((IEnumState)item));
		}
		
		initBlocks(getBlockClassName(), getBlockClass(), getItemClass(), getHardness(), getStepSound(), getHarvestTool(), getHarvestLevel());
		return this;
	}
}

