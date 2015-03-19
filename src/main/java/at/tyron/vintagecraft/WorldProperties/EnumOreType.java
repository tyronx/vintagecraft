package at.tyron.vintagecraft.WorldProperties;

import at.tyron.vintagecraft.interfaces.IEnumState;
import net.minecraft.block.Block;
import net.minecraft.util.IStringSerializable;

public enum EnumOreType implements IStringSerializable, IEnumState {
	// ids must corespond to the ids in EnumMaterialDeposit
	
	LIGNITE (2),
	BITUMINOUSCOAL (3),
	
	NATIVECOPPER (4),
	LIMONITE (5),
	NATIVEGOLD_QUARTZ (6),
	
	REDSTONE (7),
	CASSITERITE (8),
	
	IRIDIUM (9),
	PLATINUM (10),
	RHODIUM (11),
	SPHALERITE (12),
	SYLVITE_ROCKSALT (13),
	NATIVESILVER_QUARTZ (14),
	LAPISLAZULI (15),
	DIAMOND (16),
	EMERALD (17),
	BISMUTHINITE (18),
	QUARTZ (19),
	ROCKSALT  (20), 
	OLIVINE (21),
	PERIDOT_OLIVINE (22)
	
	;
	
	int id;
	
	private EnumOreType(int id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name().toLowerCase();
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
	
}
