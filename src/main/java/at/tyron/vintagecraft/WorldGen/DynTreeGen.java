package at.tyron.vintagecraft.WorldGen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import at.tyron.vintagecraft.BlockClass.TreeClass;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.block.BlockLeavesVC;
import at.tyron.vintagecraft.block.BlockLeavesBranchy;
import at.tyron.vintagecraft.block.BlockLogVC;



/*
 * Needed parameters:
 * 
 * Roots:
 *   yStart
 *   avgLength
 * 	 numRoots
 * 	 variance
 *   
 * 
 * Trunk:
 *   avgHeight
 *   width
 *   widthloss         (per block)
 *   splitStart
 *   splitAngle
 *   trunkAngleLoss    (0 = the trunk loses no angle, 1f = trunk and branch both split of at same angle)  
 *   
 *   variance
 * 
 * 
 * 
 * Branch:
 *   branchanglevert
 *   branchanglehori
 *   variance
 * 
 * 
 * Leaves:
 *   direction       (0 = downwards, 1 = sideways, 2 = all directions)
 * 
 * 
 * 
 * 
 */



public class DynTreeGen {
	// "Temporary Values" linked to currently generated tree
	World world;
	BlockPos pos;
	float size;
	
	// "Permanent Values" Linked to tree type
	float sizemodifier;
	DynTreeRoot roots;
	DynTreeTrunk trunk;
	DynTreeBranch branches;
	
	
	public static DynTreeGen birch = new DynTreeGen(
		EnumTree.BIRCH, 
		null,
		new DynTreeTrunk(0.8f, 1f, 0.05f, 0.22f, 0.01f, 0.005f, 0.1f, 3, 0.5f),
		new DynTreeBranch(Math.PI / 4 + 0.1f, Math.PI / 8, 0, 2*Math.PI, 0.01f, 0.01f, 0.05f)
	);
	
	
	// More like a christmas tree, very regular branching in discs ^^
	/*public static DynTreeGen spruce = new DynTreeGen(
		EnumTree.SPRUCE, 
		null,
		new DynTreeTrunk(0.8f, 1f, 0.04f, 0.15f, 0.1f, 0f, 0.1f, 32, 0.25f),
		new DynTreeBranch(Math.PI / 2 - 0.4f, 0, 0, 2*Math.PI, 5f, 0.05f, 0.03f),
		0.8f
	);*/
	
	// Reference: http://upload.wikimedia.org/wikipedia/commons/8/81/Picea_abies.jpg
	public static DynTreeGen spruce = new DynTreeGen(
		EnumTree.SPRUCE, 
		null,
		new DynTreeTrunk(0.8f, 1f, 0.03f, 0.12f, 0.05f, 0f, 0.1f, 16, 0.25f),
		new DynTreeBranch(Math.PI / 2 - 0.35f, 0, 0, 2*Math.PI, 5f, 0.05f, 0.03f)
	);

	
	// Reference: http://4.bp.blogspot.com/-saiB023XaGo/T5uu7K8dnAI/AAAAAAAAP14/rvvkLnFOAJA/s1600/dogwood-tree.jpg
	public static DynTreeGen mountaindogwood = new DynTreeGen(
		EnumTree.MOUNTAINDOGWOOD, 
		null, 
		//new DynTreeTrunk(avgHeight, width, widthloss, branchStart, branchSpacing, branchVarianceSpacing, variance, numBranching, branchWidthMultiplier), 
		new DynTreeTrunk(0.9f, 1f, 0.15f, 0.5f, 0.01f, 0f, 0f, 8, 1f),
		//new DynTreeBranch(anglevert, varianceAnglevert, anglehori, varianceAnglehori, spacing, varianceSpacing, widthloss)
		new DynTreeBranch(Math.PI / 2 - 0.55f, 0, 0, 2*Math.PI, 0.01f, 0f, 0.17f),
		0.8f
	);
	
	
	
	public static DynTreeGen scotspine = new DynTreeGen(
		EnumTree.SCOTSPINE, 
		null,
		new DynTreeTrunk(0.8f, 1f, 0.05f, 0.5f, 0.02f, 0f, 0.1f, 3, 0.4f),
		new DynTreeBranch(Math.PI / 2 - 0.9f, 0f, 0, 2*Math.PI, 0.25f, 0f, 0.02f, 0.2f)
	);


	
	
	
	public DynTreeGen(EnumTree treetype, DynTreeRoot roots, DynTreeTrunk trunk, DynTreeBranch branches) {
		this(treetype, roots, trunk, branches, 1.0f);
	}
	
	public DynTreeGen(EnumTree treetype, DynTreeRoot roots, DynTreeTrunk trunk, DynTreeBranch branches, float sizemodifier) {
		this.roots = roots;
		this.trunk = trunk;
		this.branches = branches;
		this.sizemodifier = sizemodifier;
		
		setTree(treetype);
	}
	
	

	public void gen(World world, BlockPos pos, float size, float bend) {
		this.world = world;
		this.pos = pos;
		this.size = size * sizemodifier;
		
		float relheight = getWithGaussVariance(world.rand, trunk.avgHeight, trunk.variance);
		float relwidth = trunk.width;
		
		genTrunk(0f, 0f, 0f, trunk.width, relheight, (float)(world.rand.nextFloat() * Math.PI * 2), bend);
		genRoots();
	}
	
	

	private void genRoots() {
		
	}



	private void genTrunk(float curx, float cury, float curz, float curwidth, float relheight, float anglehor, float anglever) {
		float numBranches = trunk.numBranching * (size / 2) + trunk.numBranching / 2; 
		
		growBranch(pos, 0f, 0f, 0f, anglever, anglehor, trunk.width, trunk.widthloss, (int) numBranches, trunk.branchStart, trunk.branchSpacing, trunk.branchVarianceSpacing, trunk.branchWidthMultiplier, 0f);
	}
	
	
	
	public void growBranch(BlockPos pos, float dx, float dy, float dz, float anglever, float anglehor, float baseWidth, float widthloss, int numbranching, float branchStart, float avgSpacing, float varianceSpacing, float branchWidthMultiplier, float gravityDrag) {
//		if (numbranching > 1)
//			System.out.println("spacing = "+avgSpacing+" +- " + varianceSpacing);
	
		float spacing = getWithGaussVariance(world.rand, avgSpacing, varianceSpacing);
		
		float reldistance = 0, lastreldistance = 0;
		float totaldistance = baseWidth / (widthloss / size);
		
		int iterations = 5000;
		
		while (baseWidth > 0 && iterations-- > 0) {
			baseWidth -= widthloss / size;
			
			
			dx += MathHelper.sin(anglever) * MathHelper.cos(anglehor);
			dy += MathHelper.cos(anglever) - Math.min(1.8f, gravityDrag * MathHelper.sqrt_float(dx*dx + dz*dz));
			dz += MathHelper.sin(anglever) * MathHelper.sin(anglehor);
			
			
			IBlockState blockstate = block(baseWidth);
			// Logs overwrite leaves
			if (canPlace(blockstate, world.getBlockState(pos.add(dx, dy, dz)))) {
				world.setBlockState(pos.add(dx, dy, dz), blockstate);
			}
			
			
			reldistance = MathHelper.sqrt_float(dx*dx + dy*dy + dz*dz) / totaldistance;
			
			if (reldistance < branchStart) continue;
			
			
			//reldistance += MathHelper.sqrt_float(dx*dx + dy*dy + dz*dz);
			
			
			
			if (reldistance > lastreldistance + spacing) {
				//if (numbranching > 1) System.out.println(reldistance+" > "+(lastreldistance + spacing));
				
				spacing = getWithGaussVariance(world.rand, avgSpacing, varianceSpacing);
				lastreldistance = reldistance;
				
				//branchsize *= branches.splitloss;
				
				for (int i = 0; i < numbranching; i++) {
					growBranch(
						pos, dx, dy, dz, 
						anglever + getWithGaussVarianceInverse(world.rand, branches.anglevert, branches.varianceAnglevert), 
						anglehor + getWithVariance(world.rand, branches.anglehori, branches.varianceAnglehori), 
						baseWidth * branchWidthMultiplier, 
						branches.widthloss, 
						1, 
						0f, 
						branches.spacing, 
						branches.varianceSpacing,
						branches.branchWidthMultiplier,
						branches.gravityDrag
					);
				}
			}
		}		
	}
	
	
	
	
	
	float getWithVariance(Random rand, float val, float variance) {
		float rnd = rand.nextFloat() - 0.5f;	
		return val + rnd * variance;
	}
	
	// Delivers a value from a gauss curve
	float getWithGaussVariance(Random rand, float val, float variance) {
		float rnd = (rand.nextFloat() + rand.nextFloat() + rand.nextFloat())/3;  // Random value out of a gauss curve between 0..1, with 0.5f being most common
		
		// Center gauss curve to 0
		rnd = rnd - 0.5f;
		
		return val + rnd * variance;
	}

	
	// Delivers a value from a vertically flipped gauss curve
	float getWithGaussVarianceInverse(Random rand, float val, float variance) {
		float rnd = (rand.nextFloat() + rand.nextFloat() + rand.nextFloat())/3;  // Random value out of a gauss curve between 0..1, with 0.5f being most common
		
		// Flip curve
		if (rnd > 0.5f) {
			rnd -= 0.5f;
		} else {
			rnd += 0.5f;
		}
		
		// Center gauss curve to 0
		rnd = rnd - 0.5f;
		
		return val + rnd * variance;
	}
	
	
	
	
	private IBlockState log;
	private IBlockState leavesbranchy;
	private IBlockState leaves;

	
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
			blockatpos.getBlock() == Blocks.air
			|| (blockstate == log && (blockatpos.getBlock() instanceof BlockLeavesVC))
			|| (blockstate == leavesbranchy && (blockatpos.getBlock() instanceof BlockLeavesVC) && !(blockatpos.getBlock() instanceof BlockLeavesBranchy))
		;
	
	}
	
	
}



/*
public class DynTreeGen {
	// Absolute values
	int width;
	int height;
	
	// All below: Relative values from 0..100  (except angles)
	
	
	// Stuff to read:
	// http://en.wikipedia.org/wiki/L-system
	
	float trunkHeight;
	float trunkRadius;
	
	float branchingMinHeight; 
	float branchingVertDist;
	
	float trunkBottomCurvature;
	float trunkTopCurvature;
	
	float logBranchingMin;
	float logBranchingMax;
	
	int leavesBranchingMin;
	int leavesBranchingMax;
		
	float minBranchAngleVert; // 0..pi
	float maxBranchAngleVert; // 0..pi
	
	float minBranchAngleHor; // 0..2 pi
	float maxBranchAngleHor; // 0..2 pi
	
	float foliageRadius;
	
	float randomness;
	
	IBlockState log;
	IBlockState leaves;
	IBlockState leavesBranchy;
	
	
	
	public static DynTreeGen testtree = new DynTreeGen(0.9f, 0.2f, 0.25f, 0.15f, 0, 0, 0, 0, 4, 4, 30, 40, 0, 360, 3, 0);
	
	public DynTreeGen(float trunkHeight, float trunkRadius, float branchingMinHeight, float branchingVertDist, float trunkBottomCurvature, float trunkTopCurvature, float logBranchingMin, float logBranchingMax, int leavesBranchingMin, int leavesBranchingMax, float minBranchAngleVert, float maxBranchAngleVert, float minBranchAngleHor, float maxBranchAngleHor, float foliageRadius, float randomness) {
		this.trunkHeight = trunkHeight;
		this.trunkRadius = trunkRadius;
		this.branchingMinHeight = branchingMinHeight;
		this.branchingVertDist = branchingVertDist;
		this.trunkBottomCurvature = trunkBottomCurvature;
		this.trunkTopCurvature = trunkTopCurvature;
		this.logBranchingMin = logBranchingMin;
		this.logBranchingMax = logBranchingMax;
		this.leavesBranchingMin = leavesBranchingMin;
		this.leavesBranchingMax = leavesBranchingMax;
		this.minBranchAngleVert = minBranchAngleVert;
		this.maxBranchAngleVert = maxBranchAngleVert;
		this.minBranchAngleHor = minBranchAngleHor;
		this.maxBranchAngleHor = maxBranchAngleHor;
		this.foliageRadius = foliageRadius;
		this.randomness = randomness;
		
		log = BlocksVC.log.getBlockStateFor(EnumTree.PINE);
		leaves = BlocksVC.leaves.getBlockStateFor(EnumTree.PINE).withProperty(BlockLeaves.CHECK_DECAY, false);
		leavesBranchy = BlocksVC.leavesbranchy.getBlockStateFor(EnumTree.PINE).withProperty(BlockLeavesBranchy.CHECK_DECAY, false);;
	}
	
	
	public void gen(World world, BlockPos pos, int width, int height) {
		for (int y = 0; y < height; y++) {
			if (y < trunkHeight * height) drawTrunk(world, pos.up(y), width);
			//System.out.println((y - branchingMinHeight * height)+" % " + (branchingVertDist*height));
			
			if (y >= branchingMinHeight * height &&  (int)(y - branchingMinHeight * height) % (int)(branchingVertDist*height) == 0) {
				int quantityLeavesBranching = leavesBranchingMin + world.rand.nextInt(leavesBranchingMax);
				int step = Math.max(1, (int) ((maxBranchAngleHor - minBranchAngleHor) / quantityLeavesBranching)); 
				
				float leaveBranchingAngleVert = minBranchAngleVert + (maxBranchAngleVert-minBranchAngleVert) * world.rand.nextFloat();
				
				for (float leaveBranchingAngleHor = minBranchAngleHor; leaveBranchingAngleHor < maxBranchAngleHor; leaveBranchingAngleHor += step) {
					float endX = MathHelper.sin(leaveBranchingAngleVert) * MathHelper.cos(leaveBranchingAngleHor);
					float endZ = MathHelper.sin(leaveBranchingAngleVert) * MathHelper.sin(leaveBranchingAngleHor);
					float endY = MathHelper.cos(leaveBranchingAngleVert);
					
					//System.out.println(endX + "/" + endY + "/" + endZ);
					
					for (int blockstephor = 0; blockstephor < width; blockstephor++) {
						BlockPos thepos = pos.add(blockstephor * endX, y + blockstephor * endY, blockstephor * endZ);
						if (world.getBlockState(thepos).getBlock() == Blocks.air) {
							world.setBlockState(thepos, leaves);
						}
					}
					
					
					
				}
				
			}
			
		}
		
		
		
	}
	
	
	public void drawTrunk(World world, BlockPos pos, int width) {
		world.setBlockState(pos, log);
		
		/*for (int x = (int) (-trunkRadius * width / 2); x < trunkRadius * width; x++) {
			for (int z = (int) (-trunkRadius * width / 2); z < trunkRadius * width; z++) {
				//if (world.getBlockState(pos).getBlock() == Blocks.air) {
				if (x*x + z*z <= width * width * trunkRadius*trunkRadius / 4) {
					world.setBlockState(new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z), log);
				//}
				}
			}
		}
	}
	
	
	
	
	
}*/
