package at.tyron.vintagecraft.WorldGen;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;

public class VCWorldType extends WorldType {
	public VCWorldType(String name) {
		super(name);
	}

	public VCWorldType(int i, String par2Str) {
		super(i, par2Str);
	}
	
	public static VCWorldType DEFAULT;
	
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
		return new VCWorldChunkManager(world);
	}
	
	
	@Override
	public int getMinimumSpawnHeight(World world) {
		return 120;
	}
	
	
}
