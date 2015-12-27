package at.tyron.vintagecraft.Interfaces.Item;

import net.minecraft.item.ItemStack;

public interface IItemSmeltable {

	// Should return an itemstack of the smelted raw itemstack
	public ItemStack getSmelted(ItemStack raw);
	
	// Should return how many raw items are needed for a smelted item
	public int getRaw2SmeltedRatio(ItemStack raw);
	
	// Melting point in degree
	public int getMeltingPoint(ItemStack raw);

	// Once the melting point is reached, how long should it take for the item to smelt?
	// e.g. 1f for no change
	// 0.5f for double speed
	public float getSmeltingSpeedModifier(ItemStack raw);
	
	
	// Can this item be smelted in batches?
	// Usually you want to return 1 here.
	
	// Return 3 if 3 at each smelt can be smelted
	public int smeltBatchSize(ItemStack raw); 
}
