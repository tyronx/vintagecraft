package at.tyron.vintagecraft.Item;

import at.tyron.vintagecraft.Interfaces.IBlockItemSink;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemBlockVC extends ItemBlock {
	
	public ItemBlockVC(Block block) {
		super(block);
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!entityplayer.canPlayerEdit(pos.offset(side), side, itemstack)) {
            return false;
        }
		
		IBlockState state = world.getBlockState(pos);
        
        if (state.getBlock() instanceof IBlockItemSink) {
    		if (((IBlockItemSink)state.getBlock()).tryPutItemstack(world, pos, entityplayer, side, itemstack)) {
    			return true;
    		} else {
    			return super.onItemUse(itemstack, entityplayer, world, pos, side, hitX, hitY, hitZ);
    		}
    	}
    	
		return super.onItemUse(itemstack, entityplayer, world, pos, side, hitX, hitY, hitZ);
	}

}
