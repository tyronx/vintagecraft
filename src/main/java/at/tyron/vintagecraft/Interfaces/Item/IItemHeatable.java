package at.tyron.vintagecraft.Interfaces.Item;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IItemHeatable {

	public int heatableUntil(ItemStack stack);
	
	// If the item is also smithable, this method will be called just before the call on workableOn()
	public void updateTemperature(ItemStack stack, World world);
	
	// Params:
	// stack - the itemstack
	// temperature - temperature in degress multiplied by 10 
	// worldtime - current worldtime. Reference time for autocooling
	public void setTemperatureM10(ItemStack stack, int temperature, long worldtime);	
	public int getTemperatureM10(ItemStack stack);
	
	public long getStartCoolingAt(ItemStack stack);
	public void setStartCoolingAt(ItemStack stack, long worldtime);
	
	public boolean canStackWith(World world, ItemStack self, ItemStack remote);
	public boolean tryStackWith(World world, ItemStack self, ItemStack remote);
	
}
