package at.tyron.vintagecraft;

import at.tyron.vintagecraft.World.MechanicalNetwork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class VCraftWorldSavedData extends WorldSavedData {
	long worldTime;
	
	
	public VCraftWorldSavedData(String name) {
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		worldTime = nbt.getLong("worldTime");
		//MechanicalNetwork.loadNetworksFromTaglist(nbt.getTagList("mechanicalNetworks", Constants.NBT.TAG_LIST));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setLong("worldTime", worldTime);
		//nbt.setTag("mechanicalNetworks", MechanicalNetwork.saveNetworksToTaglist());
	}
	
	public long getWorldTime() {
		return worldTime;
	}
	
	public void setWorldTime(long worldTime) {
		this.worldTime = worldTime;
		markDirty();
	}

}
