package at.tyron.vintagecraft.BlockClass;

import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.item.ItemBlock;
import at.tyron.vintagecraft.Block.BlockOreVC;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.Block.Organic.BlockDoubleFlowerVC;
import at.tyron.vintagecraft.Block.Organic.BlockFlowerVC;
import at.tyron.vintagecraft.Interfaces.EnumStateImplementation;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.Item.ItemFlowerVC;
import at.tyron.vintagecraft.Item.ItemOreBlock;
import at.tyron.vintagecraft.Item.ItemOreVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFlower;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFlowerGroup;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;

public class OreClass extends BaseBlockClass {
	String getBlockClassName() { return name; }
	Class<? extends Block> getBlockClass() { return blockclass; }
	Class<? extends ItemBlock> getItemClass() { return itemclass; }
	float getHardness() { return 2.25f; }
	SoundType getStepSound() { return Block.soundTypeStone; }
	String getHarvestTool() { return "pickaxe"; }
	int getHarvestLevel() { return 1; }
	String getTypeName() { return "oreandrocktype"; }
	
	
	public OreClass init() {
		name="rawore";
		blockclass = BlockOreVC.class;
		itemclass = ItemOreBlock.class;
		
		int i = 0;
		for (EnumRockType rocktype : EnumRockType.values()) {
			for (EnumOreType oretype : EnumOreType.valuesSorted()) {
				if (oretype.isParentMaterial(rocktype) && oretype.spawnsInRock) {
					EnumStateImplementation key = new EnumStateImplementation(i++, 0, oretype.getName() + "-" + rocktype.getName());
				
					values.put(key, new OreClassEntry(key, rocktype, oretype));
				}
			}
		}
		
		initBlocks(getBlockClassName(), getBlockClass(), getItemClass(), getHardness(), getStepSound(), getHarvestTool(), getHarvestLevel());
		
		return this;
	}

}
