package at.tyron.vintagecraft.block;

import java.util.List;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumTree;

public class BlockSingleWoodenSlab extends BlockWoodenSlabVC {
	
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
    	for (EnumTree tree : EnumTree.values()) {
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
	public BlockClass getBlockClass() {
		return BlocksVC.singleslab;
	}


	@Override
	public boolean isDouble() {
		return false;
	}

}
