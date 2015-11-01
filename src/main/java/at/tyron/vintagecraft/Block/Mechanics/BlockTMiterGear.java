package at.tyron.vintagecraft.Block.Mechanics;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockTMiterGear extends BlockMechanicalVC {

	public BlockTMiterGear() {
		super(Material.wood);
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
