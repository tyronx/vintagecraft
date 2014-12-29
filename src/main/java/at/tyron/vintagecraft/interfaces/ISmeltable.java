package at.tyron.vintagecraft.interfaces;

import net.minecraft.item.ItemStack;

public interface ISmeltable {

	public ItemStack getSmelted(ItemStack raw);
	
	public int getRaw2SmeltedRatio(ItemStack raw);
	
	public int getMeltingPoint(ItemStack raw);
}
