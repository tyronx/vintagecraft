package at.tyron.vintagecraft.WorldGen;

import at.tyron.vintagecraft.World.EnumCrustLayer;
import at.tyron.vintagecraft.WorldGen.GenLayers.GenLayerVC;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.layer.IntCache;

public class WorldCacheManager {
	World localWorld;
	//Rocks
	protected GenLayerVC[] rocksIndexLayer;
	protected DataCache[] rockCache;
	
	//Trees
	protected GenLayerVC[] treesIndexLayer;
	protected DataCache[] treeCache;
	
	public long seed = 0;

	/*public static DataLayer[] RockLayer1 = new DataLayer[]{
		DataLayer.SandStone, DataLayer.Diorite, DataLayer.Andesite, DataLayer.Slate
	};
	public static DataLayer[] RockLayer2 = new DataLayer[]{
			DataLayer.Diorite, DataLayer.Andesite, DataLayer.Slate
	};
	public static DataLayer[] RockLayer3 = new DataLayer[]{
		DataLayer.Granite, DataLayer.Andesite, DataLayer.Slate
	};*/

	public static DataLayer[] treeArray = new DataLayer[] {DataLayer.Ash, DataLayer.Aspen, DataLayer.Birch, DataLayer.Chestnut, DataLayer.DouglasFir, 
		DataLayer.Hickory, DataLayer.Maple, DataLayer.Oak, DataLayer.Pine, DataLayer.Redwood, DataLayer.Pine, DataLayer.Spruce, DataLayer.Sycamore, 
		DataLayer.WhiteCedar, DataLayer.WhiteElm, DataLayer.Willow, DataLayer.NoTree};

	private WorldCacheManager() {
		rockCache = new DataCache[3];
		treeCache = new DataCache[3];
		//evtCache = new DataCache(this, 0);
		//rainfallCache = new DataCache(this, 0);
		rockCache[0] = new DataCache(this, 0);
		rockCache[1] = new DataCache(this, 1);
		rockCache[2] = new DataCache(this, 2);
		treeCache[0] = new DataCache(this, 0);
		treeCache[1] = new DataCache(this, 1);
		treeCache[2] = new DataCache(this, 2);
		/*stabilityCache = new DataCache(this, 0);
		phCache = new DataCache(this, 0);
		drainageCache = new DataCache(this, 0);
		worldTempCache = new LinkedHashMap<String, Float>();*/
		
		//RockLayer1 = EnumRockType.getRockTypesForCrustLayer(EnumCrustLayer.ROCK1)
	}

	public WorldCacheManager(World world) {
		this(world.getSeed(), world.getWorldInfo().getTerrainType());
		localWorld = world;
	}

	private WorldCacheManager(long Seed, WorldType worldtype) {
		this();
		seed = Seed;

		//Setup Rocks
		/*rocksIndexLayer = new GenLayerVC[3];
		rocksIndexLayer[0] = GenRockLayer.initialize(Seed+1, EnumCrustLayer.ROCK1);
		rocksIndexLayer[1] = GenRockLayer.initialize(Seed+2, EnumCrustLayer.ROCK2);
		rocksIndexLayer[2] = GenRockLayer.initialize(Seed+3, EnumCrustLayer.ROCK3);*/
/*

		//Setup Trees
		treesIndexLayer = new GenLayerVC[3];

		treesIndexLayer[0] = GenTreeLayer.initialize(Seed+4, treeArray);
		treesIndexLayer[1] = GenTreeLayer.initialize(Seed+5, treeArray);
		treesIndexLayer[2] = GenTreeLayer.initialize(Seed+6, treeArray);
		
		
		*/
		
/*
		//Setup Evapotranspiration
		evtIndexLayer = GenEVTLayer.initialize(Seed+7, worldtype);

		//Setup Rainfall
		rainfallIndexLayer = GenRainLayerTFC.initialize(Seed+8, worldtype);

		//Setup Stability
		stabilityIndexLayer = GenStabilityLayer.initialize(Seed+9, worldtype);

		//Setup Soil PH
		phIndexLayer = GenPHLayer.initialize(Seed+10, worldtype);

		//Setup Soil Drainage
		drainageIndexLayer = GenDrainageLayer.initialize(Seed+11, worldtype);

		worldTempCache = new LinkedHashMap<String, Float>();
		*/
	}
	
	
	
	
	public DataLayer getDataLayerAt(DataCache cache, GenLayerVC indexLayers, int par1, int par2, int index)
	{
		return cache.getDataLayerAt(indexLayers, par1, par2);
	}

	private DataLayer[] loadDataLayerGeneratorData(DataCache[] cache, DataLayer[] layers, GenLayerVC[] indexLayers, int par2, int par3, int par4, int par5, int layer)
	{
		return this.getDataLayerAt(cache, layers, indexLayers, par2, par3, par4, par5, true, layer);
	}

	public DataLayer[] getDataLayerAt(DataCache[] cache, DataLayer[] layers, GenLayerVC[] indexLayers, int x, int y, int width, int height, boolean par6, int layer)
	{
		if (layers == null || layers.length < width * height)
			layers = new DataLayer[width * height];

		if (par6 && width == 16 && height == 16 && (x & 15) == 0 && (y & 15) == 0)
		{
			DataLayer[] var9 = cache[layer].getCachedData(indexLayers[layer], x, y);
			System.arraycopy(var9, 0, layers, 0, width * height);
			return layers;
		}
		else
		{
			IntCache.resetIntCache();
			int[] var7 = indexLayers[layer].getInts(x, y, width, height);
			for (int var8 = 0; var8 < width * height; ++var8)
			{
				layers[var8] = DataLayer.layers[var7[var8]];
			}
			return layers;
		}
	}

	public DataLayer[] getDataLayerAt(DataCache cache, DataLayer[] layers, GenLayerVC indexLayers, int x, int y, int width, int height, boolean par6, int layer)
	{
		if (layers == null || layers.length < width * height)
			layers = new DataLayer[width * height];

		if (par6 && width == 16 && height == 16 && (x & 15) == 0 && (y & 15) == 0)
		{
			DataLayer[] var9 = cache.getCachedData(indexLayers, x, y);
			System.arraycopy(var9, 0, layers, 0, width * height);
			return layers;
		}
		else
		{
			IntCache.resetIntCache();
			int[] var7 = indexLayers.getInts(x, y, width, height);
			for (int var8 = 0; var8 < width * height; ++var8)
			{
				layers[var8] = DataLayer.layers[var7[var8]];
			}
			return layers;
		}
	}
	
	
	public DataLayer[] loadRockLayerGeneratorData(DataLayer[] layers, int x, int y, int width, int height, int layer) {
		return this.getDataLayerAt(rockCache, layers, rocksIndexLayer, x, y, width, height, true, layer);
	}
	

	public DataLayer getRockLayerAt(int x, int y, int index) {
		return this.rockCache[index].getDataLayerAt(rocksIndexLayer[index], x, y);
	}
}
