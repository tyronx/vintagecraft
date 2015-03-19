package at.tyron.vintagecraft.WorldProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import at.tyron.vintagecraft.BlockClass.RockClass;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.block.BlockRock;
import at.tyron.vintagecraft.block.BlockSandVC;
import at.tyron.vintagecraft.block.BlockVC;
import at.tyron.vintagecraft.interfaces.IEnumState;
import at.tyron.vintagecraft.interfaces.IGenLayerSupplier;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

public enum EnumRockType implements IStringSerializable, IEnumState, IGenLayerSupplier {
	/*
	 * SANDSTONE => QUARTZITE
	 * LIMESTONE or CHALK => MARBLE	
	 * CONGLOMERATE => GNEISS
	 * SHALE => SLATE
	 * CHERT => CHERT
	 * 
	 */
	
	SANDSTONE		(0, 40, "sandstone", EnumCrustLayerGroup.SEDIMENTARY),
	LIMESTONE		(1, 5, "limestone", EnumCrustLayerGroup.SEDIMENTARY),
	CLAYSTONE		(2, 30, "claystone", EnumCrustLayerGroup.SEDIMENTARY),
	CONGLOMERATE	(3, 50, "conglomerate", EnumCrustLayerGroup.SEDIMENTARY),
	SHALE			(4, 50, "shale", EnumCrustLayerGroup.SEDIMENTARY),
	
    SCHIST			(5, 50, "schist", EnumCrustLayerGroup.METAMORPHIC),
    GNEISS			(6, 50, "gneiss", EnumCrustLayerGroup.METAMORPHIC),
    MARBLE			(7, 20, "marble", EnumCrustLayerGroup.METAMORPHIC),
    QUARTZITE		(8, 50, "quartzite", EnumCrustLayerGroup.METAMORPHIC),
    SLATE			(18,50, "slate", EnumCrustLayerGroup.METAMORPHIC),
	
	GRANITE			(9, 80, "granite", EnumCrustLayerGroup.IGNEOUS_INTRUSIVE),
	DIORITE			(10, 50, "diorite", EnumCrustLayerGroup.IGNEOUS_INTRUSIVE),
	
	BASALT			(11, 20, "basalt", EnumCrustLayerGroup.IGNEOUS_EXTRUSIVE),
	ANDESITE		(12, 80, "andesite", EnumCrustLayerGroup.IGNEOUS_EXTRUSIVE),

	CHERT			(13, 50, "chert", EnumCrustLayerGroup.METAMORPHIC), // technically SEDIMENTARY

	GABBRO			(14, 50, "gabbro", EnumCrustLayerGroup.IGNEOUS_INTRUSIVE),

	REDSANDSTONE	(15, 20, "redsandstone", EnumCrustLayerGroup.SEDIMENTARY), //EnumCrustLayerGroup.SEDIMENTARY2),
	
	KIMBERLITE		(16, 2, "kimberlite",  EnumCrustLayerGroup.SPECIAL),
	CHALK			(17, 40, "chalk", EnumCrustLayerGroup.SEDIMENTARY)
    ;
    
	private static HashMap<EnumCrustLayer, List<EnumRockType>> CRUSTLAYER_ROCKTYPES = new HashMap<EnumCrustLayer, List<EnumRockType>>(EnumCrustLayer.values().length);
   // private static EnumRockType[] META_LOOKUP = new EnumRockType[values().length+1];
    private static EnumRockType[] ID_LOOKUP = new EnumRockType[values().length+1];
    
    
    public static EnumRockType getSedimentary(EnumRockType rocktype, int age) {
    	switch (rocktype) {
    		case QUARTZITE:
    			if (age < 70) 
    				return SANDSTONE;
    			return REDSANDSTONE;
    			
    		case MARBLE:
    			if (age > 70) 
    				return LIMESTONE;
    			return CHALK;
    			
    		case GNEISS:
    			return CONGLOMERATE;
    			
			default:
				return rocktype;
    		
    		case SLATE:
    			return SHALE;
    	}
    }
    
    
    public int id; // used for GenLayerRockInit
    //public int meta;
    public int weight;
    public String name;
    public String unlocalizedName;
    public EnumCrustLayerGroup group;
    

    private EnumRockType(int id, /*int meta,*/ int weight, String name, EnumCrustLayerGroup group) {
        this(id, /*meta,*/ weight, name, name, group);
    }

    private EnumRockType(int id,/* int meta,*/ int weight, String name, String unlocalizedName, EnumCrustLayerGroup group) {
    	this.id = id;
        //this.meta = meta;
        this.name = name;
        this.unlocalizedName = unlocalizedName;
        this.group = group;
        this.weight = weight;
    }
    
    
    public EnumRockType randomRockTypeByLayer(EnumCrustLayerGroup layer) {
		return null;
    	
    }
    


    public static int colorFactor() {
    	return 10;
    }
    
	@Override
	public int getId() {
		return id;
	}
	
    @Override
    public int getColor() {
    	return id * colorFactor();
    }
    
    public static int Color2Id(int color) {
    	return color / colorFactor();
    }
    
    public int getMetaData(Block block) {
        //return this.meta;
    	return 0;
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

  /*  public static EnumRockType byMetadata(int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
        }

        return META_LOOKUP[meta];
    }
    */
    
    public static EnumRockType byId(int id) {
        if (id < 0 || id >= ID_LOOKUP.length) {
            return null;
        }

        return ID_LOOKUP[id];
    }
    
    
    public static EnumRockType byColor(int color) {
    	return byId(Color2Id(color));
    }
    
    
    
    
    
    public static EnumRockType[] getRockTypesForCrustLayer(EnumCrustLayer layer) {
    //	System.out.println("get for " + layer);
    	return CRUSTLAYER_ROCKTYPES.get(layer).toArray(new EnumRockType[0]);
    }


    static {
    	EnumRockType[] rocktypes = values();

        for (int i = 0; i < rocktypes.length; ++i) {
        	EnumRockType rocktype = rocktypes[i];
          //  META_LOOKUP[rocktype.meta] = rocktype;      
            ID_LOOKUP[rocktype.id] = rocktype;
            
          /*  for (int j = 0; j < rocktype.group.crustlayers.length; j++) {
            	List<EnumRockType> crustrocktypes = CRUSTLAYER_ROCKTYPES.get(rocktype.group.crustlayers[j]);
            	if (crustrocktypes == null) crustrocktypes = new ArrayList<EnumRockType>();
            	
            	crustrocktypes.add(rocktype);
            	
            	CRUSTLAYER_ROCKTYPES.put(rocktype.group.crustlayers[j], crustrocktypes);
            }*/
        }
        
        
        

        
    }


	@Override
	public int getWeight() {
		return weight;
	}
	
	@Override
	public int getDepthMin() {
		return 0;// group.minThickness;
	}
	
	@Override
	public int getDepthMax() {
		return 0; // group.maxThickness;
	}

	@Override
	public void init(Block block, int meta) {
		
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}
}
