package at.tyron.vintagecraft;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class CreativeTabsVC extends CreativeTabs {
	public Item icon;
	
	public CreativeTabsVC(int index, String label) {
		super(index, label);
	}

	@Override
	public Item getTabIconItem() {
		if (icon == null) return Items.apple;
		return icon;
	}

}
