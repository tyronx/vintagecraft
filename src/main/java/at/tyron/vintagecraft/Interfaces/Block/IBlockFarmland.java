package at.tyron.vintagecraft.Interfaces.Block;

import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFertility;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IBlockFarmland {

	
	EnumFertility getFertility(World world, BlockPos pos);
}
