package at.tyron.vintagecraft.World;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Recipes {

	public static void addRecipes() {
		
		// GameRegistry.addRecipe(new ItemStack(TFCBlocks.Fence2, 6, l), new Object[] { "LPL", "LPL", Character.valueOf('L'), new ItemStack(TFCItems.Logs, 1, i), Character.valueOf('P'), new ItemStack(TFCItems.SinglePlank, 1, i) });
		
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.furnace), new Object[] { "SSS", "S S", "SSS", 'S', new ItemStack(ItemsVC.stone)});
		
		
		GameRegistry.addShapedRecipe(new ItemStack(Items.stone_axe), new Object[] { "SS ", "SW ", " W ", 'S', new ItemStack(ItemsVC.stone), 'W', new ItemStack(Items.stick)});
		
		GameRegistry.addShapedRecipe(new ItemStack(Items.stone_hoe), new Object[] { "SS ", " W ", " W ", 'S', new ItemStack(ItemsVC.stone), 'W',  new ItemStack(Items.stick)});
		GameRegistry.addShapedRecipe(new ItemStack(Items.stone_pickaxe), new Object[] { "SSS", " W ", " W ", 'S', new ItemStack(ItemsVC.stone), 'W',  new ItemStack(Items.stick)});
		GameRegistry.addShapedRecipe(new ItemStack(Items.stone_shovel), new Object[] { " S ", " W ", " W ", 'S', new ItemStack(ItemsVC.stone), 'W', new ItemStack(Items.stick)});	
		
		
		
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.crafting_table), new Object[] { "LL", "LL", 'L', new ItemStack(BlocksVC.log)});
		
	}
}
