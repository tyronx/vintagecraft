package at.tyron.vintagecraft.item;

import java.util.List;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.interfaces.ISubtypeFromStackPovider;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemIngot extends ItemVC implements ISubtypeFromStackPovider {

	public ItemIngot() {
        this.setHasSubtypes(true);
        setCreativeTab(CreativeTabs.tabMaterials);
	}
	
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
    	ItemStack stack;
    	for (EnumMetal metal : EnumMetal.values()) {
    		stack = new ItemStack(ItemsVC.ingot);
    		setMetal(stack, metal);
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
	
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add("Melting Point: " + getMetal(itemstack).meltingpoint + " deg.");
		tooltip.add("Hardness: " + getMetal(itemstack).hardness);
	}
	
	
	
	
	public static EnumMetal getMetal(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null) {
			return EnumMetal.byId(itemstack.getTagCompound().getInteger("metal"));
		}
		return null;
	}
	
	
	public static ItemStack setMetal(ItemStack itemstack, EnumMetal metal) {
		NBTTagCompound nbt = itemstack.getTagCompound(); 
		if (nbt == null) {
			itemstack.setTagCompound(nbt = new NBTTagCompound());
		}	
		
		nbt.setInteger("metal", metal.id);
		itemstack.setTagCompound(nbt);
		return itemstack;
	}
	
	public static ItemStack getItemStack(EnumMetal metal, int quantity) {
		System.out.println(new ItemStack(ItemsVC.ingot, quantity).getItem());
		return setMetal(new ItemStack(ItemsVC.ingot, quantity), metal);
	}

	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if (worldIn.isRemote) {
			System.out.println("refresh resources");
			Minecraft.getMinecraft().refreshResources();
		}
		
		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}


	@Override
	public String getSubType(ItemStack stack) {
		return getMetal(stack).getName();
	}
	
	
}
