package at.tyron.vintagecraft.Interfaces.Item;

import net.minecraft.item.ItemStack;

public interface IItemFuel {
	// http://www.fao.org/docrep/x5872e/x5872e0b.htm
	/* Effective calorific value of dry substance
	     MJ/kg
	Coal 28-33
	Lignite 20-24
	Peat 20-23
	Wood 17-20
	*/
	public int getBurningHeat(ItemStack stack);
	
	public float getBurnDurationMultiplier(ItemStack stack);
	
	public boolean isMetalWorkingFuel(ItemStack stack);
	
	public int smokeLevel(ItemStack stack);
	
	
	// Ratios:
	// 1x Charcoal = 8 logs 
	// 1x Peat coke = 7 peat
	// 1x Lignite coke = 5 lignite
	// 1x Bituminous coal coke = 3 bituminous coal 
	public ItemStack getCokedOutput(ItemStack stack);
	
}
