package at.tyron.vintagecraft.WorldGen.GenLayers;

public class GenLayerSubstract extends GenLayerVC {
	GenLayerVC targetImage;
	GenLayerVC substractImage;
	
	public GenLayerSubstract(long seed, GenLayerVC targetImage, GenLayerVC substractImage) {
		super(seed);
		this.targetImage = targetImage;
		this.substractImage = substractImage;
	}
	

	@Override
	public int[] getInts(int xCoord, int zCoord, int sizeX, int sizeZ) {
		int[] targetInts = targetImage.getInts(xCoord, zCoord, sizeX, sizeZ);
		int[] substractInts = substractImage.getInts(xCoord, zCoord, sizeX, sizeZ);
		
		int[] resultInts = new int[targetInts.length];
		
		for (int i = 0; i < targetInts.length; i++) {
			
			/*int unsharpmask = Math.min(255, Math.max(0, targetInts[i] - substractInts[i]));
			
			float lumipercent = unsharpmask / 255f;
			
			float delta = 2000 * lumipercent;
			System.out.println(delta);
			if (delta > 15) {
				resultInts[i] = (int) delta;
			} else {
				resultInts[i] = targetInts[i];
			}
			*/
			

			resultInts[i] = Math.min(255, Math.max(0, (int)(((float)targetInts[i] - substractInts[i]/1.5f)))*3);
		}
		
		return resultInts;
	}
}
