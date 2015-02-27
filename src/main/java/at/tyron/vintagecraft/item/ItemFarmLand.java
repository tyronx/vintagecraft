package at.tyron.vintagecraft.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import at.tyron.vintagecraft.WorldProperties.EnumFertility;
import at.tyron.vintagecraft.WorldProperties.EnumOrganicLayer;
import at.tyron.vintagecraft.interfaces.ISubtypeFromStackPovider;

public class ItemFarmLand extends ItemBlock implements ISubtypeFromStackPovider {

	public ItemFarmLand(Block block) {
		super(block);
		setHasSubtypes(true);
	}

	@Override
	public String getSubType(ItemStack stack) {
		EnumFertility fertility = EnumFertility.fromMeta((stack.getItemDamage() >> 2) & 3);		
		return fertility.shortName();
	}

}
