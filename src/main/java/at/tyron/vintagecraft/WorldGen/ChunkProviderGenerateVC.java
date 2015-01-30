package at.tyron.vintagecraft.WorldGen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Collections;

import at.tyron.vintagecraft.VCraftWorld;
import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.BiomeVC;
import at.tyron.vintagecraft.WorldGen.GenLayers.GenLayerVC;
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
import net.minecraft.entity.passive.EntityPig;
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
	GenLayerVC depositLayer;
	GenLayerVC climate;
	
	GenLayerVC[] rockLayers = new GenLayerVC[EnumCrustLayer.values().length - EnumCrustLayer.quantityFixedTopLayers];

	/** The 7 rocklayers **/
	int[][] rockData;
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
	int[] chunkGroundLevelMap = new int[256];
	
	
	ChunkPrimer primer;
	
	
	ArrayList<BlockPos> unpopulatedChunks = new ArrayList<BlockPos>();
	
	
	public ChunkProviderGenerateVC(World worldIn, long seed, boolean mapfeaturesenabled, String customgenjson) {
		super(worldIn, seed, mapfeaturesenabled, customgenjson);
		
		caveGenerator = new MapGenCavesVC();
		floragenerator = new MapGenFlora(seed);
		
		VCraftWorld.setUnpopChunkList(unpopulatedChunks);
		
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
		
		loadRockLayers();
		climate = GenLayerVC.genClimate(seed);
	}
	
	
	
	public void loadRockLayers() {
		// The RockLayer generators do not generate rocks evenly, so shuffle them per world so each rock once gets more rare or more common
		for (int i = 0; i < rockLayers.length; i++) {
			List rocktypes = Arrays.asList(EnumRockType.getRockTypesForCrustLayer(EnumCrustLayer.fromDataLayerIndex(i)));
			Collections.shuffle(rocktypes, this.rand);
			rockLayers[i] = GenLayerVC.genRockLayer(seed+i, (EnumRockType[])rocktypes.toArray());
		}
		
		rockData = new int[rockLayers.length][];
		depositLayer = GenLayerVC.genDeposits(seed+2);
	}
	
	
	@Override
	public Chunk provideChunk(int chunkX, int chunkZ) {
		if (worldObj.getWorldChunkManager() instanceof WorldChunkManagerFlatVC) {
			return provideFlatChunk(chunkX, chunkZ);
		}
		
		primer = new ChunkPrimer();
		
		VCraftWorld.setChunkNBT(chunkX, chunkZ, "climate", climate.getInts(chunkX * 16, chunkZ * 16, 16, 16));
		
		//this.rand.setSeed(chunkX * 341873128712L + chunkZ * 132897987541L);
		
		
		biomeMap = worldObj.getWorldChunkManager().loadBlockGeneratorData(biomeMap, chunkX * 16, chunkZ * 16, 16, 16);
		
		//if (chunkX % 4 != 0) { // && (chunkX+1) % 4 != 0) {
		
			generateTerrainHigh(chunkX, chunkZ, primer);
			generateTerrainLow(chunkX, chunkZ, primer);
		
			decorate(chunkX, chunkZ, rand, primer);
			caveGenerator.func_175792_a(this, this.worldObj, chunkX, chunkZ, primer);
			caveGenerator.func_175792_a(this, this.worldObj, chunkX, chunkZ, primer);
			
	//	}
		
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
	
	
	private Chunk provideFlatChunk(int chunkX, int chunkZ) {
		primer = new ChunkPrimer();
		//System.out.println("provide flat chunk");
		VCraftWorld.setChunkNBT(chunkX, chunkZ, "climate", climate.getInts(chunkX * 16, chunkZ * 16, 16, 16));
		
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				primer.setBlockState(x, 64, z, VCraftWorld.getTopLayerAtPos(chunkX * 16 + x, 128, chunkZ * 16 + z, EnumRockType.GRANITE));
				primer.setBlockState(x, 63, z, EnumRockType.GRANITE.getRockVariantForBlock(BlocksVC.rock));
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
		// Vanilla pop-check extremely strange and not working 
		if (chunkprovider instanceof ChunkProviderServer) {
			int x, z;
			
			for (Iterator<BlockPos> it = unpopulatedChunks.iterator(); it.hasNext(); ) {
				BlockPos chunkpos = it.next();
				x = chunkpos.getX();
				z = chunkpos.getZ();
				
				if (shouldPopulate((ChunkProviderServer)chunkprovider, x, z)) {
					it.remove();
					populate(chunkprovider, x, z);
					VCraftWorld.setChunkNBT(x, z, "vcraftpopulated", true);
					break;
				}
			}
			
			if(!shouldPopulate((ChunkProviderServer)chunkprovider, chunkX, chunkZ)) {
				unpopulatedChunks.add(new BlockPos(chunkX, 0, chunkZ));
				VCraftWorld.setChunkNBT(chunkX, chunkZ, "vcraftpopulated", false);	
				return;
			}
			
		}
			
		
		BlockPos pos = new BlockPos(chunkX, 0, chunkZ);
		//BiomeVC biome = (BiomeVC) this.worldObj.getBiomeGenForCoords(pos);
		//biome.decorate(this.worldObj, this.rand, pos);

		
		
		int xCoord = chunkX * 16;
		int zCoord = chunkZ * 16;

		WorldGenAnimals.performWorldGenSpawning(this.worldObj, null, xCoord + 8, zCoord + 8, 16, 16, this.rand);
		
		floragenerator.generate(rand, chunkX, chunkZ, worldObj, chunkprovider, chunkprovider);

		
		BlockPos chunkpos = new BlockPos(xCoord, 0, zCoord);
		int temp;
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				pos = worldObj.getHorizon(chunkpos.add(x, 0, z));
				
				if (worldObj.getBlockState(pos.down()).getBlock().getMaterial() != Material.water) {
					temp = VCraftWorld.getTemperature(pos);
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
		
		for (int i = 0; i < rockLayers.length; i++) {
			rockData[i] = rockLayers[i].getInts(chunkZ*16, chunkX*16, 16, 16);
		}
		
		//biomeMap = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(biomeMap, chunkX * 16, chunkZ * 16, 16, 16);
		
		//rockDeformationData = rockDeformationLayer.getInts(chunkZ*16, chunkX*16, 16, 16);
		
		for (int x = 0; x < 16; ++x) {
			for (int z = 0; z < 16; ++z) {
				int arrayIndexChunk = z + x * 16;
				
				BiomeVC biome = (BiomeVC) biomeMap[arrayIndexChunk];

				int airblocks = 0;
				
				for (int y = 255; y >= 0; --y) {
					if (y <= 0) {
						primer.setBlockState(x, y, z, BlocksVC.uppermantle.getDefaultState());
						break;
					}
					if (primer.getBlockState(x, y, z).getBlock() == Blocks.stone) {
						if (chunkGroundLevelMap[arrayIndexChunk] == 0) {
							chunkGroundLevelMap[arrayIndexChunk] = y;
						}
						
						buildCrustLayers(x, y, z, chunkGroundLevelMap[arrayIndexChunk] - y, primer, biome, chunkX, chunkZ);						
					}
					
					if (chunkGroundLevelMap[arrayIndexChunk] != 0 && primer.getBlockState(x, y, z).getBlock() == Blocks.air) {
						airblocks++;
					}
					
					// Try to exclude floating islands in the ground level map
					if (airblocks > 8) {
						chunkGroundLevelMap[arrayIndexChunk] = 0;
						airblocks = 0;
					}
				}
			}
		}
		
		rockData = new int[rockLayers.length][];
	}
	
	
	
	public void buildCrustLayers(int x, int y, int z, int depth, ChunkPrimer primer, BiomeVC biome, int chunkX, int chunkZ) {
		int arrayIndexChunk = z + x * 16;
		
		EnumCrustLayer layer = EnumCrustLayer.crustLayerForDepth(depth, rockData, arrayIndexChunk, primer.getBlockState(x, chunkGroundLevelMap[arrayIndexChunk]+1, z).getBlock() == Blocks.water);
		
		
		if (layer == null) return;
		
		IBlockState blockstate = layer.getFixedBlock(EnumRockType.byColor(rockData[0][arrayIndexChunk] & 0xff), chunkX * 16 + x, y, chunkZ * 16 + z, depth);
		
		if (blockstate == null) {
			blockstate = EnumRockType.byColor(rockData[layer.dataLayerIndex][arrayIndexChunk] & 0xff).getRockVariantForBlock(BlocksVC.rock);
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void generateTerrainLow(int chunkX, int chunkZ, ChunkPrimer primer) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = VintageCraftConfig.terrainGenLevel; y > 0; y--) {
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
		int seaLevel = VintageCraftConfig.seaLevel() - VintageCraftConfig.terrainGenLevel;
		
		int xSize = horizontalPart + 1;
		byte ySize = 21;
		int zSize = horizontalPart + 1;
		
		//short arrayYHeight = 128;
		
		
		largerBiomeMap = this.worldObj.getWorldChunkManager().getBiomesForGeneration(largerBiomeMap, chunkX * 4 - 2, chunkZ * 4 - 2, xSize + 5, zSize + 5);
		
		this.noiseArray = this.initializeNoiseFieldHigh(this.noiseArray, chunkX * horizontalPart, 0, chunkZ * horizontalPart, xSize, ySize, zSize);
		
		double yLerp = 0.125D;
		double xLerp = 0.25D;
		double zLerp = 0.25D;
		
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
									primer.setBlockState(4*x + dx, 8*y + dy + VintageCraftConfig.terrainGenLevel, 4*z + dz, Blocks.stone.getDefaultState());
								} else if (y * 8 + dy < seaLevel) {
									primer.setBlockState(4*x + dx, 8*y + dy + VintageCraftConfig.terrainGenLevel, 4*z + dz, Blocks.water.getDefaultState());
								} else {
									primer.setBlockState(4*x + dx, 8*y + dy + VintageCraftConfig.terrainGenLevel, 4*z + dz, Blocks.air.getDefaultState());
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
		
		if (outArray == null) {
			outArray = new double[xSize * ySize * zSize];
		}

		if (this.parabolicField == null) {
			this.parabolicField = new float[2*smoothingRadius + 5 * 2 * smoothingRadius + 1];
			for (int xR = -smoothingRadius; xR <= smoothingRadius; ++xR) {
				for (int zR = -smoothingRadius; zR <= smoothingRadius; ++zR) {
					float var10 = 10.0F / MathHelper.sqrt_float(xR * xR + zR * zR + 0.2F);
					this.parabolicField[xR + smoothingRadius + (zR + smoothingRadius) * 5] = var10;
				}
			}
		}

		// Determines the height of the terrain i guess
		/*
		 * Cool settings! 
		 
		double horizontalScale = 684.412D; //684.412D;
		double verticalScale = 50D; // 684.412D;
		*/
		
		/* Very sharp cliffs to moutain  
		double horizontalScale = 684.412D;
		double verticalScale = 1D;
		*/
		
		/* Rather flat with extreme overhangs - very nice!
		double horizontalScale = 684.412D;
		double verticalScale = 2000D;
		*/
		
		
		// No overhangs, large mountains, shallow lakes 
		/*double horizontalScale = 300D;
		double verticalScale = 300D;      // probably horizontal scale*/
		
		
		
		double horizontalScale = 384.412D;
		double verticalScale = 684.412D;
		
		this.noise5 = this.noiseGen5.generateNoiseOctaves(this.noise5, xPos, zPos, xSize, zSize, 1.121D, 1.121D, 0.5D);
		this.noise6 = this.noiseGen6.generateNoiseOctaves(this.noise6, xPos, zPos, xSize, zSize, 200.0D, 200.0D, 0.5D);
		
		this.noise3 = this.noiseGen3.generateNoiseOctaves(this.noise3, xPos, yPos, zPos, xSize, ySize, zSize, horizontalScale / 80.0D, verticalScale / 160.0D, horizontalScale / 80.0D);
		this.noise1 = this.noiseGen1.generateNoiseOctaves(this.noise1, xPos, yPos, zPos, xSize, ySize, zSize, horizontalScale, verticalScale, horizontalScale);
		this.noise2 = this.noiseGen2.generateNoiseOctaves(this.noise2, xPos, yPos, zPos, xSize, ySize, zSize, horizontalScale, verticalScale, horizontalScale);
		
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
					
					double noise1var = this.noise1[posIndex] / 512.0D;
					double noise2var = this.noise2[posIndex] / 512.0D;
					double noise3var = (this.noise3[posIndex] / 10.0D + 1.0D) / 2.0D;
					
					if (noise3var < 0.0D) {
						result = noise1var;
					} else if (noise3var > 1.0D) {
						result = noise2var;
					} else {
						result = noise1var + (noise2var - noise1var) * noise3var;
					}

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
	
	
	
	
	
	
	
	

	
	//private double[] layer2Noise = new double[256];
	
	

/*	private void replaceBlocksForBiomeHigh(int chunkX, int chunkZ, Random rand, ChunkPrimer primer) {
		int seaLevel = 16;
		int worldHeight = 256;
		int indexOffset = 128;
		double var6 = 0.03125D;
		stoneNoise = noiseGen4.generateNoiseOctaves(stoneNoise, chunkX * 16, chunkZ * 16, 0, 16, 16, 1, var6 * 4.0D, var6 * 1.0D, var6 * 4.0D);
		boolean[] cliffMap = new boolean[256];
		
		for (int xCoord = 0; xCoord < 16; ++xCoord) {
			for (int zCoord = 0; zCoord < 16; ++zCoord) {
				int arrayIndex = xCoord + zCoord * 16;
				int arrayIndexDL = zCoord + xCoord * 16;
				int arrayIndex2 = xCoord+1 + zCoord+1 * 16;
				VCBiome biome = (VCBiome)getBiome(xCoord,zCoord);
				
				DataLayer rock1 = rockLayer1[arrayIndexDL] == null ? DataLayer.Granite : rockLayer1[arrayIndexDL];
				DataLayer rock2 = rockLayer2[arrayIndexDL] == null ? DataLayer.Granite : rockLayer2[arrayIndexDL];
				DataLayer rock3 = rockLayer3[arrayIndexDL] == null ? DataLayer.Granite : rockLayer3[arrayIndexDL];
				DataLayer evt = evtLayer[arrayIndexDL] == null ? DataLayer.EVT_0_125 : evtLayer[arrayIndexDL];
				
				float rain = rainfallLayer[arrayIndexDL] == null ? DataLayer.Rain_125.floatdata1 : rainfallLayer[arrayIndexDL].floatdata1;
				DataLayer drainage = drainageLayer[arrayIndexDL] == null ? DataLayer.DrainageNormal : drainageLayer[arrayIndexDL];
				int var12 = (int)(stoneNoise[arrayIndex2] / 3.0D + 6.0D);
				int var13 = -1;

				IBlockState surfaceBlock = Blocks.grass.getDefaultState(); // VC_Core.getTypeForGrassWithRain(rock1.data1, rain);
				IBlockState subSurfaceBlock = Blocks.dirt.getDefaultState(); // VC_Core.getTypeForDirtFromGrass(surfaceBlock);

				float _temp = 0.5f; //VC_Climate.getBioTemperature(worldObj, chunkX * 16 + xCoord, chunkZ * 16 + zCoord);
				int h = 0;
				

				if(VCBiome.isShore(getBiome(xCoord-1, zCoord).biomeID) || VCBiome.isShore(getBiome(xCoord+1, zCoord).biomeID) || VCBiome.isShore(getBiome(xCoord, zCoord+1).biomeID) || VCBiome.isShore(getBiome(xCoord, zCoord-1).biomeID)) {
					if(!VCBiome.isShore(getBiome(xCoord, zCoord).biomeID))
						cliffMap[arrayIndex] = true;
				}
				
				
				for (int height = 127; height >= 0; --height) {
					int indexBig = ((arrayIndex) * worldHeight + height + indexOffset);
					int index = ((arrayIndex) * 128 + height);

					float temp = 0.5f; //VC_Climate.adjustHeightToTemp(height, _temp);
					
					if(VCBiome.isShore(biome.biomeID) && height > seaLevel+h && primer.getBlockState(index).getBlock() == Blocks.stone) {
						// idsTop[index] = Blocks.air;
						primer.setBlockState(index, Blocks.stone.getDefaultState());
						
						if (h == 0) {
							h = (height-16)/4;
						}
					}
					
					/*if(idsBig[indexBig] == null) {
						idsBig[indexBig] = idsTop[index];
						if (indexBig+1 < idsBig.length && VC_Core.isSoilOrGravel(idsBig[indexBig+1]) && idsBig[indexBig] == Blocks.air) {
							for (int upCount = 1; VC_Core.isSoilOrGravel(idsBig[indexBig+upCount]); upCount++) {
								idsBig[indexBig+upCount] = Blocks.air;
							}
						}
					}

					if (idsBig[indexBig] == Blocks.stone) {
						if(seaLevelOffsetMap[arrayIndex] == 0 && height-16 >= 0) {
							seaLevelOffsetMap[arrayIndex] = height-16;
						}

						if(chunkHeightMap[arrayIndex] == 0) {
							chunkHeightMap[arrayIndex] = height+indexOffset;
						}

						convertStone(indexOffset+height, arrayIndex, indexBig, idsBig, metaBig, rock1, rock2, rock3);

						//First we check to see if its a cold desert
						if(rain < 125 && temp < 1.5f) {
							surfaceBlock = VC_Core.getTypeForSand(rock1.data1);
							subSurfaceBlock = VC_Core.getTypeForSand(rock1.data1);
						} else {
						
						//Next we check for all other warm deserts
							if (rain < 125 && biome.heightVariation < 0.5f && temp > 20f) {
								surfaceBlock = VC_Core.getTypeForSand(rock1.data1);
								subSurfaceBlock = VC_Core.getTypeForSand(rock1.data1);
							}
						}

						if (biome == VCBiome.beach || biome == VCBiome.ocean || biome == VCBiome.DeepOcean) {
							subSurfaceBlock = surfaceBlock = VC_Core.getTypeForSand(rock1.data1);
						} else if(biome == VCBiome.gravelbeach) {
							subSurfaceBlock = surfaceBlock = VC_Core.getTypeForGravel(rock1.data1);
						}

						if (var13 == -1) {
							//The following makes dirt behave nicer and more smoothly, instead of forming sharp cliffs.
							int arrayIndexx = xCoord > 0? (xCoord - 1) + (zCoord * 16):-1;
							int arrayIndexX = xCoord < 15? (xCoord + 1) + (zCoord * 16):-1;
							int arrayIndexz = zCoord > 0? xCoord + ((zCoord-1) * 16):-1;
							int arrayIndexZ = zCoord < 15? xCoord + ((zCoord+1) * 16):-1;
							int var12Temp = var12;
							for (int counter = 1; counter < var12Temp / 3; counter++) {
								
								if(arrayIndexx >= 0 && seaLevelOffsetMap[arrayIndex]-(3*counter) > seaLevelOffsetMap[arrayIndexx]) {
									seaLevelOffsetMap[arrayIndex]--;
									var12--;
									height--;
									indexBig = ((arrayIndex) * worldHeight + height + indexOffset);
									index = ((arrayIndex) * 128 + height);
								}
								else if(arrayIndexX >= 0 && seaLevelOffsetMap[arrayIndex]-(3*counter) > seaLevelOffsetMap[arrayIndexX]) {
									seaLevelOffsetMap[arrayIndex]--;
									var12--;
									height--;
									indexBig = ((arrayIndex) * worldHeight + height + indexOffset);
									index = ((arrayIndex) * 128 + height);
								}
								else if(arrayIndexz >= 0 && seaLevelOffsetMap[arrayIndex]-(3*counter) > seaLevelOffsetMap[arrayIndexz]) {
									seaLevelOffsetMap[arrayIndex]--;
									var12--;
									height--;
									indexBig = ((arrayIndex) * worldHeight + height + indexOffset);
									index = ((arrayIndex) * 128 + height);
								}
								else if(arrayIndexZ >= 0 && seaLevelOffsetMap[arrayIndex]-(3*counter) > seaLevelOffsetMap[arrayIndexZ]) {
									seaLevelOffsetMap[arrayIndex]--;
									var12--;
									height--;
									indexBig = ((arrayIndex) * worldHeight + height + indexOffset);
									index = ((arrayIndex) * 128 + height);
								}
							}
							var13 = (int)(var12 * (1d-Math.max(Math.min(((height - 16) / 80d), 1), 0)));

							//Set soil below water
							for (int c = 1; c < 3; c++) {
								if (indexBig + c < idsBig.length && (
										(idsBig[indexBig + c] != surfaceBlock) &&
										(idsBig[indexBig + c] != subSurfaceBlock) &&
										(idsBig[indexBig + c] != VCBlocks.SaltWaterStationary) &&
										(idsBig[indexBig + c] != VCBlocks.FreshWaterStationary) &&
										(idsBig[indexBig + c] != VCBlocks.HotWater))) {
									idsBig[indexBig + c] = Blocks.air;
									//metaBig[indexBig + c] = 0;
									if (indexBig + c + 1 < idsBig.length && idsBig[indexBig + c + 1] == VCBlocks.SaltWaterStationary) {
										idsBig[indexBig + c] = subSurfaceBlock;
										metaBig[indexBig + c] = (byte)VC_Core.getSoilMeta(rock1.data1);
									}
								}
							}

							//Determine the soil depth based on world height
							int dirtH = Math.max(8-((height + 96 - Global.SEALEVEL) / 16), 0);

							if(var13 > 0) {
								if (height >= seaLevel - 1 && index+1 < idsTop.length && idsTop[index + 1] != VCBlocks.SaltWaterStationary && dirtH > 0) {
									idsBig[indexBig] = surfaceBlock;
									metaBig[indexBig] = (byte)VC_Core.getSoilMeta(rock1.data1);


									for (int c = 1; c < dirtH && !VC_Core.isMountainBiome(biome.biomeID) && 
											biome != VCBiome.HighHills && biome != VCBiome.HighHillsEdge && !cliffMap[arrayIndex]; c++) {
										int _height = height - c;
										int _indexBig = ((arrayIndex) * worldHeight + _height + indexOffset);
										idsBig[_indexBig] = subSurfaceBlock;
										metaBig[_indexBig] = (byte)VC_Core.getSoilMeta(rock1.data1);

										if (c > 1+(5-drainage.data1)) {
											idsBig[_indexBig] = VC_Core.getTypeForGravel(rock1.data1);
											metaBig[_indexBig] = (byte)VC_Core.getSoilMeta(rock1.data1);
										}
									}
								}
							}
						}
						
						if (!(biome == VCBiome.swampland)) {
							if (((height > seaLevel - 2 && height < seaLevel && idsTop[index + 1] == VCBlocks.SaltWaterStationary)) || (height < seaLevel && idsTop[index + 1] == VCBlocks.SaltWaterStationary)) {
								if (idsBig[indexBig] != VC_Core.getTypeForSand(rock1.data1) && rand.nextInt(5) != 0) {
									idsBig[indexBig] = VC_Core.getTypeForGravel(rock1.data1);
									metaBig[indexBig] = (byte)VC_Core.getSoilMeta(rock1.data1);
								}
							}
						}
					}
					else if (idsTop[index] == VCBlocks.SaltWaterStationary && biome != VCBiome.ocean && biome != VCBiome.DeepOcean && biome != VCBiome.beach && biome != VCBiome.gravelbeach) {
						idsBig[indexBig] = VCBlocks.FreshWaterStationary;
					}
				}
			}
		}
	}
	*/
	
	
	
	
	
	
	
	
	

	@Override
	public boolean unloadQueuedChunks()
	{
		return true;
	}


	public static List getCreatureSpawnsByChunk(World world, BiomeVC biome, int par2, int par3) {
		ArrayList<SpawnListEntry> list = new ArrayList<SpawnListEntry>();
		
		list.add(new SpawnListEntry(EntityPig.class, 1, 2, 4));
		list.add(new SpawnListEntry(EntityCow.class, 1, 2, 4));
		list.add(new SpawnListEntry(EntityChicken.class, 1, 2, 4));
		
		return list;
	}
	
}
