package at.tyron.vintagecraft.Inventory;

import at.tyron.vintagecraft.Interfaces.Item.ISizedItem;
import at.tyron.vintagecraft.WorldProperties.EnumItemSize;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
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
    	EnumItemSize size = null;
    	if (stack.getItem() instanceof ISizedItem) {
    		size = ((ISizedItem)stack.getItem()).getItemSize();
    	}
    	
    	return 
    		(size != null && size.getSizeAsNumber() < 40) ||
    		vanillaItem(stack.getItem());
    }

	private static boolean vanillaItem(Item item) {
		return 
			item == Items.arrow ||
			item == Items.bread ||
			item == Items.carrot ||
			item == Items.potato ||
			item == Items.chicken ||
			item == Items.clay_ball ||
			item == Items.egg ||
			item == Items.ender_pearl ||
			item == Items.rotten_flesh ||
			item == Items.feather ||
			item == Items.dye ||
			item == Items.bone ||
			item == Items.gunpowder ||
			item == Items.flint ||
			item == Items.leather ||
			item == Items.string ||
			item == Items.spider_eye ||
			item == Items.wheat
		;
	}
}
