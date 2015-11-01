package at.tyron.vintagecraft.Block.Terrafirma;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.WorldProperties.EnumObjectCategory;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;


// Covers bottom layer of the world

public class BlockUpperMantle extends BlockVC {

	public BlockUpperMantle(Material material) {
		super(material);
		
		this.setDefaultState(this.blockState.getBaseState());
		setCreativeTab(VintageCraft.terrainTab);
	}
	
	@Override
	public EnumObjectCategory getCategory() {
		return EnumObjectCategory.Terrafirma;
	}

	public BlockUpperMantle() {
		this(Material.rock);
	}


	@Override
	public int getHarvestLevelVC(IBlockState state) {
		return 9999;
	}
	
	@Override
	public int getHarvestLevel(IBlockState state) {
		return 9999;
	}

}
