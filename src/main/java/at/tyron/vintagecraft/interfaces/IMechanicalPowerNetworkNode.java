package at.tyron.vintagecraft.Interfaces;

import at.tyron.vintagecraft.World.MechanicalNetwork;
import net.minecraft.util.EnumFacing;

// Generates or consumes mechanical power. Does not transfer mechanical power
public interface IMechanicalPowerNetworkNode extends IMechanicalPowerDevice {
	public float getMaxSpeed(MechanicalNetwork network);
	
	public float getTorque(MechanicalNetwork network);
	public float getResistance(MechanicalNetwork network);
	
	public EnumFacing getOutputSideForNetworkPropagation();
}
