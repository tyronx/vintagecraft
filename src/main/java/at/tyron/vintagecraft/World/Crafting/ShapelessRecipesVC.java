package at.tyron.vintagecraft.World.Crafting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class ShapelessRecipesVC implements IRecipe {
	ItemStack recipeOutput;
	List recipeItems;
	
	public ShapelessRecipesVC(ItemStack output, List inputList) {
        this.recipeOutput = output;
        this.recipeItems = inputList;
	}
	
	
	
	@Override
	public boolean matches(InventoryCrafting craftinginventory, World worldIn) {
        ArrayList arraylist = Lists.newArrayList(this.recipeItems);

        for (int i = 0; i < craftinginventory.getHeight(); ++i) {
            for (int j = 0; j < craftinginventory.getWidth(); ++j) {
                ItemStack itemstack = craftinginventory.getStackInRowAndColumn(j, i);

                if (itemstack != null) {
                    boolean flag = false;
                    Iterator iterator = arraylist.iterator();

                    while (iterator.hasNext()) {
                        ItemStack itemstack1 = (ItemStack)iterator.next();

                        if (ItemStack.areItemStackTagsEqual(itemstack, itemstack1) && itemstack.getItem() == itemstack1.getItem() && (itemstack1.getMetadata() == 32767 || itemstack.getMetadata() == itemstack1.getMetadata())) {
                            flag = true;
                            arraylist.remove(itemstack1);
                            break;
                        }
                    }

                    if (!flag) {
                        return false;
                    }
                }
            }
        }

        return arraylist.isEmpty();
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
		return this.recipeOutput.copy();
	}

	@Override
	public int getRecipeSize() {
		return this.recipeItems.size();
	}

	@Override
	public ItemStack getRecipeOutput() {
		return recipeOutput;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inventory) {
        ItemStack[] aitemstack = new ItemStack[inventory.getSizeInventory()];

        for (int i = 0; i < aitemstack.length; ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }

        return aitemstack;

	}

}
