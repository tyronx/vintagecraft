package at.tyron.vintagecraft.World;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class VCraftWorldSavedData extends WorldSavedData {
	Random rand;
	NBTTagList networks;
	
	public World world;
	
	public VCraftWorldSavedData(String name) {
		super(name);
	}
	
	public void setWorld(World world) {
		this.world = world;
		MechnicalNetworkManager.addManager(world, networks);
	}

	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		networks = (NBTTagList)nbt.getTag("mechanicalNetworks").copy();
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		
		MechnicalNetworkManager manager = MechnicalNetworkManager.getNetworkManagerForWorld(world);
		if (manager != null) {
			networks = manager.saveNetworksToTaglist();
		}
		
		nbt.setTag("mechanicalNetworks", networks);
	}
	
	
	public NBTTagList getNetworks() {
		return networks;
	}


	public boolean isDirty() {
		return true;
	}
}
