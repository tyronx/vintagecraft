package at.tyron.vintagecraft.WorldGen;

import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.block.BlockLeavesVC;
import at.tyron.vintagecraft.block.BlockLeavesBranchy;
import net.minecraft.block.state.IBlockState;

public class DynTreeTrunk {
	public float avgHeight;
	public float width;
	public float widthloss;         			 // (per 0.01f = 1% height)   => if 0.01f then at the trunk we have 100% width and at the top 0% width
	public float widthBranchLossBase = 1f;       // Each branch action, this value gets multiplied to the current width (=> 1f = no loss in width from branching)
	public float branchWidthMultiplier = 0.5f;   // This is the size of the branch multiplied by the current trunk width  (=> 0.5f = branch is half the width as the trunk)
	
	public float bend = 0f;						 // Vertical angle of the tree 
	public float bendCorrection = 0f;			 // How strongly will a bent tree correct to straight again 
	
	public int numBranching = 1;
	
	public NatFloat branchStart;
	public NatFloat branchSpacing;
	public NatFloat branchVerticalAngle;
	public NatFloat branchHorizontalAngle;

	
	public DynTreeTrunk(float avgHeight, float width, float widthloss, NatFloat branchStart, NatFloat branchSpacing, NatFloat verticalAngle, NatFloat horizontalAngle, int numBranching, float branchWidthMultiplier) {
		this(avgHeight, width, widthloss, branchStart, branchSpacing, verticalAngle, horizontalAngle, numBranching, branchWidthMultiplier, 1f, 0f, 0f);
	}
	
	public DynTreeTrunk(float avgHeight, float width, float widthloss, NatFloat branchStart, NatFloat branchSpacing, NatFloat verticalAngle, NatFloat horizontalAngle, int numBranching, float branchWidthMultiplier, float widthBranchLossBase, float bend, float bendCorrection) {
		this.avgHeight = avgHeight;
		this.width = width;
		this.widthloss = widthloss;
		this.branchStart = branchStart;
		this.branchSpacing = branchSpacing;
		this.branchVerticalAngle = verticalAngle;
		this.branchHorizontalAngle = horizontalAngle;
		
		this.numBranching = numBranching;
		this.branchWidthMultiplier = branchWidthMultiplier;
		
		this.widthBranchLossBase = widthBranchLossBase;
		
		this.bend = bend;
		this.bendCorrection = bendCorrection;
	}
}
