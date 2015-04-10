package at.tyron.vintagecraft.Interfaces;

import net.minecraft.item.ItemStack;

public interface IItemSmeltable {

	public ItemStack getSmelted(ItemStack raw);
	
	public int getRaw2SmeltedRatio(ItemStack raw);
	
	public int getMeltingPoint(ItemStack raw);

	public float getSmeltingSpeedModifier(ItemStack raw);
}
