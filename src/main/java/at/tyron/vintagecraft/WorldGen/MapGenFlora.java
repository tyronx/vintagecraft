package at.tyron.vintagecraft.WorldGen;

import java.util.Random;

import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldGen.GenLayers.GenLayerVC;
import at.tyron.vintagecraft.WorldProperties.EnumDoublePlantTypeVC;
import at.tyron.vintagecraft.WorldProperties.EnumFlower;
import at.tyron.vintagecraft.WorldProperties.EnumGrass;
import at.tyron.vintagecraft.block.BlockDoublePlantVC;
import at.tyron.vintagecraft.block.BlockFlowerVC;
import at.tyron.vintagecraft.block.BlockTallGrass;
import net.minecraft.block.BlockDoublePlant;
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
		
		for (int i = 0; i < 200; i++) {
			int x = random.nextInt(16);
			int z = random.nextInt(16);
			
			int density = forestLayer[x*16+z];
		
			BlockPos blockpos = world.getHorizon(new BlockPos(chunkX + x, 0, chunkZ + z));
			
			if (random.nextInt(255) < density) {
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
	
	
	void placeGrass(World world, BlockPos pos, Random random) {
		if (BlocksVC.tallgrass.canPlaceBlockAt(world, pos)) {
			int meta = 0;
			if (random.nextInt(20) == 0) meta = 1 + random.nextInt(3);
			world.setBlockState(pos, BlocksVC.tallgrass.getDefaultState().withProperty(BlockTallGrass.GRASSTYPE, EnumGrass.fromMeta(meta)), 2);
		}		
	}

}
