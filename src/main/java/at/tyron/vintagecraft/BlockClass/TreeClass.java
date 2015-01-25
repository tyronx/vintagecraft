package at.tyron.vintagecraft.BlockClass;

import java.util.List;

import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.block.BlockFlowerVC;
import at.tyron.vintagecraft.block.BlockLeaves;
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


// http://www.hoovedesigns.com/woods.html
// http://www.piecesofwood.com/woods.html

// Black wood: http://en.wikipedia.org/wiki/Dalbergia_melanoxylon

public class TreeClass extends BlockClass {

	
	String getBlockClassName() { return name; }
	Class<? extends BlockVC> getBlockClass() { return blockclass; }
	Class<? extends ItemBlock> getItemClass() { return itemclass; }
	float getHardness() { return hardness; }
	SoundType getStepSound() { return stepsound; }
	String getHarvestTool() { return harvesttool; }
	int getHarvestLevel() { return harvestlevel; }
	String getTypeName() { return "treetype"; }
	
	public TreeClass(String name, Class<? extends BlockVC> blockclass, Class<? extends ItemBlock> itemclass, float hardness, SoundType stepsound, String harvesLevelTool, int harvestLevel) {
		this.name = name;
		this.blockclass = blockclass;
		this.itemclass = itemclass;
		this.hardness = hardness;
		this.stepsound = stepsound;
		this.harvesttool = harvesLevelTool;
		this.harvestlevel = harvestLevel;
	}
	
	
	public TreeClass init() {
		for (Enum item : EnumTree.values()) {
			values.put((IEnumState) item, new BlockClassEntry((IEnumState)item));
		}
		
		initBlocks(getBlockClassName(), getBlockClass(), getItemClass(), getHardness(), getStepSound(), getHarvestTool(), getHarvestLevel());
		return this;
	}
	
	
/*	public static TreeClass ASH = new TreeClass("ash");
	public static TreeClass BIRCH = new TreeClass("birch");
	public static TreeClass DOUGLASFIR = new TreeClass("douglasfir");
	public static TreeClass OAK = new TreeClass("oak");
	public static TreeClass MAPLE = new TreeClass("maple");
	public static TreeClass MOUNTAINDOGWOOD = new TreeClass("mountaindogwood");
	public static TreeClass PINE = new TreeClass("pine");
	public static TreeClass SPRUCE = new TreeClass("spruce");*/
	
	// http://www.oxfordlearnersdictionaries.com/media/american_english/fullsize/d/dec/decid/deciduous_trees.jpg
	

	// in forests: http://www.sfu.ca/geog/geog351fall07/Group06/Final%20Project%20Images/Interior%20Douglas-fir/douglas%20fir.jpg
	//             http://www2.humboldt.edu/redwoods/images/photos/dougfir/1_WA_young-canopy1.jpg
	
	// alone: http://www.christmastreesucut.com/wp-content/uploads/2010/10/Douglas-Fir.jpg
	
	
	//https://tce-staging.s3.amazonaws.com/media/media/36d500de-8303-45ea-8e6b-78b94b7bc8d2.jpg
	
	
	

	


	
	/*
	public static TreeClass fromMeta(BlockVC block, int meta) {
		for (TreeClass tree : values()) {
			if (block instanceof BlockLogVC && tree.metadata == meta && tree.block == block) return tree;
			if (block instanceof BlockLeaves && tree.metadata == meta && tree.block == block) return tree;
			if (block instanceof BlockLeavesBranchy && tree.metadata == meta && tree.block == block) return tree;
		}
		return null;
	}*/

	
}

