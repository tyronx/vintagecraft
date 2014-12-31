package at.tyron.vintagecraft.item;

import java.util.List;

import at.tyron.vintagecraft.WorldProperties.EnumFurnace;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemLeaves extends ItemLogVC {

	public ItemLeaves(Block block) {
		super(block);
	}
	
	
	@Override
	public int getBurningHeat(ItemStack stack) {
		return 0;
	}

	@Override
	public float getBurnDurationMultiplier(ItemStack stack) {
		return 0;
	}
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		return;
	}
	

}
