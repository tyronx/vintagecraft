package at.tyron.vintagecraft.WorldGen;

import java.util.Random;

import at.tyron.vintagecraft.VCraftWorld;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldGen.GenLayers.GenLayerVC;
import at.tyron.vintagecraft.WorldProperties.EnumFertility;
import at.tyron.vintagecraft.WorldProperties.EnumFlora;
import at.tyron.vintagecraft.WorldProperties.EnumFlower;
import at.tyron.vintagecraft.WorldProperties.EnumTallGrass;
import at.tyron.vintagecraft.WorldProperties.EnumOrganicLayer;
import at.tyron.vintagecraft.block.BlockDoubleFlowerVC;
import at.tyron.vintagecraft.block.BlockFlowerVC;
import at.tyron.vintagecraft.block.BlockTallGrass;
import at.tyron.vintagecraft.interfaces.ISoil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
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
	WorldGenShortTrees worldGeneratorTrees;
	
	public MapGenFlora(long seed) {
		forestGen = GenLayerVC.genForest(seed);
		worldGeneratorTrees = new WorldGenShortTrees(false, 0);
	}
	
	
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		chunkX *= 16;
		chunkZ *= 16;

		int[] forestLayer = forestGen.getInts(chunkX, chunkZ, 16, 16);
		
		VCraftWorld.setChunkNBT(chunkX/16, chunkZ/16, "forest", forestLayer);
		

		if (random.nextInt(12) == 0) {
			placeFlowers(world, chunkX, chunkZ, forestLayer, random);
		}

		
		for (int i = 0; i < 200; i++) {
			int x = random.nextInt(16);
			int z = random.nextInt(16);
			
			int density = forestLayer[x+z*16];
		
			BlockPos blockpos = world.getHorizon(new BlockPos(chunkX + x, 0, chunkZ + z));
			
			if (random.nextInt(255) < density - Math.max(0, blockpos.getY() - 160)) {
				placeGrass(world, blockpos, random);
				if (density < 10) {
					placeGrass(world, blockpos.east(random.nextInt(2)*2 - 1).west(random.nextInt(2)*2 - 1), random);
				}
			}
			
			
			if (i <= 50 && random.nextInt(255) > density) {
				worldGeneratorTrees.generate(world, random, blockpos);
			}
			
		}
		
		
		


	}
	
	
	void placeFlowers(World world, int chunkX, int chunkZ, int[] forestLayer, Random random) {
		BlockPos pos;
		
		for (int i = 0; i < 3; i++) {
			int x = random.nextInt(16);
			int z = random.nextInt(16);
			
			int climate[] = VCraftWorld.getClimate(pos = new BlockPos(chunkX + x, 0, chunkZ + z));

			EnumFlora flora = EnumFlora.getRandomFlowerForClimate(climate[2], climate[0], 255 - forestLayer[x+z*16], random);
			//System.out.println("try place flower "  + flora);
			
			if (flora == null) continue;
			
			//System.out.println("try place flower "  + flora);
			
			int quantity = (random.nextInt(25) + random.nextInt(25) + random.nextInt(25)) / 3;
			
			while (quantity-- > 0) {
				pos = world.getHorizon(new BlockPos(chunkX + x + (random.nextInt(13)+random.nextInt(13))/2 - 6, 0, chunkZ + z + (random.nextInt(13)+random.nextInt(13))/2 - 6));
				Block block = world.getBlockState(pos.down()).getBlock();
				
				BlockClassEntry[] variants = BlocksVC.flower.values(flora);
				if (variants.length == 0) variants = BlocksVC.doubleflower.values(flora);
				
				IBlockState flower = variants[random.nextInt(variants.length)].getBlockState();
				
				if (block instanceof ISoil && block.canPlaceBlockAt(world, pos)) {
					world.setBlockState(pos, flower, 2);
				}
				
			}
			
		}
	}
	
	
	void placeGrass(World world, BlockPos pos, Random random) {
		Block block = world.getBlockState(pos.down()).getBlock();
		
		if (block instanceof ISoil && block.canPlaceBlockAt(world, pos)) {
			int fertility = VCraftWorld.getFertily(pos);
			
			EnumTallGrass grasstype = EnumTallGrass.fromClimate(fertility, random);
			if (grasstype != null) {
				world.setBlockState(pos, BlocksVC.tallgrass.getDefaultState().withProperty(BlockTallGrass.GRASSTYPE, grasstype), 2);
			}
		}
	}

}
