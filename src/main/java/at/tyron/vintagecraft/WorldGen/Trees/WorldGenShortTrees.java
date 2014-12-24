package at.tyron.vintagecraft.WorldGen.Trees;

import java.util.Random;

import at.tyron.vintagecraft.block.VCBlock;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenShortTrees extends WorldGenerator {
	private int treeId;

	public WorldGenShortTrees(boolean flag, int id)
	{
		super(flag);
		treeId=id;
	}
	
	@Override
	public boolean generate(World world, Random random, BlockPos blockpos) {
	
		int height = random.nextInt(3) + 7;
		boolean flag = true;
		if (blockpos.getY() < 1 || blockpos.getY() + height + 1 > world.getHeight()) {
			return false;
		}

		for (int i1 = blockpos.getY(); i1 <= blockpos.getY() + 1 + height; i1++) {
			byte byte0 = 1;
			if (i1 == blockpos.getY())
				byte0 = 0;
			if (i1 >= blockpos.getY() + 1 + height - 2)
				byte0 = 2;

			for (int i2 = blockpos.getX() - byte0; i2 <= blockpos.getX() + byte0 && flag; i2++) {
				for (int l2 = blockpos.getX() - byte0; l2 <= blockpos.getX() + byte0 && flag; l2++) {
					if (i1 >= 0 && i1 < world.getHeight()) {
						Block block = world.getBlockState(new BlockPos(i2, i1, l2)).getBlock();
						
						if (block != Blocks.air && block.canBeReplacedByLeaves(world, new BlockPos(i2, i1, l2))) {
							flag = false;
						}
					} else {
						flag = false;
					}
				}
			}
		}

		if (!flag) {
			return false;
		}

		Block block = world.getBlockState(blockpos.down(1)).getBlock();
		
		if (!(block instanceof VCBlock)) {
			return false;
		}
		
		if (!((VCBlock)block).isSoil || blockpos.getY() >= world.getHeight() - height - 1) {
			return false;
		}

		for (int k1 = blockpos.getY() - 3 + height; k1 <= blockpos.getY() + height; k1++) {
			int j2 = k1 - (blockpos.getY() + height);
			int i3 = 1 - j2 / 2;
			for (int k3 = blockpos.getX() - i3; k3 <= blockpos.getX() + i3; k3++) {
				int l3 = k3 - blockpos.getX();
				for (int i4 = blockpos.getZ() - i3; i4 <= blockpos.getZ() + i3; i4++) {
					int j4 = i4 - blockpos.getZ();
					if ((Math.abs(l3) != i3 || Math.abs(j4) != i3 || random.nextInt(2) != 0 && j2 != 0) && world.getBlockState(new BlockPos(k3, k1, i4)).getBlock() == Blocks.air)
						
						func_175903_a(world, new BlockPos(k3, k1, i4), Blocks.leaves.getDefaultState());
				}
			}
		}

		for (int l1 = 0; l1 < height; l1++) {
			func_175903_a(world, blockpos.up(l1), Blocks.log.getDefaultState());
		}

		return true;
	}


}
