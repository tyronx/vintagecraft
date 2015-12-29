package at.tyron.vintagecraft.Inventory;

import at.tyron.vintagecraft.Item.ItemToolVC;
import at.tyron.vintagecraft.TileEntity.TECarpenterTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCarpenterTable extends Container {

	public InventoryPlayer playerInventory;
	public IInventory containerInv;

	public TECarpenterTable teTable;
	
	public ContainerCarpenterTable(InventoryPlayer inventoryplayer, TECarpenterTable teTable) {
		
		if (teTable == null) throw new RuntimeException("teTable cannot be null!");
		if (inventoryplayer == null) throw new RuntimeException("inventoryplayer cannot be null!");
		
		containerInv = teTable;
		this.teTable = teTable;
		this.playerInventory = inventoryplayer;
		
		initSlots();
	}
	
	
	void initSlots() {		
		this.addSlotToContainer(new SlotCarpenterTable(containerInv, 0, 45, 56));
		this.addSlotToContainer(new SlotCarpenterTable(containerInv, 1, 94, 56));
		this.addSlotToContainer(new SlotPickupOnly(containerInv, 2, 152, 56));
		
		this.addSlotToContainer(new Slot(containerInv, 3, 7, 56));
        
        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
	}

	

	public void checkCraftable() {
		teTable.checkCraftable(this);
		detectAndSendChanges();
	}
	
	
    public void addCraftingToCrafters(ICrafting listener) {
        super.onCraftGuiOpened(listener);
    }



	@Override
	public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer playerIn) {
		ItemStack stack = super.slotClick(slotId, clickedButton, mode, playerIn);
		return stack;
	}
	

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return true;
	}



    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int clickedIndex) {
    	ItemStack returnedStack = null;
		Slot clickedSlot = (Slot)this.inventorySlots.get(clickedIndex);

		if (clickedSlot != null && clickedSlot.getHasStack()) {
			ItemStack clickedStack = clickedSlot.getStack();
			returnedStack = clickedStack.copy();

			if (clickedIndex < 4) {
				if (!this.mergeItemStack(clickedStack, 4, inventorySlots.size(), true))
					return null;
			}
			else if (clickedIndex >= 4 && clickedIndex < inventorySlots.size()) {

				if (clickedStack.getItem() instanceof ItemToolVC && !this.mergeItemStack(clickedStack, 3, 4, false)) {
					return null;
				}
				
				if (SlotAnvil.validItem(clickedStack) && !this.mergeItemStack(clickedStack, 0, 2, false)) {
					return null;
				}
				
			}

			if (clickedStack.stackSize == 0) {
				clickedSlot.putStack((ItemStack)null);
			} else {
				clickedSlot.onSlotChanged();
			}

			if (clickedStack.stackSize == returnedStack.stackSize) {
				return null;
			}

			clickedSlot.onPickupFromSlot(player, clickedStack);
		}
		
		detectAndSendChanges();
		return returnedStack;
    }
}
