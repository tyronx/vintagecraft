package at.tyron.vintagecraft.WorldGen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.World.BiomeVC;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldGen.Helper.WorldChunkManagerFlatVC;
import at.tyron.vintagecraft.WorldGen.Helper.WorldChunkManagerVC;
import at.tyron.vintagecraft.WorldGen.Layer.GenLayerTerrain;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumCrustLayer;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumCrustLayerGroup;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumCrustType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

public class ChunkProviderGenerateVC extends ChunkProviderGenerate {
	World worldObj;
	long seed;
	private Random rand;
	ChunkPrimer primer;

	// 3D Simplex Noise Rock Generator
	GenRockLayers genrocklayers;
	
	// Currently almost no use
	GenLayerVC ageLayer;
	
	MapGenCavesVC caveGenerator;
	MapGenFlora floragenerator;

	// These create deformations in the transitions of rocks, so they are not in a straight line
	GenLayerVC rockOffsetNoiseX;
	GenLayerVC rockOffsetNoiseZ;
	
	GenLayerTerrain normalTerrainGen;
	
	/** The biomes that are used to generate the chunk */
	private BiomeGenBase[] biomeMap;
	
	int[] seaLevelOffsetMap = new int[256];
	int[] chunkGroundLevelMap = new int[256]; // Skips floating islands
	int[] chunkHeightMap = new int[256];


	
	
	public ChunkProviderGenerateVC(World worldIn, long seed, boolean mapfeaturesenabled, String customgenjson) {
		super(worldIn, seed, mapfeaturesenabled, customgenjson);
		ageLayer = GenLayerVC.genAgemap(seed);
		
		caveGenerator = new MapGenCavesVC();
		floragenerator = new MapGenFlora(seed, ageLayer);
		
		this.worldObj = worldIn;
		this.rand = new Random(seed);
		this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
		this.seed = seed;
		
		
		genrocklayers = new GenRockLayers(seed);
		
		rockOffsetNoiseX = GenLayerVC.genHorizontalRockOffsetMap(seed);
		rockOffsetNoiseZ = GenLayerVC.genHorizontalRockOffsetMap(seed+500);
		//heightmapGen = GenLayerVC.genHeightmap(seed);
		
		normalTerrainGen = new GenLayerTerrain(seed + 0);
	}
	
	
	@Override
	public Chunk provideChunk(int chunkX, int chunkZ) {
		if (worldObj.getWorldChunkManager() instanceof WorldChunkManagerFlatVC) {
			return provideFlatChunk(chunkX, chunkZ, (WorldChunkManagerFlatVC)worldObj.getWorldChunkManager());
		}
		WorldChunkManagerVC wcm = (WorldChunkManagerVC)worldObj.getWorldChunkManager();
		
		primer = new ChunkPrimer();
		
		VCraftWorld.instance.setChunkNBT(chunkX, chunkZ, "climate", wcm.climateGen.getInts(chunkX * 16, chunkZ * 16, 16, 16));
		
		//this.rand.setSeed(chunkX * 341873128712L + chunkZ * 132897987541L);
		
		
		biomeMap = worldObj.getWorldChunkManager().loadBlockGeneratorData(biomeMap, chunkX * 16, chunkZ * 16, 16, 16);
		
		//if (chunkX % 4 != 0) { // && (chunkX+1) % 4 != 0) {
			normalTerrainGen.generateTerrain(chunkX, chunkZ, primer, worldObj);
		
			decorate(chunkX, chunkZ, rand, primer);
			caveGenerator.generate(this, this.worldObj, chunkX, chunkZ, primer);
			caveGenerator.generate(this, this.worldObj, chunkX, chunkZ, primer);
			
		//}
		
		Chunk chunk = new Chunk(this.worldObj, primer, chunkX, chunkZ);
		
		
		byte biomeMapbytes[] = new byte[256];
		for (int i = 0; i < biomeMap.length; i++) {
			biomeMapbytes[i] = (byte) biomeMap[i].biomeID;
		}
		
		chunk.setBiomeArray(biomeMapbytes);
		chunk.generateSkylightMap();
		
		
		
		return chunk;
	}
	
	
	private Chunk provideFlatChunk(int chunkX, int chunkZ, WorldChunkManagerFlatVC wcm) {
		primer = new ChunkPrimer();
		VCraftWorld.instance.setChunkNBT(chunkX, chunkZ, "climate", wcm.climateGen.getInts(chunkX * 16, chunkZ * 16, 16, 16));
		Random rand = new Random();
		
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				primer.setBlockState(x, 128, z, EnumCrustType.TOPSOIL.getBlock(EnumRockType.GRANITE, VCraftWorld.instance.getClimate(new BlockPos(chunkX*16 + x, 128, chunkZ*16+z))));
				primer.setBlockState(x, 127, z, BlocksVC.rock.getEntryFromKey(EnumRockType.GRANITE).getBlockState());
			}
		}
		
		Chunk chunk = new Chunk(this.worldObj, primer, chunkX, chunkZ);
		chunk.generateSkylightMap();
		
		return chunk;
	}


	
	@Override
	// chunkprovider is an instance of ChunkProviderServer
	public void populate(IChunkProvider chunkprovider, int chunkX, int chunkZ) {
		
		// Actually working check on whether or not to populate a chunk (prevents runaway chunk generation)
		// Vanilla pop-check is very strangely programmed and fails to prevent runaway chunk generation

		// Seems to be buggy on rare occassions where a stripe of chunks are not populated, but still much better than the game crashing from endless chunk generation   
		
		if (chunkprovider instanceof ChunkProviderServer) {
			int x, z;
			
			for (Iterator<BlockPos> it = VCraftWorld.instance.unpopulatedChunks.iterator(); it.hasNext(); ) {
				BlockPos chunkpos = it.next();
				x = chunkpos.getX();
				z = chunkpos.getZ();
				
				if (shouldPopulate((ChunkProviderServer)chunkprovider, x, z)) {
					it.remove();
					populate(chunkprovider, x, z);
					VCraftWorld.instance.setChunkNBT(x, z, "vcraftpopulated", true);
					break;
				}
			}
			
			if(!shouldPopulate((ChunkProviderServer)chunkprovider, chunkX, chunkZ)) {
				VCraftWorld.instance.unpopulatedChunks.add(new BlockPos(chunkX, 0, chunkZ));
				VCraftWorld.instance.setChunkNBT(chunkX, chunkZ, "vcraftpopulated", false);	
				return;
			}
			
		}
			
		
		BlockPos pos;
		int xCoord = chunkX * 16;
		int zCoord = chunkZ * 16;

		WorldGenAnimals.performWorldGenSpawning(this.worldObj, null, xCoord, zCoord, 16, 16, this.rand);
		
		floragenerator.generate(rand, chunkX, chunkZ, worldObj, chunkprovider, chunkprovider);

		
		BlockPos chunkpos = new BlockPos(xCoord, 0, zCoord);
		int temp;
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				pos = worldObj.getHeight(chunkpos.add(x, 0, z));
				
				if (worldObj.getBlockState(pos.down()).getBlock().getMaterial() != Material.water) {
					temp = VCraftWorld.instance.getTemperature(pos);
					if (temp < -10 || (temp < 2 && rand.nextInt(temp + 11) == 0)) {
						worldObj.setBlockState(pos, Blocks.snow_layer.getDefaultState());
					}
				}
			}
		}
		
		
	}
	
	

	
	
	
	
	
	void decorate(int chunkX, int chunkZ, Random rand, ChunkPrimer primer) {
		Arrays.fill(chunkGroundLevelMap, 0);
		Arrays.fill(chunkHeightMap, 0);
		
		
		//int []heightmap = this.heightmapGen.getInts(chunkX*16 - 1, chunkZ*16 - 1, 18, 18);
		
		EnumCrustLayer[] crustLayersByDepth = new EnumCrustLayer[255];
		
		int age[] = ageLayer.getInts(chunkX*16 - 1, chunkZ*16 - 1, 18, 18);
		
		// These create deformations in the transitions of rocks, so they are not in a straight line
		int rockoffsetx[] = rockOffsetNoiseX.getInts(chunkX*16, chunkZ*16, 16, 16);
		int rockoffsetz[] = rockOffsetNoiseZ.getInts(chunkX*16, chunkZ*16, 16, 16);
		
		IBlockState[] toplayers;
		
		for (int x = 0; x < 16; ++x) {
			for (int z = 0; z < 16; ++z) {
				
				int arrayIndexChunk = z*16 + x;
				int arrayIndexHeightmap = (z+1)*18 + (x+1);
				
				BiomeVC biome = (BiomeVC) biomeMap[arrayIndexChunk];

				int airblocks = 0;
				toplayers = null;
				
				for (int y = 255; y >= 0; --y) {
					if (y <= 0) {
						primer.setBlockState(x, y, z, BlocksVC.uppermantle.getDefaultState());
						break;
					}
					/*if (y > heightmap[arrayIndexHeightmap] && y <= VCraftWorld.seaLevel) {
						primer.setBlockState(x, y, z, Blocks.water.getDefaultState());
						continue;
					}*/
					
					if (primer.getBlockState(x, y, z).getBlock() == Blocks.stone) {
						if (chunkGroundLevelMap[arrayIndexChunk] == 0) {
							chunkGroundLevelMap[arrayIndexChunk] = y;
							
							EnumRockType rocktype = genrocklayers.getRockType(
								chunkX * 16 + x + rockoffsetx[arrayIndexChunk], 
								2, 
								chunkZ * 16 + z + rockoffsetz[arrayIndexChunk], 
								Math.abs(age[arrayIndexHeightmap]), 
								rand
							);
							toplayers = EnumCrustLayerGroup.getTopLayers(rocktype, new BlockPos(chunkX*16 + x, y, chunkZ*16 + z), rand);
						}
						
						if (chunkHeightMap[arrayIndexChunk] == 0) {
							chunkHeightMap[arrayIndexChunk] = y;
						}
						
						//int steepness = Math.max(Math.abs(heightmap[(z+1)*18 + (x+1)-1] - heightmap[(z+1)*18 + (x+1)+1]), Math.abs(heightmap[(z+1-1)*18 + (x+1)] - heightmap[(z+1+1)*18 + (x+1)]));
						
						int depth = chunkGroundLevelMap[arrayIndexChunk] - y;
						
						

						
						if (y < Math.abs(age[arrayIndexChunk]) / 10) {
							primer.setBlockState(x, y, z, BlocksVC.rock.getEntryFromKey(EnumRockType.KIMBERLITE).getBlockState());
						} else {
							if (toplayers.length > depth) {
								primer.setBlockState(x, y, z, toplayers[depth]);
							} else {
								EnumRockType rocktype = genrocklayers.getRockType(
									chunkX * 16 + x + rockoffsetx[arrayIndexChunk], 
									depth, 
									chunkZ * 16 + z + rockoffsetz[arrayIndexChunk], 
									Math.abs(age[arrayIndexHeightmap]), 
									rand
								);
								
								
								primer.setBlockState(x, y, z, EnumCrustType.ROCK.getBlock(rocktype, null));
							}
							
						}
						
						
						//placeCrustLayerBlock(x, y, z, chunkGroundLevelMap[arrayIndexChunk] - y, primer, biome, chunkX, chunkZ);
					}
					
					if (chunkGroundLevelMap[arrayIndexChunk] != 0 && primer.getBlockState(x, y, z).getBlock() == Blocks.air) {
						airblocks++;
					}
					
					// Try to exclude floating islands in the ground level map
					if (airblocks > 8) {
						chunkGroundLevelMap[arrayIndexChunk] = 0;
						airblocks = 0;
						/*prevCrustlayerDepth = 0;
						prevCrustlayer = null;*/

						
					}
				}
			}
		}
	}
	
	
	
	
	

	public static List getCreatureSpawnsByChunk(World world, BiomeVC biome, int xcoord, int zcoord) {
		ArrayList<SpawnListEntry> list = new ArrayList<SpawnListEntry>();
		
		// Returns climate = int[temp, fertility, rain]
		int[] climate = VCraftWorld.instance.getClimate(new BlockPos(xcoord, 128, zcoord));
		if (climate[2] < 60) return list;
		
		if (climate[0] < -3 && climate[0] > -15 && climate[2] > 70) {
			list.add(new SpawnListEntry(EntityWolf.class, 2, 1, 2));
		}

		if (climate[0] > 25) {
			list.add(new SpawnListEntry(EntityPig.class, 20, 2, 4));
			list.add(new SpawnListEntry(EntityChicken.class, 30, 2, 4));
			return list;
		}


		if (climate[0] > 10) {
			list.add(new SpawnListEntry(EntityCow.class, 25, 2, 4));
			list.add(new SpawnListEntry(EntityHorse.class, 1, 1, 2));
			list.add(new SpawnListEntry(EntitySheep.class, 1, 2, 4));
			list.add(new SpawnListEntry(EntityPig.class, 25, 2, 4));
			list.add(new SpawnListEntry(EntityChicken.class, 10, 2, 4));
			return list;
		}
		
		if (climate[0] > 0) {
			list.add(new SpawnListEntry(EntityCow.class, 15, 2, 4));
			list.add(new SpawnListEntry(EntityHorse.class, 1, 1, 2));
			list.add(new SpawnListEntry(EntitySheep.class, 10, 2, 4));
			list.add(new SpawnListEntry(EntityPig.class, 5, 2, 4));
			
			return list;
		}		

		if (climate[0] > -10) {
			list.add(new SpawnListEntry(EntityCow.class, 25, 2, 4));
			list.add(new SpawnListEntry(EntityHorse.class, 1, 1, 2));
			list.add(new SpawnListEntry(EntitySheep.class, 1, 2, 4));
			return list;
		}
		
		
		return list;
	}

	
	
	public boolean shouldPopulate(ChunkProviderServer chunkprovider, int chunkX, int chunkZ) {
		return
			 chunkprovider.chunkExists(chunkX - 1, chunkZ)
			 && chunkprovider.chunkExists(chunkX, chunkZ - 1)
			 && chunkprovider.chunkExists(chunkX + 1, chunkZ)
			 && chunkprovider.chunkExists(chunkX, chunkZ + 1)
		;
	}
	
	@Override
	public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_) {
		return false;
	}
	
	

	@Override
	public boolean unloadQueuedChunks()
	{
		return true;
	}




	// Get spawnable creatures list
	@Override
	public List getPossibleCreatures(EnumCreatureType p_177458_1_, BlockPos p_177458_2_) {
		BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(p_177458_2_);
		
		return biomegenbase.getSpawnableList(p_177458_1_);
	}
	
	@Override
	public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_) {
				
	}
	
	
	
}
