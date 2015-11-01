package at.tyron.vintagecraft.Item.Carpentry;

import at.tyron.vintagecraft.Interfaces.IItemWoodWorkable;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.Item.Flora.ItemLogVC;
import at.tyron.vintagecraft.World.Crafting.WorkableRecipeBase;
import at.tyron.vintagecraft.WorldProperties.EnumObjectCategory;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemPlanksVC extends ItemLogVC implements ISubtypeFromStackPovider, IItemWoodWorkable {
	
	public ItemPlanksVC(Block block) {
		super(block);
        this.setHasSubtypes(true);
	}
	
	@Override
	public EnumObjectCategory getCategory() {
		return EnumObjectCategory.Carpentry;
	}

	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (getTreeType(stack) == null) {
			return super.getUnlocalizedName() + ".unknown";
		}
		
		return super.getUnlocalizedName() + "." + getTreeType(stack).getStateName();
	}
	
	
	@Override
	public int getBurningHeat(ItemStack stack) {
		return 700;
	}

	@Override
	public float getBurnDurationMultiplier(ItemStack stack) {
		return 0.5f;
	}

	@Override
	public boolean isIngredient(ItemStack itemstack, ItemStack comparison, WorkableRecipeBase forrecipe) {
		return 
			itemstack.getItemDamage() == comparison.getItemDamage() &&
			itemstack.stackSize == comparison.stackSize &&
			getTreeType(itemstack) == getTreeType(comparison)
		;
	}	
}
