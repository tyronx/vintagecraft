package at.tyron.vintagecraft.Item.Carpentry;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Item.ItemVC;
import at.tyron.vintagecraft.WorldProperties.EnumObjectCategory;

public class ItemSail extends ItemVC {
	public ItemSail() {
		setCreativeTab(VintageCraft.mechanicsTab);
		setMaxStackSize(4);
	}
	@Override
	public EnumObjectCategory getCategory() {
		return EnumObjectCategory.Carpentry;
	}

}
