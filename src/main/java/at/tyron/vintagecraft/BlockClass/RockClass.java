package at.tyron.vintagecraft.BlockClass;

import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.item.ItemBlock;


public class RockClass extends BaseBlockClass {
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
			values.put((IStateEnum) item, new BlockClassEntry((IStateEnum)item));
		}
		
		initBlocks(
			getBlockClassName(), 
			getBlockClass(), 
			getItemClass(), 
			getHardness(), 
			getStepSound(), 
			getHarvestTool(), 
			getHarvestLevel()
		);
		return this;
	}
}

