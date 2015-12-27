package at.tyron.vintagecraft.Interfaces.Item;

import at.tyron.vintagecraft.World.Crafting.WorkableRecipeBase;
import at.tyron.vintagecraft.WorldProperties.EnumWorkableTechnique;
import net.minecraft.item.ItemStack;

public interface IItemMainWorkableIngredient extends IItemWorkableIngredient {
	public ItemStack applyTechnique(ItemStack itemstack, EnumWorkableTechnique technique);
	public EnumWorkableTechnique[] getAppliedTechniques(ItemStack itemstack);
}
