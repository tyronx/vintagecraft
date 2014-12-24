package at.tyron.vintagecraft.WorldGen;

import java.util.ArrayList;
import java.util.List;

import at.tyron.vintagecraft.WorldGen.GenLayers.GenLayerVC;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.LongHashMap;

public class DataCache {
	/** Reference to the WorldChunkManager */
	private final WorldCacheManager chunkManager;
	/** The last time this BiomeCache was cleaned, in milliseconds.*/
	private long lastCleanupTime = 0L;
	/** The map of keys to BiomeCacheBlocks. Keys are based on the chunk x, z coordinates as (x | z << 32). */
	private LongHashMap cacheMap = new LongHashMap();
	/** The list of cached BiomeCacheBlocks */
	private List cache = new ArrayList();
	private int index;

	public DataCache(WorldCacheManager worldLayerManager)
	{
		this.chunkManager = worldLayerManager;
	}

	public DataCache(WorldCacheManager par1, int ind)
	{
		this.chunkManager = par1;
		index = ind;
	}

	public DataCacheBlockVC getDataCacheBlock(GenLayerVC indexLayers, int x, int y)
	{
		x >>= 4;
		y >>= 4;
		long var3 = (long)x & 4294967295L | ((long)y & 4294967295L) << 32;
		DataCacheBlockVC var5 = (DataCacheBlockVC)this.cacheMap.getValueByKey(var3);
		if (var5 == null)
		{
			var5 = new DataCacheBlockVC(this, indexLayers, x, y, index);
			this.cacheMap.add(var3, var5);
			this.cache.add(var5);
		}
		var5.lastAccessTime = MinecraftServer.getCurrentTimeMillis();
		return var5;
	}

	public DataLayer getDataLayerAt(GenLayerVC indexLayers, int par1, int par2)
	{
		return this.getDataCacheBlock(indexLayers, par1, par2).getDataLayerAt(par1, par2);
	}

	public void cleanupCache()
	{
		long var1 = MinecraftServer.getCurrentTimeMillis();
		long var3 = var1 - this.lastCleanupTime;
		if (var3 > 7500L || var3 < 0L)
		{
			this.lastCleanupTime = var1;
			for (int var5 = 0; var5 < this.cache.size(); ++var5)
			{
				DataCacheBlockVC var6 = (DataCacheBlockVC)this.cache.get(var5);
				if(var6 != null)
				{
					long var7 = var1 - var6.lastAccessTime;
					if (var7 > 30000L || var7 < 0L)
					{
						this.cache.remove(var5--);
						long var9 = (long)var6.xPosition & 4294967295L | ((long)var6.zPosition & 4294967295L) << 32;
						this.cacheMap.remove(var9);
					}
				}
			}
		}
	}

	public DataLayer[] getCachedData(GenLayerVC indexLayers, int par1, int par2)
	{
		return this.getDataCacheBlock(indexLayers, par1, par2).data;
	}

	/**
	 * Get the world chunk manager object for a biome list.
	 */
	static WorldCacheManager getChunkManager(DataCache cache)
	{
		return cache.chunkManager;
	}
}
