package at.tyron.vintagecraft.Interfaces;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFertility;

public interface IBlockFarmland {

	
	EnumFertility getFertility(World world, BlockPos pos);
}
