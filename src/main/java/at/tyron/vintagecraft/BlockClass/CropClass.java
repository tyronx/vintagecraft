package at.tyron.vintagecraft.BlockClass;

import at.tyron.vintagecraft.Block.Organic.BlockCropsVC;
import at.tyron.vintagecraft.Interfaces.EnumStateImplementation;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumCrop;
import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;

public class CropClass extends BaseBlockClass {
	String getBlockClassName() { return  "crops"; }
	Class<? extends Block> getBlockClass() { return BlockCropsVC.class; }
	Class<? extends ItemBlock> getItemClass() { return ItemBlock.class; }
	float getHardness() { return 0.4f; }
	SoundType getStepSound() { return Block.soundTypeGrass; }
	String getHarvestTool() { return "hoe"; }
	int getHarvestLevel() { return 0; }
	String getTypeName() { return "croptypeandstage"; }
	
	
	
	public CropClass init() {
		name="crops";
		blockclass = BlockCropsVC.class;
		itemclass = ItemBlock.class;
		
		for (EnumCrop crop: EnumCrop.values()) {
			for (int stage = 0; stage < crop.growthstages; stage++) {
				EnumStateImplementation key = new EnumStateImplementation(stage, 0, crop.getName() + "-stage" + stage);
				
				values.put(key, new CropClassEntry(key, crop, stage));
			}
		}
		
		initBlocks(getBlockClassName(), getBlockClass(), getItemClass(), getHardness(), getStepSound(), getHarvestTool(), getHarvestLevel());
		
		return this;
	}
	

	
	public IBlockState getBlockStateFor(EnumCrop crop, int stage) {
		return getBlockStateFor(crop.getName() + "-stage" + stage);
	}

}
