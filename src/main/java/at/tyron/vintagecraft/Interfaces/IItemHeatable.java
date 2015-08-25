package at.tyron.vintagecraft.Interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IItemHeatable {

	public int heatableUntil(ItemStack stack);
	
	// If the item is also smithable, this method will be called just before the call on workableOn()
	public void updateTemperature(ItemStack stack, World world);

	
	public boolean canStackWith(World world, ItemStack self, ItemStack remote);
	public boolean tryStackWith(World world, ItemStack self, ItemStack remote);
	
}
