package at.tyron.vintagecraft.Block;

import java.util.Random;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.World.ItemsVC;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class BlockRawFireClay extends BlockVC {
	
	public BlockRawFireClay() {
		super(Material.ground);
		setCreativeTab(VintageCraft.terrainTab);
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
