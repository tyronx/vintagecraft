package at.tyron.vintagecraft.BlockClass;

import at.tyron.vintagecraft.Block.Utility.BlockMetalPlate;
import at.tyron.vintagecraft.Interfaces.EnumStateImplementation;
import at.tyron.vintagecraft.Item.ItemMetalPlate;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.item.ItemBlock;

public class MetalCoatingClass extends CoatingClass {

	public MetalCoatingClass() {
		super(
			"metalandfacing",
			BlockMetalPlate.class,
			ItemMetalPlate.class,
			1.8f,
			Block.soundTypeMetal,
			"pickaxe",
			0
		);
	}
	
	
	
	public MetalCoatingClass init() {
		String name;
		String []sides = new String[]{"d", "u", "n", "s", "w", "e"};
		int i = 0;
		
		// Only need single sided covers for now
		for (EnumMetal metal : EnumMetal.values()) {
			for (String side : sides) {
				
				name = metal + "-" + side;
				
				EnumStateImplementation key = new EnumStateImplementation(i++, 0, name);
				values.put(key, new BlockClassEntry(key));
			}
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
