package at.tyron.vintagecraft.Block.Terrafirma;

import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.Item.Terrafirma.ItemStone;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGravelVC extends BlockSandVC {
	@Override
	public BaseBlockClass getBlockClass() {
		return BlocksVC.gravel;
	}

	
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	Random rand = world instanceof World ? ((World)world).rand : RANDOM;

    	if (rand.nextInt(6) == 0) {
    		List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
	   
    		EnumRockType rocktype = (EnumRockType) getRockType(state).getKey();
    		ItemStack itemstack;
    		
    		if ((rocktype == EnumRockType.LIMESTONE || rocktype == EnumRockType.CHALK) && rand.nextInt(3) == 0) {
    	        itemstack = new ItemStack(Items.flint, 1);    			
    		} else {
    	        itemstack = new ItemStack(ItemsVC.stone, 1 + rand.nextInt(2));
    	        ItemStone.setRockType(itemstack, rocktype);
    		}
    		
	        
	        ret.add(itemstack);
	        
    		return ret;
    	}
    	
    	return super.getDrops(world, pos, state, fortune);
    }
    
	
}
