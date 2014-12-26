package at.tyron.vintagecraft.WorldGen;

import java.util.Random;

import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.block.BlockVC;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenShortTrees extends WorldGenerator {
	private int treeId;

	public WorldGenShortTrees(boolean flag, int id) {
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

		for (int y = blockpos.getY(); y <= blockpos.getY() + 1 + height; y++) {
			byte byte0 = 1;
			if (y == blockpos.getY())
				byte0 = 0;
			if (y >= blockpos.getY() + 1 + height - 2)
				byte0 = 2;

			for (int i2 = blockpos.getX() - byte0; i2 <= blockpos.getX() + byte0 && flag; i2++) {
				for (int l2 = blockpos.getX() - byte0; l2 <= blockpos.getX() + byte0 && flag; l2++) {
					if (y >= 0 && y < world.getHeight()) {
						Block block = world.getBlockState(new BlockPos(i2, y, l2)).getBlock();
						
						if (block != Blocks.air && block.canBeReplacedByLeaves(world, new BlockPos(i2, y, l2))) {
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
		
		if (!(block instanceof BlockVC)) {
			return false;
		}
		
		if (!((BlockVC)block).isSoil || blockpos.getY() >= world.getHeight() - height - 1) {
			return false;
		}

		for (int y = blockpos.getY() - 3 + height; y <= blockpos.getY() + height; y++) {
			int dyLast4Blocks = y - (blockpos.getY() + height);
			int width = 1 - dyLast4Blocks / 2;
			
			for (int x = blockpos.getX() - width; x <= blockpos.getX() + width; x++) {
				int dx = x - blockpos.getX();
				for (int z = blockpos.getZ() - width; z <= blockpos.getZ() + width; z++) {
					int dz = z - blockpos.getZ();
					if ((Math.abs(dx) != width || Math.abs(dz) != width || random.nextInt(2) != 0 && dyLast4Blocks != 0) && world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.air) {
						if (Math.abs(dx) < 2 && Math.abs(dz) < 2 && (y - blockpos.getY() < height) && width > 1) {
							world.setBlockState(new BlockPos(x, y, z), BlocksVC.leavesbranchy.getDefaultState(), 2);
						} else {
							world.setBlockState(new BlockPos(x, y, z), BlocksVC.leaves.getDefaultState(), 2);
						}
					}
				}
			}
		}

		for (int dy = -1; dy < height; dy++) {
			world.setBlockState(blockpos.up(dy), BlocksVC.log.getDefaultState(), 2);
		}

		return true;
	}


}
