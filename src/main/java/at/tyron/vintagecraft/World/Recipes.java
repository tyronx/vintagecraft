package at.tyron.vintagecraft.World;

import java.util.List;

import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.block.BlockLogVC;
import at.tyron.vintagecraft.block.BlockPlanksVC;
import at.tyron.vintagecraft.item.ItemIngot;
import at.tyron.vintagecraft.item.ItemLogVC;
import at.tyron.vintagecraft.item.ItemOre;
import at.tyron.vintagecraft.item.ItemPlanksVC;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

public class Recipes {

	public static void addRecipes() {
		ItemStack stick = new ItemStack(Items.stick);
		
		GameRegistry.addShapedRecipe(new ItemStack(BlocksVC.stove), new Object[] { "SSS", "S S", "SSS", 'S', new ItemStack(ItemsVC.stone)});
		
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.stoneAxe), new Object[] { "SS ", "SW ", " W ", 'S', new ItemStack(ItemsVC.stone), 'W', stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.stoneHoe), new Object[] { "SS ", " W ", " W ", 'S', new ItemStack(ItemsVC.stone), 'W',  Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.stonePickaxe), new Object[] { "SSS", " W ", " W ", 'S', new ItemStack(ItemsVC.stone), 'W',  Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.stoneShovel), new Object[] { " S ", " W ", " W ", 'S', new ItemStack(ItemsVC.stone), 'W', Items.stick});	
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.stoneSword), new Object[] { " S ", " S ", " W ", 'S', new ItemStack(ItemsVC.stone), 'W', Items.stick});
		
		
		ItemStack copperingot = new ItemStack(ItemsVC.ingot);
		ItemIngot.setMetal(copperingot, EnumMetal.COPPER);

		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.copperAxe), new Object[] { "SS ", "SW ", " W ", 'S', copperingot, 'W', Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.copperHoe), new Object[] { "SS ", " W ", " W ", 'S', copperingot, 'W',  Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.copperPickaxe), new Object[] { "SSS", " W ", " W ", 'S', copperingot, 'W',  Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.copperShovel), new Object[] { " S ", " W ", " W ", 'S', copperingot, 'W', Items.stick});	
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.copperSword), new Object[] { " S ", " S ", " W ", 'S', copperingot, 'W', Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.copperSaw), new Object[] { "S  ", "WS ", " WS", 'S', copperingot, 'W', Items.stick});

		
		
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.crafting_table), new Object[] { "LL", "LL", 'L', new ItemStack(BlocksVC.log)});
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.chest), new Object[] { "LLL", "L L", "LLL", 'L', new ItemStack(BlocksVC.planks)});

		
		RecipeSorter.register("vintagecraft:findings", RecipePlanks.class, Category.SHAPELESS, "after:minecraft:shapeless");
		List recipes = CraftingManager.getInstance().getRecipeList();
		recipes.add(new RecipePlanks());

		
		
		
		ItemStack lignite = new ItemStack(ItemsVC.ore);
		ItemOre.setOreType(lignite, EnumMaterialDeposit.LIGNITE);
		
		ItemStack bituminouscoal = new ItemStack(ItemsVC.ore);
		ItemOre.setOreType(lignite, EnumMaterialDeposit.BITUMINOUSCOAL);
		
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.torch, 4), new Object[] { "C", "S", 'C', lignite, 'S', stick});
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.torch, 4), new Object[] { "C", "S", 'C', bituminouscoal, 'S', stick});
		
	}
}
