package at.tyron.vintagecraft.WorldGen.Village;

import java.util.Random;

import at.tyron.vintagecraft.WorldGen.Helper.NatFloat;

public class NumFloat {
	long seed;
	static Random random = new Random();
	
	public static final float PI = (float)Math.PI;
	public static final float PIHALF = (float)Math.PI/2;

	public float basevalue = 0f;
	public float offset = 0f;
	
	public NumFloat(float value) {
		this.basevalue = value;
	}
	
	public static NumFloat create(float value) {
		return new NumFloat(value);
	}
	
	public void init(long seed) {
		
	}
	
	public void advance(float distance) {
		
	}
	
	public NumFloat addOffset(float value) {
		offset += value;
		return this;
	}
	
	public NumFloat setOffset(float offset) {
		this.offset = offset;
		return this;
	}

	public float nextFloat() {
		return basevalue + offset;
	}
	
	public float nextFloat(float multiplier) {
		return (basevalue + offset) * multiplier;
	}

}
