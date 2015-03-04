package at.tyron.vintagecraft.item;

import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class ItemGravel extends ItemSand {

	public ItemGravel(Block block) {
		super(block);
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}


	@Override
	public int getMeltingPoint(ItemStack raw) {
		if (EnumRockType.byMetadata(raw.getMetadata()) == EnumRockType.QUARTZITE) {
			return 1700;
		}
		return 0;
	}
	
}
