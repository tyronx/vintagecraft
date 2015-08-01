package at.tyron.vintagecraft.World;

import java.util.HashMap;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.WorldGen.Noise.Noisy;
import at.tyron.vintagecraft.WorldGen.Noise.SimplexNoise;

public class WindGen {
	private static HashMap<World, WindGen> windgens = new HashMap<World, WindGen>();
	
	SimplexNoise windGen;
	World world;
	
	public static void registerWorld(World world) {
		WindGen gen = new WindGen(world.getSeed(), world);
		windgens.put(world, gen);
	}
	
	public static void unregisterWorld(World world) {
		windgens.remove(world);
	}
	
	public static WindGen getWindGenForWorld(World world) {
		return windgens.get(world);
	}
	
	private WindGen(long seed, World world) {
		int octaves = 6;
		float persistence = 0.7f;
		
		windGen = new SimplexNoise(octaves, persistence, seed);
		
		this.world = world;
	}
	
	
	public double getWindAt(BlockPos pos) {
		return windGen.getNoise(
			pos.getX() / 2048f, 
			VintageCraft.instance.getWorldTime(world) / 2048f, 
			pos.getZ() / 2048f
		);
	}
	
	
}
