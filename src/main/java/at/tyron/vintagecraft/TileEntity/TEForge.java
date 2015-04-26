package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import net.minecraft.nbt.NBTTagCompound;

public class TEForge extends NetworkTileEntity {
	int fillevel;
	
	
	
	public boolean tryFill() {
		if (fillevel < 10) {
			fillevel++;
			worldObj.markBlockForUpdate(pos);
			return true;
		}
		return false;
	}
	
	public int getFillLevel() {
		return fillevel;
	}

	@Override
	public void createInitNBT(NBTTagCompound nbt) {
		writeToNBT(nbt);
	}
	
	@Override
	public void handleInitPacket(NBTTagCompound nbt) {
		readFromNBT(nbt);
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		fillevel = nbttagcompound.getInteger("fillevel");
		super.readFromNBT(nbttagcompound);
	}

	
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("fillevel", fillevel);
		super.writeToNBT(nbttagcompound);
	}
	
}
