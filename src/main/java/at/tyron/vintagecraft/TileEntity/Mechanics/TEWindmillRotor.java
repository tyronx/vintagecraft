package at.tyron.vintagecraft.TileEntity.Mechanics;

import at.tyron.vintagecraft.Interfaces.IMechanicalPowerDevice;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.World.MechanicalNetwork;
import at.tyron.vintagecraft.World.WindGen;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;



public class TEWindmillRotor extends TEMechanicalNetworkPowerNodeBase implements IUpdatePlayerListBox {
	// The tower mill was more powerful than the water mill, able to generate roughly 20 to 30 horsepower.
	// https://en.wikipedia.org/wiki/Tower_mill#Application
	// 20 hp = 15 KW
	float maxSpeedMultiplier = 1f;
	
	
	public boolean refreshModel;
	
	int bladeSize;  // 0..4
	
	
	public float getAngle() {
		MechanicalNetwork network = getNetwork(null); 
		if (network == null) {
			return lastAngle;
		}
		
		if (directionFromFacing == orientation) {
			return (lastAngle = 360 - network.getAngle());
		}
		
		return (lastAngle = network.getAngle());
	}

	
	public boolean tryAddBlades(EntityPlayer player) {
		if (bladeSize < 4 && getObstacleBlockForRadius(bladeSize + 1) != null) {
			player.addChatMessage(new ChatComponentText("Block in the way!"));
			return false;
		}
		
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
		
		
		
		if (Math.abs(wind) < 0.1) return 0;
		return Math.abs(wind) * 70;
	}
	
	
	// Should return:
	// torque according to the wind speed. High wind = large torque   | may be negative it the mill turns the other direction
	// 0 torque if network speed is higher than getWindRotatingPower() or turns the other direction

	@Override
	public float getTorque(MechanicalNetwork network) {
		if (bladeSize == 0) return 0;
		
		float rotatingpower = getWindRotatingPower();
		
		if (Math.abs(network.getSpeed()) > Math.abs(rotatingpower * maxSpeedMultiplier))  return 0;
		
		return rotatingpower * maxSpeedMultiplier / (5 - bladeSize);
	}

	
	// If the network runs at higher speed or the other direction than what the rotor creates, it will act as a resistor.
	@Override
	public float getResistance(MechanicalNetwork network) {
		// TODO: Uncomment and properly test this code
		
		/*float rotatingpower = getWindRotatingPower();
		
		boolean clockwise1 = orientation == directionFromFacing;
		boolean clockwise2 = network.isClockWise(orientation);
		
		// If windmill and network rotate in opposite directions
		// then it produces no torque but resistance
		if (clockwise1 != clockwise2 && Math.signum(rotatingpower) != 0 && Math.signum(network.getSpeed()) != 0) {
			return Math.abs(rotatingpower * maxSpeedMultiplier);
		}
		
		if (Math.abs(network.getSpeed()) > Math.abs(rotatingpower * maxSpeedMultiplier)) {
			return Math.abs(network.getSpeed()) - Math.abs(rotatingpower * maxSpeedMultiplier);
		}*/
		
		return 0;
	}

	@Override
	public EnumFacing getOutputSideForNetworkPropagation() {
		return orientation.getOpposite();
	}

	long cnt = 0;
	@Override
	public void update() {
		if (getNetwork(null) == null || worldObj == null) return; 
		
		
		if (!worldObj.isRemote) {
			if (worldObj.rand.nextFloat() < Math.min(0.03f, getNetwork(null).getSpeed() / 1000f)) {
				worldObj.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "vintagecraft:woodcreak", 1.4f, 1f);
			}
		}
		
		
		if (!worldObj.isRemote && worldObj.getWorldTime() % 60 == 0 && Math.abs(getNetwork(null).getSpeed()) > 0.01) {
			
			BlockPos closestPos = getObstacleBlockForRadius(bladeSize);
			
			if (closestPos != null) {
				double distanceSq = pos.distanceSq(getPos().getX(), getPos().getY(), getPos().getZ());
				int loss = (int) (bladeSize - Math.sqrt(distanceSq));
				
				bladeSize -= loss;
				
				worldObj.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "mob.zombie.woodbreak", 0.8f, 1f);
				worldObj.markBlockForUpdate(getPos());
				
				ItemStack sails = new ItemStack(ItemsVC.sail, loss * (1 + worldObj.rand.nextInt(4)));
				Block.spawnAsEntity(worldObj, pos, sails);
			}
			
		}
	}


	
	public BlockPos getObstacleBlockForRadius(int radius) {
		BlockPos closestPos = null;
		double closestdistanceSq = 9999;

		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				BlockPos pos = getPos().offset(orientation.rotateY(), x).add(0, y, 0);
				double distanceSq = pos.distanceSq(getPos().getX(), getPos().getY(), getPos().getZ());
				
				if (!worldObj.getBlockState(pos).getBlock().isPassable(worldObj, pos) && distanceSq <= radius*radius) {
					if (closestPos == null || distanceSq < closestdistanceSq) {
						closestdistanceSq = distanceSq;
						closestPos = pos;
					}
				}
				
			}
		}
		
		return closestPos; 
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
	

	@Override
	public AxisAlignedBB getRenderBoundingBox() {		
		return super.getRenderBoundingBox().expand(5, 5, 5);
	}


	

}
