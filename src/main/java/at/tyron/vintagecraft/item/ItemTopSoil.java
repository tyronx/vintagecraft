package at.tyron.vintagecraft.item;

import java.util.List;

import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.*;
import at.tyron.vintagecraft.interfaces.ISubtypeFromStackPovider;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTopSoil extends ItemBlock implements ISubtypeFromStackPovider {

	public ItemTopSoil(Block block) {
		super(block);
		setHasSubtypes(true);
	}

	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add(StatCollector.translateToLocal(EnumFertility.fromMeta((itemstack.getItemDamage() >> 2) & 3).getStateName() + ".name"));
		tooltip.add(StatCollector.translateToLocal(EnumOrganicLayer.fromMeta(itemstack.getItemDamage() & 3).getStateName() + ".name"));
	}
	
	
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return BlocksVC.topsoil.getRenderColor(BlocksVC.topsoil.getStateFromMeta(stack.getMetadata() & 3));
    }


	@Override
	public String getSubType(ItemStack itemstack) {
		EnumFertility fertility = EnumFertility.fromMeta((itemstack.getItemDamage() >> 2) & 3);
		EnumOrganicLayer organiclayer = EnumOrganicLayer.fromMeta(itemstack.getItemDamage() & 3);
		
		return fertility.shortName() + "_" + organiclayer.getName();
	}
	
}
