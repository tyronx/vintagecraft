package at.tyron.vintagecraft.WorldProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.block.BlockRock;
import at.tyron.vintagecraft.interfaces.IEnumState;
import at.tyron.vintagecraft.interfaces.IGenLayerSupplier;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

public enum EnumRockType implements IStringSerializable, IEnumState, IGenLayerSupplier {
	SANDSTONE		(0, 0, 40, "sandstone", EnumCrustLayerGroup.SEDIMENTARY),
	LIMESTONE		(1, 1, 5, "limestone", EnumCrustLayerGroup.SEDIMENTARY),
	CLAYSTONE		(2, 2, 30, "claystone", EnumCrustLayerGroup.SEDIMENTARY),
	CONGLOMERATE	(3, 3, 50, "conglomerate", EnumCrustLayerGroup.SEDIMENTARY),
	SHALE			(4, 4, 50, "shale", EnumCrustLayerGroup.SEDIMENTARY),
	CHERT			(13, 13, 50, "chert", EnumCrustLayerGroup.SEDIMENTARY),
	REDSANDSTONE	(15, 0, 20, "redsandstone", EnumCrustLayerGroup.SEDIMENTARY2),
	
    SCHIST			(5, 5, 50, "schist", EnumCrustLayerGroup.METAMORPHIC),
    GNEISS			(6, 6, 50, "gneiss", EnumCrustLayerGroup.METAMORPHIC),
    MARBLE			(7, 7, 20, "marble", EnumCrustLayerGroup.METAMORPHIC),
    QUARTZITE		(8, 8, 50, "quartzite", EnumCrustLayerGroup.METAMORPHIC),
	
	GRANITE			(9, 9, 80, "granite", EnumCrustLayerGroup.IGNEOUS_INTRUSIVE),
	DIORITE			(10, 10, 50, "diorite", EnumCrustLayerGroup.IGNEOUS_INTRUSIVE),
	
	BASALT			(11, 11, 20, "basalt", EnumCrustLayerGroup.IGNEOUS_EXTRUSIVE),
	ANDESITE		(12, 12, 80, "andesite", EnumCrustLayerGroup.IGNEOUS_EXTRUSIVE),
	
	GABBRO			(14, 14, 50, "gabbro", EnumCrustLayerGroup.IGNEOUS_INTRUSIVE)
	
//	KIMBERLITE(15, 15, )
    ;
    
	private static HashMap<EnumCrustLayer, List<EnumRockType>> CRUSTLAYER_ROCKTYPES = new HashMap<EnumCrustLayer, List<EnumRockType>>(EnumCrustLayer.values().length);
    private static EnumRockType[] META_LOOKUP = new EnumRockType[values().length+1];
    private static EnumRockType[] ID_LOOKUP = new EnumRockType[values().length+1];
    
    public int id; // used for GenLayerRockInit
    public int meta;
    public int weight;
    public String name;
    public String unlocalizedName;
    public EnumCrustLayerGroup group;
    

    private EnumRockType(int id, int meta, int weight, String name, EnumCrustLayerGroup group) {
        this(id, meta, weight, name, name, group);
    }

    private EnumRockType(int id, int meta, int weight, String name, String unlocalizedName, EnumCrustLayerGroup group) {
    	this.id = id;
        this.meta = meta;
        this.name = name;
        this.unlocalizedName = unlocalizedName;
        this.group = group;
        this.weight = weight;
    }
    
    
    public EnumRockType randomRockTypeByLayer(EnumCrustLayerGroup layer) {
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
    	return id * 10;
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
    
    public String getStateName() {
        return this.name;
    }
    
    public String getModelResourceName() {
    	return "rock/" + getStateName();
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
		return weight;
	}
	
	@Override
	public int getDepthMin() {
		return group.minThickness;
	}
	
	@Override
	public int getDepthMax() {
		return group.maxThickness;
	}
}
