package at.tyron.vintagecraft.WorldGen;

import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import at.tyron.vintagecraft.BlockClass.TreeClass;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.block.BlockLeavesVC;
import at.tyron.vintagecraft.block.BlockLeavesBranchy;
import at.tyron.vintagecraft.block.BlockLogVC;


public class DynTreeGen {
	public static Block vineBlock = BlocksVC.vine; 
	
	// "Temporary Values" linked to currently generated tree
	transient World world;
	transient BlockPos pos;
	transient float size;
	int yOffset;
	int vineGrowthChance; // 0..100%
	
	//public float bend = 0f;
	Random rand = new Random();
	
	public EnumTree tree;
	
	// "Permanent Values" Linked to tree type
	public float sizemodifier;
	public DynTreeRoot roots;
	public DynTreeTrunk trunk;
	public DynTreeBranch branches;
	
	public IBlockState log;
	public IBlockState leavesbranchy;
	public IBlockState leaves;
	
	
	
	public DynTreeGen(EnumTree treetype, DynTreeRoot roots, DynTreeTrunk trunk, DynTreeBranch branches) {
		this(treetype, roots, trunk, branches, 1.0f, 0);
	}

	public DynTreeGen(EnumTree treetype, DynTreeRoot roots, DynTreeTrunk trunk, DynTreeBranch branches, float sizemodifier) {
		this(treetype, roots, trunk, branches, sizemodifier, 0);
	}

	public DynTreeGen(EnumTree treetype, DynTreeRoot roots, DynTreeTrunk trunk, DynTreeBranch branches, float sizemodifier, int yOffset) {
		this.roots = roots;
		this.trunk = trunk;
		this.branches = branches;
		this.sizemodifier = sizemodifier;
		this.yOffset = yOffset;
		/*this.bend = bend;*/
		this.tree = treetype;
		setTree(treetype);
	}
	
	public void growTree(World world, BlockPos pos) {
		growTree(world, pos, 1f);
	}

	public void growTree(World world, BlockPos pos, float size) {
		growTree(world, pos, size, 0);
	}
	public void growTree(World world, BlockPos pos, float size, int vineGrowthChance) {
		this.world = world;
		this.pos = pos;
		this.size = size * sizemodifier;
		this.vineGrowthChance = vineGrowthChance;
		
		if (yOffset > 0) {
			this.pos = pos.down(yOffset);
		}
		
		//float relheight = trunk.avgHeight; // getWithGaussVariance(world.rand, trunk.avgHeight, trunk.variance);
		float relwidth = trunk.width;
		
		genTrunk(0f, 0f, 0f, trunk.width, /*relheight,*/ (float)(world.rand.nextFloat() * Math.PI * 2), trunk.bend, trunk.bendCorrection);
		genRoots();
	}
	
	

	private void genRoots() {
		
	}



	private void genTrunk(float curx, float cury, float curz, float curwidth, /*float relheight, */float anglehor, float anglever, float angleverReduction) {
		float numBranches = Math.round((float)trunk.numBranching * (size / 2) + (float)trunk.numBranching / 2); 
		//System.out.println(numBranches + " / " + trunk.numBranching);
		//growBranch(pos, 0f, 0f, 0f, anglever, anglehor, trunk.width, trunk.widthloss, (int) numBranches, trunk.branchStart, trunk.branchSpacing, trunk.branchVarianceSpacing, trunk.branchWidthMultiplier, 0f, trunk.branchAnglevert, trunk.branchVarianceAnglevert, trunk.branchAnglehori, trunk.branchVarianceAnglehori);
		
		growBranch(pos, 0f, 0f, 0f, anglever, angleverReduction, anglehor, trunk.width, trunk.widthloss, numBranches, trunk.branchStart, trunk.branchSpacing, trunk.branchWidthMultiplier, 0f, trunk.branchVerticalAngle, trunk.branchHorizontalAngle, trunk.widthBranchLossBase);
	}
	
	
	
	public void growBranch(BlockPos pos, float dx, float dy, float dz, float anglever, float angleverReduction, float anglehor, float baseWidth, float widthloss, float numbranching, NatFloat branchStart, NatFloat branchSpacing, float branchWidthMultiplier, float gravityDrag, NatFloat branchVerticalAngle, NatFloat branchHorizontalAngle, float widthBranchLossBase) {
		//if (numbranching > 1)
		//	System.out.println(angleverReduction);
//			System.out.println("spacing = "+avgSpacing+" +- " + varianceSpacing);
	
		float branchspacing = branchSpacing.nextFloat();
		float branchstart = branchStart.nextFloat();
		
		float reldistance = 0, lastreldistance = 0;
		float totaldistance = baseWidth / (widthloss / size);
		
		int iterations = 5000;
		
		while (baseWidth > 0 && iterations-- > 0) {
			baseWidth -= widthloss / size;
			if (angleverReduction > 0) {
				if (anglever > 0) {
					anglever = Math.max(0, anglever - angleverReduction);
				}
				if (anglever < 0) {
					anglever = Math.min(0, anglever + angleverReduction);
				}
			}
			
			
			dx += MathHelper.sin(anglever) * MathHelper.cos(anglehor);
			dy += MathHelper.cos(anglever) - Math.min(1.8f, gravityDrag * MathHelper.sqrt_float(dx*dx + dz*dz));
			dz += MathHelper.sin(anglever) * MathHelper.sin(anglehor);
			
			
			IBlockState blockstate = block(baseWidth);

			if (canPlace(blockstate, world.getBlockState(pos.add(dx, dy, dz)))) {
				world.setBlockState(pos.add(dx, dy, dz), blockstate, 2);
				
				if (vineGrowthChance > 0 && rand.nextInt(100) < vineGrowthChance) {
					EnumFacing facing = EnumFacing.getHorizontal(rand.nextInt(4));
					
					BlockPos vinePos = pos.add(dx, dy, dz).offset(facing);
					int cnt = 1 + rand.nextInt(11);
					
					while (world.isAirBlock(vinePos) && cnt-- > 0) {
						world.setBlockState(vinePos, vineBlock.getDefaultState().withProperty(BlockVine.getPropertyFor(facing.getOpposite()), true), 2);
						vinePos = vinePos.down();
					}
				}
			}
			
			
			reldistance = MathHelper.sqrt_float(dx*dx + dy*dy + dz*dz) / totaldistance;
			
			
			
			if (reldistance < branchstart) continue;
			
			if (reldistance > lastreldistance + branchspacing * (1f - reldistance)) {
				//if (numbranching > 1) System.out.println(reldistance+" > "+(lastreldistance + branchspacing));
				
				branchspacing = branchSpacing.nextFloat();
				lastreldistance = reldistance;
				
				//branchsize *= branches.splitloss;
				if (numbranching > 0) baseWidth *= widthBranchLossBase;
				
				for (int i = 0; i < numbranching; i++) {
					growBranch(
						pos, dx, dy, dz, 
						anglever + branchVerticalAngle.nextFloat(),
						0f,
						anglehor + branchHorizontalAngle.nextFloat(), 
						baseWidth * branchWidthMultiplier, 
						branches.widthloss, 
						1,
						branches.branchStart, 
						branches.branchSpacing, 
						branches.branchWidthMultiplier,
						branches.gravityDrag,
						branches.branchVerticalAngle, 
						branches.branchHorizontalAngle,
						branches.widthBranchLossBase
					);
				}
			}
		}		
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
	
	
	boolean canPlace(IBlockState blockstate, IBlockState blockatpos) {
		// Logs override any leaves
		// branchy leaves override leaves
		return 
			blockatpos.getBlock() == Blocks.air || blockatpos.getBlock() == Blocks.vine 
			|| blockatpos.getBlock() == BlocksVC.tallgrass
			|| (blockstate == log && (blockatpos.getBlock() instanceof BlockLeavesVC))
			|| (blockstate == leavesbranchy && (blockatpos.getBlock() instanceof BlockLeavesVC) && !(blockatpos.getBlock() instanceof BlockLeavesBranchy))
		;
	
	}
	
	
	
	
	public static void initGenerators() {
		// Reference: 
		/* new DynTreeTrunk(avgHeight, width, widthloss, branchStart, branchSpacing, branchVarianceSpacing, variance, numBranching, branchWidthMultiplier),
		 * new DynTreeBranch(anglevert, varianceAnglevert, anglehori, varianceAnglehori, spacing, varianceSpacing, widthloss, gravityDrag)
		 * new DynTreeBranch(NatFloat verticalAngle, NatFloat horizontalAngle, NatFloat spacing, float widthloss)
   		 */
		DynTreeGen test;
		DynTreeTrunk trunk;
		DynTreeBranch branch;
		
		EnumTree.BIRCH.setGenerators(test = new DynTreeGen(
			EnumTree.BIRCH, 
			null,
			trunk = new DynTreeTrunk(
//				0.8f,
				1f, 
				0.05f, 
				NatFloat.createGauss(0.22f, 0.01f), 
				NatFloat.createGauss(0.005f, 0.1f), 
				NatFloat.createInvGauss(NatFloat.PI / 4 + 0.1f, NatFloat.PI / 8), 
				NatFloat.createUniform(0, 2*NatFloat.PI),
				3, 
				0.5f
			),
			branch = new DynTreeBranch(
				NatFloat.createInvGauss(NatFloat.PI / 4 + 0.1f, NatFloat.PI / 8),
				NatFloat.createUniform(0, 2*NatFloat.PI), 
				NatFloat.createGauss(0.01f, 0.01f), 
				0.06f
			)
		), null, null);
		
		/*GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(IBlockState.class, new BlockStateSerializer());
        
        Gson gson = builder.create();
        
        System.out.println(gson.toJson(test));
		*/
		
		// Generates Trees like TFC Douglasfir 
		/*
		EnumTree.PURPLEHEARTWOOD.setGenerators(new DynTreeGen(
				EnumTree.PURPLEHEARTWOOD, 
				null,
				new DynTreeTrunk(
					1.2f,
					1f, 
					0.03f, 
					NatFloat.createUniform(0.5f, 0.05f),      // branchstart 
					NatFloat.createUniform(0f, 0f), 	      // branchspacing
					NatFloat.createGauss(0.7f, 0.4f),         // verticalangle
					NatFloat.createUniform(0, 2*NatFloat.PI), // horizontalangle
					16,
					1f,
					1f,
					0f,
					0f
				),
				new DynTreeBranch(
					NatFloat.createGauss(0.2f, 0.2f),
					NatFloat.createUniform(0, 2*NatFloat.PI), 
					NatFloat.createGauss(0f, 0f),
					NatFloat.createGauss(0f, 0f),
					0.1f,
					0f
				),
				0.8f
			), null, null);
		*/
		
		//new DynTreeTrunk(avgHeight, width, widthloss, branchStart, branchSpacing, verticalAngle, horizontalAngle, numBranching, branchWidthMultiplier, widthBranchLossBase, bend, bendCorrection)
		//new DynTreeBranch(verticalAngle, horizontalAngle, branchStart, spacing, widthloss, gravitydrag, branchWidthMultiplier)
		//DynTreeBranch(NatFloat verticalAngle, NatFloat horizontalAngle, NatFloat branchStart, NatFloat spacing, float widthloss, float gravitydrag)
		
		EnumTree.CRIMSONKINGMAPLE.setGenerators(new DynTreeGen(
			EnumTree.CRIMSONKINGMAPLE,
			null,
			new DynTreeTrunk(
				1f, 
				0.1f, 
				NatFloat.createGauss(0.5f, 0f), 
				NatFloat.createGauss(0f, 0f), 
				NatFloat.createInvGauss(NatFloat.PI / 2 - 0.55f, 0), 
				NatFloat.createUniform(0, 2*NatFloat.PI),
				8, 
				1f
			),
			new DynTreeBranch(
				NatFloat.createInvGauss(NatFloat.PI / 2 - 0.35f, 0),
				NatFloat.createUniform(0, 2*NatFloat.PI), 
				NatFloat.createGauss(0.01f, 0f), 
				0.17f
			),
			1.5f
		), null, null);
		
		EnumTree.PURPLEHEARTWOOD.setGenerators(new DynTreeGen(
				EnumTree.PURPLEHEARTWOOD, 
				null,
				new DynTreeTrunk(
//					1.15f,
					1f, 
					0.03f, 
					NatFloat.createUniform(0.4f, 0.05f),      // branchstart 
					NatFloat.createUniform(0f, 0f), 	      // branchspacing
					NatFloat.createGauss(0.7f, 0.4f),         // verticalangle
					NatFloat.createUniform(0, 2*NatFloat.PI), // horizontalangle
					8,
					1f,
					0.92f,
					0f,
					0f
				),
				new DynTreeBranch(
					NatFloat.createGauss(0.3f, 0.2f),
					NatFloat.createUniform(0, 2*NatFloat.PI), 
					NatFloat.createGauss(0.4f, 0f),
					NatFloat.createGauss(1f, 0f),
					0.05f,
					0f
				),
				0.8f
			), null, null);
		
		
		//new DynTreeTrunk(avgHeight, width, widthloss, branchStart, branchSpacing, verticalAngle, horizontalAngle, numBranching, branchWidthMultiplier, widthBranchLossBase, bend, bendCorrection)
		//new DynTreeBranch(verticalAngle, horizontalAngle, branchStart, spacing, widthloss, gravitydrag, branchWidthMultiplier)
		//DynTreeBranch(NatFloat verticalAngle, NatFloat horizontalAngle, NatFloat branchStart, NatFloat spacing, float widthloss, float gravitydrag)

		EnumTree.SPRUCE.setGenerators(new DynTreeGen(
			EnumTree.SPRUCE, 
			null,
			new DynTreeTrunk(
//				0.8f,
				1f, 
				0.03f, 
				NatFloat.createGauss(0.15f, 0.05f), 
				NatFloat.createGauss(0f, 0.1f), 
				NatFloat.createInvGauss(NatFloat.PI / 2 - 0.35f, 0), 
				NatFloat.createUniform(0, 2*NatFloat.PI),
				10, 
				0.25f
			),
			new DynTreeBranch(
				NatFloat.createInvGauss(NatFloat.PI / 2 - 0.35f, 0),
				NatFloat.createUniform(0, 2*NatFloat.PI), 
				NatFloat.createGauss(5f, 0.05f), 
				0.03f
			),
			0.8f
		), 
		new DynTreeGen(
				EnumTree.SPRUCE, 
				null,
				new DynTreeTrunk(
	//				0.1f,
					1f, 
					0.04f, 
					NatFloat.createUniform(0.15f, 0.05f), 
					NatFloat.createUniform(0.12f, 0.1f), 
					NatFloat.createInvGauss(NatFloat.PI / 2 - 0.35f, 0), 
					NatFloat.createUniform(0, 2*NatFloat.PI),
					4, 
					0.25f,
					1f,
					0.05f,
					0.4f
				),
				new DynTreeBranch(
					NatFloat.createInvGauss(NatFloat.PI / 2 - 0.35f, 0),
					NatFloat.createUniform(0, 2*NatFloat.PI), 
					NatFloat.createGauss(5f, 0.05f), 
					0.03f
				),
				0.65f
			), null);
		
		EnumTree.MOUNTAINDOGWOOD.setGenerators(new DynTreeGen(
			EnumTree.MOUNTAINDOGWOOD, 
			null,
			new DynTreeTrunk(
	//			0.9f,
				1f, 
				0.15f, 
				NatFloat.createGauss(0.5f, 0f), 
				NatFloat.createGauss(0f, 0f), 
				NatFloat.createInvGauss(NatFloat.PI / 2 - 0.55f, 0), 
				NatFloat.createUniform(0, 2*NatFloat.PI),
				8, 
				1f
			),
			new DynTreeBranch(
				NatFloat.createInvGauss(NatFloat.PI / 2 - 0.55f, 0),
				NatFloat.createUniform(0, 2*NatFloat.PI), 
				NatFloat.createGauss(0.01f, 0f), 
				0.17f
			),
			0.8f
		), null, null);
		
		
		
		EnumTree.OAK.setGenerators(new DynTreeGen(
				EnumTree.OAK, 
				null,
				new DynTreeTrunk(
//					0.9f,
					1f, 
					0.15f, 
					NatFloat.createGauss(0.5f, 0f), 
					NatFloat.createGauss(0f, 0f), 
					NatFloat.createInvGauss(NatFloat.PI / 2 - 0.55f, 0), 
					NatFloat.createUniform(0, 2*NatFloat.PI),
					8, 
					1f
				),
				new DynTreeBranch(
					NatFloat.createInvGauss(NatFloat.PI / 2 - 0.55f, 0),
					NatFloat.createUniform(0, 2*NatFloat.PI), 
					NatFloat.createGauss(0.01f, 0f), 
					0.17f
				),
				2f
			), null, null);
		
		
		
		// Reference: 
		/* new DynTreeTrunk(avgHeight, width, widthloss, branchStart, branchSpacing, branchVarianceSpacing, variance, numBranching, branchWidthMultiplier, widthBranchLossBase),
		 * new DynTreeBranch(verticalAngle, horizontalAngle, branchStart, spacing, widthloss, gravitydrag, branchWidthMultiplier)
   		 */
		
		
		EnumTree.MYRTLEBEECH.setGenerators(new DynTreeGen(
				EnumTree.MYRTLEBEECH, 
				null,
				new DynTreeTrunk(
					1f, 
					0.15f, 
					NatFloat.createGauss(0.2f, 0f), 
					NatFloat.createGauss(0f, 0f), 
					NatFloat.createInvGauss(NatFloat.PI / 2 - 0.55f, 0), 
					NatFloat.createUniform(0, 2*NatFloat.PI),
					1,
					3f
				),
				new DynTreeBranch(
					NatFloat.createUniform(NatFloat.PI / 2 - 0.4f, 0.5f),
					NatFloat.createUniform(0, 2*NatFloat.PI),
					NatFloat.createGauss(0.2f, 0f),
					NatFloat.createGauss(0.1f, 0f), 
					0.5f,
					-0.08f,
					0.25f
				),
				1.8f,
				1
			), null,
			new DynTreeGen(
				EnumTree.MYRTLEBEECH, 
				null,
				new DynTreeTrunk(
					1f, 
					0.1f, 
					NatFloat.createGauss(0.4f, 0f), 
					NatFloat.createGauss(0f, 0f), 
					NatFloat.createInvGauss(NatFloat.PI / 2 - 0.55f, 0), 
					NatFloat.createUniform(0, 2*NatFloat.PI),
					1,
					2f
				),
				new DynTreeBranch(
					NatFloat.createGauss(NatFloat.PI / 2, 0.5f),
					NatFloat.createUniform(0, 2*NatFloat.PI),
					NatFloat.createGauss(0.6f, 0f),
					NatFloat.createGauss(0.1f, 0f), 
					0.5f,
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
//				1f,
				1f,
				0.15f,
				NatFloat.createGauss(0.50f, 0.15f), 
				NatFloat.createUniform(0f, 0f), 
				NatFloat.createGauss(NatFloat.PI / 2 - 0.55f, 0f), 
				NatFloat.createUniform(0, 2*NatFloat.PI),
				4, 
				1f,
				1f,
				0.2f,
				0.02f
			),
			new DynTreeBranch(
				NatFloat.createUniform(0, 0.5f),
				NatFloat.createUniform(0, 2 * NatFloat.PI), 
				NatFloat.createGauss(0f, 0f),
				NatFloat.createUniform(0.05f, 0.01f), 
				0.09f,
				0.01f,
				0.4f
			),
			1.5f
		), null, null);
		
		EnumTree.SCOTSPINE.setGenerators(
			// Normal
			new DynTreeGen(
				EnumTree.SCOTSPINE, 
				null,
				new DynTreeTrunk(
//					1f,
					1f,
					0.02f,
					NatFloat.createGauss(0.55f, 0.15f), 
					NatFloat.createUniform(0.3f, 0.005f), 
					NatFloat.createGauss(NatFloat.PI / 2, 0.3f), 
					NatFloat.createUniform(0, 2*NatFloat.PI),
					4, 
					0.9f
				),
				new DynTreeBranch(
					NatFloat.createUniform(0, 0.5f),
					NatFloat.createUniform(0, 2 * NatFloat.PI), 
					NatFloat.createGauss(0f, 0f),
					NatFloat.createUniform(0.25f, 0.1f), 
					0.027f,
					0.1f,
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
	//					1f,
						1f,
						0.15f,
						NatFloat.createGauss(0.50f, 0.15f), 
						NatFloat.createUniform(0f, 0f), 
						NatFloat.createGauss(NatFloat.PI / 2 - 0.55f, 0f), 
						NatFloat.createUniform(0, 2*NatFloat.PI),
						4, 
						1f,
						1f,
						0.5f,
						0.02f
					),
					new DynTreeBranch(
						NatFloat.createUniform(0, 0.5f),
						NatFloat.createUniform(0, 2 * NatFloat.PI), 
						NatFloat.createGauss(0.2f, 0f),
						NatFloat.createUniform(0.2f, 0.01f), 
						0.2f,
						0.01f,
						0.4f
					),
					1.5f
			),
			// Lush
			new DynTreeGen(
				EnumTree.SCOTSPINE, 
				null,
				new DynTreeTrunk(
	//				0.8f,
					1f, 
					0.02f, 
					NatFloat.createGauss(0.3f, 0.1f), 
					NatFloat.createUniform(0.025f, 0.01f), 
					NatFloat.createGauss(NatFloat.PI / 2, 0.3f), 
					NatFloat.createUniform(0, 2*NatFloat.PI),
					2, 
					0.5f
				),
				new DynTreeBranch(
					NatFloat.createUniform(0, 0.5f),
					NatFloat.createUniform(0, 2 * NatFloat.PI), 
					NatFloat.createGauss(0f, 0f),
					NatFloat.createUniform(0.15f, 0.08f), 
					0.027f,
					0.1f,
					0.5f
				),
				0.5f
			)
		);
		
		
		
		
		// Reference: 
		/* new DynTreeTrunk(avgHeight, width, widthloss, branchStart, branchSpacing, branchVarianceSpacing, variance, numBranching, branchWidthMultiplier, widthBranchLossBase),
		 * new DynTreeBranch(verticalAngle, horizontalAngle, branchStart, spacing, widthloss, gravitydrag, branchWidthMultiplier)
   		 */

		EnumTree.ELEPHANTTREE.setGenerators(
				new DynTreeGen(
						EnumTree.ELEPHANTTREE, 
						null,
						new DynTreeTrunk(
		//					1f,
							1f,
							0.15f,
							NatFloat.createGauss(0.30f, 0.15f), 
							NatFloat.createUniform(0f, 0f), 
							NatFloat.createGauss(NatFloat.PI / 2 - 0.55f, 0f), 
							NatFloat.createUniform(0, 2*NatFloat.PI),
							4, 
							1f,
							1f,
							0.5f,
							0.02f
						),
						new DynTreeBranch(
							NatFloat.createUniform(5f, 0.5f),
							NatFloat.createUniform(0, 2 * NatFloat.PI), 
							NatFloat.createGauss(0.3f, 0f),
							NatFloat.createUniform(0.2f, 0.01f), 
							0.3f,
							0.01f,
							0.4f
						),
						1.5f
				), null, null
			);
		 
	}	
}