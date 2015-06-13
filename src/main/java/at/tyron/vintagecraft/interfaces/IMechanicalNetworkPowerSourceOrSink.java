package at.tyron.vintagecraft.Interfaces;

import net.minecraft.util.EnumFacing;

public interface IMechanicalNetworkPowerSourceOrSink extends IMechanicalNetworkDevice {
	public int getProducedSpeed();
	
	public int getProducedTorque(int speed);
	public int getConsumedTorque(int speed);
}
