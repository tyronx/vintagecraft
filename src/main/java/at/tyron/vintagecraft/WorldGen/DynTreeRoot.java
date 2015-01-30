package at.tyron.vintagecraft.WorldGen;

import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import net.minecraft.block.state.IBlockState;

public class DynTreeRoot {
	public float yStart;
	public float avgLength;
	public float numRoots;
	public float variance;
	
	private IBlockState block;
	
	public DynTreeRoot(float yStart, float avgLength, float numRoots, float variance) {
		this.yStart = yStart;
		this.avgLength = avgLength;
		this.numRoots = numRoots;
		this.variance = variance;
		
		
	}
	
	public void setTree(EnumTree tree) {
		block = BlocksVC.log.getBlockStateFor(tree);
	}
	
	
	public IBlockState block() {
		return block;
	}

}
