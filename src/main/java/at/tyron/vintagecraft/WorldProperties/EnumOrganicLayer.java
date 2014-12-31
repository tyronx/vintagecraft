package at.tyron.vintagecraft.WorldProperties;

import java.util.ArrayList;
import java.util.List;

import at.tyron.vintagecraft.block.BlockRock;
import at.tyron.vintagecraft.block.BlockTopSoil;
import at.tyron.vintagecraft.interfaces.EnumStateImplementation;
import at.tyron.vintagecraft.interfaces.IEnumState;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

public enum EnumOrganicLayer implements IStringSerializable, IEnumState {
	None ("nograss", 0, 0),
	VerySparseGrass ("verysparsegrass", 1, 3),
	SparseGrass ("sparsegrass", 2, 7),
	NormalGrass ("normalgrass", 3, 11)
	
	;
	
	
	private static EnumOrganicLayer[] META_LOOKUP = new EnumOrganicLayer[values().length+1];
	private static EnumOrganicLayer[] LIGHT_LOOKUP = new EnumOrganicLayer[16];
	
	public String name;
	public int meta;
	public int minblocklight;
	
	
	private EnumOrganicLayer(String name, int meta, int minblocklight) {
		this.name = name;
		this.meta = meta;
		this.minblocklight = minblocklight;
	}
	
	public EnumOrganicLayer adjustToLight(int blocklight) {
		if (this != None && blocklight < minblocklight) return fromMeta(meta - 1);
		if (this != NormalGrass && fromMeta(meta+1).minblocklight <= blocklight) return fromMeta(meta + 1);
		return this;
	}
	

	@Override
	public int getMetaData() {
		return meta;
	}


	@Override
    public String getName() {
        return this.name;
    }

	@Override
    public String getStateName() {
        return this.name;
    }
    
	

	
	
    public static EnumOrganicLayer fromMeta(int meta) {
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
    
    
    public static EnumOrganicLayer fromBlockLight(int blocklight) {
		return LIGHT_LOOKUP[blocklight];
	}
	
    

	private static EnumOrganicLayer _fromBlockLight(int blocklight) {
		EnumOrganicLayer result = None;
		
		for (EnumOrganicLayer layer : EnumOrganicLayer.values()) {
			if (blocklight > layer.minblocklight) result = layer;
		}
		return result;
	}
	

    
    
    
    static {
    	EnumOrganicLayer[] types = values();

        for (int i = 0; i < types.length; ++i) {
        	EnumOrganicLayer type = types[i];
            META_LOOKUP[type.getMetaData()] = type;      
        }
        
        for (int i = 0; i <= 15; i++) {
        	LIGHT_LOOKUP[i] = _fromBlockLight(i);
        	
        }
    }





	public static IEnumState[] valuesWithFertility() {
		IEnumState[] results = new EnumStateImplementation[values().length * EnumFertility.values().length];
		int i = 0;
		for (EnumOrganicLayer organiclayer : values()) {
			for (EnumFertility fertility : EnumFertility.values()) {
				results[i++] = new EnumStateImplementation(organiclayer.meta + (fertility.meta << 2), fertility.shortname + "_" + organiclayer.getName());
			}
		}
		return results;
	}
        
    
}
