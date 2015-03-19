package at.tyron.vintagecraft.Inventory;

import at.tyron.vintagecraft.interfaces.IFuel;
import at.tyron.vintagecraft.interfaces.ISmeltable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class ContainerVessel extends Container {
	public InventoryPlayer playerInventory;
	public NBTTagCompound bagNbt;
	public InventoryCrafting containerInv = new InventoryCrafting(this, 2, 2);
	
	public ContainerVessel(InventoryPlayer inventoryplayer, NBTTagCompound bagNbt) {
		this.playerInventory = inventoryplayer;
		this.bagNbt = bagNbt;
		
		this.addSlotToContainer(new Slot(containerInv, 0, 71, 25));
		this.addSlotToContainer(new Slot(containerInv, 1, 89, 25));
		this.addSlotToContainer(new Slot(containerInv, 2, 71, 43));
		this.addSlotToContainer(new Slot(containerInv, 3, 89, 43));
        
        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inventoryplayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inventoryplayer, i, 8 + i * 18, 142));
        }        

        
		//if(!world.isRemote) {
			loadBagInventory();
		//}
	}

	

	public void loadBagInventory() {
		if(bagNbt != null) {
			NBTTagList nbttaglist = bagNbt.getTagList("Items", 10);
			for(int i = 0; i < nbttaglist.tagCount(); i++) {
				NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
				byte byte0 = nbttagcompound1.getByte("Slot");
				if(byte0 >= 0 && byte0 < 4) {
					ItemStack is = ItemStack.loadItemStackFromNBT(nbttagcompound1);
					if (is == null) continue;
					if(is.stackSize >= 1)
						this.containerInv.setInventorySlotContents(byte0, is);
					else
						this.containerInv.setInventorySlotContents(byte0, null);
				}
			}
		}
	}
	

	@Override
	public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer playerIn) {
		ItemStack stack = super.slotClick(slotId, clickedButton, mode, playerIn);
		//System.out.println(stack);
		saveContents(playerInventory.getCurrentItem());
		return stack;
	}


	public void saveContents(ItemStack is) {
		//System.out.println(is);
		NBTTagList nbttaglist = new NBTTagList();
		for(int i = 0; i < containerInv.getSizeInventory(); i++) {
			if(containerInv.getStackInSlot(i) != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				containerInv.getStackInSlot(i).writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		if(is != null) {
			if(!is.hasTagCompound()) {
				is.setTagCompound(new NBTTagCompound());
			}
			is.getTagCompound().setTag("Items", nbttaglist);
		}
	}

	/*@Override
	public ItemStack loadContents(int slot) {
		if(player.inventory.getStackInSlot(bagsSlotNum) != null && 
				player.inventory.getStackInSlot(bagsSlotNum).hasTagCompound())
		{
			NBTTagList nbttaglist = player.inventory.getStackInSlot(bagsSlotNum).getTagCompound().getTagList("Items", 10);
			if(nbttaglist != null)
			{
				for(int i = 0; i < nbttaglist.tagCount(); i++)
				{
					NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
					byte byte0 = nbttagcompound1.getByte("Slot");
					if(byte0 == slot)
						return ItemStack.loadItemStackFromNBT(nbttagcompound1);
				}
			}
		}
		return null;
	}*/

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return true;
	}



    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index != 1 && index != 0) {
                /*if (itemstack1.getItem() instanceof ISmeltable && ((ISmeltable)itemstack1.getItem()).getSmelted(itemstack1) != null) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (itemstack1.getItem() instanceof IFuel && ((IFuel)itemstack1.getItem()).getBurningHeat(itemstack1) != 0) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                        return null;
                    }
                }
                else*/ if (index >= 3 && index < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                        return null;
                    }
                }
                else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack)null);
            }
            else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(playerIn, itemstack1);
        }

        return itemstack;
    }
}
