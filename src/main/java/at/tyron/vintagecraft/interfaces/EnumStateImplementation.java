package at.tyron.vintagecraft.interfaces;

import net.minecraft.block.Block;
import at.tyron.vintagecraft.block.BlockVC;

public class EnumStateImplementation implements IEnumState {
	int id;
	
	int metadata;
	String statename;
	
	
	public EnumStateImplementation(int id, int meta, String name) {
		this.id = id;
		this.metadata = meta;
		this.statename = name;
	}
		
	@Override
	public int getMetaData(Block block) {
		return metadata;
	}

	@Override
	public String getStateName() {
		return statename;
	}

	@Override
	public void init(Block block, int meta) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	
	@Override
	public String toString() {
		return " (meta= " + metadata + ", block=" + statename + ")";
	}

}
