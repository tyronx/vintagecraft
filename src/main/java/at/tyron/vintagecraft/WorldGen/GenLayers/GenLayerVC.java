package at.tyron.vintagecraft.WorldGen.GenLayers;
	
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import at.tyron.vintagecraft.World.VCBiome;
import at.tyron.vintagecraft.WorldGen.WorldTypeVC;
import at.tyron.vintagecraft.WorldGen.GenLayers.Continent.GenLayerAddIsland;
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
import at.tyron.vintagecraft.WorldGen.GenLayers.River.GenLayerRiver;
import at.tyron.vintagecraft.WorldGen.GenLayers.Rock.GenLayerRockInit;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import net.minecraft.world.gen.layer.GenLayer;

public abstract class GenLayerVC extends GenLayer {

	protected long worldGenSeed;
	protected GenLayerVC parent;
	protected long chunkSeed;
	protected long baseSeed;
	
	// Generates Wind, Temperature and Rainfall map
	// R = Temperature    use  R-Value / 5 - 10 => Temp range from -10 to +40
	// G = Wind           use lower 3 bits for 
	// B = Rain           
	public static GenLayerVC genClimate(long seed) {
		GenLayerVC noise = new GenLayerRGBNoise(1L);
		GenLayerVC.drawImageRGB(512, noise, "Climate 0 Noise");
		
		GenLayerVC climate = GenLayerZoom.magnify(2L, noise, 4);
		GenLayerVC.drawImageRGB(512, climate, "Climate 1 4x Magnify");
		
		climate = new GenLayerBlurAll(2L, 1, 5, climate);
		GenLayerVC.drawImageRGB(512, climate, "Climate 4 Blur ");
		
		climate = GenLayerZoom.magnify(2L, climate, 8);
		GenLayerVC.drawImageRGB(512, climate, "Climate 8x Magnify");
		
		return climate;
	}
	

	public static GenLayerVC genDeposits(long seed) {
		GenLayerVC noise = new GenLayerWeightedNoise(1L, EnumMaterialDeposit.values());
		GenLayerVC.drawImageRGB(512, noise, "Deposits 0 Noise");
		
		noise.initWorldGenSeed(seed);
		
		GenLayerVC deposits = GenLayerZoom.magnify(2L, noise, 2);
		GenLayerVC.drawImageRGB(512, deposits, "Deposits 1 2x Magnify");
		
		deposits = new GenLayerAddNoise(3L, 70, 10, 8, 70, 30, deposits);
		GenLayerVC.drawImageRGB(512, deposits, "Deposits 2 Add heightmap (green)");

		deposits = new GenLayerBlurSelective(2L, 1, 5, false, 8, deposits);
		GenLayerVC.drawImageRGB(512, deposits, "Deposits 3 Blur Heightmap (green)");

		deposits = GenLayerZoom.magnify(4L, deposits, 2);
		GenLayerVC.drawImageRGB(512, deposits, "Deposits 4 2x Magnify");
		
		deposits.initWorldGenSeed(seed);
		
		return deposits;
	}
	
	public static GenLayerVC genForest(long seed) {
		//System.out.println("gen forest " + seed);
		
		GenLayerVC noise = new GenLayerNoise(1L, 54);
		GenLayerVC.drawImageGrayScale(512, noise, "Forest 0 Noise");
		
	//	noise.initWorldGenSeed(seed);
		
		//GenLayerVC forest = new GenLayerBlur(seed, 8, 3, noise);
		GenLayerVC forest = new GenLayerBlurAll(2L, 2, 8, noise);
		GenLayerVC.drawImageGrayScale(512, forest, "Forest 1 Blur");
		
		forest = new GenLayerContrastAndBrightness(3L, 4f, 0, forest);
		GenLayerVC.drawImageGrayScale(512, forest, "Forest 2 Contrast");
		
		//forest = new GenLayerSubstract(seed, forest, new GenLayerBlur(seed, 2, 5, noise));
		forest = new GenLayerClampedSubstract(4L, 0, 1, forest, new GenLayerBlurAll(seed, 1, 15,  noise));
		GenLayerVC.drawImageGrayScale(512, forest, "Forest 3 Unsharp Mask");
		
		forest = GenLayerZoom.magnify(1000L, forest, 2);
		GenLayerVC.drawImageGrayScale(512, forest, "Forest 4 Zoom");
		
		forest.initWorldGenSeed(seed);
		
		return forest;
	}

	

	// Blue value = rocktype
	// Red value = layer thickness
	// Green value = temporary value layer for substracting thickness from red 
	public static GenLayerVC genRockLayer(long seed, EnumRockType[] rocktypes) {
		GenLayerVC rocklayer = new GenLayerWeightedNoise(1L, rocktypes);
		GenLayerVC.drawImageRGB(512, rocklayer, "Rocks 0 Noise - rocks and thickness");
		
		rocklayer = new GenLayerExactZoom(2001L, 2, rocklayer);
		drawImageRGB(512, rocklayer, "Rocks 2 Exact Zoom");
	
		rocklayer = new GenLayerBlurAll(2L, 1, 2, rocklayer);
		drawImageRGB(512, rocklayer, "Rocks 3 Blur");
		
		rocklayer = new GenLayerBlurAll(2L, 1, 5, rocklayer);
		drawImageRGB(512, rocklayer, "Rocks 4 Blur blue");

		rocklayer = new GenLayerReducePallette(2001L, rocktypes, rocklayer);
		drawImageRGB(512, rocklayer, "Rocks 5 reducepallete");

		
	/*	rocklayer = new GenLayerContrastAndBrightness(4L, 0.2f, 10, rocklayer);
		GenLayerVC.drawImageRGB(512, rocklayer, "Rocks 5 Contrast");
		*/
		rocklayer = GenLayerZoom.magnify(seed, rocklayer, 12);
		drawImageRGB(512, rocklayer, "Rocks 6 12x magnify");

		


		
		
		//System.out.println("gen rock");
		/*
		GenLayerVC rocklayer = new GenLayerWeightedNoise(1L, rocktypes);
		GenLayerVC.drawImageRGB(512, rocklayer, "Rocks 0 Noise - rocks and thickness");
		
		
		rocklayer.initWorldGenSeed(seed);
		
		rocklayer = GenLayerZoom.magnify(2L, rocklayer, 2);
		GenLayerVC.drawImageRGB(512, rocklayer, "Rocks 1 2x Magnify");
		
		rocklayer = new GenLayerBlur(2L, 1, 2, false, 16, rocklayer);
		GenLayerVC.drawImageRGB(512, rocklayer, "Rocks 2 Blur Thickness (red)");
		
		
		rocklayer = new GenLayerCopyColor(16, 8, rocklayer);
		GenLayerVC.drawImageRGB(512, rocklayer, "Rocks 3 Copy red to green");
		
		
		rocklayer = GenLayerZoom.magnify(4L, rocklayer, 6);
		GenLayerVC.drawImageRGB(512, rocklayer, "Rocks 5 6x Magnify");
	
	
		GenLayerVC rocklayer2 = new GenLayerBlur(2L, 2, 4, false, 8, rocklayer);
		GenLayerVC.drawImageRGB(512, rocklayer2, "Rocks 7 Blur green");
		
		rocklayer = new GenLayerClampedSubstract(4L, 8, rocklayer, rocklayer2);
		GenLayerVC.drawImageRGB(512, rocklayer, "Rocks 8 Substract green blur");
		
		
		rocklayer = new GenLayerClampedSubstract(4L, 16, 8, rocklayer, rocklayer);
		GenLayerVC.drawImageRGB(512, rocklayer, "Rocks 9 Substract green blur diff from red (thickness)");
		
		
		
		*/
		
		
	/*
		
		GenLayerVC rocklayer = new GenLayerNoise(1L, 20, 2);
		drawImageRGB(512, rocklayer, "NewRock 0 Noise");
				
		rocklayer.initWorldGenSeed(seed);
		
		rocklayer = new GenLayerBlur(2L, 1, 3, false, rocklayer);
		drawImageRGB(512, rocklayer, "NewRock 1 Blur");

		rocklayer = new GenLayerExactZoom(2001L, 2, rocklayer);
		drawImageRGB(512, rocklayer, "NewRock 2 Exact Zoom");
		
		rocklayer = new GenLayerBlur(3L, 1, 8, false, rocklayer);
		drawImageRGB(512, rocklayer, "NewRock 3 Blur");
		
		rocklayer = new GenLayerContrastAndBrightness(4L, 0.7f, 50, rocklayer);
		GenLayerVC.drawImageRGB(512, rocklayer, "NewRock 4 Contrast");

		GenLayerVC debug = new GenLayerReducePallette(2001L, rocktypes.length, rocklayer);
		drawImageRGB(512, debug, "NewRock 5 reduce pallette");
		drawImageRGB(512, GenLayerZoom.magnify(seed, debug, 8), "NewRock 6 8x magnify");
		
		
		rocklayer = new GenLayerReducePallette(2001L, rocktypes, rocklayer);
		//drawImageRGB(512, rocklayer, "NewRock 5 reduce pallette");
				
		rocklayer = GenLayerZoom.magnify(5L, rocklayer, 8);
		//drawImageRGB(512, rocklayer, "NewRock 6 8x magnify");
		*/
		
		
		rocklayer.initWorldGenSeed(seed);
		
		return rocklayer;

		
		/*GenLayerVC layer = new GenLayerRockInit(1L, rocks);
		drawImageRocks(512, layer, "Rock 0");
		
		layer = new GenLayerFuzzyZoom(2000L, layer);
		drawImageRocks(512, layer, "Rock 1");
		//layer = new GenLayerAddRock(1L, layer);
		drawImageRocks(512, layer, "Rock 2");
		layer = new GenLayerZoom(2001L, layer);
		//layer = new GenLayerAddRock(2L, layer);
		drawImageRocks(512, layer, "Rock 3");
		layer = new GenLayerZoom(2002L, layer);
		//layer = new GenLayerAddRock(3L, layer);
		drawImageRocks(512, layer, "Rock 4");
		layer = new GenLayerZoom(2003L, layer);
		//layer = new GenLayerAddRock(4L, layer);
		drawImageRocks(512, layer, "Rock 5");
		layer = new GenLayerSmooth(1000L, layer);
		drawImageRocks(512, layer, "Rock 6");
		for (int zoomLevel = 0; zoomLevel < 5; ++zoomLevel)
		{
			layer = new GenLayerZoom(1000 + zoomLevel, layer);
			drawImageRocks(512, layer, "Rock "+(7+zoomLevel));
		}

		GenLayerSmooth smoothedLayer = new GenLayerSmooth(1000L, layer);
		GenLayerVoronoiZoom voronoiLayer = new GenLayerVoronoiZoom(10L, smoothedLayer);
		drawImageRocks(512, layer, "Rock Final");
		smoothedLayer.initWorldGenSeed(seed);
		voronoiLayer.initWorldGenSeed(seed);
		return voronoiLayer;*/		
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
		
		deformationlayer = new GenLayerContrastAndBrightness(4L, 0.3f, 15, deformationlayer);
		GenLayerVC.drawImageRGB(512, deformationlayer, "Rock Deform 7 Contrast");
		
		
		return deformationlayer;
	}
	
	
	
	
	public static GenLayerVC[] genBiomes(long seed) {
		GenLayerVC continent = genContinent(0, false);
		continent = new GenLayerDeepOcean(4L, continent);
		drawImageContinent(512, continent, "Continents 8b Done Deep Ocean");
		byte var4 = 4;

		//Create Biomes
		GenLayerVC continentCopy2 = GenLayerZoom.magnify(1000L, continent, 0);
		drawImageContinent(512, continentCopy2, "Continents 14 Zoom");
		GenLayerVC var17 = new GenLayerBiome(200L, continentCopy2);
		drawImageContinent(512, var17, "Continents15 Biome");
		GenLayerLakes lakes = new GenLayerLakes(200L, var17);
		drawImageContinent(512, var17, "Continents15b Lakes");
		continentCopy2 = GenLayerZoom.magnify(1000L, lakes, 2);
		drawImageContinent(512, continentCopy2, "Continents 16 ZoomBiome");
		GenLayerVC var18 = new GenLayerBiomeEdge(1000L, continentCopy2);
		drawImageContinent(512, var18, "Continents 17 BiomeEdge");
		for (int var7 = 0; var7 < var4; ++var7) {
			var18 = new GenLayerZoom(1000 + var7, var18);
			drawImageContinent(512, var18, "Continents 18-"+var7+" Zoom");
			if (var7 == 0)
				var18 = new GenLayerAddIsland(3L, var18);
			if (var7 == 1)
			{
				var18 = new GenLayerShore(1000L, var18);
				drawImageContinent(512, var18, "Continents 18z Shore");
			}
		}

		//Create Rivers
		GenLayerVC riverCont = GenLayerZoom.magnify(1000L, continent, 2);
		drawImageContinent(512, riverCont, "Continents 9 ContinentsZoom");
		riverCont = new GenLayerRiverInit(100L, riverCont);
		drawImageContinent(512, riverCont, "Continents 10 RiverInit");
		riverCont = GenLayerZoom.magnify(1000L, riverCont, 6);
		drawImageContinent(512, riverCont, "Continents 11 RiverInitZoom");
		riverCont = new GenLayerRiver(1L, riverCont);
		drawImageContinent(512, riverCont, "Continents 12 River");
		riverCont = new GenLayerSmooth(1000L, riverCont);
		drawImageContinent(512, riverCont, "Continents 13 SmoothRiver");

		GenLayerSmoothBiome smoothContinent = new GenLayerSmoothBiome(1000L, var18);
		drawImageContinent(512, smoothContinent, "Continents Biome 19");
		GenLayerRiverMix riverMix = new GenLayerRiverMix(100L, smoothContinent, riverCont);
		drawImageContinent(512, riverMix, "Continents Biome 20");
		GenLayerVC finalCont = GenLayerZoom.magnify(1000L, riverMix, 2);
		drawImageContinent(512, finalCont, "Continents Biome 20-zoom");
		finalCont = new GenLayerSmoothBiome(1001L, finalCont);
		drawImageContinent(512, finalCont, "Continents Biome 21");
		
		
		riverMix.initWorldGenSeed(seed);
		finalCont.initWorldGenSeed(seed);
		
		return new GenLayerVC[]{riverMix, finalCont};
	}

	public static GenLayerVC genContinent(long seed, boolean oceanReduction) {
		GenLayerVC continentStart = new GenLayerIsland(1L+seed);
		drawImageContinent(512, continentStart, "Continents 0 Start");
		GenLayerFuzzyZoom continentFuzzyZoom = new GenLayerFuzzyZoom(2000L, continentStart);
		drawImageContinent(512, continentFuzzyZoom, "Continents 1 FuzzyZoom");
		GenLayerVC var10 = new GenLayerAddIsland(1L, continentFuzzyZoom);
		drawImageContinent(512, var10, "Continents 2 AddIsland");
		GenLayerVC var11 = new GenLayerZoom(2001L, var10);
		drawImageContinent(512, var11, "Continents 3 AddIslandZoom");
		var10 = new GenLayerAddIsland(2L, var11);
		drawImageContinent(512, var10, "4 AddIsland2");
		var11 = new GenLayerZoom(2002L, var10);
		drawImageContinent(512, var11, "Continents 5 AddIslandZoom2");
		var10 = new GenLayerAddIsland(3L, var11);
		drawImageContinent(512, var10, "Continents 6 AddIsland3");
		var11 = new GenLayerZoom(2003L, var10);
		drawImageContinent(512, var11, "Continents 7 AddIslandZoom3");
		GenLayerVC continent = new GenLayerAddIsland(4L, var11);
		drawImageContinent(512, continent, "Continents 8 Done");
		return continent;
	}

	public static boolean shouldDraw = false;
	
	public static void drawImageContinent(int size, GenLayerVC genlayer, String name) {
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
						if(id != -1 && VCBiome.getBiomeGenArray()[id] != null) {
							graphics.setColor(Color.getColor("", VCBiome.getBiome(id).getBiomeColor()));	
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
	public abstract int[] getInts(int var1, int var2, int var3, int var4);

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
				if(VCBiome.biomeList[array[x+z*xSize]] == null)
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
