package at.tyron.vintagecraft.WorldGen.Village;

public class DynVillageGenerators {
/*
		centerBranchingQuantity (Component.CENTER),
		centerBranchingWidth (Component.CENTER),
		
		branchAngleOffset (Component.ROAD),
		branchWidthLoss (Component.ROAD),
		
		branchingAngle (Component.ROAD),
		branchingStart (Component.ROAD),
		branchingSpacing (Component.ROAD),
		branchingQuantity (Component.ROAD),
		branchingWidth (Component.ROAD),
		branchingWidthLoss (Component.ROAD),
		
		branchingMaxRecursion (Component.ROAD),
 */
	
	public static void initGenerators() {
		
		EnumVillage.DEFAULT.setGenerator(new DynVillageGen(new NumFloat[]{
			NatFloat.createUniform(3f, 0.5f),										// centerBranchingQuantity
			NumFloat.create(1.5f),													// centerBranchingWidth
			EvolvingFloat.createPerlin(NumFloat.create(0f), 0.5f, 3, 0.002f), 		// branchAngleOffset
			NumFloat.create(0.02f),													// branchWidthLoss
			NatFloat.createInvGauss(0f, 1.8f),										// branchingAngle
			NumFloat.create(0.2f),													// branchingStart
			NumFloat.create(0.4f),													// branchingSpacing
			NumFloat.create(1f),													// branchingQuantity
			NumFloat.create(0.8f),													// branchingWidth
			NumFloat.create(1f),													// branchingWidthLoss
			NumFloat.create(1f),													// branchingMaxRecursion
		}));
		
	}
}
