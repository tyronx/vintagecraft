package at.tyron.vintagecraft.World;

import java.util.ArrayList;
import java.util.List;

import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.TreeClass;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.block.BlockLogVC;
import at.tyron.vintagecraft.block.BlockPlanksVC;
import at.tyron.vintagecraft.block.BlockVC;
import at.tyron.vintagecraft.item.ItemIngot;
import at.tyron.vintagecraft.item.ItemLogVC;
import at.tyron.vintagecraft.item.ItemOreVC;
import at.tyron.vintagecraft.item.ItemPlanksVC;
import at.tyron.vintagecraft.item.ItemStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

public class Recipes {

	public static void addRecipes() {
		ItemStack stick = new ItemStack(Items.stick);

		
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.clayVessel), new Object[] { " C ", "C C", "CCC", 'C', Items.clay_ball});
		
		ItemStack copperingot = new ItemStack(ItemsVC.ingot);
		ItemIngot.setMetal(copperingot, EnumMetal.COPPER);

		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.copperAxe), new Object[] { "SS ", "SW ", " W ", 'S', copperingot, 'W', Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.copperHoe), new Object[] { "SS ", " W ", " W ", 'S', copperingot, 'W',  Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.copperPickaxe), new Object[] { "SSS", " W ", " W ", 'S', copperingot, 'W',  Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.copperShovel), new Object[] { " S ", " W ", " W ", 'S', copperingot, 'W', Items.stick});	
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.copperSword), new Object[] { " S ", " S ", " W ", 'S', copperingot, 'W', Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.copperSaw), new Object[] { "S  ", "WS ", " WS", 'S', copperingot, 'W', Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.copperShears), new Object[] { "  M", "MM ", " M ", 'M', copperingot});


		ItemStack bronzeingot = new ItemStack(ItemsVC.ingot);
		ItemIngot.setMetal(bronzeingot, EnumMetal.BRONZE);

		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.bronzeAxe), new Object[] { "SS ", "SW ", " W ", 'S', bronzeingot, 'W', Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.bronzeHoe), new Object[] { "SS ", " W ", " W ", 'S', bronzeingot, 'W',  Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.bronzePickaxe), new Object[] { "SSS", " W ", " W ", 'S', bronzeingot, 'W',  Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.bronzeShovel), new Object[] { " S ", " W ", " W ", 'S', bronzeingot, 'W', Items.stick});	
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.bronzeSword), new Object[] { " S ", " S ", " W ", 'S', bronzeingot, 'W', Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.bronzeSaw), new Object[] { "S  ", "WS ", " WS", 'S', bronzeingot, 'W', Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.bronzeShears), new Object[] { "  M", "MM ", " M ", 'M', bronzeingot});
		

		ItemStack bismuthbronzeingot = new ItemStack(ItemsVC.ingot);
		ItemIngot.setMetal(bismuthbronzeingot, EnumMetal.BISMUTHBRONZE);

		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.bismuthbronzeAxe), new Object[] { "SS ", "SW ", " W ", 'S', bismuthbronzeingot, 'W', Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.bismuthbronzeHoe), new Object[] { "SS ", " W ", " W ", 'S', bismuthbronzeingot, 'W',  Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.bismuthbronzePickaxe), new Object[] { "SSS", " W ", " W ", 'S', bismuthbronzeingot, 'W',  Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.bismuthbronzeShovel), new Object[] { " S ", " W ", " W ", 'S', bismuthbronzeingot, 'W', Items.stick});	
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.bismuthbronzeSword), new Object[] { " S ", " S ", " W ", 'S', bismuthbronzeingot, 'W', Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.bismuthbronzeSaw), new Object[] { "S  ", "WS ", " WS", 'S', bismuthbronzeingot, 'W', Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.bismuthbronzeShears), new Object[] { "  M", "MM ", " M ", 'M', bismuthbronzeingot});
		
		
		
		//for (EnumRockType rocktype : EnumRockType.values()) {
		for (BlockClassEntry rock : BlocksVC.rock.values()) {
			EnumRockType rocktype = (EnumRockType) rock.getKey();
			ItemStack stone = ItemStone.setRockType(new ItemStack(ItemsVC.stone), rocktype);
			
			GameRegistry.addShapedRecipe(BlocksVC.cobblestone.getItemStackFor(rocktype, 2), new Object[] { "SCS", "CSC", "SCS", 'C', Items.clay_ball, 'S', stone});
			
			GameRegistry.addShapedRecipe(new ItemStack(BlocksVC.stove), new Object[] { "SSS", "S S", "SSS", 'S', stone});
			GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.stoneAxe), new Object[] { "SS ", "SW ", " W ", 'S', stone, 'W', stick});
			GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.stoneHoe), new Object[] { "SS ", " W ", " W ", 'S', stone, 'W',  Items.stick});
			GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.stonePickaxe), new Object[] { "SSS", " W ", " W ", 'S', stone, 'W',  Items.stick});
			GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.stoneShovel), new Object[] { " S ", " W ", " W ", 'S', stone, 'W', Items.stick});	
			GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.stoneSword), new Object[] { " S ", " S ", " W ", 'S', stone, 'W', Items.stick});
		}
		
		for (BlockClassEntry log : BlocksVC.log.values()) {
			ItemStack logstack = new ItemStack(log.block);
			ItemLogVC.withTreeType(logstack, log);
			
			GameRegistry.addShapedRecipe(new ItemStack(Blocks.crafting_table), new Object[] { "LL", "LL", 'L', logstack});
		}
		
		for (BlockClassEntry planks : BlocksVC.planks.values()) {
			ItemStack planksstack = new ItemStack(planks.block);
			ItemPlanksVC.withTreeType(planksstack, planks);
			
			GameRegistry.addShapedRecipe(new ItemStack(Blocks.chest), new Object[] { "LLL", "LCL", "LLL", 'L', planksstack, 'C', copperingot});
			GameRegistry.addShapedRecipe(new ItemStack(Items.boat, 1), new Object[] { "   ", "W W", "WWW", 'W', planks.block});
			
			GameRegistry.addShapedRecipe(new ItemStack(Blocks.wooden_button), new Object[] {"#", '#', planks.block});
			
			GameRegistry.addShapedRecipe(new ItemStack(Items.wooden_hoe, 1), new Object[] { "WW ", " S ", " S ", 'W', planks.block, 'S', Items.stick});
			
			GameRegistry.addShapedRecipe(new ItemStack(Blocks.jukebox, 1), new Object[] { "WWW", "WDW", "WWW", 'W', planks.block, 'D', Items.diamond});
			
			GameRegistry.addShapedRecipe(new ItemStack(Blocks.wooden_pressure_plate, 1), new Object[] { "WW", 'W', planks.block});
			
			GameRegistry.addShapedRecipe(new ItemStack(Items.sign, 1), new Object[] { "WWW", "WWW", " S ", 'W', planks.block, 'S', Items.stick});
			
			GameRegistry.addShapedRecipe(new ItemStack(Blocks.wooden_slab, 1), new Object[] { "   ", "   ", "WWW", 'W', planks.block});
			GameRegistry.addShapedRecipe(new ItemStack(Blocks.trapdoor, 1), new Object[] { "   ", "WWW", "WWW", 'W', planks.block});	
			

			GameRegistry.addShapedRecipe(BlocksVC.fence.getItemStackFor(planks.getKey()), new Object[] { "   ", "WSW", "WSW", 'W', planksstack, 'S', Items.stick});
			GameRegistry.addShapedRecipe(BlocksVC.fencegate.getItemStackFor(planks.getKey()), new Object[] { "   ", "WSW", "WSW", 'S', planksstack, 'W', Items.stick});
			
			GameRegistry.addShapedRecipe(new ItemStack(Items.bed, 1), new Object[] { "   ", "WWW", "PPP", 'W', Blocks.wool, 'P', planksstack});
		}
		
		
		GameRegistry.addShapedRecipe(new ItemStack(Items.oak_door, 1), new Object[] { "WW ", "WW ", "WW ", 'W', BlocksVC.planks.getItemStackFor(EnumTree.OAK)});
		GameRegistry.addShapedRecipe(new ItemStack(Items.spruce_door, 1), new Object[] { "WW ", "WW ", "WW ", 'W', BlocksVC.planks.getItemStackFor(EnumTree.SPRUCE)});
		GameRegistry.addShapedRecipe(new ItemStack(Items.birch_door, 1), new Object[] { "WW ", "WW ", "WW ", 'W', BlocksVC.planks.getItemStackFor(EnumTree.BIRCH)});
		GameRegistry.addShapedRecipe(new ItemStack(Items.acacia_door, 1), new Object[] { "WW ", "WW ", "WW ", 'W', BlocksVC.planks.getItemStackFor(EnumTree.ACACIA)});
		
		/*GameRegistry.addShapedRecipe(new ItemStack(Blocks.oak_fence, 1), new Object[] { "   ", "WSW", "WSW", 'W', BlocksVC.planks.getItemStackFor(EnumTree.OAK), 'S', Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.spruce_fence, 1), new Object[] { "   ", "WSW", "WSW", 'W', BlocksVC.planks.getItemStackFor(EnumTree.SPRUCE), 'S', Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.birch_fence, 1), new Object[] { "   ", "WSW", "WSW", 'W', BlocksVC.planks.getItemStackFor(EnumTree.BIRCH), 'S', Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.acacia_fence, 1), new Object[] { "   ", "WSW", "WSW", 'W', BlocksVC.planks.getItemStackFor(EnumTree.ACACIA), 'S', Items.stick});
*/
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.oak_fence_gate, 1), new Object[] { "   ", "SWS", "SWS", 'W', BlocksVC.planks.getItemStackFor(EnumTree.OAK), 'S', Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.spruce_fence_gate, 1), new Object[] { "   ", "SWS", "SWS", 'W', BlocksVC.planks.getItemStackFor(EnumTree.SPRUCE), 'S', Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.birch_fence_gate, 1), new Object[] { "   ", "SWS", "SWS", 'W', BlocksVC.planks.getItemStackFor(EnumTree.BIRCH), 'S', Items.stick});
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.acacia_fence_gate, 1), new Object[] { "   ", "SWS", "SWS", 'W', BlocksVC.planks.getItemStackFor(EnumTree.ACACIA), 'S', Items.stick});

		GameRegistry.addShapedRecipe(new ItemStack(Blocks.oak_stairs, 1), new Object[] { "W  ", "WW ", "WWW", 'W', BlocksVC.planks.getItemStackFor(EnumTree.OAK)});
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.spruce_stairs, 1), new Object[] { "W  ", "WW ", "WWW", 'W', BlocksVC.planks.getItemStackFor(EnumTree.SPRUCE)});
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.birch_stairs, 1), new Object[] { "W  ", "WW ", "WWW", 'W', BlocksVC.planks.getItemStackFor(EnumTree.BIRCH)});
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.acacia_stairs, 1), new Object[] { "W  ", "WW ", "WWW", 'W', BlocksVC.planks.getItemStackFor(EnumTree.ACACIA)});
		
		
		

		
		
		
		RecipeSorter.register("vintagecraft:planks", RecipePlanks.class, Category.SHAPELESS, "after:minecraft:shapeless");
		List recipes = CraftingManager.getInstance().getRecipeList();
		recipes.add(new RecipePlanks());

		
		RecipeSorter.register("vintagecraft:slabs", RecipeSlabs.class, Category.SHAPELESS, "after:minecraft:shapeless");
		recipes.add(new RecipeSlabs());
		
		
		ItemStack lignite = new ItemStack(ItemsVC.ore);
		ItemOreVC.setOreType(lignite, EnumOreType.LIGNITE);
		
		ItemStack bituminouscoal = new ItemStack(ItemsVC.ore);
		ItemOreVC.setOreType(lignite, EnumOreType.BITUMINOUSCOAL);
		
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.torch, 4), new Object[] { "C", "S", 'C', lignite, 'S', stick});
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.torch, 4), new Object[] { "C", "S", 'C', bituminouscoal, 'S', stick});
		
		
		removeRecipe(new ItemStack(Items.arrow, 4));
		GameRegistry.addShapedRecipe(new ItemStack(Items.arrow, 1), new Object[] { " F ", " S ", " F ", 'F', Items.flint, 'S', Items.stick, 'F', Items.feather});
	}
	
	
	
	
	private static void removeRecipe(ItemStack resultItem) { //Code by yope_fried inspired by pigalot
	    ItemStack recipeResult = null;
	    ArrayList recipes = (ArrayList) CraftingManager.getInstance().getRecipeList();

	    for (int scan = 0; scan < recipes.size(); scan++)
	    {
	        IRecipe tmpRecipe = (IRecipe) recipes.get(scan);
	        if (tmpRecipe instanceof ShapedRecipes)
	        {
	            ShapedRecipes recipe = (ShapedRecipes)tmpRecipe;
	            recipeResult = recipe.getRecipeOutput();
	        }

	        if (tmpRecipe instanceof ShapelessRecipes)
	        {
	            ShapelessRecipes recipe = (ShapelessRecipes)tmpRecipe;
	            recipeResult = recipe.getRecipeOutput();
	        }

	        if (ItemStack.areItemStacksEqual(resultItem, recipeResult))
	        {
	            System.out.println("[YOUR_MOD_NAME] Removed Recipe: " + recipes.get(scan) + " -> " + recipeResult);
	            recipes.remove(scan);
	        }
	    }
	}
}
