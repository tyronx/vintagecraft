package at.tyron.vintagecraft.WorldGen.GenLayers;

public class GenLayerCopyColor extends GenLayerVC {
	int rgbselect_from;
	int rgbselect_to;
	
	public GenLayerCopyColor(int rgbselect_from, int rgbselect_to, GenLayerVC parent) {
		super(0);
		this.parent = parent;
		this.rgbselect_from = rgbselect_from;
		this.rgbselect_to = rgbselect_to;
	}
	
	@Override
	public int[] getInts(int xCoord, int zCoord, int sizeX, int sizeZ) {
		int[] cache = parent.getInts(xCoord, zCoord, sizeX, sizeZ);
		
		for (int i = 0; i < cache.length; i++) {
			cache[i] =
				// Delete old color value
				(cache[i] & ~(0xff << rgbselect_to))
				+
				// Copy new
				(((cache[i] >> rgbselect_from) & 0xff) << rgbselect_to) 
			;
					
		}
		
		return cache;
		
	}

}
