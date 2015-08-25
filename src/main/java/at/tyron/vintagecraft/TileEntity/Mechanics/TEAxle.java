package at.tyron.vintagecraft.TileEntity.Mechanics;

import at.tyron.vintagecraft.Block.Mechanics.BlockMechanicalVC;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerDevice;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerNetworkRelay;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TEAxle extends TEMechanicalNetworkDeviceBase implements IMechanicalPowerNetworkRelay {
	// To which block side the axle is attached to
	public EnumFacing attachment;
	
	public boolean refreshModel;
	//public int angle = 0;

	
	public boolean connectedLeft = false;
	public boolean connectedRight = false;
	
	public TEAxle() {
		orientation = EnumFacing.NORTH;
	}
	
	
	@Override
	public void onDevicePlaced(World world, BlockPos pos, EnumFacing facing, EnumFacing ontoside) {
		if (!((BlockMechanicalVC)getBlockType()).suitableGround(world, pos)) {
			facing = EnumFacing.UP;
		}
		
		super.onDevicePlaced(world, pos, facing, ontoside);
		
		
		if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
			for (int i = 0; i <= 3; i++) {
				if (world.isSideSolid(pos.offset(EnumFacing.getHorizontal(i)), EnumFacing.getHorizontal(i).getOpposite())) {
					attachment = EnumFacing.getHorizontal(i);
					break;
				}
			}
		} else {
			attachment = EnumFacing.DOWN;
		}
		
		recheckNeighbours();
	}

	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		connectedLeft = compound.getBoolean("connectedLeft");
		connectedRight = compound.getBoolean("connectedRight");
		
		attachment = EnumFacing.values()[compound.getInteger("attachment")];
	}
	
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		
		compound.setBoolean("connectedLeft", connectedLeft);
		compound.setBoolean("connectedRight", connectedRight);
		
		if (attachment != null) {
			compound.setInteger("attachment", attachment.getIndex());
		}
	}


	
	
	
	@Override
	public boolean isConnectedAt(EnumFacing facing) {
		return (facing == orientation || facing.getOpposite() == orientation) && super.isConnectedAt(facing); 
	}


	public void recheckNeighbours() {
		boolean oldLeft = connectedLeft;
		boolean oldRight = connectedRight;
		
		IMechanicalPowerDevice leftdevice = getConnectibleNeighbourDevice(orientation);
		IMechanicalPowerDevice rightdevice = getConnectibleNeighbourDevice(orientation.getOpposite());
		
		connectedLeft = leftdevice instanceof TEAxle;
		connectedRight = rightdevice instanceof TEAxle;
		
		if (oldLeft != connectedLeft || oldRight != connectedRight) {
			worldObj.markBlockForUpdate(pos);
		}
	}


	@Override
	public boolean hasConnectorAt(EnumFacing facing) {
		return facing == orientation || facing.getOpposite() == orientation;
	}


}
