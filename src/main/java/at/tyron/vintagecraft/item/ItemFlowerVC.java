package at.tyron.vintagecraft.item;

import java.util.List;

import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.TreeClass;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumFlower;
import at.tyron.vintagecraft.WorldProperties.EnumFurnace;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.block.BlockVC;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemFlowerVC extends ItemBlock {

	public ItemFlowerVC(Block block) {
		super(block);
        this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (getFlowerType(stack) == null) {
			return super.getUnlocalizedName() + ".unknown";
		}
		
		return super.getUnlocalizedName() + "." + getFlowerType(stack).getName();
	}
	

	
	public static EnumFlower getFlowerType(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null) {
			return (EnumFlower) BlocksVC.flower.getBlockClassfromMeta((BlockVC) ((ItemBlock)itemstack.getItem()).block, itemstack.getTagCompound().getInteger("flowertype")).getKey();
		}
		return null;
	}
	
	
	public static ItemStack withFlowerType(ItemStack itemstack, BlockClassEntry flowertype) {
		NBTTagCompound nbt = itemstack.getTagCompound(); 
		if (nbt == null) {
			itemstack.setTagCompound(nbt = new NBTTagCompound());
		}	
		
		nbt.setInteger("flowertype", flowertype.getMetaData((BlockVC) ((ItemBlock)itemstack.getItem()).block));
		itemstack.setTagCompound(nbt);
		return itemstack;
	}

	
	
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	

	

}
