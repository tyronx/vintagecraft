package at.tyron.vintagecraft.WorldProperties.Terrain;

import at.tyron.vintagecraft.Interfaces.IStateEnum;
import net.minecraft.block.Block;
import net.minecraft.util.IStringSerializable;

public enum EnumOreType implements IStringSerializable, IStateEnum {
	// ids must corespond to the ids in EnumMaterialDeposit
	
	LIGNITE 			( 2, 1, 0.5f),
	BITUMINOUSCOAL  	( 3, 1, 1f),
	
	NATIVECOPPER 		( 4, 1, 0.8f),
	LIMONITE 			( 5, 3, 1f),
	NATIVEGOLD_QUARTZ   ( 6, 2, 1f),
	
	REDSTONE 			( 7, 2, 1f),
	CASSITERITE 		( 8, 2, 1f),
	
	IRIDIUM 			( 9, 4, 1.1f),
	PLATINUM 			(10, 5, 1.3f),
	RHODIUM 			(11, 4, 1.2f),
	SPHALERITE 			(12, 2, 0.8f),
	SYLVITE_ROCKSALT 	(13, 2, 0.6f),
	NATIVESILVER_QUARTZ (14, 2, 1f),
	LAPISLAZULI 		(15, 2, 1f),
	DIAMOND 			(16, 3, 1.1f),
	EMERALD 			(17, 3, 1.1f),
	BISMUTHINITE 		(18, 2, 0.65f),
	QUARTZ 				(19, 3, 0.7f),
	ROCKSALT  			(20, 2, 0.5f), 
	OLIVINE 			(21, 2, 0.7f),
	PERIDOT_OLIVINE 	(22, 2, 0.7f),
	QUARTZCRYSTAL		(23, 3, 1f) 
	
	;
	
	int id;
	public int harvestlevel;
	public float hardnessmultiplier;
	
	private EnumOreType(int id, int harvestlevel, float hardnessmultiplier) {
		this.id = id;
		this.harvestlevel = harvestlevel;
		this.hardnessmultiplier = hardnessmultiplier;
	}
	
	
	public boolean isParentMaterial(EnumRockType rocktype) {
		if (rocktype == null) return false;
		
		switch (this) {
			case OLIVINE:
			case PERIDOT_OLIVINE:
				return rocktype == EnumRockType.BASALT;
				
			case QUARTZCRYSTAL:
				return false;
			
			case DIAMOND:
			case EMERALD:
				return rocktype == EnumRockType.KIMBERLITE;
			
			case LAPISLAZULI:
				return rocktype == EnumRockType.LIMESTONE;
				
			case REDSTONE:
				return false;
			
			default:
				return true;
		}
	}

	
	@Override
	public String getName() {
		return name().toLowerCase();
	}
	
	public int getHarvestlevel() {
		return harvestlevel;
	}

	@Override
	public int getMetaData(Block block) {
		return 0;
	}

	@Override
	public String getStateName() {
		return name().toLowerCase();
	}

	@Override
	public void init(Block block, int meta) {
		
	}

	@Override
	public int getId() {
		return id;
	}

	public static EnumOreType byId(int id) {
		for (EnumOreType ore : values()) {
			if (ore.id == id) return ore;
		}
		return null;
	}

	
	public static EnumOreType[] valuesSorted() {
		EnumOreType []values = values();
		EnumOreType []sorted = new EnumOreType[values.length];
		
		int id = -1;
		int remaining = values.length;
		int i = 0;
		
		while(remaining > 0) {
			for (EnumOreType value : values) {
				if (value.id == id) {
					sorted[i++] = value;
					remaining--;
					break;
				}
			}
			
			id++;
		}
		
		for (EnumOreType value : sorted) {
			System.out.println(value);
		}
		
		return sorted;
	}

}
