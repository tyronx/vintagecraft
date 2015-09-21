package at.tyron.vintagecraft.TileEntity.Mechanics;

import at.tyron.vintagecraft.Interfaces.IMechanicalPowerDevice;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerNetworkRelay;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;


public class TEAngledGearBox extends TEMechanicalNetworkDeviceBase implements IUpdatePlayerListBox, IMechanicalPowerNetworkRelay {
	// orientation = peg gear orientation
	
	public EnumFacing cagegearOrientation;
	public boolean refreshModel;
	
	
	public TEAngledGearBox() {
		cagegearOrientation = null;
	}
	
	
	@Override
	public boolean isClockWiseDirection(EnumFacing localFacing) {
		return clockwise;
	}

	
	@Override
	public void onDevicePlaced(World world, BlockPos pos, EnumFacing facing, EnumFacing ontoside) {
		orientation = null;
		connectToNeighbours();
		handleMechanicalRelayPlacement();		
	}

	
	@Override
	public void setDirectionFromFacing(EnumFacing facing) {
		super.setDirectionFromFacing(facing);
		connectToNeighbours();
		if (cagegearOrientation != null && facing == cagegearOrientation.getOpposite()) {
			// Flip gears
			EnumFacing tmp = orientation;
			orientation = cagegearOrientation;
			cagegearOrientation = tmp;
		} else {
			
		}
	}

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

	@Override
	public void update() {
		if (getNetwork(orientation) != null && worldObj != null && !worldObj.isRemote) {
			if (worldObj.rand.nextFloat() < Math.min(0.01f, getNetwork(orientation).getSpeed() / 2000f)) {
				worldObj.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "vintagecraft:woodcreak", 1f, 1f);
			}
		}
	}
	
	
}
