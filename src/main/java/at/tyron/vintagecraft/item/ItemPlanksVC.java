package at.tyron.vintagecraft.Item;

import java.util.List;

import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.BlockClass.TreeClass;
import at.tyron.vintagecraft.Interfaces.IItemWoodWorkable;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.World.Crafting.WorkableRecipeBase;
import at.tyron.vintagecraft.WorldProperties.EnumStrongHeatSource;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemPlanksVC extends ItemLogVC implements ISubtypeFromStackPovider, IItemWoodWorkable {
	
	public ItemPlanksVC(Block block) {
		super(block);
        this.setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (getTreeType(stack) == null) {
			return super.getUnlocalizedName() + ".unknown";
		}
		
		return super.getUnlocalizedName() + "." + getTreeType(stack).getStateName();
	}
	
	
	@Override
	public int getBurningHeat(ItemStack stack) {
		return 150;
	}

	@Override
	public float getBurnDurationMultiplier(ItemStack stack) {
		return 0.5f;
	}

	@Override
	public boolean isIngredient(ItemStack itemstack, ItemStack comparison, WorkableRecipeBase forrecipe) {
		return 
			itemstack.getItemDamage() == comparison.getItemDamage() &&
			itemstack.stackSize == comparison.stackSize
		;
	}	
}
