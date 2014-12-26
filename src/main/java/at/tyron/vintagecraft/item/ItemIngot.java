package at.tyron.vintagecraft.item;

import java.util.List;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
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

public class ItemIngot extends ItemVC {

	public ItemIngot() {
        this.setHasSubtypes(true);
        setCreativeTab(CreativeTabs.tabMaterials);

	}
	
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
    	ItemStack stack;
    	for (EnumMetal deposit : EnumMetal.values()) {
    		stack = new ItemStack(ItemsVC.ore);
    		setMetal(stack, deposit);
    		subItems.add(stack);
    	}
    }

	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (getMetal(stack) == null) {
			return super.getUnlocalizedName() + ".unknown";
		}
		return super.getUnlocalizedName() + "." + getMetal(stack).getName();
	}
	
	
	public static EnumMaterialDeposit getMetal(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null) {
			return EnumMaterialDeposit.byId(itemstack.getTagCompound().getInteger("metal"));
		}
		return null;
	}
	
	
	public static void setMetal(ItemStack itemstack, EnumMetal metal) {
		NBTTagCompound nbt = itemstack.getTagCompound(); 
		if (nbt == null) {
			itemstack.setTagCompound(nbt = new NBTTagCompound());
		}	
		
		nbt.setInteger("metal", metal.id);
		itemstack.setTagCompound(nbt);
	}

	
	
}
