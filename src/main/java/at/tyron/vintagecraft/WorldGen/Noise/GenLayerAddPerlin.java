package at.tyron.vintagecraft.WorldGen.Noise;

import net.minecraft.util.MathHelper;
import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldGen.GenLayerVC;

public class GenLayerAddPerlin extends GenLayerVC {
	SimplexNoise noisegen;
	
	int amplitude;
	int bitoffset;
	int offset;
	float scale;
	
	public GenLayerAddPerlin(long seed, int octaves, float persistence, int amplitude, int offset, int bitoffset, float scale, GenLayerVC parent) {
		super(seed);
		this.parent = parent;
		
		noisegen = new SimplexNoise(octaves, persistence, seed);
		
		this.amplitude = amplitude;
		this.bitoffset = bitoffset;
		this.scale = scale;
		this.offset = offset;
	}
	
	
	@Override
	public int[] getInts(int xCoord, int zCoord, int sizeX, int sizeZ) {
		int[] cache = parent.getInts(xCoord, zCoord, sizeX, sizeZ);

		for (int z = 0; z < sizeZ; ++z) {
			for (int x = 0; x < sizeX; ++x) {
				
				//int height = cache[x + z * sizeX];  // ~ 64 - 180
				//float heightrel = Math.max(0f, (height - VCraftWorld.instance.seaLevel) / (256f - VCraftWorld.instance.seaLevel));
				//cache[x + z * sizeX] = Math.min(255, 15 + height + (int)(heightrel * amplitude * (1f + noisgen.perlinNoise2D((xCoord + x) / 16f, (zCoord + z) / 16f))));
				
				int value = MathHelper.clamp_int((int) (offset + amplitude * noisegen.getNoise((xCoord +  x) / scale, (zCoord + z) / scale)), 0, 255);
				
				cache[x + z * sizeX] = cache[x + z * sizeX] | (value << bitoffset);
				
			}
		}

		return cache;
	}
		 
}
