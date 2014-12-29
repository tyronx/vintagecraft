package at.tyron.vintagecraft.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumFurnace;
import at.tyron.vintagecraft.interfaces.IFuel;

public class ItemPeatBrick extends ItemVC implements IFuel {

	
	@Override
	public int getBurningHeat(ItemStack stack) {
		return 1000;
	}

	@Override
	public float getBurnDurationMultiplier(ItemStack stack) {
		return 1.1f;
	}
	
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add("Heat produced when burned");
		for (EnumFurnace furnace : EnumFurnace.values()) {
			tooltip.add("  " + furnace.name + ": " + (int)(getBurningHeat(itemstack) * furnace.maxHeatModifier()) + " °C");	
		}
	}
	
}
