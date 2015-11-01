package at.tyron.vintagecraft.Block.Carpentry;

import java.util.List;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.Flora.BlockLogVC;
import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.Item.Carpentry.ItemPlanksVC;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPlanksVC extends BlockLogVC {
	
	
	public BlockPlanksVC() {
		setCreativeTab(VintageCraft.craftedBlocksTab);
	}
	
	@Override
	public boolean canSustainLeaves(IBlockAccess world, BlockPos pos) {
		return false;
	}
	
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
    	for (EnumTree tree : EnumTree.values()) {
    		if (tree.isBush) continue;
    		
    		list.add(BlocksVC.planks.getItemStackFor(tree));
    	}
    }
	@Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
    	
    	ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));
        ItemPlanksVC.withTreeType(itemstack, getTreeType(state));
        ret.add(itemstack);
        
    	return ret;
    }
	

	@Override
	public BaseBlockClass getBlockClass() {
		return BlocksVC.planks;
	}

	@Override
	public String getUnlocalizedName() {
		return "tile.planks";
	}

}
