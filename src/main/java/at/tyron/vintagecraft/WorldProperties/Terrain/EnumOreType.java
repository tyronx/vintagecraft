package at.tyron.vintagecraft.WorldProperties.Terrain;

import at.tyron.vintagecraft.Interfaces.IStateEnum;
import net.minecraft.block.Block;
import net.minecraft.util.IStringSerializable;

public enum EnumOreType implements IStringSerializable, IStateEnum {
	// ids must corespond to the ids in EnumMaterialDeposit
	
	LIGNITE 			( 2, 1),
	BITUMINOUSCOAL  	( 3, 1),
	
	NATIVECOPPER 		( 4, 1),
	LIMONITE 			( 5, 3),
	NATIVEGOLD_QUARTZ   ( 6, 3),
	
	REDSTONE 			( 7, 2),
	CASSITERITE 		( 8, 2),
	
	IRIDIUM 			( 9, 4),
	PLATINUM 			(10, 5),
	RHODIUM 			(11, 4),
	SPHALERITE 			(12, 2),
	SYLVITE_ROCKSALT 	(13, 1),
	NATIVESILVER_QUARTZ (14, 3),
	LAPISLAZULI 		(15, 3),
	DIAMOND 			(16, 3),
	EMERALD 			(17, 3),
	BISMUTHINITE 		(18, 2),
	QUARTZ 				(19, 3),
	ROCKSALT  			(20, 2), 
	OLIVINE 			(21, 3),
	PERIDOT_OLIVINE 	(22, 3)
	
	;
	
	int id;
	int harvestlevel;
	
	private EnumOreType(int id, int harvestlevel) {
		this.id = id;
		this.harvestlevel = harvestlevel;
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
	
}
