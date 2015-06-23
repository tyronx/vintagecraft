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


// This should be a power relay

public class TEAngledGearBox extends TEMechanicalNetworkDeviceBase implements IMechanicalPowerNetworkRelay {
	public EnumFacing[] gearOrientations = new EnumFacing[2];
	public boolean[] direction = new boolean[2];
	
	
	public boolean refreshModel;
	
	public boolean clockWiseOut;

	public TEAngledGearBox() {
		gearOrientations[0] = null;
		gearOrientations[1] = null;
	}
	
	@Override
	public void onDevicePlaced(World world, BlockPos pos, EnumFacing facing) {
		connectToNeighbours();
		
		super.onDevicePlaced(world, pos, facing);
	}
	
	public void setDirection(EnumFacing facing, boolean clockwise) {
		if (facing == gearOrientations[0]) {
			direction[0] = clockwise;
			direction[1] = !clockwise;
		} else {
			direction[1] = clockwise;
			direction[0] = !clockwise;			
		}
	}

	
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		
		int ori0 = compound.getInteger("gearOrientation0");
		int ori1 = compound.getInteger("gearOrientation1");
		
		if (ori0 >= 0) {
			gearOrientations[0] = EnumFacing.values()[ori0]; 
		} else {
			gearOrientations[0] = null;
		}
		if (ori1 >= 0) {
			gearOrientations[1] = EnumFacing.values()[ori1];
		} else {
			gearOrientations[1] = null;
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		compound.setInteger("gearOrientation0", gearOrientations[0] == null ? -1 : gearOrientations[0].ordinal());
		compound.setInteger("gearOrientation1", gearOrientations[1] == null ? -1 : gearOrientations[1].ordinal());
	}



	
	
	
	public void connectToNeighbours() {
		verifyIO(false);
		
		for (EnumFacing facing : EnumFacing.values()) {
			IMechanicalPowerDevice neighbourdevice = getConnectibleNeighbourDevice(facing);
			if (neighbourdevice == null) continue;
			
			if (gearOrientations[0] == null) {
				gearOrientations[0] = facing;
				
				continue;
			}
			
			if (gearOrientations[1] == null && gearOrientations[0] != facing) {
				gearOrientations[1] = facing;
				continue;
			}
		}
		
		if (gearOrientations[0] == null) {
			worldObj.destroyBlock(getPos(), true);
		}
		
		worldObj.markBlockForUpdate(pos);
	}
	
	
	
	public boolean verifyIO(boolean dropIfUnconnected) {
		IMechanicalPowerDevice first = getConnectibleNeighbourDevice(gearOrientations[0]);
		IMechanicalPowerDevice second = getConnectibleNeighbourDevice(gearOrientations[1]);
		
		if (second == null) {
			gearOrientations[1] = null;
		}
		if (first == null) {
			gearOrientations[0] = gearOrientations[1];
			gearOrientations[1] = null;
		}
		
		
		
		
		if (gearOrientations[0] == null && dropIfUnconnected) {
			worldObj.destroyBlock(getPos(), true);
			return false;
		}
		
		return true;
				
	}



	@Override
	public boolean hasConnectorAt(EnumFacing facing) {
		// The first gear can be connect anywhere
		// The second only at a 90 degree angle
		
		return 
			// Currently no inputs => all sides are ok
			(gearOrientations[0] == null && gearOrientations[1] == null) ||
			// One input is set => adjacent side is ok 
			(gearOrientations[1] == null && gearOrientations[0] != facing && gearOrientations[0].getOpposite() != facing) ||
			// Or existing ones
			gearOrientations[0] == facing || gearOrientations[1] == facing
		;
	}


	@Override
	public boolean isConnectedAt(EnumFacing facing) {
		// TODO Auto-generated method stub
		return false;
	}


	
	
	
}
