package at.tyron.vintagecraft.item;

import java.util.List;

import at.tyron.vintagecraft.BlockClass.TreeClass;
import at.tyron.vintagecraft.WorldProperties.EnumFurnace;
import at.tyron.vintagecraft.block.BlockVC;
import at.tyron.vintagecraft.interfaces.ISubtypeFromStackPovider;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemPlanksVC extends ItemLogVC implements ISubtypeFromStackPovider {
	
	public ItemPlanksVC(Block block) {
		super(block);
        this.setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (getTreeType(stack) == null) {
			return super.getUnlocalizedName() + ".unknown";
		}
		
		return super.getUnlocalizedName() + "." + getTreeType(stack).getStateName();
	}
	
	
	@Override
	public int getBurningHeat(ItemStack stack) {
		return 900;
	}

	@Override
	public float getBurnDurationMultiplier(ItemStack stack) {
		return 0.25f;
	}	
}
