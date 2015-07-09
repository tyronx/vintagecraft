package at.tyron.vintagecraft.TileEntity.Mechanics;

import java.util.Hashtable;

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


public class TEAngledGearBox extends TEMechanicalNetworkDeviceBase implements IMechanicalPowerNetworkRelay {
	// orientation = peg gear orientation
	
	public EnumFacing cagegearOrientation;
	public boolean refreshModel;
	

	public TEAngledGearBox() {
		cagegearOrientation = null;
	}
	
	@Override
	public void onDevicePlaced(World world, BlockPos pos, EnumFacing facing) {
		connectToNeighbours();
		
		super.onDevicePlaced(world, pos, facing);
	}
	
	/*public void setDirection(EnumFacing facing, boolean clockwise) {
		if (facing == gearOrientations[0]) {
			direction[0] = clockwise;
			direction[1] = !clockwise;
		} else {
			direction[1] = clockwise;
			direction[0] = !clockwise;			
		}
	}*/

	
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		int num = compound.getInteger("cagegearOrientation");
		cagegearOrientation = num == -1 ? null : EnumFacing.values()[num];
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		compound.setInteger("cagegearOrientation", cagegearOrientation == null ? -1 : cagegearOrientation.ordinal());
	}



	
	
	
	public void connectToNeighbours() {
		verifyIO(false);
		
		for (EnumFacing facing : EnumFacing.values()) {
			IMechanicalPowerDevice neighbourdevice = getConnectibleNeighbourDevice(facing);
			if (neighbourdevice == null) continue;
			
			if (orientation == null) {
				orientation = facing;
				
				continue;
			}
			
			if (cagegearOrientation == null && orientation != facing) {
				cagegearOrientation = facing;
				continue;
			}
		}
		
		if (orientation == null) {
			worldObj.destroyBlock(getPos(), true);
		}
		
		worldObj.markBlockForUpdate(pos);
	}
	
	
	
	public boolean verifyIO(boolean dropIfUnconnected) {
		IMechanicalPowerDevice first = getConnectibleNeighbourDevice(orientation);
		IMechanicalPowerDevice second = getConnectibleNeighbourDevice(cagegearOrientation);
		
		if (second == null) {
			cagegearOrientation = null;
		}
		if (first == null) {
			orientation = cagegearOrientation;
			cagegearOrientation = null;
		}
		
		
		
		
		if (orientation == null && dropIfUnconnected) {
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
			(orientation == null && cagegearOrientation == null) ||
			// One input is set => adjacent side is ok 
			(cagegearOrientation == null && orientation != facing && orientation.getOpposite() != facing) ||
			// Or existing ones
			orientation == facing || cagegearOrientation == facing
		;
	}




	


	
	
	
}
