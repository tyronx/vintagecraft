package at.tyron.vintagecraft.WorldProperties.Terrain;

import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.util.IStringSerializable;
import at.tyron.vintagecraft.Interfaces.IStateEnum;

public enum EnumCrop implements IStateEnum, IStringSerializable {

	PEAS     (0, 9),
	WHEAT    (1, 8),
	TOMATOES (2, 13)
	
	;
	
	
	int id;
	public int growthstages;
	
	private EnumCrop(int id, int growthstages) {
		this.id = id;
		this.growthstages = growthstages;
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	@Override
	public int getMetaData(Block block) {
		return 0;
	}

	@Override
	public String getStateName() {
		return name().toLowerCase(Locale.ROOT);
	}

	@Override
	public void init(Block block, int meta) {
	}

	@Override
	public int getId() {
		return id;
	}
	
	
	
	
}
