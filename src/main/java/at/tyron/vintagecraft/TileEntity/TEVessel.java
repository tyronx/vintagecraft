package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.Item.ItemOreVC;
import at.tyron.vintagecraft.Item.ItemStone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class TEVessel extends NetworkTileEntity implements IInventory {
	ItemStack[] storage;
	
	public TEVessel() {
		storage = new ItemStack[getSizeInventory()];
	}
	
	public void setContents(ItemStack[] contents) {
		for (int i = 0; i < getSizeInventory(); i++) {
			if (contents.length > i && contents[i] != null) {
				storage[i] = contents[i].copy();
			} else break;
		}
	}
	
	public ItemStack[] getContents() {
		return storage;
	}
	
	
	public boolean canRenderBreaking() {
		return true;
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
		storage = new ItemStack[getSizeInventory()];
		
		for(int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte idx = nbttagcompound1.getByte("Slot");
			if(idx >= 0 && idx < storage.length) {
				storage[idx] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	


	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		
		NBTTagList nbttaglist = new NBTTagList();
		for(int i = 0; i < storage.length; i++) {
			if(storage[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				storage[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}	
		nbttagcompound.setTag("Items", nbttaglist);
	}

	
	

	@Override
	public void handleDataPacket(NBTTagCompound nbt) {
		readFromNBT(nbt);
	}

	@Override
	public void createDataNBT(NBTTagCompound nbt) {
		writeToNBT(nbt);
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
	public String getName() {
		return "Ceramic Vessel";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText("Ceramic Vessel");
	}

	@Override
	public int getSizeInventory() {
		return 4;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return storage[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
    {
        if (this.storage[index] != null)
        {
            ItemStack itemstack;

            if (this.storage[index].stackSize <= count)
            {
                itemstack = this.storage[index];
                this.storage[index] = null;
                this.markDirty();
                return itemstack;
            }
            else
            {
                itemstack = this.storage[index].splitStack(count);

                if (this.storage[index].stackSize == 0)
                {
                    this.storage[index] = null;
                }

                this.markDirty();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		 if (this.storage[index] != null) {
	            ItemStack itemstack = this.storage[index];
	            this.storage[index] = null;
	            return itemstack;
		 } else {
			 return null;
		 }
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		 this.storage[index] = stack;

	        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
	            stack.stackSize = this.getInventoryStackLimit();
	        }

	        this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
        for (int i = 0; i < this.storage.length; ++i) {
            this.storage[i] = null;
        }
	}
	
}
