package at.tyron.vintagecraft.Inventory;

import at.tyron.vintagecraft.Interfaces.IItemFuel;
import at.tyron.vintagecraft.Interfaces.IItemHeatable;
import at.tyron.vintagecraft.Interfaces.ISizedItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotHeatable extends Slot {

    public SlotHeatable(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
    }

    public boolean isItemValid(ItemStack stack) {
        return validItem(stack);
    }

    public int getItemStackLimit(ItemStack stack) {
        return super.getItemStackLimit(stack);
    }
    
    public static boolean validItem(ItemStack stack) {
    	return (stack.getItem() instanceof IItemHeatable) && ((IItemHeatable)stack.getItem()).heatableUntil(stack) > 0;
    }

    
}
