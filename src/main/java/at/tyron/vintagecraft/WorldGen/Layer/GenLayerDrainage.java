package at.tyron.vintagecraft.WorldGen.Layer;

import java.util.Hashtable;

import net.minecraft.util.MathHelper;

public class GenLayerDrainage {
	public static int []drainageBrush = null;

	static {
		createDrainageBrush();
	}
	
	

	
	// Expects heightmaps from 9 chunks 
	public static int[] getDrainageMap(int [][]multichunkheightmap) {
		int size = 48;
		int[] drainagemap = new int[size * size];

		// We check up to 8 blocks into the neighbouring chunks
		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				int chunkIndex = (z/16) * 3 + x / 16;
				int[] heightmap = multichunkheightmap[chunkIndex];
				int cx = x % 16;
				int cz = z % 16;
				
				int yPos = heightmap[cz * 16 + cx];
				
				
				boolean isNeighbourLower = 
					(cx > 0 && heightmap[cz*16 + cx - 1] < yPos) ||
					(cz < 16 && cx < 16 - 1 && heightmap[cz*16 + cx + 1] < yPos) ||
					(cz > 0 && heightmap[cz*16 + cx - 16] < yPos) ||
					(cz < 16 - 1 && cx < 16 && heightmap[cz*16 + cx + 16] < yPos)
				;
				
				if (isNeighbourLower) {
					for (int dx = -3; dx <= 3; dx++) {
						for (int dz = -3; dz <= 3; dz++) {
							int xcoord = x - dx;
							int zcoord = z - dz;
							
							if (xcoord >= 0 && xcoord < size && zcoord >= 0 && zcoord < size) {
								int chunkIndexNeib = (zcoord/16) * 3 + xcoord / 16; 
								int neibhgt = multichunkheightmap[chunkIndexNeib][(zcoord % 16) * 16 + (xcoord % 16)];
								
								// TODO: Maybe take into account higher elevated areas drain even more?
								if (neibhgt >= yPos) {
									drainagemap[zcoord * size + xcoord] += MathHelper.clamp_int(drainageBrush[(dx+3) * 3 + (dz+3)], 0, 255);
								}
							}
						}
					}
				}
			}
		}
		
		return drainagemap;
	}
	

	
	/*
	 * 1. Draw all black
	 * 2. Iterate through each heightmap pixel
	 * 3. If any neighbour pixel is lower. Increase lightnes of the surrounding area 
	 *    for all pixels with equal or higher elevation. 
	 *    Linear decrease over distance
	 */
	public static int[] genDrainageMap(int size, int []heightmap) {
		int[] drainagemap = new int[size*size];
		
		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				int hgt = heightmap[z*size + x];
				
				boolean isNeighbourLower = 
					(x > 0 && heightmap[z*size + x - 1] < hgt) ||
					(z < size && x < size-1 && heightmap[z*size + x + 1] < hgt) ||
					(z > 0 && heightmap[z*size + x  - size] < hgt) ||
					(z < size-1 && x < size && heightmap[z*size + x + size] < hgt)
				;
				
				if (isNeighbourLower) {
					for (int dx = -3; dx <= 3; dx++) {
						for (int dz = -3; dz <= 3; dz++) {
							int xcoord = x-dx;
							int zcoord = z-dz;
							
							if (xcoord >= 0 && xcoord < size && zcoord >= 0 && zcoord < size) {
								int neibhgt = heightmap[zcoord * size + xcoord];
								
								if (neibhgt >= hgt) {
									drainagemap[zcoord * size + xcoord] += MathHelper.clamp_int(drainageBrush[(dx+3) * 3 + (dz+3)], 0, 255);
								}
							}
						}
					}					
				}
			}
		}
		
		return drainagemap;
	}
	
	
	
	public static void createDrainageBrush() {
		drainageBrush = new int[6*6];
		int start = 40;
		int loss = 10; 
				
		for (int x = -3; x <= 3; x++) {
			for (int z = -3; z <= 3; z++) {
				int dist = (int) (loss * MathHelper.sqrt_float(x*x + z*z));		
				drainageBrush[(x+3) * 3 + (z+3)] = start - dist;
			}
		}
	}
	

}
