package at.tyron.vintagecraft.WorldGen;

import java.util.Random;

/* A more natural float number generator (nature usually doesn't always grow by the exact same numbers ) */

public class NatFloat {
	public static final float PI = (float)Math.PI;
	
	public float offset = 0f;
	
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
	
	public static NatFloat createDirac(float averagevalue, float variance) {
		return new NatFloat(averagevalue, variance, Distribution.DIRAC);
	}
	
	public static NatFloat create(Distribution distribution, float averagevalue, float variance) {
		return new NatFloat(averagevalue, variance, distribution);
	}


	public NatFloat copyWithOffset(float value) {
		NatFloat copy = new NatFloat(value, value, distribution);
		copy.offset += value;
		return copy;
	}

	
	public NatFloat addOffset(float value) {
		offset += value;
		return this;
	}
	
	public NatFloat setOffset(float offset) {
		this.offset = offset;
		return this;
	}
	
	
	public float nextFloat() {
		return nextFloat(1f);
	}
	
	public float nextFloat(float multiplier) {
		float rnd;
		
		switch (distribution) {
		
		case DIRAC:
			rnd = random.nextFloat() - 0.5f;	
			float value = offset + multiplier * (averagevalue + rnd * 2 * variance);
			
			averagevalue = 0f;
			variance = 0f;
			return value;
		
		case UNIFORM:
			rnd = random.nextFloat() - 0.5f;	
			return offset + multiplier * (averagevalue + rnd * 2 * variance);
		
		case GAUSSIAN:
			rnd = (random.nextFloat() + random.nextFloat() + random.nextFloat())/3;  // Random value out of a gauss curve between 0..1, with 0.5f being most common
			
			// Center gauss curve to 0
			rnd = rnd - 0.5f;
			
			return offset + multiplier * (averagevalue + rnd * 2 * variance);
			
		
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
			
			return offset + multiplier * (averagevalue + rnd * variance);
		
		
		case TRIANGLE:
			rnd = (random.nextFloat() + random.nextFloat())/2;  // Random value out of a triangle curve between 0..1, with 0.5f being most common
			
			// Center curve to 0
			rnd = rnd - 0.5f;
			
			return offset + multiplier * (averagevalue + rnd * 2 * variance);
				
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
		DIRAC
		;
		
	}
	
	
	
}
