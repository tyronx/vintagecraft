package at.tyron.vintagecraft.WorldGen.GenLayers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import at.tyron.vintagecraft.WorldProperties.EnumCrustLayerGroup;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;

public class GenRockLayers {
	SimplexNoise noisegenLow;
	SimplexNoise noisegenHigh;
	int amplitude = 0;
	int offset = 0;
	
	HashMap<Integer, EnumRockType> color2Rock = new HashMap<Integer, EnumRockType>(255);
	
	public GenRockLayers(long seed) {
		int octaves = 6;
		float persistence = 0.6f;
		amplitude = 128;
				
		noisegenLow = new SimplexNoise(octaves, persistence, seed);
		
		int currentcolor = 0;
		
		ArrayList<EnumRockType> rocktypeslist = new ArrayList<EnumRockType>();
		
		for (EnumRockType rocktype : EnumRockType.values()) {
			if (rocktype.group != EnumCrustLayerGroup.SEDIMENTARY && rocktype.group != EnumCrustLayerGroup.SPECIAL) {
				rocktypeslist.add(rocktype);
			}
		}
		EnumRockType[] rocktypes = rocktypeslist.toArray(new EnumRockType[0]);
		

		int colorsperrock = (int) (255f / rocktypes.length);
		
		int[] colors = new int[rocktypes.length * colorsperrock];
		
		for (int i = 0; i < colors.length; i++) {
			colors[i] = 255 - currentcolor++;
		}
		shuffleArray(colors);
		
		int k = 0;
		for (EnumRockType rocktype : rocktypes) {
//			System.out.println(rocktype);
			for (int j = 0; j < colorsperrock; j++) {
				color2Rock.put(colors[k++], rocktype);
			}
		}
		
		while(currentcolor <= 255) {
			color2Rock.put(255 - currentcolor++, EnumRockType.GRANITE);
		}
	}
	
	
	
	public EnumRockType getRockType(int xCoord, int depth, int zCoord, int age) {
		int value = Math.max(0, Math.min(255, offset + (int)(amplitude * (1f + noisegenLow.getNoise(xCoord / 524288.0, depth / 65536.0, zCoord / 524288.0))))); //65536   32768
		
		EnumRockType rock = color2Rock.get(value);
		
		//if (depth == 3) System.out.println(age);
		
		if (depth + age/10 < 20) {
			//System.out.println("get sed");
			return EnumRockType.getSedimentary(rock, age);
		}
		
		return rock;
	}
	
	
	
	  // Implementing Fisher–Yates shuffle
	  static void shuffleArray(int[] ar)
	  {
	    Random rnd = new Random();
	    for (int i = ar.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      // Simple swap
	      int a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	  }
	
}
