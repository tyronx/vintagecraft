package at.tyron.vintagecraft.WorldGen.GenLayers;

public class GenLayerClampedSubstractSelective extends GenLayerVC {
	GenLayerVC targetImage;
	GenLayerVC substractImage;
	
	int rgbselecttarget = 0;
	int rgbselectsubstract = 0;
	
	int mode = 0;
	
	
	public GenLayerClampedSubstractSelective(long seed, int rgbselect, int mode, GenLayerVC targetImage, GenLayerVC substractImage) {
		this(seed, rgbselect, rgbselect, mode, targetImage, substractImage);
	}

	public GenLayerClampedSubstractSelective(long seed, int rgbselecttarget, int rgbselectsubstract, int mode, GenLayerVC targetImage, GenLayerVC substractImage) {
		super(seed);
		this.mode = mode;
		this.targetImage = targetImage;
		this.substractImage = substractImage;
		this.rgbselecttarget = rgbselecttarget;
		this.rgbselectsubstract = rgbselectsubstract;
	}

	

	@Override
	public int[] getInts(int xCoord, int zCoord, int sizeX, int sizeZ) {
		int[] targetInts = targetImage.getInts(xCoord, zCoord, sizeX, sizeZ);
		int[] substractInts = substractImage.getInts(xCoord, zCoord, sizeX, sizeZ);
		
		int[] resultInts = new int[targetInts.length];
		
		int rgbmask = 0xff << rgbselecttarget;
		int targetcolor, substractcolor;
		
		for (int i = 0; i < targetInts.length; i++) {
			targetcolor = (targetInts[i] >> rgbselecttarget) & 0xff;
			substractcolor = (substractInts[i] >> rgbselectsubstract) & 0xff;
			
			if (mode == 0) resultInts[i] = (targetInts[i] & (~rgbmask)) + (Math.min(255, Math.max(0,  targetcolor - substractcolor)) << rgbselecttarget);
			if (mode == 1) resultInts[i] = Math.min(255, Math.max(0, (int)(((float)targetInts[i] - substractInts[i]/1.5f)))*3);
		}
		
		return resultInts;
	}
}
