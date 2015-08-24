package at.tyron.vintagecraft.BlockClass;

import at.tyron.vintagecraft.Interfaces.EnumStateImplementation;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.item.ItemBlock;

public class SoilRockClass extends RockClass {
	public SoilRockClass(String name, Class<? extends Block> blockclass, Class<? extends ItemBlock> itemclass, float hardness, SoundType stepsound, String harvesLevelTool, int harvestLevel) {
		super(name, blockclass, itemclass, hardness, stepsound, harvesLevelTool, harvestLevel);
	}
	
	@Override
	public SoilRockClass init() {
		int i = 0;
		
		for (EnumOrganicLayer organiclayer : EnumOrganicLayer.values()) {
			for (EnumRockType rocktype : EnumRockType.values()) {
				EnumStateImplementation key = new EnumStateImplementation(i++, 0, organiclayer.getName() + "-" + rocktype.getName());
				values.put(key, new SoilRockClassEntry(key, rocktype, organiclayer));
			}
		}
		
		initBlocks(getBlockClassName(), getBlockClass(), getItemClass(), getHardness(), getStepSound(), getHarvestTool(), getHarvestLevel());
		
		return this;
	}

}
