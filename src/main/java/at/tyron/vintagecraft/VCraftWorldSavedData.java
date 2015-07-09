package at.tyron.vintagecraft;

import java.util.Random;

import at.tyron.vintagecraft.World.MechanicalNetwork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class VCraftWorldSavedData extends WorldSavedData {
	long worldTime;
	int nightSkyType;
	Random rand;
	
	public int getNightSkyType() {
		return nightSkyType;
	}
	
	public VCraftWorldSavedData(String name, Random rand) {
		super(name);
		this.rand = rand;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		worldTime = nbt.getLong("worldTime");
		
		if (!nbt.hasKey("nightSkyType")) {
			nightSkyType = rand.nextInt(4);	
		} else {
			nightSkyType = nbt.getInteger("nightSkyType");
		}
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setLong("worldTime", worldTime);
		nbt.setInteger("nightSkyType", nightSkyType);
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
