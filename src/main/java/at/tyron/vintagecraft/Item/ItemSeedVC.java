package at.tyron.vintagecraft.Item;

import java.util.List;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.Organic.BlockCropsVC2;
import at.tyron.vintagecraft.Block.Organic.BlockFarmlandVC;
import at.tyron.vintagecraft.Interfaces.ISizedItem;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumItemSize;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumCrop;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemSeedVC extends ItemVC implements ISizedItem, ISubtypeFromStackPovider {
	public ItemSeedVC() {
		setCreativeTab(VintageCraft.resourcesTab);
		setHasSubtypes(true);
		setMaxStackSize(64);
	}
	
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item.seeds." + getSubType(stack);
	}
	
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		for (EnumCrop crop : EnumCrop.values()) {
			subItems.add(withCropType(crop));
		}
	}
	
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (side != EnumFacing.UP || !playerIn.canPlayerEdit(pos.offset(side), side, stack) || !worldIn.isAirBlock(pos.up())) {
            return false;
        }
        
        IBlockState state = worldIn.getBlockState(pos);
        
        IBlockState cropstate = BlocksVC.crops.getBlockStateFor(getCropType(stack), 0);
        
        if (((BlockCropsVC2)cropstate.getBlock()).suitableGround(worldIn, pos, state) && state.getBlock() instanceof BlockFarmlandVC) {
            worldIn.setBlockState(pos.up(), cropstate);
            --stack.stackSize;
            return true;
        }
        
        return false;
    }


	public EnumCrop getCropType(ItemStack stack) {
		NBTTagCompound nbt = getOrCreateNBT(stack);		
		return EnumCrop.byId(nbt.getInteger("croptype"));
	}
	
	public static ItemStack withCropType(EnumCrop croptype) {
		ItemStack stack = new ItemStack(ItemsVC.seeds);
		NBTTagCompound nbt = getOrCreateNBT(stack);
		nbt.setInteger("croptype", croptype.getId());
		return stack;
	}


	@Override
	public EnumItemSize getItemSize() {
		return EnumItemSize.TINY;
	}


	@Override
	public String getSubType(ItemStack stack) {
		return getCropType(stack).getName();
	}


}
