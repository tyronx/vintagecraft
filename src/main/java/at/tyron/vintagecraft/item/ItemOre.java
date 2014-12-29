package at.tyron.vintagecraft.item;

import java.util.List;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumFurnace;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.interfaces.IFuel;
import at.tyron.vintagecraft.interfaces.ISmeltable;
import at.tyron.vintagecraft.interfaces.ISubtypeFromStackPovider;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemOre extends ItemVC implements ISubtypeFromStackPovider, IFuel, ISmeltable {

	public ItemOre() {
        this.setHasSubtypes(true);
        setCreativeTab(CreativeTabs.tabMaterials);
	}
	
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
    	ItemStack stack;
    	for (EnumMaterialDeposit deposit : EnumMaterialDeposit.values()) {
    		if (deposit.hasOre) {
	    		stack = new ItemStack(ItemsVC.ore);
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


	@Override
	public String getSubType(ItemStack stack) {
		return getOreType(stack).getName();
	}


	@Override
	public int getBurningHeat(ItemStack stack) {
		if (getOreType(stack) == EnumMaterialDeposit.LIGNITE) {
			return 1100;
		}
		if (getOreType(stack) == EnumMaterialDeposit.BITUMINOUSCOAL) {
			return 1200;
		}
		return 0;
	}


	@Override
	public float getBurnDurationMultiplier(ItemStack stack) {
		if (getOreType(stack) == EnumMaterialDeposit.LIGNITE) {
			return 1.5f;
		}
		if (getOreType(stack) == EnumMaterialDeposit.BITUMINOUSCOAL) {
			return 2f;
		}
		return 0;
	}


	@Override
	public ItemStack getSmelted(ItemStack ore) {
		switch (getOreType(ore)) {
			case LIMONITE: return ItemIngot.setMetal(new ItemStack(ItemsVC.ingot), EnumMetal.IRON);
			case NATIVEGOLD: return ItemIngot.setMetal(new ItemStack(ItemsVC.ingot), EnumMetal.GOLD);
			case NATIVECOPPER: return ItemIngot.setMetal(new ItemStack(ItemsVC.ingot), EnumMetal.COPPER);
			
			default:
				break; 
		}
		return null;
	}


	@Override
	public int getRaw2SmeltedRatio(ItemStack ore) {
		switch (getOreType(ore)) {
			case LIMONITE:
			case NATIVECOPPER: return 4;
			case NATIVEGOLD: return 8;
			default: break;
		
		}
		return 0;
	}
	
	
	@Override
	public int getMeltingPoint(ItemStack ore) {
		switch (getOreType(ore)) {
			case LIMONITE: return EnumMetal.IRON.meltingpoint;
			case NATIVECOPPER: return EnumMetal.COPPER.meltingpoint;
			case NATIVEGOLD: return EnumMetal.GOLD.meltingpoint;
			default: break;
		}
		return 0;
	}
	
	
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		if (getBurningHeat(itemstack) > 0) {
			tooltip.add("Heat produced when burned");
			for (EnumFurnace furnace : EnumFurnace.values()) {
				tooltip.add("  " + furnace.name + ": " + (int)(getBurningHeat(itemstack) * furnace.maxHeatModifier()) + " °C");	
			}
			
		}
		
		if (getMeltingPoint(itemstack) > 0) {
			tooltip.add("Melting Point: " + getMeltingPoint(itemstack) + " °C");
		}
	}

		

	
	
}
