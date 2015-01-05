package at.tyron.vintagecraft.WorldGen.GenLayers;

public class GenLayerClampedSubstractAll extends GenLayerVC {
	GenLayerVC targetImage;
	GenLayerVC substractImage;
	
	int mode = 0;
	
	
	public GenLayerClampedSubstractAll(long seed, int rgbselect, int mode, GenLayerVC targetImage, GenLayerVC substractImage) {
		super(seed);
		this.mode = mode;
		this.targetImage = targetImage;
		this.substractImage = substractImage;
	}

	

	@Override
	public int[] getInts(int xCoord, int zCoord, int sizeX, int sizeZ) {
		int[] targetInts = targetImage.getInts(xCoord, zCoord, sizeX, sizeZ);
		int[] substractInts = substractImage.getInts(xCoord, zCoord, sizeX, sizeZ);
		
		int[] resultInts = new int[targetInts.length];
	
		int rt, gt, bt;
		int rs, gs, bs;
		
		for (int i = 0; i < targetInts.length; i++) {
			rt = (targetInts[i] >> 16) & 0xff;
			gt = (targetInts[i] >> 8) & 0xff;
			bt = targetInts[i] & 0xff;

			rs = (substractInts[i] >> 16) & 0xff;
			gs = (substractInts[i] >> 8) & 0xff;
			bs = substractInts[i] & 0xff;
			
		
			if (mode == 0) {
				resultInts[i] = 
					Math.min(255, Math.max(0,  bt - bs))
					+ (Math.min(255, Math.max(0,  gt - gs)) << 8)
					+ (Math.min(255, Math.max(0,  rt - rs)) << 16);
			}
			if (mode == 1) {
				resultInts[i] = 
					Math.min(255, Math.max(0, (int)(((float)bt - bs/1.5f)))*3)
					| Math.min(255, Math.max(0, (int)(((float)gt - gs/1.5f)))*3) << 8
					| Math.min(255, Math.max(0, (int)(((float)rt - rs/1.5f)))*3) << 16;
			}
		}
		
		return resultInts;
	}
}
