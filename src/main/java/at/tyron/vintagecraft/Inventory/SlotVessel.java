package at.tyron.vintagecraft.Inventory;

import at.tyron.vintagecraft.interfaces.IFuel;
import at.tyron.vintagecraft.item.ItemIngot;
import at.tyron.vintagecraft.item.ItemOreVC;
import at.tyron.vintagecraft.item.ItemStone;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotVessel extends Slot {

	
    public SlotVessel(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
    }

    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof ItemOreVC || stack.getItem() instanceof ItemIngot || stack.getItem() instanceof ItemStone; 
    }

    public int getItemStackLimit(ItemStack stack) {
        return super.getItemStackLimit(stack);
    }

    
    
}
