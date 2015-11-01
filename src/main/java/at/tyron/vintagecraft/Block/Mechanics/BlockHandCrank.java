package at.tyron.vintagecraft.Block.Mechanics;

import at.tyron.vintagecraft.VintageCraft;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockHandCrank extends BlockMechanicalVC {

	public BlockHandCrank() {
		super(Material.wood);
		setCreativeTab(VintageCraft.mechanicsTab);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isBlockedAllowedAt(World worldIn, BlockPos pos) {
		// TODO Auto-generated method stub
		return false;
	}

}
