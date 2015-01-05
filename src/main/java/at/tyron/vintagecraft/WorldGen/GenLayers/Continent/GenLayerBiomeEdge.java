package at.tyron.vintagecraft.WorldGen.GenLayers.Continent;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import at.tyron.vintagecraft.World.BiomeVC;
import at.tyron.vintagecraft.WorldGen.GenLayers.GenLayerVC;

public class GenLayerBiomeEdge extends GenLayerVC {

	public GenLayerBiomeEdge(long par1, GenLayer par3GenLayer)
	{
		super(par1);
		this.parent = (GenLayerVC) par3GenLayer;
	}

	/**
	 * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
	 * amounts, or biomeList[] indices based on the particular GenLayer subclass.
	 */
	@Override
	public int[] getInts(int par1, int par2, int xSize, int zSize)
	{
		int[] inCache = this.parent.getInts(par1 - 1, par2 - 1, xSize + 2, zSize + 2);
		validateBiomeIntArray(inCache, xSize+2, zSize+2);
		int[] outCache = IntCache.getIntCache(xSize * zSize);
		int var10;
		int var11;
		int var12;
		int var13;

		for (int z = 0; z < zSize; ++z)
		{
			for (int x = 0; x < xSize; ++x)
			{
				this.initChunkSeed(x + par1, z + par2);
				int thisID = inCache[x + 1 + (z + 1) * (xSize + 2)];

				var10 = inCache[x + 1 + (z + 1 - 1) * (xSize + 2)];
				var11 = inCache[x + 1 + 1 + (z + 1) * (xSize + 2)];
				var12 = inCache[x + 1 - 1 + (z + 1) * (xSize + 2)];
				var13 = inCache[x + 1 + (z + 1 + 1) * (xSize + 2)];

				if (thisID == BiomeVC.HighHills.biomeID)
				{
					if (var10 == BiomeVC.HighHills.biomeID && var11 == BiomeVC.HighHills.biomeID && var12 == BiomeVC.HighHills.biomeID && var13 == BiomeVC.HighHills.biomeID)
						outCache[x + z * xSize] = thisID;
					else
						outCache[x + z * xSize] = BiomeVC.HighHillsEdge.biomeID;
				}
				else if (thisID == BiomeVC.Mountains.biomeID)
				{
					if (var10 == BiomeVC.Mountains.biomeID && var11 == BiomeVC.Mountains.biomeID && var12 == BiomeVC.Mountains.biomeID && var13 == BiomeVC.Mountains.biomeID)
						outCache[x + z * xSize] = thisID;
					else
						outCache[x + z * xSize] = BiomeVC.MountainsEdge.biomeID;
				}
				else if (thisID == BiomeVC.swampland.biomeID)
				{
					if (var10 == BiomeVC.swampland.biomeID && var11 == BiomeVC.swampland.biomeID && var12 == BiomeVC.swampland.biomeID && var13 == BiomeVC.swampland.biomeID)
						outCache[x + z * xSize] = thisID;
					else
						outCache[x + z * xSize] = BiomeVC.plains.biomeID;
				}
				else if (thisID == BiomeVC.HighPlains.biomeID)
				{
					if (var10 == BiomeVC.HighPlains.biomeID && var11 == BiomeVC.HighPlains.biomeID && var12 == BiomeVC.HighPlains.biomeID && var13 == BiomeVC.HighPlains.biomeID)
						outCache[x + z * xSize] = thisID;
					else
						outCache[x + z * xSize] = BiomeVC.plains.biomeID;
				}
				else
				{
					outCache[x + z * xSize] = thisID;
				}

				validateInt(outCache, x + z * xSize);
			}
		}
		return outCache;
	}

}
