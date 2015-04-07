package at.tyron.vintagecraft.Inventory;

import at.tyron.vintagecraft.Interfaces.IFuel;
import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.Item.ItemOreVC;
import at.tyron.vintagecraft.Item.ItemStone;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotVessel extends Slot {

	
    public SlotVessel(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
         return validItem(stack);
    }

    public int getItemStackLimit(ItemStack stack) {
        return super.getItemStackLimit(stack);
    }

    
    public static boolean validItem(ItemStack stack) {
    	return stack.getItem() instanceof ItemOreVC || stack.getItem() instanceof ItemIngot || stack.getItem() instanceof ItemStone;
    }
}