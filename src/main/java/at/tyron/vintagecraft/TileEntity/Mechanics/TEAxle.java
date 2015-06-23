package at.tyron.vintagecraft.TileEntity.Mechanics;

import at.tyron.vintagecraft.Interfaces.IMechanicalPowerDevice;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerNetworkRelay;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerNetworkNode;
import at.tyron.vintagecraft.TileEntity.NetworkTileEntity;
import at.tyron.vintagecraft.World.MechanicalNetwork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TEAxle extends TEMechanicalNetworkDeviceBase implements IMechanicalPowerNetworkRelay {
	
	
	public boolean refreshModel;
	//public int angle = 0;

	
	public boolean connectedLeft = false;
	public boolean connectedRight = false;
	
	public TEAxle() {
		orientation = EnumFacing.NORTH;
	}
	
	
	@Override
	public void onDevicePlaced(World world, BlockPos pos, EnumFacing facing) {
		orientation = facing;
		super.onDevicePlaced(world, pos, facing);
		
		
		recheckNeighbours();
	}

	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		connectedLeft = compound.getBoolean("connectedLeft");
		connectedRight = compound.getBoolean("connectedRight");
	}
	
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		
		compound.setBoolean("connectedLeft", connectedLeft);
		compound.setBoolean("connectedRight", connectedRight);
	}



	public void recheckNeighbours() {
		boolean oldLeft = connectedLeft;
		boolean oldRight = connectedRight;
		
		connectedLeft = isConnectedAt(orientation);
		connectedRight = isConnectedAt(orientation.getOpposite());
		
		if (oldLeft != connectedLeft || oldRight != connectedRight) worldObj.markBlockForUpdate(pos);
	}


	@Override
	public boolean hasConnectorAt(EnumFacing facing) {
		return facing == orientation || facing.getOpposite() == orientation;
	}


}
