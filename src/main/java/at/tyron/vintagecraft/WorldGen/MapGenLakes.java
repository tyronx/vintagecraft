package at.tyron.vintagecraft.WorldGen;

import java.awt.Point;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import at.tyron.vintagecraft.Block.BlockRegolith;
import at.tyron.vintagecraft.Block.BlockRock;
import at.tyron.vintagecraft.Block.Organic.BlockCropsVC;
import at.tyron.vintagecraft.Block.Organic.BlockDoubleFlowerVC;
import at.tyron.vintagecraft.Block.Organic.BlockFlowerVC;
import at.tyron.vintagecraft.Block.Organic.BlockLeavesVC;
import at.tyron.vintagecraft.Block.Organic.BlockLogVC;
import at.tyron.vintagecraft.Block.Organic.BlockSubSoil;
import at.tyron.vintagecraft.Block.Organic.BlockTallGrassVC;
import at.tyron.vintagecraft.Block.Organic.BlockVineVC;
import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldGen.Layer.GenLayerDrainage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.MapGenBase;

public class MapGenLakes extends MapGenBase {
	
	// This is a mildly unoptimized landscape depression flood fill algorithm
	
	
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		markValidLakesThenFill(world, chunkX, chunkZ);
	}
	
	

	void markValidLakesThenFill(World world, int chunkX, int chunkZ) {
		int size = 16 * 3;
		
		int dx, dz;
		int ndx, ndz;
		int yPos, nhgt, chunkIndex, nchunkIndex;
		

		int rain = VCraftWorld.instance.getRainfall(world.getHorizon(new BlockPos(chunkX*16 + 8, 0, chunkZ*16 + 8)));
		if (rain < 100) rain /= 2;
		int maxTries = (rain*rain) / (8*255);
		//System.out.println("rain = "+rain+", so tries: " + maxTries);
		
		Queue<Point> queue = new LinkedList<Point>();
		Queue<Point> water = new LinkedList<Point>();
		
		int lakeYPos;
		
		
		while (maxTries-- > 0) {
			dx = world.rand.nextInt(16);
			dz = world.rand.nextInt(16);
			
			queue.clear();
			water.clear();
			int[] checked = new int[size*size];
			
			BlockPos pos = getHorizon(world, new BlockPos(chunkX*16 + dx, 0, chunkZ*16 + dz));
			if (pos == null) continue;
			
			lakeYPos = pos.getY();
			queue.add(new Point(dx, dz));
			checked[(dz+16)*size + dx + 16] = 1;
			
			
			while (!queue.isEmpty()) {
				Point p = queue.remove();
				
				
				for (int i = 0; i < EnumFacing.HORIZONTALS.length; i++) {
					EnumFacing direction = EnumFacing.HORIZONTALS[i];
					ndx = p.x + direction.getFrontOffsetX();
					ndz = p.y + direction.getFrontOffsetZ();
					
					BlockPos neibpos = new BlockPos(chunkX*16 + ndx, lakeYPos, chunkZ*16 + ndz);
					
					// Cannot be at our 3x3 chunk border and we need a solid face below the water
					// otherwise cancel lake generation 
					if (ndx > -15 && ndz > -15 && ndx < 31 && ndz < 31 && world.isSideSolid(neibpos.down(), EnumFacing.UP)) {
						
						// Already checked or did we reach a lake border? 
						if (checked[(ndz + 16)*size + ndx + 16] == 0 && world.getBlockState(neibpos).getBlock().isReplaceable(world, neibpos)) {
							queue.add(new Point(ndx, ndz));
							water.add(new Point(ndx, ndz));
							checked[(ndz + 16)*size + ndx + 16] = 1;
						}
						
					} else {
						water.clear();
						queue.clear();
						break;
					}
				}
			}
			
			if (water.size() > 0) {
				System.out.println("found " + water.size() + " water points");
			}
			
			
			for (Point p : water) {
				pos = new BlockPos(chunkX * 16 + p.x, lakeYPos, chunkZ * 16 + p.y);
				
				if (world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
					world.setBlockState(pos, Blocks.water.getDefaultState());
				}
			}
		}
		
	}

	
	
	BlockPos getHorizon(World world, BlockPos pos) {
		if (!world.getChunkProvider().chunkExists(pos.getX() >> 4, pos.getZ() >> 4)) {
			return null;
		}
		
		BlockPos horizon = world.getHorizon(pos);
		Block block = world.getBlockState(horizon.down()).getBlock();
		
		// This stuff is probably no longer needed but to lazy to test the code without it atm
		while (block instanceof BlockLeavesVC || block instanceof BlockLogVC || block instanceof BlockVineVC || block.isAir(world, pos)) {
			horizon = horizon.down();
			block = world.getBlockState(horizon).getBlock();
			
			if (horizon.getY() < 100) return null;
		}
		
		return horizon;
	}
	


	
	
	
	// Unused code... could be used for a river gen later on
	
	
	public int[] combine3by3chunk(int[][]heightmap) {
		int size = 3 * 16;
		
		int combined[] = new int[size * size];
		
		for (int z = 0; z < size; z++) {
			for (int x = 0; x < size; x++) {
				int chunkIndex = (z/16) * 3 + x / 16;
				int yPos = heightmap[chunkIndex][(z % 16)*16 + (x % 16)];
				
				combined[z * size + x] = yPos;
 			}
		}

		return combined;
	}
	
	
	int[] genDrainageMap(World world, int chunkX, int chunkZ) {
		int [][]heightmap = new int[][]{
			world.getChunkFromChunkCoords(chunkX - 1, chunkZ - 1).getHeightMap(),
			world.getChunkFromChunkCoords(chunkX + 0, chunkZ - 1).getHeightMap(),
			world.getChunkFromChunkCoords(chunkX + 1, chunkZ - 1).getHeightMap(),
			world.getChunkFromChunkCoords(chunkX - 1, chunkZ + 0).getHeightMap(),
			world.getChunkFromChunkCoords(chunkX + 0, chunkZ + 0).getHeightMap(),
			world.getChunkFromChunkCoords(chunkX + 1, chunkZ + 0).getHeightMap(),
			world.getChunkFromChunkCoords(chunkX - 1, chunkZ + 1).getHeightMap(),
			world.getChunkFromChunkCoords(chunkX + 0, chunkZ + 1).getHeightMap(),
			world.getChunkFromChunkCoords(chunkX + 1, chunkZ + 1).getHeightMap(),
		};
		int[] drainagemap = GenLayerDrainage.getDrainageMap(heightmap);
		
		return drainagemap;
	}
	
		

}
