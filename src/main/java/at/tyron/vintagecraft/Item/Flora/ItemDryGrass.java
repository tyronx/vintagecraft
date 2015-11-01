package at.tyron.vintagecraft.Item.Flora;

import java.util.List;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.IItemFuel;
import at.tyron.vintagecraft.Interfaces.ISizedItem;
import at.tyron.vintagecraft.Item.ItemVC;
import at.tyron.vintagecraft.WorldProperties.EnumItemSize;
import at.tyron.vintagecraft.WorldProperties.EnumObjectCategory;
import at.tyron.vintagecraft.WorldProperties.EnumStrongHeatSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemDryGrass extends ItemVC implements IItemFuel, ISizedItem {

	public ItemDryGrass() {
		setCreativeTab(VintageCraft.resourcesTab);
	}
	
	@Override
	public EnumObjectCategory getCategory() {
		return EnumObjectCategory.Flora;
	}

	
	@Override
	public int getDamage(ItemStack stack) {
		return 0;
	}
	
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		EnumStrongHeatSource.addItemStackInformation(itemstack, tooltip);
	}

	@Override
	public int getBurningHeat(ItemStack stack) {
		return 600;
	}

	@Override
	public float getBurnDurationMultiplier(ItemStack stack) {
		return 0.2f;
	}

	@Override
	public boolean isMetalWorkingFuel(ItemStack stack) {
		return false;
	}

	@Override
	public int smokeLevel(ItemStack stack) {
		return 900;
	}

	@Override
	public EnumItemSize getItemSize() {
		return EnumItemSize.SMALL;
	}
	
	@Override
	public ItemStack getCokedOutput(ItemStack stack) {
		return null;
	}

}
