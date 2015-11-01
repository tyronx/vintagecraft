package at.tyron.vintagecraft.WorldGen;

import java.util.Hashtable;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

public class HeightmapCache {
	public static Hashtable<Integer, HeightmapCache> worldHeightMaps = new Hashtable<Integer, HeightmapCache>();
	
	
	public static HeightmapCache getHeightmapCacheForDimension(int dimension) {
		HeightmapCache cache = worldHeightMaps.get(dimension);
		if (cache == null) {
			worldHeightMaps.put(dimension, cache = new HeightmapCache());
		}
		return cache;
	}
	
	public static int getHeightAt(World world, int xPos, int zPos) {
		HeightmapCache cache = getHeightmapCacheForDimension(world.provider.getDimensionId());
		return cache.getHeightAt_(world, xPos, zPos);
	}
	
	
	public static void putHeightMap(int dimension, int []heightmap, int chunkX, int chunkZ) {
		HeightmapCache cache = getHeightmapCacheForDimension(dimension);
		
		if (cache.heightmaps.size() > 200) {
			cache.heightmaps.remove(cache.heightmaps.keys().nextElement());
		}

		
		long hash = ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ);
		cache.heightmaps.put(hash, heightmap);
		
	}
	
	
	public static void removeHeightMap(int dimension, int chunkX, int chunkZ) {
		HeightmapCache cache = getHeightmapCacheForDimension(dimension);
		long hash = ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ);
		cache.heightmaps.remove(hash);
	}
	
	
	
	
	

	
	public Hashtable<Long, int[]> heightmaps;
	
	
	public HeightmapCache() {
		heightmaps = new Hashtable<Long, int[]>();
	}
	
	private int getHeightAt_(World world, int xPos, int zPos) {
		long hash = ChunkCoordIntPair.chunkXZ2Int(xPos >> 4, zPos >> 4);
		int[] values = heightmaps.get(hash);
		
		if (values == null) {
			values = world.getChunkFromChunkCoords(xPos >> 4, zPos >> 4).getHeightMap();
			putHeightMap(world.provider.getDimensionId(), values, xPos >> 4, zPos >> 4);
		}
		
		return values[16 * (zPos & 15) + (xPos & 15)];
	}
	
	
	
	
}
