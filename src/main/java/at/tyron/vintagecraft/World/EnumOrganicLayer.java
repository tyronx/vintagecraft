package at.tyron.vintagecraft.World;

import java.util.ArrayList;
import java.util.List;

import at.tyron.vintagecraft.block.BlockRock;
import at.tyron.vintagecraft.block.BlockTopSoil;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

public enum EnumOrganicLayer implements IStringSerializable, IEnumState {
	None ("nograss", 0),
	SparseGrass ("sparsegrass", 1),
	NormalGrass ("normalgrass", 2)
	;
	
	
	private static EnumOrganicLayer[] META_LOOKUP = new EnumOrganicLayer[values().length+1];
	
	String name;
	int meta;
	
	private EnumOrganicLayer(String name, int meta) {
		this.name = name;
		this.meta = meta;
	}

	@Override
	public int getMetaData() {
		return meta;
	}


	
    public String getName() {
        return this.name;
    }
    
    
    public static EnumOrganicLayer byMetadata(int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
        }

        return META_LOOKUP[meta];
    }
    
    
    
    
    public IBlockState getTopSoilVariantForBlock(Block block) {
    	if (block instanceof BlockTopSoil) {
    		return block.getDefaultState().withProperty(BlockTopSoil.organicLayer, this);
    	}
    	return block.getDefaultState();
    }
    
    
    
    static {
    	EnumOrganicLayer[] types = values();

        for (int i = 0; i < types.length; ++i) {
        	EnumOrganicLayer type = types[i];
            META_LOOKUP[type.getMetaData()] = type;      
        }
    }
        
    
}
