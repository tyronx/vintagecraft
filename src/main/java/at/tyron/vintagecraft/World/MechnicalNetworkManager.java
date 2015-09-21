package at.tyron.vintagecraft.World;

import java.util.Hashtable;
import java.util.Iterator;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerNetworkNode;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;

/* Holds a list mechanical network instances for each world dimension and side (client/server) */
public class MechnicalNetworkManager {
	
	
	public static MechnicalNetworkManager getNetworkManagerForWorld(World world) {
		if (world.isRemote) {
			return VintageCraft.instance.proxy.clientNetworkManagers.get(world.provider.getDimensionId());			
		} else {
			return VintageCraft.instance.proxy.serverNetworkManagers.get(world.provider.getDimensionId());
		}
	}
	
	public static MechnicalNetworkManager addManager(World world, NBTTagList networks) {
		MechnicalNetworkManager manager = new MechnicalNetworkManager(world);
		
		if (world.isRemote) {
			VintageCraft.instance.proxy.clientNetworkManagers.put(world.provider.getDimensionId(), manager);
		} else {
			VintageCraft.instance.proxy.serverNetworkManagers.put(world.provider.getDimensionId(), manager);
		}
		
		manager.loadNetworksFromTaglist(networks);
		
		return manager;
	}
	
	public static void removeManager(World world) {
		
	}
	
	public static void unloadManagers() {
		for (MechnicalNetworkManager manager : VintageCraft.instance.proxy.clientNetworkManagers.values()) {
			manager.unload();
		}
		for (MechnicalNetworkManager manager : VintageCraft.instance.proxy.serverNetworkManagers.values()) {
			manager.unload();
		}
		
		VintageCraft.instance.proxy.clientNetworkManagers.clear();
		VintageCraft.instance.proxy.serverNetworkManagers.clear();
		
	}

	
	
	private void unload() {
		FMLCommonHandler.instance().bus().unregister(this);
		networksById.clear();
	}

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if (world == null || !world.isRemote) return;
		
		if (world.getWorldTime() != lastWorldTime) {
			
			for (MechanicalNetwork network : networksById.values()) {
				network.clientTick(event);
			}
			
			lastWorldTime = world.getWorldTime();
		}
	}
	
	
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		if (world == null || world.isRemote) return;
		
		if (
			FMLCommonHandler.instance().getMinecraftServerInstance() == null || 
			event.phase == TickEvent.Phase.END || 
			event.side == Side.CLIENT
		) return;
		
		
		if (world.getWorldTime() != lastWorldTime) {
			
			Iterator<MechanicalNetwork> it = networksById.values().iterator();
			while (it.hasNext()) {
				MechanicalNetwork network = it.next();
				network.serverTick(world);
				if (network.isDead) {
					System.out.println("network is dead :(");
					it.remove();
				}
			}
			
			//System.out.println(networksById.size());
			
			
			lastWorldTime = world.getWorldTime();
		}

		
	}
	
	
	Hashtable<Integer, MechanicalNetwork> networksById = new Hashtable<Integer, MechanicalNetwork>();
	private int propagationId = 0;
	private World world;
	private long lastWorldTime = 0;

	public MechnicalNetworkManager(World world) {
		this.world = world;
		FMLCommonHandler.instance().bus().register(this);
		
	}
	
	public World getWorld() {
		return world;
	}
	
	public void loadNetworksFromTaglist(NBTTagList networks) {
		//System.out.println("load networks for dimension " + world.provider.getDimensionId() + ", list: " + networks);
		networksById.clear();
		
		if (networks == null) return;
		
		for (int i = 0; i < networks.tagCount(); i++) {
			NBTTagCompound nbt = networks.getCompoundTagAt(i);
			MechanicalNetwork network = new MechanicalNetwork(this, nbt.getInteger("networkId"));
			
			networksById.put(network.networkId, network);
			network.readFromNBT(nbt, false);
			network.rediscoverNetwork(world);
		}
		
		System.out.println(networksById.size());
	}
	
	public NBTTagList saveNetworksToTaglist() {
		
		NBTTagList taglist = new NBTTagList();
		
		for (Integer networkId : networksById.keySet()) {
			MechanicalNetwork network = networksById.get(networkId); 
					
			NBTTagCompound nbt = new NBTTagCompound();
			
			network.writeToNBT(nbt);
			taglist.appendTag(nbt);
		}
		return taglist;
	}
	
	
	
	private Integer findUniqueId() {
		int max = 0;
		for (Integer id : networksById.keySet()) {
			max = Math.max(id, max);
		}
		return max + 1;
	}
	
	public int getUniquePropagationId() {
		return ++propagationId;
	}
	
	public MechanicalNetwork getNetworkById(int id) {
		return networksById.get(id);
	}
	
	public MechanicalNetwork createAndRegisterNetwork(IMechanicalPowerNetworkNode originatorNode) {
		MechanicalNetwork network = createAndRegisterNetwork();
		network.register(originatorNode);
		network.firstPowerNode = originatorNode.getPosition();

		return network;
	}
	
	
	public MechanicalNetwork getOrCreateNetwork(int networkid) {
		MechanicalNetwork network = getNetworkById(networkid);
		if (network == null) {
			//System.out.println("from get instantiated new network with id " + networkid);
			network = new MechanicalNetwork(this, networkid);
			networksById.put(networkid, network);
		}
		return network;
	}

	public MechanicalNetwork createAndRegisterNetwork() {
		int networkId = findUniqueId();
		//System.out.println(world.isRemote + " from create instantiated new network with id " + networkId);

		MechanicalNetwork network = new MechanicalNetwork(this, networkId);
		
		networksById.put(networkId, network);
		
		return network;
	}

	
	public void unloadNetworks() {
		for (int networkid : networksById.keySet()) {
			discardNetwork(getNetworkById(networkid));
		}
	}
	
	public void discardNetwork(MechanicalNetwork network) {
		networksById.remove(network.networkId);
	}
	

}
