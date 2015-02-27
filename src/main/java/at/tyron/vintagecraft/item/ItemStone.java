package at.tyron.vintagecraft.item;

import java.util.List;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumFurnace;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
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

public class ItemStone extends ItemVC implements ISubtypeFromStackPovider, ISmeltable {

	public ItemStone() {
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.tabMaterials);
	}
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
    	ItemStack stack;
    	for (EnumRockType rocktype : EnumRockType.values()) {
    		stack = new ItemStack(ItemsVC.stone);
    		setRockType(stack, rocktype);
    		subItems.add(stack);
    		
    	}
    }

	
	
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add(StatCollector.translateToLocal(BlocksVC.rock.getUnlocalizedName() + "." + ItemStone.getRockType(itemstack) + ".name"));
		if (getMeltingPoint(itemstack) > 0) {
			tooltip.add("Melting Point: " + getMeltingPoint(itemstack) + " °C");
		}

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
		return EnumRockType.byMetadata(itemstack.getItemDamage());
	}
	
	public static void setRockType(ItemStack itemstack, EnumRockType rocktype) {
		itemstack.setItemDamage(rocktype.meta);
	}

	@Override
	public String getSubType(ItemStack stack) {
		return getRockType(stack).getName();
	}

	@Override
	public ItemStack getSmelted(ItemStack raw) {
		EnumRockType rocktype = getRockType(raw);
		
		return new ItemStack(BlocksVC.rock, 1, rocktype.meta);
	}

	@Override
	public int getRaw2SmeltedRatio(ItemStack raw) {
		return 4;
	}

	@Override
	public int getMeltingPoint(ItemStack raw) {
		return 600;
	}
	
}
