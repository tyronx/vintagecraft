package at.tyron.vintagecraft.Item.Flora;

import at.tyron.vintagecraft.Block.Flora.BlockDoubleFlowerVC;
import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.Item.ItemBlockVC;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumObjectCategory;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFlower;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemFlowerVC extends ItemBlockVC {

	public ItemFlowerVC(Block block) {
		super(block);
        this.setHasSubtypes(true);
	}
	
	@Override
	public EnumObjectCategory getCategory() {
		return EnumObjectCategory.Flora;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (getFlowerType(stack) == null) {
			return "flower.unknown";
		}
		
		return "flower." + getFlowerType(stack).getName();
	}
	

	
	public static EnumFlower getFlowerType(ItemStack itemstack) {
		return (EnumFlower) getBlockClass(itemstack).getEntryFromItemStack(itemstack).getKey();
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
