package at.tyron.vintagecraft.item;

import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.interfaces.ISmeltable;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class ItemSand extends ItemRock implements ISmeltable {

	public ItemSand(Block block) {
		super(block);
	}

	@Override
	public ItemStack getSmelted(ItemStack raw) {
		if (EnumRockType.byMetadata(raw.getMetadata()) == EnumRockType.QUARTZITE) {
			return new ItemStack(Blocks.glass);
		}
		return null;
	}

	@Override
	public int getRaw2SmeltedRatio(ItemStack raw) {
		if (EnumRockType.byMetadata(raw.getMetadata()) == EnumRockType.QUARTZITE) {
			return 1;
		}
		return 0;
	}

	@Override
	public int getMeltingPoint(ItemStack raw) {
		if (EnumRockType.byMetadata(raw.getMetadata()) == EnumRockType.QUARTZITE) {
			return 1600;
		}
		return 0;
	}

}
