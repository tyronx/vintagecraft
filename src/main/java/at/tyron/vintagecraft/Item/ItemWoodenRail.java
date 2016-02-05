package at.tyron.vintagecraft.Item;

import at.tyron.vintagecraft.Interfaces.Item.IItemMainWorkableIngredient;
import at.tyron.vintagecraft.Interfaces.Item.IItemWoodWorkable;
import at.tyron.vintagecraft.Interfaces.Item.IItemWorkableIngredient;
import at.tyron.vintagecraft.World.Crafting.WorkableRecipeBase;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemWoodenRail extends ItemBlockVC implements IItemWoodWorkable {

	public ItemWoodenRail(Block block) {
		super(block);
	}

	@Override
	public boolean isIngredient(ItemStack playerstack, ItemStack reciperestack, WorkableRecipeBase forrecipe) {
		return playerstack.getItem() == reciperestack.getItem() && playerstack.stackSize == reciperestack.stackSize;
	}

}
