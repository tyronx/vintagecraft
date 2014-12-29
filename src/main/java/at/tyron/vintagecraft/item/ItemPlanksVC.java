package at.tyron.vintagecraft.item;

import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.interfaces.ISubtypeFromStackPovider;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemPlanksVC extends ItemBlock implements ISubtypeFromStackPovider {
	
	public ItemPlanksVC(Block block) {
		super(block);
        this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (getTreeType(stack) == null) {
			return super.getUnlocalizedName() + ".unknown";
		}
		
		return super.getUnlocalizedName() + "." + getTreeType(stack).getName();
	}
	
	
	public static void setTreeType(ItemStack itemstack, EnumTree treetype) {
		NBTTagCompound nbt = itemstack.getTagCompound(); 
		if (nbt == null) {
			itemstack.setTagCompound(nbt = new NBTTagCompound());
		}	
		
		nbt.setInteger("treetype", treetype.id);
		itemstack.setTagCompound(nbt);
	}

	
	
	public static EnumTree getTreeType(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null) {
			return EnumTree.byId(itemstack.getTagCompound().getInteger("treetype"));
		}
		return null;
	}

	@Override
	public String getSubType(ItemStack stack) {
		return getTreeType(stack).getName();
	}
}
