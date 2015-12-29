package at.tyron.vintagecraft.TileEntity.Mechanics;

import at.tyron.vintagecraft.Interfaces.IMechanicalPowerDevice;
import at.tyron.vintagecraft.World.MechanicalNetwork;
import at.tyron.vintagecraft.World.WindGen;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public class TEWindmillRotor extends TEMechanicalNetworkPowerNodeBase implements ITickable {
	// The tower mill was more powerful than the water mill, able to generate roughly 20 to 30 horsepower.
	// https://en.wikipedia.org/wiki/Tower_mill#Application
	// 20 hp = 15 KW
	float maxSpeedMultiplier = 1f;
	
	
	public boolean refreshModel;
	
	int bladeSize;  // 0..4
	
	
	@SideOnly(Side.CLIENT)
	public float getAngle() {
		MechanicalNetwork network = getNetwork(null);
		if (network == null) return 0;
		
		/*if (orientation == EnumFacing.WEST || orientation == EnumFacing.SOUTH || orientation == EnumFacing.NORTH) {
			return 360 - network.getAngle();
		}*/
		
		return network.getAngle();
	}

	
	public boolean tryAddBlades() {
		if (bladeSize < 4) {
			bladeSize++;
			worldObj.markBlockForUpdate(pos);
			return true;
		}
		return false;
	}
	
	public int getBladeSize() {
		return bladeSize;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		bladeSize = compound.getInteger("bladeSize");
	}
	
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("bladeSize", bladeSize);
	}

	
	@Override
	public void onDevicePlaced(World world, BlockPos pos, EnumFacing facing, EnumFacing ontoside) {
		this.orientation = facing;
		connectToNeighbour();
		super.onDevicePlaced(world, pos, facing, ontoside);
	}

	// Connector is at the backside of the rotor
	@Override
	public boolean hasConnectorAt(EnumFacing facing) {
		return facing == orientation.getOpposite();
	}
	
	// Connector is at the backside of the rotor
	@Override
	public boolean isConnectedAt(EnumFacing facing) {
		return facing == orientation.getOpposite() && super.isConnectedAt(facing); 
	}


	// Winmill Requires a mininum amount of wind to rotate, so this method returns 0 for low wind
	// Our wind mill is angled on both sides, resulting in it turning only 1 direction
	public float getWindRotatingPower() {
		if (bladeSize == 0) return 0;
		
		WindGen windgen = WindGen.getWindGenForWorld(worldObj);
		float wind = (float) (windgen == null ? 0 : windgen.getWindAt(pos));
		//System.out.println(wind);
		//return -10f;
		
		if (Math.abs(wind) < 0.1) return 0;
		return Math.abs(wind) * 70;
	}
	
	
	// Should return:
	// torque according to the wind speed. High wind = large torque   | may be negative it the mill turns the other direction
	// 0 torque if network speed is higher than getWindRotatingPower() or turns the other direction

	@Override
	public float getTorque(MechanicalNetwork network) {
		if (bladeSize == 0) return 0;
//		if (worldObj.isRemote)
//		 new Exception().printStackTrace();
		float rotatingpower = getWindRotatingPower();
		//System.out.println("rot power = " + rotatingpower);
		int dir1 = (int)Math.signum(rotatingpower);
		int dir2 = (int)Math.signum(network.getSpeed());
		
		if (dir1 != dir2 && dir1 != 0 && dir2 != 0) {
			return 0;
		}
		
		if (Math.abs(network.getSpeed()) > Math.abs(rotatingpower * maxSpeedMultiplier))  return 0;
		
		//System.out.println("foobar " + network.getSpeed() + " / " + (rotatingpower * windmillSize));
		
		return rotatingpower * maxSpeedMultiplier / (5 - bladeSize);
	}

	
	// If the network runs at higher speed or the other direction than what the rotor creates, it will act as a resistor.
	@Override
	public float getResistance(MechanicalNetwork network) {
		float rotatingpower = getWindRotatingPower();
		
		int dir1 = (int)Math.signum(rotatingpower);
		int dir2 = (int)Math.signum(network.getSpeed());
		
		if (dir1 != dir2 && dir1 != 0 && dir2 != 0) {
			return Math.abs(rotatingpower * maxSpeedMultiplier);
		}
		
		if (Math.abs(network.getSpeed()) > Math.abs(rotatingpower * maxSpeedMultiplier)) {
			return Math.abs(network.getSpeed()) - Math.abs(rotatingpower * maxSpeedMultiplier);
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
		
		if (getNetwork(null) != null && worldObj != null && !worldObj.isRemote) {
			if (worldObj.rand.nextFloat() < Math.min(0.03f, getNetwork(null).getSpeed() / 1000f)) {
				worldObj.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "vintagecraft:woodcreak", 1.4f, 1f);
			}
		}
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
	




	

}
