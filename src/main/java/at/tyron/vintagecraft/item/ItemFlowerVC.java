package at.tyron.vintagecraft.item;

import java.util.List;

import at.tyron.vintagecraft.WorldProperties.EnumFlower;
import at.tyron.vintagecraft.WorldProperties.EnumFurnace;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
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
		return EnumFlower.fromMeta(((ItemBlock)itemstack.getItem()).block, itemstack.getItemDamage());
	}
	
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	

	

}
