package at.tyron.vintagecraft.Block.Carpentry;

import java.util.List;

import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSingleWoodenSlab extends BlockWoodenSlabVC {
	
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
    	for (EnumTree tree : EnumTree.values()) {
    		if (tree.isBush) continue;
    		
    		list.add(BlocksVC.singleslab.getItemStackFor(tree));
    	}
    }

    public int damageDropped(IBlockState state) {
        return getMetaFromState(state) & 7;
    }


	@Override
	public int multistateAvailableTypes() {
		return 8;
	}
	
	@Override
	public BaseBlockClass getBlockClass() {
		return BlocksVC.singleslab;
	}


	@Override
	public boolean isDouble() {
		return false;
	}

}
