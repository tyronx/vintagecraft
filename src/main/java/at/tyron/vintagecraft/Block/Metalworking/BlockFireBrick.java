package at.tyron.vintagecraft.Block.Metalworking;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.WorldProperties.EnumObjectCategory;
import net.minecraft.block.material.Material;

public class BlockFireBrick extends BlockVC {

	public BlockFireBrick() {
		super(Material.rock);
		setCreativeTab(VintageCraft.craftedBlocksTab);
	}

	@Override
	public EnumObjectCategory getCategory() {
		return EnumObjectCategory.Metalworking;
	}

	
}
