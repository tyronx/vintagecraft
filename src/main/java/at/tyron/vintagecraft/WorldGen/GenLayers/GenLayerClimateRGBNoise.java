package at.tyron.vintagecraft.WorldGen.GenLayers;

public class GenLayerClimateRGBNoise extends GenLayerVC {

	public GenLayerClimateRGBNoise(long seed) {
		super(seed);
	}
	
	@Override
	public int[] getInts(int xCoord, int zCoord, int sizeX, int sizeZ) {
		int[] cache = new int[sizeX * sizeZ];
		int rain, temp; 
		
		for (int z = 0; z < sizeZ; ++z) {
			for (int x = 0; x < sizeX; ++x) {
				this.initChunkSeed(xCoord + x, zCoord + z);
				
	
				// 1 in 15 chance for really cold areas
				if (nextInt(15) == 0) {
					temp = lumpyInt3(55);
					rain = lumpyInt3(130);
				
				// 1 in 13 chance for deserty areas
				} else if (nextInt(13) == 0) {
					temp = 200 + lumpyInt3(55);
					rain = lumpyInt3(55);
				
				// Otherwise temp mostly around average of 170
				} else {
					temp = lumpyInt3(330);
					rain = lumpyInt3(330 - (255 - temp));
				}
				   
				
				//System.out.println("temp is " + temp + " = " + rain); // (int)(temp/4.25f - 30));
				
				cache[x + z * sizeX] =
						(temp << 16)
						+ (Math.min(255, rain + (int)Math.max(0, (rain-128)*(temp-128)/256f)) << 8)				// high rain + high temp = high fertility, see also  http://www.ctahr.hawaii.edu/mauisoil/a_factor_form.aspx
						+ rain
				;
			}
		}

		return cache;
	}
	
	
	int lumpyInt4(int maxint) { return Math.min(255, (nextInt(maxint) + nextInt(maxint) + nextInt(maxint) + nextInt(maxint)) / 4); }
	
	int lumpyInt3(int maxint) { return Math.min(255, (nextInt(maxint) + nextInt(maxint) + nextInt(maxint)) / 3); }
	
	
	
	
	// http://www.covingtoninnovations.com/mc/research/Covington-Paper-HowToMakeALumpyRNG.pdf
	int lumpyrand(int bmin, int bmax, int rmin, int rmax, int n) {
		// Generalized random number generator;
		// sum of n random variables (usually 3).
		// Bell curve spans bmin<=x<bmax; then,
		// values outside rmin<=x<rmax are rejected.
		int i, u, sum;
		do {
			sum = 0;
			for (i=0; i<n; i++) sum += bmin + (nextInt(255) % (bmax - bmin));
			if (sum < 0) sum -= n-1; /* prevent pileup at 0 */
			u = sum / n;
		
		} while ( ! (rmin <= u && u < rmax) );
		
		return u;
	}
}
