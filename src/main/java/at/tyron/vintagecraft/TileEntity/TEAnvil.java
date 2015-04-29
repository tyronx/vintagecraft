package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

public class TEAnvil extends TENoGUIInventory {
	public EnumMetal metal;

	public TEAnvil() {
		metal = EnumMetal.IRON;
		storage = new ItemStack[3];
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



	@Override
	public int getSizeInventory() {
		return 3;
	}



	@Override
	public String getName() {
		return "Anvil";
	}



	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText("Anvil");
	}


}
