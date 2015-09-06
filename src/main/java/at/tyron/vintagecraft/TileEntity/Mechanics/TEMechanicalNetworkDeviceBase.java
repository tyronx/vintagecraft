package at.tyron.vintagecraft.TileEntity.Mechanics;

import java.util.ArrayList;
import java.util.Hashtable;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerDevice;
import at.tyron.vintagecraft.TileEntity.NetworkTileEntity;
import at.tyron.vintagecraft.World.MechanicalNetwork;
import at.tyron.vintagecraft.World.MechnicalNetworkManager;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

public abstract class TEMechanicalNetworkDeviceBase extends NetworkTileEntity implements IMechanicalPowerDevice  {
	public boolean markedForRemoval = false;
	
	int networkId;  // The network we're connected at [orientation] 
	public EnumFacing orientation;
	
	int propagationId;
	
	/* Wether the device turns clockwise as seen from clockwiseFromFacing (standing 3 blocks away from this facing and looking towards the block */
	public boolean clockwise;
	public EnumFacing directionFromFacing;
	
	
	private EnumTree treeType;
	public ResourceLocation woodTexture;
	
	public void setTreeType(EnumTree treeType) {
		if (treeType == null) treeType = EnumTree.OAK;
		
		this.treeType = treeType;
		woodTexture = new ResourceLocation(ModInfo.ModID, "textures/blocks/planks/" + treeType.getName() + ".png");
	}
	public EnumTree getTreeType() {
		return treeType;
	}
	public EnumFacing getOrientation() {
		return orientation;
	}
	

	
	//@SideOnly(Side.CLIENT)
	float lastAngle = 0f;
	
	//@SideOnly(Side.CLIENT)
	public float getAngle() {
		MechanicalNetwork network = getNetwork(null); 
		if (network == null) {
			return lastAngle;
		}
		
		if (directionFromFacing != orientation) {
			return (lastAngle = 360 - network.getAngle());
		}
		
		return (lastAngle = network.getAngle());
	}

	
	@Override
	public void validate() {
		super.validate();
		//loadNetwork();
		
		//System.out.println("registered device during validate");
		
	}
	
/*	public void loadNetwork() {
		MechnicalNetworkManager manager = MechnicalNetworkManager.getNetworkManagerForWorld(worldObj);
		if (manager != null) {
			network = manager.getNetworkById(this.networkId);
			if (network != null) {
				System.out.println("registered device during validate");
				network.register(this);
			}
		}
	}*/
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		this.networkId = compound.getInteger("networkId");
		//if (worldObj != null) loadNetwork();	
		
		clockwise = compound.getBoolean("clockwise");
		
		int num = compound.getInteger("orientation");
		orientation = num == -1 ? null : EnumFacing.values()[num];
		
		num = compound.getInteger("clockwiseFromFacing");
		directionFromFacing = num == -1 ? null : EnumFacing.values()[num];

		
		int treetypeid = compound.getInteger("treetype");
		if (treetypeid != -1) {
			setTreeType(EnumTree.byId(treetypeid));
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		compound.setInteger("networkId", networkId);
		compound.setInteger("orientation", orientation == null ? -1 : orientation.ordinal());
		compound.setInteger("clockwiseFromFacing", directionFromFacing == null ? -1 : directionFromFacing.ordinal());
		compound.setBoolean("clockwise", clockwise);
		
		if (treeType != null) {
			compound.setInteger("treetype", treeType.getId());
		} else {
			compound.setInteger("treetype", -1);
		}
	}


	public void setDirectionFromFacing(EnumFacing facing) {
		this.directionFromFacing = facing;
	}
	
	public EnumFacing getDirectionFromFacing() {
		return directionFromFacing;
	}
	
	@Override
	public MechanicalNetwork getNetwork(EnumFacing facing) {
		if (worldObj == null || MechnicalNetworkManager.getNetworkManagerForWorld(worldObj) == null) return null;
		
		return MechnicalNetworkManager.getNetworkManagerForWorld(worldObj).getNetworkById(networkId);
	}
	
	@Override
	public EnumFacing getFacing(MechanicalNetwork network) {
		return orientation;
	}
	

	
	public MechanicalNetwork[] getNetworks() {
		MechanicalNetwork network = MechnicalNetworkManager.getNetworkManagerForWorld(worldObj).getNetworkById(networkId);
		if (network == null) {
			return new MechanicalNetwork[0];
		}
		return new MechanicalNetwork[]{network};
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
	public void trySetNetwork(int networkId, EnumFacing localFacing) {
		if (hasConnectorAt(localFacing)) {
			this.networkId = networkId;
			
			IMechanicalPowerDevice device = getNeighbourDevice(localFacing, true);
			if (device != null) {
				if (getNetwork(localFacing).getDirection() != 0) {
					clockwise = device.isClockWiseDirection(localFacing.getOpposite());
					setDirectionFromFacing(localFacing.getOpposite());
				}
			} else {
				throw new RuntimeException("Eh, a network coming from " + localFacing + ", but there is no device, instead " + worldObj.getBlockState(pos.offset(localFacing)) + "?!");
			}
			
			getNetwork(localFacing).register(this);
			
			worldObj.markBlockForUpdate(pos);
		} else {
			directionFromFacing = null;
			//this.network = null;
			this.networkId = 0;
		}
	}

	
	public void onDevicePlaced(World world, BlockPos pos, EnumFacing facing, EnumFacing ontoside) {
		orientation = facing;
		handleMechanicalRelayPlacement();
	}

	public void onDeviceRemoved(World world, BlockPos pos) {
		markedForRemoval = true;
		handleMechanicRelayRemoval();
	}

	
	
	public void propagateNetworkToNeighbours(int propagationId, int networkId, EnumFacing remoteFacing) {
		// Already propagated
		if (this.propagationId == propagationId) return;
		this.propagationId = propagationId;

		trySetNetwork(networkId, remoteFacing.getOpposite());
		getNetwork(remoteFacing.getOpposite()).register(this);
		
		 		
		sendNetworkToNeighbours(propagationId, networkId, remoteFacing);
	}
	
	
	@Override
	public void propagateDirectionToNeightbours(int propagationId, EnumFacing remoteFacing, boolean clockwise) {
		// Already propagated
		if (this.propagationId == propagationId) return;
		this.propagationId = propagationId;
		
		this.clockwise = clockwise;
		setDirectionFromFacing(remoteFacing);
		
/*		if (this instanceof TEWindmillRotor) {
			System.out.println("direction set to " + clockwise + " from facing is " + remoteFacing);
		}
	*/	
		worldObj.markBlockForUpdate(pos);
		
		Hashtable<EnumFacing, IMechanicalPowerDevice> connectibleNeighbours = getNeighbourDevices(true);
		
		for (EnumFacing localFacing : connectibleNeighbours.keySet()) {
			if (remoteFacing.getOpposite() != localFacing) {
				boolean remoteClockwise = isClockWiseDirection(localFacing);
				connectibleNeighbours.get(localFacing).propagateDirectionToNeightbours(propagationId, localFacing, remoteClockwise);
			}
		}	
	}
	
	public void sendNetworkToNeighbours(int propagationId, int networkId, EnumFacing remoteFacing) {
		Hashtable<EnumFacing, IMechanicalPowerDevice> connectibleNeighbours = getNeighbourDevices(true);
		
		for (EnumFacing localFacing : connectibleNeighbours.keySet()) {
			if (remoteFacing.getOpposite() != localFacing) {
				connectibleNeighbours.get(localFacing).propagateNetworkToNeighbours(propagationId, networkId, localFacing);
			}
		}		
	}
	
	
	
	public void handleMechanicRelayRemoval() {
		if (networkId == 0) return;
		for (MechanicalNetwork network : getNetworks()) {
			network.unregister(this);
		}
	}
	
	public void handleMechanicalRelayPlacement() {
		Hashtable<EnumFacing, MechanicalNetwork> networks = new Hashtable<EnumFacing, MechanicalNetwork>();
		ArrayList<EnumFacing> nullnetworks = new ArrayList<EnumFacing>();
		
		for (EnumFacing facing : EnumFacing.values()) {
			IMechanicalPowerDevice neib = getNeighbourDevice(facing, true);
			
			if (neib == null) continue;
			
			MechanicalNetwork network = neib.getNetwork(facing.getOpposite());
			
			if (network != null && !networks.contains(network)) {
				networks.put(facing, network);
			}
			
			if (network == null) {
				nullnetworks.add(facing);
			}
		}
		
		//System.out.println(worldObj.isRemote + " found " + networks.size() + " networks ");
		
		if (networks.size() == 1) {
			EnumFacing facing = networks.keySet().toArray(new EnumFacing[0])[0];
			
			trySetNetwork(networks.get(facing).networkId, facing);
			
			
			
			for (EnumFacing nullnetworkfacing : nullnetworks) {
				getNeighbourDevice(nullnetworkfacing, true).propagateNetworkToNeighbours(
					MechnicalNetworkManager.getNetworkManagerForWorld(worldObj).getUniquePropagationId(), 
					networkId, 
					nullnetworkfacing
				);
			}
		}
		
		if (networks.size() > 1 && !worldObj.isRemote) {
			float maxSpeedDifference = 0;
			MechanicalNetwork dominantNetwork = null;
			
			for (MechanicalNetwork network : networks.values()) {
				if (dominantNetwork == null) {
					dominantNetwork = network;
					continue;
				}
				
				maxSpeedDifference = Math.max(maxSpeedDifference, Math.abs(network.getSpeed() - dominantNetwork.getSpeed()));
				
				if (Math.abs(network.getSpeed()) > Math.abs(dominantNetwork.getSpeed())) {
					dominantNetwork = network;
				}
			}
			
			
			//if (maxSpeedDifference < 1f) {
				for (MechanicalNetwork network : networks.values()) {
					if (network != dominantNetwork) {
						network.isDead = true;
						MechnicalNetworkManager.getNetworkManagerForWorld(worldObj).discardNetwork(network);
					}
				}
				dominantNetwork.rebuildNetwork();
				
				System.out.println("connected mechanical networks, because maxsspeeddiff is " + maxSpeedDifference);
				
				worldObj.markBlockForUpdate(pos);
				
			// Need to make a better mechanic for breaking, I can't break the block at this point, we get an exception
			/*} else {
				System.out.println("attempted to connect mechanical networks, but speed diff is too large: " + maxSpeedDifference);
				
				worldObj.destroyBlock(pos, true);
				worldObj.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "mob.zombie.woodbreak", 0.8f, 1f);
			}
			*/
			
			
		//	throw new RuntimeException("Connecting mechanical networks is not yet supported!");
		}				
		
	}
	
	
	// connected = true   => get connected devices
	// connected = false  => get connectible devices (= devices that could potentially connect to our own device)
	public Hashtable<EnumFacing, IMechanicalPowerDevice> getNeighbourDevices(boolean connected) {
		Hashtable<EnumFacing, IMechanicalPowerDevice> connectibleNeighbours = new Hashtable<EnumFacing, IMechanicalPowerDevice>();
		for (EnumFacing facing : EnumFacing.values()) {
			IMechanicalPowerDevice neib = getNeighbourDevice(facing, connected);
			if (neib == null) continue;
			connectibleNeighbours.put(facing, neib);
		}
		return connectibleNeighbours;
	}
	
	
	public IMechanicalPowerDevice getConnectibleNeighbourDevice(EnumFacing facing) {
		return getNeighbourDevice(facing, false);
	}
	
	public IMechanicalPowerDevice getNeighbourDevice(EnumFacing facing, boolean connected) {
		return getNeighbourDevice(worldObj, getPos(), facing, connected);
	}

	
	public static IMechanicalPowerDevice getNeighbourDevice(World worldObj, BlockPos pos, EnumFacing facing, boolean connected) {
		if (facing == null) return null;
		
		TileEntity te = worldObj.getTileEntity(pos.offset(facing));
		
		if (te instanceof IMechanicalPowerDevice) {
			IMechanicalPowerDevice mechdevice = (IMechanicalPowerDevice)te;
			if (!mechdevice.exists()) return null;
					
//			System.out.println(pos + " / " + pos.offset(facing) + "  " +  facing + " of me is a " + mechdevice.getClass());
			
			if (!connected && mechdevice.hasConnectorAt(facing.getOpposite())) {
				return mechdevice;
			}
			if (connected && mechdevice.isConnectedAt(facing.getOpposite())) {
				return mechdevice;
			}
		}
		
		return null;
	}
	
	
	public static boolean hasConnectibleDevice(World worldObj, BlockPos pos) {
		for (EnumFacing facing : EnumFacing.values()) {
			if (getNeighbourDevice(worldObj, pos, facing, false) != null) return true;
		}
		return false;
	}

	// Default behavior: Device is connected to all neighbour devices
	@Override
	public boolean isConnectedAt(EnumFacing facing) {
		IMechanicalPowerDevice device = getNeighbourDevice(facing, false);

		return device != null && (device instanceof IMechanicalPowerDevice); 
	}

	
	
	public boolean isClockWiseDirection(EnumFacing facing) {
		return clockwise;
	}
	
	@Override
	public boolean exists() {
		return worldObj.getTileEntity(pos) == this && !markedForRemoval;
	}

	
	@Override
	public void setClockWiseDirection(int networkId, boolean clockwise) {
		this.clockwise = clockwise;	
	}

	public void clearNetwork() {
		this.networkId = 0;
		worldObj.markBlockForUpdate(pos);
	}
	
	public void onNeighborBlockChange() {
	}
	
}
