package at.tyron.vintagecraft.Item;

import java.util.List;

import at.tyron.vintagecraft.Block.BlockCobblestone;
import at.tyron.vintagecraft.Block.BlockGravelVC;
import at.tyron.vintagecraft.Block.BlockRegolith;
import at.tyron.vintagecraft.Block.BlockSandVC;
import at.tyron.vintagecraft.Block.Organic.BlockSubSoil;
import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
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
		return getBlockClass(((ItemBlock)stack.getItem()).block).getCommandSenderName();
	}


	
	public static EnumRockType getRockType(ItemStack itemstack) {
		Block block = ((ItemBlock)itemstack.getItem()).block;
		return (EnumRockType) getBlockClass(block).getEntryFromMeta(block, itemstack.getItemDamage()).getKey();
	}
	
	
	public static ItemStack withRockTypeType(ItemStack itemstack, BlockClassEntry rocktype) {
		itemstack.setItemDamage(rocktype.metadata);
		return itemstack;
	}

	

	// Workaround for Java being too fail to allow overriding static methods
	public static BaseBlockClass getBlockClass(Block block) {
		if (block instanceof BlockRegolith) return BlocksVC.regolith;
		if (block instanceof BlockSubSoil) return BlocksVC.subsoil;
		if (block instanceof BlockCobblestone) return BlocksVC.cobblestone;
		if (block instanceof BlockGravelVC) return BlocksVC.gravel;
		if (block instanceof BlockSandVC) return BlocksVC.sand;
		//if (block instanceof BlockStonePot) return BlocksVC.forge;
		return null;
	}
	
	
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add(StatCollector.translateToLocal(BlocksVC.rock.getCommandSenderName() + "." + getRockType(itemstack).getUnlocalizedName() + ".name"));	
	}
	

}
