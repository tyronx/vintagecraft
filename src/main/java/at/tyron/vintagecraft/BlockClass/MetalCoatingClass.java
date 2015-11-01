package at.tyron.vintagecraft.BlockClass;

import java.util.Locale;

import at.tyron.vintagecraft.Block.Metalworking.BlockMetalPlate;
import at.tyron.vintagecraft.Interfaces.EnumStateImplementation;
import at.tyron.vintagecraft.Item.Metalworking.ItemMetalPlate;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class MetalCoatingClass extends CoatingClass {

	String getTypeName() { return "metalandfacing"; }
	
	public MetalCoatingClass() {
		super(
			"metalplate",
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
				
				name = metal.getStateName().toLowerCase(Locale.ROOT) + "-" + side;
				
				EnumStateImplementation key = new EnumStateImplementation(i++, 0, name);
				values.put(key, new MetalPlatingClassEntry(key, side, metal));
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

	
	public ItemStack getItemStackFor(EnumMetal metal) {
		return super.getItemStackFor(metal.getStateName().toLowerCase(Locale.ROOT) + "-d");
	}
}
