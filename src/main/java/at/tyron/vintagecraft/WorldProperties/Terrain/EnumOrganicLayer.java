package at.tyron.vintagecraft.WorldProperties.Terrain;

import java.util.Locale;

import at.tyron.vintagecraft.Interfaces.EnumStateImplementation;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import net.minecraft.block.Block;
import net.minecraft.util.IStringSerializable;

public enum EnumOrganicLayer implements IStringSerializable, IStateEnum {
	NOGRASS         (0,  -30,    0, 0),
	VERYSPARSEGRASS (1,  -25,   20, 3),
	SPARSEGRASS     (2,  -20,   40, 7),
	NORMALGRASS     (3,   -5,   70, 11), 
	//LushousGrass	(4,   12,  120, 11),
	
	;
	
	
	private static EnumOrganicLayer[] META_LOOKUP = new EnumOrganicLayer[values().length+1];
	private static EnumOrganicLayer[] LIGHT_LOOKUP = new EnumOrganicLayer[16];
	
	
	private static EnumOrganicLayer[] RAIN_LOOKUP = new EnumOrganicLayer[26];
	private static EnumOrganicLayer[] TEMP_LOOKUP = new EnumOrganicLayer[61];  // -30 till 30
	
	
	
	
	public int meta;
	public int minblocklight;
	public int mintemp;
	public int minrain;
	
	
	private EnumOrganicLayer(int meta, int mintemp, int minrain, int minblocklight) {
		this.meta = meta;
		this.mintemp = mintemp;
		this.minrain = minrain;
		this.minblocklight = minblocklight;
	}
	
	public EnumOrganicLayer adjustToEnviroment(int blocklight, int rainfall, int temperature) {
		
		if (blocklight < minblocklight || rainfall < minrain || temperature < mintemp) {
			return shrink();
		}
		
		EnumOrganicLayer next = grow();
		if (blocklight >= next.minblocklight && rainfall >= next.minrain && temperature >= next.mintemp) return next;
		
		
		return this;
	}
	
	
	EnumOrganicLayer shrink() {
		if (this != NOGRASS) return fromMeta(meta - 1);
		return this;
	}
	
	EnumOrganicLayer grow() {
		if (this != NORMALGRASS) return fromMeta(meta + 1);
		return this;
	}
	

	@Override
	public int getMetaData(Block block) {
		return meta;
	}


	@Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

	@Override
    public String getStateName() {
        return getName();
    }
    
	@Override
	public int getId() {
		return meta;
	}

	
	
    public static EnumOrganicLayer fromMeta(int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
        }

        return META_LOOKUP[meta];
    }
    
    
    
 
	
    public static EnumOrganicLayer fromBlockLight(int blocklight) {
		return LIGHT_LOOKUP[blocklight];
	}
    
    public static EnumOrganicLayer fromClimate(int rain, int temp) {
    	EnumOrganicLayer rainl = RAIN_LOOKUP[rain / 10];
    	EnumOrganicLayer templ = TEMP_LOOKUP[temp + 30];
    	//System.out.println((rain/10) + " / " + (temp+30) + " = " + rainl + "/" + templ);
    	if (rainl.meta > templ.meta) return templ;
    	return rainl;
    }
	
    

	private static EnumOrganicLayer _fromBlockLight(int blocklight) {
		EnumOrganicLayer result = NOGRASS;
		
		for (EnumOrganicLayer layer : EnumOrganicLayer.values()) {
			if (blocklight > layer.minblocklight) result = layer;
		}
		return result;
	}
	

    
    
    
    static {
    	EnumOrganicLayer[] types = values();

        for (int i = 0; i < types.length; ++i) {
        	EnumOrganicLayer type = types[i];
            META_LOOKUP[type.meta] = type;      
        }
        
        for (int i = 0; i <= 15; i++) {
        	LIGHT_LOOKUP[i] = _fromBlockLight(i);
        }
        
        EnumOrganicLayer layer = NOGRASS, nextlayer = NOGRASS;
        for (int temp = 0; temp <= 60; temp++) {
        	if (layer != NORMALGRASS) nextlayer = fromMeta(layer.meta + 1);
        	if (nextlayer.mintemp <= temp-30) layer = nextlayer;
        	//System.out.println("TEMP_LOOKUP["+temp+"] = "+layer);
        	TEMP_LOOKUP[temp] = layer;
        }
        
        
        layer = NOGRASS;
        nextlayer = NOGRASS;
        for (int rain = 0; rain <= 25; rain++) {
        	if (layer != NORMALGRASS) nextlayer = fromMeta(layer.meta + 1);
        	if (nextlayer.minrain/10 <= rain) layer = nextlayer;
        	//System.out.println("RAIN_LOOKUP["+rain+"] = "+layer);
        	RAIN_LOOKUP[rain] = layer;
        }
        
    }





	public static IStateEnum[] valuesWithFertilityForTopsoil() {
		EnumFertility fertilities[] = EnumFertility.valuesForTopsoil();
		
		IStateEnum[] results = new EnumStateImplementation[values().length * fertilities.length];
		int i = 0;
		for (EnumOrganicLayer organiclayer : values()) {
			for (EnumFertility fertility : fertilities) {
				results[i++] = new EnumStateImplementation(organiclayer.getId(), organiclayer.meta + (fertility.meta << 2), fertility.getStateName() + "_" + organiclayer.getName());
			}
		}
		return results;
	}

	@Override
	public void init(Block block, int meta) {
		
	}


        
    
}