package at.tyron.vintagecraft.WorldGen.GenLayers;

import at.tyron.vintagecraft.interfaces.IGenLayerSupplier;

public class GenLayerWeightedNoise extends GenLayerNoise {
	IGenLayerSupplier[] genlayersuppliers;
	/*int[] weights;
	int[] colors;*/
	
	int weightsum;

	public GenLayerWeightedNoise(long seed, IGenLayerSupplier[] genlayersupplier) {
		super(seed);
		
		for (int i = 0; i < genlayersupplier.length; i++) {
			weightsum += genlayersupplier[i].getWeight();
		}
		
		this.genlayersuppliers = genlayersupplier;
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
				
				for (int i = 0; i < genlayersuppliers.length; i++) {
					sum += genlayersuppliers[i].getWeight();
					
					if (rnd < sum) {
						cache[x + z * sizeX] =
								((genlayersuppliers[i].getDepthMin() + nextInt(1 + genlayersuppliers[i].getDepthMax() - genlayersuppliers[i].getDepthMin())) << 16)
								+ genlayersuppliers[i].getColor(); //+ (i * 255) / (weights.length-1);
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
