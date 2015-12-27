package at.tyron.vintagecraft.Interfaces.Item;

import net.minecraft.item.ItemStack;
import at.tyron.vintagecraft.World.Crafting.WorkableRecipeBase;

public interface IItemWorkableIngredient {

	public boolean isIngredient(ItemStack playerstack, ItemStack reciperestack, WorkableRecipeBase forrecipe);
}
