package at.tyron.vintagecraft.WorldGen;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.World.BiomeVC;
import at.tyron.vintagecraft.WorldGen.GenLayers.GenLayerVC;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.IntCache;

public class WorldChunkManagerFlatVC extends WorldChunkManager {
	protected World worldObj;
	public long seed = 0;
	GenLayerVC climateGen;

	public WorldChunkManagerFlatVC(World world) {
		this(world.getSeed(), world.getWorldInfo().getTerrainType());
		worldObj = world;
	}
	
	public WorldChunkManagerFlatVC(long Seed, WorldType worldtype) {
		super();
		this.seed = Seed;
		climateGen = GenLayerVC.genClimate(seed);
	}
	
	@Override
	public BlockPos findBiomePosition(int xCoord, int zCoord, int radius, List biomeList, Random rand) {
		return new BlockPos(0, 0, 0);
	}

	
	@Override
	public BiomeVC[] getBiomesForGeneration(BiomeGenBase[] biomebase, int x, int y, int width, int height) {
		IntCache.resetIntCache();

		BiomeVC[] biomes = (BiomeVC[]) biomebase;
		if (biomes == null || biomes.length < width * height) {
			biomes = new BiomeVC[width * height];
		}

		for (int i = 0; i < width * height; ++i) {
			biomes[i] = BiomeVC.Flat;
		}
		
		//System.out.println("length: " + biomes.length);
		return biomes;
	}
	
	
	@Override
	public float[] getRainfall(float[] listToReuse, int x, int z, int width, int length) {
		Arrays.fill(listToReuse, 0f);
		return listToReuse;
	}


	

	@Override
	public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] par1, int par2, int par3, int par4, int par5) {
		return this.getBiomeGenAt(par1, par2, par3, par4, par5, true);
	}


	@Override
	public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] biomes, int x, int y, int width, int length, boolean cache) {
		IntCache.resetIntCache();
		
		biomes = new BiomeVC[width * length];
		
		for (int i = 0; i < biomes.length; i++) {
			biomes[i] = BiomeVC.Flat;
		}
		
		return biomes;
	}
		
}
