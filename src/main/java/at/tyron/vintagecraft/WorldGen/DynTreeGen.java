package at.tyron.vintagecraft.WorldGen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import at.tyron.vintagecraft.World.BlocksVC;

public class DynTreeGen {
	// Absolute values
	int width;
	int height;
	
	// All below: Relative values from 0..100  (except angles)
	
	
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
		
		log = BlocksVC.log.getDefaultState();
		leaves = BlocksVC.leaves.getDefaultState();
		leavesBranchy = BlocksVC.leaves.getDefaultState();
	}
	
	
	public void gen(World world, BlockPos pos, int width, int height) {
		for (int y = 0; y < height; y++) {
			if (y < trunkHeight * height) drawTrunk(world, pos.up(y), width);
			//System.out.println((y - branchingMinHeight * height)+" % " + (branchingVertDist*height));
			if (y >= branchingMinHeight * height &&  (int)(y - branchingMinHeight * height) % (int)(branchingVertDist*height) == 0) {
				int quantityLeavesBranching = leavesBranchingMin + world.rand.nextInt(leavesBranchingMax);
				int step = Math.max(1, (int) ((maxBranchAngleHor - minBranchAngleHor) / quantityLeavesBranching)); 
				
				float leaveBranchingAngleVert = minBranchAngleVert + maxBranchAngleVert * world.rand.nextFloat();
				System.out.println(step);
				
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
		for (int x = (int) (-trunkRadius * width / 2); x < trunkRadius * width; x++) {
			for (int z = (int) (-trunkRadius * width / 2); z < trunkRadius * width; z++) {
				//if (world.getBlockState(pos).getBlock() == Blocks.air) {
				if (x*x + z*z <= width * width * trunkRadius*trunkRadius / 4) {
					world.setBlockState(new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z), log);
				//}
				}
			}
		}
	}
	
	
	
	
	
}
