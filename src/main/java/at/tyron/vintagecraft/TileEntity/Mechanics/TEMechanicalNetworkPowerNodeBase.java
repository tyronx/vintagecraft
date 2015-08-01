package at.tyron.vintagecraft.TileEntity.Mechanics;

import java.util.Hashtable;

import at.tyron.vintagecraft.Interfaces.IMechanicalPowerDevice;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerNetworkNode;
import at.tyron.vintagecraft.World.MechanicalNetwork;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class TEMechanicalNetworkPowerNodeBase extends TEMechanicalNetworkDeviceBase implements IMechanicalPowerNetworkNode {

	
	protected void createMechanicalNetwork() {
		MechanicalNetwork network = MechanicalNetwork.createAndRegisterNetwork(this);
		this.network = network;
		worldObj.markBlockForUpdate(pos);
		
		Hashtable<EnumFacing, IMechanicalPowerDevice> neighbourdevices = getNeighbourDevices(true);
		
		for (EnumFacing facing : neighbourdevices.keySet()) {
			neighbourdevices.get(facing).propagateNetworkToNeighbours(MechanicalNetwork.getUniquePropagationId(), network, facing);
		}
		
	}

	
	@Override
	public void onDevicePlaced(World world, BlockPos pos, EnumFacing facing, EnumFacing ontoside) {
		IMechanicalPowerDevice neib = getNeighbourDevice(getOutputSideForNetworkPropagation(), true);
		
		if (neib != null) {
			this.network = neib.getNetwork(facing.getOpposite());
			if(!world.isRemote && network != null) network.register(this);
		}
		
		if (this.network == null && !worldObj.isRemote) {
			//System.out.println("will create network from placement");
			createMechanicalNetwork();
		}
	}
	
	@Override
	public void propagateNetworkToNeighbours(int propagationId, MechanicalNetwork network, EnumFacing remoteFacing) {
		// Already propagated
		if (this.propagationId == propagationId) return;
		this.propagationId = propagationId;

		this.network = network;
		worldObj.markBlockForUpdate(pos);
		network.register(this);
		
		sendNetworkToNeighbours(propagationId, network, remoteFacing);
	}
}
