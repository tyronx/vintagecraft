package at.tyron.vintagecraft.Item;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.IItemWoodWorkable;
import at.tyron.vintagecraft.Interfaces.ISizedItem;
import at.tyron.vintagecraft.World.Crafting.WorkableRecipeBase;
import at.tyron.vintagecraft.WorldProperties.EnumItemSize;
import net.minecraft.item.ItemStack;

public class ItemCrafted extends ItemVC implements IItemWoodWorkable, ISizedItem {
	EnumItemSize itemsize = null;
	
	public ItemCrafted() {
		setCreativeTab(VintageCraft.resourcesTab);
	}
	
	public ItemCrafted setItemSize(EnumItemSize size) {
		this.itemsize = size;
		return this;
	}
	
	@Override
	public boolean isIngredient(ItemStack itemstack, ItemStack comparison, WorkableRecipeBase forrecipe) {
		return itemstack.stackSize == comparison.stackSize;
	}

	@Override
	public EnumItemSize getItemSize() {
		return itemsize;
	}
}
