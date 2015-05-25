package at.tyron.vintagecraft.WorldGen.Village;

import at.tyron.vintagecraft.WorldGen.Village.DynVillageGen.Param.Component;

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
			NatFloat.createUniform(3f, 0.5f),
			NumFloat.create(1.5f),
			EvolvingFloat.createPerlin(NumFloat.create(0f), 0.5f, 3, 0.002f), //Uniform(NumFloat.PIHALF / 2, 0.3f),
			NumFloat.create(0.02f),
			NatFloat.createInvGauss(0f, 1.8f),
			NumFloat.create(0.2f),
			NumFloat.create(0.4f),
			NumFloat.create(1f),
			NumFloat.create(0.8f),
			NumFloat.create(1f),
			NumFloat.create(1f),
		}));
		
	}
}
