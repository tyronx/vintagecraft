package at.tyron.vintagecraft.Interfaces;

import at.tyron.vintagecraft.AnvilRecipes;
import at.tyron.vintagecraft.WorldProperties.EnumAnvilTechnique;
import net.minecraft.item.ItemStack;

public interface ISmithable {

	public boolean workableOn(int anviltier, ItemStack itemstack, ItemStack itemstackoptional);
	
	//public EnumAnvilTechnique []getAnvilRecipe(ItemStack itemstack, ItemStack itemstackoptional);
	
	//public ItemStack getSmithingOutput(ItemStack itemstack, ItemStack itemstackoptional);
	
	public ItemStack applyAnvilTechnique(ItemStack itemstack, EnumAnvilTechnique technique);
	public EnumAnvilTechnique[] getAppliedAnvilTechniques(ItemStack itemstack);
	
	public ItemStack markOddlyShaped(ItemStack itemstack, boolean flag);
	public boolean isOddlyShaped(ItemStack itemstack);
	
	public boolean isSmithingIngredient(ItemStack itemstack, ItemStack comparison, AnvilRecipes forrecipe);
}
