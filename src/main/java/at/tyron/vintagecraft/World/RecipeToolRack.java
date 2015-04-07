package at.tyron.vintagecraft.World;


import at.tyron.vintagecraft.Block.BlockToolRack;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.Item.ItemToolVC;
import at.tyron.vintagecraft.WorldProperties.EnumTool;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeToolRack implements IRecipe {
	BlockClassEntry singleslab;

	@Override
	public boolean matches(InventoryCrafting inventory, World worldIn) {
		int px = -1, py = -1;
		int sx = -1, sy = -1;
		
		for (int x = 0; x < 3; ++x) {
			for (int y = 0; y < 3; ++y) {
				ItemStack itemstack = inventory.getStackInRowAndColumn(y, x);
				if (itemstack == null) continue;
				
				if (itemstack.getItem() instanceof ItemBlock && BlocksVC.singleslab.containsBlock(((ItemBlock)itemstack.getItem()).block)) {
					if (px != -1) {
						return false;
					}
					px = x;
					py = y;
					singleslab = BlocksVC.singleslab.getBlockClassfromMeta((Block) ((ItemBlock)itemstack.getItem()).block, itemstack.getItemDamage());
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
		ItemStack stack = ((BlockToolRack)BlocksVC.toolrack).getItemStackFor(singleslab.getKey());
		stack.stackSize = 1;
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
