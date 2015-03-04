package at.tyron.vintagecraft.WorldGen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.World.BiomeVC;
import at.tyron.vintagecraft.WorldGen.GenLayers.GenLayerVC;


import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.IntCache;

public class WorldChunkManagerVC extends WorldChunkManager {
	protected World worldObj;
	protected GenLayerVC genBiomes;
	public GenLayerVC climateGen;
	
	/** A list of biomes that the player can spawn in. */
	protected List biomesToSpawnIn;
	

	public long seed = 0;
	
	private WorldChunkManagerVC() {
		super();
		this.biomesToSpawnIn = new ArrayList();
		//this.biomesToSpawnIn.add(BiomeVC.HighHills);
		this.biomesToSpawnIn.add(BiomeVC.Flat);
		this.biomesToSpawnIn.add(BiomeVC.Mountains);
		this.biomesToSpawnIn.add(BiomeVC.HighHills);
		
		/*this.biomesToSpawnIn.add(BiomeVC.rollingHills);
		this.biomesToSpawnIn.add(BiomeVC.swampland);
		this.biomesToSpawnIn.add(BiomeVC.Mountains);
		this.biomesToSpawnIn.add(BiomeVC.HighPlains);*/
	}

	
	
	public WorldChunkManagerVC(World world) {
		this(world.getSeed(), world.getWorldInfo().getTerrainType());
		worldObj = world;
	}
	
	public WorldChunkManagerVC(long Seed, WorldType worldtype) {
		this();
		seed = Seed;

		this.genBiomes = GenLayerVC.genErosion(Seed);
		climateGen = GenLayerVC.genClimate(seed);
	}
	
	@Override
	public List getBiomesToSpawnIn() {
		return this.biomesToSpawnIn;
	}
	
	
	
	/**
	 * Returns an array of biomes for the location input.
	 */
	@Override
	public BiomeVC[] getBiomesForGeneration(BiomeGenBase[] biomebase, int x, int y, int width, int height) {
		IntCache.resetIntCache();

		BiomeVC[] biomes = (BiomeVC[]) biomebase;
		if (biomes == null || biomes.length < width * height) {
			biomes = new BiomeVC[width * height];
		}

		int[] intmap = this.genBiomes.getInts(x, y, width, height);
		
		for (int i = 0; i < width * height; ++i) {
			int index = Math.max(intmap[i], 0);
			biomes[i] = BiomeVC.biomeList[index];
		}
		
		//System.out.println("length: " + biomes.length);
		return biomes;
	}

	

	@Override
	public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] par1, int par2, int par3, int par4, int par5) {
		return this.getBiomeGenAt(par1, par2, par3, par4, par5, true);
	}


	@Override
	public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] biomes, int x, int y, int width, int length, boolean cache) {
		IntCache.resetIntCache();
		
	//	System.out.println("call to getbiomegenat @" + x + " / " + y);
		
		int ints[] = genBiomes.getInts(x, y, width, length);
		
		biomes = new BiomeVC[width * length];
		
		for (int i = 0; i < biomes.length; i++) {
			biomes[i] = BiomeVC.biomeList[ints[i]];
		}
		
		return biomes;
	}

	
	
	

	@Override
	public BlockPos findBiomePosition(int xCoord, int zCoord, int radius, List biomeList, Random rand) {
		IntCache.resetIntCache();
		int l = xCoord - radius >> 2;
		int i1 = zCoord - radius >> 2;
		int j1 = xCoord + radius >> 2;
		int k1 = zCoord + radius >> 2;
		int l1 = j1 - l + 1;
		int i2 = k1 - i1 + 1;
		int[] aint = this.climateGen.getInts(l, i1, l1, i2);
		BlockPos chunkposition = null;
		int j2 = 0;
		
		
		int bestrain = 0;
		int besttemp = 0;

		for (int k2 = 0; k2 < l1 * i2; ++k2) {
			int l2 = l + k2 % l1 << 2;
			int i3 = i1 + k2 / l1 << 2;
			
			int climate = aint[k1];
			int rain = aint[k1] & 0xff;
			int temp = (climate >> 8) & 0xff;
			
			
			if (Math.abs(bestrain - 150) > Math.abs(rain - 150) && Math.abs(besttemp - 150) > Math.abs(temp - 150)) {
				bestrain = rain;
				besttemp = temp;
				
				chunkposition = new BlockPos(l2, 0, i3);
			}
			
			/*BiomeVC biomegenbase = BiomeVC.getBiome(aint[k2]);

			if (biomeList.contains(biomegenbase) && (chunkposition == null || rand.nextInt(j2 + 1) == 0)) {
				chunkposition = new BlockPos(l2, 0, i3);
				++j2;
			}*/
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
	/*	IntCache.resetIntCache();
		int var5 = par1 - par3 >> 2;
				int var6 = par2 - par3 >> 2;
		int var7 = par1 + par3 >> 2;
		int var8 = par2 + par3 >> 2;
		int var9 = var7 - var5 + 1;
		int var10 = var8 - var6 + 1;
		int[] var11 = this.genBiomes.getInts(var5, var6, var9, var10);

		for (int var12 = 0; var12 < var9 * var10; ++var12) {
			BiomeVC var13 = BiomeVC.getBiomeGenArray()[var11[var12]];
			if (!par4List.contains(var13))
				return false;
		}*/
		return true;
	}
	

	
	@Override
	public void cleanupCache() {
		
	}
	
	
	
}
