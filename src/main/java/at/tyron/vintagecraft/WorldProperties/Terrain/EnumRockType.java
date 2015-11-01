package at.tyron.vintagecraft.WorldProperties.Terrain;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import at.tyron.vintagecraft.Interfaces.IGenLayerSupplier;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import net.minecraft.block.Block;
import net.minecraft.util.IStringSerializable;

public enum EnumRockType implements IStringSerializable, IStateEnum, IGenLayerSupplier {
	/*
	 * SANDSTONE => QUARTZITE
	 * LIMESTONE or CHALK => MARBLE	
	 * CONGLOMERATE => GNEISS
	 * SHALE => SLATE
	 * CHERT => CHERT
	 * CLAYSTONE => SCHIST
	 * 
	 */
	
	SANDSTONE		(0, 40, "sandstone", EnumRockGroup.SEDIMENTARY),
	LIMESTONE		(1, 5, "limestone", EnumRockGroup.SEDIMENTARY),
	CLAYSTONE		(2, 30, "claystone", EnumRockGroup.SEDIMENTARY),
	CONGLOMERATE	(3, 50, "conglomerate", EnumRockGroup.SEDIMENTARY),
	SHALE			(4, 50, "shale", EnumRockGroup.SEDIMENTARY),
	
    SCHIST			(5, 50, "schist", EnumRockGroup.METAMORPHIC),
    GNEISS			(6, 50, "gneiss", EnumRockGroup.METAMORPHIC),
    MARBLE			(7, 20, "marble", EnumRockGroup.METAMORPHIC),
    QUARTZITE		(8, 50, "quartzite", EnumRockGroup.METAMORPHIC),
    SLATE			(18,50, "slate", EnumRockGroup.METAMORPHIC),
	
	GRANITE			(9, 80, "granite", EnumRockGroup.IGNEOUS_INTRUSIVE),
	DIORITE			(10, 50, "diorite", EnumRockGroup.IGNEOUS_INTRUSIVE),
	
	BASALT			(11, 20, "basalt", EnumRockGroup.IGNEOUS_EXTRUSIVE),
	ANDESITE		(12, 80, "andesite", EnumRockGroup.IGNEOUS_EXTRUSIVE),

	CHERT			(13, 50, "chert", EnumRockGroup.METAMORPHIC), // technically SEDIMENTARY, but for gameplay sake metamorphic

	GABBRO			(14, 50, "gabbro", EnumRockGroup.IGNEOUS_INTRUSIVE),

	REDSANDSTONE	(15, 20, "redsandstone", EnumRockGroup.SEDIMENTARY),
	
	KIMBERLITE		(16, 2, "kimberlite",  EnumRockGroup.SPECIAL),
	CHALK			(17, 40, "chalk", EnumRockGroup.SEDIMENTARY)
    ;
    
    public int id; // used for GenLayerRockInit

    public int weight;
    public String name;
    public String unlocalizedName;
    public EnumRockGroup group;
    
	private static HashMap<EnumCrustLayer, List<EnumRockType>> CRUSTLAYER_ROCKTYPES = new HashMap<EnumCrustLayer, List<EnumRockType>>(EnumCrustLayer.values().length);
    private static EnumRockType[] ID_LOOKUP = new EnumRockType[values().length+1];


    
    private EnumRockType(int id, int weight, String name, EnumRockGroup group) {
        this(id, weight, name, name, group);
    }

    private EnumRockType(int id, int weight, String name, String unlocalizedName, EnumRockGroup group) {
    	this.id = id;
        this.name = name;
        this.unlocalizedName = unlocalizedName;
        this.group = group;
        this.weight = weight;
    }
    
    
    public EnumRockType randomRockTypeByLayer(EnumRockGroup layer) {
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
    	return CRUSTLAYER_ROCKTYPES.get(layer).toArray(new EnumRockType[0]);
    }


    static {
    	EnumRockType[] rocktypes = values();

        for (int i = 0; i < rocktypes.length; ++i) {
        	EnumRockType rocktype = rocktypes[i];
            ID_LOOKUP[rocktype.id] = rocktype;
        }
  
        
    }
    
    
    public EnumRockGroup getRockGroup() {
    	return group;
    }

    public float getHardnessMultiplier() {
    	return group.getHardNess();
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

	public String getNameUcFirst() {
		return name().toUpperCase(Locale.ROOT).substring(0, 1) + name().toLowerCase(Locale.ROOT).substring(1);
	}

	
    
    public static EnumRockType getSedimentary(EnumRockType rocktype, int age, int depth, Random rand) {
    	/*if (age > 35 || (depth < 4 && age > 30 && rand.nextInt(37 - age) == 0)) {
    		return SANDSTONE;
    	}*/
    	
    	switch (rocktype) {
    		case QUARTZITE:
    			return REDSANDSTONE;
    			
    		case MARBLE:
    			if (age > 70) 
    				return LIMESTONE;
    			return CHALK;
    			
    		case GNEISS:
    			return CONGLOMERATE;
    		
    		case SCHIST:
    			return CLAYSTONE;
    			
			default:
				return rocktype;
    		
    		case SLATE:
    			return SHALE;
    	}
    }
    
}
