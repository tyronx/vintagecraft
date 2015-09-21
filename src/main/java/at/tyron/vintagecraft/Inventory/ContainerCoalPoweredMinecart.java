package at.tyron.vintagecraft.Inventory;

import at.tyron.vintagecraft.Entity.EntityCoalPoweredMinecartVC;
import at.tyron.vintagecraft.Interfaces.IItemFuel;
import at.tyron.vintagecraft.Interfaces.IItemSmeltable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerCoalPoweredMinecart extends Container {
	int[]fields;
	IInventory minecart;
	
	public ContainerCoalPoweredMinecart(InventoryPlayer inventoryplayer, IInventory minecart) {
        this.addSlotToContainer(new SlotFuel(minecart, 0, 84, 46));
        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inventoryplayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inventoryplayer, i, 8 + i * 18, 142));
        }
        
		fields = new int[minecart.getFieldCount()];
		this.minecart = minecart;
	}
	
	

    public void addCraftingToCrafters(ICrafting listener) {
        super.addCraftingToCrafters(listener);
        
        listener.func_175173_a(this, minecart);
    }
	
	@Override
    public void detectAndSendChanges() {
		
		super.detectAndSendChanges();
		
        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);
            
            for (int fieldid = 0; fieldid < minecart.getFieldCount(); fieldid++) {
            	
            	if (fields[fieldid] != minecart.getField(fieldid)) {
            		icrafting.sendProgressBarUpdate(this, fieldid, minecart.getField(fieldid));
            	}
            }
        }
        
        for (int fieldid = 0; fieldid < minecart.getFieldCount(); fieldid++) {
        	fields[fieldid] = minecart.getField(fieldid);
        }
    }
	
	

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
    	minecart.setField(id, data);
    }


    
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            if (index != 0) {
            	
            	if (EntityCoalPoweredMinecartVC.getFuelValue(itemstack1) > 0) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, true)) {
                        return null;
                    }
                }
                else if (index > 0 && index < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 37, false)) {
                        return null;
                    }
                }
                else if (index >= 30 && index < 37 && !this.mergeItemStack(itemstack1, 1, 30, false)) {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 1, 37, false)) {
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


    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return minecart.isUseableByPlayer(playerIn);
    }

    

}
