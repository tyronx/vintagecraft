package at.tyron.vintagecraft.WorldGen.GenLayers;

public class GenLayerAddNoise extends GenLayerNoise {
	int rgbselect;
	int nonBlackOverlayWeight;
	
	int maxLightness = 255;
	
	public GenLayerAddNoise(long seed, int level, GenLayerVC parent) {
		super(seed, level, 2);
		this.parent = parent;
	}

	
	public GenLayerAddNoise(long seed, int level, int quantityColors, int rgb_select, int nonBlackOverlayWeight, int maxLightness, GenLayerVC parent) {
		super(seed, level, quantityColors);
		this.parent = parent;
		this.rgbselect = rgb_select;
		this.nonBlackOverlayWeight = nonBlackOverlayWeight;
		this.maxLightness = maxLightness;
	}
	
	
	

	@Override
	public int[] getInts(int xCoord, int zCoord, int sizeX, int sizeZ) {
		int[] cache = parent.getInts(xCoord, zCoord, sizeX, sizeZ);
		
		int colorClearMask = ~(0xff << rgbselect);
		
		
		
		for (int z = 0; z < sizeZ; ++z) {
			for (int x = 0; x < sizeX; ++x) {
				this.initChunkSeed(xCoord + x, zCoord + z);
				//cache[x + z * sizeX] = this.nextInt(100) <= level ? ((1+this.nextInt(quantityColors-1)) * 255 / (quantityColors-1)) : cache[x + z * sizeX];
				
				if (this.nextInt(100) <= level) {
					int oldcolor = (cache[x + z * sizeX] >> rgbselect) & 0xff;
					int rndcolor = (1 + this.nextInt(quantityColors-1)) * maxLightness / (quantityColors-1);
					int weightedColor = (((100 - nonBlackOverlayWeight) * oldcolor + nonBlackOverlayWeight * rndcolor) / 100) << rgbselect; 
					
					cache[x + z * sizeX] = 
						(cache[x + z * sizeX] == 0 ? rndcolor << rgbselect : weightedColor)
						+ (cache[x + z * sizeX] & colorClearMask);
				}
			}
		}

		return cache;
	}

}


