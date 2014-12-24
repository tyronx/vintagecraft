package at.tyron.vintagecraft.WorldGen.GenLayers.River;

import at.tyron.vintagecraft.WorldGen.VCBiome;
import at.tyron.vintagecraft.WorldGen.GenLayers.GenLayerVC;
import net.minecraft.world.gen.layer.GenLayer;

public class GenLayerRiverInit extends GenLayerVC {

	public GenLayerRiverInit(long par1, GenLayerVC par3GenLayer)
	{
		super(par1);
		this.parent = par3GenLayer;
	}

	/**
	 * Creates the random width of the river at the location
	 */
	@Override
	public int[] getInts(int xCoord, int zCoord, int xSize, int zSize)
	{
		int[] parentCache = this.parent.getInts(xCoord, zCoord, xSize, zSize);
		int[] outCache = new int[xSize * zSize];

		for (int z = 0; z < zSize; ++z)
		{
			for (int x = 0; x < xSize; ++x)
			{
				this.initChunkSeed(x + xCoord, z + zCoord);
				int index = x + z * xSize;
				int xn = index-1;
				int xp = index+1;
				int zn = index-zSize;
				int zp = index+zSize;
				int id = parentCache[index];
				//outCache[index] = !VC_Core.isOceanicBiome(id) && !VC_Core.isMountainBiome(id) ? 1 : 0;
				
				outCache[index] = (VCBiome.isOceanic(id) || id == VCBiome.Mountains.biomeID || id == VCBiome.MountainsEdge.biomeID) ? 0 : 1;				
			}
		}
		return outCache;
	}
	
}
