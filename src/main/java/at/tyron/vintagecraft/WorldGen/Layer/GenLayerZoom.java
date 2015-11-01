package at.tyron.vintagecraft.WorldGen.Layer;

import at.tyron.vintagecraft.WorldGen.GenLayerVC;

public class GenLayerZoom extends GenLayerVC {

	public GenLayerZoom(long seed, GenLayerVC par3GenLayer)
	{
		super(seed);
		super.parent = par3GenLayer;
	}

	
	@Override
	public int[] getInts(int xPos, int zPos, int xSize, int zSize) {
		int xCoord = xPos >> 1;
		int zCoord = zPos >> 1;
		int newXSize = (xSize >> 1) + 2;
		int newZSize = (zSize >> 1) + 2;
		
		int[] parentInts = parent.getInts(xCoord, zCoord, newXSize, newZSize);
		
		int outXsize = newXSize - 1 << 1;
		int outZSize = newZSize - 1 << 1;
		int[] out = new int[outXsize * outZSize];
		int index;

		for (int z = 0; z < newZSize - 1; ++z) {
			index = (z << 1) * outXsize;
						
			for (int x = 0; x < newXSize - 1; ++x) {
				this.initChunkSeed(x + xCoord << 1, z + zCoord << 1);

				int valTopLeft = parentInts[x + (z + 0) * newXSize];
				int valTopRight = parentInts[x + 1 + (z + 0) * newXSize];
				int valBottomLeft = parentInts[x + (z + 1) * newXSize];
				int vaBottomRight = parentInts[x + 1 + (z + 1) * newXSize];
				
				out[index] = valTopLeft;
				out[index + outXsize] = selectRandom(valTopLeft, valBottomLeft);
				out[index + 1] = selectRandom(valTopLeft, valTopRight);
				out[index + 1 + outXsize] = selectRandom(valTopLeft, valTopRight, valBottomLeft, vaBottomRight);
				
				index += 2;
			}
		}

		int[] outCache = new int[xSize * zSize];

		for (int z = 0; z < zSize; ++z) {
			int srcPos = (z + (zPos & 1)) * outXsize + (xPos & 1);
			
			// src, srcpos, dest, despos, length
			System.arraycopy(out, srcPos, outCache, z * xSize, xSize);
		}

		return outCache;
	}

	
	
	protected int selectRandom(int ... numbers) {
        return numbers[nextInt(numbers.length)];
    }
	
  

	public static GenLayerVC magnify(long seed, GenLayerVC parent, int zoomLevels) {
		GenLayerVC genlayer = parent;
		
		for (int i = 0; i < zoomLevels; ++i)
			genlayer = new GenLayerZoom(seed + i, (GenLayerVC) genlayer);
		
		return genlayer;
	}
}
