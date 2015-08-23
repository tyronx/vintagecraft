package at.tyron.vintagecraft.World.Crafting;

import java.util.Locale;

import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import at.tyron.vintagecraft.Item.ItemAnvilPart;
import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.Item.ItemMetalPlate;
import at.tyron.vintagecraft.Item.ItemPlanksVC;
import at.tyron.vintagecraft.Item.ItemToolRack;
import at.tyron.vintagecraft.Item.Mechanics.ItemMechanicalWooden;
import at.tyron.vintagecraft.Item.Mechanics.ItemMechanicalWoodenOppositePlacement;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;

import com.ibm.icu.impl.duration.impl.DataRecord.EUnitVariant;

public enum EnumCarpentryRecipes {

	AXLE(new EnumWoodWorkingTechnique[]{
		EnumWoodWorkingTechnique.SPLIT,
		EnumWoodWorkingTechnique.SPLIT,
		EnumWoodWorkingTechnique.CARVE,
		EnumWoodWorkingTechnique.CARVE,
		EnumWoodWorkingTechnique.JOIN,
	}, 2),
	
	ANGLEDGEARS(new EnumWoodWorkingTechnique[]{
		EnumWoodWorkingTechnique.SAW,
		EnumWoodWorkingTechnique.SAW,
		EnumWoodWorkingTechnique.SAW,
		EnumWoodWorkingTechnique.SAW,
		EnumWoodWorkingTechnique.PLANE,
		EnumWoodWorkingTechnique.PLANE,
		EnumWoodWorkingTechnique.PLANE,
		EnumWoodWorkingTechnique.SAW,
		EnumWoodWorkingTechnique.JOIN,
		EnumWoodWorkingTechnique.JOIN,
		EnumWoodWorkingTechnique.JOIN,
		EnumWoodWorkingTechnique.JOIN
	}, 4),
	
	WINDMILLROTOR(new EnumWoodWorkingTechnique[]{
		EnumWoodWorkingTechnique.SPLIT,
		EnumWoodWorkingTechnique.SPLIT,
		EnumWoodWorkingTechnique.SAW,
		EnumWoodWorkingTechnique.SAW,
		EnumWoodWorkingTechnique.SPLIT,
		EnumWoodWorkingTechnique.CARVE,
		EnumWoodWorkingTechnique.CARVE,
		EnumWoodWorkingTechnique.JOIN,
		EnumWoodWorkingTechnique.JOIN
	}, 6),
	
	BELLOWS(new EnumWoodWorkingTechnique[]{
		EnumWoodWorkingTechnique.SAW,
		EnumWoodWorkingTechnique.SAW,
		EnumWoodWorkingTechnique.SAW,
		EnumWoodWorkingTechnique.CARVE,
		EnumWoodWorkingTechnique.DRILL,
		EnumWoodWorkingTechnique.DRILL,
		EnumWoodWorkingTechnique.DRILL,
		EnumWoodWorkingTechnique.JOIN,
		EnumWoodWorkingTechnique.JOIN
	}, 2),
	
	
	TOOLRACK(new EnumWoodWorkingTechnique[]{
		EnumWoodWorkingTechnique.SPLIT,
		EnumWoodWorkingTechnique.SPLIT,
		EnumWoodWorkingTechnique.SAW,
		EnumWoodWorkingTechnique.SAW,
		EnumWoodWorkingTechnique.DRILL,
		EnumWoodWorkingTechnique.DRILL,
		EnumWoodWorkingTechnique.DRILL,
		EnumWoodWorkingTechnique.DRILL
	}, 1),
	
	
	SAIL(new EnumWoodWorkingTechnique[]{
		EnumWoodWorkingTechnique.SPLIT,
		EnumWoodWorkingTechnique.SAW,
		EnumWoodWorkingTechnique.SPLIT,
		EnumWoodWorkingTechnique.SAW,
		EnumWoodWorkingTechnique.DRILL,
		EnumWoodWorkingTechnique.JOIN,
		EnumWoodWorkingTechnique.DRILL,
		EnumWoodWorkingTechnique.JOIN
	}, 2),

	;
	
	
	
	EnumWoodWorkingTechnique []steps;
	int planks;
	
	private EnumCarpentryRecipes(EnumWoodWorkingTechnique []steps, int planks) {
		this.steps = steps;
		this.planks = planks;
	}
	
	
	public static void registerRecipes() {
		for (EnumTree treetype : EnumTree.values()) {
			if (treetype.isBush) continue;
			
			ItemStack plankstack = BlocksVC.planks.getItemStackFor(treetype);
			
			
			
			if (treetype.jankahardness > 1000) {		
				plankstack.stackSize = AXLE.planks;
				AXLE.registerRecipe(((ItemMechanicalWooden)Item.getItemFromBlock(BlocksVC.axle)).withTreeType(treetype), plankstack);

				plankstack.stackSize = ANGLEDGEARS.planks;
				ANGLEDGEARS.registerRecipe(((ItemMechanicalWooden)Item.getItemFromBlock(BlocksVC.angledgears)).withTreeType(treetype), plankstack);
				
				plankstack.stackSize = WINDMILLROTOR.planks;
				WINDMILLROTOR.registerRecipe(((ItemMechanicalWoodenOppositePlacement)Item.getItemFromBlock(BlocksVC.windmillrotor)).withTreeType(treetype), plankstack);
				
				ItemStack stitchedleatherstack = new ItemStack(ItemsVC.stitchedleather, 4);
				plankstack.stackSize = BELLOWS.planks;
				WorkableRecipeBase recipe = BELLOWS.registerRecipe(((ItemMechanicalWooden)Item.getItemFromBlock(BlocksVC.bellows)).withTreeType(treetype), plankstack, stitchedleatherstack);
				recipe.setIngredientText(BELLOWS.planks + "planksand4leather");
				
				
				plankstack.stackSize = SAIL.planks;
				recipe = SAIL.registerRecipe(new ItemStack(ItemsVC.sail), plankstack, new ItemStack(ItemsVC.linenCloth, 3));
				recipe.setIngredientText(SAIL.planks + "planksand3linen");
				
			}
			
			plankstack.stackSize = TOOLRACK.planks;
			TOOLRACK.registerRecipe(((ItemToolRack)Item.getItemFromBlock(BlocksVC.toolrack)).withTreeType(treetype), plankstack);
		}
	}
	
	
	
	
	
	
	
	WorkableRecipeBase registerRecipe(ItemStack output, ItemStack input) {
		return registerRecipe(output, input, null);
	}
	
	WorkableRecipeBase registerRecipe(ItemStack output, ItemStack input, ItemStack input2) {
		WoodWorkingRecipe recipe;
		
		if (input2 != null) {
			recipe = new WoodWorkingRecipe(output, input.copy(), input2.copy(), steps);
		} else {
			recipe = new WoodWorkingRecipe(output, input.copy(), steps);
			if (planks > 0) {
				recipe.setIngredientText(planks+"planks");
			}
		}
		
		if (input.getItem() instanceof ItemPlanksVC) {
			recipe.setDisplayInRecipeHelper(ItemPlanksVC.getTreeType(input) == EnumTree.OAK);
		} else {
			recipe.setDisplayInRecipeHelper(true);
		}
				
		recipe.setUnlocalizedName(name().toLowerCase(Locale.ROOT));
		WorkableRecipeManager.carpentry.registerRecipe(recipe);
		return recipe;
	}
}
