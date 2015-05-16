package at.tyron.vintagecraft.Item;

import net.minecraft.item.ItemStack;
import at.tyron.vintagecraft.VintageCraft;

public class ItemStraw extends ItemVC {

	public ItemStraw() {
		setCreativeTab(VintageCraft.resourcesTab);
	}
	
	@Override
	public int getDamage(ItemStack stack) {
		return 0;
	}
}
