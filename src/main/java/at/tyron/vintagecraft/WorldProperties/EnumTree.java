package at.tyron.vintagecraft.WorldProperties;

import at.tyron.vintagecraft.block.BlockVC;
import at.tyron.vintagecraft.interfaces.IEnumState;

public enum EnumTree implements IEnumState {
	ASH,
	BIRCH,
	DOUGLASFIR,
	OAK,
	MAPLE,
	MOUNTAINDOGWOOD,
	PINE,
	SPRUCE
	
	;

	@Override
	public int getMetaData(BlockVC block) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getStateName() {
		return name();
	}

	@Override
	public void init(BlockVC block, int meta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getId() {
		return ordinal();
	}
}
