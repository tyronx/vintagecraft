package at.tyron.vintagecraft.Item;

import java.util.List;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.Block.BlockIngotPile;
import at.tyron.vintagecraft.Interfaces.ISmeltable;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.TileEntity.TEIngotPile;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumStrongHeatSource;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStone extends ItemVC implements ISubtypeFromStackPovider {

	public ItemStone() {
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.tabMaterials);
	}
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
    	for (EnumRockType rocktype : EnumRockType.values()) {
    		subItems.add(setRockType(new ItemStack(ItemsVC.stone), rocktype));
    	}
    }

	
	
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add(StatCollector.translateToLocal("rock." + ItemStone.getRockType(itemstack) + ".name"));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return "stone";
	}
	
	
	@Override
	public int getMetadata(ItemStack stack) {
		if (stack.getTagCompound() != null) {
			return stack.getTagCompound().getInteger("rocktype");
		}
		return 0;
	}
	
	public static EnumRockType getRockType(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null) {
			return EnumRockType.byId(itemstack.getTagCompound().getInteger("rocktype"));
		}
		return null;
	}
	
	public static ItemStack setRockType(ItemStack itemstack, EnumRockType rocktype) {
		NBTTagCompound nbt = itemstack.getTagCompound(); 
		if (nbt == null) {
			itemstack.setTagCompound(nbt = new NBTTagCompound());
		}	
		
		nbt.setInteger("rocktype", rocktype.id);
		itemstack.setTagCompound(nbt);
		return itemstack;
	}

	

	@Override
	public String getSubType(ItemStack stack) {
		return getRockType(stack).getName();
	}
	


	public boolean firepitPlaceable(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos pos, EnumFacing side) {

		return
			entityplayer.isSneaking() &&
			itemstack.stackSize >= 3 &&
			side == EnumFacing.UP &&
			world.isAirBlock(pos.offset(side)) &&
			world.isSideSolid(pos, EnumFacing.UP)
		;
	}
	
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		
		if (!firepitPlaceable(itemstack, entityplayer, world, pos, side)) return false;
		
		world.setBlockState(pos.offset(side), BlocksVC.firepit.getDefaultState());
		itemstack.stackSize -=3;
		
		return true;
	}



}
