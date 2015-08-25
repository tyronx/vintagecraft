package at.tyron.vintagecraft.Item;

import java.util.List;

import at.tyron.vintagecraft.Block.Organic.BlockLeavesBranchy;
import at.tyron.vintagecraft.Block.Organic.BlockLeavesVC;
import at.tyron.vintagecraft.Block.Organic.BlockPlanksVC;
import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.Interfaces.IItemFuel;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumStrongHeatSource;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemLogVC extends ItemBlockVC implements ISubtypeFromStackPovider, IItemFuel {

	public ItemLogVC(Block block) {
		super(block);
        this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + getTreeType(stack).getStateName();
	}
	
	
	public static ItemStack withTreeType(ItemStack itemstack, BlockClassEntry treetype) {
		itemstack.setItemDamage(treetype.metadata);
		return itemstack;
	}
	
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	
	
	public static EnumTree getTreeType(ItemStack itemstack) {
		Block block = ((ItemBlock)itemstack.getItem()).block;
		return (EnumTree) getBlockClass(block).getEntryFromMeta(block, itemstack.getItemDamage()).getKey();
	}

	@Override
	public String getSubType(ItemStack stack) {
		if (getTreeType(stack) == null) return EnumTree.ASH.getStateName();
		return getTreeType(stack).getStateName();
	}
	
	

	@Override
	public int getBurningHeat(ItemStack stack) {
		return 800;
	}

	
	@Override
	public float getBurnDurationMultiplier(ItemStack stack) {
		EnumTree treetype = getTreeType(stack);
		return 1f + Math.min(2f, treetype.jankahardness / 750f);
	}
	
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		EnumStrongHeatSource.addItemStackInformation(itemstack, tooltip);
		EnumTree treetype = getTreeType(itemstack);
		if (treetype.jankahardness > 0) {
			tooltip.add("Janka Hardness: " + treetype.jankahardness + " lbf");
		}
	}
	
	
	public static BaseBlockClass getBlockClass(Block block) {
		// Workaround for Java being too fail to allow overriding static methods
		if (block instanceof BlockLeavesBranchy) return BlocksVC.leavesbranchy;
		if (block instanceof BlockLeavesVC) return BlocksVC.leaves;
		if (block instanceof BlockPlanksVC) return BlocksVC.planks;
		
		return BlocksVC.log;
	}

	@Override
	public boolean isMetalWorkingFuel(ItemStack stack) {
		return false;
	}

	@Override
	public int smokeLevel(ItemStack stack) {
		return 300;
	}
	
	@Override
	public ItemStack getCokedOutput(ItemStack stack) {
		return ItemOreVC.getItemStackFor(EnumOreType.COKE, stack.stackSize / 8);
	}

}

