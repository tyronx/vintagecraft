package at.tyron.vintagecraft.WorldGen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Collections;

import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.BiomeVC;
import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldGen.GenLayers.GenLayerVC;
import at.tyron.vintagecraft.WorldGen.GenLayers.GenRockLayers;
import at.tyron.vintagecraft.WorldProperties.EnumCrustLayer;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.block.BlockRock;
import net.minecraft.block.Block;
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
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

public class ChunkProviderGenerateVC extends ChunkProviderGenerate {
	//GenLayerVC depositLayer;
	
//	GenLayerVC heightmapGen;
	
	GenRockLayers genrocklayers;
	GenLayerVC ageLayer;
	
	GenLayerVC noiseFieldModifier;
	int noiseFieldModifierArray[];
	
	//GenLayerVC[] rockLayers = new GenLayerVC[EnumCrustLayer.values().length - EnumCrustLayer.quantityFixedTopLayers];
	
	
	/** The 7 rocklayers **/
	//int[][] rockData;
	/** The biomes that are used to generate the chunk */
	private BiomeGenBase[] biomeMap;
	
	MapGenCavesVC caveGenerator;
	MapGenFlora floragenerator;
	
	
	
	long seed;
	
	/** RNG. */
	private Random rand;

	/** A NoiseGeneratorOctaves used in generating terrain */
	private NoiseGeneratorOctaves noiseGen1;

	/** A NoiseGeneratorOctaves used in generating terrain */
	private NoiseGeneratorOctaves noiseGen2;

	/** A NoiseGeneratorOctaves used in generating terrain */
	private NoiseGeneratorOctaves noiseGen3;

	/** A NoiseGeneratorOctaves used in generating terrain */
	private NoiseGeneratorOctaves noiseGen4;

	/** A NoiseGeneratorOctaves used in generating terrain */
	public NoiseGeneratorOctaves noiseGen5;

	/** A NoiseGeneratorOctaves used in generating terrain */
	public NoiseGeneratorOctaves noiseGen6;
	public NoiseGeneratorOctaves mobSpawnerNoise;

	/** Reference to the World object. */
	World worldObj;

	/** Holds the overall noise array used in chunk generation */
	double[] noiseArray;
	//double[] stoneNoise = new double[256];

	

	int[] sealevelOffsetMap = new int[256];



	/** A double array that hold terrain noise from noiseGen3 */
	double[] noise3;

	/** A double array that hold terrain noise */
	double[] noise1;

	/** A double array that hold terrain noise from noiseGen2 */
	double[] noise2;

	/** A double array that hold terrain noise from noiseGen5 */
	double[] noise5;

	/** A double array that holds terrain noise from noiseGen6 */
	double[] noise6;

	/**
	 * Used to store the 5x5 parabolic field that is used during terrain generation.
	 */
	float[] parabolicField;

	int[] seaLevelOffsetMap = new int[256];
	int[] chunkGroundLevelMap = new int[256]; // Skips floating islands
	int[] chunkHeightMap = new int[256];
	
	
	ChunkPrimer primer;
	
	
	//ArrayList<BlockPos> unpopulatedChunks = new ArrayList<BlockPos>();
	
	
	public ChunkProviderGenerateVC(World worldIn, long seed, boolean mapfeaturesenabled, String customgenjson) {
		super(worldIn, seed, mapfeaturesenabled, customgenjson);
		
		caveGenerator = new MapGenCavesVC();
		floragenerator = new MapGenFlora(seed);
		
		//VCraftWorld.instance.setUnpopChunkList(unpopulatedChunks);
		
		this.worldObj = worldIn;
		this.rand = new Random(seed);
		this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 4);
		this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
		this.noiseGen4 = new NoiseGeneratorOctaves(this.rand, 4);
		this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 2);
		this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 1);
		this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
		this.seed = seed;
		
		this.noiseFieldModifier = GenLayerVC.genNoiseFieldModifier(seed);
		
		//loadRockLayers();
		
		genrocklayers = new GenRockLayers(seed);
		ageLayer = GenLayerVC.genAgemap(seed);
		
		//heightmapGen = GenLayerVC.genHeightmap(seed);
	}
	
	/*
	
	public void loadRockLayers() {
		// The RockLayer generators do not generate rocks evenly, so shuffle them per world so each rock once gets more rare or more common
		for (int i = 0; i < rockLayers.length; i++) {
			List rocktypes = Arrays.asList(EnumRockType.getRockTypesForCrustLayer(EnumCrustLayer.fromDataLayerIndex(i)));
			Collections.shuffle(rocktypes, this.rand);
			rockLayers[i] = GenLayerVC.genRockLayer(seed+i, (EnumRockType[])rocktypes.toArray());
		}
		
		rockData = new int[rockLayers.length][];
//		depositLayer = GenLayerVC.genDeposits(seed+2);
	}*/
	
	
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
		
			generateTerrainHigh(chunkX, chunkZ, primer);
			generateTerrainLow(chunkX, chunkZ, primer);
		
			decorate(chunkX, chunkZ, rand, primer);
			caveGenerator.func_175792_a(this, this.worldObj, chunkX, chunkZ, primer);
			caveGenerator.func_175792_a(this, this.worldObj, chunkX, chunkZ, primer);
			
		//}
		
		Chunk chunk = new Chunk(this.worldObj, primer, chunkX, chunkZ);
		
		
		//biomeMap = worldObj.getWorldChunkManager().loadBlockGeneratorData(biomeMap, chunkX * 16, chunkZ * 16, 16, 16);
		
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
		//System.out.println("provide flat chunk");
		VCraftWorld.instance.setChunkNBT(chunkX, chunkZ, "climate", wcm.climateGen.getInts(chunkX * 16, chunkZ * 16, 16, 16));
		
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				primer.setBlockState(x, 128, z, VCraftWorld.instance.getTopLayerAtPos(chunkX * 16 + x, 128, chunkZ * 16 + z, EnumRockType.GRANITE, 0));
				primer.setBlockState(x, 127, z, BlocksVC.rock.getFromKey(EnumRockType.GRANITE).getBlockState());
			}
		}
		
		Chunk chunk = new Chunk(this.worldObj, primer, chunkX, chunkZ);
		chunk.generateSkylightMap();
		
		return chunk;
	}



	// Get spawnable creatures list
	@Override
	public List func_177458_a(EnumCreatureType p_177458_1_, BlockPos p_177458_2_) {
		BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(p_177458_2_);
		
		return biomegenbase.getSpawnableList(p_177458_1_);
	}
	
	@Override
	public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_) {
				
	}
	
	
	
	
	@Override
	// chunkprovider is an instance of ChunkProviderServer
	public void populate(IChunkProvider chunkprovider, int chunkX, int chunkZ) {
		
		// Actually working check on whether or not to populate a chunk (prevents runaway chunk generation)
		// Vanilla pop-check is very strangely programmed and fails to prevent runaway chunk generation
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
			
		
		//BlockPos pos = new BlockPos(chunkX, 0, chunkZ);
		//BiomeVC biome = (BiomeVC) this.worldObj.getBiomeGenForCoords(pos);
		//biome.decorate(this.worldObj, this.rand, pos);

		
		BlockPos pos;
		int xCoord = chunkX * 16;
		int zCoord = chunkZ * 16;

		WorldGenAnimals.performWorldGenSpawning(this.worldObj, null, xCoord, zCoord, 16, 16, this.rand);
		
		floragenerator.generate(rand, chunkX, chunkZ, worldObj, chunkprovider, chunkprovider);

		
		BlockPos chunkpos = new BlockPos(xCoord, 0, zCoord);
		int temp;
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				pos = worldObj.getHorizon(chunkpos.add(x, 0, z));
				
				if (worldObj.getBlockState(pos.down()).getBlock().getMaterial() != Material.water) {
					temp = VCraftWorld.instance.getTemperature(pos);
					if (temp < -10 || (temp < 2 && rand.nextInt(temp + 11) == 0)) {
						worldObj.setBlockState(pos, Blocks.snow_layer.getDefaultState());
					}
				}
			}
		}
		
		
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
	
	
	
	
	
	
	
	
	void decorate(int chunkX, int chunkZ, Random rand, ChunkPrimer primer) {
		Arrays.fill(chunkGroundLevelMap, 0);
		Arrays.fill(chunkHeightMap, 0);
		
		
	/*for (int i = 0; i < rockLayers.length; i++) {
			rockData[i] = rockLayers[i].getInts(chunkZ*16, chunkX*16, 16, 16);
		}*/
		
		//int []heightmap = this.heightmapGen.getInts(chunkX*16 - 1, chunkZ*16 - 1, 18, 18);
		
		//biomeMap = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(biomeMap, chunkX * 16, chunkZ * 16, 16, 16);
		
		EnumCrustLayer[] crustLayersByDepth = new EnumCrustLayer[255];
		
		int age[] = ageLayer.getInts(chunkX*16 - 1, chunkZ*16 - 1, 18, 18);
		
		
		for (int x = 0; x < 16; ++x) {
			for (int z = 0; z < 16; ++z) {
				
				//crustLayersByDepth = buildStrata(crustLayersByDepth, x, z);
				
				int arrayIndexChunk = z*16 + x;
				int arrayIndexHeightmap = (z+1)*18 + (x+1);
				
				BiomeVC biome = (BiomeVC) biomeMap[arrayIndexChunk];

				int airblocks = 0;
				
				/*prevCrustlayerDepth = 0;
				prevCrustlayer = null;
				*/
				//for (int y = Math.max(VCraftWorld.seaLevel, heightmap[arrayIndexHeightmap]); y >= 0; --y) {
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
						}
						if (chunkHeightMap[arrayIndexChunk] == 0) {
							chunkHeightMap[arrayIndexChunk] = y;
						}
						
						//int steepness = Math.max(Math.abs(heightmap[(z+1)*18 + (x+1)-1] - heightmap[(z+1)*18 + (x+1)+1]), Math.abs(heightmap[(z+1-1)*18 + (x+1)] - heightmap[(z+1+1)*18 + (x+1)]));
						//buildCrustLayers(x, y, z, /*chunkGroundLevelMap[arrayIndexChunk] - y*/ heightmap[arrayIndexHeightmap] - y, primer, biome, chunkX, chunkZ, steepness);
						
						int depth = chunkGroundLevelMap[arrayIndexChunk] - y;
						//placeCrustLayerBlock(crustLayersByDepth[depth], x, y, z, primer, chunkX, chunkZ, depth);
						
						

						
						if (y < Math.abs(age[arrayIndexChunk]) / 10) {
							primer.setBlockState(x, y, z, BlocksVC.rock.getFromKey(EnumRockType.KIMBERLITE).getBlockState());
						} else {
							EnumRockType rocktype = genrocklayers.getRockType(chunkX * 16 + x, depth, chunkZ * 16 + z, Math.abs(age[arrayIndexChunk]));
							primer.setBlockState(x, y, z, EnumCrustLayer.getBlock(rand, rocktype, chunkX * 16 + x, y, chunkZ*16 + z, depth));
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
		
		//rockData = new int[rockLayers.length][];
	}
	
	
	/*public EnumCrustLayer[] buildStrata(EnumCrustLayer[] strataByDepth, int x, int z) {
		EnumCrustLayer[] crustLayers = EnumCrustLayer.values();
		int currentCrustLayer = -1;
		int thicknesslefttouse = 0;
		
		int arrayIndexChunk = z*16 + x;
		
		for (int i = 0; i < 255; i++) {
			if (currentCrustLayer >= 6 && thicknesslefttouse <= 0) {
				strataByDepth[i] = strataByDepth[i - 1];
				continue;
			}
			
			if (thicknesslefttouse <= 0) {
				currentCrustLayer++;
				
				if (crustLayers[currentCrustLayer].dataLayerIndex == -1) {
					thicknesslefttouse = crustLayers[currentCrustLayer].thickness;
				} else {
					thicknesslefttouse = (rockData[currentCrustLayer][arrayIndexChunk] >> 16) & 0xff;
				}
			}
			
			strataByDepth[i] = crustLayers[currentCrustLayer];
			
			thicknesslefttouse--;
		}
		
		return strataByDepth;
	}
	
	EnumCrustLayer prevCrustlayer = null;
	int prevCrustlayerDepth = 0;
	
	public void placeCrustLayerBlock(EnumCrustLayer crustlayer, int x, int y, int z, ChunkPrimer primer, int chunkX, int chunkZ, int depth) {
		if (crustlayer == null) return;

		int arrayIndexChunk = z*16 + x;
		IBlockState blockstate = crustlayer.getFixedBlock(EnumRockType.byColor(rockData[0][arrayIndexChunk] & 0xff), chunkX * 16 + x, y, chunkZ * 16 + z, depth, 0);
		
		if (blockstate == null) {
			int depthsincelastcrustlayer = depth - prevCrustlayerDepth;
			
			if (prevCrustlayer == null || crustlayer != prevCrustlayer) {
				prevCrustlayer = crustlayer;
				prevCrustlayerDepth = depth; 
			}
			
			if (crustlayer.dataLayerIndex == -1) crustlayer = EnumCrustLayer.ROCK_1;
			
			int rockcolor = rockData[crustlayer.dataLayerIndex][arrayIndexChunk] & 0xffff;
			int remainder = rockcolor - (rockcolor / EnumRockType.colorFactor()) * EnumRockType.colorFactor();
			
			
			rockcolor = (rockcolor / EnumRockType.colorFactor()) * EnumRockType.colorFactor();
			
			if (depthsincelastcrustlayer >= remainder) {
				// Normal rock
				// use rockcolor
				
			} else {
				// Previous rock
				// use prev rockcolor
				rockcolor += EnumRockType.colorFactor();
			}

			if (EnumRockType.byColor(rockcolor) == null) {
				System.out.println("no rock for color " + rockcolor);
				System.out.println(depthsincelastcrustlayer + " / " + remainder + " / " + (rockData[crustlayer.dataLayerIndex][arrayIndexChunk] & 0xffff));
			}
			blockstate = BlocksVC.rock.getFromKey(EnumRockType.byColor(rockcolor)).getBlockState();
		}
		
		primer.setBlockState(x, y, z, blockstate);

		if(rand.nextBoolean() && y > 0 && depth > 0) {
			primer.setBlockState(x, y - 1, z, blockstate);
			
			if(rand.nextBoolean() && y > 1 && depth > 5) {
				primer.setBlockState(x, y - 2, z, blockstate);
			}
			
			if(rand.nextBoolean() && y > 2 && depth > 15) {
				primer.setBlockState(x, y - 3, z, blockstate);
			}
		}
	}*/
	
	
	/*public void placeCrustLayerBlock(int x, int y, int z, int depth, ChunkPrimer primer, BiomeVC biome, int chunkX, int chunkZ) {
	//public void buildCrustLayers(int x, int y, int z, int depth, ChunkPrimer primer, BiomeVC biome, int chunkX, int chunkZ, int steepness) {
		int arrayIndexChunk = z*16 + x;
		
		EnumCrustLayer layer = EnumCrustLayer.crustLayerForDepth(depth, rockData, arrayIndexChunk, primer.getBlockState(x, chunkGroundLevelMap[arrayIndexChunk]+1, z).getBlock() == Blocks.water);
		if (layer == null) return;
		
		IBlockState blockstate = layer.getFixedBlock(EnumRockType.byColor(rockData[0][arrayIndexChunk] & 0xff), chunkX * 16 + x, y, chunkZ * 16 + z, depth, 0);

		
		if (blockstate == null) {
			if (layer.dataLayerIndex == -1) layer = EnumCrustLayer.ROCK_1;
			blockstate = BlocksVC.rock.getFromKey(EnumRockType.byColor(rockData[layer.dataLayerIndex][arrayIndexChunk] & 0xff)).getBlockState();
		} else {
			//System.out.println(chunkX + " / " + x + "   " + chunkZ + " / " + z);
		}
		
		primer.setBlockState(x, y, z, blockstate);

		if(rand.nextBoolean() && y > 0 && depth > 0) {
			primer.setBlockState(x, y - 1, z, blockstate);
			
			if(rand.nextBoolean() && y > 1 && depth > 5) {
				primer.setBlockState(x, y - 2, z, blockstate);
			}
			
			if(rand.nextBoolean() && y > 2 && depth > 15) {
				primer.setBlockState(x, y - 3, z, blockstate);
			}
		}
	}
	
	
	
	
	
	*/
	
	
	
	
	
	
	
	
	
	
	public void generateTerrainLow(int chunkX, int chunkZ, ChunkPrimer primer) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = VCraftWorld.instance.terrainGenHiLevel; y > 0; y--) {
					if (primer.getBlockState(x, y, z).getBlock() == Blocks.air) {
						primer.setBlockState(x, y, z, Blocks.stone.getDefaultState());
					}
				}
			}
		}
	}

	BiomeGenBase []largerBiomeMap = null;
	public void generateTerrainHigh(int chunkX, int chunkZ, ChunkPrimer primer) {
		byte horizontalPart = 4;
		byte verticalPart = 20;
		//int seaLevel = VCraftWorld.instance.seaLevel; // - VCraftWorld.instance.terrainGenLevel;
		
		int xSize = horizontalPart + 1;
		byte ySize = 21;
		int zSize = horizontalPart + 1;
		
		//short arrayYHeight = 128;
		//VCraftWorld.instance.terrainGenHiLevel = 65;
		
		largerBiomeMap = this.worldObj.getWorldChunkManager().getBiomesForGeneration(largerBiomeMap, chunkX * 4 - 2, chunkZ * 4 - 2, xSize + 5, zSize + 5);
		
		this.noiseArray = this.initializeNoiseFieldHigh(this.noiseArray, chunkX * horizontalPart, 0, chunkZ * horizontalPart, xSize, ySize, zSize);
		
		double yLerp = 0.125D;
		double xLerp = 0.25D;
		double zLerp = 0.25D;
		
		//int ycoord;
		
		for (int x = 0; x < horizontalPart; ++x) {
			for (int z = 0; z < horizontalPart; ++z) {
				for (int y = 0; y < verticalPart; ++y) {
					
					double lower_lefttop = this.noiseArray[((x + 0) * zSize + z + 0) * ySize + y + 0];
					double lower_leftbottom = this.noiseArray[((x + 0) * zSize + z + 1) * ySize + y + 0];
					double lower_righttop = this.noiseArray[((x + 1) * zSize + z + 0) * ySize + y + 0];
					double lower_rightbottom = this.noiseArray[((x + 1) * zSize + z + 1) * ySize + y + 0];
					
					double dy_lefttop = (this.noiseArray[((x + 0) * zSize + z + 0) * ySize + y + 1] - lower_lefttop) * yLerp;
					double dy_leftbottom = (this.noiseArray[((x + 0) * zSize + z + 1) * ySize + y + 1] - lower_leftbottom) * yLerp;
					double dy_righttop = (this.noiseArray[((x + 1) * zSize + z + 0) * ySize + y + 1] - lower_righttop) * yLerp;
					double dy_rightbottom = (this.noiseArray[((x + 1) * zSize + z + 1) * ySize + y + 1] - lower_rightbottom) * yLerp;

					for (int dy = 0; dy < 8; ++dy) {
						
						
						double bottom1Counting = lower_lefttop;
						double bottom2Counting = lower_leftbottom;
						
						double noisetopdx = (lower_righttop - lower_lefttop) * xLerp;
						double noisedowndx = (lower_rightbottom - lower_leftbottom) * xLerp;

						for (int dx = 0; dx < 4; ++dx) {	
							
							double var49 = (bottom2Counting - bottom1Counting) * zLerp;
							double var47 = bottom1Counting - var49;

							for (int dz = 0; dz < 4; ++dz) {
								if ((var47 += var49) > 0.0D) {
									primer.setBlockState(4*x + dx, 8*y + dy + VCraftWorld.instance.terrainGenHiLevel, 4*z + dz, Blocks.stone.getDefaultState());
								} else if (y * 8 + dy + VCraftWorld.instance.terrainGenHiLevel < VCraftWorld.instance.seaLevel) {
									primer.setBlockState(4*x + dx, 8*y + dy + VCraftWorld.instance.terrainGenHiLevel, 4*z + dz, Blocks.water.getDefaultState());
								} else {
									primer.setBlockState(4*x + dx, 8*y + dy + VCraftWorld.instance.terrainGenHiLevel, 4*z + dz, Blocks.air.getDefaultState());
								}
								
							}
							
							bottom1Counting += noisetopdx;
							bottom2Counting += noisedowndx;
						}
						
						lower_lefttop += dy_lefttop;
						lower_leftbottom += dy_leftbottom;
						lower_righttop += dy_righttop;
						lower_rightbottom += dy_rightbottom;
					}
				}
			}
		}
	}













	
	
	
	/***
	 * generates a subset of the level's terrain data. Takes 7 arguments: the [empty] noise array, the position, and the
	 * size.
	 */
	private double[] initializeNoiseFieldHigh(double[] outArray, int xPos, int yPos, int zPos, int xSize, int ySize, int zSize) {
		int smoothingRadius = 2;
		
		noiseFieldModifierArray = noiseFieldModifier.getInts(xPos, zPos, xSize, zSize);
		
		if (outArray == null) {
			outArray = new double[xSize * ySize * zSize];
		}

		VCraftWorld.instance.terrainGenHiLevel = 67;
		
		if (this.parabolicField == null) {
			this.parabolicField = new float[2*smoothingRadius + 5 * 2 * smoothingRadius + 1];
			for (int xR = -smoothingRadius; xR <= smoothingRadius; ++xR) {
				for (int zR = -smoothingRadius; zR <= smoothingRadius; ++zR) {
					float var10 = 10.0F / MathHelper.sqrt_float(xR * xR + zR * zR + 0.2F);
					this.parabolicField[xR + smoothingRadius + (zR + smoothingRadius) * 5] = var10;
				}
			}
		}		
		
		double horizontalScale = 1000D;
		double verticalScale = 1000D;
		
		this.noise5 = this.noiseGen5.generateNoiseOctaves(this.noise5, xPos, zPos, xSize, zSize, 1.121D, 1.121D, 0.5D);
		this.noise6 = this.noiseGen6.generateNoiseOctaves(this.noise6, xPos, zPos, xSize, zSize, 800.0D, 800.0D, 0.5D);
		
		// Seems to be the lowest octave
		this.noise1 = this.noiseGen1.generateNoiseOctaves(this.noise1, xPos, yPos, zPos, xSize, ySize, zSize, horizontalScale, verticalScale, horizontalScale);
		
		this.noise2 = this.noiseGen2.generateNoiseOctaves(this.noise2, xPos, yPos, zPos, xSize, ySize, zSize, horizontalScale, verticalScale, horizontalScale);

		// Seems to be a high or highest octave
		this.noise3 = this.noiseGen3.generateNoiseOctaves(this.noise3, xPos, yPos, zPos, xSize, ySize, zSize, horizontalScale / 60.0D, verticalScale / 120.0D, horizontalScale / 60.0D);

		
		boolean var43 = false;
		boolean var42 = false;
		int posIndex = 0;
		int counter = 0;

		for (int x = 0; x < xSize; ++x) {
			for (int z = 0; z < zSize; ++z) {
				float maxBlendedHeight = 0.0F;
				float minBlendedHeight = 0.0F;
				float blendedHeightSum = 0.0F;
				
				BiomeVC baseBiome = (BiomeVC)largerBiomeMap[x + smoothingRadius + (z + smoothingRadius) * (xSize + 5)];

				for (int xR = -smoothingRadius; xR <= smoothingRadius; ++xR) {
					for (int zR = -smoothingRadius; zR <= smoothingRadius; ++zR) {
						BiomeVC blendBiome = (BiomeVC)largerBiomeMap[x + xR + smoothingRadius + (z + zR + smoothingRadius) * (xSize + 5)];
						float blendedHeight = this.parabolicField[xR + smoothingRadius + (zR + smoothingRadius) * 5] / 2.0F;
						//System.out.println(blendedHeight + " / " + blendBiome.minHeight + " > " + baseBiome.minHeight + " max:" + blendBiome.maxHeight);
						if (blendBiome.minHeight > baseBiome.minHeight) {
							blendedHeight *= 0.5F;
						}

						maxBlendedHeight += blendBiome.maxHeight * blendedHeight;
						minBlendedHeight += blendBiome.minHeight * blendedHeight;
						blendedHeightSum += blendedHeight;
					}
				}

				maxBlendedHeight /= blendedHeightSum;
				minBlendedHeight /= blendedHeightSum;
				maxBlendedHeight = maxBlendedHeight * 0.9F + 0.1F;
				minBlendedHeight = (minBlendedHeight * 4.0F - 1.0F) / 8.0F;
				
				
				
				/*minBlendedHeight = -5f;
				maxBlendedHeight = -4.9f;*/
				
				// Tall Erroded Islands
				//minBlendedHeight = -0.8f;
				//maxBlendedHeight = 1f;xd
				
				// Eroded Islands
				//minBlendedHeight = -0.8f;
				//maxBlendedHeight = 0.2f;
				
				
				// Island
				// Used these settings to generate extreme overhangs etc. 
				/*minBlendedHeight = -0.2f;
				maxBlendedHeight = 0.5f;*/
				
				maxBlendedHeight /= 10;
				
				
				double noise6var = this.noise6[counter] / 8000.0D;

				if (noise6var < 0.0D)
					noise6var = -noise6var * 0.3D;
				noise6var = noise6var * 3.0D - 2.0D;

				if (noise6var < 0.0D) {
					noise6var /= 2.0D;
					if (noise6var < -1.0D)
						noise6var = -1.0D;
					noise6var /= 1.4D;
					noise6var /= 2.0D;
				}
				else
				{
					if (noise6var > 1.0D)
						noise6var = 1.0D;
					noise6var /= 8.0D;
				}

				++counter;
				for (int y = 0; y < ySize; ++y) {
					double minblendhgvar = minBlendedHeight;
					double maxblendhgvar = maxBlendedHeight;
					minblendhgvar += noise6var * 0.2D;
					minblendhgvar = minblendhgvar * ySize / 16.0D;
					
					double adjustedminhg = ySize / 2.0D + minblendhgvar * 4.0D;
					double result = 0.0D;
					double theheight = (y - adjustedminhg) * 12.0D / (2.70 + maxblendhgvar);   // * 256.0D / 256.0D

					if (theheight < 0.0D) {
						theheight *= 4.0D;
					}
					
					/*double noise1var = this.noise1[posIndex] / 512.0D;
					double noise2var = this.noise2[posIndex] / 512.0D;
					double noise3var = (this.noise3[posIndex] / 10.0D + 1.0D) / 2.0D;
					
					if (noise3var < 0.0D) {
						result = noise1var;
					} else if (noise3var > 1.0D) {
						result = noise2var;
					} else {
						result = noise1var + (noise2var - noise1var) * noise3var;
					}*/
					
					result = noise1[posIndex] / 128D 
							+ (noise2[posIndex] / 512D + (this.noise3[posIndex] / 10.0D + 1.0D) / 8.0D) * Math.max(0.2f, noiseFieldModifierArray[x + z * xSize] / 210f);

					//result = noise1var;
							
					result -= theheight;
					
					if (y > ySize - 4) {
						double var40 = (y - (ySize - 4)) / 3.0F;
						result = result * (1.0D - var40) + -10.0D * var40;
					}

					outArray[posIndex] = result;
					++posIndex;
					
					
				}
			}
		}
		return outArray;
	}
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	

	@Override
	public boolean unloadQueuedChunks()
	{
		return true;
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
	
}
