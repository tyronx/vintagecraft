package at.tyron.vintagecraft.WorldGen;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class VCWorldGen implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		// TODO Auto-generated method stub
		
		world.setBlockState(new BlockPos(chunkX*16 + random.nextInt(16), 100, chunkZ*16 + random.nextInt(16)), Blocks.grass.getDefaultState());

	}

}
