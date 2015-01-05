package at.tyron.vintagecraft.WorldProperties;

import java.util.ArrayList;

import at.tyron.vintagecraft.block.BlockDoubleFlowerVC;
import at.tyron.vintagecraft.block.BlockFlowerVC;
import at.tyron.vintagecraft.block.BlockVC;
import at.tyron.vintagecraft.interfaces.IEnumState;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

public enum EnumFlower implements IStringSerializable, IEnumState {
	ORANGEMILKWEED   ( 0, -1, false),
	ORANGEMILKWEED2  ( 1,  0, false),
	
	PURPLEMILKWEED   ( 2, -1, false),
	PURPLEMILKWEED2  ( 3,  2, false),
	
	CATMINT 	     ( 4),
	CALENDULA        ( 5, 5, false),
	CORNFLOWER       ( 6),
	CORNFLOWER2      ( 7, 6, false),
	
	LILYOFTHEVALLEY  ( 8),
	LILYOFTHEVALLEY2 ( 9,  8, false),
	LILYOFTHEVALLEY3 (10,  8, false),
	
	CLOVER			 (11),
	
    GOLDENROD        (12, -1, true),
    GOLDENROD2       (13, 12, true),
    GOLDENROD3       (14, 12, true),
    
    FORGETMENOT		 (15),
    FORGETMENOT2	 (16, 15, false),
    FORGETMENOT3	 (17, 15, false),
    FORGETMENOT4	 (18, 15, false),
    FORGETMENOT5	 (19, 15, false),
    
    NARCISSUS		 (20),
    NARCISSUS2		 (21, 20, false),
    NARCISSUS3		 (22, 20, false),
    
	PURPLETULIP		 (23),
	PURPLETULIP2	 (24, 23, false),
	PURPLETULIP3	 (25, 23, false),
	;
	
	int meta;
	BlockFlowerVC block;

	int id;

	
	
	boolean doubleHigh = false;
	int variantof;

	private EnumFlower (int id) {
		this (id, -1, false);
	}
		
	private EnumFlower (int id, int variantof, boolean doublehigh) {
		this.id = id;
		this.doubleHigh = doublehigh;
	}
	
	
	public void init(BlockVC block, int meta) {
		this.block = (BlockFlowerVC)block;
		this.meta = meta;
	}
	
	public IBlockState getBlockState() {
		if (doubleHigh) {
			return ((BlockDoubleFlowerVC)block).getDefaultState().withProperty(block.FLOWERTYPE, this).withProperty(BlockDoubleFlowerVC.HALF, BlockDoubleFlowerVC.EnumBlockHalf.LOWER);
		}
		
		return block.getDefaultState().withProperty(block.FLOWERTYPE, this);
	}
	
	public static EnumFlower[] values(boolean doublehigh) {
		ArrayList<EnumFlower> flowers = new ArrayList<EnumFlower>();
		
		for (EnumFlower flower : values()) {
			if (doublehigh == flower.doubleHigh) {
				flowers.add(flower);
			}
		}
		
		return flowers.toArray(new EnumFlower[0]);
	}
	
	
	
	
	
	public static EnumFlower fromMeta(Block block, int meta) {
		for (EnumFlower flower : EnumFlower.values()) {
			if (block == flower.block && meta == flower.meta) return flower;
		}
		return null;
	}
	
	
	

	@Override
	public String getName() {
		return name().toLowerCase();
	}

	@Override
	public int getMetaData() {
		return meta;
	}

	@Override
	public String getStateName() {
		return getName();
	}
}
