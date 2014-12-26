package at.tyron.vintagecraft.WorldGen;

import at.tyron.vintagecraft.World.VCBiome;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;

public class WorldTypeVC extends WorldType {
	public WorldTypeVC(String name) {
		super(name);
	}

	public WorldTypeVC(int i, String par2Str) {
		super(i, par2Str);
	}
	
	public static WorldTypeVC DEFAULT;
	
	private static final VCBiome[] biomesDefault = {
		VCBiome.ocean,
		VCBiome.HighHills,
		VCBiome.plains,
		VCBiome.HighPlains,
		VCBiome.swampland,
		VCBiome.rollingHills,
		VCBiome.Mountains,
	};
	
	
	
	
	@Override
	public WorldChunkManager getChunkManager(World world) {
		return new WorldChunkManagerVC(world);
	}
	
	
	@Override
	public int getMinimumSpawnHeight(World world) {
		return 120;
	}
	
	
}
