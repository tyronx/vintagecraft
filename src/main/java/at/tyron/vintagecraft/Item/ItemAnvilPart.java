
package at.tyron.vintagecraft.Item;

import java.util.Arrays;

import at.tyron.vintagecraft.Interfaces.IItemSmithable;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.World.Crafting.EnumAnvilTechnique;
import at.tyron.vintagecraft.World.Crafting.WorkableRecipeBase;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.EnumWorkableTechnique;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemAnvilPart extends ItemVC implements ISubtypeFromStackPovider, IItemSmithable {
	boolean upper;
	

	public ItemAnvilPart(boolean upper) {
		this.upper = upper;
		maxStackSize = 1;
	}

	@Override
	public String getSubType(ItemStack stack) {
		return getMetal(stack).getName();
	}
	
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (getMetal(stack) == null) {
			return super.getUnlocalizedName() + ".unknown";
		}
		return "item." + getMetal(stack).getName() + "anvil" + (upper ? "surface" : "base");
	}
	
	@Override
	public boolean workableOn(int anviltier, ItemStack itemstack, ItemStack itemstackoptional) {
		// Can make until tier+1 anvils
		return getMetal(itemstack).tier <= anviltier + 1;
	}

	@Override
	public ItemStack applyTechnique(ItemStack itemstack, EnumWorkableTechnique technique) {
		NBTTagCompound nbt = getOrCreateNBT(itemstack);
		int[] techniqueIds = nbt.getIntArray("anviltechniques");
		int[] newtechniqueIds = Arrays.copyOf(techniqueIds, techniqueIds.length + 1);
		
		newtechniqueIds[newtechniqueIds.length - 1] = technique.getId();
		
		nbt.setIntArray("anviltechniques", newtechniqueIds);
		
		itemstack.setTagCompound(nbt);
		
		return itemstack;
	}

	@Override
	public EnumAnvilTechnique[] getAppliedTechniques(ItemStack itemstack) {
		NBTTagCompound nbt = getOrCreateNBT(itemstack);
		
		int[] techniqueIds = nbt.getIntArray("anviltechniques");
		EnumAnvilTechnique []techniques = new EnumAnvilTechnique[techniqueIds.length];
		for (int i = 0; i < techniqueIds.length; i++) {
			techniques[i] = EnumAnvilTechnique.fromId(techniqueIds[i]);
		}
		
		return techniques;
	}

	@Override
	public ItemStack markOddlyShaped(ItemStack itemstack, boolean flag) {
		NBTTagCompound nbt = getOrCreateNBT(itemstack);
		nbt.removeTag("anviltechniques");
		itemstack.setTagCompound(nbt);
		return itemstack;
	}

	@Override
	public boolean isOddlyShaped(ItemStack itemstack) {
		return false;
	}


	@Override
	public boolean isIngredient(ItemStack itemstack, ItemStack comparison, WorkableRecipeBase forrecipe) {
		return 
			itemstack != null && comparison != null &&
			itemstack.getItem() == comparison.getItem() &&
			itemstack.stackSize == comparison.stackSize &&
			getMetal(itemstack) == getMetal(comparison) &&
			!isOddlyShaped(itemstack)
		;
	}
	
	
	public static EnumMetal getMetal(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null) {
			return EnumMetal.byId(itemstack.getTagCompound().getInteger("metal"));
		}
		return EnumMetal.BISMUTH;
	}
	
	
	public static ItemStack setMetal(ItemStack itemstack, EnumMetal metal) {
		NBTTagCompound nbt = getOrCreateNBT(itemstack);
		nbt.setInteger("metal", metal.id);
		itemstack.setTagCompound(nbt);
		return itemstack;
	}
	
	
	public static NBTTagCompound getOrCreateNBT(ItemStack itemstack) {
		NBTTagCompound nbt = itemstack.getTagCompound(); 
		if (nbt == null) {
			itemstack.setTagCompound(nbt = new NBTTagCompound());
		}	
		return nbt;
	}
}
