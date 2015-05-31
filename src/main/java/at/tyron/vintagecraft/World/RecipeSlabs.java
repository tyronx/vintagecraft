package at.tyron.vintagecraft.World;

import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.Block.Organic.BlockLogVC;
import at.tyron.vintagecraft.Block.Organic.BlockPlanksVC;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.Interfaces.IMultiblock;
import at.tyron.vintagecraft.Item.ItemToolVC;
import at.tyron.vintagecraft.WorldProperties.EnumTool;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;


/* There's probably a better way to do this :| */

public class RecipeSlabs implements IRecipe {
	BlockClassEntry planks;

	@Override
	public boolean matches(InventoryCrafting inventory, World worldIn) {
		int px = -1, py = -1;
		int sx = -1, sy = -1;
		
		for (int x = 0; x < 3; ++x) {
			for (int y = 0; y < 3; ++y) {
				ItemStack itemstack = inventory.getStackInRowAndColumn(y, x);
				if (itemstack == null) continue;
				
				if (itemstack.getItem() instanceof ItemBlock && BlocksVC.planks.containsBlock(((ItemBlock)itemstack.getItem()).block)) {
					if (px != -1) {
						return false;
					}
					px = x;
					py = y;
					planks = BlocksVC.planks.getBlockClassfromMeta((BlockVC) ((ItemBlock)itemstack.getItem()).block, itemstack.getItemDamage());
					continue;
				}
				
				if (itemstack.getItem() instanceof ItemToolVC && ((ItemToolVC)(itemstack.getItem())).tooltype == EnumTool.SAW) {
					if (sx != -1) {
						return false;
					}
					sx = x;
					sy = y;
					continue;
				}
				
				return false;
			}
		}
		
		
		return sx > 0 && px > 0 && (py - 1 == sy || py + 1 == sy) && px == sx;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
		return getRecipeOutput();
	}

	@Override
	public int getRecipeSize() {
		return 2*2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		if (planks == null) planks = BlocksVC.planks.getFromKey(EnumTree.SPRUCE);
		
		return BlocksVC.singleslab.getItemStackFor(planks.getKey(), 2);
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting p_179532_1_) {
        ItemStack[] aitemstack = new ItemStack[p_179532_1_.getSizeInventory()];

        for (int i = 0; i < aitemstack.length; ++i) {
            ItemStack itemstack = p_179532_1_.getStackInSlot(i);
            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }

        return aitemstack;
	}
}
