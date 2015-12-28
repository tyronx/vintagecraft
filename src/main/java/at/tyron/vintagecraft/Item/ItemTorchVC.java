package at.tyron.vintagecraft.Item;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.Block.IBlockIgniteable;
import at.tyron.vintagecraft.Interfaces.Item.IItemRackable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTorchVC extends ItemBlockVC implements IItemRackable {

	public ItemTorchVC(Block block) {
		super(block);
	}

	@Override
    public boolean doesSneakBypassUse(World world, BlockPos pos, EntityPlayer player)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack)
    {
        return true;
    }

	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof IBlockIgniteable) {
			((IBlockIgniteable)state.getBlock()).ignite(world, pos, itemstack);
			itemstack.damageItem(1, entityplayer);
			return true;
		}

		return super.onItemUse(itemstack, entityplayer, world, pos, side, hitX, hitY, hitZ);
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		
		return super.onItemUseFirst(itemstack, entityplayer, world, pos, side, hitX, hitY, hitZ);
	}
	
}
