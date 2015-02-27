package at.tyron.vintagecraft.WorldGen;

import java.util.Random;

/* A more natural float number (nature usually doesn't always grow by the exact same numbers ) */
public class NatFloat {
	public static final float PI = (float)Math.PI;
	
	public float averagevalue;
	public float variance;
	public Distribution distribution;
	
	
	static Random random = new Random();
	
	public NatFloat(float averagevalue, float variance, Distribution distribution) {
		this.averagevalue = averagevalue;
		this.variance = variance;
		this.distribution = distribution;
	}
	
	
	public static NatFloat createUniform(float averagevalue, float variance) {
		return new NatFloat(averagevalue, variance, Distribution.UNIFORM);
	}
	
	public static NatFloat createGauss(float averagevalue, float variance) {
		return new NatFloat(averagevalue, variance, Distribution.GAUSSIAN);
	}

	
	public static NatFloat createInvGauss(float averagevalue, float variance) {
		return new NatFloat(averagevalue, variance, Distribution.INVERSEGAUSSIAN);
	}

	
	public static NatFloat createTri(float averagevalue, float variance) {
		return new NatFloat(averagevalue, variance, Distribution.TRIANGLE);
	}

	
	public float nextFloat() {
		return nextFloat(1f);
	}
	
	public float nextFloat(float multiplier) {
		float rnd;
		
		switch (distribution) {
		
		case UNIFORM:
			rnd = random.nextFloat() - 0.5f;	
			return multiplier * (averagevalue + rnd * 2 * variance);
		
		case GAUSSIAN:
			rnd = (random.nextFloat() + random.nextFloat() + random.nextFloat())/3;  // Random value out of a gauss curve between 0..1, with 0.5f being most common
			
			// Center gauss curve to 0
			rnd = rnd - 0.5f;
			
			return multiplier * (averagevalue + rnd * 2 * variance);
			
		
		case INVERSEGAUSSIAN:
			rnd = (random.nextFloat() + random.nextFloat() + random.nextFloat())/3;  // Random value out of a gauss curve between 0..1, with 0.5f being most common
			
			// Flip curve
			if (rnd > 0.5f) {
				rnd -= 0.5f;
			} else {
				rnd += 0.5f;
			}
			
			// Center gauss curve to 0
			rnd = rnd - 0.5f;
			
			return multiplier * (averagevalue + rnd * variance);
		
		
		case TRIANGLE:
			rnd = (random.nextFloat() + random.nextFloat())/2;  // Random value out of a triangle curve between 0..1, with 0.5f being most common
			
			// Center gauss curve to 0
			rnd = rnd - 0.5f;
			
			return multiplier * (averagevalue + rnd * 2 * variance);
				
		default:
			return 0f;
		}
		
	}
	
	
	
	
	enum Distribution {
		UNIFORM,
		GAUSSIAN,
		INVERSEGAUSSIAN,
		TRIANGLE,
		POISSON,
		;
		
	}
	
	
	
}
