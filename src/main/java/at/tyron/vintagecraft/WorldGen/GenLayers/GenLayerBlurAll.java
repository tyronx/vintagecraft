package at.tyron.vintagecraft.WorldGen.GenLayers;

import java.awt.image.Kernel;

import net.minecraft.world.gen.layer.IntCache;

public class GenLayerBlurAll extends GenLayerVC {
	int iterations = 1;
	int radius = 3;

	public GenLayerBlurAll(long seed, int iterations, int radius, GenLayerVC parent) {
		super(seed);
		this.iterations = iterations;
		this.radius = radius;
		this.parent = parent;
	}


	
	@Override
	public int[] getInts(int xCoord, int zCoord, int sizeX, int sizeZ) {
		int margin = 10;
		
		xCoord -= margin;
		zCoord -= margin;
		sizeX += 2*margin;
		sizeZ += 2*margin;

		int[] inInts = this.parent.getInts(xCoord, zCoord, sizeX, sizeZ);
		int[] outInts = IntCache.getIntCache(inInts.length);
		
		Kernel kernel = makeKernel(radius);
		
		for (int i = 0; i < iterations; i++) {
			gaussBlurGrayScale(kernel, inInts, outInts, sizeX, sizeZ);
			gaussBlurGrayScale(kernel, outInts, inInts, sizeZ, sizeX);
		}
		
		return cutMargins(inInts, sizeX, sizeZ, margin);
	}
	
	
	
	private int clamped(int x) {
		return Math.max(0, Math.min(x, 255));
	}
	
	
	
	private void gaussBlurGrayScale(Kernel kernel, int[] inPixels, int[] outPixels, int width, int height) {
		float[] matrix = kernel.getKernelData(null);
		int cols = kernel.getWidth();
		int cols2 = cols/2;

		for (int y = 0; y < height; y++) {
			int index = y;
			int ioffset = y*width;
			
			for (int x = 0; x < width; x++) {
				float r = 0, g = 0, b = 0/*, a = 0*/;
				int moffset = cols2;
				
				for (int col = -cols2; col <= cols2; col++) {
					float f = matrix[moffset+col];

					if (f != 0) {
						int ix = x + col;
						
						if (ix < 0) {
							ix = 0 ;
						} else if ( ix >= width) {
							ix = width - 1;
						}
					
						int rgb = inPixels[ioffset+ix];
						//a += f * ((rgb >> 24) & 0xff);
						r += f * ((rgb >> 16) & 0xff);
						g += f * ((rgb >> 8) & 0xff);
						b += f * (rgb & 0xff);
					}
				}
				
				//int ia = alpha ? clamped((int)(a+0.5)) : 0xff;
				int ir = clamped((int)(r+0.5));
				int ig = clamped((int)(g+0.5));
				int ib = clamped((int)(b+0.5));
				outPixels[index] = (ir << 16) | (ig << 8) | ib;
				
                index += height;
			}
		}
	}
	
	
	
	
	
	
	/**
	 * Make a Gaussian blur kernel.
	 */
	public static Kernel makeKernel(float radius) {
		int r = (int)Math.ceil(radius);
		int rows = r*2+1;
		float[] matrix = new float[rows];
		float sigma = radius/3;
		float sigma22 = 2*sigma*sigma;
		float sigmaPi2 = (float) (2*Math.PI*sigma);
		float sqrtSigmaPi2 = (float)Math.sqrt(sigmaPi2);
		float radius2 = radius*radius;
		float total = 0;
		int index = 0;
		
		for (int row = -r; row <= r; row++) {
			float distance = row*row;
			if (distance > radius2) {
				matrix[index] = 0;
			} else {
				matrix[index] = (float)Math.exp(-(distance)/sigma22) / sqrtSigmaPi2;
			}
			total += matrix[index];
			index++;
		}
		for (int i = 0; i < rows; i++) {
			matrix[i] /= total;
		}

		return new Kernel(rows, 1, matrix);
	}


}
