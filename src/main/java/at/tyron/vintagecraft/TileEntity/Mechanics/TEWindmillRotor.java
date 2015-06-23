package at.tyron.vintagecraft.TileEntity.Mechanics;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import at.tyron.vintagecraft.Client.Render.TESR.TESRMechanicalBase;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerNetworkNode;
import at.tyron.vintagecraft.World.MechanicalNetwork;
import at.tyron.vintagecraft.World.WindGen;

public class TEWindmillRotor extends TEMechanicalNetworkPowerNodeBase implements IUpdatePlayerListBox {
	public EnumFacing orientation = EnumFacing.NORTH;
	float maxSpeed = 50;
	int availableTorque = 20;
	public boolean refreshModel;
	
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		orientation = EnumFacing.values()[compound.getInteger("orientation")];
		
		maxSpeed = compound.getFloat("maxSpeed");
		availableTorque = compound.getInteger("availableTorque");
	}
	
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		compound.setInteger("orientation", orientation.ordinal());
		compound.setFloat("maxSpeed", maxSpeed);
		compound.setInteger("availableTorque", availableTorque);
	}

	
	@Override
	public void onDevicePlaced(World world, BlockPos pos, EnumFacing facing) {
		this.orientation = facing.getOpposite();
		super.onDevicePlaced(world, pos, facing);
	}

	@Override
	public boolean hasConnectorAt(EnumFacing facing) {
		return facing == orientation;
	}

	@Override
	public float getMaxSpeed(MechanicalNetwork network) {
		WindGen widngen = WindGen.getWindGenForWorld(worldObj);
		double wind = widngen == null ? 0 : WindGen.getWindGenForWorld(worldObj).getWindAt(pos);
		
		return (float) Math.max(0, wind * wind - 0.04f) * 48;
	}

	@Override
	public float getTorque(MechanicalNetwork network) {
		if (network.getSpeed() > maxSpeed) return 0;
		return availableTorque;
	}

	@Override
	public float getResistance(MechanicalNetwork network) {
		// If the network runs at higher speed than what the rotor creates, it will act as a resistor.
		return Math.max(0, 2 * (network.getSpeed() - maxSpeed));
	}

	@Override
	public EnumFacing getOutputSideForNetworkPropagation() {
		return orientation.getOpposite();
	}

	long cnt = 0;
	@Override
	public void update() {
		
		//System.out.println("update " + worldObj.isRemote);
		
		/*if (!worldObj.isRemote) {
			double wind = WindGen.getWindGenForWorld(worldObj).getWindAt(pos);
			maxSpeed = (float) Math.max(0, wind * wind - 0.04f) * 48;
			clockwise = wind > 0;
			
			if (cnt++ % 30 == 0) {
				//System.out.println("send update to client " + wind + " / " + maxSpeed + " / " + clockwise);
				worldObj.markBlockForUpdate(pos);
			}
			
		}*/
		
		
		if (network == null && !worldObj.isRemote) {
			System.out.println("will create");
			createMechanicalNetwork();
			//System.out.println("created network " + worldObj.isRemote);
		}
		
	}


	@Override
	public int getTorqueDirection(MechanicalNetwork network) {
		float speed = getMaxSpeed(network);
		if (speed > 0) return 1;
		if (speed < 0) return -1;
		return 0;
	}




	

}
