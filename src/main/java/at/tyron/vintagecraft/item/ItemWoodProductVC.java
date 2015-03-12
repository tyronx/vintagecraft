package at.tyron.vintagecraft.item;

import java.util.List;

import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumFurnace;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.block.BlockDoubleWoodenSlab;
import at.tyron.vintagecraft.block.BlockFenceGateVC;
import at.tyron.vintagecraft.block.BlockFenceVC;
import at.tyron.vintagecraft.block.BlockLeavesBranchy;
import at.tyron.vintagecraft.block.BlockLeavesVC;
import at.tyron.vintagecraft.block.BlockPlanksVC;
import at.tyron.vintagecraft.block.BlockSaplingVC;
import at.tyron.vintagecraft.block.BlockSingleWoodenSlab;
import at.tyron.vintagecraft.block.BlockStairsVC;
import at.tyron.vintagecraft.block.BlockWoodenSlabVC;
import at.tyron.vintagecraft.interfaces.IFuel;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemWoodProductVC extends ItemBlock implements IFuel {

	public ItemWoodProductVC(Block block) {
		super(block);
		this.setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getBlockClass(((ItemBlock)stack.getItem()).block).getName()  + "." + getTreeType(stack).getStateName();
	}


	
	public static EnumTree getTreeType(ItemStack itemstack) {
		Block block = ((ItemBlock)itemstack.getItem()).block;
		return (EnumTree) getBlockClass(block).getBlockClassfromMeta(block, itemstack.getItemDamage()).getKey();
	}

	public static BlockClass getBlockClass(Block block) {
		if (block instanceof BlockStairsVC) return BlocksVC.stairs;
		if (block instanceof BlockSingleWoodenSlab) return BlocksVC.singleslab;
		if (block instanceof BlockDoubleWoodenSlab) return BlocksVC.doubleslab;
		if (block instanceof BlockFenceGateVC) return BlocksVC.fencegate;
		if (block instanceof BlockFenceVC) return BlocksVC.fence;
		if (block instanceof BlockSaplingVC) return BlocksVC.sapling;
		if (block instanceof BlockLeavesBranchy) return BlocksVC.leavesbranchy;
		if (block instanceof BlockLeavesVC) return BlocksVC.leaves;
		

		// Workaround for Java being too fail to allow overriding static methods
		return BlocksVC.fence;
	}
	
	
	
	@Override
	public int getBurningHeat(ItemStack stack) {
		return 800;
	}

	@Override
	public float getBurnDurationMultiplier(ItemStack stack) {
		return 0.2f;
	}
	
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add("Heat produced when burned");
		for (EnumFurnace furnace : EnumFurnace.values()) {
			tooltip.add("  " + furnace.name + ": " + (int)(getBurningHeat(itemstack) * furnace.maxHeatModifier()) + " deg.");	
		}
	}


}

