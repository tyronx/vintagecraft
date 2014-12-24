package at.tyron.vintagecraft.WorldGen;

import at.tyron.vintagecraft.WorldGen.GenLayers.GenLayerVC;

public class DataCacheBlockVC {
	/** The array of data types stored in this DataCacheBlockVC. */
	public DataLayer[] data;
	/** The x coordinate of the DataCacheBlockVC. */
	public int xPosition;
	/** The z coordinate of the DataCacheBlockVC. */
	public int zPosition;
	/** The last time this DataCacheBlockVC was accessed, in milliseconds. */
	public long lastAccessTime;
	/** The DataCache object that contains this DataCacheBlockVC */
	final DataCache theDataCache;

	private int index;

	public DataCacheBlockVC(DataCache datacache, GenLayerVC indexLayers, int par2, int par3, int ind)
	{
		this.theDataCache = datacache;
		this.data = new DataLayer[256];
		this.xPosition = par2;
		this.zPosition = par3;
		index = ind;
		DataCache.getChunkManager(datacache).getDataLayerAt(datacache, data, indexLayers, par2 << 4, par3 << 4, 16, 16, false, index);
	}

	public DataCacheBlockVC(DataCache datacache, int par2, int par3)
	{
		this.theDataCache = null;
		this.xPosition = par2;
		this.zPosition = par3;
	}

	/**
	 * Returns the Datalayer related to the x, z position from the cache block.
	 */
	public DataLayer getDataLayerAt(int par1, int par2)
	{
		return this.data[par1 & 15 | (par2 & 15) << 4];
	}
}
