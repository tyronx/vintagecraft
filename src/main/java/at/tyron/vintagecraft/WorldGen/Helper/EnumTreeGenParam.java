package at.tyron.vintagecraft.WorldGen.Helper;

public enum EnumTreeGenParam {
	verticalAngle (Section.BRANCH),
	horizontalAngle (Section.BRANCH),
	
	branchingVerticalAngle (Section.BRANCH),
	branchingHorizontalAngle (Section.BRANCH),
	branchingStart (Section.BRANCH),
	branchingSpacing (Section.BRANCH),
	branchingQuantity (Section.BRANCH),
	branchingWidth (Section.BRANCH),
	branchingWidthLoss (Section.BRANCH),
	
	widthLoss (Section.BRANCH),
	gravityDrag (Section.BRANCH),
	
	
	trunkWidth (Section.TRUNK),
	trunkBranchingVerticalAngle (Section.TRUNK),
	trunkBranchingHorizontalAngle (Section.TRUNK),
	trunkBranchingStart (Section.TRUNK),
	trunkBranchingSpacing (Section.TRUNK),
	trunkBranchingQuantity (Section.TRUNK),
	trunkBranchingWidth (Section.TRUNK),
	trunkBranchingWidthLoss (Section.TRUNK),
	
	
	;
	
	
	Section ofcomponent;
	
	private EnumTreeGenParam(Section ofcomponent) {
		this.ofcomponent = ofcomponent; 
	}
	
	
	
	public static enum Section {
		TRUNK,
		BRANCH,
		ROOTS
	}

	
}
