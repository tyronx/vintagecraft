package at.tyron.vintagecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockRawClay extends VCBlock {


	public BlockRawClay() {
		super(Material.ground);
		isSoil = true;
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.clay_ball;
	}

	public int quantityDropped(Random random) {
	    return 4; //1 + random.nextInt(3);
	}
	    
}
