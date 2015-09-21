package at.tyron.vintagecraft.TileEntity.Mechanics;

import java.util.Hashtable;

import at.tyron.vintagecraft.Interfaces.IMechanicalPowerDevice;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerNetworkNode;
import at.tyron.vintagecraft.World.MechanicalNetwork;
import at.tyron.vintagecraft.World.MechnicalNetworkManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class TEMechanicalNetworkPowerNodeBase extends TEMechanicalNetworkDeviceBase implements IMechanicalPowerNetworkNode {

	public void createMechanicalNetwork(MechanicalNetwork forkedFromNetwork, EnumFacing facing) {
		MechanicalNetwork network = createMechanicalNetwork();
		
		NBTTagCompound nbt = new NBTTagCompound();
		forkedFromNetwork.writeToNBT(nbt);
		network.readFromNBTForked(nbt);
		
		if (!worldObj.isRemote) { // This check might not be needed
			System.out.println("sent to clients");
			network.sentNetworkToClients();
		}
	}
	
	public MechanicalNetwork createMechanicalNetwork() {
		MechanicalNetwork network = MechnicalNetworkManager.getNetworkManagerForWorld(worldObj).createAndRegisterNetwork(this);
		this.networkId = network.networkId;
		directionFromFacing = orientation.getOpposite();
		
		worldObj.markBlockForUpdate(pos);
		
		if (worldObj.isRemote || MechnicalNetworkManager.getNetworkManagerForWorld(worldObj).getWorld().isRemote) {
			System.out.println(worldObj.isRemote + " vs. " + MechnicalNetworkManager.getNetworkManagerForWorld(worldObj).getWorld().isRemote);
			new java.lang.Exception().printStackTrace();
		}
		
		Hashtable<EnumFacing, IMechanicalPowerDevice> neighbourdevices = getNeighbourDevices(true);
		
		for (EnumFacing facing : neighbourdevices.keySet()) {
			neighbourdevices.get(facing).propagateNetworkToNeighbours(MechnicalNetworkManager.getNetworkManagerForWorld(worldObj).getUniquePropagationId(), networkId, facing);
		}
		
		return network;
	}

	
	@Override
	public void onDevicePlaced(World world, BlockPos pos, EnumFacing facing, EnumFacing ontoside) {
		IMechanicalPowerDevice neib = getNeighbourDevice(getOutputSideForNetworkPropagation(), true);
		
		if (neib != null) {
			MechanicalNetwork network = neib.getNetwork(facing.getOpposite()); 
			if (network != null) {
				this.networkId = network.networkId;
				if(!world.isRemote) network.register(this);
			}
		}
		
		if (this.networkId == 0 && !worldObj.isRemote) {
			//System.out.println("will create network from placement");
			createMechanicalNetwork();
		}
	}
	
	@Override
	public void propagateNetworkToNeighbours(int propagationId, int networkId, EnumFacing remoteFacing) {
		// Already propagated
		if (this.propagationId == propagationId) return;
		this.propagationId = propagationId;

		worldObj.markBlockForUpdate(pos);
		
		this.networkId = networkId;
		getNetwork(remoteFacing.getOpposite()).register(this);
		
		sendNetworkToNeighbours(propagationId, networkId, remoteFacing);
	}
}
