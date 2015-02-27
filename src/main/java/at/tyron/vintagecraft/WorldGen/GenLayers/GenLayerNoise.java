package at.tyron.vintagecraft.WorldGen.GenLayers;

public class GenLayerNoise extends GenLayerVC {
	int level = 50; // Between 0..100
	int quantityColors = 2;
	
	public GenLayerNoise(long seed) {
		super(seed);
	}

	
	public GenLayerNoise(long seed, int level) {
		this (seed, level, 2);
	}

	
	public GenLayerNoise(long seed, int level, int quantityColors) {
		super(seed);
		this.level = level;
		this.quantityColors = quantityColors;
	}

	
	@Override
	public int[] getInts(int xCoord, int zCoord, int sizeX, int sizeZ) {
		int[] cache = new int[sizeX * sizeZ];
		
		for (int z = 0; z < sizeZ; ++z) {
			for (int x = 0; x < sizeX; ++x) {
				this.initChunkSeed(xCoord + x, zCoord + z);
				cache[x + z * sizeX] = this.nextInt(100) <= level ? ((1+this.nextInt(quantityColors-1)) * 255 / (quantityColors-1)) : 0;
			}
		}

		return cache;
	}

}
