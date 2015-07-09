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
	EnumFacing orientation = EnumFacing.NORTH;
	float windmillSize = 1f;
	public boolean refreshModel;
	
	
	public EnumFacing getOrientation() {
		return orientation;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		orientation = EnumFacing.values()[compound.getInteger("orientation")];
	}
	
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		compound.setInteger("orientation", orientation.ordinal());
	}

	
	@Override
	public void onDevicePlaced(World world, BlockPos pos, EnumFacing facing) {
		this.orientation = facing;
		super.onDevicePlaced(world, pos, facing);
	}

	@Override
	public boolean hasConnectorAt(EnumFacing facing) {
		return facing == orientation.getOpposite();
	}

	// Winmill Requires a mininum amount of wind to rotate, so this method returns 0 for low wind
	float getWindRotatingPower() {
		WindGen windgen = WindGen.getWindGenForWorld(worldObj);
		float wind = (float) (windgen == null ? 0 : windgen.getWindAt(pos));
		
		//return (wind * wind - 0.05f) * 48 * Math.signum(wind);
		return 10f;
	}
	
	
	// Should return:
	// torque according to the wind speed. High wind = large torque   | may be negative it the mill turns the other direction
	// 0 torque if network speed is higher than getWindRotatingPower() or turns the other direction

	@Override
	public float getTorque(MechanicalNetwork network) {
		float rotatingpower = getWindRotatingPower();
		//System.out.println("rot power = " + rotatingpower);
		int dir1 = (int)Math.signum(rotatingpower);
		int dir2 = (int)Math.signum(network.getSpeed());
		
		if (dir1 != dir2 && dir1 != 0 && dir2 != 0) {
			return 0;
		}
		
		if (Math.abs(network.getSpeed()) > Math.abs(rotatingpower * windmillSize))  return 0;
		
		//System.out.println("foobar " + network.getSpeed() + " / " + (rotatingpower * windmillSize));
		
		return rotatingpower * windmillSize;
	}

	
	// If the network runs at higher speed or the other direction than what the rotor creates, it will act as a resistor.
	@Override
	public float getResistance(MechanicalNetwork network) {
		float rotatingpower = getWindRotatingPower();
		
		int dir1 = (int)Math.signum(rotatingpower);
		int dir2 = (int)Math.signum(network.getSpeed());
		
		if (dir1 != dir2 && dir1 != 0 && dir2 != 0) {
			return Math.abs(rotatingpower * windmillSize);
		}
		
		if (Math.abs(network.getSpeed()) > Math.abs(rotatingpower * windmillSize)) {
			return Math.abs(network.getSpeed()) - Math.abs(rotatingpower * windmillSize);
		}
		
		return 0;
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






	

}
