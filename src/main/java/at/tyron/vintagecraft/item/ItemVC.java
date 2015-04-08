package at.tyron.vintagecraft.Item;

import at.tyron.vintagecraft.Block.Utility.BlockFirepit;
import at.tyron.vintagecraft.Interfaces.IBlockItemSink;
import at.tyron.vintagecraft.TileEntity.TEHeatSourceWithGUI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class ItemVC extends Item {

	
	public Item register(String internalname) {
		setUnlocalizedName(internalname);
		GameRegistry.registerItem(this, internalname);
		return this;
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
