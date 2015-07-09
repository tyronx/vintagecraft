package at.tyron.vintagecraft.World;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.management.RuntimeErrorException;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerDevice;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerNetworkNode;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerNetworkRelay;
import at.tyron.vintagecraft.Network.MechanicalNetworkNBT;

public class MechanicalNetwork {
	private static Hashtable<Integer, MechanicalNetwork> networksById = new Hashtable<Integer, MechanicalNetwork>();
	private static int propagationId = 0;

	
	private static Integer findUniqueId() {
		int max = 0;
		for (Integer id : networksById.keySet()) {
			max = Math.max(id, max);
		}
		return max + 1;
	}
	
	public static int getUniquePropagationId() {
		return ++propagationId;
	}
	
	public static MechanicalNetwork getNetworkById(int id) {
		return networksById.get(id);
	}
	
	public static MechanicalNetwork createAndRegisterNetwork(IMechanicalPowerNetworkNode originatorNode) {
		MechanicalNetwork network = createAndRegisterNetwork();
		network.register(originatorNode);
		FMLCommonHandler.instance().bus().register(network);

		return network;
	}
	
	
	public static MechanicalNetwork getOrCreateNetwork(int networkid) {
		MechanicalNetwork network = getNetworkById(networkid);
		if (network == null) {
			network = new MechanicalNetwork(networkid);
			networksById.put(networkid, network);
		}
		return network;
	}

	public static MechanicalNetwork createAndRegisterNetwork() {
		int networkId = findUniqueId();
		//System.out.println("id = " + networkId);
		MechanicalNetwork network = new MechanicalNetwork(networkId);
		
		networksById.put(networkId, network);
		
		return network;
	}

	
	public static void discardNetwork(MechanicalNetwork network) {
		System.out.println("discard network");
		networksById.remove(network.networkId);
		FMLCommonHandler.instance().bus().unregister(network);
	}
	
	
	public void readFromNBT(NBTTagCompound nbt) {
		totalAvailableTorque = nbt.getFloat("totalAvailableTorque");
		totalResistance = nbt.getFloat("totalResistance");
		speed = nbt.getFloat("speed");
		clockwise = nbt.getBoolean("clockwise");
	}
	
	
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("networkId", networkId);
		nbt.setFloat("totalAvailableTorque", totalAvailableTorque);
		nbt.setFloat("totalResistance", totalResistance);
		nbt.setFloat("speed", speed);
		nbt.setBoolean("clockwise", clockwise);
	}

	
	// Currently unused
	// we recreate the network each time
	public static void loadNetworksFromTaglist(NBTTagList networks) {
		networksById.clear();
		
		for (int i = 0; i < networks.tagCount(); i++) {
			NBTTagCompound nbt = networks.getCompoundTagAt(i);
			
			MechanicalNetwork network = new MechanicalNetwork(nbt.getInteger("networkId"));
			
			networksById.put(network.networkId, network);
			network.readFromNBT(nbt);
		}
	}
	
	public static NBTTagList saveNetworksToTaglist() {
		NBTTagList taglist = new NBTTagList();
		
		for (Integer networkId : networksById.keySet()) {
			MechanicalNetwork network = networksById.get(networkId); 
					
			NBTTagCompound nbt = new NBTTagCompound();
			
			network.writeToNBT(nbt);
			taglist.appendTag(nbt);
		}
		
		return taglist;
	}
	
	
	ArrayList<IMechanicalPowerNetworkNode> powerNodes = new ArrayList<IMechanicalPowerNetworkNode>();
	ArrayList<IMechanicalPowerNetworkRelay> powerRelays = new ArrayList<IMechanicalPowerNetworkRelay>();
	
	// A network may only contain devices of same torque and speed.
	// Power converters create a new network
	float totalAvailableTorque;
	float totalResistance;
	float speed;
	
	public final int networkId;
	boolean clockwise;
	
	
	// Only for rendering the rotation
	@SideOnly(Side.CLIENT)
	private float angle = 0;
	@SideOnly(Side.CLIENT)
	private long clientWorldTime = 0;
	
	// Only for rendering the rotation
	@SideOnly(Side.CLIENT)
	public void updateAngle(float speed) {
		angle += (clockwise ? 1 : -1) * speed / 5f;
		angle = angle % 360f;
		

	}
	
	@SideOnly(Side.CLIENT)
	public float getAngle() {
		//System.out.println(angle);
		return angle;
	}


	
	

	
	private MechanicalNetwork(int networkId) {
		speed = 0;
		totalAvailableTorque = 0;
		totalResistance = 0;
		this.networkId = networkId;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public float getAvailableTorque() {
		return totalAvailableTorque;
	}

	public float getTotalResistance() {
		return totalResistance;
	}

	public float getRemainingResistance() {
		return Math.max(0, totalResistance - totalAvailableTorque);
	}
	public float getUnusedTorque() {
		return Math.max(0, totalAvailableTorque - totalResistance);
	}

	public void register(IMechanicalPowerDevice device) {
		if (device instanceof IMechanicalPowerNetworkNode) {
			powerNodes.add((IMechanicalPowerNetworkNode)device);	
		}
		if (device instanceof IMechanicalPowerNetworkRelay) {
			powerRelays.add((IMechanicalPowerNetworkRelay)device);	
		}
	}
	
	public void unregister(IMechanicalPowerDevice device) {
		if (device instanceof IMechanicalPowerNetworkNode) {
			powerNodes.remove((IMechanicalPowerNetworkNode)device);	
		}
		if (device instanceof IMechanicalPowerNetworkRelay) {
			powerRelays.remove((IMechanicalPowerNetworkRelay)device);	
		}
	}

	
	
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
	if (Minecraft.getMinecraft().theWorld != null && clientWorldTime != Minecraft.getMinecraft().theWorld.getWorldTime()) {
			updateAngle(speed);
			clientWorldTime = Minecraft.getMinecraft().theWorld.getWorldTime();
		}
	}
	
	@SubscribeEvent
	public void onServerTick(TickEvent.WorldTickEvent event) {
		//System.out.println("Tick");
		if (
			FMLCommonHandler.instance().getMinecraftServerInstance() == null || 
			event.phase == TickEvent.Phase.END || 
			event.world.provider.getDimensionId() != 0 ||
			event.world.isRemote
		) return;
		
		//System.out.println(event.world.getWorldTime());
		
		if (event.world.getWorldTime() % 5 == 0) {
			//System.out.println("server tick " + event.world.isRemote);
			updateNetwork();
		}
		if (event.world.getWorldTime() % 30 == 0) {
			NBTTagCompound nbt = new NBTTagCompound();
			writeToNBT(nbt);
			//System.out.println("send " + speed + " / " + networkId);
			VintageCraft.packetPipeline.sendToAll(new MechanicalNetworkNBT(nbt, networkId));
			
			//			VintageCraft.packetPipeline.sendToAllAround(
			//new MechanicalNetworkNBT(nbt, networkId), 
			//new TargetPoint(event.world.provider.getDimensionId(), x, y, z, range)

			
		}
	}
	
	// Should run every 5 ticks
	public void updateNetwork() {
		
		/* 1. Verify network */
		IMechanicalPowerNetworkNode[] powernodesArray = powerNodes.toArray(new IMechanicalPowerNetworkNode[0]);
		for (IMechanicalPowerNetworkNode node : powernodesArray) {
			if (!node.exists()) unregister(node);
		}
		if (powerNodes.size() == 0) {
			discardNetwork(this);
		}
		
		
		/* 2. Determine total available torque and total resistance of the network */
		totalAvailableTorque = 0;
		totalResistance = 0;
		for (IMechanicalPowerNetworkNode powerNode : powerNodes) {
			totalAvailableTorque += powerNode.getTorque(this);
			totalResistance += powerNode.getResistance(this);
		}
		
		
		
		/* 3. Unconsumed torque changes the network speed */
		
		// Positive free torque => increase speed until maxSpeed
		// Negative free torque => decrease speed until -maxSpeed
		// No free torque => lower speed until 0
		
		float unusedTorque = Math.abs(totalAvailableTorque) - totalResistance;
		int speedChange = (totalAvailableTorque > 0) ? 1 : -1;
		if (unusedTorque <= 0) {
			speedChange = 0;
		}
		
		//System.out.println("unusedTorque: " + unusedTorque + " / speedChange: " + speedChange);
		float step = 1f;
		
		switch (speedChange) {
			case 1:
				speed = speed + step;
				break;
			
			case -1:
				speed = speed - step;
				break;
			
			case 0:
				if (speed > 0) {
					speed = Math.max(0, speed - step);
				}
				if (speed < 0) {
					speed = Math.min(0, speed + step);
				}
				break;
		}
		
		
		/* 4. Set direction, also did the direction change? Propagate it through the network */
		
		boolean oldclockwise = clockwise;
		if (speed != 0) {
			clockwise = speed > 0;
		}
		
		if (oldclockwise != clockwise) {
			for (IMechanicalPowerNetworkNode powerNode : powerNodes) {
				if (powerNode.getTorque(this) > 0 == clockwise) {
					System.out.println("sent out direction change propagation");
					powerNode.propagateDirectionToNeightbours(
						getUniquePropagationId(), 
						powerNode.getOutputSideForNetworkPropagation(), 
						clockwise
					);
					break;
				}
			}
		}
				
	
		
		//System.out.println("speed: " + speed + " / availtorque: " + totalAvailableTorque + " / availresis: " + totalResistance);
	}

	
	

	
	public void rebuildNetwork() {
		if (powerNodes.size() == 0) return;
		
		IMechanicalPowerNetworkNode node = powerNodes.get(0);
		
		if (node == null) return;
		
		MechanicalNetwork network = createAndRegisterNetwork(node);
		
		node.propagateNetworkToNeighbours(
			getUniquePropagationId(), 
			network, 
			node.getOutputSideForNetworkPropagation()
		);
	}
	
	
	
	
	
}
