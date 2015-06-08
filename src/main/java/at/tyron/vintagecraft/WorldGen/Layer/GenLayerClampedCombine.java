package at.tyron.vintagecraft.WorldGen.Layer;

import at.tyron.vintagecraft.WorldGen.GenLayerVC;

public class GenLayerClampedCombine extends GenLayerVC {
	GenLayerVC layer1;
	GenLayerVC layer2;
	
	int mode = 0;
	
	public GenLayerClampedCombine(long seed, GenLayerVC layer1, GenLayerVC layer2, int mode) {
		super(seed);
		this.mode = mode;
		this.layer1 = layer1;
		this.layer2 = layer2;
	}

	

	@Override
	public int[] getInts(int xCoord, int zCoord, int sizeX, int sizeZ) {
		int[] layer1ints = layer1.getInts(xCoord, zCoord, sizeX, sizeZ);
		int[] layer2ints = layer2.getInts(xCoord, zCoord, sizeX, sizeZ);
		int r1,g1,b1, r2,g2,b2;
		
		for (int i = 0; i < layer1ints.length; i++) {
			/*layer2ints[i] =
				Math.min(255, layer2ints[i] >> 16 & 0xff + layer1ints[i] >> 16 & 0xff) |
				Math.min(255, layer2ints[i] >> 8 & 0xff + layer1ints[i] >> 8 & 0xff) |
				Math.min(255, layer2ints[i] & 0xff + layer1ints[i] & 0xff)
			;*/
			if (mode == 0) {
				layer2ints[i] =
					Math.min(255, ((layer2ints[i] >> 16) & 0xff) + ((layer1ints[i] >> 16) & 0xff)) << 16 |
					Math.min(255, ((layer2ints[i] >> 8) & 0xff) + ((layer1ints[i] >> 8) & 0xff)) << 8 |
					Math.min(255, (layer2ints[i] & 0xff) + (layer1ints[i] & 0xff))
				;
			}
			if (mode == 1) {
				r1 = (layer1ints[i] >> 16) & 0xff;
				g1 = (layer1ints[i] >> 8) & 0xff;
				b1 = layer1ints[i] & 0xff;
				
				r2 = (layer2ints[i] >> 16) & 0xff;
				g2 = (layer2ints[i] >> 8) & 0xff;
				b2 = layer2ints[i] & 0xff;
				
				layer2ints[i] =
					(int)Math.min(255, r1 + r2 * 3*(255 - r1)/255f) << 16 |
					(int)Math.min(255, g1 + g2 * 3*(255 - g1)/255f ) << 8 |
					(int)Math.min(255, b1 + b2 * 3*(255 - b1)/255f )
				;
				
			}
					
		}
		
		return layer2ints;
	}}
