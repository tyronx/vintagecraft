package at.tyron.vintagecraft.block;

import java.util.Random;

import at.tyron.vintagecraft.World.ItemsVC;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockRawFireClay extends BlockVC {
	
	public BlockRawFireClay() {
		super(Material.ground);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}


	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return ItemsVC.fireclay_ball;
	}
	
	
	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}
	

	public int quantityDropped(Random random) {
	    return 1 + random.nextInt(3);
	}

}
