package at.tyron.vintagecraft.Item;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.IItemWoodWorkable;
import at.tyron.vintagecraft.World.Crafting.WorkableRecipeBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemCrafted extends ItemVC implements IItemWoodWorkable {

	public ItemCrafted() {
		setCreativeTab(VintageCraft.resourcesTab);
	}

	@Override
	public boolean isIngredient(ItemStack itemstack, ItemStack comparison, WorkableRecipeBase forrecipe) {
		return itemstack.stackSize == comparison.stackSize;
	}
}
