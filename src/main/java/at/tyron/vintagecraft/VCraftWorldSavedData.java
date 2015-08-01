package at.tyron.vintagecraft;

import java.util.Hashtable;
import java.util.Random;

import at.tyron.vintagecraft.World.MechanicalNetwork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class VCraftWorldSavedData extends WorldSavedData {
	long worldTime;
	int nightSkyType;
	Random rand;
	NBTTagList networks;
	
	public int getNightSkyType(World world) {
		if (nightSkyType == -1) {
			nightSkyType = world.rand.nextInt(4);
		}
		return nightSkyType;
	}
	
	public VCraftWorldSavedData(String name) {
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		worldTime = nbt.getLong("worldTime");
		
		if (!nbt.hasKey("nightSkyType")) {
			nightSkyType = -1;	
		} else {
			nightSkyType = nbt.getInteger("nightSkyType");
		}

		networks = (NBTTagList)nbt.getTag("mechanicalNetworks");
		MechanicalNetwork.loadNetworksFromTaglist(networks);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setLong("worldTime", worldTime);
		nbt.setInteger("nightSkyType", nightSkyType);
		
		nbt.setTag("mechanicalNetworks", networks = MechanicalNetwork.saveNetworksToTaglist());
		System.out.println("saved networks " + networks);
	}
	
	public long getWorldTime() {
		return worldTime;
	}
	
	public void setWorldTime(long worldTime) {
		this.worldTime = worldTime;
		markDirty();
	}
	
	public NBTTagList getNetworks() {
		return networks;
	}


	public boolean isDirty() {
		return true;
	}
}
