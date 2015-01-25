package at.tyron.vintagecraft.BlockClass;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import at.tyron.vintagecraft.block.BlockVC;
import at.tyron.vintagecraft.interfaces.IEnumState;
import at.tyron.vintagecraft.interfaces.IMultiblock;

public class BlockClassEntry<E> implements IEnumState, IStringSerializable, Comparable<E> {
	IEnumState key;
	
	public BlockVC block;
	public int metadata;
	
	
	public BlockClassEntry(IEnumState key) {
		this.key = key;
	}
	
	@Override
	public String getName() {
		return key.getStateName();
	}

	public String getStateName() {
		return key.getStateName();
	}
	
	public int getMetaData(BlockVC block) {
		return metadata;
	}
	
	
	public int getId() {
		return key.getId();
	}
	
	public IEnumState getKey() {
		return key;
	}

	
	public IBlockState getBlockState() {
		//return block.getDefaultState().withProperty(((IMultiblock)block).getTypeProperty(), metadata);
		return block.getDefaultState().withProperty(((IMultiblock)block).getTypeProperty(), this);
	}
	
	public IBlockState getBlockState(IBlockState baseState) {
		return baseState.withProperty(((IMultiblock)block).getTypeProperty(), this);
	}

	
	public IBlockState getBlockState(IBlockState baseState, IProperty property) {
		return baseState.withProperty(property, this);
	}


	


	@Override
	public String toString() {
		return super.toString() + " - meta: " + metadata + " / id: " + key + " / block: " + block;
	}

	@Override
	public int compareTo(E o) {
		if (o instanceof BlockClassEntry) {
			return getId() - ((BlockClassEntry)o).getId();
		}
		return 0;
	}

	@Override
	public void init(BlockVC block, int meta) {
		this.block = block;
		this.metadata = meta;
	}
	
}
