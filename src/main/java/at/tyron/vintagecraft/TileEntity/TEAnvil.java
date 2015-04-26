package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TEAnvil extends NetworkTileEntity {
	public EnumMetal metal;

	public TEAnvil() {
		metal = EnumMetal.IRON;
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
		metal = EnumMetal.byId(nbttagcompound.getInteger("metal"));
		super.readFromNBT(nbttagcompound);
	}

	
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("metal", metal.getId());
		super.writeToNBT(nbttagcompound);
	}


}
