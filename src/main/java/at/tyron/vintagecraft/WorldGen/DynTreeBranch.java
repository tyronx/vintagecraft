package at.tyron.vintagecraft.WorldGen;

import net.minecraft.block.state.IBlockState;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumTree;

public class DynTreeBranch {
	public NatFloat branchVerticalAngle;
	public NatFloat branchHorizontalAngle;
	public NatFloat branchSpacing;
	public NatFloat branchStart;
		
	// How much thickness the branch looses over distance
	public float widthloss = 0.05f;
	
	public float widthBranchLossBase = 1f;     // Each branch action, this value gets multiplied to the current width (=> 1f = no loss in width from branching)
	public float branchWidthMultiplier = 0.5f;   		// This is the size of the branch multiplied by the current trunk width  (=> 0.5f = branch is half the width as the trunk)

	// How much does the branch give in to gravity? (increases over the branch length)
	public float gravityDrag = 0f;
	
	public float bendCorrection = 0f;
	
	//int numbranching = 1;
	EvolvingNatFloat numBranching;
	
	//public EvolvingNatFloat bendAngleVert = EvolvingNatFloat.createUniform(0f, 0f);
	
	public EvolvingNatFloat angleVert = EvolvingNatFloat.createIdentical(NatFloat.PI / 2);
	public EvolvingNatFloat angleHori = EvolvingNatFloat.createIdentical(0f);
	
	
	public DynTreeBranch(NatFloat verticalAngle, NatFloat horizontalAngle, NatFloat spacing, float widthloss) {
		this(verticalAngle, horizontalAngle, NatFloat.createUniform(0f, 0f), spacing, widthloss, 0f, 0.5f);
	}
	
	public DynTreeBranch(NatFloat branchVerticalAngle, NatFloat branchHorizontalAngle, NatFloat branchStart, NatFloat spacing, float widthloss, float gravitydrag, float branchWidthMultiplier) {
		this(branchVerticalAngle, branchHorizontalAngle, branchStart, spacing, widthloss, EvolvingNatFloat.createIdentical(1f), gravitydrag, branchWidthMultiplier, 1f);
	}
	
	public DynTreeBranch(NatFloat branchVerticalAngle, NatFloat branchHorizontalAngle, NatFloat branchStart, NatFloat spacing, float widthloss, EvolvingNatFloat numbranching, float gravitydrag, float branchWidthMultiplier, float widthBranchLossBase) {
		this.branchVerticalAngle = branchVerticalAngle;
		this.branchHorizontalAngle = branchHorizontalAngle;
		this.branchSpacing = spacing;
		this.branchStart = branchStart;
		
		this.widthloss = widthloss;
		this.gravityDrag = gravitydrag;
		
		this.branchWidthMultiplier = branchWidthMultiplier;
		this.widthBranchLossBase = widthBranchLossBase;
		
		this.numBranching = numbranching;
	}
}
