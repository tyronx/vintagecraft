package at.tyron.vintagecraft.block;

import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;


// Covers bottom layer of the world

public class BlockUpperMantle extends BlockVC {

	public BlockUpperMantle(Material material) {
		super(material);
		
		this.setDefaultState(this.blockState.getBaseState());
        this.setCreativeTab(CreativeTabs.tabBlock);
	}
	

	public BlockUpperMantle() {
		this(Material.rock);
	}


	
}
