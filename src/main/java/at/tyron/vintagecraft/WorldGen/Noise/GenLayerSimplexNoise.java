package at.tyron.vintagecraft.WorldGen.Noise;

import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldGen.GenLayerVC;

public class GenLayerSimplexNoise extends GenLayerVC {
	int amplitude;
	int offset;
	SimplexNoise noisegen;
	public double yCoord = 0;
	
	
	public double scale = 512.0;
	
	public GenLayerSimplexNoise(long seed, int octaves, float persistence, int amplitude, int offset) {
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
				cache[x + z * sizeX] = Math.max(0, Math.min(255, offset + (int)(amplitude * (1f + noisegen.getNoise((xCoord + x) / scale, yCoord, (zCoord + z) / scale)))));
			}
		}

		return cache;
	}

}
