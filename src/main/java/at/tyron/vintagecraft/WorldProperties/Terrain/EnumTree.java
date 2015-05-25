package at.tyron.vintagecraft.WorldProperties.Terrain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.util.IStringSerializable;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldGen.Helper.DynTreeGen;


//http://www.hoovedesigns.com/woods.html
//http://www.piecesofwood.com/woods.html

//Black wood: http://en.wikipedia.org/wiki/Dalbergia_melanoxylon


public enum EnumTree implements IStateEnum, IStringSerializable {
	// growthspeed, saplingdroprate weight, mintemp, maxtemp, minrain, maxrain, minfertility, maxfertility, minheight, maxheight, minforest, maxforest
	
	//		  	   growthspeed   sapd  weig mint maxt minr  maxr  minf  maxf   minh   maxh minfo   maxfo
	ASH					(   1f,  0.08f,   0,   0,   0,   0,    0,   0,     0,    0f,  0.7f,   0,  255),
	BIRCH				( 0.6f,  0.12f, 100,   3,  12,  95,  220,  10,   100,    0f, 0.65f,   0,  255),		// http://en.wikipedia.org/wiki/Birch
	OAK					( 1.8f,  0.12f,  95,   2,  22,  95,  170,  90,   255,    0f, 0.65f,   0,  150), 	// http://cdn1.arkive.org/media/4A/4A8E82BD-D487-4C74-805B-7C29A318029F/Presentation.Large/Sessile-oak-tree-in-winter.jpg
	CRIMSONKINGMAPLE	( 1.2f,  0.08f,   1,   5,  15,  95,  150,  90,   200,    0f,  0.5f,   0,  250),		// http://m3.i.pbase.com/u37/10kzoomfz/large/24239783.tree.jpg
	MOUNTAINDOGWOOD		( 0.7f,  0.08f,  60,  10,  20,  90,  180,  90,   255,    0f,    1f,   0,  150),
	SCOTSPINE			( 0.8f,  0.12f, 100, -20,  12,  50,  150,  30,   255, 0.55f, 0.85f,   0,  255),		// http://www.treetopics.com/pinus_sylvestris/scots_pine_1220645.png | http://en.wikipedia.org/wiki/Scots_pine
	SPRUCE				(   1f,  0.12f, 100, -12,  18,  70,  255,  60,   255,    0f, 0.85f,   0,  255),
	ACACIA				( 1.1f,  0.09f, 100,  23,  30,  80,  140,  50,   200,    0f,  0.8f,   0,  255),		// http://static.panoramio.com/photos/large/37061566.jpg
	KAPOK				( 1.6f,  0.01f, 100,  24,  30, 185,  255, 160,   255,     0,  0.9f, 100,  255),		// http://www.rainforest-alliance.org/sites/default/files/uploads/4/kapok-tree-inset2.jpg
	PURPLEHEARTWOOD 	(   2f,  0.01f,   3,  24,  30, 185,  255, 160,   255,     0,  0.9f, 100,  255),
	COCONUTPALM			(   1f,  0.08f,   0,   0,   0,   0,    0,   0,     0,    0f,    0f,   0,  255),
	ELEPHANTTREE		( 1.3f,  0.08f,  99,  22,  30,  30,   75,  20,   100,     0, 0.75f,   0,  255),
	MYRTLEBEECH			(1.15f,  0.08f, 100,  15,  22, 190,  255, 130,   255,    0f,  0.8f,   0,  255), 	 // http://api.ning.com/files/MqhG5otblM36rGP5RG6jXZBh7Zo3GaqEoLpCbeE6R2iWuRYcGYXPrC3070Xuqd7SUBMFB0OxF9gl8Py3gCQZ2483-NhiYqbl/NothofagusCunninghammii.jpg
	JOSHUA				( 0.9f,  0.08f,  99,  24,  30,  20,   60,  20,   100,     0,  0.6f,   0,  255),
	PEAR				( 1.2f,  0.08f,  70,   6,  20, 100,  180,  100,  200,    0f,  0.8f,   0,  100),		 // http://fc06.deviantart.net/fs40/f/2009/004/e/f/An_old_pear_tree_by_SzekelyCsaba.jpg
	AFRICANMAHOGANY		( 1.2f,  0.08f,  96,  23,  30, 145,  220,  100,  230,    0f,  0.8f,   0,  200),
	BLACKWALNUT			( 1.15f, 0.08f,  10,  15,  22, 170,  255,  130,  255,    0f,   0.5f,   0,  255),
	POPLAR				( 0.6f,  0.08f,  85,   3,  20, 115,  255,  100,  255,    0f, 0.95f,   0,  255, true),
	WEEPINGWILLOW		( 0.5f,  0.06f,  96,  -6,  18,  65,  255,   50,  255,    0f,    1f,   0,  250),		// https://c1.staticflickr.com/9/8467/8371823596_2c2e65240e.jpg
	COYOTEWILLOW		( 0.5f,  0.06f, 100, -12,  19,  60,  255,   50,  255,    0f, 1.05f,   0,  250),
	LARCH				( 0.8f,  0.06f,  99, -23,  10,  48,  150,   30,  180,    0f,  0.6f,   0,  255, true),
	KAURI				( 1.3f,  0.01f,  99,  20,  24, 120,  180,   90,  200,    0f,  0.8f,   0,  255)
	
	
//	COCOATREE				(100, 0,  0,  0,    0, 0,   0, 0f, 0f),			// http://images.fineartamerica.com/images-medium-large/cacao-tree-granger.jpg
//	APPLE					(100, 0,  0,  0,    0, 0,   0, 0f, 0f),			// http://www.gardenista.com/files/styles/733_0s/public/img/sub/uimg/01-2013/700_pruned-apple-tree-jpeg.jpg
//	FOXTAILPINE				(100, 0,  0,  0,    0, 0,   0, 0f, 0f),			// http://en.wikipedia.org/wiki/Pinus_balfouriana
//	PINYONPINE				(100, 0,  0,  0,    0, 0,   0, 0f, 0f),			// http://www.history.com/images/media/slideshow/new-mexico/new-mexico-state-tree-pinyon-pine.jpg  | http://images.fineartamerica.com/images-medium-large/pinyon-pine-tree-james-marvin-phelps.jpg
//	AFRICANBLACKWOOD		(100, 0,  0,  0,    0, 0,   0, 0f, 0f),  		// http://www.prota4u.org/plantphotos/Dalbergia%20melanoxylon%202.jpg
//	BAOBAB					(100, 0,  0,  0,    0, 0,   0, 0f, 0f),
//	SEQUOIA					(100, 0,  0,  0,    0, 0,   0, 0f, 0f),			// 
	
	// Jungle Trees: http://www.rainfor.org/upload/publication-store/2013/ter%20Steege/ter%20Steege%20et%20al%202013%20Hyperdominance%20in%20the%20Amazonian%20Tree%20Flora%20Science.pdf
	
//	DIPTEROCARP				(100, 0,  0,  0,    0, 0,   0, 0f, 0f),			//   http://atbioversity.net/edukitmys/images/Shorea.JPG (http://en.wikipedia.org/wiki/Dipterocarpaceae)
//	DATEPALM				(100, 0,  0,  0,    0, 0,   0, 0f, 0f),
	

	;


	public float growthspeed;
	public float saplingdropchance;
	
	int weight;
	int mintemp;
	int maxtemp ;
	int minrain;
	int maxrain;
	int minfertility;
	int maxfertility;
	int minforest;
	int maxforest;
	boolean competitive;
	
	// Height of 0f == sealevel
	// Height of 1f == 255
	float minheight;
	float maxheight;
	
	// Generated from above values
	public int miny;
	public int maxy;
	
	public DynTreeGen defaultGenerator;
	public DynTreeGen poorGenerator;			// Generates a poorly grown tree due to lack of light, rain, fertility, etc.
	public DynTreeGen lushGenerator;			// Generates a lushous tree that has enough water, nutrients and no competition for light 
	
	public void setGenerators(DynTreeGen defaultgen, DynTreeGen poor, DynTreeGen lush) {
		this.defaultGenerator = defaultgen;
		this.poorGenerator = poor;
		this.lushGenerator = lush;
	}
	
	public DynTreeGen getGenerator(int rain, int temp, int fertility, int lightcompetition) {
		return defaultGenerator;
	}
	
	EnumTree(float growthspeed, float saplingdropchance, int weight, int mintemp, int maxtemp, int minrain, int maxrain, int minfertility, int maxfertility, float minheight, float maxheight, int minforest, int maxforest) {
		this(growthspeed, saplingdropchance, weight, mintemp, maxtemp, minrain, maxrain, minfertility, maxfertility, minheight, maxheight, minfertility, maxforest, false);
	}
	
	EnumTree(float growthspeed, float saplingdropchance, int weight, int mintemp, int maxtemp, int minrain, int maxrain, int minfertility, int maxfertility, float minheight, float maxheight, int minforest, int maxforest, boolean competitive) {
		this.competitive = competitive;
		this.saplingdropchance = saplingdropchance;
		this.growthspeed = growthspeed;
		this.weight = weight;
		this.mintemp = mintemp;
		this.maxtemp = maxtemp;
		this.minrain = minrain;
		this.maxrain = maxrain;
		this.minforest = minforest;
		this.maxforest = maxforest;
		this.minfertility = minfertility;
		this.maxfertility = maxfertility;
		this.maxheight = maxheight;
		this.minheight = minheight;
		
		this.miny = (int)(minheight * (255 - VCraftWorld.seaLevel) + VCraftWorld.seaLevel);
		this.maxy = (int)(maxheight * (255 - VCraftWorld.seaLevel) + VCraftWorld.seaLevel);
	}
	
	
	
	
	public static DynTreeGen getRandomTreeGenForClimate(int rainfall, int temperature, int forest, int fertility, int steepness, int y, Random random) {
		EnumTree tree = getRandomTreeForClimate(rainfall, temperature, forest, y, random);
		if (tree == null) return null;
		
		float rainspan = tree.maxrain - tree.minrain;
		float tempspan = tree.maxtemp - tree.mintemp;
		
		float dist = (rainfall - tree.minrain) / rainspan + (temperature - tree.mintemp) / tempspan;    // 0 = at minimum, 2 == at maximum    => 1 means perfect conditions
		
		if ((dist < 0.55 || fertility < 50 || steepness > 5) && tree.poorGenerator != null) return tree.poorGenerator;
		if (dist > 0.9 && dist < 1.4 && fertility > 100 && tree.lushGenerator != null) return tree.lushGenerator;
		return tree.defaultGenerator;
	}
	
	
	public static EnumTree getRandomTreeForClimate(int rainfall, int temperature, int forest, int y, Random random) {
		//ArrayList<Integer> distances = new ArrayList<Integer>();
		HashMap<EnumTree, Integer> distances = new HashMap<EnumTree, Integer>();
		
		int lowestdistance = 99999; 
		EnumTree lowestdistancetree = null;
		
		for (EnumTree tree : EnumTree.values()) {
			if (tree.weight == 0) continue;
			/*
			System.out.println(tree.name() + "   " +
					rainfall + " > " + tree.minrain  
					+ " || " + rainfall + " < " + tree.maxrain 
					+ " || "+ temperature + " > " + tree.mintemp   
					+ " || "+ temperature + " < " + tree.maxtemp 
					+ " || "+ forest + " > " + tree.minforest  
					+ " || "+ forest + " < " + tree.maxforest);
			*/
			if (
				rainfall < tree.minrain 
				|| rainfall > tree.maxrain 
				|| temperature < tree.mintemp 
				|| temperature > tree.maxtemp 
				|| forest < tree.minforest 
				|| forest > tree.maxforest
				|| y > tree.maxy  
			) continue;
			
		
			
			
			int distance = (
				Math.abs(rainfall - (tree.maxrain + tree.minrain)/2) / 10
				+ Math.abs(temperature - (tree.maxtemp + tree.mintemp)/2) * 2
				+ Math.abs(forest - (tree.maxforest + tree.minforest)/2) / 10
			) * (101 - tree.weight);
			
			//distance /= 10;
			distance = Math.max(2, distance);
			
			distances.put(tree, distance);
			
			if (lowestdistance > distance) {
				lowestdistancetree = tree;
			}
			lowestdistance = Math.min(lowestdistance, distance);
			
			//System.out.println(tree + " " + distance);
		}
		
		
		
		
		
		Set<EnumTree> keys = distances.keySet();
		List<EnumTree> keyList = new ArrayList(keys);
		Collections.shuffle(keyList);
		
		int dist;
		
		for (EnumTree tree : keyList) {
			dist = distances.get(tree);
			
			if (tree.competitive) {
				if (tree == lowestdistancetree) dist = Math.max(2, dist / 4);
				else dist*=4;
			} else {
				if (lowestdistancetree.competitive) dist *= 4;
			}
			
			if (random.nextInt(dist) == 0) return tree;
		}
		
		return null;
		
	}

	
	// 0 = no forest
	// 1 = normal forest
	// 2 = double forest
	public static float getForestDensity(int forest, int rain, int temperature) {
		// forest == 0  => dense forest
		// forest == 255 => no forest
		
		float humidity = rain / Math.max(1f, 1f * VCraftWorld.instance.deScaleTemperature(temperature) - 2 * Math.max(0, rain - 180));
		
		return (1 - forest/255f) * humidity; // * (0.15f + rain / 255f + Math.max(0, rain / 255f - 0.4f));    //   + Math.max(0, 2 * (rain + temperature / 5 - 150) / 255f) - 8 * Math.min(0, rain - 130) / 255f;
	}
	
	
	
	
	public float getTreeSize(Random rand, float heightreduction) {
		float size;
		
		switch (this) {
			case WEEPINGWILLOW:
			case LARCH:
			case SPRUCE:
			case ELEPHANTTREE:
			case POPLAR:
				if (heightreduction < 0.15f && rand.nextInt(220) == 0) return 1.3f + rand.nextFloat() * 0.3f;
				break;
				
			case KAPOK:
			case PURPLEHEARTWOOD:
			case KAURI:
				if (rand.nextInt(500) == 1) return 1.3f + rand.nextFloat()*0.6f - heightreduction/2;
				return 0.2f + rand.nextFloat() - heightreduction;
				
			default: break;
		}
		
		
		return 0.66f + rand.nextFloat()/3 - heightreduction;
	}
	
	
	
	@Override
	public int getMetaData(Block block) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getStateName() {
		return name().toLowerCase();
	}

	@Override
	public void init(Block block, int meta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getId() {
		return ordinal();
	}
	
	
	public static EnumTree byId(int id) {
		for (EnumTree tree : values()) {
			if (tree.getId() == id) {
				return tree;
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return name().toLowerCase();
	}
	
	
}


