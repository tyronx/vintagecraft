package at.tyron.vintagecraft.WorldGen.GenLayers;

public class GenLayerContrastAndBrightnessAll extends GenLayerVC {

	GenLayerVC parent;
	float level = 0f;
	int brightness = 0;
	
	public GenLayerContrastAndBrightnessAll(long seed, float level, int brightness, GenLayerVC parent) {
		super(seed);
		this.parent = parent;
		this.level = level;
		this.brightness = brightness;
	}
	

	@Override
	public int[] getInts(int xCoord, int zCoord, int sizeX, int sizeZ) {
		int[] ints = parent.getInts(xCoord, zCoord, sizeX, sizeZ);
		int r, g, b;
				
		for (int i = 0; i < ints.length; i++) {
			r = (ints[i] >> 16) & 0xff;
			g = (ints[i] >> 8) & 0xff;
			b = ints[i] & 0xff;
			
			r = Math.min(255, r + brightness);
			g = Math.min(255, g + brightness);
			b = Math.min(255, b + brightness);
			
			ints[i] = 
				(Math.min(255, Math.max(0, b + (int)((b - 128) * level))))
				+ (Math.min(255, Math.max(0, g + (int)((g - 128) * level))) << 8)
				+ (Math.min(255, Math.max(0, r + (int)((r - 128) * level))) << 16)
			;
			
		}
		
		return ints;
	}

}
