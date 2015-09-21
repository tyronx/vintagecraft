package at.tyron.vintagecraft.Block;

import at.tyron.vintagecraft.VintageCraft;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;


// Covers bottom layer of the world

public class BlockUpperMantle extends BlockVC {

	public BlockUpperMantle(Material material) {
		super(material);
		
		this.setDefaultState(this.blockState.getBaseState());
		setCreativeTab(VintageCraft.terrainTab);
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
