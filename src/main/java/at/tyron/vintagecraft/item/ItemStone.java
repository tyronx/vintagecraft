package at.tyron.vintagecraft.item;

import java.util.List;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumFurnace;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.interfaces.ISmeltable;
import at.tyron.vintagecraft.interfaces.ISubtypeFromStackPovider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
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
	
	
	/*
	 *     public String getUnlocalizedName(ItemStack stack)
    {
        int i = stack.getMetadata();
        return super.getUnlocalizedName() + "." + EnumDyeColor.byDyeDamage(i).getUnlocalizedName();
    }
	 */
	
	
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
	
}
