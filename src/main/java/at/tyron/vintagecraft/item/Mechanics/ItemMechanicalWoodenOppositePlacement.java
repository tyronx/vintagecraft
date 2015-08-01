package at.tyron.vintagecraft.Item.Mechanics;

import net.minecraft.block.Block;

public class ItemMechanicalWoodenOppositePlacement extends ItemMechanicalWooden {

	public ItemMechanicalWoodenOppositePlacement(Block block) {
		super(block);
		placeWithOppositeOrientation = true;
		setMaxStackSize(1);
	}

}
