package at.tyron.vintagecraft.interfaces;

import net.minecraft.block.Block;
import at.tyron.vintagecraft.block.BlockVC;


/* For classes/enums that supply blockstate infos to blocks */

public interface IEnumState {
	public int getMetaData(Block block);
	public String getStateName();
	
	public void init(Block block, int meta);
	public int getId();
	
}
