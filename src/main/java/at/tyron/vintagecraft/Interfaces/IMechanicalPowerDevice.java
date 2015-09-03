package at.tyron.vintagecraft.Interfaces;

import at.tyron.vintagecraft.World.MechanicalNetwork;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract interface IMechanicalPowerDevice {
	public boolean hasConnectorAt(EnumFacing localFacing);
	public boolean isConnectedAt(EnumFacing localFacing);
	
	public void trySetNetwork(int networkId, EnumFacing localFacing);
	public MechanicalNetwork getNetwork(EnumFacing localFacing);
	public EnumFacing getFacing(MechanicalNetwork network);
	
	public void propagateNetworkToNeighbours(int propagationId, int  networkId, EnumFacing remoteFacing);
	public void propagateDirectionToNeightbours(int propagationId, EnumFacing remoteFacing, boolean clockwise);

	public boolean isClockWiseDirection(EnumFacing localFacing);
	public void setClockWiseDirection(int networkId, boolean clockwise);

	public EnumFacing getDirectionFromFacing();

	public void onDevicePlaced(World world, BlockPos pos, EnumFacing facing, EnumFacing ontoside);
	public void onDeviceRemoved(World world, BlockPos pos);
	
	
	
	public boolean exists();
	public BlockPos getPosition();
	
	public MechanicalNetwork[] getNetworks();
	public void clearNetwork();
	
	
}
