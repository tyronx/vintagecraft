package at.tyron.vintagecraft.Block.Mechanics;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Interfaces.IMechanicalNetworkPowerSourceOrSink;
import at.tyron.vintagecraft.World.MechanicalNetwork;

// Base for wind mills and water wheels
public class BlockBladeBase extends BlockContainerVC implements IMechanicalNetworkPowerSourceOrSink {

	protected BlockBladeBase(Material materialIn) {
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

	@Override
	public int getProducedSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getProducedTorque(int speed) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getConsumedTorque(int speed) {
		// TODO Auto-generated method stub
		return 0;
	}

}
