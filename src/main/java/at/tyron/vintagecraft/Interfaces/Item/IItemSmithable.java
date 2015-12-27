package at.tyron.vintagecraft.Interfaces.Item;

import net.minecraft.item.ItemStack;

public interface IItemSmithable extends IItemMainWorkableIngredient {

	public boolean workableOn(int anviltier, ItemStack itemstack, ItemStack itemstackoptional);
	
	
	public ItemStack markOddlyShaped(ItemStack itemstack, boolean flag);
	public boolean isOddlyShaped(ItemStack itemstack);
}
