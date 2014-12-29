package at.tyron.vintagecraft.interfaces;

import net.minecraft.item.ItemStack;


/* For Items that supply subtypes from an itemstack - required for rendering items */

public interface ISubtypeFromStackPovider {

	public String getSubType(ItemStack stack);
}
