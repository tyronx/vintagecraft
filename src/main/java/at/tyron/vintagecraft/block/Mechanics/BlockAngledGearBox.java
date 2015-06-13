package at.tyron.vintagecraft.Block.Mechanics;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Interfaces.IMechanicalNetworkDevice;
import at.tyron.vintagecraft.World.MechanicalNetwork;

public class BlockAngledGearBox extends BlockContainerVC implements IMechanicalNetworkDevice {

	protected BlockAngledGearBox(Material materialIn) {
		super(materialIn);
		// TODO Auto-generated constructor stub
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

	@Override
	public boolean canAcceptPowerAt(EnumFacing facing) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canSupplyPowerAt(EnumFacing facing) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MechanicalNetwork getNetwork(EnumFacing facing) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlockPos getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

}
