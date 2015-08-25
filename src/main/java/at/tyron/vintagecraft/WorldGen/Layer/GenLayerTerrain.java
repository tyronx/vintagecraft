package at.tyron.vintagecraft.WorldGen.Layer;

import java.util.Random;

import at.tyron.vintagecraft.World.BiomeVC;
import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldGen.GenLayerVC;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

public class GenLayerTerrain extends GenLayerVC {
	
	// This creates flat lands 
	GenLayerVC noiseFieldModifier;	
	//GenLayerVC noiseFieldModifier2;
	

	



	/** A double array that hold terrain noise*/
	double[] noise3;
	double[] noise1;
	double[] noise2;
	//double[] noise5;
	double[] noise6;
	
	int noiseFieldModifierArray[];
	int noiseFieldModifierArray2[];

	
	/**
	 * Used to store the 5x5 parabolic field that is used during terrain generation.
	 */
	float[] parabolicField;
	

	/** NoiseGeneratorOctaves used in generating terrain */
	private NoiseGeneratorOctaves noiseGen1;
	private NoiseGeneratorOctaves noiseGen2;
	private NoiseGeneratorOctaves noiseGen3;
	private NoiseGeneratorOctaves noiseGen4;
	//public NoiseGeneratorOctaves noiseGen5;
	public NoiseGeneratorOctaves noiseGen6;
	
	
	/** Holds the overall noise array used in chunk generation */
	double[] noiseArray;


	Random rand;
	BiomeGenBase []largerBiomeMap = null;
	
	
	public GenLayerTerrain(long seed) {
		super(seed);
		this.rand = new Random(seed);
		this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 2);
		this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
		this.noiseGen4 = new NoiseGeneratorOctaves(this.rand, 4);
		//this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 2);
		this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 1);

		this.noiseFieldModifier = GenLayerVC.genNoiseFieldModifier(seed, -70);
		//this.noiseFieldModifier2 = GenLayerVC.genNoiseFieldModifier(seed + 50, 0);

	}

	@Override
	public int[] getInts(int x, int z, int xSize, int zSize) {
		return null;
	}
	
	public int[] getHeightmap(World world, int chunkZ, int chunkX, int xSize, int zSize) {
		xSize = (int) (16 * Math.ceil(xSize / 16f));
		zSize = (int) (16 * Math.ceil(zSize / 16f));
		
		int[] heightmap = new int[xSize * zSize];

		/*for (int c)
		
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 256; y > 0; y--) {
					if (primer.getBlockState(x, y, z).getBlock() != Blocks.air) {
						heightmap[x + z * 16] = y;
						break;
					}
				}
			}
		}*/
		
		return heightmap;
	}

	
	public void generateTerrain(int chunkX, int chunkZ, ChunkPrimer primer, World world) {
		generateTerrainHigh(chunkX, chunkZ, primer, world);
		generateTerrainLow(chunkX, chunkZ, primer);
	}
	
	
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

	
	
	public void generateTerrainHigh(int chunkX, int chunkZ, ChunkPrimer primer, World world) {
		byte horizontalPart = 4;
		byte verticalPart = 20;
		
		int xSize = horizontalPart + 1;
		int ySize = verticalPart + 1;
		int zSize = horizontalPart + 1;
		
		largerBiomeMap = world.getWorldChunkManager().getBiomesForGeneration(largerBiomeMap, chunkX * 4 - 2, chunkZ * 4 - 2, xSize + 5, zSize + 5);
		
		this.noiseArray = this.initializeNoiseFieldHigh(this.noiseArray, chunkX * horizontalPart, 0, chunkZ * horizontalPart, xSize, ySize, zSize);
		
		double yLerp = 0.125D;
		double xLerp = 0.25D;
		double zLerp = 0.25D;
		
	
		for (int x = 0; x < horizontalPart; ++x) {
			for (int z = 0; z < horizontalPart; ++z) {
				for (int y = 0; y < verticalPart; ++y) {
					
					// Interpolation of 2x2x2 into 4x8x4 
					
					double lower_lefttop = this.noiseArray[((x + 0) * zSize + z + 0) * ySize + y + 0];
					double lower_leftbottom = this.noiseArray[((x + 0) * zSize + z + 1) * ySize + y + 0];
					double lower_righttop = this.noiseArray[((x + 1) * zSize + z + 0) * ySize + y + 0];
					double lower_rightbottom = this.noiseArray[((x + 1) * zSize + z + 1) * ySize + y + 0];
					
					double dy_lefttop = (this.noiseArray[((x + 0) * zSize + z + 0) * ySize + y + 1] - lower_lefttop) * yLerp;
					double dy_leftbottom = (this.noiseArray[((x + 0) * zSize + z + 1) * ySize + y + 1] - lower_leftbottom) * yLerp;
					double dy_righttop = (this.noiseArray[((x + 1) * zSize + z + 0) * ySize + y + 1] - lower_righttop) * yLerp;
					double dy_rightbottom = (this.noiseArray[((x + 1) * zSize + z + 1) * ySize + y + 1] - lower_rightbottom) * yLerp;

					for (int dy = 0; dy < 8; ++dy) {
						
						
						double topCounting = lower_lefttop;
						double bottomCounting = lower_leftbottom;
						
						double noisetopdx = (lower_righttop - lower_lefttop) * xLerp;
						double noisedowndx = (lower_rightbottom - lower_leftbottom) * xLerp;

						for (int dx = 0; dx < 4; ++dx) {
							
							double var49 = (bottomCounting - topCounting) * zLerp;
							double var47 = topCounting - var49;

							for (int dz = 0; dz < 4; ++dz) {
								
								if ((var47 += var49) > 0.0D) {
									primer.setBlockState(4*x + dx, 8*y + dy + VCraftWorld.instance.terrainGenHiLevel, 4*z + dz, Blocks.stone.getDefaultState());
								} else if (y * 8 + dy + VCraftWorld.instance.terrainGenHiLevel < VCraftWorld.instance.seaLevel) {
									primer.setBlockState(4*x + dx, 8*y + dy + VCraftWorld.instance.terrainGenHiLevel, 4*z + dz, Blocks.water.getDefaultState());
								} else {
									primer.setBlockState(4*x + dx, 8*y + dy + VCraftWorld.instance.terrainGenHiLevel, 4*z + dz, Blocks.air.getDefaultState());
								}
								
							}
							
							topCounting += noisetopdx;
							bottomCounting += noisedowndx;
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
		//noiseFieldModifierArray2 = noiseFieldModifier2.getInts(xPos, zPos, xSize, zSize);
		
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
		
		double horizontalScale = 1300D;
		double verticalScale = 1000D;
		
		//this.noise5 = this.noiseGen5.generateNoiseOctaves(this.noise5, xPos, zPos, xSize, zSize, 1.121D, 1.121D, 0.5D);
		this.noise6 = this.noiseGen6.generateNoiseOctaves(this.noise6, xPos, zPos, xSize, zSize, 800.0D, 800.0D, 0.5D);
		
		// Seems to be the lowest octave
		this.noise1 = this.noiseGen1.generateNoiseOctaves(this.noise1, xPos, yPos, zPos, xSize, ySize, zSize, horizontalScale / 8000D, verticalScale / 10D, horizontalScale / 8000D);
		
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
					
					result = 2D * noise1[posIndex] 
							  + (noise2[posIndex] / 512D + (this.noise3[posIndex] / 10.0D + 1.0D) / 8.0D) * Math.max(0f, noiseFieldModifierArray[x + z * xSize] / 255f);
;
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
	
	
	
	
	
}
