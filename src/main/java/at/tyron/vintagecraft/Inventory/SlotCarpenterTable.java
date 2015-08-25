package at.tyron.vintagecraft.Inventory;
import at.tyron.vintagecraft.Interfaces.IItemWoodWorkable;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


public class SlotCarpenterTable extends Slot {

    public SlotCarpenterTable(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
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
    	return stack.getItem() instanceof IItemWoodWorkable;
    }
}
