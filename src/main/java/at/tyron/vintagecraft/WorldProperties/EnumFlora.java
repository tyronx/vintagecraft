package at.tyron.vintagecraft.WorldProperties;

import java.util.*;

import at.tyron.vintagecraft.block.BlockFlowerVC;

public enum EnumFlora {
	// (int mintemp, int maxtemp, int minforest, int maxforest, int minrain, int maxrain, int weight, EnumFlower []variants)
	
	ORANGEMILKWEED   ( 22, 33,   0,  10, 100, 190, 80, new EnumFlower[]{EnumFlower.ORANGEMILKWEED, EnumFlower.ORANGEMILKWEED2}),
	PURPLEMILKWEED   ( 22, 33,   0,  10, 100, 190, 0, new EnumFlower[]{EnumFlower.PURPLEMILKWEED, EnumFlower.PURPLEMILKWEED2}),
	
	CATMINT 	     ( 12, 23,   0,  80, 100, 220, 100, new EnumFlower[]{EnumFlower.CATMINT}),
	CALENDULA        ( 12, 23,   0,  50, 100, 220, 100, new EnumFlower[]{EnumFlower.CALENDULA}),
	CORNFLOWER       ( 12, 23,   0,  50, 100, 220, 100, new EnumFlower[]{EnumFlower.CORNFLOWER, EnumFlower.CORNFLOWER2}),
	
	LILYOFTHEVALLEY  ( 0, 20, 60, 255, 60, 255, 100, new EnumFlower[]{EnumFlower.LILYOFTHEVALLEY, EnumFlower.LILYOFTHEVALLEY2, EnumFlower.LILYOFTHEVALLEY3}),
	
	CLOVER			 ( 0, 19, 60, 255,  90, 255, 100, new EnumFlower[]{EnumFlower.CLOVER}),
	
    GOLDENROD        (15, 28,   0,  10, 120, 190, 0, new EnumFlower[]{EnumFlower.GOLDENROD, EnumFlower.GOLDENROD2, EnumFlower.GOLDENROD3}),
    
    FORGETMENOT		 ( 8, 20, 0, 200, 100, 255, 0, new EnumFlower[]{EnumFlower.FORGETMENOT, EnumFlower.FORGETMENOT2, EnumFlower.FORGETMENOT3, EnumFlower.FORGETMENOT4, EnumFlower.FORGETMENOT5}),
    
    NARCISSUS		 ( 8, 20, 1, 200, 100, 255, 100, new EnumFlower[]{EnumFlower.NARCISSUS, EnumFlower.NARCISSUS2, EnumFlower.NARCISSUS3}),
    
	PURPLETULIP		 ( 8, 20, 1, 200, 100, 255, 0, new EnumFlower[]{EnumFlower.PURPLETULIP, EnumFlower.PURPLETULIP2, EnumFlower.PURPLETULIP3}),
	;
	

	
	int mintemp;
	int maxtemp;
	int minforest;
	int maxforest;
	int minrain;
	int maxrain;
	int weight;
	public EnumFlower[] variants;
		
	private EnumFlora(int mintemp, int maxtemp, int minforest, int maxforest, int minrain, int maxrain, int weight, EnumFlower []variants) {
		this.variants = variants;
		this.mintemp = mintemp;
		this.maxtemp = maxtemp;
		this.minforest = minforest;
		this.maxforest = maxforest;
		this.minrain = minrain;
		this.maxrain = maxrain;
		this.weight = weight;
	}
	
	

	public static EnumFlora getRandomFlowerForClimate(int rainfall, int temperature, int forest, Random random) {
		//ArrayList<Integer> distances = new ArrayList<Integer>();
		HashMap<EnumFlora, Integer> distances = new HashMap<EnumFlora, Integer>();
		
		
		for (EnumFlora flora : EnumFlora.values()) {
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
			) * flora.weight;
			
			distance /= 100;
			distance = Math.max(2, distance);
			
			distances.put(flora, distance);
			
			
			//System.out.println("ok " + distance);
		}
		
		Set<EnumFlora> keys = distances.keySet();
		
		List<EnumFlora> keyList = new ArrayList(keys);
		
		Collections.shuffle(keyList);
		
		
		
		for (EnumFlora flora : keyList) {
			
			if (random.nextInt(distances.get(flora)) == 0) return flora;
		}
		
		
		/*ValueComparator bvc = new ValueComparator(distances);
		TreeMap<EnumFlora,Integer> sorted_map = new TreeMap<EnumFlora,Integer>(bvc);*/
		
		
		
		return null;
		
	}
	
	
	
	
	
	
	
	
	
	static class ValueComparator implements Comparator<EnumFlora> {

	    Map<EnumFlora, Integer> base;
	    
	    public ValueComparator(Map<EnumFlora, Integer> base) {
	        this.base = base;
	    }

	    // Note: this comparator imposes orderings that are inconsistent with equals.    
	    public int compare(EnumFlora a, EnumFlora b) {
	        if (base.get(a) >= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        } // returning 0 would merge keys
	    }
	}
	
	
}
