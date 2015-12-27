package at.tyron.vintagecraft.Interfaces.Tileentity;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IStrongHeatSource {

	boolean isBurning();

	void setState(boolean burning, World worldObj, BlockPos pos);
	
}
