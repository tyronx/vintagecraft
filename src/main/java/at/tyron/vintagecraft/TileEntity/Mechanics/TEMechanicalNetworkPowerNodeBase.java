package at.tyron.vintagecraft.TileEntity.Mechanics;

import java.util.Hashtable;

import at.tyron.vintagecraft.Interfaces.IMechanicalPowerDevice;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerNetworkNode;
import at.tyron.vintagecraft.World.MechanicalNetwork;
import net.minecraft.util.EnumFacing;

public abstract class TEMechanicalNetworkPowerNodeBase extends TEMechanicalNetworkDeviceBase implements IMechanicalPowerNetworkNode {


	
	protected void createMechanicalNetwork() {
		MechanicalNetwork network = MechanicalNetwork.createAndRegisterNetwork(this);
		trySetNetwork(network, getOutputSideForNetworkPropagation());
		
		Hashtable<EnumFacing, IMechanicalPowerDevice> neighbourdevices = getConnectibleNeighbourDevices();
		
		for (EnumFacing facing : neighbourdevices.keySet()) {
			neighbourdevices.get(facing).propagateNetworkToNeighbours(MechanicalNetwork.getUniquePropagationId(), network, facing);
		}
		
	}


	
	// 1 for clockwise
	// 0 for no torque
	// -1 for counter-clockwise
	public abstract int getTorqueDirection(MechanicalNetwork network);

	
}
