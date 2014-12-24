package at.tyron.vintagecraft.WorldGen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import scala.actors.threadpool.Arrays;

import at.tyron.vintagecraft.WorldGen.GenLayers.GenLayerVC;


import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.IntCache;

public class VCWorldChunkManager extends WorldChunkManager {
	protected World worldObj;
	protected GenLayerVC genBiomes;
	protected GenLayerVC biomeIndexLayer;

	/** The BiomeCache object for this world. */
	protected BiomeCache biomeCache;
	
	/** A list of biomes that the player can spawn in. */
	protected List biomesToSpawnIn;
	

	public long seed = 0;
	
	private VCWorldChunkManager() {
		super();
		biomeCache = new BiomeCache(this);
		this.biomesToSpawnIn = new ArrayList();
		this.biomesToSpawnIn.add(VCBiome.HighHills);
		this.biomesToSpawnIn.add(VCBiome.plains);
		this.biomesToSpawnIn.add(VCBiome.rollingHills);
		this.biomesToSpawnIn.add(VCBiome.swampland);
		this.biomesToSpawnIn.add(VCBiome.Mountains);
		this.biomesToSpawnIn.add(VCBiome.HighPlains);
	}

	
	
	public VCWorldChunkManager(World world) {
		this(world.getSeed(), world.getWorldInfo().getTerrainType());
		worldObj = world;
	}
	
	public VCWorldChunkManager(long Seed, WorldType worldtype) {
		this();
		seed = Seed;

		GenLayerVC[] layers = GenLayerVC.genBiomes(Seed);

		this.genBiomes = layers[0];
		this.biomeIndexLayer = layers[1];
	}
	
	@Override
	public List getBiomesToSpawnIn() {
		return this.biomesToSpawnIn;
	}
	
	
	
	/**
	 * Returns an array of biomes for the location input.
	 */
	@Override
	public VCBiome[] getBiomesForGeneration(BiomeGenBase[] biomebase, int x, int y, int width, int height) {
		IntCache.resetIntCache();

		VCBiome[] biomes = (VCBiome[]) biomebase;
		if (biomes == null || biomes.length < width * height) {
			biomes = new VCBiome[width * height];
		}

		int[] intmap = this.genBiomes.getInts(x, y, width, height);
		
		for (int i = 0; i < width * height; ++i) {
			int index = Math.max(intmap[i], 0);
			biomes[i] = VCBiome.getBiome(index);
		}
		
		//System.out.println("length: " + biomes.length);
		return biomes;
	}

	
	
	/**
	 * Returns biomes to use for the blocks and loads the other data like temperature and humidity onto the
	 * WorldChunkManager Args: oldBiomeList, x, z, width, depth
	 */
	@Override
	public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] par1, int par2, int par3, int par4, int par5)
	{
		return this.getBiomeGenAt(par1, par2, par3, par4, par5, true);
	}

	/**
	 * Return a list of biomes for the specified blocks. Args: listToReuse, x, y, width, length, cacheFlag (if false,
	 * don't check biomeCache to avoid infinite loop in BiomeCacheBlock)
	 */
	@Override
	public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] biomes, int x, int y, int width, int length, boolean cache) {
		IntCache.resetIntCache();

		if (biomes == null || biomes.length < width * length) {
			biomes = new VCBiome[width * length];
		}

		if (cache && width == 16 && length == 16 && (x & 15) == 0 && (y & 15) == 0)
		{
			BiomeGenBase[] var9 = this.biomeCache.getCachedBiomes(x, y);
			System.arraycopy(var9, 0, biomes, 0, width * length);
			return biomes;
		}
		else
		{
			int[] var7 = this.biomeIndexLayer.getInts(x, y, width, length);
			for (int zCoord = 0; zCoord < width; ++zCoord)
			{
				for (int xCoord = 0; xCoord < length; ++xCoord)
				{
					int id = var7[zCoord * width + xCoord] != -1 ? var7[zCoord * width + xCoord] : 0;
					biomes[zCoord * width + xCoord] = VCBiome.getBiome(id);
				}
			}
			return biomes;
		}
	}

	
	
	
	/**
	 * Finds a valid position within a range, that is in one of the listed biomes. Searches {par1,par2} +-par3 blocks.
	 * Strongly favors positive y positions.
	 */
	@Override
	public BlockPos findBiomePosition(int xCoord, int zCoord, int radius, List biomeList, Random rand) {
		IntCache.resetIntCache();
		int l = xCoord - radius >> 2;
		int i1 = zCoord - radius >> 2;
		int j1 = xCoord + radius >> 2;
		int k1 = zCoord + radius >> 2;
		int l1 = j1 - l + 1;
		int i2 = k1 - i1 + 1;
		int[] aint = this.genBiomes.getInts(l, i1, l1, i2);
		BlockPos chunkposition = null;
		int j2 = 0;

		for (int k2 = 0; k2 < l1 * i2; ++k2) {
			int l2 = l + k2 % l1 << 2;
			int i3 = i1 + k2 / l1 << 2;
			VCBiome biomegenbase = VCBiome.getBiome(aint[k2]);

			if (biomeList.contains(biomegenbase) && (chunkposition == null || rand.nextInt(j2 + 1) == 0)) {
				chunkposition = new BlockPos(l2, 0, i3);
				++j2;
			}
		}

		return chunkposition;
	}

	
	

	
	/**
	 * Returns a list of rainfall values for the specified blocks. Args: listToReuse, x, z, width, length.
	 */
	@Override
	public float[] getRainfall(float[] listToReuse, int x, int z, int width, int length) {
		Arrays.fill(listToReuse, 0f);
		return listToReuse;
		//return TFC_Climate.getManager(worldObj).getRainfall(par1ArrayOfFloat, par2, par3, par4, par5);
	}

	
	
	

	/**
	 * checks given Chunk's Biomes against List of allowed ones
	 */
	@Override
	public boolean areBiomesViable(int par1, int par2, int par3, List par4List) {
		IntCache.resetIntCache();
		int var5 = par1 - par3 >> 2;
				int var6 = par2 - par3 >> 2;
		int var7 = par1 + par3 >> 2;
		int var8 = par2 + par3 >> 2;
		int var9 = var7 - var5 + 1;
		int var10 = var8 - var6 + 1;
		int[] var11 = this.genBiomes.getInts(var5, var6, var9, var10);

		for (int var12 = 0; var12 < var9 * var10; ++var12)
		{
			VCBiome var13 = VCBiome.getBiomeGenArray()[var11[var12]];
			if (!par4List.contains(var13))
				return false;
		}
		return true;
	}
	

	
	@Override
	public void cleanupCache() {
		this.biomeCache.cleanupCache();
		//WorldCacheManager wcm = TFC_Climate.getCacheManager(this.worldObj);
		//if(wcm != null) wcm.cleanupCache();
	}
}
