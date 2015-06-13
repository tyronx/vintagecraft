package at.tyron.vintagecraft.Interfaces;

import at.tyron.vintagecraft.World.MechanicalNetwork;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public interface IMechanicalNetworkDevice {

	public boolean canAcceptPowerAt(EnumFacing facing);
	
	public boolean canSupplyPowerAt(EnumFacing facing);
	
	public MechanicalNetwork getNetwork(EnumFacing facing);
	
	public BlockPos getPosition();
	
}



