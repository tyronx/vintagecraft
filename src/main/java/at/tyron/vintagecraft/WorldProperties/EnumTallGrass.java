package at.tyron.vintagecraft.WorldProperties;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.IStringSerializable;
import at.tyron.vintagecraft.block.BlockVC;
import at.tyron.vintagecraft.interfaces.IEnumState;

public enum EnumTallGrass implements IEnumState, IStringSerializable {
	VERYSHORT 				(0, 30),
	SHORT 					(1, 60),
	MEDIUM 					(2, 90),
	LONG 					(3, 120),
	VERYLONG 				(4, 150),
	VERYLONG_FLOWERING 		(5, 180),
	
	FERN					(12, 190),
	FERN2					(13, 190),
	FERN3					(14, 190),
	
	VERYLONG_OXEYEDAISY 	(6, 160, 1),
	VERYLONG_CORNFLOWER 	(7, 160, 1),
	VERYLONG_CORNFLOWER2 	(8, 160, 1),
	
	VERYLONG_FORGETMENOT 	(9, 160, 1),
	VERYLONG_FORGETMENOT2 	(10, 160, 1), 
	VERYLONG_FORGETMENOT3 	(11, 160, 1)
	
	;
	
	int id;
	int minfertility;
	int weight;
	
	
	private static EnumTallGrass[] META_LOOKUP = new EnumTallGrass[values().length];
	private static EnumTallGrass[] FERTILITY_LOOKUP = new EnumTallGrass[26];
	
	
	private EnumTallGrass (int meta, int minfertility) {
		this.id = meta;
		this.minfertility = minfertility;
		this.weight = 100;
	}

	private EnumTallGrass (int meta, int minfertility, int weight) {
		this.id = meta;
		this.minfertility = minfertility;
		this.weight = weight;
	}

	
	@Override
	public String getName() {
		return name().toLowerCase();
	}

	@Override
	public int getMetaData(Block block) {
		return id;
	}

	@Override
	public String getStateName() {
		return getName();
	}


	public static EnumTallGrass fromMeta(int meta) {
		return META_LOOKUP[meta];
	}
	
	/*
	public static EnumTallGrass fromClimate(int fertility, Random rand) {
    	EnumTallGrass result = FERTILITY_LOOKUP[fertility / 10];
    	EnumTallGrass otherresult = result;
    	
    	if (result == null) return null;
    	int dist2Other = 0;
    	
    	if (result != VERYLONG_FLOWERING) {
    		otherresult = fromMeta(result.id + 1);
    		dist2Other = otherresult.minfertility - fertility;
    	}
    	
    	if (result.id > 0 && dist2Other >= 10) {
    		otherresult = fromMeta(result.id - 1);
    		dist2Other = fertility - (result.minfertility - 1); 
    	}
    	
		if (dist2Other < 10) {
			return rand.nextInt(1 + dist2Other) == 0 ? otherresult : result; 
		} else {
			return result;
		}
		
    }
	*/
	

	@Override
	public void init(Block block, int meta) {
		
	}
	

	 static {
		 EnumTallGrass[] types = values();
		 
		 for (int i = 0; i < types.length; ++i) {
			 EnumTallGrass type = types[i];
			 META_LOOKUP[type.id] = type;      
	     }

		 EnumTallGrass layer = null;
		 EnumTallGrass nextlayer = VERYSHORT;
		 for (int fertility = 0; fertility <= 25; fertility++) {
			 
			 if (layer != VERYLONG_FLOWERING) nextlayer = fromMeta(layer == null ? 0 : layer.id + 1);
			 if (nextlayer.minfertility/10 <= fertility) layer = nextlayer;

			 FERTILITY_LOOKUP[fertility] = layer;
		 }
		    
	}


	@Override
	public int getId() {
		return id;
	}
		

}
