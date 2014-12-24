package at.tyron.vintagecraft.item;

import java.util.List;

import at.tyron.vintagecraft.World.EnumOrganicLayer;
import at.tyron.vintagecraft.World.EnumRockType;
import at.tyron.vintagecraft.block.VCBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemTopSoil extends ItemBlock {

	public ItemTopSoil(Block block) {
		super(block);
		setHasSubtypes(true);
	}

	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add(StatCollector.translateToLocal(EnumOrganicLayer.byMetadata(itemstack.getItemDamage()).getName() + ".name"));
	}
	
}
