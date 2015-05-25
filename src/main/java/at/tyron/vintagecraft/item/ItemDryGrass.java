package at.tyron.vintagecraft.Item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.IItemFuel;
import at.tyron.vintagecraft.WorldProperties.EnumStrongHeatSource;

public class ItemDryGrass extends ItemVC implements IItemFuel {

	public ItemDryGrass() {
		setCreativeTab(VintageCraft.resourcesTab);
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
	public boolean isForgeFuel(ItemStack stack) {
		return false;
	}

	@Override
	public int smokeLevel(ItemStack stack) {
		return 900;
	}
}
