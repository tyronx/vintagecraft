package at.tyron.vintagecraft.TileEntity;

import net.minecraft.nbt.NBTTagCompound;

public class TEFarmland extends NetworkTileEntity {
	private int fertility;
    
    public int getFertility() {
		return fertility;
	}
    
    public void setFertility(int fertility) {
		this.fertility = fertility;
	}

	public void consumeFertility(int quantity) {
		if (quantity > 1) {
			fertility -= quantity/2 + worldObj.rand.nextInt(quantity/2 + 1);
		} else {
			fertility--;
		}
		worldObj.markBlockForUpdate(pos);
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
		fertility = nbttagcompound.getInteger("fertility");
		super.readFromNBT(nbttagcompound);
	}

	
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("fertility", fertility);
		super.writeToNBT(nbttagcompound);
	}

}
