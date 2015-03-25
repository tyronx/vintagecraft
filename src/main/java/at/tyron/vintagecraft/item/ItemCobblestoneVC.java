package at.tyron.vintagecraft.item;

import java.util.List;

import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemCobblestoneVC extends ItemRock {

	
	public ItemCobblestoneVC(Block block) {
		super(block);
	}

	public static BlockClass getBlockClass(Block block) {
		return BlocksVC.cobblestone;
	}

	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		ItemRock item = (ItemRock) itemstack.getItem();
		
		if (getBlockClass(item.getBlock()).containsBlock(item.getBlock())) {
			EnumRockType rocktype = getRockType(itemstack);
			tooltip.add(StatCollector.translateToLocal(BlocksVC.rock.getName() + "." + rocktype.getUnlocalizedName() + ".name"));
		}
	}


	public static EnumRockType getRockType(ItemStack itemstack) {
		Block block = ((ItemBlock)itemstack.getItem()).block;
		return (EnumRockType) getBlockClass(block).getBlockClassfromMeta(block, itemstack.getItemDamage()).getKey();
	}
}
