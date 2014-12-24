package at.tyron.vintagecraft.WorldGen.GenLayers;

public class GenLayerExactZoom extends GenLayerVC {
	int zoomLevel;
		
	public GenLayerExactZoom(long seed, int zoomLevel, GenLayerVC parent) {
		super(seed);
		super.parent = parent;
		this.zoomLevel = zoomLevel;
	}

	@Override
	public int[] getInts(int xPos, int zPos, int sizeX, int sizeZ) {
		sizeX += zoomLevel;
		sizeZ += zoomLevel;
		
		int[] out = new int[sizeX * sizeZ];
		
		int xCoord = xPos / zoomLevel - 1;
		int zCoord = zPos / zoomLevel - 1;
		
		int smallXSize = sizeX / zoomLevel;
		int smallZSize = sizeZ / zoomLevel;
		
		int[] inInts = this.parent.getInts(xCoord, zCoord, smallXSize, smallZSize);
		
		int index, in;
		for (int i = 0; i < inInts.length; i++) {
			int xpos = i % smallXSize;
			int zpos = i / smallXSize;
			
			in = inInts[i];

			index = zoomLevel * xpos + zoomLevel * zpos * sizeX;
			
			for (int j = 0; j < zoomLevel * zoomLevel; j++) {
				out[index + sizeX * (j / zoomLevel) + j % zoomLevel] = in;
			}
		}
		
		
		return cutRightAndBottom(out, sizeX, sizeZ, zoomLevel);
		
	}

}
