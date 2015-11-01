package at.tyron.vintagecraft.Block.Flora;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.ICategorizedBlockOrItem;
import at.tyron.vintagecraft.WorldProperties.EnumObjectCategory;
import net.minecraft.block.BlockVine;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockVineVC extends BlockVine implements ICategorizedBlockOrItem {
	
	
	public BlockVineVC() {
        super();
        // Doesn't grow naturally for now
        this.setTickRandomly(false);
        
        setCreativeTab(VintageCraft.floraTab);
	}
	
	@Override
	public EnumObjectCategory getCategory() {
		return EnumObjectCategory.Flora;
	}

	
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}

}
