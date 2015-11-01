package at.tyron.vintagecraft.Block;

import at.tyron.vintagecraft.VintageCraft;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class BlockClayVessel extends BlockCeramicVessel {

	public BlockClayVessel() {
		super(Material.clay);
		setCreativeTab(VintageCraft.craftedBlocksTab);
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return true;
	}
	
	@Override
	public boolean isFullBlock() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

}
