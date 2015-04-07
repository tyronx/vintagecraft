package at.tyron.vintagecraft.Item;

import java.util.List;

import at.tyron.vintagecraft.Block.BlockDoubleWoodenSlab;
import at.tyron.vintagecraft.Block.BlockFenceGateVC;
import at.tyron.vintagecraft.Block.BlockFenceVC;
import at.tyron.vintagecraft.Block.BlockLeavesBranchy;
import at.tyron.vintagecraft.Block.BlockLeavesVC;
import at.tyron.vintagecraft.Block.BlockPlanksVC;
import at.tyron.vintagecraft.Block.BlockSaplingVC;
import at.tyron.vintagecraft.Block.BlockSingleWoodenSlab;
import at.tyron.vintagecraft.Block.BlockStairsVC;
import at.tyron.vintagecraft.Block.BlockWoodenSlabVC;
import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.Interfaces.IFuel;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumStrongHeatSource;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemWoodtyped extends ItemBlock implements IFuel {

	public ItemWoodtyped(Block block) {
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
		EnumStrongHeatSource.addItemStackInformation(itemstack, tooltip);
	}


}

