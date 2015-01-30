package at.tyron.vintagecraft.WorldGen;

import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.block.BlockLeavesVC;
import at.tyron.vintagecraft.block.BlockLeavesBranchy;
import net.minecraft.block.state.IBlockState;

public class DynTreeTrunk {
	float avgHeight;
	float width;
	float widthloss;         // (per 0.01f = 1% height)   => if 0.01f then at the trunk we have 100% width and at the top 0% width
	//float splitChance;
	//float splitStart;
	//float splitAngle;		// angle between branch and trunk
	//float splitWidth;
	//float splitWidthVariance;
	//float trunkAngleLoss;    //(0 = the trunk loses no angle,  0.5f = trunk and branch both split of at same angle, 1f = trunk looses full angle, branch goes vertical)  
	float variance;
	
	float branchStart;
	float branchSpacing;
	float branchVarianceSpacing;
	
	//float widthBranchLossBase = 1f;       // Each branch action, this value gets multiplied to the current width (=> 1f = no loss in width from branching)
	float branchWidthMultiplier = 0.5f;   // This is the size of the branch multiplied by the current trunk width  (=> 0.5f = branch is half the width as the trunk)
	
	int numBranching = 1;
	
	private IBlockState log;
	private IBlockState leavesbranchy;
	private IBlockState leaves;
	
	//public DynTreeTrunk(float avgHeight, float width, float widthloss, float splitChance, float splitStart, float splitAngle, float splitWidth, float splitWidthVariance, float trunkAngleLoss,float variance, int numBranching) {
	public DynTreeTrunk(float avgHeight, float width, float widthloss, float branchStart, float branchSpacing, float branchVarianceSpacing, float variance, int numBranching, float branchWidthMultiplier) {
		this.avgHeight = avgHeight;
		this.width = width;
		this.widthloss = widthloss;
		
		this.branchStart = branchStart;
		this.branchSpacing = branchSpacing;
		this.branchVarianceSpacing = branchVarianceSpacing;
		
		//this.splitChance = splitChance;
		//this.splitStart = splitStart;
		//this.splitAngle = splitAngle;
		//this.trunkAngleLoss = trunkAngleLoss;
		//this.splitWidth = splitWidth;
		//this.splitWidthVariance = splitWidthVariance;
		this.variance = variance;
		this.numBranching = numBranching;
		
		this.branchWidthMultiplier = branchWidthMultiplier;
	}
	
	
	public void setTree(EnumTree tree) {
		log = BlocksVC.log.getBlockStateFor(tree);
		leavesbranchy = BlocksVC.leavesbranchy.getBlockStateFor(tree);
		leaves = BlocksVC.leaves.getBlockStateFor(tree);
	}
	
	
	public IBlockState block(float width) {
		if (width < 0.1f)
			return leaves;
		if (width < 0.3f)
			return leavesbranchy;
		
		return log;
	}
	
}
