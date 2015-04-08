package at.tyron.vintagecraft.WorldProperties;

import java.util.List;

import at.tyron.vintagecraft.Interfaces.IItemFuel;
import at.tyron.vintagecraft.Interfaces.IItemSmeltable;
import net.minecraft.item.ItemStack;

public enum EnumStrongHeatSource {
	FIREPIT 	 ("Fire pit", 	   1, 1,    1f,  0.7f, true),
	STOVE		 ("Stove", 		   2, 1, 1.05f,  1.0f, true),
	BLOOMERY 	 ("Bloomery", 	   3, 1, 1.25f,  1.0f, false),
	BLASTFURNACE ("Blast Furnace", 4, 2,  1.4f,  1.0f, false), 
	
	;
	
	public String name;
	public int id;
	public int fueluse;
	public float heatmodifier;
	public float burndurationmodifier;
	boolean hasgui;
	
	
	private EnumStrongHeatSource(String name, int id, int fueluse, float heatmodifier, float burndurationmodifier, boolean hasgui) {
		this.name = name;
		this.id = id;
		this.fueluse = fueluse;
		this.heatmodifier = heatmodifier;
		this.burndurationmodifier = burndurationmodifier;
		this.hasgui = hasgui;
	}
	
	
	public static void addItemStackInformation(ItemStack itemstack, List tooltip) {
		if (itemstack != null && itemstack.getItem() instanceof IItemFuel && ((IItemFuel)itemstack.getItem()).getBurningHeat(itemstack) > 0) {
			tooltip.add("Can be used as fuel");
			for (EnumStrongHeatSource heatsource : EnumStrongHeatSource.values()) {
				if (heatsource.hasgui) {
					tooltip.add("  " + heatsource.name + ": " + heatsource.getProducedHeat(itemstack) + " deg. / " + heatsource.getHeatDuration(itemstack) + " sec.");
				}
			}	
		}
		
		if (itemstack != null && itemstack.getItem() instanceof IItemSmeltable && ((IItemSmeltable)itemstack.getItem()).getMeltingPoint(itemstack) > 0) {
			tooltip.add("Melting Point: " + ((IItemSmeltable)itemstack.getItem()).getMeltingPoint(itemstack) + " deg.");
		}
		
	}
	
	public int getProducedHeat(ItemStack stack) {
		if (stack != null && stack.getItem() instanceof IItemFuel) {
			IItemFuel fuel = (IItemFuel)stack.getItem();
			return (int) (fuel.getBurningHeat(stack) * heatmodifier);
		}
		return 0;
	}
	
	public int getHeatDuration(ItemStack stack) {
		if (stack != null && stack.getItem() instanceof IItemFuel) {
			IItemFuel fuel = (IItemFuel)stack.getItem();
			return (int)(fuel.getBurnDurationMultiplier(stack) * 35f * burndurationmodifier);
		}
		return 0;
	}


	public static EnumStrongHeatSource byId(int value) {
		for (EnumStrongHeatSource furnace : EnumStrongHeatSource.values()) {
			if (furnace.id == value) return furnace;
		}
		return null;
	}
	
}
