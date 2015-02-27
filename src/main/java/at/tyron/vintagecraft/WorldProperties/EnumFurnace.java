package at.tyron.vintagecraft.WorldProperties;

import at.tyron.vintagecraft.interfaces.IFuel;
import net.minecraft.item.ItemStack;

public enum EnumFurnace {
	STOVE ("Stove", 1, 1, 1100),
	FURNACE ("Furnace", 2, 3, 1600),
	BLASTFURNACE ("Blast Furnace", 3, 5, 2500)
	
	;
	
	public String name;
	public int id;
	public int fuelUse;
	public int maxHeat;
	
	private EnumFurnace(String name, int id, int fuelUse, int maxHeat) {
		this.name = name;
		this.id = id;
		this.fuelUse = fuelUse;
		this.maxHeat = maxHeat;
	}
	
	
	public int getProducedHeat(ItemStack stack) {
		if (stack != null && stack.getItem() instanceof IFuel) {
			IFuel fuel = (IFuel)stack.getItem();
			int burnheat = fuel.getBurningHeat(stack);
			return (int) Math.min(maxHeat, burnheat * maxHeatModifier());
		}
		return 0;
	}
	
	public float maxHeatModifier() {
		return 1 + (fuelUse - 1f) / 10;
	}
	
	public int getHeatDuration(ItemStack stack) {
		if (stack != null && stack.getItem() instanceof IFuel) {
			IFuel fuel = (IFuel)stack.getItem();
			return (int)fuel.getBurnDurationMultiplier(stack) * 35 - fuelUse * 2;
		}
		return 0;
	}


	public static EnumFurnace byId(int value) {
		for (EnumFurnace furnace : EnumFurnace.values()) {
			if (furnace.id == value) return furnace;
		}
		return null;
	}
	
}
