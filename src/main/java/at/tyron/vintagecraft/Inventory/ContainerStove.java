package at.tyron.vintagecraft.Inventory;

import at.tyron.vintagecraft.Interfaces.Item.IItemFuel;
import at.tyron.vintagecraft.Interfaces.Item.IItemSmeltable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerStove extends Container {
	int[]fields;
	IInventory stove;
	
	public ContainerStove(InventoryPlayer inventoryplayer, IInventory furnaceInventory) {
		this.stove = furnaceInventory;
        this.addSlotToContainer(new Slot(furnaceInventory, 0, 56, 17));
        this.addSlotToContainer(new SlotFuel(furnaceInventory, 1, 56, 53));
        this.addSlotToContainer(new SlotFurnaceOutputVC(inventoryplayer.player, furnaceInventory, 2, 116, 35));
        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inventoryplayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inventoryplayer, i, 8 + i * 18, 142));
        }
        
		fields = new int[furnaceInventory.getFieldCount()];
		stove = furnaceInventory;
	}
	
	

    public void addCraftingToCrafters(ICrafting listener) {
        super.addCraftingToCrafters(listener);
        
        listener.func_175173_a(this, stove);
    }
	
	@Override
    public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);
            
            for (int fieldid = 0; fieldid < stove.getFieldCount(); fieldid++) {
            	
            	if (fields[fieldid] != stove.getField(fieldid)) {
            		
            		icrafting.sendProgressBarUpdate(this, fieldid, stove.getField(fieldid));
            	}
            }
        }
        
        for (int fieldid = 0; fieldid < stove.getFieldCount(); fieldid++) {
        	fields[fieldid] = stove.getField(fieldid);
        }
    }
	
	

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        stove.setField(id, data);
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
                if (itemstack1.getItem() instanceof IItemSmeltable && ((IItemSmeltable)itemstack1.getItem()).getSmelted(itemstack1) != null) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (itemstack1.getItem() instanceof IItemFuel && ((IItemFuel)itemstack1.getItem()).getBurningHeat(itemstack1) != 0) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                        return null;
                    }
                }
                else if (index >= 3 && index < 30) {
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


    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return stove.isUseableByPlayer(playerIn);
    }

    
}
