package at.tyron.vintagecraft.Item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemBrick extends ItemBlockVC {

	public ItemBrick(Block block) {
		super(block);
	}

	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		ItemBrick item = (ItemBrick) itemstack.getItem();
		
		tooltip.add(item.block.getLocalizedName());
	}

}
