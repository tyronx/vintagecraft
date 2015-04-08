package at.tyron.vintagecraft.WorldGen;

import at.tyron.vintagecraft.WorldGen.Helper.*;
import at.tyron.vintagecraft.WorldProperties.EnumTreeGenMode;
import at.tyron.vintagecraft.WorldProperties.EnumTransformFunction;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;

public class DynTreeGenerators {
	
	static EvolvingNatFloat branching(int num) {
		return EvolvingNatFloat.createIdentical(num);
	}
	static EvolvingNatFloat branchWidthMul(float num) {
		return EvolvingNatFloat.createIdentical(num);
	}
	
	public static void initGenerators() {
		// Reference: 
		/* new DynTreeTrunk(avgHeight, width, widthloss, branchStart, branchSpacing, branchVarianceSpacing, variance, numBranching, branchWidthMultiplier),
		 * new DynTreeBranch(anglevert, varianceAnglevert, anglehori, varianceAnglehori, spacing, varianceSpacing, widthloss, gravityDrag)
		 * new DynTreeBranch(NatFloat verticalAngle, NatFloat horizontalAngle, NatFloat spacing, float widthloss)
   		 */
		
		EnumTree.BIRCH.setGenerators(new DynTreeGen(
			EnumTree.BIRCH, 
			null,
			new DynTreeTrunk(
				1f, 
				0.05f, 
				NatFloat.createGauss(0.22f, 0.01f), 
				NatFloat.createGauss(0.005f, 0.1f), 
				NatFloat.createInvGauss(5*NatFloat.PI / 4 + 0.1f, NatFloat.PI / 8), 
				NatFloat.createUniform(0, NatFloat.PI),
				branching(3), 
				branchWidthMul(0.5f)
			),
			new DynTreeBranch(
				NatFloat.createInvGauss(5*NatFloat.PI / 4 + 0.1f, NatFloat.PI / 8),
				NatFloat.createUniform(0, NatFloat.PI), 
				NatFloat.createGauss(0.01f, 0.01f), 
				0.06f
			),
			1.2f
		), null, null);
		
		
				
		//new DynTreeTrunk(avgHeight, width, widthloss, branchStart, branchSpacing, verticalAngle, horizontalAngle, numBranching, branchWidthMultiplier, widthBranchLossBase, bend, bendCorrection)
		//new DynTreeBranch(verticalAngle, horizontalAngle, branchStart, spacing, widthloss, gravitydrag, branchWidthMultiplier)
		//DynTreeBranch(NatFloat verticalAngle, NatFloat horizontalAngle, NatFloat branchStart, NatFloat spacing, float widthloss, float gravitydrag)
		
		EnumTree.CRIMSONKINGMAPLE.setGenerators(new DynTreeGen(
			EnumTree.CRIMSONKINGMAPLE,
			null,
			new DynTreeTrunk(
				1f, 
				0.1f, 
				NatFloat.createGauss(0.45f, 0f), 
				NatFloat.createGauss(0f, 0f), 
				NatFloat.createInvGauss(NatFloat.PI + 0.55f, 0), 
				NatFloat.createUniform(0, NatFloat.PI),
				branching(8), 
				branchWidthMul(0.8f)
			),
			new DynTreeBranch(
				NatFloat.createInvGauss(NatFloat.PI + 0.35f, 0),
				NatFloat.createUniform(0, NatFloat.PI), 
				NatFloat.createGauss(0.01f, 0f), 
				0.11f
			),
			1.5f
		), null, null);
		
		//new DynTreeTrunk(width, widthloss, branchStart, branchSpacing, verticalAngle, horizontalAngle, numBranching, branchWidthMultiplier, widthBranchLossBase)
		
		EnumTree.PURPLEHEARTWOOD.setGenerators(new DynTreeGen(
				EnumTree.PURPLEHEARTWOOD, 
				new DynTreeRoot(
					NatFloat.createUniform(1.5f, 0.5f), // baseWidth, 
					NatFloat.createUniform(0.15f, 0f), // rootEnd
					NatFloat.createUniform(0f, 0f), // rootSpacing
					NatFloat.createUniform(3f, 1f),  // numbranching 
					0.4f,                              // widthloss
					EvolvingNatFloat.createUniform(0f, NatFloat.PI*2, EnumTransformFunction.IDENTICAL, 0f),   // hor angle 
					EvolvingNatFloat.createUniform(NatFloat.PI-0.1f, 0.1f, EnumTransformFunction.LINEAR, -0.001f),  // ver angle
					NatFloat.createUniform(0f, 0f),                  // branch ver angle
					NatFloat.createInvGauss(0f, NatFloat.PI),      // branch hor angle
					NatFloat.createGauss(0.4f, 0.1f),  // branch spacing 
					NatFloat.createGauss(0.3f, 0.1f), //branchStart,  
					1f
				),
				new DynTreeTrunk(
					1f, 
					0.04f, 
					NatFloat.createUniform(0.5f, 0.05f),      // branchstart 
					NatFloat.createUniform(0f, 0f), 	      // branchspacing
					NatFloat.createUniform(NatFloat.PI + 0.6f, 0.2f),         // verticalangle
					NatFloat.createUniform(0, NatFloat.PI), // horizontalangle
					branching(3),
					//branchWidthMul(1f),
					EvolvingNatFloat.createUniform(0.3f, 0f, EnumTransformFunction.LINEAR, 0.0013f)
				),
				new DynTreeBranch(
					NatFloat.createUniform(NatFloat.PI + 0.6f, 0.1f),
					NatFloat.createUniform(0, NatFloat.PI), 
					NatFloat.createGauss(0.4f, 0f),
					NatFloat.createGauss(1f, 0f),
					0.03f,
					0.1f,
					0.5f
				),
				0.8f
			), null, null);
		
		
		
		// public DynTreeTrunk(float width, float widthloss, NatFloat branchStart, NatFloat branchSpacing, NatFloat branchVerticalAngle, NatFloat branchHorizontalAngle, EvolvingNatFloat numBranching, EvolvingNatFloat branchWidthMultiplier, float widthBranchLossBase, EvolvingNatFloat angleVert/*, EvolvingNatFloat bendAngleVert*/) {
		
		EnumTree.KAPOK.setGenerators(new DynTreeGen(
				EnumTree.KAPOK, 
				null,
				new DynTreeTrunk(
					1f, 
					0.04f, 
					NatFloat.createUniform(0.25f, 0.05f),      // branchstart 
					NatFloat.createUniform(0.4f, 0f), 	      // branchspacing
					NatFloat.createUniform(NatFloat.PI + 0.8f, 0.1f),         // verticalangle
					NatFloat.createUniform(0, NatFloat.PI), // horizontalangle
					//branching(5),
					EvolvingNatFloat.createUniform(5f, 0f, EnumTransformFunction.INVERSELINEAR, 0.01f),
					//branchWidthMul(1f),
					EvolvingNatFloat.createUniform(0.15f, 0f, EnumTransformFunction.LINEAR, 0.004f),
					1f, 
					EvolvingNatFloat.createUniform(0.1f, 0f, EnumTransformFunction.QUADRATIC, 0.0007f)
				),
				new DynTreeBranch(
					NatFloat.createUniform(0, 0.1f),
					NatFloat.createUniform(0, NatFloat.PI), 
					NatFloat.createGauss(0.85f, 0f),
					NatFloat.createGauss(0.14f, 0f),
					0.04f,
					EvolvingNatFloat.createIdentical(2f), //.createUniform(0.5f, 0f, Function.LINEAR, 0.5f),
					0.04f,
					0.6f,
					1f
				),
				0.8f,
				0,
				EnumTreeGenMode.RANDOMLENGTHBRANCHES
			), null, null);
		
		


		
		/**** REEEALY COOL TREE FOR SWAMP OR JUNGLE ****
		 * 


	EnumTree.PEAR.setGenerators(new DynTreeGen(
			EnumTree.PEAR,
			null,
			new DynTreeTrunk(
					1.5f, 
					0.13f, 
					NatFloat.createUniform(0.2f, 0.05f),     
					NatFloat.createUniform(0f, 0f), 	     
					NatFloat.createUniform(NatFloat.PI, 0.4f),   
					NatFloat.createUniform(0, NatFloat.PI),
					EvolvingNatFloat.createUniform(2f, 0, Function.LINEAR, 0.1f),
					//EvolvingNatFloat.createUniform(0.5f, 0, Function.LINEAR, 0.001f),
					branching(2),
					//branchWidthMul(0.7f),
					0.92f,
					EvolvingNatFloat.createUniform(0f, 0.4f, Function.LINEAR, 0f)
				),
				new DynTreeBranch(
					NatFloat.createUniform(0f, NatFloat.PI / 4),
					NatFloat.createInvGauss(0f, NatFloat.PI), 
					NatFloat.createGauss(0.2f, 0f),
					NatFloat.createGauss(0.1f, 0f),
					0.15f,
					EvolvingNatFloat.createUniform(2.5f, 0f, Function.LINEAR, 0.2f),
					0f,
					0.8f,
					1f
				),
				1.8f
		), null, null);
		

		ORRRRRRRRRRRRRRRRRRRRRRRRRRRR
		
		
		
			EnumTree.PEAR.setGenerators(new DynTreeGen(
			EnumTree.PEAR,
			null,
			new DynTreeTrunk(
					1.5f, 
					0.13f, 
					NatFloat.createUniform(0.2f, 0.05f),     
					NatFloat.createUniform(0f, 0f), 	     
					NatFloat.createUniform(NatFloat.PI, 0.4f),   
					NatFloat.createUniform(0, NatFloat.PI),
					EvolvingNatFloat.createUniform(2f, 0, Function.LINEAR, 0.1f),
					//EvolvingNatFloat.createUniform(0.5f, 0, Function.LINEAR, 0.001f),
					branching(2),
					//branchWidthMul(0.7f),
					0.92f,
					EvolvingNatFloat.createInvGauss(0f, 0.4f, Function.LINEAR, 0f)
				),
				new DynTreeBranch(
					NatFloat.createUniform(0f, NatFloat.PI / 4),
					NatFloat.createInvGauss(0f, NatFloat.PI), 
					NatFloat.createGauss(0.2f, 0f),
					NatFloat.createGauss(0.1f, 0f),
					0.15f,
					EvolvingNatFloat.createUniform(2.5f, 0f, Function.LINEAR, 0.2f),
					0.03f,
					0.8f,
					1f
				),
				1.8f
		), null, null);
		
		 * 
		 * 
		 * 
		 * 
		 */
		
		
		
		EnumTree.PEAR.setGenerators(new DynTreeGen(
			EnumTree.PEAR,
			null,
			new DynTreeTrunk(
					1.5f, 
					0.12f, 
					NatFloat.createUniform(0.25f, 0.05f),     
					NatFloat.createUniform(0f, 0f), 	     
					NatFloat.createUniform(NatFloat.PI, 0.4f),   
					NatFloat.createUniform(0, NatFloat.PI),
					//EvolvingNatFloat.createUniform(2f, 0, Function.LINEAR, 0.1f),
					branching(3),
					EvolvingNatFloat.createUniform(0.5f, 0, EnumTransformFunction.LINEAR, 0.001f),
					//branching(2),
					//branchWidthMul(0.7f),
					0.92f,
					EvolvingNatFloat.createInvGauss(0f, 0.3f, EnumTransformFunction.LINEAR, 0f)
				),
				new DynTreeBranch(
					NatFloat.createUniform(0f, NatFloat.PI / 4),
					NatFloat.createInvGauss(0f, NatFloat.PI), 
					NatFloat.createGauss(0.2f, 0f),
					NatFloat.createGauss(0.1f, 0f),
					0.15f,
					EvolvingNatFloat.createUniform(2.5f, 0f, EnumTransformFunction.LINEAR, 0.2f),
					-0.03f,
					0.8f,
					1f
				),
				1.8f
		), null, null);
		
		
		
		
		
		EnumTree.SPRUCE.setGenerators(new DynTreeGen(
			EnumTree.SPRUCE, 
			null,
			new DynTreeTrunk(
				1f, 
				0.03f, 
				NatFloat.createGauss(0.15f, 0.05f), 
				NatFloat.createGauss(0f, 0.1f), 
				NatFloat.createInvGauss(NatFloat.PI + 0.35f, 0), 
				NatFloat.createUniform(0, NatFloat.PI),
				branching(6), 
				branchWidthMul(0.25f)
			),
			new DynTreeBranch(
				NatFloat.createInvGauss(NatFloat.PI + 0.35f, 0),
				NatFloat.createUniform(0, NatFloat.PI), 
				NatFloat.createGauss(5f, 0.05f), 
				0.03f
			),
			0.8f
		), 
		new DynTreeGen(
				EnumTree.SPRUCE, 
				null,
				new DynTreeTrunk(
					1f, 
					0.04f, 
					NatFloat.createUniform(0.15f, 0.05f), 
					NatFloat.createUniform(0.12f, 0.1f), 
					NatFloat.createInvGauss(NatFloat.PI + 0.35f, 0), 
					NatFloat.createUniform(0, NatFloat.PI),
					branching(4), 
					branchWidthMul(0.25f),
					1f/*,
					NatFloat.createUniform(0f, 0f),
					EvolvingNatFloat.createUniform(0.1f, 0f, Function.QUADRATIC, 0.0005f)*/
				),
				new DynTreeBranch(
					NatFloat.createInvGauss(NatFloat.PI + 0.35f, 0),
					NatFloat.createUniform(0, NatFloat.PI), 
					NatFloat.createGauss(5f, 0.05f), 
					0.03f
				),
				0.65f
			), null);
		
		
		
		
		
		
		EnumTree.LARCH.setGenerators(
			new DynTreeGen(
					EnumTree.LARCH, 
					null,
					new DynTreeTrunk(
						1f, 
						0.04f, 
						NatFloat.createGauss(0.15f, 0.05f), 
						NatFloat.createUniform(0f, 0f), 
						NatFloat.createInvGauss(NatFloat.PI - 0.15f, 0), 
						NatFloat.createUniform(0, NatFloat.PI),
						branching(8), 
						EvolvingNatFloat.createUniform(0.5f, 0f, EnumTransformFunction.LINEAR, 0.0005f)
					),
					new DynTreeBranch(
						NatFloat.createInvGauss(NatFloat.PI - 0.35f, 0),
						NatFloat.createUniform(0, NatFloat.PI), 
						NatFloat.createGauss(5f, 0.05f), 
						0.08f
					),
					1f
				), 
			new DynTreeGen(
				EnumTree.LARCH, 
				null,
				new DynTreeTrunk(
					1f, 
					0.05f, 
					NatFloat.createGauss(0.15f, 0.05f), 
					NatFloat.createGauss(0f, 0.1f), 
					NatFloat.createInvGauss(NatFloat.PI - 0.15f, 0), 
					NatFloat.createUniform(0, NatFloat.PI),
					branching(4), 
					EvolvingNatFloat.createUniform(0.35f, 0f, EnumTransformFunction.LINEAR, 0.0006f)
				),
				new DynTreeBranch(
					NatFloat.createInvGauss(NatFloat.PI - 0.35f, 0),
					NatFloat.createUniform(0, NatFloat.PI), 
					NatFloat.createGauss(5f, 0.05f), 
					0.09f
				),
				0.9f
			), 
			null);
		
		
		
		
		
		EnumTree.MOUNTAINDOGWOOD.setGenerators(new DynTreeGen(
			EnumTree.MOUNTAINDOGWOOD, 
			null,
			new DynTreeTrunk(
				1f, 
				0.18f, 
				NatFloat.createGauss(0.4f, 0f), 
				NatFloat.createGauss(0f, 0f), 
				NatFloat.createInvGauss(NatFloat.PI + 0.5f, 0), 
				NatFloat.createUniform(0, NatFloat.PI),
				branching(12), 
				branchWidthMul(0.7f)
			),
			new DynTreeBranch(
				NatFloat.createInvGauss(NatFloat.PI + 0.5f, 0),
				NatFloat.createUniform(0, NatFloat.PI), 
				NatFloat.createGauss(0.01f, 0f), 
				0.13f
			),
			1f
		), null, null);
		
		
		
		EnumTree.OAK.setGenerators(new DynTreeGen(
				EnumTree.OAK, 
				null,
				new DynTreeTrunk(
					2.4f, 
					0.3f, 
					NatFloat.createGauss(0.35f, 0f), 
					NatFloat.createGauss(0f, 0f), 
					NatFloat.createInvGauss(NatFloat.PI - 0.25f, 0), 
					NatFloat.createUniform(0, NatFloat.PI),
					branching(10), 
					//EvolvingNatFloat.createUniform(0.1f, 0f, Function.QUADRATIC, 0.0008f)
					EvolvingNatFloat.createUniform(0.1f, 0f, EnumTransformFunction.LINEAR, 0.0007f)
				),
				new DynTreeBranch(
					NatFloat.createInvGauss(NatFloat.PI - 0.4f, 0.2f),
					NatFloat.createUniform(0, NatFloat.PI), 
					NatFloat.createGauss(0.01f, 0f), 
					0.1f
				),
				1.5f,
				1
			), null, null);

		
	
		
		EnumTree.AFRICANMAHOGANY.setGenerators(new DynTreeGen(
				EnumTree.AFRICANMAHOGANY, 
				null,
				new DynTreeTrunk(
					2f, 
					0.2f, 
					NatFloat.createGauss(0.35f, 0f), 
					NatFloat.createGauss(0f, 0f), 
					NatFloat.createInvGauss(NatFloat.PI - 0.25f, 0), 
					NatFloat.createUniform(0, NatFloat.PI),
					branching(8), 
					//EvolvingNatFloat.createUniform(0.1f, 0f, Function.QUADRATIC, 0.0008f)
					EvolvingNatFloat.createUniform(0.03f, 0f, EnumTransformFunction.LINEAR, 0.0003f)
				),
				new DynTreeBranch(
					NatFloat.createInvGauss(NatFloat.PI - 0.4f, 0.2f),
					NatFloat.createUniform(0, NatFloat.PI), 
					NatFloat.createGauss(0.01f, 0f), 
					0.04f
				),
				1.4f,
				1
			), null, null);

		
		
	
		EnumTree.BLACKWALNUT.setGenerators(new DynTreeGen(
				EnumTree.BLACKWALNUT, 
				null,
				new DynTreeTrunk(
					1.2f, 
					0.1f, 
					NatFloat.createGauss(0.4f, 0f), 
					NatFloat.createGauss(0f, 0f), 
					NatFloat.createInvGauss(NatFloat.PI + 0.2f, 0), 
					NatFloat.createUniform(0, NatFloat.PI),
					branching(4), 
					//EvolvingNatFloat.createUniform(0.1f, 0f, Function.QUADRATIC, 0.0008f)
					EvolvingNatFloat.createUniform(0.1f, 0f, EnumTransformFunction.LINEAR, 0.0004f)
				),
				new DynTreeBranch(
					NatFloat.createInvGauss(NatFloat.PI + 0.4f, 0.2f),
					NatFloat.createUniform(0, NatFloat.PI),
					NatFloat.createGauss(0.5f, 0f),
					NatFloat.createGauss(0.1f, 0f), 
					0.02f,
					0f,
					1f
				),
				1f,
				2
			), null, null);

		
		
		EnumTree.POPLAR.setGenerators(new DynTreeGen(
				EnumTree.POPLAR, 
				null,
				new DynTreeTrunk(
					1f, 
					0.09f, 
					NatFloat.createGauss(0.28f, 0f), 
					NatFloat.createGauss(0f, 0f), 
					NatFloat.createInvGauss(NatFloat.PI + 0.7f, 0), 
					NatFloat.createUniform(0, NatFloat.PI),
					branching(12), 
					//EvolvingNatFloat.createUniform(0.1f, 0f, Function.QUADRATIC, 0.0008f)
					//EvolvingNatFloat.createUniform(0.7f, 0f, Function.LINEAR, 0.00005f)
					EvolvingNatFloat.createUniform(0.45f, 0f, EnumTransformFunction.LINEAR, 0.00005f)
				),
				new DynTreeBranch(
					NatFloat.createInvGauss(NatFloat.PI, 0.2f),
					NatFloat.createUniform(0, NatFloat.PI), 
					NatFloat.createGauss(0.1f, 0f), 
					0.13f	
				),
				1.7f,
				1
			), null, null);
		
		
		// Reference: 
		/* new DynTreeTrunk(avgHeight, width, widthloss, branchStart, branchSpacing, branchVarianceSpacing, variance, numBranching, branchWidthMultiplier, widthBranchLossBase),
		 * new DynTreeBranch(verticalAngle, horizontalAngle, branchStart, spacing, widthloss, gravitydrag, branchWidthMultiplier)
   		 */
		
		//new DynTreeTrunk(width, widthloss, branchStart, branchSpacing, verticalAngle, horizontalAngle, numBranching, branchWidthMultiplier, widthBranchLossBase)
		//new DynTreeTrunk(width, widthloss, branchStart, branchSpacing, verticalAngle, horizontalAngle, numBranching, branchWidthMultiplier, widthBranchLossBase, angleVert)
		
		EnumTree.MYRTLEBEECH.setGenerators(new DynTreeGen(
				EnumTree.MYRTLEBEECH, 
				null,
				new DynTreeTrunk(
					1f, 
					0.15f, 
					NatFloat.createGauss(0.27f, 0f), 
					NatFloat.createUniform(0.25f, 0.1f), 
					NatFloat.createUniform(NatFloat.PI + 0.4f, 0.3f), 
					NatFloat.createUniform(0, NatFloat.PI),
					branching(2),
					//branchWidthMul(3f),
					EvolvingNatFloat.createUniform(1.5f, 0f, EnumTransformFunction.LINEAR, 0.0007f),
					1f,
					EvolvingNatFloat.createUniform(0.4f, 0.1f, EnumTransformFunction.COSINUS, 0.001f)
				),
				new DynTreeBranch(
					NatFloat.createUniform(NatFloat.PI + 0.4f, 0.5f),
					NatFloat.createUniform(0, NatFloat.PI),
					NatFloat.createGauss(0.2f, 0f),
					NatFloat.createGauss(0.08f, 0.04f), 
					0.3f,
					0.1f,
					0.25f
				),
				2f,
				0
			), null,
			new DynTreeGen(
				EnumTree.MYRTLEBEECH, 
				null,
				new DynTreeTrunk(
					1f, 
					0.1f, 
					NatFloat.createGauss(0.4f, 0f), 
					NatFloat.createGauss(0f, 0f), 
					NatFloat.createInvGauss(NatFloat.PI + 0.55f, 0), 
					NatFloat.createUniform(0, NatFloat.PI),
					branching(2),
					EvolvingNatFloat.createUniform(0.5f, 0f, EnumTransformFunction.LINEAR, 0.0007f)
				),
				new DynTreeBranch(
					NatFloat.createGauss(NatFloat.PI, 0.5f),
					NatFloat.createUniform(0, NatFloat.PI),
					NatFloat.createGauss(0.6f, 0f),
					NatFloat.createGauss(0.1f, 0f), 
					0.1f,
					0.02f,
					0.5f
				),
				2.2f,
				1
			)
		);
		
		
		
		
		EnumTree.ACACIA.setGenerators(new DynTreeGen(
			EnumTree.ACACIA,
			null,
			new DynTreeTrunk(
					1.5f, 
					0.13f, 
					NatFloat.createUniform(0.25f, 0.05f),     
					NatFloat.createUniform(0.1f, 0f), 	     
					NatFloat.createUniform(NatFloat.PI + 0.45f, 0f),   
					NatFloat.createUniform(0, NatFloat.PI),
					EvolvingNatFloat.createUniform(2f, 0, EnumTransformFunction.LINEAR, 0.1f),
					EvolvingNatFloat.createUniform(0.6f, 0, EnumTransformFunction.LINEAR, 0.001f),
					//branchWidthMul(0.7f),
					0.92f,
					EvolvingNatFloat.createInvGauss(0f, 0.45f, EnumTransformFunction.LINEAR, 0.0017f)
				),
				new DynTreeBranch(
					NatFloat.createGauss(0f, 0.2f),
					NatFloat.createInvGauss(0f, NatFloat.PI), 
					NatFloat.createGauss(0.4f, 0f),
					NatFloat.createGauss(5f, 0f),
					0.1f,
					EvolvingNatFloat.createUniform(2f, 0f, EnumTransformFunction.LINEAR, 1f),
					0f,
					0.8f,
					1f
				),
				1.45f
			),
			null,
			new DynTreeGen(
			EnumTree.ACACIA, 
			null,
			new DynTreeTrunk(
				1f,
				0.15f,
				NatFloat.createGauss(0.50f, 0.15f), 
				NatFloat.createUniform(0f, 0f), 
				NatFloat.createGauss(NatFloat.PI + 0.15f, 0f), 
				NatFloat.createUniform(0, NatFloat.PI),
				branching(8), 
				branchWidthMul(1.3f),
				1f,
				EvolvingNatFloat.createInvGauss(0f, 0.2f, EnumTransformFunction.LINEAR, 0.0005f)
				/*,
				NatFloat.createUniform(0.3f, 0.3f),
				EvolvingNatFloat.createUniform(0f, 0f, Function.QUADRATIC, -0.00027f)	*/			
			),
			new DynTreeBranch(
				NatFloat.createUniform(0, 0.5f),
				NatFloat.createUniform(0, NatFloat.PI), 
				NatFloat.createGauss(0f, 0f),
				NatFloat.createUniform(0.05f, 0.01f), 
				0.14f,
				0.01f,
				0.4f
			),
			1.5f
		)
		);
		
		EnumTree.SCOTSPINE.setGenerators(
			// Normal
			new DynTreeGen(
				EnumTree.SCOTSPINE, 
				null,
				new DynTreeTrunk(
					1f,
					0.02f,
					NatFloat.createGauss(0.5f, 0.15f), 
					NatFloat.createUniform(0.15f, 0.005f), 
					NatFloat.createGauss(NatFloat.PI, 0.3f), 
					NatFloat.createUniform(0, NatFloat.PI),
					branching(2),
					EvolvingNatFloat.createUniform(0.8f, 0f, EnumTransformFunction.LINEAR, 0.00008f),
					//branchWidthMul(0.9f),
					1f
				),
				new DynTreeBranch(
					NatFloat.createUniform(0, 0.5f),
					NatFloat.createUniform(0, NatFloat.PI), 
					NatFloat.createGauss(0f, 0f),
					NatFloat.createUniform(0.25f, 0.1f), 
					0.02f,
					0.08f,
					0.4f
				),
				0.5f
			),
			
			
			// Reference: 
			/* new DynTreeTrunk(avgHeight, width, widthloss, branchStart, branchSpacing, branchVarianceSpacing, variance, numBranching, branchWidthMultiplier, widthBranchLossBase),
			 * new DynTreeBranch(verticalAngle, horizontalAngle, branchStart, spacing, widthloss, gravitydrag, branchWidthMultiplier)
	   		 */

			
			// Poor
			new DynTreeGen(
					EnumTree.SCOTSPINE, 
					null,
					new DynTreeTrunk(
						1f,
						0.15f,
						NatFloat.createGauss(0.50f, 0.15f), 
						NatFloat.createUniform(0f, 0f), 
						NatFloat.createGauss(NatFloat.PI + 0.55f, 0f), 
						NatFloat.createUniform(0, NatFloat.PI),
						branching(4), 
						branchWidthMul(1f),
						1f,
						EvolvingNatFloat.createInvGauss(0, 2f, EnumTransformFunction.LINEARREDUCE, 0.0007f)
					),
					new DynTreeBranch(
						NatFloat.createUniform(0, 0.5f),
						NatFloat.createUniform(0, NatFloat.PI), 
						NatFloat.createGauss(0.2f, 0f),
						NatFloat.createUniform(0.2f, 0.01f), 
						0.2f,
						0.01f,
						0.4f
					),
					1.7f
			),
			// Lush
			new DynTreeGen(
				EnumTree.SCOTSPINE, 
				null,
				new DynTreeTrunk(
					1f, 
					0.02f, 
					NatFloat.createGauss(0.3f, 0.1f), 
					NatFloat.createUniform(0.025f, 0.01f), 
					NatFloat.createGauss(NatFloat.PI, 0.3f), 
					NatFloat.createUniform(0, NatFloat.PI),
					branching(2), 
					branchWidthMul(0.5f)
				),
				new DynTreeBranch(
					NatFloat.createUniform(0, 0.5f),
					NatFloat.createUniform(0, NatFloat.PI), 
					NatFloat.createGauss(0f, 0f),
					NatFloat.createUniform(0.15f, 0.08f), 
					0.027f,
					0.1f,
					0.5f
				),
				0.5f
			)
		);
		
		
		
		
		EnumTree.ELEPHANTTREE.setGenerators(
			new DynTreeGen(
					EnumTree.ELEPHANTTREE, 
					null,
					new DynTreeTrunk(
						1f,
						0.15f,
						NatFloat.createGauss(0.30f, 0.15f), 
						NatFloat.createUniform(0f, 0f), 
						NatFloat.createGauss(NatFloat.PI + 0.55f, 0f), 
						NatFloat.createUniform(0, NatFloat.PI),
						branching(4), 
						branchWidthMul(1f),
						1f,
						EvolvingNatFloat.createInvGauss(0, 2f, EnumTransformFunction.LINEARREDUCE, 0.0007f)

					),
					new DynTreeBranch(
						NatFloat.createUniform(5f, 0.5f),
						NatFloat.createUniform(0, NatFloat.PI), 
						NatFloat.createGauss(0.3f, 0f),
						NatFloat.createUniform(0.2f, 0.01f), 
						0.3f,
						0.01f,
						0.4f
					),
					1.5f
			), null, null
		);
		
		
		
		
		
		EnumTree.JOSHUA.setGenerators(
			new DynTreeGen(
					EnumTree.JOSHUA, 
					null,
					new DynTreeTrunk(
						3.5f,
						0.4f,
						NatFloat.createGauss(0.3f, 0.15f), 
						NatFloat.createUniform(0.2f, 0f), 
						NatFloat.createUniform(NatFloat.PI + 0.7f, 0f), 
						NatFloat.createUniform(0, NatFloat.PI),
						branching(3), 
						branchWidthMul(1f),
						1f,
						EvolvingNatFloat.createGauss(0, 0.2f, EnumTransformFunction.IDENTICAL, 0f)

					),
					new DynTreeBranch(
						NatFloat.createUniform(NatFloat.PI + 0.7f, 0f),
						NatFloat.createUniform(0, NatFloat.PI), 
						NatFloat.createUniform(0.95f, 0f),
						NatFloat.createUniform(5f, 0f), 
						0.8f,
						-0.8f,
						0f
					),
					0.7f
			), null, null
		);
		
		
		
		EnumTree.COYOTEWILLOW.setGenerators(
			new DynTreeGen(
				EnumTree.COYOTEWILLOW, 
				null,
				new DynTreeTrunk(
					1.5f,
					0.3f,
					NatFloat.createUniform(0.1f, 0f), 
					NatFloat.createUniform(0f, 0f), 
					NatFloat.createUniform(NatFloat.PI + 0.4f, 0.25f), 
					NatFloat.createUniform(0, NatFloat.PI),
					branching(5), 
					branchWidthMul(1f)
				),
				new DynTreeBranch(
					NatFloat.createUniform(5f, 0.5f),
					NatFloat.createUniform(0, NatFloat.PI), 
					NatFloat.createUniform(5f, 0f),
					NatFloat.createUniform(5f, 0f), 
					0.3f,
					0f,
					1f
				),
				0.7f,
				0,
				EnumTreeGenMode.BUSH
			), 
			null, 
			new DynTreeGen(
				EnumTree.COYOTEWILLOW, 
				null,
				new DynTreeTrunk(
					1.5f,
					0.25f,
					NatFloat.createUniform(0.2f, 0f), 
					NatFloat.createUniform(0f, 0f), 
					NatFloat.createUniform(NatFloat.PI + 0.8f, 0.25f), 
					NatFloat.createUniform(0, NatFloat.PI),
					branching(5), 
					branchWidthMul(1f)
				),
				new DynTreeBranch(
					NatFloat.createUniform(5f, 0.5f),
					NatFloat.createUniform(0, NatFloat.PI), 
					NatFloat.createUniform(5f, 0f),
					NatFloat.createUniform(5f, 0f), 
					0.17f,
					0f,
					1f
				),
				0.7f,
				0,
				EnumTreeGenMode.BUSH
			)
		);
		
		
		EnumTree.WEEPINGWILLOW.setGenerators(
			new DynTreeGen(
				EnumTree.WEEPINGWILLOW, 
				null,
				new DynTreeTrunk(
					1f,
					0.06f,
					NatFloat.createUniform(0.23f, 0f), 
					NatFloat.createUniform(0f, 0f), 
					NatFloat.createUniform(5*NatFloat.PI / 4, 0.25f), 
					NatFloat.createUniform(0, NatFloat.PI),
					branching(7), 
					EvolvingNatFloat.createUniform(0.01f, 0f, EnumTransformFunction.LINEAR, 0.0008f),
					1f,
					EvolvingNatFloat.createInvGauss(0, 0.25f, EnumTransformFunction.COSINUS, 0.0004f)

				),
				new DynTreeBranch(
					NatFloat.createUniform(5f, 0.5f),
					NatFloat.createUniform(0, NatFloat.PI), 
					NatFloat.createUniform(0.7f, 0.3f),
					NatFloat.createUniform(0.7f, 0.5f), 
					0.008f,
					0.23f,
					0.2f
				),
				0.4f
			),
			null,
			new DynTreeGen(
				EnumTree.WEEPINGWILLOW, 
				null,
				new DynTreeTrunk(
					1f,
					0.06f,
					NatFloat.createUniform(0.23f, 0f), 
					NatFloat.createUniform(0f, 0f), 
					NatFloat.createUniform(5*NatFloat.PI / 4, 0.25f), 
					NatFloat.createUniform(0, NatFloat.PI),
					branching(7), 
					EvolvingNatFloat.createUniform(0.01f, 0f, EnumTransformFunction.LINEAR, 0.0008f),
					1f,
					EvolvingNatFloat.createInvGauss(0, 0.25f, EnumTransformFunction.COSINUS, 0.0004f)

				),
				new DynTreeBranch(
					NatFloat.createUniform(5f, 0.5f),
					NatFloat.createUniform(0, NatFloat.PI), 
					NatFloat.createUniform(0.7f, 0.3f),
					NatFloat.createUniform(0.7f, 0.5f), 
					0.008f,
					0.23f,
					0.2f
				),
				0.75f
			)
			);

		
		// Reference: 
		/* new DynTreeTrunk(width, widthloss, branchStart, branchSpacing, verticalAngle, horizontalAngle, numBranching, branchWidthMultiplier, widthBranchLossBase, bend, bendCorrection)
		 * new DynTreeBranch(verticalAngle, horizontalAngle, branchStart, spacing, widthloss, gravitydrag)
   		 */
		/*
		
		EnumTree.COCONUTPALM.setGenerators(new DynTreeGen(
			EnumTree.COCONUTPALM, 
			null,
			trunk = new DynTreeTrunk(
				1f, 
				0.07f, 
				NatFloat.createGauss(0.25f, 0.01f), 
				NatFloat.createGauss(0f, 0f), 
				NatFloat.createUniform(NatFloat.PI / 4, 0), 
				NatFloat.createUniform(0, 2*NatFloat.PI),
				16, 
				1f,
				0.3f,
				0.5f,
				0.05f
			),
			branch = new DynTreeBranch(
				NatFloat.createInvGauss(NatFloat.PI / 4 + 0.1f, NatFloat.PI / 8),
				NatFloat.createUniform(0, 2*NatFloat.PI), 
				NatFloat.createUniform(4f, 0),
				NatFloat.createGauss(5f, 0f), 
				0.05f,
				0.2f
			)
		), null, null);*/
		 
	}	

}
