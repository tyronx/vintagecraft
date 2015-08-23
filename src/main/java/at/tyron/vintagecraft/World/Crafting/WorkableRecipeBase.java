package at.tyron.vintagecraft.World.Crafting;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;
import at.tyron.vintagecraft.Interfaces.IItemSmithable;
import at.tyron.vintagecraft.Interfaces.IItemWorkable;
import at.tyron.vintagecraft.WorldProperties.EnumWorkableTechnique;

public abstract class WorkableRecipeBase {
	
	public ItemStack []input;
	public ItemStack output;
	public EnumWorkableTechnique []requiredTechniques;
	public boolean wildcardMatch = false;
	
	boolean displayInRecipeHelper = false;
	String unlocalizedName;
	String unlocalizedIngredientText;
	int ordinal;
	ItemStack iconItemstack;
	
	
	
	public WorkableRecipeBase(ItemStack output, ItemStack input1, EnumWorkableTechnique []techniques) {
		this(output, input1, techniques, false);
	}
	
	public WorkableRecipeBase(ItemStack output, ItemStack input1, EnumWorkableTechnique []techniques, boolean wildcartMatch) {
		this.output = output;
		this.input = new ItemStack[]{input1};
		this.requiredTechniques = techniques;
		this.wildcardMatch = wildcartMatch;
	}

	
	public WorkableRecipeBase(ItemStack output, ItemStack input1, ItemStack input2, EnumWorkableTechnique []techniques) {
		this.output = output;
		this.input = new ItemStack[]{input1, input2};
		this.requiredTechniques = techniques;
	}

	public WorkableRecipeBase(ItemStack output, ItemStack[] input, EnumWorkableTechnique []techniques) {
		this(output, input, techniques, false);
	}
	
	public WorkableRecipeBase(ItemStack output, ItemStack[] input, EnumWorkableTechnique []techniques, boolean wildcartMatch) {
		this.output = output;
		this.input = input;
		this.requiredTechniques = techniques;
	}
	
	
	

	public ItemStack getOutput() {
		return output;
	}
	
	public WorkableRecipeBase setIngredientText(String unlocalizedIngredientText) {
		this.unlocalizedIngredientText = unlocalizedIngredientText;
		return this;
	}
	
	public WorkableRecipeBase setUnlocalizedName(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
		return this;
	}
	
	public String getUnlocalizedName() {
		return unlocalizedName;
	}

	public String getName() {
		if (unlocalizedName == null) {	
			return StatCollector.translateToLocal(output.getItem().getUnlocalizedName(output) + ".name");
		}
		return StatCollector.translateToLocal("recipe." + unlocalizedName + ".name");
	}
	
	
	public String getRecipeText() {
		if (unlocalizedIngredientText == null) {
			return "";
		}
		return StatCollector.translateToLocal("recipeingredients." + unlocalizedIngredientText + ".name");
	}

	public int getOrdinal() {
		return ordinal;
	}
	
	public ItemStack getIcon() {
		if (iconItemstack == null) {
			return output;
		}
		return iconItemstack;
	}
	
	public WorkableRecipeBase setIcon(ItemStack icon) {
		this.iconItemstack = icon;
		return this;
	}
	
	public void setDisplayInRecipeHelper(boolean flag) {
		this.displayInRecipeHelper = flag;
	}
	
	public boolean shouldDisplayInRecipeHelper() {
		return displayInRecipeHelper;
	}

	public EnumWorkableTechnique[] getSteps() {
		return requiredTechniques;
	}

}
