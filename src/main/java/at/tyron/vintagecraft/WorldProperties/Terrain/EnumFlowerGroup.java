package at.tyron.vintagecraft.WorldProperties.Terrain;

import java.util.*;

import at.tyron.vintagecraft.Block.Organic.BlockFlowerVC;

public enum EnumFlowerGroup {
	// (int mintemp, int maxtemp, int minforest, int maxforest, int minrain, int maxrain, int weight, EnumFlower []variants)
	
	ORANGEMILKWEED   ( 22, 33,   0,  10, 100, 190, 80, new EnumFlower[]{EnumFlower.ORANGEMILKWEED, EnumFlower.ORANGEMILKWEED2}),
	PURPLEMILKWEED   ( 22, 33,   0,  10, 100, 190, 0, new EnumFlower[]{EnumFlower.PURPLEMILKWEED, EnumFlower.PURPLEMILKWEED2}),
	
	CATMINT 	     ( 5, 20,   0,  80, 100, 220, 90, new EnumFlower[]{EnumFlower.CATMINT}),
	CALENDULA        ( 3, 18,   0,  50, 100, 220, 90, new EnumFlower[]{EnumFlower.CALENDULA}),
	CORNFLOWER       ( 3, 18,   0,  50, 100, 220, 90, new EnumFlower[]{EnumFlower.CORNFLOWER, EnumFlower.CORNFLOWER2}, 45),
	
	LILYOFTHEVALLEY  ( -10, 10, 60, 255, 80, 190, 90, new EnumFlower[]{EnumFlower.LILYOFTHEVALLEY, EnumFlower.LILYOFTHEVALLEY2, EnumFlower.LILYOFTHEVALLEY3}),
	
	CLOVER			 ( -10, 14, 60, 255, 90, 190, 100, new EnumFlower[]{EnumFlower.CLOVER}, 50),
	
    GOLDENROD        (15, 28,   0,  10, 120, 190, 0, new EnumFlower[]{EnumFlower.GOLDENROD, EnumFlower.GOLDENROD2, EnumFlower.GOLDENROD3}),
    
    FORGETMENOT		 ( 5, 15,   0, 200, 100, 190, 0, new EnumFlower[]{EnumFlower.FORGETMENOT, EnumFlower.FORGETMENOT2, EnumFlower.FORGETMENOT3, EnumFlower.FORGETMENOT4, EnumFlower.FORGETMENOT5}),
    
    NARCISSUS		 (-8, 14,   1, 200, 100, 190, 90, new EnumFlower[]{EnumFlower.NARCISSUS, EnumFlower.NARCISSUS2, EnumFlower.NARCISSUS3}),
    
	PURPLETULIP		 ( 3, 20,   1, 200, 100, 190, 0, new EnumFlower[]{EnumFlower.PURPLETULIP, EnumFlower.PURPLETULIP2, EnumFlower.PURPLETULIP3}),
	
	COWPARSLEY       ( 3, 18,   0,  80, 100, 200, 95, new EnumFlower[]{EnumFlower.COWPARSLEY, EnumFlower.COWPARSLEY2}, 75),
	
	HORSETAIL        (-5, 19,  50, 255, 115, 255, 98, new EnumFlower[]{EnumFlower.HORSETAIL}, 85),
	
	;
	

	
	int mintemp;
	int maxtemp;
	int minforest;
	int maxforest;
	int minrain;
	int maxrain;
	int weight;
	int maxQuantity;
	public EnumFlower[] variants;
	
	private EnumFlowerGroup(int mintemp, int maxtemp, int minforest, int maxforest, int minrain, int maxrain, int weight, EnumFlower []variants) {
		this(mintemp, maxtemp, minforest, maxforest, minrain, maxrain, weight, variants, 25);
	}
	
	private EnumFlowerGroup(int mintemp, int maxtemp, int minforest, int maxforest, int minrain, int maxrain, int weight, EnumFlower []variants, int quantity) {
		this.mintemp = mintemp;
		this.maxtemp = maxtemp;
		this.minforest = minforest;
		this.maxforest = maxforest;
		this.minrain = minrain;
		this.maxrain = maxrain;
		this.weight = weight;
		this.variants = variants;
		this.maxQuantity = quantity;
	}
	
	
	

	public static EnumFlowerGroup getRandomFlowerForClimate(int rainfall, int temperature, int forest, Random random) {
		//ArrayList<Integer> distances = new ArrayList<Integer>();
		HashMap<EnumFlowerGroup, Integer> distances = new HashMap<EnumFlowerGroup, Integer>();
		
		
		for (EnumFlowerGroup flora : EnumFlowerGroup.values()) {
			if (flora.weight == 0) continue;
			
			/*System.out.println(flora.name() + "   " +
					rainfall + " > " + flora.minrain  
					+ " || " + rainfall + " < " + flora.maxrain 
					+ " || "+ temperature + " > " + flora.mintemp   
					+ " || "+ temperature + " < " + flora.maxtemp 
					+ " || "+ forest + " > " + flora.minforest  
					+ " || "+ forest + " < " + flora.maxforest);
			*/
			if (
				rainfall < flora.minrain 
				|| rainfall > flora.maxrain 
				|| temperature < flora.mintemp 
				|| temperature > flora.maxtemp 
				|| forest < flora.minforest 
				//|| forest > flora.maxforest
			) continue;
			
		
			
			
			int distance = (
				Math.abs(rainfall - (flora.maxrain + flora.minrain)/2) / 10
				+ Math.abs(temperature - (flora.maxtemp + flora.mintemp)/2) * 2
				//+ Math.abs(forest - (flora.maxforest + flora.minforest)/2) / 10
			) * (101 - flora.weight);
			
			distance = Math.max(2, distance);
			
			distances.put(flora, distance);
			
			
			//System.out.println(flora + " " + distance);
		}
		
		Set<EnumFlowerGroup> keys = distances.keySet();
		
		List<EnumFlowerGroup> keyList = new ArrayList(keys);
		
		Collections.shuffle(keyList);
		
		
		
		for (EnumFlowerGroup flora : keyList) {
			
			if (random.nextInt(distances.get(flora)) == 0) return flora;
		}
		
		
		/*ValueComparator bvc = new ValueComparator(distances);
		TreeMap<EnumFlora,Integer> sorted_map = new TreeMap<EnumFlora,Integer>(bvc);*/
		
		
		
		return null;
		
	}
	
	
	
	public int getRandomQuantity(Random random) {
		return (random.nextInt(maxQuantity) + random.nextInt(maxQuantity) + random.nextInt(maxQuantity)) / 3;
	}
	
	
	
	
	
	static class ValueComparator implements Comparator<EnumFlowerGroup> {

	    Map<EnumFlowerGroup, Integer> base;
	    
	    public ValueComparator(Map<EnumFlowerGroup, Integer> base) {
	        this.base = base;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with equals.    
	    public int compare(EnumFlowerGroup a, EnumFlowerGroup b) {
	        if (base.get(a) >= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
	    }
	}
	
	
}
