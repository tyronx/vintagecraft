package at.tyron.vintagecraft.WorldGen.Helper;

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
import at.tyron.vintagecraft.Block.Organic.BlockFlowerVC;
import at.tyron.vintagecraft.Block.Organic.BlockLeavesBranchy;
import at.tyron.vintagecraft.Block.Organic.BlockLeavesVC;
import at.tyron.vintagecraft.Block.Organic.BlockLogVC;
import at.tyron.vintagecraft.BlockClass.TreeClass;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumTreeGenMode;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;


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
	
	public EnumTreeGenMode mode;
	
	
	
	public DynTreeGen(EnumTree treetype, DynTreeRoot roots, DynTreeTrunk trunk, DynTreeBranch branches) {
		this(treetype, roots, trunk, branches, 1.0f, 0);
	}

	public DynTreeGen(EnumTree treetype, DynTreeRoot roots, DynTreeTrunk trunk, DynTreeBranch branches, float sizemodifier) {
		this(treetype, roots, trunk, branches, sizemodifier, 0, EnumTreeGenMode.NORMAL);
	}

	public DynTreeGen(EnumTree treetype, DynTreeRoot roots, DynTreeTrunk trunk, DynTreeBranch branches, float sizemodifier, int yOffset) {
		this(treetype, roots, trunk, branches, sizemodifier, 0, EnumTreeGenMode.NORMAL);
	}
	
	public DynTreeGen(EnumTree treetype, DynTreeRoot roots, DynTreeTrunk trunk, DynTreeBranch branches, float sizemodifier, int yOffset, EnumTreeGenMode mode) {
		this.roots = roots;
		this.trunk = trunk;
		this.branches = branches;
		this.sizemodifier = sizemodifier;
		this.yOffset = yOffset;
		
		this.tree = treetype;
		this.mode = mode;
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
		this.pos = pos.add(0.5f, 0, 0.5f);
		this.size = size * sizemodifier;
		this.vineGrowthChance = vineGrowthChance;
		
		if (yOffset > 0) {
			this.pos = pos.down((int) (yOffset * size));
		}
		
		//float relheight = trunk.avgHeight; // getWithGaussVariance(world.rand, trunk.avgHeight, trunk.variance);
		float relwidth = trunk.width;
		
		genTrunk(0f, 0f, 0f, trunk.width, trunk.angleHori, trunk.angleVert /*trunk.angleVert != null ? trunk.angleVert.nextFloat() : 0, trunk.bendAngleVert*/);
	}
	
	

	private void growRoots(BlockPos pos, float dx, float dy, float dz, EvolvingNatFloat verticalAngle, EvolvingNatFloat horizontalAngle, NatFloat baseWidth, float widthloss, NatFloat numBranching, NatFloat branchStart, NatFloat branchSpacing, float widthBranchLossBase, NatFloat branchVerticalAngle, NatFloat branchHorizontalAngle) {
		verticalAngle.init();
		horizontalAngle.init();
		
		Block block;
		float ang;
		
		float width = baseWidth.nextFloat();
		float branchspacing = branchSpacing.nextFloat();
		float branchstart = branchStart.nextFloat();
		float branches = numBranching.nextFloat();

		float reldistance = 0, lastreldistance = 0;
		float totaldistance = width / (widthloss / size);
		
		int iteration = 0;
		float sequencesPerIteration = 1000 / (width / (widthloss / size));
		float sequence;
		
		
		while (width > 0 && iteration++ < 5000) {
			width -= widthloss / size;
			
			sequence = sequencesPerIteration * iteration;
			
			dx += MathHelper.sin(verticalAngle.nextFloat(sequence)) * MathHelper.cos(horizontalAngle.nextFloat(sequence));
			dy += Math.min(1, Math.max(-1, MathHelper.cos(ang = verticalAngle.nextFloat(sequence)))); // - gravityDrag * MathHelper.sqrt_float(dx*dx + dz*dz)));
			dz += MathHelper.sin(verticalAngle.nextFloat(sequence)) * MathHelper.sin(horizontalAngle.nextFloat(sequence));
			
			IBlockState blockstate = block(1f);
			
			
			

			if ((block = world.getBlockState(pos.add(dx, dy, dz)).getBlock()) == Blocks.air) {
				if (ang < NatFloat.PI - 0.25f  && world.getBlockState(pos.add(dx, dy+1, dz)).getBlock() == Blocks.air) dy--;
				
				world.setBlockState(pos.add(dx, dy, dz), blockstate, 2);
			} else {
				if (block != block(1f).getBlock()) {
					break;
				}
			}

			reldistance = MathHelper.sqrt_float(dx*dx + dy*dy + dz*dz) / totaldistance;
			
			if (reldistance < branchstart) continue;
			
			if (reldistance > lastreldistance + branchspacing * (1f - reldistance)) {
				branchspacing = branchSpacing.nextFloat();
				lastreldistance = reldistance;
				
				if (branches > 0) width *= widthBranchLossBase;
				
				/*for (int i = 0; i < branches; i++) {
					growRoots(
						pos, dx, dy, dz,
						verticalAngle.copyWithOffset(branchVerticalAngle.nextFloat()),
						horizontalAngle.copyWithOffset(branchHorizontalAngle.nextFloat()),
						baseWidth,
						widthloss,
						numBranching,
						branchStart,
						branchSpacing,
						widthBranchLossBase,
						branchVerticalAngle,
						branchHorizontalAngle
					);
				}*/
			}
			
			


			
		}
		
	}

	
/*	private void setBlock(BlockPos pos, IBlockState state) {
		 
	}
*/

	private void genTrunk(float curx, float cury, float curz, float curwidth, EvolvingNatFloat anglehor, /*float anglever,*/ EvolvingNatFloat angleVert) {
		trunk.branchWidthMultiplier.init();
		
		growBranch(0, pos, 0f, 0f, 0f, angleVert, anglehor, trunk.width, trunk.widthloss, trunk.numBranching, /*numBranches,*/ trunk.branchStart, trunk.branchSpacing, trunk.branchWidthMultiplier.nextFloat(0), 0f, trunk.branchVerticalAngle, trunk.branchHorizontalAngle, trunk.widthBranchLossBase, this.roots);
	}
	
	
	
	public void growBranch(int recursion, BlockPos pos, float dx, float dy, float dz, EvolvingNatFloat angleVert, EvolvingNatFloat angleHori, float baseWidth, float widthloss, EvolvingNatFloat numBranching, NatFloat branchStart, NatFloat branchSpacing, float branchWidthMultiplier, float gravityDrag, NatFloat branchVerticalAngle, NatFloat branchHorizontalAngle, float widthBranchLossBase, DynTreeRoot roots) {
		if (recursion > 30) { System.out.println("too many branches!"); return; }
		
		angleVert.init();
		angleHori.init();
		
		float branchspacing = branchSpacing.nextFloat();
		float branchstart = branchStart.nextFloat();
		
		float reldistance = 0, lastreldistance = 0;
		float totaldistance = baseWidth / (widthloss / size);
		
		int iteration = 0;
		float sequencesPerIteration = 1000 / (baseWidth / (widthloss / size));
		
		float rootEnd=0;
		float rootSpacing=0;
		float rootLastReldistance=0;
		if (roots != null) {
			rootEnd = roots.rootEnd.nextFloat();
			rootSpacing = roots.rootSpacing.nextFloat();
		}
		
		float anglever = 0f, anglehor = 0f, ddrag = 0f;
		BlockPos nextblock;
		
		// we want to place around the trunk/branch => offset the coordinates when growing stuff from the base
		float trunkOffsetX = 0, trunkOffsetZ = 0, trunkOffsetY = 0;
		
		while (baseWidth > 0 && iteration++ < 5000) {
			baseWidth -= widthloss / size;
			
			if (baseWidth < 0.0001f) break;
			
			anglever = angleVert.nextFloat(sequencesPerIteration * (iteration-1));
			anglehor = angleHori.nextFloat(sequencesPerIteration * (iteration-1));

		
			trunkOffsetX = Math.max(-0.5f, Math.min(0.5f, 0.7f * MathHelper.sin(anglever) * MathHelper.cos(anglehor))) + 0.5f;
			trunkOffsetY = Math.max(-0.5f, Math.min(0.5f, 0.7f * MathHelper.cos(anglever))) + 0.5f;
			trunkOffsetZ = Math.max(-0.5f, Math.min(0.5f, 0.7f * MathHelper.sin(anglever) * MathHelper.sin(anglehor))) + 0.5f;
			
			ddrag = gravityDrag * MathHelper.sqrt_float(dx*dx + dz*dz);
			
			dx += MathHelper.sin(anglever) * MathHelper.cos(anglehor) / Math.max(1, ddrag);
			dy += Math.min(1, Math.max(-1, MathHelper.cos(anglever) - ddrag));
			dz += MathHelper.sin(anglever) * MathHelper.sin(anglehor) / Math.max(1, ddrag);
			
			
			IBlockState blockstate = block(baseWidth);

			if (canPlace(blockstate, world.getBlockState(pos.add(dx, dy, dz)))) {
				world.setBlockState(pos.add(dx, dy, dz), blockstate, 2);
				
				//System.out.println("block @" + dx + "/" + dy + "/"+ dz);
				if (vineGrowthChance > 0 && rand.nextInt(100) < vineGrowthChance) {
					EnumFacing facing = EnumFacing.getHorizontal(rand.nextInt(4));
					
					BlockPos vinePos = pos.add(dx, dy, dz).offset(facing);
					int cnt = 1 + rand.nextInt(11);
					
					while (world.isAirBlock(vinePos) && cnt-- > 0) {
						world.setBlockState(vinePos, vineBlock.getDefaultState().withProperty(BlockVine.getPropertyFor(facing.getOpposite()), true), 2);
						vinePos = vinePos.down();
					}
				}
			} else {
				//System.out.println("cant place " + blockstate + " @ angle " + anglehor);
			}
			
			
			reldistance = MathHelper.sqrt_float(dx*dx + dy*dy + dz*dz) / totaldistance;
			
			if (recursion == 0) {
				branchWidthMultiplier = trunk.branchWidthMultiplier.nextFloat(sequencesPerIteration * (iteration-1));
			}
			
			if (roots != null && recursion == 0 && rootEnd > reldistance && reldistance > rootLastReldistance + rootSpacing * (1f - reldistance)) {
				rootLastReldistance = reldistance;
				rootSpacing = roots.rootSpacing.nextFloat();
				int num = (int) roots.numBranching.nextFloat();
				
				for (int i = 0; i < num; i++) {
					growRoots(pos, dx + trunkOffsetX, dy, dz + trunkOffsetZ, roots.verticalAngle, roots.horizontalAngle, roots.baseWidth, roots.widthloss, roots.numBranching, roots.branchStart, roots.branchSpacing, roots.widthBranchLossBase, roots.branchVerticalAngle, roots.branchHorizontalAngle);
				}
			}

			

			
			if (reldistance < branchstart) continue;
			
			//if (recursion > 1) continue;
			
			if (reldistance > lastreldistance + branchspacing * (1f - reldistance)) {
				branchspacing = branchSpacing.nextFloat();
				lastreldistance = reldistance;
				numBranching.init();
				
				int quantity = (int)numBranching.nextFloat(recursion);
				
				//if (quantity > 0) baseWidth *= widthBranchLossBase;
				
				for (int i = 0; i < quantity; i++) {
					baseWidth *= widthBranchLossBase;
					
					growBranch(
						recursion+1, 
						pos, dx + trunkOffsetX, dy, dz + trunkOffsetZ, 
						branches.angleVert.copyWithOffset(branchVerticalAngle.nextFloat()),
						branches.angleHori.copyWithOffset(branchHorizontalAngle.nextFloat()),
						baseWidth * branchWidthMultiplier * (mode == EnumTreeGenMode.RANDOMLENGTHBRANCHES ? 1F : (0.5f + rand.nextFloat()*0.5f)), 
						branches.widthloss, 
						branches.numBranching,
						branches.branchStart, 
						branches.branchSpacing, 
						branches.branchWidthMultiplier,
						branches.gravityDrag,
						branches.branchVerticalAngle, 
						branches.branchHorizontalAngle,
						branches.widthBranchLossBase,
						null
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
		if (mode == EnumTreeGenMode.BUSH) {
			if (width < 0.04f) return leaves;
			return leavesbranchy;
		}
		
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
			|| blockatpos.getBlock() instanceof BlockFlowerVC
			|| (blockstate == log && (blockatpos.getBlock() instanceof BlockLeavesVC))
			|| (blockstate == leavesbranchy && (blockatpos.getBlock() instanceof BlockLeavesVC) && !(blockatpos.getBlock() instanceof BlockLeavesBranchy))
			
		;
	
	}
	
	
	
	
}