package at.tyron.vintagecraft.WorldGen.GenLayers;

import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.IGenLayerSupplier;

public class GenLayerWeightedNoise extends GenLayerNoise {
	IGenLayerSupplier[] deposits;
	/*int[] weights;
	int[] colors;*/
	
	int weightsum;

	public GenLayerWeightedNoise(long seed, IGenLayerSupplier[] deposits) {
		super(seed);
		
		//this.weights = new int[deposits.length];
		
		for (int i = 0; i < deposits.length; i++) {
			//weights[i] = deposits[i].getWeight();
			weightsum += deposits[i].getWeight();
			
			//System.out.println(i + ": " + weights[i]);
		}
		
		this.deposits = deposits;
	}
	
	@Override
	public int[] getInts(int xCoord, int zCoord, int sizeX, int sizeZ) {
		int[] cache = new int[sizeX * sizeZ];
		int rnd, sum=0;
		
		for (int z = 0; z < sizeZ; ++z) {
			for (int x = 0; x < sizeX; ++x) {
				this.initChunkSeed(xCoord + x, zCoord + z);
				
				rnd = nextInt(weightsum);
				sum = 0;
				
				//cache[x + z * sizeX] = nextInt(255) << 8;
				
				for (int i = 0; i < deposits.length; i++) {
					sum += deposits[i].getWeight();
					
					if (rnd < sum) {
						cache[x + z * sizeX] =
								((deposits[i].getDepthMin() + nextInt(1 + deposits[i].getDepthMax() - deposits[i].getDepthMin())) << 16)
								+ deposits[i].getColor(); //+ (i * 255) / (weights.length-1);
						break;
					}
				}
				
				
				
				
			}
		}

		return cache;
	}
}



/*

	int[] depositDepths = new int[deposits.length];

for (int i = 0; i < deposits.length; i++) {
	depositDepths[i] = deposits[i].minDepth + random.nextInt(deposits[i].maxDepth - deposits[i].minDepth);
}

*/
