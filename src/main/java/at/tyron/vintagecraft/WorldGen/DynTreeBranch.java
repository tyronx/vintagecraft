package at.tyron.vintagecraft.WorldGen;

import net.minecraft.block.state.IBlockState;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumTree;

public class DynTreeBranch {
	float anglevert;
	float varianceAnglevert;
	
	float anglehori;
	float varianceAnglehori;
	
	float spacing;
	float varianceSpacing;
	
	// How much thickness the branch looses over distance
	public float widthloss = 0.05f;
	
	//public float widthBranchLossBase = 1f;     // Each branch action, this value gets multiplied to the current width (=> 1f = no loss in width from branching)
	public float branchWidthMultiplier = 0.5f;   		// This is the size of the branch multiplied by the current trunk width  (=> 0.5f = branch is half the width as the trunk)

	// How much does the branch give in to gravity? (increases over the branch length)
	public float gravityDrag = 0f;
	
	private IBlockState block;
	
	public DynTreeBranch(double anglevert, double varianceAnglevert, double anglehori, double varianceAnglehori, float spacing, float varianceSpacing, float widthloss) {
		this(anglevert, varianceAnglevert, anglehori, varianceAnglehori, spacing, varianceSpacing, widthloss, 0f);
	}
	
	
	public DynTreeBranch(double anglevert, double varianceAnglevert, double anglehori, double varianceAnglehori, float spacing, float varianceSpacing, float widthloss, float gravitydrag) {
		this.anglevert = (float) anglevert;
		this.varianceAnglevert = (float) varianceAnglevert;
		this.anglehori = (float) anglehori;
		this.varianceAnglehori = (float) varianceAnglehori;
		this.spacing = spacing;
		this.varianceSpacing = varianceSpacing;
		this.widthloss = widthloss;
		
		this.gravityDrag = gravitydrag;
		
	}
	
	public void setTree(EnumTree tree) {
		block = BlocksVC.log.getBlockStateFor(tree);
	}
	
	
	public IBlockState block() {
		return block;
	}
	
}
