package at.tyron.vintagecraft.WorldProperties.Terrain;

import java.util.ArrayList;

import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.Block.Organic.BlockDoubleFlowerVC;
import at.tyron.vintagecraft.Block.Organic.BlockFlowerVC;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

public enum EnumFlower implements IStringSerializable, IStateEnum {
	ORANGEMILKWEED   ( 0),
	ORANGEMILKWEED2  ( 1),
	
	PURPLEMILKWEED   ( 2),
	PURPLEMILKWEED2  ( 3),
	
	CATMINT 	     ( 4),
	CALENDULA        ( 5),
	CORNFLOWER       ( 6),
	CORNFLOWER2      ( 7),
	
	LILYOFTHEVALLEY  ( 8),
	LILYOFTHEVALLEY2 ( 9),
	LILYOFTHEVALLEY3 (10),
	
	CLOVER			 (11),
	
    GOLDENROD        (12, true),
    GOLDENROD2       (13, true),
    GOLDENROD3       (14, true),
    
    FORGETMENOT		 (15),
    FORGETMENOT2	 (16),
    FORGETMENOT3	 (17),
    FORGETMENOT4	 (18),
    FORGETMENOT5	 (19),
    
    NARCISSUS		 (20),
    NARCISSUS2		 (21),
    NARCISSUS3		 (22),
    
	PURPLETULIP		 (23),
	PURPLETULIP2	 (24),
	PURPLETULIP3	 (25),
	
	COWPARSLEY		 (26),
	COWPARSLEY2		 (27),
	
	HORSETAIL		 (28)
	;
	
/*	int meta;
	BlockFlowerVC block;*/

	int id;

	
	
	public boolean doubleHigh = false;
	int variantof;

	private EnumFlower (int id) {
		this (id, false);
	}
		
	private EnumFlower (int id, boolean doublehigh) {
		this.id = id;
		this.doubleHigh = doublehigh;
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
	
	
	
	
	

	

	@Override
	public String getName() {
		return name().toLowerCase();
	}

	@Override
	public int getMetaData(Block block) {
		return 0;
	}

	@Override
	public String getStateName() {
		return getName();
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public void init(Block block, int meta) {
		// TODO Auto-generated method stub
		
	}
}
