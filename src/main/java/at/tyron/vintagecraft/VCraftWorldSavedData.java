package at.tyron.vintagecraft;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class VCraftWorldSavedData extends WorldSavedData {
	long worldTime;
	
	public VCraftWorldSavedData(String name) {
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		worldTime = nbt.getLong("worldTime");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setLong("worldTime", worldTime);
	}
	
	public long getWorldTime() {
		return worldTime;
	}
	
	public void setWorldTime(long worldTime) {
		this.worldTime = worldTime;
		markDirty();
	}

}
