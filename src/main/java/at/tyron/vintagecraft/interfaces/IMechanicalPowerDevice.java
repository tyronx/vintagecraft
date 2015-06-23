package at.tyron.vintagecraft.Interfaces;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import at.tyron.vintagecraft.World.MechanicalNetwork;

public abstract interface IMechanicalPowerDevice {
	public boolean hasConnectorAt(EnumFacing facing);
	public boolean isConnectedAt(EnumFacing facing);
	
	public void trySetNetwork(MechanicalNetwork network, EnumFacing facing);
	public MechanicalNetwork getNetwork(EnumFacing facing);
	public void propagateNetworkToNeighbours(int propagationId, MechanicalNetwork network, EnumFacing fromFacing);

	
	public boolean isClockWiseDirection(EnumFacing facing);

	public void onDevicePlaced(World world, BlockPos pos, EnumFacing facing);
	public void onDeviceRemoved(World world, BlockPos pos);
	
	
	
	public boolean exists();
	public BlockPos getPosition();
	
}
