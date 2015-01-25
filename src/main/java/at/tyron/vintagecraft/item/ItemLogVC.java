package at.tyron.vintagecraft.item;

import java.util.List;

import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.TreeClass;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumFurnace;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.block.BlockVC;
import at.tyron.vintagecraft.interfaces.IFuel;
import at.tyron.vintagecraft.interfaces.ISubtypeFromStackPovider;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemLogVC extends ItemBlock implements ISubtypeFromStackPovider, IFuel {

	public ItemLogVC(Block block) {
		super(block);
        this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (getTreeType(stack) == null) {
			return super.getUnlocalizedName() + ".unknown";
		}
		
		return super.getUnlocalizedName() + "." + getTreeType(stack).getStateName();
	}
	
	
	public static ItemStack withTreeType(ItemStack itemstack, BlockClassEntry treetype) {
		NBTTagCompound nbt = itemstack.getTagCompound(); 
		if (nbt == null) {
			itemstack.setTagCompound(nbt = new NBTTagCompound());
		}	
		
		nbt.setInteger("treetype", treetype.getMetaData((BlockVC) ((ItemBlock)itemstack.getItem()).block));
		itemstack.setTagCompound(nbt);
		return itemstack;
	}

	
	
	public static EnumTree getTreeType(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null) {
			return (EnumTree) BlocksVC.log.getBlockClassfromMeta((BlockVC) ((ItemBlock)itemstack.getItem()).block, itemstack.getTagCompound().getInteger("treetype")).getKey();
		}
		return null;
	}

	@Override
	public String getSubType(ItemStack stack) {
		if (getTreeType(stack) == null) return EnumTree.ASH.getStateName();
		return getTreeType(stack).getStateName();
	}
	
	

	@Override
	public int getBurningHeat(ItemStack stack) {
		return 900;
	}

	@Override
	public float getBurnDurationMultiplier(ItemStack stack) {
		return 1f;
	}
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add("Heat produced when burned");
		for (EnumFurnace furnace : EnumFurnace.values()) {
			tooltip.add("  " + furnace.name + ": " + (int)(getBurningHeat(itemstack) * furnace.maxHeatModifier()) + " °C");	
		}
	}
	
	

}
