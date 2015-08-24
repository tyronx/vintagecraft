package at.tyron.vintagecraft.WorldGen.Village;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import net.minecraft.util.MathHelper;

/* The same as NatFloat but this number wanders based on the given sequence */
// Sequence has to be between 0 and 1000

public class EvolvingFloat extends NatFloat {
	EnumTransformFunction function;
	float amplitude;
	float frequency;
	
	NumFloat initalfloat;
	float firstvalue;
	
	float sequence;

	public float smoothing = 4.0f; // for perlin
	public float perlinzoom;
	
	public EvolvingFloat(NumFloat initalvalue, EnumTransformFunction function, float amplitude) {
		super(0f, 0f, Distribution.DIRAC);
		this.initalfloat = initalvalue;
		this.amplitude = amplitude;
		this.frequency = 1f;
		this.function = function;
	}

	public EvolvingFloat(NumFloat initalvalue, EnumTransformFunction function, float amplitude, float frequency) {
		super(0f, 0f, Distribution.DIRAC);
		this.initalfloat = initalvalue;
		this.amplitude = amplitude;
		this.frequency = frequency;
		this.function = function;
	}

	
	public static EvolvingFloat createLinear(NumFloat initalvalue, float amplitude) {
		return new EvolvingFloat(initalvalue, EnumTransformFunction.LINEAR, amplitude);
	}
	public static EvolvingFloat createLinearReduce(NumFloat initalvalue, float amplitude) {
		return new EvolvingFloat(initalvalue, EnumTransformFunction.LINEARREDUCE, amplitude);
	}
	public static EvolvingFloat createLinearIncrease(NumFloat initalvalue, float amplitude) {
		return new EvolvingFloat(initalvalue, EnumTransformFunction.LINEARINCREASE, amplitude);
	}
	public static EvolvingFloat createQuadratic(NumFloat initalvalue, float amplitude) {
		return new EvolvingFloat(initalvalue, EnumTransformFunction.QUADRATIC, amplitude);
	}
	public static EvolvingFloat createInverseQuadratic(NumFloat initalvalue, float amplitude) {
		return new EvolvingFloat(initalvalue, EnumTransformFunction.INVERSEQUADRATIC, amplitude);
	}
	public static EvolvingFloat createSin(NumFloat initalvalue, float amplitude, float frequency) {
		return new EvolvingFloat(initalvalue, EnumTransformFunction.SINUS, amplitude, frequency);
	}
	public static EvolvingFloat createCos(NumFloat initalvalue, float amplitude, float frequency) {
		return new EvolvingFloat(initalvalue, EnumTransformFunction.COSINUS, amplitude, frequency);
	}
	public static EvolvingFloat createPerlin(NumFloat initalvalue, float persistence, float octaves, float perlinzoom) {
		EvolvingFloat num = new EvolvingFloat(initalvalue, EnumTransformFunction.PERLIN, persistence, octaves);
		num.perlinzoom = perlinzoom;

		return num;
	}

	
	
	
	
	
	public void init(long seed) {
		firstvalue = initalfloat.nextFloat(0f);
		random = new Random(seed);
		this.seed = seed;
		Collections.shuffle(Arrays.asList(p));
	}
	
	@Override
	public void advance(float distance) {
		sequence += distance;
	}

	
	public float nextFloat() {
		return nextFloat(1f);
	}
	
	
	public float nextFloat(float multiplier) {
		float result = firstvalue;
		
		switch (function) {
			case LINEAR: result = firstvalue + amplitude * sequence; break;
			case INVERSELINEAR: result = firstvalue - amplitude * sequence; break;
			
			case LINEARREDUCE: result = firstvalue - firstvalue/Math.abs(firstvalue) * amplitude * sequence; break;
			case LINEARINCREASE: result = firstvalue + firstvalue/Math.abs(firstvalue) * amplitude * sequence; break;
			
			case QUADRATIC: result = firstvalue + (amplitude * sequence) * (amplitude * sequence); break;
			case INVERSEQUADRATIC: result = firstvalue + 1f/(amplitude * (sequence+1)); break;
			
			case SINUS: result = firstvalue + amplitude * MathHelper.sin(frequency * sequence); break;
			case COSINUS: result = firstvalue + amplitude *MathHelper.cos(frequency * sequence); break;
			
			// amplitude is persistence
			case PERLIN: result = firstvalue + (float)noise(sequence * perlinzoom); break;
				
		}
		
		return offset + result;
	}



	public static enum EnumTransformFunction {
		LINEAR,
		LINEARREDUCE,
		LINEARINCREASE,
		QUADRATIC,
		//EXPONENTIAL,
		INVERSELINEAR,
		INVERSEQUADRATIC,
		//INVERSEEXPONENTNIAL,
		SINUS,
		COSINUS,
		PERLIN
	}
	
	
	


	

	static double lerp(double t, double a, double b) { return a + t * (b - a); }
	static double fade(double t) { return t * t * t * (t * (t * 6 - 15) + 10); }
	static double grad(int hash, double x, double y, double z) {
	  int h = hash & 15;                      // CONVERT LO 4 BITS OF HASH CODE
	  double u = h<8 ? x : y,                 // INTO 12 GRADIENT DIRECTIONS.
			 v = h<4 ? y : h==12||h==14 ? x : z;
	  return ((h&1) == 0 ? u : -u) + ((h&2) == 0 ? v : -v);
	}

	static public double noise(double x) {
	   int X = (int)Math.floor(x) & 255;
	   x -= Math.floor(x);
	   double u = fade(x);
	   return lerp(u, grad(p[X  ], x), grad(p[X+1], x-1));
	}

	static double grad(int hash, double x) {
	   return ((hash&1) == 0 ? x : -x);
	}

	static final Integer p[] = new Integer[512], permutation[] = { 151,160,137,91,90,15,
		131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
		190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
		88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
		77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
		102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
		135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
		5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
		223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
		129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
		251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
		49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
		138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180
	};
		
	static { for (int i=0; i < 256 ; i++) p[256+i] = p[i] = permutation[i]; }

}
