package at.tyron.vintagecraft.item;

import java.util.List;

import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumFurnace;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.block.BlockCobblestone;
import at.tyron.vintagecraft.block.BlockDoubleWoodenSlab;
import at.tyron.vintagecraft.block.BlockFenceGateVC;
import at.tyron.vintagecraft.block.BlockFenceVC;
import at.tyron.vintagecraft.block.BlockGravelVC;
import at.tyron.vintagecraft.block.BlockLeavesBranchy;
import at.tyron.vintagecraft.block.BlockLeavesVC;
import at.tyron.vintagecraft.block.BlockRegolith;
import at.tyron.vintagecraft.block.BlockSandVC;
import at.tyron.vintagecraft.block.BlockSaplingVC;
import at.tyron.vintagecraft.block.BlockSingleWoodenSlab;
import at.tyron.vintagecraft.block.BlockStairsVC;
import at.tyron.vintagecraft.block.BlockSubSoil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemRockTyped extends ItemBlock {

	public ItemRockTyped(Block block) {
		super(block);
		setHasSubtypes(true);
	}
	
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		//return getBlockClass(((ItemBlock)stack.getItem()).block).getName()  + "." + getRockType(stack).getStateName();
		return getBlockClass(((ItemBlock)stack.getItem()).block).getName();
	}


	
	public static EnumRockType getRockType(ItemStack itemstack) {
		Block block = ((ItemBlock)itemstack.getItem()).block;
		return (EnumRockType) getBlockClass(block).getBlockClassfromMeta(block, itemstack.getItemDamage()).getKey();
	}

	public static BlockClass getBlockClass(Block block) {
		if (block instanceof BlockRegolith) return BlocksVC.regolith;
		if (block instanceof BlockSubSoil) return BlocksVC.subsoil;
		if (block instanceof BlockCobblestone) return BlocksVC.cobblestone;
		if (block instanceof BlockGravelVC) return BlocksVC.gravel;
		if (block instanceof BlockSandVC) return BlocksVC.sand;
		
		// Workaround for Java being too fail to allow overriding static methods
		return null;
	}
	
	
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add(StatCollector.translateToLocal(BlocksVC.rock.getName() + "." + getRockType(itemstack).getUnlocalizedName() + ".name"));	
	}
	

}
