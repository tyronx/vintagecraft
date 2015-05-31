package at.tyron.vintagecraft.WorldGen.Noise;

import at.tyron.vintagecraft.WorldGen.GenLayerVC;


public class GenLayerSimplexNoiseUnclamped extends GenLayerVC {
	int amplitude;
	int offset;
	public float resolution = 512;
	
	SimplexNoise noisegen;
	public double yCoord = 0;
	
	
	
	public GenLayerSimplexNoiseUnclamped(long seed, int octaves, float persistence, int amplitude, int offset) {
		super(seed);
		this.amplitude = amplitude;
		this.offset = offset;
		noisegen = new SimplexNoise(octaves, persistence, seed);
	}
	
	@Override
	public int[] getInts(int xCoord, int zCoord, int sizeX, int sizeZ) {
		int[] cache = new int[sizeX * sizeZ];

		for (int z = 0; z < sizeZ; ++z) {
			for (int x = 0; x < sizeX; ++x) {
				cache[x + z * sizeX] = offset + (int)(amplitude * (1f + noisegen.getNoise((xCoord + x) / resolution, yCoord, (zCoord + z) / resolution)));
			}
		}

		return cache;
	}
}
