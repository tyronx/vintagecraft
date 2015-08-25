package at.tyron.vintagecraft.World.Crafting;

import java.util.ArrayList;

import at.tyron.vintagecraft.Interfaces.IItemWorkable;
import at.tyron.vintagecraft.WorldProperties.EnumWorkableTechnique;
import net.minecraft.item.ItemStack;

public class WorkableRecipeManager {
	public static WorkableRecipeManager smithing = new WorkableRecipeManager();
	public static WorkableRecipeManager carpentry = new WorkableRecipeManager();
	
			
	
	ArrayList<WorkableRecipeBase> recipes;

	
	public WorkableRecipeManager() {
		recipes = new ArrayList<WorkableRecipeBase>();
	}
	
	public ArrayList<WorkableRecipeBase> getRecipes() {
		return recipes;
	}
	
	public void registerRecipe(WorkableRecipeBase recipe) {
		recipe.ordinal = recipes.size();
		recipes.add(recipe);
	}
	
	
	public boolean isInvalidRecipe(EnumWorkableTechnique []techniques, ItemStack []input) {
	//	System.out.println("isinvalidrecipe " + recipes.size());
		
		for (WorkableRecipeBase  recipe : recipes) {
			
		/*	System.out.println(recipe.wildcardMatch + "   " +
				(input.length != recipe.input.length) + " || " + (techniques.length > recipe.requiredTechniques.length && !recipe.wildcardMatch)
			);
			*/
			if (input.length != recipe.input.length || (techniques.length > recipe.requiredTechniques.length && !recipe.wildcardMatch)) continue;

			
		//	System.out.println("input is ok ");
			
			boolean match = true;
			
			for (int i = 0; i < input.length; i++) {
				if (!((IItemWorkable)input[i].getItem()).isIngredient(input[i], recipe.input[i], recipe)) {
					match = false;
					break;
				}
			}
			
			int matching = quantityMatches(techniques, recipe.requiredTechniques, recipe.wildcardMatch);
			match = match && (matching > 0);
			
			if (!match) continue;
			
			
			//System.out.println("match on " + recipe.getName());

			
			return false;
		}			
		
		return true;
	}
	
	
	
	
	public WorkableRecipeBase getMatchingRecipe(EnumWorkableTechnique []techniques, ItemStack []input) {
		//System.out.println("getmatchingrecipe");
		
		for (WorkableRecipeBase recipe : recipes) {
			boolean match = true;
			
			//System.out.println("match for " + recipe.output + ": " + input.length+" == "+recipe.input.length+" && " + recipe.requiredTechniques.length + " == " + recipe.requiredTechniques.length);
			
			if (input.length != recipe.input.length || (techniques.length != recipe.requiredTechniques.length && !recipe.wildcardMatch)) continue;
			
			//System.out.println("basically ok");
			
			int matching1=0, matching2=0;
			
			for (int i = 0; i < input.length; i++) {
				if (!((IItemWorkable)input[i].getItem()).isIngredient(input[i], recipe.input[i], recipe)) {
					match = false;
					break;
				}
				matching1++;
			}
			
			 
			
			int matching = quantityMatches(techniques, recipe.requiredTechniques, recipe.wildcardMatch);
			match = match && (matching == recipe.requiredTechniques.length);
			
			//System.out.println(match + "    " +matching+" == "+recipe.requiredTechniques.length);
			
			if (!match) continue;
			
			return recipe;
		}
		
		return null;
	}
	
	
	
	
	
	
	public int quantityMatches(EnumWorkableTechnique []techniques, EnumWorkableTechnique []recipeTechniques, boolean wildcardMatch) {
		int matching = 0;
		
		if (wildcardMatch) {
			int curMatching = 0;
			// x = recipeTechniques.length
			// Check the last x - 0 techniques and see if any match
			// (as in, check if the last sequence was fully or at least partially matching)
			
			//System.out.println(techniques.length - recipeTechniques.length + " < " + techniques.length);
			
			for (int shift = 0; shift < recipeTechniques.length; shift++) {
				int start = Math.max(0, techniques.length - recipeTechniques.length);
				
				for (int i = 0 ; i < recipeTechniques.length; i++) {
					if (start + i + shift >= techniques.length) break;
					
					if (techniques[start + i + shift] == recipeTechniques[i]) {
						curMatching++;
					} else {
						curMatching = 0;
						break;
					}
				}
				
				//System.out.println(curMatching);
				
				matching = Math.max(curMatching, matching);
			}
			
		} else {
			
			for (int i = 0 ; i < techniques.length; i++) {
				if (techniques[i] != recipeTechniques[i]) {
					return 0;
				} else {
					matching++;
				}
			}
		}	
		
		return matching;
	}
	

}
