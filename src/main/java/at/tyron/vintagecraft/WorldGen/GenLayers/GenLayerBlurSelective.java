package at.tyron.vintagecraft.WorldGen.GenLayers;

import java.awt.image.Kernel;

import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerBlurSelective extends GenLayerVC {
	int iterations = 1;
	int radius = 3;
	boolean motionBlur = false;
	// which color to blur
	// 0 = blue
	// 8 = green
	// 16 = red
	public static int rgbselect = 0;

	
	public GenLayerBlurSelective(long seed, int iterations, int radius, boolean motionBlur, int rgbselect, GenLayerVC parent) {
		super(seed);
		this.rgbselect = rgbselect;
		super.parent = parent;
		this.radius = radius;
		this.iterations = iterations;
		this.motionBlur = motionBlur;
	}
	
	
	public GenLayerBlurSelective(long seed, int iterations, int radius, boolean motionBlur, GenLayerVC parent) {
		this (seed, iterations, radius, motionBlur, 0, parent);
	}
	
	
	public GenLayerBlurSelective(long seed, GenLayerVC parent) {
		this (seed, 3, 3, false, parent);
	}
	
	
	

	@Override
	public int[] getInts(int xCoord, int zCoord, int sizeX, int sizeZ) {
		int margin = 10;
		
		xCoord -= margin;
		zCoord -= margin;
		sizeX += 2*margin;
		sizeZ += 2*margin;

		int[] inInts = this.parent.getInts(xCoord, zCoord, sizeX, sizeZ);
		//int[] outInts = new int[inInts.length];
		int[] outInts = IntCache.getIntCache(inInts.length);
		
		Kernel kernel = makeKernel(radius);
		
		for (int i = 0; i < iterations; i++) {
			gaussBlurGrayScale(kernel, inInts, outInts, sizeX, sizeZ);
			if (motionBlur) {
				kernel = makeKernel(1);
			}
			gaussBlurGrayScale(kernel, outInts, inInts, sizeZ, sizeX);
			if (motionBlur) {
				kernel = makeKernel(radius);
			}
		}
		
		return cutMargins(inInts, sizeX, sizeZ, margin);
	}
	
	
	

	
	
	private int boundBy(int x, int min, int max) {
		return Math.max(min, Math.min(x, max));
	}
	
	
	
	private void gaussBlurGrayScale(Kernel kernel, int[] inPixels, int[] outPixels, int width, int height) {
		float[] matrix = kernel.getKernelData(null);
		int cols = kernel.getWidth();
		int cols2 = cols/2;

		int colorClearMask = ~(0xff << rgbselect);
				
		
		
		for (int y = 0; y < height; y++) {
			int index = y;
			int ioffset = y*width;
			
			for (int x = 0; x < width; x++) {
				float gray = 0;
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
					
						gray +=  f * ((inPixels[ioffset+ix] >> rgbselect) & 0xff);
					}
				}
				
				outPixels[index] = (inPixels[index] & colorClearMask) + (boundBy((int)(gray + 0.5), 0, 255) << rgbselect);
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
