package at.tyron.vintagecraft.Item;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.IBlockIgniteable;
import at.tyron.vintagecraft.Interfaces.IItemRackable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemFireStarter extends ItemVC implements IItemRackable {

	public ItemFireStarter() {
		setMaxDamage(16);
		setMaxStackSize(1);
		setCreativeTab(VintageCraft.toolsarmorTab);
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
}
