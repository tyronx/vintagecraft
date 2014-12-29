package at.tyron.vintagecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockPlanksVC extends BlockLogVC {

	@Override
	public boolean canSustainLeaves(IBlockAccess world, BlockPos pos) {
		return false;
	}

}
