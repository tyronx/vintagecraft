package at.tyron.vintagecraft.WorldGen.GenLayers.Rock;

import at.tyron.vintagecraft.WorldGen.GenLayers.GenLayerVC;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;

public class GenLayerRockInit extends GenLayerVC {

	EnumRockType[] layerRocks;
	
	
	public GenLayerRockInit(long par1, EnumRockType[] rocks) {
		super(par1);
		layerRocks = rocks;
	}

	
	
	@Override
	public int[] getInts(int par1, int par2, int maxX, int maxZ) {
		int[] cache = new int[maxX * maxZ];

		for (int z = 0; z < maxZ; ++z) {
			for (int x = 0; x < maxX; ++x) {
				this.initChunkSeed(par1 + x, par2 + z);
				cache[x + z * maxX] = layerRocks[this.nextInt(layerRocks.length)].id;
			}
		}

		return cache;
	}
}
