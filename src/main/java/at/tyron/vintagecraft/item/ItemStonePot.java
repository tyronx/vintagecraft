package at.tyron.vintagecraft.Item;

import java.util.List;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.TileEntity.TEStonePot;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemStonePot extends ItemBlockVC implements ISubtypeFromStackPovider {
	public ItemStonePot(Block block) {
		super(block);
		setHasSubtypes(true);
		setMaxStackSize(1);
	}
	
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add(StatCollector.translateToLocal("rock." + ItemStone.getRockType(stack) + ".name"));
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
	
	public static EnumRockType getRockType(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null) {
			return EnumRockType.byId(itemstack.getTagCompound().getInteger("rocktype"));
		}
		return EnumRockType.ANDESITE;
	}
	
	public static ItemStack setRockType(ItemStack itemstack, EnumRockType rocktype) {
		NBTTagCompound nbt = getOrCreateNBT(itemstack);
		
		nbt.setInteger("rocktype", rocktype.id);
		itemstack.setTagCompound(nbt);
		return itemstack;
	}
	
	
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (!world.setBlockState(pos, newState, 3)) return false;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block) {
            this.block.onBlockPlacedBy(world, pos, state, player, stack);
            TileEntity testonepot = world.getTileEntity(pos);
            
            if (testonepot instanceof TEStonePot) {
            	((TEStonePot) testonepot).rocktype = getRockType(stack);
            }
        }

        return true;
    }


	@Override
	public String getSubType(ItemStack stack) {
		return getRockType(stack).getName();
	}
}
