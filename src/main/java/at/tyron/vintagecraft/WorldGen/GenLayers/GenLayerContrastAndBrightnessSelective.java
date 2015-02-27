package at.tyron.vintagecraft.WorldGen.GenLayers;

public class GenLayerContrastAndBrightnessSelective extends GenLayerVC {

	GenLayerVC parent;
	float level = 0f;
	int brightness = 0;
	
	int rgbselect = 0;
	
	public GenLayerContrastAndBrightnessSelective(long seed, float level, int brightness, GenLayerVC parent) {
		super(seed);
		this.parent = parent;
		this.level = level;
		this.brightness = brightness;
	}
	

	@Override
	public int[] getInts(int xCoord, int zCoord, int sizeX, int sizeZ) {
		int[] ints = parent.getInts(xCoord, zCoord, sizeX, sizeZ);
		int color;
		
		for (int i = 0; i < ints.length; i++) {
			color = (ints[i] >> rgbselect) & 0xff;
			
			ints[i] += Math.min(255, brightness << rgbselect);
			ints[i] = (ints[i] & ~(0xff << rgbselect)) + (Math.min(255, Math.max(0, color + (int)((color - 128) * level))) << rgbselect); 
		}
		
		return ints;
	}

}
