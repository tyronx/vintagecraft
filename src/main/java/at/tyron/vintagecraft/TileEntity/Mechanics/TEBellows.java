package at.tyron.vintagecraft.TileEntity.Mechanics;

import at.tyron.vintagecraft.Block.Metalworking.BlockFurnaceSection;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerDevice;
import at.tyron.vintagecraft.TileEntity.TEFurnaceSection;
import at.tyron.vintagecraft.World.MechanicalNetwork;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TEBellows extends TEMechanicalNetworkPowerNodeBase implements IUpdatePlayerListBox {
	public boolean refreshModel = false;
	public boolean connectedToFurnace = false;

	float lastSqueeze;
	int lastDirection = 0;

	@Override
	public boolean hasConnectorAt(EnumFacing facing) {
		return facing == orientation.getOpposite();
	}
	
	// Connector is at the backside of the rotor
	@Override
	public boolean isConnectedAt(EnumFacing facing) {
		return facing == orientation.getOpposite() && super.isConnectedAt(facing); 
	}

	

	@Override
	public float getTorque(MechanicalNetwork network) {
		return 0;
	}

	@Override
	public float getResistance(MechanicalNetwork network) {
		return 5f;
	}

	@Override
	public EnumFacing getOutputSideForNetworkPropagation() {
		return orientation.getOpposite();
	}

	
	@Override
	public void onDevicePlaced(World world, BlockPos pos, EnumFacing facing, EnumFacing ontoside) {
		this.orientation = facing;
		connectToNeighbour();
		super.onDevicePlaced(world, pos, facing, ontoside);
	}

	
	public void connectToFurnace() {
		IBlockState state = worldObj.getBlockState(pos.offset(orientation, 2));
		
		connectedToFurnace =
			state.getBlock() instanceof BlockFurnaceSection &&
			((EnumFacing)state.getValue(BlockFurnaceSection.FACING)) == orientation.getOpposite()
		;
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
		
		connectToFurnace();
	}

	
	
	@Override
	public void update() {
		
		if (worldObj != null && getNetwork(orientation) != null && getNetwork(orientation).getSpeed() > 1f) {
			float angle = getAngle();
			float squeeze = MathHelper.sin((float) ((angle+90) / 180 * Math.PI)) / 6 + 0.166f;	
			int direction = (squeeze - lastSqueeze) > 0 ? 1 : -1;
			float pitch = getNetwork(orientation).getSpeed() / 30f;
			
			if (direction != lastDirection && Math.abs(squeeze - lastSqueeze) > 0.000001f) {
				if (worldObj.isRemote) {
					if (direction > 0) {
						((WorldClient)worldObj).playSoundAtPos(pos, "vintagecraft:air_blow", pitch/4, pitch, false);
					} else {
						((WorldClient)worldObj).playSoundAtPos(pos, "vintagecraft:air_suck", pitch/16, pitch/2, false);
					}
				}
				
				
				if (direction > 0) {
					
					connectToFurnace();
					if (connectedToFurnace) {
						IBlockState state = worldObj.getBlockState(pos.offset(orientation, 2).down());
						if (state.getBlock() instanceof BlockFurnaceSection) {
							TEFurnaceSection tefs = (TEFurnaceSection) worldObj.getTileEntity(pos.offset(orientation, 2).down());
							tefs.receiveAirBlowFromBellows();
						}
					}
				}
				
					
				lastDirection = direction;
			}
			
			lastSqueeze = squeeze;
		}
	}
	

	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().expand(5f, 5f, 5f);
	}

}
