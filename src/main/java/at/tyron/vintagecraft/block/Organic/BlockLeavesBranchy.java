package at.tyron.vintagecraft.Block.Organic;

import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLeavesBranchy extends BlockLeavesVC {

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return false;
    }


    @Override
    public java.util.List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	Random rand = world instanceof World ? ((World)world).rand : new Random();
        java.util.List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
        
        if (rand.nextFloat() < 0.7f) {
        	ret.add(new ItemStack(Items.stick, 1));
        }
        
        EnumTree tree = (EnumTree) ((BlockClassEntry)state.getValue(getTypeProperty())).getKey();
        
        if (rand.nextFloat() < tree.saplingdropchance) {
       		ret.add(BlocksVC.sapling.getItemStackFor(tree));
    	}


        return ret;
    }
    
    
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ);
    }
    
    


	@Override
	public BaseBlockClass getBlockClass() {
		return BlocksVC.leavesbranchy;
	}
	
    
    
}
