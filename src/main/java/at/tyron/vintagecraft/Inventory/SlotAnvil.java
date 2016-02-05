package at.tyron.vintagecraft.Inventory;

import at.tyron.vintagecraft.Interfaces.Item.IItemSmithable;
import at.tyron.vintagecraft.Interfaces.Item.IItemWorkableIngredient;
import at.tyron.vintagecraft.World.BlocksVC;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class SlotAnvil extends Slot {

	
    public SlotAnvil(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
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
    	return stack.getItem() instanceof IItemWorkableIngredient;
    }
}
