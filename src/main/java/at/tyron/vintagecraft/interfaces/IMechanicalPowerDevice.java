package at.tyron.vintagecraft.Interfaces;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import at.tyron.vintagecraft.World.MechanicalNetwork;

public abstract interface IMechanicalPowerDevice {
	public boolean hasConnectorAt(EnumFacing localFacing);
	public boolean isConnectedAt(EnumFacing localFacing);
	
	public void trySetNetwork(MechanicalNetwork network, EnumFacing localFacing);
	public MechanicalNetwork getNetwork(EnumFacing localFacing);
	
	public void propagateNetworkToNeighbours(int propagationId, MechanicalNetwork network, EnumFacing remoteFacing);
	public void propagateDirectionToNeightbours(int propagationId, EnumFacing remoteFacing, boolean clockwise);

	public boolean isClockWiseDirection(EnumFacing localFacing);
	public void setClockWiseDirection(MechanicalNetwork network, boolean clockwise);


	public void onDevicePlaced(World world, BlockPos pos, EnumFacing facing, EnumFacing ontoside);
	public void onDeviceRemoved(World world, BlockPos pos);
	
	
	
	public boolean exists();
	public BlockPos getPosition();
	public void clearNetwork();
	
	
}
