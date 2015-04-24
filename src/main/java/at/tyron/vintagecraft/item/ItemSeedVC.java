package at.tyron.vintagecraft.Item;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.Organic.BlockCropsVC;
import at.tyron.vintagecraft.Block.Organic.BlockFarmlandVC;
import at.tyron.vintagecraft.Interfaces.IBlockSoil;
import at.tyron.vintagecraft.Interfaces.ISizedItem;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumItemSize;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemSeedVC extends ItemVC implements ISizedItem {
	public ItemSeedVC() {
		setCreativeTab(VintageCraft.resourcesTab);
	}
	
	
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (side != EnumFacing.UP || !playerIn.canPlayerEdit(pos.offset(side), side, stack) || !worldIn.isAirBlock(pos.up())) {
            return false;
        } 
        
        IBlockState state = worldIn.getBlockState(pos);
         
        if (((BlockCropsVC)BlocksVC.wheatcrops).suiteableGround(worldIn, pos, state) && state.getBlock() instanceof BlockFarmlandVC) {
            worldIn.setBlockState(pos.up(), BlocksVC.wheatcrops.getDefaultState());
            --stack.stackSize;
            return true;
        }
        
        return false;
    }


	@Override
	public EnumItemSize getItemSize() {
		return EnumItemSize.TINY;
	}


}
