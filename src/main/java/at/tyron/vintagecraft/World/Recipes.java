package at.tyron.vintagecraft.World;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.Block.Organic.BlockLogVC;
import at.tyron.vintagecraft.Block.Organic.BlockPlanksVC;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.TreeClass;
import at.tyron.vintagecraft.Item.ItemArmorVC;
import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.Item.ItemLogVC;
import at.tyron.vintagecraft.Item.ItemOreVC;
import at.tyron.vintagecraft.Item.ItemPlanksVC;
import at.tyron.vintagecraft.Item.ItemStone;
import at.tyron.vintagecraft.Item.ItemToolVC;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.EnumTool;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

public class Recipes {
	static  List recipes = CraftingManager.getInstance().getRecipeList();
	
	static void registerToSorter() {
		RecipeSorter.register("vintagecraft:slabs", RecipeSlabs.class, Category.SHAPELESS, "after:minecraft:shapeless");
		RecipeSorter.register("vintagecraft:planks", RecipePlanks.class, Category.SHAPELESS, "after:minecraft:shapeless");
		RecipeSorter.register("vintagecraft:toolrack", RecipeToolRack.class, Category.SHAPELESS, "after:minecraft:shapeless");
		RecipeSorter.register("vintagecraft:toolsupportedrecipe", ToolSupportedRecipe.class, Category.SHAPED, "after:minecraft:shaped");
	}
	
	
	public static void addRecipes() {
		registerToSorter();
		
		ItemStack stick = new ItemStack(Items.stick);
		ItemStack copperingot = ItemIngot.setMetal(new ItemStack(ItemsVC.ingot), EnumMetal.COPPER);
		ItemStack tinbronzeingot = ItemIngot.setMetal(new ItemStack(ItemsVC.ingot), EnumMetal.TINBRONZE);
		ItemStack bismuthbronzeingot = ItemIngot.setMetal(new ItemStack(ItemsVC.ingot), EnumMetal.BISMUTHBRONZE);
		ItemStack ironingot = ItemIngot.setMetal(new ItemStack(ItemsVC.ingot), EnumMetal.IRON);
		
		EnumMetal[] metals = new EnumMetal[]{EnumMetal.COPPER, EnumMetal.TINBRONZE, EnumMetal.BISMUTHBRONZE, EnumMetal.IRON};
		

		
		for (EnumMetal metal : metals) {
			ItemToolVC []tools = ItemsVC.getTools(metal.getName());
			ItemStack ingot = ItemIngot.setMetal(new ItemStack(ItemsVC.ingot), metal);

			for (ItemToolVC tool : tools) {
				Object []recipe = null;
				switch(tool.tooltype) {
					case AXE:     recipe = new Object[] { "SS ", "SW ", " W ", 'S', ingot, 'W', stick}; break;
					case HOE:     recipe = new Object[] { "SS ", " W ", " W ", 'S', ingot, 'W',  stick}; break;
					case PICKAXE: recipe = new Object[] { "SSS", " W ", " W ", 'S', ingot, 'W', stick}; break;
					case SAW:     recipe = new Object[] { "S  ", "WS ", " WS", 'S', ingot, 'W', stick}; break;
					case SHEARS:  recipe = new Object[] { "  M", "MM ", " M ", 'M', ingot}; break;
					case SHOVEL:  recipe = new Object[] { " S ", " W ", " W ", 'S', ingot, 'W', stick}; break;
					case SWORD:   recipe = new Object[] { " S ", " S ", " W ", 'S', ingot, 'W', stick}; break;
					default: break;
				}
				
				
				GameRegistry.addShapedRecipe(new ItemStack(tool), recipe);
			}
			
			ItemArmorVC[] armorpieces = ItemsVC.getArmorPieces(metal.getName());
			
			for (ItemArmorVC armorpiece : armorpieces) {
				Object []recipe = null;
				//System.out.println(armorpiece + " / " + armorpiece.armorType);
				/** Stores the armor type: 0 is helmet, 1 is plate, 2 is legs and 3 is boots */
				switch(armorpiece.armorType) {
					case 0: recipe = new Object[] { "III", "I I", "   ", 'I', ingot}; break;
					case 1: recipe = new Object[] { "I I", "III", "III", 'I', ingot}; break;
					case 2: recipe = new Object[] { "III", "I I", "I I", 'I', ingot}; break;
					case 3: recipe = new Object[] { "   ", "I I", "I I", 'I', ingot}; break;
					default: break;
				}
				
				GameRegistry.addShapedRecipe(new ItemStack(armorpiece), recipe);
			}
		}
		
		
		
		for (BlockClassEntry rock : BlocksVC.rock.values()) {
			EnumRockType rocktype = (EnumRockType) rock.getKey();
			ItemStack stone = ItemStone.setRockType(new ItemStack(ItemsVC.stone), rocktype);
			
			GameRegistry.addShapedRecipe(BlocksVC.cobblestone.getItemStackFor(rocktype, 2), new Object[] { "SSS", "SCS", "SSS", 'C', Items.clay_ball, 'S', stone});
			GameRegistry.addShapedRecipe(BlocksVC.forge.getItemStackFor(rocktype, 2), new Object[] { "S S", "SCS", "SSS", 'C', Items.clay_ball, 'S', stone});
			
			
			GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.stoneAxe), new Object[] { "SS ", "SW ", " W ", 'S', stone, 'W', stick});
			GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.stoneHoe), new Object[] { "SS ", " W ", " W ", 'S', stone, 'W',  Items.stick});
			GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.stonePickaxe), new Object[] { "SSS", " W ", " W ", 'S', stone, 'W',  Items.stick});
			GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.stoneShovel), new Object[] { " S ", " W ", " W ", 'S', stone, 'W', Items.stick});	
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
			
			GameRegistry.addShapedRecipe(new ItemStack(Blocks.trapdoor, 1), new Object[] { "   ", "WWW", "WWW", 'W', planks.block});	
			

			GameRegistry.addShapedRecipe(BlocksVC.fence.getItemStackFor(planks.getKey()), new Object[] { "   ", "WSW", "WSW", 'W', planksstack, 'S', Items.stick});
			GameRegistry.addShapedRecipe(BlocksVC.fencegate.getItemStackFor(planks.getKey()), new Object[] { "   ", "WSW", "WSW", 'S', planksstack, 'W', Items.stick});
			
			GameRegistry.addShapedRecipe(new ItemStack(Items.bed, 1), new Object[] { "   ", "WWW", "PPP", 'W', Blocks.wool, 'P', planksstack});
			
			ItemStack quartzcrystal = ItemOreVC.getItemStackFor(EnumOreType.QUARTZCRYSTAL, 1);
			GameRegistry.addShapedRecipe(BlocksVC.quartzglass.getItemStackFor(planks.getKey()), new Object[] { " S ", "SQS", " S ", 'S', planksstack, 'Q', quartzcrystal});
			
			
			recipes.add(new ToolSupportedRecipe(BlocksVC.singleslab.getItemStackFor(planks.getKey()), new Object[] { "SW", "  ", 'S', ItemsVC.copperSaw, 'W', planksstack}));
			recipes.add(new ToolSupportedRecipe(BlocksVC.singleslab.getItemStackFor(planks.getKey()), new Object[] { "  ", "SW", 'S', ItemsVC.copperSaw, 'W', planksstack}));
			
			recipes.add(new ToolSupportedRecipe(BlocksVC.stairs.getItemStackFor(planks.getKey()), new Object[] { "S ", " W", 'S', ItemsVC.copperSaw, 'W', planksstack}));
			recipes.add(new ToolSupportedRecipe(BlocksVC.stairs.getItemStackFor(planks.getKey()), new Object[] { " S", "W ", 'S', ItemsVC.copperSaw, 'W', planksstack}));
		}
		
		
		GameRegistry.addShapedRecipe(new ItemStack(Items.oak_door, 1), new Object[] { "WW ", "WW ", "WW ", 'W', BlocksVC.planks.getItemStackFor(EnumTree.OAK)});
		GameRegistry.addShapedRecipe(new ItemStack(Items.spruce_door, 1), new Object[] { "WW ", "WW ", "WW ", 'W', BlocksVC.planks.getItemStackFor(EnumTree.SPRUCE)});
		GameRegistry.addShapedRecipe(new ItemStack(Items.birch_door, 1), new Object[] { "WW ", "WW ", "WW ", 'W', BlocksVC.planks.getItemStackFor(EnumTree.BIRCH)});
		GameRegistry.addShapedRecipe(new ItemStack(Items.acacia_door, 1), new Object[] { "WW ", "WW ", "WW ", 'W', BlocksVC.planks.getItemStackFor(EnumTree.ACACIA)});
		

		
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.fireclay_brick_raw, 1), new Object[] { "CC", "CC", 'C', ItemsVC.fireclay_ball});
		GameRegistry.addShapedRecipe(new ItemStack(BlocksVC.fireclaybricks, 1), new Object[] { "CCC", "CCC", "CCC", 'C', ItemsVC.fireclay_brick});
		
		
		GameRegistry.addShapedRecipe(new ItemStack(BlocksVC.bloomerybase, 1), new Object[] { "CCC", "CCC", "C C", 'C', ItemsVC.fireclay_brick});
		GameRegistry.addShapedRecipe(new ItemStack(BlocksVC.bloomerychimney, 1), new Object[] { " C ", "CCC", "   ", 'C', ItemsVC.fireclay_brick});
		GameRegistry.addShapedRecipe(new ItemStack(BlocksVC.bloomerychimney, 1), new Object[] { "   ", " C ", "CCC", 'C', ItemsVC.fireclay_brick});

		
		GameRegistry.addShapedRecipe(new ItemStack(Item.getItemFromBlock(BlocksVC.clayVessel)), new Object[] { " C ", "C C", "CCC", 'C', Items.clay_ball});

		GameRegistry.addShapedRecipe(new ItemStack(BlocksVC.stove), new Object[] { "BIB", "B B", "BBB", 'B', ItemsVC.fireclay_brick, 'I', ironingot});
		
		
		
		
		
		recipes.add(new RecipePlanks());

		
		/*RecipeSorter.register("vintagecraft:slabs", RecipeSlabs.class, Category.SHAPELESS, "after:minecraft:shapeless");
		recipes.add(new RecipeSlabs());*/
		
		
		recipes.add(new RecipeToolRack());
		
		
		ItemStack lignite = new ItemStack(ItemsVC.ore);
		ItemOreVC.setOreType(lignite, EnumOreType.LIGNITE);
		
		ItemStack bituminouscoal = new ItemStack(ItemsVC.ore);
		ItemOreVC.setOreType(lignite, EnumOreType.BITUMINOUSCOAL);
		
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.torch, 4), new Object[] { "C", "S", 'C', lignite, 'S', stick});
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.torch, 4), new Object[] { "C", "S", 'C', bituminouscoal, 'S', stick});
		
		
		removeRecipe(new ItemStack(Items.arrow, 4));
		GameRegistry.addShapedRecipe(new ItemStack(Items.arrow, 1), new Object[] { " F ", " S ", " F ", 'F', Items.flint, 'S', Items.stick, 'F', Items.feather});
		
		
		GameRegistry.addShapedRecipe(new ItemStack(Items.armor_stand), new Object[] {"SSS", " S ", "SCS", 'S', Items.stick, 'C', Items.clay_ball});
		
		
		GameRegistry.addShapedRecipe(new ItemStack(Items.saddle), new Object[] {"LLL", "LLL", " B ", 'L', Items.leather, 'B', tinbronzeingot});
		GameRegistry.addShapedRecipe(new ItemStack(Items.saddle), new Object[] {"LLL", "LLL", " B ", 'L', Items.leather, 'B', bismuthbronzeingot});
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
