package at.tyron.vintagecraft.interfaces;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import at.tyron.vintagecraft.WorldProperties.EnumFertility;

public interface IFarmland {

	
	EnumFertility getFertility(World world, BlockPos pos);
}
