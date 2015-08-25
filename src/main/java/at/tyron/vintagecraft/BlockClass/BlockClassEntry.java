package at.tyron.vintagecraft.BlockClass;

import at.tyron.vintagecraft.Interfaces.IMultiblock;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockClassEntry<E> implements IStateEnum, IStringSerializable, Comparable<E> {
	IStateEnum key;
	
	public Block block;
	public int metadata;
	
	
	public BlockClassEntry(IStateEnum key) {
		this.key = key;
	}
	
	@Override
	public String getName() {
		return key.getStateName();
	}

	public String getStateName() {
		return key.getStateName();
	}
	
	public int getMetaData(Block block) {
		return metadata;
	}
	
	public int getId() {
		return key.getId();
	}
	
	public IStateEnum getKey() {
		return key;
	}

	
	
	
	
	public IBlockState getBlockState() {
		return block.getDefaultState().withProperty(((IMultiblock)block).getTypeProperty(), this);
	}
	
	public IBlockState getBlockState(IBlockState baseState) {
		return baseState.withProperty(((IMultiblock)block).getTypeProperty(), this);
	}

	public IBlockState getBlockState(IBlockState baseState, IProperty property) {
		return baseState.withProperty(property, this);
	}
	
	public ItemStack getItemStack() {
		return new ItemStack(Item.getItemFromBlock(block), 1, metadata);
	}
	
	public ItemStack getItemStack(int quantity) {
		return new ItemStack(Item.getItemFromBlock(block), quantity, metadata);
	}


	


	@Override
	public String toString() {
		return key + " (meta= " + metadata + ", block=" + block.getUnlocalizedName() + ")";
	}

	@Override
	public int compareTo(E o) {
		if (o instanceof BlockClassEntry) {
			return getId() - ((BlockClassEntry)o).getId();
		}
		return 0;
	}

	@Override
	public void init(Block block, int meta) {
		this.block = block;
		this.metadata = meta;
	}
	
}
