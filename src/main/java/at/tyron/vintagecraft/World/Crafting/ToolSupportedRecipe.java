package at.tyron.vintagecraft.World.Crafting;

import java.util.HashMap;

import com.google.common.collect.Maps;

import at.tyron.vintagecraft.Item.Metalworking.ItemToolVC;
import at.tyron.vintagecraft.WorldProperties.EnumTool;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class ToolSupportedRecipe implements IRecipe {
    public int recipeWidth;
    public int recipeHeight;
    public ItemStack[] recipeItems;
    private ItemStack recipeOutput;
    
   
	public ToolSupportedRecipe(ItemStack stack, Object ... recipeComponents) {
        String s = "";
        int i = 0;
        int j = 0;
        int k = 0;

        if (recipeComponents[i] instanceof String[]) {
            String[] astring = (String[])((String[])recipeComponents[i++]);

            for (int l = 0; l < astring.length; ++l) {
                String s1 = astring[l];
                ++k;
                j = s1.length();
                s = s + s1;
            }
        } else {
            while (recipeComponents[i] instanceof String) {
                String s2 = (String)recipeComponents[i++];
                ++k;
                j = s2.length();
                s = s + s2;
            }
        }

        HashMap hashmap;

        for (hashmap = Maps.newHashMap(); i < recipeComponents.length; i += 2) {
            Character character = (Character)recipeComponents[i];
            ItemStack itemstack1 = null;

            if (recipeComponents[i + 1] instanceof Item) {
                itemstack1 = new ItemStack((Item)recipeComponents[i + 1]);
            }
            else if (recipeComponents[i + 1] instanceof Block) {
                itemstack1 = new ItemStack((Block)recipeComponents[i + 1], 1, 32767);
            }
            else if (recipeComponents[i + 1] instanceof ItemStack) {
                itemstack1 = (ItemStack)recipeComponents[i + 1];
            }

            hashmap.put(character, itemstack1);
        }

        ItemStack[] aitemstack = new ItemStack[j * k];

        for (int i1 = 0; i1 < j * k; ++i1) {
            char c0 = s.charAt(i1);

            if (hashmap.containsKey(Character.valueOf(c0))) {
                aitemstack[i1] = ((ItemStack)hashmap.get(Character.valueOf(c0))).copy();
            } else {
                aitemstack[i1] = null;
            }
        }


        this.recipeWidth = j;
        this.recipeHeight = k;
        this.recipeItems = aitemstack;
        this.recipeOutput = stack;
    }
    
	
	
	
	
    private boolean checkMatch(InventoryCrafting inventory, int x, int y, boolean reverse) {
        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 3; ++l) {
                int i1 = k - x;
                int j1 = l - y;
                ItemStack itemstack = null;

                if (i1 >= 0 && j1 >= 0 && i1 < this.recipeWidth && j1 < this.recipeHeight) {
                    if (reverse) {
                        itemstack = this.recipeItems[this.recipeWidth - i1 - 1 + j1 * this.recipeWidth];
                    } else {
                        itemstack = this.recipeItems[i1 + j1 * this.recipeWidth];
                    }
                }

                ItemStack itemstack1 = inventory.getStackInRowAndColumn(k, l);

                if (itemstack1 != null || itemstack != null) {
                    if (itemstack1 == null && itemstack != null || itemstack1 != null && itemstack == null) {
                        return false;
                    }
                    
                    EnumTool tool = null;
                    if (itemstack.getItem() instanceof ItemToolVC) {
                    	tool = ((ItemToolVC)itemstack.getItem()).tooltype;
                    }
                    
                    if (tool != null && itemstack1.getItem() instanceof ItemToolVC && ((ItemToolVC)itemstack1.getItem()).tooltype == tool) {
                    	
                    } else {

	                    if (itemstack.getItem() != itemstack1.getItem()) {
	                        return false;
	                    }
	
	                    if (itemstack.getMetadata() != 32767 && itemstack.getMetadata() != itemstack1.getMetadata()) {
	                        return false;
	                    }
                    }
                }
            }
        }

        return true;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    

    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
    }

    public ItemStack[] getRemainingItems(InventoryCrafting inventory) {
        ItemStack[] aitemstack = new ItemStack[inventory.getSizeInventory()];

        for (int i = 0; i < aitemstack.length; ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }

        return aitemstack;
    }


    public boolean matches(InventoryCrafting inventory, World worldIn) {
        for (int i = 0; i <= 3 - this.recipeWidth; ++i) {
            for (int j = 0; j <= 3 - this.recipeHeight; ++j) {
                if (this.checkMatch(inventory, i, j, true)) {
                    return true;
                }

                if (this.checkMatch(inventory, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

  
    public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
        ItemStack itemstack = this.getRecipeOutput().copy();

        return itemstack;
    }
    

    public int getRecipeSize() {
        return this.recipeWidth * this.recipeHeight;
    }
}
