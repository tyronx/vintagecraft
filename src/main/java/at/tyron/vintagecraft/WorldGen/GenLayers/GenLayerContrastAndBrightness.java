package at.tyron.vintagecraft.WorldGen.GenLayers;

public class GenLayerContrastAndBrightness extends GenLayerVC {

	GenLayerVC parent;
	float level = 0f;
	int brightness = 0;
	
	public GenLayerContrastAndBrightness(long seed, float level, int brightness, GenLayerVC parent) {
		super(seed);
		this.parent = parent;
		this.level = level;
		this.brightness = brightness;
	}
	

	@Override
	public int[] getInts(int xCoord, int zCoord, int sizeX, int sizeZ) {
		int[] ints = parent.getInts(xCoord, zCoord, sizeX, sizeZ);
		
		for (int i = 0; i < ints.length; i++) {
			ints[i] += Math.min(255, brightness);
			ints[i] = Math.min(255, Math.max(0, ints[i] + (int)((ints[i] - 128) * level))); 
		}
		
		return ints;
	}

}
