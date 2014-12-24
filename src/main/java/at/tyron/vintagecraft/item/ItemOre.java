package at.tyron.vintagecraft.item;

import java.util.List;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.World.EnumMaterialDeposit;
import at.tyron.vintagecraft.World.EnumRockType;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemOre extends ItemVC {

	public ItemOre() {
        this.setHasSubtypes(true);
        setCreativeTab(CreativeTabs.tabMaterials);

	}
	
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
    	ItemStack stack;
    	for (EnumMaterialDeposit deposit : EnumMaterialDeposit.values()) {
    		if (deposit.hasOre) {
	    		stack = new ItemStack(VCItems.ore);
	    		setOreType(stack, deposit);
	    		subItems.add(stack);
    		}
    	}
    }

	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (getOreType(stack) == null) {
			return super.getUnlocalizedName() + ".unknown";
		}
		return super.getUnlocalizedName() + "." + getOreType(stack).getName();
	}
	
	
	public static EnumMaterialDeposit getOreType(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null) {
			return EnumMaterialDeposit.byId(itemstack.getTagCompound().getInteger("oretype"));
		}
		return null;
	}
	
	public static void setOreType(ItemStack itemstack, EnumMaterialDeposit oretype) {
		NBTTagCompound nbt = itemstack.getTagCompound(); 
		if (nbt == null) {
			itemstack.setTagCompound(nbt = new NBTTagCompound());
		}	
		
		nbt.setInteger("oretype", oretype.id);
		itemstack.setTagCompound(nbt);
	}

	
	
}
