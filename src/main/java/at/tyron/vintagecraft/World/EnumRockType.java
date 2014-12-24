package at.tyron.vintagecraft.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import at.tyron.vintagecraft.block.BlockRock;
import at.tyron.vintagecraft.block.VCBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

public enum EnumRockType implements IStringSerializable, IEnumState, IGenLayerSupplier {
	LIMESTONE(1, 1, "limestone", EnumRockGroup.SEDIMENTARY),
	CLAYSTONE(2, 2, "claystone", EnumRockGroup.SEDIMENTARY),
	CONGLOMERATE(3, 3, "conglomerate", EnumRockGroup.SEDIMENTARY),
	SHALE(4, 4, "shale", EnumRockGroup.SEDIMENTARY),
	CHERT(13, 13, "chert", EnumRockGroup.SEDIMENTARY),
	SANDSTONE(0, 0, "sandstone", EnumRockGroup.SEDIMENTARY),
	REDSANDSTONE(15, 0, "redsandstone", EnumRockGroup.SEDIMENTARY2),
	
    SCHIST(5, 5, "schist", EnumRockGroup.METAMORPHIC),
    GNEISS(6, 6, "gneiss", EnumRockGroup.METAMORPHIC),
    MARBLE(7, 7, "marble", EnumRockGroup.METAMORPHIC),
    QUARTZITE(8, 8, "quartzite", EnumRockGroup.METAMORPHIC),
	
	GRANITE(9, 9, "granite", EnumRockGroup.IGNEOUS_INTRUSIVE),
	DIORITE(10, 10, "diorite", EnumRockGroup.IGNEOUS_INTRUSIVE),
	
	BASALT(11, 11, "basalt", EnumRockGroup.IGNEOUS_EXTRUSIVE),
	ANDESITE(12, 12, "andesite", EnumRockGroup.IGNEOUS_EXTRUSIVE),
	
	GABBRO(14, 14, "gabbro", EnumRockGroup.IGNEOUS_INTRUSIVE)
	
    ;
    
	private static HashMap<EnumCrustLayer, List<EnumRockType>> CRUSTLAYER_ROCKTYPES = new HashMap<EnumCrustLayer, List<EnumRockType>>(EnumCrustLayer.values().length);
    private static EnumRockType[] META_LOOKUP = new EnumRockType[values().length+1];
    private static EnumRockType[] ID_LOOKUP = new EnumRockType[values().length+1];
    
    public int id; // used for GenLayerRockInit
    public int meta;
    public String name;
    public String unlocalizedName;
    public EnumRockGroup group;

    private EnumRockType(int id, int meta, String name, EnumRockGroup group) {
        this(id, meta, name, name, group);
    }

    private EnumRockType(int id, int meta, String name, String unlocalizedName, EnumRockGroup group) {
    	this.id = id;
        this.meta = meta;
        this.name = name;
        this.unlocalizedName = unlocalizedName;
        this.group = group;
    }
    
    
    public EnumRockType randomRockTypeByLayer(EnumRockGroup layer) {
		return null;
    	
    }
    
    public IBlockState getRockVariantForBlock(Block block) {
    	if (block instanceof BlockRock) {
    		return block.getDefaultState().withProperty(BlockRock.STONETYPE, this);
    	}
    	return block.getDefaultState();
    }

    
    
    @Override
    public int getColor() {
    	return id;
    }
    
    public int getMetaData() {
        return this.meta;
    }

    public String toString() {
        return this.name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getModelResourceName() {
    	return "rock/" + getName();
    }

    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    public static String[] getNames() {
    	String[] names = new String[values().length];
    	
    	for (int i = 0; i < values().length; i++) {
    		names[i] = values()[i].name;
    	}
    	return names;
    }

    public static EnumRockType byMetadata(int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
        }

        return META_LOOKUP[meta];
    }
    
    
    public static EnumRockType byId(int id) {
        if (id < 0 || id >= META_LOOKUP.length) {
            id = 0;
        }

        return ID_LOOKUP[id];
    }
    
    
    
    
    
    public static EnumRockType[] getRockTypesForCrustLayer(EnumCrustLayer layer) {
    //	System.out.println("get for " + layer);
    	return CRUSTLAYER_ROCKTYPES.get(layer).toArray(new EnumRockType[0]);
    }


    static {
    	EnumRockType[] rocktypes = values();

        for (int i = 0; i < rocktypes.length; ++i) {
        	EnumRockType rocktype = rocktypes[i];
            META_LOOKUP[rocktype.getMetaData()] = rocktype;      
            ID_LOOKUP[rocktype.id] = rocktype;
            
            for (int j = 0; j < rocktype.group.crustlayers.length; j++) {
            	List<EnumRockType> crustrocktypes = CRUSTLAYER_ROCKTYPES.get(rocktype.group.crustlayers[j]);
            	if (crustrocktypes == null) crustrocktypes = new ArrayList<EnumRockType>();
            	
            	crustrocktypes.add(rocktype);
            	
            	CRUSTLAYER_ROCKTYPES.put(rocktype.group.crustlayers[j], crustrocktypes);
            }
        }
        
        
        

        
    }


	@Override
	public int getWeight() {
		return 0;
	}
	
	@Override
	public int getDepthMin() {
		return 0;
	}
	
	@Override
	public int getDepthMax() {
		return 0;
	}
}
