package at.tyron.vintagecraft.WorldGen.GenLayers;
	
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import at.tyron.vintagecraft.World.BiomeVC;
import at.tyron.vintagecraft.WorldGen.WorldTypeVC;
/*import at.tyron.vintagecraft.WorldGen.GenLayers.Continent.GenLayerAddIsland;
import at.tyron.vintagecraft.WorldGen.GenLayers.Continent.GenLayerBiomeEdge;
import at.tyron.vintagecraft.WorldGen.GenLayers.Continent.GenLayerBiome;
import at.tyron.vintagecraft.WorldGen.GenLayers.Continent.GenLayerDeepOcean;
import at.tyron.vintagecraft.WorldGen.GenLayers.Continent.GenLayerIsland;
import at.tyron.vintagecraft.WorldGen.GenLayers.Continent.GenLayerLakes;
import at.tyron.vintagecraft.WorldGen.GenLayers.Continent.GenLayerShore;
import at.tyron.vintagecraft.WorldGen.GenLayers.Continent.GenLayerSmoothBiome;
import at.tyron.vintagecraft.WorldGen.GenLayers.Continent.GenLayerSmooth;
import at.tyron.vintagecraft.WorldGen.GenLayers.River.GenLayerRiverInit;
import at.tyron.vintagecraft.WorldGen.GenLayers.River.GenLayerRiverMix;
import at.tyron.vintagecraft.WorldGen.GenLayers.River.GenLayerRiver;*/
import at.tyron.vintagecraft.WorldGen.GenLayers.Rock.GenLayerRockInit;
import at.tyron.vintagecraft.WorldProperties.*;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraftforge.fml.common.asm.transformers.DeobfuscationTransformer;

public abstract class GenLayerVC extends GenLayer {

	protected long worldGenSeed;
	protected GenLayerVC parent;
	protected long chunkSeed;
	protected long baseSeed;
	
	
	public static GenLayerVC genNoiseFieldModifier(long seed) {	
		GenLayerSimplexNoise noise = new GenLayerSimplexNoise(seed, 5, 0.95f, 170, 0);
		GenLayerVC.drawImageGrayScale(512, noise, "NoiseFieldModifier 0 Noise");
		
		GenLayerVC noisemod = new GenLayerBlurAll(seed, 1, 4, noise);
		GenLayerVC.drawImageGrayScale(512, noisemod, "NoiseFieldModifier 1 Blur");
		
		return noisemod;
	}
	
	
	public static GenLayerVC genAgemap(long seed) {
		GenLayerSimplexNoiseUnclamped noise = new GenLayerSimplexNoiseUnclamped(seed, 3, 0.7f, 45, 20);
		GenLayerVC.drawImageGrayScale(512, noise, "Age 0 Noise");

		return noise;
	}
	
	public static GenLayerVC genHeightmap(long seed) {
		GenLayerSimplexNoise noise = new GenLayerSimplexNoise(seed, 6, 0.6f, 67, 80);
		GenLayerVC.drawImageGrayScale(512, noise, "Heightmap 0 Noise");
		
		return noise;
		
		//GenLayerVC noise = new GenLayerHeightmap(seed);
/*		GenLayerVC noise = new GenLayerNoise(seed, 40);
		GenLayerVC.drawImageGrayScale(512, noise, "Heightmap 0 Noise");
		
		noise.initWorldGenSeed(seed);
		
		GenLayerVC heightmap = new GenLayerBlurAll(seed, 2, 3, noise);
		GenLayerVC.drawImageGrayScale(512, heightmap, "Heightmap 1 Blur");
		
		heightmap = GenLayerZoom.magnify(1000L, heightmap, 2);
		GenLayerVC.drawImageGrayScale(512, heightmap, "Heightmap 2 2xZoom");

		heightmap = new GenLayerBlurAll(seed, 2, 5, heightmap);
		GenLayerVC.drawImageGrayScale(512, heightmap, "Heightmap 3 Blur");

		heightmap = GenLayerZoom.magnify(1000L, heightmap, 4);
		GenLayerVC.drawImageGrayScale(512, heightmap, "Heightmap 4 2xZoom");
		
		heightmap = new GenLayerBlurAll(seed, 2, 5, heightmap);
		GenLayerVC.drawImageGrayScale(512, heightmap, "Heightmap 6 Blur");

		heightmap = new GenLayerAddPerlin(seed, 3, 0.65f, 150, heightmap);
		GenLayerVC.drawImageGrayScale(512, heightmap, "Heightmap 8 Add Perlin");
		
		heightmap.initWorldGenSeed(seed);

		return heightmap;*/
	}
	
	// Generates Fertility, Temperature and Rainfall map
	// R = Temperature    temp = R-Value / 4.25 - 30 ( = Temp range from -30 to +30)      | inverse R-Value = (temp + 30) * 4.25
	// G = Fertility      = generated from temp * rain                         (+ water nearby?)
	// B = Rain           between 0 - 255
	public static GenLayerVC genClimate(long seed) {
		GenLayerVC noise = new GenLayerClimateRGBNoise(1L);
		GenLayerVC.drawImageRGB(512, noise, "Climate 0 Noise");
		
		noise.initWorldGenSeed(seed);
		
		GenLayerVC climate = new GenLayerBlurAll(2L, 1, 3, noise);
		GenLayerVC.drawImageRGB(512, climate, "Climate 2 Blur ");
		
		climate = new GenLayerContrastAndBrightnessAll(3L, 0.5f, 0, climate);
		GenLayerVC.drawImageRGB(512, climate, "Climate 2 Contrast");
		
		climate = new GenLayerClampedSubstractAll(4L, 0, 1, climate, new GenLayerBlurAll(seed, 1, 15,  noise));
		GenLayerVC.drawImageRGB(512, climate, "Climate 3 With Unsharp Mask");
		
		climate = GenLayerZoom.magnify(1000L, climate, 2);
		GenLayerVC.drawImageRGB(512, climate, "Climate 4 2xZoom");

		climate = new GenLayerBlurAll(2L, 1, 3, climate);
		GenLayerVC.drawImageRGB(512, climate, "Climate 5 Blur");
		
		climate = GenLayerZoom.magnify(1000L, climate, 2);
		GenLayerVC.drawImageRGB(512, climate, "Climate 6 2xZoom");
		
		climate = new GenLayerBlurAll(2L, 1, 3, climate);
		GenLayerVC.drawImageRGB(512, climate, "Climate 7 Blur");

		climate = GenLayerZoom.magnify(1000L, climate, 3);
		GenLayerVC.drawImageRGB(512, climate, "Climate 8 4xZoom");

		climate = new GenLayerBlurAll(2L, 1, 3, climate);
		GenLayerVC.drawImageRGB(512, climate, "Climate 9 Blur");

		
		climate.initWorldGenSeed(seed);
		
		return climate;
	}
	

	public static GenLayerVC genDeposits(long seed) {
		ArrayList<EnumMaterialDeposit> largedeposits = new ArrayList<EnumMaterialDeposit>();
		
		for (EnumMaterialDeposit deposit : EnumMaterialDeposit.values()) {
			if (deposit.size == DepositSize.LARGE || deposit.size == DepositSize.SMALLANDLARGE || deposit.size == DepositSize.NONE || deposit.size == DepositSize.HUGE)
				largedeposits.add(deposit);
		}
		
		GenLayerVC noise = new GenLayerWeightedNoise(1L, largedeposits.toArray(new EnumMaterialDeposit[0]));
		GenLayerVC.drawImageRGB(512, noise, "Deposits 0 Noise");
		
		noise.initWorldGenSeed(seed);
		
		GenLayerVC deposits = GenLayerZoom.magnify(2L, noise, 2);
		GenLayerVC.drawImageRGB(512, deposits, "Deposits 1 2x Magnify");
		
		deposits = new GenLayerAddNoise(3L, 70, 10, 8, 70, 30, deposits);
		GenLayerVC.drawImageRGB(512, deposits, "Deposits 2 Add heightmap (green)");

		deposits = GenLayerZoom.magnify(4L, deposits, 2);
		GenLayerVC.drawImageRGB(512, deposits, "Deposits 3 2x Magnify");

		deposits = new GenLayerBlurSelective(2L, 2, 5, false, 8, deposits);
		GenLayerVC.drawImageRGB(512, deposits, "Deposits 4 Blur Heightmap (green)");


		deposits.initWorldGenSeed(seed);
		
		return deposits;
	}
	
	public static GenLayerVC genForest(long seed) {
		//System.out.println("gen forest " + seed);
		
		GenLayerVC noise = new GenLayerNoise(1L, 46);
		GenLayerVC.drawImageGrayScale(512, noise, "Forest 0 Noise");
		
	//	noise.initWorldGenSeed(seed);
		
		//GenLayerVC forest = new GenLayerBlur(seed, 8, 3, noise);
		GenLayerVC forest = new GenLayerBlurAll(2L, 2, 8, noise);
		GenLayerVC.drawImageGrayScale(512, forest, "Forest 1 Blur");
		
		forest = new GenLayerContrastAndBrightnessSelective(3L, 4f, 0, forest);
		GenLayerVC.drawImageGrayScale(512, forest, "Forest 2 Contrast");
		
		//forest = new GenLayerSubstract(seed, forest, new GenLayerBlur(seed, 2, 5, noise));
		forest = new GenLayerClampedSubstractSelective(4L, 0, 1, forest, new GenLayerBlurAll(seed, 1, 15,  noise));
		GenLayerVC.drawImageGrayScale(512, forest, "Forest 3 Unsharp Mask");
		
		forest = GenLayerZoom.magnify(1000L, forest, 2);
		GenLayerVC.drawImageGrayScale(512, forest, "Forest 4 Zoom");

		forest = new GenLayerBlurAll(2L, 1, 3, forest);
		GenLayerVC.drawImageGrayScale(512, forest, "Forest 5 Blur");

		
		forest.initWorldGenSeed(seed);
		
		return forest;
	}

	

	// Blue value = rocktype
	// Red value = layer thickness
	// Green value = temporary value layer for substracting thickness from red 
	public static GenLayerVC genRockLayer(long seed, EnumRockType[] rocktypes) {
		GenLayerVC rocklayer = new GenLayerWeightedNoise(1L, rocktypes, true);
		GenLayerVC.drawImageRGB(512, rocklayer, "Rocks 0 Noise - rocks and thickness");
		
		/*rocklayer = new GenLayerExactZoom(2001L, 2, rocklayer);
		drawImageRGB(512, rocklayer, "Rocks 2 Exact Zoom");*/
		
		rocklayer = GenLayerZoom.magnify(seed, rocklayer, 8);
		drawImageRGB(512, rocklayer, "Rocks 2 8x magnify");
	
		rocklayer = new GenLayerBlurAll(2L, 2, 5, rocklayer);
		drawImageRGB(512, rocklayer, "Rocks 3 Blur");
		
		/*rocklayer = new GenLayerBlurAll(2L, 1, 2, rocklayer);
		drawImageRGB(512, rocklayer, "Rocks 3 Blur");
		
		rocklayer = new GenLayerBlurAll(2L, 1, 5, rocklayer);
		drawImageRGB(512, rocklayer, "Rocks 4 Blur blue");

		rocklayer = new GenLayerReducePallette(2001L, rocktypes, rocklayer);
		drawImageRGB(512, rocklayer, "Rocks 5 reducepallete");*/

	/*	rocklayer = GenLayerZoom.magnify(seed, rocklayer, 4);
		drawImageRGB(512, rocklayer, "Rocks 6 12x magnify");*/
		
		rocklayer.initWorldGenSeed(seed);
		
		return rocklayer;
	}
	
	
	
	public static GenLayerVC genRockDeformation(long seed) {
		GenLayerVC deformationlayer = new GenLayerNoise(1L, 25, 3);
		drawImageRGB(512, deformationlayer, "Rock Deform 0 Noise");
		
		deformationlayer.initWorldGenSeed(seed);

		deformationlayer = new GenLayerBlurSelective(2L, 1, 3, false, deformationlayer);
		drawImageRGB(512, deformationlayer, "Rock Deform 1 Blur");

	
		deformationlayer = new GenLayerExactZoom(5L, 2, deformationlayer);
		drawImageRGB(512, deformationlayer, "Rock Deform 2 2x exact zoom");

	
		deformationlayer = new GenLayerBlurSelective(2L, 1, 3, false, deformationlayer);
		drawImageRGB(512, deformationlayer, "Rock Deform 3 Blur");

	
		deformationlayer = new GenLayerExactZoom(5L, 5, deformationlayer);
		drawImageRGB(512, deformationlayer, "Rock Deform 5 5x exact zoom");

		deformationlayer = new GenLayerBlurSelective(2L, 1, 5, false, deformationlayer);
		drawImageRGB(512, deformationlayer, "Rock Deform 6 Blur");
		
		deformationlayer = new GenLayerContrastAndBrightnessSelective(4L, 0.3f, 15, deformationlayer);
		GenLayerVC.drawImageRGB(512, deformationlayer, "Rock Deform 7 Contrast");
		
		
		return deformationlayer;
	}
	
	
	// Formerly Biomes
	public static GenLayerVC genErosion(long seed) {
		GenLayerVC noise = new GenLayerWeightedNoise(1L, BiomeVC.getBiomes());
		drawImageBiome(512, noise, "Erosion 1 noise");
		
		
		GenLayerVC erosion = GenLayerZoom.magnify(seed, noise, 6);
		drawImageBiome(512, erosion, "Erosion 2 6x magnify");
		
		erosion.initWorldGenSeed(seed);
		
		return erosion;
	}
	
	

	public static boolean shouldDraw = false;
	
	public static void drawImageBiome(int size, GenLayerVC genlayer, String name) {
		drawImage(size, genlayer, name, 0);
	}

	public static void drawImageRGB(int size, GenLayerVC genlayer, String name) {
		drawImage(size, genlayer, name, 1);
	}
	
	public static void drawImageGrayScale(int size, GenLayerVC genlayer, String name) {
		drawImage(size, genlayer, name, 3);
	}


	public static void drawImageRocks(int size, GenLayerVC genlayer, String name) {
		drawImage(size, genlayer, name, 2);
	}

	public static void drawImage(int size, GenLayerVC genlayer, String name, int type) {
		if (!shouldDraw) return;
		drawImage(size, genlayer.getInts(0, 0, size, size), name, type);
	}
	
	public static void drawImage(int x, int z, GenLayerVC genlayer, String name, int type) {
		if (!shouldDraw) return;
		drawImage(512, genlayer.getInts(x, z, 512, 512), name, type);
	}
	
	
	
	// type:
	// 0 .. biome
	// 1 .. grayscale
	// 2 .. rocklayer
	public static void drawImage(int size, int[] ints, String name, int type) {
		if(!shouldDraw) {
			return;
		}
		try 
		{
			File outFile = new File(name+".bmp");
			
			Color c;
			
			BufferedImage outBitmap = new BufferedImage(size,size,BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = (Graphics2D) outBitmap.getGraphics();
			graphics.clearRect(0, 0, size, size);
			
			System.out.println(name+".bmp");
			
			for(int x = 0; x < size; x++) {
				for(int z = 0; z < size; z++) {
					int id = ints[x*size+z];
					
					switch (type) {
					case 0:
						if(id != -1 && BiomeVC.getBiomeGenArray()[id] != null) {
							graphics.setColor(new Color(BiomeVC.getBiome(id).getBiomeColor()));
							graphics.drawRect(x, z, 1, 1);
						}
						break;
						
					case 1:
						//id = id & 0xff;
						c = new Color((id >> 16) & 0xff, (id >> 8) & 0xff, id & 0xff);
						graphics.setColor(c);
						graphics.drawRect(x, z, 1, 1);

						break;
						
					case 2:
						int color = ((id*8)<<16)+((id*8)<<8)+((id*8));
						graphics.setColor(Color.getColor("", color));	
						graphics.drawRect(x, z, 1, 1);
						break;
						
					case 3:
						c = new Color(id & 0xff, id & 0xff, id & 0xff);
						graphics.setColor(c);
						graphics.drawRect(x, z, 1, 1);
						
					}
				}
			}
			
			ImageIO.write(outBitmap, "BMP", outFile);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	

	
	

	public GenLayerVC(long par1) {
		super(par1);
	}


	/**
	 * Returns a list of integer values generated by this layer. These may be interpreted as temperatures, rainfall
	 * amounts, or biomeList[] indices based on the particular GenLayer subclass.
	 */
	@Override
	public abstract int[] getInts(int x, int z, int xSize, int zSize);

	public static int validateInt(int[] array, int index)
	{
		/*if(TFCBiome.biomeList[array[index]] == null)
			System.out.println("Error garbage data: "+array[index]);*/
		return array[index];
	}

	public static void validateBiomeIntArray(int[] array, int xSize, int zSize)
	{
		for(int z = 0; z < zSize; z++)
		{
			for(int x = 0; x < xSize; x++)
			{
				if(BiomeVC.biomeList[array[x+z*xSize]] == null)
				{
					System.out.println("Error Array garbage data: "+array[x+z*xSize]);
					return;
				}
			}
		}
	}
	
	
	
	
	
	
	public int[] cutMargins(int[] inInts, int sizeX, int sizeZ, int margin) {
		int []resultInts = new int[(sizeX - 2*margin) * (sizeZ - 2*margin)];
        int j = 0;
        
		for (int i = 0; i < inInts.length; i++) {
			int xpos = i % sizeX;
			int zpos = i / sizeX;
			
		
			if (xpos >= margin && xpos < sizeX - margin && zpos >= margin && zpos < sizeZ - margin) {
				resultInts[j++] = inInts[i]; 
			}
		}
		
		return resultInts;	
	}
	
	
	public int[] cutRightAndBottom(int[] inInts, int sizeX, int sizeZ, int margin) {
		int []resultInts = new int[(sizeX - margin) * (sizeZ - margin)];
        int j = 0;
        
		for (int i = 0; i < inInts.length; i++) {
			int xpos = i % sizeX;
			int zpos = i / sizeX;
			
		
			if (xpos < sizeX - margin && zpos < sizeZ - margin) {
				resultInts[j++] = inInts[i]; 
			}
		}
		
		return resultInts;			
	}

}
