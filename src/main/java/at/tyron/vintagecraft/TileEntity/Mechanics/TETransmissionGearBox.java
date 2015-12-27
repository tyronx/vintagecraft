package at.tyron.vintagecraft.TileEntity.Mechanics;

import at.tyron.vintagecraft.Interfaces.Tileentity.IMechanicalPowerDevice;
import at.tyron.vintagecraft.Interfaces.Tileentity.IMechanicalPowerNetworkNode;
import at.tyron.vintagecraft.World.MechanicalNetwork;
import at.tyron.vintagecraft.World.MechnicalNetworkManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;


/* This code here is completely unfinished and currently not used */

public class TETransmissionGearBox extends TEMechanicalNetworkDeviceBase implements IMechanicalPowerNetworkNode {
	public EnumFacing[] gearOrientations = new EnumFacing[2];
	public EnumFacing[] networkOrientations = new EnumFacing[2];
	
	public boolean refreshModel;
	public int angle = 0;
	
	public MechanicalNetwork network2;
	private int networkId2;

	public TETransmissionGearBox() {
		gearOrientations[0] = null;
		gearOrientations[1] = null;
	}
	
	
	@Override
	public void validate() {
		super.validate();
		network2 = MechnicalNetworkManager.getNetworkManagerForWorld(worldObj).getNetworkById(this.networkId2);
		if (network2 != null) {
			network2.register(this);
		}
	}

	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		this.networkId2 = compound.getInteger("networkId2");
		
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
		
		compound.setInteger("networkId", network2 == null ? 0 : network2.networkId);
		
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
	public float getTorque(MechanicalNetwork network) {
		if (getEnergyConsumingNetwork() == network) {
			return getEnergyProducingNetwork().getUnusedTorque();
		}
		return 0;
	}


	@Override
	public float getResistance(MechanicalNetwork network) {
		if (getEnergyProducingNetwork() == network) {
			return getEnergyConsumingNetwork().getRemainingResistance();
		}
		return 0;
	}

	
	
	
	
	MechanicalNetwork getEnergyProducingNetwork() {
		if (getNetwork(networkOrientations[0]).getSpeed() > getNetwork(networkOrientations[1]).getSpeed()) {
			return getNetwork(networkOrientations[0]);
		}
		return getNetwork(networkOrientations[1]);
	}

	MechanicalNetwork getEnergyConsumingNetwork() {
		if (getNetwork(networkOrientations[0]).getSpeed() > getNetwork(networkOrientations[1]).getSpeed()) {
			return getNetwork(networkOrientations[1]);
		}
		return getNetwork(networkOrientations[0]);
	}


	@Override
	public boolean hasConnectorAt(EnumFacing facing) {
		// The first gear can be connect anywhere
		// The second only at a 90 degree angle
		
		return 
			// Currently no inputs => all sides are ok
			(gearOrientations[0] == null && gearOrientations[1] == null) ||
			// One input => adjacent side is ok 
			(gearOrientations[1] == null && gearOrientations[0] != facing && gearOrientations[0].getOpposite() != facing)
		;
	}


	@Override
	public EnumFacing getOutputSideForNetworkPropagation() {
		return null;
	}


	@Override
	public void createMechanicalNetwork(MechanicalNetwork forkedFromNetwork, EnumFacing facing) {
	}






	
	
	
}
