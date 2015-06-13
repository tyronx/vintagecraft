package at.tyron.vintagecraft.World;

import java.util.ArrayList;
import at.tyron.vintagecraft.Interfaces.IMechanicalNetworkPowerSourceOrSink;

public class MechanicalNetwork {
	ArrayList<IMechanicalNetworkPowerSourceOrSink> powerSourcesSinks = new ArrayList<IMechanicalNetworkPowerSourceOrSink>();
	
	
	// A network may only contain devices of same torque and speed.
	// Power converters create a new network
	int producedTorque;
	int consumedTorque;
	int speed;
	
	public MechanicalNetwork() {
		speed = 0;
		producedTorque = 0;
		consumedTorque = 0;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public int getProducedTorque() {
		return producedTorque;
	}

	public int getConsumedTorque() {
		return producedTorque;
	}

	
	public void registerPowerSourceOrSink(IMechanicalNetworkPowerSourceOrSink sourceorsink) {
		powerSourcesSinks.add(sourceorsink);
	}
	
	public void unregisterPowerSourceOrSink(IMechanicalNetworkPowerSourceOrSink sourceorsink) {
		powerSourcesSinks.remove(sourceorsink);
	}
	
	
	// Should run every 3-4 ticks
	public void updateNetwork() {
		producedTorque = 0;
		consumedTorque = 0;
		
		// The power source with the largest speed determines the speed of the network
		int largestSpeed = -1;
		IMechanicalNetworkPowerSourceOrSink largestSpeedDevice;
		
		for (IMechanicalNetworkPowerSourceOrSink sourceorsink : powerSourcesSinks) {
			if (sourceorsink.getProducedSpeed() > largestSpeed) {
				largestSpeed = sourceorsink.getProducedSpeed();
				largestSpeedDevice = sourceorsink;
			}
		}
				
		for (IMechanicalNetworkPowerSourceOrSink sourceorsink : powerSourcesSinks) {
			producedTorque += sourceorsink.getProducedTorque(largestSpeed);
			consumedTorque += sourceorsink.getConsumedTorque(largestSpeed);
		}
		
		// Reduce speed to 0 if more torque is consumed than produced
		// Increase speed to max if more torque is produced than consumed
		int step = Math.max(1, largestSpeed / 100);
		
		if (consumedTorque >= producedTorque) {
			speed = Math.max(0, speed - step);
		} else {
			speed = Math.min(largestSpeed, speed + step);
		}
	}
	
	public void loadNetwork() {
		
	}
	
	
	
	
	
}
