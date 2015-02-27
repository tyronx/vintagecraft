package at.tyron.vintagecraft.WorldGen.GenLayers;

import at.tyron.vintagecraft.VCraftWorld;
import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

public class GenLayerAddPerlin extends GenLayerVC {
	/*PerlinNoise noisegen;
	RivenPerlinNoise noisegen2;*/
	Noisy noisgen3;
	
	int amplitude;
	
	public GenLayerAddPerlin(long seed, int octaves, float persistence, int amplitude, GenLayerVC parent) {
		super(seed);
		this.parent = parent;
		//System.out.println(seed);
		//noisegen = new PerlinNoise(seed);
		//noisegen2 = new RivenPerlinNoise(seed);
		noisgen3 = new Noisy(seed, octaves, persistence);
		
		this.amplitude = amplitude;
	}
	
	@Override
	public int[] getInts(int xCoord, int zCoord, int sizeX, int sizeZ) {
		int[] cache = parent.getInts(xCoord, zCoord, sizeX, sizeZ);

		for (int z = 0; z < sizeZ; ++z) {
			for (int x = 0; x < sizeX; ++x) {
				//System.out.println(noisegen2.smoothNoise(xCoord + x, 0f, zCoord + z, 4));
				//System.out.println((noisgen3.perlinNoise2D(xCoord + x, zCoord + z) + 1f)/2);
				int height = cache[x + z * sizeX];  // ~ 64 - 180
				float heightrel = Math.max(0f, (height - VCraftWorld.instance.seaLevel) / (256f - VCraftWorld.instance.seaLevel));
				//System.out.println(heightrel);
				
				cache[x + z * sizeX] = Math.min(255, 15 + height + (int)(heightrel * amplitude * (1f + noisgen3.perlinNoise2D((xCoord + x) / 16f, (zCoord + z) / 16f))));
			}
		}

		return cache;
	}
		 
}
