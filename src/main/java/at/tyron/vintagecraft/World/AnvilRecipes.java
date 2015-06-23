package at.tyron.vintagecraft.World;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import at.tyron.vintagecraft.Interfaces.ISmithable;
import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.WorldProperties.EnumAnvilTechnique;

public class AnvilRecipes {
	static ArrayList<AnvilRecipes> recipes = new ArrayList<AnvilRecipes>();
	
	public ItemStack []input;
	public ItemStack output;
	public EnumAnvilTechnique []requiredTechniques;
	public boolean wildcardMatch = false;

	
	public static ArrayList<AnvilRecipes> getAnvilRecipes() {
		return recipes;
	}
	
	public AnvilRecipes(ItemStack output, ItemStack input1, EnumAnvilTechnique []techniques) {
		this(output, input1, techniques, false);
	}
	
	public AnvilRecipes(ItemStack output, ItemStack input1, EnumAnvilTechnique []techniques, boolean wildcartMatch) {
		this.output = output;
		this.input = new ItemStack[]{input1};
		this.requiredTechniques = techniques;
		this.wildcardMatch = wildcartMatch;
	}

	
	public AnvilRecipes(ItemStack output, ItemStack input1, ItemStack input2, EnumAnvilTechnique []techniques) {
		this.output = output;
		this.input = new ItemStack[]{input1, input2};
		this.requiredTechniques = techniques;
	}

	public AnvilRecipes(ItemStack output, ItemStack[] input, EnumAnvilTechnique []techniques) {
		this(output, input, techniques, false);
	}
	
	public AnvilRecipes(ItemStack output, ItemStack[] input, EnumAnvilTechnique []techniques, boolean wildcartMatch) {
		this.output = output;
		this.input = input;
		this.requiredTechniques = techniques;
	}
	
	// If wildcardMatch = true, then previous invalid sequences are ignored
	public static void registerRecipe(AnvilRecipes recipe) {
		recipes.add(recipe);
	}
	
	public static boolean isInvalidRecipe(EnumAnvilTechnique []techniques, ItemStack []input) {
	//	System.out.println("isinvalidrecipe " + recipes.size());
		
		for (AnvilRecipes recipe : recipes) {
			
			/*System.out.println(recipe.wildcardMatch + "   " +
				(input.length != recipe.input.length) + " || " + (techniques.length > recipe.requiredTechniques.length && !recipe.wildcardMatch)
			);*/
			
			if (input.length != recipe.input.length || (techniques.length > recipe.requiredTechniques.length && !recipe.wildcardMatch)) continue;

			boolean match = true;
			
			for (int i = 0; i < input.length; i++) {
				if (!((ISmithable)input[i].getItem()).isSmithingIngredient(input[i], recipe.input[i], recipe)) {
					match = false;
					break;
				}
			}
			
			int matching = quantityMatches(techniques, recipe.requiredTechniques, recipe.wildcardMatch);
			match = match && (matching > 0);
			
			if (!match) continue;

			
			return false;
		}			
		
		return true;
	}
	
	
	
	
	public static AnvilRecipes getMatchingRecipe(EnumAnvilTechnique []techniques, ItemStack []input) {
		//System.out.println("getmatchingrecipe");
		
		for (AnvilRecipes recipe : recipes) {
			boolean match = true;
			
			//System.out.println("match for " + recipe.output + ": " + input.length+" == "+recipe.input.length+" && " + recipe.requiredTechniques.length + " == " + recipe.requiredTechniques.length);
			
			if (input.length != recipe.input.length || (techniques.length != recipe.requiredTechniques.length && !recipe.wildcardMatch)) continue;
			
			//System.out.println("basically ok");
			
			int matching1=0, matching2=0;
			
			for (int i = 0; i < input.length; i++) {
				if (!((ISmithable)input[i].getItem()).isSmithingIngredient(input[i], recipe.input[i], recipe)) {
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
	
	
	
	
	
	
	public static int quantityMatches(EnumAnvilTechnique []techniques, EnumAnvilTechnique []recipeTechniques, boolean wildcardMatch) {
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
	
	
	public ItemStack getOutput() {
		return output;
	}

}
