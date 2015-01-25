package at.tyron.vintagecraft.interfaces;

import at.tyron.vintagecraft.block.BlockVC;


/* For classes/enums that supply blockstate infos to blocks */

public interface IEnumState {
	public int getMetaData(BlockVC block);
	public String getStateName();
	
	public void init(BlockVC block, int meta);
	public int getId();
	
}
