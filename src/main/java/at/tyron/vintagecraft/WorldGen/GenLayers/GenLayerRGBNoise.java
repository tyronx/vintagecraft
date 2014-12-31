package at.tyron.vintagecraft.WorldGen.GenLayers;

public class GenLayerRGBNoise extends GenLayerVC {

	public GenLayerRGBNoise(long seed) {
		super(seed);
	}
	
	@Override
	public int[] getInts(int xCoord, int zCoord, int sizeX, int sizeZ) {
		int[] cache = new int[sizeX * sizeZ];
		
		for (int z = 0; z < sizeZ; ++z) {
			for (int x = 0; x < sizeX; ++x) {
				this.initChunkSeed(xCoord + x, zCoord + z);
				cache[x + z * sizeX] = //this.nextInt(100) <= level ? ((1+this.nextInt(quantityColors-1)) * 255 / (quantityColors-1)) : 0;
						lumpyInt(255)+
						+ 0 //+ (lumpyInt(255) << 8)
						+ (lumpyInt(255) << 16)
				;
			}
		}

		return cache;
	}
	
	
	int lumpyInt(int maxint) {
		return (nextInt(maxint) + nextInt(maxint) + nextInt(maxint)) / 3;
	}

}
