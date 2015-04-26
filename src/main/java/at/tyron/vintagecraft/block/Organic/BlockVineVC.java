package at.tyron.vintagecraft.Block.Organic;

import at.tyron.vintagecraft.VintageCraft;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockVineVC extends BlockVine {
	
	
	public BlockVineVC() {
        super();
        // Doesn't grow naturally for now
        this.setTickRandomly(false);
        
        setCreativeTab(VintageCraft.floraTab);
	}
	
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}

}
