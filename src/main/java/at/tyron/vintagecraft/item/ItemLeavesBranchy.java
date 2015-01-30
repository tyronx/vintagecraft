package at.tyron.vintagecraft.item;

import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.World.BlocksVC;
import net.minecraft.block.Block;

public class ItemLeavesBranchy extends ItemLeaves {

	public ItemLeavesBranchy(Block block) {
		super(block);
	}

	
	public static BlockClass getBlockClass() {
		return BlocksVC.leavesbranchy;
	}

}
