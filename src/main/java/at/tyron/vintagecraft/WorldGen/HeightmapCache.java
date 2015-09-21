package at.tyron.vintagecraft.WorldGen;

import java.util.Hashtable;

import net.minecraft.world.ChunkCoordIntPair;

public class HeightmapCache {
	public static Hashtable<Integer, HeightmapCache> worldHeightMaps = new Hashtable<Integer, HeightmapCache>();
	
	
	public static HeightmapCache getHeightmapCacheForDimension(int dimension) {
		HeightmapCache cache = worldHeightMaps.get(dimension);
		if (cache == null) {
			worldHeightMaps.put(dimension, cache = new HeightmapCache());
			System.out.println("created heightmap cache for dimension " + dimension);
		}
		return cache;
	}
	
	public static int getHeightAt(int dimension, int xPos, int zPos) {
		HeightmapCache cache = getHeightmapCacheForDimension(dimension);
		return cache.getHeightAt(xPos, zPos);
	}
	
	
	public static void putHeightMap(int dimension, int []heightmap, int chunkX, int chunkZ) {
		HeightmapCache cache = getHeightmapCacheForDimension(dimension);
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
	
	public int getHeightAt(int xPos, int zPos) {
		long hash = ChunkCoordIntPair.chunkXZ2Int(xPos >> 4, zPos >> 4);
		int[] values = heightmaps.get(hash);
		
		if (values == null) {
			System.out.println("Coding error, heightmap at " + xPos + "/" + zPos + "is null");
			return 128;
		}
		
		return values[16 * (zPos & 15) + (xPos & 15)];
	}
	
	
	
	
}
