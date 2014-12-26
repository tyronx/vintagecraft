package at.tyron.vintagecraft.WorldGen;

import java.util.Random;

import at.tyron.vintagecraft.WorldGen.GenLayers.GenLayerVC;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenForests implements IWorldGenerator {
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		chunkX *= 16;
		chunkZ *= 16;

        WorldGenShortTrees worldGeneratorTrees = new WorldGenShortTrees(false, 0);

		int[] forestLayer = GenLayerVC.genForest(world.getSeed()).getInts(chunkX, chunkZ, 16, 16);
		
		
		
		for (int i = 0; i < 50; i++) {
			int x = random.nextInt(16);
			int z = random.nextInt(16);
			
			int density = forestLayer[x*16+z];
		
			if (random.nextInt(255) < density) continue;
			
	        BlockPos blockpos = world.getHorizon(new BlockPos(chunkX + x, 0, chunkZ + z));

	        worldGeneratorTrees.generate(world, random, blockpos);
					
		}
		

	}

}
