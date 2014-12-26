package at.tyron.vintagecraft.block;

import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.item.ItemStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRegolith extends BlockRock {
	public BlockRegolith() {
		super(Material.ground);
	}


    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }
    
    
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	Random rand = world instanceof World ? ((World)world).rand : RANDOM;

    	if (rand.nextInt(5) == 0) {
    		List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
	   
	        ItemStack itemstack = new ItemStack(ItemsVC.stone, 1);
	        ItemStone.setRockType(itemstack, (EnumRockType) state.getValue(STONETYPE));
	        
	        ret.add(itemstack);
	        
    		return ret;
    	}
    	
    	return super.getDrops(world, pos, state, fortune);
    }
    
}
