package at.tyron.vintagecraft.Item;

import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.World.BlocksVC;
import net.minecraft.block.Block;

public class ItemLeavesBranchy extends ItemLeaves {

	public ItemLeavesBranchy(Block block) {
		super(block);
	}

	
	public static BaseBlockClass getBlockClass() {
		return BlocksVC.leavesbranchy;
	}

}
