package at.tyron.vintagecraft.TileEntity.Mechanics;

import at.tyron.vintagecraft.Interfaces.IMechanicalPowerDevice;
import at.tyron.vintagecraft.World.MechanicalNetwork;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TEBellows extends TEMechanicalNetworkPowerNodeBase implements IUpdatePlayerListBox {
	public boolean refreshModel = false;
	
	@Override
	public boolean hasConnectorAt(EnumFacing facing) {
		return facing == orientation.getOpposite();
	}
	
	// Connector is at the backside of the rotor
	@Override
	public boolean isConnectedAt(EnumFacing facing) {
		return facing == orientation.getOpposite() && super.isConnectedAt(facing); 
	}

	

	@Override
	public float getTorque(MechanicalNetwork network) {
		return 0;
	}

	@Override
	public float getResistance(MechanicalNetwork network) {
		return 5f;
	}

	@Override
	public EnumFacing getOutputSideForNetworkPropagation() {
		return orientation.getOpposite();
	}

	
	@Override
	public void onDevicePlaced(World world, BlockPos pos, EnumFacing facing, EnumFacing ontoside) {
		this.orientation = facing;
		connectToNeighbour();
		super.onDevicePlaced(world, pos, facing, ontoside);
	}

	public void connectToNeighbour() {
		if (getConnectibleNeighbourDevice(orientation.getOpposite()) != null) return;
		
		for (EnumFacing facing : EnumFacing.values()) {
			IMechanicalPowerDevice neighbourdevice = getConnectibleNeighbourDevice(facing.getOpposite());
			if (neighbourdevice == null) continue;			
			orientation = facing;
			worldObj.markBlockForUpdate(pos);
			break;
		}
	}

	
	long cnt = 0;
	@Override
	public void update() {
		
		if (network != null && worldObj != null && worldObj.isRemote) {
			//if (worldObj.rand.nextFloat() < Math.min(0.03f, network.getSpeed() / 1000f)) {
			//	worldObj.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "vintagecraft:woodcreak", 1.2f, 1f);
			//	System.out.println("play sound 2 " + network.getSpeed()); 
			//}
		}
	}

}
