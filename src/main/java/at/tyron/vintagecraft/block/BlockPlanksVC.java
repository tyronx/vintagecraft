package at.tyron.vintagecraft.block;

import java.util.List;

import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.item.ItemLogVC;
import at.tyron.vintagecraft.item.ItemPlanksVC;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPlanksVC extends BlockLogVC {
	
	@Override
	public boolean canSustainLeaves(IBlockAccess world, BlockPos pos) {
		return false;
	}
	
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
    	for (EnumTree tree : EnumTree.values()) {
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
	public BlockClass getBlockClass() {
		return BlocksVC.planks;
	}

	@Override
	public String getUnlocalizedName() {
		return "tile.planks";
	}

}
