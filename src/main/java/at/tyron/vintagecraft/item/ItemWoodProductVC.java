package at.tyron.vintagecraft.item;

import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.block.BlockDoubleWoodenSlab;
import at.tyron.vintagecraft.block.BlockFenceGateVC;
import at.tyron.vintagecraft.block.BlockLeavesBranchy;
import at.tyron.vintagecraft.block.BlockLeavesVC;
import at.tyron.vintagecraft.block.BlockPlanksVC;
import at.tyron.vintagecraft.block.BlockSingleWoodenSlab;
import at.tyron.vintagecraft.block.BlockStairsVC;
import at.tyron.vintagecraft.block.BlockWoodenSlabVC;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemWoodProductVC extends ItemBlock {

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
		return super.getUnlocalizedName() + "." + getTreeType(stack).getStateName();
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

		// Workaround for Java being too fail to allow overriding static methods
		return BlocksVC.fence;
	}


}
