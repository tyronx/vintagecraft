package at.tyron.vintagecraft.WorldProperties.Terrain;

import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.util.IStringSerializable;
import at.tyron.vintagecraft.Interfaces.IStateEnum;

public enum EnumCrop implements IStateEnum, IStringSerializable {

	PEAS     (0, 9, 2, 0),
	WHEAT    (1, 8, 4, 0),
	TOMATOES (2, 13, 0, 2),
	FLAX 	 (3, 5, 2, 0)
	
	;
	
	
	int id;
	public int growthstages;
	public int quantitySeedsDroped;
	public int quantityFruitDropped;
	
	private EnumCrop(int id, int growthstages, int quantitySeedsDropped, int quantityFruitDropped) {
		this.id = id;
		this.growthstages = growthstages;
		this.quantitySeedsDroped = quantitySeedsDropped;
		this.quantityFruitDropped = quantityFruitDropped;
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
	
	public static EnumCrop byId(int id) {
		for (EnumCrop crop : values()) {
			if (crop.id == id) return crop;
		}
		return null;
	}
	
	
	
	
}
