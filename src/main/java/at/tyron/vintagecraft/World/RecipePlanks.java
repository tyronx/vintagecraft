package at.tyron.vintagecraft.World;

import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.Block.Organic.BlockLogVC;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.TreeClass;
import at.tyron.vintagecraft.Item.ItemPlanksVC;
import at.tyron.vintagecraft.Item.ItemToolVC;
import at.tyron.vintagecraft.WorldProperties.EnumTool;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipePlanks implements IRecipe {
	BlockClassEntry log;

	@Override
	public boolean matches(InventoryCrafting inventory, World worldIn) {
		int found = 0, quantityfound = 0;
		
		for (int x = 0; x < 3; ++x) {
			for (int y = 0; y < 3; ++y) {
				ItemStack itemstack = inventory.getStackInRowAndColumn(y, x);
				if (itemstack == null) continue;
				
				if (itemstack.getItem() instanceof ItemBlock && BlocksVC.log.containsBlock(((ItemBlock)itemstack.getItem()).block)) {
					found |= 1;
					quantityfound++;
					log = BlocksVC.log.getBlockClassfromMeta((BlockVC) ((ItemBlock)itemstack.getItem()).block, itemstack.getItemDamage());
				}
				
				if (itemstack.getItem() instanceof ItemToolVC && ((ItemToolVC)(itemstack.getItem())).tooltype == EnumTool.SAW) {
					found |= 2;
					quantityfound++;
				}
				
			}
		}
		return (found & 3) == 3 && quantityfound == 2;
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
		ItemStack stack = BlocksVC.planks.getItemStackFor(log.getKey());
		stack.stackSize = 4;
		return stack;
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
