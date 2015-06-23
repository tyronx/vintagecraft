package at.tyron.vintagecraft.Block.Mechanics;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerNetworkNode;
import at.tyron.vintagecraft.World.MechanicalNetwork;

public class BlockGrindstone extends BlockMechanicalVC  {

	protected BlockGrindstone(Material materialIn) {
		super(materialIn);
	}

	@Override
	public String getSubType(ItemStack stack) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		// TODO Auto-generated method stub
		return null;
	}

}
