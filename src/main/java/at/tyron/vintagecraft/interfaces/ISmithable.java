package at.tyron.vintagecraft.Interfaces;

import at.tyron.vintagecraft.WorldProperties.EnumAnvilTechnique;
import net.minecraft.item.ItemStack;

public interface ISmithable {

	public boolean workable(ItemStack itemstack, ItemStack itemstackoptional);
	
	public EnumAnvilTechnique []getAnvilRecipe(ItemStack itemstack, ItemStack itemstackoptional);
	
	public ItemStack getSmithingOutput(ItemStack itemstack, ItemStack itemstackoptional);
}
