package at.tyron.vintagecraft.World.Crafting;

import at.tyron.vintagecraft.WorldProperties.EnumWorkableTechnique;
import net.minecraft.item.ItemStack;

public class AnvilRecipe extends WorkableRecipeBase {
	public AnvilRecipe(ItemStack output, ItemStack input, EnumWorkableTechnique[] steps) {
		super(output, input, steps);
	}

	public AnvilRecipe(ItemStack output, ItemStack input, EnumAnvilTechnique[] steps, boolean wildcardmatch) {
		super(output, input, steps, wildcardmatch);
	}

	public AnvilRecipe(ItemStack output, ItemStack input, ItemStack input2, EnumAnvilTechnique[] steps) {
		super(output, input, input2, steps);
	}
}
