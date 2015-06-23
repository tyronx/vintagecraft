package at.tyron.vintagecraft.TileEntity.Mechanics;

import java.util.ArrayList;
import java.util.Hashtable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerDevice;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerNetworkNode;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerNetworkRelay;
import at.tyron.vintagecraft.TileEntity.NetworkTileEntity;
import at.tyron.vintagecraft.World.MechanicalNetwork;

public abstract class TEMechanicalNetworkDeviceBase extends NetworkTileEntity implements IMechanicalPowerDevice  {
	public MechanicalNetwork network;
	int propagationId;
	
	public boolean clockwise;
	public EnumFacing orientation;
	
	
	@SideOnly(Side.CLIENT)
	public float getAngle() {
		MechanicalNetwork network = getNetwork(null); 
		if (network == null) return 0;
		return network.getAngle();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		network = MechanicalNetwork.getNetworkById(compound.getInteger("networkId"));
		clockwise = compound.getBoolean("clockwise");
		
		int num = compound.getInteger("orientation");
		orientation = num == -1 ? null : EnumFacing.values()[num];
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		compound.setInteger("networkId", network == null ? 0 : network.networkId);
		compound.setInteger("orientation", orientation == null ? -1 : orientation.ordinal());
		compound.setBoolean("clockwise", clockwise);
	}


	@Override
	public MechanicalNetwork getNetwork(EnumFacing facing) {
		if (network == null || MechanicalNetwork.getNetworkById(network.networkId) == null) {
			network = null;
		}
		return network;
	}

	public float getNetworkSpeed(EnumFacing facing) {
		MechanicalNetwork network = getNetwork(facing); 
		if (network == null) return 0;
		return network.getSpeed();
	}

	
	@Override
	public BlockPos getPosition() {
		return getPos();
	}

	@Override
	public void trySetNetwork(MechanicalNetwork network, EnumFacing facing) {
		if (hasConnectorAt(facing)) {
			this.network = network;
			worldObj.markBlockForUpdate(pos);
		}
	}

	
	public void onDevicePlaced(World world, BlockPos pos, EnumFacing facing) {
		
		handleMechanicalRelayPlacement();
	}

	public void onDeviceRemoved(World world, BlockPos pos) {
		handleMechanicRelayRemoval();
	}

	
	
	public void propagateNetworkToNeighbours(int propagationId, MechanicalNetwork network, EnumFacing fromFacing) {
		// Already propagated
		if (this.network == network) return;
		
		trySetNetwork(network, fromFacing);
		
		System.out.println("connected to network " + network + ", propagate to neighbours");
		
		sendNetworkToNeighbours(propagationId, network, fromFacing);
	}
	
	
	public void sendNetworkToNeighbours(int propagationId, MechanicalNetwork network, EnumFacing fromFacing) {
		Hashtable<EnumFacing, IMechanicalPowerDevice> connectibleNeighbours = getConnectibleNeighbourDevices();
		
		for (EnumFacing toFacing : connectibleNeighbours.keySet()) {
			if (fromFacing.getOpposite() != toFacing) {
				connectibleNeighbours.get(toFacing).propagateNetworkToNeighbours(propagationId, network, toFacing);
			}
		}		
	}
	
	
	
	public void handleMechanicRelayRemoval() {
		Hashtable<EnumFacing, MechanicalNetwork> networks = new Hashtable<EnumFacing, MechanicalNetwork>();
		
		for (EnumFacing facing : EnumFacing.values()) {
			IMechanicalPowerDevice neib = getConnectibleNeighbourDevice(facing);
			if (neib == null) continue;
			MechanicalNetwork network = neib.getNetwork(facing.getOpposite());
			if (network != null && !networks.contains(network)) {
				networks.put(facing, network);
			}
		}
		
		if (networks.size() >= 2) {
			for (MechanicalNetwork network : networks.values()) {
				network.rebuildNetwork();
			}
		}
		
	}
	
	public void handleMechanicalRelayPlacement() {
		Hashtable<EnumFacing, MechanicalNetwork> networks = new Hashtable<EnumFacing, MechanicalNetwork>();
		ArrayList<EnumFacing> nullnetworks = new ArrayList<EnumFacing>();
		
		for (EnumFacing facing : EnumFacing.values()) {
			IMechanicalPowerDevice neib = getConnectibleNeighbourDevice(facing);
			if (neib == null) continue;
			MechanicalNetwork network = neib.getNetwork(facing.getOpposite());
			
			if (network != null && !networks.contains(network)) {
				networks.put(facing, network);
			}
			
			if (network == null) {
				nullnetworks.add(facing);
			}
		}
		
		
		if (networks.size() == 1) {
			EnumFacing facing = networks.keySet().toArray(new EnumFacing[0])[0];
			
			trySetNetwork(networks.get(facing), facing);
			
			for (EnumFacing nullnetworkfacing : nullnetworks) {
				getConnectibleNeighbourDevice(nullnetworkfacing).propagateNetworkToNeighbours(
					MechanicalNetwork.getUniquePropagationId(), 
					network, 
					nullnetworkfacing
				);
			}
		}
		
		if (networks.size() > 1) {
			throw new RuntimeException("Connecting different mechanical network is not yet supported!");
		}				
		
	}
	
	
	public Hashtable<EnumFacing, IMechanicalPowerDevice> getConnectibleNeighbourDevices() {
		Hashtable<EnumFacing, IMechanicalPowerDevice> connectibleNeighbours = new Hashtable<EnumFacing, IMechanicalPowerDevice>();
		for (EnumFacing facing : EnumFacing.values()) {
			IMechanicalPowerDevice neib = getConnectibleNeighbourDevice(facing);
			if (neib == null) continue;
			connectibleNeighbours.put(facing, neib);
		}
		return connectibleNeighbours;
	}
	
	
	public IMechanicalPowerDevice getConnectibleNeighbourDevice(EnumFacing facing) {
		return getConnectibleNeighbourDevice(worldObj, getPos(), facing);
	}

	
	public static IMechanicalPowerDevice getConnectibleNeighbourDevice(World worldObj, BlockPos pos, EnumFacing facing) {
		if (facing == null) return null;
		
		TileEntity te = worldObj.getTileEntity(pos.offset(facing));
		if (te instanceof IMechanicalPowerDevice) {
			IMechanicalPowerDevice mechdevice = (IMechanicalPowerDevice)te;
			
			if (mechdevice.hasConnectorAt(facing.getOpposite())) {
				return mechdevice;
			}
		}
		
		return null;
	}
	
	
	public static boolean hasConnectibleDevice(World worldObj, BlockPos pos) {
		for (EnumFacing facing : EnumFacing.values()) {
			if (getConnectibleNeighbourDevice(worldObj, pos, facing) != null) return true;
		}
		return false;
	}

	@Override
	public boolean isConnectedAt(EnumFacing facing) {
		IMechanicalPowerDevice device = getConnectibleNeighbourDevice(facing);
		return device != null && !(device instanceof IMechanicalPowerDevice); 
	}

	
	
	public boolean isClockWiseDirection(EnumFacing facing) {
		return clockwise;
	}
	
	public void setDirection(EnumFacing facing, boolean clockwise) {
		this.clockwise = clockwise;
	}

	@Override
	public boolean exists() {
		return worldObj.getTileEntity(pos) == this;
	}

}
