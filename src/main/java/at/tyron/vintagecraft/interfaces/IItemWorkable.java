package at.tyron.vintagecraft.Interfaces;

import net.minecraft.item.ItemStack;
import at.tyron.vintagecraft.World.Crafting.AnvilRecipe;
import at.tyron.vintagecraft.World.Crafting.EnumAnvilTechnique;
import at.tyron.vintagecraft.World.Crafting.WorkableRecipeBase;
import at.tyron.vintagecraft.WorldProperties.EnumWorkableTechnique;

public interface IItemWorkable {

	public boolean isIngredient(ItemStack itemstack, ItemStack comparison, WorkableRecipeBase forrecipe);
	
	
	public ItemStack applyTechnique(ItemStack itemstack, EnumWorkableTechnique technique);
	public EnumWorkableTechnique[] getAppliedTechniques(ItemStack itemstack);
}
