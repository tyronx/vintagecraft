package at.tyron.vintagecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLeavesBranchy extends BlockLeaves {

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return false;
    }


    @Override
    public java.util.List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	Random rand = world instanceof World ? ((World)world).rand : new Random();
    	
        java.util.List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
        
        int chance = this.getSaplingDropChance(state);

       /* if (rand.nextInt(chance) == 0) {
            ret.add(new ItemStack(getItemDropped(state, rand, fortune), 1, damageDropped(state)));
        }*/
        
        if (rand.nextInt(2) == 0) {
        	ret.add(new ItemStack(Items.stick, 1));
        }


        return ret;
    }
    
    
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ);
    }

}
