package at.tyron.vintagecraft.WorldGen;

import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldGen.EvolvingNatFloat.Function;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.block.BlockLeavesVC;
import at.tyron.vintagecraft.block.BlockLeavesBranchy;
import net.minecraft.block.state.IBlockState;

public class DynTreeTrunk {
	/*public float avgHeight;*/
	public float width;
	public float widthloss;         			 // (per 0.01f = 1% height)   => if 0.01f then at the trunk we have 100% width and at the top 0% width
	public float widthBranchLossBase = 1f;       // Each branch action, this value gets multiplied to the current width (=> 1f = no loss in width from branching)
	public EvolvingNatFloat branchWidthMultiplier;   // This is the size of the branch multiplied by the current trunk width  (=> 0.5f = branch is half the width as the trunk)
	
	public EvolvingNatFloat numBranching;
	//public int numBranching = 1;
	
	public NatFloat branchStart;
	public NatFloat branchSpacing;
	public NatFloat branchVerticalAngle;
	public NatFloat branchHorizontalAngle;
	
	public EvolvingNatFloat angleVert = EvolvingNatFloat.createIdentical(0f);
	public EvolvingNatFloat angleHori = EvolvingNatFloat.createUniform(0, NatFloat.PI * 2, Function.IDENTICAL, 0f);
	
	public DynTreeTrunk(float width, float widthloss, NatFloat branchStart, NatFloat branchSpacing, NatFloat branchVerticalAngle, NatFloat branchHorizontalAngle, EvolvingNatFloat numBranching, EvolvingNatFloat branchWidthMultiplier) {
		this(width, widthloss, branchStart, branchSpacing, branchVerticalAngle, branchHorizontalAngle, numBranching, branchWidthMultiplier, 1f, EvolvingNatFloat.createIdentical(0f));
	}
	
	public DynTreeTrunk(float width, float widthloss, NatFloat branchStart, NatFloat branchSpacing, NatFloat verticalAngle, NatFloat horizontalAngle, EvolvingNatFloat numBranching, EvolvingNatFloat branchWidthMultiplier, float widthBranchLossBase) {
		this(width, widthloss, branchStart, branchSpacing, verticalAngle, horizontalAngle, numBranching, branchWidthMultiplier, widthBranchLossBase, EvolvingNatFloat.createIdentical(0f));
	}
	
	public DynTreeTrunk(float width, float widthloss, NatFloat branchStart, NatFloat branchSpacing, NatFloat branchVerticalAngle, NatFloat branchHorizontalAngle, EvolvingNatFloat numBranching, EvolvingNatFloat branchWidthMultiplier, float widthBranchLossBase, EvolvingNatFloat angleVert/*, EvolvingNatFloat bendAngleVert*/) {
		
		this.width = width;
		this.widthloss = widthloss;
		this.branchStart = branchStart;
		this.branchSpacing = branchSpacing;
		this.branchVerticalAngle = branchVerticalAngle;
		this.branchHorizontalAngle = branchHorizontalAngle;
		
		this.numBranching = numBranching;
		this.branchWidthMultiplier = branchWidthMultiplier;
		
		this.widthBranchLossBase = widthBranchLossBase;
		
		//this.bendAngleVert = bendAngleVert;
		this.angleVert = angleVert;
	}
}
