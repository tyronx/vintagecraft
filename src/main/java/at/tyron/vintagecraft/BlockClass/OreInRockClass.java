package at.tyron.vintagecraft.BlockClass;

import at.tyron.vintagecraft.Block.BlockOreVC;
import at.tyron.vintagecraft.Interfaces.EnumStateImplementation;
import at.tyron.vintagecraft.Item.ItemOreBlock;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.item.ItemBlock;

public class OreInRockClass extends BaseBlockClass {
	String getBlockClassName() { return name; }
	Class<? extends Block> getBlockClass() { return blockclass; }
	Class<? extends ItemBlock> getItemClass() { return itemclass; }
	float getHardness() { return 2.25f; }
	SoundType getStepSound() { return Block.soundTypeStone; }
	String getHarvestTool() { return "pickaxe"; }
	int getHarvestLevel() { return 1; }
	String getTypeName() { return "oreandrocktype"; }
	
	
	public OreInRockClass init() {
		name="rawore";
		blockclass = BlockOreVC.class;
		itemclass = ItemOreBlock.class;
		
		int i = 0;
		for (EnumRockType rocktype : EnumRockType.values()) {
			for (EnumOreType oretype : EnumOreType.valuesSorted()) {
				if (oretype.isParentMaterial(rocktype) && oretype.spawnsInRock) {
					EnumStateImplementation key = new EnumStateImplementation(i++, 0, oretype.getName() + "-" + rocktype.getName());
				
					values.put(key, new OreInRockClassEntry(key, rocktype, oretype));
				}
			}
		}
		
		initBlocks(getBlockClassName(), getBlockClass(), getItemClass(), getHardness(), getStepSound(), getHarvestTool(), getHarvestLevel());
		
		return this;
	}

}
