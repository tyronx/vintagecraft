package at.tyron.vintagecraft.WorldProperties.Terrain;

import java.util.Locale;

import at.tyron.vintagecraft.Interfaces.IStateEnum;
import net.minecraft.block.Block;
import net.minecraft.util.IStringSerializable;

public enum EnumTallGrass implements IStateEnum, IStringSerializable {
	VERYSHORT 				(0, 30, 0.1f),
	SHORT 					(1, 60, 0.2f),
	MEDIUM 					(2, 90, 0.4f),
	LONG 					(3, 120, 0.8f),
	VERYLONG 				(4, 150, 1f),
	VERYLONG_FLOWERING 		(5, 180, 1f),
	
	FERN					(12, 190, 1f),
	FERN2					(13, 190, 1f),
	FERN3					(14, 190, 1f),
	
	VERYLONG_OXEYEDAISY 	(6, 160, 1f, 1),
	VERYLONG_CORNFLOWER 	(7, 160, 1f, 1),
	VERYLONG_CORNFLOWER2 	(8, 160, 1f, 1),
	
	VERYLONG_FORGETMENOT 	(9, 160, 1f, 1),
	VERYLONG_FORGETMENOT2 	(10, 160, 1f, 1), 
	VERYLONG_FORGETMENOT3 	(11, 160, 1f, 1),
	
	// Eaten short by a cow
	// Applies only for tallgrasses short, medium, long, verylong and verylong_flowering
	EATEN					(15, 500, 0, 0)
	
	;
	
	int id;
	int minfertility;
	int weight;
	public float drygrassDropChance;
	
	
	private static EnumTallGrass[] META_LOOKUP = new EnumTallGrass[values().length];
	private static EnumTallGrass[] FERTILITY_LOOKUP = new EnumTallGrass[26];
	
	
	private EnumTallGrass (int id, int minfertility, float dropchance) {
		this.id = id;
		this.minfertility = minfertility;
		this.weight = 100;
		this.drygrassDropChance = dropchance;
	}

	private EnumTallGrass (int id, int minfertility, float dropchance, int weight) {
		this.id = id;
		this.minfertility = minfertility;
		this.weight = weight;
		this.drygrassDropChance = dropchance;
	}

	
	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
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
