package at.tyron.vintagecraft.Inventory;

import at.tyron.vintagecraft.TileEntity.TEAnvil;
import at.tyron.vintagecraft.TileEntity.TEStonePot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerForge extends Container {

	public InventoryPlayer playerInventory;
	public IInventory containerInv;

	public TEStonePot teforge; 
	
	
	public ContainerForge(InventoryPlayer inventoryplayer, TEStonePot teforge) {
		
		if (teforge == null) throw new RuntimeException("teforge cannot be null!");
		if (inventoryplayer == null) throw new RuntimeException("inventoryplayer cannot be null!");
		
		containerInv = teforge;
		this.teforge = teforge ;
		this.playerInventory = inventoryplayer;
		
		initSlots();
	}
	
	
	void initSlots() {		
		this.addSlotToContainer(new SlotHeatable(containerInv, 0, 45, 56));
		this.addSlotToContainer(new SlotFuel(containerInv, 1, 94, 56));
        
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

			if (clickedIndex < 2) {
				if (!this.mergeItemStack(clickedStack, 2, inventorySlots.size(), true))
					return null;
			}
			else if (clickedIndex >= 2 && clickedIndex < inventorySlots.size()) {
				if (SlotHeatable.validItem(clickedStack) && !this.mergeItemStack(clickedStack, 0, 1, false)) {
					return null;
				}
				if (SlotFuel.validItem(clickedStack) && !this.mergeItemStack(clickedStack, 1, 2, false)) {
					return null;
				}
			}

			if (clickedStack.stackSize == 0)
				clickedSlot.putStack((ItemStack)null);
			else
				clickedSlot.onSlotChanged();

			if (clickedStack.stackSize == returnedStack.stackSize)
				return null;

			clickedSlot.onPickupFromSlot(player, clickedStack);
		}
		
		detectAndSendChanges();
		return returnedStack;
    }

    
}
