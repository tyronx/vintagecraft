package at.tyron.vintagecraft.WorldGen;

import java.util.Random;

import at.tyron.vintagecraft.VCraftWorld;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldGen.GenLayers.GenLayerVC;
import at.tyron.vintagecraft.WorldProperties.*;
import at.tyron.vintagecraft.block.BlockCropsVC;
import at.tyron.vintagecraft.block.BlockDoubleFlowerVC;
import at.tyron.vintagecraft.block.BlockFlowerVC;
import at.tyron.vintagecraft.block.BlockTallGrass;
import at.tyron.vintagecraft.block.BlockVC;
import at.tyron.vintagecraft.interfaces.ISoil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.common.IWorldGenerator;

public class MapGenFlora {
	GenLayerVC forestGen;
	
	public MapGenFlora(long seed) {
		forestGen = GenLayerVC.genForest(seed);
	}
	
	
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		chunkX *= 16;
		chunkZ *= 16;

		int[] forestLayer = forestGen.getInts(chunkX, chunkZ, 16, 16);
		VCraftWorld.instance.setChunkNBT(chunkX/16, chunkZ/16, "forest", forestLayer);
		
		/**** 1. Flowers and Wheat ****/
		if (random.nextInt(8) == 0) {
			placeFlowers(world, chunkX, chunkZ, forestLayer, random);
		}

		/**** 2. Grass, Trees, Vines ****/
		for (int i = 0; i < 200; i++) {
			int x = random.nextInt(16);
			int z = random.nextInt(16);
			
			int density = forestLayer[x+z*16];
		
			BlockPos blockpos = world.getHorizon(new BlockPos(chunkX + x, 0, chunkZ + z));
			
			int climate[] = VCraftWorld.instance.getClimate(blockpos);
			

			if (random.nextInt(255) < Math.min(200, density) - Math.max(0, blockpos.getY() - 180)) {
				placeGrass(world, blockpos, random, climate[1]);
				if (density < 20) {
					placeGrass(world, blockpos.east(random.nextInt(2)*2 - 1).west(random.nextInt(2)*2 - 1), random, climate[1]);
				}
				if (density < 40) {
					placeGrass(world, blockpos.east(random.nextInt(2)*2 - 1).west(random.nextInt(2)*2 - 1), random, climate[1]);
				}

			}
			
			//if (true) continue;
			
			
			int forestDensityDiff = Math.max(1, climate[1] - 180);
			
			if (i <= 50 + forestDensityDiff/4 && (random.nextInt(255) > density || random.nextInt(forestDensityDiff) > 0)) {
				Block block = world.getBlockState(blockpos.down()).getBlock();
				if (!(block instanceof ISoil)) {
					continue;
				}
				
				
				
				int steepness = Math.max(
					Math.abs(world.getHorizon(blockpos.east(2)).getY() - world.getHorizon(blockpos.west(2)).getY()),
					Math.abs(world.getHorizon(blockpos.north(2)).getY() - world.getHorizon(blockpos.south(2)).getY())
				);
				
				DynTreeGen treegen = EnumTree.getRandomTreeGenForClimate(climate[2], climate[0], 255 - forestLayer[x+z*16], climate[1], steepness, blockpos.getY(), random);				
				
				if (treegen != null) {
					if (treegen.tree == EnumTree.PURPLEHEARTWOOD) {
						
						treegen.growTree(world, blockpos.down(), 0.4f + random.nextFloat(), Math.max(0, climate[2] - 190));
					} else {
						treegen.growTree(world, blockpos.down(), 0.66f + random.nextFloat()/3 - Math.max(0, (blockpos.getY() * 1f / treegen.tree.maxy) - 0.5f), Math.max(0, climate[2] - 190));
					}
				}
			}
		}

	}
	
	
	void placeFlowers(World world, int chunkX, int chunkZ, int[] forestLayer, Random random) {
		BlockPos pos;
		
		for (int i = 0; i < 3; i++) {
			int x = random.nextInt(16);
			int z = random.nextInt(16);
			
			int climate[] = VCraftWorld.instance.getClimate(pos = new BlockPos(chunkX + x, 0, chunkZ + z));
			EnumFlora flora = EnumFlora.getRandomFlowerForClimate(climate[2], climate[0], 255 - forestLayer[x+z*16], random);
			//System.out.println("try place flower "  + flora);
			
			if (flora == null) continue;
			
			//System.out.println("try place flower "  + flora);
			
			int quantity = (random.nextInt(25) + random.nextInt(25) + random.nextInt(25)) / 3;
			
			while (quantity-- > 0) {
				pos = world.getHorizon(new BlockPos(chunkX + x + (random.nextInt(17)+random.nextInt(17))/2 - 6, 0, chunkZ + z + (random.nextInt(17)+random.nextInt(17))/2 - 6));
				Block block = world.getBlockState(pos.down()).getBlock();
				
				BlockClassEntry[] variants = BlocksVC.flower.values(flora);
				if (variants.length == 0) variants = BlocksVC.doubleflower.values(flora);
				
				IBlockState flower = variants[random.nextInt(variants.length)].getBlockState();
				
				if (block instanceof ISoil && block.canPlaceBlockAt(world, pos)) {
					world.setBlockState(pos, flower, 2);
				}
				
			}
			
		}
		
		if (random.nextInt(12) == 0) {
			int x = random.nextInt(16);
			int z = random.nextInt(16);
			
			int fertility = VCraftWorld.instance.getFertily(pos = new BlockPos(chunkX + x, 0, chunkZ + z));
			if (fertility < 140 ) return;
			
			int quantity = random.nextInt(10) + 2;
			
			while (quantity-- > 0) {
				pos = world.getHorizon(new BlockPos(chunkX + x + (random.nextInt(13)+random.nextInt(13))/2 - 6, 0, chunkZ + z + (random.nextInt(13)+random.nextInt(13))/2 - 6));
				Block block = world.getBlockState(pos.down()).getBlock();
				
				if (block instanceof ISoil && block.canPlaceBlockAt(world, pos)) {
					world.setBlockState(pos, BlocksVC.wheatcrops.getDefaultState().withProperty(BlockCropsVC.AGE, random.nextInt(4)), 2);
				}
			}
		}

	}
	
	
	void placeGrass(World world, BlockPos pos, Random random, int fertility) {
		Block block = world.getBlockState(pos.down()).getBlock();
		
		if (block instanceof ISoil && block.canPlaceBlockAt(world, pos)) {
			EnumTallGrass grasstype = EnumTallGrassGroup.fromClimate(fertility, random);
			if (grasstype != null) {
				world.setBlockState(pos, BlocksVC.tallgrass.getDefaultState().withProperty(BlockTallGrass.GRASSTYPE, grasstype), 2);
			}
		}
	}

}
