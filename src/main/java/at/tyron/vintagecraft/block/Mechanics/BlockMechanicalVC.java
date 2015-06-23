package at.tyron.vintagecraft.Block.Mechanics;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerDevice;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAngledGearBox;

public abstract class BlockMechanicalVC extends BlockContainerVC {

	protected BlockMechanicalVC(Material materialIn) {
		super(materialIn);
	}

	@Override
	public String getSubType(ItemStack stack) {
		return null;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof IMechanicalPowerDevice) {
			((IMechanicalPowerDevice)te).onDeviceRemoved(worldIn, pos);
		}

		super.breakBlock(worldIn, pos, state);
	}
	
	
    public boolean isOpaqueCube() {
        return false;
    } 
    public boolean isFullCube() {
    	return false;
    }
    public boolean isVisuallyOpaque() {
        return false;
    }
	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}
    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
    	return true;
    }

}
