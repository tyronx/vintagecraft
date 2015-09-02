package at.tyron.vintagecraft.Interfaces;

import net.minecraft.item.ItemStack;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;

public interface IItemMetalTyped {
	
	public ItemStack setItemMetal(ItemStack itemstack, EnumMetal metal);
	public EnumMetal getItemMetal(ItemStack itemstack);
}
