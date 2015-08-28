package at.tyron.vintagecraft.World.Crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ShapedRecipesVC implements IRecipe {
	public int recipeWidth;
	public int recipeHeight;
	public ItemStack[] recipeItems;
	ItemStack recipeOutput;
	
    public ShapedRecipesVC(int width, int height, ItemStack[] items, ItemStack output) {
        this.recipeWidth = width;
        this.recipeHeight = height;
        this.recipeItems = items;
        this.recipeOutput = output;
    }


    
	@Override
	public boolean matches(InventoryCrafting craftinginventory, World worldIn) {
        for (int x = 0; x <= 3 - this.recipeWidth; ++x) {
            for (int y = 0; y <= 3 - this.recipeHeight; ++y) {
                if (this.checkMatch(craftinginventory, x, y, true)) {
                    return true;
                }

                if (this.checkMatch(craftinginventory, x, y, false)) {
                    return true;
                }
            }
        }

        return false;
	}
	
	
    private boolean checkMatch(InventoryCrafting craftinginventory, int x, int y, boolean reverse) {
        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 3; ++l) {
                int i1 = k - x;
                int j1 = l - y;
                ItemStack itemstack = null;

                if (i1 >= 0 && j1 >= 0 && i1 < this.recipeWidth && j1 < this.recipeHeight) {
                    if (reverse) {
                        itemstack = this.recipeItems[this.recipeWidth - i1 - 1 + j1 * this.recipeWidth];
                    }
                    else {
                        itemstack = this.recipeItems[i1 + j1 * this.recipeWidth];
                    }
                }

                ItemStack itemstack1 = craftinginventory.getStackInRowAndColumn(k, l);

                if (itemstack1 != null || itemstack != null) {
                    if (itemstack1 == null && itemstack != null || itemstack1 != null && itemstack == null) {
                        return false;
                    }

                    if (itemstack.getItem() != itemstack1.getItem()) {
                        return false;
                    }

                    if (itemstack.getMetadata() != 32767 && itemstack.getMetadata() != itemstack1.getMetadata()) {
                        return false;
                    }
                 
                }
            }
        }

        return true;
    }
    

	@Override
	public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
		ItemStack itemstack = this.getRecipeOutput().copy();

        for (int i = 0; i < p_77572_1_.getSizeInventory(); ++i) {
            ItemStack itemstack1 = p_77572_1_.getStackInSlot(i);

            if (itemstack1 != null && itemstack1.hasTagCompound()) {
                itemstack.setTagCompound((NBTTagCompound)itemstack1.getTagCompound().copy());
            }
        }

        return itemstack;
	}

	@Override
	public int getRecipeSize() {
		return this.recipeWidth * this.recipeHeight;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.recipeOutput;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting craftinginventory) {
        ItemStack[] aitemstack = new ItemStack[craftinginventory.getSizeInventory()];

        for (int i = 0; i < aitemstack.length; ++i) {
            ItemStack itemstack = craftinginventory.getStackInSlot(i);
            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }

        return aitemstack;
	}

}
