package at.tyron.vintagecraft.Item;

import java.util.List;

import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.Block.Organic.BlockDoubleFlowerVC;
import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.TreeClass;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumStrongHeatSource;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFlower;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemFlowerVC extends ItemBlockVC {

	public ItemFlowerVC(Block block) {
		super(block);
        this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (getFlowerType(stack) == null) {
			return "flower.unknown";
		}
		
		return "flower." + getFlowerType(stack).getName();
	}
	

	
	public static EnumFlower getFlowerType(ItemStack itemstack) {
		return (EnumFlower) getBlockClass(itemstack).getFromItemStack(itemstack).getKey();
	}

	
	
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	
	static BaseBlockClass getBlockClass(ItemStack itemstack) {
		ItemBlock itemblock = (ItemBlock)itemstack.getItem();
		
		if (itemblock.block instanceof BlockDoubleFlowerVC) {
			return BlocksVC.doubleflower;
		}
		
		return BlocksVC.flower;
	}

	

}
