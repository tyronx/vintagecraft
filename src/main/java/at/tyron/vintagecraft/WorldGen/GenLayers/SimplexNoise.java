package at.tyron.vintagecraft.WorldGen.GenLayers;

import java.util.Random;

public class SimplexNoise {
	SimplexNoiseOctave[] octaves;
    double[] frequencys;
    double[] amplitudes;

    int quantityOctaves;
    double persistence;
    long seed;

    public SimplexNoise(int quantityOctaves, double persistence, long seed){
        this.persistence = persistence;
        this.seed = seed;
        this.quantityOctaves = quantityOctaves;

        octaves = new SimplexNoiseOctave[quantityOctaves];
        frequencys = new double[quantityOctaves];
        amplitudes = new double[quantityOctaves];

        for(int i = 0; i < quantityOctaves; i++){
            octaves[i] = new SimplexNoiseOctave(seed + i);
            frequencys[i] = Math.pow(2, i);
            amplitudes[i] = Math.pow(persistence, i);
        }
    }


    public double getNoise(double x, double y) {
        double result = 0;

        for (int i = 0; i < octaves.length; i++) {
        	result += octaves[i].noise(x * frequencys[i], y * frequencys[i]) * amplitudes[i];
        }


        return result;
    }

    public double getNoise(double x, double y, double z) {
        double result = 0;

        for(int i = 0; i < octaves.length; i++) {
        	result += octaves[i].noise(x * frequencys[i], y * frequencys[i], z * frequencys[i]) * amplitudes[i];
        }

        return result;

    }
}
